package kimble.automation.scenarios;

import static kimble.automation.scenarios.Stages.activateEngagements;
import static kimble.automation.scenarios.Stages.createAccountsSalesOppsEngagementsAndElements;
import static kimble.automation.scenarios.Stages.navigateAndWinOpportunity;
import static kimble.automation.scenarios.Stages.validateForecasts;
import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class NoP1ForecastAfterWinning extends KimbleOneTest {

	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "NoP1ForecastAfterWinning")
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
			validateForecasts(null, "postSalesOppWin", getSH(), data())
		);
	}
}
