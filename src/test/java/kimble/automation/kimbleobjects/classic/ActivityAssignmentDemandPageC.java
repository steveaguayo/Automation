package kimble.automation.kimbleobjects.classic;

import java.util.List;

import kimble.automation.domain.Resource;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class ActivityAssignmentDemandPageC extends BasePageC{
		
	private static final String RESOURCE_ID_ONCLICK_DELIMITER = "'";
	private static final String ONCLICK = "onclick";
	private static final String ACTIVITY_ASSIGNMENT_GANTT_BORDER = "activity-assignment-gantt-border";
	private static final String INPUT_VALUE$_SAVE = "input[class$='save-assignment-button'][onclick='saveAssignmentClick()']";
	private static final String ASSIGNMENT_ASSIGN = "assignment-assign";
	private String RESOURCE_NAME_TOKEN = "<RESOURCENAME>";
	private String RESOURCE_ID_TOKEN = "<RESOURCEID>";
	private String RESOURCE_NAME_NODE_XPATH = "//a[contains(text(), \"" + RESOURCE_NAME_TOKEN + "\")]";
	private String RESOURCE_GANTT_LINE_XPATH = "//div[@id='" + RESOURCE_ID_TOKEN + "' and contains(concat(' ', @class, ' '), ' resource-gantt-row ')]";
	
	@FindBy(css = "input[value='Propose Candidates']")
	private WebElement proposeCandidatesBtn;
	
	public ActivityAssignmentDemandPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}

	public void AssignCandidates(List<Resource> candidateResources) {
		for (Resource resourceDetails : candidateResources) {
			// get a handle on the demand
			WebElement demandContainer = theSH.getWebElement(By.className(ACTIVITY_ASSIGNMENT_GANTT_BORDER));
			Point demandContainerLocation = demandContainer.getLocation();
			
			// get a handle on the target resource line
			String resourceNodeLocator = RESOURCE_NAME_NODE_XPATH.replace(RESOURCE_NAME_TOKEN, resourceDetails.name);
			theSH.waitForElementToBeClickable(By.xpath(resourceNodeLocator));
			WebElement resourceNode = theSH.getWebElement(By.xpath(resourceNodeLocator));
			
			// get the resource id from this node from the onclick which will be in this format:
			// toggleResource('a2Lb000000001yiEAA');
			WebElement resourceParent = resourceNode.findElement(By.xpath("../.."));
			String resourceId = resourceParent.getAttribute("id");
			
			String resourceGanttLineLocator = RESOURCE_GANTT_LINE_XPATH.replace(RESOURCE_ID_TOKEN, resourceId);
			// get the gantt line for this resource
			WebElement resourceGanttLine = theSH.getWebElement(By.xpath(resourceGanttLineLocator));
			Point resourceGanttLocation = resourceGanttLine.getLocation();
			
			Actions builder = new Actions(theSH.getWD());
			// builder.dragAndDrop(demandContainer, resourceGanttLine).perform();
			// drag and drop as above doesn't work well as the drop point is somewhere in the middle of the target
			// instead can we can try dragAndDropBy with offset co-ordinates based on the difference between the source and the target
			// use an x co-ordinate shift of 0 to indicate to drag straight down, the y co-ordinate is then set to the
			// difference between the gantt line and the demand line in screen position
			builder.dragAndDropBy(demandContainer, 0, resourceGanttLocation.y - demandContainerLocation.y).perform();
			
			// wait for the popup confirming the details
			theSH.waitForElementToBeClickable(By.cssSelector(INPUT_VALUE$_SAVE), 60);
			theSH.sleep(3000);
			
			// confirm the popup with a save
			theSH.getWebElement(By.cssSelector(INPUT_VALUE$_SAVE)).click();
			// wait for the popup to disappear
			theSH.waitForElementToBeHidden(By.id(ASSIGNMENT_ASSIGN));
			
			// screen has to refresh after the assignment is made
			theSH.sleep(5000);
		}
		
		proposeCandidatesBtn.click();
		// wait for assignment to complete
		theSH.sleep(5000);
	}

	public void FilterResources() {
		theSH.clickLink(By.linkText("Go"));
		// explicit wait for the resources to be returned
		theSH.sleep(3000);
	}

	public void SetCandidateMode() {
		theSH.getElementBasedOnLabel("Candidate").click();
		theSH.sleep(500);
	}
}
