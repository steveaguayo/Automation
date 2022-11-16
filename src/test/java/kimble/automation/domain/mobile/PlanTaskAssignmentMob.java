package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.general.Deserializers.AssignmentFromIdDeserializer;
import kimble.automation.domain.mobile.general.Entities.StringIdEntity;
import kimble.automation.domain.mobile.general.Serializers.AssignmentToIdSerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class PlanTaskAssignmentMob 
extends
StringIdEntity<PlanTaskAssignmentMob>
{
	public enum Status { Open, Frozen }

	Status status;
	String startDate;
	String name;
	String endDate;
	AssignmentMob assignment;
	boolean isDeleted;

	@JsonProperty("Status")
	public Status getStatus() { return status; }
	public void setStatus(Status aStatus) { this.status = aStatus; }

	@JsonProperty("StartDate")
	public String getStartDate() { return startDate; }
	public void setStartDate(String startDate) { this.startDate = startDate; }

	@JsonProperty("Name")
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	@JsonProperty("EndDate")
	public String getEndDate() { return endDate; }
	public void setEndDate(String endDate) { this.endDate = endDate; }
	
	@JsonProperty("AssignmentId")
	@JsonSerialize(using = AssignmentToIdSerializer.class)
	@JsonDeserialize(using = AssignmentFromIdDeserializer.class)
	public AssignmentMob getAssignment() { return assignment; }
	public void setAssignment(AssignmentMob assignment) { this.assignment = assignment; }

	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }

}
