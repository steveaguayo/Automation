package kimble.automation.kimbleobjects.lightning;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.Opportunity;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class OpportunityZ {
	
	static final By 
	newButton = KBy.title("New"),
	editButton = KBy.title("Edit"),
	saveButton = KBy.title("Save"),
	
	nameInput = KBy.inputByLightningLabel("Opportunity Name"),
	closeDateInput = KBy.inputByLightningLabel("Close Date"),
	propositionDropdown = By.cssSelector("select[id$='proposalProposition']"),
	riskContingencyLink = By.xpath("//a[text()='Risk Contingency']");
		
	public static void inputValuesAndSave(SeleniumHelper sh, Opportunity opp) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 30,
			/* click new			*/	newButton,
			/* wait for name input	*/	nameInput
			);
			
			clearAndInputText(sh, nameInput, opp.name);
			lightningLookupSelect(sh, "Account Name", opp.accountName);
			clearAndInputText(sh, closeDateInput, opp.closeDate);
			lightningDropdownSelect(sh, "Stage", opp.stage);
			
			clickAndWaitSequence(sh, 20,
			/* click save			*/	saveButton,
			/* wait for edit button	*/	editButton
			);			
			
			
			executeSequenceWithRefreshRetry(sh, 3, () -> {
				clickAndWaitSequence(sh, 30,
						/* click new			*/	editButton,
						/* wait for name input	*/	nameInput
						);
				
				sh.waitForLightningSpinnerToBeHidden();		
				dropdownSelect(sh, propositionDropdown, opp.proposals.get(0).proposition);

				waitClickable(sh, saveButton, 10);
				click(sh, saveButton);
				sh.waitForLightningSpinnerToBeHidden();
				
				waitClickable(sh, riskContingencyLink, 20);
			});
		});
	}
}
