package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.xpath.XPath;

import kimble.automation.domain.ExpenseDetail;
import kimble.automation.domain.ExpenseEntry;
import kimble.automation.domain.TestState;
import kimble.automation.domain.TimeEntry;
import kimble.automation.domain.TimeEntryAdjustment;
import kimble.automation.domain.Timesheet;
import kimble.automation.domain.mobile.ExpenseClaimMob;
import kimble.automation.domain.mobile.ExpenseItemMob;
import kimble.automation.domain.mobile.TimeEntryMob;
import kimble.automation.helpers.General;
import kimble.automation.helpers.MobileRequests;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.scenarios.EnterAndSubmitTimeBruteTest;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.webdriven.commands.WaitForCondition;

public class TimesheetPageC extends BasePageC {
	
	static SimpleDateFormat pageDateFormat = new SimpleDateFormat("yyyy-M-d");
	
	private static final String ADD_TIME_ENTRY_POPUP = "AddEntryPopup";
	private static final String INPUT_CLASS$_HAS_DATEPICKER = "input[class$='hasDatepicker']";
	private static final String SELECT_ID$_PLANNED_ACTIVITY_SELECT = "select[id$='plannedActivitySelect']";
	private static final String INPUT_ID$_ENTRY_UNITS = "input[id$='entryUnits']";
	private static final String A_ONCLICK_NEW_TIME_ENTRY = "span[onclick*='EntryPopup']";
	private static final String DIV_CLASS_TIME_ENTRY_PERIOD_HEADER = "div[class$='TimeEntryPeriodHeader']";
	private static final String EXPAND_DATE_ENTRY_FIELD_WIDTH = "$(\"input[class$='hasDatepicker']\")[0].style.width='50px'";
	private static final String EXPAND_DATE_ENTRY_FIELD_HEIGHT = "$(\"input[class$='hasDatepicker']\")[0].style.height='20px'";
	private static final String INPUT_ID$_SUBMIT_TIME_ENTRIES_BTN = "input[id$='submitTimeEntriesBtn']";
	private static final String DATE_TOKEN = "<DATETOKEN>";
	private static final String ACTIVITY_NAME = "<ACTIVITYNAME>";
	private static final String ACTUALISE_CHECKBOX_XPATH = "//div[contains(text(),'" + DATE_TOKEN + "')]/parent::div/following-sibling::div//*[contains(text(),\"" + ACTIVITY_NAME + "\")]/parent::div/following-sibling::div//input[@class='actualise-time-checkbox']";
	private static final String ACTUALISE_CHECKBOX_ENHANCED_XPATH = "//div[@id='fixed-header-container']//div[contains(text(),'" + DATE_TOKEN + "')]/parent::div/following-sibling::div//span[contains(text(),\"" + ACTIVITY_NAME + "\")]/following-sibling::div//input[@class='actualise-time-checkbox']";
	private static final String REJECTED_TIMESHEET_XPATH = "//div[contains(text(),'" + DATE_TOKEN + "')]/parent::div/following-sibling::div//*[contains(text(),\"" + ACTIVITY_NAME + "\")]/parent::div/following-sibling::div";
	private static final String ACTUALISE_ALL_CHECKBOX_XPATH = "//div[contains(text(),\"" + ACTIVITY_NAME + "\")]/parent::div/following-sibling::div//input[@class='actualise-time-checkbox']";
	private static final String ACTUALISE_ALL_CHECKBOX_ENHANCED_XPATH = "//span[contains(text(),\"" + ACTIVITY_NAME + "\")]/following-sibling::div//input[@class='actualise-time-checkbox']";
	
	static By 
	startTimeEntryInput = By.cssSelector("input.StartTimeText"),
	endTimeEntryInput = By.cssSelector("input.EndTimeText");
	
	static final By logoImage = By.cssSelector("#phHeaderLogoImage");
	static final By TIME_TYPE = By.xpath("//label[text()='Time Type']/../following-sibling::td[1]/div[1]/select[1]");
	static final By calendarView = By.linkText("Calendar");
	
	static final By datePickerButton = By.cssSelector(".ui-datepicker-trigger");
	static final By previousWeek = By.cssSelector("a[title='Previous Period']");
	static final By nextWeek = By.cssSelector("a[title='Next Period']");
	
	static final String dayTitle = "//div[@class='TimeEntryPeriodHeaderLabel'][contains(text(), '{{title}}')]";
	static final String timeEntryDialogButton = dayTitle + "/following-sibling::div/span[@class='newTimeEntryIcon headerActionIcon']";
	static final String editTimeEntry = "//div[contains(@onclick, \"editTimeEntry('{{id}}')\")][{{statusQuery}}]";
	static final String expenseItem = "//div[contains(@onclick, \"getExpenseItem('{{id}}')\")][{{statusQuery}}]";

	static final String addTimeEntryPopup = "//div[@id='AddEntryPopup']";
	static final String newTimeEntryDialogSave = addTimeEntryPopup + "//input[@id='saveNewEntryBtn']";
	static final String newTimeEntryDialogCancel = addTimeEntryPopup + "//input[@value='Cancel']";
	
