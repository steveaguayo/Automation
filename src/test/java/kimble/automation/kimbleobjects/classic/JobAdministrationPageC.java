package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.SupplierProduct;
import kimble.automation.helpers.General;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.thoughtworks.selenium.webdriven.commands.Click;

public class JobAdministrationPageC {
	
	static final By 
	getJobSummaryBtn = By.xpath("//input[@value='Get Job Summary']"),
	getPendingJobsBtn = By.xpath("//input[@value='Get Pending Jobs']"),
	noJobsPendingText = By.xpath("//div[contains(text(),'No Jobs Pending!')]"),
	failedJobsText = By.xpath("//td[contains(text(),'Failed')]"),
	completedJobsText = By.xpath("//td[contains(text(),'Completed')]"),
	decodeJobArgument = By.xpath("//span[contains(text(),'Decode Job Argument')]"),
	decodedValue = By.xpath("//input[@id='decode-job-arg-result']"),
	encodedValueInput = By.xpath("//input[@id='decode-job-arg']"),
	encodeBtn = By.xpath("//input[@onclick='encodeJobArg();']");
	
	public static void navigateToJobAdministration(SeleniumHelper sh){
		String jobAdmin = sh.getCurrentUrl().split(".com")[0] + ".com/apex/jobadministration";
		sh.goToUrl(jobAdmin);
		sh.waitForElementToBeClickable(getJobSummaryBtn);
	}

	public static boolean pendingJobsExist(SeleniumHelper sh){

		sh.refreshBrowser();
		sh.clickLink(getJobSummaryBtn);
		sh.clickLink(getPendingJobsBtn);
		boolean jobsPendingExist = true;
		
		try{
			sh.waitForElementToBeClickable(By.linkText("Run"));
			jobsPendingExist = true;
			return jobsPendingExist;
		}catch(Exception e){
			sh.waitForElementToBeVisible(noJobsPendingText);
			jobsPendingExist = false;
			return jobsPendingExist;
			
		}
	}
	
	public static void waitForJobsComplete(SeleniumHelper sh) throws Exception{
		navigateToJobAdministration(sh);
		
		boolean pendingJobsExist = pendingJobsExist(sh);
		
		if(pendingJobsExist==true){
			sh.sleep(300000);
			pendingJobsExist = pendingJobsExist(sh);
			
			if(pendingJobsExist==true){
				sh.sleep(300000); 
				pendingJobsExist = pendingJobsExist(sh);
				
				if(pendingJobsExist==true){
					sh.sleep(300000); 
					pendingJobsExist = pendingJobsExist(sh);
					
					if(pendingJobsExist==true){
						sh.sleep(300000); 
						pendingJobsExist = pendingJobsExist(sh);
					}
				}
			}
		}
		failedJobsExist(sh);
	}
	
	public static void failedJobsExist(SeleniumHelper sh) {

		sh.refreshBrowser();
		sh.clickLink(getJobSummaryBtn);
		sh.sleep(5000);
		sh.waitForElementToBeVisible(completedJobsText);
		
		sh.waitForElementToBeHidden(failedJobsText);
	}
	
	public static void encodeJobArgument(SeleniumHelper sh) {

	sh.waitForElementVisible(decodeJobArgument, 10);
	sh.clickLink(decodeJobArgument, 5);
	clearAndInputText(sh, decodedValue, General.objectId + ":" + General.timePeriodId);
	WebElement clickbtn = sh.getWebElement(encodeBtn);
	clickbtn.click();
	//encoding the assignment id and tracking period id
	sh.waitMilliseconds(3000);
	String getEncodedValue = sh.getWebElement(By.id("decode-job-arg")).getAttribute("value");	
	General.encodedValue = getEncodedValue;

	
	}
	
}