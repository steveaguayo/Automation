package kimble.automation.domain;

import java.util.List;

public class Task {
	public String testStage;
	public String name;
	public boolean fixedEffort = false;
	public String startDate;
	public String endDate;
	
	public List<TaskAssignment> taskAssignments;
}
