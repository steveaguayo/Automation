package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario111Test extends KimbleOneTest {

	// TEST OVERVIEW:
	// Create a sales opp and a single DE for a Tracked Plan product
	// Create a number of resource pool assignments
	// Create tasks and assign to resources
	// win and activate
	// confirm the forecast
	// book actuals against tasks
	// confirm the forecast revenue recognition and plan effort to go
	
	// TODO: We need to test that if a period close happens half way through the plan
	// that the revenue recognition calculation is based on that point onwards
	// (either in this test or another test)

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario111")
	public void Scenario111(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
//		testNameLoginFormat = testName + "-" + getTestIndex();
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception
	{
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		
		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts, sales opps, engagements, elements and assignments", getSH(), data().accounts),
			createTasksAndTaskAssignmentsStage(null, "create initial plan tasks and task assignments", getSH(), salesOpp),

			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			validateForecasts(null, "postInitialSetup", getSH(), data()),
			
			enterTime(null, "firstTimeBooking", getSH(), data().timesheets),
			
			// we have to manually trigger jobspending here as otherwise the actual isn't on the tasks
			// being updated - if this works then review with Stu as shouldn't have to do this and didn't
			// need to do this in < 1.23
			runAllJobs(null, "we have to manually trigger jobspending here as otherwise the actual isn't on the tasks being updated - 1", getSH()),
			
//			updateTaskEstimatesStage(null, "postFirstTimeBooking", getSH(), salesOpp),
			validateForecasts(null, "postFirstTimeBooking", getSH(), data()),
			closeTrackingPlanStage(null, "close the tracking plan after first time booking", getSH(), salesOpp, true),
			
			enterTime(null, "secondTimeBooking", getSH(), data().timesheets),
			
			// we have to manually trigger jobspending here as otherwise the actual isn't on the tasks
			// being updated - if this works then review with Stu as shouldn't have to do this and didn't
			// need to do this in < 1.23
			runAllJobs(null, "we have to manually trigger jobspending here as otherwise the actual isn't on the tasks being updated - 1", getSH()),
			
			updateTaskEstimatesStage(null, "update task estimates post second time booking", getSH(), salesOpp),
			validateForecasts(null, "postSecondTimeBooking", getSH(), data()),
			
			closeTrackingPlanStage(null, "close the tracking plan after second time booking", getSH(), salesOpp, true),
			enterTime(null, "thirdTimeBooking", getSH(), data().timesheets),
			
			// we have to manually trigger jobspending here as otherwise the actual isn't on the tasks
			// being updated - if this works then review with Stu as shouldn't have to do this and didn't
			// need to do this in < 1.23
			runAllJobs(null, "we have to manually trigger jobspending here as otherwise the actual isn't on the tasks being updated - 1", getSH()),
			
			updateTaskEstimatesStage(null, "update task estimates post third time booking", getSH(), salesOpp),
			validateForecasts(null, "postThirdTimeBooking", getSH(), data()),
			closeTrackingPlanStage(null, "close the tracking plan after third time booking", getSH(), salesOpp, true)
		);
	}
}