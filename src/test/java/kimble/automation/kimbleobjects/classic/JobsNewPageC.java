package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.text.ParseException;
import java.util.List;

import kimble.automation.helpers.General;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class JobsNewPageC extends BasePageC {

	
	static final By jobNewButton = By.xpath("//td[@class='pbButton']/input");
	static final By jobName = By.xpath("//input[@id='Name']");
	static final By jobStatus = By.xpath("//div[1]/div[2]/table/tbody/tr/td[2]/form/div/div[2]/div[3]/table/tbody/tr[5]/td[2]/span/input");
	static final By contextID = By.xpath("//div[1]/div[2]/table/tbody/tr/td[2]/form/div/div[2]/div[3]/table/tbody/tr[3]/td[2]/input");
	static final By jobParameter = By.xpath("//div[1]/div[2]/table/tbody/tr/td[2]/form/div/div[2]/div[3]/table/tbody/tr[4]/td[2]/textarea");
	static final By jobPriority = By.xpath("//div[1]/div[2]/table/tbody/tr/td[2]/form/div/div[2]/div[3]/table/tbody/tr[4]/td[4]/span/input");
	static final By jobSaveButton = By.xpath("//td[@id='bottomButtonRow']/input[1]");

	static final By jobRunNowButton = By.xpath("//td[@id='topButtonRow']/input[@value='Run Now']");
	private static final String JOB_COMPLETED = "Completed";

	public static final By allTabs = By.cssSelector("img.allTabsArrow");
	public static final By jobsTab = By.xpath("//div[@class='pbBody']//a[.='Jobs']");
	
	public JobsNewPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public static void createNewJob(SeleniumHelper sh, String name, String status, String assignmentID) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
						
			click(sh, allTabs);
			waitClickable(sh, jobsTab, 20);
			click(sh, jobsTab);
			waitClickable(sh, jobNewButton, 20);
			
			clickAndWaitSequence(sh, 20, 
			/* click new					*/ 			jobNewButton,
			/* wait for jobName				*/ 			jobSaveButton
			);

			/* input the name				*/			clearAndInputText(sh, jobName, name);	
			/* input the context ID			*/			clearAndInputText(sh, contextID, assignmentID);	
			/* input the priority			*/			clearAndInputText(sh, jobPriority, "high");	
			/* input the status				*/			clearAndInputText(sh, jobStatus, status);		
			
			clickAndWaitSequence(sh, 20, 
			/* click new					*/ 			jobSaveButton,
			/* wait for Run Now				*/ 			jobRunNowButton
			);
			clickAndWaitSequence(sh, 20, 
			/* click new					*/ 			jobRunNowButton,
			/* wait for Run Now				*/ 			jobRunNowButton
			);
			sh.waitForPageToContainTextWithRetry(JOB_COMPLETED, 40);
		});
	}
	
	public static void createNewJobWithCustomParameter(SeleniumHelper sh, String name, String status, String assignmentID) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
						
			click(sh, allTabs);
			waitClickable(sh, jobsTab, 20);
			click(sh, jobsTab);
			waitClickable(sh, jobNewButton, 20);
			
			clickAndWaitSequence(sh, 20, 
			/* click new					*/ 			jobNewButton,
			/* wait for jobName				*/ 			jobSaveButton
			);

			/* input the name				*/			clearAndInputText(sh, jobName, name);	
			/* input the parameter			*/			clearAndInputText(sh, jobParameter, assignmentID);	
			/* input the priority			*/			clearAndInputText(sh, jobPriority, "high");	
			/* input the status				*/			clearAndInputText(sh, jobStatus, status);		
			
			clickAndWaitSequence(sh, 20, 
			/* click new					*/ 			jobSaveButton,
			/* wait for Run Now				*/ 			jobRunNowButton
			);
			clickAndWaitSequence(sh, 20, 
			/* click new					*/ 			jobRunNowButton,
			/* wait for Run Now				*/ 			jobRunNowButton
			);
			sh.waitForPageToContainTextWithRetry(JOB_COMPLETED, 40);
		});
	}		
	
	public static void seedEncodedJob(SeleniumHelper sh, String name, String status) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
						
			click(sh, allTabs);
			waitClickable(sh, jobsTab, 20);
			click(sh, jobsTab);
			waitClickable(sh, jobNewButton, 20);
			
			clickAndWaitSequence(sh, 20, 
			/* click new					*/ 			jobNewButton,
			/* wait for jobName				*/ 			jobSaveButton
			);
			/* input the name				*/			clearAndInputText(sh, jobName, name);	
														sh.waitMilliseconds(1500);
			/* input the context ID			*/			clearAndInputText(sh, jobParameter, General.encodedValue);	
														sh.waitMilliseconds(1500);
			/* input the priority			*/			clearAndInputText(sh, jobPriority, "high");	
														sh.waitMilliseconds(1500);
			/* input the status				*/			clearAndInputText(sh, jobStatus, status);		
			
			clickAndWaitSequence(sh, 20, 
			/* click new					*/ 			jobSaveButton,
			/* wait for Run Now				*/ 			jobRunNowButton
			);
			clickAndWaitSequence(sh, 20, 
			/* click new					*/ 			jobRunNowButton,
			/* wait for Run Now				*/ 			jobRunNowButton
			);
			sh.waitForPageToContainTextWithRetry(JOB_COMPLETED, 40);
		});
	}
}
