package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.text.ParseException;
import java.util.List;

import kimble.automation.domain.ActivityAssignment;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.MonthlyProfile;
import kimble.automation.domain.Resource;
import kimble.automation.domain.ScheduledActivity;
import kimble.automation.helpers.General;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ActivityAssignmentPageC extends BasePageC {
		

	private static final String SCOPED_WITH_SELECT = "select[id='simple-input-owning-activity']";
	private static final String ASSIGNMENT_ACCEPT_CANDIDATE_BUTTON = "assignment-accept-candidate-button";
	private static final String ASSIGNMENT_REJECT_CANDIDATE_BUTTON = "assignment-reject-candidate-button";
	private static final String ICON_CALENDAR = "fa fa-calendar";
	private static final String ICON_EDIT = "fa fa-pencil-square-o";
	private static final String INPUT_ID$_COST_RATE = "input[id$='cost-rate']";
	private static final String INPUT_ID$_REVENUE_RATE = "input[id$='revenue-rate']";
	private static final String SELECT_ID$_ACTIVITY_ROLE_LIST = "select[id$='simple-input-role']";
	private static final String SELECT_ID$_LOCATION_SELECT_LIST = "select[id$='simple-input-location']";
	private static final String INPUT_ID$_ENTRY_END_DATE = "input[id$='EntryEndDate']";
	private static final String INPUT_ID$_ENTRY_UTILISATION = "input[id$='EntryUtilisation']";
	private static final String INPUT_ID$_ENTRY_REMAINING_USAGE = "input[id$='EntryRemainingUsage']";
	private static final String ASSIGNMENT_ROW_CLASSNAME = "assignment-row";
	private static final String DEFAULT_FORECASTING_FROM_DATE = "01/01/2013";
	private static final String SELECT_ID$_USAGE_BEHAVIOUR_RULE = "select[id$='UsageBehaviourRule']";
	private static final String INPUT_ID$_SAVE_ACTIVITY_ASSIGNMENT_BUTTON = "input[id$='SaveActivityAssignmentButton']";
	private static final String ASSIGNMENTID_ATTRIBUTE = "assignment-id";
	private static final String MONTHLY_VIEW_LINK = "Monthly";
	private static final String FORECASTING_FROM_DATE_PARAM = "&forecastingFromDate=";
	private static final String INPUT_ID$_START_DATE = "input[id$='start-date']";
	private static final String BTN_SAVE_CALENDAR = "btnSaveCalendar";
	
	private static final String ACTIVITY_ASSIGNMENT_EDIT = "ActivityAssignmentNewEdit";
	private static final String SELECT_ID$_ACTIVITY_ASSIGNMENT_RESOURCE_SELECT = "select[id$='simple-input-resource']";
	private static final String SELECT_ID$_ACTIVITY_ASSIGNMENT_RESOURCE_INPUT = "input[id$='ResourceInput']";
	private static final String INPUT_ID$_SPLIT_SCREEN_BUTTON = "img[title$='Show Details and Gantt']";

	private static final By groupAssignmentsEnabledCheckbox = By.cssSelector("input[id$='with-group-members']");
	private static final By assignmentDelete = By.xpath("//div[6]/div[1]/a[4]/i");
	
	private static final String RESOURCENAMETOKEN = "<RESOURCENAME>";
	private static final String STARTDATETOKEN = "<STARTDATE>";
	private static final String ASSIGNMENTIDTOKEN = "<ASSIGNMENTID>";
	private static final String ICONNAMETOKEN = "<ICONNAME>";
	private static final String ASSIGNMENTACCEPTTOKEN = "<ASSIGNMENTACCEPTTOKEN>";
	private static final String ASSIGNMENT_ROW_XPATH = "//a[contains(text(), \"" + RESOURCENAMETOKEN + "\")]/parent::div/parent::td/following-sibling::td/div[contains(text(),'" + STARTDATETOKEN + "')]";
	private static final String GROUPRESOURCETWISTERXPATH = "//a[.=\"" + RESOURCENAMETOKEN + "\"]/parent::div/preceding-sibling::span";
	private static final String RESOURCEROWCOGXPATH = "//a[.=\"" + RESOURCENAMETOKEN + "\"]/parent::div/parent::td/preceding-sibling::td/a";
	private static final String POPUPMENUXPATH = "//div[@class='tool-items']/a[@assignment-id='" + ASSIGNMENTIDTOKEN + "']/i[@class='" + ICONNAMETOKEN + "']";
	private static final String CANDIDATEMENUXPATH = "//div[@class='tool-items']/a[contains(@class,'assignment-demand-button')]";
	private static final String RESOURCEROWCANDIDATEICONXPATH = "//a[.=\"" + RESOURCENAMETOKEN + "\"]/parent::div/parent::td/preceding-sibling::td/a";
	private static final String CANDIDATEACCEPTREJECTXPATH = "//div[@class='tool-items']/a[contains(@class,'" + ASSIGNMENTACCEPTTOKEN + "')][@assignment-id='" + ASSIGNMENTIDTOKEN + "']";
	
	private static final String ACTIVITYNAMETOKEN = "<ACTIVITYNAME>";
	private static final String ACTIVITY_ASSIGNMENT_NEW_XPATH = "//td[contains(text(),\""+ ACTIVITYNAMETOKEN + "\")]/preceding-sibling::td/a[@title='Add Assignment']";
	
	private static final String ASSIGNMENT_ID_TOKEN = "<ASSIGNMENTID>";
	private static final String MONTH_YEAR_TOKEN = "<MONTHYEAR>";
	private static final String ASSIGNMENT_FORECAST_INPUT = "//input[@class='forecast-input' and @assignment-id='" + ASSIGNMENT_ID_TOKEN + "' and @month-name='" + MONTH_YEAR_TOKEN + "']";
	
	private static final String TR_BASED_ON_ASSIGNMENTID = "tr[node-id='" + ASSIGNMENT_ID_TOKEN + "']";
	
	public static final By layoutContainer = By.cssSelector("#layout-container");
	public static final By gridLayout = By.cssSelector("#grid-layout");
	public static final By gridBodyContainer = By.cssSelector("#grid-body-container");
	
	public static final By groupAssignmentCaret = By.xpath("//tr[contains(@class, 'assignment-row')][@level='1']/td/span[@class='indent']");

	public static final By assignmentKIcon2 = By.xpath("//div[1]/div[2]/table/tbody/tr/td[2]/div[1]/span/div/div[2]/a/span");
	public static final By assignmentKIcon1 = By.xpath("//span[@id='k-image']");
	public static final By assignmentOverrideElement = By.xpath("//div[1]/div[2]/table/tbody/tr/td[2]/div[9]/div[1]/div/div[2]/table/tbody/tr[2]/th/a");
	public static final By assignmentOverrideResActivity = By.xpath("//div[1]/div[2]/table/tbody/tr/td[2]/div[7]/div[1]/div/div[2]/table/tbody/tr[2]/th/a");
	public static final String assignmentOverrideAssignment = RESOURCENAMETOKEN + ", on ";
	
	// CACHE
	static Integer 
	layoutContainerHeight,
	gridLayoutHeight,
	gridBodyContainerMaxHeight;
	
	private boolean monthlyViewInitialised = false;
	public static String ActivityAssignmentId = null;
		
	@FindBy(css = SELECT_ID$_ACTIVITY_ASSIGNMENT_RESOURCE_INPUT)
	private WebElement activityAssignmentResourceInput;
	
	@FindBy(css = SELECT_ID$_ACTIVITY_ROLE_LIST)
	private WebElement activityAssignmentRoleSelect;

	@FindBy(css = SELECT_ID$_LOCATION_SELECT_LIST)
	private WebElement activityAssignmentLocation;
	
	@FindBy(css = INPUT_ID$_REVENUE_RATE)
	private WebElement revenueRateField;

	@FindBy(css = INPUT_ID$_COST_RATE)
	private WebElement costRateField;
		
	@FindBy(css = INPUT_ID$_ENTRY_REMAINING_USAGE)
	private WebElement activityAssignmentRemainingUsageField;

	@FindBy(css = INPUT_ID$_ENTRY_UTILISATION)
	private WebElement activityAssignmentUtilisationField;
		
	@FindBy(css = INPUT_ID$_SAVE_ACTIVITY_ASSIGNMENT_BUTTON)
	private WebElement saveActivityAssignmentButton;
	
	@FindBy(css = "img[title$='Show Gantt']")
	private WebElement expandEastBtn;
	
	public ActivityAssignmentPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void CreateNew(String activityName, ActivityAssignment assignment){
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			theSH.waitForLightningSpinnerToBeHidden();
			// assignments screen can show more than one activity so find the new button for the activity we are dealing with
			By addSelector = By.xpath(ACTIVITY_ASSIGNMENT_NEW_XPATH.replace(ACTIVITYNAMETOKEN, activityName));
			theSH.switchToClassicIframeContext();
			
			clickAndWaitSequence(theSH, 20,
				addSelector,
				By.cssSelector(SELECT_ID$_ACTIVITY_ASSIGNMENT_RESOURCE_SELECT)
			);
			if(assignment.scopedWith != null && !assignment.scopedWith.equals(""))
				theSH.selectByVisibleText(By.cssSelector(SCOPED_WITH_SELECT), assignment.scopedWith);
			if(exists(theSH, By.cssSelector(SELECT_ID$_ACTIVITY_ASSIGNMENT_RESOURCE_SELECT), 20))
				dropdownSelect(theSH, By.cssSelector(SELECT_ID$_ACTIVITY_ASSIGNMENT_RESOURCE_SELECT), assignment.resourceName);
			else
			{
				clearAndInputText(theSH, By.cssSelector(SELECT_ID$_ACTIVITY_ASSIGNMENT_RESOURCE_INPUT), assignment.resourceName);
				// waiting for save to be reenabled as this indicates the screen is ready again
				waitClickable(theSH, By.cssSelector(INPUT_ID$_SAVE_ACTIVITY_ASSIGNMENT_BUTTON), 20);
			}
			dropdownSelect(theSH, By.cssSelector(SELECT_ID$_ACTIVITY_ROLE_LIST), assignment.resourceRole);
			waitClickable(theSH, By.cssSelector(INPUT_ID$_SAVE_ACTIVITY_ASSIGNMENT_BUTTON), 20);

			// only set location if the data indicats a location and there is more than one location configured in the org (otherwise the dropdown is not rendered)
			if(exists(theSH, By.cssSelector(SELECT_ID$_LOCATION_SELECT_LIST), 5)) {
				clearAndInputText(theSH, By.cssSelector(SELECT_ID$_LOCATION_SELECT_LIST), assignment.location);
				waitClickable(theSH, By.cssSelector(INPUT_ID$_SAVE_ACTIVITY_ASSIGNMENT_BUTTON), 20);
			}

			// short wait while the javascript sets the default revenue and costs rates
			// we can proceed once the Start Date field is editable as this indicates the post resource selection
			// javascript has fired
			waitClickable(theSH, By.cssSelector(INPUT_ID$_START_DATE), 20);

			if(!assignment.costOnlyAssignment)
				clearAndInputText(theSH, By.cssSelector(INPUT_ID$_REVENUE_RATE), assignment.revenueRate);

			clearAndInputText(theSH, By.cssSelector(INPUT_ID$_COST_RATE), assignment.costRate);

			dropdownSelect(theSH, By.cssSelector(SELECT_ID$_USAGE_BEHAVIOUR_RULE), assignment.usageBehaviourRule);
			
			checkboxSelect(theSH, groupAssignmentsEnabledCheckbox, assignment.groupAssignmentsEnabled);

			clearAndInputText(theSH, By.cssSelector(INPUT_ID$_START_DATE), assignment.startDate);
			click(theSH, By.id(ACTIVITY_ASSIGNMENT_EDIT));
			waitClickable(theSH, By.cssSelector(INPUT_ID$_SAVE_ACTIVITY_ASSIGNMENT_BUTTON), 20);

			clearAndInputText(theSH, By.cssSelector(INPUT_ID$_ENTRY_END_DATE), assignment.endDate);
			waitClickable(theSH, By.cssSelector(INPUT_ID$_SAVE_ACTIVITY_ASSIGNMENT_BUTTON), 20);
			clearAndInputText(theSH, By.cssSelector(INPUT_ID$_ENTRY_REMAINING_USAGE), assignment.remainingEffort);
			clearAndInputText(theSH, By.cssSelector(INPUT_ID$_ENTRY_UTILISATION), assignment.utilisationPct);
			
			waitClickable(theSH, By.cssSelector(INPUT_ID$_SAVE_ACTIVITY_ASSIGNMENT_BUTTON), 20);
			click(theSH, By.cssSelector(INPUT_ID$_SAVE_ACTIVITY_ASSIGNMENT_BUTTON));
			
			// the screen takes a while to close after clicking save so wait for it to have closed
			waitHidden(theSH, By.id(ACTIVITY_ASSIGNMENT_EDIT), 60);
			VerifyAssignmentExists(assignment.resourceName, assignment.startDate);
		}); 
	}

	public void ExpandGroupResource(String parentResourceName) {
		// expand the row if the twister for the group resource row
		String grouptwisterXPath = GROUPRESOURCETWISTERXPATH.replace(RESOURCENAMETOKEN, parentResourceName);
		theSH.waitForElementToBeClickable(By.xpath(grouptwisterXPath));
		WebElement groupResourceRowTwister = theSH.getWebElement(By.xpath(grouptwisterXPath));
		groupResourceRowTwister.click();
		theSH.sleep(200);
	}

	public void ScheduleActivity(List<ScheduledActivity> scheduledActivity) throws ParseException {
		String previousParentResourceName = "";
		theSH.waitForLightningSpinnerToBeHidden();
		
		for (ScheduledActivity scheduledActivityDetails : scheduledActivity)
		{
			theSH.waitForLightningSpinnerToBeHidden();
			// expand the group level assignment
			// launch the calendar for each of the resources
			// assign some days
			// save

			// if this is for a group resource then we need to first expand the group resource line to reveal the individual resources
			// but only if we haven't done this already (if in a loop then the previous choice to open the twister will have been rememebered by the screen
			if(scheduledActivityDetails.parentResourceName != null && scheduledActivityDetails.parentResourceName != "" && !scheduledActivityDetails.parentResourceName.equals(previousParentResourceName))
			{
				ExpandGroupResource(scheduledActivityDetails.parentResourceName);
				previousParentResourceName = scheduledActivityDetails.parentResourceName;
			}
			
			AssignmentCalendarPageC assignmentCalendarPageHandler = new AssignmentCalendarPageC(theSH);
			OpenCalendar(scheduledActivityDetails.resourceName);
			assignmentCalendarPageHandler.ScheduleDays(scheduledActivityDetails.scheduledDays, scheduledActivityDetails.resourceName);
		}		
	}

	public void ScheduleOtherActivity(SeleniumHelper sh, List<ScheduledActivity> scheduledActivity) throws ParseException {
		String previousParentResourceName = "";
		
		for (ScheduledActivity scheduledActivityDetails : scheduledActivity)
		{
			// expand the group level assignment
			// launch the calendar for each of the resources
			// assign some days
			// save

			// if this is for a group resource then we need to first expand the group resource line to reveal the individual resources
			// but only if we haven't done this already (if in a loop then the previous choice to open the twister will have been rememebered by the screen
			if(scheduledActivityDetails.parentResourceName != null)
			{
				sh.waitForElementToBeClickable(By.linkText(scheduledActivityDetails.parentResourceName));
			}
			if(scheduledActivityDetails.parentResourceName != null && !exists(sh, By.linkText(scheduledActivityDetails.resourceName), 20)/*&& scheduledActivityDetails.parentResourceName != "" && !scheduledActivityDetails.parentResourceName.equals(previousParentResourceName)*/)
			{
				ExpandGroupResource(scheduledActivityDetails.parentResourceName);
				previousParentResourceName = scheduledActivityDetails.parentResourceName;
			}
			sh.waitForElementToBeClickable(By.linkText(scheduledActivityDetails.resourceName));
			AssignmentCalendarPageC assignmentCalendarPageHandler = new AssignmentCalendarPageC(theSH);
			OpenCalendar(scheduledActivityDetails.resourceName);
			assignmentCalendarPageHandler.ScheduleOtherDays(scheduledActivityDetails.scheduledDays, scheduledActivityDetails.resourceName, scheduledActivityDetails.parentResourceName);	
		}		
	}
	
	public static void showAssignmentFor(SeleniumHelper sh, String resourceName) {
		By resourceCog = By.xpath(RESOURCEROWCOGXPATH.replace(RESOURCENAMETOKEN, resourceName));
		if(!exists(sh, resourceCog, 3))
			expandGridLayout(sh);
		if(!exists(sh, resourceCog, 10))
			throw new RuntimeException("No assignment found for resource: " + resourceName + " - " + resourceCog);
	}
	
	static void expandGridLayout(SeleniumHelper sh) {
		JavascriptExecutor js = sh.getJsExecutor();
		if(layoutContainerHeight == null) layoutContainerHeight = Integer.parseInt(js.executeScript("return arguments[0].style.height", sh.getWebElement(layoutContainer)).toString().replace("px", ""));
		if(gridLayoutHeight == null) gridLayoutHeight = Integer.parseInt(js.executeScript("return arguments[0].style.height", sh.getWebElement(gridLayout)).toString().replace("px", ""));
		if(gridBodyContainerMaxHeight == null) gridBodyContainerMaxHeight = Integer.parseInt(js.executeScript("return arguments[0].style.maxHeight", sh.getWebElement(gridBodyContainer)).toString().replace("px", ""));
		js.executeScript("arguments[0].style.height = '1500px';", sh.getWebElement(layoutContainer));
		js.executeScript("arguments[0].style.height = '1500px';", sh.getWebElement(gridLayout));
		js.executeScript("arguments[0].style.maxHeight = '1500px'; alert('grid layout expanded');", sh.getWebElement(gridBodyContainer));
//		waitClickable(sh, alertOk, 20);
//		click(sh, alertOk);
		sh.alertClickOk();
	}
	
	static void returnGridLayoutToNormal(SeleniumHelper sh) {
		JavascriptExecutor js = sh.getJsExecutor();
		js.executeScript("arguments[0].style.height = arguments[0];", sh.getWebElement(layoutContainer), layoutContainerHeight + "px");
		js.executeScript("arguments[0].style.height = arguments[0];", sh.getWebElement(gridLayout), gridLayoutHeight + "px");
		js.executeScript("arguments[0].style.maxHeight = arguments[0]; alert('grid layout returned to normal');", sh.getWebElement(gridBodyContainer), gridBodyContainerMaxHeight + "px");
//		waitClickable(sh, alertOk, 20);
//		click(sh, alertOk);
		sh.alertClickOk();
	}
	
	private void OpenCalendar(String resourceName) {
		executeSequenceWithRetry(theSH, 3, () -> {
			showAssignmentFor(theSH, resourceName);
			
			String activityAssignmentID = clickResourceCog(resourceName);
			
			clickPopupMenuIcon(activityAssignmentID, ICON_CALENDAR);
			
			theSH.waitForElementToBeClickable(By.id(BTN_SAVE_CALENDAR));
//			returnGridLayoutToNormal();
		});
	}
	
	private void OpenActivityAssignment(String resourceName) {
		String activityAssignmentID = clickResourceCog(resourceName);
		
		clickPopupMenuIcon(activityAssignmentID, ICON_EDIT);
		
		theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_SAVE_ACTIVITY_ASSIGNMENT_BUTTON));
	}

	public void clickPopupMenuIcon(String activityAssignmentID, String iconName) {
		String popupIconXPath = POPUPMENUXPATH.replace(ASSIGNMENTIDTOKEN, activityAssignmentID).replace(ICONNAMETOKEN, iconName);
		theSH.waitForElementToBeClickable(By.xpath(popupIconXPath));
		theSH.getWebElement(By.xpath(popupIconXPath)).click();
	}

	private void OpenViewDemandAssigment(String resourceName) {
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			theSH.waitForLightningSpinnerToBeHidden();
			showAssignmentFor(theSH, resourceName);
			clickResourceCog(resourceName);
			
			theSH.waitForElementToBeClickable(By.xpath(CANDIDATEMENUXPATH));
			theSH.getWebElement(By.xpath(CANDIDATEMENUXPATH)).click();

			theSH.waitForLightningSpinnerToBeHidden();
			theSH.waitForElementToBeClickable(By.linkText("Go"));
		});
	}
	
	private String clickResourceCog(String resourceName) {
		By resourceCog = By.xpath(RESOURCEROWCOGXPATH.replace(RESOURCENAMETOKEN, resourceName));
		
		waitForResourceCogToBeClickable(resourceName);
		String activityAssignmentID = theSH.getWebElement(resourceCog).getAttribute(ASSIGNMENTID_ATTRIBUTE);

		try
		{
			waitForResourceCogToBeClickable(resourceName);
			click(theSH, resourceCog);
		}
		catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		}
		catch (Exception e)
		{
			// this action can fail when there are two many assignment and the target assignment is scrolled out of view
			// hacky but selenium can't get a handle on the inner scrollbar so instead inject js to hide all the assignments expect those
			// relating to the target assignment and try again
			theSH.hideAllElementsByClassName(ASSIGNMENT_ROW_CLASSNAME);
			// then unhide the tr with this attribute
			theSH.showAllElementsByCssSelector(TR_BASED_ON_ASSIGNMENTID.replace(ASSIGNMENT_ID_TOKEN, activityAssignmentID));
			
			waitForResourceCogToBeClickable(resourceName);
			theSH.waitMilliseconds(5000);
			click(theSH, resourceCog);
		}
		return activityAssignmentID;
	}
	
	void waitForResourceCogToBeClickable(String resourceName) {
		By resourceCog = By.xpath(RESOURCEROWCOGXPATH.replace(RESOURCENAMETOKEN, resourceName));
		try {
			waitClickable(theSH, resourceCog, 10);
		} catch(Exception e) {
			clickAll(theSH, groupAssignmentCaret);
			waitClickable(theSH, resourceCog, 10);
		}
	}

	private String clickCandidateIcon(String resourceName) {
		
		String resourceRowCandidateXPath = RESOURCEROWCANDIDATEICONXPATH.replace(RESOURCENAMETOKEN, resourceName);
		WebElement resourceRowCandidateIcon = theSH.getWebElement(By.xpath(resourceRowCandidateXPath));
		String activityAssignmentID = resourceRowCandidateIcon.getAttribute(ASSIGNMENTID_ATTRIBUTE);
		
		executeSequenceWithRetry(theSH, 3, () -> {	
		
			theSH.waitForElementToBeClickable(By.xpath(resourceRowCandidateXPath));
			// find the candidate icon for the assignment row
			theSH.waitMilliseconds(9000);
			resourceRowCandidateIcon.click();
			
		});
		
		return activityAssignmentID;
	}
	
	public void AssignCandidates(ActivityAssignment activityAssignmentDetails) {
		theSH.waitForLightningSpinnerToBeHidden();
		ActivityAssignmentDemandPageC activityAssignmentDemandPageHandler = new ActivityAssignmentDemandPageC(theSH);
		OpenViewDemandAssigment(activityAssignmentDetails.resourceName);
		activityAssignmentDemandPageHandler.SetCandidateMode();
		activityAssignmentDemandPageHandler.FilterResources();
		activityAssignmentDemandPageHandler.AssignCandidates(activityAssignmentDetails.candidateResources);
	}

	public void ReviewCandidates(ActivityAssignment activityAssignmentDetails) {
		theSH.waitForLightningSpinnerToBeHidden();
	
		// the action any rejections
		for(Resource candidateResource : activityAssignmentDetails.candidateResources)
		{
			theSH.refreshBrowser();
	        theSH.waitForLightningSpinnerToBeHidden();
	        ExpandGroupResource(activityAssignmentDetails.resourceName);
			
	        if(candidateResource.candidateAction != null && candidateResource.candidateAction.equals("Reject"))
			{
				rejectCandidate(candidateResource.name);									
			} else if(candidateResource.candidateAction != null && candidateResource.candidateAction.equals("Accept")){
				acceptCandidate(candidateResource.name);	
			}
		}
	}
	
	public void VerifyAssignmentExists(String resourceName, String startDate, int wait) {
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickable(By.xpath(ASSIGNMENT_ROW_XPATH.replace(RESOURCENAMETOKEN, resourceName).replace(STARTDATETOKEN, startDate)), wait);
		// if no exception has been thrown then we must have found the assignment row
	}
	
	public void VerifyAssignmentExists(String resourceName, String startDate) {
		VerifyAssignmentExists(resourceName, startDate, 20);
	}
	
	public boolean doesAssignmentExist(String resourceName, String startDate) {
		try {
			VerifyAssignmentExists(resourceName, startDate);
		} catch(Exception e) { return false; }
		return true;
	}

	private void rejectCandidate(String name) {
		String activityAssignmentId = clickCandidateIcon(name);
		theSH.sleep(1000);
		WebElement rejectIcon = theSH.getWebElement(By.xpath(CANDIDATEACCEPTREJECTXPATH.replace(ASSIGNMENTIDTOKEN, activityAssignmentId).replace(ASSIGNMENTACCEPTTOKEN, ASSIGNMENT_REJECT_CANDIDATE_BUTTON)));
		rejectIcon.click();
		theSH.sleep(10000);
	}

	private void acceptCandidate(String name) {
		String activityAssignmentId = clickCandidateIcon(name);
		theSH.sleep(1000);
		WebElement acceptIcon = theSH.getWebElement(By.xpath(CANDIDATEACCEPTREJECTXPATH.replace(ASSIGNMENTIDTOKEN, activityAssignmentId).replace(ASSIGNMENTACCEPTTOKEN, ASSIGNMENT_ACCEPT_CANDIDATE_BUTTON)));
		acceptIcon.click();
		theSH.sleep(10000);
	}

	public void InitialiseMonthlyView(String forecastingFromDate) throws ParseException {
		if(!monthlyViewInitialised)
		{
			SetForecastingFromDate(forecastingFromDate);
			theSH.waitForPageLoadComplete(10);
			theSH.waitMilliseconds(5000);
			expandEastBtn.click();
			theSH.waitMilliseconds(5000);
			theSH.clickLink(By.linkText(MONTHLY_VIEW_LINK));
			theSH.sleep(10000);
						
			monthlyViewInitialised = true;
		}
	}
	
	// initialise the activity assignment screen with a default forecasting from date of the earliest
	// automation test scenario (01/01/2013)
	public void DefaultForecastingFromDate()
	{
		try {
			SetForecastingFromDate(DEFAULT_FORECASTING_FROM_DATE);
		} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		} catch (ParseException e) {
			// try catch only required as this could generate a parse exception, but since the parameter is known it won't
			e.printStackTrace();
		}
	}

	private void SetForecastingFromDate(String forecastingFromDate) throws ParseException {
		if(!theSH.isLightning()){
			theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_SPLIT_SCREEN_BUTTON), 20);
			// convert the forecastingFromDate into the format required by the page param e.g. "&forecastingFromDate=2013-01-01"
			String forecastingFromDateParam = FORECASTING_FROM_DATE_PARAM + theSH.getDateLabelForDate(forecastingFromDate, theSH.YEARMONTHDAYPARAMFORMAT);
			theSH.appendPageParam(forecastingFromDateParam);
			theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_SPLIT_SCREEN_BUTTON), 20);
		}
	}

	public void ProfileAssignments(ActivityAssignment activityAssignmentDetails, String testStage) {
		theSH.waitForLightningSpinnerToBeHidden();
		
		for(MonthlyProfile monthlyProfileDetails : activityAssignmentDetails.monthlyProfiles)
		{
			if(monthlyProfileDetails.testStage.equals(testStage))
			{
				// find the assignment ID of the resources assignment (code assumes one assignment per resource - too complicated to do anything else just now!)
				WebElement resourceRowCog = theSH.getWebElement(By.xpath(RESOURCEROWCOGXPATH.replace(RESOURCENAMETOKEN, activityAssignmentDetails.resourceName)));
				String resourceActivityAssignmentID = resourceRowCog.getAttribute(ASSIGNMENTID_ATTRIBUTE);
								
				// get the forecast input field for this assignment month combination
				String forecastInputLocator = ASSIGNMENT_FORECAST_INPUT.replace(ASSIGNMENT_ID_TOKEN, resourceActivityAssignmentID).replace(MONTH_YEAR_TOKEN, monthlyProfileDetails.month);
				theSH.waitForLightningSpinnerToBeHidden();
				WebElement forecastInput = theSH.getWebElement(By.xpath(forecastInputLocator));
				theSH.waitForElementToBeEditable(By.xpath(forecastInputLocator));
				// clear the contents, enter the new value and tab away to commit
				forecastInput.clear();
				theSH.waitForElementToBeEditable(By.xpath(forecastInputLocator));
				forecastInput.sendKeys(monthlyProfileDetails.value);
				theSH.waitForElementToBeEditable(By.xpath(forecastInputLocator));
				forecastInput.sendKeys(Keys.TAB);
				
				// short sleep to allow the update to save
				theSH.sleep(1000);
			}
		}
	}

	public void UpdateRemainingUsageStage2(ActivityAssignment activityAssignmentDetails)
	{
		UpdateRemainingUsage(activityAssignmentDetails.resourceName, activityAssignmentDetails.remainingEffortStage2);
	}
	
	public void UpdateRemainingUsageStage3(ActivityAssignment activityAssignmentDetails)
	{
		UpdateRemainingUsage(activityAssignmentDetails.resourceName, activityAssignmentDetails.remainingEffortStage3);
	}
	
	public void UpdateRemainingUsage(String resourceName, String remainingEffort)
	{
		theSH.waitForLightningSpinnerToBeHidden();
		OpenActivityAssignment(resourceName);
		
		// wait for the resource selector to appear as this indicates the screen is loaded
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(SELECT_ID$_USAGE_BEHAVIOUR_RULE), 20);
		
		activityAssignmentRemainingUsageField.clear();
		activityAssignmentRemainingUsageField.sendKeys(remainingEffort);

		saveActivityAssignmentButton.click();
		// the screen takes a while to close after clicking save so wait for it to have closed
		theSH.waitForElementToBeHidden(By.id(ACTIVITY_ASSIGNMENT_EDIT), 40);
		
		theSH.sleep(2000);
	}
	
	public void deleteAssignment(String name)
	{
		clickResourceCog(name);
		waitClickable(theSH, assignmentDelete, 20);
		click(theSH, assignmentDelete);
		theSH.ClickOKButtonOnOpenAlert();
		theSH.waitForElementToBeHidden(By.linkText(name), 40);
	}

	public static void getAssignmentId(SeleniumHelper sh, String jobName, String jobStatus, ActivityAssignment assignment) {

			click(sh, assignmentKIcon1);
			waitClickable(sh, assignmentOverrideElement, 10);
			click(sh, assignmentOverrideElement);
			
			waitClickable(sh, By.xpath("//*[contains(text(),'You will be redirected in 5 seconds')]"), 10);			
			waitClickable(sh, assignmentKIcon1, 10);
			click(sh, assignmentKIcon1);
			
			if(sh.checkElementVisible(assignmentKIcon1))
				sh.clickLink(assignmentKIcon1);
			
			waitClickable(sh, assignmentOverrideResActivity, 10);
			click(sh, assignmentOverrideResActivity);

			waitClickable(sh, By.xpath("//*[contains(text(),'You will be redirected in 5 seconds')]"), 10);	
			click(sh, assignmentKIcon1);
			if(sh.checkElementVisible(assignmentKIcon1))
				sh.clickLink(assignmentKIcon1);
		
		By assignmentNameOverride = By.partialLinkText(assignmentOverrideAssignment.replace(RESOURCENAMETOKEN, assignment.resourceName));
		
		waitClickable(sh, assignmentNameOverride, 20);
		click(sh, assignmentNameOverride);
		String assignmentID = sh.getCurrentUrl().replaceAll(".+salesforce.com/", "").replace("?nooverride=1", "");
		
		JobsNewPageC.createNewJob(sh, jobName, jobStatus, assignmentID);		
		
	}
	
	public static void getAssignmentIdFromActivityAssignments(SeleniumHelper sh, String jobName, String jobStatus, ActivityAssignment assignment){
		sh.waitForElementToBeClickable(By.linkText(assignment.resourceName));
		String resId = sh.getWebElement(By.linkText(assignment.resourceName)).getAttribute("id").split("-a-")[1];
				
		General.objectId = resId;
		
	}
	
}
