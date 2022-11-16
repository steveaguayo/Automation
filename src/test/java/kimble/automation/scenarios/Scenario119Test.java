package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.domain.TimeEntry;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario119Test extends KimbleOneTest {
	
	// TEST OVERVIEW:
	// Create a sales opp and a single T&M DE
	// Create an assignment for each of the "standard" usage behaviour rules
	// validate that no activity is returned before activating the engagement(s)
	// confirm the forecast
	// schedule some time
	// confirm the forecast
	// book actuals using rest requests - underbook in a period
	// confirm expected behaviour for each assignment
	// book actuals using rest requests - overbook in a period
	// confirm expected behaviour for each assignment
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario119")
	public void Scenario119(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		List<DeliveryEngagement> engagements = salesOpp.deliveryEngagements;
		TimeEntry firstTimeEntry = data().timesheets.get(0).timeEntries.get(1);
		
		execute(
			createAccountsAndSalesOpportunities(null, "create accounts and sales opps", getSH(), data().accounts),
			
			// Configure and validate the engagement and it's elements in the OCS screens before winning the opportunity
			configureDeliveryEngagementsInOcs(null, "config engagements elements", getSH(), engagements),
			validateSalesOpportunityInOcs(null, "validate sales opportunity after configuration", getSH(), data().expectedResults),
			
			// Win the opportunity and validate results
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			
			navigateFromAnywhereToTimesheetDayInTnx(null, "Navigate to date 07/01/2013", state, getSH(), firstTimeEntry.startDate),
			validateNoActivitiesAvailableForPeriod(null, "validate that no activities are available for entering time", getSH(), data().timesheets.get(0).timeEntries),

			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			runAllJobs(null, "Run all jobs after activation", getSH()),
			
			navigateFromAnywhereToTimesheetDayInTnx(null, "Navigate to date 07/01/2013 2", state, getSH(), firstTimeEntry.startDate),
			enterTimesheets(null, "enter time", getSH().getWD(), getCredentials(), state, data().timesheets),
			refreshPage(null, "refresh page 1", getSH()),
			validateTimesheets(null, "validate timesheets after entering", getSH(), state, data().timesheets),
			editTimesheets(null, "edit time", getSH().getWD(), getCredentials(), state, data().timesheets),
			refreshPage(null, "refresh page 2", getSH()),
			validateTimesheets(null, "validate timesheets after editing", getSH(), state, data().timesheets),
			deleteTimesheets(null, "delete time", getSH().getWD(), getCredentials(), state, data().timesheets),
			refreshPage(null, "refresh page 3", getSH()),
			
			enterExpenseClaims(null, "enter claims", getSH().getWD(), getCredentials(), state, data().expenseEntries),
			editExpenseClaims(null, "edit claims", getSH().getWD(), getCredentials(), state, data().expenseEntries),
			deleteExpenseClaims(null, "delete claims", getSH().getWD(), getCredentials(), state, data().expenseEntries),
			
			addExpenseItems(null, "add expense items", getSH().getWD(), getCredentials(), state, data().expenseEntries),
			refreshPage(null, "refresh page 5", getSH()),
			validateExpenseClaims(null, "validate claims after adding items", getSH(), getCredentials(), state, data().expenseEntries),
			editExpenseItems(null, "edit expense items", getSH().getWD(), getCredentials(), state, data().expenseEntries),
			refreshPage(null, "refresh page 6", getSH()),
			validateExpenseClaims(null, "validate claims after editing items", getSH(), getCredentials(), state, data().expenseEntries),
			deleteExpenseItems(null, "delete expense items", getSH().getWD(), getCredentials(), state, data().expenseEntries),
			refreshPage(null, "refresh page 7", getSH()),
			
			validateForecasts(null, "validate sales forecast after entering, editing and deleteing time and expenses", getSH(), data()),		
			
			navigateFromAnywhereToTimesheetDayInTnx(null, "Navigate to date 07/01/2013 3", state, getSH(), firstTimeEntry.startDate),
			submitTimeEntries(null, "submit time", getSH().getWD(), getCredentials(), state, data().timesheets),
			sleep(null, "wait for time entry auto-approval to complete", getSH(), 2000),
			refreshPage(null, "refresh page 8", getSH()),
			validateTimesheets(null, "validate timesheets after submitting", getSH(), state, data().timesheets),
			
			submitExpenseClaims(null, "submit claims", getSH().getWD(), getCredentials(), state, data().expenseEntries),
			sleep(null, "wait for claim auto-approval to complete", getSH(), 2000),
			refreshPage(null, "refresh page 9", getSH()),
			validateExpenseClaims(null, "validate claims after submitting", getSH(), getCredentials(), state, data().expenseEntries),
			
			validateForecasts(null, "validate sales forecast after submitting time and expenses", getSH(), data())			
		);
	}
}