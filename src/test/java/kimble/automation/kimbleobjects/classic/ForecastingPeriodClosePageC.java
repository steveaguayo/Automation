package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.Date;

import kimble.automation.domain.PerformanceAnalysis;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ForecastingPeriodClosePageC extends BasePageC {
	
	static final By unactualisedItemsError = By.xpath("//div[normalize-space(text())='There are Cost and/or Revenue Items which "
			+ "have not been actualised for this Period. This may be because operational analysis processes are still running. "
			+ "Please try again later. If the problem persists contact your Administrator.']");
	
	static final By unactualisedItemsCloseError = By.xpath("//li[normalize-space(text())='There are 1 Usage, Cost and/or Revenue Items which have not been actualised for this Period."
			+ " This may be because operational analysis processes are still running. Please try again later."
			+ " If the problem persists contact your Administrator.']");
	
	private static final String INPUT_ID$_CLOSE_FORECASTING_PERIOD_BTN = "input[id$='closeForecastingPeriodBtn']";
	private static final String INPUT_ID$_REOPEN_FORECASTING_PERIOD_BTN = "input[id$='reOpenForecastingPeriodBtn']";

	private static final String CLOSE_FORECASTING = "Close Forecasting";
	private static final String CLOSE_FORECASTING_HOVER = "//a[normalize-space(text())='Close Forecasting']/..//parent::li[contains(@class, 'has-sub ')]";

	private static final String PERIOD_NAME_TOKEN = "<PERIODNAME>";
	private static final String BU_NAME_TOKEN = "<BUNAME>";
	private static final String CLOSE_PERIOD_LINK = "//a[contains(@href, 'ForecastingPeriodClose')][normalize-space(text()[2])='" + BU_NAME_TOKEN + " [" + PERIOD_NAME_TOKEN + "]" + "']";
	private static final String CLOSE_PERIOD_LINK_NO_PERIOD = "//a[contains(@href, 'ForecastingPeriodClose')][contains(text()[2], '" + BU_NAME_TOKEN + "')]";

	@FindBy(linkText = "Period Management")
	private WebElement periodManagementLink;

	public ForecastingPeriodClosePageC(SeleniumHelper seleniumHelperInstance) {
		super(seleniumHelperInstance);
	}
	
	public void NavigateToForecastingPeriodClose() {
		theSH.goToAllTabsPage();
		periodManagementLink.click();
	}
	
	public void SelectBusinessUnitGroup(String businessUnitGroupName) {
		theSH.clickLink(By.linkText(businessUnitGroupName));
		theSH.sleep(1000);
	}
	
	public static void validateForecastingPeriodExists(SeleniumHelper sh, String businessUnitGroupName, String forecastingPeriod, int waitSecs) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			sh.forceOpenCollapsedMenus(true);
			waitClickable(sh, By.xpath(CLOSE_PERIOD_LINK.replace(BU_NAME_TOKEN, businessUnitGroupName).replace(PERIOD_NAME_TOKEN, forecastingPeriod)), waitSecs);
		});
	}
	
	public static void navigateToForecastingPeriodForDate(SeleniumHelper sh, Date date) {
		TrackingPeriodClosePageC.navigateToTrackingPeriodForDate(sh, date);
	}
	
	public static void navigateToForecastingPeriodByGroup(SeleniumHelper sh, String businessUnitGroupName) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			// WORKAROUND UNTIL THE PERIOD MANAGEMENT SCREEN IS CHANGED TO TAKE A FILTER
			sh.waitForElementToBeClickable(By.partialLinkText(CLOSE_FORECASTING));
			sh.hoverOnElement(By.partialLinkText(CLOSE_FORECASTING));		
			
			sh.clickLink(By.id("menu-collapse-toggler"));
			sh.clickLink(By.xpath(CLOSE_PERIOD_LINK_NO_PERIOD.replace(BU_NAME_TOKEN, businessUnitGroupName)));

			sh.sleep(1000);
			
			// not using page factory pattern as this screen recycles the same button which can mean stale element exceptions
			sh.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_CLOSE_FORECASTING_PERIOD_BTN));
		});
	}

	public static void navigateToForecastingPeriod(SeleniumHelper sh, String businessUnitGroupName, String forecastingPeriod) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			// WORKAROUND UNTIL THE PERIOD MANAGEMENT SCREEN IS CHANGED TO TAKE A FILTER
			sh.waitForElementToBeClickable(By.partialLinkText(CLOSE_FORECASTING));
			sh.hoverOnElement(By.partialLinkText(CLOSE_FORECASTING));		
			
			try{
				sh.clickLink(By.id("menu-collapse-toggler"));
			} catch(Exception e){
				// do nothing
			}
			
			sh.clickLink(By.xpath(CLOSE_PERIOD_LINK.replace(BU_NAME_TOKEN, businessUnitGroupName).replace(PERIOD_NAME_TOKEN, forecastingPeriod)));
			sh.waitForLightningSpinnerToBeHidden();

			sh.sleep(1000);
			
			// not using page factory pattern as this screen recycles the same button which can mean stale element exceptions
			sh.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_CLOSE_FORECASTING_PERIOD_BTN));
		});
	}

	public void CloseForecastingPeriodWhereForecastComplete() {
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			if(exists(theSH, By.cssSelector(INPUT_ID$_REOPEN_FORECASTING_PERIOD_BTN), 3))
				return;
			click(theSH, By.cssSelector(INPUT_ID$_CLOSE_FORECASTING_PERIOD_BTN));
//			waitClickable(theSH, alertOk, 20);
//			click(theSH, alertOk);
			theSH.alertClickOk();
			theSH.waitForLightningSpinnerToBeHidden();
			waitClickable(theSH, By.cssSelector(INPUT_ID$_REOPEN_FORECASTING_PERIOD_BTN), 3);
			if(exists(theSH, unactualisedItemsError, 3))
				throw new RuntimeException("There are unactualised items in the forecasting period");
			waitClickable(theSH, By.cssSelector(INPUT_ID$_REOPEN_FORECASTING_PERIOD_BTN), 50);
		});
	}
	
	public static void openForecastingPeriod(SeleniumHelper sh, boolean force) {
		try {
			clickAndWaitSequence(sh, 60,
				By.cssSelector(INPUT_ID$_REOPEN_FORECASTING_PERIOD_BTN),
//				alertOk,
				sh.alertOkButton,
				By.cssSelector(INPUT_ID$_CLOSE_FORECASTING_PERIOD_BTN)
			);
		} catch(Exception e) {
			if(force)
				throw new RuntimeException("Failed to re-open the forecasting period");
		}
	}

	public static void VerifyErrorOnForecastingClose(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			click(sh, By.cssSelector(INPUT_ID$_CLOSE_FORECASTING_PERIOD_BTN));
//			waitClickable(sh, alertOk, 20);
//			click(sh, alertOk);
			sh.alertClickOk();
			sh.waitForLightningSpinnerToBeHidden();
			
			sh.waitForElementToBeVisible(unactualisedItemsCloseError, 20);
			
			waitClickable(sh, By.cssSelector(INPUT_ID$_CLOSE_FORECASTING_PERIOD_BTN), 50);
		});
	}

	static String performanceAnalysisUrl = "";
	
	public static void createPARecord(SeleniumHelper sh, PerformanceAnalysis pa, boolean isNewPA) {
		
				if(isNewPA == true) {
					sh.navigateToVFPage("ObjectLinks"); 
					
					if(sh.isPackagedOrg()) sh.clickAndWait(By.linkText("KimbleOne__PerformanceAnalysis__c"), By.xpath("//input[@value='New Performance Analysis']"), 15);
					else sh.clickAndWait(By.linkText("PerformanceAnalysis__c"), By.xpath("//input[@value='New Performance Analysis']"), 15);
					
					sh.clickAndWait(By.xpath("//input[@value='New Performance Analysis']"),  By.xpath("//input[@value=' Save ']") ,  15);
				}
				else {
					sh.goToUrl(performanceAnalysisUrl);
					sh.clickAndWait(By.xpath("//input[@value=' Edit ']"),  By.xpath("//input[@value=' Save ']") ,  15);
				}
				
				if(isNewPA == true) {
					By 
					analysisNameInput = sh.getSelectorBasedOnLabel(KBy.label("Analysis Name")),
					timePeriodInput = sh.getSelectorBasedOnLabel(KBy.label("Time Period")),
					domainClassInput = sh.getSelectorBasedOnLabel(KBy.label("Domain Class")),
					accountInput = sh.getSelectorBasedOnLabel(KBy.label("Account")),
					deliveryGroupInput = sh.getSelectorBasedOnLabel(KBy.label("Delivery Group")),
					deliveryElementInput = sh.getSelectorBasedOnLabel(KBy.label("Delivery Element")),
					actualInput = sh.getSelectorBasedOnLabel(KBy.label("Actual")),
					p1ForecastInput = sh.getSelectorBasedOnLabel(KBy.label("P1 Forecast")),
					p2ForecastInput = sh.getSelectorBasedOnLabel(KBy.label("P2 Forecast")),
					p3ForecastInput = sh.getSelectorBasedOnLabel(KBy.label("P3 Forecast")),
					businessUnitInput = sh.getSelectorBasedOnLabel(KBy.label("Business Unit")),
					factInput = sh.getSelectorBasedOnLabel(KBy.label("Fact"));
					
					clearAndInputText(sh, analysisNameInput, pa.name);
					clearAndInputText(sh, timePeriodInput, pa.timePeriod);
					clearAndInputText(sh, domainClassInput, pa.domainClass);
					clearAndInputText(sh, accountInput, pa.account);
					clearAndInputText(sh, deliveryGroupInput, pa.deliveryGroup);
					clearAndInputText(sh, deliveryElementInput, pa.deliveryElement);
					clearAndInputText(sh, actualInput, pa.actual);
					clearAndInputText(sh, p1ForecastInput, pa.p1Forecast);
					clearAndInputText(sh, p2ForecastInput, pa.p2Forecast);
					clearAndInputText(sh, p3ForecastInput, pa.p3Forecast);
					clearAndInputText(sh, businessUnitInput, pa.businessUnit);
					clearAndInputText(sh, factInput, pa.fact);
					
					
				}
				else {
					By 
					actualInput = sh.getSelectorBasedOnLabel(KBy.label("Actual"));
					clearAndInputText(sh, actualInput, pa.actual);
				}

				sh.clickAndWait(By.xpath("//input[@value=' Save ']"),  By.xpath("//input[@value=' Edit ']") ,  15);
				
				performanceAnalysisUrl = sh.getCurrentUrl();
		
	}
}
