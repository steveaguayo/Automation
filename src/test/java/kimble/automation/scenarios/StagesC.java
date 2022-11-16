//package kimble.automation.scenarios;
//
//import static kimble.automation.helpers.SequenceActions.*;
//import static kimble.automation.helpers.ScenarioFunctions.*;
//
//import java.text.ParseException;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import kimble.automation.domain.Account;
//import kimble.automation.domain.ActivityAssignment;
//import kimble.automation.domain.Annuity;
//import kimble.automation.domain.BulkEdit;
//import kimble.automation.domain.CreditNote;
//import kimble.automation.domain.DeliveryElement;
//import kimble.automation.domain.DeliveryEngagement;
//import kimble.automation.domain.ExpectedResult;
//import kimble.automation.domain.ExpenseClaim;
//import kimble.automation.domain.ExpenseDetail;
//import kimble.automation.domain.ExpenseEntry;
//import kimble.automation.domain.ExpenseLineItem;
//import kimble.automation.domain.GroupAssignmentTemplate;
//import kimble.automation.domain.Invoice;
//import kimble.automation.domain.Journey;
//import kimble.automation.domain.JourneyAllowanceAdjustment;
//import kimble.automation.domain.JourneyAllowanceAllocation;
//import kimble.automation.domain.JourneyLeg;
//import kimble.automation.domain.KimbleData;
//import kimble.automation.domain.Milestone;
//import kimble.automation.domain.OtherResourcedActivity;
//import kimble.automation.domain.PurchaseOrder;
//import kimble.automation.domain.Resource;
//import kimble.automation.domain.ResourcedActivity;
//import kimble.automation.domain.RevenueItemAdjustment;
//import kimble.automation.domain.Risk;
//import kimble.automation.domain.SalesOpportunity;
//import kimble.automation.domain.SalesOpportunityForecast;
//import kimble.automation.domain.ScheduledActivity;
//import kimble.automation.domain.SupplierInvoice;
//import kimble.automation.domain.SupplierRequisition;
//import kimble.automation.domain.Task;
//import kimble.automation.domain.TimeEntry;
//import kimble.automation.domain.TimeEntryAdjustment;
//import kimble.automation.domain.TimePeriodAction;
//import kimble.automation.domain.Timesheet;
//import kimble.automation.domain.TrackingPlanTotal;
//import kimble.automation.domain.UserCredentials;
//import kimble.automation.domain.WhatIfScenario;
//import kimble.automation.domain.results.RiskDashboardResults;
//import kimble.automation.domain.results.RisksSalesResults;
//import kimble.automation.helpers.General;
//import kimble.automation.helpers.MobileRequests;
//import kimble.automation.helpers.SeleniumHelper;
//import kimble.automation.helpers.ScenarioFunctions.Stage;
//import kimble.automation.kimbleobjects.classic.AccountPageC;
//import kimble.automation.kimbleobjects.classic.ActivityAssignmentBulkEditPageC;
//import kimble.automation.kimbleobjects.classic.ActivityAssignmentPageC;
//import kimble.automation.kimbleobjects.classic.AnnuityPageC;
//import kimble.automation.kimbleobjects.classic.CreditNotePageC;
//import kimble.automation.kimbleobjects.classic.DeliveryElementPageC;
//import kimble.automation.kimbleobjects.classic.DeliveryElementSupplierRequisitionsPageC;
//import kimble.automation.kimbleobjects.classic.DeliveryEngagementPageC;
//import kimble.automation.kimbleobjects.classic.DeliveryGroupActivitiesPageC;
//import kimble.automation.kimbleobjects.classic.DeliveryGroupBudgetsPageC;
//import kimble.automation.kimbleobjects.classic.DeliveryGroupMilestonesPageC;
//import kimble.automation.kimbleobjects.classic.ExpenseForecastingPageC;
//import kimble.automation.kimbleobjects.classic.ExpensePageC;
//import kimble.automation.kimbleobjects.classic.ForecastingPeriodClosePageC;
//import kimble.automation.kimbleobjects.classic.GeneralOperations;
//import kimble.automation.kimbleobjects.classic.GroupAssignmentTemplatePageC;
//import kimble.automation.kimbleobjects.classic.InvoicePageC;
//import kimble.automation.kimbleobjects.classic.JourneysPageC;
//import kimble.automation.kimbleobjects.classic.PagesC;
//import kimble.automation.kimbleobjects.classic.ResourcePageC;
//import kimble.automation.kimbleobjects.classic.RevenueItemAdjustmentPageC;
//import kimble.automation.kimbleobjects.classic.RiskDashboardPageC;
//import kimble.automation.kimbleobjects.classic.RisksPageC;
//import kimble.automation.kimbleobjects.classic.SalesOpportunityPageC;
//import kimble.automation.kimbleobjects.classic.SupplierInvoicePageC;
//import kimble.automation.kimbleobjects.classic.TimesheetPageC;
//import kimble.automation.kimbleobjects.classic.TrackingPeriodClosePageC;
//import kimble.automation.kimbleobjects.classic.TrackingPlanTasksPageC;
//import kimble.automation.kimbleobjects.classic.WhatIfScenariosPageC;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//
//public class StagesC {
//	
//	public static Stage navigateToSetup(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return new StageC(parentStage, stageName, () -> { sh.navigateToSFPage("setup/forcecomHomepage.apexp"); });
//	}
//	
//	public static Stage navigateToUsers(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return new StageC(parentStage, stageName, () -> { Stages.navigateToUsers(sh); });
//	}
//	
//	public static Stage loginByUsername(Stage parentStage, String stageName, SeleniumHelper sh, String userName) {
//		return new StageC(parentStage, stageName, () -> { 
//			Stages.loginByUsername(parentStage, stageName, sh, userName);
//		});
//	}
//	
//	public static Stage waitForLoggedInAsSpan(Stage parentStage, String stageName, SeleniumHelper sh, String userName) {
//		return new StageC(parentStage, stageName, () -> { GeneralOperations.waitForLoggedInAsSpan(sh, userName); });
//	}
//	
//	public static Stage navigateFromProposalToRisksDelivery(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName) {
//		return new StageC(parentStage, stageName, () -> { 
//			sh.clickMenuItem(PagesC.PROPOSALITEMS);
//			sh.clickLink(By.xpath("//a[text()=\"" + engagementName + "\"]"));
//			sh.clickMenuItem(PagesC.RISKSDELIVERY); 
//		});
//	}
//	
//	public static Stage navigateFromAnywhereToAllTabs(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return new StageC(parentStage, stageName, () -> { sh.goToAllTabsPage(); });
//	}
//	
//	public static Stage navigateFromAllTabsToTab(Stage parentStage, String stageName, SeleniumHelper sh, String tabName) {
//		return new StageC(parentStage, stageName, () -> { sh.clickLastLink(tabName); });
//	}
//	
//	public static Stage navigateFromAnywhereToTab(Stage parentStage, String stageName, SeleniumHelper sh, String tabName) {
//		Stage stage = new StageC(parentStage, stageName);
//		navigateFromAnywhereToAllTabs(stage, "navi to all tabs", sh);
//		navigateFromAllTabsToTab(stage, "navi to tab " + tabName, sh, tabName);
//		return stage;
//	}
//	
//	public static Stage clickGoInListView(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return new StageC(parentStage, stageName, () -> { GeneralOperations.clickGoInListView(sh); });
//	}
//	
//	public static Stage navigateFromAllTabsToSalesOpps(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return navigateFromAllTabsToList(parentStage, stageName, sh, "Sales Opportunities");
//	}
//	
//	public static Stage navigateFromAllTabsToList(Stage parentStage, String stageName, SeleniumHelper sh, String listName) {
//		return new StageC(parentStage, stageName, () -> { GeneralOperations.navigateToList(sh, listName); });
//	}
//	
//	/** The invoice reference is populated dynamically while a scenario runs and therefore needs a separate stage for navigation from  a list view.
//	 * 	This is because the tree of stages is created before the scenario actually runs.
//	 */
//	public static Stage navigateToInvoiceFromList(Stage parentStage, String stageName, SeleniumHelper sh, Invoice invoice) {
//		return new StageC(parentStage, stageName, () -> { sh.clickLink(By.linkText(invoice.invoiceReference)); });
//	}
//	
//	public static Stage navigateMenu(Stage parentStage, String stageName, SeleniumHelper sh, String pageName) {
//		return navigateMenu(parentStage, stageName, sh, pageName, false);
//	}
//	
//	public static Stage navigateMenu(Stage parentStage, String stageName, SeleniumHelper sh, String pageName, boolean withoutFilterId) {
//		return new StageC(parentStage, stageName, () -> { sh.clickMenuItem(pageName, withoutFilterId); });
//	}
//	
//	public static Stage assertTrue(Stage parentStage, String stageName, SeleniumHelper sh, boolean isTrue) {
//		return new Stage(parentStage, stageName, () -> { sh.assertTrue(isTrue, stageName); }); 
//	}
//	
//	public static Stage addDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, List<DeliveryEngagement> engagements) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryEngagement en : engagements)
//			addDeliveryElementsInOcs(stage, "add delivery elements for engagement: " + en.name, sh, en);
//		return stage;
//	}
//	
//	public static Stage addDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
//		Stage stage = new StageC(parentStage, stageName);
//		if(engagement.deliveryElements == null)
//			return Stages.doNothing(parentStage, "no elements");
//		for(DeliveryElement el : engagement.deliveryElements)
//			if(el.stage.equals(stage.getRootStageName()))
//				addDeliveryElementInOcs(stage, "add delivery element: " + el.name, sh, engagement.name, el);
//		return stage;
//	}
//	
//	public static Stage addDeliveryElementInOcs(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, DeliveryElement element) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.addElement(sh, engagementName, element); });
//	}
//	
//	/** expects to be in the ProposalItems page */
//	public static Stage addAllOptionalDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, List<DeliveryEngagement> engagements) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryEngagement de : engagements)
//			if(stage.getRootStageName().equals(de.stage))
//				addAllOptionalElements(stage, "add all optional elements to engagement: " + de.name, sh, de.name);
//		return stage;
//	}
//	
//	/** expects to be in the ProposalItems page */
//	public static Stage addAllOptionalElements(Stage parentStage, String stageName, SeleniumHelper sh, String engagement) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.addAllOptionalElements(sh, engagement); });
//	}
//	
//	public static Stage deleteDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, List<DeliveryEngagement> engagements) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryEngagement en : engagements)
//			deleteDeliveryElementsInOcs(stage, stageName, sh, en);
//		return stage;
//	}
//
//	public static Stage deleteDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
//		return deleteDeliveryElementsInOcs(parentStage, stageName, sh, engagement.name, engagement.deliveryElements);
//	}
//
//	public static Stage deleteDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, String engagement, List<DeliveryElement> elements) {
//		if(elements == null)
//			return Stages.doNothing(parentStage, "no elements");
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryElement el : elements) 
//			if(el.stage.equals(stage.getRootStageName()))
//				deleteDeliveryElementInOcs(stage, "deleting element: " + el.name, sh, engagement, el);
//		return stage;
//	}
//	
//	public static Stage deleteDeliveryElementInOcs(Stage parentStage, String stageName, SeleniumHelper sh, String engagement, DeliveryElement element) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.deleteElement(sh, engagement, element); });
//	}
//	
//	/** Expects to be in Proposal Items */
//	public static Stage validateSalesOpportunitySummaryFieldsOcs(Stage parentStage, String stageName, SeleniumHelper sh, Collection<ExpectedResult> expectedResults) {
//		return new StageC(parentStage, stageName, (stage) -> { 
//			for(ExpectedResult res : expectedResults)
//				if(res.testStage.equals(stage.getRootStageName()))
//					SalesOpportunityPageC.validateProposalItemsSummaryFields(sh, res.salesOpportunity, stage.getRootStageName());
//		});
//	}
//
//	/** Expects to be in Proposal Items */
//	public static Stage validateSalesOpportunityInOcs(Stage parentStage, String stageName, SeleniumHelper sh, Collection<ExpectedResult> expectedResults) {
//		Stage stage = new StageC(parentStage, stageName);
//		Stages.sleep(stage, "!For some reason the sales opp values are taking their time to update - therefore sleep a couple of seconds!", sh, 2000);
//		Stages.refreshPage(stage, "refresh page to make sure values have updated in dom", sh);
//		for(ExpectedResult res : expectedResults) {
//			if(res.testStage.equals(stage.getRootStageName())) {
//				validateProposalItemsSummaryFields(stage, "validate proposal items summary fields", sh, res.salesOpportunity);
//				validateEngagementElements(stage, "validate engagement elements", sh, res.salesOpportunity);
//			}
//		}
//		return stage;
//	}
//
//	/** Expects to be in Proposal Items */
//	public static Stage validateProposalItemsSummaryFields(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) {
//		return new StageC(parentStage, stageName, stage -> { SalesOpportunityPageC.validateProposalItemsSummaryFields(sh, salesOpp, stage.getRootStageName());; });
//	}
//
//	/** Expects to be in Proposal Items */
//	public static Stage validateEngagementElements(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) {
//		Stage stage = new StageC(parentStage, stageName);
//		validateEngagementElements(stage, "validate engagements", sh, salesOpp.deliveryEngagements);
//		return stage;
//	}
//
//	/** expects to be in the ProposalItems page */
//	public static Stage validateEngagementElements(Stage parentStage, String stageName, SeleniumHelper sh, List<DeliveryEngagement> expected) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryEngagement e : expected)
//			if(e.stage.equals(stage))
//				validateElements(stage, "validate engagement elements", sh, e.name, e.deliveryElements);
//		return stage;
//	}
//		
//	/** expects to be in the ProposalItems page */
//	public static Stage validateElements(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, List<DeliveryElement> expected) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryElement el : expected)
//			if(el.stage.equals(stage))
//				validateElement(stage, "validate element: " + el.name, sh, engagementName, el);
//		return stage;
//	}
//		
//	/** expects to be in the ProposalItems page */
//	public static Stage validateElement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, DeliveryElement expected) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.validateElement(sh, engagementName, expected); });
//	}
//		
//	/** Expects to be in Proposal Items and have the correctly named Delivery Engagement and Element created*/
//	public static Stage configureDeliveryEngagementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, List<DeliveryEngagement> engagements) {	
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryEngagement e : engagements)
//			if(e.stage == null || stage.getRootStageName().equals(e.stage))
//				configureDeliveryEngagementInOcs(stage, "configure engagemet: " + e.name, sh, e);
//		return stage;
//	}
//
//	/** expects to be in the ProposalItems page */
//	public static Stage configureDeliveryEngagementInOcs(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
//		return configureDeliveryElementsInOcs(parentStage, stageName, sh, engagement.name, engagement.deliveryElements);
//	}
//	
//	public static Stage configureDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, String engagement, Collection<DeliveryElement> elements) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryElement el : elements)
//			if(el.stage == null || el.stage.equals(stage.getRootStageName()))
//				configureDeliveryElementInOcs(stage, "configure element: " + el.name, sh, engagement, el);
//		return stage;
//	}
//		
//	public static Stage configureDeliveryElementInOcs(Stage parentStage, String stageName, SeleniumHelper sh, String engagement, DeliveryElement element) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.configureElementDynamic(sh, engagement, element); });
//	}
//		
//	public static Stage navigateFromAnywhereToSalesOpp(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName) {
//		Stage stage = new StageC(parentStage, stageName);
//		navigateFromAnywhereToAllTabs(stage, "navi to all tabs", sh);
//		navigateFromAllTabsToSalesOpps(stage, "navi to sales opps", sh);
//		Stages.navigateLink(stage, "navi to sales opp: " + salesOppName, sh, salesOppName);
//		return stage;
//	}
//	
//	public static Stage navigateToSalesOppForecastFromHome(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.NavigateToSalesOppForecastFromHome(sh); });
//	}
//	
//	public static Stage navigateToSalesOppHomeFromForecast(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.NavigateToSalesOppHomeFromForecast(sh); });
//	}
//	
//	public static Stage navigateToSalesOppLose(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.NavigateLoseSalesOpp(sh); });
//	}
//	
//	public static Stage navigateToSupplierInvoice(Stage parentStage, String stageName, SeleniumHelper sh, String reference) {
//		return new StageC(parentStage, stageName, () -> { sh.clickLink(By.xpath("//a[normalize-space(text())=\"" + reference + "\"]")); });
//	}
//	
//	/** input the date in the format "dd/MM/yyyy" i.e. "26/01/2013" */
//	public static Stage navigateTotimesheetDayInTnx(Stage parentStage, String stageName, SeleniumHelper sh, String date) {
//		return new StageC(parentStage, stageName, () -> { TimesheetPageC.navigateToDay(sh, date); });
//	}
//	
//	public static Stage createRisks(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Risk> risks) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(Risk r : risks) {
//			if(!r.stage.equals(stage.getRootStageName()))
//				continue;
//			createRisk(stage, "create " + r.category + " risk with impact: " + r.impact + ", probability: " + r.probability + " and severity: " + r.severity, sh, r);
//		}
//		return stage;
//	}
//	
//	public static Stage createRisk(Stage parentStage, String stageName, SeleniumHelper sh, Risk risk) {
//		return new StageC(parentStage, stageName, () -> { RisksPageC.create(sh, risk); });
//	}
//	
//	public static Stage createRisksInDashboard(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Risk> risks) {
//		return new StageC(parentStage, stageName, stage -> { RiskDashboardPageC.createRisks(sh, risks, stage.getRootStageName()); });
//	}
//	
//	public static Stage editRisks(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Risk> risks) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(Risk r : risks)
//			if(r.stage.equals(stage.getRootStageName()))
//				editRisk(stage, "edit " + r.category + " risk", sh, r);
//		return stage;
//	}
//	
//	public static Stage editRisk(Stage parentStage, String stageName, SeleniumHelper sh, Risk risk) {
//		return new StageC(parentStage, stageName, () -> { RisksPageC.edit(sh, risk); });
//	}
//	
//	public static Stage deleteRisks(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Risk> risks) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(Risk r : risks)
//			if(r.stage.equals(stage.getRootStageName()))
//				deleteRisk(stage, "delete risk", sh, r);
//		return stage;
//	}
//	
//	public static Stage deleteRisk(Stage parentStage, String stageName, SeleniumHelper sh, Risk risk) {
//		return new StageC(parentStage, stageName, () -> { RisksPageC.delete(sh, risk); });
//	}
//	
//	public static Stage validateRisks(Stage parentStage, String stageName, SeleniumHelper sh, KimbleData data) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(RisksSalesResults e : data.getExpectedResult(stage.getRootStageName()).risksSales)
//			validateRisk(stage, "validate risk is of level: " + e.riskLevel, sh, e);
//		return stage;
//	}
//	
//	public static Stage validateRisk(Stage parentStage, String stageName, SeleniumHelper sh, RisksSalesResults expected) {
//		return new StageC(parentStage, stageName, () -> { RisksPageC.validate(sh, expected); });
//	}
//	
//	public static Stage validateRiskDashboard(Stage parentStage, String stageName, SeleniumHelper sh, KimbleData data) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(RiskDashboardResults e : data.getExpectedResult(stage.getRootStageName()).riskDashboards)
//			validateRiskDashboard(stage, "validate risk dashboard", sh, e);
//		return stage;
//	}
//	
//	public static Stage validateRiskDashboard(Stage parentStage, String stageName, SeleniumHelper sh, RiskDashboardResults expected) {
//		return new StageC(parentStage, stageName, () -> { RiskDashboardPageC.validate(sh, expected); });
//	}
//	
//	public static Stage validateRisksExist(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Risk> risks, String... creationStageNames) {
//		return new StageC(parentStage, stageName, () -> {
//			for(String sn : creationStageNames)
//				RisksPageC.validateExistence(sh, risks, sn); 
//		});
//	}
//	
//	public static Stage createResource(Stage parentStage, String stageName, SeleniumHelper sh, Resource resource) {
//		Stage stage = new StageC(parentStage, stageName);
//		StagesC.startResourceCreateWizard(stage, "start resource create wizard", sh);
//		StagesC.runResourceCreateWizardStep2(stage, "input resource type and continue wizard", sh, resource.type);
//		StagesC.inputResourceValuesAndSave(stage, "input resource values and finish wizard", sh, resource);
//		return stage;
//	}
//	
//	public static Stage navigateToResources(Stage parentStage, String testStage, SeleniumHelper sh) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateFromAnywhereToAllTabs(stage, "navigate to all tabs", sh);
//		navigateFromAllTabsToList(stage, "navigate to Resources tab", sh, "Resources");
//		StagesC.clickGoInListView(stage, "click go", sh);
//		return stage;
//	}
//	
//	public static Stage navigateToResourceHome(Stage parentStage, String testStage, SeleniumHelper sh, String resourceName) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateToResources(stage, "navigate to resources", sh);
//		Stages.navigateLink(stage, "navigate to resource: " + resourceName, sh, resourceName);
//		return stage;
//	}
//	
//	public static Stage startResourceCreateWizard(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return new StageC(parentStage, stageName, () -> { ResourcePageC.startCreateWiz(sh); });
//	}
//	
//	public static Stage runResourceCreateWizardStep2(Stage parentStage, String stageName, SeleniumHelper sh, String resourceType) {
//		return new StageC(parentStage, stageName, () -> { ResourcePageC.createWizStep2SelectType(sh, resourceType); });
//	}
//	
//	public static Stage inputResourceValuesAndSave(Stage parentStage, String stageName, SeleniumHelper sh, Resource resource) {
//		return new StageC(parentStage, stageName, () -> { ResourcePageC.createWizStep3inputValues(sh, resource); });
//	}
//	
//	public static Stage startResourceEdit(Stage parentStage, String stageName, SeleniumHelper sh, String resource) {
//		return new StageC(parentStage, stageName, () -> { ResourcePageC.startEdit(sh, resource); });
//	}
//	
//	public static Stage startSalesOpportunityCreation(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.startNewSalesOppCreation(sh); });
//	}
//
//	public static Stage inputSalesOppValuesAndSave(Stage parentStage, String stageName, SeleniumHelper sh, String acccountName, SalesOpportunity salesOpp) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.inputValuesAndSave(sh, acccountName, salesOpp); });
//	}
//	
//	public static Stage startAccountCreation(Stage parentStage, String stageName, SeleniumHelper sh, String accountName) {
//		return new StageC(parentStage, stageName, () -> { AccountPageC.startNewAccountCreation(sh, accountName); });
//	}
//	
//	public static Stage inputAccountValuesAndSave(Stage parentStage, String stageName, SeleniumHelper sh, Account account) {
//		return new StageC(parentStage, stageName, () -> { AccountPageC.inputValuesAndSave(sh, account); });
//	}
//	
//	public static Stage changeStartDateOfDeliveryEngagement(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement){
//		Stage stage = new StageC(parentStage, stageName);
//		navigateToAssignmentBulkEditViaCharm(stage, "navigate to activity assignment bulk edit via charm", sh);
//		changeStartDateOfDeliveryEngagement(stage, "change the start date of engagement: " + engagement.name, sh, engagement.name);
//		return stage;
//	}
//	
//	
//	public static Stage navigateToAssignmentBulkEditViaCharm(Stage parentStage, String stageName, SeleniumHelper sh){
//		return new StageC(parentStage, stageName, () -> { 
//			// there should be a proposal level charm which indicates the Detailed level start date is out of step 
//			// with the close date which when clicked launches the bulk edit wizard
//			sh.clickMiniCharmByPageName(PagesC.ACTIVITYASSIGNMENTSBULKEDIT); 
//		});
//	}
//		
//	public static Stage changeStartDateOfDeliveryEngagement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName){
//		return new StageC(parentStage, stageName, () -> { 
//			// we should now be on the bulk edit with a newly defaulted date
//			// where we can push back the start date of the assignments		
//			ActivityAssignmentBulkEditPageC.CompleteWizardWithDefaults(sh, engagementName); 
//		});
//	}
//	
//	
//	public static Stage createChangeElements(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, Collection<DeliveryElement> elements) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryElement element : elements)
//			createChangeElement(stage, "create change element for delivery element: " + element.name, sh, engagementName, element);
//		return stage;
//	}
//	
//	public static Stage createChangeElement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, DeliveryElement element) {
//		return new StageC(parentStage, stageName, () -> { DeliveryElementPageC.createChangeElement(sh, engagementName, element); });
//	}
//	
//	public static Stage navigateAndMoveSalesOpportunityToNextStage(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppStage) {
//		Stage stage = new StageC(parentStage, stageName);
//		navigateToMoveSalesOpportunityToNextStage(stage, "navigate to move sales opp to next stage", sh);
//		moveSalesOpportunityToNextStage(stage, "move sales opp to next stage", sh, salesOppStage);
//		return stage;
//	}
//	
//	public static Stage navigateToMoveSalesOpportunityToNextStage(Stage parentStage, String stageName, SeleniumHelper sh) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.navigateToMoveSalesOpportunityToNextStage(sh); });
//	}
//	
//	public static Stage moveSalesOpportunityToNextStage(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppStage) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.moveSalesOpportunityToNextStage(sh, salesOppStage); });
//	}
//	
//	public static Stage addElementsToExistingEngagement(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
//		Stage stage = new StageC(parentStage, stageName);
//		startAddingElementsToExistingEngagement(stage, "start adding new elements to the existing engagement: " + engagement.name, sh, engagement.name);
//		addTheElementsToTheExistingEngagement(stage, "add the elements in the wizard", sh, engagement.deliveryElements);
//		Stages.sleep(stage, "!This shouldn't be necessary! - Wait for jobs to get seeded", sh, 4000);
//		finishAddingElementsToExistingEngagement(stage, "finish adding new elements to the existing engagement: " + engagement.name, sh, engagement.name);
//		navigateToEngagementFromProposalItems(stage, "navigate to the engagement: " + engagement.name, sh, engagement.name);
//		configureElements(stage, "configure the elements", sh, engagement.deliveryElements);
//		return stage;
//	}
//	
//	public static Stage startAddingElementsToExistingEngagement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.startAddingElementsToExistingEngagement(sh, engagementName); });
//	}
//	
//	public static Stage addTheElementsToTheExistingEngagement(Stage parentStage, String stageName, SeleniumHelper sh, Collection<DeliveryElement> elements) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryElement e : elements) 
//			addAnElementToTheExistingEngagement(stage, "add the element: " + e.name, sh, e);
//		return stage;
//	}
//	
//	public static Stage addAnElementToTheExistingEngagement(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.addElementInWizard(sh, element); });
//	}
//	
//	public static Stage finishAddingElementsToExistingEngagement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName) {
//		return new StageC(parentStage, stageName, () -> { SalesOpportunityPageC.finishAddingElementsToExistingEngagement(sh, engagementName); });
//	}
//	
//	public static Stage configureElements(Stage parentStage, String stageName, SeleniumHelper sh, Collection<DeliveryElement> elements) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryElement e : elements)
//			configureElement(stage, "configure element: " + e.name, sh, e);
//		return stage;
//	}
//	
//	public static Stage configureElement(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, stageName);
//		createAssignments(stage, "create assignments", sh, element);
//		createAnnuities(stage, "create annuities", sh, element.annuities);
//		createRevenueMilestones(stage, "create revenue milestones", sh, element);
//		createCostMilestones(stage, "create cost milestones", sh, element);
//		return stage;
//	}
//	
//	public static Stage createTasksAndTaskAssignments(Stage parentStage, String stageName, SeleniumHelper sh, Collection<DeliveryElement> elements) throws ParseException {
//		Stage stage = new StageC(parentStage, stageName);
//		for(DeliveryElement e : elements)
//			createTasksAndTaskAssignments(stage, "create tasks and task assignments for element " + e.name, sh, e);
//		return stage;
//	}
//	
//	public static Stage createTasksAndTaskAssignments(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element) throws ParseException {
//		Stage stage = new StageC(parentStage, stageName);
//		navigateMenu(stage, "navigate to tracking plan gant", sh, PagesC.TRACKINGPLANGANTT);
//		createTasksAndTaskAssignments(stage, "create tasks and task assignments", sh, element.tasks);
//		return stage;
//	}
//	
//	public static Stage createTasksAndTaskAssignments(Stage parentStage, String stageName, SeleniumHelper sh, List<Task> tasks) {
//		Stage stage = new StageC(parentStage, stageName);
//		for(Task t : tasks) {
//			if(t.testStage == null || t.testStage.equals(stage.getRootStageName())) {
//				createTask(stage, "create task: " + t.name, sh, t);
//				if(t.taskAssignments != null)
//					createTaskAssignments(stage, "create task assignments for task: " + t.name, sh, t);
//			}
//		}
//		return stage;
//	}
//	
//	public static Stage createTask(Stage parentStage, String stageName, SeleniumHelper sh, Task task) {
//		return new StageC(parentStage, stageName, () -> { TrackingPlanTasksPageC.CreateNew(sh, task); });
//	}
//
//	public static Stage createTaskAssignments(Stage parentStage, String stageName, SeleniumHelper sh, Task task) {
//		return new StageC(parentStage, stageName, () -> { TrackingPlanTasksPageC.AssignTask(sh, task, task.taskAssignments); });
//	}
//	
//	public static Stage updateTaskEstimates(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
//		Stage stage = new StageC(parentStage, stageName);
//		for (DeliveryElement e : engagement.deliveryElements) {
//			navigateMenu(stage, "navigate to tracking plan gant", sh, PagesC.TRACKINGPLANGANTT);
//			updateTaskEstimates(stage, "update task estimates for element: " + e.name, sh, e);
//		}
//		return stage;
//	}
//	
//	public static Stage updateTaskEstimates(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, stageName);
//		for (Task task : element.tasks)
//			if(task.testStage != null && task.testStage.equals(stage.getRootStageName()) && task.taskAssignments != null)
//				updateTaskEstimates(stage, "update estimates for task: " + task.name, sh, task);
//		return stage;
//	}
//	
//	public static Stage updateTaskEstimates(Stage parentStage, String stageName, SeleniumHelper sh, Task task) {
//		return new StageC(parentStage, stageName, () -> { TrackingPlanTasksPageC.UpdateTaskEstimates(sh, task, task.taskAssignments); });
//	}
//	
//	public static Stage closeTrackingPlan(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement, boolean expectToClose) {
//		Stage stage = new StageC(parentStage, stageName);
//		for (DeliveryElement e : engagement.deliveryElements) {
//			navigateMenu(stage, "navigate to plan dashboard", sh, PagesC.TRACKINGPLANTASKS);
//			closeTrackingPlan(stage, "close tracking plan for element: " + e.name, sh, e, expectToClose);
//		}
//		return stage;
//	}
//	
//	public static Stage closeTrackingPlan(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element, boolean expectToClose) {
//		return new StageC(parentStage, stageName, () -> { 
//			if(expectToClose)
//				TrackingPlanTasksPageC.CloseTrackingPlan(sh);	
//			else
//				TrackingPlanTasksPageC.AttemptToCloseTrackingPlan(sh); 
//		});
//	}
//	
//	public static Stage editResourcedActivity(Stage parentStage, String stageName, SeleniumHelper sh, ResourcedActivity activity) {
//		return new StageC(parentStage, stageName, () -> { DeliveryGroupActivitiesPageC.editActivity(sh, activity); });
//	}
//
//	public static Stage updateProduct(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
//		Stage stage = new StageC(parentStage, stageName);
//		navigateMenu(stage, "navigate to revenue and costs", sh, PagesC.DELIVERYGROUPREVENUESANDCOSTS);
//		for(DeliveryElement e : engagement.deliveryElements)
//			updateProduct(stage, "update product and name for element: " + e.name, sh, engagement.name, e);
//		return stage;
//	}
//		
//	public static Stage updateProduct(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, DeliveryElement element) {
//		return new StageC(parentStage, stageName, () -> { 
//			DeliveryElementPageC.EditExisting(sh, engagementName, element.name);
//			// switch the delivery element product and name (edit the underlying KimbleTestData object to replace the name
//			// so that subsequent checks for the delivery element name pick up the new name
//			element.name = element.step2Name;
//			
//			DeliveryElementPageC.UpdateProductAndName(sh, element.step2Product, element.step2Name); 
//		});
//	}
//		
//	public static Stage confirmWonProductsAndServices(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement) {
//		return new StageC(parentStage, testStage, () -> { DeliveryEngagementPageC.ConfirmWonProductsAndServices(sh, engagement); });
//	}
//	
//	public static Stage winOpportunity(Stage parentStage, String testStage, SeleniumHelper sh, String winDate) {
//		return new StageC(parentStage, testStage, () -> { SalesOpportunityPageC.winOpportunity(sh, winDate); });
//	}
//	
//	public static Stage loseOpportunity(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
//		return new StageC(parentStage, testStage, () -> { SalesOpportunityPageC.loseSalesOpportunity(sh, salesOpp); });
//	}
//
//	public static Stage createRevenueMilestones(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, testStage);
//		if(!General.hasMilestonesToCreate(element.revenueMilestones, stage.getRootStageName()))
//			return Stages.doNothing(stage, "no revenue milestones");
//		navigateToDeliveryGroupMilestones(stage, "navigate to milestones", sh);
//		for(Milestone m : element.revenueMilestones)
//			if(m.creationStage == null || m.creationStage.equals(stage.getRootStageName()))
//				createRevenueMilestone(stage, "create revenue milestone: " + m.name, sh, element.parentElementName, element.name, m);
//		return stage;
//	}
//	
//	public static Stage createCostMilestones(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, testStage);
//		if(!General.hasMilestonesToCreate(element.costMilestones, stage.getRootStageName()))
//			return Stages.doNothing(stage, "no cost milestones");
//		navigateToDeliveryGroupMilestones(stage, "navigate to milestones", sh);
//		for(Milestone m : element.costMilestones)
//			if(m.creationStage == null || m.creationStage.equals(stage.getRootStageName()))
//				createCostMilestone(stage, "create cost milestone: " + m.name, sh, element.name, m);
//		return stage;
//	}
//	
//	public static Stage createRevenueMilestone(Stage parentStage, String testStage, SeleniumHelper sh, String parentElementName, String elementName, Milestone milestone) {
//		return new StageC(parentStage, testStage, () -> { DeliveryGroupMilestonesPageC.CreateNewRevenueMilestone(sh, parentElementName, elementName, milestone); });
//	}
//	
//	public static Stage createCostMilestone(Stage parentStage, String testStage, SeleniumHelper sh, String elementName, Milestone milestone) {
//		return new StageC(parentStage, testStage, () -> { DeliveryGroupMilestonesPageC.CreateNewCostMilestone(sh, elementName, milestone); });
//	}
//	
//	public static Stage createAnnuities(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Annuity> annuities) {
//		Stage stage = new StageC(parentStage, testStage);
//		if(annuities == null)
//			return Stages.doNothing(stage, "no annuities");
//		navigateMenu(stage, "navigate to annuities", sh, PagesC.DELIVERYGROUPANNUITIES);
//		for(Annuity a : annuities)
//			createAnnuity(stage, "create annuity: " + a.name, sh, a);
//		return stage;
//	}
//	
//	public static Stage createAnnuity(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity) {
//		return new StageC(parentStage, testStage, () -> { AnnuityPageC.CreateNew(sh, annuity); });
//	}
//
//	public static Stage completeAnnuityPeriods(Stage parentStage, String testStage, SeleniumHelper sh, List<DeliveryElement> elements) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(DeliveryElement e : elements)
//			completeAnnuityPeriods(stage, "complete annuity periods for element: " + e.name, sh, e.annuities);
//		return stage;
//	}
//	
//	public static Stage completeAnnuityPeriods(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Annuity> annuities) {
//		Stage stage = new StageC(parentStage, testStage);
//		if(annuities == null)
//			return Stages.doNothing(stage, "no annuities");
//		navigateMenu(stage, "navigate to annuities", sh, PagesC.DELIVERYGROUPANNUITIES);
//		for(Annuity a : annuities) {
//			getAnnuityId(stage, "get the id for annuity: " + a.name, sh, a);
//			completeAnnuityPeriods(stage, "complete periods for annuity: " + a.name, sh, a);
//		}
//		return stage;
//	}
//	
//	public static Stage getAnnuityId(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity) {
//		return new StageC(parentStage, testStage, () -> { annuity.id = AnnuityPageC.getAnnuityId(sh, annuity.name); });
//	}
//	
//	public static Stage completeAnnuityPeriods(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity) {
//		if(annuity.periodsToComplete == null)
//			return Stages.doNothing(parentStage, "no periods to complete");
//		Stage stage = new StageC(parentStage, testStage);
//		for(int i : annuity.periodsToComplete) {
//			completeRevenueAnnuityPeriod(stage, "complete period: " + i + " for revenue annuity: " + annuity.name, sh, annuity, i);
//			completeCostAnnuityPeriod(stage, "complete period: " + i + " for cost annuity: " + annuity.name, sh, annuity, i);
//		}
//		return stage;
//	}
//	
//	public static Stage completeRevenueAnnuityPeriod(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity, int periodIndex) {
//		return new StageC(parentStage, testStage, () -> { AnnuityPageC.completeRevenueAnnuityPeriodById(sh, annuity.id, periodIndex); });
//	}
//	
//	public static Stage completeCostAnnuityPeriod(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity, int periodIndex) {
//		return new StageC(parentStage, testStage, () -> { AnnuityPageC.completeCostAnnuityPeriodById(sh, annuity.id, periodIndex); });
//	}
//	
//	public static Stage navigateToGroupAssignmentByName(Stage parentStage, String testStage, SeleniumHelper sh, String activityName) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateFromAnywhereToTab(stage, "navigate to group assignment list", sh, "Other Activities");
//		Stages.navigateLink(stage, "navigate to group assignment from list", sh, activityName);
//		return stage;
//	}
//	
//	public static Stage createAndActivateGroupAssignmentTemplate(Stage parentStage, String testStage, SeleniumHelper sh, GroupAssignmentTemplate assignment) {
//		Stage stage = new StageC(parentStage, testStage);
//		removeExistingGroupAssignment(stage, "remove existing group assignment", sh, assignment);
//		createNewGroupAssignment(stage, "create group assignment", sh, assignment);
//		activateGroupAssignment(stage, "activate group assignment", sh, assignment);
//		return stage;
//	}
//	
//	public static Stage removeExistingGroupAssignment(Stage parentStage, String testStage, SeleniumHelper sh, GroupAssignmentTemplate template) {
//		return new StageC(parentStage, testStage, () -> { 
//			if(!GroupAssignmentTemplatePageC.validateExistingAndActive(sh, template.templateName)) {
//				GroupAssignmentTemplatePageC.deactivateExisting(sh, template.templateName);
//				GroupAssignmentTemplatePageC.deleteExisting(sh, template.templateName);
//			}
//		});
//	}
//	
//	public static Stage createNewGroupAssignment(Stage parentStage, String testStage, SeleniumHelper sh, GroupAssignmentTemplate template) {
//		return new StageC(parentStage, testStage, () -> { 
//			if(!GroupAssignmentTemplatePageC.validateExistingAndActive(sh, template.templateName))
//				GroupAssignmentTemplatePageC.CreateNew(sh, template); 
//		});
//	}
//	
//	public static Stage activateGroupAssignment(Stage parentStage, String testStage, SeleniumHelper sh, GroupAssignmentTemplate template) {
//		return new StageC(parentStage, testStage, () -> { 
//			if(!GroupAssignmentTemplatePageC.validateExistingAndActive(sh, template.templateName))
//				GroupAssignmentTemplatePageC.viewAndActivate(sh, template.templateName); 
//		});
//	}
//	
//	public static Stage createAssignments(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, testStage);
//		if(element.activityAssignments == null)
//			return Stages.doNothing(stage, "no assignments");
//		navigateToAssignments(stage, "navigate to assignments", sh);
//		for(ActivityAssignment aa : element.activityAssignments)
//			createAssignment(stage, "create assignment for resource: " + aa.resourceName, sh, element, aa);
//		return stage;
//	}
//	
//	public static Stage createAssignment(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element, ActivityAssignment assignment) {
//		// default to creating the assignment against the element
//		String activityName = element.name;
//		// but if this is a CR then we want to create the assignment against the parent only if operated with primary
//		if(element.operatedWithPrimary) activityName = element.parentElementName;
//		return createAssignment(parentStage, testStage, sh, activityName, assignment);
//	}
//	
//	public static Stage createAssignment(Stage parentStage, String testStage, SeleniumHelper sh, String activityName, ActivityAssignment assignment) {
//		return new StageC(parentStage, testStage, () -> { ActivityAssignmentPageC.CreateNew(activityName, assignment); });
//	}
//	
//	public static Stage createAssignment(Stage parentStage, String testStage, SeleniumHelper sh, OtherResourcedActivity activity, ActivityAssignment assignment) {
//		return new StageC(parentStage, testStage, () -> { 
//			if(ActivityAssignmentPageC.doesAssignmentExist(sh, assignment.resourceName, assignment.startDate))
//				return;
//			ActivityAssignmentPageC.CreateNew(sh, activity.name, assignment);
//			ActivityAssignmentPageC.VerifyAssignmentExists(sh, assignment.resourceName, assignment.startDate); 
//		});
//	}
//	
//	public static Stage createBidTeamAssignmentsStage(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
//		Stage stage = new StageC(parentStage, testStage);
//		if(salesOpp.bidTeamActivityAssignments == null)
//			return Stages.doNothing(stage, "no bid team assignments");
//		navigateMenu(stage, "navigate to bid team assignments", sh, PagesC.BIDTEAMASSIGNMENTS);
//		for(ActivityAssignment aa : salesOpp.bidTeamActivityAssignments)
//			createAssignment(stage, "create assignment for resource: " + aa.resourceName, sh, salesOpp.name, aa);
//		return stage;
//	}
//	
//	public static Stage navigateToDeliveryGroupMilestones(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { sh.clickMenuItem(PagesC.DELIVERYGROUPMILESTONES);
//				sh.waitForElementToBeClickable(By.cssSelector("#element-menu")); });
//	}
//	
//	public static Stage navigateToAssignments(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { DeliveryEngagementPageC.NavigateToAssignments(sh, true); });
//	}
//	
//	public static Stage navigateToEngagementDashboardByName(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateFromAnywhereToTab(stage, "navigate to delivery engagements", sh, "Delivery Engagements");
//		clickGoInListView(stage, "click go", sh);
//		Stages.navigateLink(stage, "navigate to engagement", sh, engagementName);
//		return stage;
//	}
//	
//	public static Stage navigateToEngagementFromProposalItems(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName) {
//		return new StageC(parentStage, testStage, () -> { 
//			sh.clickLink(By.xpath(SalesOpportunityPageC.engagementLink.replace("{{engagement}}", engagementName)));
//			sh.waitForMenuItemToBeClickable(PagesC.DELIVERYGROUPDASHBOARD);
//		});
//	}
//	
//	public static Stage navigateToResourceForecastFromHome(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { ResourcePageC.NavigateToResourceForecastFromHome(sh); });
//	}
//	
//	public static Stage navigateToResourceHomeFromForecast(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { ResourcePageC.NavigateToResourceHomeFromForecast(sh); });
//	}
//	
//	public static Stage navigateToTimesheet(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { ResourcePageC.NavigateToTimesheetView(sh); });
//	}
//	
//	public static Stage activateElements(Stage parentStage, String testStage, SeleniumHelper sh, Collection<DeliveryElement> elements) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(DeliveryElement de : elements)
//			if(de.activate)
//				activateElement(stage, "activate element: " +de.name, sh, de.name);
//		return stage;
//	}
//	
//	public static Stage activateElement(Stage parentStage, String testStage, SeleniumHelper sh, String elementName) {
//		return new StageC(parentStage, testStage, () -> { DeliveryEngagementPageC.ActivateDeliveryElement(sh, elementName); });
//	}
//	
//	public static Stage scheduleActivities(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateToAssignments(stage, "navigate to assignments", sh);
//		scheduleActivitiesOnAssignements(stage, "schedule activities on assignments", sh, element.activityAssignments);
//		return stage;
//	}
//	
//	public static Stage scheduleActivitiesOnAssignements(Stage parentStage, String testStage, SeleniumHelper sh, List<ActivityAssignment> assignments) {
//		Stage stage = new StageC(parentStage, testStage);
//		for (ActivityAssignment a : assignments)
//			if(a.scheduledActivity != null)
//				scheduleActivities(stage, "schedule activity assignment for resource: " + a.resourceName, sh, a.scheduledActivity);
//		return stage;
//	}
//	
//	public static Stage scheduleActivities(Stage parentStage, String testStage, SeleniumHelper sh, List<ScheduledActivity> activities) {
//		return new StageC(parentStage, testStage, () -> { try {
//				ActivityAssignmentPageC.ScheduleActivity(sh, activities);
//			} catch(Exception e) {
//				throw new RuntimeException("Failed to schedule activities", e);
//			}
//		});
//	}
//	
//	public static Stage assignCandidates(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateToAssignments(stage, "navigate to assignments", sh);
//		for (ActivityAssignment assignment : element.activityAssignments)
//			assignCandidates(stage, "assign candidates on assignment for resource: " + assignment.resourceName, sh, assignment);
//		return stage;
//	}
//	
//	public static Stage assignCandidates(Stage parentStage, String testStage, SeleniumHelper sh, ActivityAssignment assignment) {
//		return new StageC(parentStage, testStage, () -> { 
//			if(assignment.candidateResources != null)
//				ActivityAssignmentPageC.AssignCandidates(sh, assignment); 
//		});
//	}
//	
//	public static Stage reviewCandidates(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateToAssignments(stage, "navigate to assignments", sh);
//		for (ActivityAssignment assignment : element.activityAssignments)
//			reviewCandidates(stage, "review candidates on assignment for resource: " + assignment.resourceName, sh, assignment);
//		return stage;
//	}
//	
//	public static Stage reviewCandidates(Stage parentStage, String testStage, SeleniumHelper sh, ActivityAssignment assignment) {
//		return new StageC(parentStage, testStage, () -> { 
//			if(assignment.candidateResources != null)
//				ActivityAssignmentPageC.ReviewCandidates(sh, assignment); 
//		});
//	}
//	
//	public static Stage profileMonthlyAssignments(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateToAssignments(stage, "navigate to assignments", sh);
//		for (int i = 0; i < element.activityAssignments.size(); i++) {
//			ActivityAssignment assignment = element.activityAssignments.get(i);
//			profileMonthlyAssignments(stage, "profile monthly assignment for resource: " + assignment.resourceName, sh, assignment, i == 0);
//		}
//		return stage;
//	}
//	
//	public static Stage profileMonthlyAssignments(Stage parentStage, String testStage, SeleniumHelper sh, ActivityAssignment assignment, boolean initialise) {
//		return new StageC(parentStage, testStage, () -> { 
//			if(assignment.monthlyProfiles != null) {
//				try {
//					if(initialise)
//						ActivityAssignmentPageC.InitialiseMonthlyView(sh, assignment.startDate);
//				} catch (ParseException e) { throw new RuntimeException("failed to initialize monthly view on assignment for resource: " + assignment.resourceName, e); }
//				ActivityAssignmentPageC.ProfileAssignments(sh, assignment, parentStage.getRootStageName());
//			}
//		});
//	}
//	
//	public static Stage editElementExpenseForecast(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseLineItem eli : element.expenseLineItems)
//			editExpenseLineItem(stage, "edit expense line item with category: " + eli.category + " and business unit: " + eli.businessUnit, sh, eli);
//		return stage;
//	}
//	
//	public static Stage editExpenseLineItem(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseLineItem lineItem) {
//		return new StageC(parentStage, testStage, () -> { ExpenseForecastingPageC.editExpenseLineItem(lineItem, sh); });
//	}
//	
//	public static Stage validateNoActivitiesAvailableForPeriod(Stage parentStage, String testStage, SeleniumHelper sh, List<TimeEntry> entries) {
//		return new StageC(parentStage, testStage, () -> { TimesheetPageC.validateNoActivitiesAvailableForPeriod(sh, entries); });
//	}
//	
//	public static Stage switchToDetailedForecastLevelStage(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { SalesOpportunityPageC.SwitchToDetailedForecastLevel(sh); });
//	}
//	
//	public static Stage enableWorkingAtRisk(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { DeliveryEngagementPageC.EnableWorkingAtRisk(sh); });
//	}
//	
//	public static Stage verifyWorkingAtRisk(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { DeliveryEngagementPageC.verifyWorkingAtRisk(sh); });
//	}
//	
//	public static Stage applyRateReduction(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(DeliveryElement e : engagement.deliveryElements)
//			if(e.bulkEdits != null)
//				applyRateReduction(stage, "apply rate reduction to element: " + e, sh, engagement.name, e);
//		return stage;
//	}
//	
//	public static Stage applyRateReduction(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(BulkEdit be : element.bulkEdits)
//			applyRateReduction(stage, "apply rate reduction operation: " + be.operation, sh, engagementName, be);
//		return stage;
//	}
//	
//	public static Stage applyRateReduction(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName, BulkEdit bulkEdit) {
//		return new StageC(parentStage, testStage, () -> { ActivityAssignmentBulkEditPageC.ChangeRevenueRate(sh, bulkEdit.operation, bulkEdit.value, engagementName); });
//	}
//	
//	public static Stage validateSalesOppForecast(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunityForecast expected) {
//		return new StageC(parentStage, testStage, stage -> { 
//			try {
//				SalesOpportunityPageC.checkExpectedResultsForSalesOpportunity(sh, expected, stage.getRootStageName());
//			} catch(Exception e) {
//				throw new RuntimeException("Failed to validate sales opp forecast", e);
//			}
//		});
//	}
//
//	public static Stage validatePlanTotals(Stage parentStage, String testStage, SeleniumHelper sh, TrackingPlanTotal total) { 
//		return new StageC(parentStage, testStage, stage -> { GeneralOperations.validateTrackingPlanTotals(sh, stage.getRootStageName(), total); });
//	}
//	
//	public static Stage enterTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet timesheet) {
//		return new StageC(parentStage, testStage, () -> { 
//			try {
//				TimesheetPageC.enterTime(sh, timesheet);
//			} catch(Exception e) {
//				throw new RuntimeException("Failed to enter timesheet for resource: " + timesheet.resourceName, e);
//			}
//		});
//	}
//	
//	public static Stage adjustTimeEntries(Stage parentStage, String testStage, SeleniumHelper sh, String resource, Date trackingPeriodStart, List<TimeEntryAdjustment> adjustmentBatch) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateTotimesheetDayInTnx(stage, "navigate to date", sh, MobileRequests.automationDateFormatter.format(trackingPeriodStart));
//		switchToAdjustTimeEntries(stage, "navigate to adjust time entries", sh);
//		adjustTimeEntries(stage, "adjust time entries", sh, adjustmentBatch);
//		saveAndSubmitTimeAdjustments(stage, "save and submit time adjustments", sh);
//		return stage;
//	}
//	
//	public static Stage switchToAdjustTimeEntries(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { TimesheetPageC.navigateToAdjustTimeEntries(sh); });
//	}
//
//	public static Stage adjustTimeEntries(Stage parentStage, String testStage, SeleniumHelper sh, List<TimeEntryAdjustment> adjustmentBatch) {
//		return new StageC(parentStage, testStage, () -> { TimesheetPageC.adjustTimeEntries(sh, adjustmentBatch); });
//	}
//
//	public static Stage saveAndSubmitTimeAdjustments(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { TimesheetPageC.saveAndSubmitAdjustments(sh); });
//	}
//
//	public static Stage navigateToCreateExpenseClaimFromTimesheet(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { ResourcePageC.getExpenseEntryLinkForResource(sh).click(); });
//	}
//
//	public static Stage enterExpenseEntry(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseClaim claim, ExpenseEntry entry, boolean submit) {
//		return new StageC(parentStage, testStage, () -> { ExpensePageC.CreateNew(sh, entry, submit); });
//	}
//
//	public static Stage waitForExpensesList(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { ExpensePageC.waitForListView(sh); });	
//	}
//
//	public static Stage saveAndSubmitExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { ExpensePageC.saveAndSubmit(sh); });
//	}
//	
//	public static Stage completeMilestonesStage(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryElement element) {
//		Stage stage = new StageC(parentStage, testStage);
//		if(element.revenueMilestones != null)
//			for(Milestone m : element.revenueMilestones)
//				if(m.completionStage == null || stage.getRootStageName().equals(m.completionStage))
//					completeMilestoneStage(stage, "Complete milestone: " + m.name, sh, element, m);
//		if(element.costMilestones != null)
//			for(Milestone m : element.costMilestones)
//				if(m.completionStage == null || stage.getRootStageName().equals(m.completionStage))
//					completeMilestoneStage(stage, "Complete milestone: " + m.name, sh, element, m);
//		return stage;
//	}
//	
//	public static Stage completeMilestoneStage(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element, Milestone milestone) {
//		return new StageC(parentStage, testStage, () -> { DeliveryGroupMilestonesPageC.CompleteMilestone(sh, element.name, milestone.name); });
//	}
//	
//	public static Stage closeTrackingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
//		return new StageC(parentStage, testStage, () -> { 
//			try {
//				executeSequenceWithRefreshRetry(sh, 2, () -> {
//					if(action.removeUsage)
//						TrackingPeriodClosePageC.RemoveUsage(sh, action.businessUnitGroup, action.periodName);
//	
//					TrackingPeriodClosePageC.CloseTrackingPeriodWhereForecastComplete(sh, action.businessUnitGroup, action.periodName);
//				});
//			} catch(Exception e) {
//				if(action.force)
//					throw new RuntimeException("Failed to close the tracking period", e);
//			}
//		});
//	}
//	
//	public static Stage navigateToTrackingPeriodForGroup(Stage parentStage, String testStage, SeleniumHelper sh, String businessUnitGroup) {
//		return new StageC(parentStage, testStage, () -> { TrackingPeriodClosePageC.navigateToTrackingPeriodForGroup(sh, businessUnitGroup); });
//	}
//
//	public static Stage navigateToTrackingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
//		return new StageC(parentStage, testStage, () -> { 
//			try {
//				if(!action.force)
//					TrackingPeriodClosePageC.validateTrackingPeriodExists(sh, action.businessUnitGroup, action.periodName, 2);
//				TrackingPeriodClosePageC.navigateToTrackingPeriod(sh, action.businessUnitGroup, action.periodName);
//			} catch(Exception e) {
//				if(action.force)
//					throw new RuntimeException("Failed to close the tracking period", e);
//			}
//		});
//	}
//
//	public static Stage navigateToTrackingPeriodForDate(Stage parentStage, String testStage, SeleniumHelper sh, String periodName) {
//		Date trackingPeriodDate = General.getDateForTrackingPeriod(periodName);
//		return new StageC(parentStage, testStage, () -> { TrackingPeriodClosePageC.navigateToTrackingPeriodForDate(sh, trackingPeriodDate); });
//	}
//
//	public static Stage openTrackingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
//		return new StageC(parentStage, testStage, () -> { 
//			try {
//				TrackingPeriodClosePageC.openTrackingPeriod(sh, action.businessUnitGroup, action.periodName);
//			} catch(Exception e) {
//				if(action.force)
//					throw new RuntimeException("Failed to re-open the tracking period: " + action.periodName, e);
//			}
//		});
//	}
//
//	public static Stage closeForecastingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
//		return new StageC(parentStage, testStage, () -> { 
//			try {
//				try {
//					ForecastingPeriodClosePageC.CloseForecastingPeriodWhereForecastComplete(sh);
//				} catch(Exception e) {
//					GeneralOperations.runAllJobs(sh);
//					ForecastingPeriodClosePageC.CloseForecastingPeriodWhereForecastComplete(sh);
//				}
//			} catch(Exception e) {
//				if(action.force)
//					throw new RuntimeException("Failed to close the forecasting period", e);
//			}
//		});
//	}
//
//	public static Stage navigateToForecastingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
//		return new StageC(parentStage, testStage, () -> { 
//			try {
//				if(!action.force)
//					ForecastingPeriodClosePageC.validateForecastingPeriodExists(sh, action.businessUnitGroup, action.periodName, 2);
//				ForecastingPeriodClosePageC.navigateToForecastingPeriod(sh, action.businessUnitGroup, action.periodName);
//			} catch(Exception e) {
//				if(action.force)
//					throw new RuntimeException("Failed to close the forecasting period", e);
//			}
//		});
//	}
//
//	public static Stage navigateToForecastingPeriodForGroup(Stage parentStage, String testStage, SeleniumHelper sh, String businessUnitGroup) {
//		return new StageC(parentStage, testStage, () -> { ForecastingPeriodClosePageC.navigateToForecastingPeriodByGroup(sh, businessUnitGroup); });
//	}
//
//	public static Stage navigateToForecastingPeriodDate(Stage parentStage, String testStage, SeleniumHelper sh, String periodName) {
//		Date forecastingPeriodDate = General.getDateForForecastingPeriod(periodName);
//		return new StageC(parentStage, testStage, () -> { ForecastingPeriodClosePageC.navigateToForecastingPeriodForDate(sh, forecastingPeriodDate); });
//	}
//
//	public static Stage openForecastingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
//		return new StageC(parentStage, testStage, () -> { ForecastingPeriodClosePageC.openForecastingPeriod(sh, action.force); });
//	}
//
//	public static Stage updateRemainingUsage(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element, int stageNo) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateToAssignments(stage, "navigate to assignments", sh);
//		for (ActivityAssignment a : element.activityAssignments)
//			updateRemainingUsage(stage, "update remaining usage (stage 3) for assignment for resource: " + a.resourceName, sh, a, stageNo);
//		return stage;
//	}
//	
//	public static Stage updateRemainingUsage(Stage parentStage, String testStage, SeleniumHelper sh, ActivityAssignment assignment, int stageNo) {
//		return new StageC(parentStage, testStage, () -> { 
//			if(stageNo == 1)
//				ActivityAssignmentPageC.UpdateRemainingUsage(sh, assignment.resourceName, assignment.remainingEffort);
//			else if(stageNo == 2 && assignment.remainingEffortStage2 != null)
//				ActivityAssignmentPageC.UpdateRemainingUsageStage2(sh, assignment);
//			else if(stageNo == 3 && assignment.remainingEffortStage3 != null)
//				ActivityAssignmentPageC.UpdateRemainingUsageStage3(sh, assignment);
//			if(stageNo < 1 || stageNo > 3)
//				throw new RuntimeException("The stage number: " + stageNo + " is out of range. Should be one off: 1, 2 or 3"); 
//		});
//	}
//	
////	public static Stage force
//
////	mobile requests
//	 
//	public static Stage enterMobileTimesheets(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(Timesheet ts : data.timesheets)
//			if(stage.getRootStageName().equals(ts.testStage))
//				enterMobileTimesheet(stage, "submit timesheet for resource: " + ts.resourceName, wd, creds, data, ts);
//		return stage;
//	}
//	
//	public static Stage enterMobileTimesheet(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, Timesheet sheet) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.enterTimesheet(wd, creds, data, sheet); });
//	}
//	
//
//	public static Stage submitMobileTimeEntries(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(Timesheet ts : data.timesheets)
//			if(stage.getRootStageName().equals(ts.testStage))
//				submitMobileTimeEntries(stage, "submit time entries for resource: " + ts.resourceName, wd, creds, data, ts);
//		return stage;
//	}
//	
//	public static Stage submitMobileTimeEntries(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, Timesheet sheet) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.submitTimesheet(wd, creds, data, sheet); });
//	}
//	
//
//	public static Stage editMobileTimesheets(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(Timesheet ts : data.timesheets)
//			if(stage.getRootStageName().equals(ts.testStage))
//				editMobileTimesheet(stage, "delete timesheet for resource: " + ts.resourceName, wd, creds, data, ts);
//		return stage;
//	}
//	
//	public static Stage editMobileTimesheet(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, Timesheet sheet) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.editTimesheet(wd, creds, data, sheet); });
//	}
//	
//
//	public static Stage deleteMobileTimesheets(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(Timesheet ts : data.timesheets)
//			if(stage.getRootStageName().equals(ts.testStage))
//				deleteMobileTimesheet(stage, "delete timesheet for resource: " + ts.resourceName, wd, creds, data, ts);
//		return stage;
//	}
//	
//	public static Stage deleteMobileTimesheet(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, Timesheet sheet) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.deleteTimesheet(wd, creds, data, sheet); });
//	}
//	
//
//	public static Stage validateMobileTimesheets(Stage parentStage, String testStage, SeleniumHelper sh, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(Timesheet ts : data.timesheets)
//			if(stage.getRootStageName().equals(ts.testStage))
//				validateMobileTimesheet(stage, "validate timesheet for resource: " + ts.resourceName, sh, data, ts);
//		return stage;
//	}
//	
//	public static Stage validateMobileTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, KimbleData data, Timesheet sheet) {
//		return new StageC(parentStage, testStage, () -> { TimesheetPageC.validateMobileTimeEntries(sh, data, sheet.timeEntries.toArray(new TimeEntry[sheet.timeEntries.size()])); });
//	}
//	
//
//	public static Stage enterMobileExpenseClaims(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseEntry ec : data.expenseEntries)
//			if(stage.getRootStageName().equals(ec.testStage))
//				enterMobileExpenseClaim(stage, "enter expense claim: " + ec.name, wd, creds, data, ec);
//		return stage;
//	}
//	
//	public static Stage enterMobileExpenseClaim(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, ExpenseEntry claim) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.enterExpenseClaim(wd, creds, data, claim); });
//	}
//	
//
//	public static Stage submitMobileExpenseClaims(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseEntry ec : data.expenseEntries)
//			if(stage.getRootStageName().equals(ec.testStage))
//				submitMobileExpenseClaim(stage, "submit expense claim: " + ec.name, wd, creds, data, ec);
//		return stage;
//	}
//	
//	public static Stage submitMobileExpenseClaim(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, ExpenseEntry claim) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.submitExpenseClaim(wd, creds, data, claim); });
//	}
//	
//
//	public static Stage editMobileExpenseClaims(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseEntry ec : data.expenseEntries)
//			if(stage.getRootStageName().equals(ec.testStage))
//				editMobileExpenseClaim(stage, "edit expense claim: " + ec.name, wd, creds, data, ec);
//		return stage;
//	}
//	
//	public static Stage editMobileExpenseClaim(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, ExpenseEntry claim) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.editExpenseClaim(wd, creds, data, claim); });
//	}
//	
//
//	public static Stage deleteMobileExpenseClaims(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseEntry ec : data.expenseEntries)
//			if(stage.getRootStageName().equals(ec.testStage))
//				deleteMobileExpenseClaim(stage, "delete expense claim: " + ec.name, wd, creds, data, ec);
//		return stage;
//	}
//	
//	public static Stage deleteMobileExpenseClaim(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, ExpenseEntry claim) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.deleteExpenseClaim(wd, creds, data, claim); });
//	}
//	
//
//	public static Stage validateMobileExpenseClaims(Stage parentStage, String testStage, SeleniumHelper sh, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseEntry ec : data.expenseEntries)
//			if(stage.getRootStageName().equals(ec.testStage))
//				validateMobileExpenseClaim(stage, "validate expense claim: " + ec.name, sh, creds, data, ec);
//		return stage;
//	}
//	
//	public static Stage validateMobileExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, UserCredentials creds, KimbleData data, ExpenseEntry claim) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.validateExpenseClaim( sh, creds, data, claim); });
//	}
//	
//
//	public static Stage addMobileExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseEntry ec : data.expenseEntries)
//			if(stage.getRootStageName().equals(ec.testStage))
//				addMobileExpenseItems(stage, "add expense items for claim: " + ec.name, wd, creds, data, ec);
//		return stage;
//	}
//	
//	public static Stage addMobileExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, ExpenseEntry claim) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseDetail ei : claim.expenseDetails)
//			addMobileExpenseItem(stage, "add expense item with notes: " + ei.notes, wd, creds, data, claim, ei);
//		return stage;
//	}
//	
//	public static Stage addMobileExpenseItem(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, ExpenseEntry claim, ExpenseDetail item) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.addExpenseItem(wd, creds, data, claim, item); });
//	}
//	
//	
//	public static Stage editMobileExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseEntry ec : data.expenseEntries)
//			if(stage.getRootStageName().equals(ec.testStage))
//				editMobileExpenseItems(stage, "edit expense items of claim: " + ec.name, wd, creds, data, ec);
//		return stage;
//	}
//	
//	public static Stage editMobileExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, ExpenseEntry claim) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseDetail ei : claim.expenseDetails)
//			editMobileExpenseItem(stage, "edit expense item with notes: " + ei.notes, wd, creds, data, claim, ei);
//		return stage;
//	}
//	
//	public static Stage editMobileExpenseItem(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, ExpenseEntry claim, ExpenseDetail item) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.editExpenseItem(wd, creds, data, claim, item); });
//	}
//	
//	
//	public static Stage deleteMobileExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseEntry ec : data.expenseEntries)
//			if(stage.getRootStageName().equals(ec.testStage))
//				deleteMobileExpenseItems(stage, "delete expense items of claim: " + ec.name, wd, creds, data, ec);
//		return stage;
//	}
//	
//	public static Stage deleteMobileExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, ExpenseEntry claim) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(ExpenseDetail ei : claim.expenseDetails)
//			deleteMobileExpenseItem(stage, "delete expense item with notes: " + ei.notes, wd, creds, data, ei);
//		return stage;
//	}
//	
//	public static Stage deleteMobileExpenseItem(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, KimbleData data, ExpenseDetail item) {
//		return new StageC(parentStage, testStage, () -> { MobileRequests.deleteExpenseItem(wd, creds, data, item); });
//	}
//	
//	public static Stage setPurchaseOrderRules(Stage parentStage, String testStage, SeleniumHelper sh, Collection<DeliveryElement> elements) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateMenu(stage, "navigate to budgets and purchase orders", sh, PagesC.BUDGETSANDPOS);
//		for(DeliveryElement e : elements)
//			if(e.purchaseOrderRule != null)
//				setPurchaseOrderRule(stage, "set purchase order rule to: " + e.purchaseOrderRule + " on element: " + e.name, sh, e);
//		return stage;
//	}
//	
//	public static Stage setPurchaseOrderRule(Stage stage, String testStage, SeleniumHelper sh, DeliveryElement element) {
//		return new StageC(stage, testStage, () -> { DeliveryGroupBudgetsPageC.setPurchaseOrderRule(sh, element.name, element.purchaseOrderRule); });
//	}
//
//	public static Stage allocateNewPurchaseOrder(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName, PurchaseOrder po) {
//		return new StageC(parentStage, testStage, () -> { DeliveryEngagementPageC.AllocateNewPurchaseOrder(sh, engagementName, po); });
//	}
//	
//	public static Stage createNewInvoice(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
//		return new StageC(parentStage, testStage, () -> { InvoicePageC.CreateNew(sh, invoice); });
//	}
//	
//	public static Stage generateNewInvoiceWithNoPO(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
//		return new StageC(parentStage, testStage, () -> { InvoicePageC.GenerateInvoiceForDeliveryEngagementWithNoPO(sh, invoice); });
//	}
//	
//	public static Stage validateInvoiceHomeValues(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
//		return new StageC(parentStage, testStage, () -> { InvoicePageC.validateInvoiceHomeValues(sh, invoice); });
//	}
//	
//	public static Stage validateInvoicePdfValues(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
//		return new StageC(parentStage, testStage, () -> {
//			waitClickable(sh, InvoicePageC.sendInvoiceForApprovalBtn, 20);
//			String id = sh.getIDFromCurrentURL();
//			String invoicePreviewUrl = sh.getApexBaseFromCurrentUrl() + "/InvoicePrintT1?id=" + id + "&renderAs=html";
//			
//			sh.runInOuterScopeAndReturn(() -> {
//				sh.goToUrl(invoicePreviewUrl);
//				InvoicePageC.validateInvoicePdfValues(sh, invoice);
//			});
//		});
//	}
//	
//	public static Stage sendInvoiceForApproval(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
//		return new StageC(parentStage, testStage, () -> { InvoicePageC.SendInvoiceForApproval(sh, invoice); });
//	}
//	
//	public static Stage dispatchInvoice(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
//		return new StageC(parentStage, testStage, () -> { InvoicePageC.DispatchInvoice(sh, invoice); });
//	}
//	
//	public static Stage getDeliveryElementId(Stage parentStage, String testStage, SeleniumHelper sh, RevenueItemAdjustment adjustment) {
//		return new StageC(parentStage, testStage, () -> { adjustment.deliveryElementId = DeliveryEngagementPageC.getElementId(sh, adjustment.deliveryElement); });
//	}
//	
//	public static Stage navigateToRevenueAdjustment(Stage parentStage, String testStage, SeleniumHelper sh, RevenueItemAdjustment adjustment) {
//		return new StageC(parentStage, testStage, () -> { DeliveryEngagementPageC.NavigateToRevenueAdjustments(sh, adjustment.deliveryElementId); });
//	}
//	
//	public static Stage createRevenueItemAdjustment(Stage parentStage, String testStage, SeleniumHelper sh, RevenueItemAdjustment adjustment) {
//		return new StageC(parentStage, testStage, () -> { RevenueItemAdjustmentPageC.CreateNew(sh, adjustment); });
//	}
//
//	public static Stage sendRevenueItemAdjustmentForApproval(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { RevenueItemAdjustmentPageC.SendForApproval(sh); });
//	}
//
//	public static Stage getDeliveryElementId(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
//		return new StageC(parentStage, testStage, () -> { requisition.deliveryElementId = DeliveryEngagementPageC.getElementId(sh, requisition.deliveryElement); });
//	}
//	
//	public static Stage navigateToSupplierRequisition(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
//		return new StageC(parentStage, testStage, () -> { DeliveryEngagementPageC.NavigateToSupplierRequisitions(sh, requisition.deliveryElementId); });
//	}
//	
//	public static Stage navigateToSupplierRequisitionDetails(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
//		return new StageC(parentStage, testStage, () -> { DeliveryElementSupplierRequisitionsPageC.navigateToRequisition(sh, requisition.reference); });
//	}
//	
//	public static Stage startSupplierRequisitionCreateWizard(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { DeliveryElementSupplierRequisitionsPageC.startCreateWizard(sh); });
//	}
//
//	public static Stage createSupplierRequisition(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
//		return new StageC(parentStage, testStage, () -> { DeliveryElementSupplierRequisitionsPageC.create(requisition, sh); });
//	}
//
//	public static Stage getSupplierRequisitionReference(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
//		return new StageC(parentStage, testStage, () -> { requisition.reference = DeliveryElementSupplierRequisitionsPageC.getReference(sh);
//			sh.assertNotEquals(requisition.reference, null, "supplier requisition reference"); });
//	}
//
//	public static Stage submitSupplierRequisition(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { DeliveryElementSupplierRequisitionsPageC.submit(sh); });
//	}
//	
//	public static Stage validateSupplierRequisitionOverview(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
//		return new StageC(parentStage, testStage, () -> { DeliveryElementSupplierRequisitionsPageC.validateOverview(sh, requisition); });
//	}
//	
//	public static Stage validateSupplierRequisitionDetails(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
//		return new StageC(parentStage, testStage, () -> { DeliveryElementSupplierRequisitionsPageC.validateDetails(sh, requisition); });
//	}
//	
//	public static Stage startCreateSupplierInvoice(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { SupplierInvoicePageC.startCreateWiz(sh); });
//	}
//
//	public static Stage supplierInvoiceCreateInWiz(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
//		return new StageC(parentStage, testStage, () -> { 
//			SupplierInvoicePageC.runCreateWizStep1(sh, invoice);
//			SupplierInvoicePageC.runCreateWizStep2(sh, invoice);
//			SupplierInvoicePageC.runCreateWizStep3(sh, invoice);
//			SupplierInvoicePageC.runCreateWizStep4(sh, invoice); 
//		});
//	}
//
//	public static Stage validateSupplierInvoicePdfValues(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
//		return new StageC(parentStage, testStage, () -> { 
//			String id = sh.getIDFromCurrentURL();
//			String invoicePreviewUrl = sh.getApexBaseFromCurrentUrl() + "/SupplierInvoicePrintT2?id=" + id + "&renderAs=html";
//			
//			sh.runInOuterScopeAndReturn(() -> {
//				sh.goToUrl(invoicePreviewUrl);
//				SupplierInvoicePageC.validateInvoicePdfValues(sh, invoice);
//			});
//		});
//	}
//	
//	public static Stage openInvoice(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
//		Stage stage = new StageC(parentStage, testStage);
//		clickGoInListView(stage, "click go", sh);
//		navigateToInvoiceFromList(stage, "navigate to invoice: " + invoice.invoiceReference, sh, invoice);
//		return stage;
//	}
//	
//	public static Stage createCreditNote(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice, CreditNote creditNote) {
//		return new StageC(parentStage, testStage, () -> { CreditNotePageC.CreateNew(sh, invoice, creditNote); });
//	}
//	
//	public static Stage getCreditNoteReference(Stage parentStage, String testStage, SeleniumHelper sh, CreditNote creditNote) {
//		return new StageC(parentStage, testStage, () -> { InvoicePageC.GetCreditNoteReference(sh, creditNote); });
//	}
//	
//	public static Stage openCreditNote(Stage parentStage, String testStage, SeleniumHelper sh, CreditNote creditNote) {
//		return new StageC(parentStage, testStage, () -> { CreditNotePageC.OpenCreditNote(sh, creditNote); });
//	}
//	
//	public static Stage sendCreditNoteForApproval(Stage parentStage, String testStage, SeleniumHelper sh, CreditNote creditNote) {
//		return new StageC(parentStage, testStage, () -> { CreditNotePageC.SendCreditNoteForApproval(sh, creditNote); });
//	}
//	
//	public static Stage dispatchCreditNote(Stage parentStage, String testStage, SeleniumHelper sh, CreditNote creditNote) {
//		return new StageC(parentStage, testStage, () -> { CreditNotePageC.DispatchCreditNote(sh, creditNote); });
//	}
//	
//	public static Stage startJourneyCreateWizard(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { JourneysPageC.startCreateWizard(sh); });
//	}
//	
//	public static Stage createJourney(Stage parentStage, String testStage, SeleniumHelper sh, Journey journey) {
//		Stage stage = new StageC(parentStage, testStage, () -> { JourneysPageC.enterNameAndFinishWizard(sh, journey.name); });
//		StagesC.createJourneyLegs(stage, "create legs", sh, journey.legs);
//		return stage;
//	}
//	
//	public static Stage createJourneyLegs(Stage parentStage, String testStage, SeleniumHelper sh, Collection<JourneyLeg> journeyLegs) {
//		Stage stage = new Stage(parentStage, testStage);
//		for(JourneyLeg jl : journeyLegs)
//			createJourneyLeg(stage, "create leg from " + jl.departingFrom + " to " + jl.arrivingAt, sh, jl);
//		return stage;
//	}
//	
//	public static Stage createJourneyLeg(Stage parentStage, String testStage, SeleniumHelper sh, JourneyLeg journeyLeg) {
//		return new Stage(parentStage, testStage, () -> { JourneysPageC.addLeg(sh, journeyLeg); });
//	}
//	
//	public static Stage openJourney(Stage parentStage, String stageName, SeleniumHelper sh, String journeyName) {
//		return new Stage(parentStage, stageName, () -> { sh.clickLink(By.xpath("//a[normalize-space(text())=\"" + journeyName + "\"]")); });
//	}
//	
//	public static Stage enterAllowanceAdjustments(Stage parentStage, String testStage, SeleniumHelper sh, List<JourneyAllowanceAdjustment> adjustments) {
//		Stage stage = new StageC(parentStage, testStage, () -> { JourneysPageC.calculateAllowances(sh); });
//		for(JourneyAllowanceAdjustment aa : adjustments)
//			enterAllowanceAdjustment(stage, "enter allowance adjustments for period " + aa.period, sh, aa);
//		return stage;
//	}
//	
//	public static Stage enterAllowanceAdjustment(Stage parentStage, String testStage, SeleniumHelper sh, JourneyAllowanceAdjustment adjustment) {
//		return new StageC(parentStage, testStage, () -> { JourneysPageC.adjustAllowance(sh, adjustment); });
//	}
//	
//	public static Stage validateAllowanceAmounts(Stage parentStage, String testStage, SeleniumHelper sh, Map<String, String> amounts) {
//		return new StageC(parentStage, testStage, () -> { JourneysPageC.validateAmounts(sh, amounts); });
//	}
//
//	public static Stage enterAllowanceAllocations(Stage parentStage, String testStage, SeleniumHelper sh, List<JourneyAllowanceAllocation> allocation) {
//		Stage stage = new StageC(parentStage, testStage);
//		for(JourneyAllowanceAllocation aa : allocation)
//			enterAllowanceAllocation(stage, "enter allowance allocation for period " + aa.period, sh, aa);
//		return stage;
//	}
//	
//	public static Stage enterAllowanceAllocation(Stage parentStage, String testStage, SeleniumHelper sh, JourneyAllowanceAllocation allocation) {
//		return new StageC(parentStage, testStage, () -> { JourneysPageC.allocateAllowance(sh, allocation); });
//	}
//	
//	public static Stage createExpenseItemsForJourney(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { JourneysPageC.createExpenseItems(sh); });
//	}
//	
//	public static Stage navigateFromJourneyToExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, String activity, boolean isSubmitted) {
//		return new StageC(parentStage, testStage, () -> { 
//			if(!isSubmitted)
//				JourneysPageC.navigateToExpenseClaim(sh, activity);
//			else
//				JourneysPageC.navigateToSubmittedExpenseClaim(sh, activity); 
//		});
//	}
//	
//	public static Stage validateExpenseEntries(Stage parentStage, String testStage, SeleniumHelper sh, String resource, ExpenseEntry entry) {
//		return new StageC(parentStage, testStage, () -> { ExpensePageC.validate(sh, resource, entry); });
//	}
//	
//	public static Stage updateHighLevelForecastStage(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement) {
//		return new StageC(parentStage, testStage, () -> { DeliveryEngagementPageC.updateHighLevelForecast(sh, engagement); });
//	}
//	
//	public static Stage navigateAndUpdateForecastStatusStage(Stage parentStage, String testStage, SeleniumHelper sh, String toStatus) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateMenu(stage, "navigate to maintain opportunity", sh, PagesC.SALESOPPEDIT);
//		updateForecastStatusAndSave(stage, "update forecast status and save", sh, toStatus);
//		return stage;		
//	}
//	
//	public static Stage updateForecastStatusAndSave(Stage parentStage, String testStage, SeleniumHelper sh, String toStatus) {
//		return new StageC(parentStage, testStage, () -> { SalesOpportunityPageC.updateForecastStatusAndSave(sh, toStatus); });
//	}
//	
//	public static Stage navigateAndUpdateCloseDate(Stage parentStage, String testStage, SeleniumHelper sh, String closeDate) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateMenu(stage, "navigate to maintain opportunity", sh, PagesC.SALESOPPEDIT);
//		updateCloseDateStage(stage, "update close date", sh, closeDate);
//		return stage;
//	}
//	
//	public static Stage updateCloseDateStage(Stage parentStage, String testStage, SeleniumHelper sh, String closeDate) {
//		return new StageC(parentStage, testStage, () -> { SalesOpportunityPageC.updateCloseDateAndSave(sh, closeDate); });
//	}
//	
//	public static Stage navigateAndUpdateBusinessUnit(Stage parentStage, String testStage, SeleniumHelper sh, String businessUnit) {
//		Stage stage = new StageC(parentStage, testStage);
//		navigateMenu(stage, "navigate to maintain opportunity", sh, PagesC.SALESOPPEDIT);
//		updateBusinessUnitStage(stage, "update close date", sh, businessUnit);
//		return stage;
//	}
//	
//	public static Stage updateBusinessUnitStage(Stage parentStage, String testStage, SeleniumHelper sh, String businessUnit) {
//		return new StageC(parentStage, testStage, () -> { SalesOpportunityPageC.updateBusinessUnitAndSave(sh, businessUnit); });
//	}
//	
//	public static Stage startWhatIfScenarioCreateWizard(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { WhatIfScenariosPageC.startCreateWizard(sh); });
//	}
//	
//	public static Stage inputWhatIfScenarioValues(Stage parentStage, String testStage, SeleniumHelper sh, WhatIfScenario scenario) {
//		return new StageC(parentStage, testStage, () -> { WhatIfScenariosPageC.inputValuesAndSave(sh, scenario); });
//		
//	}
//	
//	public static Stage acceptWhatIfScenario(Stage parentStage, String testStage, SeleniumHelper sh, String scenarioName) {
//		return new StageC(parentStage, testStage, () -> { WhatIfScenariosPageC.acceptWhatIfScenario(sh, scenarioName); });
//		
//	}
//	
//	public static Stage waitForWhatIfScenarioAcceptToComplete(Stage parentStage, String testStage, SeleniumHelper sh) {
//		return new StageC(parentStage, testStage, () -> { WhatIfScenariosPageC.waitForAcceptToComplete(sh); });
//	}
//}
