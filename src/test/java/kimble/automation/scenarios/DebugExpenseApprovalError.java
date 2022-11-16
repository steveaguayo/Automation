package kimble.automation.scenarios;

import static kimble.automation.scenarios.Stages.*;

import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class DebugExpenseApprovalError extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "debugExpenseApprovalError")
	public void Scenario(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	@Override
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		List<DeliveryEngagement> engagements = salesOpp.deliveryEngagements;
		
		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts, sales opps, engagements and elements", getSH(), data().accounts),
			navigateFromAnywhereToSalesOppScope(null, "navigate back to sales opportunity scope", getSH(), salesOpp.name),
			switchToDetailedForecastLevelStage(null, "switch to detailed forecast level", getSH()),
			enableWorkingAtRiskAndActivate(null, "enable working at risk", getSH(), salesOpp.name, engagements),
			runAllJobs(null, "run all jobs after activation", getSH()),
			
			enterTime(null, "enterTime", getSH(), data().timesheets),
			enterExpenses(null, "enterExpenses", getSH(), data().expenseClaims),
			runAllJobs(null, "run all jobs after entering time and expenses", getSH()),
			abort(null, "enterExpenses")
		);
	}
}
