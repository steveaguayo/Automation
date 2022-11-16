package kimble.automation.kimbleobjects.classic;

import java.util.List;

import kimble.automation.domain.Task;
import kimble.automation.domain.TaskAssignment;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TrackingPlanTasksPageC extends BasePageC {
	
	private static final String A_TITLE_NEXT_PLAN = "a[title='Next Plan']";
	private static final String INPUT_VALUE_CLOSE = "input[value='Close']";
	private static final String A_CLASS$_ADD_TASK_BUTTON = "a[class*='addTaskButton']";
	private static final String INPUT_ID$_SAVE_BUTTON = "a[id$='saveButton']";
	private static final String INPUT_ID$_EDIT_FIXED = "input[id$='edit-fixed']";
	private static final String TASK_DETAIL_OK_BTN = "input[id$='TaskOkButton']";
	
	private static final String UNSUBMITTED_OR_UNAPPROVED_TIME_EXISTS = "Unsubmitted or unapproved time exists for the Period covered by this Tracking Plan. Approve all time before closing the Tracking Plan";

	private static final String NEW_TASK_PLACEHOLDER = "--NEW--";
	private static final String INPUT_ID$_TASK_NAME_FIELD = "input[id='edit-task-name']";
	private static final String INPUT_ID$_TASK_STARTDATE_FIELD = "input[id='edit-task-start-date']";
	private static final String INPUT_ID$_TASK_ENDDATE_FIELD = "input[id='edit-task-end-date']";
	
	private static final String EDIT_TASK_DETAILS_POPUP = "EditTaskContainer";
	
	private static final String RESOURCENAMETOKEN = "<resourceName>";
	private static final String XPATH_RESOURCE_ALLOCATE_CHECKBOX = "//td[contains(text(),\"" + RESOURCENAMETOKEN + "\")]/preceding-sibling::td/input";
	private static final String XPATH_ALLOCATION_REMAINING_EFFORT = "//td[contains(text(),\"" + RESOURCENAMETOKEN + "\")]/following-sibling::td/input[@id='remainingEffort']";
	
	private static final String TASKPLACEHOLDERTOKEN = "<taskPlaceholder>";
	private static final String XPATH_NEW_TASK_DETAIL = "//input[@placeholder='" + TASKPLACEHOLDERTOKEN +	"'][@value='']/parent::div/parent::td/preceding-sibling::td/span[@class='position-num undecorated-link draggable ui-draggable ui-draggable-handle']";
	private static final String TASKTITLETOKEN = "<taskTitle>";
	private static final String XPATH_TASK_RESOURCES = "//div[@title='" + TASKTITLETOKEN + "']/parent::td/following-sibling::td[@class='assignedResources undecorated-link']";
			
	@FindBy(css = A_CLASS$_ADD_TASK_BUTTON)
	private WebElement newTaskBtn;
	
	@FindBy(css = INPUT_VALUE_CLOSE)
	private WebElement closeTrackingPlanBtn;
	
	@FindBy(css = INPUT_ID$_SAVE_BUTTON)
	private WebElement saveGanttBtn;
	
	public TrackingPlanTasksPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}

	public void CreateNew(Task taskDetails) {
		theSH.waitForElementToBeHidden(By.className("loadingIndicator"));
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(A_CLASS$_ADD_TASK_BUTTON), 20);
		theSH.sleep(1000);
		newTaskBtn.click();

		launchNewTaskDetails(NEW_TASK_PLACEHOLDER);
		
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_TASK_STARTDATE_FIELD), 20);
		
		WebElement taskNameField = theSH.getVisibleWebElement(By.cssSelector(INPUT_ID$_TASK_NAME_FIELD));
		taskNameField.clear();
		taskNameField.sendKeys(taskDetails.name);
		taskNameField.sendKeys(Keys.TAB);
				
		if(taskDetails.startDate != null)		
		{
			theSH.sleep(1000);
			WebElement startDateField = theSH.getWebElement(By.cssSelector(INPUT_ID$_TASK_STARTDATE_FIELD));
			
			startDateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), taskDetails.startDate);
			// the calendar popup lingers unless you tab out of the date preventing click of the save button
			startDateField.sendKeys(Keys.TAB);
		}

		theSH.sleep(1000);
		WebElement endDateField = theSH.getWebElement(By.cssSelector(INPUT_ID$_TASK_ENDDATE_FIELD));

		if(taskDetails.endDate != null)
		{
			endDateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), taskDetails.endDate);
		}

		// with or without an end date, tabbing out of the start date will have popped up 
		// the end date calendar anyway so always tab out of field to close the calendar
		endDateField.sendKeys(Keys.TAB);
		
		// 1.21 onwards the fixed Effort checkbox is accessible via the task details poup
		if(taskDetails.fixedEffort)
		{	
			theSH.sleep(1000);
			theSH.getWebElement(By.cssSelector(INPUT_ID$_EDIT_FIXED)).click();
		}

		theSH.sleep(1000);
		theSH.getWebElement(By.cssSelector(TASK_DETAIL_OK_BTN)).click();
		theSH.waitForElementToBeHidden(By.id(EDIT_TASK_DETAILS_POPUP));

		theSH.waitForElementToBeHidden(By.className("loadingIndicator"));
		saveGanttBtn.click();
		
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_SAVE_BUTTON), 20);
		// additional wait as navigating before save is causing errors
		theSH.sleep(5000);
	}
	
	private void launchNewTaskDetails(String taskPlaceholder)
	{
		String taskDetailsFieldXPath = XPATH_NEW_TASK_DETAIL.replace(TASKPLACEHOLDERTOKEN, taskPlaceholder);
		WebElement taskDetailsField = theSH.getWebElement(By.xpath(taskDetailsFieldXPath));
		taskDetailsField.click();
	}

	public void AssignTask(Task taskDetails, List<TaskAssignment> taskAssignments) {
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeHidden(By.className("loadingIndicator"));
		// click the resources cell for the task at hand
		String resourcesFieldXPath = XPATH_TASK_RESOURCES.replace(TASKTITLETOKEN, taskDetails.name);
		WebElement resourcesField = theSH.getWebElement(By.xpath(resourcesFieldXPath));
		resourcesField.click();
		theSH.waitForElementToBeClickableWithRetry(By.id(EDIT_TASK_DETAILS_POPUP), 20);

		for(TaskAssignment taskAssignmentDetails : taskDetails.taskAssignments)
		{
			// there are multiple checkboxes and input fields, one for each resource - find them with relative xpath based on resource name

			// check the checkbox for this resource
			theSH.sleep(2000);
			String resourceAllocateXPath = XPATH_RESOURCE_ALLOCATE_CHECKBOX.replace(RESOURCENAMETOKEN, taskAssignmentDetails.resourceName);
			WebElement resourceAllocationCheckbox = theSH.getWebElement(By.xpath(resourceAllocateXPath));
			resourceAllocationCheckbox.click();
			theSH.sleep(1000);
			
			// don't need to set the baseline anymore, it gets set when the plan is baselined
			//if(taskAssignmentDetails.baselineEffort != null) baselineEffortField.sendKeys(taskAssignmentDetails.baselineEffort);
			if(taskAssignmentDetails.remainingEffort != null)
			{
				String remainingEffortXPath = XPATH_ALLOCATION_REMAINING_EFFORT.replace(RESOURCENAMETOKEN, taskAssignmentDetails.resourceName);
				WebElement remainingEffortField = theSH.getWebElement(By.xpath(remainingEffortXPath));
				
				remainingEffortField.sendKeys(Keys.chord(Keys.CONTROL, "a"), taskAssignmentDetails.remainingEffort, Keys.TAB);
			}
		}
		
		theSH.waitForElementToBeClickable(By.cssSelector(TASK_DETAIL_OK_BTN));
		theSH.getWebElement(By.cssSelector(TASK_DETAIL_OK_BTN)).click();
		theSH.waitForElementToBeHidden(By.id(EDIT_TASK_DETAILS_POPUP));
		theSH.sleep(2000);

		theSH.waitForElementToBeHidden(By.className("loadingIndicator"));
		saveGanttBtn.click();
		
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_SAVE_BUTTON), 20);
		theSH.sleep(5000);
	}
	
	public void AttemptToCloseTrackingPlan() {
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_VALUE_CLOSE), 10);
		closeTrackingPlanBtn.click();
		theSH.sleep(5000);
		assert theSH.verifyPageContainsText(UNSUBMITTED_OR_UNAPPROVED_TIME_EXISTS);
	}

	public void CloseTrackingPlan() {
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_VALUE_CLOSE), 10);
		closeTrackingPlanBtn.click();
		theSH.sleep(5000);
		// after a refresh the tracking plan will be frozen : check that there is a navigate to next plan link
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(A_TITLE_NEXT_PLAN), 10);
	}

	public void UpdateTaskEstimates(Task taskDetails, List<TaskAssignment> taskAssignments) {
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeHidden(By.className("loadingIndicator"));

		// click the resources cell for the task at hand
		String resourcesFieldXPath = XPATH_TASK_RESOURCES.replace(TASKTITLETOKEN, taskDetails.name);
		
		theSH.clickAndWaitSequenceWithRefreshRetry(20, By.xpath(resourcesFieldXPath), By.id(EDIT_TASK_DETAILS_POPUP));
		
		for(TaskAssignment taskAssignmentDetails : taskDetails.taskAssignments)
		{			
			String remainingEffortXPath = XPATH_ALLOCATION_REMAINING_EFFORT.replace(RESOURCENAMETOKEN, taskAssignmentDetails.resourceName);
			WebElement remainingEffortField = theSH.getWebElement(By.xpath(remainingEffortXPath));
			
			remainingEffortField.clear();
			remainingEffortField.sendKeys(taskAssignmentDetails.remainingEffort);
			remainingEffortField.sendKeys(Keys.TAB);
		}
		
		theSH.getWebElement(By.cssSelector(TASK_DETAIL_OK_BTN)).click();
		theSH.waitForElementToBeHidden(By.id(EDIT_TASK_DETAILS_POPUP));
		theSH.sleep(2000);

		theSH.waitForElementToBeHidden(By.className("loadingIndicator"));
		saveGanttBtn.click();
		
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_SAVE_BUTTON), 20);
		theSH.sleep(5000);
	}	
}
