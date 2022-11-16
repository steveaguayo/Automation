package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class SalesOppLostPageC {
		
	static final By closeDateInput = By.cssSelector("input[id$='closeDate']");
	static final By salesOppHomePageBlock = By.cssSelector("div[id$='mainSalesOppHomePB']");
	
	static final By lostReasonSelect = By.cssSelector("select[id$='lostReason']");
	static final By lostNarrativeInput = By.cssSelector("textarea[id$='lostNarrative']");
	static final By saveLoseOpportunityBtn = By.cssSelector("input[id$='saveLoseOpportunityBtn']");
	
	public static void LoseOpportunity(SeleniumHelper sh, SalesOpportunity salesOpportunityDetails){
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, closeDateInput, 20);
			if(sh.isLightning()){
				// the calendar doesn't align correctly in lightning so this needs to be separate
				if(!(salesOpportunityDetails.lostCloseDate==null)){
					sh.waitForLightningSpinnerToBeHidden();
					sh.clearField(closeDateInput);
					sh.waitForLightningSpinnerToBeHidden();
					inputText(sh, closeDateInput, salesOpportunityDetails.lostCloseDate);
				}	
			}
			else{
				clearAndInputText(sh, closeDateInput, salesOpportunityDetails.lostCloseDate);
			}
			sh.waitForLightningSpinnerToBeHidden();
			dropdownSelect(sh, lostReasonSelect, salesOpportunityDetails.lostReason);
			clearAndInputText(sh, lostNarrativeInput, salesOpportunityDetails.lostNarrative);
			click(sh, saveLoseOpportunityBtn);
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, salesOppHomePageBlock, 20);
		});
	}
}
