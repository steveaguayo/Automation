package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.TypesMob.DisplayType;
import kimble.automation.domain.mobile.general.Deserializers.ActivityFromIdDeserializer;
import kimble.automation.domain.mobile.general.Entities.StringIdEntity;
import kimble.automation.domain.mobile.general.Serializers.ActivityToIdSerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class TimeCategoryMob 
extends
StringIdEntity<TimeCategoryMob>
{

	String values;
	String name;
	DisplayType displayType;
	int displaySequence;
	ActivityMob activity;
	boolean isMandatory;
	boolean isDeleted;
	
	@JsonProperty("Values")
	public String getValues() { return values; }
	public void setValues(String values) { this.values = values; }
	
	@JsonProperty("Name")
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	@JsonProperty("DisplayType")
	public DisplayType getDisplayType() { return displayType; }
	public void setDisplayType(DisplayType displayType) { this.displayType = displayType; }
	
	@JsonProperty("DisplaySequence")
	public int getDisplaySequence() { return displaySequence; }
	public void setDisplaySequence(int displaySequence) { this.displaySequence = displaySequence; }
	
	@JsonProperty("ActivityId")
	@JsonSerialize(using = ActivityToIdSerializer.class)
	@JsonDeserialize(using = ActivityFromIdDeserializer.class)
	public ActivityMob getActivity() { return activity; }
	public void setActivity(ActivityMob activity) { this.activity = activity; }
	
	@JsonProperty("IsMandatory")
	public boolean getIsMandatory() { return isMandatory; }
	public void setMandatory(boolean isMandatory) { this.isMandatory = isMandatory; }
	
	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
	
}
