package kimble.automation.scenarios;

import static kimble.automation.helpers.SequenceActions.*;
import static kimble.automation.scenarios.Stages.enableGenerateSupplierInvoiceRef;
import static kimble.automation.scenarios.Stages.generateInvoice;
import static kimble.automation.scenarios.Stages.navigateToSupplierRequisitionDetails;
import static kimble.automation.scenarios.Stages.navigateToSupplierRequisitions;
import static kimble.automation.scenarios.Stages.overrideOrgDate;
import static kimble.automation.scenarios.Stages.scheduleOtherActivities;
import static kimble.automation.scenarios.Stages.seedCustomJob;
import static kimble.automation.scenarios.Stages.validateSupplierRequisitionOverviewTable;
import static kimble.automation.scenarios.Stages.validateSupplierRequisitionResultsDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import kimble.automation.domain.Account;
import kimble.automation.domain.AccountCredits;
import kimble.automation.domain.ActivityAssignment;
import kimble.automation.domain.ActivityAssignmentsMany;
import kimble.automation.domain.ActivityUnavailability;
import kimble.automation.domain.Annuity;
import kimble.automation.domain.BulkEdit;
import kimble.automation.domain.ChangeControl;
import kimble.automation.domain.CreditNote;
import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.ExpectedResult;
import kimble.automation.domain.ExpenseClaim;
import kimble.automation.domain.ExpenseDetail;
import kimble.automation.domain.ExpenseEntry;
import kimble.automation.domain.ExpenseLineItem;
import kimble.automation.domain.GroupAssignmentTemplate;
import kimble.automation.domain.Invoice;
import kimble.automation.domain.Journey;
import kimble.automation.domain.JourneyAllowanceAdjustment;
import kimble.automation.domain.JourneyAllowanceAllocation;
import kimble.automation.domain.JourneyLeg;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.Milestone;
import kimble.automation.domain.Opportunity;
import kimble.automation.domain.OtherResourcedActivity;
import kimble.automation.domain.PerformanceAnalysis;
import kimble.automation.domain.Proposal;
import kimble.automation.domain.PurchaseOrder;
import kimble.automation.domain.Resource;
import kimble.automation.domain.ResourceForecast;
import kimble.automation.domain.ResourceList;
import kimble.automation.domain.ResourceUsageAdjustment;
import kimble.automation.domain.ResourcedActivity;
import kimble.automation.domain.RevenueItemAdjustment;
import kimble.automation.domain.Risk;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.domain.SalesOpportunityForecast;
import kimble.automation.domain.ScheduledActivity;
import kimble.automation.domain.SupplierInvoice;
import kimble.automation.domain.SupplierProduct;
import kimble.automation.domain.SupplierRequisition;
import kimble.automation.domain.SupplierRequisitionResult;
import kimble.automation.domain.Task;
import kimble.automation.domain.TestState;
import kimble.automation.domain.TimeEntry;
import kimble.automation.domain.TimeEntryAdjustment;
import kimble.automation.domain.TimeEntryAdjustmentList;
import kimble.automation.domain.TimePeriodAction;
import kimble.automation.domain.Timesheet;
import kimble.automation.domain.TrackingPlanTotal;
import kimble.automation.domain.TravelRequisition;
import kimble.automation.domain.TravelRequisitionBooking;
import kimble.automation.domain.TravelRequisitionItinerary;
import kimble.automation.domain.TrendAnalysis;
import kimble.automation.domain.UserCredentials;
import kimble.automation.domain.WhatIfScenario;
import kimble.automation.domain.imports.TimeEntryWithTimesList;
import kimble.automation.domain.mobile.TimeEntryMob;
import kimble.automation.domain.mobile.general.Identifiers.TimestampedIdentifier;
import kimble.automation.domain.results.RiskDashboardResults;
import kimble.automation.domain.results.RisksSalesResults;
import kimble.automation.domain.results.UsageAdjustmentResults;
import kimble.automation.helpers.General;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.MobileRequests;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.kimbleobjects.classic.PendingApprovalsPageC;
import kimble.automation.kimbleobjects.classic.AccountPageC;
import kimble.automation.kimbleobjects.classic.ActivityAssignmentBulkEditPageC;
import kimble.automation.kimbleobjects.classic.ActivityAssignmentPageC;
import kimble.automation.kimbleobjects.classic.ActivityAssignmentsManyPageC;
import kimble.automation.kimbleobjects.classic.AnnuityPageC;
import kimble.automation.kimbleobjects.classic.BusinessUnitPageC;
import kimble.automation.kimbleobjects.classic.ConfigurationSettingsPageC;
import kimble.automation.kimbleobjects.classic.CreditNotePageC;
import kimble.automation.kimbleobjects.classic.CreditPageC;
import kimble.automation.kimbleobjects.classic.DeliveryElementPageC;
import kimble.automation.kimbleobjects.classic.DeliveryElementSupplierRequisitionsPageC;
import kimble.automation.kimbleobjects.classic.DeliveryEngagementPageC;
import kimble.automation.kimbleobjects.classic.DeliveryGroupActivitiesPageC;
import kimble.automation.kimbleobjects.classic.DeliveryGroupBudgetsPageC;
import kimble.automation.kimbleobjects.classic.DeliveryGroupMilestonesPageC;
import kimble.automation.kimbleobjects.classic.ExpenseForecastingPageC;
import kimble.automation.kimbleobjects.classic.ExpensePageC;
import kimble.automation.kimbleobjects.classic.ForecastingPeriodClosePageC;
import kimble.automation.kimbleobjects.classic.GroupAssignmentTemplatePageC;
import kimble.automation.kimbleobjects.classic.InterfaceRunPageC;
import kimble.automation.kimbleobjects.classic.InvoicePageC;
import kimble.automation.kimbleobjects.classic.JobAdministrationPageC;
import kimble.automation.kimbleobjects.classic.JobsNewPageC;
import kimble.automation.kimbleobjects.classic.JourneysPageC;
import kimble.automation.kimbleobjects.classic.LoginPageC;
import kimble.automation.kimbleobjects.classic.MobileExpenseEntry;
import kimble.automation.kimbleobjects.classic.MobileHomePage;
import kimble.automation.kimbleobjects.classic.MobileLoginPage;
import kimble.automation.kimbleobjects.classic.MobileSelectors;
import kimble.automation.kimbleobjects.classic.MobileTimeEntry;
import kimble.automation.kimbleobjects.classic.OpportunityPageC;
import kimble.automation.kimbleobjects.classic.OtherResourcedActivityPageC;
import kimble.automation.kimbleobjects.classic.PagesC;
import kimble.automation.kimbleobjects.classic.PendingApprovalsPageC;
import kimble.automation.kimbleobjects.classic.ProposalEditPageC;
import kimble.automation.kimbleobjects.classic.ResourcePageC;
import kimble.automation.kimbleobjects.classic.ResourceSupplierDashboardPageC;
import kimble.automation.kimbleobjects.classic.RevenueItemAdjustmentPageC;
import kimble.automation.kimbleobjects.classic.RiskDashboardPageC;
import kimble.automation.kimbleobjects.classic.RisksPageC;
import kimble.automation.kimbleobjects.classic.SalesOpportunityPageC;
import kimble.automation.kimbleobjects.classic.SupplierInvoicePageC;
import kimble.automation.kimbleobjects.classic.TimePeriodPageC;
import kimble.automation.kimbleobjects.classic.TimesheetPageC;
import kimble.automation.kimbleobjects.classic.TrackingPeriodClosePageC;
import kimble.automation.kimbleobjects.classic.TrackingPlanTasksPageC;
import kimble.automation.kimbleobjects.classic.TravelRequisitionPageC;
import kimble.automation.kimbleobjects.classic.TrendAnalysisPageC;
import kimble.automation.kimbleobjects.classic.WhatIfScenariosPageC;
import kimble.automation.kimbleobjects.lightning.GeneralOperationsZ;
import kimble.automation.kimbleobjects.lightning.OpportunityZ;
import kimble.automation.kimbleobjects.lightning.SalesOpportunityPageZ;
import kimble.automation.kimbleobjects.classic.GeneralOperations;
import kimble.automation.KimbleOneTest;
import kimble.automation.KimbleOneTest.CleardownType;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;

public class Stages {
	
	public static Stage abort(Stage parentStage, String stageName) {
		return new Stage(parentStage, stageName) { 
			public void run() { throw new RuntimeException("Aborted at stage: '" + stageName + "'"); } 
		};
	}
	
	public static Stage doNothing(Stage parentStage, String stageName) {
		return new Stage(parentStage, stageName) {};
	}
	
	public static Stage dataCleardown(Stage parentStage, String stageName, SeleniumHelper sh, CleardownType type) {
		Stage stage = new Stage(parentStage, stageName);
		overrideOrgDate(stage, "override the org date", sh, "");
		if(type == CleardownType.none)
			return doNothing(stage, "no data cleardown will be performed");
		runDataCleardown(stage, "run data cleardown", sh);
		if(type == CleardownType.dataandperiodreset) {
			reOpenOperationalPeriods(stage, "re-open operational periods", sh);
			runAllJobsMultiThread(stage, "run all jobs after opening periods", sh);
		}
		if(type != CleardownType.datapreserveaccounts)
			deleteAccountInvoicingModel(stage,"delete account invoicing models", sh);
			deleteAccounts(stage, "delete accounts", sh);

		deleteOpportunities(stage, "delete opportunities", sh);
		deleteResources(stage, "delete UK Co 3.. resources", sh);
		return stage;
	}
	
