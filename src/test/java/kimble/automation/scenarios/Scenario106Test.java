package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;


public class Scenario106Test extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario106")
	public void Scenario106(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);

		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts and sales opps", getSH(), data().accounts),
			validateForecasts(null, "postInitialSetup", getSH(), data()),
			
			confirmWonEnagagementsStage(null, "confirm won engagements", getSH(), salesOpp),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			validateForecasts(null, "postSalesOppWin", getSH(), data()),
			
			activateEngagements(null, "activate engagement", getSH(), salesOpp),
			runAllJobs(null, "run all jobs after activating engagement", getSH()),
			createChangeElementAndAssignmentsStage(null, "create the change element, assignments and milestones", getSH(), account, false),
			validateForecasts(null, "CR1", getSH(), data())
		);
	}
}