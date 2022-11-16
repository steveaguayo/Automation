package kimble.automation.domain;

import java.util.List;
import java.util.Map;

public class Journey {
	
	public String name;
	public String resourceName;
	
	public List<JourneyLeg> legs;
	public List<JourneyAllowanceAdjustment> adjustments;
	public List<JourneyAllowanceAllocation> allocations;
	
	//     Map<stageName, Map<period, amount>>
	public Map<String, Map<String, String>> allowanceAmountValidations;
	
	public List<ExpenseEntry> expectedExpenseEntries;
	
}

