package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.util.Collection;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.Invoice;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario199Test extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario199")
	public void Scenario199(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	@Override
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);

		execute(
				
				createSalesOpportunities(null, "create sales opp", getSH(), data().accounts), 
				winOpportunity(null, "win the opportunity", getSH(), salesOpp.winDate),
				runInLineJobsLDV(null, "run proposal jobs in line", getSH(), salesOpp),
				testPageLoads(null, "test engagement page loading", getSH(), engagement.name),
				verifyNoFailedJobs(null, "verify that no failed jobs exist in the org", getSH())
				
		);
	}
}
