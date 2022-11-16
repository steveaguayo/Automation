package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario113Test extends KimbleOneTest {

	// this scenario reproduces the conditions for ticket #2962
	
	// create a fixed price DE for 4 days over 4 mths, derived utilisation
	// win and activate
	// book 5 days in the first month, causing the first month to be over recognised
	// edit effort to go to return rest to original forecast
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario113")
	public void Scenario113(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);

		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create account, sales opp, engagement, element and assignment", getSH(), data().accounts),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			validateForecasts(null, "postSalesOppWin", getSH(), data()),
			
			enterTime(null, "firstTimeBooking", getSH(), data().timesheets),
			validateForecasts(null, "postFirstTimeBooking", getSH(), data()),
			
			closeTrackingPeriodsStage(null, "close tracking periods post first time booking", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "close forecasting periods post first time booking", getSH(), data().forecastingPeriodActions),
			updateRemainingUsage(null, "update remaining usage stage 2", getSH(), salesOpp, 2),
			validateForecasts(null, "postJanuaryPeriodCloseAndUpdateRemainingUsage", getSH(), data()),
			
			closeTrackingPeriodsStage(null, "closeFebruaryPeriodWithoutBookingTime", getSH(), data().trackingPeriodActions),
			validateForecasts(null, "postFebruaryRemoveAllUsageAndCloseTracking", getSH(), data()),

			closeForecastingPeriodsStage(null, "closeFebruaryPeriodWithoutBookingTime", getSH(), data().forecastingPeriodActions),
			validateForecasts(null, "postFebruaryPeriodClose", getSH(), data()),

			scheduleActivities(null, "schedule activities", getSH(), salesOpp),
			validateForecasts(null, "postScheduledWork", getSH(), data()),
			
			updateRemainingUsage(null, "update remaining usage stage 3", getSH(), salesOpp, 3),
			sleep(null, "added a ten second sleep because for some reason there is a latency here in seeding jobs", getSH(), 10000),
			validateForecasts(null, "postUpdateToZeroRemainingUsage", getSH(), data())
		);
	}
}