	static final String editTimeEntryPopup = "//div[@id='EditTimeEntryPopup']";
	static final String timeEntryDialogSave = editTimeEntryPopup + "//input[@id='saveTimeEntryBtn']";
	static final String timeEntryDialogCancel = editTimeEntryPopup + "//input[@value='Cancel']";
	static final By anyActivityOption = By.xpath("//select[@class='ResourceActivityName AllTimeFields']/option[@value!='']");
	static final By plannedActivity = By.xpath(editTimeEntryPopup + "//th[text()='Planned Activity']/following-sibling::td/span");
	static final By timeEntryDay = By.xpath(editTimeEntryPopup + "//th[text()='Day']/following-sibling::td");
	static final By timeTypeInput = By.xpath(editTimeEntryPopup + "//select[contains(@class, ' PeriodRateBandSelect')]");
	static final By timeTypeText = By.xpath(editTimeEntryPopup + "//label[text()='Time Type']/parent::th/following-sibling::td");
	static final By entryHoursInput = By.xpath(editTimeEntryPopup + "//input[normalize-space(@class)='EntryUnitsText entry-units persist-time-field']");
	static final By entryHoursText = By.xpath(editTimeEntryPopup + "//div[text()='Entry Hours']/parent::th/following-sibling::td/span[2]");
	static final By notesInput = By.xpath(editTimeEntryPopup + "//textarea[@class='NoteText persist-time-field']");
	static final By notesText = By.xpath(editTimeEntryPopup + "//label[text()='Notes']/parent::th/following-sibling::td/span[2]");
	
	static final String expenseItemPopup = "//div[@id='ExpenseItemPopup']";
	static final String expenseItemDialogCancel = expenseItemPopup + "//input[@value='Cancel']";
	static final By expenseClaim = By.xpath(expenseItemPopup + "//th[text()='Expense Claim']/following-sibling::td/span/a");
	static final By expenseItemPlannedActivity = By.xpath(expenseItemPopup + "//label[text()='Planned Activity']/parent::th/following-sibling::td/span");
	static final By expenseCategory = By.xpath(expenseItemPopup + "//label[text()='Category']/parent::th/following-sibling::td/span");
	static final By expenseItemDay = By.xpath(expenseItemPopup + "//th[text()='Day']/following-sibling::td");
	static final By incurredAmount = By.xpath(expenseItemPopup + "//label[text()='Incurred Amount']/parent::th/following-sibling::td");
	static final By expenseAmount = By.xpath(expenseItemPopup + "//span[text()='Amount']/parent::th/following-sibling::td/span");
	static final By expenseNotes = By.xpath(expenseItemPopup + "//th[text()='Notes']/following-sibling::td/span");
	
	static final By adjustTimeEntriesButton = By.xpath("//input[normalize-space(@value)='Adjust Time Entries']");
	static final By saveAndSubmitButton = By.xpath("//input[normalize-space(@value)='Save and Submit']");
	static final By loadingIndicator = By.cssSelector(".loadingIndicator");
	
	static final By hoursReEntry = By.cssSelector("input[class$=' EntryUnitsText entry-units persist-time-field']");
	
	static final By saveTimeEntryBtn = By.id("saveTimeEntryBtn");
	static final By submitTimeEntryBtn = By.xpath("//input[@id='j_id0:j_id1:TimeForm:submitTimeEntriesBtn']");
	static final By timesheetReadyForApproval = By.xpath("//div[contains(text(),'ReadyForApproval')]");
	
	static final By timesheetReadyForApprovalImg = By.xpath("//img[contains(@src, 'Time_ReadyForApproval.png')]");
	static final By timesheetApprovedImg = By.xpath("//img[contains(@src, 'Time_Approved.png')]");
	
	
	public static final By timeEntryPeriodHeader = By.cssSelector("div.TimeEntryPeriodHeader");
	
	// templated selectors
	static final String activitySpan = "//span[normalize-space(text())=\"{{activity}}\"]";
	static final String activityRow = activitySpan + "/../parent::tr";
	static final String activityTimesheetRow = "//tr[@activity-id='{{activityId}}'][contains(@class, timesheet-row)]";
	static final String activityDayInput = activityTimesheetRow + "//input[@entry-date='{{date}}']";
	static final String activityDayInputEnabled = activityTimesheetRow + "//input[@entry-date='{{date}}'][not(@readonly)]";
	static final String activityDayEditButton = activityDayInput + "/preceding-sibling::span[contains(@class, 'edit-entry')]";
	static final String startTimeInput = "//input[@activity-id='{{activityId}}'][@entry-date='{{date}}'][contains(@class, 'start-time')][not(@readonly)]";
	static final String endTimeInput = "//input[@activity-id='{{activityId}}'][@entry-date='{{date}}'][contains(@class, 'end-time')][not(@readonly)]";
	
	
//	static final String datePickerDay = "//td[@data-handler='selectDay']/a[text()='{{dayNo}}']";

	List<WebElement> thePeriodLabels;
	List<WebElement> theTimeEntryLinks;
	List<TimesheetDayC> theTimesheetDays;
	
	WebElement getDatePickerInput() {
		return theSH.getWebElement(By.cssSelector(INPUT_CLASS$_HAS_DATEPICKER));
	}
		
	WebElement getTimeEntryEndDateText() {
		return theSH.getWebElement(By.cssSelector("input[id$='entryEndDate']"));
	}
		
	WebElement getTaskSelect() {
		return theSH.getWebElement(By.cssSelector("select[id$='planTaskSelect']"));
	}
		
	
	public TimesheetPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	private void initialise(Timesheet timesheetDetails) {
		setTimePeriod(timesheetDetails.trackingPeriodStartDate);
		theSH.waitForLightningSpinnerToBeHidden();		
		refreshLinkMap();
	}

	private void refreshLinkMap() {
		theSH.waitForElementToBeClickableWithRefreshRetry(By.cssSelector(A_ONCLICK_NEW_TIME_ENTRY), 10);
		
		// construct a list of webelements, each entry being a column on the timesheet view
		thePeriodLabels = theSH.getWebElements(By.cssSelector(DIV_CLASS_TIME_ENTRY_PERIOD_HEADER));
		// also construct a matching list of webelements, each entry being the link to the Time and Expense Entry screen for that day
		theTimeEntryLinks = theSH.getWebElements(By.cssSelector(A_ONCLICK_NEW_TIME_ENTRY));
		
		theTimesheetDays = new ArrayList();
			
		assembleDayLinkMap();
	}
	
