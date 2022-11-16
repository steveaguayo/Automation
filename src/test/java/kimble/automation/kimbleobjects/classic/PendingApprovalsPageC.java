package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.Account;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class PendingApprovalsPageC {
	
	static final By 
	allPendingApprovalRequestsSwitch = By.xpath("//div[@class='approval-mode-toggle-container']//span[.='All Pending Approval Requests']"),
	filterButton = By.xpath("//i[@class='fa fa-filter']"),
	filterGo = By.xpath("//input[@value='Go']"),
	clearFilterButton = By.xpath("//input[@value='Clear All']"),
	timesheetBtn = By.xpath("//div[@class='charms-card-body']//span[.='Timesheet']"),
	expenseBtn = By.xpath("//div[@class='charms-card-body']//span[.='Expense Claim']"),
	approvalCheckbox = By.id("AllPendingApprovals"),
	approvalBurgerMenu = By.xpath("//div[@class='inner-card-header']/div[3]"),
	approveSelected = By.linkText("Approve Selected"),
	rejectSelected =  By.linkText("Reject Selected"),
	approveBtn = By.xpath("//table/tbody/tr/td/div[2]/span/form/div[2]/div/div/div[2]/table/tbody/tr/td[2]/input[1]"),
	rejectBtn =  By.xpath("//input[@value='Reject']"),
	loadingScreen = By.id("loadingIndicatorInnerContainer"),
	nothingToApprove = By.linkText("Nothing To Approve");
	
	
	public static void approveOrRejectTimesheet(SeleniumHelper sh, String activityName, Boolean approveTrue){
		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForElementToBeClickable(allPendingApprovalRequestsSwitch, 15);
			
			//1.31 pending approvals filtered by business unit group
			sh.clickAndWait(filterButton, filterGo, 5);
			sh.clickLink(clearFilterButton);
			sh.clickLink(filterGo);
			
			sh.waitForElementToBeClickable(allPendingApprovalRequestsSwitch, 15);
			sh.clickAndWait(allPendingApprovalRequestsSwitch, timesheetBtn, 5);
			sh.clickLink(timesheetBtn);
			sh.clickLink(By.partialLinkText(activityName));
			sh.waitForElementToBeHidden(loadingScreen);
			sh.clickLink(approvalCheckbox);
			sh.clickLink(approvalBurgerMenu);
			
			if(approveTrue==true){
				sh.clickLink(approveSelected);
				sh.clickLink(approveBtn);
			}
			else if(!approveTrue==true){
				sh.clickLink(rejectSelected);
				sh.clickLink(rejectBtn);
			}
		});
		
		sh.waitForElementToBeVisible(nothingToApprove);
	}
	
	public static void approveOrRejectExpense(SeleniumHelper sh, String activityName, Boolean approveTrue){

		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.waitForElementToBeClickable(allPendingApprovalRequestsSwitch, 15);
			//1.31 pending approvals filtered by business unit group
			sh.clickAndWait(filterButton, filterGo, 5);
			sh.clickLink(clearFilterButton);
			sh.clickLink(filterGo);
			
			sh.waitForElementToBeClickable(allPendingApprovalRequestsSwitch, 15);
			sh.clickAndWait(allPendingApprovalRequestsSwitch, expenseBtn, 5);
			sh.clickLink(expenseBtn);
			sh.clickLink(By.partialLinkText(activityName));
			sh.waitForElementToBeHidden(loadingScreen);
			sh.clickLink(approvalCheckbox);
			sh.clickLink(approvalBurgerMenu);
			
			if(approveTrue==true){
				sh.clickLink(approveSelected);
				sh.clickLink(approveBtn);
			}
			else if(!approveTrue==true){
				sh.clickLink(rejectSelected);
				sh.clickLink(rejectBtn);
			}
		});
		
		sh.waitForElementToBeVisible(nothingToApprove);
	}
	
	
}
