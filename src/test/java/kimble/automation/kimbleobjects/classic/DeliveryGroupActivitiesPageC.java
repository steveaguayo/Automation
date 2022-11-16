package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.ResourcedActivity;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class DeliveryGroupActivitiesPageC {
	
	// templated
	static String
	activityTitle = "//h3[normalize-space(@class)='cardTitle']/a[normalize-space(text())=\"{{activity}}\"]",
	activityCard = "//div[contains(@class, 'card')][contains(@class, 'resourced-activity')][" + activityTitle + "]",
	activityBurgerMenu = activityCard + "/div[normalize-space(@class)='cardHeadingContainer']//div[contains(@class, 'fa-bars')][contains(@class, 'burger-menu')]",
	burgerDropDown = activityCard + "//div[contains(@class, 'jq-dropdown')]/ul[normalize-space(@class)='jq-dropdown-menu']",
	editActivityMenuItem = burgerDropDown + "/li/a[normalize-space(text())='Edit Resourced Activity']",
	editActivityPopup = "//div[normalize-space(@id)='ResourcedActivityEditPopup']";
	
	static By
	usageUnitsSelect = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='Usage Units']/../following-sibling::td[1]/select[1]"),
	allocationTypeSelect = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='Usage Allocation Type']/../following-sibling::td[1]/select[1]"),
	hideForecastTimeEntriesCheckbox = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='Hide Forecast Time Entries']/../following-sibling::td[1]/input[1]"),
	canSelfAssignCheckbox = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='Can Self Assign?']/../following-sibling::td[1]/input[1]"),
	usageTrackingModelSelect = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='Usage Tracking Model']/../following-sibling::td[1]/select[1]"),
	utilisationRuleSelect = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='Utilisation Rule']/../following-sibling::td[1]/select[1]"),
	timeEntryRuleSelect = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='Time Entry Rule']/../following-sibling::td[1]/select[1]"),
	calendarShowAsSelect = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='Calendar Show As']/../following-sibling::td[1]/select[1]"),
	scheduledTimeRuleSelect = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='Scheduled Time Rule']/../following-sibling::td[1]/select[1]"),
	calendarIsPrivateCheckbox = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='Calendar Is Private']/../following-sibling::td[1]/input[1]"),
	externalIdInput = By.xpath(editActivityPopup + "//th/label[normalize-space(text())='External Id']/../following-sibling::td[1]/input[1]"),
	
	saveButton = By.xpath("//input[@value='Save']"),
	
	// period rate band edits
	businessDayStandard = By.linkText("Business Day - Standard Time"),
	businessDayOvertime = By.linkText("Business Day - Overtime"),
	nonBusinessDayStandard = By.linkText("Non-Business Day - Standard Time"),
	nonBusinessDayOvertime = By.linkText("Non-Business Day - Overtime"),

	toilFactorInput = By.xpath("//label[text()='TOIL Factor']/../following-sibling::td[1]/input[1]"),
	saveButtonPeriodRateBand = By.cssSelector("input[name$='saveActivityRateBandBtn']");    
	
	
	public static void editActivity(SeleniumHelper sh, ResourcedActivity activity) {
		By menu = By.xpath(activityBurgerMenu.replace("{{activity}}", activity.name));
		By editMemuItem = By.xpath(editActivityMenuItem.replace("{{activity}}", activity.name));
		
		sh.waitForLightningSpinnerToBeHidden();
		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20, 
			/* click the burger menu					*/	menu,
			/* click 'Edit Resourced Activity'			*/	editMemuItem,
			/* wait for the usage units input			*/	usageUnitsSelect
			);
			
			dropdownSelect(sh, usageUnitsSelect, activity.usageUnits);
			dropdownSelect(sh, allocationTypeSelect, activity.allocationType);
			
			checkboxSelect(sh, hideForecastTimeEntriesCheckbox, activity.hideForecastTimeEntries);
			checkboxSelect(sh, canSelfAssignCheckbox, activity.canSelfAssign);
			
			dropdownSelect(sh, usageTrackingModelSelect, activity.usageTrackingModel);
			dropdownSelect(sh, utilisationRuleSelect, activity.utilisationRule);
			dropdownSelect(sh, timeEntryRuleSelect, activity.timeEntryRule);
			dropdownSelect(sh, calendarShowAsSelect, activity.calendarShowAs);
			dropdownSelect(sh, scheduledTimeRuleSelect, activity.scheduledTimeRule);
			
			checkboxSelect(sh, calendarIsPrivateCheckbox, activity.calendarIsPrivate);
			
			clearAndInputText(sh, externalIdInput, activity.externalId);
		
			// there's a glitch where the boxy seems to flicker in and out and cause an error
			click(sh, saveButton);
			sh.waitForBoxyToBeHidden();
			waitClickable(sh, menu, 20);
//			clickAndWaitSequence(sh, 20, 
//			/* click save								*/	saveButton,
//			/* wait for the burger menu					*/	menu
//			);
		});
	}
	
	public static void editActivityPeriodRateBand(SeleniumHelper sh, ResourcedActivity activity) {
		sh.waitForLightningSpinnerToBeHidden();
		if(activity.businessDayStandardToilFactor != null)	{
			sh.clickAndWait(businessDayStandard, saveButtonPeriodRateBand, 20);
			clearAndInputText(sh, toilFactorInput, activity.businessDayStandardToilFactor);
			waitClickable(sh, saveButtonPeriodRateBand, 20);
			click(sh, saveButtonPeriodRateBand);
			sh.waitForBoxyToBeHidden();
			waitClickable(sh, businessDayStandard, 20);
		}
		if(activity.businessDayOvertimeToilFactor != null)	{
			sh.clickAndWait(businessDayOvertime, saveButtonPeriodRateBand, 20);
			clearAndInputText(sh, toilFactorInput, activity.businessDayOvertimeToilFactor);
			waitClickable(sh, saveButtonPeriodRateBand, 20);
			click(sh, saveButtonPeriodRateBand);	
			sh.waitForBoxyToBeHidden();
			waitClickable(sh, businessDayOvertime, 20);		
		}
		if(activity.nonBusinessDayStandardToilFactor != null)	{
			sh.clickAndWait(nonBusinessDayStandard, saveButtonPeriodRateBand, 20);
			clearAndInputText(sh, toilFactorInput, activity.nonBusinessDayStandardToilFactor);
			waitClickable(sh, saveButtonPeriodRateBand, 20);
			click(sh, saveButtonPeriodRateBand);
			sh.waitForBoxyToBeHidden();
			waitClickable(sh, nonBusinessDayStandard, 20);
		}
		if(activity.nonBusinessDayOvertimeToilFactor != null)	{
			sh.clickAndWait(nonBusinessDayOvertime, saveButtonPeriodRateBand, 20);
			clearAndInputText(sh, toilFactorInput, activity.nonBusinessDayOvertimeToilFactor);
			waitClickable(sh, saveButtonPeriodRateBand, 20);
			click(sh, saveButtonPeriodRateBand);
			sh.waitForBoxyToBeHidden();		
			waitClickable(sh, nonBusinessDayOvertime, 20);		
		}
	}
}