	private void assembleDayLinkMap() {
		// construct a list of timesheet days, each with a name and a link to the timesheet Time Entry Link
		Iterator<WebElement> itPeriodLabels = thePeriodLabels.iterator();
		Iterator<WebElement> itTimeEntryLinks = theTimeEntryLinks.iterator();
		
		while (itPeriodLabels.hasNext() && itTimeEntryLinks.hasNext()) {
			theTimesheetDays.add(new TimesheetDayC(itPeriodLabels.next().getText(), itTimeEntryLinks.next()));
		}
	}
	
	public static void navigateToAdjustTimeEntries(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			if(exists(sh, saveAndSubmitButton, 3))
				return;
			clickAndWaitSequence(sh, 3, 
			/* click adjust time entries			*/	adjustTimeEntriesButton, 
			/* wait for save and submit button		*/	saveAndSubmitButton
			);
		});
	}
	
	public static String getActivityId(SeleniumHelper sh, String activity) {
		By activityRowSelector = By.xpath(activityRow.replace("{{activity}}", activity));
		waitClickable(sh, activityRowSelector, 20);
		return sh.getWebElement(activityRowSelector).getAttribute("id");
	}
	
	public static void adjustTimeEntries(SeleniumHelper sh, Collection<TimeEntryAdjustment> adjustments) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			for(TimeEntryAdjustment a : adjustments) {
				String activityId = getActivityId(sh, a.plannedActivity);
				String date;
				// convert date string
				try { date = pageDateFormat.format(MobileRequests.automationDateFormatter.parse(a.day));
				} catch (Exception e) { throw new RuntimeException("failed to convert the automation date format to page date format"); }
				By editButton = By.xpath(activityDayEditButton.replace("{{activityId}}", activityId).replace("{{date}}", date));
				By enabledDayInput = By.xpath(activityDayInputEnabled.replace("{{activityId}}", activityId).replace("{{date}}", date));
				By startTimeInputSelector = By.xpath(startTimeInput.replace("{{activityId}}", activityId).replace("{{date}}", date));
				By endTimeInputSelector = By.xpath(endTimeInput.replace("{{activityId}}", activityId).replace("{{date}}", date));
				
				clickAndWaitSequence(sh, 20, 
				/* click adjust day input				*/	editButton, 
				/* click the enabled day input			*/	enabledDayInput,
				/* wait for start time input			*/	startTimeInputSelector
				);
				clearAndInputText(sh, startTimeInputSelector, a.startTime);
				clearAndInputText(sh, endTimeInputSelector, a.endTime);
			}
		});
	}
	
	public static void saveAndSubmitAdjustments(SeleniumHelper sh) {
		waitClickable(sh, saveAndSubmitButton, 20);
		click(sh, saveAndSubmitButton);
		waitClickable(sh, loadingIndicator, 20);
		waitHidden(sh, loadingIndicator, 20);
	}
	
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		c.set(2013, 0, 28);
		System.out.println(pageDateFormat.format(c.getTime()));
	}
	
	public static void navigateToDay(SeleniumHelper sh, String dateStr) {
		sh.waitForLightningSpinnerToBeHidden();
		JQueryDatePickerC.navigateToDay(sh, datePickerButton, dateStr);
	}
	
	public void navigateToPreviousWeek() {
		theSH.clickAndWaitSequenceWithRetry(5, previousWeek, datePickerButton);
	}
	
	public void navigateToNextWeek() {
		theSH.clickAndWaitSequenceWithRetry(5, nextWeek, datePickerButton);
	}
	
	public void openNewTimeEntryDialog(String dateStr) {
		try {
			String dayTitle = theSH.getDateLabelForDate(dateStr, theSH.DAYNUMBERFORMAT);
			theSH.clickAndWaitSequenceWithRefreshRetry(5, By.xpath(timeEntryDialogButton.replace("{{title}}", dayTitle)), By.cssSelector(SELECT_ID$_PLANNED_ACTIVITY_SELECT));
		} catch(Exception e) {
			throw new RuntimeException("Failed to open time entry dialog", e);
		}
	}
	
	public void openEditTimeEntryDialog(TestState state, String localId, String... statuses) {
		try {
			theSH.clickAndWaitSequenceWithRefreshRetry(5, By.xpath(editTimeEntry
					.replace("{{id}}", MobileRequests.getRemoteId(state, localId))
					.replace("{{statusQuery}}", createStatusXpathQuery(statuses))), By.xpath(timeEntryDialogCancel));
		} catch(Exception e) {
			throw new RuntimeException("Failed to open time entry dialog", e);
		}
	}
	
	public static String createStatusXpathQuery(String... statuses) {
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < statuses.length; i++)
			sb.append(i > 0 ? " or " : "").append("contains(@class, '").append(statuses[i]).append("')");
		return sb.toString();
	}
	
	public void openExpenseItemDialog(String remoteId, String... statuses) {
		try {
			theSH.clickAndWaitSequenceWithRefreshRetry(5, By.xpath(expenseItem.replace("{{id}}", remoteId).replace("{{statusQuery}}", createStatusXpathQuery(statuses))), By.xpath(expenseItemDialogCancel));
		} catch(Exception e) {
			throw new RuntimeException("Failed to open time entry dialog", e);
		}
	}
	
	public void closeNewTimeEntryDialog() {
		theSH.clickAndWaitSequenceWithRetry(5, By.xpath(newTimeEntryDialogCancel), logoImage);
	}
	
	public void closeEditTimeEntryDialog() {
		theSH.clickAndWaitSequenceWithRetry(5, By.xpath(timeEntryDialogCancel), logoImage);
	}
	
	public void closeExpenseItemDialog() {
		theSH.clickAndWaitSequenceWithRetry(5, By.xpath(expenseItemDialogCancel), logoImage);
	}
	
	public void validateMobileTimeEntries(TestState state, TimeEntry... entries) {
		try {
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			Map<Date, TimeEntry> orderedMap = new TreeMap();
			for(TimeEntry e : entries)
				orderedMap.put(f.parse(e.startDate), e);
			
			Calendar c = Calendar.getInstance();
			int daysNavigated = 0;
			int weeksNavigated = 0;
			for(Date d : orderedMap.keySet()) {
				c.setTime(d);
				if(daysNavigated > 0 && c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
					navigateToNextWeek();
					weeksNavigated++;
				}
				TimeEntry e = orderedMap.get(d);
				openAndValidateMobileTimeEntry(state, e);
				daysNavigated++;
			}
			theSH.clickAndRepeat(5, previousWeek, weeksNavigated);
		}catch(Exception e) {
			throw new RuntimeException("failed to validate time entries", e);
		}
	}
	
	class ExpenseItemPair {
		ExpenseDetail automation;
		ExpenseItemMob mobile;
	}
	
	public void validateMobileExpenseClaim(TestState state, ExpenseEntry ee, ExpenseClaimMob ecm) {
		validateMobileExpenseItems(state, ee, ecm.getExpenseItems().toArray(new ExpenseItemMob[ecm.getExpenseItems().size()]));
	}
	
	public void validateMobileExpenseItems(TestState state, ExpenseEntry ee, ExpenseItemMob... itemsObs) {
		try {
			Map<Date, ExpenseItemPair> orderedMap = new TreeMap();
			for(ExpenseDetail i : ee.expenseDetails) {
				String remoteId = MobileRequests.getRemoteId(state, i.localId);
				ExpenseItemPair pair = new ExpenseItemPair();
				pair.automation = i;
				for(ExpenseItemMob eim : itemsObs)
					if(eim.getId().equals(remoteId))
						pair.mobile = eim;
				orderedMap.put(MobileRequests.automationDateFormatter.parse(i.incurredDate), pair);
			}
			
			Calendar c = Calendar.getInstance();
			int daysNavigated = 0;
			int weeksNavigated = 0;
			for(Date d : orderedMap.keySet()) {
				c.setTime(d);
				if(daysNavigated > 0 && c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
					navigateToNextWeek();
					weeksNavigated++;
				}
				ExpenseItemPair pair = orderedMap.get(d);
				openAndValidateMobileExpenseItem(state, ee, pair.automation, pair.mobile.getId());
				daysNavigated++;
			}
			theSH.clickAndRepeat(5, previousWeek, weeksNavigated);
		}catch(Exception e) {
			throw new RuntimeException("failed to validate expense items", e);
		}
	}
	
	public void openAndValidateMobileTimeEntry(TestState state, TimeEntry entry) {
		openEditTimeEntryDialog(state, entry.localId, entry.statuses);
		try {
			validateMobileTimeEntry(entry);
		} catch(Exception e) {
			throw new RuntimeException("Failed to validate the time entry with local id: " + entry.localId);
		}
		closeEditTimeEntryDialog();
	}
	
	public void validateMobileTimeEntry(TimeEntry expected) throws Exception {
		By timeTypeSel = timeTypeInput;
		By entryHourSel = entryHoursInput;
		By notesSel = notesInput;
		for(String status : expected.statuses) {
			if(TimeEntryMob.Status.Submitted.name().equals(status) || TimeEntryMob.Status.Approved.name().equals(status)) {
				timeTypeSel = timeTypeText;
				entryHourSel = entryHoursText;
				notesSel = notesText;
				break;
			}
		}
		
		if(expected.plannedActivity != null) theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(plannedActivity), expected.plannedActivity, "The planned activity is incorrect on time entry with local id: " + expected.localId);
		if(expected.startDate != null) theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(timeEntryDay), theSH.getDateLabelForDate(expected.startDate, theSH.DAYNUMBERFORMAT), "The start date is incorrect on time entry with local id: " + expected.localId);
		if(expected.rateBand != null) theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(timeTypeSel), expected.rateBand, "The rate band is incorrect on time entry with local id: " + expected.localId);
		if(expected.entryUnits != null) theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(entryHourSel), Double.parseDouble(expected.entryUnits), "The entry units are incorrect on time entry with local id: " + expected.localId);
		if(expected.notes != null) theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(notesSel), expected.notes, "The notes are incorrect on time entry with local id: " + expected.localId);
	}
	
	public void openAndValidateMobileExpenseItem(TestState state, ExpenseEntry expectedClaim, ExpenseDetail item, String remoteId) {
		openExpenseItemDialog(remoteId, expectedClaim.statuses);
		try {
			validateMobileExpenseItem(expectedClaim, item);
		} catch(Exception e) {
			throw new RuntimeException("Failed to validate the time entry with local id: " + item.localId, e);
		}
		closeExpenseItemDialog();
	}
	
	public void validateMobileExpenseItem(ExpenseEntry expectedClaim, ExpenseDetail expectedItem) throws Exception {
		String targetDayLabel = theSH.getDateLabelForDate(MobileRequests.automationDateFormatter.parse(expectedItem.incurredDate), theSH.DAYNUMBERFORMAT);
		
		if(expectedClaim.name != null) theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(expenseClaim), expectedClaim.name, "The claim is incorrect on the time entry with local id: " + expectedItem.localId);
		if(expectedClaim.activityName != null) theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(expenseItemPlannedActivity), expectedClaim.activityName, "The activity is incorrect on the time entry with local id: " + expectedItem.localId);
		if(expectedItem.category != null) theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(expenseCategory), expectedItem.category, "The expense category is incorrect on the time entry with local id: " + expectedItem.localId);
		if(expectedItem.incurredDate != null) theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(expenseItemDay), targetDayLabel, "The incurred date is incorrect on the time entry with local id: " + expectedItem.localId);
