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

public class Scenario143Test extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario143")
	public void Scenario143(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
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
		   enterTimeEnhancedTimesheet(null, "enhanced time entry", getSH(), data().timesheets),
		   runAllJobs(null, "run jobs after creating timesheets", getSH()),
		   validateForecasts(null, "post time submission", getSH(), data())
			
		);
	}	
}
