package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;
import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.ActivityAssignment;
import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.InterfaceRun;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.OtherResourcedActivity;
import kimble.automation.domain.Resource;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.domain.Timesheet;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.KimbleOneTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class Scenario139Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario139")
	public void Scenario139(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {	
		SalesOpportunity salesOpp = data().accounts.get(0).salesOpportunities.get(0);
		List<ActivityAssignment> aa = salesOpp.deliveryEngagements.get(0).deliveryElements.get(0).activityAssignments;
		OtherResourcedActivity oa = data().otherResourcedActivities.get(2);
	
		
		String jobName = new String("JobActivityAssignmentActualiseUsage");
		String jobStatus = new String("Pending");
		
		execute(		
				
			overrideOrgDate(null, "override the org date", getSH(), "2018-01-01"),
			createAccountsSalesOppsEngagementsAndElements(null, "create account, sales opp, engagement, element and assignment", getSH(), data().accounts),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			createAndActivateGroupAssignmentTemplates(null, "create group assignment templates", getSH(), data().otherResourcedActivities.get(1)),
			
			validateUsageAdjustments(null, "preActualSubmit", getSH(), data().expectedResults.get(2).resourceUsageAdjustments.get(0)),	
			validateUsageAdjustments(null, "preActualSubmit", getSH(), data().expectedResults.get(2).resourceUsageAdjustments.get(1)),	
			validateUsageAdjustments(null, "preActualSubmit", getSH(), data().expectedResults.get(2).resourceUsageAdjustments.get(2)),	
			
			navigateToGroupAssignmentByName(null, "navigate to other activity: " + oa.name, getSH(), oa.name),
			navigateToOtherActivityAssignments(null, "navigate to add assignments", getSH()),			
			scheduleOtherActivities(null, "createOtherActivityAssignments", getSH(), oa.activityUnavailability.get(0).scheduledActivity), 
			runAllJobs(null, "", getSH()),
			overrideOrgDate(null, "override the org date", getSH(), "2018-01-22"),
			
			retrieveTimePeriodId(null, "retrieveTrackingPeriodIdOtherActivity", getSH(), data().timesheets),
			
			getOtherActivityIdAndTimePeriodIdAndSeedCustomJob(null, "get assignment IDs and seed job", getSH(), jobName, jobStatus, "UK Holiday - 39", aa.get(0).resourceName),
			getOtherActivityIdAndTimePeriodIdAndSeedCustomJob(null, "get assignment IDs and seed job", getSH(), jobName, jobStatus, "UK Holiday - 39", aa.get(1).resourceName),	
			getOtherActivityIdAndTimePeriodIdAndSeedCustomJob(null, "get assignment IDs and seed job", getSH(), jobName, jobStatus, "UK Holiday - 39", aa.get(2).resourceName),	
			
     		getDeliveryAssignmentsIdAndTimePeriodAndSeedCustomJob(null, "retrieveTrackingPeriodIdDeliveryActivity", getSH(), jobName, jobStatus, salesOpp.deliveryEngagements.get(0), salesOpp.deliveryEngagements.get(0).deliveryElements.get(0).activityAssignments.get(0),data().timesheets),
			getDeliveryAssignmentsIdAndTimePeriodAndSeedCustomJob(null, "retrieveTrackingPeriodIdDeliveryActivity2", getSH(), jobName, jobStatus, salesOpp.deliveryEngagements.get(0), salesOpp.deliveryEngagements.get(0).deliveryElements.get(0).activityAssignments.get(1),data().timesheets),
			getDeliveryAssignmentsIdAndTimePeriodAndSeedCustomJob(null, "retrieveTrackingPeriodIdDeliveryActivity3", getSH(), jobName, jobStatus, salesOpp.deliveryEngagements.get(0), salesOpp.deliveryEngagements.get(0).deliveryElements.get(0).activityAssignments.get(2),data().timesheets),
			validateUsageAdjustments(null, "postActualSubmit", getSH(), data().expectedResults.get(3).resourceUsageAdjustments.get(0)),	
			validateUsageAdjustments(null, "postActualSubmit", getSH(), data().expectedResults.get(3).resourceUsageAdjustments.get(1)),	
			validateUsageAdjustments(null, "postActualSubmit", getSH(), data().expectedResults.get(3).resourceUsageAdjustments.get(2)),
			validateForecasts(null, "postActualSubmit", getSH(), data()),			

			overrideOrgDate(null, "override the org date", getSH(), ""),
			runAllJobs(null, "", getSH())
		);
	}
}