	public static Stage runDataCleardown(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new GeneralOperations(sh).runDataCleardown();
			}
		};
	}
	
	public static Stage reOpenOperationalPeriods(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new GeneralOperations(sh).reOpenOperationalPeriods();
			}
		};
	}
	
	public static Stage deleteAccounts(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new GeneralOperations(sh).deleteAccounts();
			}
		};
	}
	
	public static Stage deleteAccountInvoicingModel(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new GeneralOperations(sh).deleteAccountInvoicingModel();
			}
		};
	}

	public static Stage deleteOpportunities(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new GeneralOperations(sh).deleteOpportunities();
			}
		};
	}
	
	public static Stage deleteResources(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				ResourcePageC.deleteAllResources(sh);
			}
		};
	}
	
	public static Stage loginAs(Stage parentStage, String stageName, SeleniumHelper sh, String userName, String loginName) {
		Stage stage = new Stage(parentStage, stageName);
		stage.forceRun = true;
		navigateToSetup(stage, "navigate to setup", sh);
		navigateToUsersAndLoginByUsername(stage, "navigate to users and login as: " + userName, userName, sh, loginName);
		closeOtherTabs(stage, "close created tab", sh);
		return stage;
	}
	
	public static Stage navigateToSetup(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				sh.navigateToSFPage("setup/forcecomHomepage.apexp");
			}
		};
	}
	
	public static Stage navigateToUsersAndLoginByUsername(Stage parentStage, String stageName, String userName, SeleniumHelper sh, String loginName) {
		return new Stage(parentStage, stageName) {
			public void run() {
				executeSequenceWithRetry(sh, 3, () -> {
					By quickFindInput = By.cssSelector("input.quickFindInput");
					By usersLink = By.xpath("//div[@class='setupLeaf' or contains(@class, 'setupHighlightLeaf')]/a[text()='Users']");
					By loginLink = By.xpath("//a[text()='Login'][../../th/a[text()=\"" + userName + "\"]]");
					By usernameLink = By.xpath("//a[normalize-space(text())=\"" + userName + "\"]");
					By loggedInAsSpan = By.xpath("//span[contains(@class, 'highImportance')][contains(text(), \"Logged in as " + loginName + "\")]");
					
					if(!exists(sh, loggedInAsSpan, 5)) {
						sh.closeLightningPopUp();
						clearAndInputText(sh, quickFindInput, "users");
						clickAndWaitSequence(sh, 20,
							usersLink,
							loginLink,
							usernameLink,
							loggedInAsSpan
						);
					}
				});
			}
		};
	}
	
	public static Stage navigateUsersFromSetup(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				By quickFindInput = By.cssSelector("input.quickFindInput");
				By usersLink = By.xpath("//div[@class='setupLeaf']/a[text()='Users']");
				clearAndInputText(sh, quickFindInput, "users");
				waitClickable(sh, usersLink, 20);
				click(sh, usersLink);
			}
		};
	}
	
	public static Stage switchToTab(Stage parentStage, String stageName, SeleniumHelper sh, int tabNumber) {
		return new Stage(parentStage, stageName) {
			public void run() {
				String selectAll = Keys.chord(Keys.CONTROL, "" + tabNumber);
				sh.getWD().findElement(By.tagName("html")).sendKeys(selectAll);
			}
		};
	}
	
	public static Stage closeOtherTabs(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				String currentHandle = sh.getWD().getWindowHandle();
				for(String handle : sh.getWD().getWindowHandles())
					if(!handle.equals(currentHandle))
						sh.getWD().switchTo().window(handle).close();
				sh.getWD().switchTo().window(currentHandle);
			}
		};
	}
	
	public static Stage loginByUsername(Stage parentStage, String stageName, SeleniumHelper sh, String userName) {
		return new Stage(parentStage, stageName) {
			public void run() {
				By loginLink = By.xpath("//a[text()='Login'][../../th/a[text()=\"" + userName + "\"]]");
				By usernameLink = By.xpath("//a[normalize-space(text())=\"" + userName + "\"]");
				By loggedInAsSpan = By.xpath("//span[contains(@class, 'highImportance')][contains(text(), \"Logged in as " + userName + "\")]");
				clickAndWaitSequence(sh, 20,
					loginLink,
					usernameLink,
					loggedInAsSpan
				);
			}
		};
	}
	
	public static Stage navigateToLoginPage(Stage parentStage, String stageName, SeleniumHelper sh, String loginUrl, String startUrl) {
		return new Stage(parentStage, stageName) {
			public void run() {
				LoginPageC.NavigateToLoginPageAndDirect(sh, loginUrl, startUrl);
			}
		};
	}
	
	public static Stage navigateToLoginPageWithRedirectBack(Stage parentStage, String stageName, SeleniumHelper sh, String loginUrl) {
		return new Stage(parentStage, stageName) {
			public void run() {
				LoginPageC.NavigateToLoginPageAndDirect(sh, loginUrl, sh.getCurrentUrl());
			}
		};
	}
	
	public static Stage login(Stage parentStage, String stageName, SeleniumHelper sh, UserCredentials creds) {
		return new Stage(parentStage, stageName) {
			public void run() {
			    new LoginPageC(sh).fastLogin(creds);
			    sh.waitForPageLoadComplete(20);
			}
		};
	}
	
	public static Stage navigateToLoginPageAndLogin(Stage parentStage, String stageName, SeleniumHelper sh, UserCredentials creds, String startUrl) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToLoginPage(stage, "navigate to login page", sh, creds.loginUrl, startUrl);
		login(stage, "login", sh, creds);
		return stage;
	}
	
	public static Stage logout(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				General.logout(sh);
			}
		};
	}
	
	public static Stage sleep(Stage parentStage, String stageName, SeleniumHelper sh, long millis) {
		return new Stage(parentStage, stageName) { 
			public void run() { sh.sleep(millis, stageName); } 
		};
	}
	
	public static Stage refreshPage(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) { 
			public void run() { sh.refreshBrowser(); } 
		};
	}
	
	public static Stage navigateFromProposalToRisksDelivery(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName) {
		return new Stage(parentStage, stageName) {
			public void run() {
				sh.clickMenuItem(PagesC.PROPOSALITEMS);
				sh.clickLink(By.xpath("//a[text()=\"" + engagementName + "\"]"));
				sh.clickMenuItem(PagesC.RISKSDELIVERY);
			}
		};
	}
	
	public static Stage navigateFromAnywhereToAllTabs(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) { 
			public void run() { sh.goToAllTabsPage(); } 
		};
	}
	
	public static Stage navigateFromAllTabsToTab(Stage parentStage, String stageName, SeleniumHelper sh, String tabName) {
		return new Stage(parentStage, stageName) {
			public void run() { sh.clickLastLink(tabName); }
		};
	}
	
	public static Stage navigateFromAnywhereToTab(Stage parentStage, String stageName, SeleniumHelper sh, String tabName) {
		Stage stage = new Stage(parentStage, stageName);
		if(sh.isLightning()) {
			StagesZ.navigateFromAnywhereToTab(stage, "navigate to accounts tab", sh, tabName);
		}
		else {
			navigateFromAnywhereToAllTabs(stage, "navi to all tabs", sh);
			sh.closeLightningPopUp();
			navigateFromAllTabsToTab(stage, "navi to tab " + tabName, sh, tabName);
		}
		return stage;
	}
	
	  public static Stage closeLightningpopup(Stage parentStage, String stageName, SeleniumHelper sh) {
	        return new Stage(parentStage, stageName) {
	            public void run() {
	                sh.closeLightningPopUp();
	            }
	        };
	    }
		
	public static Stage clickGoInListView(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				sh.closeLightningPopUp();
				sh.clickGoInListView();
			}
		};
	}
	
	public static Stage navigateFromAllTabsToSalesOpps(Stage parentStage, String stageName, SeleniumHelper sh) {
		return navigateFromAllTabsToList(parentStage, stageName, sh, "Sales Opportunities");
	}
	
	public static Stage navigateFromAllTabsToList(Stage parentStage, String stageName, SeleniumHelper sh, String listName) {
		return new Stage(parentStage, stageName) {
			public void run() { sh.NavigateToList(listName); }
		};
	}
	
	public static Stage navigateFromSalesOppsToSalesOpp(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName) {
		return new Stage(parentStage, stageName) {
			public void run() { sh.clickLink(By.linkText(salesOppName)); }
		};
	}

	public static Stage navigateFromProposalsToProposal(Stage parentStage, String stageName, SeleniumHelper sh, String proposalName) {
		return new Stage(parentStage, stageName) {
			public void run() { sh.clickLink(By.linkText(proposalName)); }
		};
	}
	
	public static Stage navigateMenu(Stage parentStage, String stageName, SeleniumHelper sh, String pageName) {
		return navigateMenu(parentStage, stageName, sh, pageName, false);
	}
	
	public static Stage navigateMenu(Stage parentStage, String stageName, SeleniumHelper sh, String pageName, boolean withoutFilterId) {
		return new Stage(parentStage, stageName) {
			public void run() { sh.clickMenuItem(pageName, withoutFilterId); }
		};
	}
	
	public static Stage assertTrue(Stage parentStage, String stageName, SeleniumHelper sh, boolean isTrue) {
		return new Stage(parentStage, stageName) { public void run() {
			sh.assertTrue(isTrue, stageName);
		}}; 
	}
	
	public static Stage assertEquals(Stage parentStage, String stageName, SeleniumHelper sh, Object expected, Object actual) {
		return new Stage(parentStage, stageName) { public void run() {
			sh.assertEquals(expected, actual, stageName);
		}}; 
	}
	
	public static Stage assertNotEquals(Stage parentStage, String stageName, SeleniumHelper sh, Object expected, Object actual) {
		return new Stage(parentStage, stageName) { public void run() {
			sh.assertNotEquals(expected, actual, stageName);
		}}; 
	}
	
	public static Stage addDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) {
		return addDeliveryElementsInOcs(parentStage, stageName, sh, salesOpp.deliveryEngagements);
	}
	
	public static Stage addDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, List<DeliveryEngagement> engagements) {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryEngagement en : engagements)
			addDeliveryElementsInOcs(stage, "add delivery elements for engagement: " + en.name, sh, en);
		return stage;
	}
	
	public static Stage addDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName);
		if(engagement.deliveryElements == null)
			return doNothing(parentStage, "no elements");
		for(DeliveryElement el : engagement.deliveryElements)
			if(el.stage.equals(stage.getRootStageName()))
				addDeliveryElementInOcs(stage, "add delivery element: " + el.name, sh, engagement.name, el);
		return stage;
	}
	
	public static Stage addDeliveryElementInOcs(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, DeliveryElement element) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.addElement(sh, engagementName, element);
			}
		};
	}
	
	/** expects to be in the ProposalItems page */
	public static Stage addAllOptionalDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, List<DeliveryEngagement> engagements) {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryEngagement de : engagements)
			if(stage.getRootStageName().equals(de.stage))
				addAllOptionalElements(stage, "add all optional elements to engagement: " + de.name, sh, de.name);
		return stage;
	}
	
	/** expects to be in the ProposalItems page */
	public static Stage addAllOptionalElements(Stage parentStage, String stageName, SeleniumHelper sh, String engagement) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.addAllOptionalElements(sh, engagement);
			}
		};
	}
	
	public static Stage deleteDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, List<DeliveryEngagement> engagements) {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryEngagement en : engagements)
			deleteDeliveryElementsInOcs(stage, stageName, sh, en);
		return stage;
	}

	public static Stage deleteDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		return deleteDeliveryElementsInOcs(parentStage, stageName, sh, engagement.name, engagement.deliveryElements);
	}

	public static Stage deleteDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, String engagement, List<DeliveryElement> elements) {
		if(elements == null)
			return doNothing(parentStage, "no elements");
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryElement el : elements) 
			if(el.stage.equals(stage.getRootStageName()))
				deleteDeliveryElementInOcs(stage, "deleting element: " + el.name, sh, engagement, el);
		return stage;
	}
	
	public static Stage deleteDeliveryElementInOcs(Stage parentStage, String stageName, SeleniumHelper sh, String engagement, DeliveryElement element) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.deleteElement(sh, engagement, element);
			}
		};
	}
	
	/** Expects to be in Proposal Items */
	public static Stage validateSalesOpportunitySummaryFieldsOcs(Stage parentStage, String stageName, SeleniumHelper sh, Collection<ExpectedResult> expectedResults) {
		return new Stage(parentStage, stageName) {
			public void run() {
				for(ExpectedResult res : expectedResults)
					if(res.testStage.equals(getRootStageName()))
						SalesOpportunityPageC.validateProposalItemsSummaryFields(sh, res.salesOpportunity, getRootStageName());;
			}
		};
	}

	/** Expects to be in Proposal Items */
	/** Copy of validateSalesOpportunitySummaryFieldsOcs but changed for standalone proposals without salesops */
	public static Stage validateProposalItemsSummaryFieldsOcs(Stage parentStage, String stageName, SeleniumHelper sh, Collection<ExpectedResult> expectedResults) {
		return new Stage(parentStage, stageName) {
			public void run() {
				for(ExpectedResult res : expectedResults)
					if(res.testStage.equals(getRootStageName()))
						ProposalEditPageC.validateProposalItemsSummaryFields(sh, res.proposals, getRootStageName());;
			}
		};
	}	
	
	/** Expects to be in Proposal Items */
	public static Stage validateSalesOpportunityInOcs(Stage parentStage, String stageName, SeleniumHelper sh, Collection<ExpectedResult> expectedResults) {
		Stage stage = new Stage(parentStage, stageName);
		for(ExpectedResult res : expectedResults) {
			if(res.testStage.equals(stage.getRootStageName())) {
				validateProposalItemsSummaryFields(stage, "validate proposal items summary fields", sh, res.salesOpportunity);
				validateEngagementElements(stage, "validate engagement elements", sh, res.salesOpportunity);
			}
		}
		return stage;
	}

	/** Expects to be in Proposal Items */
	public static Stage validateProposalItemsSummaryFields(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.validateProposalItemsSummaryFields(sh, salesOpp, getRootStageName());;
			}
		};
	}

	/** Expects to be in Proposal Items */
	public static Stage validateEngagementElements(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, stageName);
		validateEngagementElements(stage, "validate engagements", sh, salesOpp.deliveryEngagements);
		return stage;
	}

	/** expects to be in the ProposalItems page */
	public static Stage validateEngagementElements(Stage parentStage, String stageName, SeleniumHelper sh, List<DeliveryEngagement> expected) {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryEngagement e : expected)
			if(e.stage.equals(stage))
				validateElements(stage, "validate engagement elements", sh, e.name, e.deliveryElements);
		return stage;
	}
		
	/** expects to be in the ProposalItems page */
	public static Stage validateElements(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, List<DeliveryElement> expected) {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryElement el : expected)
			if(el.stage.equals(stage))
				validateElement(stage, "validate element: " + el.name, sh, engagementName, el);
		return stage;
	}
		
	/** expects to be in the ProposalItems page */
	public static Stage validateElement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, DeliveryElement expected) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.validateElement(sh, engagementName, expected);
			}
		};
	}
		
	/** Expects to be in Proposal Items and have the correctly named Delivery Engagement and Element created*/
	public static Stage configureDeliveryEngagementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, List<DeliveryEngagement> engagements) {	
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryEngagement e : engagements)
			if(e.stage == null || stage.getRootStageName().equals(e.stage))
				configureDeliveryEngagementInOcs(stage, "configure engagemet: " + e.name, sh, e);
		return stage;
	}

	/** expects to be in the ProposalItems page */
	public static Stage configureDeliveryEngagementInOcs(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		return configureDeliveryElementsInOcs(parentStage, stageName, sh, engagement.name, engagement.deliveryElements);
	}
	
	public static Stage configureDeliveryElementsInOcs(Stage parentStage, String stageName, SeleniumHelper sh, String engagement, Collection<DeliveryElement> elements) {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryElement el : elements)
			if(el.stage == null || el.stage.equals(stage.getRootStageName()))
				configureDeliveryElementInOcs(stage, "configure element: " + el.name, sh, engagement, el);
		return stage;
	}
		
	public static Stage configureDeliveryElementInOcs(Stage parentStage, String stageName, SeleniumHelper sh, String engagement, DeliveryElement element) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.configureElementDynamic(sh, engagement, element);
			}
		};
	}
		
	public static Stage navigateFromAnywhereToSalesOpp(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName) {
		Stage stage = new Stage(parentStage, stageName);
		if(sh.isLightning())
			return StagesZ.navigateFromAnywhereToListViewItem(stage, stageName, sh, "Sales Opportunities", salesOppName);
		else{
			navigateFromAnywhereToAllTabs(stage, "navi to all tabs", sh);
			navigateFromAllTabsToSalesOpps(stage, "navi to sales opps", sh);
			navigateFromSalesOppsToSalesOpp(stage, "navi to sales opp: " + salesOppName, sh, salesOppName);
		}
		return stage;
	}
	
	public static Stage navigateFromAnywhereToSalesOppScope(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToSalesOpp(stage, "navigate to sales opp '" + salesOppName + "' home", sh, salesOppName);
		navigateMenu(stage, "navigate to sales opp '" + salesOppName + "' scope", sh, PagesC.PROPOSALITEMS);
		return stage;
	}
	
	public static Stage navigateFromAnywhereToSalesOppForecast(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToSalesOpp(stage, "navigate to sales opp '" + salesOppName + "' home", sh, salesOppName);
		navigateToSalesOppForecastFromHome(stage, "navigate to sales opp '" + salesOppName + "' forecast", sh);
		return stage;
	}
	
	public static Stage navigateFromAnywhereToSalesOppLose(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToSalesOpp(stage, "navigate to sales opp '" + salesOppName + "' home", sh, salesOppName);
		navigateToSalesOppLose(stage, "navigate to lose", sh);
		return stage;
	}
	
	public static Stage navigateToSalesOppForecastFromHome(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName){ public void run() {
			SalesOpportunityPageC.NavigateToSalesOppForecastFromHome(sh);
		}};
	}
	
	public static Stage navigateToSalesOppHomeFromForecast(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName) {
		return new Stage(parentStage, stageName){ public void run() {
			SalesOpportunityPageC.NavigateToSalesOppHomeFromForecast(sh, salesOppName);
		}};
	}
	
	public static Stage navigateToSalesOppLose(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName){ public void run() {
			SalesOpportunityPageC.NavigateLoseSalesOpp(sh);
		}};
	}
	
	public static Stage navigateFromAnywhereToProposal(Stage parentStage, String stageName, SeleniumHelper sh, String proposalName) {
		Stage stage = new Stage(parentStage, stageName);
		if(sh.isLightning())
			return StagesZ.navigateFromAnywhereToListViewItem(stage, stageName, sh, "Proposals", proposalName);
		else{
			navigateToProposals(stage, "navigate to all Proposals", sh);
			navigateFromProposalsToProposal(stage, "navi to proposal: " + proposalName, sh, proposalName);
		}
		return stage;
	}
	
	/** input the date in the format "dd/MM/yyyy" i.e. "26/01/2013" */
	public static Stage navigateFromAnywhereToTimesheetDayInTnx(Stage parentStage, String stageName, TestState state, SeleniumHelper sh, String date) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToTab(stage, "navi to Time & Expenses", sh, "Time & Expense Entry");
		navigateTotimesheetDayInTnx(stage, "navi to date " + date, sh, date);
		return stage;
	}
	
	public static Stage navigateFromAnywhereToSupplierInvoices(Stage parentStage, String stageName, SeleniumHelper sh) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToAllTabs(stage, "navigate to all tabs", sh);
		navigateFromAllTabsToList(stage, "navigate to supplier invoices", sh, "Supplier Invoices");
		return stage;
	}
	
	public static Stage navigateToSupplierInvoice(Stage parentStage, String stageName, SeleniumHelper sh, String reference) {
		return new Stage(parentStage, stageName){ public void run() {
			sh.clickLink(By.xpath("//a[normalize-space(text())=\"" + reference + "\"]"));
		}};
	}
	
	/** input the date in the format "dd/MM/yyyy" i.e. "26/01/2013" */
	public static Stage navigateTotimesheetDayInTnx(Stage parentStage, String stageName, SeleniumHelper sh, String date) {
		return new Stage(parentStage, stageName) {
			public void run() {
				TimesheetPageC.navigateToDay(sh, date);
			}
		};
	}
	
	public static Stage createRisks(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Risk> risks) {
		Stage stage = new Stage(parentStage, stageName);
		for(Risk r : risks) {
			if(!r.stage.equals(stage.getRootStageName()))
				continue;
			createRisk(stage, "create " + r.category + " risk with impact: " + r.impact + ", probability: " + r.probability + " and severity: " + r.severity, sh, r);
		}
		return stage;
	}
	
	public static Stage createRisk(Stage parentStage, String stageName, SeleniumHelper sh, Risk risk) {
		return new Stage(parentStage, stageName) {
			public void run() {
				RisksPageC page = new RisksPageC(sh);
				page.create(risk);
			}
		};
	}
	
	public static Stage createRisksInDashboard(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Risk> risks) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new RiskDashboardPageC(sh).createRisks(risks, getRootStageName());
			}
		};
	}
	
	public static Stage editRisks(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Risk> risks) {
		Stage stage = new Stage(parentStage, stageName);
		for(Risk r : risks)
			if(r.stage.equals(stage.getRootStageName()))
				editRisk(stage, "edit " + r.category + " risk", sh, r);
		return stage;
	}
	
	public static Stage editRisk(Stage parentStage, String stageName, SeleniumHelper sh, Risk risk) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new RisksPageC(sh).edit(risk);
			}
		};
	}
	
	public static Stage deleteRisks(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Risk> risks) {
		Stage stage = new Stage(parentStage, stageName);
		for(Risk r : risks)
			if(r.stage.equals(stage.getRootStageName()))
				deleteRisk(stage, "delete risk", sh, r);
		return stage;
	}
	
	public static Stage deleteRisk(Stage parentStage, String stageName, SeleniumHelper sh, Risk risk) {
		return new Stage(parentStage, stageName) {
			public void run() { 
				new RisksPageC(sh).delete(risk);
			}
		};
	}
	
	public static Stage validateRisks(Stage parentStage, String stageName, SeleniumHelper sh, KimbleData data) {
		Stage stage = new Stage(parentStage, stageName);
		for(RisksSalesResults e : data.getExpectedResult(stage.getRootStageName()).risksSales)
			validateRisk(stage, "validate risk is of level: " + e.riskLevel, sh, e);
		return stage;
	}
	
	public static Stage validateRisk(Stage parentStage, String stageName, SeleniumHelper sh, RisksSalesResults expected) {
		return new Stage(parentStage, stageName) {
			public void run() { 
				new RisksPageC(sh).validate(expected);
			}
		};
	}
	
	public static Stage validateRiskDashboard(Stage parentStage, String stageName, SeleniumHelper sh, KimbleData data) {
		Stage stage = new Stage(parentStage, stageName);
		for(RiskDashboardResults e : data.getExpectedResult(stage.getRootStageName()).riskDashboards)
			validateRiskDashboard(stage, "validate risk dashboard", sh, e);
		return stage;
	}
	
	public static Stage validateRiskDashboard(Stage parentStage, String stageName, SeleniumHelper sh, RiskDashboardResults expected) {
		return new Stage(parentStage, stageName) {
			public void run() { 
				new RiskDashboardPageC(sh).validate(expected);
			}
		};
	}
	
	public static Stage validateRisksExist(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Risk> risks, String... creationStageNames) {
		return new Stage(parentStage, stageName) {
			public void run() {
				RisksPageC risksPage = new RisksPageC(sh);
				for(String sn : creationStageNames)
					risksPage.validateExistence(risks, sn);
			}
		};
	}
	
	public static Stage createResources(Stage parentStage, String stageName, SeleniumHelper sh, Map<String, ResourceList> resources) {
		Stage stage = new Stage(parentStage, stageName);
		for(Resource r : resources.get(stage.getRootStageName()).resources)
			createResource(stage, "create resource: " + r.name, sh, r);
		runAllJobs(stage, "run all jobs after creating resources", sh);
		return stage;
	}
	
	public static Stage createResource(Stage parentStage, String stageName, SeleniumHelper sh, Resource resource) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToResources(stage, "navigate to Resources", sh);
		startResourceCreateWizard(stage, "start resource create wizard", sh);
		runResourceCreateWizardStep2(stage, "input resource type and continue wizard", sh, resource.type);
		inputResourceValuesAndSave(stage, "input resource values and finish wizard", sh, resource);
		return stage;
	}
	
	public static Stage startResourceCreateWizard(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				ResourcePageC.startCreateWiz(sh);
			}
		};
	}
	
	public static Stage runResourceCreateWizardStep2(Stage parentStage, String stageName, SeleniumHelper sh, String resourceType) {
		return new Stage(parentStage, stageName) {
			public void run() {
				ResourcePageC.createWizStep2SelectType(sh, resourceType);
			}
		};
	}
	
	public static Stage inputResourceValuesAndSave(Stage parentStage, String stageName, SeleniumHelper sh, Resource resource) {
		return new Stage(parentStage, stageName) {
			public void run() {
				ResourcePageC.createWizStep3inputValues(sh, resource);
			}
		};
	}
	
	public static Stage editResources(Stage parentStage, String stageName, SeleniumHelper sh, Map<String, ResourceList> resources) {
		Stage stage = new Stage(parentStage, stageName);
		for(Resource r : resources.get(stage.getRootStageName()).resources)
			editResource(stage, "edit resource: " + r.name, sh, r);
		runAllJobs(stage, "run all jobs after editing resources", sh);
		return stage;
	}
	
	public static Stage editResource(Stage parentStage, String stageName, SeleniumHelper sh, Resource resource) {
		Stage stage = new Stage(parentStage, stageName);
		if(sh.isLightning()){
			StagesZ.navigateFromAnywhereToListViewItem(stage, stageName, sh, "Resources", resource.name);
		}
		else{
			navigateToResources(stage, "navigate to Resources", sh);
			startResourceEdit(stage, "start resource edit", sh, resource.name);			
		}
		inputResourceValuesAndSave(stage, "input resource values and finish wizard", sh, resource);
		return stage;
	}
	
	public static Stage startResourceEdit(Stage parentStage, String stageName, SeleniumHelper sh, String resource) {
		return new Stage(parentStage, stageName) {
			public void run() {
				ResourcePageC.startEdit(sh, resource);
			}
		};
	}
	
	public static Stage createAccountsAndSalesOpportunities(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Account> accounts) {
		Stage stage = new Stage(parentStage, stageName);
		createAccounts(stage, "create accounts", sh, accounts);
		createSalesOpportunities(stage, "create sales opportunities for accounts", sh, accounts);
		return stage;
	}

	public static Stage createAccountsOpportunitiesAndProposal(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Account> accounts) {
		Stage stage = new Stage(parentStage, stageName);
		createAccounts(stage, "create accounts", sh, accounts);
		createOpportunities(stage, "create opportunities for accounts", sh, accounts);
		return stage;
	}
	
	public static Stage createAccountsAndProposal(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Account> accounts) {
		Stage stage = new Stage(parentStage, stageName);
		createAccounts(stage, "create accounts", sh, accounts);
		createProposals(stage, "create proposals for accounts", sh, accounts);
		return stage;
	}

	public static Stage createAccountsSalesOppsEngagementsAndElements(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Account> accounts) {
		Stage stage = new Stage(parentStage, stageName);
		createAccounts(stage, "create accounts", sh, accounts);
		createSalesOpportunitiesEngagementsAndElements(stage, "create sales opportunities for accounts", sh, accounts);
		return stage;
	}
	
	public static Stage createSalesOpportunities(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Account> accounts) {
		Stage stage = new Stage(parentStage, stageName);
		for(Account a : accounts)
			createSalesOpportunities(stage, "create sales opps for account " + a.name, sh, a);
		return stage;
	}
	
	public static Stage createSalesOpportunitiesEngagementsAndElements(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Account> accounts) {
		Stage stage = new Stage(parentStage, stageName);
		for(Account a : accounts)
			createSalesOpportunitiesEngagementsAndElements(stage, "create sales opps for account " + a.name, sh, a);
		return stage;
	}
	
	public static Stage createSalesOpportunities(Stage parentStage, String stageName, SeleniumHelper sh, Account account) {
		return createSalesOpportunities(parentStage, stageName, sh, account, account.salesOpportunities.toArray(new SalesOpportunity[account.salesOpportunities.size()]));
	}
	
	public static Stage createSalesOpportunities(Stage parentStage, String stageName, SeleniumHelper sh, Account account, SalesOpportunity... aSalesOpps) {
		Stage stage = new Stage(parentStage, stageName);
		for(SalesOpportunity so : aSalesOpps) {
			navigateFromAnywhereToTab(stage, "navigate to create sales opp " + so.name, sh, "Sales Opportunities");
			createSalesOpportunity(stage, "create sales opp " + so.name, sh, account.name, so);
		}
		return stage;
	}
	
	public static Stage createSalesOpportunitiesEngagementsAndElements(Stage parentStage, String stageName, SeleniumHelper sh, Account account) {
		Stage stage = new Stage(parentStage, stageName);
		for(SalesOpportunity so : account.salesOpportunities)
			createSalesOpportunitiyEngagementsAndElements(stage, "Create sales opportunity: " + so.name, sh, account, so);
		return stage;
	}
	
	public static Stage createSalesOpportunitiyEngagementsAndElements(Stage parentStage, String stageName, SeleniumHelper sh, Account account, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToTab(stage, "navigate to create sales opp " + salesOpp.name, sh, "Sales Opportunities");
		createSalesOpportunity(stage, "create sales opp " + salesOpp.name, sh, account.name, salesOpp);
		navigateAndMoveSalesOpportunityToNextStage(stage, "move sales opp to stage Develop Proposal", sh, "Develop Proposal");
		createEngagementsAndElements(stage, "create engagements and elements", sh, salesOpp.name, salesOpp.deliveryEngagements);
		return stage;
	}
	
	public static Stage createSalesOpportunitiyEngagementAndElement(Stage parentStage, String stageName, SeleniumHelper sh, Account account, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToTab(stage, "navigate to create sales opp " + salesOpp.name, sh, "Sales Opportunities");
		createSalesOpportunity(stage, "create sales opp " + salesOpp.name, sh, account.name, salesOpp);
		navigateAndMoveSalesOpportunityToNextStage(stage, "move sales opp to stage Develop Proposal", sh, "Develop Proposal");
		createEngagementsAndElements(stage, "create engagements and elements", sh, salesOpp.name, salesOpp.deliveryEngagements);
		return stage;
	}
	
	public static Stage createSalesOpportunity(Stage parentStage, String stageName, SeleniumHelper sh, String accountName, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, stageName);
		if(sh.isLightning())
			StagesZ.startSalesOpportunityCreation(stage, "click new (L)", sh);
		else
			startSalesOpportunityCreation(stage, "click new", sh);
		inputSalesOppValuesAndSave(stage, "input values and save", sh, accountName, salesOpp);
		return stage;
	}
	
	public static Stage startSalesOpportunityCreation(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.startNewSalesOppCreation(sh);
			}
		};
	}

	public static Stage inputSalesOppValuesAndSave(Stage parentStage, String stageName, SeleniumHelper sh, String acccountName, SalesOpportunity salesOpp) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.inputValuesAndSave(sh, acccountName, salesOpp);
			}
		};
	}
	
	public static Stage createAccounts(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Account> accounts) {
		Stage stage = new Stage(parentStage, stageName);
		for(Account a : accounts) {
			navigateFromAnywhereToTab(stage, "navigate to create account " + a.name, sh, "Accounts");
			closeLightningpopup(stage, "close the lightning popup", sh);
			createAccount(stage, "create account " + a.name, sh, a);
		}
		return stage;
	}
	
	public static Stage createOpportunities(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Account> accounts) {
		Stage stage = new Stage(parentStage, stageName);
		for(Account a : accounts) {
			for(Opportunity o : a.opportunities) {
				navigateFromAnywhereToTab(stage, "navigate to create opportunity" + a.name, sh, "Opportunities");
				createOpportunity(stage, "create opportunitiy " + o.name, sh, o);
			}
		}
		return stage;
	}

	public static Stage createProposals(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Account> accounts) {
		Stage stage = new Stage(parentStage, stageName);
		for(Account a : accounts) {
			for(Proposal p : a.proposals) {
				navigateFromAnywhereToTab(stage, "navigate to create proposal" + a.name, sh, "Proposals");
				createProposal(stage, "create proposal " + p.name, sh, p);
			}
		}
		return stage;
	}
	
	public static Stage createOpportunity(Stage parentStage, String stageName, SeleniumHelper sh, Opportunity Opportunity) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToOpportunities(stage, "navigate to all Opportunities", sh);
		if(sh.isLightning()){
			startNewSalesOppCreationL(stage, "Lightning: start sales opp creation", sh);
			inputOpportunityValuesAndSaveL(stage, "input Opportunity values", sh, Opportunity);
		}
		else{
			startOpportunityCreation(stage, "click new", sh, Opportunity.name);
			inputOpportunityValuesAndSave(stage, "input Opportunity values", sh, Opportunity);
		}
		return stage;
	}
	
	public static Stage startNewSalesOppCreationL(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageZ.startNewSalesOppCreation(sh);
			}
		};
	}
	
	public static Stage createProposal(Stage parentStage, String stageName, SeleniumHelper sh, Proposal proposal) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToProposals(stage, "navigate to all Proposals", sh);
		startProposalCreation(stage, "click new", sh, proposal.name);
		inputProposalValuesAndSave(stage, "input Proposal values", sh, proposal);
		return stage;
	}

	public static Stage createAltProposal(Stage parentStage, String stageName, SeleniumHelper sh, Proposal altProposal, String originalProposal) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToProposal(stage, "Navigate to Proposal: " + originalProposal, sh, originalProposal);
		createAltProposalAndSave(stage, "Create Alternate Proposal", sh, altProposal);
		return stage;
	}

	public static Stage switchAltProposalToPreferred(Stage parentStage, String stageName, SeleniumHelper sh, String altProposalName) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToProposal(stage, "Navigate to Proposal: " + altProposalName, sh, altProposalName);
		switchAltPropToPref(stage, "Switching Alternate Proposal to Preferred", sh, altProposalName);
		return stage;
	}
	
	public static Stage navigateToOpportunities(Stage parentStage, String stageName, SeleniumHelper sh) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToTab(stage, "navigate to Opportunities tab", sh, "Opportunities");
		clickGoInListView(stage, "show all Opportunities", sh);
		return stage;
	}
	
	public static Stage navigateToProposals(Stage parentStage, String stageName, SeleniumHelper sh) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToTab(stage, "navigate to Proposals tab", sh, "Proposals");
		clickGoInListView(stage, "show all Proposals", sh);
		return stage;
	}
	
	public static Stage startOpportunityCreation(Stage parentStage, String stageName, SeleniumHelper sh, String OpportunityName) {
		return new Stage(parentStage, stageName) {
			public void run() {
				OpportunityPageC.startNewOpportunityCreation(sh);
			}
		};
	}
	
	public static Stage startProposalCreation(Stage parentStage, String stageName, SeleniumHelper sh, String ProposalName) {
		return new Stage(parentStage, stageName) {
			public void run() {
				ProposalEditPageC.startNewProposalCreation(sh);
			}
		};
	}
	
	public static Stage inputProposalValuesAndSave(Stage parentStage, String stageName, SeleniumHelper sh, Proposal proposal) {
		return new Stage(parentStage, stageName) {
			public void run() {
				ProposalEditPageC.inputValuesAndSave(sh, proposal);
			}
		};
	}

	public static Stage createAltProposalAndSave(Stage parentStage, String stageName, SeleniumHelper sh, Proposal altProposal) {
		return new Stage(parentStage, stageName) {
			public void run() {
				ProposalEditPageC.altProposalCreation(sh, altProposal);
			}
		};
	}	

	public static Stage switchAltPropToPref(Stage parentStage, String stageName, SeleniumHelper sh, String altProposalName) {
		return new Stage(parentStage, stageName) {
			public void run() {
				ProposalEditPageC.altPropToPref(sh, altProposalName);
			}
		};
	}
	
	public static Stage inputOpportunityValuesAndSave(Stage parentStage, String stageName, SeleniumHelper sh, Opportunity Opportunity) {
		return new Stage(parentStage, stageName) {
			public void run() {
				OpportunityPageC.inputValuesAndSave(sh, Opportunity);
			}
		};
	}
	
	public static Stage inputOpportunityValuesAndSaveL(Stage parentStage, String stageName, SeleniumHelper sh, Opportunity Opportunity) {
		return new Stage(parentStage, stageName) {
			public void run() {
				OpportunityZ.inputValuesAndSave(sh, Opportunity);
			}
		};
	}
	
	public static Stage createAccount(Stage parentStage, String stageName, SeleniumHelper sh, Account account) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToAccounts(stage, "navigate to all accounts", sh);

		if(sh.isLightning()) {
			StagesZ.createAccount(stage, "create the account", sh, account);
		}
		else {
			startAccountCreation(stage, "click new", sh, account.name);
			inputAccountValuesAndSave(stage, "input account values", sh, account);
		}
		return stage;
	}
	
	public static Stage navigateToAccounts(Stage parentStage, String stageName, SeleniumHelper sh) {
		Stage stage = new Stage(parentStage, stageName);
		if(sh.isLightning()) {
			StagesZ.navigateFromAnywhereToTab(stage, "navigate to accounts tab", sh, "Accounts");
		}
		else {
			Stages.navigateFromAnywhereToTab(stage, "navigate to accounts tab", sh, "Accounts");
			Stages.clickGoInListView(stage, "show all accounts", sh);
		}
		return stage;
	}
	
	
	public static Stage startAccountCreation(Stage parentStage, String stageName, SeleniumHelper sh, String accountName) {
		return new Stage(parentStage, stageName) {
			public void run() {
				AccountPageC.startNewAccountCreationIfDoesntExist(sh, accountName);
			}
		};
	}
	
	public static Stage inputAccountValuesAndSave(Stage parentStage, String stageName, SeleniumHelper sh, Account account) {
		return new Stage(parentStage, stageName) {
			public void run() {
				AccountPageC.inputValuesAndSave(sh, account);
				AccountPageC.inputInvoicingDetails(sh, account);
				AccountPageC.inputCoreAccountDetails(sh, account);
				
			}
		};
	}
	
	public static Stage changeStartDateOfDeliveryEngagement(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp, DeliveryEngagement engagement){
		Stage stage = new Stage(parentStage, stageName);
//		navigateToAssignmentBulkEditViaCharm(stage, "navigate to activity assignment bulk edit via charm", sh);
		navigateToEngagementDashboardViaSalesOpp(stage, "nav to engagemenmt", sh, salesOpp.name, engagement.name);
		changeStartDateOfDeliveryEngagement(stage, "change the start date of engagement: " + engagement.name, sh, engagement.name);
		return stage;
	}
	
	
	public static Stage navigateToAssignmentBulkEditViaCharm(Stage parentStage, String stageName, SeleniumHelper sh){
		return new Stage(parentStage, stageName) {
			public void run() {
				// there should be a proposal level charm which indicates the Detailed level start date is out of step 
				// with the close date which when clicked launches the bulk edit wizard
				sh.clickMiniCharmByPageName(PagesC.ACTIVITYASSIGNMENTSBULKEDIT);
			}
		};
	}
		
	public static Stage changeStartDateOfDeliveryEngagement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName){
		return new Stage(parentStage, stageName) {
			public void run() {
				// we should now be on the bulk edit with a newly defaulted date
				// where we can push back the start date of the assignments		
				ActivityAssignmentBulkEditPageC bulkEditHandler = new ActivityAssignmentBulkEditPageC(sh);
				bulkEditHandler.CompleteWizardWithDefaults(engagementName);
			}
		};
	}
	
	
	public static Stage createChangeElementAndAssignmentsStage(Stage parentStage, String stageName, SeleniumHelper sh, Collection<Account> accounts, boolean activate) {
		Stage stage = new Stage(parentStage, stageName);
		for (Account acc : accounts)
			createChangeElementAndAssignmentsStage(stage, "create change elements and assignments for account: " + acc.name, sh, acc, activate);
		return stage;
	}

	public static Stage createChangeElementAndAssignmentsStage(Stage parentStage, String stageName, SeleniumHelper sh, Account account, boolean activate) {
		Stage stage = new Stage(parentStage, stageName);
		
		for (ChangeControl control : account.changeControls) {
			createDeliveryEngagementChangeElementsAndAssignments(stage, "create element change elements and assignments for change control: " + control.name, sh, control);
			
			for(DeliveryEngagement engagement : control.deliveryEngagements) {
				configureElements(stage, "configure elements", sh, engagement.deliveryElements);
				if(activate)
					activateElements(stage, "activate elements", sh, engagement.deliveryElements);
			}		
		}
		return stage;
	}
	
	public static Stage createDeliveryEngagementChangeElementsAndAssignments(Stage parentStage, String stageName, SeleniumHelper sh, ChangeControl control) {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryEngagement engagement : control.deliveryEngagements) {
			navigateToEngagementDashboardByName(stage, "navigate to engagemet: " + engagement.name, sh, engagement.name);
			navigateMenu(stage, "navigate to revenue and costs", sh, PagesC.DELIVERYGROUPREVENUESANDCOSTS);
			createChangeElements(stage, "create change elements", sh, engagement.name, engagement.deliveryElements);
		}
		return stage;
	}
	
	public static Stage createChangeElements(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, Collection<DeliveryElement> elements) {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryElement element : elements)
			createChangeElement(stage, "create change element for delivery element: " + element.name, sh, engagementName, element);
		return stage;
	}
	
	public static Stage createChangeElement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, DeliveryElement element) {
		return new Stage(parentStage, stageName) {
			public void run() {
				DeliveryElementPageC elementHandler = new DeliveryElementPageC(sh);
				elementHandler.createChangeElement(engagementName, element);
			}
		};
	}
	
	public static Stage navigateAndMoveSalesOpportunityToNextStage(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppStage) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToMoveSalesOpportunityToNextStage(stage, "navigate to move sales opp to next stage", sh);
		moveSalesOpportunityToNextStage(stage, "move sales opp to next stage", sh, salesOppStage);
		return stage;
	}
	
	public static Stage navigateToMoveSalesOpportunityToNextStage(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.navigateToMoveSalesOpportunityToNextStage(sh);
			}
		};
	}
	
	public static Stage moveSalesOpportunityToNextStage(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppStage) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.moveSalesOpportunityToNextStage(sh, salesOppStage);
			}
		};
	}
	
	public static Stage addElementsToExistingEngagement(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName);
		startAddingElementsToExistingEngagement(stage, "start adding new elements to the existing engagement: " + engagement.name, sh, engagement.name);
		addTheElementsToTheExistingEngagement(stage, "add the elements in the wizard", sh, engagement.deliveryElements);
		sleep(stage, "!This shouldn't be necessary! - Wait for jobs to get seeded", sh, 4000);
		finishAddingElementsToExistingEngagement(stage, "finish adding new elements to the existing engagement: " + engagement.name, sh, engagement.name);
		navigateToEngagementFromProposalItems(stage, "navigate to the engagement: " + engagement.name, sh, engagement.name);
		configureElements(stage, "configure the elements", sh, engagement.deliveryElements);
		return stage;
	}
	
	public static Stage startAddingElementsToExistingEngagement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.startAddingElementsToExistingEngagement(sh, engagementName);
			}
		};
	}
	
	public static Stage addTheElementsToTheExistingEngagement(Stage parentStage, String stageName, SeleniumHelper sh, Collection<DeliveryElement> elements) {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryElement e : elements) 
			addAnElementToTheExistingEngagement(stage, "add the element: " + e.name, sh, e);
		return stage;
	}
	
	public static Stage addAnElementToTheExistingEngagement(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.addElementInWizard(sh, element);
			}
		};
	}
	
	public static Stage finishAddingElementsToExistingEngagement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName) {
		return new Stage(parentStage, stageName) {
			public void run() {
				SalesOpportunityPageC.finishAddingElementsToExistingEngagement(sh, engagementName);
			}
		};
	}
	
	public static Stage createEngagementsAndElements(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName, Collection<DeliveryEngagement> engagements) {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryEngagement de : engagements) {
			if(de.stage != null && !de.stage.equals(stage.getRootStageName()))
				continue;
			navigateFromAnywhereToSalesOpp(stage, "navigate to sales opportunity: " + salesOppName, sh, salesOppName);
			navigateMenu(stage, "navigate to scope", sh, PagesC.PROPOSALITEMS);
			if(de.isExisting)
				addElementsToExistingEngagement(stage, "add elements to existing engagement: " + de.name, sh, de);
			else
				createEngagementAndElements(stage, "create engagement: " + de.name, sh, de);
		}
		return stage;
	}
	
	public static Stage createEngagementAndConfigureElements(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName) {
			public void run() {
				DeliveryEngagementPageC.CreateNew(sh, engagement);
			}
		};
		configureEngagementElements(stage, "configure elements for engagement: " + engagement.name, sh, engagement);
		return stage;
	}
	
	public static Stage createEngagementAndElements(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName) {
			public void run() {
				DeliveryEngagementPageC.CreateNewWithElements(sh, engagement);
			}
		};
		configureEngagementElements(stage, "configure elements for engagement: " + engagement.name, sh, engagement);
		return stage;
	}
	
	public static Stage configureEngagementElements(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName);
		if(engagement.deliveryElements != null) {
			navigateToEngagementDashboardByName(stage, "navigate to engagement", sh, engagement.name);
			configureElements(stage, "configure elements", sh, engagement.deliveryElements);
			return stage;
		}
		return doNothing(parentStage, "Engagement: " + engagement.name + " has no elements");
	}
	
	public static Stage configureElements(Stage parentStage, String stageName, SeleniumHelper sh, Collection<DeliveryElement> elements) {
		Stage stage = new Stage(parentStage, stageName);
		switchToClassicFrame(stage, "switch to classic frame", sh);
		for(DeliveryElement e : elements)
			configureElement(stage, "configure element: " + e.name, sh, e);
		return stage;
	}
	
	public static Stage configureElement(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, stageName);
		createAssignments(stage, "create assignments", sh, element);
		createAnnuities(stage, "create annuities", sh, element.annuities);
		createRevenueMilestones(stage, "create revenue milestones", sh, element);
		createCostMilestones(stage, "create cost milestones", sh, element);
		createAccountCredits(stage, "create account credits", sh, element);
		return stage;
	}
	
	public static Stage createTasksAndTaskAssignmentsStage(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) throws ParseException {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryEngagement e : salesOpp.deliveryEngagements)
			createTasksAndTaskAssignments(stage, "create task and task assignments for sales opportunity " + salesOpp.name, sh, salesOpp.name, e);
		return stage;
	}
	
	public static Stage createTasksAndTaskAssignments(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName, Collection<DeliveryEngagement> engagements) throws ParseException {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryEngagement e : engagements)
			createTasksAndTaskAssignments(stage, "create task and task assignments for engagement " + e.name, sh, salesOppName, e);
		return stage;
	}
	
	public static Stage createTasksAndTaskAssignments(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) throws ParseException {
		Stage stage = new Stage(parentStage, stageName);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to the engagement", sh, salesOppName, engagement.name);
		createTasksAndTaskAssignments(stage, "create tasks and task assignments for elements", sh, engagement.deliveryElements);
		return stage;
	}
	
	public static Stage createTasksAndTaskAssignments(Stage parentStage, String stageName, SeleniumHelper sh, Collection<DeliveryElement> elements) throws ParseException {
		Stage stage = new Stage(parentStage, stageName);
		for(DeliveryElement e : elements)
			createTasksAndTaskAssignments(stage, "create tasks and task assignments for element " + e.name, sh, e);
		return stage;
	}
	
	public static Stage createTasksAndTaskAssignments(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element) throws ParseException {
		Stage stage = new Stage(parentStage, stageName);
		navigateToTrackingPlanGant(stage, "navigate to tracking plan gant", sh);
		createTasksAndTaskAssignments(stage, "create tasks and task assignments", sh, element.tasks);
		return stage;
	}
	
	public static Stage createTasksAndTaskAssignments(Stage parentStage, String stageName, SeleniumHelper sh, List<Task> tasks) {
		Stage stage = new Stage(parentStage, stageName);
		for(Task t : tasks) {
			if(t.testStage == null || t.testStage.equals(stage.getRootStageName())) {
				createTask(stage, "create task: " + t.name, sh, t);
				if(t.taskAssignments != null)
					createTaskAssignments(stage, "create task assignments for task: " + t.name, sh, t);
			}
		}
		return stage;
	}
	
	public static Stage createTask(Stage parentStage, String stageName, SeleniumHelper sh, Task task) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new TrackingPlanTasksPageC(sh).CreateNew(task);
			}
		};
	}

	public static Stage createTaskAssignments(Stage parentStage, String stageName, SeleniumHelper sh, Task task) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new TrackingPlanTasksPageC(sh).AssignTask(task, task.taskAssignments);
			}
		};
	}
	
	public static Stage updateTaskEstimatesStage(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, stageName);
		for (DeliveryEngagement e : salesOpp.deliveryEngagements) {
			navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagemet: " + e.name, sh, salesOpp.name, e.name);
			updateTaskEstimates(stage, "update task estimates for engagement: " + e.name, sh, e);
		}
		return stage;
	}
	
	public static Stage updateTaskEstimates(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName);
		for (DeliveryElement e : engagement.deliveryElements) {
			navigateToTrackingPlanGant(stage, "navigate to tracking plan gant", sh);
			updateTaskEstimates(stage, "update task estimates for element: " + e.name, sh, e);
		}
		return stage;
	}
	
	public static Stage updateTaskEstimates(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, stageName);
		for (Task task : element.tasks)
			if(task.testStage != null && task.testStage.equals(stage.getRootStageName()) && task.taskAssignments != null)
				updateTaskEstimates(stage, "update estimates for task: " + task.name, sh, task);
		return stage;
	}
	
	public static Stage updateTaskEstimates(Stage parentStage, String stageName, SeleniumHelper sh, Task task) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new TrackingPlanTasksPageC(sh).UpdateTaskEstimates(task, task.taskAssignments);
			}
		};
	}
	
	public static Stage closeTrackingPlanStage(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp, boolean expectToClose) {
		Stage stage = new Stage(parentStage, stageName);
		for (DeliveryEngagement e : salesOpp.deliveryEngagements) {
			navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagemet: " + e.name, sh, salesOpp.name, e.name);
			closeTrackingPlan(stage, "close tracking plan for engagement: " + e.name, sh, e, expectToClose);
		}
		return stage;
	}
	
	public static Stage closeTrackingPlan(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement, boolean expectToClose) {
		Stage stage = new Stage(parentStage, stageName);
		for (DeliveryElement e : engagement.deliveryElements) {
			navigateToPlanDashboard(stage, "navigate to plan dashboard", sh);
			closeTrackingPlan(stage, "close tracking plan for element: " + e.name, sh, e, expectToClose);
		}
		return stage;
	}
	
	public static Stage closeTrackingPlan(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element, boolean expectToClose) {
		return new Stage(parentStage, stageName) {
			public void run() {
				TrackingPlanTasksPageC page = new TrackingPlanTasksPageC(sh);
				if(expectToClose)
					page.CloseTrackingPlan();	
				else
					page.AttemptToCloseTrackingPlan();	
			}
		};
	}
	
	public static Stage navigateToTrackingPlanGant(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new DeliveryElementPageC(sh).NavigateToPlanGantt();
			}
		};
	}
	
	public static Stage navigateToPlanDashboard(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() {
				new DeliveryElementPageC(sh).NavigateToPlanDashboard();
			}
		};
	}
	
	public static Stage editResourcedActivities(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement: " + engagement.name, sh, salesOppName, engagement.name);
		navigateMenu(stage, "navigate to delivery group activities", sh, PagesC.DELIVERYGROUPACTIVITIES);
		for(ResourcedActivity a : engagement.activities)
			editResourcedActivity(stage, "edit activity: " + a.name, sh, a);
		return stage;
	}

	public static Stage editResourcedActivity(Stage parentStage, String stageName, SeleniumHelper sh, ResourcedActivity activity) {
		return new Stage(parentStage, stageName){
			public void run() {
				DeliveryGroupActivitiesPageC.editActivity(sh, activity);
			}
		};
	}

	public static Stage getElementId(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryElement element) {
		return new Stage(parentStage, stageName) {
			public void run() {
				element.id = DeliveryEngagementPageC.getElementId(sh, element.name);
			}
		};
	}
	
	public static Stage updateProductStage(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, stageName);
		for (DeliveryEngagement e : salesOpp.deliveryEngagements) {
			navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement: " + e.name,  sh, salesOpp.name, e.name);
			updateProduct(stage, "update product for engagement: " + e.name, sh, e);
		}
		return stage;
	}	

	public static Stage updateProduct(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToRevenueAndCosts(stage, "navigate to revenue and costs", sh);
		for(DeliveryElement e : engagement.deliveryElements)
			updateProduct(stage, "update product and name for element: " + e.name, sh, engagement.name, e);
		return stage;
	}
		
	public static Stage updateProduct(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName, DeliveryElement element) {
		return new Stage(parentStage, stageName) {
			public void run() {
				DeliveryElementPageC page = new DeliveryElementPageC(sh);
				sh.waitForLightningSpinnerToBeHidden();
				page.EditExisting(engagementName, element.name);
				// switch the delivery element product and name (edit the underlying KimbleTestData object to replace the name
				// so that subsequent checks for the delivery element name pick up the new name
				element.name = element.step2Name;
				
				page.UpdateProductAndName(element.step2Product, element.step2Name);			
			}
		};
	}
		
	public static Stage confirmWonEnagagementsStage(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		for (DeliveryEngagement e : salesOpp.deliveryEngagements)
			confirmWonProductsAndServices(stage, "confirm won products and services for engagement: " + e.name, sh, salesOpp.name, e);
		return stage;
	}
	
	public static Stage confirmWonProductsAndServices(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateFromAnywhereToSalesOpp(stage, "navigate to sales opportunity: " + salesOppName, sh, salesOppName);
		navigateToConfirmWonProductsAndServices(stage, "navigate to confirm won products and services", sh);
		confirmWonProductsAndServices(stage, "confirm won products and services", sh, engagement);
		return stage;
	}
	
	public static Stage navigateToConfirmWonProductsAndServices(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				SalesOpportunityPageC.NavigateToConfirmWonProductsAndServices(sh);
			}
		};
	}
	
	public static Stage confirmWonProductsAndServices(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement) {
		return new Stage(parentStage, testStage) {
			public void run() {
				DeliveryEngagementPageC.ConfirmWonProductsAndServices(sh, engagement);
			}
		};
	}
	
	public static Stage navigateAndWinOpportunity(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		navigateFromAnywhereToSalesOpp(stage, "navigate to the opportunity", sh, salesOpp.name);
		winOpportunity(stage, "win the opportunity", sh, salesOpp.winDate);
		return stage;
	}
	
	public static Stage winOpportunity(Stage parentStage, String testStage, SeleniumHelper sh, String winDate) {
		return new Stage(parentStage, testStage) {
			public void run() {
				SalesOpportunityPageC.winOpportunity(sh, winDate);
			}
		};
	}
	
	public static Stage navigateAndLoseOpportunity(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		navigateFromAnywhereToSalesOppLose(stage, "navigate to lose oppotunity", sh, salesOpp.name);
		loseOpportunity(stage, "lose the opportunity", sh, salesOpp);
		return stage;
	}

	public static Stage loseOpportunity(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		return new Stage(parentStage, testStage) {
			public void run() {
				SalesOpportunityPageC.loseSalesOpportunity(sh, salesOpp);
			}
		};
	}

	public static Stage createMilestonesForDeliveryElements(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement: " + engagement.name, sh, salesOppName, engagement.name);
		for (DeliveryElement e : engagement.deliveryElements) {
			createRevenueMilestones(stage, "create revenue milestones for element: " + e.name, sh, e);
			createCostMilestones(stage, "create cost milestones for element: " + e.name, sh, e);
		}
		return stage;
	}

	public static Stage createRevenueMilestones(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		if(!hasMilestonesToCreate(element.revenueMilestones, stage.getRootStageName()))
			return doNothing(stage, "no revenue milestones");
		navigateToDeliveryGroupMilestones(stage, "navigate to milestones", sh);
		for(Milestone m : element.revenueMilestones)
			if(m.creationStage == null || m.creationStage.equals(stage.getRootStageName()))
				createRevenueMilestone(stage, "create milestone: " + m.name, sh, element.parentElementName, element.name, m);
		return stage;
	}
	
	public static Stage createCostMilestones(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		if(!hasMilestonesToCreate(element.costMilestones, stage.getRootStageName()))
			return doNothing(stage, "no cost milestones");
		navigateToDeliveryGroupMilestones(stage, "navigate to milestones", sh);
		for(Milestone m : element.costMilestones)
			if(m.creationStage == null || m.creationStage.equals(stage.getRootStageName()))
				createCostMilestone(stage, "create milestone: " + m.name, sh, element.name, m);
		return stage;
	}
	
	public static Stage createRevenueMilestone(Stage parentStage, String testStage, SeleniumHelper sh, String parentElementName, String elementName, Milestone milestone) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new DeliveryGroupMilestonesPageC(sh).CreateNewRevenueMilestone(parentElementName, elementName, milestone);
			}
		};
	}
	
	public static Stage createCostMilestone(Stage parentStage, String testStage, SeleniumHelper sh, String elementName, Milestone milestone) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new DeliveryGroupMilestonesPageC(sh).CreateNewCostMilestone(elementName, milestone);
			}
		};
	}
	
	public static Stage createAnnuities(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Annuity> annuities) {
		Stage stage = new Stage(parentStage, testStage);
		if(annuities == null)
			return doNothing(stage, "no annuities");
		navigateToAnnuities(stage, "navigate to annuities", sh);
		for(Annuity a : annuities)
			createAnnuity(stage, "create annuity: " + a.name, sh, a);
		return stage;
	}
	
	public static Stage createAnnuity(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new AnnuityPageC(sh).CreateNew(sh, annuity);
			}
		};
	}
	
	public static Stage extendAnnuity(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new AnnuityPageC(sh).extendAnnuity(sh, annuity);
			}
		};
	}
	
	public static Stage extendAnnuities(Stage parentStage, String testStage, SeleniumHelper sh, Annuity a) {
		Stage stage = new Stage(parentStage, testStage);
			navigateToAnnuities(stage, "navigate to annuities", sh);
			extendAnnuity(stage, "extend annuity: " + a.name, sh, a);
		return stage;
	}

	public static Stage completeAnnuityPeriods(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to the dashboard for engagement: " + engagement.name, sh, salesOppName, engagement.name);
		completeAnnuityPeriods(stage, "complete annuity periods for engagement: " + engagement.name, sh, engagement.deliveryElements);
		return stage;
	}

	public static Stage navigateToEngagementDashboardThroughSalesOpp(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
			navigateToEngagementDashboardViaSalesOpp(stage, "navigate to the dashboard for engagement: " + engagement.name, sh, salesOppName, engagement.name);
		return stage;
	}
	
	public static Stage completeAnnuityPeriods(Stage parentStage, String testStage, SeleniumHelper sh, List<DeliveryElement> elements) {
		Stage stage = new Stage(parentStage, testStage);
		for(DeliveryElement e : elements)
			completeAnnuityPeriods(stage, "complete annuity periods for element: " + e.name, sh, e.annuities);
		return stage;
	}
	
	public static Stage completeAnnuityPeriods(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Annuity> annuities) {
		Stage stage = new Stage(parentStage, testStage);
		if(annuities == null)
			return doNothing(stage, "no annuities");
		navigateToAnnuities(stage, "navigate to annuities", sh);
		for(Annuity a : annuities) {
			getAnnuityId(stage, "get the id for annuity: " + a.name, sh, a);
			completeAnnuityPeriods(stage, "complete periods for annuity: " + a.name, sh, a);
		}
		return stage;
	}
	
	public static Stage getAnnuityId(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity) {
		return new Stage(parentStage, testStage) {
			public void run() {
				annuity.id = new AnnuityPageC(sh).getAnnuityId(annuity.name);
			}
		};
	}
	
	public static Stage completeAnnuityPeriods(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity) {
		if(annuity.periodsToComplete == null)
			return doNothing(parentStage, "no periods to complete");
		Stage stage = new Stage(parentStage, testStage);
		for(int i : annuity.periodsToComplete) {
			completeRevenueAnnuityPeriod(stage, "complete period: " + i + " for revenue annuity: " + annuity.name, sh, annuity, i);
			completeCostAnnuityPeriod(stage, "complete period: " + i + " for cost annuity: " + annuity.name, sh, annuity, i);
		}
		return stage;
	}
	
	public static Stage completeRevenueAnnuityPeriod(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity, int periodIndex) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new AnnuityPageC(sh).completeRevenueAnnuityPeriodById(annuity.id, periodIndex);;
			}
		};
	}
	
	public static Stage completeCostAnnuityPeriod(Stage parentStage, String testStage, SeleniumHelper sh, Annuity annuity, int periodIndex) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new AnnuityPageC(sh).completeCostAnnuityPeriodById(annuity.id, periodIndex);;
			}
		};
	}
	
	public static Stage createOtherActivityAssignmentsStage(Stage parentStage, String testStage, SeleniumHelper sh, List<OtherResourcedActivity> activities) {
		Stage stage = new Stage(parentStage, testStage);
		for(OtherResourcedActivity a : activities)
			createOtherActivityAssignments(stage, "create assignments for other activity: " + a.name, sh, a);
		return stage;
	}
	
	public static Stage createOtherActivityAssignments(Stage parentStage, String testStage, SeleniumHelper sh, OtherResourcedActivity activity)
	{				
		// other activities can be assigned through activity assignments or via group assignment templates
		// if we have group assignment templates we make an assumption in the automation tests that
		// there are therefore no direct assignments and vice-versa
		Stage stage = new Stage(parentStage, testStage);
		createAndActivateGroupAssignmentTemplates(stage, "create and activate group assignment templates", sh, activity);
		createAssignments(parentStage, testStage, sh, activity);
		runAllJobs(stage, "run all jobs", sh);
		return stage;
	}
	
	public static Stage createAndActivateGroupAssignmentTemplates(Stage parentStage, String testStage, SeleniumHelper sh, OtherResourcedActivity activity) {
		if(activity.groupAssignmentTemplates == null)
			return doNothing(parentStage, "no other resourced activity templates");
		Stage stage = new Stage(parentStage, testStage);
		navigateToGroupAssignmentByName(stage, "navigate to other activity: " + activity.name, sh, activity.name);
		navigateMenu(stage, "navigate group assignment templates menu", sh, PagesC.ACTIVITYGROUPMEMBERTEMPLATES);
		
		for(GroupAssignmentTemplate a : activity.groupAssignmentTemplates)
			createAndActivateGroupAssignmentTemplate(stage, "create and activate group assignment template: " + a.templateName, sh, a);
		return stage;
	}
	
	public static Stage createAndActivateGroupAssignmentTemplate(Stage parentStage, String testStage, SeleniumHelper sh, GroupAssignmentTemplate assignment) {
		Stage stage = new Stage(parentStage, testStage);
		removeExistingGroupAssignment(stage, "remove existing group assignment", sh, assignment);
		createNewGroupAssignment(stage, "create group assignment", sh, assignment);
		activateGroupAssignment(stage, "activate group assignment", sh, assignment);
		return stage;
	}
	
	public static Stage navigateToGroupAssignmentByName(Stage parentStage, String testStage, SeleniumHelper sh, String activityName) {
		Stage stage = new Stage(parentStage, testStage);
		if(sh.isLightning()){
			StagesZ.navigateFromAnywhereToListViewItem(stage, "lightning: navigate to assignment", sh, "Other Activities", activityName);
		}
		else{
			navigateToOtherActivitiesList(stage, "navigate to group assignment list", sh);
			navigateToOtherActivityFromList(stage, "navigate to group assignment from list", sh, activityName);			
		}

		return stage;
	}
	
	public static Stage navigateToOtherActivitiesList(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			new OtherResourcedActivityPageC(sh).NavigateToList();
		}};
	}
	
	public static Stage navigateToOtherActivityFromList(Stage parentStage, String testStage, SeleniumHelper sh, String activityName) {
		return new Stage(parentStage, testStage) { public void run() {
			new OtherResourcedActivityPageC(sh).LoadExistingFromList(activityName);
		}};
	}
	
	public static Stage removeExistingGroupAssignment(Stage parentStage, String testStage, SeleniumHelper sh, GroupAssignmentTemplate template) {
		return new Stage(parentStage, testStage) { public void run() {
			GroupAssignmentTemplatePageC page = new GroupAssignmentTemplatePageC(sh);
			if(!page.validateExistingAndActive(template.templateName)) {
				page.deactivateExisting(template.templateName);
				page.deleteExisting(template.templateName);
			}
		}};
	}
	
	public static Stage createNewGroupAssignment(Stage parentStage, String testStage, SeleniumHelper sh, GroupAssignmentTemplate template) {
		return new Stage(parentStage, testStage) { public void run() {
			GroupAssignmentTemplatePageC page = new GroupAssignmentTemplatePageC(sh);
			if(!page.validateExistingAndActive(template.templateName))
				page.CreateNew(template);
		}};
	}
	
	public static Stage activateGroupAssignment(Stage parentStage, String testStage, SeleniumHelper sh, GroupAssignmentTemplate template) {
		return new Stage(parentStage, testStage) { public void run() {
			GroupAssignmentTemplatePageC page = new GroupAssignmentTemplatePageC(sh);
			if(!page.validateExistingAndActive(template.templateName))
				page.viewAndActivate(template.templateName);
		}};
	}
	
	public static Stage createAssignments(Stage parentStage, String testStage, SeleniumHelper sh, OtherResourcedActivity activity) {
		Stage stage = new Stage(parentStage, testStage);
		if(activity.activityAssignments == null)
			return doNothing(stage, "no assignments");
		navigateToGroupAssignmentByName(stage, "navigate to other activity: " + activity.name, sh, activity.name);
		navigateToOtherActivityAssignments(stage, "navigate to add assignments", sh);
		for(ActivityAssignment aa : activity.activityAssignments)
			createAssignment(stage, "create assignment for resource: " + aa.resourceName, sh, activity, aa);
		return stage;
	}
	
	public static Stage createAssignments(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		if(element.activityAssignments == null)
			return doNothing(stage, "no assignments");
		navigateToAssignments(stage, "navigate to assignments", sh);
		for(ActivityAssignment aa : element.activityAssignments)
			createAssignment(stage, "create assignment for resource: " + aa.resourceName, sh, element, aa);
		return stage;
	}
	
	public static Stage createAccountCredits(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		if(element.accountCredits == null)
			return doNothing(stage, "no account Credits");
		navigateToCredits(stage, "navigate to credits", sh);
		for(AccountCredits ac : element.accountCredits)
			createCredit(stage, "create credit: " + ac.creditName, sh, ac);
		return stage;
	}
	
	public static Stage createCredit(Stage parentStage, String testStage, SeleniumHelper sh, AccountCredits ac) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new CreditPageC(sh).enterCreditDetails(sh, ac);
			}
		};
	}
		
	public static Stage createAssignment(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element, ActivityAssignment assignment) {
		// default to creating the assignment against the element
		String activityName = element.name;
		// but if this is a CR then we want to create the assignment against the parent only if operated with primary
		if(element.operatedWithPrimary) activityName = element.parentElementName;
		return createAssignment(parentStage, testStage, sh, activityName, assignment);
	}
	
	public static Stage createAssignment(Stage parentStage, String testStage, SeleniumHelper sh, String activityName, ActivityAssignment assignment) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new ActivityAssignmentPageC(sh).CreateNew(activityName, assignment);
			}
		};
	}
	
	public static Stage createAssignment(Stage parentStage, String testStage, SeleniumHelper sh, OtherResourcedActivity activity, ActivityAssignment assignment) {
		return new Stage(parentStage, testStage) {
			public void run() {
				ActivityAssignmentPageC page = new ActivityAssignmentPageC(sh);
				if(page.doesAssignmentExist(assignment.resourceName, assignment.startDate))
					return;
				page.CreateNew(activity.name, assignment);
				page.VerifyAssignmentExists(assignment.resourceName, assignment.startDate);
			}
		};
	}
	
	public static Stage createBidTeamAssignmentsStage(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		if(salesOpp.bidTeamActivityAssignments == null)
			return doNothing(stage, "no bid team assignments");
		navigateToBidTeamAssignments(stage, "navigate to bid team assignments", sh);
		for(ActivityAssignment aa : salesOpp.bidTeamActivityAssignments)
			createAssignment(stage, "create assignment for resource: " + aa.resourceName, sh, salesOpp.name, aa);
		return stage;
	}
	
	public static Stage navigateToBidTeamAssignments(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				SalesOpportunityPageC.NavigateToAssignBidTeam(sh);
			}
		};
	}

	public static Stage navigateToDeliveryGroupMilestones(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				sh.clickMenuItem(PagesC.DELIVERYGROUPMILESTONES);
				sh.waitForLightningSpinnerToBeHidden();
				sh.waitForElementToBeClickable(By.cssSelector("#element-menu"));
			}
		};
	}
	
	public static Stage navigateToAnnuities(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				DeliveryEngagementPageC.navigateToAnnuities(sh);
			}
		};
	}
	
	public static Stage navigateToAssignments(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				DeliveryEngagementPageC.NavigateToAssignments(sh, true);
			}
		};
	}
	
	public static Stage navigateToCredits(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				CreditPageC.NavigateToCredits(sh);
			}
		};
	}
	
	public static Stage navigateToManyAssignments(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				DeliveryEngagementPageC.NavigateToManyAssignments(sh);
			}
		};
	}
	
	public static Stage navigateToOtherActivityAssignments(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				OtherResourcedActivityPageC.NavigateToAssignments(sh);
			}
		};
	}
	
	public static Stage navigateToMilestones(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				DeliveryEngagementPageC.NavigateToDeliveryGroupMilestones(sh);
			}
		};
	}
	
	public static Stage switchToClassicFrame(Stage parentStage, String testStage, SeleniumHelper sh) {
		if(sh.isLightning())
			return StagesZ.switchToClassicFrame(parentStage, testStage, sh);
		else
			return doNothing(parentStage, testStage);
	}
	
	public static Stage navigateToEngagementDashboardByName(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName) {
		if(sh.isLightning())
			return StagesZ.navigateFromAnywhereToListViewItem(parentStage, testStage, sh, "Delivery Engagements", engagementName);
		else
			return navigateToEngagementDashboardByNameC(parentStage, testStage, sh, engagementName);
	}
	
	public static Stage navigateToEngagementDashboardByNameC(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName) {
		return new Stage(parentStage, testStage) {
			public void run() {
				DeliveryEngagementPageC.LoadExistingByName(sh, engagementName);
			}
		};
	}
	
	public static Stage navigateToBusinessUnitByNameandGenerateInvoice(Stage parentStage, String testStage, SeleniumHelper sh, String buName, String invoicingBU, String invoicingAccount ) {
		return new Stage(parentStage, testStage) {
			public void run() {
				BusinessUnitPageC.LoadExistingByName(sh,buName);
				BusinessUnitPageC.selectGenerateInvoice(sh, buName, invoicingBU, invoicingAccount);
			}
		};
	}
	
	public static Stage navigateToBusinessUnitByName(Stage parentStage, String testStage, SeleniumHelper sh, String bu) {
		return new Stage(parentStage, testStage) {
			public void run() {
				BusinessUnitPageC.LoadExistingByName(sh, bu);
			}
		};
	}
	
	public static Stage enterBusinessUnitInternalAccount(Stage parentStage, String testStage, SeleniumHelper sh, String accountname) {
		return new Stage(parentStage, testStage) {
			public void run() {
				BusinessUnitPageC.setInternalAccount(sh, accountname);
			}
		};
	}
		
	public static Stage navigateToEngagementDashboardViaSalesOpp(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, String engagementName) {
		return new Stage(parentStage, testStage) {
			public void run() {
				DeliveryEngagementPageC.LoadExistingByName(sh, salesOppName, engagementName);
			}
		};
	}
	
	public static Stage navigateToEngagementDashboardViaProposal(Stage parentStage, String testStage, SeleniumHelper sh, String proposalName, String engagementName) {
		return new Stage(parentStage, testStage) {
			public void run() {
				DeliveryEngagementPageC.LoadExistingByNameViaProposal(sh, proposalName, engagementName);
			}
		};
	}
	
	public static Stage navigateToEngagementFromProposalItems(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName) {
		return new Stage(parentStage, testStage) {
			public void run() {
				DeliveryEngagementPageC.navigateToEngagement(sh, engagementName);
			}
		};
	}
	
	public static Stage navigateToResourceTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, String resourceName) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToResourceHome(stage, "navigate to resource " + resourceName + " home", sh, resourceName);
		navigateToTimesheet(stage, "navigate to timesheet", sh);
		return stage;
	}
	
	public static Stage navigateToResources(Stage parentStage, String testStage, SeleniumHelper sh) {
		Stage stage = new Stage(parentStage, testStage);
		navigateFromAnywhereToAllTabs(stage, "navigate to all tabs", sh);
		navigateFromAllTabsToList(stage, "navigate to Resources tab", sh, "Resources");
		closeLightningpopup(stage, "close the lightning popup", sh);
		return stage;
	}
	
	public static Stage selectResourceandGenerateSupplierInvoice(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		Stage stage = new Stage(parentStage, testStage);
		generateBatchSupplierInvoice(stage, "select supplier resource and click generate supplier invoice", sh, invoice);
		return stage;
	}
	
	public static Stage selectAccountandGenerateSupplierInvoice(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		Stage stage = new Stage(parentStage, testStage);
		generateBatchSupplierInvoiceForAccount(stage, "select supplier account and click generate supplier invoice", sh, invoice);
		return stage;
	}
	
	public static Stage selectAssignmentToSupplierInvoice(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		Stage stage = new Stage(parentStage, testStage);
		selectAssignmentAndClickStart(stage, "select assignment and click start", sh, invoice);
		return stage;
	}
	
	public static Stage generateBatchSupplierInvoice(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new SupplierInvoicePageC().selectResourceAndGenerateInvoice(sh, invoice.supplierResource);
			}
		};
	}
	
	public static Stage generateBatchSupplierInvoiceForAccount(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new SupplierInvoicePageC().selectAccountAndGenerateInvoice(sh, invoice.supplier);
			}
		};
	}
	
	public static Stage selectAssignmentAndClickStart(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new SupplierInvoicePageC().selectAssignmentToInvoice(sh, invoice);
			}
		};
	}
	
	
	public static Stage navigateToResourceHome(Stage parentStage, String testStage, SeleniumHelper sh, String resourceName) {
		if(sh.isLightning())
			return StagesZ.navigateFromAnywhereToListViewItem(parentStage, testStage, sh, "Resources", resourceName);
		else
			return navigateToResourceHomeClassic(parentStage, testStage, sh, resourceName);
	}
	
	public static Stage navigateToSupplierResourceHomeAndSetSupplierAccount(Stage parentStage, String testStage, SeleniumHelper sh, String resourceName, String account) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToResourceHome(stage, "navigate to resource " + resourceName + " home", sh, resourceName);
		navigateToSupplierDashboard(stage, "navigate to resource supplier dashboard", sh);
		addSupplierProducttoResource(stage, "add supplier product", sh, account);
		return stage;
		
	}
	public static Stage navigateToResourceHomeClassic(Stage parentStage, String testStage, SeleniumHelper sh, String resourceName) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new ResourcePageC(sh).LoadExistingByName(resourceName);
			}
		};
	}
	
	public static Stage navigateToTimesheet(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new ResourcePageC(sh).NavigateToTimesheetView();
			}
		};
	}
	
	public static Stage navigateToSupplierDashboard(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new ResourcePageC(sh).NavigateToSupplierDashboard();
			}
		};
	}
	
	public static Stage addSupplierProducttoResource(Stage parentStage, String testStage, SeleniumHelper sh, String account) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new ResourceSupplierDashboardPageC().addSupplierProduct(sh, account);
			}
		};
	}
	public static Stage navigateToResourceJourney(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new ResourcePageC(sh).NavigateToResourceJourney(sh);
			}
		};
	}
	
	public static Stage activateElements(Stage parentStage, String testStage, SeleniumHelper sh, Collection<DeliveryElement> elements) {
		return new Stage(parentStage, testStage) {
			public void run() {
				for(DeliveryElement de : elements)
					if(de.activate)
						DeliveryEngagementPageC.ActivateDeliveryElement(sh, de.name);
			}
		};
	}
	
	public static Stage activateEngagements(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		for(DeliveryEngagement e : salesOpp.deliveryEngagements) {
			if((e.stage == null || stage.getRootStageName().equals(e.stage)) &&
					(e.deliveryStatus == null || !(e.deliveryStatus.equals("Lost")))) {
				navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement " + e.name + " dashboard", sh, salesOpp.name, e.name);
				activateElements(stage, "activate engagements " + e.name + " elements", sh, e.deliveryElements);
			}
		}
		return stage;
	}
	
	public static Stage scheduleActivities(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		for (DeliveryEngagement e : salesOpp.deliveryEngagements)
			if(e.stage == null || stage.getRootStageName().equals(e.stage))
				scheduleActivities(stage, "schedule activities for engagement: " + e.name, sh, salesOpp.name, e);
		return stage;
	}
	
	public static Stage scheduleActivities(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement: " + engagement.name, sh, salesOppName, engagement.name);
		for (DeliveryElement e : engagement.deliveryElements)
			if(e.stage == null || stage.getRootStageName().equals(e.stage))
				scheduleActivities(stage, "schedule activities for element: " + e.name, sh, e);
		return stage;
	}
	
	public static Stage scheduleActivities(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToAssignments(stage, "navigate to assignments", sh);
		scheduleActivitiesOnAssignements(stage, "schedule activities on assignments", sh, element.activityAssignments);
		return stage;
	}
	
	public static Stage scheduleActivitiesOnAssignements(Stage parentStage, String testStage, SeleniumHelper sh, List<ActivityAssignment> assignments) {
		Stage stage = new Stage(parentStage, testStage);
		for (ActivityAssignment a : assignments)
			if(a.scheduledActivity != null)
				scheduleActivities(stage, "schedule activity assignment for resource: " + a.resourceName, sh, a.scheduledActivity);
		return stage;
	}
	
	public static Stage scheduleOtherActivitiesOnAssignements(Stage parentStage, String testStage, SeleniumHelper sh, List<ActivityUnavailability> activityUnavailability) {
		Stage stage = new Stage(parentStage, testStage);
		for (ActivityUnavailability a : activityUnavailability)
			if(a.scheduledActivity != null)
				scheduleOtherActivities(stage, "schedule activity assignment for resource: " + a.resourceName, sh, a.scheduledActivity);
		return stage;
	}	
	
	
	public static Stage scheduleOtherActivities(Stage parentStage, String testStage, SeleniumHelper sh, List<ScheduledActivity> activities) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				new ActivityAssignmentPageC(sh).ScheduleOtherActivity(sh, activities);
			} catch(Exception e) {
				throw new RuntimeException("Failed to schedule activities", e);
			}
		}};
	}
	
	
	
	public static Stage scheduleOtherActivities(Stage parentStage, String testStage, SeleniumHelper sh, OtherResourcedActivity activity) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToAssignments(stage, "navigate to assignments", sh);
