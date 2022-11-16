package kimble.automation.domain;

import java.util.List;

public class ExpenseEntry {
	// link the object to a test stage
	public String testStage;
	
	public String localId;
	public String[] statuses;
	public String name;
	public String incurredDate;
	public String activityName;
	public String notes;
	public List<ExpenseDetail> expenseDetails;

	public String monthBeginning;
	public String incurredDay;
}
