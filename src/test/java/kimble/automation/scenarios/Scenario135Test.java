package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.ExpenseClaim;
import kimble.automation.domain.ExpenseDetail;
import kimble.automation.domain.ExpenseEntry;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.domain.TimeEntry;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.kimbleobjects.classic.MobileSelectors;
import kimble.automation.KimbleOneTest;

import org.openqa.selenium.By;
import org.testng.annotations.Test;


import io.appium.java_client.AppiumDriver;

public class Scenario135Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario135")
	public void Scenario135(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
        AppiumDriver driver = getAppiumDriver();
        MobileSelectors selectors = getMobileSelectors();
		String OS = data().userCredentials.get(0).operatingSystem;

		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		String resourceName = engagement.deliveryElements.get(0).activityAssignments.get(0).resourceName;
		ExpenseClaim expenseClaim = data().expenseClaims.get(0);
		ExpenseEntry expenseEntry = expenseClaim.expenseEntries.get(0);
		ExpenseDetail expenseDetail = expenseEntry.expenseDetails.get(0);
		List<TimeEntry> te = data().timesheets.get(0).timeEntries;
        
		execute( 			
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts, sales opps, engagements, elements and assignments", getSH(), data().accounts),
			setActivityUsageUnits(null, "set usage units to day", getSH(), engagement, "Day"),
			setTimeAndExpenseApprovalRules(null, "set approval rules", getSH(), engagement),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), data().accounts.get(0).salesOpportunities.get(0)),
			activateEngagements(null, "activate engagements", getSH(), data().accounts.get(0).salesOpportunities.get(0)),
			savePeriodRateBand(null, "open and save rate band", getSH(), engagement),
			runAllJobs(null, "run jobs initial", getSH()),  
			
			//mobile
			mobileEnterTimesheet(null, "timeBooking", getSH(), data().timesheets.get(0), driver, selectors, OS),
			uncheckAndSubmitTime(null, "submit selected time entry", getSH(), driver, selectors, OS),
			
			//web
			navigateFromAnywhereToAllTabs(null, "re-navigate to run all jobs", getSH()),
			runAllJobs(null, "run jobs after time and expense submission", getSH()),  
			pendingApprovalsTimesheet(null, "reject timesheet", getSH(), engagement.deliveryElements.get(0).name, false),
			runAllJobs(null, "run jobs", getSH()),
			
			//mobile
			syncMobile(null, "sync before deleting timesheets", getSH(), driver, selectors, OS),
			mobileScrollToPeriod(null, "scroll to the correct week", getSH(), data().timesheets.get(0), driver, selectors),
			deleteAllTimeEntries(null, "delete all time entry", getSH(), driver, selectors, OS),
			navigateFromAnywhereToAllTabs(null, "navigate to all tabs to keep browser alive", getSH()),
			mobileEnterExpenses(null, "enter expenses on mobile", getSH(), expenseClaim, expenseDetail, driver, selectors, OS),
			submitAll(null, "submit all expenses", getSH(), driver, selectors, OS),
			
			//web
			runAllJobs(null, "run jobs before final validations", getSH()),
			navigateToResourceTimesheet(null, "navigate to resource timesheet", getSH(), resourceName),
			validateNoDraftTimeEntries(null, "timeBooking", getSH(), data().timesheets.get(0)),
			navigateFromAnywhereToTab(null, "nav to expenses tab", getSH(), "Expense Claims"),
			navigateToExpense(null, "nav to expense", getSH(), expenseEntry, expenseDetail)
		);
	}
}
