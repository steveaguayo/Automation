package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario112Test extends KimbleOneTest {

	// TEST OVERVIEW:
	// Create a sales opp and a single T&M DE
	// Create an assignment for each of the entry criteria (usage behaviour rules)
	// confirm the forecast
	// use the monthly grid to make a series of changes to the profile of the assignments
	// confirm the forecast
	// schedule some days
	// use the monthly grid to make a series of changes to the profile of the assignments
	// confirm the forecast
	// book some actuals
	// use the monthly grid to make a series of changes to the profile of the assignments
	// confirm the forecast
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario112")
	public void Scenario112(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
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

			profileMonthlyAssignmentsStage(null, "firstReprofile", getSH(), salesOpp),
			validateForecasts(null, "postFirstReprofile", getSH(), data()),
			
			scheduleActivities(null, "schedule activities", getSH(), salesOpp),
			profileMonthlyAssignmentsStage(null, "secondReprofile", getSH(), salesOpp),
			validateForecasts(null, "postSecondReprofile", getSH(), data())
		);
	}
}
