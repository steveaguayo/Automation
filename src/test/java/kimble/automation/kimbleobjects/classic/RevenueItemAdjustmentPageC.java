package kimble.automation.kimbleobjects.classic;

import java.util.List;

import kimble.automation.domain.JsTreeItemPath;
import kimble.automation.domain.RevenueItemAdjustment;
import kimble.automation.helpers.General;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RevenueItemAdjustmentPageC extends BasePageC {
	
	private static final String SEND_FOR_APPROVAL_LINK = "Send For Approval";

	private static final String OPEN_ALL_TREE_NODES = "$(\"#selectRevenueItems\").jstree('open_all')";

	private static final String REVENUE_ITEMS_ADJUSTMENT_POPUP = "RevenueItemsAdjustmentPopup";

	private static final String DIV_ID$_JS_TREE_CONTAINER = "div[id$='jsTreeContainer']";

	private static final String SAVE_BUTTON = "input[id$='saveRevenueItemsAdjustmentBtn']";

	@FindBy(css = "select[id$='revenueItemAdjustmentReason']")
	private WebElement revenueItemAdjustmentReasonSelect;
	
	@FindBy(css = "textarea[id$='revenueItemAdjustmentDescription']")
	private WebElement revenueItemAdjustmentDescription;
	
	By newRevenueItemAdjustmentButton = By.cssSelector("input[value='New Revenue Item Adjustment']");

	@FindBy(linkText = SEND_FOR_APPROVAL_LINK)
	private WebElement sendRevenueItemAdjustmentForApprovalLink;
	
	@FindBy(css = SAVE_BUTTON)
	private WebElement saveRevenueItemAdjustmentButton;
	
	public RevenueItemAdjustmentPageC(SeleniumHelper seleniumHelperInstance) {
		super(seleniumHelperInstance);
	}

	public void CreateNew(RevenueItemAdjustment revenueAdjustmentDetail){
		// popup takes sometime to render so wait for the most complex part of the screen to render
		theSH.clickAndWaitSequenceWithRefreshRetry(20, newRevenueItemAdjustmentButton, By.cssSelector(DIV_ID$_JS_TREE_CONTAINER));
		
		theSH.selectByVisibleText(revenueItemAdjustmentReasonSelect, revenueAdjustmentDetail.reason);
		revenueItemAdjustmentDescription.sendKeys(revenueAdjustmentDetail.description);
		
		// Selennium will complain if we try to click nodes that haven't been expanded
		// instruct the jstree to open all elements before we start clicking checkboxes
		theSH.executeJavaScript(OPEN_ALL_TREE_NODES);
		
		if(revenueAdjustmentDetail.adjustmentItems != null)
			selectRevenueAdjustmentItems(revenueAdjustmentDetail.adjustmentItems.items);
		
		theSH.waitForElementToBeClickable(By.cssSelector(SAVE_BUTTON));
		
		saveRevenueItemAdjustmentButton.click();
		
		// the screen takes a while to close after clicking save so wait for it to have closed
		theSH.waitForElementToBeHidden(By.id(REVENUE_ITEMS_ADJUSTMENT_POPUP));	
	}
	
	public void SendForApproval(){
		theSH.waitForElementToBeClickable(By.linkText(SEND_FOR_APPROVAL_LINK), 10);
		
		sendRevenueItemAdjustmentForApprovalLink.click();
		
		theSH.waitForElementToBeHidden(By.linkText(SEND_FOR_APPROVAL_LINK));
	}

	void selectRevenueAdjustmentItems(List<JsTreeItemPath> aGroup) {
		String rootName = General.getRootItemName(theSH);
		if(rootName != null)
			General.selectJsTreeItems(aGroup, rootName, theSH);
	}

}
