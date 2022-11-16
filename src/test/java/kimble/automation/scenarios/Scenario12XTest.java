package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.KimbleData;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario12XTest extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario12X")
	public void Scenario12X(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		execute(
			closeTrackingPeriodsStage(null, "closeTrackingBeforeStarting", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "closeForecastingBeforeStarting", getSH(), data().forecastingPeriodActions)
		);
	}
}
