package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.OtherResourcedActivity;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario129Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario129")
	public void Scenario129(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		
		execute(
		createAssignments(null, "create group assignment for TOIL", getSH(), data().otherResourcedActivities.get(0)),	
		createAccountsSalesOppsEngagementsAndElements(null, "create account, sales opp, engagement, element and assignment", getSH(), data().accounts),
		editToilFactor(null, "update toil factors", getSH(), salesOpp.name, engagement),
		navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
		activateEngagements(null, "activate engagements", getSH(), salesOpp),
		enterTime(null, "firstTimeBooking", getSH(), data().timesheets),
		validateForecasts(null, "postIntitialSetUp", getSH(), data()),	
		validateUsageAdjustments(null, "postIntitialSetUp", getSH(), data().expectedResults.get(0).resourceUsageAdjustments.get(0)),	
		
		enterTime(null, "secondTimeBooking", getSH(), data().timesheets),
		validateForecasts(null, "postOvertimeBooking", getSH(), data()),	
		validateUsageAdjustments(null, "postOvertimeBooking", getSH(), data().expectedResults.get(0).resourceUsageAdjustments.get(1)),	
		enterTime(null, "thirdTimeBooking", getSH(), data().timesheets),
		validateForecasts(null, "postWeekendTimeBooking", getSH(), data()),	 // FIX YAML 
		validateUsageAdjustments(null, "postWeekendTimeBooking", getSH(), data().expectedResults.get(0).resourceUsageAdjustments.get(2)),
		
		enterTime(null, "fourthTimeBooking", getSH(), data().timesheets),
		validateUsageAdjustments(null, "postTOILTimeBooking", getSH(), data().expectedResults.get(0).resourceUsageAdjustments.get(3))	
					
		);
	}

}
