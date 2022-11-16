package kimble.automation.kimbleobjects.classic;

import org.openqa.selenium.WebElement;

public class TimesheetDayC {
	public String theDayName;
	public WebElement theAddTimeEntryLink;
	
	public TimesheetDayC(String dayName, WebElement addTimeEntryLink) {
		theDayName = dayName;
		theAddTimeEntryLink = addTimeEntryLink;
	}
}