//		theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(incurredAmount), expectedItem.getIncurredAmount(), "The incurred amount is incorrect on the time entry with local id: " + expectedItem.localId);
		if(expectedItem.incurredGrossAmount != null) theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(expenseAmount), Double.parseDouble(expectedItem.incurredGrossAmount), "The expense incurred gross amount is incorrect on the time entry with local id: " + expectedItem.localId);
		if(expectedItem.notes != null) theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(expenseNotes), expectedItem.notes, "The notes are incorrect on the time entry with local id: " + expectedItem.localId);
	}
	
	public void validateActivityCountForPeriod(String startDateStr, String endDateStr, int expected) {
		try {
			theSH.waitForLightningSpinnerToBeHidden();
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			Date curDate = f.parse(startDateStr);
			Date endDate = f.parse(endDateStr);
			Calendar c = Calendar.getInstance();
			c.setTime(curDate);
			
			if(endDate.before(curDate))
				throw new Exception("The end date must be the same or after the start date");
			int weeksNavigated = 0;
			while(curDate.compareTo(endDate) <= 0) {
				openNewTimeEntryDialog(f.format(curDate));
				theSH.waitForLightningSpinnerToBeHidden();
				validateActivityCount(expected);
				
				if(theSH.isLightning()){
					click(theSH, By.xpath(newTimeEntryDialogCancel));
					waitClickable(theSH, calendarView, 10);
				}
				else
					closeNewTimeEntryDialog();
				
				c.add(Calendar.HOUR, 24); 
				curDate = c.getTime();
				if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
					navigateToNextWeek();
					weeksNavigated++;
				}
			}
			theSH.clickAndRepeat(5, previousWeek, weeksNavigated);
		}catch(Exception e) {
			throw new RuntimeException("failed to validate activity counts", e);
		}
	}
	
