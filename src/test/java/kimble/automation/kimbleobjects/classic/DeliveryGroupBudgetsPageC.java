package kimble.automation.kimbleobjects.classic;

import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DeliveryGroupBudgetsPageC {
	
	public static final String elementGroupCardTitle = "//a[normalize-space(text())=\"{{element}}\"]";
	public static final String elementGroupCard = elementGroupCardTitle + "/ancestor::div[normalize-space(@class)='delivery-element-group card']";
	public static final String elementCard = elementGroupCard + "//div[contains(@class, 'delivery-element-card')][1]";
	public static final String elementBurgerMenu = elementCard + "//div[@class='fa fa-bars burger-menu'][1]";
	public static final String elementBurgerMenuPopup = elementCard + "//ul[@class='jq-dropdown-menu']";

	public static final String elementBurgerMenuOption = elementBurgerMenuPopup + "/li/a[normalize-space(text())='{{option}}']";
	
	public static final String purchaseOrderRulePopup = "//div[@id='PORulePopup']";
	public static final String purchaseOrderRuleOption = purchaseOrderRulePopup + "//label[normalize-space(text())='{{poRuleOption}}']";
	public static final String purchaseOrderRulePopupSaveButton = purchaseOrderRulePopup + "//input[normalize-space(@value)='Save']";
	
	public static final By nextButton = By.xpath("//input[@value='Next']");
	public static final By finishButton = By.xpath("//input[@value='Finish']");
	public static final By inputAllocation = By.xpath("//input[@value='0.00']");
	public static final By creditAllocationLabel = By.xpath("//div[@class='element-credit-label']");
	
	
	public static void setPurchaseOrderRule(SeleniumHelper sh, String element, String ruleOption) {
		sh.waitForLightningSpinnerToBeHidden();
		By burgerMenuSelector = By.xpath(elementBurgerMenu.replace("{{element}}", element));
		By optionSelector = By.xpath(elementBurgerMenuOption.replace("{{element}}", element).replace("{{option}}", "Edit Purchase Order Rule"));
		By ruleOptionSelector = By.xpath(purchaseOrderRuleOption.replace("{{poRuleOption}}", ruleOption));
		By poRulePopupSaveSelector = By.xpath(purchaseOrderRulePopupSaveButton);
		sh.clickAndWaitSequenceWithRefreshRetry(5, burgerMenuSelector, optionSelector, ruleOptionSelector, poRulePopupSaveSelector, burgerMenuSelector);
	}
	

	public static void allocateCreditAgainstElement(SeleniumHelper sh, DeliveryElement element) {
		sh.waitForLightningSpinnerToBeHidden();
		By burgerMenuSelector = By.xpath(elementBurgerMenu.replace("{{element}}", element.name));
		By optionSelector = By.xpath(elementBurgerMenuOption.replace("{{element}}", element.name).replace("{{option}}", "Allocate Credit"));
		sh.clickAndWait(burgerMenuSelector, optionSelector, 5);
		sh.clickAndWait(optionSelector, nextButton, 5);
		
		//checks that the Credit Value 
		List<WebElement> headerRow = sh.getWebElements(By.tagName("th"));
		int rowNumber = sh.getWebElements(By.tagName("th")).size();	
		for (int i = 0; i < rowNumber; i++){
				if (headerRow.get(i).getText().equals("Credit Value")){
					i = i+1;
					WebElement cellValue = sh.getWebElement(By.xpath("//td[@class='dataCell  '][" + i + "]"));
					sh.assertEquals(cellValue.getText(), element.creditValue);
					break;
					}	
		};
		sh.clickAndWait(nextButton, finishButton, 5);
		
		sh.clearAndSendKeysIfVisibleAndEnabled(inputAllocation, element.creditAllocation);
		sh.clickAndWait(finishButton, creditAllocationLabel, 5);
				
	}

}