package kimble.automation.scenarios;

import org.testng.annotations.Test;

// additional
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
import kimble.automation.domain.Timesheet;
import kimble.automation.kimbleobjects.classic.MobileSelectors;
import kimble.automation.KimbleOneTest;

import io.appium.java_client.AppiumDriver;

public class MobileTimeTest extends KimbleOneTest{

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "MobileScenarioTest")	
	public void ScenarioMobileTest(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
//		KimbleData.isMobileAndroidTest=true;
//		KimbleData.isMobileiOSTest=true;
		executeScenario(loginCredentials, kimbleTestData);		
	}

	@Override
	public void executeTest() throws Exception {
        AppiumDriver driver = getAppiumDriver();
        MobileSelectors selectors = getMobileSelectors();

        Timesheet ts = data().timesheets.get(0);
        TimeEntry te = ts.timeEntries.get(0);
        List<ExpenseEntry> expenses = data().expenseClaims.get(0).expenseEntries;
        ExpenseEntry entryToSubmit = expenses.get(0);
        ExpenseEntry entryToAdd = data().expenseClaims.get(0).expenseEntries.get(0);
		ExpenseDetail detail = entryToAdd.expenseDetails.get(0);
		
		execute(
				runAllJobs(null, "run jobs initial", getSH()),  
				
//				mobileEnterTimesheet(null, "enter time on mobile", getSH(), ts, driver, selectors),
//				editExistingTimesheet(null, "edit timesheet", getSH(), ts, te, driver, selectors),
//				mobileEnterExpenses(null, "enter expenses on mobile", getSH(), data().expenseClaims.get(0), data().expenseClaims.get(0).expenseEntries.get(0).expenseDetails.get(0), driver, selectors),
//				actualiseMobileTime(null, "actualise defaulted time", getSH(), ts, te, driver, selectors),
				
				
				
//				addExpenseToExistingEntry(null, "add to existing expense", getSH(), entryToAdd, detail, driver, selectors),
//				editExistingExpenseClaim(null, "edit existing entry", getSH(), entryToAdd, detail, driver, selectors),
//				submitAll(null, "submit all expenses", getSH(), driver, selectors),
//				switchToExpensesView(null, "switch to expense view", getSH(), driver, selectors),
//				submitSelectedExpense(null, "submit selected", getSH(), expenses, entryToSubmit, driver, selectors),
				runAllJobs(null, "run jobs final", getSH())     
		);   
	}
}