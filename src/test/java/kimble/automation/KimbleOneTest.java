package kimble.automation;

import static kimble.automation.scenarios.Stages.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.naming.ConfigurationException;

import kimble.automation.domain.ExpectedValue;
import kimble.automation.domain.Fact;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.ResourceForecast;
import kimble.automation.domain.SalesOpportunityForecast;
import kimble.automation.domain.TestState;
import kimble.automation.domain.TrackingPlanTotal;
import kimble.automation.domain.UserCredentials;
import kimble.automation.helpers.CredentialsPool;
import kimble.automation.helpers.General;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.YamlHelper;
import kimble.automation.helpers.controlpanel.ControlPanel;
import kimble.automation.kimbleobjects.classic.AndroidSelectors;
import kimble.automation.kimbleobjects.classic.DeliveryEngagementPageC;
import kimble.automation.kimbleobjects.classic.IosSelectors;
import kimble.automation.kimbleobjects.classic.LoginPageC;
import kimble.automation.kimbleobjects.classic.MobileLoginPage;
import kimble.automation.kimbleobjects.classic.MobileSelectors;
import kimble.automation.kimbleobjects.classic.ResourceForecastPageC;
import kimble.automation.kimbleobjects.classic.ResourcePageC;
import kimble.automation.kimbleobjects.classic.SalesOppForecastPageC;
import kimble.automation.kimbleobjects.lightning.GeneralOperationsZ;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.TestResult;

import com.saucelabs.common.SauceOnDemandAuthentication;


//Mobile imports
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.AppiumDriver;
import org.json.JSONException;
import org.json.JSONObject;
//---------
public abstract class KimbleOneTest extends BaseTest {

	public static String appiumJobID = "";
	
	public static String[] mobileTestIDs = new String[50];
	
	public static enum CleardownType { none, data, datapreserveaccounts, dataandperiodreset };
	
	@DataProvider(name = "yamlDataProvider", parallel = true)
	public Object[][] getTestDataFromYamlInput(Method m) throws Exception {
		// the "testName" attribute of @Test is being used to pass in the name of the test
		// and this is tshen used for name of the yaml file we are loading for the test
		testName = m.getAnnotation(Test.class).testName();
		
		// two parameters to each test, the first is the login credentials (defaulted from the superUserLogin file but overridable in the test)
		// second is the test data itself
		Object[][] data = null;
		try {
			data = getTheYamlHelper().getYamlData(SeleniumHelper.config.credentialsSource, getTestName());
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return data;
	}
	
	/** 
	 * A large part of the configuration code that used to be in this method can now be found in the class TestInterceptor in method onStart()
	 * 
	 * @param aConfig
	 * @throws Exception
	 */
	public static void automationSuiteSetup(Config aConfig) throws Exception {
		failureCount = 0;
		SeleniumHelper.config = aConfig;
		
		setExecutionEnvironments(aConfig.targetEnvironments, aConfig.credentialsSource);
		setSeedDate(aConfig.seedDate);
		initialiseErrorCollection();
		
		// run any pre-test actions (such as datacleardown)
		if(aConfig.dataClearDownAction != CleardownType.none)
			initialiseOrgs();
	}
	
//	@BeforeTest
	public void initialiseBrowser() throws MalformedURLException, ConfigurationException, InterruptedException {
		// switch to either local or remote - remoteTest can be set manually in this file
		// remoteExecution is a boolean value which is optionally on the suite definition (in the TestNG.xml file)
		// e.g. <suite name="KimbleTestSuite" verbose="1" >
		//        <parameter name="remoteExecution" value="true" />		
		if(isRemoteTest()) {
			webDriverInstance = getRemoteWebDriver();
		} else {
			webDriverInstance = localChromeTest();
		}
	}

	private static WebDriver localChromeTest() {
		ChromeOptions options = getChromeOptions();
		
		// wire up the chromedriver location (relative file in the lib directory)
		File chromeDriverExe = FileUtils.getFile("lib", "chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", chromeDriverExe.getAbsolutePath());
		
		return new ChromeDriver(options);
	}
	
	private static ChromeOptions getChromeOptions() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		// supress the chrome message about ignoring certificate errors
		options.addArguments("--test-type");
		options.addArguments("--disable-extensions");
		options.addArguments("--disable-notifications");
		return options;
	}
	
	@SuppressWarnings("unused")  // only used manually for switching
	private static WebDriver localFirefoxTest() {	
		return new FirefoxDriver();
	}
	
