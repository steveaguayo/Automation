package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.Collection;

import kimble.automation.domain.Risk;
import kimble.automation.domain.results.RiskDashboardResults;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class RiskDashboardPageC extends BasePageC {
	
	// Value selectors -> maybe worth adding a class per cell and/(or not) per row
	static final By technicalImpact = By.cssSelector(".left-hand-panel .risk-summary-table tr:nth-child(1) .risk-attribute-cell:nth-child(2)");
	static final By technicalProbability = By.cssSelector(".left-hand-panel .risk-summary-table tr:nth-child(1) .risk-attribute-cell:nth-child(3)");
	static final By technicalSeverity = By.cssSelector(".left-hand-panel .risk-summary-table tr:nth-child(1) .risk-attribute-cell:nth-child(4)");

	static final By commercialImpact = By.cssSelector(".left-hand-panel .risk-summary-table tr:nth-child(2) .risk-attribute-cell:nth-child(2)");
	static final By commercialProbability = By.cssSelector(".left-hand-panel .risk-summary-table tr:nth-child(2) .risk-attribute-cell:nth-child(3)");
	static final By commercialSeverity = By.cssSelector(".left-hand-panel .risk-summary-table tr:nth-child(2) .risk-attribute-cell:nth-child(4)");

	static final By managementImpact = By.cssSelector(".left-hand-panel .risk-summary-table tr:nth-child(3) .risk-attribute-cell:nth-child(2)");
	static final By managementProbability = By.cssSelector(".left-hand-panel .risk-summary-table tr:nth-child(3) .risk-attribute-cell:nth-child(3)");
	static final By managementSeverity = By.cssSelector(".left-hand-panel .risk-summary-table tr:nth-child(3) .risk-attribute-cell:nth-child(4)");

	static final By riskLevel = By.cssSelector("td.header-risk-outcome-cell");

	// Risk creation selectors	-> maybe worth adding a class per row
	static final By newTechnicalRisk = By.cssSelector(".risk-summary-table tr:nth-child(1) .add-risk-icon");
	static final By newCommercialRisk = By.cssSelector(".risk-summary-table tr:nth-child(2) .add-risk-icon");
	static final By newManagementRisk = By.cssSelector(".risk-summary-table tr:nth-child(3) .add-risk-icon");
	
	// Risk Input selectors
	static final By riskReference = By.cssSelector("input[id$='RiskNameInput']");
	static final By riskSummary = By.cssSelector("input[id$='RiskSummaryInput']");
		// maybe worth adding a class or id
	static final By riskRaisedDate = By.cssSelector("input[id$='j_id118']");
	
	// save button
	static final By saveRisk = By.cssSelector("input[value='Save']");

	static final By riskProbability = By.cssSelector("[id$='RiskProbabilityInput']");
	static final By riskImpact = By.cssSelector("[id$='RiskImpactInput']");
		
	public RiskDashboardPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void createRisks(Collection<Risk> risks, String stage){
		for(Risk r : risks)
			if(r.stage.equals(stage))
				createRisk(r);
	}
	
	public void createRisk(Risk risk){
		By buttonSelector;
		theSH.waitForLightningSpinnerToBeHidden();
		if(risk.category.equals("Technical"))
			buttonSelector = newTechnicalRisk;
		else if(risk.category.equals("Commercial"))
			buttonSelector = newCommercialRisk;
		else if(risk.category.equals("Management"))
			buttonSelector = newManagementRisk;
		else
			throw new RuntimeException("Only the values Technical, Commercial and Management are valid risk categories. Given value was: " + risk.category);

		RisksPageC riskPage = new RisksPageC(theSH);
		riskPage.createOrUpdate(buttonSelector, risk);
	}
	
	public void validateTechnicalImpact(String expected) {
		String actual = theSH.getWebElement(technicalImpact).getText();
		theSH.assertEquals(expected, actual, "Technical impact value validation failed");
	}
	
	public void validateTechnicalProbability(String expected) {
		String actual = theSH.getWebElement(technicalProbability).getText();
		theSH.assertEquals(expected, actual, "Technical probability value validation failed");
	}
	
	public void validateTechnicalSeverity(String expected) {
		String actual = theSH.getWebElement(technicalSeverity).getText();
		theSH.assertEquals(expected, actual, "Technical severity value validation failed");
	}
	
	
	public void validateCommercialImpact(String expected) {
		String actual = theSH.getWebElement(commercialImpact).getText();
		theSH.assertEquals(expected, actual, "Commercial impact value validation failed");
	}
	
	public void validateCommercialProbability(String expected) {
		String actual = theSH.getWebElement(commercialProbability).getText();
		theSH.assertEquals(expected, actual, "Commercial probability value validation failed");
	}
	
	public void validateCommercialSeverity(String expected) {
		String actual = theSH.getWebElement(commercialSeverity).getText();
		theSH.assertEquals(expected, actual, "Commercial severity value validation failed");
	}
	
	
	public void validateManagementImpact(String expected) {
		String actual = theSH.getWebElement(managementImpact).getText();
		theSH.assertEquals(expected, actual, "Management impact value validation failed");
	}
	
	public void validateManagementProbability(String expected) {
		String actual = theSH.getWebElement(managementProbability).getText();
		theSH.assertEquals(expected, actual, "Management probability value validation failed");
	}
	
	public void validateManagementSeverity(String expected) {
		String actual = theSH.getWebElement(managementSeverity).getText();
		theSH.assertEquals(expected, actual, "Management severity value validation failed");
	}
	
	
	public void validateRiskLevel(String expected) {
		String actual = theSH.getWebElement(riskLevel).getText();
		theSH.assertEquals(expected, actual, "Risk level value validation failed");
	}
	
	public void validate(RiskDashboardResults expected) {
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			validateStringValue(theSH, technicalImpact, expected.technicalRiskImpact, "technical impact");
			validateStringValue(theSH, technicalProbability, expected.technicalRiskProbability, "technical probability");
			validateStringValue(theSH, technicalSeverity, expected.technicalRiskSeverity, "technical severity");

			validateStringValue(theSH, commercialImpact, expected.commercialRiskImpact, "commercial impact");
			validateStringValue(theSH, commercialProbability, expected.commercialRiskProbability, "commercial probability");
			validateStringValue(theSH, commercialSeverity, expected.commercialRiskSeverity, "commercial severity");

			validateStringValue(theSH, managementImpact, expected.managementRiskImpact, "management impact");
			validateStringValue(theSH, managementProbability, expected.managementRiskProbability, "management probability");
			validateStringValue(theSH, managementSeverity, expected.managementRiskSeverity, "management severity");

			validateStringValue(theSH, riskLevel, expected.riskLevel, "risk level");
		});
	}
}
