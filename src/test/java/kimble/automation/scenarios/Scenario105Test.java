package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.domain.Timesheet;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;


public class Scenario105Test extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario105")
	public void Scenario105(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		
		Timesheet ts = data().timesheets.get(0);
		
		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts and sales opps", getSH(), data().accounts),
			validateForecasts(null, "postInitialSetup", getSH(), data()),
			
			confirmWonEnagagementsStage(null, "confirm won engagements", getSH(), salesOpp),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			validateForecasts(null, "postSalesOppWin", getSH(), data()),
			
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			enterTime(null, "silent jobs test", getSH(), data().timesheets),
			validateTimesheetSubmissionSilentApproval(null, "enter timesheet and verify silent jobs submits", getSH(), ts)
		);
	}
}