	@SuppressWarnings("unused") // only used manually for switching
	private static WebDriver localIETest() {  
		return new InternetExplorerDriver();
	}
	
	private static void setSauceLabsConfig(DesiredCapabilities caps) {
		// saucelabs specific configuration
		// see: https://saucelabs.com/docs/additional-config for options	    
		caps.setCapability("record-video", recordVideo); // enable/disable the video recording feature in saucelabs
		caps.setCapability("record-screenshots", captureScreenshots); // enable/disable the screenshot feature in saucelabs
		caps.setCapability("sauce-advisor", enableSauceAdvisor); // enable/disable the sauce advisor test inspector feature in saucelabs
		caps.setCapability("public", jobVisibility); // share jobs with people who have a valid link to the job

		caps.setCapability("screen-resolution", targetScreenResolution_SL); // SauceLabs indicate this is experimental

		caps.setCapability("idle-timeout", commandTimeout); // timeout on an individual action defaults to 90 secs but datacleardown actions can take longer so try 3 mins			
		caps.setCapability("max-duration", jobMaxDurationSeconds); // the end to end job lasts much longer than the default 30mins, set to 60mins for now and monitor
	}

	private static void setTestingBotConfig(DesiredCapabilities caps) {
		
	}
	
	private void setBrowserStackConfig(DesiredCapabilities caps) {
		//browserstack config
		caps.setCapability("resolution", targetScreenResolution_BS);
		caps.setCapability("browserstack.debug", true);
		
		// set the test name and project information - use the date
		String formattedDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());

