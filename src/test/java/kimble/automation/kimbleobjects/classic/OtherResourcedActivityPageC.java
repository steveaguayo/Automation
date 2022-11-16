package kimble.automation.kimbleobjects.classic;

import kimble.automation.domain.ActivityAssignment;
import kimble.automation.domain.GroupAssignmentTemplate;
import kimble.automation.domain.OtherResourcedActivity;
import kimble.automation.domain.Risk;
import kimble.automation.helpers.General;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import static kimble.automation.helpers.SequenceActions.clearAndInputText;
import static kimble.automation.helpers.SequenceActions.clickAndWaitSequence;
import static kimble.automation.helpers.SequenceActions.dropdownSelect;
import static kimble.automation.helpers.SequenceActions.executeSequenceWithRefreshRetry;
import static kimble.automation.helpers.SequenceActions.waitClickable;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.FindBy;

import com.thoughtworks.selenium.webdriven.commands.Click;

public class OtherResourcedActivityPageC extends BasePageC {

	private static final String OTHER_ACTIVITIES = "Other Activities";
	
	static final By removeUsageOffset = By.xpath("//label[normalize-space(text())='Remove Usage Offset']/parent::td/following-sibling::td//input");
	static final By removeUsageOffsetL = By.cssSelector("input[id$='removeUsageOffset']");
	static final By usageBehaviourRuleEditButton = By.xpath("//input[@value='Edit']");
	static final By usageBehaviourRuleSaveButton = By.xpath("//td[@class='pbButton']/input[@name='save']");
	static final By usageBehaviourRuleSaveButtonL = By.xpath("//input[@value='Save']");
	
	static final By newEventButton = By.xpath("//input[@value='New Usage Behaviour Rule']");
	private static final String NEW_UBR = "New Usage Behaviour Rule";

	static final By usageBehaviourRuleNewButton = By.xpath("//input[@value='New Usage Behaviour Rule']");
	
	@FindBy(css = "input[name='go']")
	private WebElement goBtn;
	
	public static void usageBehaviourRuleClick(SeleniumHelper sh, String usageBehaviourRuleName) {
		if(!sh.isLightning()){
			executeSequenceWithRefreshRetry(sh, 3, () -> {
				clickAndWaitSequence(sh, 20, 
				/* click UBR					*/ 			By.linkText(usageBehaviourRuleName),
				/* wait for edit button			*/ 			usageBehaviourRuleEditButton
				);
			});			
		}
		else{
			clickAndWaitSequence(sh, 20, 
			/* click UBR					*/ 			By.linkText(usageBehaviourRuleName),
			/* wait for edit button			*/ 			KBy.title("Edit")
			);
		}

	}
	
	public static void usageBehaviourRuleOffsetEdit(SeleniumHelper sh, String offset, String usageBehaviourRuleName) {
		if(sh.isLightning()){
			
			waitClickable(sh, KBy.title("Edit"), 10);
			sh.clickLink(KBy.title("Edit"), 10);
			sh.waitForLightningSpinnerToBeHidden();
			
			executeSequenceWithRefreshRetry(sh, 3, () -> {
				/* wait for save button			*/			waitClickable(sh, usageBehaviourRuleSaveButtonL, 20);
				/* input the offset				*/			clearAndInputText(sh, removeUsageOffsetL, offset);	
				/* Save 						*/			sh.clickLink(usageBehaviourRuleSaveButtonL, 10);
				/* wait hidden					*/			sh.waitForElementToBeHidden(usageBehaviourRuleSaveButtonL);
			});		
		}
		else{
			
			
			String id = null;
			id = sh.getCurrentUrl().split("id=")[1];
			id = id.split("&")[0];
			System.out.println(id);	
			
			String newURL = sh.getCurrentUrl();
			newURL = newURL.split("apex/")[0];
			System.out.println(newURL);	
			
			sh.goToUrl(newURL + id + "/e?nooverride");
					
			
			
			executeSequenceWithRefreshRetry(sh, 3, () -> {
				/* wait for save button			*/			waitClickable(sh, usageBehaviourRuleSaveButton, 20);
				/* input the offset				*/			clearAndInputText(sh, removeUsageOffset, offset);	 
				/* click save					*/ 			sh.clickLink(usageBehaviourRuleSaveButton);
			});		
			
			sh.waitForElementToBeHidden(usageBehaviourRuleSaveButton);	
		}

	}
	
