package kimble.automation.kimbleobjects.classic;

import java.text.ParseException;
import java.util.List;

import kimble.automation.domain.ScheduledDay;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AssignmentCalendarPageC extends BasePageC {
	
	static final String calendarMonth = "//div[@class='calendarMonth'][not(contains(@style, 'display: none'))]";

	static final By previousPeriodButton = By.xpath(calendarMonth + "//div[@id='previousPeriodBtn']");

	static final By currentPeriodDisplayName = By.xpath(calendarMonth + "//div[@id='currentPeriodDisplayName']");

	private static final String ACTIVITY_ASSIGNMENT_CALENDAR = "ActivityAssignmentCalendar";

	private static final String DAYNUMTOKEN = "<DAYNUMTOKEN>";
	private static final String DAY_CLICKABLE_AREA_XPATH = calendarMonth + "//div[.='" + DAYNUMTOKEN + "']/following-sibling::div[2]";
	
	@FindBy(id="btnSaveCalendar")
	private WebElement saveCalenderBtn;

	@FindBy(id="specificToggle")
	private WebElement specificUsageRadio;
	
	@FindBy(css = "select[id$='usageForecastFormat']")
	private WebElement usageForecastFormatSelect;
	
	public AssignmentCalendarPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}

	public void ScheduleDays(List<ScheduledDay> scheduledDays, String resourceName) throws ParseException {
		for (ScheduledDay scheduledDay : scheduledDays) {
			// first force any other scheduled days to be hidden (there is no onscreen way of doing this
			// so instead manipulate the class display style
			// this is only required since selenium can't click the normal day div since the other day divs are in the way
			// a problem that doesn't occur for normal users
			theSH.hideAllElementsByClassName("other-assignments");

			specificUsageRadio.click();
			theSH.selectByVisibleText(usageForecastFormatSelect, scheduledDay.numberOfHours);
			
			// navigate to the correct month
			navigateToPeriod(scheduledDay.date);
			
			String clickableAreaXPath = DAY_CLICKABLE_AREA_XPATH.replace(DAYNUMTOKEN, theSH.getDateLabelForDate(scheduledDay.date, theSH.DAYNATURALNUMBERFORMAT));
			// if multiple months then we'd get more than one returned day element (as the same day number exists in each month)
			// find and click the visible one
			theSH.getVisibleWebElement(By.xpath(clickableAreaXPath)).click();
		}
		saveCalenderBtn.click();
		
		// the screen takes a while to close after clicking save so wait for it to have closed
		theSH.waitForElementToBeHiddenCss("#" + ACTIVITY_ASSIGNMENT_CALENDAR, 30);
		theSH.sleep(5000, "a job isn't getting seeded without a wait here");
		ActivityAssignmentPageC.showAssignmentFor(theSH, resourceName);
	}
	
	public void ScheduleOtherDays(List<ScheduledDay> scheduledDays, String resourceName, String parentResourceName) throws ParseException {
		for (ScheduledDay scheduledDay : scheduledDays) {
			// first force any other scheduled days to be hidden (there is no onscreen way of doing this
			// so instead manipulate the class display style
			// this is only required since selenium can't click the normal day div since the other day divs are in the way
			// a problem that doesn't occur for normal users
			theSH.hideAllElementsByClassName("other-assignments");

			specificUsageRadio.click();
			theSH.selectByVisibleText(usageForecastFormatSelect, scheduledDay.numberOfHours);
			
			// navigate to the correct month
			navigateToPeriod(scheduledDay.date);
			
			String clickableAreaXPath = DAY_CLICKABLE_AREA_XPATH.replace(DAYNUMTOKEN, theSH.getDateLabelForDate(scheduledDay.date, theSH.DAYNATURALNUMBERFORMAT));
			// if multiple months then we'd get more than one returned day element (as the same day number exists in each month)
			// find and click the visible one
			theSH.getVisibleWebElement(By.xpath(clickableAreaXPath)).click();
		}
		saveCalenderBtn.click();
		if(parentResourceName != null)
		{
			theSH.waitForElementToBeClickable(By.linkText(parentResourceName));
		}
		theSH.waitForElementToBeHidden(By.cssSelector("#assignmentCalendarPopup"), 20);
		// the screen takes a while to close after clicking save so wait for it to have closed
//		theSH.waitForElementToBeHiddenCss("#" + ACTIVITY_ASSIGNMENT_CALENDAR, 20);
//		theSH.sleep(1000, "a job isn't getting seeded without a wait here");
//		ActivityAssignmentPage.showAssignmentFor(theSH, resourceName);
	}

	private void navigateToPeriod(String date) throws ParseException {
		String monthYearLabel = theSH.getDateLabelForDate(date, theSH.MONTHYEARFORMAT);
		int counter = 0;
		
		String monthAndYear = theSH.getWebElementTextOrNull(currentPeriodDisplayName);
		// while the label on the current screen isn't equal to the month we want
		while(!monthAndYear.equals(monthYearLabel) && counter < 50)
		{
			// switch the month and try again
			theSH.clickLink(previousPeriodButton);
			theSH.sleep(1000);
			
			monthAndYear = theSH.getWebElementTextOrNull(currentPeriodDisplayName);
			// just in case we get into a stupid loop
			counter++;
		}
	}
}
