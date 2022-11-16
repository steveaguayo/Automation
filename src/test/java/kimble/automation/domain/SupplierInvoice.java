package kimble.automation.domain;

import java.util.List;

public class SupplierInvoice {

	public String stage;
	public String purchaseOrder;
	public String supplier;
	public boolean selfBill;
	public boolean supplierRequisitioned;
	public boolean allowSupplierInvoiceWithoutReq;
	public boolean resourceBatchGenerated;
	public boolean accountBatchGenerated;
	public String reference;
	public String invoiceDate;
	public String paymentTerm;
	public String businessUnit;
	public String invoiceTemplate;
	public List<String> itemDescriptions;
	public String netAmount;
	public String grossTotal;
	public String taxAmount;
	public String taxCode;
	public String supplierResource;
	public String resource;
	public String supplierContact;
	public String deliveryElement;
	
	public List<InvoiceLine> lines;
	public List<CreditNote> creditNotes;
	public String invoiceFormat;
	
	
}