//		scheduleOtherActivitiesOnAssignments(stage, "schedule activities on assignments", sh, activity.activityUnavailability, activity);
		return stage;
	}
	
	// create calendar entries
	
	public static Stage scheduleOtherActivitiesOnAssignments(Stage parentStage, String testStage, SeleniumHelper sh, OtherResourcedActivity activity) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToGroupAssignmentByName(stage, "navigate to other activity: " + activity.name, sh, activity.name);
		navigateToOtherActivityAssignments(stage, "navigate to add assignments", sh);
		return scheduleOtherActivities(stage, "createOtherActivityAssignments", sh, activity.activityUnavailability.get(0).scheduledActivity);
	}
	
	
	
	public static Stage scheduleActivities(Stage parentStage, String testStage, SeleniumHelper sh, List<ScheduledActivity> activities) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				new ActivityAssignmentPageC(sh).ScheduleActivity(activities);
			} catch(Exception e) {
				throw new RuntimeException("Failed to schedule activities", e);
			}
		}};
	}
	
	public static Stage scheduleOtherActivities(Stage parentStage, String testStage, SeleniumHelper sh, Collection<OtherResourcedActivity> activities) {
		Stage stage = new Stage(parentStage, testStage);
		for(OtherResourcedActivity a : activities)
			if(a.activityUnavailability != null)
				scheduleOtherActivitiy(stage, "schedule other activity: " + a.name, sh, a);
		return stage;
	}
	
	public static Stage scheduleOtherActivitiy(Stage parentStage, String testStage, SeleniumHelper sh, OtherResourcedActivity activity) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToGroupAssignmentByName(stage, "navigate to other activity: " + activity.name, sh, activity.name);
		navigateToOtherActivityAssignments(stage, "navigate to add assignments", sh);
		scheduleOtherActivitiesOnAssignements(null, "chedule activities on assignments", sh, activity.activityUnavailability);
		return stage;
	}
	
	public static Stage assignCandidatesStage(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		for (DeliveryEngagement engagement : salesOpp.deliveryEngagements)
			assignCandidates(stage, "assign candidates for engagement: " + engagement.name, sh, engagement);
		return stage;
	}

	public static Stage assignCandidates(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardByName(stage, "navigate to the engagement: " + engagement.name + " dashboard", sh, engagement.name);
		for (DeliveryElement element : engagement.deliveryElements)
			assignCandidates(stage, "assign candidates for element: " + element.name, sh, element);
		return stage;
	}

	public static Stage assignCandidates(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToAssignments(stage, "navigate to assignments", sh);
		for (ActivityAssignment assignment : element.activityAssignments)
			assignCandidates(stage, "assign candidates on assignment for resource: " + assignment.resourceName, sh, assignment);
		return stage;
	}
	
	public static Stage assignCandidates(Stage parentStage, String testStage, SeleniumHelper sh, ActivityAssignment assignment) {
		return new Stage(parentStage, testStage) {
			public void run() {
				if(assignment.candidateResources != null)
					new ActivityAssignmentPageC(sh).AssignCandidates(assignment);
			}
		};
	}
	
	public static Stage reviewCandidatesStage(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		for (DeliveryEngagement engagement : salesOpp.deliveryEngagements)
			reviewCandidates(stage, "review candidates for engagement: " + engagement.name, sh, engagement);
		return stage;
	}

	public static Stage reviewCandidates(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardByName(stage, "navigate to the engagement: " + engagement.name + " dashboard", sh, engagement.name);
		for (DeliveryElement element : engagement.deliveryElements)
			reviewCandidates(stage, "review candidates for element: " + element.name, sh, element);
		return stage;
	}

	public static Stage reviewCandidates(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToAssignments(stage, "navigate to assignments", sh);
		for (ActivityAssignment assignment : element.activityAssignments)
			reviewCandidates(stage, "review candidates on assignment for resource: " + assignment.resourceName, sh, assignment);
		return stage;
	}
	
	public static Stage reviewCandidates(Stage parentStage, String testStage, SeleniumHelper sh, ActivityAssignment assignment) {
		return new Stage(parentStage, testStage) {
			public void run() {
				if(assignment.candidateResources != null)
					new ActivityAssignmentPageC(sh).ReviewCandidates(assignment);
			}
		};
	}
	
	public static Stage profileMonthlyAssignmentsStage(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		for (DeliveryEngagement engagement : salesOpp.deliveryEngagements)
			profileMonthlyAssignments(stage, "profile monthly assignments for engagement: " + engagement.name, sh, engagement);
		return stage;
	}

	public static Stage profileMonthlyAssignments(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardByName(stage, "navigate to the engagement: " + engagement.name + " dashboard", sh, engagement.name);
		for (DeliveryElement element : engagement.deliveryElements)
			profileMonthlyAssignments(stage, "profile monthly assignments for element: " + element.name, sh, element);
		return stage;
	}

	public static Stage profileMonthlyAssignments(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToAssignments(stage, "navigate to assignments", sh);
		ActivityAssignmentPageC page = new ActivityAssignmentPageC(sh);
		for (ActivityAssignment assignment : element.activityAssignments)
			profileMonthlyAssignments(stage, "profile monthly assignment for resource: " + assignment.resourceName, sh, assignment, page);
		return stage;
	}
	
	public static Stage profileMonthlyAssignments(Stage parentStage, String testStage, SeleniumHelper sh, ActivityAssignment assignment, ActivityAssignmentPageC page) {
		return new Stage(parentStage, testStage) {
			public void run() {
				if(assignment.monthlyProfiles != null) {
					try {
						page.InitialiseMonthlyView(assignment.startDate);
					} catch (ParseException e) { throw new RuntimeException("failed to initialize monthly view on assignment for resource: " + assignment.resourceName, e); }
					page.ProfileAssignments(assignment, parentStage.getRootStageName());
				}
			}
		};
	}
	
	public static Stage editEngagementExpenseForecast(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		for(DeliveryEngagement e : salesOpp.deliveryEngagements) {
			if(stage.getRootStageName().equals(e.stage)) {
				editEngagementExpenseForecast(stage, "edit engagement's " + e.name + " expense forecast", sh, salesOpp. name, e);
			}
		}
		return stage;
	}
	
	public static Stage editEngagementExpenseForecast(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);

		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement dashboard", sh, salesOppName, engagement.name);
		navigateMenu(stage, "navigate to expense forecasting", sh, PagesC.ACTIVITYEXPENSECATEGORYPROFILES);
		for(DeliveryElement de : engagement.deliveryElements)
			editElementExpenseForecast(stage, "edit delivery element: " + de.name + " expense forecasts", sh, de);

		return stage;
	}
	
	public static Stage editElementExpenseForecast(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseLineItem eli : element.expenseLineItems)
			editExpenseLineItem(stage, "edit expense line item with category: " + eli.category + " and business unit: " + eli.businessUnit, sh, eli);
		return stage;
	}
	
	public static Stage editExpenseLineItem(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseLineItem lineItem) {
		return new Stage(parentStage, testStage) {
			public void run() {
				ExpenseForecastingPageC.editExpenseLineItem(lineItem, sh);
			}
		};
	}
	
	public static Stage createEngagementExpenseForecast(Stage parentStage, String testStage, SeleniumHelper sh, Proposal proposal) {
		Stage stage = new Stage(parentStage, testStage);
		for(DeliveryEngagement e : proposal.deliveryEngagements) {
			if(stage.getRootStageName().equals(e.stage)) {
				createEngagementExpenseForecast(stage, "create engagement's " + e.name + " expense forecast", sh, proposal. name, e);
			}
		}
		return stage;
	}
	
	public static Stage createEngagementExpenseForecast(Stage parentStage, String testStage, SeleniumHelper sh, String proposalName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);

		navigateToEngagementDashboardViaProposal(stage, "navigate to engagement dashboard", sh, proposalName, engagement.name);
		navigateMenu(stage, "navigate to expense forecasting", sh, PagesC.ACTIVITYEXPENSECATEGORYPROFILES);
		for(DeliveryElement de : engagement.deliveryElements)
			createElementExpenseForecast(stage, "create delivery element: " + de.name + " expense forecasts", sh, de);

		return stage;
	}
	
	public static Stage createElementExpenseForecast(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseLineItem eli : element.expenseLineItems)
			createExpenseLineItem(stage, "create expense line item with category: " + eli.category + " and business unit: " + eli.businessUnit, sh, element, eli);
		return stage;
	}
	
	public static Stage createExpenseLineItem(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element, ExpenseLineItem lineItem) {
		return new Stage(parentStage, testStage) {
			public void run() {
				ExpenseForecastingPageC.createExpenseLineItem(element, lineItem, sh);
			}
		};
	}
	
	public static Stage runAllJobs(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new GeneralOperations(sh).RunAllJobs();
			}
		};
	}

	public static Stage runAllJobsMultiThread(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new GeneralOperations(sh).RunAllJobsMultiThread();
			}
		};
	}
	
	public static Stage validateNoActivitiesAvailableForPeriod(Stage parentStage, String testStage, SeleniumHelper sh, List<TimeEntry> entries) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					TreeSet<Date> orderedDates = new TreeSet();
					for(int i = 0; i < entries.size(); i++)
						orderedDates.add(MobileRequests.automationDateFormatter.parse(entries.get(i).startDate));
					String startDate = MobileRequests.automationDateFormatter.format(orderedDates.first());
					String endDate = MobileRequests.automationDateFormatter.format(orderedDates.last());
					new TimesheetPageC(sh).validateActivityCountForPeriod(startDate, endDate, 0);
				} catch(Exception e) {
					throw new RuntimeException("Failed vaidating that no activities are available for the time entries", e);
				}
			}
		};
	}
	
	public static Stage resetTimePeriodsStage(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new TimePeriodPageC(sh).resetTimePeriods();
			}
		};
	}

	public static Stage switchToDetailedForecastLevelStage(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() { 
				SalesOpportunityPageC.SwitchToDetailedForecastLevel(sh); 
			}
		};
	}
	
	public static Stage enableWorkingAtRiskAndActivate(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, Collection<DeliveryEngagement> engagements) {
		Stage stage = new Stage(parentStage, testStage);
		for (DeliveryEngagement de : engagements) {
			navigateAndEnableWorkingAtRisk(stage, "enable working at risk for engagement: " + de.name, sh, salesOppName, de);
			navigateMenu(stage, "navigate back to engagement: " + de.name + " revenues and costs", sh, PagesC.DELIVERYGROUPREVENUESANDCOSTS);
			activateElements(stage, "activate engagements " + de.name + " elements", sh, de.deliveryElements);
		}
		return stage;
	}
	
	public static Stage enableWorkingAtRiskStage(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, Collection<DeliveryEngagement> engagements) {
		Stage stage = new Stage(parentStage, testStage);
		for (DeliveryEngagement de : engagements)
			navigateAndEnableWorkingAtRisk(stage, "enable working at risk for engagement: " + de.name, sh, salesOppName, de);
		return stage;
	}
	
	public static Stage navigateAndEnableWorkingAtRisk(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement", sh, salesOppName, engagement.name);
		enableWorkingAtRisk(stage, "enable working at risk", sh);
		verifyWorkingAtRisk(stage, "verify working at risk", sh);
		return stage;
	}
	
	public static Stage enableWorkingAtRisk(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryEngagementPageC.EnableWorkingAtRisk(sh);
		} };
	}
	
	public static Stage verifyWorkingAtRisk(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryEngagementPageC.NavigateToDashboard(sh);
			assert(sh.verifyPageContainsText("Elements Working At Risk"));
		} };
	}
	
	public static Stage applyRateReductionStage(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		for(DeliveryEngagement e : salesOpp.deliveryEngagements) {
			navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement: " + e.name, sh, salesOpp.name, e.name);
			launchBulkEdit(stage, "launch bulk edit", sh);
			applyRateReduction(stage, "apply rate reduction to engagement: " + e.name, sh, e);
		}
		return stage;
	}
	
	public static Stage applyRateReduction(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		for(DeliveryElement e : engagement.deliveryElements)
			if(e.bulkEdits != null)
				applyRateReduction(stage, "apply rate reduction to element: " + e, sh, engagement.name, e);
		return stage;
	}
	
	public static Stage applyRateReduction(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		for(BulkEdit be : element.bulkEdits)
			applyRateReduction(stage, "apply rate reduction operation: " + be.operation, sh, engagementName, be);
		return stage;
	}
	
	public static Stage applyRateReduction(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName, BulkEdit bulkEdit) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new ActivityAssignmentBulkEditPageC(sh).ChangeRevenueRate(bulkEdit.operation, bulkEdit.value, engagementName);
			}
		};
	}
	
	public static Stage launchBulkEdit(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				DeliveryEngagementPageC.LaunchBulkEdit(sh);
			}
		};
	}
	
	public static Stage navigateToExpense(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseEntry entry, ExpenseDetail detail) {
		return new Stage(parentStage, testStage) {
			public void run() {
				ExpensePageC.navigateToExpense(sh, entry, detail);
			}
		};
	}
	
	public static Stage verifyPageContainsText(Stage parentStage, String testStage, SeleniumHelper sh, String text) {
		return new Stage(parentStage, testStage) { public void run() {
			assert(sh.verifyPageContainsText(text));
		} };
	}
	
	public static Stage validateForecasts(Stage parentStage, String testStage, SeleniumHelper sh, KimbleData data) {
		Stage stage = new Stage(parentStage, testStage);
		runAllJobs(stage, "run all jobs", sh);
		if(!SeleniumHelper.config.validateExpectedResults)
			return stage;
		for(ExpectedResult res : data.expectedResults) {
			if(stage.getRootStageName().equals(res.testStage)) {
				if(res.salesOpportunityForecasts != null)
					navigateToSalesOppForecastAndValidate(stage, "validate sales opp forecast", sh, res.salesOpportunityForecasts);
				if(res.resourceForecasts != null)
					validateResourceForecast(stage, "validate resource forecast", sh, res.resourceForecasts);
				if(res.trackingPlanTotals != null)
					validatePlanTotals(stage, "validate plan totals", sh, res.trackingPlanTotals);
				if(res.trendAnalysis != null)
					validateTrendAnalysis(stage, testStage, sh, res.trendAnalysis);
			}
		}
		if(SeleniumHelper.config.abortOnFirstValidationFailure)
			assertValidationFailure(stage, "assert validation vailure", sh);
		return stage;
	}
	
	public static Stage assertValidationFailure(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			Assert.assertEquals(sh.validationFailure, "");
		}};
	}
	
	public static Stage navigateToSalesOppForecastAndValidate(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunityForecast expected) {
		Stage stage = new Stage(parentStage, testStage);
		navigateFromAnywhereToSalesOppForecast(stage, "navigate to sales opp: " + expected.salesOpportunityName + " forecast", sh, expected.salesOpportunityName);
		validateSalesOppForecast(stage, "validate sales opp: " + expected.salesOpportunityName + " forecast", sh, expected);
		navigateToSalesOppHomeFromForecast(stage, "navigate back to sales opp home", sh, expected.salesOpportunityName);
		return stage;
	}

	public static Stage validateSalesOppForecast(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunityForecast expected) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				KimbleOneTest.checkExpectedResultsForSalesOpportunity(expected, getRootStageName(), sh);
			} catch(Exception e) {
				throw new RuntimeException("Failed to validate sales opp forecast", e);
			}
		}};
	}

	public static Stage validateResourceForecast(Stage parentStage, String testStage, SeleniumHelper sh, Collection<ResourceForecast> expected) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				KimbleOneTest.checkExpectedResultsForResource(expected, getRootStageName(), sh);
			} catch(Exception e) {
				throw new RuntimeException("Failed to validate resource", e);
			}
		}};
	}

	public static Stage validatePlanTotals(Stage parentStage, String testStage, SeleniumHelper sh, Collection<TrackingPlanTotal> expected) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				KimbleOneTest.checkTrackingPlanTotals(expected, getRootStageName(), sh);
			} catch(Exception e) {
				throw new RuntimeException("Failed to validate tracking plan totals", e);
			}
		}};
	}

	public static Stage validateTrendAnalysis(Stage parentStage, String testStage, SeleniumHelper sh, List<TrendAnalysis> ta) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				TrendAnalysisPageC.validateTrendAnalysis(sh, ta);
			} catch(Exception e) {
				throw new RuntimeException("Failed to validate trend analysis", e);
			}
		}};
	}

	public static Stage enterTime(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Timesheet> timesheets) {
		Stage stage =  new Stage(parentStage, testStage);
		for (Timesheet ts : timesheets) {
			if(ts.testStage == null || stage.getRootStageName().equals(ts.testStage)) {
				navigateToResourceTimesheet(stage, "navigate to timesheet for resource: " + ts.resourceName, sh, ts.resourceName);
				enterTimesheet(stage, "enter timesheet for resource: " + ts.resourceName, sh, ts);
			}
		}
		return stage;
	}
	
	public static Stage enterTimeEnhancedTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Timesheet> timesheets) {
		Stage stage =  new Stage(parentStage, testStage);
		for (Timesheet ts : timesheets) {
			if(ts.testStage == null || stage.getRootStageName().equals(ts.testStage)) {
				navigateToResourceTimesheet(stage, "navigate to timesheet for resource: " + ts.resourceName, sh, ts.resourceName);
				enterTimesheetEnhanced(stage, "enter timesheet for resource: " + ts.resourceName, sh, ts);							
			}
		}
		return stage;
	}
	
	public static Stage retrieveTimePeriodId(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Timesheet> timesheets) {
		Stage stage =  new Stage(parentStage, testStage);
		for (Timesheet ts : timesheets) {
			if(ts.testStage == null || stage.getRootStageName().equals(ts.testStage)) {
				navigateToResourceTimesheet(stage, "navigate to timesheet for resource: " + ts.resourceName, sh, ts.resourceName);
				retrieveTrackingPeriodId(stage, "enter timesheet for resource: " + ts.resourceName, sh, ts);						
			}
		}
		return stage;
	}
	public static Stage manyAssignmentsEdit(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement e, List<ActivityAssignmentsMany> assignments, List<ActivityAssignment> resources) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardByName(stage, "navigate to engagement", sh, e.name);
		navigateToManyAssignments(stage, "navigate to many assignments page", sh);
		editAssignmentsManypage(stage, "make changes to Assignments via the Many Assignments page",sh, e, assignments, resources);
		return stage;
	}
	
	public static Stage editAssignmentsManypage(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement e, List<ActivityAssignmentsMany> assignments, List<ActivityAssignment> resources) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				new ActivityAssignmentsManyPageC(sh).ManyAssignmentsedit(assignments, resources);
			} catch(Exception e) {
				throw new RuntimeException("Failed to edit Assignments: ");
			}
		}};
	}
	public static Stage enterTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet timesheet) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				new TimesheetPageC(sh).enterTime(timesheet);
			} catch(Exception e) {
				throw new RuntimeException("Failed to enter timesheet for resource: " + timesheet.resourceName, e);
			}
		}};
	}

	
	public static Stage enterTimesheetEnhanced(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet timesheet) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				new TimesheetPageC(sh).enterTimeEnhanced(timesheet);
			} catch(Exception e) {
				throw new RuntimeException("Failed to enter timesheet for resource: " + timesheet.resourceName, e);
			}
		}};
	}	
	
	public static Stage retrieveTrackingPeriodId(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet timesheet) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				new TimesheetPageC(sh).getTrackingPeriodId(timesheet);
			} catch(Exception e) {
				throw new RuntimeException("Failed to retrieve time period id: " + timesheet.resourceName, e);
			}
		}};
	}
	
	public static Stage validateNoDraftTimeEntries(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet timesheet) {
		return new Stage(parentStage, testStage) { public void run() {
			try {
				new TimesheetPageC(sh).validateNoDraftTimeEntries(timesheet);
			} catch(Exception e) {
				throw new RuntimeException("Validate no timesheet for resource: " + timesheet.resourceName, e);
			}
		}};
	}
	
	
	public static Stage adjustTimeEntries(Stage parentStage, String testStage, SeleniumHelper sh, Map<String, TimeEntryAdjustmentList> adjustmentLists) {
		Stage stage = new Stage(parentStage, testStage);
		
		TimeEntryAdjustmentList list = adjustmentLists.get(stage.getRootStageName());
		
		// sort adjustments by date
		SimpleDateFormat format = MobileRequests.automationDateFormatter;//new SimpleDateFormat("dd/MM/yyyy");
//		Collections.sort(list.timeAdjustments, (a, b) -> {
//		    try {
//				return format.parse(a.day).compareTo(format.parse(b.day));
//			} catch (Exception e) { throw new RuntimeException("Failed to compare time adjustment dates", e); }
//		});

		// put adjustments into batches by tracking period start date
		TreeMap<Date, List<TimeEntryAdjustment>> batches = new TreeMap();
		for(TimeEntryAdjustment tea : list.timeAdjustments) {
			Date day;
			try { day = format.parse(tea.day); } catch (ParseException e) { throw new RuntimeException("Failed to parse the time entry adjustment date", e); }
			General.getList(General.getTrackingPeriodStartDate(day), batches).add(tea);
		}
		navigateToResourceTimesheet(stage, "navigate to timesheet for resource: " + list.resourceName, sh, list.resourceName);
//		navigateTotimesheetDayInTnx(stage, "navigate to a closed tracking period", sh, MobileRequests.automationDateFormatter.format(batches.lastKey()));
		for(Date trackingPeriodStart : batches.keySet())
			adjustTimeEntries(stage, "adjust time entries in tracking period starting: " + format.format(trackingPeriodStart), sh, 
				list.resourceName, trackingPeriodStart, batches.get(trackingPeriodStart));
		return stage;
	}
	
	public static Stage adjustTimeEntries(Stage parentStage, String testStage, SeleniumHelper sh, String resource, Date trackingPeriodStart, List<TimeEntryAdjustment> adjustmentBatch) {
		Stage stage = new Stage(parentStage, testStage);
		navigateTotimesheetDayInTnx(stage, "navigate to date", sh, MobileRequests.automationDateFormatter.format(trackingPeriodStart));
		switchToAdjustTimeEntries(stage, "navigate to adjust time entries", sh);
		adjustTimeEntries(stage, "adjust time entries", sh, adjustmentBatch);
		saveAndSubmitTimeAdjustments(stage, "save and submit time adjustments", sh);
		return stage;
	}
	
	public static Stage switchToAdjustTimeEntries(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				TimesheetPageC.navigateToAdjustTimeEntries(sh);
			}
		};
	}

	public static Stage adjustTimeEntries(Stage parentStage, String testStage, SeleniumHelper sh, List<TimeEntryAdjustment> adjustmentBatch) {
		return new Stage(parentStage, testStage) {
			public void run() {
				TimesheetPageC.adjustTimeEntries(sh, adjustmentBatch);
			}
		};
	}

	public static Stage saveAndSubmitTimeAdjustments(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				TimesheetPageC.saveAndSubmitAdjustments(sh);
			}
		};
	}

	public static Stage enterExpenses(Stage parentStage, String testStage, SeleniumHelper sh, Collection<ExpenseClaim> claims) {
		return enterExpenses(parentStage, testStage, sh, claims, true);
	}
	
	public static Stage enterExpenses(Stage parentStage, String testStage, SeleniumHelper sh, Collection<ExpenseClaim> claims, boolean submit) {
		Stage stage =  new Stage(parentStage, testStage);
		for (ExpenseClaim e : claims)
			if(stage.getRootStageName().equals(e.testStage))
				enterExpenseClaim(stage, "enter expense claim for resource: " + e.resourceName, sh, e, submit);
		return stage;
	}
	
	public static Stage enterExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseClaim claim) {
		return enterExpenseClaim(parentStage, testStage, sh, claim, true);
	}

	public static Stage enterExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseClaim claim, boolean submit) {
		Stage stage =  new Stage(parentStage, testStage);
		for (ExpenseEntry e : claim.expenseEntries)
			enterExpenseEntry(stage, "enter expense entry: " + e.name + " for activity: " + e.activityName, sh, claim, e, submit);
		return stage;
	}

	public static Stage enterExpenseEntry(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseClaim claim, ExpenseEntry entry) {
		return enterExpenseEntry(parentStage, testStage, sh, claim, entry, true);
	}

	public static Stage enterExpenseEntry(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseClaim claim, ExpenseEntry entry, boolean submit) {
		return new Stage(parentStage, testStage) { public void run() {
			new ResourcePageC(sh).getExpenseEntryLinkForResource(claim).click();
			new ExpensePageC(sh).CreateNew(entry, submit);				
		}};
	}

	public static Stage saveAndSubmitExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				ExpensePageC.saveAndSubmit(sh);
			}
		};
	}
	
	public static Stage completeMilestonesStage(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage = new Stage(parentStage, testStage);
		for(DeliveryEngagement e : salesOpp.deliveryEngagements)
			completeMilestonesStage(stage, "complete milestones for engagement: " + e.name, sh, salesOpp.name, e);
		return stage;
	}
	
	public static Stage completeMilestonesStage(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement dashboard", sh, salesOppName, engagement.name);
		boolean milestoneNavigationAdded = false;
		for (DeliveryElement e : engagement.deliveryElements) {
			if(e.revenueMilestones != null || e.costMilestones != null) {
				if(!milestoneNavigationAdded) {
					navigateToMilestones(stage, "navigate to milestones", sh);
					milestoneNavigationAdded = true;
				}
				completeMilestonesStage(stage, "complete milestones for element: " + e.name, sh, salesOppName, e);
			}
		}
		return stage;
	}
	
	public static Stage completeMilestonesStage(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryElement element) {
		Stage stage = new Stage(parentStage, testStage);
		if(element.revenueMilestones != null)
			for(Milestone m : element.revenueMilestones)
				if(m.completionStage == null || stage.getRootStageName().equals(m.completionStage))
					completeMilestoneStage(stage, "Complete milestone: " + m.name, sh, element, m);
		if(element.costMilestones != null)
			for(Milestone m : element.costMilestones)
				if(m.completionStage == null || stage.getRootStageName().equals(m.completionStage))
					completeMilestoneStage(stage, "Complete milestone: " + m.name, sh, element, m);
		return stage;
	}
	
	public static Stage completeMilestoneStage(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element, Milestone milestone) {
		return new Stage(parentStage, testStage) { public void run() {
			new DeliveryGroupMilestonesPageC(sh).CompleteMilestone(element.name, milestone.name);
		}};
	}
	
	public static Stage closeTrackingPeriodsStage(Stage parentStage, String testStage, SeleniumHelper sh, List<TimePeriodAction> actions) {
		Stage stage = new Stage(parentStage, testStage);
		runAllJobs(stage, "run all jobs before closing the periods", sh);
		navigateToPeriodManagement(stage, "navigate to period management", sh);
		for(TimePeriodAction a : actions) {
			if(stage.getRootStageName().equals(a.testStage) && a.action.equals("close")) {
				navigateToTrackingPeriod(stage, "navigate to tracking period: " + a.periodName, sh, a);
				closeTrackingPeriod(stage, "close tracking period: " + a.businessUnitGroup + " [" + a.periodName + "]", sh, a);
			}
		}
		return stage;
	}
	
	public static Stage openTrackingPeriods(Stage parentStage, String testStage, SeleniumHelper sh, List<TimePeriodAction> actions) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToPeriodManagement(stage, "navigate to period management", sh);
		for(TimePeriodAction a : actions) {
			if(stage.getRootStageName().equals(a.testStage)) {
				navigateToTrackingPeriodForGroup(stage, "navigate to tracking period for group: " + a.businessUnitGroup, sh, a.businessUnitGroup);
				navigateToTrackingPeriodForDate(stage, "navigate to period: " + a.periodName, sh, a.periodName);
				openTrackingPeriod(stage, "re-open tracking period: " + a.businessUnitGroup + " [" + a.periodName + "]", sh, a);
			}
		}
		return stage;
	}

	public static Stage navigateToPeriodManagement(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { 
			public void run() { 
				new TrackingPeriodClosePageC(sh).NavigateToPeriodManagement(); 
			} 
		};
	}

	public static Stage closeTrackingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					executeSequenceWithRefreshRetry(sh, 2, () -> {
						if(action.removeUsage)
							TrackingPeriodClosePageC.RemoveUsage(sh, action.businessUnitGroup, action.periodName);
		
						TrackingPeriodClosePageC.CloseTrackingPeriodWhereForecastComplete(sh, action.businessUnitGroup, action.periodName);
					});
				} catch(Exception e) {
					if(action.force)
						throw new RuntimeException("Failed to close the tracking period", e);
				}
			}
		};
	}
	
	public static Stage navigateToTrackingPeriodForGroup(Stage parentStage, String testStage, SeleniumHelper sh, String businessUnitGroup) {
		return new Stage(parentStage, testStage) {
			public void run() {
				TrackingPeriodClosePageC.navigateToTrackingPeriodForGroup(sh, businessUnitGroup);
			}
		};
	}

	public static Stage navigateToTrackingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					if(!action.force)
						TrackingPeriodClosePageC.validateTrackingPeriodExists(sh, action.businessUnitGroup, action.periodName, 2);
					TrackingPeriodClosePageC.navigateToTrackingPeriod(sh, action.businessUnitGroup, action.periodName);
				} catch(Exception e) {
					if(action.force)
						throw new RuntimeException("Failed to close the tracking period", e);
				}
			}
		};
	}

	public static Stage navigateToTrackingPeriodForDate(Stage parentStage, String testStage, SeleniumHelper sh, String periodName) {
		Date trackingPeriodDate = General.getDateForTrackingPeriod(periodName);
		return new Stage(parentStage, testStage) {
			public void run() {
				TrackingPeriodClosePageC.navigateToTrackingPeriodForDate(sh, trackingPeriodDate);
			}
		};
	}

	public static Stage openTrackingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					TrackingPeriodClosePageC trackingPeriodCloseHandler = new TrackingPeriodClosePageC(sh);
	
					trackingPeriodCloseHandler.openTrackingPeriod(action.businessUnitGroup, action.periodName);
				} catch(Exception e) {
					if(action.force)
						throw new RuntimeException("Failed to re-open the tracking period: " + action.periodName, e);
				}
			}
		};
	}

	public static Stage closeForecastingPeriodsStage(Stage parentStage, String testStage, SeleniumHelper sh, List<TimePeriodAction> actions) {
		Stage stage = new Stage(parentStage, testStage);
		runAllJobs(stage, "run all jobs", sh);
		navigateToPeriodManagement(stage, "navigate to forecast period close", sh);
		for(TimePeriodAction a : actions) {
			if(stage.getRootStageName().equals(a.testStage) && a.action.equals("close")) {
				navigateToForecastingPeriod(stage, "navigate to forecasting period: " + a.businessUnitGroup + " [" + a.periodName + "]", sh, a);
				closeForecastingPeriod(stage, "close forecasting period: " + a.businessUnitGroup + " [" + a.periodName + "]", sh, a);
			}
		}
		return stage;
	}

	public static Stage openForecastingPeriodsStage(Stage parentStage, String testStage, SeleniumHelper sh, List<TimePeriodAction> actions) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToPeriodManagement(stage, "navigate to period management", sh);
		for(TimePeriodAction a : actions) {
			if(stage.getRootStageName().equals(a.testStage)) {
				navigateToForecastingPeriodForGroup(stage, "navigate to forecasting period: " + a.businessUnitGroup, sh, a.businessUnitGroup);
				navigateToForecastingPeriodDate(stage, "navigate to period: " + a.periodName, sh, a.periodName);
				openForecastingPeriod(stage, "re-open forecasting period: " + a.businessUnitGroup + " [" + a.periodName + "]", sh, a);
			}
		}
		return stage;
	}

	public static Stage closeForecastingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					ForecastingPeriodClosePageC forecastingPeriodCloseHandler = new ForecastingPeriodClosePageC(sh);
	
					try {
						forecastingPeriodCloseHandler.CloseForecastingPeriodWhereForecastComplete();
					} catch(Exception e) {
						String returnUrl = sh.getCurrentUrl();
						new GeneralOperations(sh).RunAllJobs();
						sh.goToUrl(returnUrl);
						forecastingPeriodCloseHandler.CloseForecastingPeriodWhereForecastComplete();
					}
				} catch(Exception e) {
					if(action.force)
						throw new RuntimeException("Failed to close the forecasting period", e);
				}
			}
		};
	}

	public static Stage navigateToForecastingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					if(!action.force)
						ForecastingPeriodClosePageC.validateForecastingPeriodExists(sh, action.businessUnitGroup, action.periodName, 2);
					ForecastingPeriodClosePageC.navigateToForecastingPeriod(sh, action.businessUnitGroup, action.periodName);
				} catch(Exception e) {
					if(action.force)
						throw new RuntimeException("Failed to close the forecasting period", e);
				}
			}
		};
	}

	public static Stage navigateToForecastingPeriodForGroup(Stage parentStage, String testStage, SeleniumHelper sh, String businessUnitGroup) {
		return new Stage(parentStage, testStage) {
			public void run() {
				ForecastingPeriodClosePageC.navigateToForecastingPeriodByGroup(sh, businessUnitGroup);
			}
		};
	}

	public static Stage navigateToForecastingPeriodDate(Stage parentStage, String testStage, SeleniumHelper sh, String periodName) {
		Date forecastingPeriodDate = General.getDateForForecastingPeriod(periodName);
		return new Stage(parentStage, testStage) {
			public void run() {
				ForecastingPeriodClosePageC.navigateToForecastingPeriodForDate(sh, forecastingPeriodDate);
			}
		};
	}

	public static Stage navigateToTrackingPeriodDate(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
		Date trackingPeriodDate = General.getDateForTrackingPeriod(action.periodName);
		return new Stage(parentStage, testStage) {
			public void run() {
				TrackingPeriodClosePageC.navigateToTrackingPeriodForDate(sh, trackingPeriodDate);
			}
		};
	}

	public static Stage openForecastingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
		return new Stage(parentStage, testStage) {
			public void run() {
				ForecastingPeriodClosePageC.openForecastingPeriod(sh, action.force);
			}
		};
	}