//	public void validateActivityCountForPeriod(String startDateStr, String endDateStr, int expected) {
//		try {
//			ArrayList<Date> dates = new ArrayList();
//			
//			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
//			Date curDate = f.parse(startDateStr);
//			Date endDate = f.parse(endDateStr);
//			Calendar c = Calendar.getInstance();
//			c.setTime(curDate);
//			
//			if(endDate.before(curDate))
//				throw new Exception("The end date must be the same or after the start date");
//			while(curDate.compareTo(endDate) <= 0) {
//				dates.add(curDate);
//				c.add(Calendar.HOUR, 24); 
//				curDate = c.getTime();
//			}
//			validateActivityCountForPeriod(dates.toArray(new Date[dates.size()]), expected);
//		}catch(Exception e) {
//			throw new RuntimeException("failed to validate activity counts", e);
//		}
//	}
//	
//	public void validateActivityCountForPeriod(Date[] dates, int expected) {
//		try {
//			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
//			Calendar c = Calendar.getInstance();
//			
//			int weeksNavigated = 0;
//			for(Date curDate : dates) {
//				c.setTime(curDate);
//				openNewTimeEntryDialog(f.format(curDate));
//				validateActivityCount(expected);
//				closeNewTimeEntryDialog();
//				if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
//					navigateToNextWeek();
//					weeksNavigated++;
//				}
//			}
//			theSH.clickAndRepeat(5, previousWeek, weeksNavigated);
//		}catch(Exception e) {
//			throw new RuntimeException("failed to validate activity counts", e);
//		}
//	}
	
	public void validateActivityCount(int expected) {
		theSH.assertEquals(getActivityCount(), expected, "The activity count in the time entry dialog is incorrect");
	}
	
	public int getActivityCount() {
		return theSH.getWebElements(anyActivityOption).size();
	}
	
	public void validateNoDraftTimeEntries(Timesheet timesheetDetails) throws ParseException {
		theSH.waitForLightningSpinnerToBeHidden();
		initialise(timesheetDetails);
		theSH.waitForElementVisible(By.xpath("//input[@class='actualise-time-checkbox']"), 15);
	}
	
	public void enterTime(Timesheet timesheetDetails) throws ParseException {
		theSH.waitForLightningSpinnerToBeHidden();
		
		initialise(timesheetDetails);
		
		for (TimeEntry timeEntry : timesheetDetails.timeEntries) {
			// time can be entered manually or an existing forecast/scheduled day actualised
			// during automation of demo data there is a shortcut to actualise all forecast time in this
			// period for the activity
			if(timeEntry.actualiseAllForecastEntries)
			{
				actualiseAllTimeForActivity(timeEntry);
			}
			else if(timeEntry.actualiseForecastEntry)
			{
				actualiseTimeForDay(timeEntry);
			}
			else
			{
				enterTimeForDay(timeEntry);				
			}
			
			// after entering time the screen will have been refreshed, we must reconstruct the link map
			refreshLinkMap();
		}
				
		submitTimeEntries();
	}

	public void enterTimeEnhanced(Timesheet timesheetDetails) throws ParseException {
		theSH.waitForLightningSpinnerToBeHidden();		
		initialise(timesheetDetails);	
		waitClickable(theSH, TimesheetPageC.timeEntryPeriodHeader, 20);	
		String EnhancedURL = theSH.getCurrentUrl() + "&ev=1";
		
		boolean SubmitAll = true;

		theSH.goToUrl(EnhancedURL);		
		By TimesheetTipsLink = By.xpath("//a[normalize-space(text())='Timesheet Tips']");		
		theSH.waitForElementToBePresent(TimesheetTipsLink, 10);
		
		for (TimeEntry timeEntry : timesheetDetails.timeEntries) {
		
			if(timeEntry.actualiseAllForecastEntries)	
			{
				SubmitAll = false;
				//Clicking actual on all check boxes
				actualiseAllTimeForActivityEnhanced(timeEntry);
			}
			else if(timeEntry.actualiseForecastEntry)
			{
				SubmitAll = false;
				//Clicking actual on 1 check box
				actualiseTimeForDayEnhanced(timeEntry);
			}
			
			else if(timeEntry.CreateAllDraftTimeEntries)
			{
				//check all of the actual check boxes			
				String checkboxWebElementsXPath = ACTUALISE_ALL_CHECKBOX_ENHANCED_XPATH.replace(ACTIVITY_NAME, timeEntry.plannedActivity);
				List<WebElement> actualiseCheckboxes = theSH.getWebElements(By.xpath(checkboxWebElementsXPath));
				
				for(WebElement actualiseCheckbox : actualiseCheckboxes)
				{		
					theSH.waitForElementToBeClickable(By.xpath(ACTUALISE_ALL_CHECKBOX_ENHANCED_XPATH.replace(ACTIVITY_NAME, timeEntry.plannedActivity)));
					actualiseCheckbox.click();			
				}
				CreateDraftTimeEntries(timeEntry);
			}
			else if(timeEntry.SelectAll)
			{
				//Click on the Select All button				
				By SubmitItems = By.xpath("//div[@id='fixed-header-container']//div[contains(@t-tip, 'TESelectDropdown')]");
				theSH.hoverOnElement(SubmitItems);
				theSH.getWebElement(By.xpath("//div[@id='fixed-header-container']//li[@t-tip-right='SelectAll']")).click();			
			}
			else if(timeEntry.NoTimeEntrySubmit)
			{
				//Submitting - this is used to navigate to the next time period without entering any more time 	
			}			
			else
			{
				enterTimeForDayEnhanced(timeEntry);			
			}			
		}

		if (SubmitAll) submitTimeEntriesEnhanced(timesheetDetails);
	
	}
	
	
	public void getTrackingPeriodId(Timesheet timesheetDetails) throws ParseException {
		theSH.waitForLightningSpinnerToBeHidden();		
		initialise(timesheetDetails);	
		waitClickable(theSH, TimesheetPageC.timeEntryPeriodHeader, 20);			
		String trackingPerioddId = theSH.getCurrentUrl().split("periodId=")[1];			
		General.timePeriodId = trackingPerioddId;
		
		
	}

	private void submitTimeEntries() {
		// manually find submit button since factory version was throwing a stale element exception
		theSH.getWebElement(By.cssSelector(INPUT_ID$_SUBMIT_TIME_ENTRIES_BTN)).click();
		// wait for the save to return
		theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_SUBMIT_TIME_ENTRIES_BTN), 60);
	}
	
	
	private void submitTimeEntriesEnhanced(Timesheet timesheetDetails) throws ParseException {
			
		try{
			By SubmitVisible = By.xpath("//div[@id='fixed-header-container']//div[@t-tip='TESubmitDropdown']");
			theSH.waitForElementVisible(SubmitVisible, 10);
		} catch(Exception e){
			throw new ParseException("Submit Button not visible", 0);
		}				
		for (TimeEntry timeEntry : timesheetDetails.timeEntries) {
	
			if(timeEntry.timeSubmissionMethodSelected)
			{
				//Submit selected Time Entries on the Enhanced Timesheet view
				By SubmitHover = By.xpath("//div[@id='fixed-header-container']//div[@t-tip='TESubmitDropdown']");
				theSH.hoverOnElement(SubmitHover);
				theSH.clickLink(By.xpath("//div[@id='fixed-header-container']//li[@t-tip-right='SubmitSelected']"));
			}
			else if (timeEntry.timeSubmissionMethodAll)
			{
				By SubmitHover = By.xpath("//div[@id='fixed-header-container']//div[@t-tip='TESubmitDropdown']");
				theSH.waitForElementToBeClickable(SubmitHover);
				theSH.hoverOnElement(SubmitHover);
				theSH.clickLink(By.xpath("//div[@id='fixed-header-container']//li[@t-tip-right='SubmitAll']"));		
			}
		}
		
		By SubmittedPill = By.xpath("//div[normalize-space(text())='SUBMITTED']");
		theSH.waitForElementToBeVisible(SubmittedPill);
	}
		
	private void setTimePeriod(String timePeriod) {
		// force the datePickerInput field to be visible (nasty)
		expandDatePickerInputField();

		getDatePickerInput().click();
		getDatePickerInput().sendKeys(timePeriod);
		getDatePickerInput().sendKeys(Keys.TAB);	// tab out of the field to make the screen refresh
		
		//TODO: replace sleep with something more definitive
		theSH.sleep(200);
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickable(timeEntryPeriodHeader);
	}

	private void expandDatePickerInputField() {
		// wait for menu to load before waiting for the calendar picker to appear
		theSH.waitForElementToBeClickable(theSH.getLinkByPageName(PagesC.TIMESHEET));
		theSH.waitForElementToBeClickable(datePickerButton);
		
		//theSH.waitForElementToBePresent(By.cssSelector(INPUT_CLASS$_HAS_DATEPICKER), 20);
		theSH.executeJavaScript(EXPAND_DATE_ENTRY_FIELD_WIDTH);
		theSH.executeJavaScript(EXPAND_DATE_ENTRY_FIELD_HEIGHT);
	}
	
	private void enterTimeForDay(TimeEntry timeEntry) throws ParseException {
		// depending on the environment the date format on the timesheet grid will be different
		// least impact way is to just iterate through the different ways of formatting until we get a match
		WebElement addTimeEntryLink = null;
		addTimeEntryLink = getTimeEntryLink(timeEntry.startDate, theSH.DAYNUMBERFORMAT);
		
		if(addTimeEntryLink != null) {
			addTimeEntryLink.click();
		}
		else
		{
			addTimeEntryLink = getTimeEntryLink(timeEntry.startDate, theSH.DAYMONTHYEARFORMAT);
			
			if(addTimeEntryLink != null)
			{
				addTimeEntryLink.click();
			}
			else
			{
				throw new ParseException("Unrecognised day label format on timesheet grid", 0);
			}
		}
		
		// popup takes sometime to render so wait for the first selector field to appear (as these take the most time to render)
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(SELECT_ID$_PLANNED_ACTIVITY_SELECT), 20);
		
		theSH.selectByVisibleText(By.cssSelector(SELECT_ID$_PLANNED_ACTIVITY_SELECT), timeEntry.plannedActivity);

		// optionally set the end date if this entry spans multiple days
		if(timeEntry.endDate != null && timeEntry.endDate !="") {
			theSH.waitForElementToBeClickable(By.cssSelector("input[id$='entryEndDate']"));
			getTimeEntryEndDateText().clear();
			getTimeEntryEndDateText().sendKeys(timeEntry.endDate);
			getTimeEntryEndDateText().sendKeys(Keys.TAB);
		}
		
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_ENTRY_UNITS), 20);
		
		if(timeEntry.task != null && timeEntry.task != "") {
			theSH.selectByVisibleText(getTaskSelect(), timeEntry.task);
		}
				
		// overtime
		if(timeEntry.timeType != null) {
			theSH.selectByVisibleText(TIME_TYPE, timeEntry.timeType);
		}
		
		if(timeEntry.startTime != null) {
			waitClickable(theSH, startTimeEntryInput, 20);
			clearAndInputTextIfVisibleAndEnabled(theSH, startTimeEntryInput, timeEntry.startTime);
		}
		if(timeEntry.endTime != null) {
			waitClickable(theSH, endTimeEntryInput, 20);
			clearAndInputTextIfVisibleAndEnabled(theSH, endTimeEntryInput, timeEntry.endTime);
		}
		
		// there is a refresh having selected a task so can't use pagefactory
		waitClickable(theSH, By.cssSelector(INPUT_ID$_ENTRY_UNITS), 20);
		clearAndInputText(theSH, By.cssSelector(INPUT_ID$_ENTRY_UNITS), timeEntry.entryUnits);
		
		// popup takes sometime to render so wait for the first selector field to appear (as these take the most time to render)
		theSH.waitForElementToBeClickableWithRetry(By.xpath(newTimeEntryDialogSave), 20);
		theSH.getWebElement(By.xpath(newTimeEntryDialogSave)).click();
		
		// the screen takes a while to close after clicking save so wait for it to have closed
		theSH.waitForElementToBeHidden(By.id(ADD_TIME_ENTRY_POPUP), 40);
	}
	
	/**
	 * @param timeEntry
	 * @throws ParseException
	 */
	private void enterTimeForDayEnhanced(TimeEntry timeEntry) throws ParseException {

		// Clicks the activity name		
		By TimeEntryBtn = By.xpath("//div[@id='fixed-header-container']//div[contains(@class, 'time-entry-period-header-btn')]");
		
		theSH.clickLinkIfVisible(TimeEntryBtn);	
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(SELECT_ID$_PLANNED_ACTIVITY_SELECT), 20);		
		theSH.selectByVisibleText(By.cssSelector(SELECT_ID$_PLANNED_ACTIVITY_SELECT), timeEntry.plannedActivity);
		String QuickEntryUnits = "//div[normalize-space(text())='[ " +timeEntry.entryUnits + " ]']";		
		theSH.clickLink(By.xpath(QuickEntryUnits));
		
		if(timeEntry.endDate != null && timeEntry.endDate !="") {
			theSH.waitForElementToBeClickable(By.cssSelector("input[id$='entryEndDate']"));
			getTimeEntryEndDateText().clear();
			getTimeEntryEndDateText().sendKeys(timeEntry.endDate);
			getTimeEntryEndDateText().sendKeys(Keys.TAB);
		}
					
		theSH.getWebElement(By.xpath("//input[@id='saveNewEntryBtn']")).click();	
	}
		
	private WebElement getTimeEntryLink(String targetDate, String targetFormat) throws ParseException {
		WebElement addTimeEntryLink = null;

		String targetDayLabel = theSH.getDateLabelForDate(targetDate, targetFormat);

		for (TimesheetDayC theTimesheetDay : theTimesheetDays) {
			if(theTimesheetDay.theDayName.equals(targetDayLabel)) {
				addTimeEntryLink = theTimesheetDay.theAddTimeEntryLink;
				break;
			}
		}
		
		return addTimeEntryLink;
	}
	
	private void actualiseTimeForDay(TimeEntry theTimeEntry) throws ParseException
	{
		// construct the link to the checkbox then click it
		String checkboxWebElementXPath = ACTUALISE_CHECKBOX_XPATH.replace(DATE_TOKEN, theSH.getDateLabelForDate(theTimeEntry.startDate, theSH.DAYNUMBERFORMAT)).replace(ACTIVITY_NAME, theTimeEntry.plannedActivity);
		WebElement actualiseCheckbox = theSH.getWebElement(By.xpath(checkboxWebElementXPath));
		actualiseCheckbox.click();
	}

	private void actualiseTimeForDayEnhanced(TimeEntry theTimeEntry) throws ParseException
	{
		// clicking the actual checkbox on 1 Time Entry
			
		List<WebElement> TodayDate = theSH.getWebElements(By.xpath("//div[@id='fixed-header-container']//div[@class='time-entry-period-name']"));	
	    int counter = 1;  
	     
		for (WebElement TDay : TodayDate)
		{
			counter++;			
			// looping to match the day based on the Start date  e.g "Tue 1"
			if (TDay.getText().contains(theTimeEntry.startDate))
			{
				break;
			}	
		}
		
		WebElement actualiseCheckBox = theSH.getWebElement(By.xpath("//div[@class='with-menu']/*/*[" + counter + "]//input[@type='checkbox']"));
		actualiseCheckBox.click();	
		By SubmitHover = By.xpath("//div[@id='fixed-header-container']//div[contains(@t-tip, 'TESubmitDropdown')]");
		theSH.hoverOnElement(SubmitHover);	
		theSH.clickLink(By.xpath("//div[@id='fixed-header-container']//li[@t-tip-right='SubmitSelected']"));
		By SubmittedPill = By.xpath("//div[normalize-space(text())='SUBMITTED']");
		theSH.waitForElementToBeVisible(SubmittedPill);
	}

	private void CreateDraftTimeEntries(TimeEntry theTimeEntry) throws ParseException
	{
		// clicking the Create Draft Time Entries button
					
		By DraftEntries = By.xpath("//div[@id='fixed-header-container']//div[@class='actualise-btn toolbar-button']");
		
		theSH.waitForElementToBeClickable(DraftEntries);
		
		theSH.getWebElement(DraftEntries).click();
	
	}
	
	
	private void actualiseAllTimeForActivity(TimeEntry theTimeEntry) throws ParseException
	{
		// construct the link to the checkbox then click it
		String checkboxWebElementsXPath = ACTUALISE_ALL_CHECKBOX_XPATH.replace(ACTIVITY_NAME, theTimeEntry.plannedActivity);
		List<WebElement> actualiseCheckboxes = theSH.getWebElements(By.xpath(checkboxWebElementsXPath));
		
		for(WebElement actualiseCheckbox : actualiseCheckboxes)
		{
			actualiseCheckbox.click();
		}
	}
	
	private void actualiseAllTimeForActivityEnhanced(TimeEntry theTimeEntry) throws ParseException
	{
		// checking all of the Actual check boxes in a Period
		String checkboxWebElementsXPath = ACTUALISE_ALL_CHECKBOX_ENHANCED_XPATH.replace(ACTIVITY_NAME, theTimeEntry.plannedActivity);
		List<WebElement> actualiseCheckboxes = theSH.getWebElements(By.xpath(checkboxWebElementsXPath));
		
		for(WebElement actualiseCheckbox : actualiseCheckboxes)
		{		
			theSH.waitForElementToBeClickable(By.xpath(ACTUALISE_ALL_CHECKBOX_ENHANCED_XPATH.replace(ACTIVITY_NAME, theTimeEntry.plannedActivity)));
			actualiseCheckbox.click();			
		}
		theSH.waitMilliseconds(4000);
		By SubmitHover = By.xpath("//div[@id='fixed-header-container']//div[contains(@t-tip, 'TESubmitDropdown')]");		
		theSH.hoverOnElement(SubmitHover);
		
		theSH.clickLink(By.xpath("//div[@id='fixed-header-container']//li[@t-tip-right='SubmitSelected']"));		
		By SubmittedPill = By.xpath("//div[normalize-space(text())='SUBMITTED']");
		theSH.waitForElementToBeVisible(SubmittedPill);
		
	}
	
	
	public static void navigateToTimesheetAndReject(SeleniumHelper sh, Timesheet ts){
		sh.clickLink(By.name("go"));
		String timesheetId = sh.getWD().findElement(timesheetReadyForApproval).getAttribute("id").split("\\_")[0];		
		sh.waitForElementToBeClickable(By.linkText(ts.resourceName));
		sh.clickLink(By.linkText(ts.resourceName));
		String url = sh.getCurrentUrl().split("\\ResourceHome")[0].replace("apex/", "apex/TimesheetApprove?id=" + timesheetId + "&sfdc.override=1");
		sh.goToUrl(url);
		sh.waitForElementToBeClickable(By.linkText("Approve / Reject"), 40);
		sh.clickAndWait(By.linkText("Approve / Reject"), By.name("Reject"), 5);
		sh.clickLink(By.name("Reject"));
	}
	
	public static void navigateToTimesheetAndApprove(SeleniumHelper sh, Timesheet ts){
		sh.clickLink(By.name("go"));
		String timesheetId = sh.getWD().findElement(timesheetReadyForApproval).getAttribute("id").split("\\_")[0];		
		sh.clickLink(By.linkText(ts.resourceName));
		String url = sh.getCurrentUrl().split("\\ResourceHome")[0].replace("apex/", "apex/TimesheetApprove?id=" + timesheetId + "&sfdc.override=1");
		sh.goToUrl(url);
		sh.clickAndWait(By.linkText("Approve / Reject"), By.name("goNext"), 5);
		sh.clickLink(By.name("goNext"));
	}
	
	public  void editAndSubmitRejectedTimesheet(SeleniumHelper sh, Timesheet ts, TimeEntry te) throws ParseException {
		initialise(ts);
		sh.getWD().findElement(By.xpath("//div[contains(@title,'Rejected')]")).click();
		sh.waitForElementToBeClickable(hoursReEntry);
		
		sh.getWD().findElement(hoursReEntry).clear();
		clearAndInputTextIfVisibleAndEnabled(sh, hoursReEntry, te.entryUnits);
		
		sh.clickLink(saveTimeEntryBtn);
		sh.waitForBoxyToBeHidden();
		sh.waitForElementToBeClickable(submitTimeEntryBtn);
		sh.clickAndWait(submitTimeEntryBtn, submitTimeEntryBtn, 10);

	}
	
	public static void verifyApproval(SeleniumHelper sh, Timesheet ts){
		sh.sleep(5000);
		sh.refreshBrowser();
		sh.switchToClassicIframeContext();
		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.switchToClassicIframeContext();
			sh.waitForElementToBeClickable(submitTimeEntryBtn);
			sh.waitForElementToBeHidden(timesheetReadyForApprovalImg, 15);
			sh.waitForElementToBeClickable(submitTimeEntryBtn);
			sh.waitForElementToBeVisible(timesheetApprovedImg);
		});
	}
	
}
