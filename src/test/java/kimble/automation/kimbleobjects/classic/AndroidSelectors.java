package kimble.automation.kimbleobjects.classic;

import org.openqa.selenium.By;

public class AndroidSelectors extends MobileSelectors {

	public AndroidSelectors(){
		// home page
		menuBtn = By.xpath("//android.widget.ImageButton");
		dateWidget = By.id("kimble.timeandexpenses2:id/timesheet_view_header_txt");
		timeEntryBtn = By.xpath("//android.widget.TextView[@text='Timesheet']");
		expenseEntryBtn = By.xpath("//android.widget.TextView[@text='Expense Claims']");
		timePeriodAttribute = "text";
		timeEntryAdd = By.xpath("//android.widget.ImageView[contains(@resource-id,'add_newtimeperiod_img')]");
		burgerMenu = null;
		syncBtn = By.xpath("//android.widget.TextView[@text='Sync']");
		syncSuccessful = By.xpath("//android.widget.TextView[@text='Synced Successfully!']");
		
		// submission
		submitAll = By.xpath("//android.widget.Button[contains(@resource-id,'submit_all_btn')]");
		submitSelected = By.xpath("//android.widget.Button[@text='Select Items']");
		optionsBtn = By.xpath("//android.widget.ImageView[contains(@content-desc,'More options')]");
		selectBtn = By.xpath("//android.widget.TextView[@text='Select']");
		deleteBtn = By.id("kimble.timeandexpenses2:id/menu_timesheet_delete_action");
		
		// time entry 
		timeEntryActivityField = By.xpath("//android.widget.TextView[@text='Activity']");
		timeEntryStartDateField = By.xpath("//android.widget.TextView[@text='Start Date']");
		timeEntryEndDateField = By.xpath("//android.widget.TextView[@text='End Date']");
		timeEntryTimeTypeField = By.xpath("//android.widget.TextView[@text='Time Type']");
		timeEntryUnitsHourField = By.xpath("//android.widget.TextView[@text='Units (Hour)']");
		timeEntryUnitsDayField = By.xpath("//android.widget.TextView[@text='Units (Day)']");
		timeEntryNotesField = By.xpath("//android.widget.TextView[@text='Notes']");
		timeEntryDoneBtn = By.xpath("//android.widget.Button[contains(@resource-id,'done_btn')]");
		timeEntryCalendarOK = By.xpath("//android.widget.Button[@text='OK']");
		timeEntryTaskField = By.xpath("//android.widget.TextView[@text='Task Assignment']");
		timeEntryDraftRemainingField = By.xpath("//android.widget.TextView[@text='Draft Remaining Effort (Hour)']");
		scrollWheel1 = null;
		scrollWheel2 = null;
		
		// expense entry 
		expenseActivitySelect = "//android.widget.CheckedTextView[@text='"+ activity +" ']";
		expenseActivitySelectFirst = "//android.widget.CheckedTextView[contains(@resource-id,'desc_txt')]";
		expenseEntryAdd = By.xpath("//android.widget.Button[@content-desc='Add New Expense Claim']");
		expenseNameField = By.xpath("//android.widget.TextView[@text='Name']/following-sibling::android.widget.ImageView");
		expenseStartDateField = By.xpath("//android.widget.TextView[@text='Start Date']");
		expenseEndDateField = By.xpath("//android.widget.TextView[@text='Incurred Date']");
		expenseActivityField = By.xpath("//android.widget.TextView[@text='Activity']");
		expensePreviousMonthBtn = By.xpath("//android.widget.ImageButton[contains(@content-desc,'Previous month')]");
		expenseCategoryField = By.xpath("//android.widget.TextView[@text='Expense Category']");
		expenseIncurredAmountField = By.xpath("//android.widget.TextView[@text='Incurred Amount']/following-sibling::android.widget.EditText");
		expenseCurrencyBtn = By.xpath("//android.widget.TextView[@text='Incurred Amount']/following-sibling::android.widget.Spinner");
		expenseRecieptField = By.xpath("//android.widget.TextView[@text='Receipt']");
		expenseNotesField = By.xpath("//android.widget.TextView[@text='Notes']");
		
		expenseStartLocationField = By.xpath("//android.widget.ExpandableListView[@content-desc='Expense entry form']/android.widget.LinearLayout[7]");
		expenseEndLocationField = By.xpath("//android.widget.ExpandableListView[@content-desc='Expense entry form']/android.widget.LinearLayout[10]");
		expenseMileageUnitsField = By.xpath("//android.widget.TextView[@text='Units (Mile)']");
		expenseAttendeesField = By.xpath("//android.widget.TextView[@text='Attendees']");
		expenseAddAttendeeBtn =By.xpath("//android.widget.TextView[contains(@resource-id,'add_attendee_manual')]");
		expenseDoneBtn =  By.xpath("//android.widget.Button[@text='Done']");
		expenseNotesEntry = null;
		expenseReturnTo = null;
		expenseCategorySelect = "//android.widget.CheckedTextView[@text='"+expenseCategory+"']";
		expenseCurrencySelect = "//android.widget.TextView[@text='"+expenseCurrency+"']";
		expenseTaxCode = "//android.widget.TextView[contains(@text,'Tax Code')]/following-sibling::android.widget.TextView[@index=2]";
				
		//general
		keyboardButton = null;
	}
}
