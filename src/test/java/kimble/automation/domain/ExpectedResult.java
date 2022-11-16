package kimble.automation.domain;

import java.util.List;

import kimble.automation.domain.results.RiskDashboardResults;
import kimble.automation.domain.results.RisksSalesResults;

public class ExpectedResult {
	public String testStage;
	public SalesOpportunityForecast salesOpportunityForecasts;
	public List<ResourceForecast> resourceForecasts;	
	public List<TrackingPlanTotal> trackingPlanTotals;
	
	// Business Unit Trend Analysis
	public List<TrendAnalysis> trendAnalysis;
	
	// Risks
	public SalesOpportunity salesOpportunity;
	public Proposal proposals;
	public List<RisksSalesResults> risksSales;
	public List<RiskDashboardResults> riskDashboards;
	public List<DeliveryEngagement> deliveryEngagements;
	
	public List<SupplierRequisitionResult> supplierRequisitions;
	public List<SupplierInvoice> supplierInvoices;
	public List<ResourceUsageAdjustment> resourceUsageAdjustments;
}
