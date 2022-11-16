package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kimble.automation.domain.AssignmentLineItem;
import kimble.automation.domain.DeliveryDateBulkEdit;
import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.ExpenseLineItem;
import kimble.automation.domain.Milestone;
import kimble.automation.domain.Proposal;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.kimbleobjects.lightning.GeneralOperationsZ;
import kimble.automation.scenarios.StagesZ;
import org.openqa.selenium.By;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

public class SalesOpportunityPageC {
	
	public static final String 
	SWITCH_TO_LOW_LEVEL = "switchToLowLevel",
	FORECASTIN_AT_LOW_LEVEL = "//div[normalize-space(@title)='Forecasting at the Detailed Level']",
	HERE = "here",
	PROPOSAL_ITEMS = "ProposalItems",
	SELECT_ID$_PROPOSAL_BUSINESS_UNIT = "select[id$='proposalBusinessUnit']",
	DIV_ID$_MAIN_SALES_OPP_HOME_PB = "div[id$='mainSalesOppHomePB']",
	INPUT_ID$_SALES_OPP_CLOSE_DATE_FIELD = "input[id$='salesOppCloseDateField']",
	SELECT_ID$_PROPOSAL_PROPOSITION_SELECT = "select[id$='proposalProposition']",
	SELECT_ID$_OPPORTUNITY_SOURCE_SELECT = "select[id$='opportunitySource']",
	SELECT_ID$_SALES_OPPORTUNITY_FORECAST_STATUS_SELECT = "select[id$='opportunityForecastStatus']",
	SALES_OPP_STAGE_CLOSE_BTN = "input[value$='Next']",
	MOVE_TO_NEXT_STAGE = "Move To Next Stage",
	A_NAME$_EDIT_REV_RESOURCES = "a[name$='editRevResources']",
	SALES_OPP_LIST_NEW_SALES_OPP_BTN = "input[value='New Sales Opportunity']",
	ADD_ENGAGEMENT_TO_PROPOSAL_CSS = "div[class*='fa-bars'][id$='proposal-menu']",
	TOGGLE_FORECAST_LEVEL_SWITCH_CSS = "div[class*='toggle-forecast-level']",
	
	assignmentTable = "//div[@id='editProposalElementPopup']//table[substring(@id, string-length(@id) - 22)='assignment-config-table']",
	expensesTable = "//div[@id='editProposalElementPopup']//table[substring(@id, string-length(@id) - 20)='expenses-config-table']",
	
	//Templated xpaths for the proposal items page main burger menu and items
	titleCard = "//div[normalize-space(@class)='title-card']",
	titleCardMenu = titleCard + "//div[normalize-space(@class)='title-card-menu fa fa-bars burger-menu']",
	titleCardMenuPopup = titleCard + "//ul[normalize-space(@class)='jq-dropdown-menu']",
	titleCardMenuItem = titleCardMenuPopup + "/li/a[normalize-space(text())='{{item}}']",
	
	addExistingEngagementSpan = "//table//span[normalize-space(text())=\"{{engagement}}\"]",
	addExistingEngagementRow = addExistingEngagementSpan + "/ancestor::tr[1]",
	existingEngagementSelectRadio = addExistingEngagementRow + "/td[1]/input",
	addExistingEngagementNextButton = "//div[normalize-space(@class)='pbBottomButtons']//input[normalize-space(@value)='Next']",
	addExistingEngagementFinnisButton = "//div[normalize-space(@class)='pbBottomButtons']//input[normalize-space(@value)='Finish']",
	
	addElementInWizardButton = "//span[normalize-space(@onclick)='addNewElementToGroup()']",
	wizardNewDeliveryElementPopup = "//div[normalize-space(@id)='NewDeliveryElementPopup']",
	wizardNewElementProductSelect = wizardNewDeliveryElementPopup + "//select[contains(@id, 'newDeliveryElementProductSelect')]",
	wizardNewElementNameInput = wizardNewDeliveryElementPopup + "//input[contains(@id, 'newDeliveryElementName')]",
	wizardNewElementOkButton = wizardNewDeliveryElementPopup + "//input[normalize-space(@value)='OK']",
	
	// These are 'templated' xpaths with placeholders to insert the engagement and/or element names
	engagementLink = "//a[normalize-space(text())=\"{{engagement}}\"]",
	engagementCard = engagementLink + "/ancestor::div[contains(@class, 'delivery-group card')]",
	engagementMenu = engagementCard + "//div[@class='fa fa-bars delivery-group-menu burger-menu tip-right']",
	addElementMenuItem = engagementCard + "//a[text()='Add A New Element']",
	editEngagementMenuItem = engagementCard + "//a[text()='Edit this Engagement']",
	engagementEditPopup = "div#DeliveryGroupEditPopup",
	engagementStartDate = engagementCard + "//span[@class='group-dates']/span[1]",
	engagementEndDate = engagementCard + "//span[@class='group-dates']/span[2]",
	addOptionalElementLink = engagementCard + "//div[contains(@class, 'delivery-element-menu')][contains(@class, 'burger-menu')]",

	elementTitle = engagementLink + "/ancestor::div[@class='delivery-group card']//div[@class='inner-card-title'][normalize-space(text())=\"{{element}}\"]",
	elementBurgerMenu = elementTitle + "/following-sibling::div[contains(@class, 'delivery-element-menu')]",
	elementInnerHeader = elementTitle + "/ancestor::div[@class='inner-card-header']",
	elementInnerCard = elementInnerHeader + "/ancestor::div[contains(@class, 'inner-card')][1]",
	elementConfigLink = elementInnerHeader + "//a[text()='Configure Element']",
	elementServicesRevenue = elementInnerCard + "//span[contains(text(),'Services Contract Revenue')]/following-sibling::span[@class='card-field-value']/a/span",
	elementServicesCost = elementInnerCard + "//span[contains(text(),'Services Contract Cost')]/following-sibling::span[@class='card-field-value']//span[@data-currency]",
	elementServicesCostOverridden = elementInnerCard + "//span[contains(text(),'Services Contract Cost')]/following-sibling::span[@class='card-field-value']/a/span",
	elementExpensesRevenue = elementInnerCard + "//span[contains(text(),'Expenses Contract Revenue')]/following-sibling::span[@class='card-field-value']/span",
	elementExpensesCost = elementInnerCard + "//span[contains(text(),'Expenses Contract Cost')]/following-sibling::span[@class='card-field-value']/span",
	
	// These are 'templated' xpaths with placeholders to insert the resource name
	assignmentRowByNo = assignmentTable + "/tbody/tr[{{rowNo}}]",
	assignmentResourceInput = assignmentRowByNo + "//input[@class='assignment-resource kimbleSearch']",
	assignmentResourceLink = "//input[@value=\"{{resource}}\"]",
	assignmentRow = assignmentResourceLink + "/ancestor::tr[1]",
	assignmentPercentage = assignmentRow + "//input[substring(@id, string-length(@id) - 20)='assignment-percentage']",
	assignmentUsage = assignmentRow + "//input[substring(@id, string-length(@id) - 15)='assignment-usage']",
	assignmentStartDate = assignmentRow + "//input[substring(@id, string-length(@id) - 20)='assignment-start-date']",
	assignmentEndDate = assignmentRow + "//input[substring(@id, string-length(@id) - 18)='assignment-end-date']",
	assignmentEndDateText = assignmentRow + "//span[substring(@id, string-length(@id) - 23)='assignment-end-date-text']",
	assignmentUtilisation = assignmentRow + "//span[substring(@id, string-length(@id) - 26)='assignment-utilisation-text']",
	assignmentUtilisationInput = assignmentRow + "//input[substring(@id, string-length(@id) - 21)='assignment-utilisation']",
	assignmentCost = assignmentRow + "//input[normalize-space(@class)='cost-rate-input']",
	assignmentRevenue = assignmentRow + "//input[normalize-space(@class) = 'revenue-rate-input']",//"//input[substring(@id, string-length(@id) - 22)='assignment-revenue-rate']",
	
