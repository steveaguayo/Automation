package kimble.automation.domain;

public class ResourcedActivityTemplate {
	public String name; 					//Template Name	
	public String type; 					//Resourced Activity Type
	public String utilisationRule; 			//Utilisation Rule	
	public String productDomain; 			//Product Domain		 	 
	public String planType; 				//Plan Type
	public boolean canSelfForecast; 		//Can Self Forecast?	
	public boolean approvalRequired;		//Approval Required?
	public boolean hasExternalPlan;			//Has External Plan	
	public String forecastUnitType;			//Forecast Unit Type
	public boolean hideForecastTimeEntries;	//Hide Forecast Time Entries	
	public String usageUnitType;			//Usage Unit Type
	public String defaultUsage;				//*Default Usage	
	public String usageForecastFormat;		//Usage Forecast Format
	public String usageActualFormat;		//*Usage Actual Format	
	public String usageAllocationType;		//Usage Allocation Type		 	 
	public String invoicingUnitType;		//Invoicing Unit Type		 	 
	public String calendarShowAs;			//Calendar Show As
	public boolean calendarIsPrivate;		//Calendar Is Private	
}
