package kimble.automation.kimbleobjects.classic;

import java.math.BigDecimal;

public class PerformanceAnalysisObjC {
	protected DisplayCurrencyC processDisplayCurrency(String currencyDisplay) {
		DisplayCurrencyC processedDisplayCurrency = new DisplayCurrencyC();
		
		// there may be no figure at all (if not at this stage for instance)
		if(currencyDisplay.equals("")){
			processedDisplayCurrency.baseCurrencyCode = "XXX";
			processedDisplayCurrency.baseCurrencyAmount = new BigDecimal(0);
			processedDisplayCurrency.localEquivalentCurrencyCode = "GBP";
			processedDisplayCurrency.localEquivalentCurrencyAmount = new BigDecimal(0);
		} else {		
			boolean twoCurrencyDisplay = false;

			// if the currency is different to the local Equivalent then the
			// display currency is in the format USD 17,000.00 (GBP 11,184.21)
			// if the same the it's just GBP 11,184.21
			
			// scan for a bracket "(" this indicates which format we are processing
			twoCurrencyDisplay = currencyDisplay.contains("(");
			
			// extract this into component parts and remove any formatting
			// first remove any punctuation, leaving the . as this is for the
			// decimal current amount
			currencyDisplay = currencyDisplay.replace(",", "");
			
			if(twoCurrencyDisplay) {
				currencyDisplay = currencyDisplay.replace("(", "");
				currencyDisplay = currencyDisplay.replace(")", "");
			}
	
			// now split the value by space, each element will be a part of the
			// displaycurrency to return
			String[] displayCurrencyComponents = currencyDisplay.split(" ");
	
			processedDisplayCurrency.baseCurrencyCode = displayCurrencyComponents[0];
			processedDisplayCurrency.baseCurrencyAmount = new BigDecimal(displayCurrencyComponents[1]);
			
			// to keep consumers logic clean if this is just in local currency set them both equal
			if(twoCurrencyDisplay) {
				processedDisplayCurrency.localEquivalentCurrencyCode = displayCurrencyComponents[2];
				processedDisplayCurrency.localEquivalentCurrencyAmount = new BigDecimal(displayCurrencyComponents[3]);
			} else {
				processedDisplayCurrency.localEquivalentCurrencyCode = processedDisplayCurrency.baseCurrencyCode;
				processedDisplayCurrency.localEquivalentCurrencyAmount = processedDisplayCurrency.baseCurrencyAmount;
			}
		}
		return processedDisplayCurrency;
	}
}
