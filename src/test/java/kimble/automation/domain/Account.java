package kimble.automation.domain;

import java.util.List;

public class Account {
	public String name;
	public String type;
	public String invoicingCurrency;
	public String operatingBusinessUnit;
	public String tradingBusinessUnit;
	public String businessUnitGroup;
	public String billingContact;
	public Address billingAddress;
	public Address shippingAddress;
	public String purchaseOrderRule;
	public String industry;
	public String taxCode;
	public String invoiceTemplate;
	public String invoiceFormat;
	public String locale;
	public String accountCurrency;
	public boolean isSupplier;
	public boolean isInternal;
	public boolean isCustomer;
	
	public boolean allowPartItemInvoicing = false;
	
	public List<SalesOpportunity> salesOpportunities;
	public List<ChangeControl> changeControls;
	public List<Opportunity> opportunities;
	public List<Proposal> proposals;
	public List<SupplierProduct> supplierProducts;
}