public static Stage updateRemainingUsage(Stage parentStage, String testStage, SeleniumHelper  sh, SalesOpportunity salesOpp, int stageNo) {
		Stage stage = new Stage(parentStage, testStage);
		for (DeliveryEngagement e : salesOpp.deliveryEngagements)
			updateRemainingUsage(stage, "update remaining usage (stage 3) for engagement: " + e.name, sh, salesOpp.name, e, stageNo);
		return stage;
	}
	
	public static Stage updateRemainingUsage(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement, int stageNo) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement: " + engagement.name + " dashboard", sh, salesOppName, engagement.name);
		for (DeliveryElement e : engagement.deliveryElements)
			updateRemainingUsage(stage, "update remaining usage (stage 3) for element: " + e.name, sh, e, stageNo);
		return stage;
	}

	public static Stage updateRemainingUsage(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element, int stageNo) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToAssignments(stage, "navigate to assignments", sh);
		for (ActivityAssignment a : element.activityAssignments)
			updateRemainingUsage(stage, "update remaining usage (stage 3) for assignment for resource: " + a.resourceName, sh, a, stageNo);
		return stage;
	}
	
	public static Stage updateRemainingUsage(Stage parentStage, String testStage, SeleniumHelper sh, ActivityAssignment assignment, int stageNo) {
		return new Stage(parentStage, testStage) { public void run() {
			if(stageNo == 1)
				new ActivityAssignmentPageC(sh).UpdateRemainingUsage(assignment.resourceName, assignment.remainingEffort);
			else if(stageNo == 2 && assignment.remainingEffortStage2 != null)
				new ActivityAssignmentPageC(sh).UpdateRemainingUsageStage2(assignment);
			else if(stageNo == 3 && assignment.remainingEffortStage3 != null)
				new ActivityAssignmentPageC(sh).UpdateRemainingUsageStage3(assignment);
			if(stageNo < 1 || stageNo > 3)
				throw new RuntimeException("The stage number: " + stageNo + " is out of range. Should be one off: 1, 2 or 3");
		}};
	}
	
