package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import kimble.automation.domain.ExpectedResult;
import kimble.automation.domain.SupplierRequisition;
import kimble.automation.domain.SupplierRequisitionLine;
import kimble.automation.domain.SupplierRequisitionResult;
import kimble.automation.domain.SupplierRequisitionResultLine;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class DeliveryElementSupplierRequisitionsPageC {
	
	// wizard
	public static final By nextButton = By.xpath("//input[@value='Next']");
	public static final By previousButton = By.xpath("//input[@value='Previous']");
	public static final By finishButton = By.xpath("//input[@value='Finish']");
	public static final By requestedByInput = By.xpath("//label[normalize-space(text())='Requested By']/../following-sibling::td[1]//span[@class='lookupInput']/input");
	public static final By supplierInput = By.xpath("//label[normalize-space(text())='Supplier']/../following-sibling::td[1]//span[@class='lookupInput']/input");
	public static final By requisitionDateInput = By.xpath("//label[normalize-space(text())='Requisition Date']/../following-sibling::td[1]//span[@class='dateInput dateOnlyInput']/input");
	public static final By requisitionTypeSelect = By.xpath("//label[normalize-space(text())='Requisition Type']/../following-sibling::td[1]//select");
	public static final By billingEntitySelect = By.xpath("//label[normalize-space(text())='Billing Entity']/../following-sibling::td[1]//select");
	public static final By requisitionReferenceSpan = By.xpath("//tbody//span[substring(normalize-space(text()), 0, 4)='SR0']");
	public static final By submitButton = By.xpath("//input[@value='Submit For Approval']");
	public static final By cancelRequisitionButton = By.xpath("//input[@value='Cancel Requisition']");
	
	
	// wizard templated selectors
	public static final String confirmItemsTbody = "//tbody[contains(@id, ':tb')]";
	public static final By confirmItemsTable = By.xpath("//table[@class='detailList']/tbody/tr/td");
	public static final String confirmItemsRowElementSpan = confirmItemsTbody + "/tr/td/span[normalize-space(text())=\"{{element}}\"]";
	public static final String confirmItemsRow = confirmItemsRowElementSpan + "/../..";
	public static final String supplierInvoicingInput = confirmItemsRow + "//input[@type='text']";
	public static final String supplierInvoicingElementCheckBox = confirmItemsRow + "//input[@type='checkbox']";
	
	// overview screen
	public static final By newRequisitionButton = By.xpath("//input[@value='Requisition Planned Cost']");
	public static final By editRequisitionPurchaseOrderRuleButton = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[1]/div/div[2]/table/tbody/tr/td[1]/a");
	public static final String editPoRuleMultipleElements = "//span[text()='{{elementname}}']/../..//a[text()='Edit']";
	
	public static final By saveRequisitionPurchaseOrderRuleButton = By.xpath("//table/tbody/tr/td/div[2]/span/form/div[2]/div/div[2]/table/tbody/tr/td[2]/input[1]");
	public static final By requisitionPurchaseOrderRuleRadioButton = By.xpath("//table[3]/tbody/tr/td/div[2]/span/form/div[2]/div/div[1]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td/input");
	public static final By requisitionPurchaseOrderRuleTable = By.xpath("//table[@class='detailList']/tbody/tr[2]/td");
	public static final By viewLink = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[2]/div/div[2]/table/tbody/tr/td[1]/a");
	
	// overview table
	public static final By supplierRequisitionTableSupplier = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[2]/div/div[2]/table/tbody/tr/td[3]"); 
	public static final By supplierRequisitionTableStatus = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[2]/div/div[2]/table/tbody/tr/td[5]");
	public static final By supplierRequisitionTableNetAmount = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[2]/div/div[2]/table/tbody/tr/td[7]");
	
	
	// overview templated
	public static final String supplierRequisitionTable = "//div[contains(@id, ':SupplierPORulesBlock')][contains(@class, 'apexDefaultPageBlock')]/../following-sibling::div[@class='apexp'][1]//table[@class='list']";
	public static final String requisitionRefCell = supplierRequisitionTable + "//tbody/tr/td[2]/span[normalize-space(text())=\"{{reference}}\"]";
	public static final String requisitionRow = requisitionRefCell + "/../..";
	public static final String requisitionViewLink = requisitionRow + "/td[1]/a";
	public static final String supplierCell = requisitionRow + "/td[3]/span";
	public static final String statusCell = requisitionRow + "/td[5]/span";
	public static final String netAmountCell = requisitionRow + "/td[7]/span";
	
	// details screen
	public static final By requestedByField = By.xpath("//th[normalize-space(text())='Requested By']/following-sibling::td[1]/span/a");
	public static final By supplierField = By.xpath("//th[normalize-space(text())='Supplier']/following-sibling::td[1]/span/a");
	public static final By deliveryEngagementField = By.xpath("");
	public static final By requisitionDateField = By.xpath("//th[normalize-space(text())='Requisition Date']/following-sibling::td[1]/span");
	public static final By statusField = By.xpath("//label[normalize-space(text())='Status']/../following-sibling::td[1]/span");
	public static final By typeField = By.xpath("//th[normalize-space(text())='Type']/following-sibling::td[1]/span");
	public static final By billingEntityField = By.xpath("//th[normalize-space(text())='Billing Entity']/following-sibling::td[1]/span/a");
	public static final By itemDescriptionTable = By.xpath("");
	public static final By redqUnitsTable = By.xpath("");
	public static final By unitAmountTable = By.xpath("");
	public static final By netAmountTable = By.xpath("");
	public static final By invoicedAmountTable = By.xpath("");
	public static final By supplierInvoicesTable = By.xpath("");
	
	// lines
	public static final String linesTable = "//div[@class='apexp']//table[@class='list']";
	public static final String lineDescriptionCell = linesTable + "/tbody/tr/td[2]/span[normalize-space(text())=\"{{description}}\"]";
	public static final String line = lineDescriptionCell + "/../..";
	public static final String lineRequiredUnitsCell = line + "/td[5]/span";
	public static final String lineUnitAmountCell = line + "/td[6]/span";
	public static final String lineNetAmountCell = line + "/td[7]/span";
	public static final String lineInvoicedAmountCell = line + "/td[9]/span";
	

	public static void startCreateWizard(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20, 
			/* click new requisition 					*/ newRequisitionButton, 
			/* wait for next button						*/ nextButton
			);
		});
	}

	public static void setSupplierRequisitionPurchaseOrderRule(SeleniumHelper sh, SupplierRequisition requisition) {
		By
		SupplierPORuleRadio = sh.getSelectorBasedOnLabel(KBy.label(requisition.purchaseOrderRule));
		By ElementPORule = By.xpath(editPoRuleMultipleElements.replace("{{elementname}}", requisition.deliveryElement));
		
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20, 
			/* click edit requisition PO rule			*/ ElementPORule,		
			/* click edit requisition PO rule			*/ //editRequisitionPurchaseOrderRuleButton,
			/* wait for table to be clickable			*/ requisitionPurchaseOrderRuleTable 
			);			
			/* radio button select						*/ radioButtonSelect(sh, SupplierPORuleRadio, true);			
			clickAndWaitSequence(sh, 20, 
			/* click save								*/ saveRequisitionPurchaseOrderRuleButton,
			/* click save								*/ newRequisitionButton
			);
		});
	}
	
	public static void create(SupplierRequisition requisition, SeleniumHelper sh) {
		final By invoicingInput = By.xpath(supplierInvoicingInput.replace("{{element}}", requisition.deliveryElement));
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			/* add all the assignments for the element	*/ checkboxSelectMany(sh, By.xpath(supplierInvoicingElementCheckBox.replace("{{element}}", requisition.deliveryElement)), true);
			clickAndWaitSequence(sh, 20, 
			/* click next								*/ nextButton,
			/* wait for input resource input			*/ previousButton
			);
			/* input requesting resource				*/ clearAndInputText(sh, requestedByInput, requisition.requestedBy);
			/* input supplier account					*/ clearAndInputText(sh, supplierInput, requisition.supplier);
			/* input requisition date					*/ clearAndInputText(sh, requisitionDateInput, requisition.requisitionDate);
			clickAndWaitSequence(sh, 20, 
			/* click next 								*/ nextButton,
			/* wait for item confirmation data to show	*/ confirmItemsTable
			);
			try{
				final By allocatedAmount = By.xpath("//input[contains(@id,'TheActivityAssignmentsToConfirm:0')]");
				final By allocatedAmount2 = By.xpath("//input[contains(@id,'TheActivityAssignmentsToConfirm:1')]");
			//for scenario147, the allocated amount is not changed. Added an if statement to skip over the below code and just click Next. 	
			if (requisition.allocateFullamount == false){
				sh.waitForElementToBeClickable(allocatedAmount, 5);
				clearAndInputText(sh, allocatedAmount, 500);
				sh.waitForElementToBeClickable(nextButton);
				clearAndInputText(sh, allocatedAmount2, 550);
				sh.waitForElementToBeClickable(nextButton);
				clearAndInputText(sh, allocatedAmount2, 550);
				sh.waitForElementToBeClickable(nextButton);
			} else{
				sh.waitForElementToBeClickable(nextButton);
			}
			} catch(Exception e){}
			
			clickAndWaitSequence(sh, 20, 
			/* click next			 					*/ nextButton, 
			/* wait for type select input				*/ requisitionTypeSelect
			);
			/* input requisition type					*/ dropdownSelect(sh, requisitionTypeSelect, requisition.requisitionType);
			/* input billing entity						*/ dropdownSelect(sh, billingEntitySelect, requisition.billingEntity);
			clickAndWaitSequence(sh, 20, 
			/* click finish			 					*/ finishButton, 
			/* wait for new requisition button			*/ newRequisitionButton
			);
		});
	}
	
	public static String getReference(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		return sh.getWebElementTextOrNullWithRefreshRetry(requisitionReferenceSpan);
	}
	
	public static void navigateToRequisition(SeleniumHelper sh, String reference) {
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20,
			/* click view 								*/ viewLink,
			/* wait for supplier field					*/ supplierField
			);
		});
	}
	
	public static void submit(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20,
			/* click submit for approval				*/ submitButton,
			/* wait for cancel requisition button		*/ cancelRequisitionButton
			);
		});
	}
	
	public static void validateOverview(SeleniumHelper sh, SupplierRequisition requisition) {
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			/* validate supplier						*/ validateStringValue(sh, By.xpath(supplierCell.replace("{{reference}}", requisition.reference)), requisition.supplier, "requisition overview supplier");
			/* validate status							*/ validateStringValue(sh, By.xpath(statusCell.replace("{{reference}}", requisition.reference)), requisition.status, "requisition overview status");
			/* validate net amount						*/ validateStringValue(sh, By.xpath(netAmountCell.replace("{{reference}}", requisition.reference)), requisition.status, "requisition overview net amount");
		});
	}
	
	public static void validateOverviewTable(SeleniumHelper sh, SupplierRequisitionResult requisition) {
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			/* validate supplier						*/ validateStringValue(sh, supplierRequisitionTableSupplier, requisition.supplier, "requisition overview supplier");
			/* validate status							*/ validateStringValue(sh, supplierRequisitionTableStatus, requisition.status, "requisition overview status");
			/* validate net amount						*/ validateStringValue(sh, supplierRequisitionTableNetAmount, requisition.netAmount, "requisition overview net amount");

		
		});
	}
	public static void validateDetails(SeleniumHelper sh, SupplierRequisition requisition) {
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			/* validate requester						*/ validateStringValue(sh, requestedByField, requisition.requestedBy, "requisition details requested by");
			/* validate supplier						*/ validateStringValue(sh, supplierField, requisition.supplier, "requisition details supplier");
			/* validate requisition date				*/ validateStringValue(sh, requisitionDateField, requisition.requisitionDate, "requisition details requisition date");
			/* validate status							*/ validateStringValue(sh, statusField, requisition.status, "requisition details status");
			/* validate type							*/ validateStringValue(sh, typeField, requisition.requisitionType, "requisition details type");
			/* validate billing entity					*/ validateStringValue(sh, billingEntityField, requisition.billingEntity, "requisition details billing entity");
			for(SupplierRequisitionLine l : requisition.lines) {
			/* validate item description				*/ validateStringValue(sh, By.xpath(lineDescriptionCell.replace("{{description}}", l.itemDescription)), l.itemDescription, "requisition line item description");
			/* validate required units					*/ validateNumberValue(sh, By.xpath(lineRequiredUnitsCell.replace("{{description}}", l.itemDescription)), l.requiredUnits, "requisition line required units");
			/* validate unit amount						*/ validateNumberValue(sh, By.xpath(lineUnitAmountCell.replace("{{description}}", l.itemDescription)), l.unitAmount, "requisition line unit amount");
			/* validate net amount						*/ validateNumberValue(sh, By.xpath(lineNetAmountCell.replace("{{description}}", l.itemDescription)), l.netAmount, "requisition line net amount");
			/* validate invoiced amount					*/ validateNumberValue(sh, By.xpath(lineInvoicedAmountCell.replace("{{description}}", l.itemDescription)), l.invoicedAmount, "requisition line invoiced amount");
			}
		});
	}
	
	public static void validateSupplierRequisitionDetails(SeleniumHelper sh, SupplierRequisitionResult requisition) {
		sh.waitForLightningSpinnerToBeHidden();
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			/* validate requester						*/ validateStringValue(sh, requestedByField, requisition.requestedBy, "requisition details requested by");
			/* validate supplier						*/ validateStringValue(sh, supplierField, requisition.supplier, "requisition details supplier");
			/* validate requisition date				*/ validateStringValue(sh, requisitionDateField, requisition.requisitionDate, "requisition details requisition date");
			/* validate status							*/ validateStringValue(sh, statusField, requisition.status, "requisition details status");
			/* validate type							*/ validateStringValue(sh, typeField, requisition.requisitionType, "requisition details type");
			/* validate billing entity					*/ validateStringValue(sh, billingEntityField, requisition.billingEntity, "requisition details billing entity");
			for(SupplierRequisitionResultLine l : requisition.lines) {
			/* validate item description				*/ validateStringValue(sh, By.xpath(lineDescriptionCell.replace("{{description}}", l.itemDescription)), l.itemDescription, "requisition line item description");
			/* validate required units					*/ validateNumberValue(sh, By.xpath(lineRequiredUnitsCell.replace("{{description}}", l.itemDescription)), l.requiredUnits, "requisition line required units");
			/* validate unit amount						*/ validateStringValue(sh, By.xpath(lineUnitAmountCell.replace("{{description}}", l.itemDescription)), l.unitAmount, "requisition line unit amount");
			/* validate net amount						*/ validateStringValue(sh, By.xpath(lineNetAmountCell.replace("{{description}}", l.itemDescription)), l.netAmount, "requisition line net amount");
			/* validate invoiced amount					*/ validateStringValue(sh, By.xpath(lineInvoicedAmountCell.replace("{{description}}", l.itemDescription)), l.invoicedAmount, "requisition line invoiced amount");
			}
		});
	}
}
