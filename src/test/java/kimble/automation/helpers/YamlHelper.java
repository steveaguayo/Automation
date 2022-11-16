package kimble.automation.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kimble.automation.domain.KimbleData;
import kimble.automation.domain.TestState;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlHelper {
	public static final String FULL_DATETIME_FORMAT = "ddMMyyyy HHmmss";
	public static final String FULL_YEAR_FORMAT = "yyyy";
	public static final String FULL_MONTH_FORMAT = "MMMMM";
	
	public static final String YAML_EXTENSION = ".yml";
	public static final String YAMLINPUTTOKEN_DATETIME = "<dt>";	
	public static final String YAMLINPUTTOKEN_CURRENTMONTHINFULL = "<currentMonthFull>";	
	public static final String YAMLINPUTTOKEN_CURRENTYEARINFULL = "<currentYearFull>";
	
	public static Date today;
	
	public static final String YAMLINPUTTOKEN_TESTNAME = "<testName>";	
	
	private static String dateTimeOverride = "";
	private static Map<String, String> seedDateMap = new HashMap();

	private boolean initialised;
	
	public YamlHelper() {
		super();
	}
	
	public YamlHelper(String dateTimeOverrideIn, Map<String, String> seedDateMapIn) {
		super();
		YamlHelper.dateTimeOverride = dateTimeOverrideIn;
		YamlHelper.seedDateMap = seedDateMapIn;
	}
	
	public Object[][] getYamlData(String credentialsFilename, String testFilename) throws Exception {
		
		initialise();
		
		Object[][] testData=null;
		int counter = 0;
		
		List<KimbleData> credentialsDataList = getDataFromYamlFile(credentialsFilename);
		List<KimbleData> testDataList = getDataFromYamlFile(testFilename);
		
	    testData = new Object[testDataList.size()][2];
		
	    for (KimbleData kimbleTestInput : testDataList) {
	    	testData[counter][0] = credentialsDataList.get(0);
	    	testData[counter][1] = kimbleTestInput;
	    	counter++;
		}
		
		return testData;
	}

	public List<KimbleData> getDataFromYamlFile(String testFilename) throws FileNotFoundException, Exception {
		File inputFile = FileUtils.getFile("src", "test", "resources", testFilename + YAML_EXTENSION);
		InputStream yamlInputFile = new FileInputStream(inputFile);
		InputStream parsedInputFile = prepareInputFile(testFilename, yamlInputFile);
		
		List<KimbleData> testDataList = loadTestData(parsedInputFile);
		return testDataList;
	}
	
	@SuppressWarnings("resource")
	public static InputStream prepareInputFile(String testName, InputStream yamlInputFile) {
		// the input Yaml file can contain a series of tokens if we want the test
		// to be dynamic, replace these tokens
		InputStream parsedInputFile = new SearchAndReplaceInputStream(yamlInputFile, YAMLINPUTTOKEN_DATETIME, getFormattedCurrentDateTime(FULL_DATETIME_FORMAT));
		parsedInputFile = new SearchAndReplaceInputStream(parsedInputFile, YAMLINPUTTOKEN_CURRENTMONTHINFULL, getFormattedCurrentDateTime(FULL_MONTH_FORMAT));
		parsedInputFile = new SearchAndReplaceInputStream(parsedInputFile, YAMLINPUTTOKEN_CURRENTYEARINFULL, getFormattedCurrentDateTime(FULL_YEAR_FORMAT));
		
		parsedInputFile = new SearchAndReplaceInputStream(parsedInputFile, YAMLINPUTTOKEN_TESTNAME, testName);
		
		// if this is automation of data into a demo environment then we will have been passed
		// a map of seedDate tokens and respective dates
		if(seedDateMap.size() > 0)
		{
			parsedInputFile = processSeedDates(parsedInputFile);
		}
			
		return parsedInputFile;
	}
	
	public static InputStream processSeedDates(InputStream parsedInputFile) {
		for(String seedDateToken : seedDateMap.keySet())
		{
			parsedInputFile = new SearchAndReplaceInputStream(parsedInputFile, seedDateToken, seedDateMap.get(seedDateToken));			
		}
		
		return parsedInputFile;
	}

	public static String getFormattedCurrentDateTime(String timeFormat) {
		String formattedDateTime;
		DateFormat df = new SimpleDateFormat(timeFormat);
		
		// default to the value of the overriden date time if specified, otherwise we'll derive it (the normal operation)
		if(today == null)
			today = Calendar.getInstance().getTime();
		if(!dateTimeOverride.equals(""))
		{
			// overridden date is in the format: 26112013 215406 - process this as if it were today
			SimpleDateFormat formatter = new SimpleDateFormat(FULL_DATETIME_FORMAT);
			try
			{
				today = formatter.parse(dateTimeOverride);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
		
		formattedDateTime = df.format(today);
		
		return formattedDateTime;
	}
	
	public String generateStateFileName(String scenario, String date) {
		return scenario + "-" + date + YAML_EXTENSION;
	}

	public TestState recoverState(String scenario, String date) throws Exception {
		return recoverState(new File("dumps/" + generateStateFileName(scenario, date)));
	}
	
	public TestState recoverState(File dumpFile) throws Exception {
		if(dumpFile == null || !dumpFile.exists())
			return null;
		Yaml yaml = new Yaml(new Constructor(TestState.class));
		return (TestState)yaml.load(new FileInputStream(dumpFile));
	}
	
	public List<KimbleData> loadTestData(InputStream yamlInputFile) throws Exception {
		Yaml yaml = new Yaml(new Constructor(KimbleData.class));
	    List<KimbleData> testDataList = new ArrayList();

	    for (Object testData : yaml.loadAll(yamlInputFile)) {
	    	testDataList.add((KimbleData) testData);
		}
	    
		return testDataList;
	}
	
	public void dumpYamlToFile(KimbleData testData, String fileName) throws IOException {
	    dumpObjectToYaml(testData, testData.getClass(), fileName);
	}

	public void dumpStateToYaml(TestState state, String scenario, String date) throws IOException {
	    dumpObjectToYaml(state, state.getClass(), generateStateFileName(scenario, date));
	}

	public void dumpObjectToYaml(Object obj, Class<?> cls, String fileName) throws IOException {
		File dumpDir = new File("dumps");
		if(!dumpDir.exists())
			dumpDir.mkdir();
	    FileWriter fileWriter = new FileWriter("dumps/" + fileName);
	    StringWriter stringWriter = new StringWriter();
	    
	    Yaml yaml = new Yaml(new Constructor(cls));
	    yaml.dump(obj,stringWriter);
	    
	    fileWriter.write(stringWriter.toString());
	    fileWriter.close();
	}

	private void initialise() throws Exception {
		if(!initialised){
			// Startup
			// Startup
			// Startup
			initialised = true;
		}
	}
	
	public void closeDown(){
		if(initialised){
			// Tear down
			// Tear down
			// Tear down
			initialised = false;
		}
	}
}
