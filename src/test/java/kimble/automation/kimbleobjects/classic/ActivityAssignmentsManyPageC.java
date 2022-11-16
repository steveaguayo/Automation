package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import kimble.automation.domain.ActivityAssignment;
import kimble.automation.domain.ActivityAssignmentsMany;

import kimble.automation.helpers.SeleniumHelper;


import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.ClickElement;

import com.thoughtworks.selenium.webdriven.commands.Click;

public class ActivityAssignmentsManyPageC extends BasePageC {

	
	private static final String SELECT_ALL = "input[id$='AllActivityAssignments']";
	private static final String APPLY_CHANGES_TO_ALL = "input[value$='all']";
	private static final String SAVE_CHANGES = "input[value$='Save']";
	private static final String DROPDOWN_SELECT = "select[class$='input-value']";
	private static final String INPUT_VALUE = "input[class$='input-value']";
	private static final String INPUT_DATE_VALUE = "input[data-field-type='DATE']";
	private static final String COST_EFFECTIVE_DATE = "input[class$='effective-date kimbleDatePicker hasDatepicker']";
	private static final String DROPDOWN_SELECT_REVENUE = "select[onchange$='changeOptionsChange()']";
	private static final String COUNTER = "<COUNT>";
	private static final String REVENUE_RESULTS = "//tr[@class='dataRow '][" + COUNTER + "]//td[contains(@data-fld-name,'InvoicingCurrencyForecastRevenueRate__c')]";
	private static final String COST_RESULTS = "//tr[@class='dataRow '][" + COUNTER + "]//td[contains(@data-fld-name,'InvoicingCostRate__c')]";
	private static final String EXPECTED_EFFORT_RESULTS = "//tr[@class='dataRow '][" + COUNTER + "]//td[contains(@data-fld-name,'EntryRemainingUsage__c')]";
	private static final String EXPECTED_UTILISATION = "//tr[@class='dataRow '][" + COUNTER + "]//td[contains(@data-fld-name,'BaselineUtilisationPercentage__c')]";
	private static final String EXPECTED_USAGE_BEHAVIOR = "//tr[@class='dataRow '][" + COUNTER + "]//td[contains(@data-fld-name,'UsageBehaviourRule__c')]/child::*";
	private static final String EXPECTED_START_DATE = "//tr[@class='dataRow '][" + COUNTER + "]//td[contains(@data-fld-name,'StartDate__c')]/child::*";
	
	public ActivityAssignmentsManyPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public int counter;
	public int maxResource; 

	private	 void validateManyAssignmentrow(ActivityAssignmentsMany assignment) {
		//theSH.waitForElementToBeClickable(By.cssSelector(SELECT_ALL));
		//theSH.refreshBrowser();
		
		theSH.waitForElementToBeHidden(By.cssSelector(SAVE_CHANGES));
		theSH.waitForPageLoadComplete(5);
		theSH.waitMilliseconds(3000);
		theSH.waitForElementToBeClickable(By.xpath("//tr[@class='dataRow '][" + counter + "]//child::input[@type='checkbox']"));
		theSH.waitForElementToBePresent(By.xpath("//tr[@class='dataRow '][" + maxResource + "]/child::td[contains(@data-fld-name,'Resource__c')]"));
		
		if (assignment.selectAll != null) counter = 1;
		
		if (assignment.revenueExpected != null) theSH.assertEquals(theSH.getWebElement(By.xpath(REVENUE_RESULTS.replace(COUNTER, String.valueOf(counter)))).getAttribute("data-sort-value"), assignment.revenueExpected, "Expected Revenue Value: " + assignment.revenueExpected);
		if (assignment.costExpected != null) theSH.assertEquals(theSH.getWebElement(By.xpath(COST_RESULTS.replace(COUNTER, String.valueOf(counter)))).getAttribute("data-sort-value"), assignment.costExpected, "Expected Cost Value: " + assignment.costExpected);
		if (assignment.usageBehaviorExpected != null) theSH.assertEquals(theSH.getWebElement(By.xpath(EXPECTED_USAGE_BEHAVIOR.replace(COUNTER, String.valueOf(counter)))).getText(), assignment.usageBehaviorExpected, "Expected Usage Behaviour: " + assignment.usageBehaviorExpected);
		if (assignment.remainingEffortExpected != null) theSH.assertEquals(theSH.getWebElement(By.xpath(EXPECTED_UTILISATION.replace(COUNTER, String.valueOf(counter)))).getAttribute("data-sort-value"), assignment.remainingEffortExpected, "Expected Remaining Effort: " + assignment.remainingEffortExpected);
		if (assignment.utilisationExpected != null) theSH.assertEquals(theSH.getWebElement(By.xpath(EXPECTED_UTILISATION.replace(COUNTER, String.valueOf(counter)))).getAttribute("data-sort-value"), assignment.utilisationExpected, "Expected Utilisation: " + assignment.utilisationExpected);	
		if (assignment.startDateExpected != null) theSH.assertEquals(theSH.getWebElement(By.xpath(EXPECTED_START_DATE.replace(COUNTER, String.valueOf(counter)))).getText(), assignment.startDateExpected, "Expected Start Date: " + assignment.startDateExpected);
	}	
		
