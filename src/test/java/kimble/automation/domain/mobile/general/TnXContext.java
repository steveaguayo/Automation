package kimble.automation.domain.mobile.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kimble.automation.domain.mobile.SettingsMob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TnXContext{

	public enum ExecutionMode { Live, RoboTest, InstrumentationTest  }
	
	public static final String settingsPath = "settings.json";
	public static final String settingsTestPath = "settings_test.json";
	static boolean initialised = false;
	public static final int dbVersion = 2;

	static String liveDbName = "TnX.db";
	static String testDbName = "TnX_test.db";

//	static Context applicationContext;

	public static ObjectMapper mapper;
	static Databases.KimbleDbHelper db;

	static SettingsMob settings;

	static {
		initObjectMapper();
		initSettings();
		initialised = true;
	}

	public static boolean isInitialised() { return initialised; };

	static void initObjectMapper() {
		mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		mapper.setConfig(mapper.getSerializationConfig().
				without(SerializationFeature.FAIL_ON_EMPTY_BEANS)	);
	}

	public static File openFile(String aPath) {
		return new File("mobileFiles" + aPath);
	}

	public static File openExternalCacheFile(String aName) {
		File file;
		try {
			file = new File( "mobileFiles/cache", aName);
			file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException("Failed to access the external cache", e);
		}
		return file;
	}

	public static File openExternalFile(String aPath) {
		File topfolder = new File("mobileFiles/Kimble");
		if(!topfolder.exists())
			try {
				topfolder.createNewFile();
			} catch (IOException e) { throw new RuntimeException("Failed to access the external storage", e); }
		return new File(topfolder, aPath);
	}

	public static File createTempImage(String aPrefix, String aSuffix) throws Exception {
		File dir = new File("mobileFiles/pictures");
		return File.createTempFile(createTempFileName(aPrefix), aSuffix, dir);
	}

	public static String createTempFileName(String aPrefix) { return aPrefix + "_" + System.currentTimeMillis(); }

	public static File openSettingsFile() throws Exception{
		if(getExecutionMode() == ExecutionMode.RoboTest || getExecutionMode() == ExecutionMode.InstrumentationTest)
			return openFile(settingsTestPath);
		else
			return openFile(settingsPath);
	}

	public static File getReportFile(String aName) {
		File file = TnXContext.openExternalCacheFile(aName);
		file.setReadable(true, false);
		return file;
	}

	static void initSettings() {
		try {
		File f = openSettingsFile();
		if(!f.exists())
			f.createNewFile();
		FileInputStream fs = new FileInputStream(f);
		if(fs.available() > 0) {
			try {
				settings = TnXContext.getMapper().readValue(fs, SettingsMob.class);
			} catch (Exception e) {}
		}

		if(settings == null)
			settings = new SettingsMob();

		fs.close();
		} catch(Exception e) {
			throw new RuntimeException("Failed to initialise settings", e);
		}
	}
	
	// close
	public static void close(){
		try {
			saveSettings();
		} catch (Exception e) {
			System.err.println("Failed to close the TnXContext object");
			e.printStackTrace();
		}
	}
	
	public static void saveSettings() throws Exception {
		File f = openSettingsFile();
		if(!f.exists())
			f.createNewFile();
		FileOutputStream fs = new FileOutputStream(f);
		settings.runningId = db.runningId;
		TnXContext.getMapper().writerWithDefaultPrettyPrinter().writeValue(fs, settings);
		fs.flush();
		fs.close();
	}

	public static ExecutionMode getExecutionMode() {
		try {
			return Class.forName("com.kimble.timeandexpense2.KT") != null ? ExecutionMode.RoboTest :
					Class.forName("com.kimble.timeandexpense2.KInstrumentation") != null ? ExecutionMode.InstrumentationTest : ExecutionMode.Live;
		} catch(Exception e) {}
		try {
			return Class.forName("com.kimble.timeandexpense2.KInstrumentation") != null ? ExecutionMode.InstrumentationTest : ExecutionMode.Live;
		} catch(Exception e) {
			return ExecutionMode.Live;
		}
	}

	public static ObjectMapper getMapper() { return mapper; }

	public static DateFormat getDateFormat() { return getMapper().getDateFormat(); }

//	public static Context getInstance() { return applicationContext; }

	public static void setLabels(Properties aLabels) { settings.labels = aLabels; }
	public static Properties getLabels() { return settings.labels; }

	public static void addNonBusinessDays(List<String> aNonBusinessDays) {
		if (settings.nonBusinessDays == null)
			settings.nonBusinessDays = new ArrayList();
		settings.nonBusinessDays.addAll(aNonBusinessDays);
	}
	public static void setNonBusinessDays(List<String> aNonBusinessDays) { settings.nonBusinessDays = aNonBusinessDays; }
	public static List<String> getNonBusinessDays() { return settings.nonBusinessDays; }

	public static void setAllowCompanyPaidExpenses(boolean aAllowCompanyPaidExpenses) { settings.allowCompanyPaidExpenses = aAllowCompanyPaidExpenses; }
	public static boolean getAllowCompanyPaidExpenses() { return settings.allowCompanyPaidExpenses; }

}
