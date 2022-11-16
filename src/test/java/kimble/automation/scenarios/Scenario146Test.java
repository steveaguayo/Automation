package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;


public class Scenario146Test extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario146")
	public void Scenario146(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		SalesOpportunity salesOpp2 = account.salesOpportunities.get(1);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		DeliveryEngagement engagement2 = salesOpp2.deliveryEngagements.get(0);
		
		
		execute(
			createAccountsAndSalesOpportunities(null, "create accounts and sales opps", getSH(), data().accounts),
			createEngagementAndConfigureElements(null, "create engagement and configure elements", getSH(), engagement2),
			navigateFromAnywhereToSalesOppScope(null,"naviagate to proposal items", getSH(), salesOpp.name),
			createEngagementAndConfigureElements(null, "create engagement and configure elements 2", getSH(), engagement),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp2),
			validateForecasts(null, "sales opp win credit sold", getSH(), data()),
			validateForecasts(null, "sales opp win credit allocation opp", getSH(), data()),
			allocateCredit(null, "allocate credit", getSH(), salesOpp2.name, engagement2),
			activateEngagements(null, "activate engagement with credits allocated", getSH(), salesOpp2),
			runAllJobs(null, "run jobs before creating timesheets", getSH()),
			enterTimeEnhancedTimesheet(null, "enhanced time entry", getSH(), data().timesheets),
			validateForecasts(null, "post time booked", getSH(), data()),
			setPurchaseOrderRules(null, "set purchase order rule for credit allocation", getSH(), salesOpp2.name, engagement2),
			generateInvoices(null, "generate invoice with credit allocation included", getSH(), data().invoices, false), 
			validateForecasts(null, "post invoicing", getSH(), data())
		);
	}
}
