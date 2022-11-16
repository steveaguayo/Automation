package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.lang.reflect.Array;
import java.sql.DriverAction;
import java.util.Arrays;

import kimble.automation.domain.ChangeControl;
import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.ExpectedValue;
import kimble.automation.domain.Fact;
import kimble.automation.domain.PurchaseOrder;
import kimble.automation.helpers.General;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.kimbleobjects.lightning.GeneralOperationsZ;
import kimble.automation.scenarios.StagesZ;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.thoughtworks.selenium.webdriven.commands.SeleniumSelect;

public class DeliveryEngagementPageC {
	
	static final String 
	NEW_PO = "newPO",
	CREATE_PURCHASE_ORDER = "Create Purchase Order",
	EXPECTED_END_DATE_INPUT = "input[id$='expectedEndDate']",
	HL_EXPECTED_END_DATE_INPUT = "input[id$='expectedHLEndDate']",
	EXPECTED_START_DATE_INPUT = "input[id$='expectedStartDate']",
	HL_EXPECTED_START_DATE_INPUT = "input[id$='expectedHLStartDate']",
	WIZARD_NEXT_BTN = "input[value$='Next']",
	DASHBOARD_CONTAINER = "dashboardContainer",
	DIV_ID_DELIVERY_GROUP_WAR_POPUP = "div[id='DeliveryGroupWARPopup']",
	WORK_AT_RISK = "Work at Risk",
	INPUT_ID$_SAVE_DELIVERY_GROUP_LOST_BTN = "input[id$='saveDeliveryGroupLostBtn']",
	LOST = "Lost",
	SPAN_ONCLICK_DETAIL_MODE = "span[onclick='detailMode()']",
	DELIVERY_GROUP_PO_MENU = "div[class*='delivery-group-po-menu']",
	PROPOSAL_MENU_DIV = "div[class*='fa-bars'][id$='proposal-menu']",
	DIV_CLASS_DELIVERY_GROUP_MENU = "div[class*='delivery-group-menu']",
	ADD_A_NEW_DELIVERY_ENGAGEMENT = "Add a New Delivery Engagement",
	EDIT_ENGAGEMENT_HIGH_LEVEL_FORECAST = "High Level Forecast",
	
	WAR_DELIVERY_STATUS = "WAR",
	UPDATE_WAR_STATUS_BTN= "input[id$='updateWARStatusBtn']",
	WAR_CANCEL_BTN= "//input[contains(@id,'updateWARStatusBtn')]/following-sibling::input",
	ALLOCATE_PURCHASE_ORDER_LINK = "Allocate Purchase Order",
	BTN_NEW_DELIVERY_ELEMENT = "btnNewDeliveryElement",
	SELECT_ID$_DELIVERY_GROUP_LOST_REASON_SELECT = "select[id$='deliveryGroupLostReasonSelect']",
	SELECT_ID$_DELIVERY_GROUP_PRODUCT_GROUP_SELECT = "select[id$='newDeliveryGroupProductGroup']",
	DELIVERY_GROUP_POPUP_LOST = "DeliveryGroupPopupLost",
	INPUT_ID_ADD_ANNUITY = "input[id='newAnnuityButton']",
	
	DELIVERY_ENGAGEMENTS = "Delivery Engagements",
	DELIVERY_ELEMENT_NAME_TOKEN = "<deliveryelementname>",
	ACTIVATE_SWITCH_XPATH_TEMPLATE = "//div[@class='delivery-element-title inline-name-edit-container']/span[contains(text(),\"" + DELIVERY_ELEMENT_NAME_TOKEN + "\")]/parent::div/parent::div/parent::div//span[@class='activation-switch-container']",
	DEACTIVATE_SWITCH_XPATH_TEMPLATE = "//div[@class='delivery-element-title inline-name-edit-container']/span[contains(text(),\"" + DELIVERY_ELEMENT_NAME_TOKEN + "\")]/parent::div/parent::div/parent::div//span[@class='activation-switch-container']/i[contains(@class, 'fa-toggle-on')]/parent::span",

	ELEMENT_PO_CHECKBOX = "//span[.=\"" + DELIVERY_ELEMENT_NAME_TOKEN + "\"]/parent::td/preceding-sibling::td/input",

	DELIVERY_ENGAGEMENT_NAME_TOKEN = "<deliveryengagementname>",
	ENGAGEMENT_MENU_XPATH_TEMPLATE = "//a[contains(text(),\"" + DELIVERY_ENGAGEMENT_NAME_TOKEN + "\")]/parent::h3/parent::div/following-sibling::div[contains(@class,'fa-bars delivery-group-menu')]",
	
	LINK_HREF_SPAN = "<linkspantext>",
	LINK_HREF_TEMPLATE = "//span[text()='" + LINK_HREF_SPAN + "']/..",

	ROW_INDEX_TOKEN = "<rowindex>",
	COLUMN_INDEX_TOKEN = "<columnindex>",
	PLAN_FACT_XPATH = "//table[@id='DeliveryGroupTrackingPlans']/tbody/tr[" + ROW_INDEX_TOKEN + "]/td[" + COLUMN_INDEX_TOKEN + "]/span",

	INPUT_ID$_CONTRACT_REVENUE = "input[id$='contractRevenue']",
	HL_INPUT_ID$_CONTRACT_REVENUE = "input[id$='contractRevenueHL']",

	WIZARD_FINISH = "input[value$='Finish']",

	INPUT_ID$_NEW_PO_REFERENCE = "input[id$='newPOReference']",

	DELIVERY_GROUP_FORECAST_POPUP = "DeliveryGroupForecastPopup",

	INPUT_ID$_SAVE_GROUP = "input[id$='saveGroupHL']",

