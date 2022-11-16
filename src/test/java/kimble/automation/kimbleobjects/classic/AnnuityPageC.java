package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.Annuity;
import kimble.automation.domain.AnnuityResults;
import kimble.automation.helpers.General;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

public class AnnuityPageC extends BasePageC {
	
	private static final String 
	ANNUITY_EDIT_POPUP = "AnnuityEditPopup",

	INPUT_ID$_ANNUITY_NAME = "input[id$='annuity-name']",

	INPUT_ID_NEW_ANNUITY_BUTTON = "input[id='newAnnuityButton']",
	
	ANNUITY_NAME_TOKEN = "<ANNUITY_NAME>",
	ANNUITY_XPATH = "//tr[contains(@class,'dataRow')]/td[.='" + ANNUITY_NAME_TOKEN + "']",
	
	annuityNameCell = "//td[normalize-space(text())=\"{{annuity}}\"]",
	annuityRow = annuityNameCell + "/ancestor::tr[1]",
	
	ganttRevenueAnnuityPeriod = "//div[@annuity-id='{{annuityId}}'][@annuity-type='revenue'][{{index}}]",
	ganttCostAnnuityPeriod = "//div[@annuity-id='{{annuityId}}'][@annuity-type='cost'][{{index}}]",
	completeAnnuityPopup = "//div[@id='AnnuityCompletePopup']",
	completeAnnuityPopupCompleteButton = completeAnnuityPopup + "//input[@value='Complete']";
	
	static final By 
	unitsField = By.cssSelector("input[id$='number-of-units']"),
	deliveryElementSelect = By.cssSelector("select[id$='delivery-element-select']"),
	loadingIndicator = By.cssSelector("#loadingIndicatorOuterContainer"),
	annuityStartDateInput = By.cssSelector("input[id$='annuity-start-date-input']"),
	numberOfPeriodsInput = By.cssSelector("input[id$='number-of-periods']"),
	annuityEndDateInput = By.cssSelector("input[id$='annuity-end-date-input']"),
	annuityCostInput = By.cssSelector("input[id$='annuity-cost-input']"),
	annuityRevenueInput = By.cssSelector("input[id$='annuity-revenue-input']"),
	saveAnnuityButton = By.cssSelector("input[id$='saveAnnuity']");

	public AnnuityPageC(SeleniumHelper seleniumHelperInstance) {
		super(seleniumHelperInstance);
	}

	public void CreateNew(SeleniumHelper sh, Annuity annuityDetails) {		
		executeSequenceWithRefreshRetry(theSH, 3, () -> { 
			/* wait for the loading indicator to go		*/ waitHidden(theSH, loadingIndicator, 20);
			clickAndWaitSequence(theSH, 20, 
			/* click new 								*/ By.cssSelector(INPUT_ID_NEW_ANNUITY_BUTTON),
			/* wait for name input						*/ By.cssSelector(INPUT_ID$_ANNUITY_NAME)
			);
			if(annuityDetails.elementName != null) {
			/* input delivery element name if present	*/ dropdownSelect(theSH, deliveryElementSelect, annuityDetails.elementName);
			/* wait for dom to start reloading			*/ theSH.sleep(4000);
			/* wait name input to reappear				*/ waitClickable(theSH, By.cssSelector(INPUT_ID$_ANNUITY_NAME), 20);
			}
			/* input name								*/ clearAndInputText(theSH, By.cssSelector(INPUT_ID$_ANNUITY_NAME), annuityDetails.name);
			/* input start date							*/ clearAndInputText(theSH, annuityStartDateInput, annuityDetails.startDate);
			/* input number of periods					*/ clearAndInputText(theSH, numberOfPeriodsInput, annuityDetails.numberOfPeriods);
			/* wait for dom update						*/ theSH.sleep(2000);
			/* input end date							*/ clearAndInputText(theSH, annuityEndDateInput, annuityDetails.endDate);
			/* input cost								*/ clearAndInputText(theSH, annuityCostInput, annuityDetails.cost);
			/* input revenue							*/ clearAndInputText(theSH, annuityRevenueInput, annuityDetails.revenue);
			/* input units								*/ clearAndInputText(theSH, unitsField, annuityDetails.units);
			/* click save 								*/ click(theSH, saveAnnuityButton);
			/* wait for popup to go						*/ waitHidden(theSH, By.id(ANNUITY_EDIT_POPUP), 20);
			/* wait for annuity info 					*/ waitClickable(theSH, By.xpath(ANNUITY_XPATH.replace(ANNUITY_NAME_TOKEN, annuityDetails.name)), 20);
		});

	}
	
	public String getAnnuityId(String annuity) {
		By annuityRowSelector = By.xpath(annuityRow.replace("{{annuity}}", annuity));
		theSH.waitForElementToBeClickableWithRefreshRetry(annuityRowSelector, 20);
		return theSH.getWebElement(annuityRowSelector).getAttribute("node-id");
	}
	
	public void completeRevenueAnnuityPeriod(String annuity, int periodIndex) {
		completeRevenueAnnuityPeriodById(getAnnuityId(annuity), periodIndex);
	}
	
	public void completeCostAnnuityPeriod(String annuity, int periodIndex) {
		completeCostAnnuityPeriodById(getAnnuityId(annuity), periodIndex);
	}
	
