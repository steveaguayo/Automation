package kimble.automation.kimbleobjects.classic;

import kimble.automation.domain.ActivityAssignmentTemplate;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ActivityAssignmentTemplatePageC extends BasePageC {
		
	private static final String SELECT_ID$_ACTIVITY_ASSIGNMENT_TEMPLATE_RESOURCE_SELECT = "select[id$='activityAssignmentTemplateResource_SELECT']";
	private static final String SELECT_ID$_ACTIVITY_ASSIGNMENT_TEMPLATE_ROLE_SELECT = "select[id$='activityAssignmentTemplateRole_SELECT']";
	private static final String ACTIVITY_ASSIGNMENT_TEMPLATES_POPUP = "ActivityAssignmentTemplatesPopup";
	private static final String INPUT_VALUE_NEW_ASSIGNMENT_TEMPLATE = "input[value='New Assignment Template']";

	@FindBy(css = INPUT_VALUE_NEW_ASSIGNMENT_TEMPLATE)
	private WebElement newAssignmentTemplateButton;
	
	@FindBy(css = "input[id$='activityAssignmentTemplateName']")
	private WebElement activityAssignmentTemplateNameField;	
		
	@FindBy(css = "input[id$='fixedUsageCheckbox']")
	private WebElement fixedWorkCheckbox;

	@FindBy(css = "input[id$='activityAssignmentTemplateDefinedEffortStartDateOffset']")
	private WebElement activityAssignmentTemplateDefinedEffortStartDateOffsetField;

	@FindBy(css = "input[id$='activityAssignmentTemplateDefinedEffortEndDate']")
	private WebElement activityAssignmentTemplateDefinedEffortEndDateField;
	
	@FindBy(css = "input[id$='definedEffortRemainingEffort']")
	private WebElement definedEffortRemainingEffortField;

	@FindBy(css = "input[id$='activityAssignmentTemplateDefinedEffortUtilisation']")
	private WebElement activityAssignmentTemplateDefinedEffortUtilisationField;
	
	@FindBy(id="DerivedEndDate")
	private WebElement derivedEndDateRadioButton;
	
	@FindBy(id="DerivedUtilisation")
	private WebElement derivedUtilisationRadioButton;

	// commented until a test that needs the "Other" tab
//	@FindBy(id="AssignmentEntryOther")
//	private WebElement assignmentEntryOtherButton;
//	
//	@FindBy(css = "input[id$='activityAssignmentTemplateOtherStartDateOffset']")
//	private WebElement activityAssignmentTemplateOtherStartDateOffsetField;
//	
//	@FindBy(css = "input[id$='activityAssignmentTemplateOtherUtilisation']")
//	private WebElement activityAssignmentTemplateOtherUtilisationField;
//	
//	@FindBy(css = "input[id$='activityAssignmentTemplateOtherEndingOn']")
//	private WebElement activityAssignmentTemplateOtherEndingOnField;

	@FindBy(css = "input[id$='saveActivityAssignmentTemplateButton']")
	private WebElement saveActivityAssignmentTemplateButton;
	
	public ActivityAssignmentTemplatePageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void CreateNew(ActivityAssignmentTemplate activityAssignmentTemplateDetails){
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_VALUE_NEW_ASSIGNMENT_TEMPLATE), 20);
		newAssignmentTemplateButton.click();
		
		// wait for the resource selector to appear as this indicates the screen is loaded
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(SELECT_ID$_ACTIVITY_ASSIGNMENT_TEMPLATE_RESOURCE_SELECT), 20);
		
		activityAssignmentTemplateNameField.sendKeys(activityAssignmentTemplateDetails.name);
		theSH.selectByVisibleText(By.cssSelector(SELECT_ID$_ACTIVITY_ASSIGNMENT_TEMPLATE_RESOURCE_SELECT), activityAssignmentTemplateDetails.resource);
		theSH.selectByVisibleText(By.cssSelector(SELECT_ID$_ACTIVITY_ASSIGNMENT_TEMPLATE_ROLE_SELECT), activityAssignmentTemplateDetails.activityRole);
		
		activityAssignmentTemplateDefinedEffortStartDateOffsetField.sendKeys(activityAssignmentTemplateDetails.startDateOffset);
		definedEffortRemainingEffortField.sendKeys(activityAssignmentTemplateDetails.remainingEffort);
		
		if(activityAssignmentTemplateDetails.entrySpec.equals("derivedEndDate")) {
			derivedEndDateRadioButton.click();
			activityAssignmentTemplateDefinedEffortUtilisationField.sendKeys(activityAssignmentTemplateDetails.utilisationPct);	
		} else if (activityAssignmentTemplateDetails.entrySpec.equals("derivedUtilisation")){
			derivedUtilisationRadioButton.click();
			activityAssignmentTemplateDefinedEffortEndDateField.sendKeys(activityAssignmentTemplateDetails.endDate);			
		}
		
		if(activityAssignmentTemplateDetails.fixedWork != null && activityAssignmentTemplateDetails.fixedWork.equals("true")){
			fixedWorkCheckbox.click();
		}
		
		saveActivityAssignmentTemplateButton.click();
		
		// the screen takes a while to close after clicking save so wait for it to have closed
		theSH.waitForElementToBeHidden(By.id(ACTIVITY_ASSIGNMENT_TEMPLATES_POPUP));
	}
}
