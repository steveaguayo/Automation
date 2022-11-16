package kimble.automation.kimbleobjects.classic;

import java.math.BigDecimal;

public class CurrencyItemC {
	public String currencyCode;
	public BigDecimal amount = new BigDecimal(0);
	
	public CurrencyItemC() {
		currencyCode = "";
		amount = new BigDecimal(0);
	}
}
