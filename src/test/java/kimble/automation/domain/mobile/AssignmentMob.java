package kimble.automation.domain.mobile;

import java.util.List;

import kimble.automation.domain.mobile.general.Deserializers.ActivityFromIdDeserializer;
import kimble.automation.domain.mobile.general.Entities.StringIdEntity;
import kimble.automation.domain.mobile.general.Serializers.ActivityToIdSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AssignmentMob
extends
StringIdEntity<AssignmentMob>
{

	double usageEntryFormat;
    String startDate;
    String name;
    boolean isDeleted;
    double hoursPerDay;
    double latitude;
    double longitude;
	String endDate;
    ActivityMob activity;
	List<TimeEntryMob> timeEntries;
	List<PlanTaskAssignmentMob> planTaskAssignments;
	int remainingUsage;

	@JsonProperty("RemainingUsage")
	public double getRemainingUsage() { return remainingUsage; }
	public void setRemainingUsage(int remainingUsage) { this.remainingUsage = remainingUsage; }
	
	@JsonProperty("UsageEntryFormat")
	public double getUsageEntryFormat() { return usageEntryFormat; }
	public void setUsageEntryFormat(double usageEntryFormat) { this.usageEntryFormat = usageEntryFormat; }

	@JsonProperty("StartDate")
	public String getStartDate() { return startDate; }
	public void setStartDate(String startDate) { this.startDate = startDate; }

	@JsonProperty("Name")
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }

	@JsonProperty("HoursPerDay")
	public double getHoursPerDay() { return hoursPerDay; }
	public void setHoursPerDay(double hoursPerDay) { this.hoursPerDay = hoursPerDay; }

	@JsonProperty("Latitude")
	public double getLatitude() { return latitude; }
	public void setLatitude(double latitude) { this.latitude = latitude; }

	@JsonProperty("Longitude")
	public double getLongitude() { return longitude; }
	public void setLongitude(double longitude) { this.latitude = longitude; }

	@JsonProperty("EndDate")
	public String getEndDate() { return endDate; }
	public void setEndDate(String endDate) { this.endDate = endDate; }

	@JsonProperty("ActivityId")
	@JsonSerialize(using = ActivityToIdSerializer.class)
	@JsonDeserialize(using = ActivityFromIdDeserializer.class)
	public ActivityMob getActivity() { return activity; }
	public void setActivity(ActivityMob activity) { this.activity = activity; }

	@JsonIgnore
	public List<TimeEntryMob> getTimeEntries() { return timeEntries; }

	@JsonIgnore
	public List<PlanTaskAssignmentMob> getPlanTaskAssignments() { return planTaskAssignments; }
}
