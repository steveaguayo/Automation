package kimble.automation.kimbleobjects.lightning;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class SalesOpportunityPageZ {
	
	static final By 
	newButton = By.xpath("//div[@class='forceObjectHomeDesktop']//div[.='New']");
	
	
	public static void startNewSalesOppCreation(SeleniumHelper sh) {
		executeSequenceWithRetry(sh, 3, () -> {
			sh.sleep(1000, "!there are bugs in lightning to do with accumulating ribbon buttons in the dom and sometimes it seems the wrong button can be visible for a momet and get pressed!");
			waitClickable(sh, newButton, 20);
			click(sh, newButton);
			// Can't wait for an element, because any element to wait for will be in a classic iframe
		});
	}
}
