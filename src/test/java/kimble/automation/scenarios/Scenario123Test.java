package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario123Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario123")
	public void Scenario123(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
	
		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts and sales opps", getSH(), data().accounts),
			validateForecasts(null, "postInitialSetup", getSH(), data()),
		
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			validateForecasts(null, "postSalesOppWin", getSH(), data()),
			
			scheduleActivities(null, "schedule activities", getSH(), salesOpp),
			sleep(null, "added a thirty second sleep because for some reason there is a latency here in seeding jobs", getSH(), 30000),
			validateForecasts(null, "postScheduledWork", getSH(), data()),
			
			enterTime(null, "firstTimeBooking", getSH(), data().timesheets),
			validateForecasts(null, "postFirstTimeBooking", getSH(), data()),
			
			enterTime(null, "secondTimeBooking", getSH(), data().timesheets),
			validateForecasts(null, "postSecondTimeBooking", getSH(), data())
		);
	}
}
