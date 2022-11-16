package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario116Test extends KimbleOneTest {

	// TEST OVERVIEW:
	// Create a sales opp and a single DE for a Tracked Plan product
	// Create a group assignments
	// Create tasks and assign to the group
	// win and activate
	// confirm the forecast
	// book actuals against tasks for each of the members of the group
	// confirm the forecast revenue recognition and plan effort to go
	
	// note this is similar to scenario111 but instead of specific resources we are testing
	// the behaviour for resources within a group assignment
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario116")
	public void Scenario116(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws ParseException, Exception {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		
		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts and sales opps", getSH(), data().accounts),
			createTasksAndTaskAssignmentsStage(null, "initialPlan", getSH(), salesOpp),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			validateForecasts(null, "postInitialSetup", getSH(), data()),
			
			enterTime(null, "firstTimeBooking", getSH(), data().timesheets),
			updateTaskEstimatesStage(null, "update task estimates post first time booking", getSH(), salesOpp),
			validateForecasts(null, "postFirstTimeBooking", getSH(), data()),
			closeTrackingPlanStage(null, "close tracking plan 1", getSH(), salesOpp, true),
			
			enterTime(null, "secondTimeBooking", getSH(), data().timesheets),
			updateTaskEstimatesStage(null, "update task estimates post second time booking", getSH(), salesOpp),
			validateForecasts(null, "postSecondTimeBooking", getSH(), data()),
			closeTrackingPlanStage(null, "close tracking plan 2", getSH(), salesOpp, true),
			
			enterTime(null, "thirdTimeBooking", getSH(), data().timesheets),
			updateTaskEstimatesStage(null, "update task estimates post third time booking", getSH(), salesOpp),
			validateForecasts(null, "postThirdTimeBooking", getSH(), data()),
			closeTrackingPlanStage(null, "close tracking plan 3", getSH(), salesOpp, true)
		);
	}
}