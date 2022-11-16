package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario110Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario110")
	public void Scenario(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		
		execute(
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts, sales opps, engagements, elements and assignments", getSH(), data().accounts),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			
			validateForecasts(null, "postSalesOppWin", getSH(), data()),
			
			scheduleActivities(null, "shedule activities", getSH(), salesOpp),
			validateForecasts(null, "postScheduledWork", getSH(), data()),
			
			enterTime(null, "firstTimeBooking", getSH(), data().timesheets),
			validateForecasts(null, "postFirstTimeBooking", getSH(), data()),
			
			enterTime(null, "secondTimeBooking", getSH(), data().timesheets),
			validateForecasts(null, "postSecondTimeBooking", getSH(), data())
		);
	}
}
