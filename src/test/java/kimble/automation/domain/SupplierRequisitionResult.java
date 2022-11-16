package kimble.automation.domain;

import java.util.List;

public class SupplierRequisitionResult {

	public String deliveryEngagementName;
	public String deliveryElementName;
	public String supplier;
	public String currency;
	public String netAmount;
	public String requestedBy;
	public String requisitionDate;
	public String billingEntity;
	public String requisitionType;
	public String itemDescription;
	public String reqdUnits;
	public String supplierInvoices;
	public String status;
	public boolean noSupperlierReq;
	
	public String reference;
	public List<SupplierRequisitionResultLine> lines;
}
