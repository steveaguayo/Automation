package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;
import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.ActivityAssignment;
import kimble.automation.domain.ActivityUnavailability;
import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.OtherResourcedActivity;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.domain.ScheduledActivity;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario125Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario125")
	public void Scenario125(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		DeliveryElement element = engagement.deliveryElements.get(0);
		
		String UsageBehaviourOffsetStart = new String("30");
		String UsageBehaviourName = new String("Entitlement between two Dates, with Adjustments (Remove Usage On Period Change)");
		String UsageBehaviourOffsetFinal = new String("60");
		String jobName = new String("JobActivityAssignmentRemoveUsage");
		String jobStatus = new String("Pending");
		
		List<ActivityAssignment> otherActivityAssignmentsFirst = data().otherResourcedActivities.get(1).activityAssignments;
	
		OtherResourcedActivity otherActivityJuly = data().otherResourcedActivities.get(2);
		OtherResourcedActivity otherActivityJan = data().otherResourcedActivities.get(3);
		OtherResourcedActivity otherActivityFeb = data().otherResourcedActivities.get(4);
		OtherResourcedActivity otherActivitySickness = data().otherResourcedActivities.get(6);
		
		execute(
			updateUsageBehaviourRuleOffset(null, "update UBR offset = 30", getSH(), UsageBehaviourOffsetStart, UsageBehaviourName),
			createAndActivateGroupAssignmentTemplates(null, "create group assignment templates", getSH(), data().otherResourcedActivities.get(1)),	//  HOLIDAY	
			runAllJobs(null, "run jobs", getSH()),							
			createAssignments(null, "create group assignment for sickness", getSH(), data().otherResourcedActivities.get(5)),	//  SICKNESS			
			runAllJobs(null, "run jobs", getSH()),	
			
    		createAccount(null, "create account", getSH(), account),
			createSalesOpportunitiyEngagementsAndElements(null, "create sales opp, engagements and elements", getSH(), account, salesOpp),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements" + salesOpp.deliveryEngagements.get(0).name, getSH(), salesOpp), 	
			seedCustomJob(null, "get assignment ID's and seed job", getSH(), jobName, jobStatus, engagement, element),
			validateForecasts(null, "priorTimeEntry", getSH(), data()),			
							
			//sickness	
			navigateToGroupAssignmentByName(null, "navigate to other activity: " + otherActivitySickness.name, getSH(), otherActivitySickness.name),
			navigateToOtherActivityAssignments(null, "navigate to add assignments", getSH()),				
			scheduleOtherActivities(null, "createOtherActivityAssignments", getSH(), otherActivitySickness.activityUnavailability.get(0).scheduledActivity),
			validateForecasts(null, "postScheduleForecastTime", getSH(), data()),
			//july	
			navigateToGroupAssignmentByName(null, "navigate to other activity: " + otherActivityJuly.name, getSH(), otherActivityJuly.name),
			navigateToOtherActivityAssignments(null, "navigate to add assignments", getSH()),				
			scheduleOtherActivities(null, "createOtherActivityAssignments", getSH(), otherActivityJuly.activityUnavailability.get(0).scheduledActivity),
			validateForecasts(null, "postSecondScheduleForecastTime", getSH(), data()),					
			//jan + feb
			navigateToGroupAssignmentByName(null, "navigate to other activity: " + otherActivityJan.name, getSH(), otherActivityJan.name),
			navigateToOtherActivityAssignments(null, "navigate to add assignments", getSH()),				
			scheduleOtherActivities(null, "createOtherActivityAssignments", getSH(), otherActivityJan.activityUnavailability.get(0).scheduledActivity),				
			navigateToGroupAssignmentByName(null, "navigate to other activity: " + otherActivityFeb.name, getSH(), otherActivityFeb.name),
			navigateToOtherActivityAssignments(null, "navigate to add assignments", getSH()),				
			scheduleOtherActivities(null, "createOtherActivityAssignments", getSH(), otherActivityFeb.activityUnavailability.get(0).scheduledActivity),															
						
			updateUsageBehaviourRuleOffset(null, "update UBR offset = 60", getSH(), UsageBehaviourOffsetFinal, UsageBehaviourName),
			seedCustomJob(null, "get assignment ID's and seed job", getSH(), jobName, jobStatus, engagement, element),

			validateUsageAdjustments(null, "validate usage adjustments", getSH(), data().expectedResults.get(3).resourceUsageAdjustments.get(0)),	
			validateUsageAdjustments(null, "validate usage adjustments", getSH(), data().expectedResults.get(3).resourceUsageAdjustments.get(1)),	
			validateUsageAdjustments(null, "validate usage adjustments", getSH(), data().expectedResults.get(3).resourceUsageAdjustments.get(2)),	
			enterTime(null, "firstTimeBooking", getSH(), data().timesheets),	
			validateUsageAdjustments(null, "validate usage adjustments", getSH(), data().expectedResults.get(4).resourceUsageAdjustments.get(0)),	
			validateUsageAdjustments(null, "validate usage adjustments", getSH(), data().expectedResults.get(4).resourceUsageAdjustments.get(1)),	
			validateUsageAdjustments(null, "validate usage adjustments", getSH(), data().expectedResults.get(4).resourceUsageAdjustments.get(2)),	
			updateUsageBehaviourRuleOffset(null, "update UBR offset = 30", getSH(), UsageBehaviourOffsetStart, UsageBehaviourName)		
		);
	}

}
