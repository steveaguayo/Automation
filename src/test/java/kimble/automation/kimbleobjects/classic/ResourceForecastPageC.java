package kimble.automation.kimbleobjects.classic;

import java.util.List;
import java.util.stream.Collectors;

import kimble.automation.domain.ExpectedValue;
import kimble.automation.domain.Fact;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class ResourceForecastPageC extends BasePageC {
	
	// assignment columns
	private static final String START_DATE = "Start Date";
	private static final String P1_FORECAST_END_DATE = "Forecast P1 End Date";
	private static final String P2_FORECAST_END_DATE = "Forecast P2 End Date";
	private static final String P3_FORECAST_END_DATE = "Forecast P3 End Date";
	
	// usage columns
	private static final String USAGE_TIME_PERIOD = "Time Period";
	private static final String USAGE_BUSINESS_UNIT = "BusinessUnit";
	private static final String USAGE_ACCOUNT = "Account";
	private static final String USAGE_SALES_OPP = "Sales Opportunity";
	private static final String USAGE_DELIVERY_GROUP = "Delivery Group";
	private static final String USAGE_DELIVERY_ELEMENT = "Delivery Element";
	private static final String USAGE_RESOURCED_ACTIVITY = "Resourced Activity";
	private static final String USAGE_RESOURCE = "Resource";
	private static final String USAGE_RESOURCE_PERIOD = "Resource Period";
	private static final String USAGE_ACTUAL = "Actual";
	private static final String USAGE_P1FORECAST = "P1 Forecast";
	private static final String USAGE_P2FORECAST = "P2 Forecast";
	private static final String USAGE_P3FORECAST = "P3 Forecast";
	
	private static final String PASSED = "Passed";
	private static final String ASSIGNMENTS = "Assignments";
	private static final String USAGEFORECAST = "UsageForecast";
	
	ResourceForecastObjC performanceAnalysisDetail;
	
	String assignmentJson;
	String usageForecastJson;
	
	private static final String assignmentJsonSelector = "div[id='assignmentsJson']";
	private static final String usageForecastJsonSelector = "div[id='usageAnalysesJson']";
	
	public ResourceForecastPageC(SeleniumHelper seleniumHelperInstance) {
		super(seleniumHelperInstance);
	}
	
	public void Initialise() {
		readWebElements();
		performanceAnalysisDetail = new ResourceForecastObjC(assignmentJson, usageForecastJson);
	}

	private void readWebElements() {
		// resource forecast can get very long as the tests run instead of reading the web elements we read a hidden Json value that
		// maps to the automation test representation of each forecast elemeent
		// first unhide the Json strings
		theSH.displayElementsByClassName("automationHelper");

		// the read them from the screen
		assignmentJson = theSH.getWebElement(By.cssSelector(assignmentJsonSelector)).getText();
		usageForecastJson = theSH.getWebElement(By.cssSelector(usageForecastJsonSelector)).getText();
	}
	
	public boolean ValidateFact(String testStage, Fact factDetail, ExpectedValue expectedValueDetail) throws Exception {
		String factName = factDetail.factName;
		String ignoreReason = factDetail.ignoreReason;
		String measure = expectedValueDetail.measure;
		String expectedValue = expectedValueDetail.value;
				
		String actualValue = "";
		String result = PASSED;
		boolean returnValue = true;
		
		if (ignoreReason != null && ignoreReason != "") {
			theSH.logIgnoreFact(testStage, factName, ignoreReason, measure);
		} else {

			if (factName.equals(ASSIGNMENTS)) {
				AssignmentObjC theAssignment;
				theAssignment = GetAssignment(factDetail);
				
				if(theAssignment == null)
				{
					theSH.invalidExpectedResultsConfiguration(factName, measure);
				}
				else
				{
					if (measure.equals(START_DATE) && theAssignment.startDate != null) {
							actualValue = theAssignment.startDate;
					} else if (measure.equals(P1_FORECAST_END_DATE) && theAssignment.p1ForecastEndDate != null) {
						actualValue = theAssignment.p1ForecastEndDate;
					} else if (measure.equals(P2_FORECAST_END_DATE) && theAssignment.p2ForecastEndDate != null) {
						actualValue = theAssignment.p2ForecastEndDate;
					} else if (measure.equals(P3_FORECAST_END_DATE) && theAssignment.p3ForecastEndDate != null) {
						actualValue = theAssignment.p3ForecastEndDate;
					} else if (measure.equals(P3_FORECAST_END_DATE) && theAssignment.p3ForecastEndDate != null) {
						actualValue = theAssignment.p3ForecastEndDate;
					}
				}
			}
			else if (factName.equals(USAGEFORECAST)) {
				UsageForecastObjC theUsageForecast;
				theUsageForecast = GetUsageForecast(factDetail);
				
				if(theUsageForecast == null)
				{
					theSH.invalidExpectedResultsConfiguration(factName, measure);
				}
				else
				{				
					if (measure.equals(USAGE_TIME_PERIOD) && theUsageForecast.period != null) {
							actualValue = theUsageForecast.period;
					} else if (measure.equals(USAGE_BUSINESS_UNIT) && theUsageForecast.businessUnitName != null) {
						actualValue = theUsageForecast.businessUnitName;
					} else if (measure.equals(USAGE_ACCOUNT) && theUsageForecast.accountName != null) {
						actualValue = theUsageForecast.accountName;
					} else if (measure.equals(USAGE_SALES_OPP) && theUsageForecast.salesOpportunityName != null) {
						actualValue = theUsageForecast.salesOpportunityName;
					} else if (measure.equals(USAGE_DELIVERY_GROUP) && theUsageForecast.deliveryEngagementName != null) {
						actualValue = theUsageForecast.deliveryEngagementName;
					} else if (measure.equals(USAGE_DELIVERY_ELEMENT) && theUsageForecast.deliveryElementName != null) {
						actualValue = theUsageForecast.deliveryElementName;
					} else if (measure.equals(USAGE_RESOURCED_ACTIVITY) && theUsageForecast.resourcedActivityName != null) {
						actualValue = theUsageForecast.resourcedActivityName;
					} else if (measure.equals(USAGE_RESOURCE) && theUsageForecast.resourceName != null) {
						actualValue = theUsageForecast.resourceName;
					} else if (measure.equals(USAGE_RESOURCE_PERIOD) && theUsageForecast.resourcePeriod != null) {
						actualValue = theUsageForecast.resourcePeriod;
					} else if (measure.equals(USAGE_ACTUAL) && theUsageForecast.actual != null) {
						actualValue = theUsageForecast.actual;
					} else if (measure.equals(USAGE_P1FORECAST) && theUsageForecast.p1ForecastUsage != null) {
						actualValue = theUsageForecast.p1ForecastUsage;
					} else if (measure.equals(USAGE_P2FORECAST) && theUsageForecast.p2ForecastUsage != null) {
						actualValue = theUsageForecast.p2ForecastUsage;
					} else if (measure.equals(USAGE_P3FORECAST) && theUsageForecast.p3ForecastUsage != null) {
						actualValue = theUsageForecast.p3ForecastUsage;
					}
				}
			}
			else {
				theSH.invalidExpectedResultsConfiguration(factName, measure);
			}
			
			returnValue = (actualValue.toString().equals(expectedValue));

			if (!returnValue) {
				result = "FAILED";
			}

			theSH.logValidationResult(testStage, factName, measure, expectedValue, actualValue, result);
		}
		return returnValue;
	}

	public AssignmentObjC GetAssignment(final Fact factDetail){
		List<AssignmentObjC> filteredList = performanceAnalysisDetail.assignments.stream().filter((candidate) -> {
	        return candidate.proposalName.equals(factDetail.salesOpportunityName)
	            && candidate.deliveryEngagement.equals(factDetail.deliveryEngagementName)
	            && candidate.deliveryElement.equals(factDetail.deliveryElementName)
	            && candidate.activity.equals(factDetail.activityName);
		}).collect(Collectors.toList());
		
		if (filteredList != null && filteredList.iterator() != null && filteredList.iterator().hasNext())
			return filteredList.iterator().next();
		else
			return null;
	}
	
	public UsageForecastObjC GetUsageForecast(final Fact factDetail){
		List<UsageForecastObjC> filteredList = performanceAnalysisDetail.usageForecast.stream().filter((candidate) -> {
	        return candidate.period.equals(factDetail.period)
	            && candidate.businessUnitName.equals(factDetail.businessUnitName)
	            && candidate.deliveryEngagementName.equals(factDetail.deliveryEngagementName)
	            && candidate.deliveryElementName.equals(factDetail.deliveryElementName)
	            && candidate.resourcedActivityName.equals(factDetail.activityName);
		}).collect(Collectors.toList());
		
		if (filteredList != null && filteredList.iterator() != null && filteredList.iterator().hasNext())
			return filteredList.iterator().next();
		else
			return null;
	}
}
