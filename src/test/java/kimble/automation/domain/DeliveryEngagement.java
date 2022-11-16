package kimble.automation.domain;

import java.util.List;

public class DeliveryEngagement {
	// link engagement to a test stage
	public String stage;
	
	public boolean isExisting = false;
	
	public String name;
	public String reference;
	public String owner;
	public String productGroup;
	public String forecastStatus;
	public String expectedStartDate;
	public String expectedEndDate;
	public String contractRevenue;
	
	public String deliveryStatus;
	public String lossReason;
	public String lossNarrative;
	public String approvalRule;
	
	public String contractRevenueDisplay;
	
	public List<PurchaseOrder> purchaseOrderAllocations;
	
	public List<DeliveryElement> deliveryElements;
	
	public List<Risk> risks;
	
	public List<Issue> issues;
	
	public List<StatusSummary> statusSummary;
	
	public List<ResourcedActivity> activities;
	
	// OCS Screen
	
	public String startDate;
	public String endDate;
	public String revenue;
}
