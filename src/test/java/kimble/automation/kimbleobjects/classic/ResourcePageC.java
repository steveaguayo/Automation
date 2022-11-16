package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kimble.automation.domain.ExpenseClaim;
import kimble.automation.domain.Invoice;
import kimble.automation.domain.InvoiceLine;
import kimble.automation.domain.Resource;
import kimble.automation.domain.ResourceUsageAdjustment;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.kimbleobjects.lightning.GeneralOperationsZ;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class ResourcePageC extends BasePageC {
	
	private static final String 
	RESOURCE_EDIT_PARAMS = "retURL=%2Fa2L%2Fo&save_new=1&sfdc.override=1",
	MAINTAIN_RESOURCE_LINK_TEXT = "Maintain Resource",
	A_HREF_EXPENSE_CLAIM_EDIT = "a[href*='ExpenseClaimEdit']";
	
	public static final By 
	RESOURCE_EDIT_LINK_XPATH = By.xpath("//span[.='Edit']/parent::a"),
	RESOURCE_DELETE_LINK_XPATH = By.xpath("//span[.='Del']/parent::a"),
	RESOURCE_OPEN_LINK_XPATH = By.xpath("//span[.='Edit']/parent::a/parent::div/parent::td/following-sibling::td/div[contains(@id,'_Name')]/a"),

	newResourceButton = By.xpath("//input[normalize-space(@value)='New Resource']"),
	newButton = By.xpath("//input[normalize-space(@value)='New']"),
	
	resourceTypeSelect = By.cssSelector("select[id$='resourceType']"),
	goButton = By.xpath("//input[normalize-space(@value)='Go']"),
	listGoButton = By.xpath("//input[normalize-space(@value)='Go!']"),
	
	saveButton = By.xpath("//input[normalize-space(@value)='Save']"),
	cloneButton = By.xpath("//input[normalize-space(@value)='Clone']"),
	
	maintainResourceLink = By.xpath("//a[normalize-space(text())='Maintain Resource']"),
	
	firstNameInput = By.cssSelector("input.ResourceFirstName"),
	lastNameInput = By.cssSelector("input.ResourceLastName"),

	userSelect = By.cssSelector("select[id$='sfUser_SELECT']"),

	businessUnitSelect = By.cssSelector("select[id$='primaryBusinessUnit']"),
	secondaryBusinessUnitSelect = By.cssSelector("select[id$='secondaryBusinessUnit']"),

	gradeSelect = By.cssSelector("select[id$='resourceGrade']"),
	effectiveDatePicker = By.cssSelector("span.effective-date"),
	locationSelect = By.cssSelector("select[id$='resourceLocationDropdown']"),

	calendarSelect = By.cssSelector("select[id$='resourceCalendar']"),
	timePatternVariantSelect = By.xpath("//th[normalize-space(text())='Time Pattern Variant']/following-sibling::td/select"),
	
	allowanceSchemeSelect = By.cssSelector("select[id$='resourceAllowanceScheme']"),

	currencySelect = By.cssSelector("select[id$='resourceCurrency']"),
	expenseCurrencySelect = By.cssSelector("select[id$='expenseCurrency_SELECT']"),

	actualCostInput = By.cssSelector("input[id$='actualCost']"),
	actualCostUnitTypeSelect = By.cssSelector("select[id$='actualCostUnitType']"),

	standardCostInput = By.cssSelector("input[id$='standardCost']"),
	standardCostUnitTypeSelect = By.cssSelector("select[id$='standardCostUnitType']"),

	targetRateInput = By.cssSelector("input[id$='targetRate']"),
	targetRateUnitTypeSelect = By.cssSelector("select[id$='targetRateUnitType']"),

	startDateInput = By.cssSelector("input[id$='startDate']"),
	endDateInput = By.cssSelector("input[id$='endDate']"),	
	
	journeyTab = By.linkText("Journey"),
	journeyTable = By.cssSelector("div.title-card-menu"),
	
	baselineUsage = By.xpath("//td[.='Baseline Usage']/following-sibling::td[@class='tdright']"),
	adjustments = By.xpath("//td[.='Adjustments']/following-sibling::td[@class='tdright']"),
	totalActualUsage = By.xpath("//td[.='Total Actual Usage']/following-sibling::td[@class='tdright']"),
	scheduledUsage = By.xpath("//td[.='Scheduled Usage']/following-sibling::td[@class='tdright']"),
	remainingUsage = By.xpath("//td[.='Remaining Usage']/following-sibling::td[@class='tdright']"),
	unforecastUsage = By.xpath("//td[.='Unforecast Usage']/following-sibling::td[@class='tdright']")
	
	;

	public ResourcePageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}	
	
	public void NavigateToResourceJourney(SeleniumHelper sh){
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20, 
			/* click new						*/	journeyTab,
			/* wait for Journey save button		*/	journeyTable
			);
		});
	}
		
	public static void startCreateWiz(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20, 
			/* click new						*/	newButton,
			/* wait for resource type select	*/	resourceTypeSelect
			);
		});
	}
	
	public static void startEdit(SeleniumHelper sh, String resource) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 40, 
			/* click go								*/	listGoButton,
			/* click on the resource				*/	By.linkText(resource),
			/* click to maintain the resource		*/	maintainResourceLink,
			/* wait for resource type select		*/	firstNameInput
			);
		});
	}
	
	public static void createWizStep2SelectType(SeleniumHelper sh, String aResourceType) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			dropdownSelect(sh, resourceTypeSelect, aResourceType);
			clickAndWaitSequence(sh, 20, 
			/* click go							*/	goButton,
			/* wait for first name input		*/	firstNameInput
			);
		});
	}
	
	public static void createWizStep3inputValues(SeleniumHelper sh, Resource resource) {
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clearAndInputText(sh, firstNameInput, resource.firstName);
			clearAndInputText(sh, lastNameInput, resource.lastName);
			dropdownSelect(sh, userSelect, resource.user);
			dropdownSelect(sh, businessUnitSelect, resource.businessUnitPrimary);
			dropdownSelect(sh, secondaryBusinessUnitSelect, resource.businessUnitSecondary);
	
			dropdownSelect(sh, locationSelect, resource.location);
			dropdownSelect(sh, calendarSelect, resource.calendar);
			dropdownSelect(sh, timePatternVariantSelect, resource.timePatternVariant);
			dropdownSelect(sh, allowanceSchemeSelect, resource.allowanceScheme);
			dropdownSelect(sh, currencySelect, resource.currency);
			dropdownSelect(sh, expenseCurrencySelect, resource.expenseReimbursementCurrency);
			clearAndInputText(sh, actualCostInput, resource.actualCost);
			dropdownSelect(sh, actualCostUnitTypeSelect, resource.actualCostUnit);
			clearAndInputText(sh, standardCostInput, resource.standardCost);
			dropdownSelect(sh, standardCostUnitTypeSelect, resource.standardCostUnit);
			clearAndInputText(sh, targetRateInput, resource.targetRate);
			dropdownSelect(sh, targetRateUnitTypeSelect, resource.targetRateUnit);
			clearAndInputText(sh, startDateInput, resource.startDate);
			clearAndInputText(sh, endDateInput, resource.endDate);
			
			if (!(resource.grade==null)){
				dropdownSelect(sh, gradeSelect, resource.grade);
			    try {
					JQueryDatePickerC.navigateToDay(sh, effectiveDatePicker, resource.gradeChangeDate);
			    } catch (Exception ex){
			    	System.out.print("The grade was already selected");
			    }
			}				
			
			clickAndWaitSequence(sh, 20, 
			/* click go							*/	saveButton,
			/* wait for clone button			*/	cloneButton
			);
		});
	}
	
	public static void deleteAllResources(SeleniumHelper sh) {
		// The resource names need to go through two escapes and the '-characters need to remain escaped hence the \\\\ escape sequence
		String resourcesStr = Arrays.asList("'UK Co 3 x\\\\'x x&x Employee One'").stream()
				.reduce((a, b) -> { return a + ", " + b; }).get();
		try {
			sh.executeJavaScript("result = sforce.connection.query(\"Select Id, name from " + (SeleniumHelper.isPackagedOrg() ? "KimbleOne__" : "") + "Resource__c where name in (" + resourcesStr + ")\"); records = result.getArray('records');  for (var i=0; i< records.length; i++) {    var record = records[i];    sforce.connection.deleteIds([record.Id]);  }");
		} catch(Exception e) { e.printStackTrace(); }
	}

	public void NavigateToList(){
		if(theSH.isLightning()){
			GeneralOperationsZ.navigateFromAnywhereToTab(theSH, "Resources");
		}
		else{
			theSH.NavigateToList("Resources");
			waitClickable(theSH, listGoButton, 20);
			click(theSH, listGoButton);
		}

	}
	
	public void NavigateToNewResource() {
		theSH.navigateToVFPage(PagesC.RESOURCEEDIT, RESOURCE_EDIT_PARAMS);
	}
	
	public void LoadExistingByName(String resourceName) {
		// first try and click the link in the Recent Items list (in normal operation of the test
		// it's likely that the item will have been recently created
			try {
				NavigateToList();
				if(theSH.isLightning())
					theSH.clickLink(By.linkText(resourceName), 15);
				else
					theSH.OpenExisting(resourceName);
			} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
				// if the browser has died (can happen with glitches in chromedriver)
				// then we should exit the test as retry will never succeed
				throw ubEx;
			} catch (Exception e1) {
				// not there so navigate to the main list then try in the list recent items
				NavigateToList();
				try {
					if(theSH.isLightning())
						theSH.clickLink(By.linkText(resourceName), 15);
					else
						theSH.OpenExisting(resourceName);
				} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
					// if the browser has died (can happen with glitches in chromedriver)
					// then we should exit the test as retry will never succeed
					throw ubEx;
				} catch (Exception e) {
					// not in recent items list so continue onto the list itself
					waitClickable(theSH, listGoButton, 20);
					click(theSH, listGoButton);
					theSH.waitForElementToBeClickable(By.cssSelector("input[value='New Resource']"), 40);
					theSH.OpenExisting(resourceName);
				}
			}
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickable(By.partialLinkText(MAINTAIN_RESOURCE_LINK_TEXT));
	}
	
	public void NavigateToTimesheetView(){
		theSH.switchToClassicIframeContext();
		theSH.clickMenuItem(PagesC.TIMESHEET);
		// this button is now only shown if there are entries to be submitted....
		//theSeleniumHelper.waitForElementToBeClickableBy(By.cssSelector(INPUT_ID$_SUBMIT_TIME_ENTRIES_BTN));
		// sleep for now until I can think of a fix
		theSH.waitForLightningSpinnerToBeHidden();
		waitClickable(theSH, TimesheetPageC.timeEntryPeriodHeader, 20);
	}
	
	public void NavigateToSupplierDashboard(){
		theSH.switchToClassicIframeContext();
		theSH.clickMenuItem(PagesC.SUPPLIERDASHBOARD);
		// this button is now only shown if there are entries to be submitted....
		//theSeleniumHelper.waitForElementToBeClickableBy(By.cssSelector(INPUT_ID$_SUBMIT_TIME_ENTRIES_BTN));
		// sleep for now until I can think of a fix
		theSH.waitForLightningSpinnerToBeHidden();
		waitClickable(theSH, ResourceSupplierDashboardPageC.addSupplierProduct, 20);
	
	}
	public void NavigateToResourceForecastFromHome() {
		// use the URL for an open sales opp, substituting "Forecast" for "Home"
		theSH.goToUrl(theSH.getCurrentUrl().replaceAll(PagesC.RESOURCEHOME, PagesC.RESOURCEFORECAST));
	}
	
	public void NavigateToResourceUsageAdjustmentFromHome() {
		if(theSH.isLightning()){
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.clickLink(By.cssSelector("a[href*='ResourceUsageAdjustments']"));
		}
		else{
			theSH.goToUrl(theSH.getCurrentUrl().replaceAll(PagesC.RESOURCEHOME, PagesC.RESOURCEUSAGEADJUSTMENTS));			
		}
	}
	
	public void NavigateToResourceHomeFromForecast() {
		theSH.goToUrl(theSH.getCurrentUrl().replaceAll(PagesC.RESOURCEFORECAST, PagesC.RESOURCEHOME));
	}
	
	public WebElement getExpenseEntryLinkForResource(ExpenseClaim expenseClaimDetails) {
		WebElement expenseEntryLink;
		LoadExistingByName(expenseClaimDetails.resourceName);
		// expenses are normally entered in the context of the logged in user
		// so instead we'll directly navigate to the expense entry screen by re-using the Expense Entry
		// link that's on the timesheet view
		NavigateToTimesheetView();
		// get the link to any one of the Expense Item links on the timesheet screen
		expenseEntryLink = theSH.getWebElements(By.cssSelector(A_HREF_EXPENSE_CLAIM_EDIT)).get(0);
		return expenseEntryLink;
	}
	
	public void deleteWithPreservation(List<Resource> preservedResources){
		NavigateToList();
		waitClickable(theSH, listGoButton, 20);
		click(theSH, listGoButton);
		
		List<ListGridItemC> allLinks = theSH.getEditDelAndOpenLinks(RESOURCE_EDIT_LINK_XPATH, RESOURCE_DELETE_LINK_XPATH, RESOURCE_OPEN_LINK_XPATH, RESOURCE_OPEN_LINK_XPATH);
		List<ListGridItemC> targetLinks = filterLinks(allLinks, preservedResources);
		
		for (ListGridItemC listGridItem : targetLinks) {
			theSH.goToUrl(listGridItem.deleteUrl);
		}
	}

	// remove any resources that are in the preserve resource list from the list of links
	private List<ListGridItemC> filterLinks(List<ListGridItemC> allResourceLinks, List<Resource> preservedResources) {
		List<ListGridItemC> filteredResourceLinks = new ArrayList();
		boolean preserveResource;
		
		// only bother filtering if we have any preserved resources indicated
		if(preservedResources.size() > 0) {
			for (ListGridItemC resourceLink : allResourceLinks) {
				preserveResource = false;
				// if this resource is in the list of preserved resources then mark it for preservation
				for (Resource resource : preservedResources) {
					if(resource.name.equals(resourceLink.name)) {
						preserveResource = true;
						break;
					}
				}
					
				if(!preserveResource) {
					filteredResourceLinks.add(resourceLink);
				}
			}
			return filteredResourceLinks;
		}
		else {
			return allResourceLinks;
		}
	}
	
	public void validateUsageAdjustment(ResourceUsageAdjustment res) {
		theSH.waitForPageLoadComplete(20);
		if(res.baselineUsage != null)
			theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(baselineUsage), res.baselineUsage, "The baselineUsage is incorrect");
		if(res.adjustments != null)
			theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(adjustments), res.adjustments, "The adjustmentst is incorrect");
		if(res.totalActualUsage != null)
			theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(totalActualUsage), res.totalActualUsage, "The totalActualUsage is incorrect");
		if(res.scheduledUsage != null)
			theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(scheduledUsage), res.scheduledUsage, "The scheduledUsage is incorrect");
		if(res.remainingUsage != null)
			theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(remainingUsage), res.remainingUsage, "The remainingUsage is incorrect");
		if(res.unforecastUsage != null)
			theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(unforecastUsage), res.unforecastUsage, "The unforecastUsage is incorrect");
	
		
	}
}
