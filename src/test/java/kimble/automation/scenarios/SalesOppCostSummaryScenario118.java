package kimble.automation.scenarios;

import static kimble.automation.scenarios.Stages.configureDeliveryEngagementsInOcs;
import static kimble.automation.scenarios.Stages.createAccountsAndSalesOpportunities;
import static kimble.automation.scenarios.Stages.createRisksInDashboard;
import static kimble.automation.scenarios.Stages.navigateMenu;
import static kimble.automation.scenarios.Stages.validateSalesOpportunityInOcs;

import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.kimbleobjects.classic.PagesC;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class SalesOppCostSummaryScenario118 extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "SalesOppCostSummaryScenario118")
	public void SalesOppCostSummaryScenario118Test(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		List<DeliveryEngagement> engagements = salesOpp.deliveryEngagements;
		
		execute(
			createAccountsAndSalesOpportunities(null, "create accounts and sales opps", getSH(), data().accounts),
			
			// Create, edit, delete and validate risks before winning the opportunity
			navigateMenu(null, "navigate to proposal risk dashboard", getSH(), PagesC.PROPOSALRISKDASHBOARD),
			createRisksInDashboard(null, "create 'high' commercial risk", getSH(), salesOpp.risks),
			
			// Create, edit, delete and validate the engagement and it's elements in the OCS screens before winning the opportunity
			navigateMenu(null, "navigate to proposal items", getSH(), PagesC.PROPOSALITEMS),
			configureDeliveryEngagementsInOcs(null, "config engagements elements", getSH(), engagements),
			validateSalesOpportunityInOcs(null, "validate sales opportunity after configuration", getSH(), data().expectedResults)
		);
	}
	
}
