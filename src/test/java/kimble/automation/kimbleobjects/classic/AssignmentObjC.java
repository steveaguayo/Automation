package kimble.automation.kimbleobjects.classic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignmentObjC {
	public String recordID;
	public String proposalName;
	public String deliveryEngagement;
	public String deliveryElement;
	public String activity;
	public String resource;
	public String activityAssignmentType;
	public String startDate;
	public String p1ForecastEndDate;
	public String p2ForecastEndDate;
	public String p3ForecastEndDate;
}