//	public static Stage force

//	mobile requests
	 
	public static Stage createTimeEntry(Stage parentStage, String testStage, SeleniumHelper sh, UserCredentials creds, TimeEntryMob entry) {
		return new Stage(parentStage, testStage) {
			public void run() {
				TimestampedIdentifier id;
				try {
					id = MobileRequests.createTimeEntry(sh.getWD(), creds, entry);
				} catch(Exception e) {
					throw new RuntimeException("Failed to execute request", e);
				}
				System.out.println(id);
			}
		};
	}
	
	public static Stage enterTimesheets(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<Timesheet> sheets) {
		Stage stage = new Stage(parentStage, testStage);
		for(Timesheet ts : sheets)
			if(stage.getRootStageName().equals(ts.testStage))
				enterTimesheet(stage, "submit timesheet for resource: " + ts.resourceName, wd, creds, state, ts);
		return stage;
	}
	
	public static Stage enterTimesheet(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Timesheet sheet) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.enterTimesheet(wd, creds, state, sheet);
			}
		};
	}
	

	public static Stage submitTimeEntries(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<Timesheet> sheets) {
		Stage stage = new Stage(parentStage, testStage);
		for(Timesheet ts : sheets)
			if(stage.getRootStageName().equals(ts.testStage))
				submitTimeEntries(stage, "submit time entries for resource: " + ts.resourceName, wd, creds, state, ts);
		return stage;
	}
	
	public static Stage submitTimeEntries(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Timesheet sheet) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.submitTimesheet(wd, creds, state, sheet);
			}
		};
	}
	

	public static Stage editTimesheets(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<Timesheet> sheets) {
		Stage stage = new Stage(parentStage, testStage);
		for(Timesheet ts : sheets)
			if(stage.getRootStageName().equals(ts.testStage))
				editTimesheet(stage, "delete timesheet for resource: " + ts.resourceName, wd, creds, state, ts);
		return stage;
	}
	
	public static Stage editTimesheet(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Timesheet sheet) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.editTimesheet(wd, creds, state, sheet);
			}
		};
	}
	

	public static Stage deleteTimesheets(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<Timesheet> sheets) {
		Stage stage = new Stage(parentStage, testStage);
		for(Timesheet ts : sheets)
			if(stage.getRootStageName().equals(ts.testStage))
				deleteTimesheet(stage, "delete timesheet for resource: " + ts.resourceName, wd, creds, state, ts);
		return stage;
	}
	
	public static Stage deleteTimesheet(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Timesheet sheet) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.deleteTimesheet(wd, creds, state, sheet);
			}
		};
	}
	

	public static Stage validateTimesheets(Stage parentStage, String testStage, SeleniumHelper sh, TestState state, Collection<Timesheet> sheets) {
		Stage stage = new Stage(parentStage, testStage);
		for(Timesheet ts : sheets)
			if(stage.getRootStageName().equals(ts.testStage))
				validateTimesheet(stage, "validate timesheet for resource: " + ts.resourceName, sh, state, ts);
		return stage;
	}
	
	public static Stage validateTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, TestState state, Timesheet sheet) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new TimesheetPageC(sh).validateMobileTimeEntries(state, sheet.timeEntries.toArray(new TimeEntry[sheet.timeEntries.size()]));
			}
		};
	}
	

	public static Stage enterExpenseClaims(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<ExpenseEntry> claims) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseEntry ec : claims)
			if(stage.getRootStageName().equals(ec.testStage))
				enterExpenseClaim(stage, "enter expense claim: " + ec.name, wd, creds, state, ec);
		return stage;
	}
	
	public static Stage enterExpenseClaim(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, ExpenseEntry claim) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.enterExpenseClaim(wd, creds, state, claim);
			}
		};
	}
	

	public static Stage submitExpenseClaims(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<ExpenseEntry> claims) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseEntry ec : claims)
			if(stage.getRootStageName().equals(ec.testStage))
				submitExpenseClaim(stage, "submit expense claim: " + ec.name, wd, creds, state, ec);
		return stage;
	}
	
	public static Stage submitExpenseClaim(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, ExpenseEntry claim) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.submitExpenseClaim(wd, creds, state, claim);
			}
		};
	}
	

	public static Stage editExpenseClaims(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<ExpenseEntry> claims) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseEntry ec : claims)
			if(stage.getRootStageName().equals(ec.testStage))
				editExpenseClaim(stage, "edit expense claim: " + ec.name, wd, creds, state, ec);
		return stage;
	}
	
	public static Stage editExpenseClaim(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, ExpenseEntry claim) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.editExpenseClaim(wd, creds, state, claim);
			}
		};
	}
	

	public static Stage deleteExpenseClaims(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<ExpenseEntry> claims) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseEntry ec : claims)
			if(stage.getRootStageName().equals(ec.testStage))
				deleteExpenseClaim(stage, "delete expense claim: " + ec.name, wd, creds, state, ec);
		return stage;
	}
	
	public static Stage deleteExpenseClaim(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, ExpenseEntry claim) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.deleteExpenseClaim(wd, creds, state, claim);
			}
		};
	}
	

	public static Stage validateExpenseClaims(Stage parentStage, String testStage, SeleniumHelper sh, UserCredentials creds, TestState state, Collection<ExpenseEntry> claims) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseEntry ec : claims)
			if(stage.getRootStageName().equals(ec.testStage))
				validateExpenseClaim(stage, "validate expense claim: " + ec.name, sh, creds, state, ec);
		return stage;
	}
	
	public static Stage validateExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, UserCredentials creds, TestState state, ExpenseEntry claim) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.validateExpenseClaim( sh, creds, state, claim);
			}
		};
	}
	

	public static Stage addExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<ExpenseEntry> claims) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseEntry ec : claims)
			if(stage.getRootStageName().equals(ec.testStage))
				addExpenseItems(stage, "add expense items for claim: " + ec.name, wd, creds, state, ec);
		return stage;
	}
	
	public static Stage addExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, ExpenseEntry claim) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseDetail ei : claim.expenseDetails)
			addExpenseItem(stage, "add expense item with notes: " + ei.notes, wd, creds, state, claim, ei);
		return stage;
	}
	
	public static Stage addExpenseItem(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, ExpenseEntry claim, ExpenseDetail item) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.addExpenseItem(wd, creds, state, claim, item);
			}
		};
	}
	
	
	public static Stage editExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<ExpenseEntry> claims) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseEntry ec : claims)
			if(stage.getRootStageName().equals(ec.testStage))
				editExpenseItems(stage, "edit expense items of claim: " + ec.name, wd, creds, state, ec);
		return stage;
	}
	
	public static Stage editExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, ExpenseEntry claim) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseDetail ei : claim.expenseDetails)
			editExpenseItem(stage, "edit expense item with notes: " + ei.notes, wd, creds, state, claim, ei);
		return stage;
	}
	
	public static Stage editExpenseItem(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, ExpenseEntry claim, ExpenseDetail item) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.editExpenseItem(wd, creds, state, claim, item);
			}
		};
	}
	
	
	public static Stage deleteExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, Collection<ExpenseEntry> claims) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseEntry ec : claims)
			if(stage.getRootStageName().equals(ec.testStage))
				deleteExpenseItems(stage, "delete expense items of claim: " + ec.name, wd, creds, state, ec);
		return stage;
	}
	
	public static Stage deleteExpenseItems(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, ExpenseEntry claim) {
		Stage stage = new Stage(parentStage, testStage);
		for(ExpenseDetail ei : claim.expenseDetails)
			deleteExpenseItem(stage, "delete expense item with notes: " + ei.notes, wd, creds, state, ei);
		return stage;
	}
	
	public static Stage deleteExpenseItem(Stage parentStage, String testStage, WebDriver wd, UserCredentials creds, TestState state, ExpenseDetail item) {
		return new Stage(parentStage, testStage) {
			public void run() {
				MobileRequests.deleteExpenseItem(wd, creds, state, item);
			}
		};
	}
	
	public static Stage setPurchaseOrderRules(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardByName(parentStage, "navigate to engagement dashboard via sales opportunity", sh, engagement.name);
		setPurchaseOrderRules(stage, "set purchase order rules on elements", sh, engagement.deliveryElements);
		return stage;
	}

	public static Stage setPurchaseOrderRules(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement dashboard via sales opportunity", sh, salesOppName, engagement.name);
		setPurchaseOrderRules(stage, "set purchase order rules on elements", sh, engagement.deliveryElements);
		return stage;
	}
	
	public static Stage allocateCredit(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement dashboard via sales opportunity", sh, salesOppName, engagement.name);
		allocateCredit(stage, "set purchase order rules on elements", sh, salesOppName, engagement.deliveryElements);
		return stage;
	}
	
	public static Stage allocateCredit(Stage parentStage, String testStage, SeleniumHelper sh, String salesOpp, Collection<DeliveryElement> elements) {
		Stage stage = new Stage(parentStage, testStage);
		navigateMenu(stage, "navigate to budgets and purchase orders for the " + salesOpp + " element", sh, PagesC.BUDGETSANDPOS);
		for(DeliveryElement e : elements)
			creditAllocation(stage, "allocate credit against " + salesOpp, sh, e);
		return stage;
	}
	
	public static Stage creditAllocation(Stage stage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		return new Stage(stage, testStage) { public void run() {
			DeliveryGroupBudgetsPageC.allocateCreditAgainstElement(sh, element);
		}};
	}
	
	public static Stage setPurchaseOrderRules(Stage parentStage, String testStage, SeleniumHelper sh, Collection<DeliveryElement> elements) {
		Stage stage = new Stage(parentStage, testStage);
		navigateMenu(stage, "navigate to budgets and purchase orders", sh, PagesC.BUDGETSANDPOS);
		for(DeliveryElement e : elements)
			if(e.purchaseOrderRule != null)
				setPurchaseOrderRule(stage, "set purchase order rule to: " + e.purchaseOrderRule + " on element: " + e.name, sh, e);
		return stage;
	}
	
	public static Stage setPurchaseOrderRule(Stage stage, String testStage, SeleniumHelper sh, DeliveryElement element) {
		return new Stage(stage, testStage) { public void run() {
			DeliveryGroupBudgetsPageC.setPurchaseOrderRule(sh, element.name, element.purchaseOrderRule);
		}};
	}

	public static Stage allocateAllPurchaseOrdersStage(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Invoice> invoices) {
		Stage stage = new Stage(parentStage, testStage);
		for (Invoice iv : invoices)
			if(iv.purchaseOrders != null)
				for (PurchaseOrder po : iv.purchaseOrders)
					allocateNewPurchaseOrder(stage, "Allocate purchase order: " + po.reference, sh, iv.deliveryEngagementName, po);
		return stage;
	}
	
	public static Stage allocateNewPurchaseOrder(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName, PurchaseOrder po) {
		return new Stage(parentStage, testStage){ public void run() {
			DeliveryEngagementPageC.AllocateNewPurchaseOrder(sh, engagementName, po);
		}};
	}
	
	public static Stage generateInvoices(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Invoice> invoices, boolean allocatePOs) {
		return generateInvoices(parentStage, testStage, sh, invoices, true, allocatePOs);
	}
	
	
	public static Stage createInternalInvoice(Stage parentStage, String stageName, SeleniumHelper sh, List<Invoice> invoices) {
		Stage stage = new Stage(parentStage, stageName);
		for (Invoice iv : invoices) {
			if(iv.testStage == null || stage.getRootStageName().equals(iv.testStage)) {
				navigateFromAnywhereToTab(stage, "navigate to business unit tab", sh, "Business Units");
				clickGoInListView(stage, "show all business units", sh);
				navigateToBusinessUnitByNameandGenerateInvoice(stage, "click business unit", sh, iv.businessUnit, iv.invoicingBusinessUnit, iv.invoicingAccount);			
				createNewInvoice(stage, "create new invoice", sh, iv);
				validateInvoiceHomeValues(stage, "validate invoice home values", sh, iv);
				validateInvoicePdfValues(stage, "validate invoice pdf values", sh, iv);
				sendInvoiceForApproval(stage, "send invoice for approval", sh, iv);
				dispatchInvoice(stage, "dispatch invoice", sh, iv);
			}
		}
		return stage;
	}
	
	public static Stage setInternalAccountOnBusinessUnit(Stage parentStage, String stageName, SeleniumHelper sh, List<Account> allaccounts) {
		Stage stage = new Stage(parentStage, stageName);
		for (Account ac : allaccounts) {
				navigateFromAnywhereToTab(stage, "navigate to business unit tab", sh, "Business Units");
				clickGoInListView(stage, "show all business units", sh);		
				navigateToBusinessUnitByName(stage, "navigate to business unit", sh, ac.operatingBusinessUnit);
				enterBusinessUnitInternalAccount(stage, "enter business unit internal account", sh, ac.name);	
		}
		return stage;
	}

	public static Stage generateInvoices(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Invoice> invoices, boolean validateInvoiceAmount, boolean allocatePOs) {		
		return generateInvoices(parentStage, testStage, sh, invoices, validateInvoiceAmount, allocatePOs, 1);
	}
	
	public static Stage generateInvoices(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Invoice> invoices, boolean validateInvoiceAmount, boolean allocatePOs, int lineCount) {
		Stage stage = new Stage(parentStage, testStage);
		// invoicing requires timesheet completion jobs to complete, navigate to jobspending as a workaround for ticket #6016
		runAllJobs(stage, "run all jobs before generating invoices", sh);
//		if(allocatePOs)
//			allocateNewPurchaseOrdersStage(stage, "allocate purcahse orders", sh, invoices);
		for (Invoice iv : invoices) {
			if(iv.testStage == null || stage.getRootStageName().equals(iv.testStage)) {
				if(allocatePOs && iv.purchaseOrders != null)
					for (PurchaseOrder po : iv.purchaseOrders)
						allocateNewPurchaseOrder(stage, "Allocate purchase order: " + po.reference, sh, iv.deliveryEngagementName, po);
				generateInvoice(stage, "generate invoice for engagement: " + iv.deliveryEngagementName, sh, iv, validateInvoiceAmount, lineCount);
			}
		}
		return stage;
	}
	
	public static Stage generateInvoice(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice, boolean validateInvoiceValues, int lineCount) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardByName(stage, "navigate to engagement dashboard", sh, invoice.deliveryEngagementName);
		createNewInvoice(stage, "create new invoice", sh, invoice);
