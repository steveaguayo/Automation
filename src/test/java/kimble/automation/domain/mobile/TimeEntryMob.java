package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.general.Deserializers.ActivityRateBandFromIdDeserializer;
import kimble.automation.domain.mobile.general.Deserializers.AssignmentFromIdDeserializer;
import kimble.automation.domain.mobile.general.Deserializers.PlanTaskAssignmentFromIdDeserializer;
import kimble.automation.domain.mobile.general.Entities.RemotelySaveableEntity;
import kimble.automation.domain.mobile.general.Entities.TimestampedEntity;
import kimble.automation.domain.mobile.general.Identifiers.TimestampedIdentifier;
import kimble.automation.domain.mobile.general.Serializers.ActivityRateBandToIdSerializer;
import kimble.automation.domain.mobile.general.Serializers.AssignmentToIdSerializer;
import kimble.automation.domain.mobile.general.Serializers.PlanTaskAssignmentToIdSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class TimeEntryMob
extends
TimestampedEntity<TimeEntryMob>
implements
RemotelySaveableEntity<TimeEntryMob, TimestampedIdentifier>
{

	public enum Status { Draft, Forecast, Submitted, ReadyForApproval, Approved, Rejected; }

	double units;
	Status status;
	PlanTaskAssignmentMob planTaskAssignment;
	String notes;
	String entryDate;
	Integer draftRemainingEffort;
	String category1;
	String category2;
	String category3;
	String category4;
	String category5;
	String category6;
	String startTime;
	String endTime;
	AssignmentMob assignment;
	String approvalComments;
	ActivityRateBandMob activityRateBand;
	boolean isDeleted;
	
	// Client side only
    boolean hasChanged;
    boolean isDeletedLocally;
    boolean isDeletedOnServer;
	
	@JsonProperty("Units")
	public double getUnits() { return units; }
	public void setUnits(double units) { this.units = units; }
	
	@JsonProperty("Status")
	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }
	
	@JsonProperty("PlanTaskAssignmentId")
	@JsonSerialize(using = PlanTaskAssignmentToIdSerializer.class)		
	@JsonDeserialize(using = PlanTaskAssignmentFromIdDeserializer.class)		
	public PlanTaskAssignmentMob getPlanTaskAssignment() { return planTaskAssignment; }
	public void setPlanTaskAssignment(PlanTaskAssignmentMob planTaskAssignment) { this.planTaskAssignment = planTaskAssignment; }
	
	@JsonProperty("Notes")
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
	
	@JsonProperty("EntryDate")
	public String getEntryDate() { return entryDate; }
	public void setEntryDate(String entryDate) { this.entryDate = entryDate; }
	
	@JsonProperty("DraftRemainingEffort")
	public Integer getDraftRemainingEffort() { return draftRemainingEffort; }
	public void setDraftRemainingEffort(Integer draftRemainingEffort) { this.draftRemainingEffort = draftRemainingEffort; }
	
	@JsonProperty("Category1")
	public String getCategory1() { return category1; }
	public void setCategory1(String category1) { this.category1 = category1; }
	
	@JsonProperty("Category2")
	public String getCategory2() { return category2; }
	public void setCategory2(String category2) { this.category2 = category2; }
	
	@JsonProperty("Category3")
	public String getCategory3() { return category3; }
	public void setCategory3(String category3) { this.category3 = category3; }
	
	@JsonProperty("Category4")
	public String getCategory4() { return category4; }
	public void setCategory4(String category4) { this.category4 = category4; }
	
	@JsonProperty("Category5")
	public String getCategory5() { return category5; }
	public void setCategory5(String category5) { this.category5 = category5; }
	
	@JsonProperty("Category6")
	public String getCategory6() { return category6; }
	public void setCategory6(String category6) { this.category6 = category6; }
	
	@JsonProperty("StartTime")
	public String getStartTime() { return startTime; }
	public void setStartTime(String time) { this.startTime = time; }
	
	@JsonProperty("EndTime")
	public String getEndTime() { return endTime; }
	public void setEndTime(String time) { this.endTime = time; }
	
	@JsonProperty("AssignmentId")
	@JsonSerialize(using = AssignmentToIdSerializer.class)
	@JsonDeserialize(using = AssignmentFromIdDeserializer.class)
	public AssignmentMob getAssignment() { return assignment; }
	public void setAssignment(AssignmentMob assignment) { this.assignment = assignment; }
	
	@JsonProperty("ApprovalComments")
	public String getApprovalComments() { return approvalComments; }
	public void setApprovalComments(String approvalComments) { this.approvalComments = approvalComments; }
	
	@JsonProperty("ActivityRateBandId")
	@JsonSerialize(using = ActivityRateBandToIdSerializer.class)
	@JsonDeserialize(using = ActivityRateBandFromIdDeserializer.class)
	public ActivityRateBandMob getActivityRateBand() { return activityRateBand; }
	public void setActivityRateBand(ActivityRateBandMob activityRateBand) { this.activityRateBand = activityRateBand; }
	
	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
	
	// Client side only
	@JsonIgnore
	public boolean hasChanged() { return hasChanged; }
	public void setHasChanged(boolean hasChanged) { this.hasChanged = hasChanged; }
	
	@JsonIgnore
	public boolean isDeletedLocally() { return isDeletedLocally; }
	public void setDeletedLocally(boolean isDeletedLocally) { this.isDeletedLocally = isDeletedLocally; }
	
	@JsonIgnore
	public boolean isDeletedOnServer() { return isDeletedOnServer; }
	public void setDeletedOnServer(boolean isDeletedOnServer) { this.isDeletedOnServer = isDeletedOnServer; }
	
}
