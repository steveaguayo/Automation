package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.WhatIfScenario;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class WhatIfScenariosPageC {

	static final By
	newButton = By.name("new"),
	newButtonL = KBy.title("New"),
	saveButton = By.xpath("//input[@value='Save']"),
	acceptButton = By.xpath("//div[1]/div[2]/table/tbody/tr/td[2]/div[1]/div[1]/form/div[2]/div/div[1]/ul/li[1]/input[1]"),
	jobSpinner = By.cssSelector(".Processing"),
	nameLabel = By.xpath("//label[normalize-space(text())='What-If Scenario Name']"),
	typeSelect = By.xpath("//th[normalize-space(text())='What-If Data Type']/following-sibling::td/select"),
	dateLabel = By.xpath("//label[normalize-space(text())='What-If Date']");
	
	static final String
	scenarioNameSpan = "/div[contains(@class, 'x-grid3-cell-inner')]/a/span[normalize-space(text())=\"{{name}}\"]",
	rowCheckbox = "//tr[td" + scenarioNameSpan + "]//input[@type='checkbox']";
	
	public static void startCreateWizard(SeleniumHelper sh) {
		sh.closeLightningPopUp();
		if(sh.isLightning()){
			waitClickable(sh, newButtonL, 20);
			click(sh, newButtonL);
			sh.waitForLightningSpinnerToBeHidden();
		}
		else{
			executeSequenceWithRetry(sh, 3, () -> {
				/* click new					*/	click(sh, newButton);
				/* wait for the name input		*/	waitClickable(sh, sh.getSelectorBasedOnLabel(nameLabel), 20);
			});			
		}

	}
	
	public static void inputValuesAndSave(SeleniumHelper sh, WhatIfScenario scenario) {
		By rowSelectorCheckbox = By.xpath(rowCheckbox.replace("{{name}}", scenario.name));
		
		// this method is best left without a retry loop as saving navigates across multiple screens
		clearAndInputText(sh, sh.getSelectorBasedOnLabel(nameLabel), scenario.name);
		dropdownSelect(sh, typeSelect, scenario.type);
		clearAndInputText(sh, sh.getSelectorBasedOnLabel(dateLabel), scenario.date);
		click(sh, saveButton);
		waitClickable(sh, rowSelectorCheckbox, 40);
	}
	
	public static void acceptWhatIfScenario(SeleniumHelper sh, String scenarioName) {
		By rowSelectorCheckbox = By.xpath(rowCheckbox.replace("{{name}}", scenarioName));
		executeSequenceWithRetry(sh, 3, () -> {
			/* refresh the browsers                     */	sh.refreshBrowser();
			/* select the scenario you want to accept	*/	checkboxSelect(sh, rowSelectorCheckbox, true);
			/* click accept								*/	click(sh, acceptButton);
			try {
			/* wait for the job spinner to appear		*/	waitClickable(sh, jobSpinner, 20);
			} catch(Exception c) { sh.LogInfoMessageLine( "Didn't see job spinner?!" ); }
			/* wait for the accept button to re-appear	*/	waitClickable(sh, acceptButton, 40);
		});
	}
	
}