	expenseRowByNo = expensesTable + "/tbody/tr[{{rowNo}}]",
	expenseCategory = expenseRowByNo + "//select[substring(@id, string-length(@id) - 18)='expense-category-id']",
	expenseBusinessUnit = expenseRowByNo + "//select[substring(@id, string-length(@id) - 15)='business-unit-id']",
	expenseDescription = expenseRowByNo + "//input[substring(@id, string-length(@id) - 11)='expense-name']",
	expenseNetAmount = expenseRowByNo + "//input[substring(@id, string-length(@id) - 17)='expense-net-amount']",
	expenseUnits = expenseRowByNo + "//input[substring(@id, string-length(@id) - 12)='expense-units']",
	expenseRemainingCost = expenseRowByNo + "//input[substring(@id, string-length(@id) - 21)='expense-remaining-cost']",
	expenseRemainingRevenue = expenseRowByNo + "//input[substring(@id, string-length(@id) - 24)='expense-remaining-revenue']",

	ratesTable = "//table[@id='element-rates-table']",
	
	milestonesTable = "table[id$='milestone-config-table']",
	milestonesTableRow = milestonesTable + " tbody tr:nth-child({{rowNo}})",
	milestonesNameInput = milestonesTableRow + " td:nth-child(3) input", // revise this - needs a proper identifier
	milestonesDateInput = milestonesTableRow + " td:nth-child(4) input", // revise this - needs a proper identifier 
	milestonesValueInput = milestonesTableRow + " td:nth-child(5) input", // revise this - needs a proper identifier
	
	LIGHTNINGHOME = "https://gs0.lightning.force.com/lightning/page/home";

	static final By 
	applyButton = By.xpath("//input[@value='Apply']"),
	
	newButton = By.name("new"),
	saveButton = By.id("save"),
	saveSalesOppWonButton = By.cssSelector("input[id$='saveSalesOppWon']"),
	proposalBurgerMenu = By.cssSelector(ADD_ENGAGEMENT_TO_PROPOSAL_CSS),
	
	accountNameInput = By.cssSelector("input[id$='accountName']"),
	opportunityNameInput = By.cssSelector("input[id$='opportunityName']"),
	opportunitySourceDropdown = By.cssSelector(SELECT_ID$_OPPORTUNITY_SOURCE_SELECT),
	responseRequiredInput = By.cssSelector("input[id$='opportunityResponseRequiredDate']"),
	closeDateInput = By.cssSelector("input[id$='opportunityCloseDate'], input[id$='salesOppCloseDateField'], input[id$='closeDate']"),
	closeDateNarrative = By.cssSelector("[id$=SalesOpportunityDetailPanel]"),
	forecastStatusDropdown = By.cssSelector(SELECT_ID$_SALES_OPPORTUNITY_FORECAST_STATUS_SELECT),
	businessUnitDropdown = By.cssSelector(SELECT_ID$_PROPOSAL_BUSINESS_UNIT),
	businessUnitInput = By.xpath("//input[@data-typename='ActiveOperatingBusinessUnit']"),
	propositionDropdown = By.cssSelector(SELECT_ID$_PROPOSAL_PROPOSITION_SELECT),
	startDateWizardLink = By.linkText(HERE),
	moveToNextStageButton = By.cssSelector(SALES_OPP_STAGE_CLOSE_BTN),
//	closeDateInput = By.cssSelector(INPUT_ID$_SALES_OPP_CLOSE_DATE_FIELD),
	contractRevenueField = By.cssSelector("span[id$='proposal-contract-revenue']"),
	editRevenueForecastLink = By.cssSelector(A_NAME$_EDIT_REV_RESOURCES),
	goButton = By.cssSelector("input[name='go']"),
	
	salesOppHomePageBlock = By.cssSelector(DIV_ID$_MAIN_SALES_OPP_HOME_PB),
	
	addAssignment = By.xpath(assignmentTable + "//a[text()='Add Assignment']"),
	proposalContractRevenue = By.xpath("//label[normalize-space(text())='Contract Revenue']/parent::div/span/span"),
	proposalContractCost = By.xpath("//label[normalize-space(text())='Contract Cost']/parent::div/span/span"),
	proposalContractMargin = By.xpath("//label[normalize-space(text())='Margin Pct']/parent::div/span/span"),
	editDeliveryElementPopup = By.cssSelector("div#editProposalElementPopup"),
	elementAddingMenu = By.cssSelector("div.burger-menu.adding"),
	
	newDeliveryElementPopup = By.cssSelector("div#DeliveryElementPopup"),
	newElementProductSelect = By.cssSelector("select[id$='productInput']"),
	newElementNameInput = By.cssSelector("input[id$='elementName']"),
	newElementSave = By.cssSelector("input.saveDeliveryElement"),
	
	engagementNameInput = By.cssSelector(engagementEditPopup + " input[id$='deliveryGroupShortName']"),
	engagementReferenceInput = By.cssSelector(engagementEditPopup + " input[id$='deliveryGroupReference']"),
	engagementSaveButton = By.cssSelector(engagementEditPopup + " input[id$='saveGroup']"),
	
	// Edit Delivery Element links and inputs
	elementEditAssignmentsTab = By.cssSelector("li.assignments-tab"),
	elementEditExpensesTab = By.cssSelector("li.expenses-tab"),
	elementEditMilestonesTab = By.cssSelector("li.milestones-tab"),
	targetMarginButton = By.xpath("//span[@class='linkText'][text()='Target Margin']"),
	expectedUsegeButton = By.xpath("//span[@class='linkText'][text()='Expected Usage']"),
	usageItemButton = By.xpath("//span[@class='linkText'][text()='Usage Item']"),
	addMilestone = By.xpath("//a[text()='Add Milestone']"),
	addExpense = By.xpath("//a[text()='Add Expense']"),
	
	elementEditExpectedServicesRevenue = By.cssSelector("input[id$='element-expected-revenue']"),
	elementEditServicesRevenue = By.cssSelector("input[id$='element-services-revenue']"),
	elementEditServicesCost = By.cssSelector("input[id$='element-services-cost']"),
	elementEditServicesCostOverridden = By.cssSelector("input[id$='element-services-cost-overriden']"),
	
	elementEditExpensesRevenue = By.cssSelector("input[id$='element-expenses-revenue']"),
	elementEditExpensesCost = By.cssSelector("input[id$='element-expenses-cost']"),
	
	elementEditProductExtension = By.xpath("//input[@data-typename='ProductExtension']"),
	elementEditTargetMargin = By.cssSelector("input[id$='element-target-margin']"),
	elementEditExpectedUsage = By.cssSelector("input[id$='element-expected-usage']"),
	
