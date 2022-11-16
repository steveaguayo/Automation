package kimble.automation.domain;

import java.util.List;

public class DeliveryElement {
	// Link the element to a stage in the test flow
	public String stage;

	public String name;
	public String id;
	public String step2Name;
	
	public String product;
	public String step2Product;
	public String taxCode;
	
	public String purchaseOrderRule;
	
	public boolean activate = true;
	
	public List<BulkEdit> bulkEdits;
	
	public List<ActivityAssignment> activityAssignments;

	public List<Task> tasks;
	
	public List<Annuity> annuities;
	
	public List<Milestone> revenueMilestones;
	public List<Milestone> step2RevenueMilestones;

	public List<Milestone> costMilestones;
	
//	public List<AccountCredits> accountCredits;
	
	public List<AccountCredits> accountCredits;

	// change control related properties
	public String parentElementName;
	public boolean operatedWithPrimary = false;
	public String changeEffective;
	public String changeOrderBehaviourRule;
	
	// OCS VALUES
	// possible values: target, expected and usage
	public String forecastMode;

	public Double servicesRevenue;
	public Double servicesCost;
	public Double servicesCostOverridden;

	public Double expensesRevenue;
	public Double expensesCost;
	
	public String productExtension;
	public Double targetMargin;
	public Double expectedUsage;
	
	public String startDate;
	public String endDate;
	
	public List<AssignmentLineItem> configurableAssignmentLineItems;
	public List<AssignmentLineItem> createableAssignmentLineItems;
	public List<ExpenseLineItem> expenseLineItems;
//	public List<Milestone> milestoneLineItems;

	public String rateItemName;
	public Double rateDefault;
	public Double rateOverride;
	public Double rateSourceValue;
	
//  values for Credit allocation
	public String creditAllocation;
	public String allocatedValue;
	public String unallocatedValue;
	public String creditValue;
	
}
