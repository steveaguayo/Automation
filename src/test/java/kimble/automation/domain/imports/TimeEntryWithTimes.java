package kimble.automation.domain.imports;

import java.util.Arrays;
import java.util.List;

import kimble.automation.domain.mobile.general.TnXContext;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeEntryWithTimes {

	
	@JsonProperty("Resource")
	public String resource;
	@JsonProperty("Hours")
	public String hours;
	@JsonProperty("Date")
	public String date;
	@JsonProperty("Start Time")
	public String startTime;
	@JsonProperty("End Time")
	public String endTime;
	@JsonProperty("Project")
	public String project;
	
	// Run this to see an example serialisation result in the console
	public static void main(String[] argv) throws Exception {
		TimeEntryWithTimes te = new TimeEntryWithTimes();
		List<TimeEntryWithTimes> list = Arrays.asList(te);
		TnXContext.getMapper().writerWithDefaultPrettyPrinter().writeValue(System.out, list);
	}
	
}
