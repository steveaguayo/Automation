package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.InterfaceRun;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.Resource;
import kimble.automation.domain.ResourceList;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario128Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario128")
	public void Scenario128(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		
		InterfaceRun firstImport = data().interfaceRuns.get(0);
		InterfaceRun secondImport = data().interfaceRuns.get(1);

		
		
		execute(
			editResources(null, "change initial resource grade", getSH(), data().resources),
			createAccountsSalesOppsEngagementsAndElements(null, "create accounts, sales opps, engagements, elements and assignments", getSH(), data().accounts),
			editResourcedActivities(null, "update time entry rule = DateAndTime", getSH(), salesOpp.name, engagement),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),
			completeMilestonesStage(null, "complete milestones", getSH(), salesOpp),
			validateForecasts(null, "postInitialSetup", getSH(), data()),
			submitInterfaceRun(null, "firstTimeBooking", getSH(), firstImport.interfaceType, firstImport.interfaceRunData),
			runAllJobs(null, "run jobs after submitting timesheets", getSH()),
 			editResources(null, "change test resource grade", getSH(), data().resources),
			createWhatIfScenario(null, "create what if scenario", getSH(), data().whatIfScenarios),
			submitInterfaceRun(null, "secondTimeBooking", getSH(), secondImport.interfaceType, secondImport.interfaceRunData),
			validateForecasts(null, "postCostRateChange", getSH(), data()),
			closeTrackingPeriodsStage(null, "closeTrackingPeriods", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "closeForecastingPeriods", getSH(), data().forecastingPeriodActions),
			adjustTimeEntries(null, "adjust time entries", getSH(), data().timeAdjustmentLists),
			validateForecasts(null, "postTimeAdjustment", getSH(), data()),								
			validateForecasts(null, "calculateCost%CompleteRevRec", getSH(), data())
			
		);
	}

}