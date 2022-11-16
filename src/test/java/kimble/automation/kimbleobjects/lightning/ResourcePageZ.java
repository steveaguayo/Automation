package kimble.automation.kimbleobjects.lightning;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class ResourcePageZ {
	
	private static final By 
	newButton = KBy.lightningRibbonButtonByTitle("New");

	public static void startCreateWiz(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			waitClickable(sh, newButton, 20);
			click(sh, newButton);
		});
	}

}
