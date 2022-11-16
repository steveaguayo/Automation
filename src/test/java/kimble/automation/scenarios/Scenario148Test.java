package kimble.automation.scenarios;

import kimble.automation.domain.KimbleData;
import kimble.automation.kimbleobjects.classic.PagesC;
import kimble.automation.KimbleOneTest;

import static kimble.automation.scenarios.Stages.*;

import org.testng.annotations.Test;

import kimble.automation.domain.Proposal;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;

public class Scenario148Test extends KimbleOneTest {
	
	/* True Standalone test - checks that a Standalone Proposal with Proposition results in a Proposal with Proposal items */
	/* Alternate Proposal Test - Generates Alternate Proposals and marks them as preferred */
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario148")
	public void Scenario148(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception {
		Account account = data().accounts.get(0);
		Proposal proposal1 = account.proposals.get(0);
		Proposal altProposal = data().proposals.get(0);
		Proposal altProposal1 = data().proposals.get(1);
		DeliveryEngagement engagement = proposal1.deliveryEngagements.get(0);
		
		execute(
				createAccountsAndProposal(null, "create accounts and standalone proposal", getSH(), data().accounts),
				
				// Create risks
				navigateMenu(null, "navigate to risk sales", getSH(), PagesC.RISKSSALES),
				createRisks(null, "create proposal risks", getSH(), proposal1.risks),
				// Create engagement and elements including assignments, milestones, annuities and expense forecasts
				navigateMenu(null, "navigate to proposal scope", getSH(), PagesC.PROPOSALITEMS),
				createEngagementAndConfigureElements(null, "create engagement and configure elements", getSH(), engagement),
				createEngagementExpenseForecast(null, "create expense forecast", getSH(), proposal1),
				//Create alternate proposal using copy from original
				navigateMenu(null, "navigate to originating proposal", getSH(), PagesC.PROPOSALITEMS),
				switchToDetailedForecastLevelStage(null, "switch to detailed forecast level", getSH()),
				createAltProposal(null, "Create Alternate Proposal", getSH(), altProposal, proposal1.name),
				//Make alternate proposal the preferred proposal
				switchToDetailedForecastLevelStage(null, "switch to detailed forecast level", getSH()),
				switchAltProposalToPreferred(null, "Switching Alternate Proposal to Preferred", getSH(), altProposal.name),
				navigateFromAnywhereToProposal(null, "Navigate to Proposal: " + altProposal.name, getSH(), altProposal.name),
				navigateMenu(null, "navigate to risk sales", getSH(), PagesC.RISKSSALES),
				//Validate risks and proposal, engagement and element summary fields
				validateRisks(null, "validate : create proposal risks", getSH(), data()),
				navigateFromAnywhereToProposal(null, "Navigate to Proposal: " + altProposal.name, getSH(), altProposal.name),
				validateProposalItemsSummaryFieldsOcs(null, "validate proposal items summary fields", getSH(), data().expectedResults),
				//Create alternate proposal using proposition
				createAltProposal(null, "Create Alternate Proposal", getSH(), altProposal1, altProposal.name),
				//Make alternate proposal the preferred proposal
				switchAltProposalToPreferred(null, "Switching Alternate Proposal to Preferred", getSH(), altProposal1.name),
				navigateFromAnywhereToProposal(null, "Navigate to Proposal: " + altProposal1.name, getSH(), altProposal1.name),
				//Validate proposal, engagement and element summary fields
				validateProposalItemsSummaryFieldsOcs(null, "validate proposition proposal items summary fields", getSH(), data().expectedResults)
			);
	}
}