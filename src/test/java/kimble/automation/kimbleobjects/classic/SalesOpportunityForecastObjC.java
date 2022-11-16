package kimble.automation.kimbleobjects.classic;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

public class SalesOpportunityForecastObjC extends PerformanceAnalysisObjC {

	public List<BookingForecastObjC> bookingForecasts = new ArrayList();
	public List<RevenueForecastObjC> revenueForecasts = new ArrayList();
	public List<CostForecastObjC> costForecasts = new ArrayList();
	public List<ReceivablesForecastObjC> receivablesForecasts = new ArrayList();
	public List<InvoicedAmountsObjC> invoicedAmounts = new ArrayList();

	public SalesOpportunityForecastObjC(List<WebElement> bookingForecastRows,
			List<WebElement> revenueForecastRows,
			List<WebElement> costForecastRows,
			List<WebElement> receivablesForecastRows,
			List<WebElement> invoiceAmountRows) {
		processBookingForecastRows(bookingForecastRows);
		processRevenueForecastRows(revenueForecastRows);
		processCostForecastRows(costForecastRows);
		processReceivablesForecastRows(receivablesForecastRows);
		processInvoiceAmountRows(invoiceAmountRows);
	}

	private void processInvoiceAmountRows(List<WebElement> invoiceAmountRows) {
		InvoicedAmountsObjC newInvoicedAmounts = new InvoicedAmountsObjC();
		int columnCounter = 1;

		for (WebElement webElement : invoiceAmountRows) {

			switch (columnCounter) {
			case 1:
				newInvoicedAmounts = new InvoicedAmountsObjC();
				newInvoicedAmounts.recordID = webElement.getText();
				columnCounter++;
				break;
			case 2:
				newInvoicedAmounts.timePeriod = webElement.getText();
				columnCounter++;
				break;
			case 3:
				newInvoicedAmounts.domainClass = webElement.getText();
				columnCounter++;
				break;
			case 4:
				newInvoicedAmounts.businessUnit = webElement.getText();
				columnCounter++;
				break;
			case 5:
				newInvoicedAmounts.account = webElement.getText();
				columnCounter++;
				break;
			case 6:
				newInvoicedAmounts.deliveryEngagement = webElement.getText();
				columnCounter++;
				break;
			case 7:
				newInvoicedAmounts.deliveryElement = webElement.getText();
				columnCounter++;
				break;
			case 8:
				newInvoicedAmounts.resource = webElement.getText();
				columnCounter++;
				break;
			case 9:
				newInvoicedAmounts.actual = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 10:
				newInvoicedAmounts.p1Forecast = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 11:
				newInvoicedAmounts.p2Forecast = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 12:
				newInvoicedAmounts.p3Forecast = processDisplayCurrency(webElement
						.getText());
				invoicedAmounts.add(newInvoicedAmounts);
				columnCounter = 1;
				break;
			default:
				break;
			}
		}
	}

	private void processReceivablesForecastRows(
			List<WebElement> receivablesForecastRows) {
		ReceivablesForecastObjC newReceivablesForecast = new ReceivablesForecastObjC();
		int columnCounter = 1;

		for (WebElement webElement : receivablesForecastRows) {

			switch (columnCounter) {
			case 1:
				newReceivablesForecast = new ReceivablesForecastObjC();
				newReceivablesForecast.recordID = webElement.getText();
				columnCounter++;
				break;
			case 2:
				newReceivablesForecast.deliveryEngagement = webElement
						.getText();
				columnCounter++;
				break;
			case 3:
				newReceivablesForecast.deliveryElement = webElement.getText();
				columnCounter++;
				break;
			case 4:
				newReceivablesForecast.startDate = webElement.getText();
				columnCounter++;
				break;
			case 5:
				newReceivablesForecast.endDate = webElement.getText();
				columnCounter++;
				break;
			case 6:
				newReceivablesForecast.receivableDate = webElement.getText();
				columnCounter++;
				break;
			case 7:
				newReceivablesForecast.forecastRevenue = processDisplayCurrency(webElement
						.getText());
				receivablesForecasts.add(newReceivablesForecast);
				columnCounter = 1;
				break;
			default:
				break;
			}
		}
	}

