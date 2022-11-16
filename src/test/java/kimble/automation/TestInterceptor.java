package kimble.automation;

import java.util.logging.Level;
import java.util.logging.Logger;

import kimble.automation.helpers.General;
import kimble.automation.helpers.controlpanel.ControlPanel;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class TestInterceptor implements ISuiteListener {
	
	@Override
	public void onStart(ISuite suite) {
		Config config;
		try { 
			config = new Config(suite);

			configure(config);
			if(!config.packagedOrg && config.cloudbeesUrl != null && config.cloudbeesUrl.length() > 0)
				ControlPanel.fetchDumpsUI(config.cloudbeesUrl);
			else if(config.packagedOrg && config.cloudbeesPackagedUrl != null && config.cloudbeesPackagedUrl.length() > 0)
				ControlPanel.fetchDumpsUI(config.cloudbeesPackagedUrl);
			BaseTest.enableRerunDialog = config.enableRerunDialog;
			
			if(config.debugExecution) {
				Logger.getLogger("").setLevel(Level.INFO);
				// try and display the control panel and if it fails, do it silently
				try { ControlPanel.open(); } catch(Exception e) {
					e.printStackTrace();
				}
			}
			else {
				Logger.getLogger("").setLevel(Level.WARNING);
			}
			KimbleOneTest.automationSuiteSetup(config);
		} catch (Exception e) { throw new RuntimeException("Failed to configure the automation run", e); }
	}

	@Override
	public void onFinish(ISuite suite) {
		KimbleOneTest.logDetailedErrors();
		scheduleRerun();
	}
	
	public static void configure(Config config) {
		if(config.configureAtStart)
			ControlPanel.editConfiguration(config);
		else
			config.overrideWithCommandLineArguments();
		config.writeToSystemProperties();
	}
	
	/**
	 * This method will setup a properties file for a rerun or if no rerun should be scheduled it will remove the file if it exists
	 */
	public static void scheduleRerun() {
		// If there are no failures, no rerun is needed
		if(!BaseTest.hasFailures()) {
			General.deleteRerunSchedule();
			return;
		}
		boolean abortOnFirstValidationFailure = General.getSystemProperty(boolean.class, "abortOnFirstValidationFailure");
		// If the suite has been set to abort on first failure, don't spoil the state of the orgs by rerunning suite
		if(abortOnFirstValidationFailure) {
			General.deleteRerunSchedule();
			return;
		}
		boolean isRerun = General.getSystemProperty(boolean.class, "isRerun");
		// If the latest run was a re-run, but didn't abort on first failure, re-run again and this time abort on first failure.
		// If the latest run wasn't a re-run, re-run without aborting on first failure.
		General.setRerunProperty("abortOnFirstValidationFailure", "" + isRerun);
		// Finally set the re-reun property to true and save the property file
		General.setRerunProperty("isRerun", "" + true);
		General.saveRerunSchedule();
	}
}
