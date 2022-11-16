package kimble.automation.scenarios;

import static kimble.automation.scenarios.Stages.*;
import kimble.automation.domain.KimbleData;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class ClosePeriods extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "closePeriods")
	public void Scenario(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	@Override
	public void executeTest() {
		execute(
			closeTrackingPeriodsStage(null, "close Jan tracking periods unforced", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "close Jan forecasting periods unforced", getSH(), data().forecastingPeriodActions),
			
			closeTrackingPeriodsStage(null, "try closing Jan tracking periods again unforced", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "try closing Jan forecasting periods again unforced", getSH(), data().forecastingPeriodActions),
			
			closeTrackingPeriodsStage(null, "close Feb tracking periods forced", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "close Feb forecasting periods forced", getSH(), data().forecastingPeriodActions),
			
			closeTrackingPeriodsStage(null, "try closing Feb tracking periods again unforced", getSH(), data().trackingPeriodActions),
			closeForecastingPeriodsStage(null, "try closing Feb forecasting periods again unforced", getSH(), data().forecastingPeriodActions)
		);
	}
}
