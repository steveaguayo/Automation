package kimble.automation.domain;

import java.util.List;
import java.util.Map;

public class Invoice {
	public String testStage;
	public String deliveryEngagementName;
	public String invoiceValue;
	public String invoiceReference;
	public String businessUnit;
	public String invoicingBusinessUnit;
	public String invoicingAccount;
	public boolean isinternal;
	public boolean selectall;
	
	public String expectedInvoiceValue;
	public String expectedInvoiceNetValue;
	public String expectedInvoiceTaxValue;

	public Double grossTotal;
	public Double subTotal;
	public Double tax;
	public Double taxMulti1;
	public Double taxMulti2;
	public Double taxMulti3;
	public Double taxMulti4;
	public Double taxMulti5;
	
	
	public List<String> deliveryElements;
	public Map<String, JsTreeItemList> invoiceGroups;
	
	public List<PurchaseOrder> purchaseOrders;
	public List<CreditNote> creditNotes;
	
	public List<InvoiceLine> lines;
}
