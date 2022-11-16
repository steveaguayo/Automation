package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.Milestone;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DeliveryGroupMilestonesPageC extends BasePageC {
	
	private static final String COMPLETE_MILESTONE_BTN = "input[id$='saveAndCompleteMilestone']";
	private static final String INPUT_ID$_SAVE_MILESTONE = "input[id$='saveMilestone']";
	private static final String EDIT_MILESTONE_LINK = "//td[normalize-space(text())=\"{{element}}\"]/ancestor::tr[contains(@class, 'element-row')]/following-sibling::tr//a[@class='open-milestone-link'][text()=\"{{milestone}}\"]/parent::td//preceding-sibling::td[@class='action-col']/a[contains(@href,'editMilestone')]";
	private static final String INPUT_ID$_SAVE_DELIVERY_ELEMENT = "input[id$='saveDeliveryElement']";
	private static final String INPUT_ID$_CANCEL_DELIVERY_ELEMENT = "input[id$='cancelDeliveryElement']";
	private static final String DELIVERY_ELEMENT_POPUP = "DeliveryElementPopup";
	private static final String REVENUE_ITEM = "RevenueItem";
	private static final String MILESTONE_FORECAST_MODE = "Milestone Forecast Mode";
	private static final String A_CLASS_OPEN_MILESTONE_LINK_CONTAINS_TEXT_S = "//a[@class='open-milestone-link'][contains(text(),'%s')]";
	private static final String NEW_COST_MILESTONE = "Add Cost Milestone";
	private static final String NEW_REVENUE_MILESTONE = "Add Revenue Milestone";
	private static final String INPUT_ID$_MILESTONE_VALUE = "input[id$='ilestoneValue']";
	private static final String INPUT_ID$_COST_MILESTONE_VALUE = "input[id$='MilestoneValue']";	
	private static final String ELEMENT_NAME_TOKEN = "<ELEMENT_NAME>";
	private static final String ELEMENT_MILESTONE_BURGER_MENU = "//td[contains(.,\"" + ELEMENT_NAME_TOKEN + "\")]/preceding-sibling::td[@class=\"action-col\"]/div[contains(@class,\"burger-menu\")]";
	private static final String MILESTONE_POPUP = "MilestonePopup";
			
	@FindBy(css = INPUT_ID$_MILESTONE_VALUE)
	private WebElement milestoneValueField;	
	
	@FindBy(css = INPUT_ID$_COST_MILESTONE_VALUE)
	private WebElement MilestoneValueField;	
	
	@FindBy(css = "input[id$='milestoneName']")
	private WebElement milestoneNameField;
	
	@FindBy(css = "input[id$='milestoneDateInput']")
	private WebElement milestoneDateField;	
	
	@FindBy(css = INPUT_ID$_SAVE_MILESTONE)
	private WebElement saveMilestoneBtn;
		
	public DeliveryGroupMilestonesPageC(SeleniumHelper seleniumHelperInstance) {
		super(seleniumHelperInstance);
	}

	public void CreateNewRevenueMilestone(String elementName, Milestone milestoneDetails) {
		CreateNewRevenueMilestone(null, elementName, milestoneDetails);
	}

	public void CreateNewRevenueMilestone(String parentElementName, String elementName, Milestone milestoneDetails) {
		String burgerMenuXPath, menuItemName;
		if(parentElementName != null) {
			setRevenueForecastMode(parentElementName, true);
			burgerMenuXPath = ELEMENT_MILESTONE_BURGER_MENU.replace(ELEMENT_NAME_TOKEN, parentElementName);
			menuItemName = elementName + " - " + NEW_REVENUE_MILESTONE;
		}
		else {
			setRevenueForecastMode(elementName, false);
			burgerMenuXPath = ELEMENT_MILESTONE_BURGER_MENU.replace(ELEMENT_NAME_TOKEN, elementName);
			menuItemName = NEW_REVENUE_MILESTONE;
		}
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			theSH.clickAndWaitSequence(10, By.xpath(burgerMenuXPath), By.linkText(menuItemName), By.cssSelector(INPUT_ID$_MILESTONE_VALUE));
			CreateNew(elementName, milestoneDetails);
		});
	}

	public void CreateNewCostMilestone(String elementName, Milestone milestoneDetails) {
		String burgerMenuXPath = ELEMENT_MILESTONE_BURGER_MENU.replace(ELEMENT_NAME_TOKEN, elementName);
		
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			theSH.clickAndWaitSequence(10, By.xpath(burgerMenuXPath), By.linkText(NEW_COST_MILESTONE), By.cssSelector(INPUT_ID$_COST_MILESTONE_VALUE));
			CreateNew(elementName, milestoneDetails);
		});
	}
	
	private void CreateNew(String elementName, Milestone milestoneDetails) {
						
		theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_MILESTONE_VALUE), 20);
		
		milestoneNameField.sendKeys(milestoneDetails.name);

		milestoneDateField.sendKeys(milestoneDetails.date);
		milestoneDateField.sendKeys(Keys.TAB);
		
		milestoneValueField.sendKeys(milestoneDetails.value.toString());

		saveMilestoneBtn.click();
		
		theSH.waitForElementToBeHidden(By.id(MILESTONE_POPUP));
		
		// wait for a link to be present to open the created milestone
		theSH.waitForElementToBeClickable(By.xpath(String.format(A_CLASS_OPEN_MILESTONE_LINK_CONTAINS_TEXT_S, milestoneDetails.name)), 40);
	}

	public void setRevenueForecastMode(String elementName, boolean hasChildElement) {
		// ensure that the forecast mode is set correctly
		String burgerMenuXPath = ELEMENT_MILESTONE_BURGER_MENU.replace(ELEMENT_NAME_TOKEN, elementName);
		String menuItemName = (hasChildElement ? elementName + " - " : "") + MILESTONE_FORECAST_MODE;
		
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			theSH.selectBurgerMenuOption(By.xpath(burgerMenuXPath), menuItemName, By.id(DELIVERY_ELEMENT_POPUP));
			
			// wait for the popup to appear
			theSH.waitForElementToBeVisible(By.id(DELIVERY_ELEMENT_POPUP));
			theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_SAVE_DELIVERY_ELEMENT));
			
			// if the forecast mode has been set there will be a hyperlink to that forecast mode name
			// if not then we need to set the appropriate radio button and save
			if(!(theSH.checkElementVisible(By.linkText(REVENUE_ITEM))))
			{
				theSH.getElementBasedOnLabel(REVENUE_ITEM).click();
				theSH.sleep(2000);
				theSH.getWebElement(By.cssSelector(INPUT_ID$_SAVE_DELIVERY_ELEMENT)).click();
			}
			else
			{
				theSH.getWebElement(By.cssSelector(INPUT_ID$_CANCEL_DELIVERY_ELEMENT)).click();
			}
			
			theSH.waitForElementToBeHidden(By.id(DELIVERY_ELEMENT_POPUP));
			
			// small pause for the screen to refresh
			theSH.sleep(2000);
		});
	}
	
	public void CompleteMilestone(String elementName, String milestoneName)
	{
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			By editLink = By.xpath(EDIT_MILESTONE_LINK.replace("{{element}}", elementName).replace("{{milestone}}", milestoneName));
			By popup = By.id(MILESTONE_POPUP);
			theSH.clickAndWaitSequenceWithRefreshRetry(10, editLink, popup);

			theSH.getWebElement(By.cssSelector(COMPLETE_MILESTONE_BTN)).click();

			theSH.waitForElementToBeHidden(popup);
			
			// small pause for the screen to refresh
			theSH.sleep(2000);
		});
	}
}
