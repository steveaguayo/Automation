package kimble.automation.kimbleobjects.lightning;

import static kimble.automation.helpers.SequenceActions.click;
import static kimble.automation.helpers.SequenceActions.executeSequenceWithRefreshRetry;
import static kimble.automation.helpers.SequenceActions.waitClickable;

import org.openqa.selenium.By;

import kimble.automation.helpers.SeleniumHelper;

public class SupplierInvoicePageZ {

	private static final By 
	newButton = kimble.automation.helpers.KBy.lightningRibbonButtonByTitle("New");

	public static void startCreateWiz(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			waitClickable(sh, newButton, 20);
			click(sh, newButton);
		});
	}
	
}
