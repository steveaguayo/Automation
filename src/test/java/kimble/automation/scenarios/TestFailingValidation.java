package kimble.automation.scenarios;

import static kimble.automation.scenarios.Stages.*;

import kimble.automation.KimbleOneTest;
import kimble.automation.domain.KimbleData;

import org.testng.annotations.Test;

public class TestFailingValidation extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "TestFailingValidation")
	public void Scenario(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	@Override
	public void executeTest() {
		execute(assertTrue(null, "fail validation deliberately", getSH(), false));
	}
}