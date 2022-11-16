package kimble.automation.scenarios;

import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.KimbleData;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class CloseAndOpenPeriods extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "closeAndOpenPeriods")
	public void Scenario(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	@Override
	public void executeTest() {
		execute(
			closeTrackingPeriodsStage(null, "close Jan tracking periods unforced", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "close Jan forecasting periods unforced", getSH(), data().forecastingPeriodActions),
			
			openForecastingPeriodsStage(null, "open Jan forecasting periods", getSH(), data().forecastingPeriodActions),
			openTrackingPeriods(null, "open Jan tracking periods", getSH(), data().trackingPeriodActions)
		);
	}
}