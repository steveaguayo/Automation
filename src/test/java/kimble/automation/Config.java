package kimble.automation;

//import static test.java.KimbleOneTest.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.ConfigurationException;

import kimble.automation.KimbleOneTest.CleardownType;
import kimble.automation.helpers.General;

import org.testng.ISuite;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Config {
			
	public static	String[] mobileTests = { "Scenario133", "Scenario134", "Scenario135", "Scenario136", "Scenario137", "Scenario138"};
	public static	String[] androidTests = { "Scenario133", "Scenario135", "Scenario137"};
	public static	String[] iOSTests = { "Scenario134", "Scenario136", "Scenario138"};

	public static final String CREDENTIALS_SOURCE = "superUserLogin";
	public static enum EnvironmentType { classic, lightning };
	
	public static final String IE = "ie";
	public static final String FIREFOX = "firefox";
	public static final String CHROME = "chrome";
	public static final String DEFAULT_REMOTE_PLATFORM = "8.1";
	public static final String DEFAULT_REMOTE_PROVIDER = "BL";
	public static final String DEFAULT_REMOTE_BROWSER = "Chrome";
	public static final String DEFAULT_REMOTE_BROWSER_VERSION = "39.0";
	
	public static final SimpleDateFormat timeStampFormatter = new SimpleDateFormat("ddMMyyyy hhmmss");
	static {
		timeStampFormatter.getCalendar().setFirstDayOfWeek(Calendar.MONDAY);
	}
	
	Config() {};
	
	public Config(ISuite aSuite) throws ConfigurationException {
		suite = aSuite;
		suite.getXmlSuite().getTests().forEach(t -> {
			testsToRun.put(t.getName(), true);
		});
		
		remoteExecution = getBoolean(remoteExecution, "remoteExecution");
		validateExpectedResults = getBoolean(validateExpectedResults, "validateExpectedResults");
		abortOnFirstValidationFailure = getBoolean(abortOnFirstValidationFailure, "abortOnFirstValidationFailure");
		debugExecution = getBoolean(debugExecution, "debugExecution");
		packagedOrg = getBoolean(packagedOrg, "packagedOrg");
		overrideOrgTypeCheck = getBoolean(overrideOrgTypeCheck, "overrideOrgTypeCheck");
		enableRerunDialog = getBoolean(enableRerunDialog, "enableRerunDialog");
		configureAtStart = getBoolean(configureAtStart, "configureAtStart");

		lightning = getBoolean(lightning, "lightning");
		
		remoteProvider = getString(remoteProvider, "remoteProvider");
		credentialsSource = getString(credentialsSource, "credentialsSource");
		remotePlatform = getString(remotePlatform, "remotePlatform");
		remoteBrowser = getString(remoteBrowser, "remoteBrowser");
		remoteBrowserVersion = getString(remoteBrowserVersion, "remoteBrowserVersion");
		targetEnvironments = getString(targetEnvironments, "targetEnvironments");
		dateTimeOverride = getString(dateTimeOverride, "dateTimeOverride");
		seedDate = getString(seedDate,"seedDate");
		cloudbeesUrl = getString(cloudbeesUrl, "cloudbeesUrl");
		cloudbeesPackagedUrl = getString(cloudbeesPackagedUrl, "cloudbeesPackagedUrl");
		
		try {
			dataClearDownAction = suite.getParameter("dataClearDownAction") != null ? CleardownType.valueOf(suite.getParameter("dataClearDownAction")) : dataClearDownAction;
		} catch(IllegalArgumentException e) { throw new ConfigurationException(suite.getParameter("dataClearDownAction") + " is an unsupported data cleardown action"); };
	}
	
	@JsonIgnore
	public ISuite suite;
	
	public TreeMap<String, Boolean> testsToRun = new TreeMap(); 
	
	public String getString(String aDefault, String paramName) { return suite.getParameter(paramName) != null ? suite.getParameter(paramName) : aDefault;  }
	public boolean getBoolean(boolean aDefault, String paramName) { return suite.getParameter(paramName) != null ? Boolean.parseBoolean(suite.getParameter(paramName)) : aDefault;  }
	
	public static List<String> nonOverridableFields = Arrays.asList(
		"configureAtStart",
		"cloudbeesPackagedUrl",
		"cloudbeesUrl",
		"credentialsSource",
		"overrideOrgTypeCheck",
		"seedDate",
		"targetEnvironments",
		"cloudbeesUrl",
		"cloudbeesPackagedUrl"
	);
	
	public void overrideWith(Config override) {
		if(override == null)
			return;
		Class cls = Config.class;
		try {
			for(Field f : cls.getDeclaredFields())
				if(!nonOverridableFields.contains(f.getName()) && // don't override excluded fields
						Modifier.isPublic(f.getModifiers()) && // override public fields
						!Modifier.isStatic(f.getModifiers()) && // don't override static fields
						!Collection.class.isAssignableFrom(f.getType()) && // don't override collections with reflection
						!Map.class.isAssignableFrom(f.getType()) && // don't override maps using reflection
						!ISuite.class.isAssignableFrom(f.getType()) && // don't override the suite parameter
						f.get(override) != null) // don't override with a null value
					f.set(this, f.get(override));
			this.testsToRun.replaceAll((k, v) -> { return override.testsToRun.get(k) != null ? override.testsToRun.get(k) : v; });
		} catch (Exception e) { throw new RuntimeException("Failed merge " + cls.getName(), e); }
	}
	
	public void overrideWithCommandLineArguments() {
		Class cls = Config.class;
		try {
			for(Field f : cls.getDeclaredFields()) {
				String value = System.getProperty(f.getName());
				if(!nonOverridableFields.contains(f.getName()) && // don't override excluded fields
						Modifier.isPublic(f.getModifiers()) && // override public fields
						!Modifier.isStatic(f.getModifiers()) && // don't override static fields
						General.isSimpleType(f.getType()) && // only override strings and primitive data types
						value != null) // don't override with a null value
				{
					General.setField(this, f, value);
				}
			}
			
			this.testsToRun.replaceAll((k, v) -> {
				String serialised = System.getProperty(k);
				boolean run = serialised != null ? Boolean.parseBoolean(serialised) : false;
				return run; 
			});
		} catch (Exception e) { throw new RuntimeException("Failed merge " + cls.getName(), e); }
	}
	
	public void writeToSystemProperties() {
		Class cls = Config.class;
		try {
			for(Field f : cls.getDeclaredFields()) {
				if(!nonOverridableFields.contains(f.getName()) && // don't write excluded fields
						Modifier.isPublic(f.getModifiers()) && // write public fields
						!Modifier.isStatic(f.getModifiers()) && // don't write static fields
						General.isSimpleType(f.getType()) && // only write strings and primitive data types
						f.get(this) != null) // don't write with a null value
				{
					System.setProperty(f.getName(), "" + f.get(this));
				}
			}
			
			this.testsToRun.forEach((k, v) -> { System.setProperty(k, "" + v); });
		} catch (Exception e) { throw new RuntimeException("Failed merge " + cls.getName(), e); }
	}
	
	public boolean
	remoteExecution = false,
	isRerun = false,
	validateExpectedResults = true,
	abortOnFirstValidationFailure = false,
	debugExecution = true,
	packagedOrg = false,
	overrideOrgTypeCheck = false,
	enableRerunDialog = false,
	configureAtStart= false;
	
	public boolean lightning = false;
	
	public String
	remoteProvider = DEFAULT_REMOTE_PROVIDER,
	credentialsSource = CREDENTIALS_SOURCE,
	remotePlatform = DEFAULT_REMOTE_PLATFORM,
	remoteBrowser = DEFAULT_REMOTE_BROWSER,
	remoteBrowserVersion = DEFAULT_REMOTE_BROWSER_VERSION,
	targetEnvironments = "markKimbleDev",
	dateTimeOverride = "",
	seedDate = "",
	cloudbeesUrl = "",
	cloudbeesPackagedUrl = "";
	
	// mobile setup
	public static final String ANDROID_MOBILE_OS = "Android";
	public static final String ANDROID_MOBILE_DEVICE_NAME = "Android GoogleAPI Emulator";
	public static final String ANDROID_MOBILE_PLATFORM_VERSION = "6.0";
	public static final String ANDROID_APPIUM_VERSION = "1.9.1";
	public static final String ANDROID_MOBILE_DEVICE_ORIENTATION = "portrait";
	
	public static final String IOS_MOBILE_OS = "iOS";
	public static final String IOS_MOBILE_DEVICE_NAME = "iPhone 8 Simulator";
	public static final String IOS_MOBILE_PLATFORM_VERSION = "12.2";
	public static final String IOS_APPIUM_VERSION = "1.13.0";
	public static final String IOS_MOBILE_NO_RESET = "true";
	
	public static final String DEFAULT_WEEK = "2013-09-27";
	public static final String STARTING_PERIOD = "--es \"CurrentDate\" "+DEFAULT_WEEK;
	
	public String
	androidPlatformName = ANDROID_MOBILE_OS,
	androidDeviceName = ANDROID_MOBILE_DEVICE_NAME,
	androidPlatformVersion = ANDROID_MOBILE_PLATFORM_VERSION,
	androidAppiumVersion = ANDROID_APPIUM_VERSION,
	androidDeviceOrientation = ANDROID_MOBILE_DEVICE_ORIENTATION,
	
	iOSPlatformName = IOS_MOBILE_OS,
	iOSDeviceName = IOS_MOBILE_DEVICE_NAME,
	iOSPlatformVersion = IOS_MOBILE_PLATFORM_VERSION,
	iOSAppiumVersion = IOS_APPIUM_VERSION,
	iOSNoReset = IOS_MOBILE_NO_RESET,
			
	platformName = ANDROID_MOBILE_OS,
	deviceName = ANDROID_MOBILE_DEVICE_NAME,
	platformVersion = ANDROID_MOBILE_PLATFORM_VERSION,
	appiumVersion = ANDROID_APPIUM_VERSION,
	deviceOrientation = ANDROID_MOBILE_DEVICE_ORIENTATION,
	
	startingPeriod = STARTING_PERIOD;
			
	public CleardownType dataClearDownAction = CleardownType.none;
	
	static int[][] practitestInstanceMap = {
			{101,10415980},{102,10415981},{103,10415982},{104,10415983},{105,10415984},{106,10415985},{107,10415986},{108,10415987},
			{109,10415988},{110,10415989},{111,10415990},{112,10415991},{113,10415992},{114,10415993},{115,10415994},{116,10415995},
			{117,10415996},{118,10415997},{119,10415998},{120,10415999},{121,10416000},{123,10416001},{124,10416002},{125,10416003},
			{126,10416004},{127,10416006},{128,10416007},{129,10416008},{130,10416009},{131,10416010},{132,10416011},{133,10416012},
			{134,10416013},{135,10416014},{136,10416015},{139,11460377},{140,12815380}
			};
	
}
