package kimble.automation.domain;

import java.util.List;

public class Timesheet {
	// allow phased entry of time by specifying this testStage
	public String testStage;
	
	public String resourceName;
	public String trackingPeriodStartDate;

	public List<TimeEntry> timeEntries;
	
	public String mobilePeriodDate;
	public String mobileTrackingPeriodStartDate;
}
