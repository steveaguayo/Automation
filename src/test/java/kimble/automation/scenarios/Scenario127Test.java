package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.Account;
import kimble.automation.domain.JourneyLeg;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.Resource;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario127Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario127")
	public void Scenario127(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		String resourceNameForJourney = salesOpp.deliveryEngagements.get(0).deliveryElements.get(0).activityAssignments.get(0).resourceName;
		
		execute(
			loginAs(null, "login as: " + data().loginUser, getSH(), data().loginUser, data().loginName),
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts, sales opps, engagements, elements and assignments", getSH(), data().accounts),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			createChangeElementAndAssignmentsStage(null, "create change elements and assignments and activate", getSH(), data().accounts, true),
			
			enterExpenses(null, "enter draft expense", getSH(), data().expenseClaims, false),
			createJourneys(null, "create multi-leg journey", getSH(), data().journeys, resourceNameForJourney),	
	
			enterAllowanceAdjustments(null, "enter allowance adjustments", getSH(), data().journeys),	

			runAllJobs(null, "run jobs before validating allowances", getSH()),
			validateAllowanceAmounts(null, "validate allowance amounts pre-allocation", getSH(), data().journeys),
			
			enterAllowanceAllocations(null, "allocate allowances", getSH(), data().journeys),
			createExpenseItemsAndValidate(null, "create expense items and validate them", getSH(), data().journeys, true)
		);
	}
}