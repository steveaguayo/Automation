package kimble.automation.kimbleobjects.classic;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import kimble.automation.domain.ExpectedValue;
import kimble.automation.domain.Fact;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class SalesOppForecastPageC extends BasePageC {
	
	private static final String ACTUAL = "Actual";
	private static final String P1_FORECAST = "P1 Forecast";
	private static final String P2_FORECAST = "P2 Forecast";
	private static final String P3_FORECAST = "P3 Forecast";
	private static final String PASSED = "Passed";
	private static final String FORECAST_REVENUE = "ForecastRevenue";
	private static final String RECEIVABLES_FORECAST = "ReceivablesForecast";
	private static final String BOOKING_FORECAST = "BookingForecast";
	private static final String REVENUE_FORECAST = "RevenueForecast";
	private static final String COST_FORECAST = "CostForecast";
	private static final String INVOICED_AMOUNTS = "InvoicedAmounts";

	SalesOpportunityForecastObjC performanceAnalysisDetail;
	
	List<WebElement> bookingForecastElements;
	List<WebElement> revenueForecastElements;
	List<WebElement> costForecastElements;
	List<WebElement> receivablesForecastElements;
	List<WebElement> invoiceAmountElements;
	
	private static final String bookingForecastSelector = "td.dataCell[id*='contractValue']";
	private static final String revenueForecastSelector = "td.dataCell[id*='revenue']";
	private static final String costForecastSelector = "td.dataCell[id*='cost']";
	private static final String receivablesForecastSelector = "td.dataCell[id*='receivables']";
	private static final String invoiceAmountSelector = "td.dataCell[id*='invoiced']";

	public SalesOppForecastPageC(SeleniumHelper seleniumHelperInstance) {
		super(seleniumHelperInstance);
	}
	
	public void Initialise() {
		readWebElements();
		performanceAnalysisDetail = new SalesOpportunityForecastObjC(
				bookingForecastElements, revenueForecastElements, costForecastElements,
				receivablesForecastElements, invoiceAmountElements);
	}

	private void readWebElements() {
		bookingForecastElements = theSH.getWebElements(By.cssSelector(bookingForecastSelector));
		revenueForecastElements = theSH.getWebElements(By.cssSelector(revenueForecastSelector));
		costForecastElements = theSH.getWebElements(By.cssSelector(costForecastSelector));
		receivablesForecastElements = theSH.getWebElements(By.cssSelector(receivablesForecastSelector));
		invoiceAmountElements = theSH.getWebElements(By.cssSelector(invoiceAmountSelector));
	}
	
	public boolean ValidateFact(String testStage, Fact factDetail, ExpectedValue expectedValueDetail) throws Exception {
		String factName = factDetail.factName;
		String ignoreReason = factDetail.ignoreReason;
		String measure = expectedValueDetail.measure;
		String expectedValueCurrency = "";
		String expectedValue = expectedValueDetail.value;
		String actualValueCurrency = "";
		String actualValue = "";
		String result = PASSED;
		boolean returnValue = true;
		boolean validateCurrency = false;
		
		if(expectedValueDetail.currency != null && !expectedValueDetail.currency.equals("")){
			expectedValueCurrency = expectedValueDetail.currency;
			validateCurrency = true;
		}
		
		if (ignoreReason != null && ignoreReason != "") {
			theSH.logIgnoreFact(testStage, factName, ignoreReason, measure);
		} else {

			if (factName.equals(BOOKING_FORECAST)) {
				CurrencyItemC bookingForecastTotal = new CurrencyItemC();

				if (measure.equals(ACTUAL)) {
					bookingForecastTotal = GetBookingForecastTotalActualForDeliveryEngagement(factDetail);
				} else if (measure.equals(P1_FORECAST)) {
					bookingForecastTotal = GetBookingForecastTotalP1ForecastForDeliveryEngagement(factDetail);
				} else if (measure.equals(P2_FORECAST)) {
					bookingForecastTotal = GetBookingForecastTotalP2ForecastForDeliveryEngagement(factDetail);
				} else if (measure.equals(P3_FORECAST)) {
					bookingForecastTotal = GetBookingForecastTotalP3ForecastForDeliveryEngagement(factDetail);
				}
				actualValue = bookingForecastTotal.amount.toString();
				if(validateCurrency && bookingForecastTotal.currencyCode != null) actualValueCurrency = bookingForecastTotal.currencyCode.toString();
			} else if (factName.equals(REVENUE_FORECAST)) {
				CurrencyItemC revenueForecastTotal = new CurrencyItemC();

				if (measure.equals(ACTUAL)) {
					revenueForecastTotal = GetRevenueForecastTotalActualForDeliveryEngagement(factDetail);
				} else if (measure.equals(P1_FORECAST)) {
					revenueForecastTotal = GetRevenueForecastTotalP1ForecastForDeliveryEngagement(factDetail);
				} else if (measure.equals(P2_FORECAST)) {
					revenueForecastTotal = GetRevenueForecastTotalP2ForecastForDeliveryEngagement(factDetail);
				} else if (measure.equals(P3_FORECAST)) {
					revenueForecastTotal = GetRevenueForecastTotalP3ForecastForDeliveryEngagement(factDetail);
				}
				actualValue = revenueForecastTotal.amount.toString();
				if(validateCurrency && revenueForecastTotal.currencyCode != null) actualValueCurrency = revenueForecastTotal.currencyCode.toString();
			} else if (factName.equals(COST_FORECAST)) {
				CurrencyItemC costForecastTotal = new CurrencyItemC();

				if (measure.equals(ACTUAL)) {
					costForecastTotal = GetCostForecastTotalActualForDeliveryEngagement(factDetail);
				} else if (measure.equals(P1_FORECAST)) {
					costForecastTotal = GetCostForecastTotalP1ForecastForDeliveryEngagement(factDetail);
				} else if (measure.equals(P2_FORECAST)) {
					costForecastTotal = GetCostForecastTotalP2ForecastForDeliveryEngagement(factDetail);
				}
				actualValue = costForecastTotal.amount.toString();
				if(validateCurrency && costForecastTotal.currencyCode != null) actualValueCurrency = costForecastTotal.currencyCode.toString();
			} else if (factName.equals(INVOICED_AMOUNTS) && measure.equals(ACTUAL)) {
				CurrencyItemC InvoicedAmountsTotal = GetInvoicedAmountsTotalActualForDeliveryEngagement(factDetail);

				actualValue = InvoicedAmountsTotal.amount.toString();
				if(validateCurrency && InvoicedAmountsTotal.currencyCode != null) actualValueCurrency = InvoicedAmountsTotal.currencyCode.toString();
			} else if (factName.equals(RECEIVABLES_FORECAST) && measure.equals(FORECAST_REVENUE)) {
				CurrencyItemC receivablesForecastTotal = GetReceivablesForecastTotalForecastRevenueForDeliveryEngagement(factDetail);

				actualValue = receivablesForecastTotal.amount.toString();
				if(validateCurrency && receivablesForecastTotal.currencyCode != null) actualValueCurrency = receivablesForecastTotal.currencyCode.toString();
			} else {
				theSH.invalidExpectedResultsConfiguration(factName, measure);
			}

			returnValue = (actualValue.toString().equals(expectedValue));
			if(returnValue && validateCurrency) returnValue = (actualValueCurrency.toString().equals(expectedValueCurrency)); 
			
			if (!returnValue) {
				result = "FAILED";
			}

			String expectedValueForLog = expectedValue;
			String actualValueForLog = actualValue;
			if(validateCurrency) {
				expectedValueForLog = expectedValueCurrency + " " + expectedValueForLog;
				actualValueForLog = actualValueCurrency + " " + actualValueForLog;
			}
				
			theSH.logValidationResult(testStage, factName, measure, expectedValueForLog, actualValueForLog, result);
		}
		return returnValue;
	}

	public CurrencyItemC GetRevenueForecastTotalActualForDeliveryEngagement(final Fact factDetail){
		Collection<RevenueForecastObjC> filteredList = getFilteredRevenueForecast(factDetail);
		
		CurrencyItemC actualRevenue = new CurrencyItemC();
		for (RevenueForecastObjC revenueForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			
			//A PA without a currency is treated as having 'XXX' as the currency; this is to catch the scenario where high level forecast records are still
            //included in the SalesOppForecast after the Opportunity has been won
			if(revenueForecastObj.actual.baseCurrencyCode != null && revenueForecastObj.actual.baseCurrencyCode.equals("XXX")) {
				   Assert.assertEquals(actualRevenue.currencyCode, revenueForecastObj.actual.baseCurrencyCode);
				}
			else if(actualRevenue.currencyCode != null && !actualRevenue.currencyCode.equals("")) {
				      Assert.assertEquals(actualRevenue.currencyCode, revenueForecastObj.actual.baseCurrencyCode);
				}
			actualRevenue.currencyCode = revenueForecastObj.actual.baseCurrencyCode;
			actualRevenue.amount = actualRevenue.amount.add(revenueForecastObj.actual.baseCurrencyAmount);
		}
		
		return actualRevenue;
	}
	
	public CurrencyItemC GetRevenueForecastTotalP1ForecastForDeliveryEngagement(final Fact factDetail){
		Collection<RevenueForecastObjC> filteredList = getFilteredRevenueForecast(factDetail);
		
		CurrencyItemC p1ForecastRevenue = new CurrencyItemC();
		for (RevenueForecastObjC revenueForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(p1ForecastRevenue.currencyCode != null && !p1ForecastRevenue.currencyCode.equals("")) {
				Assert.assertEquals(p1ForecastRevenue.currencyCode, revenueForecastObj.p1Forecast.baseCurrencyCode);
			}
			p1ForecastRevenue.currencyCode = revenueForecastObj.p1Forecast.baseCurrencyCode;
			p1ForecastRevenue.amount = p1ForecastRevenue.amount.add(revenueForecastObj.p1Forecast.baseCurrencyAmount);
		}
		
		return p1ForecastRevenue;
	}
	
	public CurrencyItemC GetRevenueForecastTotalP2ForecastForDeliveryEngagement(final Fact factDetail){
		Collection<RevenueForecastObjC> filteredList = getFilteredRevenueForecast(factDetail);
		
		CurrencyItemC forecastRevenue = new CurrencyItemC();
		for (RevenueForecastObjC revenueForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(forecastRevenue.currencyCode != null && !forecastRevenue.currencyCode.equals("")) {
				Assert.assertEquals(forecastRevenue.currencyCode, revenueForecastObj.p2Forecast.baseCurrencyCode);
			}
			forecastRevenue.currencyCode = revenueForecastObj.p2Forecast.baseCurrencyCode;
			forecastRevenue.amount = forecastRevenue.amount.add(revenueForecastObj.p2Forecast.baseCurrencyAmount);
		}
		
		return forecastRevenue;
	}
	
	public CurrencyItemC GetRevenueForecastTotalP3ForecastForDeliveryEngagement(final Fact factDetail){
		Collection<RevenueForecastObjC> filteredList = getFilteredRevenueForecast(factDetail);
		
		CurrencyItemC forecastRevenue = new CurrencyItemC();
		for (RevenueForecastObjC revenueForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(forecastRevenue.currencyCode != null && !forecastRevenue.currencyCode.equals("")) {
				Assert.assertEquals(forecastRevenue.currencyCode, revenueForecastObj.p3Forecast.baseCurrencyCode);
			}
			forecastRevenue.currencyCode = revenueForecastObj.p3Forecast.baseCurrencyCode;
			forecastRevenue.amount = forecastRevenue.amount.add(revenueForecastObj.p3Forecast.baseCurrencyAmount);
		}
		
		return forecastRevenue;
	}

	private Collection<RevenueForecastObjC> getFilteredRevenueForecast(final Fact factDetail) {
		@SuppressWarnings("unchecked")
		Collection<RevenueForecastObjC> filteredList = performanceAnalysisDetail.revenueForecasts.stream().filter((o) -> {
	    	RevenueForecastObjC candidate = (RevenueForecastObjC)o;
	        return ((factDetail.period == null) || (factDetail.period != null && !factDetail.period.equals("") && candidate.timePeriod.equals(factDetail.period)))
	        	&& ((factDetail.domainClass == null || factDetail.domainClass.length() == 0) || factDetail.domainClass.equals(candidate.domainClass))
	            && candidate.deliveryEngagement.equals(factDetail.deliveryEngagementName)
	            && ((factDetail.deliveryElementName == null) || (factDetail.deliveryElementName.equals(null) && candidate.deliveryElement != null && candidate.deliveryElement.equals("")) ||
	            		(factDetail.deliveryElementName != null && !factDetail.deliveryElementName.equals("") && candidate.deliveryElement.equals(factDetail.deliveryElementName)))
	        	&& ((factDetail.secondaryDeliveryElementName == null || factDetail.secondaryDeliveryElementName.length() == 0) || factDetail.secondaryDeliveryElementName.equals(candidate.secondaryDeliveryElement))
	        	&& ((factDetail.resourceName == null || factDetail.resourceName.length() == 0) || factDetail.resourceName.equals(candidate.resource))
	            && candidate.businessUnit.equals(factDetail.businessUnitName);
		}).collect(Collectors.toList());
		return filteredList;
	}
	
	private CurrencyItemC GetReceivablesForecastTotalForecastRevenueForDeliveryEngagement(final Fact factDetail) {
		Collection<ReceivablesForecastObjC> filteredList = getFilteredReceivablesForecast(factDetail);
		
		CurrencyItemC forecastRevenueAmount = new CurrencyItemC();
		for (ReceivablesForecastObjC receivablesForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(forecastRevenueAmount.currencyCode != null && !forecastRevenueAmount.currencyCode.equals("")) {
				Assert.assertEquals(forecastRevenueAmount.currencyCode, receivablesForecastObj.forecastRevenue.baseCurrencyCode);
			}
			forecastRevenueAmount.currencyCode = receivablesForecastObj.forecastRevenue.baseCurrencyCode;
			forecastRevenueAmount.amount = forecastRevenueAmount.amount.add(receivablesForecastObj.forecastRevenue.baseCurrencyAmount);
		}
		
		return forecastRevenueAmount;
	}
	
	private Collection<ReceivablesForecastObjC> getFilteredReceivablesForecast(final Fact factDetail) {
		@SuppressWarnings("unchecked")
		Collection<ReceivablesForecastObjC> filteredList = performanceAnalysisDetail.receivablesForecasts.stream().filter((o) -> {
	    	ReceivablesForecastObjC candidate = (ReceivablesForecastObjC)o;
	        return ((factDetail.receivableDate == null) || (factDetail.receivableDate != null && !factDetail.receivableDate.equals("") && candidate.receivableDate.equals(factDetail.receivableDate)))
	        	&& candidate.deliveryEngagement.equals(factDetail.deliveryEngagementName)
	            && ((factDetail.deliveryElementName == null) || (factDetail.deliveryElementName.equals(null) && candidate.deliveryElement != null && candidate.deliveryElement.equals("")) ||
	            		(factDetail.deliveryElementName != null && !factDetail.deliveryElementName.equals("") && candidate.deliveryElement.equals(factDetail.deliveryElementName)));
		}).collect(Collectors.toList());
		return filteredList;
	}

	private CurrencyItemC GetInvoicedAmountsTotalActualForDeliveryEngagement(final Fact factDetail) {
		Collection<InvoicedAmountsObjC> filteredList = getFilteredInvoicedAmounts(factDetail);
		
		CurrencyItemC actualInvoicedAmount = new CurrencyItemC();
		for (InvoicedAmountsObjC invoicedAmountObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(actualInvoicedAmount.currencyCode != null && !actualInvoicedAmount.currencyCode.equals("")) {
				Assert.assertEquals(actualInvoicedAmount.currencyCode, invoicedAmountObj.actual.baseCurrencyCode);
			}
			actualInvoicedAmount.currencyCode = invoicedAmountObj.actual.baseCurrencyCode;
			actualInvoicedAmount.amount = actualInvoicedAmount.amount.add(invoicedAmountObj.actual.baseCurrencyAmount);
		}
		
		return actualInvoicedAmount;
	}

	private Collection<InvoicedAmountsObjC> getFilteredInvoicedAmounts(final Fact factDetail) {
		@SuppressWarnings("unchecked")
		Collection<InvoicedAmountsObjC> filteredList = performanceAnalysisDetail.invoicedAmounts.stream().filter((o) -> {
	    	InvoicedAmountsObjC candidate = (InvoicedAmountsObjC)o;
	        return candidate.timePeriod.equals(factDetail.period)
		        && ((factDetail.domainClass == null || factDetail.domainClass.length() == 0) || factDetail.domainClass.equals(candidate.domainClass))
	        	&& ((factDetail.resourceName == null || factDetail.resourceName.length() == 0) || factDetail.resourceName.equals(candidate.resource))
	            && candidate.deliveryEngagement.equals(factDetail.deliveryEngagementName)
	            && ((factDetail.deliveryElementName == null) || (factDetail.deliveryElementName.equals(null) && candidate.deliveryElement != null && candidate.deliveryElement.equals("")) ||
	            		(factDetail.deliveryElementName != null && !factDetail.deliveryElementName.equals("") && candidate.deliveryElement.equals(factDetail.deliveryElementName)));
		}).collect(Collectors.toList());
		return filteredList;
	}

	private CurrencyItemC GetCostForecastTotalActualForDeliveryEngagement(final Fact factDetail) {
		Collection<CostForecastObjC> filteredList = getFilteredCostForecast(factDetail);

		CurrencyItemC actualCost = new CurrencyItemC();
		for (CostForecastObjC costForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(actualCost.currencyCode != null && !actualCost.currencyCode.equals("")) {
				Assert.assertEquals(actualCost.currencyCode, costForecastObj.actual.baseCurrencyCode);
			}
			actualCost.currencyCode = costForecastObj.actual.baseCurrencyCode;
			actualCost.amount = actualCost.amount.add(costForecastObj.actual.baseCurrencyAmount);
		}
		
		return actualCost;
	}
	
	private CurrencyItemC GetCostForecastTotalP1ForecastForDeliveryEngagement(final Fact factDetail) {
		Collection<CostForecastObjC> filteredList = getFilteredCostForecast(factDetail);
		
		CurrencyItemC actualCost = new CurrencyItemC();
		for (CostForecastObjC costForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(actualCost.currencyCode != null && !actualCost.currencyCode.equals("")) {
				Assert.assertEquals(actualCost.currencyCode, costForecastObj.p1Forecast.baseCurrencyCode);
			}
			actualCost.currencyCode = costForecastObj.p1Forecast.baseCurrencyCode;
			actualCost.amount = actualCost.amount.add(costForecastObj.p1Forecast.baseCurrencyAmount);
		}
		
		return actualCost;
	}
	
	private CurrencyItemC GetCostForecastTotalP2ForecastForDeliveryEngagement(final Fact factDetail) {
		Collection<CostForecastObjC> filteredList = getFilteredCostForecast(factDetail);
		
		CurrencyItemC actualCost = new CurrencyItemC();
		for (CostForecastObjC costForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(actualCost.currencyCode != null && !actualCost.currencyCode.equals("")) {
				Assert.assertEquals(actualCost.currencyCode, costForecastObj.p2Forecast.baseCurrencyCode);
			}
			actualCost.currencyCode = costForecastObj.p2Forecast.baseCurrencyCode;
			actualCost.amount = actualCost.amount.add(costForecastObj.p2Forecast.baseCurrencyAmount);
		}
		
		return actualCost;
	}
	
	private Collection<CostForecastObjC> getFilteredCostForecast(final Fact factDetail) {
		@SuppressWarnings("unchecked")
		Collection<CostForecastObjC> filteredList = performanceAnalysisDetail.costForecasts.stream().filter((o) -> {
	    	CostForecastObjC candidate = (CostForecastObjC)o;
	        return ((factDetail.period == null) || (factDetail.period != null && !factDetail.period.equals("") && candidate.timePeriod.equals(factDetail.period)))
		        && ((factDetail.domainClass == null || factDetail.domainClass.length() == 0) || factDetail.domainClass.equals(candidate.domainClass))
	        	&& ((factDetail.resourceName == null || factDetail.resourceName.length() == 0) || factDetail.resourceName.equals(candidate.resource))
	            && candidate.deliveryEngagement.equals(factDetail.deliveryEngagementName)
	            && ((factDetail.deliveryElementName == null) || (factDetail.deliveryElementName.equals(null) && candidate.deliveryElement != null && candidate.deliveryElement.equals("")) ||
	            		(factDetail.deliveryElementName != null && !factDetail.deliveryElementName.equals("") && candidate.deliveryElement.equals(factDetail.deliveryElementName)))
	        	&& ((factDetail.secondaryDeliveryElementName == null || factDetail.secondaryDeliveryElementName.length() == 0) || factDetail.secondaryDeliveryElementName.equals(candidate.secondaryDeliveryElement))
	            && candidate.businessUnit.equals(factDetail.businessUnitName);
		}).collect(Collectors.toList());
		return filteredList;
	}

	private CurrencyItemC GetBookingForecastTotalActualForDeliveryEngagement(final Fact factDetail) {
		Collection<BookingForecastObjC> filteredList = getFilteredBookingForecast(factDetail);
		
		CurrencyItemC actualBooking = new CurrencyItemC();
		for (BookingForecastObjC bookingForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(actualBooking.currencyCode != null && !actualBooking.currencyCode.equals("")) {
				Assert.assertEquals(actualBooking.currencyCode, bookingForecastObj.actual.baseCurrencyCode);
			}
			actualBooking.currencyCode = bookingForecastObj.actual.baseCurrencyCode;
			actualBooking.amount = actualBooking.amount.add(bookingForecastObj.actual.baseCurrencyAmount);
		}
		
		return actualBooking;
	}

	private CurrencyItemC GetBookingForecastTotalP1ForecastForDeliveryEngagement(final Fact factDetail) {
		Collection<BookingForecastObjC> filteredList = getFilteredBookingForecast(factDetail);
		
		CurrencyItemC booking = new CurrencyItemC();
		for (BookingForecastObjC bookingForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if((booking.currencyCode != null && booking.currencyCode.equals("XXX"))) {
				Assert.assertEquals(booking.currencyCode, bookingForecastObj.p1Forecast.baseCurrencyCode);
			}		
			else if(booking.currencyCode != null && !booking.currencyCode.equals("")) {
				Assert.assertEquals(booking.currencyCode, bookingForecastObj.p1Forecast.baseCurrencyCode);
			}
			booking.currencyCode = bookingForecastObj.p1Forecast.baseCurrencyCode;
			booking.amount = booking.amount.add(bookingForecastObj.p1Forecast.baseCurrencyAmount);
		}
		
		return booking;
	}
	
	private CurrencyItemC GetBookingForecastTotalP2ForecastForDeliveryEngagement(final Fact factDetail) {
		Collection<BookingForecastObjC> filteredList = getFilteredBookingForecast(factDetail);
		
		CurrencyItemC booking = new CurrencyItemC();
		for (BookingForecastObjC bookingForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(booking.currencyCode != null && !booking.currencyCode.equals("")) {
				Assert.assertEquals(booking.currencyCode, bookingForecastObj.p2Forecast.baseCurrencyCode);
			}
			booking.currencyCode = bookingForecastObj.p2Forecast.baseCurrencyCode;
			booking.amount = booking.amount.add(bookingForecastObj.p2Forecast.baseCurrencyAmount);
		}
		
		return booking;
	}

	private CurrencyItemC GetBookingForecastTotalP3ForecastForDeliveryEngagement(final Fact factDetail) {
		Collection<BookingForecastObjC> filteredList = getFilteredBookingForecast(factDetail);
		
		CurrencyItemC booking = new CurrencyItemC();
		for (BookingForecastObjC bookingForecastObj : filteredList) {
			// check that the lines being summed have the same currency code - this always has to
			// hold true and finding one not the same is a failure condition
			if(booking.currencyCode != null && !booking.currencyCode.equals("")) {
				Assert.assertEquals(booking.currencyCode, bookingForecastObj.p3Forecast.baseCurrencyCode);
			}
			booking.currencyCode = bookingForecastObj.p3Forecast.baseCurrencyCode;
			booking.amount = booking.amount.add(bookingForecastObj.p3Forecast.baseCurrencyAmount);
		}
		
		return booking;
	}
	
	private Collection<BookingForecastObjC> getFilteredBookingForecast(final Fact factDetail) {
		@SuppressWarnings("unchecked")
		Collection<BookingForecastObjC> filteredList = performanceAnalysisDetail.bookingForecasts.stream().filter((o) -> {
	    	BookingForecastObjC candidate = (BookingForecastObjC)o;

	        return candidate.timePeriod.equals(factDetail.period)
		        && ((factDetail.domainClass == null || factDetail.domainClass.length() == 0) || factDetail.domainClass.equals(candidate.domainClass))
	        	&& ((factDetail.resourceName == null || factDetail.resourceName.length() == 0) || factDetail.resourceName.equals(candidate.resource))
	            && candidate.deliveryEngagement.equals(factDetail.deliveryEngagementName)
	            && ((factDetail.deliveryElementName == null) || (factDetail.deliveryElementName.equals(null) && candidate.deliveryElement != null && candidate.deliveryElement.equals("")) ||
	            		(factDetail.deliveryElementName != null && !factDetail.deliveryElementName.equals("") && candidate.deliveryElement.equals(factDetail.deliveryElementName)))
	        	&& ((factDetail.secondaryDeliveryElementName == null || factDetail.secondaryDeliveryElementName.length() == 0) || factDetail.secondaryDeliveryElementName.equals(candidate.secondaryDeliveryElement))
	            && candidate.businessUnit.equals(factDetail.businessUnitName);
		}).collect(Collectors.toList());
		return filteredList;
	}
}
