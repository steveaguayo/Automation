package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.ExpenseClaim;
import kimble.automation.domain.ExpenseDetail;
import kimble.automation.domain.ExpenseEntry;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.kimbleobjects.classic.MobileSelectors;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;


import io.appium.java_client.AppiumDriver;

public class Scenario133Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario133")
	public void Scenario133(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
        AppiumDriver driver = getAppiumDriver();
        MobileSelectors selectors = getMobileSelectors();
		String OS = data().userCredentials.get(0).operatingSystem;

		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		ExpenseClaim expenseClaim = data().expenseClaims.get(0);
		ExpenseEntry expenseEntry = expenseClaim.expenseEntries.get(0);
		ExpenseDetail expenseDetail = expenseEntry.expenseDetails.get(0);
		ExpenseDetail expenseDetailVerify = data().expenseClaims.get(2).expenseEntries.get(0).expenseDetails.get(0);
		
		execute( 			

			navigateAndCheckExpenseCategoryTaxCode(null, "disable tax code", getSH(), "Mileage", false),
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts, sales opps, engagements, elements and assignments", getSH(), data().accounts),
			setTimeAndExpenseApprovalRules(null, "set approval rules", getSH(), engagement),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), data().accounts.get(0).salesOpportunities.get(0)),
			activateEngagements(null, "activate engagements", getSH(), data().accounts.get(0).salesOpportunities.get(0)),
			runAllJobs(null, "run jobs after set up", getSH()),  
			
			//mobile
			mobileEnterTimesheet(null, "enter time on mobile", getSH(), data().timesheets.get(0), driver, selectors, OS),
			submitAll(null, "submit all time", getSH(), driver, selectors, OS),
			navigateFromAnywhereToAllTabs(null, "navigate to all tabs to keep browser alive", getSH()),
			mobileEnterExpenses(null, "enter expenses on mobile", getSH(), expenseClaim, expenseDetail, driver, selectors, OS),
			submitAll(null, "submit all expenses", getSH(), driver, selectors, OS),
			
			//web
			runAllJobs(null, "run jobs after time and expense submission", getSH()),  
			navigateFromAnywhereToTab(null, "navigate to timesheet", getSH(), "Timesheets"),
			pendingApprovalsTimesheet(null, "reject timesheet", getSH(), engagement.deliveryElements.get(0).name, false),
			reenterTimesheet(null, "re submit timesheet", getSH(), data().timesheets.get(1), data().timesheets.get(1).timeEntries.get(0)),
			
			navigateFromAnywhereToTab(null, "navigate to Expense Claim", getSH(), "Expense Claims"),
			navigateToExpenseClaimAndReject(null, "reject claim", getSH(), data().expenseClaims.get(1)),
			createExpenseTaxCode(null, "create tax code for expense", getSH(), "Mileage", engagement, salesOpp),
			navigateAndCheckExpenseCategoryTaxCode(null, "enable tax code", getSH(), "Mileage", true),
			runAllJobs(null, "run jobs", getSH()) ,
			
			//mobile
			switchToMobileTimeView(null, "switch to time view", getSH(), driver, selectors, OS),
			syncMobile(null, "sync mobile", getSH(), driver, selectors, OS),
			verifyMobileTimesheet(null, "verify  mobile timesheet", getSH(), data().timesheets.get(2), driver, selectors, OS),
			
			switchToExpensesView(null, "switch to expenses", getSH(), driver, selectors, OS),
			selectExistingExpense(null, "select expense", getSH(), expenseEntry, driver, selectors, OS),
			validateExpenseTaxCode(null, "validate tax code", getSH(), expenseDetailVerify, driver, selectors, OS),
			submitAll(null, "resubmit expense", getSH(), driver, selectors, OS),

			//web
			runAllJobs(null, "run jobs before approval", getSH()),  
			navigateFromAnywhereToTab(null, "navigate to timesheet", getSH(), "Timesheets"),
			pendingApprovalsTimesheet(null, "approve timesheet", getSH(), engagement.deliveryElements.get(0).name, true),
			
			navigateFromAnywhereToTab(null, "navigate to Expense Claim", getSH(), "Expense Claims"),
			pendingApprovalsExpense(null, "approve expense claim", getSH(), engagement.deliveryElements.get(0).name, true),
			navigateAndCheckExpenseCategoryTaxCode(null, "disable tax code", getSH(), "Mileage", false),
			runAllJobs(null, "run jobs del", getSH())
		);
	}
}
