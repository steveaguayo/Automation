package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;


public class Scenario108Test extends KimbleOneTest {

	// TEST OVERVIEW:
	// (group resources are in different business units)
	// Create a sales opp and DE with a group assignment and a specific assignment for a Fixed Price Product
	// Check that forecast is based on the contracted revenue value
	// Win and activate
	// Check that the forecast is based on the sum of the group revenue and cost
	// Schedule specific resources and hours
	// Check the forecast - shouldn't be affected by scheduled time
	// Book time
	// Check actuals - actual cost should be resource grade cost
	// book a second round of time and check that the forecasts are updated in line
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario108")
	public void Scenario108(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);

		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create account, sales opp, engagement, element and assignment", getSH(), data().accounts),
			validateForecasts(null, "postInitialSetup", getSH(), data()),
			
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			validateForecasts(null, "postSalesOppWin", getSH(), data()),

			scheduleActivities(null, "schedule activities", getSH(), salesOpp),
			validateForecasts(null, "postScheduledWork", getSH(), data()),
			
			enterTime(null, "firstTimeBooking", getSH(), data().timesheets),
			validateForecasts(null, "postFirstTimeBooking", getSH(), data()),
			
			enterTime(null, "secondTimeBooking", getSH(), data().timesheets),
			validateForecasts(null, "postSecondTimeBooking", getSH(), data())
		);
	}
}
