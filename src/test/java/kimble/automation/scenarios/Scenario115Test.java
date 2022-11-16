package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;

import kimble.automation.domain.KimbleData;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario115Test extends KimbleOneTest {

	/* Connected test - checks that an opportunity with Proposition results in a Proposal with Proposal items */

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario115")
	public void Scenario115(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		execute(
			createAccountsOpportunitiesAndProposal(null, "create accounts, opportunities and proposals", getSH(), data().accounts)
		);
	}
}