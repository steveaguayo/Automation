package kimble.automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import kimble.automation.domain.KimbleData;
import kimble.automation.domain.TestState;
import kimble.automation.domain.UserCredentials;
import kimble.automation.helpers.CredentialsPool;
import kimble.automation.helpers.General;
import kimble.automation.helpers.ScenarioFunctions;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.YamlHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.helpers.controlpanel.ControlPanel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.saucelabs.saucerest.SauceREST;
import io.appium.java_client.AppiumDriver;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class BaseTest {
	
	public static enum Status { running, paused, pendingAbort, aborted, passed, failed };

	protected WebDriver webDriverInstance;
	private SeleniumHelper seleniumHelperInstance;
	private YamlHelper theYamlHelper;

	protected static String SLusername = "KimbleSauce";
    protected static String SLaccesskey = "185364a9-adb7-4eb4-9fe6-b504a5b44f66";

	// browserstack
	private static final String AUTOMATION_USERNAME = "marksmith36";
	private static final String AUTOMATION_AUTOMATE_KEY = "2xyxxxqMxUys48BXjFs2";
	protected static final String AUTOMATION_URL = "http://" + AUTOMATION_USERNAME + ":" + AUTOMATION_AUTOMATE_KEY + "@hub.browserstack.com/wd/hub";

	public static Map<String, UserCredentials> credentialsMap;
	
	public static boolean enableRerunDialog = false;
	
    protected static boolean recordVideo = true;
    protected static boolean captureScreenshots = true;
    protected static boolean enableSauceAdvisor = false;
    protected static String targetScreenResolution_BS = "1920x1200";
    protected static String targetScreenResolution_SL = "1280x1024";
    
    protected static String jobVisibility = "share";
    
    protected static String jobMaxDurationSeconds = "7200";
    
    protected static String commandTimeout = "800"; // 10 mins to account for Mobile Scenarios switching context
    
    protected static int failureCount;
    
    public String testName;
	public TestState state;
	
	public Stage[] stages;
	
	static ReentrantLock counterLock = new ReentrantLock();
	static int testCount = 0;
	public int getTestIndex() {
		if(state.testIndex > 0)
			return state.testIndex;
		counterLock.lock();
		testCount++;
		state.testIndex = testCount;
		counterLock.unlock();
		return state.testIndex;
	}
	
	public String getTestName() { return testName; }
	
	public String getTestNameLoginFormat() {
		return getTestName() + "-" + getTestIndex();
	}
	
	public String getTestNameLogFormat() {
		return getTestNameLoginFormat() + " : " + getExecutionEnvironment();
	}
	
	public String getMobileTestNameLogFormat() {
		String mobileTestNameFormat = "";
		for(int i=0; i<Config.iOSTests.length; i++){
			if(Config.iOSTests[i].toString().contains(getTestName())){
				mobileTestNameFormat = getTestNameLoginFormat() + " [" + SeleniumHelper.config.iOSDeviceName + "]" + " : " + getExecutionEnvironment();
			}
			if(Config.androidTests[i].toString().contains(getTestName())){
				mobileTestNameFormat = getTestNameLoginFormat() + " [" + SeleniumHelper.config.androidDeviceName + "]" + " : " + getExecutionEnvironment();
			}
		}	
		return mobileTestNameFormat;
	}
	
	public KimbleData data() { return state.data; }

	public String pauseStage;
	private Status status;
    public void setStatus(Status s) {
    	status = s;
    	ControlPanel.notifyChange((KimbleOneTest)this);
    }
    public Status getStatus() { return status; }
    
	protected static boolean seedDateInd = false;
	protected static Date seedDate;
	
	protected static HashMap<String, String> seedDateMap = new HashMap();
	
	protected static List<String> detailedErrors;
	
	public void resetValidationTracker() {
		ControlPanel.notifyChange((KimbleOneTest)this);
		getSH().resetValidationFailure();
	}
	
	@AfterMethod(alwaysRun = true)
	public void recordTestResult(ITestResult result) throws IOException{
		Boolean didTestRun = SeleniumHelper.config.testsToRun.get(testName);
		if(didTestRun != null && !didTestRun)
			return;
		
		String testResult = "passed";
		// as tests fail, increment the test failed counter
        if(!result.isSuccess()) {
        	failureCount++;
        	testResult = "FAILED";
        	if(getStatus() != Status.aborted)
        		setStatus(Status.failed);
        	General.scheduleForRerun(getTestName(), true);
        	//updatePractitestScenarioResult(1);
        }
        else {
    		setStatus(Status.passed);
        	General.scheduleForRerun(getTestName(), false);
        	//updatePractitestScenarioResult(0);
        }
        
        logTestEnd(testResult);
	}
    	
    	public void updatePractitestScenarioResult(int exitCode){
          try {
    			createPractitestRun(exitCode);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}

	 private ThreadLocal<AppiumDriver> AppiumDriver = new ThreadLocal<AppiumDriver>();
	 
	 public AppiumDriver getAppiumDriver() {
	     return AppiumDriver.get();
	 }
	
	@AfterTest
	public void automationTestTeardown() throws IOException {
		if(SeleniumHelper.config.testsToRun.get(testName) == null || SeleniumHelper.config.testsToRun.get(testName)){
		    endJobAndCloseBrowser();
			for(int i=0; i<Config.mobileTests.length; i++){
				if(Config.mobileTests[i].toString().contains(getTestName())){
					endJobAndCloseAppium();
				}
			}
		}
	}
	
	public static boolean hasFailures() {
		return failureCount > 0;
	}
	
	protected void endJobAndCloseAppium() throws IOException {		
		
	    int arrayPosition = Integer.parseInt(getTestName().substring(9));
	    String appiumJobID =KimbleOneTest.mobileTestIDs[arrayPosition];
		
		getSH().LogInfoMessageLine("Finished " + appiumJobID);
        SauceREST client = new SauceREST(SLusername, SLaccesskey);

        // set the name of the job to the last executed test
        Map<String, Object> updates = new HashMap();
        updates.put("name", getMobileTestNameLogFormat());

        client.updateJobInfo(appiumJobID, updates);
        
        if(failureCount > 0) {
            client.jobFailed(appiumJobID);
        } else {
        	client.jobPassed(appiumJobID);
        }
		getSH().LogInfoMessageLine("tear down mobile instance " + appiumJobID);
		client.stopJob(appiumJobID);
	}
	
	protected void endJobAndCloseBrowser() throws IOException {
		if(isRemoteTest()) {
	        String jobID = ((RemoteWebDriver)webDriverInstance).getSessionId().toString();

			getSH().LogInfoMessageLine("Finished " + jobID);

	        if(remoteProvider().equals("SL"))
	        {
		        SauceREST client = new SauceREST(SLusername, SLaccesskey);

		        // set the name of the job to the last executed test
		        Map<String, Object> updates = new HashMap();
		        updates.put("name", getTestNameLogFormat());

		        client.updateJobInfo(jobID, updates);
		        
		        if(failureCount > 0) {
		            client.jobFailed(jobID);
		        } else {
		        	client.jobPassed(jobID);
		        }
	        }
	        else if(remoteProvider().equals("TB"))
	        {
	        	// TODO how to update Job with Pass/Fail
	        }
	        else
	        {
		        if(failureCount > 0) {
		        	updateJobInfo(jobID, false);
		        } else {
		        	updateJobInfo(jobID, true);
		        }
	        }
	    }
		
	    // only close the browser if we aren't running a debug (an optional parameter in the xml file)
	    if(!SeleniumHelper.IsDebugRun()) {
	    	getSH().closeBrowser();
		}
	}
	
	static HttpClient client = HttpClientBuilder.create().build();
	static HttpResponse response; 
	
	public void execute(Stage... aStages) {
		stages = aStages;
		ScenarioFunctions.normaliseAndExecute(this, getSH(), stages);
	}
	
	public List<Stage> getNormalisedStageList() {
		return ScenarioFunctions.normalise(stages);
	}

	protected HttpResponse updateJobInfo(String sessionId, boolean pass) throws IOException
	{
		if(response!=null)
			EntityUtils.consume(response.getEntity());
		
		String url = "https://www.browserstack.com/automate/sessions/" + sessionId + ".json";
		HttpPut putRequest = new HttpPut(url);

//		Content-type and Accept header not required
		String encoding = Base64.encodeBase64String(AUTOMATION_AUTOMATE_KEY.getBytes());
		putRequest.setHeader("Authorization", "Basic " + encoding);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList();
		nameValuePairs.add(new BasicNameValuePair("status", (pass ? "completed" : "error")));
		
		putRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// Get the responses
		response = client.execute(putRequest);
		return response;
	}
	
	public String getSessionId() {
        return ((RemoteWebDriver)webDriverInstance).getSessionId().toString();
	}
		
	protected void logTestEnd(String testResult) {
		if(!testResult.equals("")) {
			getSH().LogInfoMessageLine("Result [" + testResult + "] ( https://eu-central-1.saucelabs.com/tests/" + this.getSessionId() + " )");
			if(testResult.equals("FAILED")) {
				UserCredentials creds = getCredentials();
				String msg;
				if(getSH().isLightning())
					msg = "See: " + General.generateLoginUrlFor(getSH().getCurrentUrl(), creds.usernameLightning, creds.passwordLightning);
				else
					msg = "See: " + General.generateLoginUrlFor(getSH().getCurrentUrl(), creds.username, creds.password);
				getSH().LogInfoMessageLine(msg);
			}
			testResult = "";
		}
	}

	protected void logTestJobDetails(String sessionId, String jobName) {
		getSH().LogInfoMessageLine("SessionId:" + sessionId);
	}
	
	protected void LogDetailedErrorMessage(String output) {
		// if we are in a debug session then output the message now
		// otherwise add this message to a detailed error description for output once the tests are complete
		if(SeleniumHelper.config.debugExecution) {
			System.out.println(getSH().formatLogOutput(output));
		}
		else
		{
			detailedErrors.add(getSH().formatLogOutput(output));
		}
	}
	
	
	protected static boolean isRemoteTest() {
		return SeleniumHelper.config.remoteExecution;
	}

	protected static String remoteProvider() {
		return SeleniumHelper.config.remoteProvider;
	}

	protected static String remotePlatform() {
		return SeleniumHelper.config.remotePlatform;
	}
	
	protected static String remoteBrowser() {
		return SeleniumHelper.config.remoteBrowser;
	}
	
	protected static String remoteBrowserVersion() {
		return SeleniumHelper.config.remoteBrowserVersion;
	}
	
	protected static void setExecutionEnvironments(String executionEnvironmentsFileName, String credentialsFilename) {
		try {
			File inputFile = FileUtils.getFile("suite", "config", executionEnvironmentsFileName + YamlHelper.YAML_EXTENSION);
			Yaml yaml = new Yaml(new Constructor(String[].class));
			String[] environments = (String[])yaml.load(new FileInputStream(inputFile));
			
			inputFile = FileUtils.getFile("src", "test", "resources", credentialsFilename + YamlHelper.YAML_EXTENSION);
			yaml = new Yaml(new Constructor(KimbleData.class));
			KimbleData credsData = (KimbleData)yaml.load(new FileInputStream(inputFile));
			
			if(credentialsMap == null)
				credentialsMap = new HashMap();
			for(UserCredentials uc : credsData.userCredentials)
				credentialsMap.put(uc.environment, uc);
			for(int i = 0; i < environments.length; i++)
				if(credentialsMap.containsKey(environments[i]))
					CredentialsPool.addCredentials(credentialsMap.get(environments[i]));
		} catch(Exception e) {
			throw new RuntimeException("Failed to populate execution environments pool", e);
		}
	}
	
	protected static void initialiseErrorCollection() {
		BaseTest.detailedErrors = new ArrayList();
	}
	
	protected static void addDetailedError(String detailedErrorMessage) {
		BaseTest.detailedErrors.add(detailedErrorMessage);
	}

	protected static void setSeedDate(String seedDate) {
		if(!seedDate.equals(""))
		{
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try
			{
				System.out.println("Initialising with Seed Date [" + seedDate + "]");
				BaseTest.seedDate = formatter.parse(seedDate);
				BaseTest.seedDateInd = true;
				generateSeedDateMap();
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		} 
	}

	protected static void logDetailedErrors() {
		if(detailedErrors.size() > 0)
		{
			System.out.println("");
			System.out.println("");
			System.out.println("-----------Detailed Exceptions from test run--------");
			for(String detailedErrorMessage : detailedErrors)
			{
				System.out.println("");
				System.out.println(detailedErrorMessage);
				System.out.println("");
			}
			System.out.println("---------------End of Detailed Exceptions-----------");
			System.out.println("");
		}
	}

	public String getExecutionEnvironment() {
		if(state.environment == null)
			System.out.println("The environment hasn't been set in the test state. If you're running the test from a dump file, "
					+ "check the environment setting. It's probably set to null");
		return state.environment;
	}

	public UserCredentials getCredentials() {
		return credentialsMap.get(getExecutionEnvironment());
	}

	protected void setSeleniumHelperInstance(SeleniumHelper seleniumHelperInstance) {
		this.seleniumHelperInstance = seleniumHelperInstance;
	}

	protected void initialiseSeleniumHelper() {
		setSeleniumHelperInstance(new SeleniumHelper(webDriverInstance));
		SeleniumHelper.SetDebugRun(SeleniumHelper.config.debugExecution);
		SeleniumHelper.SetPackagedOrg(SeleniumHelper.config.packagedOrg);
		SeleniumHelper.SetLightningEnabled(SeleniumHelper.config.lightning);
	}
	
	protected SeleniumHelper getSH() {
		if(seleniumHelperInstance == null)
			initialiseSeleniumHelper();
		
		return seleniumHelperInstance;
	}

	protected void setTheYamlHelper(YamlHelper theYamlHelper) {
		this.theYamlHelper = theYamlHelper;
	}

	protected YamlHelper getTheYamlHelper() {
		if(theYamlHelper == null)
			initialiseYamlHelper();
		
		return theYamlHelper;
	}
	
	private void initialiseYamlHelper() {
		setTheYamlHelper(new YamlHelper(SeleniumHelper.config.dateTimeOverride, seedDateMap));
	}

	private static Integer forwardDays = 365;
	private static Integer forwardWeeks = 52;
	private static Integer forwardMonths = 12;
	private static Integer backwardDays = 365;
	private static Integer backwardWeeks = 52;
	private static Integer backwardMonths = 12;
	
	private static void generateSeedDateMap() {
		seedDateMap = new HashMap();
        
		// using the seed date, generate a map of possible tokens we would be able to replace
		// each of these can be used in the yml file in any combination and this map whilst
		// inefficient does allow a much more dynamic use of tokens in the input yml file
		// rather than hardcoding against a known set
		Calendar calendar = new GregorianCalendar();
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(BaseTest.seedDate);

		String tokenSuffix = "";
		// output 365 days worth of days forwards
		for (int i = 0; i <= forwardDays; i++) {
			calendar.setTime(BaseTest.seedDate);
		    calendar.add(Calendar.DAY_OF_YEAR, i);
		    Date newDate = calendar.getTime();
		    formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(newDate);
		    
		    if(i>0)
		    {
		    	tokenSuffix = "+" + i + "D";
				seedDateMap.put("<SEED_DATE" + tokenSuffix + ">", formattedDate);
		    }
		}

		tokenSuffix = "";		
		// output 365 days worth of days forwards
		for (int i = 0; i <= backwardDays; i++) {
			calendar.setTime(BaseTest.seedDate);
		    calendar.add(Calendar.DAY_OF_YEAR, -i);
		    Date newDate = calendar.getTime();
		    formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(newDate);
		    
		    if(i>0)
		    {
		    	tokenSuffix = "-" + i + "D";
				seedDateMap.put("<SEED_DATE" + tokenSuffix + ">", formattedDate);
		    }
		}

		tokenSuffix = "";		
		// output 10 weeks worth of weeks backwards
		for (int i = 0; i <= backwardWeeks; i++) {
			calendar.setTime(BaseTest.seedDate);
		    calendar.add(Calendar.DAY_OF_YEAR, -(i*7));
		    Date newDate = calendar.getTime();
		    formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(newDate);
		    
		    if(i>0)
		    {
		    	tokenSuffix = "-" + i + "W";
				seedDateMap.put("<SEED_DATE" + tokenSuffix + ">", formattedDate);
				seedDateMap.put("<SEED_DATE" + tokenSuffix + "_EOWW>", getLastWorkingDay(newDate));
		    }
		}
		
		tokenSuffix = "";
		// output 10 months worth of start of calendar months backwards
		for (int j = 0; j <= backwardMonths; j++) {
			calendar.setTime(BaseTest.seedDate);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		    calendar.add(Calendar.MONTH, -j);
		    Date newDate = calendar.getTime();
		    formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(newDate);

		    if(j>0)
		    {
		    	tokenSuffix = "-" + j + "M";
		    	seedDateMap.put("<SEED_DATE" + tokenSuffix + "_SOCM>", formattedDate);
		    	seedDateMap.put("<SEED_DATE" + tokenSuffix + "_SOWM>", getNextWorkingDay(newDate));
		    }
		}

		tokenSuffix = "";
		// output 10 months worth of end of calendar months backwards
		for (int k = 0; k <= backwardMonths; k++) {
			calendar.setTime(BaseTest.seedDate);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			// go to next month then subtract a day
		    calendar.add(Calendar.MONTH, -(k-1));
		    calendar.add(Calendar.DAY_OF_YEAR, -1);
		    Date newDate = calendar.getTime();
		    formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(newDate);

		    if(k>0)
		    {
		    	tokenSuffix = "-" + k + "M";
		    	seedDateMap.put("<SEED_DATE" + tokenSuffix + "_EOCM>", formattedDate);
		    	seedDateMap.put("<SEED_DATE" + tokenSuffix + "_EOWM>", getPreviousWorkingDay(newDate));
		    }
		}
		
		tokenSuffix = "";
		// output 10 weeks worth of weeks
		for (int i = 0; i <= forwardWeeks; i++) {
			calendar.setTime(BaseTest.seedDate);
		    calendar.add(Calendar.DAY_OF_YEAR, i*7);
		    Date newDate = calendar.getTime();
		    formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(newDate);
		    
		    if(i>0)
		    {
		    	tokenSuffix = "+" + i + "W";
		    }
		    seedDateMap.put("<SEED_DATE" + tokenSuffix + ">", formattedDate);
		    seedDateMap.put("<SEED_DATE" + tokenSuffix + "_EOWW>", getLastWorkingDay(newDate));
		}
		
		tokenSuffix = "";
		// output 10 months worth of start of calendar months
		for (int j = 0; j <= forwardMonths; j++) {
			calendar.setTime(BaseTest.seedDate);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		    calendar.add(Calendar.MONTH, j);
		    Date newDate = calendar.getTime();
		    formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(newDate);

		    if(j>0)
		    {
		    	tokenSuffix = "+" + j + "M";
		    }

		    seedDateMap.put("<SEED_DATE" + tokenSuffix + "_SOCM>", formattedDate);
		    seedDateMap.put("<SEED_DATE" + tokenSuffix + "_SOWM>", getNextWorkingDay(newDate));
		}

		tokenSuffix = "";
		// output 10 months worth of end of calendar months
		for (int k = 0; k <= forwardMonths; k++) {
			calendar.setTime(BaseTest.seedDate);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			// go to next month then subtract a day
		    calendar.add(Calendar.MONTH, k+1);
		    calendar.add(Calendar.DAY_OF_YEAR, -1);
		    Date newDate = calendar.getTime();
		    formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(newDate);

		    if(k>0)
		    {
		    	tokenSuffix = "+" + k + "M";
		    }

		    seedDateMap.put("<SEED_DATE" + tokenSuffix + "_EOCM>", formattedDate);
		    seedDateMap.put("<SEED_DATE" + tokenSuffix + "_EOWM>", getPreviousWorkingDay(newDate));
		}
	}
	
	private static String getNextWorkingDay(Date formattedDate) {
		// determine if the date is a working day, if so return it
		// otherwise, add days until we get to the first working day and return that
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(formattedDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SATURDAY)
        {
            calendar.add(Calendar.DATE, 2);
        } 
        else if (dayOfWeek == Calendar.SUNDAY)
        {
            calendar.add(Calendar.DATE, 1);
        }
        
        Date nextWorkingDay = calendar.getTime();
        
		return new SimpleDateFormat("dd/MM/yyyy").format(nextWorkingDay);
	}
	
	private static String getLastWorkingDay(Date formattedDate) {
		// return the date of the Friday in the week of the given date
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(formattedDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        while(dayOfWeek != Calendar.FRIDAY)
        {
            calendar.add(Calendar.DATE, 1);
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        } 
        
        Date lastWorkingDay = calendar.getTime();
        
		return new SimpleDateFormat("dd/MM/yyyy").format(lastWorkingDay);
	}
	
	private static String getPreviousWorkingDay(Date formattedDate) {
		// determine if the date is a working day, if so return it
		// otherwise, subtract days until we get to the next previous working day and return that
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(formattedDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SATURDAY)
        {
            calendar.add(Calendar.DATE, -1);
        } 
        else if (dayOfWeek == Calendar.SUNDAY)
        {
            calendar.add(Calendar.DATE, -2);
        }
        
        Date previousWorkingDay = calendar.getTime();
        
		return new SimpleDateFormat("dd/MM/yyyy").format(previousWorkingDay);
	}	          

    private static final String DEVELOPER_EMAIL = "tom.anderson@kimbleapps.com";
    private static final String API_TOKEN = "f6adef4667fb12ea4af17c3d0b92c876a260aaee";
    private static final String URI = "https://api.practitest.com/api/v2/projects/9204/runs.json";
        
    public final void createPractitestRun(int exitCode) throws Exception {
        
        if(!getTestName().contains("Scenario") || !(isRemoteTest()))
        	return;
        
        byte[] encoding = Base64.encodeBase64((DEVELOPER_EMAIL + ":" + API_TOKEN).getBytes());
        HttpClient httpclient = new DefaultHttpClient();
        
		String json_str = "{" +
        						"\"data\" : {" +
    							"\"type\": \"instances\"," + 
								"\"attributes\": {" +
									"\"instance-id\": "+findInstanceValue()+"," +
									"\"exit-code\": "+exitCode+" , " +
									"\"steps\": {\"data\": [{\"name\": \"step one\", \"expected-results\": \"result\"} ]}"+
    						"} } }";
        
        HttpPost request = new HttpPost(URI);
        request.setEntity(new StringEntity(json_str));
        request.setHeader("Authorization", "Basic " + new String(encoding));
        request.addHeader("content-type", "application/json");

        try {
        // Create a response handler
            HttpResponse response = httpclient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            if (statusCode == 200) {
//              System.out.println("SUCCESS: " + responseBody);
            } else {
                System.out.println("ERROR: " + statusCode + ": " + responseBody);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();
    }
 
    public int findInstanceValue(){
    	int scenario = Integer.parseInt(getTestName().replace("Scenario", ""));
    	for (int i = 0; i < Config.practitestInstanceMap.length; i++){
    		if (Config.practitestInstanceMap[i][0] == scenario){
    			return Config.practitestInstanceMap[i][1];
    		}
    	}
		return 0;
    }
}
