package kimble.automation.domain;

import java.util.List;

public class SupplierInvoiceResult {

	public String stage;
	public String purchaseOrder;
	public String supplier;
	public boolean selfBill;
	public String reference;
	public String invoiceDate;
	public String paymentTerm;
	public String businessUnit;
	public String invoiceTemplate;
	public List<String> itemDescriptions;
	public String netAmount;
	public String grossTotal;
	public String taxCode;
	
	public List<InvoiceLine> lines;
	public String invoiceFormat;
	
}
