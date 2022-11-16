package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.Collection;
import java.util.List;

import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.Opportunity;
import kimble.automation.domain.Proposal;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByCssSelector;

public class ProposalEditPageC {
	
	//public static final String RISK_CONTINGENCY_PRODUCT_LINK = "//a[text()='Risk Contingency']";
	//public static final String ENGAGEMENT_BURGER_MENU = "//div[@kimble-tip='EngagementBurgermenu']";
	
	public static final String
	SELECT_ID$_PROPOSAL_PROPOSITION_SELECT = "select[id$='proposalProposition']",
	SELECT_ID$_ACCOUNT_NAME = "input[id$='accountName']",
	ENGAGEMENT_BURGER_MENU = "div[id='proposal-menu']",
	PROPOSAL_ALTERNATIVES = "//div[@id='proposal-alternatives']",
	ALTERNATE_PROPOSAL = "//span[contains(text(), 'This is an Alternative to a Preferred Proposal')]",
	ENGAGEMENT_LINK = "//a[normalize-space(text())=\"{{engagement}}\"]",
	ENGAGEMENT_CARD = ENGAGEMENT_LINK + "/ancestor::div[contains(@class, 'delivery-group card')]",
	ENGAGEMENT_START_DATE = ENGAGEMENT_CARD + "//span[@class='group-dates']/span[1]",
	ENGAGEMENT_END_DATE = ENGAGEMENT_CARD + "//span[@class='group-dates']/span[2]",
	ENGAGEMENT_REVENUE = ENGAGEMENT_CARD + "//span[@kimble-tip='EngagementRevenue']/span",
	elementTitle = ENGAGEMENT_LINK + "/ancestor::div[@class='delivery-group card']//div[@class='inner-card-title'][normalize-space(text())=\"{{element}}\"]",
	elementBurgerMenu = elementTitle + "/following-sibling::div[contains(@class, 'delivery-element-menu')]",
	elementInnerHeader = elementTitle + "/ancestor::div[@class='inner-card-header']",
	elementInnerCard = elementInnerHeader + "/ancestor::div[contains(@class, 'inner-card')][1]",
	elementConfigLink = elementInnerHeader + "//a[text()='Configure Element']",
	elementServicesRevenue = elementInnerCard + "//span[contains(text(),'Services Contract Revenue')]/following-sibling::span[@class='card-field-value']/a/span",
	elementServicesCost = elementInnerCard + "//span[contains(text(),'Services Contract Cost')]/following-sibling::span[@class='card-field-value']//span[@data-currency]",
	elementServicesCostOverridden = elementInnerCard + "//span[contains(text(),'Services Contract Cost')]/following-sibling::span[@class='card-field-value']/a/span",
	elementExpensesRevenue = elementInnerCard + "//span[contains(text(),'Expenses Contract Revenue')]/following-sibling::span[@class='card-field-value']/span",
	elementExpensesCost = elementInnerCard + "//span[contains(text(),'Expenses Contract Cost')]/following-sibling::span[@class='card-field-value']/span";
	
	static final By 
	newButton = By.name("new"),
	saveButton = By.id("save"),
	
	accountNameInput = By.cssSelector(SELECT_ID$_ACCOUNT_NAME),
	nameLabel = KBy.label("Short Name"),
	acceptanceDateLabel = KBy.label("Acceptance Date"),

	propositionDropdown = By.cssSelector(SELECT_ID$_PROPOSAL_PROPOSITION_SELECT),
	//riskContingencyLink = By.xpath(RISK_CONTINGENCY_PRODUCT_LINK),
	engagementBurgerMenuLink = By.cssSelector(ENGAGEMENT_BURGER_MENU),
	altProposalSave = By.xpath("//td[contains(@id, 'ProposalAlternativeForm')]//input[@value='Save']"),
	altProposalCancel = By.xpath("//td[contains(@id, 'ProposalAlternativeForm')]//input[@value='Cancel']"),
	altProposalName = By.xpath("//label[contains(text(),'Short Name')]//..//..//td//input"),
	proposalAlternativesLink = By.xpath(PROPOSAL_ALTERNATIVES),
	alternateProposalLink = By.xpath(ALTERNATE_PROPOSAL),
	makePreferred = By.xpath("//div[@title='Make this the Preferred Proposal']"),
	proposalContractRevenue = By.xpath("//label[normalize-space(text())='Contract Revenue']/parent::div/span/span"),
	proposalContractCost = By.xpath("//label[normalize-space(text())='Contract Cost']/parent::div/span/span"),
	proposalContractMargin = By.xpath("//label[normalize-space(text())='Margin Pct']/parent::div/span/span");
	
	static final String 
	PROPOSAL_MENU_DIV = "div[class*='fa-bars'][id$='proposal-menu']",
	CREATE_AN_ALTERNATIVE_PROPOSAL = "Create an Alternative Proposal";
	