	PASSED = "Passed",

	EFFORT = "Effort",
	REVENUE = "Revenue",
	COST = "Cost",
	
	BASELINE = "Baseline",
	EXPENDED = "Expended",
	REMAINING = "Remaining",
	TOTAL = "Total",
	VARIANCE = "Variance",
	
	// templated selectors 
	deliveryElementCardTitle = "//div[@class='delivery-element-title inline-name-edit-container']/span[normalize-space(text())=\"{{element}}\"]",
	deliveryElementCard = deliveryElementCardTitle + "/ancestor::div[normalize-space(@class)='delivery-element-card' or normalize-space(@class)='delivery-element-card level2']",
	deliveryElementContractCostLink = deliveryElementCard + "//span[normalize-space(text())='Contract Cost']/following-sibling::span/a[@class='undecorated-link']",
	saveActivateButton = "//input[normalize-space(@onclick)=\"activateResourcedActivity('{{id}}');\"]",

	revenueAdjustmentsMenuItem = "//a[contains(@href, 'DeliveryElementRevenueAdjustments?filterid={{id}}')]",
	supplierRequisitionsMenuItemById = "//a[contains(@href, 'DeliveryElementSupplierRequisitions?filterid={{id}}')]",
	
	dashboardElementSpan = "//span[@class='cursor-pointer'][normalize-space(text())=\"{{element}}\"]",
	
	editActivityDetailsBurgerMenu = "//div[@class='fa fa-bars resourced-activity-template-menu burger-menu']";
	
	static final By
	deliveryGroupShortNameField = By.cssSelector("input[id$='newDeliveryGroupShortName']"),
	saveDeliveryGroupLostBtn = By.cssSelector(INPUT_ID$_SAVE_DELIVERY_GROUP_LOST_BTN),
	deliveryGroupLostReasonSelect = By.cssSelector(SELECT_ID$_DELIVERY_GROUP_LOST_REASON_SELECT),
	deliveryGroupLostNarrativeField = By.cssSelector("textarea[id$='deliveryGroupLostNarrativeField']"),
	budgetsAndPODetailModeSwitch = By.cssSelector(SPAN_ONCLICK_DETAIL_MODE),	
	newPOReferenceField = By.cssSelector(INPUT_ID$_NEW_PO_REFERENCE),
	newPOOrderValueField = By.cssSelector("input[id$='newPOOrderValue']"),
	newPOOrderDateField = By.cssSelector("input[id$='newPOOrderDate']"),
	newPOExpiryDateField = By.cssSelector("input[id$='newPOExpiryDate']"),
	allocationCapField = By.cssSelector("input[id$='allocationCapField']"),
	POFinishButton = By.cssSelector(WIZARD_FINISH),
	goBtn = By.cssSelector("input[name='go']");
	
	public static void CreateNew(SeleniumHelper sh, DeliveryEngagement deliveryEngagementDetails){
		CreateNewEngagement(sh, true, deliveryEngagementDetails);
	}
	
	public static void CreateNewWithElements(SeleniumHelper sh, DeliveryEngagement deliveryEngagementDetails) {
		// passing false will mean this will not finish the wizard but leave
		// in a position where we can add additional elements
		CreateNewEngagement(sh, false, deliveryEngagementDetails);
	}
	
	public static boolean engagementExists(SeleniumHelper sh, String aEngagementName) {
		return exists(sh, By.xpath(ENGAGEMENT_MENU_XPATH_TEMPLATE.replace(DELIVERY_ENGAGEMENT_NAME_TOKEN, aEngagementName)), 5);
	}
	
	private static void CreateNewEngagement(SeleniumHelper sh, boolean defaultElementsOnly, DeliveryEngagement deliveryEngagementDetails){
//		if(!engagementExists(sh, deliveryEngagementDetails.name))
		forceCreateEngagement(sh, deliveryEngagementDetails);
			
		
		// only finish the wizard if we aren't creating additional elements (i.e. not just
		// relying on element defaulting from the product group)
		if(!defaultElementsOnly)
		{
			// add the elements
			createDeliveryElementsInWizard(sh, deliveryEngagementDetails);			
		}
		finishWizard(sh, deliveryEngagementDetails);
	}
	
	static void forceCreateEngagement(SeleniumHelper sh, DeliveryEngagement deliveryEngagementDetails) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.selectBurgerMenuOption(By.cssSelector(PROPOSAL_MENU_DIV), ADD_A_NEW_DELIVERY_ENGAGEMENT, By.cssSelector(SELECT_ID$_DELIVERY_GROUP_PRODUCT_GROUP_SELECT));
		
		// popup takes sometime to render so wait for the first selector field to appear (as these take the most time to render)
//		sh.waitForElementToBeClickableWithRetry(By.cssSelector(SELECT_ID$_DELIVERY_GROUP_PRODUCT_GROUP_SELECT), 20);
		
		clearAndInputText(sh, deliveryGroupShortNameField, deliveryEngagementDetails.name);
		
		sh.selectByVisibleText(By.cssSelector(SELECT_ID$_DELIVERY_GROUP_PRODUCT_GROUP_SELECT), deliveryEngagementDetails.productGroup);
			
		sh.getWebElement(By.cssSelector(WIZARD_NEXT_BTN)).click();
		
		sh.waitForElementToBeClickable(By.cssSelector(EXPECTED_START_DATE_INPUT));
		sh.setDatePickerField(By.cssSelector(EXPECTED_START_DATE_INPUT), deliveryEngagementDetails.expectedStartDate);
		sh.setDatePickerField(By.cssSelector(EXPECTED_END_DATE_INPUT), deliveryEngagementDetails.expectedEndDate);	
		
