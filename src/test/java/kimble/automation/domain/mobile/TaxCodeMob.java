package kimble.automation.domain.mobile;

import java.util.List;

import kimble.automation.domain.mobile.general.Entities.StringIdEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class TaxCodeMob 
extends
StringIdEntity<TaxCodeMob>
{
	String name;
	
	@JsonProperty("Name")
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	@JsonIgnore
	public boolean getDeleted() { return false; }

	List<TaxRateMob> taxRates;

	@JsonIgnore
	public List<TaxRateMob> getTaxRates() { return taxRates; }
}
