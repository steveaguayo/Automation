package kimble.automation.domain;

public class ProductDomain {
	//Information
	public String name;
	public String className;
	public boolean isResourced;
	public String revenueGenerationModel;
	public String revenueRecognitionModel;
	public String revenueAllocationModel;
	public String revenueForecastMode;
	public String costGenerationModel;
	public String costRecongnitionModel;
	public String costAllocationModel;
	public String costForecastMode;
	public String usageAllocationModel;
	
	//Invoicing
	public String invoiceMode;
	public boolean enforceReceivableDate;
	public boolean invoicingRequiresCompletion;
	public String invoicePeriodType;
	public String invoiceNumberOfPeriods;
	public String invoicingPoint;
	public String invoicingOffsetPeriodType;
	public String invoicingOffsetPeriods;
	public String invoicingReferenceDataRule;
	public boolean isSupplierRequisitioned;
}