		if(deliveryEngagementDetails.contractRevenue != null) sh.getVisibleWebElement(By.cssSelector(INPUT_ID$_CONTRACT_REVENUE)).sendKeys(deliveryEngagementDetails.contractRevenue);
		
		sh.waitForElementToBeClickable(By.cssSelector(WIZARD_NEXT_BTN));
		sh.getWebElement(By.cssSelector(WIZARD_NEXT_BTN)).click();
	}

	public static void finishWizard(SeleniumHelper sh, DeliveryEngagement deliveryEngagementDetails) {
		sh.clickLink(By.cssSelector(WIZARD_FINISH), 40);
		
		// wait for the page to reload and the delivery group card to be on the page
		String deliveryGroupMenuXPath = ENGAGEMENT_MENU_XPATH_TEMPLATE.replace(DELIVERY_ENGAGEMENT_NAME_TOKEN, deliveryEngagementDetails.name);
		sh.waitForElementToBeClickable(By.xpath(deliveryGroupMenuXPath), 120);
	}
	
	private static void createDeliveryElementsInWizard(SeleniumHelper sh, DeliveryEngagement deliveryEngagementDetails) {
		for (DeliveryElement deliveryElementDetails : deliveryEngagementDetails.deliveryElements) {
			DeliveryElementPageC deliveryElementHandler = new DeliveryElementPageC(sh);
			deliveryElementHandler.CreateNewInWizard(deliveryEngagementDetails, deliveryElementDetails);

			// in 1.19 we seem to get the same problem we have with assignments where a new item
			// replaces an existing if added too quick - add a sleep if we are about to add another item
			if(deliveryEngagementDetails.deliveryElements.iterator().hasNext())
			{
				sh.sleep(1000);
			}
		}
	}
	
	public static void LoadExistingByName(SeleniumHelper sh, String deliveryEngagementName){
		// note that we can't use the lefthand recent items list for delivery engagements
		// as we can't be sure we don't end up in the proposition itself
		
		// navigate to the main list then try a click in the list of recent items
		if(sh.isLightning()){
			GeneralOperationsZ.navigateFromAnywhereToTab(sh, "Delivery Engagements");
			GeneralOperationsZ.pickListViewItemZ("navi to item: " + deliveryEngagementName, sh, deliveryEngagementName);
		}
		else
			NavigateToList(sh);
		
		try {
			sh.OpenExisting(deliveryEngagementName);
		} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		} catch (Exception e) {

			// not in recent items list so continue onto the list itself
			click(sh, goBtn);
			try {
				sh.OpenExisting(deliveryEngagementName);
			} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
				// if the browser has died (can happen with glitches in chromedriver)
				// then we should exit the test as retry will never succeed
				throw ubEx;
			} catch (Exception e1) {
				// one last try: attempt to directly navigate to the href of the anchor with the link text
				String deliveryEngagementLinkXPath = LINK_HREF_TEMPLATE.replace(LINK_HREF_SPAN, deliveryEngagementName);
					
				sh.waitForElementToBeClickable(By.xpath(deliveryEngagementLinkXPath));
				sh.getWebElement(By.xpath(deliveryEngagementLinkXPath)).click();
			}
		}
		
		sh.waitForMenuItemToBeClickable(PagesC.DELIVERYGROUPDASHBOARD);
	}
	
	public static void LoadExistingByName(SeleniumHelper sh, String salesOppName, String engagementName){
		if(sh.isLightning()){
			GeneralOperationsZ.navigateFromAnywhereToTab(sh, "Sales Opportunities");
			GeneralOperationsZ.pickListViewItemZ("navi to item: " + engagementName, sh, salesOppName);
			sh.switchToClassicIframeContext();
		}
		else{
			sh.goToAllTabsPage();
			sh.NavigateToList("Sales Opportunities");
			sh.clickLink(By.linkText(salesOppName));
		}
		sh.clickMenuItem(PagesC.PROPOSALITEMS);
		navigateToEngagement(sh, engagementName);
	}
	
	//created for standalone proposals
	public static void LoadExistingByNameViaProposal(SeleniumHelper sh, String proposalName, String engagementName){
		if(sh.isLightning()){
			GeneralOperationsZ.navigateFromAnywhereToTab(sh, "Proposals");
			GeneralOperationsZ.pickListViewItemZ("navi to item: " + engagementName, sh, proposalName);
			sh.switchToClassicIframeContext();
		}
		else{
			sh.goToAllTabsPage();
			sh.NavigateToList("Proposals");
			sh.clickLink(By.linkText(proposalName));
		}
		sh.clickMenuItem(PagesC.PROPOSALITEMS);
		navigateToEngagement(sh, engagementName);
	}
	
	public static void navigateToEngagement(SeleniumHelper sh, String engagementName) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.switchToClassicIframeContext();
		sh.waitForElementToBeClickable(By.xpath(SalesOpportunityPageC.engagementLink.replace("{{engagement}}", engagementName)),20);
		sh.clickLink(By.xpath(SalesOpportunityPageC.engagementLink.replace("{{engagement}}", engagementName)));
		sh.switchToClassicIframeContext();
		sh.waitForMenuItemToBeClickable(PagesC.DELIVERYGROUPDASHBOARD);
	}
	
	public static void navigateToAnnuities(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.DELIVERYGROUPANNUITIES);
		sh.waitForLightningSpinnerToBeHidden();
		sh.waitForElementToBeClickable(By.cssSelector(DeliveryEngagementPageC.INPUT_ID_ADD_ANNUITY));
	}
	
	public static void NavigateToList(SeleniumHelper sh) {
		sh.NavigateToList(DELIVERY_ENGAGEMENTS);
	}
	
	public static void ActivateDeliveryElement(SeleniumHelper sh, String deliveryElementName) {
		sh.switchToClassicIframeContext();
		navigateToRevenueAndCosts(sh);
		
		By elementCostLink = By.xpath(deliveryElementContractCostLink.replace("{{element}}", deliveryElementName));
		sh.switchToClassicIframeContext();
		sh.waitForElementToBeClickableWithRefreshRetry(elementCostLink, 20);
		String id = sh.getWebElement(elementCostLink).getAttribute("href").split("filterid=")[1].split("&Id=")[0];
		
		String activateToggleLinkXPath = ACTIVATE_SWITCH_XPATH_TEMPLATE.replace(DELIVERY_ELEMENT_NAME_TOKEN, deliveryElementName);
		String deactivateToggleLinkXPath = DEACTIVATE_SWITCH_XPATH_TEMPLATE.replace(DELIVERY_ELEMENT_NAME_TOKEN, deliveryElementName);
		
		By toggleLink = By.xpath(activateToggleLinkXPath);
		By saveActivate = By.xpath(saveActivateButton.replace("{{id}}", id));
		By deactivateToggleLink = By.xpath(deactivateToggleLinkXPath);
		
		sh.clickAndWaitSequenceWithRefreshRetry(25, toggleLink, saveActivate, deactivateToggleLink);
	}
	
	public static void EnableWorkingAtRisk(SeleniumHelper sh) {
		
		navigateToRevenueAndCosts(sh);
		
		sh.selectBurgerMenuOption(By.cssSelector(DIV_CLASS_DELIVERY_GROUP_MENU), WORK_AT_RISK, By.xpath(WAR_CANCEL_BTN));
				
//		sh.waitForElementToBeClickable(By.cssSelector(UPDATE_WAR_STATUS_BTN));
		
		sh.getElementBasedOnLabel(WAR_DELIVERY_STATUS).click();
		
		sh.getWebElement(By.cssSelector(UPDATE_WAR_STATUS_BTN)).click();
		
		sh.waitForElementToBeHidden(By.cssSelector(DIV_ID_DELIVERY_GROUP_WAR_POPUP));
	}
	
	public static void AllocateNewPurchaseOrder(SeleniumHelper sh, String deliveryEngagementName, PurchaseOrder purchaseOrderDetails) {
		LoadExistingByName(sh, deliveryEngagementName);
		
		NavigateToBudgetsAndPOs(sh);
		sh.switchToClassicIframeContext();
		
		sh.waitForElementToBeClickableWithRetry(By.cssSelector(SPAN_ONCLICK_DETAIL_MODE), 20);
		
		// ensure we are in detail mode
		click(sh, budgetsAndPODetailModeSwitch);
		
		if(sh.isLightning()){
			sh.clickLink(By.cssSelector(DELIVERY_GROUP_PO_MENU));
			sh.waitForElementToBeClickable(By.linkText(ALLOCATE_PURCHASE_ORDER_LINK), 30);
			sh.clickLink(By.linkText(ALLOCATE_PURCHASE_ORDER_LINK));
			sh.waitForLightningSpinnerToBeHidden();
			sh.waitForElementToBeClickable(By.cssSelector(WIZARD_NEXT_BTN), 30);
//			sh.clickLink(By.cssSelector(WIZARD_NEXT_BTN));
		}
		else
			sh.selectBurgerMenuOption(By.cssSelector(DELIVERY_GROUP_PO_MENU), ALLOCATE_PURCHASE_ORDER_LINK, By.cssSelector(WIZARD_NEXT_BTN));

		// if a delivery element is specified it's because we need to apply to a specific element
		// (should only be used where we have more than one element)
		// find the checkbox for the element and check it
		if(purchaseOrderDetails.deliveryElement != null)
		{
			String elementCheckboxXpath = ELEMENT_PO_CHECKBOX.replace(DELIVERY_ELEMENT_NAME_TOKEN, purchaseOrderDetails.deliveryElement);
			sh.waitForElementToBeClickable(By.xpath(elementCheckboxXpath));
			sh.getWebElement(By.xpath(elementCheckboxXpath)).click();
			
			sh.waitForElementToBeClickable(By.cssSelector(WIZARD_NEXT_BTN));
			sh.getWebElement(By.cssSelector(WIZARD_NEXT_BTN)).click();
			sh.sleep(4000);
		}

		// back in the normal flow of the wizard
		sh.waitForElementToBeClickable(By.id(NEW_PO));
		sh.getElementBasedOnLabel(CREATE_PURCHASE_ORDER).click();
		
		sh.sleep(1000);
		sh.getWebElement(By.cssSelector(WIZARD_NEXT_BTN)).click();
		
		sh.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_NEW_PO_REFERENCE), 20);
		
		clearAndInputText(sh, newPOReferenceField, purchaseOrderDetails.reference);
		clearAndInputText(sh, newPOOrderValueField, purchaseOrderDetails.orderValue);
		clearAndInputText(sh, newPOOrderDateField, purchaseOrderDetails.orderDate);
		clearAndInputText(sh, newPOExpiryDateField, purchaseOrderDetails.expiryDate);
		
		click(sh, By.cssSelector(WIZARD_NEXT_BTN));
		sh.waitForElementToBeClickableWithRetry(By.cssSelector(WIZARD_FINISH), 20);
		
		clearAndInputText(sh, allocationCapField, purchaseOrderDetails.orderValue);
		
		click(sh, POFinishButton);
		
		// wait to be back on the main PO screen
		sh.waitForLightningSpinnerToBeHidden();
		sh.waitForElementToBeClickableWithRetry(By.cssSelector(SPAN_ONCLICK_DETAIL_MODE), 20);
	}

	public static void NavigateToBudgetsAndPOs(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.DELIVERYGROUPBUDGETS);
		sh.waitForLightningSpinnerToBeHidden();
	}

	public static void NavigateToRisks(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.RISKSDELIVERY);
		sh.waitForLightningSpinnerToBeHidden();
	}

	public static void NavigateToIssues(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.ISSUES);
		sh.waitForLightningSpinnerToBeHidden();
	}
	
	public static void NavigateToEngagementStatus(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.DELIVERYGROUPSTATUSES);
		sh.waitForLightningSpinnerToBeHidden();
	}

	public static void NavigateToRevenueAdjustments(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.DELIVERYELEMENTREVENUEADJUSTMENTS);
		sh.waitForLightningSpinnerToBeHidden();
	}
	
	public static void NavigateToRevenueAdjustments(SeleniumHelper sh, String elementId) {
		sh.clickMenuItem(PagesC.DELIVERYELEMENTREVENUEADJUSTMENTS);
		sh.waitForLightningSpinnerToBeHidden();
		sh.hoverOverLinkByPageName(PagesC.DELIVERYELEMENTREVENUEADJUSTMENTS);
		sh.clickLink(By.xpath(revenueAdjustmentsMenuItem.replace("{{id}}", elementId)), 5);
	}
	
	public static void NavigateToSupplierRequisitions(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.DELIVERYELEMENTSUPPLIERREQUISITIONS);
		sh.waitForLightningSpinnerToBeHidden();
	}
	
	// works only from the engagement dashboard
	public static void NavigateToSupplierRequisitions(SeleniumHelper sh, String elementId) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.DELIVERYELEMENTSUPPLIERREQUISITIONS);
		sh.waitForLightningSpinnerToBeHidden();
		General.scrollToCopyrightSpan(sh);
		
		executeSequenceWithRetry(sh, 3, () -> {
			sh.waitForMenuItemToBeClickable(PagesC.DELIVERYELEMENTSUPPLIERREQUISITIONS);
			sh.hoverOverLinkByPageNameClickandHold(PagesC.DELIVERYELEMENTSUPPLIERREQUISITIONS, 10);
		   	WebElement travel = sh.getWebElement(By.xpath(supplierRequisitionsMenuItemById.replace("{{id}}", elementId)));
			sh.waitForElementToBeVisible(travel);
			sh.waitForElementToBeClickable(By.xpath(supplierRequisitionsMenuItemById.replace("{{id}}", elementId)));;
			
			travel.click();
		});

	}
	
	// works only from the engagement dashboard
	public static String getElementId(SeleniumHelper sh, String element) {
		By selector = By.xpath(dashboardElementSpan.replace("{{element}}", element));
		sh.waitForLightningSpinnerToBeHidden();
		sh.waitForElementToBeClickable(selector);
		return sh.getWebElement(selector).getAttribute("id");
	}

	public static void updateHighLevelForecast(SeleniumHelper sh, DeliveryEngagement deliveryEngagementDetails) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.selectBurgerMenuOption(By.cssSelector(DIV_CLASS_DELIVERY_GROUP_MENU), EDIT_ENGAGEMENT_HIGH_LEVEL_FORECAST, By.cssSelector(INPUT_ID$_SAVE_GROUP));
		
		if(deliveryEngagementDetails.expectedStartDate != null)
		{
			sh.waitForElementToBeClickableWithRetry(By.cssSelector(HL_EXPECTED_START_DATE_INPUT), 5);
			sh.setDatePickerField(By.cssSelector(HL_EXPECTED_START_DATE_INPUT), deliveryEngagementDetails.expectedStartDate);
		}
		
		if(deliveryEngagementDetails.expectedEndDate != null)
		{
			sh.waitForElementToBeClickableWithRetry(By.cssSelector(HL_EXPECTED_END_DATE_INPUT), 5);
			sh.setDatePickerField(By.cssSelector(HL_EXPECTED_END_DATE_INPUT), deliveryEngagementDetails.expectedEndDate);
		}
		
		if(deliveryEngagementDetails.contractRevenue != null)
		{
			sh.waitForElementToBeClickableWithRetry(By.cssSelector(HL_INPUT_ID$_CONTRACT_REVENUE), 20);		
			sh.getVisibleWebElement(By.cssSelector(HL_INPUT_ID$_CONTRACT_REVENUE)).sendKeys(deliveryEngagementDetails.contractRevenue);
		}
		
		sh.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_SAVE_GROUP));
		sh.getWebElement(By.cssSelector(INPUT_ID$_SAVE_GROUP)).click();
		
		// the screen takes a while to close after clicking save so wait for it to have closed
		sh.waitForElementToBeHidden(By.id(DELIVERY_GROUP_FORECAST_POPUP));
	}
	
	public static void navigateToRevenueAndCosts(SeleniumHelper sh){
		sh.clickMenuItem(PagesC.DELIVERYGROUPREVENUESANDCOSTS);
		sh.waitForLightningSpinnerToBeHidden();
	}
	
	public static void navigateToExpenseCategories(SeleniumHelper sh){
		sh.clickMenuItem(PagesC.ACTIVITYEXPENSECATEGORIES);
		sh.waitForLightningSpinnerToBeHidden();
	}

	public static void NavigateToAssignments(SeleniumHelper sh, boolean defaultForecastingFromDate) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.ACTIVITYASSIGNMENTSDELIVERY, false);
		
		// current screen implementation only shows assignments which end within the last four months
		// override this behaviour to ensure that the screen shows all assignments known to be created in the automation scenarios
		if(defaultForecastingFromDate)
		{
			ActivityAssignmentPageC activityAssignmentHandler = new ActivityAssignmentPageC(sh);
			activityAssignmentHandler.DefaultForecastingFromDate();
		}
		try{
			sh.waitForElementToBeClickable(By.xpath("//a[@title='Add Assignment']"));
		}catch(Exception e){
			sh.switchToClassicIframeContext();
			sh.waitForElementToBeClickable(By.xpath("//a[@title='Add Assignment']"));
		}
	}
	
	public static void NavigateToManyAssignments(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.ACTIVITYASSIGNMENTSMANY);
		
	}


	public static void NavigateToAnnuities(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.DELIVERYGROUPANNUITIES);
		sh.waitForLightningSpinnerToBeHidden();
		sh.waitForElementToBeClickable(By.cssSelector(INPUT_ID_ADD_ANNUITY));
	}
	
	public static void NavigateToDashboard(SeleniumHelper sh) {
		// only navigate to the dashboard if we aren't already there
		if(!sh.getCurrentUrl().contains(PagesC.DELIVERYGROUPDASHBOARD)){
			sh.clickMenuItem(PagesC.DELIVERYGROUPDASHBOARD);
			sh.sleep(2000);
			sh.waitForLightningSpinnerToBeHidden();
			sh.waitForElementToBeClickable(By.id(DASHBOARD_CONTAINER));
		}
	}
	
	public static void NavigateToDeliveryGroupMilestones(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.DELIVERYGROUPMILESTONES);
		sh.waitForLightningSpinnerToBeHidden();
	}
	
	public static void NavigateToRevenueAndCostDetails(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.DELIVERYGROUPREVENUESANDCOSTS);
		sh.waitForLightningSpinnerToBeHidden();
	}
	
	public static boolean validateDeliveryElementsDeleted(SeleniumHelper sh, DeliveryEngagement deliveryEngagementDetails) {
		LoadExistingByName(sh, deliveryEngagementDetails.name);
		navigateToRevenueAndCosts(sh);
		sh.waitForElementToBeClickableWithRetry(By.id(BTN_NEW_DELIVERY_ELEMENT), 5);
		return !sh.checkLinkExists(By.linkText("Edit"));
	}
	
	public static void ConfirmWonProductsAndServices(SeleniumHelper sh, DeliveryEngagement deliveryEngagementDetails) {		
		// when there is more than one delivery engagement there is more than one New Delivery Element button
		// find the burger on the card of the relevant Delivery Engagement
		String newDeliveryGroupMenuXPath = ENGAGEMENT_MENU_XPATH_TEMPLATE.replace(DELIVERY_ENGAGEMENT_NAME_TOKEN, deliveryEngagementDetails.name);

		// we no longer win won engagements separately, instead we win the proposal
		if(deliveryEngagementDetails.deliveryStatus.equals(LOST))
		{
			sh.selectBurgerMenuOption(By.xpath(newDeliveryGroupMenuXPath), LOST, By.cssSelector(INPUT_ID$_SAVE_DELIVERY_GROUP_LOST_BTN));
//			sh.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_SAVE_DELIVERY_GROUP_LOST_BTN));
			sh.selectByVisibleText(deliveryGroupLostReasonSelect, deliveryEngagementDetails.lossReason);
			clearAndInputText(sh, deliveryGroupLostNarrativeField, deliveryEngagementDetails.lossNarrative);
			click(sh, saveDeliveryGroupLostBtn);
			sh.waitForElementToBeHidden(By.id(DELIVERY_GROUP_POPUP_LOST));
		}
	}

	public static void LaunchBulkEdit(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.ACTIVITYASSIGNMENTSBULKEDIT);	
		sh.waitForLightningSpinnerToBeHidden();
	}
	
	public static boolean ValidateFact(SeleniumHelper sh, String testStage, Fact factDetail, ExpectedValue expectedValueDetail) throws Exception {
		String factName = factDetail.factName;
		String ignoreReason = factDetail.ignoreReason;
		String measure = expectedValueDetail.measure;
		String expectedValue = expectedValueDetail.value;
		String actualValue = "";
		String result = PASSED;
		boolean returnValue = true;
		String rowIndex = "-1";
		String columnIndex = "-1";
		
		// the values in the tracking plan dashboard are in a table - the structure of the table is always the same
		// 			BASELINE	EXPENDED	REMAINING	PROJECTED	VARIANCE
		//	EFFORT
		//	REVENUE
		//	COST
		//
		// viewing this as a matrix, get the values that we want to check as specific web elements using xpath
				
		if (ignoreReason != null && ignoreReason != "")
		{
			sh.logIgnoreFact(testStage, factName, ignoreReason, measure);
		}
		else
		{
			if (factName.equals(EFFORT))
			{
				rowIndex = "1";
			}
			else if (factName.equals(REVENUE))
			{
				rowIndex = "2";
			}
			else if (factName.equals(COST))
			{
				rowIndex = "3";
			}
			else
			{
				sh.invalidExpectedResultsConfiguration(factName, measure);
			}
			
			if(rowIndex != "-1")
			{
				if (measure.equals(BASELINE)) 
				{
					columnIndex = "3";
				} 
				else if (measure.equals(EXPENDED)) 
				{
					columnIndex = "4";
				} 
				else if (measure.equals(REMAINING)) 
				{
					columnIndex = "5";
				} 
				else if (measure.equals(TOTAL)) 
				{
					columnIndex = "6";
				} 
				else if (measure.equals(VARIANCE)) 
				{
					columnIndex = "7";
				}
			}

			String factXPath = PLAN_FACT_XPATH.replace(ROW_INDEX_TOKEN, rowIndex).replace(COLUMN_INDEX_TOKEN, columnIndex);
			WebElement factElement = sh.getWebElement(By.xpath(factXPath));
			
			actualValue = factElement.getText();
			returnValue = actualValue.equals(expectedValue);
			
			if (!returnValue) {
				result = "FAILED";
			}

			String expectedValueForLog = expectedValue;
			String actualValueForLog = actualValue;
				
			sh.logValidationResult(testStage, factName, measure, expectedValueForLog, actualValueForLog, result);
		}
		return returnValue;
	}

	public static void createDeliveryEngagementChangeElementsAndAssignments(SeleniumHelper sh, ChangeControl changeControlDetails) {
		for(DeliveryEngagement engagementDetails : changeControlDetails.deliveryEngagements)
		{
			LoadExistingByName(sh, engagementDetails.name);
			navigateToRevenueAndCosts(sh);
			for(DeliveryElement elementDetails : engagementDetails.deliveryElements)
			{
				DeliveryElementPageC elementHandler = new DeliveryElementPageC(sh);
				elementHandler.createChangeElement(engagementDetails.name, elementDetails);
			}
		}		
	}

	static final By approvalRuleTimesheet = By.xpath("//a[text()='ActualTimesheet']/../../..//a[text()='Edit']");
	static final By approvalRuleExpenses = By.xpath("//a[text()='ActualExpenseClaim']/../../..//a[text()='Edit']");
	static final String approverTypeSelect = "ApprovalRuleUser";

	static final By approverTypeList = By.xpath("//table[@class='detailList']/tbody/tr[3]/td/select");
	static final By approverTimesheetInput = By.xpath("//label[normalize-space(text())='Approver 1 User']/parent::th/parent::tr//input[@type='text']");
	static final By approverExpensesInput = By.xpath("//label[normalize-space(text())='Approver 1 User']/parent::th/parent::tr//input[@type='text']");
	static final By saveBtn = By.xpath("//input[@value='Save']");
	
	static final By usageUnitsDropdown = By.xpath("//select[.='HourDay']");
	static final By saveResourcedActivityBtn = By.cssSelector("[id$=saveResourcedActivityBtn]");
	
	static final By periodRateBandBurgerMenu = By.xpath("//div[@class='fa fa-bars burger-menu']");

	static final By forecastUsageEntryFormat = By.xpath("//label[normalize-space(text())='Forecast Usage Entry Format (Day)']/parent::th/following-sibling::td//input");
	static final By actualUsageEntryFormat = By.xpath("//label[normalize-space(text())='Actual Usage Entry Format (Day)']/parent::th/following-sibling::td//input");

	static final By saveActivityTimePatternBtn = By.cssSelector("[id$=saveActivityTimePatternBtn]");
	
	public static void UpdateApprovalRules(SeleniumHelper sh, DeliveryEngagement engagement) {		

		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForElementToBeClickableWithRetry(approvalRuleTimesheet, 20);
			click(sh, approvalRuleTimesheet);
			dropdownSelect(sh, approverTypeList, approverTypeSelect);
			sh.waitForElementToBeClickable(approverTimesheetInput, 10);
			clearAndInputText(sh, approverTimesheetInput, "Employee One, UK Co 1 x'x x&x");
			click(sh, saveBtn);
			sh.waitForBoxyToBeHidden();
		});

		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForElementToBeClickableWithRetry(approvalRuleExpenses, 20);
			click(sh, approvalRuleExpenses);
			dropdownSelect(sh, approverTypeList, approverTypeSelect);
			sh.waitForElementToBeClickable(approverExpensesInput, 10);
			clearAndInputText(sh, approverExpensesInput, "Employee One, UK Co 1 x'x x&x");
			click(sh, saveBtn);
			sh.waitForBoxyToBeHidden();
		});
	}
	
	public static void UpdateActivityUsageUnits(SeleniumHelper sh, DeliveryEngagement engagement, String unit) {		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForElementToBeClickableWithRetry(By.xpath(editActivityDetailsBurgerMenu), 20);
			click(sh, By.xpath(editActivityDetailsBurgerMenu));
			click(sh, By.linkText("Edit Resourced Activity"));
			dropdownSelect(sh, usageUnitsDropdown, unit);
			waitClickable(sh, saveResourcedActivityBtn, 5);
			click(sh, saveResourcedActivityBtn);
			sh.waitForBoxyToBeHidden();
		});
		
		UpdateActivityTimePattern(sh);
	}
	
	static final By standardDayPeriodRateBand = By.linkText("Business Day - Standard Time");
	static final By savePeriodRateBandBtn = By.cssSelector("[id$=saveActivityRateBandBtn]");
	
	public static void openAndSaveRateBand(SeleniumHelper sh, DeliveryEngagement engagement) {		
		sh.waitForElementToBeClickableWithRetry(standardDayPeriodRateBand, 20);
		click(sh, standardDayPeriodRateBand);
		waitClickable(sh, savePeriodRateBandBtn, 5);
		click(sh, savePeriodRateBandBtn);
		sh.waitForBoxyToBeHidden();
	}
	
	public static void UpdateActivityTimePattern(SeleniumHelper sh) {		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForElementToBeClickableWithRetry(periodRateBandBurgerMenu, 20);
			click(sh, periodRateBandBurgerMenu);
			click(sh, By.linkText("Edit"));
			waitClickable(sh, forecastUsageEntryFormat, 15);
			clearAndInputText(sh, forecastUsageEntryFormat, "0.5");
			clearAndInputText(sh, actualUsageEntryFormat, "0.5");
	
			waitClickable(sh, saveActivityTimePatternBtn, 15);
			click(sh, saveActivityTimePatternBtn);
			sh.waitForBoxyToBeHidden();
		});
	}
	
	static final By 
	scopeBurgerMenu = By.xpath("//div[@class='fa fa-bars delivery-group-menu burger-menu']"),
	dashboardElementBurgerMenu = By.xpath("//div[@class='fa fa-bars delivery-element-menu burger-menu']"),
	timelineDateCard = By.xpath("//div[@class='event-date-card-header']"),
	milestoneLink = By.linkText("MS01"),
	annuityPencil = By.xpath("//i[@class='fa fa-pencil']"),
	assignmentNameLink = By.linkText("Generic Resource BU1 GR1"),
	activityDetailsInnerBurgerMenu = By.xpath("//div[@class='fa fa-bars burger-menu']"),
	trackingPlanPin = By.xpath("//span[@title='Manually Scheduled ']"),
	invoiceNewButton = By.xpath("//input[@value='New']"),
	hmm = By.xpath("");
	
	public static void TestEngagementPageLoadingLDV(SeleniumHelper sh){
		
		String[][] avgLoadTime = new String[5][8];
		avgLoadTime = InitialiseArray(sh, avgLoadTime);

		for(int n = 0; n < 3; n++){
			long dashboardLoadTime = LoadKimblePage(sh, PagesC.DELIVERYGROUPDASHBOARD, dashboardElementBurgerMenu);
			avgLoadTime[n+1][1] = String.valueOf(dashboardLoadTime + "s");

			long scopeLoadTime = LoadKimblePage(sh, PagesC.DELIVERYGROUPREVENUESANDCOSTS, scopeBurgerMenu);
			avgLoadTime[n+1][2] = String.valueOf(scopeLoadTime + "s");

			long timelineLoadTime = LoadKimblePage(sh, PagesC.DELIVERYGROUPTIMELINE, timelineDateCard);
			avgLoadTime[n+1][3] = String.valueOf(timelineLoadTime + "s");

			long invoiceLoadTime = LoadKimblePage(sh, PagesC.DELIVERYGROUPINVOICES, invoiceNewButton);
			avgLoadTime[n+1][4] = String.valueOf(invoiceLoadTime + "s");

			long milestoneLoadTime = LoadKimblePage(sh, PagesC.DELIVERYGROUPMILESTONES, milestoneLink);
			avgLoadTime[n+1][5] = String.valueOf(milestoneLoadTime + "s");

			long annuityLoadTime = LoadKimblePage(sh, PagesC.DELIVERYGROUPANNUITIES, annuityPencil);
			avgLoadTime[n+1][6] = String.valueOf(annuityLoadTime + "s");

			long planLoadTime = LoadKimblePage(sh, PagesC.TRACKINGPLANGANTT, trackingPlanPin);
			avgLoadTime[n+1][7] = String.valueOf(planLoadTime + "s");

//			long assignmentsLoad = returnPageLoadTime(sh, PagesC.ACTIVITYASSIGNMENTSDELIVERY, assignmentNameLink);
//			avgLoadTime[n+1][8] = String.valueOf(assignmentsLoad + "s");
//
//			long activitesLoad = returnPageLoadTime(sh, PagesC.DELIVERYGROUPACTIVITIES, activityDetailsInnerBurgerMenu);
//			avgLoadTime[n+1][9] = String.valueOf(activitesLoad + "s");

		}
		for (int k = 1; k < avgLoadTime[0].length; k++){
			int valToAdd1 = Integer.parseInt(avgLoadTime[1][k].replace("s", ""));	
			int valToAdd2 = Integer.parseInt(avgLoadTime[2][k].replace("s", ""));	
			int valToAdd3 = Integer.parseInt(avgLoadTime[3][k].replace("s", ""));	
			int avgValue = (valToAdd1 + valToAdd2 + valToAdd3)/3;
			avgLoadTime[4][k] = String.valueOf(avgValue) + "s";
		}
		PrintArray(sh, avgLoadTime);
	}
	
	public static void PrintArray(SeleniumHelper sh, String[][] a){
		System.out.println("------------------------------------------------------------------------");
		for (int i = 0; i < a.length; i++) {
		    for (int j = 0; j < a[i].length; j++) {
		    	String spaces = " ";
		    	
		    	if(a[i][j].length() < 5){
			    	int spacesNeeded = 1 + a[0][j].length() - a[i][j].length();
			    	spaces = new String(new char[spacesNeeded]).replace("\0", " ");
		    	}
		        System.out.print(spaces + a[i][j]);
		    }
		    System.out.println();
		}
		System.out.println("------------------------------------------------------------------------");
	}
	
	public static long LoadKimblePage(SeleniumHelper sh, String page, By loadIndicator){
		
		long startTime = System.currentTimeMillis();
		sh.clickMenuItem(page);
		try{
			sh.waitForElementToBeClickable(loadIndicator, 120);
		} 
		catch (Exception e){
			System.out.println("An Error has occurred. Failed to load Page: "+ page);
			return 999;
		}
		long duration = (System.currentTimeMillis() - startTime)/1000;
		
		return duration;
	}
	
	public static String[][] InitialiseArray(SeleniumHelper sh, String[][] a){
		
		for(int i = 1; i < a[0].length; i++){
			// populate the average row with 0
			a[4][i] = "0";
		}
			
		a[0][1] = "Dashboard";
		a[0][2] = "Scope";
		a[0][3] = "Timeline";
		a[0][4] = "Invoice";
		a[0][5] = "Milestone";
		a[0][6] = "Annuity";
		a[0][7] = "Plan Gantt";
	
		a[0][0] = "Page    |";
		a[1][0] = "Test 1  |";
		a[2][0] = "Test 2  |";
		a[3][0] = "Test 3  |";
		a[4][0] = "Average |";
		return a;
	}
}
