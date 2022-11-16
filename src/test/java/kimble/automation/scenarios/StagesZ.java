package kimble.automation.scenarios;

import static kimble.automation.helpers.SequenceActions.*; 

import org.openqa.selenium.By;

import kimble.automation.domain.Account;
import kimble.automation.domain.Invoice;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.helpers.ScenarioFunctions.StageZ;
import kimble.automation.kimbleobjects.classic.GeneralOperations;
import kimble.automation.kimbleobjects.lightning.AccountPageZ;
import kimble.automation.kimbleobjects.lightning.ExpensePageZ;
import kimble.automation.kimbleobjects.lightning.GeneralOperationsZ;
import kimble.automation.kimbleobjects.lightning.JourneysPageZ;
import kimble.automation.kimbleobjects.lightning.ResourcePageZ;
import kimble.automation.kimbleobjects.lightning.SalesOpportunityPageZ;
import kimble.automation.kimbleobjects.lightning.SupplierInvoicePageZ;
import kimble.automation.kimbleobjects.lightning.WhatIfScenariosPageZ;

public class StagesZ {

	public static Stage navigateToSetup(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new StageZ(parentStage, stageName) { 
			public void run() { GeneralOperationsZ.navigateToSetup(sh); }
		};
	}
	
	public static Stage navigateToUsers(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new StageZ(parentStage, stageName) { 
			public void run() { GeneralOperationsZ.navigateToUsers(sh); }
		};
	}
	
	public static Stage waitForLoggedInAsSpan(Stage parentStage, String stageName, SeleniumHelper sh, String userName) {
		return new StageZ(parentStage, stageName) { 
			public void run() { GeneralOperationsZ.waitForLoggedInAsSpan(sh, userName); }
		};
	}

	public static Stage switchToClassicFrame(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new StageZ(parentStage, stageName) { 
			public void run() { sh.switchToClassicIframeContext(); }
		};
	}
	
	public static Stage runAllJobs(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new StageZ(parentStage, testStage, () -> { 
			Stages.runAllJobs(parentStage, testStage, sh);
		});
	}
	
	public static Stage navigateFromAnywhereToTab(Stage parentStage, String stageName, SeleniumHelper sh, String tabName) {
		return new Stage(parentStage, stageName) { 
			public void run() { GeneralOperationsZ.navigateFromAnywhereToTab(sh, tabName); }
		};
	}
	
	public static Stage createAccount(Stage parentStage, String stageName, SeleniumHelper sh, Account account) {
		return new StageZ(parentStage, stageName) { 
			public void run() { AccountPageZ.createNew(sh, account); }
		};
	}
	
	public static Stage startSalesOpportunityCreation(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new StageZ(parentStage, stageName) { 
			public void run() { SalesOpportunityPageZ.startNewSalesOppCreation(sh); }
		};
	}

	public static Stage navigateFromAnywhereToListViewItem(Stage parentStage, String stageName, SeleniumHelper sh, String tabName, String itemName) {
		Stage stage = new StageZ(parentStage, stageName);
		navigateFromAnywhereToTab(stage, "navi to tab: " + tabName, sh, tabName);
		pickListViewItem(stage, "navi to item: " + itemName, sh, itemName);
		waitForLightningToLoadAndSwitchToClassic(stage, "switching to classic", sh);
		return stage;
	}
	
	public static Stage waitForLightningToLoadAndSwitchToClassic(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new StageZ(parentStage, stageName) { 
			public void run() { sh.waitForLightningSpinnerToBeHidden(); }
		};
	}
	
	public static Stage pickListViewItem(Stage parentStage, String stageName, SeleniumHelper sh, String itemName) {
		return new StageZ(parentStage, stageName) { 
			public void run() { GeneralOperationsZ.pickListViewItemZ("navi to item: " + itemName, sh, itemName); }
		};
	}
		
	public static Stage startResourceCreateWizard(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new StageZ(parentStage, stageName) { 
			public void run() { ResourcePageZ.startCreateWiz(sh); }
		};
	}
	
	public static Stage startCreateSupplierInvoice(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new StageZ(parentStage, testStage) { 
			public void run() { SupplierInvoicePageZ.startCreateWiz(sh); }
		};
	}

	/** The invoice reference is populated dynamically while a scenario runs and therefore needs a separate stage for navigation from  a list view.
	 * 	This is because the tree of stages is created before the scenario actually runs.
	 */
	public static Stage navigateToInvoiceFromList(Stage parentStage, String stageName, SeleniumHelper sh, Invoice invoice) {
		return new StageZ(parentStage, stageName) { 
			public void run() { sh.clickLink(By.linkText(invoice.invoiceReference)); }
		};
	}
	
	public static Stage waitForExpensesList(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new StageZ(parentStage, testStage) { 
			public void run() { ExpensePageZ.waitForListView(sh); }
		};
	}

	public static Stage startWhatIfScenarioCreateWizard(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new StageZ(parentStage, testStage) { 
			public void run() { WhatIfScenariosPageZ.startCreateWizard(sh); }
		};
	}
	
	public static Stage acceptWhatIfScenario(Stage parentStage, String testStage, SeleniumHelper sh, String scenarioName) {
		return new StageZ(parentStage, testStage) { 
			public void run() { WhatIfScenariosPageZ.acceptWhatIfScenario(sh, scenarioName); }
		};
		
	}
	
	public static Stage startJourneyCreateWizard(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new StageZ(parentStage, testStage) { 
			public void run() { JourneysPageZ.startCreateWizard(sh); }
		};
	}
	
}
