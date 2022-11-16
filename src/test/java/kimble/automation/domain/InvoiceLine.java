package kimble.automation.domain;

public class InvoiceLine {
//	public InvoiceLine(String aName, InvoiceLineItem... aItems) {
//		name = aName;  items = aItems;
//	}
	public String name;
	
	public String netAmountString;
	public String taxAmountString;
	
	public Double rate;
	public Double units;
	public Double amount;
	public Double tax;
	
	public InvoiceLineItem[] items;
}
