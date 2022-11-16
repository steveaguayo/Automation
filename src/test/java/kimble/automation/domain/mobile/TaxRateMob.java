package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.general.Deserializers.TaxCodeFromIdDeserializer;
import kimble.automation.domain.mobile.general.Entities.StringIdEntity;
import kimble.automation.domain.mobile.general.Serializers.TaxCodeToIdSerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class TaxRateMob 
extends
StringIdEntity<TaxRateMob>
{

	TaxCodeMob taxCode;
	double rate;
	String name;
	boolean isDeleted;
	String effectiveDate;
	
	@JsonProperty("TaxCodeId")
	@JsonSerialize(using = TaxCodeToIdSerializer.class)
	@JsonDeserialize(using = TaxCodeFromIdDeserializer.class)
	public TaxCodeMob getTaxCode() { return taxCode; }
	public void setTaxCode(TaxCodeMob taxCode) { this.taxCode = taxCode; }
	
	@JsonProperty("Rate")
	public double getRate() { return rate; }
	public void setRate(double rate) { this.rate = rate; }
	
	@JsonProperty("Name")
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
	
	@JsonProperty("EffectiveDate")
	public String getEffectiveDate() { return effectiveDate; }
	public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
	
}
