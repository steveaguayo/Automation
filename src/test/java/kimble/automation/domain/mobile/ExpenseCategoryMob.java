package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.general.Deserializers;
import kimble.automation.domain.mobile.general.Serializers;
import kimble.automation.domain.mobile.general.Deserializers.ActivityFromIdDeserializer;
import kimble.automation.domain.mobile.general.Entities.StringIdEntity;
import kimble.automation.domain.mobile.general.Serializers.ActivityToIdSerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class ExpenseCategoryMob  
extends
StringIdEntity<ExpenseCategoryMob>
{

	String unitType;
	String name;
	boolean isDeleted;
	boolean isActive;
    boolean hasStartLocation;
    boolean hasEndLocation;
    boolean hasAttendees;
    String currency;
    boolean canEditTaxCode;
    boolean canEditTaxAmount;
    ActivityMob activity;
	TaxCodeMob taxCode;

	@JsonProperty("UnitType")
	public String getUnitType() { return unitType; }
	public void setUnitType(String unitType) { this.unitType = unitType; }
	
	@JsonProperty("Name")
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
	
	@JsonProperty("IsActive")
	public boolean getIsActive() { return isActive; }
	public void setActive(boolean isActive) { this.isActive = isActive; }
	
	@JsonProperty("HasStartLocation")
	public boolean getHasStartLocation() { return hasStartLocation; }
	public void setHasStartLocation(boolean hasStartLocation) { this.hasStartLocation = hasStartLocation; }
	
	@JsonProperty("HasEndLocation")
	public boolean getHasEndLocation() { return hasEndLocation; }
	public void setHasEndLocation(boolean hasEndLocation) { this.hasEndLocation = hasEndLocation; }
	
	@JsonProperty("HasAttendees")
	public boolean getHasAttendees() { return hasAttendees; }
	public void setHasAttendees(boolean hasAttendees) { this.hasAttendees = hasAttendees; }
	
	@JsonProperty("CurrencyISOCode")
	public String getCurrency() { return currency; }
	public void setCurrency(String currency) { this.currency = currency; }
	
	@JsonProperty("CanEditTaxCode")
	public boolean getCanEditTaxCode() { return canEditTaxCode; }
	public void setCanEditTaxCode(boolean canEditTaxCode) { this.canEditTaxCode = canEditTaxCode; }
	
	@JsonProperty("CanEditTaxAmount")
	public boolean getCanEditTaxAmount() { return canEditTaxAmount; }
	public void setCanEditTaxAmount(boolean canEditTaxAmount) { this.canEditTaxAmount = canEditTaxAmount; }
	
	@JsonProperty("ActivityId")
	@JsonSerialize(using = ActivityToIdSerializer.class)
	@JsonDeserialize(using = ActivityFromIdDeserializer.class)
	public ActivityMob getActivity() { return activity; }
	public void setActivity(ActivityMob activity) { this.activity = activity; }

	//TODO: Wait for stu to add then recheck to make sure this is right
	@JsonProperty("TaxCodeId")
	@JsonSerialize(using = Serializers.TaxCodeToIdSerializer.class)
	@JsonDeserialize(using = Deserializers.TaxCodeFromIdDeserializer.class)
	public TaxCodeMob getTaxCode() { return taxCode; }
	public void setTaxCode(TaxCodeMob taxCode) { this.taxCode = taxCode; }
	
}
