package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.Account;
import kimble.automation.domain.Annuity;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario124Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario124")
	public void Scenario124(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOppInitial = account.salesOpportunities.get(0);
		DeliveryEngagement engagementInitial = salesOppInitial.deliveryEngagements.get(0);
		SalesOpportunity salesOppMoreUsers = account.salesOpportunities.get(1);
		DeliveryEngagement engagementMoreUsers = salesOppMoreUsers.deliveryEngagements.get(0);
		SalesOpportunity salesOppMoreUsersAndSandbox = account.salesOpportunities.get(2);
		DeliveryEngagement engagementMoreUsersAndSandbox = salesOppMoreUsersAndSandbox.deliveryEngagements.get(0);
		DeliveryEngagement engagementNegativeSandbox = salesOppMoreUsersAndSandbox.deliveryEngagements.get(1);
		
		Annuity annuityToExtend1 = account.salesOpportunities.get(1).deliveryEngagements.get(0).deliveryElements.get(0).annuities.get(0);
		Annuity annuityToExtend2 = account.salesOpportunities.get(2).deliveryEngagements.get(0).deliveryElements.get(0).annuities.get(0);
		
		execute(
			createAccount(null, "create account", getSH(), account),
			createSalesOpportunitiyEngagementsAndElements(null, "create initial contract sales opp, engagements, elements and annuities", getSH(), account, salesOppInitial),
			navigateAndWinOpportunity(null, "win initial contract opportunity", getSH(), salesOppInitial),
			validateForecasts(null, "postInitialSetup", getSH(), data()),

			setPurchaseOrderRules(null, "set the purchase order rules", getSH(), salesOppInitial.name, engagementInitial),
			generateInvoices(null, "create invoice for first quarter", getSH(), data().invoices, true),
			validateForecasts(null, "postFirstInvoice", getSH(), data()),

			createSalesOpportunitiyEngagementsAndElements(null, "add 15 more consultant users", getSH(), account, salesOppMoreUsers),
			navigateAndWinOpportunity(null, "win opportunity for adding consultant licenses", getSH(), salesOppMoreUsers),
			validateForecasts(null, "postSecondOppSetup", getSH(), data()),

			setPurchaseOrderRules(null, "set the purchase order rules for second quarter", getSH(), salesOppMoreUsers.name, engagementMoreUsers),
			generateInvoices(null, "create invoice for second quarter", getSH(), data().invoices, true),
				
// COMPLETE & EXTEND	
//			completeAnnuityPeriods(null, "complete new annuity", getSH(), salesOppMoreUsers.name, engagementMoreUsers),
//			extendAnnuities(null, "extend annuity", getSH(), annuityToExtend1),
			
			validateForecasts(null, "postSecondInvoice", getSH(), data()),
			createSalesOpportunitiyEngagementsAndElements(null, "add 10 more flexible users", getSH(), account, salesOppMoreUsersAndSandbox),
			navigateAndWinOpportunity(null, "win opportunity for adding flexible licenses", getSH(), salesOppMoreUsersAndSandbox),
			validateForecasts(null, "postThirdOppSetup", getSH(), data()),

			setPurchaseOrderRules(null, "set the purchase order rules for third quarter", getSH(), salesOppMoreUsersAndSandbox.name, engagementMoreUsersAndSandbox),
			generateInvoices(null, "create invoice for third quarter", getSH(), data().invoices, true),
			createCreditNotes(null, "create credit note", getSH(), data().invoices.get(2)),
			completeAnnuityPeriods(null, "navigate to engagement to complete annuities and create revenue adjustments", getSH(),
					salesOppMoreUsersAndSandbox.name, engagementMoreUsersAndSandbox),	
// EXTEND ANNUITY
//			navigateToEngagementDashboardThroughSalesOpp(null, "navigate to engagement dashboard", getSH(), salesOppMoreUsersAndSandbox.name, engagementMoreUsersAndSandbox),
//			extendAnnuities(null, "extend annuity", getSH(), annuityToExtend2),		
			
			createRevenueItemAdjustments(null, "create revenue item adjustments", getSH(), salesOppMoreUsersAndSandbox.name, data().revenueItemAdjustments),
			validateForecasts(null, "postRevenueAdjustment", getSH(), data()),
			navigateToEngagementDashboardViaSalesOpp(null, "navigate to engagement to configure negative annuity", getSH(), 
					salesOppMoreUsersAndSandbox.name, engagementNegativeSandbox.name),
			configureElements(null, "configure elements", getSH(), engagementNegativeSandbox.deliveryElements),
			generateInvoices(null, "create invoice for fourth quarter", getSH(), data().invoices, true),

			validateForecasts(null, "postNegativeAnnuity", getSH(), data())
		);
	}

}
