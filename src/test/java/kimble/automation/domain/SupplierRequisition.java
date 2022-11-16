package kimble.automation.domain;

import java.util.List;

public class SupplierRequisition {
	
	public String stage;
	public String engagement;
	public String deliveryElement;
	public String deliveryElementId;
	public String reference;
	public String requestedBy;
	public String requisitionDate;
	public String supplier;
	public String supplierInvoicing;
	public String billingEntity;
	public String requisitionType;
	
	// for validation only
	public Double netAmount;
	public String status;
	
	public List<SupplierRequisitionLine> lines;
	public String purchaseOrderRule;
	
	public boolean noSupperlierReq;
	public boolean allocateFullamount;

}
