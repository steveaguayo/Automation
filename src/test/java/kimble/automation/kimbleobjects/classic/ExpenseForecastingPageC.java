package kimble.automation.kimbleobjects.classic;

import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.ExpenseLineItem;
import kimble.automation.helpers.SeleniumHelper;

import static kimble.automation.helpers.SequenceActions.clearAndInputText;
import static kimble.automation.helpers.SequenceActions.dropdownSelect;

import org.openqa.selenium.By;

public class ExpenseForecastingPageC {
	
	public static final String 
	editCellRowPostfix = "td/span[contains(@onclick, 'editExpense')]",
	categoryRowPostfix = "td[2]",
	businessUnitPostfix = "td[3]",

	expensesCard = "//div[text()='Expenses']/ancestor::div[contains(@class, 'inner-card')]",
	expenseTable = expensesCard + "//table",
	expenseRow = expenseTable + "//tr[" + categoryRowPostfix + "='{{category}}'][" + businessUnitPostfix + "='{{businessUnit}}']",
	editExpenseLink = expenseRow + "/" + editCellRowPostfix,
	
	editPopup = "//div[@id='ExpenseEditPopup']",
	remainingCostLabel = editPopup + "//span[text()='Remaining Cost (GBP)']",
	remainingCostInput = remainingCostLabel + "/../following-sibling::td//input",
	remainingRevenueLabel = editPopup + "//span[text()='Remaining Revenue (GBP)']",
	remainingRevenueInput = remainingRevenueLabel + "/../following-sibling::td//input",
	editSave = editPopup + "//input[@value='Save']",
	editCancel = editPopup + "//input[@value='Cancel']",
	addLink = "//h3[text()='{{elementName}}']//parent::div//parent::div//span[@class='fa fa-plus-square']";
	
	static final By
	startDate = By.cssSelector("input[id$='expenseStartDateInput']"),
	endDate = By.cssSelector("input[id$=expenseEndDateInput]"),
	expensecategory = By.cssSelector("select[id$='expense-category-id']");
	
	
	public static final void editExpenseLineItem(ExpenseLineItem eli, SeleniumHelper sh) {
		By editLink = By.xpath(editExpenseLink.replace("{{category}}", eli.category).replace("{{businessUnit}}", eli.businessUnit));
		By cancelButton = By.xpath(editCancel);
		By saveButton = By.xpath(editSave);
		
		By costInput = By.xpath(remainingCostInput);
		By revenueInput = By.xpath(remainingRevenueInput);

		sh.waitForLightningSpinnerToBeHidden();
		sh.clickAndWaitSequenceWithRefreshRetry(5, editLink, cancelButton);
		if(eli.remainingCost != null) {
			sh.getWebElement(costInput).clear();
			sh.getWebElement(costInput).sendKeys(eli.remainingCost.toString());
		}
		if(eli.remainingRevenue != null) {
			sh.getWebElement(revenueInput).clear();
			sh.getWebElement(revenueInput).sendKeys(eli.remainingRevenue.toString());
		}
		
		sh.clickLink(saveButton);
		sh.waitForBoxyToBeHidden();
	}
	
	public static final void createExpenseLineItem(DeliveryElement element, ExpenseLineItem eli, SeleniumHelper sh) {
		By cancelButton = By.xpath(editCancel);
		By saveButton = By.xpath(editSave);
		
		By costInput = By.xpath(remainingCostInput);
		By revenueInput = By.xpath(remainingRevenueInput);
		By addexpense = By.xpath(addLink.replace("{{elementName}}", element.name));
		System.out.println(addexpense);
		
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickAndWaitSequenceWithRefreshRetry(5, addexpense, cancelButton);
		dropdownSelect(sh, expensecategory, eli.category);
		clearAndInputText(sh, costInput, eli.remainingCost);
		clearAndInputText(sh, revenueInput, eli.remainingRevenue);
		clearAndInputText(sh, startDate, eli.startDate);
		clearAndInputText(sh, endDate, eli.endDate);
		sh.clickLink(saveButton);
		sh.waitForBoxyToBeHidden();
	}
	
}
