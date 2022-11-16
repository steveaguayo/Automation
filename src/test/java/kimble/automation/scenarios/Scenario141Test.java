package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario141Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario141")
	public void Scenario141(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {		
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		SalesOpportunity salesOpp2 = account.salesOpportunities.get(1);
		
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		
		execute(
 			createAccountsSalesOppsEngagementsAndElements(null, "create accounts and sales opps", getSH(), data().accounts),
 			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
 			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp2),
			validateForecasts(null, "postInitialSetup", getSH(), data())
		);
	}

}