	public OtherResourcedActivityPageC(SeleniumHelper seleniumHelperInstance)
	{
		super(seleniumHelperInstance);
	}

	public void LoadExistingByName(String otherActivityName)
	{
		// navigate to the main list then try a click in the list of recent items
		NavigateToList();
		LoadExistingFromList(otherActivityName);
	}
	
	public void LoadExistingFromList(String otherActivityName) {
		try
		{
			theSH.closeLightningPopUp();
			theSH.OpenExisting(otherActivityName);
		}
		catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		} catch (Exception e)
		{
			// not in recent items list so continue onto the list itself
			goBtn.click();
			theSH.OpenExisting(otherActivityName);
		}
	}
	
	public void NavigateToList()
	{
		theSH.NavigateToList(OTHER_ACTIVITIES);
	}
	
	public void CreateOtherActivityAssignments(OtherResourcedActivity resourcedActivityDetails)
	{				
		// other activities can be assigned through activity assignments or via group assignment templates
		// if we have group assignment templates we make an assumption in the automation tests that
		// there are therefore no direct assignments and vice-versa
		if(resourcedActivityDetails.groupAssignmentTemplates != null)
		{
			createAndActivateGroupAssignmentTemplates(resourcedActivityDetails);		
		}
		else if(resourcedActivityDetails.activityAssignments != null)
		{
			createActivityAssignments(resourcedActivityDetails);
		}
	}

	private void createActivityAssignments(OtherResourcedActivity resourcedActivityDetails) {
		LoadExistingByName(resourcedActivityDetails.name);
		theSH.clickMenuItem(PagesC.ACTIVITYASSIGNMENTSOTHER);
		
		for(ActivityAssignment assignmentDetails : resourcedActivityDetails.activityAssignments)
		{
			ActivityAssignmentPageC activityAssignmentHandler = new ActivityAssignmentPageC(theSH);
			activityAssignmentHandler.CreateNew(resourcedActivityDetails.name, assignmentDetails);
			
			activityAssignmentHandler.VerifyAssignmentExists(assignmentDetails.resourceName, assignmentDetails.startDate);
		}
	}

	private void createAndActivateGroupAssignmentTemplates(OtherResourcedActivity resourcedActivityDetails) {
		LoadExistingByName(resourcedActivityDetails.name);
		theSH.clickMenuItem(PagesC.ACTIVITYGROUPMEMBERTEMPLATES);
		
		for(GroupAssignmentTemplate groupAssignmentTemplateDetails : resourcedActivityDetails.groupAssignmentTemplates)
		{
			GroupAssignmentTemplatePageC groupAssignmentTemplateHandler = new GroupAssignmentTemplatePageC(theSH);
			groupAssignmentTemplateHandler.RemoveExisting(groupAssignmentTemplateDetails);
			groupAssignmentTemplateHandler.CreateNew(groupAssignmentTemplateDetails);
			groupAssignmentTemplateHandler.Activate(groupAssignmentTemplateDetails);
		}
	}
	
	public static void NavigateToAssignments(SeleniumHelper sh) {
		sh.clickMenuItem(PagesC.ACTIVITYASSIGNMENTSOTHER);
		
		// current screen implementation only shows assignments which end within the last four months
		// override this behaviour to ensure that the screen shows all assignments known to be created in the automation scenarios
		ActivityAssignmentPageC activityAssignmentHandler = new ActivityAssignmentPageC(sh);
		activityAssignmentHandler.DefaultForecastingFromDate();
	}
	
	public static void getOtherActivityAssignmentId(SeleniumHelper sh, String jobName, String jobStatus, String resourceName){
		NavigateToAssignments(sh);
		sh.waitForElementToBeClickable(By.linkText(resourceName));
		String resId = sh.getWebElement(By.linkText(resourceName)).getAttribute("id").split("-a-")[1];
		
		General.objectId = resId;
	}
}
