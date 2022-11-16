package kimble.automation.kimbleobjects.classic;

import org.openqa.selenium.By;

public class MobileSelectors {
	static final String
	activity = "<activity>",
	expenseCategory = "<expenseCategory>",
	expenseCurrency = "<expenseCurrency>",
	taxCode = "taxCode";
	
	public String 
	timePeriodAttribute = null,
	expenseActivitySelect = null,
	expenseCategorySelect = null,
	expenseActivitySelectFirst = null,
	expenseTaxCode = null,
	expenseCurrencySelect = null;
	
	// home page
	public By
	menuBtn = null,
	menuBtnExpenses = null,
	expenseEntryBtn = null,
	timeEntryAdd = null,
	timeEntryBtn = null,
	dateWidget = null,
	syncBtn = null,
	syncSuccessful = null,
	burgerMenu = null,
	
	// submission
	submitAll = null,
	submitSelected = null,
	optionsBtn = null,
	selectBtn = null,
	
	// time entry
	timeEntryActivityField = null,
	timeEntryStartDateField = null,
	timeEntryEndDateField = null,
	timeEntryTimeTypeField = null,
	timeEntryUnitsHourField = null,
	timeEntryUnitsDayField = null,
	timeEntryNotesField = null,
	timeEntryDoneBtn = null,
	timeEntryCalendarOK = null,
	timeEntryTaskField= null,
	timeEntryDraftRemainingField = null,
	scrollWheel1 = null,
	scrollWheel2 = null,
	deleteBtn = null,
	
	// expense entry
	expenseEntryAdd = null,
	expenseNameField = null,
	expenseStartDateField = null,
	expenseEndDateField = null,
	expensePreviousMonthBtn = null,
	expenseActivityField = null,
	expenseCategoryField = null,
	expenseIncurredAmountField = null,
	expenseCurrencyBtn = null,
	expenseRecieptField = null,
	expenseNotesField = null,
	expenseNotesEntry = null,
	expenseStartLocationField = null,
	expenseEndLocationField = null,
	expenseMileageUnitsField = null,
	expenseAttendeesField = null,
	expenseAddAttendeeBtn = null,
	expenseDoneBtn = null,
	expenseReturnTo = null,
	
	//general
	keyboardButton = null;
	
}
