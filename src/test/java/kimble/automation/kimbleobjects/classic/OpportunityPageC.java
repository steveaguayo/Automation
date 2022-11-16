package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.Opportunity;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;

public class OpportunityPageC {
	
	public static final String SELECT_ID$_PROPOSAL_PROPOSITION_SELECT = "select[id$='proposalProposition']";
	public static final String CREATE_PROPOSAL_BTN = "input[id$='createProposal']";
	public static final String RISK_CONTINGENCY_PRODUCT_LINK = "//a[text()='Risk Contingency']";

	static final By 
	newButton = By.name("newOpp"),
	editButton = By.name("edit"),
	saveButton = By.name("save"),
	
	nameLabel = KBy.label("Opportunity Name"),
	accountNameLabel = KBy.label("Account Name"),
	closeDateLabel = KBy.label("Close Date"),
	stageLabel = KBy.label("Stage"),

	propositionDropdown = By.cssSelector(SELECT_ID$_PROPOSAL_PROPOSITION_SELECT),
	createProposal = By.cssSelector(CREATE_PROPOSAL_BTN),
	riskContingencyLink = By.xpath(RISK_CONTINGENCY_PRODUCT_LINK);
	
	public static void startNewOpportunityCreation(SeleniumHelper sh) {
		executeSequenceWithRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20,
			/* click new			*/	newButton,
			/* wait for name input	*/	nameLabel
			);
		});
	}
	
	public static void inputValuesAndSave(SeleniumHelper sh, Opportunity opportunity) {
		By 
		nameInput = sh.getSelectorBasedOnLabel(nameLabel),
		accountNameInput = sh.getSelectorBasedOnLabel(accountNameLabel),
		closeDateInput = sh.getSelectorBasedOnLabel(closeDateLabel),
		stageInput = sh.getSelectorBasedOnLabel(stageLabel);
		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clearAndInputText(sh, nameInput, opportunity.name);
			clearAndInputText(sh, accountNameInput, opportunity.accountName);
			clearAndInputText(sh, closeDateInput, opportunity.closeDate);
			dropdownSelect(sh, stageInput, opportunity.stage);
			
			clickAndWaitSequence(sh, 20,
			/* click save			*/	saveButton,
			/* wait for edit button	*/	editButton
			);
		});
		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.switchToFrameByTitle("OpportunityProposal");
			dropdownSelect(sh, propositionDropdown, opportunity.proposals.get(0).proposition);
			
			clickAndWaitSequence(sh, 45,
				/* click save			*/	createProposal,
				/* wait for Risk Contingency Product Link to be clickable */	riskContingencyLink
			);
		});
	}
	
	public static void deleteAllOpportunities(SeleniumHelper sh){
		sh.executeJavaScript("result = sforce.connection.query('Select Id from Opportunity'); records = result.getArray('records');  for (var i=0; i< records.length; i++) {    var record = records[i];    sforce.connection.deleteIds([record.Id]);  }");
	}
}
