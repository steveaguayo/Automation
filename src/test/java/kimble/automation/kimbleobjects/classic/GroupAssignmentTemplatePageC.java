package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.List;

import kimble.automation.domain.GroupAssignmentTemplate;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GroupAssignmentTemplatePageC extends BasePageC {
		
	private static final String INPUT_ID$_UTILISATION_PCT = "input[id$='utilisationPct']";
	private static final String INPUT_ID$_ENTRY_UNITS = "input[id$='entryUnits']";
	private static final String ACTIVATE_ACTION_LINK = "Activate";
	private static final String REMOVE_ACTION_LINK = "Remove";
	private static final String VIEW_ACTION_LINK = "View";
	private static final String DEL_ACTION_LINK = "Del";
	private static final String ASSIGNMENT_TEMPLATE_ACTIVATE_POPUP = "AssignmentTemplateActivatePopup";
	private static final String INPUT_$_ACTIVATE_TEMPLATE = "input[id$='activateTemplate']";
	private static final String ASSIGNMENT_TEMPLATE_POPUP = "AssignmentTemplatePopup";
	private static final String INPUT_ID$_ADD_GROUP_ASSIGNMENT_TEMPLATE_BTN = "input[id$='addGroupAssignmentTemplateBtn']";
	private static final String SELECT_ID$_USAGE_BEHAVIOUR_RULE = "select[id$='assignmentUsageBehaviourRuleSelect']";
	
	private static final String ACTIVITY_NAME_TOKEN = "<ACTIVITYNAME>";
	private static final String ACTION_LINK_TOKEN = "<ACTIONLINK>";
	private static final String STATUS_TOKEN = "<STATUS>";
	private static final String XPATH_OTHER_ACTIVITY_ACTIONLINK = "//span[contains(text(),\"" + ACTIVITY_NAME_TOKEN + "\")]/parent::td/preceding-sibling::td/a[text()='" + ACTION_LINK_TOKEN + "']";
	private static final String OTHER_ACTIVITY_STATUS = "//span[contains(text(),\"" + ACTIVITY_NAME_TOKEN + "\")]/parent::td/following-sibling::td/span[text()='" + STATUS_TOKEN + "']";
	
	public static final By addButton = By.xpath("//input[@value='Add']");
	public static final By activateButton = By.xpath("//input[@value='Activate']");
	public static final By revertToDraftButton = By.xpath("//input[@value='Revert To Draft']");
	
	@FindBy(css = INPUT_ID$_ADD_GROUP_ASSIGNMENT_TEMPLATE_BTN)
	private WebElement newAssignmentButton;
	
	private By templateName = By.cssSelector("input[id$='templateName']");
	
	private By saveGroupAssignmentTemplateBtn = By.cssSelector("input[id$='saveGroupAssignmentTemplate']");

	private By resourceGroupSelect = By.cssSelector("select[id$='resourceGroup']");
	
	private By assignmentUsageBehaviourRuleSelect = By.xpath("//div[@class='pbBody']/div[2]/div/table/tbody/tr[4]/td/select");
	
	private By assignmentStartDateField = By.cssSelector("input[id$='startDate']");

	private By assignmentEndDateField = By.cssSelector("input[id$='endDate']");

	@FindBy(css = INPUT_ID$_ENTRY_UNITS)
	private WebElement assignmentEntryUnitsField;

	@FindBy(css = INPUT_ID$_UTILISATION_PCT)
	private WebElement assignmentUtilisationField;
	
	@FindBy(css = INPUT_$_ACTIVATE_TEMPLATE)
	private WebElement activateTemplateBtn;
	
	public GroupAssignmentTemplatePageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void CreateNew(final GroupAssignmentTemplate groupAssignmentTemplateDetails){
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			theSH.waitForLightningSpinnerToBeHidden();
			clickAndWaitSequence(theSH, 20, 
			/* click add								*/ By.cssSelector(INPUT_ID$_ADD_GROUP_ASSIGNMENT_TEMPLATE_BTN),
			/* wait for usage behaviour rule select		*/ By.cssSelector(SELECT_ID$_USAGE_BEHAVIOUR_RULE)
			);
			/* input template name						*/ clearAndInputText(theSH, templateName, groupAssignmentTemplateDetails.templateName);
			/* select group name						*/ dropdownSelect(theSH, resourceGroupSelect, groupAssignmentTemplateDetails.groupName);
			/* input start date							*/ clearAndInputText(theSH, assignmentStartDateField, groupAssignmentTemplateDetails.startDate);
			/* select usage behaviour rule				*/ dropdownSelect(theSH, assignmentUsageBehaviourRuleSelect, groupAssignmentTemplateDetails.usageBehaviourRule);
			/*		wait								*/ waitClickable(theSH, By.cssSelector("input[id$='entryUnits']"), 20);
			/* input entry units						*/ clearAndInputText(theSH, By.cssSelector("input[id$='entryUnits']"), groupAssignmentTemplateDetails.entryUnits);
			/* input end date							*/ clearAndInputText(theSH, assignmentEndDateField, groupAssignmentTemplateDetails.endDate);
			/* input utilisation pct if given 			*/ if(groupAssignmentTemplateDetails.utilisationPct != null) {
			/*		wait								*/		waitClickable(theSH, By.cssSelector(INPUT_ID$_UTILISATION_PCT), 20);
			/* 		input								*/ 		clearAndInputText(theSH, By.cssSelector(INPUT_ID$_UTILISATION_PCT), groupAssignmentTemplateDetails.utilisationPct);
			}
			/* click save								*/ click(theSH, saveGroupAssignmentTemplateBtn);
			/* wait for popup to disappear				*/ waitHidden(theSH, By.id(ASSIGNMENT_TEMPLATE_POPUP), 40);
		});
	}

	public void Activate(GroupAssignmentTemplate groupAssignmentTemplateDetails) {
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_ADD_GROUP_ASSIGNMENT_TEMPLATE_BTN), 20);
		
		String locator = XPATH_OTHER_ACTIVITY_ACTIONLINK.replace(ACTIVITY_NAME_TOKEN, groupAssignmentTemplateDetails.templateName).replace(ACTION_LINK_TOKEN, ACTIVATE_ACTION_LINK);
		
		WebElement activateActionLink = theSH.getWebElement(By.xpath(locator));
		activateActionLink.click();
		
		theSH.waitForElementToBeClickable(By.cssSelector(INPUT_$_ACTIVATE_TEMPLATE));
		activateTemplateBtn.click();
		theSH.waitForLightningSpinnerToBeHidden();
		
		theSH.waitForElementToBeHidden(By.id(ASSIGNMENT_TEMPLATE_ACTIVATE_POPUP), 40);
	}

	public void viewAndActivate(String templateName) {
		final By viewSelector = By.xpath(XPATH_OTHER_ACTIVITY_ACTIONLINK.replace(ACTIVITY_NAME_TOKEN, templateName).replace(ACTION_LINK_TOKEN, VIEW_ACTION_LINK));
		theSH.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			clickAndWaitSequence(theSH, 20, 
			/* click view							*/ viewSelector, 
			/* click activate						*/ activateButton,
			/* wait for view link					*/ viewSelector
			);
		});
	}

	public boolean validateExistingAndActive(String templateName) {
		final By statusSelector = By.xpath(OTHER_ACTIVITY_STATUS.replace(ACTIVITY_NAME_TOKEN, templateName).replace(STATUS_TOKEN, "Active"));
		try {
			theSH.waitForLightningSpinnerToBeHidden();
			executeSequenceWithRefreshRetry(theSH, 2, () -> {
				waitClickable(theSH, statusSelector, 3);
			});
		} catch(Exception e) { return false; }
		return true;
	}

	public void deactivateExisting(String templateName) {
		final By viewSelector = By.xpath(XPATH_OTHER_ACTIVITY_ACTIONLINK.replace(ACTIVITY_NAME_TOKEN, templateName).replace(ACTION_LINK_TOKEN, VIEW_ACTION_LINK));
		try {
			theSH.waitForLightningSpinnerToBeHidden();
			executeSequenceWithRefreshRetry(theSH, 2, () -> {
				clickAndWaitSequence(theSH, 3, 
				/* click view						*/ viewSelector, 
				/* click revert to draft			*/ revertToDraftButton,
				/* wait for add button				*/ addButton
				);
			});
		} catch(Exception e) { /* probably failed due to no template being available or template being deactivated already*/ }
	}

	public void deleteExisting(String templateName) {
		final By delSelector = By.xpath(XPATH_OTHER_ACTIVITY_ACTIONLINK.replace(ACTIVITY_NAME_TOKEN, templateName).replace(ACTION_LINK_TOKEN, DEL_ACTION_LINK));
		try {
			theSH.waitForLightningSpinnerToBeHidden();
			executeSequenceWithRefreshRetry(theSH, 2, () -> {
				clickAndWaitSequence(theSH, 3, 
				/* click del						*/ delSelector, 
//				/* click ok on alert				*/ alertOk,
				/* click ok on alert				*/ theSH.alertOkButton,
				/* wait for add button				*/ addButton
				);
			});
		} catch(Exception e) { /* probably failed due to no template being available */ }
	}

	public void RemoveExisting(GroupAssignmentTemplate groupAssignmentTemplateDetails) {
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_ADD_GROUP_ASSIGNMENT_TEMPLATE_BTN), 20);
		
		String locator = XPATH_OTHER_ACTIVITY_ACTIONLINK.replace(ACTIVITY_NAME_TOKEN, groupAssignmentTemplateDetails.templateName).replace(ACTION_LINK_TOKEN, REMOVE_ACTION_LINK);
		
		List<WebElement> removeActionLinks = theSH.getWebElements(By.xpath(locator));
		
		if(removeActionLinks != null)
		{
			// we can't iterate through the remove links and click each as after the first the rest are stale
			// instead use the number of links as an indication of how many times to loop, re-fill the collection each time and always delete the first entry
			for (int i = 0; i < removeActionLinks.size(); i++) {

				if(i > 0) {
					// ensure that if this is the xth time around we wait for the screen to refresh
					theSH.sleep(5000);
				}
				
				List<WebElement> removeActionLinksTemp = theSH.getWebElements(By.xpath(locator));
				removeActionLinksTemp.get(0).click();
				theSH.ClickOKButtonOnOpenAlert();
			}
		}
	}
}
