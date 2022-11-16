package kimble.automation.domain;

import java.util.List;

public class ActivityUnavailability {
	public String resourceName;
	public Boolean groupAssignmentsEnabled;
	public String resourceRole;
	public String revenueRate;
	public String costRate;
	public String usageBehaviourRule;
	public String startDate;
	public String endDate;
	public String remainingEffort;
	public String remainingEffortStage2;
	public String remainingEffortStage3;
	public String utilisationPct;
	public boolean costOnlyAssignment = false;
	public String location;
	
	// groupAssignmentTemplate fields
	public String templateName;
	public String groupName;
	public String entryUnits;
	
	public List<ScheduledActivity> scheduledActivity;
	public List<Resource> candidateResources;
	public List<MonthlyProfile> monthlyProfiles;

	// for change assignments
	public String scopedWith;
}
