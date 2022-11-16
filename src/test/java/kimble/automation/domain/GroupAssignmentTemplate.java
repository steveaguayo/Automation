package kimble.automation.domain;

import java.util.List;

public class GroupAssignmentTemplate {
	public String templateName;
	public String groupName;
	public String startDate;
	public String endDate;
	public String usageBehaviourRule;
	public String utilisationPct;	
	public String entryUnits;
	
	public List<ScheduledActivity> scheduledActivity;
}