//		assertNotEquals(stage, "invoice reference set?", sh, null, invoice.invoiceReference);
//		assertNotEquals(stage, "invoice reference set?", sh, invoice.invoiceReference, "");
		if(validateInvoiceValues) {
			validateInvoiceHomeValues(stage, "validate invoice home values", sh, invoice);
			validateInvoicePdfValues(stage, "validate invoice pdf values", sh, invoice);
		}
		sendInvoiceForApproval(stage, "send invoice for approval", sh, invoice);
		dispatchInvoice(stage, "dispatch invoice", sh, invoice);
		return stage;
	}
		
	public static Stage createNewInvoice(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			new InvoicePageC(sh).CreateNew(invoice);
		}};
	}
	
	public static Stage generateNewInvoiceWithNoPO(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			new InvoicePageC(sh).GenerateInvoiceForDeliveryEngagementWithNoPO(invoice);
		}};
		
	}
	
	public static Stage validateInvoiceHomeValues(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			InvoicePageC page = new InvoicePageC(sh);
			page.validateInvoiceHomeValues(invoice);
		}};
	}
	
	public static Stage validateInvoicePdfValues(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			InvoicePageC page = new InvoicePageC(sh);
			String home = sh.getCurrentUrl();
			if(sh.isLightning()){
				sh.waitForLightningSpinnerToBeHidden();
				sh.clickLink(By.id("k-image"));
				
				String id = sh.getIDFromCurrentURL();
				String invoicePreviewUrl = sh.getApexBaseFromCurrentUrl() + "/InvoicePrintT1?id=" + id + "&renderAs=html";
				sh.goToUrl(invoicePreviewUrl);
				
				page.validateInvoicePdfValues(invoice);
				
				sh.goToUrl(home);
			}
			else{
				String id = sh.getIDFromCurrentURL();
				String invoicePreviewUrl = sh.getApexBaseFromCurrentUrl() + "/InvoicePrintT1?id=" + id + "&renderAs=html";
				sh.goToUrl(invoicePreviewUrl);
			
				page.validateInvoicePdfValues(invoice);
				
				sh.goToUrl(home);
			}
		}};
	}
	
	public static Stage clickKimbleLogo(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			new InvoicePageC(sh).clickKimbleLogo(sh);
		}};
	}
	
	public static Stage sendInvoiceForApproval(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			new InvoicePageC(sh).SendInvoiceForApproval(invoice);
		}};
	}
	
	public static Stage dispatchInvoice(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			new InvoicePageC(sh).DispatchInvoice(invoice);
		}};
	}
	
	public static Stage createRevenueItemAdjustments(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, Collection<RevenueItemAdjustment> adjustments) {
		Stage stage = new Stage(parentStage, testStage);
		for(RevenueItemAdjustment a : adjustments) {
			navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement dashboard", sh, salesOppName, a.deliveryEngagementName);
			if(a.deliveryElement != null)
				navigateToRevenueAdjustments(stage, "navigate to revenue adjustments for element: " + a.deliveryElement, sh, salesOppName, a);
			else
				navigateToRevenueAdjustments(stage, "navigate to revenue adjustments: ", sh);
			createRevenueItemAdjustment(stage, "create revenue item adjustment: " + a.description, sh, a);
			sendRevenueItemAdjustmentForApproval(stage, "send revenue item adjustment: " + a.description + " for approval", sh);
			runAllJobs(stage, "run jobs after creating an adjustment", sh);
		}
		return stage;
	}
	
	public static Stage navigateToRevenueAdjustments(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryEngagementPageC.NavigateToRevenueAdjustments(sh);
		}};
	}
	
	public static Stage navigateToRevenueAdjustments(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, RevenueItemAdjustment adjustment) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement dashboard to get element: " + adjustment.deliveryElement + " id", sh, salesOppName, adjustment.deliveryEngagementName);
		getDeliveryElementId(stage, "get the delivery element id", sh, adjustment);
		navigateToRevenueAdjustment(stage, "navigate to revenue adjustments", sh, adjustment);
		return stage;
	}
	
	public static Stage getDeliveryElementId(Stage parentStage, String testStage, SeleniumHelper sh, RevenueItemAdjustment adjustment) {
		return new Stage(parentStage, testStage) { public void run() {
			adjustment.deliveryElementId = DeliveryEngagementPageC.getElementId(sh, adjustment.deliveryElement);
		}};
	}
	
	public static Stage navigateToRevenueAndCosts(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryEngagementPageC.navigateToRevenueAndCosts(sh);
		}};
	}
	
	public static Stage navigateToRevenueAdjustment(Stage parentStage, String testStage, SeleniumHelper sh, RevenueItemAdjustment adjustment) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryEngagementPageC.NavigateToRevenueAdjustments(sh, adjustment.deliveryElementId);
		}};
	}
	
	public static Stage createRevenueItemAdjustment(Stage parentStage, String testStage, SeleniumHelper sh, RevenueItemAdjustment adjustment) {
		return new Stage(parentStage, testStage) { public void run() {
			new RevenueItemAdjustmentPageC(sh).CreateNew(adjustment);
		}};
	}

	public static Stage sendRevenueItemAdjustmentForApproval(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			new RevenueItemAdjustmentPageC(sh).SendForApproval();
		}};
	}

	public static Stage createSupplierRequisitions(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, Collection<SupplierRequisition> requisitions) {
		Stage stage = new Stage(parentStage, testStage);
		for(SupplierRequisition r : requisitions) {
			if(r.stage != null && stage.getRootStageName().equals(r.stage))
				continue;
			navigateToSupplierRequisitions(stage, "navigate to supplier requisitions for element: " + r.deliveryElement, sh, salesOppName, r);
			startSupplierRequisitionCreateWizard(stage, "start creation wizard", sh, r);
			if (r.noSupperlierReq == false){
				createSupplierRequisition(stage, "create supplier requisition for supplier: " + r.supplier + " and element: " + r.deliveryElement, sh, r);
				getSupplierRequisitionReference(stage, "get requisition reference", sh, r);
				navigateToSupplierRequisitionDetails(stage, "view requisition", sh, r);
				submitSupplierRequisition(stage, "send supplier requisition for approval", sh);
				runAllJobs(stage, "run jobs after submitting supplier requisition", sh);
			}
		}
		return stage;
	}
	
	public static Stage navigateToSupplierRequisitions(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryEngagementPageC.NavigateToSupplierRequisitions(sh);
		}};
	}
	
	public static Stage navigateToSupplierRequisitions(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, SupplierRequisition requisition) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement dashboard to get element: " + requisition.deliveryElement + " id", sh, salesOppName, requisition.engagement);
		getDeliveryElementId(stage, "get the delivery element id", sh, requisition);
		navigateToSupplierRequisition(stage, "navigate to revenue adjustments", sh, requisition);
		return stage;
	}
	
	public static Stage getDeliveryElementId(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			requisition.deliveryElementId = DeliveryEngagementPageC.getElementId(sh, requisition.deliveryElement);
		}};
	}
	
	public static Stage navigateToSupplierRequisition(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryEngagementPageC.NavigateToSupplierRequisitions(sh, requisition.deliveryElementId);
		}};
	}
	
	public static Stage navigateToSupplierRequisitionDetails(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			if (requisition.noSupperlierReq == false){
			DeliveryElementSupplierRequisitionsPageC.navigateToRequisition(sh, requisition.reference);
			}
		}};
	}
	
	public static Stage navigateToSupplierRequisitionResultsDetails(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisitionResult requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryElementSupplierRequisitionsPageC.navigateToRequisition(sh, requisition.reference);
		}};
	}
	
	public static Stage startSupplierRequisitionCreateWizard(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryElementSupplierRequisitionsPageC.setSupplierRequisitionPurchaseOrderRule(sh, requisition);
			if (requisition.noSupperlierReq == false){
			DeliveryElementSupplierRequisitionsPageC.startCreateWizard(sh);
			}
		}};
	}

	public static Stage createSupplierRequisition(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryElementSupplierRequisitionsPageC.create(requisition, sh);
		}};
	}

	public static Stage getSupplierRequisitionReference(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			requisition.reference = DeliveryElementSupplierRequisitionsPageC.getReference(sh);
			sh.assertNotEquals(requisition.reference, null, "supplier requisition reference");
		}};
	}

	public static Stage submitSupplierRequisition(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryElementSupplierRequisitionsPageC.submit(sh);
		}};
	}
	
	public static Stage validateSupplierRequisitions(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, List<SupplierRequisition> requisitions) {
		Stage stage = new Stage(parentStage, testStage);
		for(int i = 0; i < requisitions.size(); i++)
			validateSupplierRequisition(stage, "validate supplier requisition: " + i, sh, salesOppName, requisitions.get(i), i);
		return stage;
	}
	
	public static Stage validateSupplierRequisitionResults(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, List<SupplierRequisition> requisitions, List<SupplierRequisitionResult> expectedRes) {
		Stage stage = new Stage(parentStage, testStage);		
		for(int i = 0; i < requisitions.size(); i++){
				navigateToSupplierRequisitions(stage, "navigate to supplier requisitions for element: " + requisitions.get(i).deliveryElement, sh, salesOppName, requisitions.get(i));
				validateSupplierRequisitionOverviewTable(stage, "postSupplierRequisitionCreation", sh, expectedRes.get(i));
				navigateToSupplierRequisitionDetails(stage, "navigate to requisition details", sh, requisitions.get(i));			
				validateSupplierRequisitionResultsDetails(stage, "validate supplier requisition: " + requisitions.get(i) + " details", sh, expectedRes.get(i));			
				}
			return stage;	
	}
	
	

	
	public static Stage validateSupplierRequisition(Stage parentStage, String testStage, SeleniumHelper sh, String salesOppName, SupplierRequisition requisition, int index) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement dashboard to get element: " + requisition.deliveryElement + " id", sh, salesOppName, requisition.engagement);
		getDeliveryElementId(stage, "get the delivery element id", sh, requisition);		
		navigateToSupplierRequisition(stage, "navigate to supplier requisitions for element: " + requisition.deliveryElement, sh, requisition);
		validateSupplierRequisitionOverview(stage, "validate supplier requisition: " + index + " overview", sh, requisition);
		navigateToSupplierRequisitionDetails(stage, "navigate to requisition: " + index, sh, requisition);
		validateSupplierRequisitionDetails(stage, "validate supplier requisition: " + index + " details", sh, requisition);
		return stage;
	}
	
	public static Stage validateSupplierRequisitionOverview(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryElementSupplierRequisitionsPageC.validateOverview(sh, requisition);
		}};
	}

	public static Stage validateSupplierRequisitionOverviewTable(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisitionResult requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			if (requisition.noSupperlierReq == false){
			DeliveryElementSupplierRequisitionsPageC.validateOverviewTable(sh, requisition);
			}
		}};
	}
	
	public static Stage validateSupplierRequisitionDetails(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisition requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			DeliveryElementSupplierRequisitionsPageC.validateDetails(sh, requisition);
		}};
	}
	
	public static Stage validateSupplierRequisitionResultsDetails(Stage parentStage, String testStage, SeleniumHelper sh, SupplierRequisitionResult requisition) {
		return new Stage(parentStage, testStage) { public void run() {
			if (requisition.noSupperlierReq == false){
			DeliveryElementSupplierRequisitionsPageC.validateSupplierRequisitionDetails(sh, requisition);
			}
		}};
	}
	
	public static Stage createSupplierInvoices(Stage parentStage, String testStage, SeleniumHelper sh, Collection<SupplierInvoice> invoices) {
		Stage stage = new Stage(parentStage, testStage);
		for(SupplierInvoice i : invoices)
			createSupplierInvoice(stage, "create supplier invoice : " + i.reference, sh, i);
		return stage;
	}
	
	public static Stage createSupplierInvoices(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		Stage stage = new Stage(parentStage, testStage);
			createSupplierInvoice(stage, "create supplier invoice : " + invoice.reference, sh, invoice);
		return stage;
	}

	public static Stage createSupplierInvoice(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		Stage stage = new Stage(parentStage, testStage);
		if(sh.isLightning()){
			StagesZ.navigateFromAnywhereToTab(stage, "lightning: navigate to supplier invoice", sh, "Supplier Invoices");
		}
		else{
			navigateFromAnywhereToSupplierInvoices(stage, "navigate to supplier invoices", sh);
		}
		
		
		if (invoice.allowSupplierInvoiceWithoutReq == true){
				startCreateSupplierInvoiceNoRequsition(stage, "start supplier invoice create wiz no req", sh, invoice);	
				supplierInvoiceCreateInWizNoReq(stage, "supplier invoice create wiz step 1 no req", sh, invoice);
				validateSupplierInvoicePdfValuesNoReq(stage, "validate supplier invoice pdf no req", sh, invoice);
				findSupplierInvoiceRefAndApprove(stage, "find reference and approve the supplier invoice", sh);
			}
		else if(invoice.resourceBatchGenerated == true) {
				navigateToResources(stage, "navigate to Resources", sh);
				clickGoInListView(stage, "click go to view all resources", sh);
				selectResourceandGenerateSupplierInvoice(stage, "select resource and click gnerate supplier invoice", sh, invoice);
				selectAssignmentToSupplierInvoice(stage, "select assignment to include on the supplier invoice", sh, invoice);
				openSupplierInvoice(stage, "navigate to newly created supplier invoice", sh, invoice);
				validateSupplierInvoicePdfValuesNoReq(stage, "validate supplier invoice pdf", sh, invoice);
				
			}
		else if(invoice.accountBatchGenerated == true) {
				navigateToAccounts(stage, "navigate to accountss tab", sh);
				selectAccountandGenerateSupplierInvoice(stage, "select account and click gnerate supplier invoice", sh, invoice);
				selectAssignmentToSupplierInvoice(stage, "select assignment to include on the supplier invoice", sh, invoice);
				openSupplierInvoice(stage, "navigate to newly created supplier invoice", sh, invoice);
				validateSupplierInvoicePdfValuesNoReq(stage, "validate supplier invoice pdf", sh, invoice);
		}
     	else{
				startCreateSupplierInvoice(stage, "start supplier invoice create wiz", sh, invoice);
				supplierInvoiceCreateInWiz(stage, "supplier invoice create wiz step 1", sh, invoice);
			}
		return stage;
	}
	
	public static Stage startCreateSupplierInvoice(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			SupplierInvoicePageC.startCreateWiz(sh, invoice);
		}};
	}
	
	public static Stage startCreateSupplierInvoiceNoRequsition(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			SupplierInvoicePageC.startCreateWizNoReq(sh, invoice);
		}};
	}

	public static Stage supplierInvoiceCreateInWiz(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			SupplierInvoicePageC.runCreateWizStep1(sh, invoice);
			SupplierInvoicePageC.runCreateWizStep2(sh, invoice);
			SupplierInvoicePageC.runCreateWizStep3(sh, invoice);
			SupplierInvoicePageC.runCreateWizStep4(sh, invoice);
		}};
	}
	
	public static Stage supplierInvoiceCreateInWizNoReq(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
	//		SupplierInvoicePageC.runCreateWizStep1NoReq(sh, invoice);
			SupplierInvoicePageC.runCreateWizStep2NoReq(sh, invoice);
			SupplierInvoicePageC.runCreateWizStep3NoReq(sh, invoice);
			SupplierInvoicePageC.runCreateWizStep4NoReq(sh, invoice);
		}};
	}

	public static Stage validateSupplierInvoices(Stage parentStage, String testStage, SeleniumHelper sh, Collection<SupplierInvoice> invoices) {
		Stage stage = new Stage(parentStage, testStage);
		for(SupplierInvoice i : invoices)
			validateSupplierInvoice(stage, "validate supplier invoice: " + i.reference, sh, i);
		return stage;
	}
	
	public static Stage validateSupplierInvoice(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		Stage stage = new Stage(parentStage, testStage);
		if(sh.isLightning()){
			StagesZ.navigateFromAnywhereToListViewItem(stage, "lightning: navigate to supplier invoice", sh, "Supplier Invoices", invoice.reference);
		}
		else{
			navigateFromAnywhereToSupplierInvoices(stage, "navigate to supplier invoices", sh);
			navigateToSupplierInvoice(stage, "navigate to supplier invoice: " + invoice.reference, sh, invoice.reference);
		}
		validateSupplierInvoicePdfValues(stage, "validate supplier invoice: " + invoice.reference + " pdf values", sh, invoice);
		return stage;
	}
	
	public static Stage validateSupplierInvoicePdfValues(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			String home = sh.getCurrentUrl();
			String id = sh.getIDFromCurrentURL();
			String invoicePreviewUrl = sh.getApexBaseFromCurrentUrl() + "/SupplierInvoicePrintT2?id=" + id + "&renderAs=html";
			sh.goToUrl(invoicePreviewUrl);
			
			SupplierInvoicePageC.validateInvoicePdfValues(sh, invoice);
			
			sh.goToUrl(home);
		}};
	}
	
	public static Stage validateSupplierInvoicePdfValuesNoReq(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			String home = sh.getCurrentUrl();
			String id = sh.getIDFromCurrentURL();
			String invoicePreviewUrl = sh.getApexBaseFromCurrentUrl() + "/SupplierInvoicePrintT2?id=" + id + "&renderAs=html";
			sh.goToUrl(invoicePreviewUrl);
			
			SupplierInvoicePageC.validateInvoicePdfValuesNoReq(sh, invoice, home);
			
		
			
		}};
	}
	
	public static Stage validateSupplierCreditNotePdfValues(Stage parentStage, String testStage, SeleniumHelper sh, CreditNote creditNote) {
		return new Stage(parentStage, testStage) { public void run() {
			String home = sh.getCurrentUrl();
			String id = sh.getIDFromCurrentURL();
			String invoicePreviewUrl = sh.getApexBaseFromCurrentUrl() + "/SupplierCreditNotePrintT2?id=" + id + "&renderAs=html";
			sh.goToUrl(invoicePreviewUrl);
			
			CreditNotePageC.validateSupplierCreditNotePdfValues(sh, creditNote, home);
		}};
	}
	public static Stage findSupplierInvoiceRefAndApprove(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {

		
			String id = sh.getIDFromCurrentURL();
			String supplierInvoiceHome = sh.getApexBaseFromCurrentUrl() + "/SupplierInvoice?id=" + id;
			sh.goToUrl(supplierInvoiceHome);
					
			SupplierInvoicePageC.approveSupplierInvoice(sh);
			SupplierInvoicePageC.findSupplierInvoiceReference(sh);
			
		
		}};
	}
	
	public static Stage createCreditNotes(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Invoice> invoices) {
		Stage stage = new Stage(parentStage, testStage);
		for (Invoice i : invoices)
			if(hasInvoiceToCredit(i.creditNotes, stage.getRootStageName()))
				createCreditNotes(stage, "create credit notes for invoice: "  + i, sh, i);
		return stage;
	}
	
	public static Stage createSupplierCreditNotes(Stage parentStage, String testStage, SeleniumHelper sh, Collection<SupplierInvoice> invoices) {
		Stage stage = new Stage(parentStage, testStage);
		for (SupplierInvoice i : invoices)
			if(hasInvoiceToCredit(i.creditNotes, stage.getRootStageName()))
				createSupplierCreditNotes(stage, "create credit notes for invoice: "  + i, sh, i);
		return stage;
	}
	
	public static Stage createSupplierCreditNotes(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		Stage stage = new Stage(parentStage, testStage);
		for(CreditNote cn : invoice.creditNotes) {
			navigateToSupplierInvoices(stage, "navigate to invoices", sh, invoice);
			openSupplierInvoice(stage, "open supplier invoice", sh, invoice);
			createSupplierCreditNote(stage, "create a supplier credit note for invoice", sh, invoice, cn);
			getSupplierCreditNoteReference(stage, "get supplier credit note reference", sh, cn);

			openCreditNote(stage, "open supplier credit note", sh, cn);
			validateSupplierCreditNotePdfValues(stage, "validate supplier credit note pdf", sh, cn);
			
		}
		return stage;
	}
	
	public static Stage createCreditNotes(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
		Stage stage = new Stage(parentStage, testStage);
		for(CreditNote cn : invoice.creditNotes) {
			if(cn.stage != null && !cn.stage.equals(stage.getRootStageName()))
				continue;
			navigateToInvoices(stage, "navigate to invoices", sh, invoice);
			if(!sh.isLightning())
				openInvoice(stage, "open invoice", sh, invoice);
			createCreditNote(stage, "create a credit note for invoice", sh, invoice, cn);
			getCreditNoteReference(stage, "get credit note reference", sh, cn);

			openCreditNote(stage, "open credit note", sh, cn);
			
			sendCreditNoteForApproval(stage, "send credit note for approval", sh, cn);
			dispatchCreditNote(stage, "dispatch credit note", sh, cn);
		}
		return stage;
	}
	
	public static Stage navigateToInvoices(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
		Stage stage = new Stage(parentStage, testStage);
		if(sh.isLightning())
			return StagesZ.navigateToInvoiceFromList(stage, testStage, sh, invoice);
		else
			return navigateToInvoicesC(stage, "navigate to invoices", sh);
	}
	
	public static Stage navigateToSupplierInvoices(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		Stage stage = new Stage(parentStage, testStage);
			return navigateToSupplierInvoicesC(stage, "navigate to supplier invoices", sh);
	}
	
	public static Stage navigateToInvoicesC(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			new InvoicePageC(sh).NavigateToList();
		}};
	}
	
	public static Stage navigateToSupplierInvoicesC(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) { public void run() {
			new SupplierInvoicePageC().NavigateToList(sh);
		}};
	}
	
	public static Stage openInvoice(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			new InvoicePageC(sh).OpenInvoice(invoice);
		}};
	}
	
	public static Stage openSupplierInvoice(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice) {
		return new Stage(parentStage, testStage) { public void run() {
			new SupplierInvoicePageC().OpenSupplierInvoice(sh, invoice);
		}};
	}
	
	public static Stage createCreditNote(Stage parentStage, String testStage, SeleniumHelper sh, Invoice invoice, CreditNote creditNote) {
		return new Stage(parentStage, testStage) { public void run() {
			new CreditNotePageC(sh).CreateNew(invoice, creditNote);
		}};
	}
	
	public static Stage createSupplierCreditNote(Stage parentStage, String testStage, SeleniumHelper sh, SupplierInvoice invoice, CreditNote creditNote) {
		return new Stage(parentStage, testStage) { public void run() {
			new CreditNotePageC(sh).CreateNewSupplierCn(sh, invoice, creditNote);
		}};
	}
	
	public static Stage getCreditNoteReference(Stage parentStage, String testStage, SeleniumHelper sh, CreditNote creditNote) {
		return new Stage(parentStage, testStage) { public void run() {
			new InvoicePageC(sh).GetCreditNoteReference(creditNote);
		}};
	}
	
	public static Stage getSupplierCreditNoteReference(Stage parentStage, String testStage, SeleniumHelper sh, CreditNote creditNote) {
		return new Stage(parentStage, testStage) { public void run() {
			new SupplierInvoicePageC().GetSupplierCreditNoteReference(sh, creditNote);
		}};
	}
	
	public static Stage openCreditNote(Stage parentStage, String testStage, SeleniumHelper sh, CreditNote creditNote) {
		return new Stage(parentStage, testStage) { public void run() {
			new CreditNotePageC(sh).OpenCreditNote(creditNote);
		}};
	}
	
	public static Stage sendCreditNoteForApproval(Stage parentStage, String testStage, SeleniumHelper sh, CreditNote creditNote) {
		return new Stage(parentStage, testStage) { public void run() {
			new CreditNotePageC(sh).SendCreditNoteForApproval(creditNote);
		}};
	}
	
	public static Stage dispatchCreditNote(Stage parentStage, String testStage, SeleniumHelper sh, CreditNote creditNote) {
		return new Stage(parentStage, testStage) { public void run() {
			new CreditNotePageC(sh).DispatchCreditNote(creditNote);
		}};
	}
	
	public static Stage createJourneys(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Journey> journeys, String resourceName) {
		Stage stage = new Stage(parentStage, testStage);
		for(Journey j : journeys) {
	
			navigateToResources(stage, "navigate to Resources", sh);
			navigateToResourceHome(stage, "navigate to resource " + resourceName + " home", sh, resourceName);			
			navigateToResourceJourney(stage, "navigate to Resources", sh);			
			createJourney(stage, "create journey: " + j.name, sh, j);
		}
		return stage;
	}
	
	public static Stage createJourney(Stage parentStage, String testStage, SeleniumHelper sh, Journey journey) {
		Stage stage = new Stage(parentStage, testStage) {
			public void run() {
				JourneysPageC.enterNameAndFinishWizard(sh, journey.name);
			}
		};
		
		createJourneyLegs(stage, "create legs", sh, journey.legs);
		
		return stage;
	}
	
	public static Stage createJourneyLegs(Stage parentStage, String testStage, SeleniumHelper sh, Collection<JourneyLeg> journeyLegs) {
		Stage stage = new Stage(parentStage, testStage);
		for(JourneyLeg jl : journeyLegs)
			createJourneyLeg(stage, "create leg from " + jl.departingFrom + " to " + jl.arrivingAt, sh, jl);
		return stage;
	}
	
	public static Stage createJourneyLeg(Stage parentStage, String testStage, SeleniumHelper sh, JourneyLeg journeyLeg) {
		return new Stage(parentStage, testStage) {
			public void run() {
				JourneysPageC.addLeg(sh, journeyLeg);
			}
		};
	}
	
	public static Stage enterAllowanceAdjustments(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Journey> journeys) {
		Stage stage = new Stage(parentStage, testStage);
		for(Journey j : journeys) {
			navigateFromAnywhereToTab(stage, "navigate to journeys", sh, "Journeys");
			openJourney(stage, "open journey: " + j.name, sh, j.name);
			enterAllowanceAdjustments(stage, "enter allowance adjustments for journey " + j.name, sh, j.adjustments);
		}
		return stage;
	}
	
	public static Stage enterAllowanceAdjustmentsFromJourney(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Journey> journeys) {
		Stage stage = new Stage(parentStage, testStage);
		for(Journey j : journeys) {
			enterAllowanceAdjustments(stage, "enter allowance adjustments for journey " + j.name, sh, j.adjustments);
		}
		return stage;
	}
		
	public static Stage openJourney(Stage parentStage, String stageName, SeleniumHelper sh, String journeyName) {
		return new Stage(parentStage, stageName){ public void run() {
			sh.clickLink(By.xpath("//a[normalize-space(text())=\"" + journeyName + "\"]"));
		}};
	}
	
	public static Stage enterAllowanceAdjustments(Stage parentStage, String testStage, SeleniumHelper sh, List<JourneyAllowanceAdjustment> adjustments) {
		Stage stage = new Stage(parentStage, testStage) {
			public void run() {
				JourneysPageC.calculateAllowances(sh); 
			}
		};
		for(JourneyAllowanceAdjustment aa : adjustments)
			enterAllowanceAdjustment(stage, "enter allowance adjustments for period " + aa.period, sh, aa);
		return stage;
	}
	
	public static Stage enterAllowanceAdjustment(Stage parentStage, String testStage, SeleniumHelper sh, JourneyAllowanceAdjustment adjustment) {
		return new Stage(parentStage, testStage) {
			public void run() {
				JourneysPageC.adjustAllowance(sh, adjustment); 
			}
		};
	}
	
	public static Stage validateAllowanceAmounts(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Journey> journeys) {
		Stage stage = new Stage(parentStage, testStage);
		for(Journey j : journeys) {
			if(j.allowanceAmountValidations == null || j.allowanceAmountValidations.get(stage.getRootStageName()) == null)
				continue;
			navigateFromAnywhereToTab(stage, "navigate to journeys", sh, "Journeys");
			openJourney(stage, "open journey: " + j.name, sh, j.name);
			validateAllowanceAmounts(stage, "validate allowance amounts for journey " + j.name, sh, j.allowanceAmountValidations.get(stage.getRootStageName()));
		}
		return stage;
	}
	
	public static Stage validateAllowanceAmounts(Stage parentStage, String testStage, SeleniumHelper sh, Map<String, String> amounts) {
		return new Stage(parentStage, testStage) {
			public void run() {
				JourneysPageC.validateAmounts(sh, amounts); 
			}
		};
	}

	public static Stage enterAllowanceAllocations(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Journey> journeys) {
		Stage stage = new Stage(parentStage, testStage);
		for(Journey j : journeys) {
			navigateFromAnywhereToTab(stage, "navigate to journeys", sh, "Journeys");
			openJourney(stage, "open journey: " + j.name, sh, j.name);
			enterAllowanceAllocations(stage, "enter allowance allocations for journey " + j.name, sh, j.allocations);
		}
		return stage;
	}
	
	public static Stage enterAllowanceAllocations(Stage parentStage, String testStage, SeleniumHelper sh, List<JourneyAllowanceAllocation> allocation) {
		Stage stage = new Stage(parentStage, testStage);
		for(JourneyAllowanceAllocation aa : allocation)
			enterAllowanceAllocation(stage, "enter allowance allocation for period " + aa.period, sh, aa);
		return stage;
	}
	
	public static Stage enterAllowanceAllocation(Stage parentStage, String testStage, SeleniumHelper sh, JourneyAllowanceAllocation allocation) {
		return new Stage(parentStage, testStage) {
			public void run() {
				JourneysPageC.allocateAllowance(sh, allocation); 
			}
		};
	}
	
	public static Stage createExpenseItemsAndValidate(Stage parentStage, String testStage, SeleniumHelper sh, Collection<Journey> journeys, boolean submit) {
		Stage stage = new Stage(parentStage, testStage);
		for(Journey j : journeys) {
			navigateFromAnywhereToTab(stage, "navigate to journeys", sh, "Journeys");
			openJourney(stage, "open journey: " + j.name, sh, j.name);
			createExpenseItemsAndValidate(stage, "create expense items for journey " + j.name + " and validate", sh, j, submit);
		}
		return stage;
	}
	
	public static Stage createExpenseItemsAndValidate(Stage parentStage, String testStage, SeleniumHelper sh, Journey journey, boolean submit) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToJourney(stage, "navigate to journey: " + journey.name, sh, journey.name);
		createExpenseItemsForJourney(stage, "create expense items", sh);
		if(journey.expectedExpenseEntries != null)
			for(ExpenseEntry entry : journey.expectedExpenseEntries)
				validateExpenseEntries(stage, "validate expense claim for journey activity: " + entry.activityName, sh, journey, entry);
		if(submit)
			for(ExpenseEntry entry : journey.expectedExpenseEntries)
				saveAndSubmitExpenseClaim(stage, "submit expense claim for activity: " + entry.activityName, sh, journey.name, entry.activityName);
		return stage;
	}
	
	public static Stage saveAndSubmitExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, String journeyName, String activityName) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToJourneyExpenseClaim(stage, "navigate to submit expense claim for activity: " + activityName, sh, journeyName, activityName, false);
		saveAndSubmitExpenseClaim(stage, "save and submit expense items", sh);
		return stage;
	}
	
	public static Stage validateExpenseEntries(Stage parentStage, String testStage, SeleniumHelper sh, Journey journey, ExpenseEntry entry) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToJourneyExpenseClaim(stage, "navigate to validate expense claim for activity: " + entry.activityName, sh, journey.name, entry.activityName, false);
		validateExpenseEntries(stage, "validate expense items", sh, journey.resourceName, entry);
		return stage;
	}
	
	public static Stage navigateToJourneyExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, String journeyName, String activityName, boolean isSubmitted) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToJourney(parentStage, "navigate from anywhere to journey: " + journeyName, sh, journeyName);
		navigateFromJourneyToExpenseClaim(parentStage, "navigate to expense claim for activity: " + activityName, sh, activityName, isSubmitted);
		return stage;
	}
	
	public static Stage navigateToJourney(Stage parentStage, String testStage, SeleniumHelper sh, String journeyName) {
		Stage stage = new Stage(parentStage, testStage);
		navigateFromAnywhereToTab(stage, "navigate to journeys", sh, "Journeys");
		openJourney(stage, "open journey: " + journeyName, sh, journeyName);
		return stage;
	}
	
	public static Stage createExpenseItemsForJourney(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				JourneysPageC.createExpenseItems(sh);
			}
		};
	}
	
	public static Stage navigateFromJourneyToExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, String activity, boolean isSubmitted) {
		return new Stage(parentStage, testStage) {
			public void run() {
				if(!isSubmitted)
					JourneysPageC.navigateToExpenseClaim(sh, activity);
				else
					JourneysPageC.navigateToSubmittedExpenseClaim(sh, activity);
			}
		};
	}
	
	public static Stage validateExpenseEntries(Stage parentStage, String testStage, SeleniumHelper sh, String resource, ExpenseEntry entry) {
		return new Stage(parentStage, testStage) {
			public void run() {
				ExpensePageC.validate(sh, resource, entry);
			}
		};
	}
	
	public static Stage updateHighLevelForecastStage(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement) {
		return new Stage(parentStage, testStage) {
			public void run() { 
				DeliveryEngagementPageC.updateHighLevelForecast(sh, engagement);
			}
		};
	}
	
	public static Stage navigateAndUpdateForecastStatusStage(Stage parentStage, String testStage, SeleniumHelper sh, String toStatus) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToMaintainOpportunitySummary(stage, "navigate to maintain opportunity", sh);
		updateForecastStatusAndSave(stage, "update forecast status and save", sh, toStatus);
		return stage;		
	}
	
	public static Stage updateForecastStatusAndSave(Stage parentStage, String testStage, SeleniumHelper sh, String toStatus) {
		return new Stage(parentStage, testStage) {
			public void run() { 
				SalesOpportunityPageC.updateForecastStatusAndSave(sh, toStatus);
			}
		};
	}
	
	public static Stage navigateToMaintainOpportunitySummary(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() { 
				SalesOpportunityPageC.NavigateToMaintainOpportunitySummary(sh);
			}
		};
	}
	
	public static Stage navigateAndUpdateCloseDate(Stage parentStage, String testStage, SeleniumHelper sh, String closeDate) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToMaintainOpportunitySummary(stage, "navigate to maintain opportunity summary", sh);
		updateCloseDateStage(stage, "update close date", sh, closeDate);
		return stage;
	}
	
	public static Stage updateCloseDateStage(Stage parentStage, String testStage, SeleniumHelper sh, String closeDate) {
		return new Stage(parentStage, testStage) {
			public void run() { 
				SalesOpportunityPageC.updateCloseDateAndSave(sh, closeDate);
			}
		};
	}
	
	public static Stage navigateAndUpdateBusinessUnit(Stage parentStage, String testStage, SeleniumHelper sh, String businessUnit) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToMaintainOpportunitySummary(stage, "navigate to maintain opportunity summary", sh);
		updateBusinessUnitStage(stage, "update close date", sh, businessUnit);
		return stage;
	}
	
	public static Stage updateBusinessUnitStage(Stage parentStage, String testStage, SeleniumHelper sh, String businessUnit) {
		return new Stage(parentStage, testStage) {
			public void run() { 
				SalesOpportunityPageC.updateBusinessUnitAndSave(sh, businessUnit);
			}
		};
	}
	
	// What if scenarios
	
	public static Stage createWhatIfScenario(Stage parentStage, String testStage, SeleniumHelper sh, Map<String, WhatIfScenario> scenariosMap) {
		Stage stage = new Stage(parentStage, testStage);
		WhatIfScenario scenario = scenariosMap.get(stage.getRootStageName());
		if(scenario == null)
			return doNothing(stage, "no what if scenario available for stage: " + stage.getRootStageName());
//		runAllJobs(stage, "run all jobs before creating what if scenario", sh);
		navigateFromAnywhereToTab(stage, "navigate to what if scenarios list", sh, "What-If Scenarios");
		createWhatIfScenario(stage, "create what if scenario: " + scenario.name, sh, scenario);
		return stage;
	}
	
	public static Stage createWhatIfScenario(Stage parentStage, String testStage, SeleniumHelper sh, WhatIfScenario scenario) {
		Stage stage = new Stage(parentStage, testStage);
		sh.closeLightningPopUp();
		startWhatIfScenarioCreateWizard(stage, "click new", sh);
		inputWhatIfScenarioValues(stage, "input scenario values and save", sh, scenario);
		acceptWhatIfScenario(stage, "accept what if scenario: " + scenario.name, sh, scenario.name);
		return stage;
	}
	
	public static Stage startWhatIfScenarioCreateWizard(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				WhatIfScenariosPageC.startCreateWizard(sh);
			}
		};
		
	}
	
	public static Stage inputWhatIfScenarioValues(Stage parentStage, String testStage, SeleniumHelper sh, WhatIfScenario scenario) {
		return new Stage(parentStage, testStage) {
			public void run() {
				WhatIfScenariosPageC.inputValuesAndSave(sh, scenario);
			}
		};
		
	}
	
	public static Stage acceptWhatIfScenario(Stage parentStage, String testStage, SeleniumHelper sh, String scenarioName) {
		return new Stage(parentStage, testStage) {
			public void run() {
				WhatIfScenariosPageC.acceptWhatIfScenario(sh, scenarioName);
			}
		};
		
	}
	
	// Interface runs
	
	public static Stage enterTimeWithInterfaceRun(Stage parentStage, String testStage, SeleniumHelper sh, UserCredentials creds, Map<String, TimeEntryWithTimesList> entriesWithTimes) {
		return new Stage(parentStage, testStage) {
			public void run() {
				String interfaceRunId = MobileRequests.submitTimeEntriesWithStartAndEndTime(sh.getWD(), creds, entriesWithTimes.get(this.getRootStageName()).entries);
				sh.LogMessageLine("The inteface run id is: " + interfaceRunId);
			}
		};
	}
	
	// Utility methods
	
	public static boolean hasMilestonesToCreate(Collection<Milestone> milestones, String rootStageName) {
		if(milestones == null)
			return false;
		for(Milestone m : milestones)
			if(m.creationStage == null || m.creationStage.equals(rootStageName))
				return true;
		return false;
	}
	
	public static boolean hasInvoiceToCredit(Collection<CreditNote> creditNotes, String rootStageName) {
		if(creditNotes == null)
			return false;
		for(CreditNote cn : creditNotes)
			if(cn.stage == null || cn.stage.equals(rootStageName))
				return true;
		return false;
	}	
	
	//	Usage Behaviour Rule Edit
	public static Stage updateUsageBehaviourRuleOffset(Stage parentStage, String stageName, SeleniumHelper sh, String UsageBehaviourOffset, String UsageBehaviourName) {
		Stage stage = new Stage(parentStage, stageName);
		navigateFromAnywhereToTab(stage, "navigate to UBR tab", sh, "Usage Behaviour Rules");
		closeLightningpopup(stage, "Close the lighting popup", sh);
		clickGoInListView(stage, "show all UBRs", sh);
		updateUsageBehaviourRuleOffsets(stage, "navigate to UBR", sh, UsageBehaviourOffset, UsageBehaviourName);
		return stage;
	}
	
	public static Stage updateUsageBehaviourRuleOffsets(Stage parentStage, String testStage, SeleniumHelper sh, String UsageBehaviourOffset, String UsageBehaviourName) {		
		return new Stage(parentStage, testStage) {
			public void run() {
				 OtherResourcedActivityPageC.usageBehaviourRuleClick(sh, UsageBehaviourName);
				 OtherResourcedActivityPageC.usageBehaviourRuleOffsetEdit(sh, UsageBehaviourOffset, UsageBehaviourName);
			}
		};
	}		

	// seed new jobs
	public static Stage seedCustomJob(Stage parentStage, String stageName, SeleniumHelper sh, String jobName, String jobStatus, DeliveryEngagement engagement, DeliveryElement element) {
		Stage stage = new Stage(parentStage, stageName);
		for(ActivityAssignment assignment : element.activityAssignments){
			navigateToEngagementDashboardByName(stage, "navigate to engagement", sh, engagement.name);
			navigateToAssignments(stage, "navigate to assignments", sh);
//			getAssignmentID(stage, "get ID ", sh, jobName, jobStatus, assignment);	
			getElementAssignmentID(stage, "get ID ", sh, jobName, jobStatus, assignment);	
		}
		runAllJobs(stage, "run all jobs after after seeding new job", sh);
		return stage;
	}	
	
	public static Stage getAssignmentID(Stage parentStage, String testStage, SeleniumHelper sh, String jobName, String jobStatus, ActivityAssignment assignment) {		
		return new Stage(parentStage, testStage) {
			public void run() {			
				ActivityAssignmentPageC.getAssignmentId(sh, jobName, jobStatus, assignment);
			}
		};
	}			
	
	public static Stage getDeliveryAssignmentsIdAndTimePeriodAndSeedCustomJob(Stage parentStage, String stageName, SeleniumHelper sh, String jobName, String jobStatus, DeliveryEngagement engagement, ActivityAssignment assignments, List<Timesheet> timeEntries) {
		Stage stage = new Stage(parentStage, stageName);
		for(Timesheet ts : timeEntries){		
			if(ts.testStage == null || stage.getRootStageName().equals(ts.testStage)) {
				navigateToEngagementDashboardByName(stage, "navigate to engagement", sh, engagement.name);
				navigateToAssignments(stage, "navigate to assignments", sh);
				getElementAssignmentID(stage, "get ID ", sh, jobName, jobStatus, assignments);
				navigateToResourceTimesheet(stage, "navigate to timesheet for resource: " + ts.resourceName, sh, ts.resourceName);
				retrieveTrackingPeriodId(stage, "enter timesheet for resource: " + ts.resourceName, sh, ts);	
				encodeJobArgument(stage, "encode job argument", sh);
				navigateToJobpageandSeedjob(stage, "seed job using encoded value", sh, jobName, jobStatus);		
				runAllJobs(stage, "run all jobs after after seeding jobs", sh);
			}
		}
	
		return stage;
	}
	public static Stage getElementAssignmentID(Stage parentStage, String testStage, SeleniumHelper sh, String jobName, String jobStatus, ActivityAssignment assignment) {		
		return new Stage(parentStage, testStage) {
			public void run() {			
				ActivityAssignmentPageC.getAssignmentIdFromActivityAssignments(sh, jobName, jobStatus, assignment);
			}
		};
	}	

	// seed new jobs from other activity assignment
	public static Stage getOtherActivityIdAndTimePeriodIdAndSeedCustomJob(Stage parentStage, String stageName, SeleniumHelper sh, String jobName, String jobStatus, String activityName, String res) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToOtherActivitiesList(stage, "nav to other activities", sh);
		navigateToOtherActivityFromList(stage, "nav to: " + activityName, sh, activityName);
		navigateAndGetOtherActivityAssignmentIdAndSeedJob(stage, "get ID for: " + res, sh, jobName, jobStatus, res);
		encodeJobArgument(stage, "encode job argument", sh);
		navigateToJobpageandSeedjob(stage, "seed job using encoded value", sh, jobName, jobStatus);		
		runAllJobs(stage, "run all jobs after after seeding jobs", sh);
		return stage;
	}
	
	public static Stage navigateAndGetOtherActivityAssignmentIdAndSeedJob(Stage parentStage, String testStage, SeleniumHelper sh, String jobName, String jobStatus, String resourceName) {		
		return new Stage(parentStage, testStage) {
			public void run() {			
				OtherResourcedActivityPageC.getOtherActivityAssignmentId(sh, jobName, jobStatus, resourceName);
			}
		};
	}	
	public static Stage navigateToJobpageandSeedjob(Stage parentStage, String testStage, SeleniumHelper sh, String jobName, String jobStatus) {		
		return new Stage(parentStage, testStage) {
			public void run() {			
				JobsNewPageC.seedEncodedJob(sh, jobName, jobStatus);
			}
		};
	}	
	

	public static Stage encodeJobArgument (Stage parentStage, String testStage, SeleniumHelper sh) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToJobAdministration(stage, "navigate to job administration page", sh);
		encodeJobArgumentViaJobAdministration(stage, "encode job argument to allow job to be manually seeded", sh);
		return stage;
	}	
	
	//tax code
	public static Stage editInvoiceTaxCode(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryEngagement engagement, DeliveryElement element, Integer row) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardByName(stage, "navigate to engagement dashboard", sh, engagement.name);
		editTaxCode(stage, "edit tax code", sh, element, row);
		return stage;
	}	
	
	public static Stage editTaxCode(Stage parentStage, String testStage, SeleniumHelper sh, DeliveryElement element, Integer row) {
		return new Stage(parentStage, testStage) { public void run() {
			new InvoicePageC(sh).EditTaxCode(element, row);
		}};
	}
	
	public static Stage editToilFactor(Stage parentStage, String stageName, SeleniumHelper sh, String salesOppName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement: " + engagement.name, sh, salesOppName, engagement.name);
		navigateMenu(stage, "navigate to delivery group activities", sh, PagesC.DELIVERYGROUPACTIVITIES);
		for(ResourcedActivity a : engagement.activities)
			editPeriodRateBand(stage, "edit activity rate band", sh, a);
		return stage;
	}

	public static Stage editPeriodRateBand(Stage parentStage, String stageName, SeleniumHelper sh, ResourcedActivity activity) {
		return new Stage(parentStage, stageName){
			public void run() {
				DeliveryGroupActivitiesPageC.editActivityPeriodRateBand(sh, activity);
			}
		};
	}
	

	public static Stage validateUsageAdjustments(Stage parentStage, String testStage, SeleniumHelper sh, ResourceUsageAdjustment res) {
		Stage stage = new Stage(parentStage, testStage);
		runAllJobs(stage, "run all jobs", sh);
		navigateToResourceHome(stage, "navigate to resource " + res.resourceName + " home", sh, res.resourceName);
		navigateToUsageAdjustments(stage, "validate usage adjustments", sh);
		validateUsageAdjustment(stage, "validate usage adjustments", sh, res);
		return stage;
	}
	
	public static Stage navigateToUsageAdjustments(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new ResourcePageC(sh).NavigateToResourceUsageAdjustmentFromHome();
			}
		};
	}
	
	public static Stage validateUsageAdjustment(Stage parentStage, String testStage, SeleniumHelper sh, ResourceUsageAdjustment res) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new ResourcePageC(sh).validateUsageAdjustment(res);
			}
		};
	}
	
	public static Stage deleteTemplatedAssignment(Stage parentStage, String testStage, SeleniumHelper sh, String resourceName, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, testStage);
		navigateToEngagementDashboardByName(stage, "navigate to engagement", sh, engagement.name);
		navigateToAssignments(stage, "navigate to assignments", sh);
		deleteAssignment(stage, "navigate to assignments", sh, resourceName);
		
		return stage;
	}	
	
	public static Stage deleteAssignment(Stage parentStage, String testStage, SeleniumHelper sh, String resourceName) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new ActivityAssignmentPageC(sh).deleteAssignment(resourceName);
			}
		};
	}	
	
	public static Stage mobileEnterTimesheets(Stage parentStage, String testStage, SeleniumHelper sh, List<Timesheet> ts, AppiumDriver driver, MobileSelectors selectors, String OS) {
		Stage stage = new Stage(parentStage, testStage);
		syncMobile(stage, "sync mobile", sh, driver, selectors, OS);
		for(int i = 0; i < ts.size(); i++){
			mobileEnterTimesheet(stage, "enter timesheet: "+ts.get(i), sh, ts.get(i), driver, selectors, OS);
		}
		return stage;
	}	
	
	public static Stage mobileEnterTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet timesheetDetails, AppiumDriver driver, MobileSelectors selectors, String OS) {
		Stage stage = new Stage(parentStage, testStage);
		syncMobile(stage, "sync mobile", sh, driver, selectors, OS);
		for(int i = 0; i < timesheetDetails.timeEntries.size(); i++){
			mobileScrollToPeriod(stage, "navigate to time period", sh, timesheetDetails, driver, selectors);
			addTimeEntry(stage, "add time", sh, driver, timesheetDetails, selectors, OS);
			createTimeEntry(stage, "enter details", sh, driver, selectors, timesheetDetails.timeEntries.get(i), OS);
		}
		return stage;
	}	
	
	public static Stage mobileScrollToPeriod(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet timesheetDetails, AppiumDriver driver, MobileSelectors selectors) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					new MobileHomePage(sh).scrollToTimePeriod(sh, timesheetDetails, driver, selectors);
				} catch (ParseException e) {
					throw new RuntimeException("Mobile: Failed to locate tracking period", e);
				}
			}
		};
	}	
	
	public static Stage switchToMobileTimeView(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS) {
		Stage stage = new Stage(parentStage, testStage);
		switchToTimeView(stage, "switch to time view", sh, driver, selectors, OS);
		return stage;
	}	
	
	public static Stage switchToTimeView(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).switchToTimeView(sh, driver, selectors, OS);
			}
		};
	}	
	
	public static Stage addTimeEntry(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, Timesheet timesheetDetails, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).addTimeEntry(sh, driver, timesheetDetails, selectors, OS);
			}
		};
	}	
	
	public static Stage syncMobile(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).mobileSync(sh, driver, selectors, OS);
			}
		};
	}
	
	public static Stage submitAll(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).submitAll(sh, driver, selectors, OS);
			}
		};
	}
	
	public static Stage submitSelectedTimeEntry(Stage parentStage, String testStage, SeleniumHelper sh, List<TimeEntry> te, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).submitSelectedTimeEntry(sh, te, driver, selectors, OS);
			}
		};
	}
	
	public static Stage uncheckAndSubmitTime(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).uncheckFridayAndSubmitTime(sh, driver, selectors, OS);
			}
		};
	}
	
	public static Stage deleteAllTimeEntries(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).deleteTimeEntries(sh, driver, selectors, OS);
			}
		};
	}
	
	public static Stage createTimeEntry(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, TimeEntry timeEntry, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					new MobileTimeEntry(sh).createTimeEntry(sh, timeEntry, driver, selectors, OS);
				} catch (ParseException e) {
					throw new RuntimeException("Mobile: Failed to enter time", e);
				}
			}
		};
	}
	
	public static Stage mobileEnterExpenses(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseClaim expense, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		Stage stage = new Stage(parentStage, testStage);
		syncMobile(stage, "sync mobile", sh, driver, selectors, OS);
		switchToExpensesView(stage, "switch to expenses view", sh, driver, selectors, OS);
		for(int i = 0; i < expense.expenseEntries.size(); i++){
			addExpenseClaim(stage, "add expense", sh, driver, selectors, OS);
			createExpenseClaim(stage, "enter expense details", sh, expense.expenseEntries.get(i), expense.expenseEntries.get(i).expenseDetails.get(0), driver, selectors, OS);
		}
		return stage;
	}	
	
	public static Stage addExpenseToExistingEntry(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseEntry entry, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		Stage stage = new Stage(parentStage, testStage);
		switchToExpensesView(stage, "switch to expenses view", sh, driver, selectors, OS);
		addToExistingEntry(stage, "add to existing entry", sh, entry, driver, selectors, OS);
		enterAdditionalExpenseDetails(stage, "enter details", sh, entry, detail, driver, selectors, OS);
		return stage;
	}		
	
	public static Stage editExistingExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseEntry entry, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		Stage stage = new Stage(parentStage, testStage);
		switchToExpensesView(stage, "switch to expenses view", sh, driver, selectors, OS);
		selectClaimToEdit(stage, "edit claim", sh, entry, detail, driver, selectors, OS);
		editClaimDetails(stage, "enter details", sh, entry, detail, driver, selectors, OS);
		return stage;
	}
	
	public static Stage editExistingTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet ts, TimeEntry te, AppiumDriver driver, MobileSelectors selectors, String OS) {
		Stage stage = new Stage(parentStage, testStage);
		switchToTimeView(stage, "switch to time view", sh, driver, selectors, OS);
		mobileScrollToPeriod(stage, "navigate to time period", sh, ts, driver, selectors);
		selectTimeToEdit(stage, testStage, sh, ts, te, driver, selectors, OS);
		editTimesheetDetails(stage, "edit timesheet", sh, ts, te, driver, selectors, OS);
		return stage;
	}	
	
	public static Stage actualiseMobileTime(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet ts, TimeEntry te, AppiumDriver driver, MobileSelectors selectors, String OS) {
		Stage stage = new Stage(parentStage, testStage);
		switchToTimeView(stage, "switch to time view", sh, driver, selectors, OS);
		mobileScrollToPeriod(stage, "navigate to time period", sh, ts, driver, selectors);
		selectTimeToEdit(stage, testStage, sh, ts, te, driver, selectors, OS);
		actualise(stage, "edit timesheet", sh, te, driver, selectors);
		return stage;
	}		
	
	public static Stage actualise(Stage parentStage, String testStage, SeleniumHelper sh, TimeEntry te, AppiumDriver driver, MobileSelectors selectors) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileTimeEntry(sh).clickDone(sh, te, driver, selectors);
			}
		};
	}	
	
	public static Stage editTimesheetDetails(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet ts, TimeEntry te, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileTimeEntry(sh).editTimesheetDetails(sh, ts, te, driver, selectors, OS);
			}
		};
	}				
	
	public static Stage selectTimeToEdit(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet ts, TimeEntry te, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).editTimeEntry(sh, ts, te, driver, selectors, OS);
			}
		};
	}	
	
	public static Stage editClaimDetails(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseEntry expense, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					new MobileExpenseEntry(sh).editClaimDetails(sh, expense, detail, driver, selectors, OS);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		};
	}		
	
	public static Stage selectClaimToEdit(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseEntry entry, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).editExpenseClaim(sh, entry, detail, driver, selectors, OS);
			}
		};
	}	
	
	public static Stage enterAdditionalExpenseDetails(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseEntry expense, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					new MobileExpenseEntry(sh).addExpenseToExistingClaim(sh, expense, detail, driver, selectors, OS);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		};
	}	
	
	public static Stage addToExistingEntry(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseEntry entry, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).addExpenseToExistingEntry(sh, entry, driver, selectors, OS);
			}
		};
	}	
	
	public static Stage selectExistingExpense(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseEntry entry, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).selectExpense(sh, entry, driver, selectors, OS);
			}
		};
	}		
	
	public static Stage validateExpenseTaxCode(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileExpenseEntry(sh).validateTaxCode(sh, detail, driver, selectors, OS);
			}
		};
	}
	
	public static Stage submitSelectedExpense(Stage parentStage, String testStage, SeleniumHelper sh, List<ExpenseEntry> expense, ExpenseEntry entryToSubmit, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).submitSelectedExpenseClaim(sh, expense, entryToSubmit, driver, selectors, OS);
			}
		};
	}		
	
	public static Stage submitAllExpense(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).submitAll(sh, driver, selectors, OS);
			}
		};
	}		
	
	public static Stage switchToExpensesView(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).switchToExpensesView(sh, driver, selectors, OS);
			}
		};
	}		
	
	public static Stage addExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileHomePage(sh).addExpense(sh, driver, selectors, OS);
			}
		};
	}		
	
	public static Stage createExpenseClaim(Stage parentStage, String testStage, SeleniumHelper sh, ExpenseEntry expense, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				try {
					new MobileExpenseEntry(sh).createExpenseClaim(sh, expense, detail, driver, selectors, OS);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		};
	}		
	
	public static Stage verifyMobileTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet ts, AppiumDriver driver, MobileSelectors selectors, String OS) {
		Stage stage = new Stage(parentStage, testStage);
		mobileScrollToPeriod(stage, "navigate to time period", sh, ts, driver, selectors);
		validateTimesheet(stage, "validate time entry hours", sh, driver, selectors, ts, OS);
		return stage;
	}		
	
	public static Stage validateTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, Timesheet ts, String OS) {
		return new Stage(parentStage, testStage) {
			public void run() {
				new MobileTimeEntry(sh).verifyTime(sh, driver, selectors, ts, OS);
			}
		};
	}	
	
	public static Stage setTimeAndExpenseApprovalRules(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName);
			navigateToEngagementDashboardByName(stage, "navigate to engagemet: " + engagement.name, sh, engagement.name);
			navigateMenu(stage, "navigate to revenue and costs", sh, PagesC.APPROVALRULES);
			updateApprovalRule(stage, "update approval rules", sh, engagement);
		return stage;
	}
	
	public static Stage updateApprovalRule(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		return new Stage(parentStage, stageName) {
			public void run() { DeliveryEngagementPageC.UpdateApprovalRules(sh, engagement); }
		};
	}	
	
	public static Stage setActivityUsageUnits(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement, String unit) {
		Stage stage = new Stage(parentStage, stageName);
			navigateToEngagementDashboardByName(stage, "navigate to engagement: " + engagement.name, sh, engagement.name);
			navigateMenu(stage, "navigate to activities", sh, PagesC.DELIVERYGROUPACTIVITIES);
			updateActiviyUsageUnits(stage, "update usage units", sh, engagement, unit);
		return stage;
	}
	
	public static Stage savePeriodRateBand(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		Stage stage = new Stage(parentStage, stageName);
			navigateToEngagementDashboardByName(stage, "navigate to engagement: " + engagement.name, sh, engagement.name);
			navigateMenu(stage, "navigate to activities", sh, PagesC.DELIVERYGROUPACTIVITIES);
			saveRateBand(stage, "update usage units", sh, engagement);
		return stage;
	}
	
	public static Stage saveRateBand(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement) {
		return new Stage(parentStage, stageName) {
			public void run() { DeliveryEngagementPageC.openAndSaveRateBand(sh, engagement); }
		};
	}
	
	public static Stage updateActiviyUsageUnits(Stage parentStage, String stageName, SeleniumHelper sh, DeliveryEngagement engagement, String unit) {
		return new Stage(parentStage, stageName) {
			public void run() { DeliveryEngagementPageC.UpdateActivityUsageUnits(sh, engagement, unit); }
		};
	}
	
	public static Stage rejectTimesheet(Stage parentStage, String stageName, SeleniumHelper sh, Timesheet ts) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToTimesheetAndReject(stage, "navigate to timesheet and reject", sh, ts);
		return stage;
	}
	
	public static Stage approveTimesheet(Stage parentStage, String stageName, SeleniumHelper sh, Timesheet ts) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToTimesheetAndApprove(stage, "navigate to timesheet and approve", sh, ts);
		return stage;
	}
	
	public static Stage navigateToTimesheetAndReject(Stage parentStage, String stageName, SeleniumHelper sh, Timesheet ts) {
		return new Stage(parentStage, stageName) {
			public void run() { TimesheetPageC.navigateToTimesheetAndReject(sh, ts); }
		};
	}	
	
	public static Stage navigateToTimesheetAndApprove(Stage parentStage, String stageName, SeleniumHelper sh, Timesheet ts) {
		return new Stage(parentStage, stageName) {
			public void run() { TimesheetPageC.navigateToTimesheetAndApprove(sh, ts); }
		};
	}	

	public static Stage reenterTimesheet(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet ts, TimeEntry te) {
		Stage stage =  new Stage(parentStage, testStage);
		navigateFromAnywhereToAllTabs(stage, "nav to all tabs", sh);
		navigateToResourceTimesheet(stage, "navigate to timesheet for resource: " + ts.resourceName, sh, ts.resourceName);
		editAndSubmitRejectedTimesheet(stage, "enter timesheet for resource: " + ts.resourceName, sh, ts, te);
		return stage;
	}
	
	public static Stage editAndSubmitRejectedTimesheet(Stage parentStage, String stageName, SeleniumHelper sh, Timesheet ts, TimeEntry te) {
		return new Stage(parentStage, stageName) { public void run() {  
			try {
				new TimesheetPageC(sh).editAndSubmitRejectedTimesheet(sh, ts, te);
			} catch(Exception e) {
				throw new RuntimeException("Failed to enter timesheet for resource: " + ts.resourceName, e);
			}
		}};
	}	
	
	public static Stage rejectExpenseClaim(Stage parentStage, String stageName, SeleniumHelper sh, ExpenseClaim ex) {
		Stage stage = new Stage(parentStage, stageName);
		navigateToExpenseClaimAndReject(stage, "navigate to Expense and reject", sh, ex);
		return stage;
	}
	
	public static Stage navigateToExpenseClaimAndReject(Stage parentStage, String stageName, SeleniumHelper sh, ExpenseClaim ex) {
		return new Stage(parentStage, stageName) {
			public void run() { ExpensePageC.navigateToExpenseClaimAndReject(sh, ex); }
		};
	}	
	
	public static Stage navigateToExpenseClaimAndApprove(Stage parentStage, String stageName, SeleniumHelper sh, ExpenseClaim ex) {
		return new Stage(parentStage, stageName) {
			public void run() { ExpensePageC.navigateToExpenseClaimAndApprove(sh, ex); }
		};
	}			

	public static Stage createExpenseTaxCode(Stage parentStage, String testStage, SeleniumHelper sh, String exCategory, DeliveryEngagement e, SalesOpportunity salesOpp) {
		Stage stage =  new Stage(parentStage, testStage);
		navigateToEngagementDashboardViaSalesOpp(stage, "navigate to engagement " + e.name + " dashboard", sh, salesOpp.name, e.name);
		navigateToExpenseCategories(stage, "navigate to expense cats", sh);
		setExpenseCategoryTaxCode(stage, "set expense tax code", sh, exCategory);
		return stage;
	}
	
	public static Stage navigateToExpenseCategories(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() { DeliveryEngagementPageC.navigateToExpenseCategories(sh); }
		};
	}	
	
	public static Stage setExpenseCategoryTaxCode(Stage parentStage, String stageName, SeleniumHelper sh, String exCategory) {
		return new Stage(parentStage, stageName) {
			public void run() { ExpensePageC.setTaxCodeOnCategory(sh, exCategory); }
		};
	}			

	public static Stage navigateAndCheckExpenseCategoryTaxCode(Stage parentStage, String testStage, SeleniumHelper sh, String exCategory, Boolean check) {
		Stage stage =  new Stage(parentStage, testStage);
		navigateFromAnywhereToAllTabs(stage, "nav to all tabs", sh);
		navigateFromAllTabsToTab(stage, "navigate to expense categories", sh, "Expense Categories");
		checkExpenseCategoryTaxCode(stage, "enable tax code on category", sh, exCategory, check);
		return stage;
	}
	
	public static Stage checkExpenseCategoryTaxCode(Stage parentStage, String stageName, SeleniumHelper sh, String exCategory, Boolean check) {
		return new Stage(parentStage, stageName) {
			public void run() { ExpensePageC.checkTaxCodeOnExpenseCategory(sh, exCategory, check); }
		};
	}	
	
	public static Stage pendingApprovalsTimesheet(Stage parentStage, String stageName, SeleniumHelper sh, String activityName, Boolean approveTrue) {
		Stage stage = new Stage(parentStage, stageName);
			navigateFromAnywhereToTab(stage, "navigate to Pending Approvals", sh, "Pending Approvals");
			approveRejectTimesheetPending(stage, "approve or reject", sh, activityName, approveTrue);
		return stage;
	}
	
	public static Stage approveRejectTimesheetPending(Stage parentStage, String stageName, SeleniumHelper sh, String activityName, Boolean approveTrue) {
		return new Stage(parentStage, stageName) {
			public void run() { PendingApprovalsPageC.approveOrRejectTimesheet(sh, activityName, approveTrue); }
		};
	}
	
	public static Stage pendingApprovalsExpense(Stage parentStage, String stageName, SeleniumHelper sh, String activityName, Boolean approveTrue) {
		Stage stage = new Stage(parentStage, stageName);
			navigateFromAnywhereToTab(stage, "navigate to Pending Approvals", sh, "Pending Approvals");
			approveRejectExpensePending(stage, "approve or reject", sh, activityName, approveTrue);
		return stage;
	}
	
	public static Stage approveRejectExpensePending(Stage parentStage, String stageName, SeleniumHelper sh, String activityName, Boolean approveTrue) {
		return new Stage(parentStage, stageName) {
			public void run() { PendingApprovalsPageC.approveOrRejectExpense(sh, activityName, approveTrue); }
		};
	}		

	public static Stage submitInterfaceRun(Stage parentStage, String testStage, SeleniumHelper sh, String interfaceType, String data) {
		Stage stage =  new Stage(parentStage, testStage);
			interfaceRunDataEntry(stage, "import data through interface runs", sh, interfaceType, data);
		return stage;
	}
	
	public static Stage interfaceRunDataEntry(Stage parentStage, String stageName, SeleniumHelper sh, String interfaceType, String data) {
		return new Stage(parentStage, stageName) {
			public void run() { InterfaceRunPageC.enterInterfaceRun(sh, interfaceType, data); }
		};
	}
	
	public static Stage overrideOrgDate(Stage parentStage, String testStage, SeleniumHelper sh, String date) {
		Stage stage =  new Stage(parentStage, testStage);
		overrideCurrentDate(stage, "override org date to: " + date, sh, date);
		return stage;
	}
	
	public static Stage overrideCurrentDate(Stage parentStage, String stageName, SeleniumHelper sh, String date) {
		return new Stage(parentStage, stageName) {
			public void run() { ConfigurationSettingsPageC.overrideDateService(sh, date); }
		};
	}
	
	public static Stage enableTravelReqConfigSetting(Stage parentStage, String testStage, SeleniumHelper sh, String setting) {
		Stage stage =  new Stage(parentStage, testStage);
		enableSetting(stage, "Enable Config Setting: " + setting, sh, setting);
		return stage;
	}
	
	public static Stage enableSuppInvoiceWithoutReq(Stage parentStage, String testStage, SeleniumHelper sh, String setting) {
		Stage stage =  new Stage(parentStage, testStage);
		enableSettingSuppInvoiceWithoutReq(stage, "Enable Config Setting: " + setting, sh, setting);
		return stage;
	}
	
	public static Stage enablePartialSuppCreditNotes(Stage parentStage, String testStage, SeleniumHelper sh, String setting) {
		Stage stage =  new Stage(parentStage, testStage);
		enableSettingPartialSupplierCreditNote(stage, "Enable Config Setting: " + setting, sh, setting);
		return stage;
	}
	
	public static Stage enableGenerateSupplierInvoiceRef(Stage parentStage, String testStage, SeleniumHelper sh, String setting) {
		Stage stage =  new Stage(parentStage, testStage);
		enableSettingGenerateSupplierInvoiceRef(stage, "Enable Config Setting: " + setting, sh, setting);
		return stage;
	}
	
	public static Stage enableSettingSuppInvoiceWithoutReq(Stage parentStage, String stageName, SeleniumHelper sh, String setting) {
		return new Stage(parentStage, stageName) {
			public void run() { ConfigurationSettingsPageC.enableSuppInvoiceWithoutReqConfigSetting(sh, setting); }
		};
	}
	
	public static Stage enableSettingPartialSupplierCreditNote(Stage parentStage, String stageName, SeleniumHelper sh, String setting) {
		return new Stage(parentStage, stageName) {
			public void run() { ConfigurationSettingsPageC.enablePartialSuppCreditNoteConfigSetting(sh, setting); }
		};
	}
	
	public static Stage enableSettingGenerateSupplierInvoiceRef(Stage parentStage, String stageName, SeleniumHelper sh, String setting) {
		return new Stage(parentStage, stageName) {
			public void run() { ConfigurationSettingsPageC.enableGenerateSupplierInvoiceRefConfigSetting(sh, setting); }
		};
	}
	
	public static Stage enableSetting(Stage parentStage, String stageName, SeleniumHelper sh, String setting) {
		return new Stage(parentStage, stageName) {
			public void run() { ConfigurationSettingsPageC.enableTravelReqConfigSetting(sh, setting); }
		};
	}
	
	public static Stage disableTravelReqConfigSetting(Stage parentStage, String testStage, SeleniumHelper sh, String setting) {
		Stage stage =  new Stage(parentStage, testStage);
		disableSetting(stage, "Disable Config Setting: " + setting, sh, setting);
		return stage; 
	}
	
	public static Stage disableGenerateSupplierInvoiceRef(Stage parentStage, String testStage, SeleniumHelper sh, String setting) {
		Stage stage =  new Stage(parentStage, testStage);
		disableSettingGenerateSupplierInvoiceRef(stage, "Disable Config Setting: " + setting, sh, setting);
		return stage;
	}
	
	public static Stage disableEnablePartialSuppCreditNotes(Stage parentStage, String testStage, SeleniumHelper sh, String setting) {
		Stage stage =  new Stage(parentStage, testStage);
		disableSettingEnablePartialSuppCreditNotes(stage, "Disable Config Setting: " + setting, sh, setting);
		return stage;
	}
	
	public static Stage disableSuppInvoiceWithoutReq(Stage parentStage, String testStage, SeleniumHelper sh, String setting) {
		Stage stage =  new Stage(parentStage, testStage);
		disableSettingSuppInvoiceWithoutReq(stage, "Disable Config Setting: " + setting, sh, setting);
		return stage;
	}
	
	public static Stage disableSettingGenerateSupplierInvoiceRef(Stage parentStage, String stageName, SeleniumHelper sh, String setting) {
		return new Stage(parentStage, stageName) {
			public void run() { ConfigurationSettingsPageC.disableGenerateSupplierInvoiceRefConfigSetting(sh, setting); }
		};
	}
	
	public static Stage disableSettingEnablePartialSuppCreditNotes(Stage parentStage, String stageName, SeleniumHelper sh, String setting) {
		return new Stage(parentStage, stageName) {
			public void run() { ConfigurationSettingsPageC.disablePartialSuppCreditNoteConfigSetting(sh, setting); }
		};
	}
	
	public static Stage disableSettingSuppInvoiceWithoutReq(Stage parentStage, String stageName, SeleniumHelper sh, String setting) {
		return new Stage(parentStage, stageName) {
			public void run() { ConfigurationSettingsPageC.disableSuppInvoiceWithoutReqConfigSetting(sh, setting); }
		};
	}
	
	public static Stage disableSetting(Stage parentStage, String stageName, SeleniumHelper sh, String setting) {
		return new Stage(parentStage, stageName) {
			public void run() { ConfigurationSettingsPageC.disableTravelReqConfigSetting(sh, setting); }
		};
	}
	
	public static Stage addSupplierProductsToAccount(Stage parentStage, String testStage, SeleniumHelper sh, List<SupplierProduct> suppProduct) {
		Stage stage = new Stage(parentStage, testStage);
		for(int i = 0; i < suppProduct.size(); i++){
			addSupplierProducts(stage, "add product", sh, suppProduct.get(i));
		}
		return stage;
	}	
	
	public static Stage addSupplierProducts(Stage parentStage, String stageName, SeleniumHelper sh, SupplierProduct suppProduct) {
		return new Stage(parentStage, stageName) {
			public void run() { AccountPageC.addSupplierProducts(sh, suppProduct);}
		};
	}
	
	public static Stage createTravelRequisitions(Stage parentStage, String testStage, SeleniumHelper sh, TravelRequisition travelReq) {
		Stage stage =  new Stage(parentStage, testStage);
		createTravelReq(stage, "create travel req for: " + travelReq.resource, sh, travelReq);
		return stage;
	}
	
	public static Stage createTravelReq(Stage parentStage, String stageName, SeleniumHelper sh, TravelRequisition travelReq) {
		return new Stage(parentStage, stageName) {
			public void run() { TravelRequisitionPageC.createNewTravelRequisition(sh, travelReq); }
		};
	}
	
	public static Stage updateTravelRequestItinerary(Stage parentStage, String testStage, SeleniumHelper sh, TravelRequisition travelReq, List<TravelRequisitionItinerary> itinerary) {
		Stage stage =  new Stage(parentStage, testStage);
		navigateFromAnywhereToTab(stage, "nav to travel req", sh, "Travel Requisitions");
		clickGoInListView(stage, "navigate to travel req", sh);
		navigateFromTravelReqsToTravelReq(stage, "nav to travel req", sh, travelReq.name);
		updateItinerary(stage, "update itinerary", sh, travelReq.name);
		for(int i = 0; i < itinerary.size(); i++)
			updateItinerary(stage, "update itinerary", sh, travelReq, itinerary.get(i));
		saveItinerary(stage, "save", sh);
		return stage;
	}
	
	public static Stage updateItinerary(Stage parentStage, String stageName, SeleniumHelper sh, TravelRequisition travelReq, TravelRequisitionItinerary itin) {
		return new Stage(parentStage, stageName) {
			public void run() { TravelRequisitionPageC.updateItineraryOptions(sh, travelReq.name, itin); }
		};
	}
	
	public static Stage saveItinerary(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() { TravelRequisitionPageC.saveItineraryOptions(sh); }
		};
	}

	public static Stage navigateFromTravelReqsToTravelReq(Stage parentStage, String stageName, SeleniumHelper sh, String travelReqName) {
		return new Stage(parentStage, stageName) {
			public void run() { 
				sh.clickLink(By.partialLinkText(travelReqName));
			}
		};
	}

	public static Stage updateItinerary(Stage parentStage, String stageName, SeleniumHelper sh, String travelReqName) {
		return new Stage(parentStage, stageName) {
			public void run() { 
				sh.clickLink(By.xpath("//input[@value='Update Itinerary Options']"));
			}
		};
	}


	
	public static Stage bookTravelRequisition(Stage parentStage, String testStage, SeleniumHelper sh, TravelRequisition travelReq, TravelRequisitionBooking booking) {
		Stage stage =  new Stage(parentStage, testStage);
		navigateFromAnywhereToTab(stage, "nav to travel req", sh, "Travel Requisitions");
		clickGoInListView(stage, "navigate to travel req", sh);
		navigateFromTravelReqsToTravelReq(stage, "nav to travel req", sh, travelReq.name);
		bookTravelReq(stage, "create travel req for: " + travelReq.resource, sh, travelReq, booking);
		return stage;
	}
	
	public static Stage bookTravelReq(Stage parentStage, String stageName, SeleniumHelper sh, TravelRequisition travelReq, TravelRequisitionBooking booking) {
		return new Stage(parentStage, stageName) {
			public void run() { TravelRequisitionPageC.selectTravelOptions(sh, booking); }
		};
	}
	
	public static Stage requestItineraryForRequisition(Stage parentStage, String testStage, SeleniumHelper sh) {
		Stage stage =  new Stage(parentStage, testStage);
		requestItinerary(stage, "request itinerary", sh);
		return stage;
	}
	
	public static Stage requestItinerary(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() { TravelRequisitionPageC.requestItinerary(sh); }
		};
	}
	
	public static Stage submitTravelRequisitionRequestForApproval(Stage parentStage, String testStage, SeleniumHelper sh) {
		Stage stage =  new Stage(parentStage, testStage);
		submitTravelRequestForApproval(stage, "submit", sh);
		return stage;
	}
	
	public static Stage submitTravelRequestForApproval(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() { TravelRequisitionPageC.submitRequestForApproval(sh); }
		};
	}
	
	public static Stage validateTimesheetSubmissionSilentApproval(Stage parentStage, String testStage, SeleniumHelper sh, Timesheet ts) {
		Stage stage =  new Stage(parentStage, testStage);
			verifySilentJobsTimesheetApproval(stage, "verify timesheet approved", sh, ts);
		return stage;
	}
	
	public static Stage verifySilentJobsTimesheetApproval(Stage parentStage, String stageName, SeleniumHelper sh, Timesheet ts) {
		return new Stage(parentStage, stageName) {
			public void run() { TimesheetPageC.verifyApproval(sh, ts); }
		};
	}
	
	public static Stage manuallyUpdateDeliveryDates(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage =  new Stage(parentStage, testStage);
			navigateFromAnywhereToSalesOpp(stage, "navigate to sales opp", sh, salesOpp.name);
			manuallyUpdateDeliveryDatesStage(stage, "update delivery dates", sh, salesOpp);
		return stage;
	}
	
	public static Stage manuallyUpdateDeliveryDatesStage(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) {
		return new Stage(parentStage, stageName) {
			public void run() { SalesOpportunityPageC.manuallyUpdateDeliveryDates(sh, salesOpp); }
		};
	}
	
	public static Stage automaticallyUpdateDeliveryDates(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage =  new Stage(parentStage, testStage);
			navigateFromAnywhereToSalesOpp(stage, "navigate to sales opp", sh, salesOpp.name);
			automaticallyUpdateDeliveryDatesStage(stage, "update delivery dates", sh, salesOpp);
		return stage;
	}
	
	public static Stage automaticallyUpdateDeliveryDatesStage(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) {
		return new Stage(parentStage, stageName) {
			public void run() { SalesOpportunityPageC.automaticallyUpdateDeliveryDates(sh, salesOpp); }
		};
	}
	
	public static Stage archiveElementInScopePage(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName) {
		Stage stage =  new Stage(parentStage, testStage);
			navigateToEngagement(stage, "navigate to engagement", sh, engagementName);
			archiveElement(stage, "archive element", sh);
		return stage;
	}
	
	public static Stage navigateToEngagement(Stage parentStage, String stageName, SeleniumHelper sh, String engagementName) {
		return new Stage(parentStage, stageName) {
			public void run() { DeliveryEngagementPageC.navigateToEngagement(sh, engagementName); }
		};
	}
	
	public static Stage archiveElement(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() { DeliveryElementPageC.hideDeliveryElementInLDV(sh); }
		};
	}
	
	public static Stage testPageLoads(Stage parentStage, String testStage, SeleniumHelper sh, String engagementName) {
		Stage stage =  new Stage(parentStage, testStage);
			navigateToEngagement(stage, "navigate to engagement", sh, engagementName);
			testEngagementPages(stage, "test engagement pages load", sh);
		return stage;
	}
	
	public static Stage testEngagementPages(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() { DeliveryEngagementPageC.TestEngagementPageLoadingLDV(sh); }
		};
	}
	
	public static Stage verifyNoFailedJobs(Stage parentStage, String testStage, SeleniumHelper sh) {
		Stage stage =  new Stage(parentStage, testStage);
			navigateToJobAdministration(stage, "navigate to job administration page", sh);
			verifyFailedJobs(stage, "verify there are no failed jobs present in the org", sh);
		return stage;
	}
	
	public static Stage navigateToJobAdministration(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() { JobAdministrationPageC.navigateToJobAdministration(sh); }
		};
	}
	
	public static Stage encodeJobArgumentViaJobAdministration(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() { JobAdministrationPageC.encodeJobArgument(sh); }
		};
	}
	
	public static Stage verifyFailedJobs(Stage parentStage, String stageName, SeleniumHelper sh) {
		return new Stage(parentStage, stageName) {
			public void run() { JobAdministrationPageC.failedJobsExist(sh);}
		};
	}
	
	public static Stage runJobsInLine(Stage parentStage, String testStage, SeleniumHelper sh, SalesOpportunity salesOpp) {
		Stage stage =  new Stage(parentStage, testStage);
			runInLineJobsLDV(stage, "run in line jobs", sh, salesOpp);
		return stage;
	}
	
	public static Stage runInLineJobsLDV(Stage parentStage, String stageName, SeleniumHelper sh, SalesOpportunity salesOpp) {
		return new Stage(parentStage, stageName) {
			public void run() { SalesOpportunityPageC.runInLineJobsLDV(sh, salesOpp); }
		};
	}

	public static Stage waitForSilentJobsToRun(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				sh.sleep(10000);
			}
		};
	}
	
	public static Stage runJobActualiseUsageScheduledJob(Stage parentStage, String testStage, SeleniumHelper sh) {
		return new Stage(parentStage, testStage) {
			public void run() {
				String debugUrl = sh.getApexBaseFromCurrentUrl().replace("apex", "_ui/common/apex/debug/ApexCSIPage");
				sh.goToUrl(debugUrl);
				sh.clickLink(By.xpath("//span[normalize-space(text())='Debug']"));
				sh.clickLink(By.xpath("//div[normalize-space(text())='Open Execute Anonymous Window']"));
				sh.sleep(5000);
				sh.sendKeysWithRetry(By.xpath("//div[@class='CodeMirror-code']"), "test");
			}
		};
		
	}
	
	public static Stage createUnactualisedPerformanceAnalysisRecord(Stage parentStage, String testStage, SeleniumHelper sh, PerformanceAnalysis pa, boolean isNewPA) {
		return new Stage(parentStage, testStage) {
			public void run() { ForecastingPeriodClosePageC.createPARecord(sh, pa, isNewPA); }
		};
	}

	public static Stage verifyErrorInForecastingPeriodCloseStage(Stage parentStage, String testStage, SeleniumHelper sh, List<TimePeriodAction> actions) {
		Stage stage = new Stage(parentStage, testStage);
		runAllJobs(stage, "run all jobs", sh);
		navigateToPeriodManagement(stage, "navigate to forecast period close", sh);
		for(TimePeriodAction a : actions) {
			if(stage.getRootStageName().equals(a.testStage) && a.action.equals("close")) {
				navigateToForecastingPeriod(stage, "navigate to forecasting period: " + a.businessUnitGroup + " [" + a.periodName + "]", sh, a);
				verifyErrorCloseForecastingPeriod(stage, "verify error: forecasting period: " + a.businessUnitGroup + " [" + a.periodName + "]", sh, a);
			}
		}
		return stage;
	}
	
	public static Stage verifyErrorCloseForecastingPeriod(Stage parentStage, String testStage, SeleniumHelper sh, TimePeriodAction action) {
		return new Stage(parentStage, testStage) {
			public void run() { ForecastingPeriodClosePageC.VerifyErrorOnForecastingClose(sh); }
		};
	}

	
}








