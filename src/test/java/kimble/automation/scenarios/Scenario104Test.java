package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario104Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario104")
	public void Scenario104(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {		
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		
		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts and sales opps", getSH(), data().accounts),
			
			automaticallyUpdateDeliveryDates(null, "automatically update delivery dates for: "+salesOpp.name, getSH(), salesOpp),
			runAllJobs(null, "run jobs after updating delivery dates", getSH()),
			manuallyUpdateDeliveryDates(null, "manually update delivery dates for: "+salesOpp.name, getSH(), salesOpp),
			runAllJobs(null, "run jobs after updating delivery dates", getSH()),
			
			validateForecasts(null, "preSalesOppWin", getSH(), data()),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			validateForecasts(null, "postSalesOppWin", getSH(), data()),
			
			changeStartDateOfDeliveryEngagement(null, "change start date for engagement", getSH(), salesOpp, engagement),
			validateForecasts(null, "postStartDateWizard", getSH(), data())
		);
	}

}