package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.general.Entities.StringIdEntity;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ExchangeRateMob 
extends
StringIdEntity<ExchangeRateMob>
{

    String toCurrency;
    boolean isDeleted;
    String fromCurrency;
	String effectiveDate;
    double conversionFactor;
    
	@JsonProperty("ToCurrencyISOCode")
	public String getToCurrency() { return toCurrency; }
	public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }
	
	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
	
	@JsonProperty("FromCurrencyISOCode")
	public String getFromCurrency() { return fromCurrency;}
	public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }
	
	@JsonProperty("EffectiveDate")
	public String getEffectiveDate() { return effectiveDate; }
	public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
	
	@JsonProperty("ConversionFactor")
	public double getConversionFactor() { return conversionFactor; }
	public void setConversionFactor(double conversionFactor) { this.conversionFactor = conversionFactor; }
    
}
