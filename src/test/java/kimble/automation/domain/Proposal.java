package kimble.automation.domain;

import java.util.List;

public class Proposal {
	public String stageName;
	public String accountName;
	public String name;
	public String acceptanceDate;

	public String businessUnit;
	public String proposition;

	public String forecastStatus;
	
	public String creationMode;

	public Double contractRevenue;
	public Double contractCost;
	public Double contractMargin;
	
	public List<Risk> risks;
	public List<DeliveryEngagement> deliveryEngagements;
}