	public void ManyAssignmentsedit(List<ActivityAssignmentsMany> assignments, List<ActivityAssignment> resources) throws ParseException {
		theSH.waitForLightningSpinnerToBeHidden();
					
		maxResource = resources.size();	
		
		for (ActivityAssignmentsMany assignmentvariable : assignments)
		{			
							
			if(assignmentvariable.usageBehaviourRule != null)
			{	
				editManyAssignmentUsageBehavior(assignmentvariable);
				validateManyAssignmentrow(assignmentvariable);
			}
			else if(assignmentvariable.revenueRate != null)
			{
				editManyAssignmentRevenue(assignmentvariable);
				validateManyAssignmentrow(assignmentvariable);
			}
			else if(assignmentvariable.costRate != null)
			{
				editManyAssignmentCost(assignmentvariable);
				validateManyAssignmentrow(assignmentvariable);
			}
			else if(assignmentvariable.remainingEffort != null)
			{	
				editManyAssignmentRemaining(assignmentvariable);
				validateManyAssignmentrow(assignmentvariable);
			}
			else if(assignmentvariable.utilisation != null)
			{
				editManyAssignmentUtilisation(assignmentvariable);
				validateManyAssignmentrow(assignmentvariable);
			}
			else if(assignmentvariable.startDate != null)
			{
				editManyAssignmentStartDate(assignmentvariable);
				validateManyAssignmentrow(assignmentvariable);
			}
			
		}
	}	
	
	
	private void editManyAssignmentUsageBehavior(ActivityAssignmentsMany editAssignments) throws ParseException {
	
		theSH.refreshBrowser();
		theSH.waitForElementToBeClickable(By.cssSelector(SELECT_ALL));
		if (editAssignments.selectAll != null)
		{
			//selecting all Resources button
			waitClickable(theSH,By.cssSelector(SELECT_ALL), 10);
			WebElement selectall = theSH.getWebElement(By.cssSelector(SELECT_ALL));
			selectall.click();
			
			theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'UsageBehaviourRule__c')]"));
			WebElement locationpencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'UsageBehaviourRule__c')]//child::i[@class='fa fa-pencil']"));
			theSH.waitForElementToBeVisible(locationpencil);
			locationpencil.click();
			
			//Making changes to the Assignments via the edit pop up box	
			theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
			theSH.selectByVisibleText(By.cssSelector(DROPDOWN_SELECT), editAssignments.usageBehaviourRule);
			//waiting for the Save button to be clickable
			theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
			theSH.clickLink(By.cssSelector(APPLY_CHANGES_TO_ALL));
			theSH.clickLink(By.cssSelector(SAVE_CHANGES));
			
		}			
		else
		{					
			theSH.waitForElementToBeVisible(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
	 		List<WebElement>  AssignmentMany = theSH.getWebElements(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
	 		
	 		counter = 0;
			for (WebElement Resource : AssignmentMany)
			{
				counter++;
				if (Resource.getText().contains(editAssignments.resourceName))
				{				
					break;
				}	
			}
			
			WebElement actualiseCheckBox = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//child::input[@type='checkbox']"));
			actualiseCheckBox.click();
	
			theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'UsageBehaviourRule__c')]"));
			WebElement locationpencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'UsageBehaviourRule__c')]//child::i[@class='fa fa-pencil']"));
			theSH.waitForElementToBeVisible(locationpencil);
			locationpencil.click();
				
			// selecting the value from the dropdown menu		
			theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
			theSH.selectByVisibleText(By.cssSelector(DROPDOWN_SELECT), editAssignments.usageBehaviourRule);
						
			theSH.clickLink(By.cssSelector(SAVE_CHANGES));			
		}
	}
						
		private void editManyAssignmentRevenue(ActivityAssignmentsMany editAssignments) throws ParseException {
			theSH.refreshBrowser();
			theSH.waitForElementToBeClickable(By.cssSelector(SELECT_ALL));
			
			if (editAssignments.selectAll != null)
			{
				//selecting all Resources button
				waitClickable(theSH,By.cssSelector(SELECT_ALL), 10);
				WebElement selectall = theSH.getWebElement(By.cssSelector(SELECT_ALL));
				selectall.click();
				
				theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'InvoicingCurrencyForecastRevenueRate__c')]"));
				WebElement revenuePencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'InvoicingCurrencyForecastRevenueRate__c')]//child::i[@class='fa fa-pencil']"));
				theSH.waitForElementToBeVisible(revenuePencil);
				revenuePencil.click();
				
				// selecting the value from the dropdown menu
				theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
				theSH.selectByVisibleText(By.cssSelector(DROPDOWN_SELECT_REVENUE), editAssignments.revenueRateChange);
				
				theSH.sendKeysIfVisibleAndEnabled(By.cssSelector(INPUT_VALUE), editAssignments.revenueRate);

				//clicking Select All button
				theSH.clickLink(By.cssSelector(APPLY_CHANGES_TO_ALL));
				theSH.clickLink(By.cssSelector(SAVE_CHANGES));			
			}			
			else
			{					
				theSH.waitForElementToBeVisible(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
				
				
				
				List<WebElement>  AssignmentMany = theSH.getWebElements(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
				counter = 0;
				for (WebElement Resource : AssignmentMany)
				{
					counter++;
					// looping until the correct Resource has been found						
					if (Resource.getText().contains(editAssignments.resourceName))
					{
						break;
					}				
				}
				//clicking the checkbox of the selected Resource	
				WebElement actualiseCheckBox = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//child::input[@type='checkbox']"));
				actualiseCheckBox.click();	
				//	WebElement rowhover = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[@data-fld-name='Location']"));
				theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'InvoicingCurrencyForecastRevenueRate__c')]"));
				WebElement revenuePencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'InvoicingCurrencyForecastRevenueRate__c')]//child::i[@class='fa fa-pencil']"));
				theSH.waitForElementToBeVisible(revenuePencil);
				revenuePencil.click();
				
				// selecting the value from the dropdown menu
				theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
				theSH.selectByVisibleText(By.cssSelector(DROPDOWN_SELECT_REVENUE), editAssignments.revenueRateChange);
				
				theSH.sendKeysIfVisibleAndEnabled(By.cssSelector(INPUT_VALUE), editAssignments.revenueRate);
						
				theSH.clickLink(By.cssSelector(SAVE_CHANGES));			
			}
		}
		
		private void editManyAssignmentCost(ActivityAssignmentsMany editAssignments) throws ParseException {
			theSH.refreshBrowser();
			theSH.waitForElementToBeClickable(By.cssSelector(SELECT_ALL));
			if (editAssignments.selectAll != null)
			{
				//selecting all Resources button
				waitClickable(theSH,By.cssSelector(SELECT_ALL), 10);
				WebElement selectall = theSH.getWebElement(By.cssSelector(SELECT_ALL));
				selectall.click();
				
				theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'InvoicingCostRate__c')]"));
				WebElement costPencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'InvoicingCostRate__c')]//child::i[@class='fa fa-pencil']"));
				theSH.waitForElementToBeVisible(costPencil);
				costPencil.click();
				
				theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
				theSH.waitForElementToBeVisible(By.cssSelector(INPUT_VALUE));
				theSH.clearAndSendKeysIfVisibleAndEnabled(By.cssSelector(INPUT_VALUE), editAssignments.costRate);
				theSH.waitForElementToBeClickable(By.cssSelector(COST_EFFECTIVE_DATE));
				theSH.waitForElementToBeVisible(By.cssSelector(COST_EFFECTIVE_DATE));
				theSH.clearAndSendKeysIfVisibleAndEnabled(By.cssSelector(COST_EFFECTIVE_DATE), editAssignments.startDate);

				//clicking Select All button
				theSH.clickLink(By.cssSelector(APPLY_CHANGES_TO_ALL));
				theSH.clickLink(By.cssSelector(SAVE_CHANGES));		
			}			
			else
			{					
				theSH.waitForElementToBeVisible(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
		 		List<WebElement>  AssignmentMany = theSH.getWebElements(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
				counter = 0;
				for (WebElement Resource : AssignmentMany)
				{
					counter++;
					// looping until the correct Resource has been found						
					if (Resource.getText().contains(editAssignments.resourceName))
					{
						break;
					}				
				}
				//clicking the checkbox of the selected Resource	
				WebElement actualiseCheckBox = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//child::input[@type='checkbox']"));
				actualiseCheckBox.click();	
				//	WebElement rowhover = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[@data-fld-name='Location__c']"));
				theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'InvoicingCostRate__c')]"));
				WebElement costPencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'InvoicingCostRate__c')]//child::i[@class='fa fa-pencil']"));
				theSH.waitForElementToBeVisible(costPencil);
				costPencil.click();
				
				theSH.waitForElementToBeVisible(By.cssSelector(INPUT_VALUE));					
				theSH.clearAndSendKeysIfVisibleAndEnabled(By.cssSelector(INPUT_VALUE), editAssignments.costRate);
				theSH.waitForElementToBeClickable(By.cssSelector(COST_EFFECTIVE_DATE));
				theSH.waitForElementToBeVisible(By.cssSelector(COST_EFFECTIVE_DATE));
				theSH.clearAndSendKeysIfVisibleAndEnabled(By.cssSelector(COST_EFFECTIVE_DATE), editAssignments.startDate);
				theSH.clickLink(By.cssSelector(SAVE_CHANGES));				
			}			
		}
		
		private void editManyAssignmentRemaining(ActivityAssignmentsMany editAssignments) throws ParseException {
			theSH.refreshBrowser();
			theSH.waitForElementToBeClickable(By.cssSelector(SELECT_ALL));
			if (editAssignments.selectAll != null)
			{
				//selecting all Resources button
				waitClickable(theSH,By.cssSelector(SELECT_ALL), 10);
				WebElement selectall = theSH.getWebElement(By.cssSelector(SELECT_ALL));
				selectall.click();
				
				theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'EntryRemainingUsage__c')]"));
				WebElement remainingPencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'EntryRemainingUsage__c')]//child::i[@class='fa fa-pencil']"));
				theSH.waitForElementToBeVisible(remainingPencil);
				remainingPencil.click();
				
				//Making changes to the Assignments via the edit pop up box		
				// selecting the value from the dropdown menu
				theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
				theSH.selectByVisibleText(By.cssSelector(DROPDOWN_SELECT_REVENUE), editAssignments.remainingEffortChange);
				
				theSH.clearAndSendKeysIfVisibleAndEnabled(By.cssSelector(INPUT_VALUE), editAssignments.remainingEffort);

				//clicking Select All button
				theSH.clickLink(By.cssSelector(APPLY_CHANGES_TO_ALL));
				theSH.clickLink(By.cssSelector(SAVE_CHANGES));
					
			}			
			else
			{					
				theSH.waitForElementToBeVisible(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
		 		List<WebElement>  AssignmentMany = theSH.getWebElements(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
				counter = 0;
				for (WebElement Resource : AssignmentMany)
				{
					counter++;
					// looping until the correct Resource has been found						
					if (Resource.getText().contains(editAssignments.resourceName))
					{
						break;
					}				
				}
				//clicking the checkbox of the selected Resource	
				WebElement actualiseCheckBox = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//child::input[@type='checkbox']"));
				actualiseCheckBox.click();	
				//	WebElement rowhover = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[@data-fld-name='Location__c']"));
				theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'EntryRemainingUsage__c')]"));
				WebElement remainingPencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'EntryRemainingUsage__c')]//child::i[@class='fa fa-pencil']"));
				theSH.waitForElementToBeVisible(remainingPencil);
				remainingPencil.click();
				
				// selecting the value from the dropdown menu
				theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
				theSH.selectByVisibleText(By.cssSelector(DROPDOWN_SELECT_REVENUE), editAssignments.remainingEffortChange);
				
				theSH.clearAndSendKeysIfVisibleAndEnabled(By.cssSelector(INPUT_VALUE), editAssignments.remainingEffort);
						
				theSH.clickLink(By.cssSelector(SAVE_CHANGES));			
			}
		
		 }
		private void editManyAssignmentUtilisation(ActivityAssignmentsMany editAssignments) throws ParseException {		
			theSH.refreshBrowser();
			theSH.waitForElementToBeClickable(By.cssSelector(SELECT_ALL));
			if (editAssignments.selectAll != null)
			{
				//selecting all Resources button
				waitClickable(theSH,By.cssSelector(SELECT_ALL), 10);
				WebElement selectall = theSH.getWebElement(By.cssSelector(SELECT_ALL));
				selectall.click();
				
				theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'BaselineUtilisationPercentage__c')]"));
				WebElement remainingPencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'BaselineUtilisationPercentage__c')]//child::i[@class='fa fa-pencil']"));
				theSH.waitForElementToBeVisible(remainingPencil);
				remainingPencil.click();
				
				//Making changes to the Assignments via the edit pop up box		
				// selecting the value from the dropdown menu
				
				theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
				theSH.sendKeysIfVisibleAndEnabled(By.cssSelector(INPUT_VALUE), editAssignments.utilisation);

				//clicking Select All button
				theSH.clickLink(By.cssSelector(APPLY_CHANGES_TO_ALL));
				theSH.clickLink(By.cssSelector(SAVE_CHANGES));
					
			}			
			else
			{					
				theSH.waitForElementToBeVisible(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
		 		List<WebElement>  AssignmentMany = theSH.getWebElements(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
				counter = 0;
				for (WebElement Resource : AssignmentMany)
				{
					counter++;
					// looping until the correct Resource has been found						
					if (Resource.getText().contains(editAssignments.resourceName))
					{
						break;
					}				
				}
				//clicking the checkbox of the selected Resource	
				WebElement actualiseCheckBox = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//child::input[@type='checkbox']"));
				actualiseCheckBox.click();	
				//	WebElement rowhover = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[@data-fld-name='Location__c']"));
				theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'BaselineUtilisationPercentage__c')]"));
				WebElement remainingPencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'BaselineUtilisationPercentage__c')]//child::i[@class='fa fa-pencil']"));
				theSH.waitForElementToBeVisible(remainingPencil);
				remainingPencil.click();
				
				// selecting the value from the dropdown menu
				
				theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
				theSH.clearAndSendKeysIfVisibleAndEnabled(By.cssSelector(INPUT_VALUE), editAssignments.utilisation);
						
				theSH.clickLink(By.cssSelector(SAVE_CHANGES));			
			}
		
		 }
		
		
		private void editManyAssignmentStartDate(ActivityAssignmentsMany editAssignments) throws ParseException {	
			theSH.refreshBrowser();
			theSH.waitForElementToBeClickable(By.cssSelector(SELECT_ALL));
			if (editAssignments.selectAll != null)
			{
				//selecting all Resources button
				waitClickable(theSH,By.cssSelector(SELECT_ALL), 10);
				WebElement selectall = theSH.getWebElement(By.cssSelector(SELECT_ALL));
				selectall.click();
				
				theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'StartDate__c')]"));
				WebElement startDatePencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][1]//td[contains(@data-fld-name,'StartDate__c')]//child::i[@class='fa fa-pencil']"));
				theSH.waitForElementToBeVisible(startDatePencil);
				startDatePencil.click();
				
				//Making changes to the Assignments via the edit pop up box		
				// selecting the value from the dropdown menu
				
				theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
				theSH.selectByVisibleText(By.cssSelector(DROPDOWN_SELECT_REVENUE), editAssignments.startDateChange);
						
				theSH.sendKeysIfVisibleAndEnabled(By.cssSelector(INPUT_DATE_VALUE), editAssignments.startDate);

				//clicking Select All button
				theSH.clickLink(By.cssSelector(APPLY_CHANGES_TO_ALL));
				theSH.clickLink(By.cssSelector(SAVE_CHANGES));
					
			}			
			else
			{					
				theSH.waitForElementToBeVisible(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
		 		List<WebElement>  AssignmentMany = theSH.getWebElements(By.xpath("//tr[@class='dataRow ']/child::td[contains(@data-fld-name,'Resource__c')]//child::div"));
				counter = 0;
				for (WebElement Resource : AssignmentMany)
				{
					counter++;
					// looping until the correct Resource has been found						
					if (Resource.getText().contains(editAssignments.resourceName))
					{
						break;
					}				
				}
				//clicking the checkbox of the selected Resource	
				WebElement actualiseCheckBox = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//child::input[@type='checkbox']"));
				actualiseCheckBox.click();	
				//	WebElement rowhover = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[@data-fld-name='Location__c']"));
				theSH.hoverOnElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'StartDate__c')]"));
				WebElement startDatePencil = theSH.getWebElement(By.xpath("//tr[@class='dataRow '][" + counter + "]//td[contains(@data-fld-name,'StartDate__c')]//child::i[@class='fa fa-pencil']"));
				theSH.waitForElementToBeVisible(startDatePencil);
				startDatePencil.click();
				
				// selecting the value from the dropdown menu
				
				theSH.waitForElementToBeClickable(By.cssSelector(SAVE_CHANGES));
				theSH.selectByVisibleText(By.cssSelector(DROPDOWN_SELECT_REVENUE), editAssignments.startDateChange);
				
				theSH.sendKeysIfVisibleAndEnabled(By.cssSelector(INPUT_DATE_VALUE), editAssignments.startDate);
						
				theSH.clickLink(By.cssSelector(SAVE_CHANGES));			
			}
		
		 }
	}



		
	

			
			
			