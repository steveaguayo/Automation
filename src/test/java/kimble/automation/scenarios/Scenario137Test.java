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
import kimble.automation.kimbleobjects.classic.MobileSelectors;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;


import io.appium.java_client.AppiumDriver;

public class Scenario137Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario137")
	public void Scenario137(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
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
        
		List<ExpenseEntry> expenseList = data().expenseClaims.get(0).expenseEntries;
		
		execute( 			

			navigateAndCheckExpenseCategoryTaxCode(null, "disable tax code", getSH(), "Mileage", false),
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts, sales opps, engagements, elements and assignments", getSH(), data().accounts),
			setTimeAndExpenseApprovalRules(null, "set approval rules", getSH(), engagement),
			// setup time categories
			navigateAndWinOpportunity(null, "win opportunity", getSH(), data().accounts.get(0).salesOpportunities.get(0)),
			activateEngagements(null, "activate engagements", getSH(), data().accounts.get(0).salesOpportunities.get(0)),
			runAllJobs(null, "run jobs initial", getSH()),  
			
			//mobile
			// actualise all forecast
			submitAll(null, "submit all time", getSH(), driver, selectors, OS),
			
			//web
			runAllJobs(null, "run jobs after time and expense submission", getSH()),  
			navigateFromAnywhereToTab(null, "navigate to timesheet", getSH(), "Timesheets"),
			approveTimesheet(null, "reject timesheet", getSH(), data().timesheets.get(0)),

			//mobile
			syncMobile(null, "sync mobile", getSH(), driver, selectors, OS),
			// add receipt submission to expense
			// add tax code entry to expense submission
			mobileEnterExpenses(null, "enter expenses on mobile", getSH(), expenseClaim, expenseDetail, driver, selectors, OS),
			submitSelectedExpense(null, "select all expenses and submit", getSH(), expenseList, expenseEntry, driver, selectors, OS), // not sure what the non-list is for?
			
			//web
			navigateToExpenseClaimAndApprove(null, "reject claim", getSH(), data().expenseClaims.get(1)),
			runAllJobs(null, "run jobs del", getSH())
		);
	}
}
