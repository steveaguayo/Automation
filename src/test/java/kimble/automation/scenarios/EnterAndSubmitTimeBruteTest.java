package kimble.automation.scenarios;

import static kimble.automation.scenarios.Stages.activateEngagements;
import static kimble.automation.scenarios.Stages.createAccountsAndSalesOpportunities;
import static kimble.automation.scenarios.Stages.createEngagementAndConfigureElements;
import static kimble.automation.scenarios.Stages.enterTime;
import static kimble.automation.scenarios.Stages.navigateAndWinOpportunity;
import static kimble.automation.scenarios.Stages.validateForecasts;
import kimble.automation.KimbleOneTest;
import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;

import org.testng.annotations.Test;

public class EnterAndSubmitTimeBruteTest extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "EnterAndSubmitTimeBruteTest")
	public void EnterAndSubmitTimeBruteTestScenario(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);

		execute(
			createAccountsAndSalesOpportunities(null, "create accounts and sales opps", getSH(), data().accounts),
			createEngagementAndConfigureElements(null, "create engagement and configure elements", getSH(), engagement),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			
			enterTime(null, "time 1", getSH(), data().timesheets),
			validateForecasts(null, "time 1 validate", getSH(), data()),
		
			enterTime(null, "time 2", getSH(), data().timesheets),
			validateForecasts(null, "time 2 validate", getSH(), data()),
	
			enterTime(null, "time 3", getSH(), data().timesheets),
			validateForecasts(null, "time 3 validate", getSH(), data()),
			
			enterTime(null, "time 4", getSH(), data().timesheets),
			validateForecasts(null, "time 4 validate", getSH(), data()),
			
			enterTime(null, "time 5", getSH(), data().timesheets),
			validateForecasts(null, "time 5 validate", getSH(), data()),
			
			enterTime(null, "time 6", getSH(), data().timesheets),
			validateForecasts(null, "time 6 validate", getSH(), data()),
			
			enterTime(null, "time 7", getSH(), data().timesheets),
			validateForecasts(null, "time 7 validate", getSH(), data()),
			
			enterTime(null, "time 8", getSH(), data().timesheets),
			validateForecasts(null, "time 8 validate", getSH(), data()),
			
			enterTime(null, "time 9", getSH(), data().timesheets),
			validateForecasts(null, "time 9 validate", getSH(), data()),
			
			enterTime(null, "time 10", getSH(), data().timesheets),
			validateForecasts(null, "time 10 validate", getSH(), data()),
			
			enterTime(null, "time 11", getSH(), data().timesheets),
			validateForecasts(null, "time 11 validate", getSH(), data()),
			
			enterTime(null, "time 12", getSH(), data().timesheets),
			validateForecasts(null, "time 12 validate", getSH(), data()),
			
			enterTime(null, "time 13", getSH(), data().timesheets),
			validateForecasts(null, "time 13 validate", getSH(), data()),
			
			enterTime(null, "time 14", getSH(), data().timesheets),
			validateForecasts(null, "time 14 validate", getSH(), data()),
			
			enterTime(null, "time 15", getSH(), data().timesheets),
			validateForecasts(null, "time 15 validate", getSH(), data())
		);
	}
}