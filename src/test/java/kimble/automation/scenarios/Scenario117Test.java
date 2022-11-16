package kimble.automation.scenarios;

import kimble.automation.domain.KimbleData;
import kimble.automation.KimbleOneTest;

import static kimble.automation.scenarios.Stages.*;

import org.testng.annotations.Test;

public class Scenario117Test extends KimbleOneTest {
	
	/* True Standalone test - checks that a Standalone Proposal with Proposition results in a Proposal with Proposal items */
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario117")
	public void Scenario117(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception {
		execute(
				createAccountsAndProposal(null, "create accounts and standalone proposal", getSH(), data().accounts)
			);
	}
}