package kimble.automation.kimbleobjects.classic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceForecastObjC extends PerformanceAnalysisObjC {

	public List<AssignmentObjC> assignments = new ArrayList();
	public List<UsageForecastObjC> usageForecast = new ArrayList();
	
	public ResourceForecastObjC(String assignmentsJson, String usageForecastJson) {
		processAssignmentJson(assignmentsJson);
		processUsageForecastJson(usageForecastJson);
	}

	private void processAssignmentJson(String assignmentsJson) {
		// map assignmentsJson to a list of AssignmentObj
		ObjectMapper mapper = new ObjectMapper();
		try {
			AssignmentObjC[] allAssignmentsAsArray = mapper.readValue(assignmentsJson, AssignmentObjC[].class);
			assignments = Arrays.asList(allAssignmentsAsArray);
		} catch (Exception e) {
			throw new RuntimeException("The AssignmentObj array deserialisation failed", e);
		}
	}
	
	private void processUsageForecastJson(String usageForecastJson) {
		ObjectMapper mapper = new ObjectMapper();  
		try {
			UsageForecastObjC[] allUsageForecastsAsArray = mapper.readValue(usageForecastJson, UsageForecastObjC[].class);
			usageForecast = Arrays.asList(allUsageForecastsAsArray);
		} catch (Exception e) {
			throw new RuntimeException("The AssignmentObj array deserialisation failed", e);
		}
	}
}
