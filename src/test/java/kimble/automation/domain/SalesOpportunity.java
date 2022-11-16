package kimble.automation.domain;

import java.util.List;

public class SalesOpportunity {
	public String name;
	public String businessUnit;
	public String source;
	public String proposition;
	public String responseRequiredDate;
	public String closeDate;
	public String winDate;
	
	public String lostCloseDate;
	public String lostReason;
	public String lostNarrative;

	public String forecastStatus;
	
	public List<DeliveryEngagement> deliveryEngagements;
	
	public List<DeliveryDateBulkEdit> deliveryDateEdits;
	
	public List<ActivityAssignment> bidTeamActivityAssignments;

	public List<Risk> risks;
	
	// used as containers for any changes to the sales opp during the course of a test
	// would be specified in the yml input if required and test needs to know
	// that stepx properties should be used when relevant
	public String step2BusinessUnit;
	public String step2CloseDate;
	
	// OCS
	
	public Double contractRevenue;
	public Double contractCost;
	public Double contractMargin;
}
