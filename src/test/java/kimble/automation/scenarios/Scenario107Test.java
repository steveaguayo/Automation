package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario107Test extends KimbleOneTest {

	// TEST OVERVIEW:
	// Create a sales opp and DE with a group assignment - Time and Materials Product
	// (group resources are in different business units)
	// Check that forecast is based on the contracted revenue value
	// Win and activate
	// Check that the forecast is based on the sum of the group revenue and cost
	// Schedule specific resources and hours
	// Check the forecast - shouldn't be affected by scheduled time
	// Book time and expense
	// Check actuals - actual cost should be resource grade cost
	// invoice the booked time, check the invoice values

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario107")
	public void Scenario107(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);

		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create account, sales opp, engagement, element and assignment", getSH(), data().accounts),
			validateForecasts(null, "postInitialSetup", getSH(), data()),
			
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			validateForecasts(null, "postSalesOppWin", getSH(), data()),

			scheduleActivities(null, "schedule activities", getSH(), salesOpp),
			validateForecasts(null, "postScheduledWork", getSH(), data()),
			
			enterTime(null, "book time", getSH(), data().timesheets),
			validateForecasts(null, "postTimeBooking", getSH(), data()),
			
			generateInvoices(null, "allocate purchase orders and invoice", getSH(), data().invoices, true),
			validateForecasts(null, "postInvoicing", getSH(), data())
		);
	}
}
