package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.Date;

import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.kimbleobjects.lightning.GeneralOperationsZ;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TrackingPeriodClosePageC extends BasePageC {
	
	private static final String CLOSE_TRACKING = "Close Tracking";
	private static final String RESOURCED_LINK = "Resourced";
	private static final String INPUT_ID$_CLOSE_TRACKING_PERIOD_BTN = "input[id$='closeTrackingPeriodBtn']";
	private static final String REMOVE_USAGE_HOVER = "//*[@id='action-buttons']//div";
	private static final String INPUT_ID$_REMOVE_ALL_USAGE_BTN = "//*[@id='select-page']";
	private static final String INPUT_ID$_REOPEN_TRACKING_PERIOD_BTN = "input[id$='reOpenTrackingPeriodBtn']";
	
	private static final String PERIOD_NAME_TOKEN = "<PERIODNAME>";
	private static final String BU_NAME_TOKEN = "<BUNAME>";
	private static final String CLOSE_PERIOD_LINK = BU_NAME_TOKEN + " [" + PERIOD_NAME_TOKEN + "]";
//	private static final String CLOSE_PERIOD_LINK_NO_PERIOD = "//a[contains(@href, 'TrackingPeriodClose?filterid=')][contains(text()[2], '" + BU_NAME_TOKEN + "')]";
	private static final String CLOSE_PERIOD_LINK_NO_PERIOD = "//a[contains(@href, 'TrackingPeriodClose?filterid=')][contains(text(), '" + BU_NAME_TOKEN + "')]";
	
	static final By 
	datePicker = By.cssSelector("input[class$='datepicker go-date-btn hasDatepicker']"),
	resourcedUsageExists = By.xpath("//div[@id='graphValue1divForActiveUsageSummaryGraph'][not(text()='0')]"),
	resourcedUsageDoesntExists = By.xpath("//div[@id='graphValue1divForActiveUsageSummaryGraph'][text()='0']")
	;
	
	@FindBy(linkText = "Period Management")
	private WebElement periodManagementLink;

	public TrackingPeriodClosePageC(SeleniumHelper seleniumHelperInstance) {
		super(seleniumHelperInstance);
	}
	
	public void NavigateToPeriodManagement() {
		if(theSH.isLightning()){
			GeneralOperationsZ.navigateFromAnywhereToTab(theSH, "Period Management");
		}
		else{
			theSH.goToAllTabsPage();
			periodManagementLink.click();			
		}
	}
	
	public static void navigateToTrackingPeriod(SeleniumHelper sh, String businessUnitGroupName, String trackingPeriodName) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			sh.waitForElementToBeClickable(By.partialLinkText(CLOSE_TRACKING));
			sh.forceOpenCollapsedMenus(true);
			sh.clickLink(By.linkText(CLOSE_PERIOD_LINK.replace(BU_NAME_TOKEN, businessUnitGroupName).replace(PERIOD_NAME_TOKEN, trackingPeriodName)));
			sh.waitForLightningSpinnerToBeHidden();
		});
	}
	
	public static void CloseTrackingPeriodWhereForecastComplete(SeleniumHelper sh, String businessUnitGroupName, String trackingPeriodName) {
		// not using page factory pattern as this screen recycles the same button which can mean stale element exceptions
		if(sh.isLightning()){
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, By.cssSelector(INPUT_ID$_CLOSE_TRACKING_PERIOD_BTN), 10);
			click(sh, By.cssSelector(INPUT_ID$_CLOSE_TRACKING_PERIOD_BTN));
			
//			waitClickable(sh, alertOk, 10);
//			click(sh, alertOk);
			sh.alertClickOk();
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, By.cssSelector(INPUT_ID$_REOPEN_TRACKING_PERIOD_BTN), 10);
		}
		else{
			clickAndWaitSequence(sh, 20,
				/* click close				*/	By.cssSelector(INPUT_ID$_CLOSE_TRACKING_PERIOD_BTN),
//				/* click ok on alert		*/	alertOk,
				/* click ok on alert		*/	sh.alertOkButton,
				/* wait for re-open button	*/	By.cssSelector(INPUT_ID$_REOPEN_TRACKING_PERIOD_BTN)
			);			
		}

	}
	
	public void openTrackingPeriod(String businessUnitGroupName, String trackingPeriodName) {
		By reopenButton = By.cssSelector(INPUT_ID$_REOPEN_TRACKING_PERIOD_BTN);
		By closeButton = By.cssSelector(INPUT_ID$_CLOSE_TRACKING_PERIOD_BTN);
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			clickAndWaitSequence(theSH, 20,
			/* click re-open						*/	reopenButton,
//			/* click ok on the confirmation alert	*/	alertOk,
			/* click ok on the confirmation alert	*/  theSH.alertOkButton,
			/* wait for close button to appear		*/	closeButton
			);
		});
	}
	
	public static void validateTrackingPeriodExists(SeleniumHelper sh, String businessUnitGroupName, String trackingPeriodName, int waitSecs) { 
		sh.waitForLightningSpinnerToBeHidden();
		sh.forceOpenCollapsedMenus(true);
		By link = By.linkText(CLOSE_PERIOD_LINK.replace(BU_NAME_TOKEN, businessUnitGroupName).replace(PERIOD_NAME_TOKEN, trackingPeriodName));
		sh.waitForElementToBeClickable(link, waitSecs);
	}

	public void validateTrackingPeriodOpenable(String businessUnitGroupName, String trackingPeriodName, int waitSecs) { 
		By reopenButton = By.cssSelector(INPUT_ID$_REOPEN_TRACKING_PERIOD_BTN);
		theSH.waitForElementToBeClickable(reopenButton, waitSecs);
	}

	public static void RemoveUsage(SeleniumHelper sh, String businessUnitGroupName, String trackingPeriodName) {
		sh.waitForLightningSpinnerToBeHidden();
		waitClickable(sh, By.partialLinkText(CLOSE_TRACKING), 20);
		hoverOn(sh, By.partialLinkText(CLOSE_TRACKING));
		if(!sh.isLightning()){
//			sh.clickLink(By.linkText(CLOSE_PERIOD_LINK.replace(BU_NAME_TOKEN, businessUnitGroupName).replace(PERIOD_NAME_TOKEN, trackingPeriodName)), 20);
			sh.clickLink(By.xpath(CLOSE_PERIOD_LINK_NO_PERIOD.replace(BU_NAME_TOKEN, businessUnitGroupName)), 20);
			
			try{
				sh.clickAndWait(By.linkText(RESOURCED_LINK), By.xpath(REMOVE_USAGE_HOVER), 10);
				sh.clickAndWait(By.xpath(REMOVE_USAGE_HOVER), By.xpath(INPUT_ID$_REMOVE_ALL_USAGE_BTN), 10);
				click(sh, By.xpath(INPUT_ID$_REMOVE_ALL_USAGE_BTN));
//				waitClickable(sh, alertOk, 10);
//				click(sh, alertOk);
				sh.alertClickOk();
				sh.waitMilliseconds(5000);
				sh.refreshBrowser();
			} catch(Exception e){/*do nothing as no usage exists so no remove usage button*/}
			
			sh.waitForElementToBeClickable(resourcedUsageDoesntExists);
		}
		else{
			waitClickable(sh, By.linkText(CLOSE_PERIOD_LINK.replace(BU_NAME_TOKEN, businessUnitGroupName).replace(PERIOD_NAME_TOKEN, trackingPeriodName)), 10);
			click(sh, By.linkText(CLOSE_PERIOD_LINK.replace(BU_NAME_TOKEN, businessUnitGroupName).replace(PERIOD_NAME_TOKEN, trackingPeriodName)));
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, By.linkText(RESOURCED_LINK), 10);
			click(sh, By.linkText(RESOURCED_LINK));
			sh.waitForLightningSpinnerToBeHidden();
			try{
				sh.hoverOnElement(By.xpath(REMOVE_USAGE_HOVER));
				waitClickable(sh, By.xpath(INPUT_ID$_REMOVE_ALL_USAGE_BTN), 10);
				click(sh, By.xpath(INPUT_ID$_REMOVE_ALL_USAGE_BTN));
//				waitClickable(sh, alertOk, 10);
//				click(sh, alertOk);
				sh.alertClickOk();
				sh.waitForLightningSpinnerToBeHidden();
			} catch(Exception e){/*do nothing as no usage exists so no remove usage button*/}
			
			waitClickable(sh, resourcedUsageDoesntExists, 10);
		}
	}
	
	public static void navigateToTrackingPeriodForGroup(SeleniumHelper sh, String groupName) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForLightningSpinnerToBeHidden();
			sh.waitForElementToBeClickable(By.partialLinkText(CLOSE_TRACKING));
			sh.forceOpenCollapsedMenus(true);
			sh.clickLink(By.xpath(CLOSE_PERIOD_LINK_NO_PERIOD.replace(BU_NAME_TOKEN, groupName)));
		});
	}
	
	public static void navigateToTrackingPeriodForDate(SeleniumHelper sh, Date date) {
		JQueryDatePickerC.navigateToDay(sh, datePicker, date);
	}
}
