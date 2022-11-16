package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class DeliveryElementPageC extends BasePageC {
	
	private static final String ADD_ELEMENT_IN_WIZARD = "span[onclick='addNewElementToGroup()']";

	private static final String CREATE_A_NEW_CHANGE_ORDER = "Create a new Change Order";

	private static final String INPUT_ID$_OPERATED_WITH_PRIMARY = "input[id$='OperatedWithPrimary']";
	
	private static final String NEW_ELEMENT_OK_BUTTON = "input[id$='saveDeliveryElement'][value='OK']";

	private static final String NEW_CHANGE_ELEMENT = "New Change Element";

	private static final String EDIT = "Edit";

	private static final String DELIVERY_ELEMENT_POPUP = "DeliveryElementPopup";
	private static final String NEW_DELIVERY_ELEMENT_POPUP = "NewDeliveryElementPopup";
	
	private static final String INPUT_ID$_ELEMENT_NAME = "input[id$='elementName']";
	private static final String NEW_POPUP_ELEMENT_NAME = "input[id$='newDeliveryElementName']";
	
	private static final String SELECT_ID$_DELIVERY_ELEMENT_PRODUCT_SELECT = "select[id$='productInput']";
	private static final String DELIVERY_ELEMENT_PRODUCT_WIZARD_SELECT = "select[id$='newDeliveryElementProductSelect']";

	private static final String DELIVERYENGAGEMENTNAMETOKEN = "<deliveryengagementname>";
	private static final String DELIVERYELEMENTNAMETOKEN = "<deliveryelementname>";

	private static final String EDIT_ELEMENT_BTN_XPATH_TEMPLATE = "//a[contains(text(),\"" + DELIVERYENGAGEMENTNAMETOKEN + "\")]/parent::h3/parent::div/parent::div/following-sibling::div//span[.='" + DELIVERYELEMENTNAMETOKEN + "']/parent::div/following-sibling::div[contains(@class,'delivery-element-menu')]";
		
	static By saveDeliveryElementButton = By.cssSelector("input[id$='saveDeliveryElement']");

	public DeliveryElementPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void CreateNewInWizard(DeliveryEngagement deliveryEngagementDetails, DeliveryElement deliveryElementDetails) {
		// click the wizard new button	
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(ADD_ELEMENT_IN_WIZARD), 3);
		theSH.getWebElement(By.cssSelector(ADD_ELEMENT_IN_WIZARD)).click();
		
		// temp sleep as after changing the product the screen is refreshed
//		theSH.sleep(3000);
		theSH.waitForElementToBeClickable(By.cssSelector(NEW_POPUP_ELEMENT_NAME));
		theSH.getWebElement(By.cssSelector(NEW_POPUP_ELEMENT_NAME)).clear();
		theSH.getWebElement(By.cssSelector(NEW_POPUP_ELEMENT_NAME)).sendKeys(deliveryElementDetails.name);
		
		theSH.selectByVisibleText(theSH.getWebElement(By.cssSelector(DELIVERY_ELEMENT_PRODUCT_WIZARD_SELECT)), deliveryElementDetails.product);
		
		theSH.getWebElement(By.cssSelector(NEW_ELEMENT_OK_BUTTON)).click();
		
		// the screen takes a while to close after clicking save so wait for it to have closed
		theSH.waitForElementToBeHidden(By.id(NEW_DELIVERY_ELEMENT_POPUP), 40);
	}

	public void EditExisting(String deliveryEngagementName, String deliveryElementName){
		
		String editDeliveryElementBtnXPath = EDIT_ELEMENT_BTN_XPATH_TEMPLATE.replace(DELIVERYENGAGEMENTNAMETOKEN, deliveryEngagementName).replace(DELIVERYELEMENTNAMETOKEN, deliveryElementName);
		theSH.selectBurgerMenuOption(By.xpath(editDeliveryElementBtnXPath), EDIT, By.cssSelector(SELECT_ID$_DELIVERY_ELEMENT_PRODUCT_SELECT));
						
//		theSH.waitForElementToBeClickable(By.cssSelector(SELECT_ID$_DELIVERY_ELEMENT_PRODUCT_SELECT));
	}

	public void NavigateToAssignments() {
		theSH.clickMenuItem(PagesC.ACTIVITYASSIGNMENTSDELIVERY);
		
		// current screen implementation only shows assignments which end within the last four months
		// override this behaviour to ensure that the screen shows all assignments known to be created in the automation scenarios
		ActivityAssignmentPageC activityAssignmentHandler = new ActivityAssignmentPageC(theSH);
		activityAssignmentHandler.DefaultForecastingFromDate();
	}
	
	public void NavigateToPlanDashboard() {
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.clickMenuItem(PagesC.TRACKINGPLANTASKS);
//		theSH.sleep(3000);
	}
	
	public void NavigateToPlanGantt() {
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.clickMenuItem(PagesC.TRACKINGPLANGANTT);
//		theSH.sleep(3000);
	}

	public void UpdateProductAndName(String step2Product, String elementName) {
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_ELEMENT_NAME));
			theSH.getWebElement(By.cssSelector(INPUT_ID$_ELEMENT_NAME)).clear();
			theSH.getWebElement(By.cssSelector(INPUT_ID$_ELEMENT_NAME)).sendKeys(elementName);
			
			theSH.selectByVisibleText(theSH.getWebElement(By.cssSelector(SELECT_ID$_DELIVERY_ELEMENT_PRODUCT_SELECT)), step2Product);
			
			waitClickable(theSH, saveDeliveryElementButton, 20);
			click(theSH, saveDeliveryElementButton);
			
			// the screen takes a while to close after clicking save so wait for it to have closed
			theSH.waitForElementToBeHidden(By.id(DELIVERY_ELEMENT_POPUP), 40);		
		});
	}

	public void createChangeElement(String deliveryEngagementName, DeliveryElement deliveryElementDetails) {
		String editDeliveryElementBurgerXPath = EDIT_ELEMENT_BTN_XPATH_TEMPLATE.replace(DELIVERYENGAGEMENTNAMETOKEN, deliveryEngagementName).replace(DELIVERYELEMENTNAMETOKEN, deliveryElementDetails.parentElementName);
		theSH.selectBurgerMenuOption(By.xpath(editDeliveryElementBurgerXPath), NEW_CHANGE_ELEMENT, By.cssSelector(SELECT_ID$_DELIVERY_ELEMENT_PRODUCT_SELECT));
						
		
		// temp sleep as after changing the product the screen is refreshed
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_ELEMENT_NAME));
		theSH.getWebElement(By.cssSelector(INPUT_ID$_ELEMENT_NAME)).clear();
		theSH.getWebElement(By.cssSelector(INPUT_ID$_ELEMENT_NAME)).sendKeys(deliveryElementDetails.name);
		
		if(!deliveryElementDetails.operatedWithPrimary)
		{
			theSH.getWebElement(By.cssSelector(INPUT_ID$_OPERATED_WITH_PRIMARY)).click();
		}

		theSH.waitForLightningSpinnerToBeHidden();
		theSH.getElementBasedOnLabel(deliveryElementDetails.changeEffective).click();
		
		// if a new change order we need to fill in extra info
		if(deliveryElementDetails.changeEffective.equals(CREATE_A_NEW_CHANGE_ORDER))
		{
			theSH.waitForElementToBeClickableBasedOnLabel(deliveryElementDetails.changeOrderBehaviourRule);
			theSH.getElementBasedOnLabel(deliveryElementDetails.changeOrderBehaviourRule).click();
		}

		theSH.waitForLightningSpinnerToBeHidden();
		theSH.selectByVisibleText(theSH.getWebElement(By.cssSelector(SELECT_ID$_DELIVERY_ELEMENT_PRODUCT_SELECT)), deliveryElementDetails.product);
		
		waitClickable(theSH, saveDeliveryElementButton, 20);
		click(theSH, saveDeliveryElementButton);
		
		// the screen takes a while to close after clicking save so wait for it to have closed
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeHidden(By.id(DELIVERY_ELEMENT_POPUP), 40);		
	}		
	
	static final By scopeElementPencil = By.xpath("//i[@class='fa fa-pencil inline-name-pencil toggle-for-save']");
	static final By scopeElementNameInput = By.xpath("//input[@class='inline-name-edit toggle-for-save']");
	static final By scopeElementNameSave = By.xpath("//input[@class='inline-name-edit toggle-for-save']/following-sibling::i[@title='Save']");
	static final By scopeBurgerMenu = By.xpath("//div[@class='fa fa-bars delivery-group-menu burger-menu']");
	static final By scopeEditDeliveryStatus = By.xpath("//a[normalize-space(text())='Edit Delivery Element Status']");
	static final By scopeArchiveElement = By.xpath("//td[normalize-space(text())='Element to Hide']/following-sibling::td[@class='archived-ind']/i");
	static final By scopeSaveStatus = By.xpath("//a[@id='saveButton']");
	
	
	public static void hideDeliveryElementInLDV(SeleniumHelper sh){
/*		// select row 1 and rename to "element to hide"
		sh.clickMenuItem(PagesC.DELIVERYGROUPREVENUESANDCOSTS);
		sh.waitForElementToBeClickable(scopeElementPencil, 60);
		sh.clickLink(scopeElementPencil);
		// burger menu edit status
		clearAndInputText(sh, scopeElementNameInput, "Element to Hide");
		sh.clickLink(scopeElementNameSave);
		sh.clickLink(scopeBurgerMenu);
		sh.clickLink(scopeEditDeliveryStatus);
		sh.clickLink(scopeArchiveElement);
//		sh.clickLink(scopeSaveStatus);
		sh.sleep(5000);
		// ensure row hidden
		sh.waitForElementToBeHidden(By.xpath("//td[@orig-name='Element to Hide']"));
		sh.refreshBrowser();
		sh.waitForElementToBeHidden(By.xpath("//td[@orig-name='Element to Hide']"));
*/		
		
	}
}
