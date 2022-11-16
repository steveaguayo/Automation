package kimble.automation.kimbleobjects.classic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsageForecastObjC {
	public String recordID;
	public String period;
	public String businessUnitName;
	public String accountName;
	public String salesOpportunityName;
	public String deliveryEngagementName;
	public String deliveryElementName;
	public String resourcedActivityName;
	public String resourceName;
	public String resourcePeriod;
	public String actual;
	public String p1ForecastUsage;
	public String p2ForecastUsage;
	public String p3ForecastUsage;
}
