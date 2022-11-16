package kimble.automation.domain;

public class TimeEntry {
	public boolean actualiseAllForecastEntries = false;
	public boolean actualiseForecastEntry = false;
	public boolean timeSubmissionMethodAll = false;
	public boolean timeSubmissionMethodSelected = false;
	public boolean CreateAllDraftTimeEntries = false;
	public boolean SelectAll = false;
	public boolean NoTimeEntrySubmit = false;
	
	public String localId;
	
	public String startDate;
	public String endDate;
	public String startTime;
	public String endTime;
	public String plannedActivity;
	public String task;
	public String entryUnits;
	public String rateBand;
	public String notes;
	public String timeType;
	public String draftRemaining;
	public String timeCategory1;
	public String timeCategory1Selection;
	public String timeCategory2;
	public String timeCategory2Selection;
	
	
	public String[] statuses;
}
