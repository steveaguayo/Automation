package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.util.Collection;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.Invoice;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario101Test extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario101")
	public void Scenario101(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	@Override
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);

		execute(
			createAccountsAndSalesOpportunities(null, "create accounts and sales opps", getSH(), data().accounts),
			createEngagementAndConfigureElements(null, "create engagement and configure elements", getSH(), engagement),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			runAllJobs(null, "run jobs before creating timesheets", getSH()),
			enterTime(null, "pre-CR1 enter time", getSH(), data().timesheets),
			runAllJobs(null, "run all jobs before attempting to invoice without purchase order", getSH()),
			attemptToInvoiceEnteredTimeWithoutPO(data().invoices, "pre-CR1 attempt to generate an invoice without having a purchase order"),
			generateInvoices(null, "pre-CR1 generate invoice", getSH(), data().invoices, true),
			createCreditNotes(null, "pre-CR1 credit the invoice", getSH(), data().invoices),
			createRevenueItemAdjustments(null, "create revenue item adjustments", getSH(), salesOpp.name, data().revenueItemAdjustments),
			generateInvoices(null, "pre-CR1 re-generate invoice", getSH(), data().invoices, false, false),

			validateForecasts(null, "pre-CR1", getSH(), data()),
			
			createChangeElementAndAssignmentsStage(null, "create change elements and assignments and activate", getSH(), data().accounts, true),
			runAllJobs(null, "run jobs before creating timesheets", getSH()),
			enterTime(null, "CR1 enter time", getSH(), data().timesheets),
			generateInvoices(null, "CR1 generate invoice", getSH(), data().invoices, true),

			validateForecasts(null, "CR1", getSH(), data())
		);
	}

	Stage attemptToInvoiceEnteredTimeWithoutPO(Collection<Invoice> invoices, String testStage) {
		Stage stage = new Stage(null, testStage);
		
		// invoicing requires timesheet completion jobs to complete, navigate to jobspending as a workaround for ticket #6016

		for (Invoice iv : invoices) {
			if(testStage == null || (testStage != null && iv.testStage != null && testStage.equals(iv.testStage)))
			{
				navigateToEngagementDashboardByName(stage, "navigate to engagement: " + iv.deliveryEngagementName, getSH(), iv.deliveryEngagementName);
				generateNewInvoiceWithNoPO(stage, "generate invoice without purchase order", getSH(), iv);
				
				// assert that an error message is present to indicate that you can't progress without assigning a PO
				String text = "Please check that Purchase Orders have been allocated to the Elements as per the Purchase Order Rule";
				verifyPageContainsText(stage, "verify the page contains the text: " + text, getSH(), text);
			}
		}
		return stage;
	}
	
}
