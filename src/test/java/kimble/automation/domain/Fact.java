package kimble.automation.domain;

import java.util.List;

public class Fact {
	public String factName;
	public String ignoreReason;
	public String salesOpportunityName;
	public String deliveryEngagementName;
	public String deliveryElementName;
	public String secondaryDeliveryElementName;
	public String activityName;
	public String businessUnitName;
	public String period;
	public String domainClass;
	public String resourceName;
	public String receivableDate;
	
	// Trend Analysis
	public String factCard;
	public String thisValue;
	public String lastValue;
	public String changeValue;
	public String info;
	public String impact;

	
	public List<ExpectedValue> expectedValues;
}