	private void processCostForecastRows(List<WebElement> costForecastRows) {
		CostForecastObjC newCostForecast = new CostForecastObjC();
		int columnCounter = 1;

		for (WebElement webElement : costForecastRows) {

			switch (columnCounter) {
			case 1:
				newCostForecast = new CostForecastObjC();
				newCostForecast.recordID = webElement.getText();
				columnCounter++;
				break;
			case 2:
				newCostForecast.timePeriod = webElement.getText();
				columnCounter++;
				break;
			case 3:
				newCostForecast.domainClass = webElement.getText();
				columnCounter++;
				break;
			case 4:
				newCostForecast.businessUnit = webElement.getText();
				columnCounter++;
				break;
			case 5:
				newCostForecast.account = webElement.getText();
				columnCounter++;
				break;
			case 6:
				newCostForecast.deliveryEngagement = webElement.getText();
				columnCounter++;
				break;
			case 7:
				newCostForecast.deliveryElement = webElement.getText();
				columnCounter++;
				break;
			case 8:
				newCostForecast.secondaryDeliveryElement = webElement.getText();
				columnCounter++;
				break;
			case 9:
				newCostForecast.resource = webElement.getText();
				columnCounter++;
				break;
			case 10:
				newCostForecast.actual = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 11:
				newCostForecast.p1Forecast = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 12:
				newCostForecast.p2Forecast = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 13:
				newCostForecast.p3Forecast = processDisplayCurrency(webElement
						.getText());
				costForecasts.add(newCostForecast);
				columnCounter = 1;
				break;
			default:
				break;
			}
		}
	}

	private void processRevenueForecastRows(List<WebElement> revenueForecastRows) {
		RevenueForecastObjC newRevenueForecast = new RevenueForecastObjC();
		int columnCounter = 1;

		for (WebElement webElement : revenueForecastRows) {

			switch (columnCounter) {
			case 1:
				newRevenueForecast = new RevenueForecastObjC();
				newRevenueForecast.recordID = webElement.getText();
				columnCounter++;
				break;
			case 2:
				newRevenueForecast.timePeriod = webElement.getText();
				columnCounter++;
				break;
			case 3:
				newRevenueForecast.domainClass = webElement.getText();
				columnCounter++;
				break;
			case 4:
				newRevenueForecast.businessUnit = webElement.getText();
				columnCounter++;
				break;
			case 5:
				newRevenueForecast.account = webElement.getText();
				columnCounter++;
				break;
			case 6:
				newRevenueForecast.deliveryEngagement = webElement.getText();
				columnCounter++;
				break;
			case 7:
				newRevenueForecast.deliveryElement = webElement.getText();
				columnCounter++;
				break;
			case 8:
				newRevenueForecast.secondaryDeliveryElement = webElement.getText();
				columnCounter++;
				break;
			case 9:
				newRevenueForecast.resource = webElement.getText();
				columnCounter++;
				break;
			case 10:
				newRevenueForecast.actual = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 11:
				newRevenueForecast.p1Forecast = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 12:
				newRevenueForecast.p2Forecast = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 13:
				newRevenueForecast.p3Forecast = processDisplayCurrency(webElement
						.getText());
				revenueForecasts.add(newRevenueForecast);
				columnCounter = 1;
				break;
			default:
				break;
			}
		}
	}

	private void processBookingForecastRows(List<WebElement> bookingForecastRows) {
		BookingForecastObjC newBookingForecast = new BookingForecastObjC();
		int columnCounter = 1;

		for (WebElement webElement : bookingForecastRows) {

			switch (columnCounter) {
			case 1:
				newBookingForecast = new BookingForecastObjC();
				newBookingForecast.recordID = webElement.getText();
				columnCounter++;
				break;
			case 2:
				newBookingForecast.timePeriod = webElement.getText();
				columnCounter++;
				break;
			case 3:
				newBookingForecast.domainClass = webElement.getText();
				columnCounter++;
				break;
			case 4:
				newBookingForecast.businessUnit = webElement.getText();
				columnCounter++;
				break;
			case 5:
				newBookingForecast.account = webElement.getText();
				columnCounter++;
				break;
			case 6:
				newBookingForecast.deliveryEngagement = webElement.getText();
				columnCounter++;
				break;
			case 7:
				newBookingForecast.deliveryElement = webElement.getText();
				columnCounter++;
				break;
			case 8:
				newBookingForecast.secondaryDeliveryElement = webElement.getText();
				columnCounter++;
				break;
			case 9:
				newBookingForecast.resource = webElement.getText();
				columnCounter++;
				break;
			case 10:
				newBookingForecast.actual = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 11:
				newBookingForecast.p1Forecast = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 12:
				newBookingForecast.p2Forecast = processDisplayCurrency(webElement
						.getText());
				columnCounter++;
				break;
			case 13:
				newBookingForecast.p3Forecast = processDisplayCurrency(webElement
						.getText());
				bookingForecasts.add(newBookingForecast);
				columnCounter = 1;
				break;
			default:
				break;
			}
		}
	}
}
