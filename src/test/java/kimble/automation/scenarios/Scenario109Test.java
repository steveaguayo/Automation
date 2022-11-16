package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario109Test extends KimbleOneTest {

	// TEST OVERVIEW:
	// open need created for 20 days T&M with two scheduled days
	// allocate two candidates [TODO: one of which has some scheduled unavailability]
	// Reject One candidate
	// Accept the other
	// check the forecast
	// book time and check actuals etc
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario109")
	public void Scenario109(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);

		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create account, sales opp, engagement, element and assignment", getSH(), data().accounts),
			validateForecasts(null, "postInitialSetup", getSH(), data()),
			
			scheduleActivities(null, "schedule work", getSH(), salesOpp),
			validateForecasts(null, "postScheduledWork", getSH(), data()),
			
			assignCandidatesStage(null, "assign candidates", getSH(), salesOpp),
			validateForecasts(null, "postCandidateAssignment", getSH(), data()),
			
			reviewCandidatesStage(null, "assign candidates", getSH(), salesOpp),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			validateForecasts(null, "postCandidateReviewAndSalesOppWin", getSH(), data()),
			
			enterTime(null, "book time", getSH(), data().timesheets),
			validateForecasts(null, "postTimeBooking", getSH(), data())
		);
	}
}
