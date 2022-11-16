package kimble.automation.kimbleobjects.classic;

import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ActivityAssignmentBulkEditPageC extends BasePageC {
		
	private static final String SELECT_ID$_REV_RATE_CHANGE_MODE = "select[id$='revRateChangeMode']";

	private static final String INPUT_VALUE_FINISH = "input[value='Finish']";

	private static final String INPUT_ID_REVENUE_AMT = "input[id$='revRateAmount']";
	private static final String POP_UP_SAVE_BTN = "input[value='Save'][onclick='saveAssignmentUpdates()']";
	
	private static final String DASHBOARD_CONTAINER = "dashboardContainer";
	private static final String DELIVERY_ASSIGNMENT_CHARM = "//span[@tip-title='Start date before Proposal Acceptance date']";
	@FindBy(css="input[value='Next']")
	private WebElement step1NextBtn;

	@FindBy(css=POP_UP_SAVE_BTN)
	private WebElement step1SaveBtn;
	
	@FindBy(css=INPUT_ID_REVENUE_AMT)
	private WebElement revRateAmtField;
		
	@FindBy(css=INPUT_VALUE_FINISH)
	private WebElement step2FinishBtn;
	
	public ActivityAssignmentBulkEditPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void CompleteWizardWithDefaults(String deliveryGroupName)
	{
		theSH.clickLink(By.xpath(DELIVERY_ASSIGNMENT_CHARM));
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickable(By.cssSelector(POP_UP_SAVE_BTN));
		step1SaveBtn.click();
		theSH.waitForLightningSpinnerToBeHidden();
		
		theSH.waitForPageLoadComplete(30);
		// after saving the wizard jobs are run inline and then we are returned to the calling screen
		try{
			theSH.waitForElementToBeHidden(By.cssSelector(POP_UP_SAVE_BTN));
			theSH.waitForElementToBeClickable(By.cssSelector("input[class$='table-filter-term']"));
		}
		catch(Exception e){
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.waitForElementToBeClickable(By.cssSelector("input[class$='table-filter-term']"));
		}
	}

	public void ChangeRevenueRate(String mode, String byValue, String deliveryGroupName) {
		theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID_REVENUE_AMT));
		theSH.waitForElementToBeClickable(By.cssSelector(SELECT_ID$_REV_RATE_CHANGE_MODE));
		theSH.selectByVisibleText(By.cssSelector(SELECT_ID$_REV_RATE_CHANGE_MODE), mode);

		revRateAmtField.sendKeys(byValue);
		step1NextBtn.click();

		theSH.waitForElementToBeClickable(By.cssSelector(INPUT_VALUE_FINISH));
		step2FinishBtn.click();
		
		// after saving the wizard jobs are run inline and then we are returned to the
		// delivery group home screen
		theSH.waitForLightningSpinnerToBeHidden();
		try{
			theSH.waitForElementToBeClickable(By.id(DASHBOARD_CONTAINER), 120);
		} catch(Exception e){
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.waitForElementToBeClickable(By.id(DASHBOARD_CONTAINER), 120);
		}
	}	
}
