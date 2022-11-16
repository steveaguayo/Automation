package kimble.automation.kimbleobjects.lightning;

import static kimble.automation.helpers.SequenceActions.checkboxSelect;
import static kimble.automation.helpers.SequenceActions.click;
import static kimble.automation.helpers.SequenceActions.clickAndWaitSequence;
import static kimble.automation.helpers.SequenceActions.executeSequenceWithRefreshRetry;
import static kimble.automation.helpers.SequenceActions.executeSequenceWithRetry;
import static kimble.automation.helpers.SequenceActions.waitClickable;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class WhatIfScenariosPageZ {

	
	private static final By 
	newButton = KBy.lightningRibbonButtonByTitle("New"),
	acceptButton = KBy.lightningRibbonButtonByTitle("Accept");

	static final String
	scenarioNameSpan = "//a[contains(@class, 'outputLookupLink')][normalize-space(text())=\"{{name}}\"]",
	rowCheckbox = scenarioNameSpan + "/../../../td[2]//input[@type='checkbox']/following-sibling::span[@class='slds-checkbox--faux']";
	
	public static void startCreateWizard(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			waitClickable(sh, newButton, 20);
			click(sh, newButton);
		});
	}
	
	public static void acceptWhatIfScenario(SeleniumHelper sh, String scenarioName) {
		By listViewPicker = KBy.last(By.cssSelector("span.selectedListView"));
		By listViewPickerOptionPending = By.xpath("//span[normalize-space(@class)='listViewPickerOption'][text()='Pending']");
		By rowSelectorCheckbox = By.xpath(rowCheckbox.replace("{{name}}", scenarioName));
		executeSequenceWithRetry(sh, 3, () -> {
			waitClickable(sh, listViewPicker, 60);
			/* switch to pending list view	*/
			clickAndWaitSequence(sh, 20, 
					listViewPicker, 
					listViewPickerOptionPending,
					rowSelectorCheckbox);
			sh.sleep(500, "There's a dom update glitch that sometimes occurs here");
			/* select the scenario you want to accept	*/	checkboxSelect(sh, rowSelectorCheckbox, true);
			/* click accept								*/	click(sh, acceptButton);
		});
	}
}
