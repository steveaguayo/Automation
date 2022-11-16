package kimble.automation.kimbleobjects.lightning;

import static kimble.automation.helpers.SequenceActions.click;
import static kimble.automation.helpers.SequenceActions.executeSequenceWithRefreshRetry;
import static kimble.automation.helpers.SequenceActions.waitClickable;

import org.openqa.selenium.By;

import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

public class JourneysPageZ {

	private static final By 
	newButton = KBy.lightningRibbonButtonByTitle("New");

	public static void startCreateWizard(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			waitClickable(sh, newButton, 20);
			click(sh, newButton);
		});
	}

}
