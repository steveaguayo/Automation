package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.Collection;

import kimble.automation.domain.Risk;
import kimble.automation.domain.results.RisksSalesResults;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RisksPageC extends BasePageC {

	private static final By newRisk = By.xpath("//a[normalize-space(text())='New Risk']");
	
	// These are 'templated' so the actual id can be switched in place of the {{id}} string
//	private static final String edit_link = "a.risk-id-{{id}}[title='Edit'] span";
	
	private static final By edit_link = By.xpath("//a[@class='tool-item raid-edit-button']");
	
	static final String assignment_cog = "//tr[@item-id='{{id}}']//i[@class='fa fa-cog item-actions']";
	static final String delete_icon = "//div[@item-id='{{id}}']//i[@class='fa fa-trash-o']";
	static final String save_button = "//div[@id='pageEditMode']//input[@value='Save']";
	
	// Value selectors
		// This is 'templated' so the actual reference can be switched in place of the {{reference}} string
	static final String referenceLink = "//a[text()='{{reference}}']";

	static final By riskLevel = By.cssSelector("td.header-risk-outcome-cell");

	// Risk Input selectors
	static final By reference = By.cssSelector("input[id$='RiskNameInput']");
	static final By category = By.cssSelector("select[id$='RiskCategoryInput']");
	static final By summary = By.cssSelector("input[id$='RiskSummaryInput']");
	// no class or id so select by xpath based on preceding label
	static final By raisedDate = By.xpath("//label[contains(text(),'Raised Date')]/parent::th/following-sibling::td//input");
	
	// save button
	static final By saveRisk = By.cssSelector("input[value='Save']");
	
	static final By riskProbability = By.cssSelector("[id$='RiskProbabilityInput']");
	static final By riskImpact = By.cssSelector("[id$='RiskImpactInput']");
	//static final By riskStatus = By.xpath("//select/option[normalize-space(text())='Active']//..//@size");
	static final By riskStatus = By.xpath("//select/option[normalize-space(text())='Active']//ancestor::select");
	static final By internalOnly = By.xpath("//th[contains(text(), 'Internal Only')]//..//input");
		
	public RisksPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void create(Risk risk) {
		createOrUpdate(newRisk, risk);

		if(risk.id == null)
			populateId(risk);
	}
	
	public void edit(Risk risk) {
		if(risk.id == null)
			populateId(risk);
		By selector = By.xpath(assignment_cog.replace("{{id}}", risk.id));
		createOrUpdate(selector, risk);

		if(risk.id == null)
			populateId(risk);
	}
	
	public void createOrUpdate(By buttonSelector, Risk risk) {
		theSH.waitForLightningSpinnerToBeHidden();
		if(theSH.isLightning()){
			waitClickable(theSH, buttonSelector, 10);
			click(theSH, buttonSelector);
			theSH.waitForLightningSpinnerToBeHidden();
			waitClickable(theSH, reference, 10);
		}
		else if(risk.id != null){
			executeSequenceWithRefreshRetry(theSH, 3, () -> {
				
				clickAndWaitSequence(theSH, 20,                 
				/* click create/update button		*/ buttonSelector,
				/* wait for reference input			*/ edit_link
			);
				clickAndWaitSequence(theSH, 20, edit_link, reference);
		});
		}
		else{executeSequenceWithRefreshRetry(theSH, 3, () -> {
			clickAndWaitSequence(theSH, 20, buttonSelector, reference);
		});
			
		}
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			/* input reference					*/ clearAndInputText(theSH, reference, risk.reference);
			/* select category					*/ dropdownSelect(theSH, category, risk.category);
			/* input summary					*/ clearAndInputText(theSH, summary, risk.summary);
			/* input raised date				*/ clearAndInputText(theSH, raisedDate, risk.raisedDate);
			/* select risk probability			*/ dropdownSelect(theSH, riskProbability, risk.probability);
			/* select risk impact				*/ dropdownSelect(theSH, riskImpact, risk.impact);
			/* select risk status				*/ dropdownSelect(theSH, riskStatus, risk.status);
			/* wait for clickable				*/ theSH.waitForElementToBeClickable(internalOnly, 20);
			/* select internal only				*/ checkboxSelect(theSH, internalOnly, risk.internalOnly);
			if(theSH.isLightning()){
				waitClickable(theSH, saveRisk, 5);
				click(theSH, saveRisk);
				theSH.waitForLightningSpinnerToBeHidden();
				waitClickable(theSH, buttonSelector, 5);
			}
			else{
				clickAndWaitSequence(theSH, 60, 
				/* click save						*/ saveRisk,
				/* wait for create/update button	*/ buttonSelector
				);				
			}

		});
	}
	
	public By getReferenceLink(String reference) {
		return By.xpath(referenceLink.replace("{{reference}}", reference));
	}
	
	public void populateId(Risk risk) {
		By selector = getReferenceLink(risk.reference);
		theSH.waitForElementToBeClickable(selector, 20);
		WebElement link = theSH.getWebElement(selector);
		risk.id = link.getAttribute("href").split("id=")[1];
	}
	
	public void delete(Risk risk) {
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			/* get the risk id					*/ if(risk.id == null) populateId(risk);
			/* create selector					*/ By delete = By.xpath(assignment_cog.replace("{{id}}", risk.id));
			/* create delete icon selector      */ By deleteIcon = By.xpath(delete_icon.replace("{{id}}", risk.id));
			/* create save button selector      */ By saveButton = By.xpath(save_button);
//			clickAndWaitSequence(theSH, 20, delete, deleteIcon); 
//			clickAndWaitSequence(theSH, 20, deleteIcon, alertOk); 
//			clickAndWaitSequence(theSH, 20, alertOk, saveButton); 
//			clickAndWaitSequence(theSH, 20, saveButton, alertOk);
//			clickAndWaitSequence(theSH, 20, alertOk, newRisk);
			
			clickAndWaitSequence(theSH, 20, delete, deleteIcon);								
			clickAndWaitSequence(theSH, 20, deleteIcon, theSH.alertOkButton); 
			clickAndWaitSequence(theSH, 20, theSH.alertOkButton, saveButton); 
			clickAndWaitSequence(theSH, 20, saveButton, theSH.alertOkButton);
			clickAndWaitSequence(theSH, 20, theSH.alertOkButton, newRisk);				
			
			/* wait for the reference to go		*/ waitHidden(theSH, getReferenceLink(risk.reference), 60);
		
		});
	}
	
	public void validateRiskLevel(String expected) {
		theSH.refreshBrowser();
		theSH.waitForElementToBeVisible(riskLevel);
		theSH.waitMilliseconds(5000);
		String actual = theSH.getWebElement(riskLevel).getText();
		theSH.assertEquals(actual, expected, "Risk level value validation failed");
	}
	
	public void validate(RisksSalesResults expected) {
		validateRiskLevel(expected.riskLevel);
	}
	public void validateExistence(Collection<Risk> expected, String stage) {
		for(Risk r : expected)
			if(r.stage.equals(stage))
				validateExistence(r);
	}
	
	public void validateExistence(Collection<Risk> expected) {
		for(Risk r : expected)
			validateExistence(r);
	}
	
	public void validateExistence(Risk expected) {
		try {
			theSH.checkLinkExists(By.xpath(referenceLink));
		} catch(Exception e) {
			throw new RuntimeException("The risk: " + expected.reference + " doesn't exist", e);
		}
	}
}
