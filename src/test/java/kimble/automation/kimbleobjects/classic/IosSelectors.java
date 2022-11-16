package kimble.automation.kimbleobjects.classic;

import org.openqa.selenium.By;

public class IosSelectors extends MobileSelectors {

	public IosSelectors(){
		// home page
		menuBtn = By.xpath("//XCUIElementTypeButton[contains(@name,'menu_btn')] ");	
		menuBtnExpenses = By.xpath("//XCUIElementTypeButton[contains(@name,'Expense')] ");
		dateWidget = By.xpath("//XCUIElementTypeStaticText[@name='week_lbl']");							
		timeEntryBtn = By.xpath("//XCUIElementTypeStaticText[@name='Timesheet']");
		expenseEntryBtn = By.xpath("//XCUIElementTypeStaticText[@name='Expense Claims']");
		timePeriodAttribute = "value";
		timeEntryAdd = By.xpath("(//XCUIElementTypeImage[@name=\"add_time_\"])");
		burgerMenu = By.xpath("(//XCUIElementTypeImage[@name=\"burger_menu_\"])");
		syncBtn = By.xpath("//XCUIElementTypeStaticText[@name='Sync']");
		syncSuccessful = null;
		
		// submission
		submitAll = By.xpath("//XCUIElementTypeButton[@name='Submit All']");
		submitSelected = By.xpath("//XCUIElementTypeButton[@name='Submit Selected 1']");
		optionsBtn = null;	
		selectBtn = By.xpath("//XCUIElementTypeButton[@name='select ']");
		deleteBtn = By.xpath("//XCUIElementTypeButton[@name='delete ']");
		
		// time entry 
		timeEntryActivityField = By.xpath("//XCUIElementTypeStaticText[@name='Activity']");
		timeEntryStartDateField = By.xpath("//XCUIElementTypeStaticText[@name='Start Date']");
		timeEntryEndDateField = By.xpath("//XCUIElementTypeStaticText[@name='End Date']");
		timeEntryTimeTypeField = By.xpath("//XCUIElementTypeStaticText[@name='Time Type']");
		timeEntryUnitsHourField = By.xpath("//XCUIElementTypeStaticText[@name='Units (Hour)']");
		timeEntryUnitsDayField = By.xpath("//XCUIElementTypeStaticText[@name='Units (Day)']");
		timeEntryNotesField = By.xpath("//XCUIElementTypeStaticText[@name='Notes']");
	//	timeEntryDoneBtn = By.xpath("//XCUIElementTypeButton[@name='done_btn']");
		timeEntryDoneBtn = By.xpath("//XCUIElementTypeButton[@label='Save']");
		timeEntryCalendarOK = null;
		timeEntryTaskField = By.xpath("//XCUIElementTypeStaticText[@name='Task Assignment']");
		timeEntryDraftRemainingField = By.xpath("//XCUIElementTypeStaticText[@name='Draft Remaining Effort (Hour)']");
		scrollWheel1 = By.xpath("//XCUIElementTypePickerWheel[1]");
		scrollWheel2 = By.xpath("//XCUIElementTypePickerWheel[2]");
		
		// expense entry 
		expenseActivitySelect = "//XCUIElementTypeStaticText[@name='"+activity+" ']";
		expenseActivitySelectFirst = "//XCUIElementTypeStaticText[@name='Activity']/../following-sibling::XCUIElementTypeCell[@name='dropdown']/XCUIElementTypeStaticText";
		expenseEntryAdd = By.xpath("//XCUIElementTypeButton[@name='add_expense_btn'][2]");
		expenseNameField = By.xpath("//XCUIElementTypeStaticText[@name='Name']");
		expenseStartDateField = By.xpath("//XCUIElementTypeStaticText[@name='Start Date']");
		expenseEndDateField = By.xpath("//XCUIElementTypeStaticText[@name='End Date']");
		expenseActivityField = By.xpath("//XCUIElementTypeStaticText[@name='Activity']");
		expensePreviousMonthBtn = null;
		expenseCategoryField = By.xpath("//XCUIElementTypeStaticText[@name='Expense Category']");
		expenseIncurredAmountField = By.xpath("//XCUIElementTypeStaticText[@name='Incurred Amount']");
		expenseCurrencyBtn = By.xpath("//XCUIElementTypeStaticText[@name='Incurred Amount']/following-sibling::XCUIElementTypeButton");
		expenseRecieptField = By.xpath("//XCUIElementTypeStaticText[@name='Receipt']");
		expenseNotesField = By.xpath("//XCUIElementTypeStaticText[@name='Notes']");
		
		expenseStartLocationField = By.xpath("//XCUIElementTypeStaticText[@name='Start Location']/../following-sibling::XCUIElementTypeCell/following-sibling::XCUIElementTypeCell");
		expenseEndLocationField = By.xpath("//XCUIElementTypeStaticText[@name='End Location']/../following-sibling::XCUIElementTypeCell/following-sibling::XCUIElementTypeCell");
		expenseMileageUnitsField = By.xpath("//XCUIElementTypeStaticText[@name='Units']");
		expenseAttendeesField = By.xpath("//XCUIElementTypeStaticText[@name='Attendees']");
		expenseAddAttendeeBtn = null;
		expenseDoneBtn = By.xpath("//XCUIElementTypeButton[@name='done_btn'][2]");
		expenseNotesEntry = By.xpath("//XCUIElementTypeStaticText[@name='Notes']");
		expenseReturnTo = By.xpath("//XCUIElementTypeButton[@name='Expense Item']");
		expenseCategorySelect = "//XCUIElementTypeStaticText[@name='"+expenseCategory+"']";
		expenseCurrencySelect = "//XCUIElementTypeStaticText[@name='"+expenseCurrency+"']";	
		expenseTaxCode = "//XCUIElementTypeStaticText[@name='"+taxCode+"']";
		
		//general
		keyboardButton = By.xpath("//XCUIElementTypeKey[@name='1']");
	}
}
