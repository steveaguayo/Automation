package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.TypesMob.PeriodType;
import kimble.automation.domain.mobile.TypesMob.UsageAllocationType;
import kimble.automation.domain.mobile.general.Deserializers.ActivityFromIdDeserializer;
import kimble.automation.domain.mobile.general.Entities.StringIdEntity;
import kimble.automation.domain.mobile.general.Serializers.ActivityToIdSerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ActivityRateBandMob  
extends
StringIdEntity<ActivityRateBandMob>
{
	UsageAllocationType usageAllocationType;
    int priority;
	PeriodType periodType;
    String name;
    double maxUsage;
    boolean isDeleted;
    ActivityMob activity;
    String assignmentId;
    boolean isNotesMandatory;
    
	@JsonProperty("UsageAllocationType")
	public UsageAllocationType getUsageAllocationType() { return usageAllocationType; }
	public void setUsageAllocationType(UsageAllocationType usageAllocationType) { this.usageAllocationType = usageAllocationType; }
	
	@JsonProperty("Priority")
	public int getPriority() { return priority; }
	public void setPriority(int priority) { this.priority = priority; }
	
	@JsonProperty("PeriodType")
	public PeriodType getPeriodType() { return periodType; }
	public void setPeriodType(PeriodType periodType) { this.periodType = periodType; }
	
	@JsonProperty("Name")
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	@JsonProperty("MaxUsage")
	public double getMaxUsage() { return maxUsage; }
	public void setMaxUsage(double maxUsage) { this.maxUsage = maxUsage; }
	
	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
	
	@JsonProperty("IsNotesMandatory")
	public boolean getIsNotesMandatory() { return isNotesMandatory; }
	public void setIsNotesMandatory(boolean isNotesMandatory) { this.isNotesMandatory = isNotesMandatory; }
	
	@JsonProperty("ActivityId")
	@JsonSerialize(using = ActivityToIdSerializer.class)
	@JsonDeserialize(using = ActivityFromIdDeserializer.class)
	public ActivityMob getActivity() { return activity; }
	public void setActivity(ActivityMob activity) { this.activity = activity; }
    
	@JsonProperty("AssignmentId")
	public String getAssignmentId() { return assignmentId; }
	public void setAssignmentId(String id) { this.assignmentId = id; }
    
}