		//caps.setCapability("name", testName);
		caps.setCapability("build", formattedDate);
		caps.setCapability("project", getExecutionEnvironment());
	}
	
	private RemoteWebDriver getRemoteWebDriver() throws MalformedURLException, ConfigurationException {
		DesiredCapabilities caps;
		// possible options for browser are here: https://saucelabs.com/docs/platforms
		if(remoteBrowser().toLowerCase().equals(Config.CHROME))
		{
			caps = DesiredCapabilities.chrome();
			ChromeOptions options = getChromeOptions();
			caps.setCapability(ChromeOptions.CAPABILITY, options);
			caps.setCapability("browser", "Chrome");
		}
		else if(remoteBrowser().toLowerCase().equals(Config.IE))
		{
			caps = DesiredCapabilities.internetExplorer();
		}
		else if(remoteBrowser().toLowerCase().equals(Config.FIREFOX))
		{
			caps = DesiredCapabilities.firefox();	
		}
		else
		{
			throw new ConfigurationException(remoteBrowser() + " is an unsupported remote browser type");
		}
		
		if(remoteProvider().equals("SL"))
		{
			caps.setCapability("version", remoteBrowserVersion());
			caps.setCapability("platform", remotePlatform());
		}
		else if(remoteProvider().equals("TB"))
		{
			caps.setCapability("version", remoteBrowserVersion());
			caps.setCapability("platform", remotePlatform());
			caps.setCapability("browserName", remoteBrowser());
		}
		else if(remoteProvider().equals("BS"))
		{
			caps.setCapability("browser_version", remoteBrowserVersion());
			caps.setCapability("os", "Windows");
			caps.setCapability("os_version", remotePlatform());
		}

		for(int i=0; i<Config.androidTests.length; i++){
			if(Config.androidTests[i].toString().contains(getTestName())){
				caps.setCapability("idle-timeout", "600");
			}
		}	
	     
		return remoteWebDriver(caps);
	}
	
	private RemoteWebDriver remoteWebDriver(DesiredCapabilities caps) throws MalformedURLException {

		if(remoteProvider().equals("SL"))
		{
			setSauceLabsConfig(caps);
			// temporary workaround for saucelabs DNS issues
			//return new RemoteWebDriver(new URL("http://" + SLusername + ":" + SLaccesskey + "@162.222.75.33:80/wd/hub"), caps);			
			return new RemoteWebDriver(new URL("http://" + SLusername + ":" + SLaccesskey + "@ondemand.eu-central-1.saucelabs.com:80/wd/hub"), caps);//https://ondemand.eu-central-1.saucelabs.com/wd/hub
		}
		else if(remoteProvider().equals("TB"))
		{
			setTestingBotConfig(caps);
			
			String KEY = "dde8d4067c8b1ff1970b2b3434170cb4";
			String SECRET = "696d9958483dbbbdac3e321968cff059";
			String URL = "http://" + KEY + ":" + SECRET + "@hub.testingbot.com/wd/hub";
			  
			return new RemoteWebDriver(new URL(URL), caps);
		}
		else
		{
			setBrowserStackConfig(caps);
			return new RemoteWebDriver(new URL(AUTOMATION_URL), caps);
		}
	}
	
	static void initialiseOrgs() throws FileNotFoundException, Exception {
		String[] environments = CredentialsPool.getEnvironments();
        ExecutorService executor = Executors.newFixedThreadPool(environments.length);
        
        final CountDownLatch latch = new CountDownLatch(environments.length);
		for(int i = 0; i < CredentialsPool.getEnvironments().length; i++) {
			executor.execute(() -> {
				try {
					initialiseOrg();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("Failed to initialise an environment", e);
				}
				finally {
					latch.countDown();
				}
			});
		}
		
		latch.await(2, TimeUnit.HOURS);
		executor.shutdown();
	}

	private static void initialiseOrg() throws FileNotFoundException, Exception {
		// the base testng supporting methods to construct the test environment
		// are static so we need to start an instance method for the datacleardown
		KimbleOneTest initialise = new KimbleOneTest(){

			@Override
			public void executeTest() throws Exception {
				DataCleardown(SeleniumHelper.config.dataClearDownAction);
			}
		};
		
		initialise.testName = "dataCleardown-" + SeleniumHelper.config.dataClearDownAction;
		initialise.executeScenario(null, new KimbleData());
		TestResult result = new TestResult() { public boolean isSuccess() { return initialise.getSH().hasValidationFailure(); } };
		initialise.recordTestResult(result);
		initialise.automationTestTeardown();
	}
	
	protected void DataCleardown(CleardownType action) throws FileNotFoundException, Exception {
		if(action == CleardownType.none)
			return;
		execute(dataCleardown(null, "perform data cleardown: " + action, getSH(), action));
	}
	
	public void executeScenario(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception
	{
		if(SeleniumHelper.config.testsToRun.get(testName) != null && !SeleniumHelper.config.testsToRun.get(testName)) {
			General.scheduleForRerun(testName, false);
			throw new SkipException("!SKIPPED! - " + testName + " - !SKIPPED!");
		}
				
		try {
			initialiseBrowser();
			resetValidationTracker();
			
			for(int i=0; i<Config.mobileTests.length; i++){
				if(Config.mobileTests[i].toString().contains(getTestName())){
						createDriver();
				}
			}
			
			if(BaseTest.enableRerunDialog)
				state = getTheYamlHelper().recoverState(ControlPanel.chooseDumpUI(getTestName()));
			if(state == null) {
				state = new TestState(kimbleTestData, YamlHelper.getFormattedCurrentDateTime(YamlHelper.FULL_DATETIME_FORMAT));
				login();
			}
			else
				login(getCredentials());		
			
			executeTest();
		}
		catch(org.openqa.selenium.UnhandledAlertException ex) { handleAlert(ex); }
		catch(Exception ex) { handleException(ex); }
		catch(Error err) { handleError(err); }
		finally { endScenario(); }
	}
	
	public abstract void executeTest() throws Exception;
	
	// this version of login manually constructs the test input that would be provided to the normal
	// test method by testng (this is required since datacleardown is done before an actual test
	// is invoked so testng will not yet have fired up the test parameters
	public UserCredentials login()
	{
		UserCredentials creds = CredentialsPool.allocateCredentialsFor(getTestNameLoginFormat());
		try {
			return login(creds);
		} catch(Exception e) {
			throw new RuntimeException("Failed login for test: " + getTestNameLoginFormat(), e);
		}
	}
	
	protected UserCredentials login(UserCredentials creds) {
		state.environment = creds.environment;
		LoginPageC loginHandler = new LoginPageC(getSH());         
	    navigateToLoginPage(loginHandler, creds);
	    loginHandler.Login(creds);
		logTestStart();
		
		if(getSH().isLightning()==true){
			getSH().sleep(5000);
			assert getSH().checkUrlContainsText("home", true);
		}
		else{
			assert getSH().checkUrlContainsText("home", true);
		}
		
		creds.nodeName = (String) ((JavascriptExecutor) getSH().getWD()).executeScript("return window.location.origin;");
			
			for(int i=0; i<Config.mobileTests.length; i++){
				if(Config.mobileTests[i].toString().contains(getTestName())){
				    AppiumDriver driver = getAppiumDriver();
					MobileLoginPage mobileLoginHandler = new MobileLoginPage(getSH());    
					mobileLoginHandler.MobileLogin(creds, driver, getTestName());
				}
			}	
		return creds;
	}
		
    protected void logTestStart() {
		getSH().SetCurrentTest(this);
		getSH().LogInfoMessageLine("Start ( https://eu-central-1.saucelabs.com/tests/" + ((RemoteWebDriver)webDriverInstance).getSessionId() + " )");
	}
	
	protected void endScenario()
	{
		CredentialsPool.freeCredentialsFor(getTestNameLoginFormat());
		endScenario(true);
	}
	
	protected void endScenario(boolean validateResult)
	{
		// post any Fact validations we need to check whether any had failed
		if(validateResult)
			Assert.assertEquals(getSH().validationFailure, "");
	}
	
	public void handleAlert(org.openqa.selenium.UnhandledAlertException ex) {
		dumpState();
		
		// Get a handle to the open alert, prompt or confirmation 
		Alert alert = getSH().getWD().switchTo().alert(); 
		
		// log the alert text
		getSH().LogErrorMessage("UnhandledAlertException: " + alert.getText());
		LogDetailedErrorMessage("Stack Trace: " + ex.getStackTrace());
		getSH().recordValidationFailure(alert.getText());
	}
	
	public void handleException(Exception ex) throws Exception {
		ex.printStackTrace();
		dumpState();
		
		java.io.StringWriter sw = new java.io.StringWriter();
		ex.printStackTrace(new java.io.PrintWriter(sw));
		String exceptionAsString = sw.toString();
		
		getSH().LogErrorMessage("Exception: " + ex.getMessage());
		LogDetailedErrorMessage("Exception Stack Trace: " + exceptionAsString);
		getSH().recordValidationFailure(ex.getMessage());
		
		// attempt to read any functional/technical error messages that might be present on screen
		getSH().handleOnScreenMessage();
		
		if(SeleniumHelper.config.debugExecution)
		{
			// only re-throw exceptions if in a debug run as only then will the exception stack
			// be useful in the debugger
			throw ex;
		}
	}

	public void handleError(Error err) throws Exception {
		dumpState();
		
		java.io.StringWriter sw = new java.io.StringWriter();
		err.printStackTrace(new java.io.PrintWriter(sw));
		String errorAsString = sw.toString();
		
		getSH().LogErrorMessage("Exception: " + err.getMessage());
		LogDetailedErrorMessage("Exception Stack Trace: " + errorAsString);
		getSH().recordValidationFailure(err.getMessage());
		
		// attempt to read any functional/technical error messages that might be present on screen
		getSH().handleOnScreenMessage();
		
		if(SeleniumHelper.config.debugExecution)
		{
			// only re-throw exceptions if in a debug run as only then will the exception stack
			// be useful in the debugger
			throw err;
		}
	}
	
	public void dumpState() {
		// state is only set in scenarios coded to use the TestState approach
		if(state != null)
		{
			try {
				TestState snapshot = state.clone();
				snapshot.url = getSH().getCurrentUrl();
				getTheYamlHelper().dumpStateToYaml(snapshot, getTestNameLoginFormat(), snapshot.date);
			} catch(Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private void navigateToLoginPage(LoginPageC loginHandler, UserCredentials targetEnvironmentCredentials) {
		if(targetEnvironmentCredentials.loginUrl != null && !targetEnvironmentCredentials.loginUrl.equals("")) {
		    loginHandler.NavigateToLoginPage(targetEnvironmentCredentials.loginUrl);
	    }
	    else {
		    loginHandler.NavigateToLoginPage();
	    }
	}

	public static void checkExpectedResultsForSalesOpportunity(SalesOpportunityForecast expected, String testStage, SeleniumHelper sh) throws Exception {
		SalesOppForecastPageC salesOppForecastHandler = new SalesOppForecastPageC(sh);
				
		sh.LogMessageLine("");
		sh.LogMessageLine("Validating Sales Opp Forecast for [" + testStage + "] test stage, Sales Opp [" + expected.salesOpportunityName + "]");
		sh.waitForLightningSpinnerToBeHidden();
		salesOppForecastHandler.Initialise();
		
		for (Fact factDetail : expected.facts) {
			for (ExpectedValue expectedValueDetail : factDetail.expectedValues) {
				boolean factValidationResult = salesOppForecastHandler.ValidateFact(testStage, factDetail, expectedValueDetail);
				if(!factValidationResult) {
					sh.recordValidationFailure("Sales Opp Forecast Fact Validation Failed");
				}
			}
		}
	}
	
	public static void checkExpectedResultsForResource(Collection<ResourceForecast> expected, String testStage, SeleniumHelper sh) throws Exception {		
		ResourcePageC resourceHandler = new ResourcePageC(sh);		
		ResourceForecastPageC resourceForecastHandler = new ResourceForecastPageC(sh);
		
		for(ResourceForecast resourceForecastDetail : expected)
		{
			sh.LogMessageLine("");
			sh.LogMessageLine("Validating Resource Forecast for [" + testStage + "] test stage, resource [" + resourceForecastDetail.resourceName + "]");
			
			if(sh.isLightning()){
				GeneralOperationsZ.navigateFromAnywhereToTab(sh, "Resources");
				GeneralOperationsZ.pickListViewItemZ("navi to item: " + resourceForecastDetail.resourceName, sh, resourceForecastDetail.resourceName); 
				String id = sh.getCurrentUrl().replace("/view", "").split("Resource__c/")[1];
				sh.goToUrl("https://kimbleone.gus.visual.force.com/apex/ResourceForecast?id=" + id);
			}
			else{
				resourceHandler.NavigateToList();
				resourceHandler.LoadExistingByName(resourceForecastDetail.resourceName);		
				resourceHandler.NavigateToResourceForecastFromHome();
			}
			
			resourceForecastHandler.Initialise();

			for (Fact factDetail : resourceForecastDetail.facts) {
				for (ExpectedValue expectedValueDetail : factDetail.expectedValues) {
					boolean factValidationResult = resourceForecastHandler.ValidateFact(testStage, factDetail, expectedValueDetail);
					if(!factValidationResult) {
						sh.recordValidationFailure("Resource Forecast Fact Validation Failed");
					}
				}
			}
		if(sh.isLightning()){
			sh.goToUrl("https://gs0.lightning.force.com/lightning/page/home");
		}
		}
		if(!sh.isLightning()){
			resourceHandler.NavigateToResourceHomeFromForecast();			
		}

	}

	public static void checkTrackingPlanTotals(Collection<TrackingPlanTotal> totals, String testStage, SeleniumHelper sh) throws Exception {
		sh.LogMessageLine("");
		sh.LogMessageLine("Validating Tracking Plan Totals for [" + testStage + "] test stage");

		for(TrackingPlanTotal trackingPlanTotalDetail : totals)
		{
			DeliveryEngagementPageC.LoadExistingByName(sh, trackingPlanTotalDetail.deliveryEngagementName);
			
			sh.sleep(2000);
			
			for (Fact factDetail : trackingPlanTotalDetail.facts) {
				for (ExpectedValue expectedValueDetail : factDetail.expectedValues) {
					boolean factValidationResult = DeliveryEngagementPageC.ValidateFact(sh, testStage, factDetail, expectedValueDetail);
					if(!factValidationResult) {
						sh.recordValidationFailure("Tracking Plan Fact Validation Failed");
					}
				}
			}
		}
	}
	
	
	// mobile declarations
	//public String seleniumURI = "@162.222.75.33:443";
	public String seleniumURI = "@ondemand.eu-central-1.saucelabs.com:443";
	public String buildTag = System.getenv("BUILD_TAG");
	public String username = "KimbleSauce";
	public String accesskey = "185364a9-adb7-4eb4-9fe6-b504a5b44f66";

	 public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

	 private ThreadLocal<AppiumDriver> AppiumDriver = new ThreadLocal<AppiumDriver>();

	 private ThreadLocal<String> sessionId = new ThreadLocal<String>();

	 @DataProvider(name = "hardCodedBrowsers", parallel = true)
	 public static Object[][] sauceBrowserDataProvider(Method testMethod) {
	     return new Object[][]{
	             new Object[]{"Android", "Android GoogleAPI Emulator", "6.0", "1.9.1", "portrait"}
	     };
	 }

	 public AppiumDriver getAppiumDriver() {
	     return AppiumDriver.get();
	 }

	 protected MobileSelectors getMobileSelectors() {
		 for(int i=0; i<Config.androidTests.length; i++){
		   	 if(Config.androidTests[i].toString().contains(getTestName())){		 
		 	        return new AndroidSelectors();
			 }
		   	 if(Config.iOSTests[i].toString().contains(getTestName())){
	                return new IosSelectors();
			 }
		 }		 
		 return new IosSelectors();
	 }	

	 public SauceOnDemandAuthentication getAuthentication() {
	     return authentication;
	 }
	 protected void createDriver()
	         throws MalformedURLException, UnexpectedException {
		for(int i=0; i<Config.androidTests.length; i++){
			if(Config.androidTests[i].toString().contains(getTestName())){
			     DesiredCapabilities capabilities =  new DesiredCapabilities();
			     capabilities.setCapability("platformName", SeleniumHelper.config.androidPlatformName);
			     capabilities.setCapability("platformVersion", SeleniumHelper.config.androidPlatformVersion);
			     capabilities.setCapability("deviceName", SeleniumHelper.config.androidDeviceName);
			     capabilities.setCapability("deviceOrientation", SeleniumHelper.config.androidDeviceOrientation);
			     capabilities.setCapability("appiumVersion", SeleniumHelper.config.androidAppiumVersion);
			     capabilities.setCapability("unicodeKeyboard", "true");                                     
			     capabilities.setCapability("resetKeyboard", "true");              
			     capabilities.setCapability("name", testName +" [" + SeleniumHelper.config.androidDeviceName + "]");
			     String app = "sauce-storage:Kimble131AndroidMobileApp2-release.apk";
			     capabilities.setCapability("app", app);
			     capabilities.setCapability("optionalIntentArguments", SeleniumHelper.config.startingPeriod);
			     capabilities.setCapability("idle-timeout", "600");
			     capabilities.setCapability("maxDuration", jobMaxDurationSeconds);
		
			     if (buildTag != null) {
			         capabilities.setCapability("build", buildTag);
			     }
			      
			     // Launch remote browser and set it as the current thread
		     	AppiumDriver.set(new AndroidDriver(
		     			new URL("https://" + authentication.getUsername() + ":" + authentication.getAccessKey() + seleniumURI + "/wd/hub"),
		     			capabilities));
			}
			if(Config.iOSTests[i].toString().contains(getTestName())){
			     DesiredCapabilities capabilities =  new DesiredCapabilities();
			     capabilities.setCapability("platformName", SeleniumHelper.config.iOSPlatformName);
			     capabilities.setCapability("deviceName", SeleniumHelper.config.iOSDeviceName);
			     capabilities.setCapability("platformVersion", SeleniumHelper.config.iOSPlatformVersion);
			     capabilities.setCapability("iOSNoReset", SeleniumHelper.config.iOSNoReset);
		         capabilities.setCapability("deviceOrientation", "portrait");
			     capabilities.setCapability("appiumVersion", SeleniumHelper.config.iOSAppiumVersion);
			     capabilities.setCapability("name", testName +" [" + SeleniumHelper.config.iOSDeviceName.replace(" Simulator", "]"));
			     capabilities.setCapability("idle-timeout", "600");
			     capabilities.setCapability("maxDuration", jobMaxDurationSeconds);
			     
			     final Map<String, String> processEnv = new HashMap<String, String>();
			     processEnv.put("IsRunningTest", "YES");
			     processEnv.put("CurrentDate", SeleniumHelper.config.DEFAULT_WEEK);
			    
			     final JSONObject argsValue = new JSONObject();
			     
			     try {
					argsValue.put("env", processEnv);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			     //Set arguments for test to use in running APP
			     capabilities.setCapability("processArguments", argsValue.toString());    
			     String app = "sauce-storage:Kimble131.app.zip";
			     capabilities.setCapability("app", app);
			     if (buildTag != null) {capabilities.setCapability("build", buildTag); }
			      
			     // Launch remote browser and set it as the current thread
			     	AppiumDriver.set(new IOSDriver(
			     			new URL("https://" + authentication.getUsername() + ":" + authentication.getAccessKey() + seleniumURI + "/wd/hub"),
			     			capabilities));
			 }
		 }
	     String id = ((RemoteWebDriver) getAppiumDriver()).getSessionId().toString();
	     int arrayPosition = Integer.parseInt(getTestName().substring(9));
	     mobileTestIDs[arrayPosition] = id;
	 }

	public void tearDownMobile() {		
		    AppiumDriver.get().quit();	
	}	
}
