package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import kimble.automation.domain.ExpenseClaim;
import kimble.automation.domain.ExpenseDetail;
import kimble.automation.domain.ExpenseEntry;
import kimble.automation.domain.Timesheet;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class ExpensePageC extends BasePageC {
	
	public static final By
	resourceLink = By.xpath("//tr/th[label[contains(text(),'Resource')]]/following-sibling::td/span/a"),
	activitySelect = By.cssSelector("select[id$='ActivityList']"),
	addExpenseButton = By.cssSelector("input[id$='addExpenseBtn']"),
	revertToDraftButton = By.cssSelector("input[value='Revert To Draft']"),
	expenseCategoryTaxCodeCheckbox = By.xpath("//label[text()='Can Edit Tax Code']/../..//input[@type='checkbox']"),
	expenseCategoryTaxCodeEdit = By.name("edit"),
	expenseCategoryTaxCodeSave = By.name("save");

	public static final String 
	INPUT_ID$_EXPENSE_CLAIM_NAME = "input[id$='expenseClaimName']",
	SELECT_ID$_INCURRED_AMOUNT_CURRENCY_FIELD = "select[id$='incurredAmountCurrencyField']",
	TEXTAREA_ID$_EXPENSE_ITEM_NOTES = "textarea[id$='expenseItemNotes']",
	INPUT_ID$_INCURRED_DATE_FIELD = "input[id$='incurredDateField']",
	INPUT_VALUE_NEW_EXPENSE_CLAIM = "input[value='New Expense Claim']",
	INPUT_VALUE_NEW_EXPENSE_CLAIM_FROM_TIMESHEET = "a[title='+ Expense Item']",
	INPUT_ID$_INCURRED_AMOUNT_FIELD = "input[id$='incurredAmountField']",
	SELECT_ID$_EXPENSE_CATEGORY_SELECT = "select[id$='expenseCategorySelect']",
	SELECT_ID$_ACTIVITY_LIST = "select[id$='ActivityList']",
	START_LOCATION_FIELD_XPATH = "//tr[@id='start-end-location']/td[2]/input",
	END_LOCATION_FIELD_XPATH = "//tr[@id='start-end-location']/td[4]/input";

	static By 
	saveAndSubmitBtn = By.cssSelector("input[id$='saveAndSubmitBtn']"),
	newExpenseClaimBtn = By.xpath("//input[normalize-space(@value)='New Expense Claim']");
	
	static final By taxCodeDropdown = By.xpath("//table/tbody/tr/td/div[2]/span/form/div[1]/span/span[2]/div/div/div/div[1]/div[3]/div/table/tbody/tr[4]/td/select");
	static final String taxCodeEurope = "Europe";
	static final By saveBtn = By.xpath("//input[@value='Save']");
	static final By expenseReadyForApproval = By.xpath("//div[contains(text(),'ReadyForApproval')]");

	@FindBy(css = "input[value='Save']")
	private WebElement save;

	@FindBy(css = "input[value='Save and Close']")
	private WebElement saveAndClose;

	private By 
	incurredDateField = By.cssSelector(INPUT_ID$_INCURRED_DATE_FIELD),
	jobSpinner = By.cssSelector(".Processing");
	
	// templated
	//public static final String expenseItemRow = "//tr/td[2][span/span[normalize-space(text())=\"{{date}}\"]]/.."
		//	+ "/td[4][div//div[normalize-space(text())=\"{{amount}}\"]][div//td[normalize-space(text())=\"{{currency}}\"]]";
	
	public static final String expenseItemRow = "//tr/td[2][span/span[normalize-space(text())=\"{{date}}\"]]/.."
			+ "/td[4][div//div//span[normalize-space(text())=\"{{currency}}" + " " + "{{amount}}\"]]";
	
	public ExpensePageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void CreateNew(ExpenseEntry expenseEntryDetails) {
		CreateNew(expenseEntryDetails, true);	
	}

	public void CreateNew(ExpenseEntry expenseEntryDetails, boolean submit) {
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_INCURRED_DATE_FIELD), 5);

		clearAndInputText(theSH, incurredDateField, expenseEntryDetails.incurredDate);
		
		// now the date is settled, fill in the rest of the detail
		theSH.getWebElement(By.cssSelector(INPUT_ID$_EXPENSE_CLAIM_NAME)).sendKeys(expenseEntryDetails.name);

		waitHidden(theSH, jobSpinner, 20);
	
		
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			waitClickable(theSH, By.cssSelector(SELECT_ID$_ACTIVITY_LIST), 5);
			theSH.waitMilliseconds(3000);
			dropdownSelect(theSH, By.cssSelector(SELECT_ID$_ACTIVITY_LIST), expenseEntryDetails.activityName);
			theSH.waitForElementToBeClickable(addExpenseButton);
		});
				
		addExpenseDetails(expenseEntryDetails);
		
		if(submit)
			click(theSH, saveAndSubmitBtn);
		else
			saveAndClose.click();
		if(theSH.isLightning())
			theSH.waitForElementToBeClickableWithRetry(KBy.title("New"), 5);	
		else
		{
//			theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_VALUE_NEW_EXPENSE_CLAIM), 5);	// old behaviour - when directed back to list view
			theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_VALUE_NEW_EXPENSE_CLAIM_FROM_TIMESHEET), 5);
		}
	}
	
	public static void saveAndSubmit(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
		/* click save and submit					*/	click(sh, saveAndSubmitBtn);
		/* wait for the new journey button			*/	waitClickable(sh, newExpenseClaimBtn, 20);
		});
	}

	private void addExpenseDetails(ExpenseEntry expenseEntryDetails) {
		// due to the dynamic nature of the screen we are seeing stale element exceptions so always access elements directly based on css selector
		theSH.waitForElementToBeClickableWithRetry(addExpenseButton, 5);
		for (ExpenseDetail expenseDetails : expenseEntryDetails.expenseDetails) {
			// no page factory references here as each additional expense causes a screen refresh
			theSH.waitForElementToBeClickableWithRetry(addExpenseButton, 5);
			theSH.getWebElement(addExpenseButton).click();
			
			// as we add new expense lines we need to ensure we are working with the last row
			// this is done by always working with the last entry in the array of webelements returned
			// for each selector
			theSH.waitForElementToBeClickableWithRetry(By.cssSelector(SELECT_ID$_EXPENSE_CATEGORY_SELECT), 5);
			theSH.getLastWebElement(By.cssSelector(SELECT_ID$_EXPENSE_CATEGORY_SELECT)).sendKeys(expenseDetails.category);
			
			theSH.waitForElementToBeClickableWithRetry(By.cssSelector(SELECT_ID$_INCURRED_AMOUNT_CURRENCY_FIELD), 5);
			theSH.getLastWebElement(By.cssSelector(SELECT_ID$_INCURRED_AMOUNT_CURRENCY_FIELD)).sendKeys(expenseDetails.currency);
			
			theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_INCURRED_AMOUNT_FIELD), 5);
			theSH.getLastWebElement(By.cssSelector(INPUT_ID$_INCURRED_AMOUNT_FIELD)).sendKeys(expenseDetails.amount + Keys.TAB);
			
			if(expenseDetails.startLocation != null && expenseDetails.endLocation != null)
			{
				theSH.getLastWebElement(By.xpath(START_LOCATION_FIELD_XPATH)).sendKeys(expenseDetails.startLocation);
				theSH.getLastWebElement(By.xpath(END_LOCATION_FIELD_XPATH)).sendKeys(expenseDetails.endLocation);			
			}
			
			if(expenseDetails.notes != null && expenseDetails.notes != "")
			{
				theSH.waitForElementToBeClickableWithRetry(By.cssSelector(TEXTAREA_ID$_EXPENSE_ITEM_NOTES), 5);
				theSH.getLastWebElement(By.cssSelector(TEXTAREA_ID$_EXPENSE_ITEM_NOTES)).sendKeys(expenseDetails.notes);
			}
		}
	}
	
	public static void navigateToExpense(SeleniumHelper sh, ExpenseEntry entry, ExpenseDetail detail){
		sh.clickLink(By.name("go"));

		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.clickLink(By.partialLinkText(entry.activityName));
		});
		
		By currencyCode = By.cssSelector("span.kimbleCurrencyCode");
		By incurredAmount = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[1]/div/div/div[2]/div/div[2]/table/tbody/tr/td/span/table/tbody/tr[1]/td[6]/span[1]");
		sh.waitForElementToBeClickable(By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[1]/div/div/div[2]/div/div[2]/table/tbody/tr/td/span/table/tbody/tr[1]/td[6]/span[2]"));
		sh.assertEquals(sh.getWebElementTextOrNullWithRetry(incurredAmount), detail.currency, "The incurred amount is incorrect");
	}
	
	public static void validate(SeleniumHelper sh, String resource, ExpenseEntry entry) {
		/* validate the resource name			*/	validateStringValue(sh, resourceLink, resource, "resource name");
		/* validate the activity name			*/	validateStringValue(sh, activitySelect, entry.activityName, "activity name");
		if(entry.expenseDetails == null)
			return;
		entry.expenseDetails.forEach((detail) -> {
			String expression = expenseItemRow
		/* validate incurred date				*/	.replace("{{date}}", detail.incurredDate)
		/* validate currency					*/	.replace("{{currency}}", detail.currency)
		/* validate amount						*/	.replace("{{amount}}", detail.amount);
			sh.assertTrue(exists(sh, By.xpath(expression)
			, 20), "expense item with incurred date: " + detail.incurredDate + ", currency: " + detail.currency + " and amount: " + detail.amount + " -- "
					+ "selector: " + expression);
		});
	}
	
	public static void navigateToExpenseClaimAndReject(SeleniumHelper sh, ExpenseClaim ex){
		sh.clickLink(By.name("go"));
		String expenseId = sh.getWD().findElement(expenseReadyForApproval).getAttribute("id").split("\\_")[0];	
		sh.clickLink(By.linkText(ex.resourceName));
		String url = sh.getCurrentUrl().split("\\ResourceHome")[0].replace("apex/", "apex/expenseclaimapprove?id=" + expenseId + "&sfdc.override=1");
		sh.goToUrl(url);
		sh.clickAndWait(By.linkText("Approve / Reject"), By.name("Reject"), 5);
		sh.clickLink(By.name("Reject"));
	}
	
	public static void navigateToExpenseClaimAndApprove(SeleniumHelper sh, ExpenseClaim ex){
		sh.clickLink(By.name("go"));
		String expenseId = sh.getWD().findElement(expenseReadyForApproval).getAttribute("id").split("\\_")[0];		
		sh.clickLink(By.linkText(ex.resourceName));
		String url = sh.getCurrentUrl().split("\\ResourceHome")[0].replace("apex/", "apex/expenseclaimapprove?id=" + expenseId + "&sfdc.override=1");
		sh.goToUrl(url);
		sh.clickAndWait(By.linkText("Approve / Reject"), By.xpath("//input[@title='Approve']"), 5);
		sh.clickLink(By.xpath("//input[@title='Approve']"));
	}
	
	public static void setTaxCodeOnCategory(SeleniumHelper sh, String exCategory){
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForElementToBeClickable(By.xpath("//span[text()='"+exCategory+"']/../..//a[text()='Edit']"));
			sh.sleep(500);
			sh.clickLink(By.xpath("//span[text()='"+exCategory+"']/../..//a[text()='Edit']"));
			dropdownSelect(sh, taxCodeDropdown, taxCodeEurope);
			sh.clickLink(saveBtn);
		});
	}
	
	public static void checkTaxCodeOnExpenseCategory(SeleniumHelper sh, String cat, Boolean check){
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.closeLightningPopUp();
			sh.clickLink(By.name("go"));
			sh.clickAndWait(By.linkText(cat), expenseCategoryTaxCodeEdit, 5);
			sh.clickLink(expenseCategoryTaxCodeEdit);
			sh.waitForElementToBeVisible(expenseCategoryTaxCodeCheckbox);
			checkboxSelectIfVisibleAndEnabled(sh, expenseCategoryTaxCodeCheckbox, check);
			sh.clickAndWait(expenseCategoryTaxCodeSave, expenseCategoryTaxCodeEdit, 5);
		});
	}
	
}
