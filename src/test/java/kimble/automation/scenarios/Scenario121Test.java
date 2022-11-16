package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;
import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario121Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario121")
	public void Scenario121(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		List<DeliveryEngagement> engagements = salesOpp.deliveryEngagements;
		
		execute(
			closeTrackingPeriodsStage(null, "closeTrackingBeforeStarting", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "closeForecastingBeforeStarting", getSH(), data().forecastingPeriodActions),
				
			createAccountsAndSalesOpportunities(null, "create accounts and sales opps", getSH(), data().accounts),
			addAllOptionalDeliveryElementsInOcs(null, "add all optional delivery elements", getSH(), engagements),
			configureDeliveryEngagementsInOcs(null, "config engagements elements", getSH(), engagements),
			switchToDetailedForecastLevelStage(null, "switch to detailed forecast level", getSH()),

			validateForecasts(null, "preSalesOppWin", getSH(), data()),
		
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			runAllJobs(null, "run all jobs after activation", getSH()),
			
			enterTime(null, "firstTimeBooking", getSH(), data().timesheets),
			enterExpenses(null, "firstExpenseBooking", getSH(), data().expenseClaims),
			runAllJobs(null, "run all jobs before milesone completion", getSH()),
			completeMilestonesStage(null, "completeMilestonesForPeriodClose", getSH(), salesOpp),
			
			validateForecasts(null, "postFirstTimeAndExpenseBooking", getSH(), data()),
			
			closeTrackingPeriodsStage(null, "closeTrackingPostFirstBooking", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "closeForecastingPostFirstBooking", getSH(), data().forecastingPeriodActions),
			
			validateForecasts(null, "postClosePeriods", getSH(), data()),
			
			enterTime(null, "secondTimeBooking", getSH(), data().timesheets),
			editEngagementExpenseForecast(null, "amend expense forecast", getSH(), salesOpp),
			enterExpenses(null, "secondExpenseBooking", getSH(), data().expenseClaims),
			runAllJobs(null, "run all jobs before milesone completion", getSH()),
			completeMilestonesStage(null, "completeMilestonesPostPeriodClose", getSH(), salesOpp),
			
			validateForecasts(null, "postSecondTimeExpenseBooking", getSH(), data()),
			
			// additional test: unable to close period when unactualised forecast exists
			openForecastingPeriodsStage(null, "closeForecastingVerifyError", getSH(), data().forecastingPeriodActions),
			createUnactualisedPerformanceAnalysisRecord(null, "create unactualised performance analysis record", getSH(), data().performanceAnalysis.get(0), true),
			verifyErrorInForecastingPeriodCloseStage(null, "closeForecastingVerifyError", getSH(), data().forecastingPeriodActions),
			
			createUnactualisedPerformanceAnalysisRecord(null, "update performance analysis record: actual = forecast", getSH(), data().performanceAnalysis.get(1), false),
			closeForecastingPeriodsStage(null, "closeForecastingVerifyError", getSH(), data().forecastingPeriodActions)
			
		);
	}
}
