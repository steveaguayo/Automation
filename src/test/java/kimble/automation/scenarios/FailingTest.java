package kimble.automation.scenarios;

import static kimble.automation.scenarios.Stages.abort;
import kimble.automation.domain.KimbleData;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class FailingTest extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "debugExpenseApprovalError")
	public void Scenario(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	@Override
	public void executeTest() {
		execute(
			abort(null, "fail deliberately")
		);
	}
}
