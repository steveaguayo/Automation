package kimble.automation.scenarios;



import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.kimbleobjects.classic.PagesC;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;


public class Scenario114Test extends KimbleOneTest {
	
	// WAR Delivery Engagement through to invoicing time and expense
	// WAR status defined as having 25% contingency
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario114")
	public void Scenario114(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws ParseException, Exception {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		
		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts and sales opps", getSH(), data().accounts),
			navigateMenu(null, "navigate to originating proposal", getSH(), PagesC.PROPOSALITEMS),
			switchToDetailedForecastLevelStage(null, "switch to detailed forecast level", getSH()),
			enableWorkingAtRiskAndActivate(null, "enable working at risk", getSH(), salesOpp.name, salesOpp.deliveryEngagements),
			
			runAllJobs(null, "run all jobs after activating engagements", getSH()),
			
			enterTime(null, "enter time", getSH(), data().timesheets),
			enterExpenses(null, "enterExpenses", getSH(), data().expenseClaims),
			validateForecasts(null, "preRateReduction", getSH(), data()),
			
			// update the rates to reduce by 10%
			applyRateReductionStage(null, "apply rate reduction", getSH(), salesOpp),
			validateForecasts(null, "postRateReduction", getSH(), data()),
			
			// change the product on delivery element to fixed price
			updateProductStage(null, "update product", getSH(), salesOpp),
			// need to create milestones for new fixed price project
			createMilestonesForDeliveryElements(null, "create step 2 revenue milestones for delivery elements", getSH(), salesOpp.name, engagement),
			validateForecasts(null, "postProductChange", getSH(), data()),
			
			allocateAllPurchaseOrdersStage(null, "allocate new purchase orders", getSH(), data().invoices),
			
			// only the fixed price + expenses should be invoiceable
			// first check only the expense is invoiceable (since the FP milestone is complete
			generateInvoices(null, "preMilestoneComplete", getSH(), data().invoices, false),
			
			navigateAndWinOpportunity(null, "win the opportunity", getSH(), salesOpp),
			validateForecasts(null, "postSalesOppWin", getSH(), data()),
			
			completeMilestonesStage(null, "complete milestones", getSH(), salesOpp),
			generateInvoices(null, "postMilestoneComplete", getSH(), data().invoices, false),
			validateForecasts(null, "postInvoicing", getSH(), data())
		);

	}
}