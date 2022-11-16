package kimble.automation.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kimble.automation.domain.imports.TimeEntryWithTimesList;

public class KimbleData {

	public String loginUser;
	
	public String seedDate;
	
	public List<UserCredentials> userCredentials;
	
	public List<ApprovalProcess> approvalProcesses;
	
	public List<Account> accounts;
	
	public List<Proposal> proposals;
	
	public List<TravelRequisition> travelRequisitions;
	
	public List<BusinessUnitGroup> businessUnitGroups;
	
	public Map<String, ResourceList> resources;

	public List<Timesheet> timesheets;
	
	public List<ActivityAssignmentsMany> ActivityAssignmentsMany;
	
	public Map<String, TimeEntryWithTimesList> timeEntriesWithStartAndEndTimes;
	
	public Map<String, TimeEntryAdjustmentList> timeAdjustmentLists;

	public List<ExpenseClaim> expenseClaims;
	
	public List<ExpenseEntry> expenseEntries;
	
	public List<Invoice> invoices;
	
	public List<RevenueItemAdjustment> revenueItemAdjustments;

	public List<CustomLabel> customLabels;
	
	public List<ProductDomain> productDomains;
	
	public List<ResourcedActivityTemplate> resourcedActivityTemplates;

	public List<ApprovalRuleTemplate> approvalRuleTemplates;
	
	public List<Product> products;
	
	public List<ProductGroup> productGroups;
	
	public List<Proposition> propositions;
	
	public List<PurchaseOrder> purchaseOrders;
	
	public List<TimePeriodAction> trackingPeriodActions;
	
	public List<TimePeriodAction> forecastingPeriodActions;

	public List<Report> reports;
	
	public List<OtherResourcedActivity> otherResourcedActivities;
	
	public List<SupplierRequisition> supplierRequisitions;
	
	public List<SupplierInvoice> supplierInvoices;
	
	public List<ExpectedResult> expectedResults;
	
	public List<Journey> journeys;
	
	public List<InterfaceRun> interfaceRuns;
	
	public PreservedData preserveData = new PreservedData();
	
	public Map<String, WhatIfScenario> whatIfScenarios = new HashMap();
	
	public Map<String, ResourceRateChangeValidation> rateChangeValidations = new HashMap();

	public String loginName;
	
	public List<PerformanceAnalysis> performanceAnalysis;
	
	public ExpectedResult getExpectedResult(String stage) {
		for(ExpectedResult er : expectedResults)
			if(er.testStage.equals(stage))
				return er;
		throw new RuntimeException("No expected results exist for the stage: " + stage);
	}
}