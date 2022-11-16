package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.general.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SynchroniseWindowMob extends Entities.StringIdEntity<SynchroniseWindowMob>
{
	public static String singletonId = "synchroniseWindow";

	public SynchroniseWindowMob() { this.setId(singletonId); }

	String versionNumber;
	String synchronizeFrom;
	String synchronizeTo;
	String lastSynchronizedDateTime;
	String earliestExpenseItemEntryDate;
	
	@JsonProperty("VersionNumber")
	public String getVersionNumber() { return versionNumber; }
	public void setVersionNumber(String versionNumber) { this.versionNumber = versionNumber; }
	
	@JsonProperty("SynchronizeFrom")
	public String getSynchronizeFrom() { return synchronizeFrom; }
	public void setSynchronizeFrom(String synchronizeFrom) { this.synchronizeFrom = synchronizeFrom; }
	
	@JsonProperty("SynchronizeTo")
	public String getSynchronizeTo() { return synchronizeTo; }
	public void setSynchronizeTo(String synchronizeTo) { this.synchronizeTo = synchronizeTo; }
	
	@JsonProperty("EarliestExpenseItemEntryDate")
	public String getEarliestExpenseItemEntryDate() { return earliestExpenseItemEntryDate; }
	public void setEarliestExpenseItemEntryDate(String earliestExpenseItemEntryDate) { this.earliestExpenseItemEntryDate = earliestExpenseItemEntryDate; }
	
	@JsonProperty("LastSynchronizedDateTime")
	public String getLastSynchronizedDateTime() { return lastSynchronizedDateTime; }
	public void setLastSynchronizedDateTime(String lastSynchronizedDateTime) { this.lastSynchronizedDateTime = lastSynchronizedDateTime; }

	@JsonIgnore
	public boolean getDeleted() { return false; }
}