	public void completeRevenueAnnuityPeriodById(String annuityId, int periodIndex) {
		General.removeWalkMeElement(theSH);
		By annuityPeriodSelector = By.xpath(ganttRevenueAnnuityPeriod.replace("{{annuityId}}", annuityId).replace("{{index}}", "" + periodIndex));
		
		executeSequenceWithRetry(theSH, 3, () -> {
			Actions actions = new Actions(theSH.getWD());
			actions.moveToElement(theSH.getWebElement(annuityPeriodSelector));
			actions.perform();
			By[] sequence = new By[]{
				/* click the revenue annuity period				*/ annuityPeriodSelector,
				/* click complete								*/ By.xpath(completeAnnuityPopupCompleteButton),
				/* wait for the new annuity button to be usable	*/ By.cssSelector(INPUT_ID_NEW_ANNUITY_BUTTON)
			};
			try {
				theSH.clickAndWaitSequenceWithRefreshRetry(5, sequence);
			} catch(Exception e) {
				General.removeWalkMeElement(theSH);
				theSH.clickAndWaitSequenceWithRefreshRetry(5, sequence);
			}
			theSH.waitForBoxyToBeHidden();
		
		});
	}
	
	public void completeCostAnnuityPeriodById(String annuityId, int periodIndex) {
		General.removeWalkMeElement(theSH);
		By annuityPeriodSelector = By.xpath(ganttCostAnnuityPeriod.replace("{{annuityId}}", annuityId).replace("{{index}}", "" + periodIndex));
		
		executeSequenceWithRetry(theSH, 3, () -> {
			Actions actions = new Actions(theSH.getWD());
			actions.moveToElement(theSH.getWebElement(annuityPeriodSelector));
			actions.perform();
			By[] sequence = new By[]{
				/* click the cost annuity period				*/ annuityPeriodSelector,
				/* click complete								*/ By.xpath(completeAnnuityPopupCompleteButton),
				/* wait for the new annuity button to be usable	*/ By.cssSelector(INPUT_ID_NEW_ANNUITY_BUTTON)
			};
			
			try {
				theSH.clickAndWaitSequenceWithRefreshRetry(5, sequence);
			} catch(Exception e) {
				General.removeWalkMeElement(theSH);
				theSH.clickAndWaitSequenceWithRefreshRetry(5, sequence);
			}
			theSH.waitForBoxyToBeHidden();
		
		});
	}
	
	public void extendAnnuity(SeleniumHelper sh, Annuity a){
		By unitsUpdatedField = By.xpath("//input[@start-date='"+a.newUnitsDate+"']");
		
		String id = getAnnuityId(a.name);
		sh.clickLink(By.xpath("//a[@annuity-id='"+id+"']/i") );
		clearAndInputText(theSH, annuityEndDateInput, a.newEndDate);
		
		if(!(a.newUnits.isEmpty())){
			clearAndInputText(theSH, unitsUpdatedField, a.newUnits);
		}
		
		if((a.newUnitsDate.contains("-"))){
			By unitsQuantity = By.xpath("//input[@start-date='"+a.newUnitsDate+"']");
			sh.waitForElementToBeClickable(unitsQuantity);
			clearAndInputText(theSH, unitsQuantity, a.newUnits);
		}
		
		if((a.newUnitsDate2.contains("-"))){
			By unitsQuantity = By.xpath("//input[@start-date='"+a.newUnitsDate2+"']");
			clearAndInputText(theSH, unitsQuantity, a.newUnits);
		}
		
		if((a.newUnitsDate3.contains("-"))){
			By unitsQuantity = By.xpath("//input[@start-date='"+a.newUnitsDate3+"']");
			clearAndInputText(theSH, unitsQuantity, a.newUnits);
		}
		
		click(theSH, saveAnnuityButton);
		waitClickable(theSH, By.xpath(ANNUITY_XPATH.replace(ANNUITY_NAME_TOKEN, a.name)), 20);
		
		validateAnnuityExtension(sh, a);
	}
	
	static final String
	rowByValue = "//td[@class='annuity-value-col'][normalize-space(text())='VALUE_HERE']",
	rowByStartDate = "//td[@class='annuity-value-col'][normalize-space(text())='VALUE_HERE']/preceding-sibling::td[@class='annuity-start-dte-col']",
	rowByEndDate = "//td[@class='annuity-value-col'][normalize-space(text())='VALUE_HERE']/preceding-sibling::td[@class='annuity-end-dte-col']",
	rowByType = "//td[@class='annuity-value-col'][normalize-space(text())='VALUE_HERE']/preceding-sibling::td[@class='annuity-type-col']";
	
	static final By
	cancelBtn = By.xpath("//input[@value='Cancel'][@id='cancelCompleteItemsButton']"),
	newAnnuityBtn = By.xpath("//input[@value='New Annuity']");
	
	public void validateAnnuityExtension(SeleniumHelper sh, Annuity a){
		for(AnnuityResults an : a.annuityResults){
			sh.clickLink(By.xpath("//div[normalize-space(text())='"+an.monthYear+"']"));
			
			validateStringValue(sh, By.xpath(rowByValue.replace("VALUE_HERE", an.value)), an.value, "assert Value: " + an.value);
			validateStringValue(sh, By.xpath(rowByStartDate.replace("VALUE_HERE", an.value)), an.startDate, "assert Start Date: " + an.startDate);
			validateStringValue(sh, By.xpath(rowByEndDate.replace("VALUE_HERE", an.value)), an.endDate, "assert End Date: " + an.endDate);
			validateStringValue(sh, By.xpath(rowByType.replace("VALUE_HERE", an.value)), an.type, "assert Type: " + an.type);
			
			sh.clickLink(cancelBtn);
			sh.waitForElementToBeClickable(newAnnuityBtn);
		}
	}
}