	elementEditSaveButton = By.cssSelector("input#saveButton"),
	elementEditDeleteButton = By.cssSelector("input#deleteButton"),
	elementEditCancelButton = By.cssSelector("input#CancelButton"),
	ratesTableItemName = By.xpath(ratesTable + "/tbody/tr/td[1]"), // revise this - needs a proper identifier
	ratesTableOverride = By.xpath(ratesTable + "//input[@type='checkbox']"), // revise this - needs a proper identifier
	ratesTableRateInput = By.xpath(ratesTable + "//div[@class='override-rates']/input"), // revise this - needs a proper identifier
	ratesTableRateDefault = By.xpath(ratesTable + "//div[@class='standard-rates']/input"), // revise this - needs a proper identifier
	ratesTableSourceValue = By.xpath(ratesTable + "//input[@class='kimble-number source-value']"); // revise this - needs a proper identifier

	
	public static void startNewSalesOppCreation(SeleniumHelper sh) {
		sh.closeLightningPopUp();
		executeSequenceWithRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20,
			/* click new					*/	newButton,
			/* wait for account name input	*/	accountNameInput
			);
		});
	}
	
	public static void inputValuesAndSave(SeleniumHelper sh, String accountName, SalesOpportunity salesOpp) {
		
		sh.switchToClassicIframeContext();
		try{
			waitClickable(sh, accountNameInput, 15);
		} catch(Exception e){
			sh.switchToClassicIframeContext();
		}
		if(!(salesOpp.name.contains("LDV"))){
			executeSequenceWithRetry(sh, 3, () -> {
				clearAndInputText(sh, accountNameInput, accountName);
				clearAndInputText(sh, opportunityNameInput, salesOpp.name);
				dropdownSelect(sh, forecastStatusDropdown, salesOpp.forecastStatus);
				dropdownSelect(sh, businessUnitDropdown, salesOpp.businessUnit);
				dropdownSelect(sh, opportunitySourceDropdown, salesOpp.source);
				dropdownSelect(sh, propositionDropdown, salesOpp.proposition);
				clearAndInputText(sh, responseRequiredInput, salesOpp.responseRequiredDate);
				clearAndInputText(sh, closeDateInput, salesOpp.closeDate);
				saveNewOpportunity(sh);
			});
		}
		else{
			executeSequenceWithRetry(sh, 3, () -> {
				clearAndInputText(sh, accountNameInput, accountName);
				clearAndInputText(sh, opportunityNameInput, salesOpp.name);
				dropdownSelect(sh, forecastStatusDropdown, salesOpp.forecastStatus);
				clearAndInputText(sh, businessUnitInput, salesOpp.businessUnit);
				dropdownSelect(sh, opportunitySourceDropdown, salesOpp.source);
				dropdownSelect(sh, propositionDropdown, salesOpp.proposition);
				clearAndInputText(sh, responseRequiredInput, salesOpp.responseRequiredDate);
				clearAndInputText(sh, closeDateInput, salesOpp.closeDate);
			});
			saveNewOpportunityLDV(sh);
		}
	}
	
	static void saveNewOpportunity(SeleniumHelper sh) {
		if(sh.isLightning()) {
			click(sh, saveButton);
			sh.waitForPageLoadComplete(15);
			sh.switchToClassicIframeContext(); // new method needed here as it appears it's needed to re-switch to classic iframe after using any lightning components or moving to a new page
			try{
				waitClickable(sh, proposalBurgerMenu, 20);
			} catch(Exception e){
				sh.switchToClassicIframeContext();
			}
		}
		else {
			clickAndWaitSequence(sh, 90,
			/* click save			*/	saveButton,
			/* wait for edit button	*/	proposalBurgerMenu
			);
		}
	}
	
	static void saveNewOpportunityLDV(SeleniumHelper sh) {
		long startTime = System.currentTimeMillis();	
		
		click(sh, saveButton);
		sh.waitForPageLoadComplete(30);
		
		executeSequenceWithRetry(sh, 3, () -> {
		//Max Time 30 mins
			try{
				sh.sleep(80000);
				sh.refreshBrowser();
				waitClickable(sh, proposalBurgerMenu, 20);
			}	catch(Exception e){
					try{
						sh.sleep(80000);
						sh.refreshBrowser();
						waitClickable(sh, proposalBurgerMenu, 20);
					}	catch(Exception e2){
							try{
								sh.sleep(80000);
								sh.refreshBrowser();
								waitClickable(sh, proposalBurgerMenu, 20);
							}	catch(Exception e3){
									try{
										sh.sleep(80000);
										sh.refreshBrowser();
										waitClickable(sh, proposalBurgerMenu, 20);
									}	catch(Exception e4){
										try{
											sh.sleep(80000);
											sh.refreshBrowser();
											waitClickable(sh, proposalBurgerMenu, 20);
										}	catch(Exception e5){
												sh.sleep(80000);
												sh.refreshBrowser();
												waitClickable(sh, proposalBurgerMenu, 20);											
											}										
										}
								}
						}
				}
		});
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime)/1000;
		System.out.println("------------------------------------------------------------------------");
		System.out.println(" Sales Opp Creation: "+duration +"s");
	}
	
	static void saveMaintainedOpportunity(SeleniumHelper sh) {
		if(sh.isLightning()){
			sh.waitForLightningSpinnerToBeHidden();
			sh.clickLink(saveButton);
			sh.waitForLightningSpinnerToBeHidden();
			sh.waitForElementToBeClickable(salesOppHomePageBlock);
		}
		else{
			clickAndWaitSequence(sh, 40,
			/* click save							*/	saveButton,
			/* wait for sales opp home page block	*/	salesOppHomePageBlock
			);
		}
	}
	
	public static void startAddingElementsToExistingEngagement(SeleniumHelper sh, String engagementName) {
		sh.clickAndWaitSequenceWithRefreshRetry(5, 
		/* click the title card burger menu 					*/ By.xpath(titleCardMenu),  
		/* click the 'Add an Existing Delivery Engagement' item */ By.xpath(titleCardMenuItem.replace("{{item}}", "Add an Existing Delivery Engagement")), 
		/* click the radio button to select the engagement 		*/ By.xpath(existingEngagementSelectRadio.replace("{{engagement}}", engagementName)), 
		/* click next to start adding elements 					*/ By.xpath(addExistingEngagementNextButton), 
		/* wait for the button for adding elements to appear	*/ By.xpath(addElementInWizardButton)
		);
	}
	
	public static void addElementInWizard(SeleniumHelper sh, DeliveryElement element) {
		executeSequenceWithRetry(sh, 3, () -> {
			By addElementButtonSelector = By.xpath(addElementInWizardButton);
			By productSelectSelector = By.xpath(wizardNewElementProductSelect);
			By elementNameInputSelector = By.xpath(wizardNewElementNameInput);
			By okButtonSelector = By.xpath(wizardNewElementOkButton);
			
			clickAndWaitSequence(sh, 5, 
			/* click to add an element					*/ addElementButtonSelector,  
			/* wait for the product select to be usable	*/ productSelectSelector
			);
			
			// input the values
			dropdownSelect(sh, productSelectSelector, element.product);
			clearAndInputText(sh, elementNameInputSelector, element.name);
	
			clickAndWaitSequence(sh, 5, 
			/* click the ok button							*/ okButtonSelector,  
			/* wait for the add element button to be usable	*/ addElementButtonSelector
			);
		});
	}
	
	public static void finishAddingElementsToExistingEngagement(SeleniumHelper sh, String engagementName) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			if(exists(sh, By.xpath(addExistingEngagementFinnisButton), 5))
				/* click the finish button					*/ click(sh, By.xpath(addExistingEngagementFinnisButton));
			/* wait for the engagement card to appear	*/ waitClickable(sh, By.xpath(engagementCard.replace("{{engagement}}", engagementName)), 40);
		});
	}

	public static void updateCloseDateAndSave(SeleniumHelper sh, String closeDate) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, closeDateInput, 20);
			clearAndInputText(sh, closeDateInput, closeDate);
			saveMaintainedOpportunity(sh);
		});
	}

	public static void updateBusinessUnitAndSave(SeleniumHelper sh, String businessUnit) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, closeDateInput, 20);
			dropdownSelect(sh, businessUnitDropdown, businessUnit);
			saveMaintainedOpportunity(sh);
		});
	}

	public static void updateForecastStatusAndSave(SeleniumHelper sh, String forecastStatus) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, forecastStatusDropdown, 30);
			dropdownSelect(sh, forecastStatusDropdown, forecastStatus);
			saveMaintainedOpportunity(sh);
		});
	}

	public static void winOpportunity(SeleniumHelper sh, String winDate){
		StringBuilder sb = new StringBuilder();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.switchToClassicIframeContext();
			sh.clickMenuItem(PagesC.SALESOPPWON);
			sh.switchToClassicIframeContext();
			try{
				waitClickable(sh, By.cssSelector(INPUT_ID$_SALES_OPP_CLOSE_DATE_FIELD), 20);
			}
			catch(Exception e){
				sh.switchToClassicIframeContext();
				waitClickable(sh, By.cssSelector(INPUT_ID$_SALES_OPP_CLOSE_DATE_FIELD), 20);
			}
			if(sh.isLightning()){
				// the calendar doesn't align correctly in lightning so this needs to be separate
				if(!(winDate==null)){
					sh.waitForLightningSpinnerToBeHidden();
					sh.clearField(By.cssSelector(INPUT_ID$_SALES_OPP_CLOSE_DATE_FIELD));
					sh.waitForLightningSpinnerToBeHidden();
					inputText(sh, By.cssSelector(INPUT_ID$_SALES_OPP_CLOSE_DATE_FIELD), winDate);
				}	
			}
			else{
				clearAndInputText(sh, closeDateInput, winDate);				
			}
			click(sh, saveSalesOppWonButton);
			if(exists(sh, saveSalesOppWonButton, 5))
				waitHidden(sh, By.id(sh.getWebElement(saveSalesOppWonButton).getAttribute("Id")), 20);
//				sh.waitForElementToBeHidden(By.id(sh.getWebElement(saveSalesOppWonButton).getAttribute("Id")));
		});
	}
	
	public static void navigateToMoveSalesOpportunityToNextStage(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			sh.clickMenuItemWithRetry(PagesC.SALESOPPSTAGECLOSE);
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, moveToNextStageButton, 20);
		});
	}
	
	public static void moveSalesOpportunityToNextStage(SeleniumHelper sh, String expectedStage) {
		By expectedElement = By.xpath("//*[normalize-space(text())='" + expectedStage + "']");
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			click(sh, moveToNextStageButton);
			sh.waitForLightningSpinnerToBeHidden();
			try{
				waitClickable(sh, expectedElement, 20);
			}catch(Exception e){
				sh.waitForLightningSpinnerToBeHidden();
				waitClickable(sh, By.linkText(expectedStage), 20);
			}
		});
	}

	public static void NavigateToSalesOppForecastFromHome(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			if(sh.isLightning()){
				String id = sh.getCurrentUrl().replace("/view", "").split("SalesOpportunity__c/")[1];
				sh.goToUrl("https://kimbleone.gus.visual.force.com/apex/SalesOppForecast?id=" + id);
			}
			else{
				// if we are on the proposal items screen (which can happen if the previous step opened the sales opp from a method
				// other than the sales opp list, then switch to the sales opp screen
				if(sh.getCurrentUrl().contains(PROPOSAL_ITEMS)) {
					NavigateToMaintainOpportunityHome(sh);
					sh.goToUrl(sh.getCurrentUrl().replaceAll(PagesC.SALESOPPHOME, PagesC.SALESOPPFORECAST));
				}
				
				// use the URL for an open sales opp, substituting "Forecast" for "Home"
				if(sh.getCurrentUrl().contains(PagesC.SALESOPPHOME)) {
					sh.goToUrl(sh.getCurrentUrl().replaceAll(PagesC.SALESOPPHOME, PagesC.SALESOPPFORECAST));
				}
				
				sh.waitForPageLoadComplete(20);
			}

		});
	}
	
	public static void NavigateToSalesOppHomeFromForecast(SeleniumHelper sh, String salesOppName) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			if(sh.isLightning()){
				GeneralOperationsZ.navigateFromAnywhereToTab(sh, "Sales Opportunities");
				GeneralOperationsZ.pickListViewItemZ("", sh, salesOppName);
			}
			else{
				if(sh.getCurrentUrl().contains(PagesC.SALESOPPFORECAST)) {
					sh.goToUrl(sh.getCurrentUrl().replaceAll(PagesC.SALESOPPFORECAST, PagesC.SALESOPPHOME));
				}
			}
		});
	}

	public static void NavigateToAssignBidTeam(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.BIDTEAMASSIGNMENTS);
	}
	
	public static void NavigateToMaintainOpportunitySummary(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.SALESOPPEDIT);
	}
	
	public static void NavigateToMaintainOpportunityHome(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.SALESOPPHOME);
	}
	
	public static void NavigateToViewSalesOpportunity(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.PROPOSALITEMS);
	}
	
	public static void NavigateToConfirmWonProductsAndServices(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.PROPOSALITEMS);
	}
	
	public static void NavigateToRisksSales(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.RISKSSALES);
	}
	
	public static void NavigateToRisksDelivery(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.RISKSDELIVERY);
	}
	
	public static void NavigateTopProposalRiskDashboard(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.PROPOSALRISKDASHBOARD);
	}
	
	public static void NavigateTopDeliveryGroupRiskDashboard(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.DELIVERYGROUPRISKDASHBOARD);
	}
	
	public static void NavigateLoseSalesOpp(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			sh.clickMenuItem(PagesC.SALESOPPLOST);
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, closeDateInput, 20);
		});
	}
	
	public static void loseSalesOpportunity(SeleniumHelper sh, SalesOpportunity salesOpportunityDetails) {
		SalesOppLostPageC.LoseOpportunity(sh, salesOpportunityDetails);
	}
	
	public static boolean validateContractRevenue(SeleniumHelper sh, String targetContractRevenue) {
		sh.waitForLightningSpinnerToBeHidden();
		return targetContractRevenue.equals(sh.getWebElementTextOrNull(contractRevenueField));
	}

	public static void SwitchToDetailedForecastLevel(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickAndWaitSequenceWithRefreshRetry(60, 
				By.cssSelector(TOGGLE_FORECAST_LEVEL_SWITCH_CSS), 
				By.cssSelector(TOGGLE_FORECAST_LEVEL_SWITCH_CSS), 
				By.id(SWITCH_TO_LOW_LEVEL),
				By.xpath(FORECASTIN_AT_LOW_LEVEL));
	}
	
	public static void addElement(SeleniumHelper sh, String engagementName, DeliveryElement element) {
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20, 
			/* click engagement burger menu					*/	By.xpath(engagementMenu.replace("{{engagement}}", engagementName)),
			/* click add element							*/	By.xpath(addElementMenuItem.replace("{{engagement}}", engagementName)),
			/* wait for save button to enable				*/	newElementProductSelect
			);
			/* select product								*/	dropdownSelect(sh, newElementProductSelect, element.product);
			/* input element name							*/	clearAndInputText(sh, newElementNameInput, element.name);
			/* wait for save button to re-enable			*/	waitClickable(sh, newElementSave, 20);
			/* click save									*/	click(sh, newElementSave);
			/* wait for boxy to disappear					*/	sh.waitForBoxyToBeHidden();
		});
	}
	
	public static void deleteElement(SeleniumHelper sh, String engagementName, DeliveryElement element) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			openElementEditor(sh, engagementName, element);
			sh.clickLink(elementEditDeleteButton);
			sh.waitForBoxyToBeHidden();
		});
	}
	
	/** expects to be in the ProposalItems page */
	public static void addAllOptionalElements(SeleniumHelper sh, String engagementName) {
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			for(WebElement we : sh.getWebElements(By.xpath(addOptionalElementLink.replace("{{engagement}}", engagementName)))) {
				WebDriverWait wait = new WebDriverWait(sh.getWD(), 20);
				wait.until(ExpectedConditions.visibilityOf(we));
				we.click();
			}
			sh.waitForElementToBeHidden(elementAddingMenu);
		});
	}
	
	
	/** expects to be in the ProposalItems page */
	public static void validateProposalItemsSummaryFields(SeleniumHelper sh, SalesOpportunity salesOpp, String stage) {
		sh.refreshBrowser();
		sh.waitForLightningSpinnerToBeHidden();
		sh.waitForPageLoadComplete(20);
		sh.waitForElementToBeClickable(proposalContractRevenue, 120);
		sh.waitForElementToBeClickable(proposalContractCost, 120);
		

		if(salesOpp.contractRevenue != null) {
			Double contractRevenue = sh.getWebElementNumberOrNullWithRefreshRetry(proposalContractRevenue);
			sh.assertEquals(contractRevenue, salesOpp.contractRevenue, "The sales opps: " + salesOpp.name + " contract revenue summary field is incorrect");
		}
		if(salesOpp.contractCost != null) {
			Double contractCost = sh.getWebElementNumberOrNullWithRefreshRetry(proposalContractCost);
			sh.assertEquals(contractCost, salesOpp.contractCost, "The sales opps: " + salesOpp.name + " contract cost summary field is incorrect");
		}
		if(salesOpp.contractMargin != null) {
			Double contractMargin = sh.getWebElementNumberOrNullWithRefreshRetry(proposalContractMargin);
			sh.assertEquals(contractMargin, salesOpp.contractMargin, "The sales opps: " + salesOpp.name + " contract margin summary field is incorrect");
		}
		
		validateEngagementSummaryFields(sh, salesOpp.deliveryEngagements, stage);
	}
	
	/** expects to be in the ProposalItems page */
	public static void validateEngagementSummaryFields(SeleniumHelper sh, Collection<DeliveryEngagement> engagements, String stage) {
		for(DeliveryEngagement engagement : engagements)
			validateEngagementSummaryFields(sh, engagement, stage);
	}
	
	/** expects to be in the ProposalItems page */
	public static void validateEngagementSummaryFields(SeleniumHelper sh, DeliveryEngagement engagement, String stage) {
		if(!engagement.stage.equals(stage))
			return;

		try {
			sh.waitForElementToBeClickable(By.xpath(engagementLink.replace("{{engagement}}", engagement.name)));
		} catch(Exception e) {
			sh.assertTrue(false, "A delivery engagement by the name: " + engagement.name + " doesn't appear in the OCS screen");
		}
		
		By startDateSel = By.xpath(engagementStartDate.replace("{{engagement}}", engagement.name));
		By endDateSel = By.xpath(engagementEndDate.replace("{{engagement}}", engagement.name));
		
		if(engagement.startDate != null) {
			String startDate = sh.getWebElementTextOrNullWithRetry(startDateSel);
			sh.assertEquals(startDate, engagement.startDate, "The engagements: " + engagement.name + " start date summary field is incorrect");
		}
		if(engagement.endDate != null) {
			String endDate = sh.getWebElementTextOrNullWithRetry(endDateSel);
			sh.assertEquals(endDate, engagement.endDate, "The engagements: " + engagement.name + " end date summary field is incorrect");
		}
			
		for(DeliveryElement el : engagement.deliveryElements)
			validateElementSummaryFields(sh, engagement.name, el, stage);
	}
	
	/** expects to be in the ProposalItems page */
	public static void validateElementSummaryFields(SeleniumHelper sh, String engagementName, DeliveryElement element, String stage) {
		try {
			sh.waitForElementToBeClickable(By.xpath(elementTitle.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
		} catch(Exception e) {
			sh.assertTrue(false, "A delivery element by the name: " + element.name + " doesn't appear in the OCS screen as a child of an engagement by the name: " + engagementName);
		}
		
		if(element.servicesRevenue != null) {
			Double servicesRevenue = sh.getWebElementNumberOrNullWithRetry(By.xpath(elementServicesRevenue.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
			sh.assertEquals(servicesRevenue, element.servicesRevenue, "The elements: " + element.name + " services revenue summary field is incorrect");
		}
		if(element.servicesCost != null) {
			Double servicesCost = sh.getWebElementNumberOrNullWithRetry(By.xpath(elementServicesCost.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
			if(servicesCost == null)
				servicesCost = sh.getWebElementNumberOrNullWithRetry(By.xpath(elementServicesCostOverridden.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
			sh.assertEquals(servicesCost, element.servicesCost, "The elements: " + element.name + " services cost summary field is incorrect");
		}
		if(element.expensesRevenue != null) {
			Double expensesRevenue = sh.getWebElementNumberOrNullWithRetry(By.xpath(elementExpensesRevenue.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
			sh.assertEquals(expensesRevenue, element.expensesRevenue, "The elements: " + element.name + " expenses revenue summary field is incorrect");
		}
		if(element.expensesCost != null) {
			Double expensesCost = sh.getWebElementNumberOrNullWithRetry(By.xpath(elementExpensesCost.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
			sh.assertEquals(expensesCost, element.expensesCost, "The elements: " + element.name + " expenses cost summary field is incorrect");
		}
	}
	
	/** expects to be in the ProposalItems page */
	public static void openElementEditor(SeleniumHelper sh, String engagementName, DeliveryElement element) {
		By burgerSelector = By.xpath(elementBurgerMenu.replace("{{engagement}}", engagementName).replace("{{element}}", element.name));
		By configSelector = By.xpath(elementConfigLink.replace("{{engagement}}", engagementName).replace("{{element}}", element.name));
		sh.clickAndWaitSequenceWithRefreshRetry(20, burgerSelector, configSelector, elementEditSaveButton);
		sh.moveBoxyToTheLeft();
	}
	
	/** expects to be in the ProposalItems page with the edit dialog open*/
	public static void configureElementDynamic(SeleniumHelper sh, String engagementName, DeliveryElement element) {
		sh.waitForLightningSpinnerToBeHidden();
		StringBuilder sb = new StringBuilder("");
		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			openElementEditor(sh, engagementName, element);
			
			if(element.forecastMode != null)
				click(sh, elementEditAssignmentsTab);
			if("target".equals(element.forecastMode))
				clickIfExists(sh, targetMarginButton);
			else if("expected".equals(element.forecastMode))
				clickIfExists(sh, expectedUsegeButton);
			else if("usage".equals(element.forecastMode))
				clickIfExists(sh, usageItemButton);
			
			clearAndInputTextIfVisibleAndEnabled(sh, elementEditExpectedServicesRevenue, element.servicesRevenue);
			clearAndInputTextIfVisibleAndEnabled(sh, elementEditProductExtension, element.productExtension);
			clearAndInputTextIfVisibleAndEnabled(sh, elementEditTargetMargin, element.targetMargin);
			clearAndInputTextIfVisibleAndEnabled(sh, elementEditTargetMargin, element.targetMargin);		
			clearAndInputTextIfVisibleAndEnabled(sh, elementEditExpectedUsage, element.expectedUsage);
			
			if(element.createableAssignmentLineItems != null) {
				click(sh, elementEditAssignmentsTab);
				for(int i = 0; i < element.createableAssignmentLineItems.size(); i++) {
					AssignmentLineItem li = element.createableAssignmentLineItems.get(i);
					String rowNo = "" + (i + 1);
					
					String name = li.resourceName;
					click(sh, editDeliveryElementPopup);
					click(sh, addAssignment);
					click(sh, editDeliveryElementPopup);
					waitClickable(sh, By.xpath(assignmentResourceInput.replace("{{rowNo}}", rowNo)), 20);
					inputText(sh, By.xpath(assignmentResourceInput.replace("{{rowNo}}", rowNo)), name);
					click(sh, editDeliveryElementPopup);
					waitClickable(sh, By.xpath(assignmentRevenue.replace("{{resource}}", name)), 20);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentPercentage.replace("{{resource}}", name)), li.usagePercent);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentUsage.replace("{{resource}}", name)), li.usageDays);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentEndDate.replace("{{resource}}", name)), li.endDate);
					if(sh.isLightning()){sb.append(Keys.ESCAPE);}
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentUtilisationInput.replace("{{resource}}", name)), li.utilisation);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentCost.replace("{{resource}}", name)), li.cost);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentRevenue.replace("{{resource}}", name)), li.revenue);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentStartDate.replace("{{resource}}", name)), li.startDate);
					if(sh.isLightning()){sb.append(Keys.ESCAPE);}
					sh.sleep(2000, "Changing the start date refreshes all assignment lines in the ocs delivery element edit "
							+ "- This could likely be fixed by making only the effected assignment line refresh");
				}
			}
			
			if(element.configurableAssignmentLineItems != null) {
				sh.clickLink(elementEditAssignmentsTab);
				for(AssignmentLineItem li : element.configurableAssignmentLineItems) {
					String name = li.resourceName;
					waitClickable(sh, By.xpath(assignmentRevenue.replace("{{resource}}", name)), 20);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentPercentage.replace("{{resource}}", name)), li.usagePercent);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentUsage.replace("{{resource}}", name)), li.usageDays);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentEndDate.replace("{{resource}}", name)), li.endDate);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentCost.replace("{{resource}}", name)), li.cost);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentRevenue.replace("{{resource}}", name)), li.revenue);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(assignmentStartDate.replace("{{resource}}", name)), li.startDate);
					sh.sleep(2000, "Changing the start date refreshes all assignment lines in the ocs delivery element edit "
							+ "- This could likely be fixed by making only the effected assignment line refresh");
				}
			}
			
			if(element.expenseLineItems != null) {
				click(sh, elementEditExpensesTab);
				for(int i = 0; i < element.expenseLineItems.size(); i++) {
					ExpenseLineItem li = element.expenseLineItems.get(i);
					String rowNo = "" + (i + 1);
					
					clickAndWaitSequenceWithRetry(sh, 10, 3,
						addExpense,
						By.xpath(expenseRemainingRevenue.replace("{{rowNo}}", rowNo)));
					
					dropdownSelect(sh, By.xpath(expenseCategory.replace("{{rowNo}}", rowNo)), li.category);
					//required
					dropdownSelectWithRetry(sh, By.xpath(expenseBusinessUnit.replace("{{rowNo}}", rowNo)), li.businessUnit, 3);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(expenseDescription.replace("{{rowNo}}", rowNo)), li.description);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(expenseNetAmount.replace("{{rowNo}}", rowNo)), li.netAmount);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(expenseUnits.replace("{{rowNo}}", rowNo)), li.units);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(expenseRemainingCost.replace("{{rowNo}}", rowNo)), li.remainingCost);
					clearAndInputTextIfVisibleAndEnabled(sh, By.xpath(expenseRemainingRevenue.replace("{{rowNo}}", rowNo)), li.remainingRevenue);
				}
			}
			
			List<Milestone> milestones = new ArrayList();
			if(element.revenueMilestones != null) milestones.addAll(element.revenueMilestones);
			if(element.costMilestones != null) milestones.addAll(element.costMilestones);
			if(milestones.size() > 0) {
				click(sh, elementEditMilestonesTab);
				for(int i = 0; i < milestones.size(); i++) {
					Milestone li = milestones.get(i);
					String rowNo = "" + (i + 1);
					
					clickAndWaitSequenceWithRetry(sh, 10, 3, addMilestone, By.cssSelector(milestonesValueInput.replace("{{rowNo}}", rowNo)));

					clearAndInputTextIfVisibleAndEnabled(sh, By.cssSelector(milestonesNameInput.replace("{{rowNo}}", rowNo)), li.name);
					clearAndInputTextIfVisibleAndEnabled(sh, By.cssSelector(milestonesDateInput.replace("{{rowNo}}", rowNo)), li.date);
					clearAndInputTextIfVisibleAndEnabled(sh, By.cssSelector(milestonesValueInput.replace("{{rowNo}}", rowNo)), li.value);
				}
			}
			
			if(element.rateOverride != null) {
				checkboxSelectIfVisibleAndEnabled(sh, ratesTableOverride, true);
				clearAndInputTextIfVisibleAndEnabled(sh, ratesTableRateInput, element.rateOverride);
			}

			click(sh, elementEditSaveButton);
			sh.waitForBoxyToBeHidden();
		});
	}
	
	/** expects to be in the ProposalItems page */
	public static void validateElement(SeleniumHelper sh, String engagementName, DeliveryElement expected) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			openElementEditor(sh, engagementName, expected);
			
			validateElementWhileInEditDialog(sh, expected);
			
			sh.clickLink(elementEditCancelButton);
			sh.waitForBoxyToBeHidden();
		});
	}
	
	/** expects to be in the ProposalItems page with an edit dialog open*/
	public static void validateElementWhileInEditDialog(SeleniumHelper sh, DeliveryElement expected) {
		sh.clickLink(editDeliveryElementPopup);
		
		if(expected.targetMargin != null || expected.expectedUsage != null)
			sh.clickLink(elementEditAssignmentsTab);
		
		if(expected.servicesRevenue != null) {
			Double servicesRevenue = sh.getWebElementNumberOrNullWithRetry(elementEditExpectedServicesRevenue);
			if(servicesRevenue == null)
				servicesRevenue = sh.getWebElementNumberOrNullWithRetry(elementEditServicesRevenue);
			sh.assertEquals(servicesRevenue, expected.servicesRevenue, "The delivery element: " + expected.name + " services revenue is incorrect");
		}
		if(expected.servicesCost != null) {
			Double servicesCost = sh.getWebElementNumberOrNullWithRetry(elementEditServicesCost);
			sh.assertEquals(servicesCost, expected.servicesCost, "The delivery element: " + expected.name + " services cost is incorrect");
		}
		if(expected.servicesCostOverridden != null) {
			Double servicesCostOverridden = sh.getWebElementNumberOrNullWithRetry(elementEditServicesCostOverridden);
			sh.assertEquals(servicesCostOverridden, expected.servicesCostOverridden, "The delivery element: " + expected.name + " overridden services cost is incorrect");
		}
		if(expected.expensesRevenue != null) {
			Double expensesRevenue = sh.getWebElementNumberOrNullWithRetry(elementEditExpensesRevenue);
			sh.assertEquals(expensesRevenue, expected.expensesRevenue, "The delivery element: " + expected.name + " expenses revenue is incorrect");
		}
		if(expected.expensesCost != null) {
			Double expensesCost = sh.getWebElementNumberOrNullWithRetry(elementEditExpensesCost);
			sh.assertEquals(expensesCost, expected.expensesCost, "The delivery element: " + expected.name + " expenses cost is incorrect");
		}
		if(expected.targetMargin != null) {
			Double targetMargin = sh.getWebElementNumberOrNullWithRetry(elementEditTargetMargin);
			sh.assertEquals(targetMargin, expected.targetMargin, "The delivery element: " + expected.name + " target margin is incorrect");
		}
		if(expected.expectedUsage != null) {
			Double expectedUsage = sh.getWebElementNumberOrNull(elementEditExpectedUsage);
			sh.assertEquals(expectedUsage, expected.expectedUsage, "The delivery element: " + expected.name + " expected usage is incorrect");
		}

		if(expected.createableAssignmentLineItems != null)
			validateAssignmentLineItems(sh, expected.name, expected.createableAssignmentLineItems);
		
		if(expected.configurableAssignmentLineItems != null)
			validateAssignmentLineItems(sh, expected.name, expected.configurableAssignmentLineItems);
		
		if(expected.expenseLineItems != null) {
			sh.clickLink(elementEditExpensesTab);
			for(int i = 0; i < expected.expenseLineItems.size(); i++) {
				ExpenseLineItem li = expected.expenseLineItems.get(i);
				String rowNo = "" + (i + 1);
				
				
				if(li.category != null) {
					String category = sh.getWebElementTextOrNullWithRetry(By.xpath(expenseCategory.replace("{{rowNo}}", rowNo)));
					sh.assertEquals(category, li.category, "The delivery element: " + expected.name + " expense line item " + li.description + " category is incorrect");
				}
				//required
				String businessUnit = sh.getWebElementTextOrNullWithRetry(By.xpath(expenseBusinessUnit.replace("{{rowNo}}", rowNo)));
				sh.assertEquals(businessUnit, li.businessUnit, "The delivery element: " + expected.name + " expense line item " + li.description + " business unit is incorrect");
				
				if(li.description != null) {
					String description = sh.getWebElementTextOrNullWithRetry(By.xpath(expenseDescription.replace("{{rowNo}}", rowNo)));
					sh.assertEquals(description, li.description, "The delivery element: " + expected.name + " expense line item " + li.description + " description is incorrect");
				}
				if(li.netAmount != null) {
					Double netAmount = sh.getWebElementNumberOrNullWithRetry(By.xpath(expenseNetAmount.replace("{{rowNo}}", rowNo)));
					sh.assertEquals(netAmount, li.netAmount, "The delivery element: " + expected.name + " expense line item " + li.netAmount + " new amount is incorrect");
				}
				if(li.units != null) {
					Double units = sh.getWebElementNumberOrNullWithRetry(By.xpath(expenseUnits.replace("{{rowNo}}", rowNo)));
					sh.assertEquals(units, li.units, "The delivery element: " + expected.name + " expense line item " + li.units + " units is incorrect");
				}
				if(li.remainingCost != null) {
					Double remainingCost = sh.getWebElementNumberOrNullWithRetry(By.xpath(expenseRemainingCost.replace("{{rowNo}}", rowNo)));
					sh.assertEquals(remainingCost, li.remainingCost, "The delivery element: " + expected.name + " expense line item " + li.remainingCost + " remaining cost is incorrect");
				}
				if(li.remainingRevenue != null) {
					Double remainingRevenue = sh.getWebElementNumberOrNullWithRetry(By.xpath(expenseRemainingRevenue.replace("{{rowNo}}", rowNo)));
					sh.assertEquals(remainingRevenue, li.remainingRevenue, "The delivery element: " + expected.name + " expense line item " + li.remainingRevenue + " remaining revenue is incorrect");
				}
			}
		}
		
		List<Milestone> milestones = new ArrayList();
		if(expected.revenueMilestones != null) milestones.addAll(expected.revenueMilestones);
		if(expected.costMilestones != null) milestones.addAll(expected.costMilestones);
		if(milestones != null) {
			for(int i = 0; i < milestones.size(); i++) {
				Milestone li = milestones.get(i);
				String rowNo = "" + (i + 1);
				
				
				if(li.name != null) {
					String name = sh.getWebElementTextOrNullWithRetry(By.cssSelector(milestonesNameInput.replace("{{rowNo}}", rowNo)));
					sh.assertEquals(name, li.name, "The delivery element: " + expected.name + " milestone line item " + li.name + " name is incorrect");
				}
				if(li.date != null) {
					String date = sh.getWebElementTextOrNullWithRetry(By.cssSelector(milestonesDateInput.replace("{{rowNo}}", rowNo)));
					sh.assertEquals(date, li.date, "The delivery element: " + expected.name + " milestone line item " + li.name + " date is incorrect");
				}
				if(li.value != null) {
					Double value = sh.getWebElementNumberOrNullWithRetry(By.cssSelector(milestonesValueInput.replace("{{rowNo}}", rowNo)));
					sh.assertEquals(value, li.value, "The delivery element: " + expected.name + " milestone line item " + li.name + " value is incorrect");
				}
			}
		}
		
		if(expected.rateItemName != null) {
			String ratesItemName = sh.getWebElementTextOrNullWithRetry(ratesTableItemName);
			sh.assertEquals(ratesItemName, expected.rateItemName, "The delivery element: " + expected.name + " rate item name is incorrect");
		}
		if(expected.rateDefault != null) {
			Double ratesItemRateDefault = sh.getWebElementNumberOrNullWithRetry(ratesTableRateDefault);
			sh.assertEquals(ratesItemRateDefault, expected.rateDefault, "The delivery element: " + expected.name + " default rate is incorrect");
		}
		if(expected.rateSourceValue != null) {
			Double ratesItemSourceValue = sh.getWebElementNumberOrNullWithRetry(ratesTableSourceValue);
			sh.assertEquals(ratesItemSourceValue, expected.rateSourceValue, "The delivery element: " + expected.name + " rate source value is incorrect");
		}
	}
	
	public static void validateAssignmentLineItems(SeleniumHelper sh, String deliveryElementName, List<AssignmentLineItem> items) {
		sh.clickLink(elementEditAssignmentsTab);
		for(AssignmentLineItem li : items) {
			String name = li.resourceName;
			
			
			if(li.usagePercent != null) {
				Double usagePercent = sh.getWebElementNumberOrNullWithRetry(By.xpath(assignmentPercentage.replace("{{resource}}", name)));
				sh.assertEquals(usagePercent, li.usagePercent, "The delivery element: " + deliveryElementName + " line item " + name + " usage percent is incorrect");
			}
			if(li.usageDays != null) {
				Double usageDays = sh.getWebElementNumberOrNullWithRetry(By.xpath(assignmentUsage.replace("{{resource}}", name)));
				sh.assertEquals(usageDays, li.usageDays, "The delivery element: " + deliveryElementName + " line item " + name + " usage days is incorrect");
			}
			if(li.utilisation != null) {
				Double utilisation = sh.getWebElementNumberOrNullWithRetry(By.xpath(assignmentUtilisation.replace("{{resource}}", name)));
				if(utilisation == null)
					utilisation = sh.getWebElementNumberOrNullWithRetry(By.xpath(assignmentUtilisationInput.replace("{{resource}}", name)));
				sh.assertEquals(utilisation, li.utilisation, "The delivery element: " + deliveryElementName + " line item " + name + " utilisation is incorrect");
			}
			if(li.startDate != null) {
				String startDate = sh.getWebElementTextOrNullWithRetry(By.xpath(assignmentStartDate.replace("{{resource}}", name)));
				sh.assertEquals(startDate, li.startDate, "The delivery element: " + deliveryElementName + " line item " + name + " start date is incorrect");
			}
			if(li.endDate != null) {
				String endDate = sh.getWebElementTextOrNullWithRetry(By.xpath(assignmentEndDate.replace("{{resource}}", name)));
				if(endDate == null)
					endDate = sh.getWebElementTextOrNullWithRetry(By.xpath(assignmentEndDateText.replace("{{resource}}", name)));
				sh.assertEquals(endDate, li.endDate, "The delivery element: " + deliveryElementName + " line item " + name + " end date is incorrect");
			}
			if(li.cost != null) {
				Double cost = sh.getWebElementNumberOrNullWithRetry(By.xpath(assignmentCost.replace("{{resource}}", name)));
				sh.assertEquals(cost, li.cost, "The delivery element: " + deliveryElementName + " line item " + name + " cost is incorrect");
			}
			if(li.revenue != null) {
				Double revenue = sh.getWebElementNumberOrNullWithRetry(By.xpath(assignmentRevenue.replace("{{resource}}", name)));
				sh.assertEquals(revenue, li.revenue, "The delivery element: " + deliveryElementName + " line item " + name + " revenue is incorrect");
			}
		}
	}
	
	public static void manuallyUpdateDeliveryDates(SeleniumHelper sh, SalesOpportunity salesOpp){
		
		sh.clickMenuItem(PagesC.EDITDELIVERYDATES);
		
		for(DeliveryDateBulkEdit dd : salesOpp.deliveryDateEdits){
			if(dd.isActive==true && dd.isAutomatic==false){
				if(dd.checkBox==true)
				sh.waitForPageLoadComplete(3);
				sh.clickLink(By.xpath("//td[normalize-space(text())='"+dd.name+"']/preceding-sibling::td/input"), 10);	
				sh.waitMilliseconds(3000);
				clearAndInputText(sh, By.xpath("//td[normalize-space(text())='"+dd.name+"']/following-sibling::td/span[contains(@id, 'outputDatePanel')]/span/input"), dd.date);
			}
			else{
				if(dd.checkBox==true && dd.isAutomatic==false)
					sh.clickLink(By.xpath("//td[contains(text(),'"+dd.name+"')]/preceding-sibling::td/input"), 10);
			}
		}
		sh.clickLink(applyButton);
		sh.waitForElementToBeClickable(applyButton, 45);
	}
	
	public static void automaticallyUpdateDeliveryDates(SeleniumHelper sh, SalesOpportunity salesOpp){
		
		sh.clickMenuItem(PagesC.EDITDELIVERYDATES);
		
		for(DeliveryDateBulkEdit dd : salesOpp.deliveryDateEdits){
			if(dd.isActive==true && dd.isAutomatic==true && dd.checkBox==true){
				sh.clickLink(By.xpath("//td[normalize-space(text())='"+dd.name+"']/preceding-sibling::td/input"), 10);
				By dateInputField = By.xpath("//td[normalize-space(text())='"+dd.name+"']/following-sibling::td/span/span[contains(@class, 'dateOnlyInput')]/input"); 
				sh.waitForElementToBeVisible(dateInputField);
			}
			else if(dd.checkBox==true && dd.isAutomatic==true){
				sh.clickLink(By.xpath("//td[contains(text(),'"+dd.name+"')]/preceding-sibling::td/input"), 10);
			}
		}
		sh.clickLink(applyButton);
		sh.waitForElementToBeClickable(applyButton);

		for(DeliveryDateBulkEdit dd : salesOpp.deliveryDateEdits){
			if(dd.isActive==true && dd.isAutomatic==true && dd.checkBox==true){
					By currentDateField = By.xpath("//td[normalize-space(text())='"+dd.name+"']/following-sibling::td/span[contains(text(),'"+dd.date+"')]");
					sh.clickMenuItem(PagesC.EDITDELIVERYDATES);
					sh.waitForElementToBeVisible(currentDateField);
			}
		}
	}
	
	public static void runInLineJobsLDV(SeleniumHelper sh, SalesOpportunity salesOpp){

		long startTimeProposal = System.currentTimeMillis();
		sh.clickMenuItem(PagesC.PROPOSALITEMS);
		
		executeSequenceWithRetry(sh, 3, () -> {
		//MAX TIMEOUT 20 mins
			try{
				sh.sleep(60000);
				sh.refreshBrowser();
				waitClickable(sh, proposalContractCost, 20);
			}	catch(Exception e){
					try{
						sh.sleep(60000);
						waitClickable(sh, proposalContractCost, 20);
						sh.refreshBrowser();
					}	catch(Exception e2){
							try{
								sh.sleep(60000);
								waitClickable(sh, proposalContractCost, 20);
								sh.refreshBrowser();
							}	catch(Exception e3){
								try{
									sh.sleep(60000);
									waitClickable(sh, proposalContractCost, 20);
									sh.refreshBrowser();
								}	catch(Exception e4){
										sh.sleep(60000);
										waitClickable(sh, proposalContractCost, 20);
										sh.refreshBrowser();
									}
								}
						}
				}
		});
		long durationProposal = (System.currentTimeMillis() - startTimeProposal)/1000;
		System.out.println(" Proposal Won: "+durationProposal +"s");
		System.out.println("------------------------------------------------------------------------");
		
		validateProposalItemsSummaryFields(sh, salesOpp, "validate proposal summary fields");
	}
}
