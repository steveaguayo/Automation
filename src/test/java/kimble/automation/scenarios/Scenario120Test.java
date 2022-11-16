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

public class Scenario120Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario120")
	public void Scenario120(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		List<DeliveryEngagement> engagements = salesOpp.deliveryEngagements;
		
		execute(
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
			completeMilestonesStage(null, "completeMilestonesForPeriodClose", getSH(), salesOpp),
			
			validateForecasts(null, "postFirstTimeAndExpenseBooking", getSH(), data()),
			
			closeTrackingPeriodsStage(null, "closeTrackingPostFirstBooking", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "closeForecastingPostFirstBooking", getSH(), data().forecastingPeriodActions),
			
			validateForecasts(null, "postClosePeriods", getSH(), data()),
			
			enterTime(null, "secondTimeBooking", getSH(), data().timesheets),
			enterExpenses(null, "secondExpenseBooking", getSH(), data().expenseClaims),
			completeMilestonesStage(null, "completeMilestonesPostPeriodClose", getSH(), salesOpp),
			
			validateForecasts(null, "postSecondTimeExpenseBooking", getSH(), data())
		);
	}
}
