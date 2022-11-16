package kimble.automation.kimbleobjects.lightning;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class ExpensePageZ {
	
	static final By
	newClaimButton = KBy.lightningRibbonButtonByTitle("New");
	
	public static void waitForListView(SeleniumHelper sh) {
		waitClickable(sh, newClaimButton, 20);	
	}
}
