package kimble.automation.scenarios;


import static kimble.automation.helpers.SequenceActions.*;
import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.kimbleobjects.classic.SalesOpportunityPageC;
import kimble.automation.KimbleOneTest;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Scenario103Test extends KimbleOneTest {
	
	private static final String MISSING_COST = "No Cost Items";
	private static final String MISSING_REVENUE = "No Revenue Items";

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario103")
	public void Scenario103(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {		
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		SalesOpportunity salesOpp = data().accounts.get(0).salesOpportunities.get(0);
		DeliveryEngagement engagement = data().accounts.get(0).salesOpportunities.get(0).deliveryEngagements.get(0);
		
		execute(
			createAccountsAndSalesOpportunities(null, "create accounts and sales opps", getSH(), data().accounts),
			updateHighLevelForecastStage(null, "update high level forecast for engagement: " + engagement.name, getSH(), engagement),
			navigateAndUpdateForecastStatusStage(null, "update forecast status to: Probable (60%)", getSH(), "Probable (60%)"),
			validateForecasts(null, "PostUpdateToProbable", getSH(), data()),
			
			navigateAndUpdateForecastStatusStage(null, "update forecast status to: Possible (10%)", getSH(), "Possible (10%)"),
			navigateAndUpdateCloseDate(null, "update close date to: " + salesOpp.step2CloseDate, getSH(), salesOpp.step2CloseDate),
			validateForecasts(null, "PostCloseDateChange", getSH(), data()),
			
			navigateAndUpdateBusinessUnit(null, "update business unit to: " + salesOpp.step2BusinessUnit, getSH(), salesOpp.step2BusinessUnit),
			validateForecasts(null, "PostFirstBusinessUnitChange", getSH(), data()),
			
			navigateToMaintainOpportunitySummary(null, "navigate to maintain opportunity summary", getSH()),
			navigateAndMoveSalesOpportunityToNextStage(null, "move the sales opportunity to stage: Develop Proposal", getSH(), "Develop Proposal"),
			attemptToMoveSalesOppToNextStageWithoutDetailedForecast("Develop Proposal"),
			
			navigateToMaintainOpportunitySummary(null, "navigate to maintain opportunity summary", getSH()),
			configureEngagementElements(null, "create engagement and configure elements", getSH(), engagement),
			runAllJobs(null, "run jobs after creating elements", getSH()),
			navigateFromAnywhereToSalesOpp(null, "navigate back to sales opportunity: " + salesOpp.name, getSH(), salesOpp.name),
			navigateAndMoveSalesOpportunityToNextStage(null, "move the sales opportunity to stage: Negotiation", getSH(), "Negotiation"),
			validateForecasts(null, "PostDetailedRevenueDefinition", getSH(), data()),

			navigateAndUpdateBusinessUnit(null, "update business unit to: " + salesOpp.businessUnit, getSH(), salesOpp.businessUnit),
			validateForecasts(null, "PostSecondBusinessUnitChange", getSH(), data()),
			
			createBidTeamAssignmentsStage(null, "create bid team assignments", getSH(), salesOpp),
			validateForecasts(null, "PostBidTeamAssignment", getSH(), data()),
			
			enterTime(null, "enter time", getSH(), data().timesheets),
			enterExpenses(null, "enterExpensesPostBidTeamAssignment", getSH(), data().expenseClaims),
			validateForecasts(null, "PostBidTeamTimeAndExpenseBooking", getSH(), data()),
			
			navigateAndLoseOpportunity(null, "lose opportunity", getSH(), salesOpp),
			validateSalesOpportunity(salesOpp.name, engagement),
			validateForecasts(null, "PostLossOfSalesOpportunity", getSH(), data())
		);
	}

	public Stage attemptToMoveSalesOppToNextStageWithoutDetailedForecast(String expectedStage) {
		Stage stage = new Stage(null, "attempt to move the sales opportunity to the stage: " + expectedStage + " without detailed forecast");
		navigateAndMoveSalesOpportunityToNextStage(stage, "move sales opportunity to stage: " + expectedStage, getSH(), expectedStage);
		verifyPageContainsErrors(stage);
		return stage;
	}
	
	public Stage verifyPageContainsErrors(Stage parentStage) {
		return new Stage(parentStage, "verify page contains errors") {
			public void run() {
				getSH().assertTrue(exists(getSH(), By.xpath("//*[contains(text(), \"" + MISSING_REVENUE + "\")]"), 10), "The error: " + MISSING_REVENUE + " didn't appear");
				getSH().assertTrue(exists(getSH(), By.xpath("//*[contains(text(), \"" + MISSING_COST + "\")]"), 10), "The error: " + MISSING_COST + " didn't appear");
			}
		};
	}

	public Stage validateSalesOpportunity(String salesOppName, DeliveryEngagement deliveryEngagementDetails) {
		Stage stage = new Stage(null, "validate sales opportunity");
		navigateFromAnywhereToSalesOpp(stage, "navigate to sales opportunity: " + salesOppName, getSH(), salesOppName);
		checkContractRevenue(stage, deliveryEngagementDetails);
		return stage;
	}

	public Stage checkContractRevenue(Stage parentStage, DeliveryEngagement deliveryEngagementDetails) {
		return new Stage(parentStage, "validate sales opportunity") {
			public void run() {
				Assert.assertTrue(SalesOpportunityPageC.validateContractRevenue(getSH(), deliveryEngagementDetails.contractRevenueDisplay));
			}
		};
	}
}