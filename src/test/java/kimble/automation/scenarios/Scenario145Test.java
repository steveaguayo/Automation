package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.util.Collection;
import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.ActivityAssignment;
import kimble.automation.domain.ActivityAssignmentsMany;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.Invoice;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class
Scenario145Test extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario145")
	public void Scenario145(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	@Override
	public void executeTest() {
		Account usAccount = data().accounts.get(0);
		Account emeaAccount = data().accounts.get(1);
		Account ukAccount = data().accounts.get(2);
		List <Account> allaccounts = data().accounts;
		SalesOpportunity salesOpp = ukAccount.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		List<ActivityAssignment> resources = engagement.deliveryElements.get(0).activityAssignments;

		execute(
	   	   createAccount(null, "create account for uk business unit", getSH(), emeaAccount),
		   createAccount(null, "create account for us business unit", getSH(), usAccount),
		   createAccount(null, "create account for emea business unit", getSH(), ukAccount),
		   setInternalAccountOnBusinessUnit(null, "set internal account on business unit", getSH(), allaccounts),
		   createSalesOpportunitiyEngagementsAndElements(null, "create sales opp, engagements and elements", getSH(), ukAccount, salesOpp),
		   navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
		   activateEngagements(null, "activate engagements", getSH(), salesOpp),
		   runAllJobs(null, "run jobs", getSH()),
		   validateForecasts(null, "post winning opp", getSH(), data()),
		   enterTime(null, "time entry 1", getSH(), data().timesheets),
		   runAllJobs(null, "run jobs", getSH()),
		   completeMilestonesStage(null, "complete milestones", getSH(), salesOpp),
		   generateInvoices(null, "postMilestoneComplete", getSH(), data().invoices, false),
		   validateForecasts(null, "post invoicing", getSH(), data()),
		   createInternalInvoice(null,"internalinvoice1", getSH(), data().invoices)
		  
		);
	}
	
}

