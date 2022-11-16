package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.InterfaceRun;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.KimbleOneTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class Scenario131Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario131")
	public void Scenario131(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {	
		SalesOpportunity salesOpp = data().accounts.get(0).salesOpportunities.get(0);
		
		InterfaceRun assignmentImport = data().interfaceRuns.get(0);	
		InterfaceRun timeEntryImport = data().interfaceRuns.get(1);	
		InterfaceRun expenseEntryImport = data().interfaceRuns.get(2);	
		InterfaceRun paymentStatementImport = data().interfaceRuns.get(3);	
		InterfaceRun resourcePaymentStatementImport = data().interfaceRuns.get(4);	
//		InterfaceRun PerformanceAnalysisImport = data().interfaceRuns.get(5);	not in yml?

		
		execute(		
			createAccountsSalesOppsEngagementsAndElements(null, "create account, sales opp, engagement, element and assignment", getSH(), data().accounts),
			submitInterfaceRun(null, "import assignment", getSH(), assignmentImport.interfaceType, assignmentImport.interfaceRunData),
			runAllJobs(null, "run jobs", getSH()),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			validateForecasts(null, "postInitialSetup", getSH(), data()),
			
			submitInterfaceRun(null, "import time entries", getSH(), timeEntryImport.interfaceType, timeEntryImport.interfaceRunData),
			validateForecasts(null, "postTimeEntryImport", getSH(), data()),
			
			submitInterfaceRun(null, "import expenses", getSH(), expenseEntryImport.interfaceType, expenseEntryImport.interfaceRunData),
			validateForecasts(null, "postExpenseItemImport", getSH(), data())
				/*,					
			submitInterfaceRun(null, "import resource payment statement", getSH(), resourcePaymentStatementImport.interfaceType, resourcePaymentStatementImport.interfaceRunData),
			validate resource payment statement
				
			generateInvoices(null, "pre-CR1 generate invoice", getSH(), data().invoices, true),
			submitInterfaceRun(null, "import payment statement", getSH(), paymentStatementImport.interfaceType, paymentStatementImport.interfaceRunData),
			validate payment statement
*/			
		);
	}
}