	public static void startNewProposalCreation(SeleniumHelper sh) {
		executeSequenceWithRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20,
			/* click new			*/	newButton,
			/* wait for name input	*/	nameLabel
			);
		});
	}
	
	public static void inputValuesAndSave(SeleniumHelper sh, Proposal proposal) {
		By 
		nameInput = sh.getSelectorBasedOnLabel(nameLabel),
		acceptanceDateInput = sh.getSelectorBasedOnLabel(acceptanceDateLabel),
		forecastStatusRadio = sh.getSelectorBasedOnLabel(KBy.label(proposal.forecastStatus));
		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clearAndInputText(sh, accountNameInput, proposal.accountName);
			clearAndInputText(sh, nameInput, proposal.name);
			clearAndInputText(sh, acceptanceDateInput, proposal.acceptanceDate);
			dropdownSelect(sh, propositionDropdown, proposal.proposition);
			radioButtonSelect(sh, forecastStatusRadio, true);
			
			clickAndWaitSequence(sh, 60,
			/* click save			*/	saveButton,
			/* wait for risk contingency sub product meaning engagement has created */	engagementBurgerMenuLink
			);
		});
	}
	
	public static void altProposalCreation(SeleniumHelper sh, Proposal proposal) {
		//Creates Alternate Proposal
		sh.selectBurgerMenuOption(By.cssSelector(PROPOSAL_MENU_DIV), CREATE_AN_ALTERNATIVE_PROPOSAL, altProposalCancel);
		By 
		creationModeRadio = sh.getSelectorBasedOnLabel(KBy.label(proposal.creationMode));
		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			sh.clearField(altProposalName);
			clearAndInputText(sh, altProposalName, proposal.name);
			radioButtonSelect(sh, creationModeRadio, true);
			//creationMode in yaml file must be either Copy Proposal Items or Select Proposition
//			String selectprop="Select Proposition";
			if(proposal.creationMode.equals("Select Proposition")){
				sh.waitForElementToBeClickable(propositionDropdown, 10);
				dropdownSelect(sh, propositionDropdown, proposal.proposition);
			}
			
			clickAndWaitSequence(sh, 80,
			/* click save			*/	altProposalSave,
			/* wait for alternative proposal box meaning alternative proposal has created */	proposalAlternativesLink
			);
		});
	}
	
	/** expects to be in the ProposalItems page */
	public static void altPropToPref(SeleniumHelper sh, String altProposalName) {
		//Switches an alternate proposal to a preferred proposal
		By preferredProposal = By.xpath("//th[@class='header-row-1'][2]//div[@title=\"" + altProposalName + "\"]");
		clickAndWaitSequence(sh, 80,
		/* click link to proposal alternatives page		*/	alternateProposalLink,
		/* wait for a make preferred link which means page has loaded */	makePreferred
		);

		//loops through the web elements to find which column the proposal is on so that can be used to click the correct make preferred link
		int counter;

		List<WebElement>  makePreferredLink = sh.getWebElements(By.xpath("//th[@class='header-row-1']/div"));
		counter = 0;
		for (WebElement Proposal : makePreferredLink)
		{
			counter++;
			// looping until the correct Proposal has been found						
			if (Proposal.getText().contains(altProposalName))
			{
				break;
			}				
		}
		
		counter=counter+1;
		By preferredLink=By.xpath("//th[@class='header-row-2'][" + counter + "]");
		
		clickAndWaitSequence(sh, 80,
		/* click make preferred link for desired proposal	*/	preferredLink,
//		/* wait for OK button in alert */	alertOk
		/* wait for OK button in alert */	sh.alertOkButton
		);
		
		clickAndWaitSequence(sh, 80,
//		/* click OK			*/	alertOk,
		/* click OK			*/	sh.alertOkButton,
		/* wait for proposal to be made preferred */	preferredProposal
		);
		};

	/** expects to be in the ProposalItems page */
	public static void validateProposalItemsSummaryFields(SeleniumHelper sh, Proposal proposal, String stage) {
		sh.refreshBrowser();
		sh.waitForLightningSpinnerToBeHidden();
		sh.waitForPageLoadComplete(20);
		sh.waitForElementToBeClickable(proposalContractRevenue, 120);
		sh.waitForElementToBeClickable(proposalContractCost, 120);
	

		if(proposal.contractRevenue != null) {
			Double contractRevenue = sh.getWebElementNumberOrNullWithRefreshRetry(proposalContractRevenue);
			sh.assertEquals(contractRevenue, proposal.contractRevenue, "The proposal: " + proposal.name + " contract revenue summary field is incorrect");
		}
		if(proposal.contractCost != null) {
			Double contractCost = sh.getWebElementNumberOrNullWithRefreshRetry(proposalContractCost);
			sh.assertEquals(contractCost, proposal.contractCost, "The proposal: " + proposal.name + " contract cost summary field is incorrect");
		}
		if(proposal.contractMargin != null) {
			Double contractMargin = sh.getWebElementNumberOrNullWithRefreshRetry(proposalContractMargin);
			sh.assertEquals(contractMargin, proposal.contractMargin, "The proposal: " + proposal.name + " contract margin summary field is incorrect");
		}
	
		validateEngagementSummaryFields(sh, proposal.deliveryEngagements, stage);
	}
	
	/** expects to be in the ProposalItems page */
	public static void validateEngagementSummaryFields(SeleniumHelper sh, Collection<DeliveryEngagement> engagements, String stage) {
		for(DeliveryEngagement engagement : engagements)
			validateEngagementSummaryFields(sh, engagement, stage);
	}
	
	/** expects to be in the ProposalItems page */
	public static void validateEngagementSummaryFields(SeleniumHelper sh, DeliveryEngagement engagement, String stage) {
		if(!engagement.stage.equals(stage))
			return;

		try {
			sh.waitForElementToBeClickable(By.xpath(ENGAGEMENT_LINK.replace("{{engagement}}", engagement.name)));
		} catch(Exception e) {
			sh.assertTrue(false, "A delivery engagement by the name: " + engagement.name + " doesn't appear in the OCS screen");
		}
		
		By startDateSel = By.xpath(ENGAGEMENT_START_DATE.replace("{{engagement}}", engagement.name));
		By endDateSel = By.xpath(ENGAGEMENT_END_DATE.replace("{{engagement}}", engagement.name));
		By revenueSel = By.xpath(ENGAGEMENT_REVENUE.replace("{{engagement}}", engagement.name));
		
		if(engagement.startDate != null) {
			String startDate = sh.getWebElementTextOrNullWithRetry(startDateSel);
			sh.assertEquals(startDate, engagement.startDate, "The engagements: " + engagement.name + " start date summary field is incorrect");
		}
		if(engagement.endDate != null) {
			String endDate = sh.getWebElementTextOrNullWithRetry(endDateSel);
			sh.assertEquals(endDate, engagement.endDate, "The engagements: " + engagement.name + " end date summary field is incorrect");
		}
		
		/** expected from yaml should include currency and thousands separator */
		if(engagement.revenue != null) {
			String revenue = sh.getWebElementTextOrNullWithRetry(revenueSel);
			sh.assertEquals(revenue, engagement.revenue, "The engagements: " + engagement.name + " revenue summary field is incorrect");
		}
			
		for(DeliveryElement el : engagement.deliveryElements)
			validateElementSummaryFields(sh, engagement.name, el, stage);
	}
	
	/** expects to be in the ProposalItems page */
	public static void validateElementSummaryFields(SeleniumHelper sh, String engagementName, DeliveryElement element, String stage) {
		try {
			sh.waitForElementToBeClickable(By.xpath(elementTitle.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
		} catch(Exception e) {
			sh.assertTrue(false, "A delivery element by the name: " + element.name + " doesn't appear in the OCS screen as a child of an engagement by the name: " + engagementName);
		}
		
		/** for all of the below the expected from yaml should NOT include currency and thousands separator */
		if(element.servicesRevenue != null) {
			Double servicesRevenue = sh.getWebElementNumberOrNullWithRetry(By.xpath(elementServicesRevenue.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
			sh.assertEquals(servicesRevenue, element.servicesRevenue, "The elements: " + element.name + " services revenue summary field is incorrect");
		}
		if(element.servicesCost != null) {
			Double servicesCost = sh.getWebElementNumberOrNullWithRetry(By.xpath(elementServicesCost.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
			if(servicesCost == null)
				servicesCost = sh.getWebElementNumberOrNullWithRetry(By.xpath(elementServicesCostOverridden.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
			sh.assertEquals(servicesCost, element.servicesCost, "The elements: " + element.name + " services cost summary field is incorrect");
		}
		if(element.expensesRevenue != null) {
			Double expensesRevenue = sh.getWebElementNumberOrNullWithRetry(By.xpath(elementExpensesRevenue.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
			sh.assertEquals(expensesRevenue, element.expensesRevenue, "The elements: " + element.name + " expenses revenue summary field is incorrect");
		}
		if(element.expensesCost != null) {
			Double expensesCost = sh.getWebElementNumberOrNullWithRetry(By.xpath(elementExpensesCost.replace("{{engagement}}", engagementName).replace("{{element}}", element.name)));
			sh.assertEquals(expensesCost, element.expensesCost, "The elements: " + element.name + " expenses cost summary field is incorrect");
		}
	}
	
}