package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.executeSequenceWithRefreshRetry;

import java.util.List;

import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.KimbleOneTest.CleardownType;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GeneralOperations extends BasePageC {
	
	private static final String JOBS_PENDING_PARAMS = "allJobs=1&autorefresh=1" ;
	private static final String JOBS_PENDING_PARAMS_MULTI_THREAD = "allJobs=1&autorefresh=1&threads=10" ;
	private static final String OPERATIONAL_TIME_PERIODS_OPENED_FROM = "Operational Time Periods opened from";
	private static final String RESET_RESOURCE_SCHEDULE_JOBS_CREATED = "jobs created";
	private static final String RESET_RESOURCE_SCHEDULE_MESSAGE = "Re-calculating Schedule";

	private static final String RESET_RESOURCE_SCHEDULE_LINK = "//input[contains(@id, 'createAllResourcePeriodsBtn')]/parent::td/parent::tr//a";
	private static final String RESET_RESOURCE_SCHEDULE_CUSTOM_BATCH_TEXT = "//h3[normalize-space(text())='Custom Batch']";
	private static final String RESET_RESOURCE_SCHEDULE_100_PCT = "//div[@id='more-fifty']/following-sibling::div//div[normalize-space(text())='100%']";
	
	private static final String DEFAULT_EARLIEST_TEST_PERIOD = "01/01/2013";

	private static final String DEVELOPER_EDITION_ORG_TYPE = "Developer Edition";
	
	static final By jobStatusSelector = By.id("jobStatus");
	private static final String FIRST_PERIOD_START_DATE_INPUT = "input.first-open-period";

	int lastFailedJobCount = 0;
	
	@FindBy(css = "input[value='Run All']")
	private WebElement datacleardownRunAllButton;
	
	@FindBy(css = "span[id$='cleardownStatus']")
	private WebElement datacleardownStatusLabel;

	@FindBy(css = "span[id$='orgName']")
	private WebElement orgNameLabel;
	
	@FindBy(id="verifyOrgName")
	private WebElement verifyOrgNameField;
	
	@FindBy(css = "span[id$='orgType']")
	private WebElement orgTypeLabel;
	
	@FindBy(id = "jobStatus")
	private WebElement jobStatusLabel;
	
	@FindBy(css = "input[id$='openOperationalTimePeriodsFromDate']")
	private WebElement openOperationalTimePeriodsFromDateField;

	@FindBy(css = "input[id$='openOperationalTimePeriodsBtn']")
	private WebElement openOperationalTimePeriodsBtn;
	
	@FindBy(css = "input[id$='createAllResourcePeriodsBtn']")
	private WebElement createAllResourcePeriodsBtn;
	
	public GeneralOperations(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void DataCleardown(CleardownType type){
		if(type == null || type == CleardownType.none)
			return;
		runDataCleardown();
				
		if(type == CleardownType.dataandperiodreset) {
			reOpenOperationalPeriods();
			// workaround for #3196 no longer required, instead run jobs following the period re-open
			RunAllJobs();
		}
				
		if(type != CleardownType.datapreserveaccounts) {
			deleteAccounts();
		}
		
		deleteOpportunities();
	}
	
	public void runDataCleardown() {
		navigateToDatacleardown();
		invokeCleardown();
		// post data cleardown we must reset the resource schedule information
		resetResourceSchedule();
	}

	private void navigateToDatacleardown() {
		// short sleep before the cleardown as there is a new redirect straight after login
		theSH.sleep(2000);
		theSH.navigateToVFPage(PagesC.DATACLEARDOWN);
	}
	
	private void navigateToCustomSettings() {
		theSH.navigateToVFPage(PagesC.CUSTOMSETTINGS_PAGE);
	}

	private void invokeCleardown() {
		theSH.sleep(2000);
		// Only allow this test to run if in a Developer Edition Org!
		if(!SeleniumHelper.config.overrideOrgTypeCheck)
		{
			assert(orgTypeLabel.getText().equals(DEVELOPER_EDITION_ORG_TYPE));
		}
		
		// copy and paste the name of the org into the verify org name field
		verifyOrgNameField.sendKeys(orgNameLabel.getText());
		
		datacleardownRunAllButton.click();
		theSH.waitForElementToBeVisible(datacleardownStatusLabel, 600);  //longer timeout as org clear down can take a while
	}
	
	private void reOpenOperationalPeriods(String fromDate) {
		// go to new configuration settings page
		navigateToCustomSettings();

		theSH.waitForElementToBeClickable(By.cssSelector(FIRST_PERIOD_START_DATE_INPUT));
		theSH.setDatePickerField(By.cssSelector(FIRST_PERIOD_START_DATE_INPUT), fromDate);

		// wait for completion
		theSH.waitForPageToContainTextWithRetry("Time Periods opened from", 40);
		
		// back to datacleardown
		navigateToDatacleardown();
	}
	
	// default implementation used in automation tests to open from default of 01/01/2013
	public void reOpenOperationalPeriods() {
		reOpenOperationalPeriods(DEFAULT_EARLIEST_TEST_PERIOD);
	}
	
	public void reOpenOperationalPeriodsForSeedDate(String seedDate)
	{
		navigateToDatacleardown();
		reOpenOperationalPeriods(seedDate);
		RunAllJobs();
	}

	private void enableAutomationHelper() {
		theSH.displayElementsByClassName("automationHelper");
	}
	
	private void resetResourceSchedule() {
		createAllResourcePeriodsBtn.click();
		
		// wait for the screen to have reloaded and for the jobs created statement to be displayed
		theSH.waitForPageToContainTextWithRetry(RESET_RESOURCE_SCHEDULE_MESSAGE, 40);
		
		theSH.clickLink(By.xpath(RESET_RESOURCE_SCHEDULE_LINK));
		
		// wait for Custom Batch to run
		theSH.sleep(10000);
		
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			theSH.sleep(5000);
			theSH.waitForElementToBeVisible(By.xpath(RESET_RESOURCE_SCHEDULE_CUSTOM_BATCH_TEXT), 30);
			theSH.waitForElementToBeVisible(By.xpath(RESET_RESOURCE_SCHEDULE_100_PCT), 120);
		});
	}
	
	public void deleteAccounts() {
		AccountPageC.deleteAllAccounts(theSH);
	}
	
	public void deleteAccountInvoicingModel() {
		AccountPageC.deleteAllAccountInvoicingModelRecords(theSH);
	}
	
	public void deleteOpportunities() {
		OpportunityPageC.deleteAllOpportunities(theSH);
	}
	
	public void RunAllJobs() {
		String urlL = theSH.getCurrentUrl();
		if(!theSH.getCurrentUrl().toLowerCase().contains(PagesC.JOBSPENDING.toLowerCase()))
			theSH.navigateToVFPage(PagesC.JOBSPENDING, JOBS_PENDING_PARAMS);
		try{
			theSH.waitForElementToBeVisible(jobStatusSelector);
		}
		catch(Exception e){
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.waitForElementToBeVisible(jobStatusSelector);			
		}

		theSH.waitForPageLoadComplete(40);
		while(verifyHasJobsAndLogFailed(0));
		lastFailedJobCount = 0;
		if(theSH.isLightning()){
			theSH.goToUrl(urlL);
		}
	}

	public void RunAllJobsMultiThread() {
		String urlL = theSH.getCurrentUrl();
		if(!theSH.getCurrentUrl().toLowerCase().contains(PagesC.JOBSPENDING.toLowerCase()))
			theSH.navigateToVFPage(PagesC.JOBSPENDING, JOBS_PENDING_PARAMS_MULTI_THREAD);
		try{
			theSH.waitForElementToBeVisible(jobStatusSelector);
		}
		catch(Exception e){
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.waitForElementToBeVisible(jobStatusSelector);			
		}

		theSH.waitForPageLoadComplete(40);
		while(verifyHasJobsAndLogFailed(0));
		lastFailedJobCount = 0;
		if(theSH.isLightning()){
			theSH.goToUrl(urlL);
		}
	}	
	
	public boolean verifyHasJobsAndLogFailed(int retries) {
		try {
			List<WebElement> failedJobs = theSH.getWebElements(By.cssSelector("span[class$='Failed']") );
			
			for(int i = lastFailedJobCount; i < failedJobs.size(); i++)
			{
				// a job has failed - log this
				theSH.LogErrorMessageLine("Job FAILED during the runAllJobs step: Exception [" + failedJobs.get(i).getAttribute("title") + "]" );		
			}
			lastFailedJobCount = failedJobs.size();
			
			return theSH.hasVisibleWebElement(By.cssSelector("span.job"));
		} catch(Exception e) {
			if(retries > 4)
				throw e;
			try {
				theSH.ClickOKButtonOnOpenAlert();
			} catch(Exception ee) {}
			theSH.refreshBrowser();
			theSH.waitForElementToBeVisible(jobStatusSelector);
			theSH.waitForPageLoadComplete(40);
			return verifyHasJobsAndLogFailed(retries + 1);
		}
	}
}
