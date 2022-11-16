package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.General.getDateParts;
import static kimble.automation.helpers.General.getDifferenceInMonths;
import static kimble.automation.helpers.General.getMonthNumber;
import static kimble.automation.helpers.General.newDateParts;
import static kimble.automation.helpers.SequenceActions.*;

import java.util.Date;

import kimble.automation.helpers.MobileRequests;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.General.DateParts;

import org.openqa.selenium.By;

public class JQueryDatePickerC {

	static final By 
	datePickerMonth = By.cssSelector(".ui-datepicker-month"),
	datePickerYear = By.cssSelector(".ui-datepicker-year"),
	datePickerToday = By.cssSelector(".ui-state-default.ui-state-highlight"),
	prevMonth = By.cssSelector(".ui-icon.ui-icon-circle-triangle-w"),
	nextMonth = By.cssSelector(".ui-icon.ui-icon-circle-triangle-e"),
	
	logoImage = By.cssSelector("#phHeaderLogoImage"),
	calendarView = By.linkText("Calendar");
	
	// templated selector to select a day in the jquery datepicker
	static final String 
	datePickerDay = "//td[@data-handler='selectDay']/a[not(contains(@class, 'ui-priority-secondary'))][text()='{{dayNo}}']";

	public static void navigateToDay(SeleniumHelper sh, By datePicker, Date date) {
		String dateStr = MobileRequests.automationDateFormatter.format(date);
		navigateToDay(sh, datePicker, dateStr);
	}
	
	public static void navigateToDay(SeleniumHelper sh, By datePicker, String dateStr) {
		if(dateStr == null)
			return;
		executeSequenceWithRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			if(sh.isLightning()){
				waitClickable(sh, datePicker, 20);
				click(sh, datePicker);
				sh.waitForLightningSpinnerToBeHidden();
				waitClickable(sh, datePickerYear, 20);
			}
			else{
				clickAndWaitSequence(sh, 20, 
				/* click to open date picker						*/	datePicker, 
				/* wait for the date picker to open					*/	datePickerYear
				);				
			}

			navigateToDayInOpenPicker(sh, dateStr);
			sh.waitForLightningSpinnerToBeHidden();
			
			if(sh.isLightning()){
				waitClickable(sh, calendarView, 20);
				click(sh, calendarView);
				sh.waitForLightningSpinnerToBeHidden();
				waitClickable(sh, datePicker, 20);
			}
			else{
				clickAndWaitSequence(sh, 20, 
				/* click logo to close the date picker				*/	logoImage, 
				/* wait for the date picker button to appear		*/	datePicker
				);
			}
		});
	}
	
	public static void navigateToDayInOpenPicker(SeleniumHelper sh, String dateStr) {
		DateParts fromD = getCurrentDateInOpenPicker(sh);
		DateParts toD = getDateParts(dateStr);
		navigateMonthsInOpenPicker(sh, getDifferenceInMonths(fromD, toD));
		By targetDay = By.xpath(datePickerDay.replace("{{dayNo}}", "" + toD.getDay()));
		click(sh, targetDay);
		if(!sh.isLightning()){
			sh.waitForPageLoadComplete(10);
			waitClickable(sh, logoImage, 20);	
		}
		else
			sh.waitForLightningSpinnerToBeHidden();		

	}
	
	public static DateParts getCurrentDateInOpenPicker(SeleniumHelper sh) {
		int d = sh.getWebElementNumberOrNullWithRetry(datePickerToday).intValue();
		String monthName = sh.getWebElementTextOrNullWithRetry(datePickerMonth);
		int m = getMonthNumber(monthName);
		int y = sh.getWebElementNumberOrNullWithRetry(datePickerYear).intValue();
		return newDateParts(d, m, y);
	}
	
	public static void navigateMonthsInOpenPicker(SeleniumHelper sh, int monthDiff) {
		if(monthDiff == 0)
			return;
		sh.clickAndRepeat(0, monthDiff < 0 ? prevMonth : nextMonth, Math.abs(monthDiff));
	}
	
}
