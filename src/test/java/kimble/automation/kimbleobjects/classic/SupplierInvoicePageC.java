package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import javax.swing.text.AbstractDocument.Content;

import kimble.automation.domain.CreditNote;
import kimble.automation.domain.InvoiceLine;
import kimble.automation.domain.SupplierInvoice;
import kimble.automation.helpers.General;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.FindBy;

public class SupplierInvoicePageC {
	
	private static final String 
	INS_CLASS$_JSTREE_CHECKBOX = "i[class$='jstree-checkbox']",
	SUPPLIERINVOICES = "Supplier Invoices";
	
	static final By	
	jsTreeClosedCaret = By.xpath("//li[@class='jstree-node  jstree-closed jstree-last']/i[@class='jstree-icon jstree-ocl']");
	

	public static final By newButton = By.xpath("//input[normalize-space(@value)='New']");
	public static final By newSupplierInvoiceButton = By.xpath("//input[normalize-space(@value)='New Supplier Invoice']");
	public static final By nextButton = By.xpath("//input[normalize-space(@value)='Next']");
	public static final By cancelButton = By.xpath("//input[normalize-space(@value)='Cancel']");
	public static final By resourceDropDown = By.xpath("//select[contains(@id,'resource')]");
	public static final By finishButton = By.xpath("//input[normalize-space(@value)='Finish']");
	public static final By purcahseOrderInput = By.xpath("//label[normalize-space(text())='Purchase Order']/../following-sibling::td[1]//span[@class='lookupInput']/input");
	public static final By suppReqCheckBox = By.xpath("//input[@type='checkbox']");
	public static final By supplierInput = By.xpath("//label[normalize-space(text())='Supplier']/../following-sibling::td[1]//span[@class='lookupInput']/input");
	public static final By selfBillCheckBox = By.xpath("//label[normalize-space(text())='Self Bill?']/../following-sibling::td[1]//input");
	//public static final By referenceInput = By.xpath("//label[normalize-space(text())='Reference']/../following-sibling::td[1]//input");
	public static final By referenceInput = By.xpath("//th[normalize-space(text())='Reference']/..//div/input");
	public static final By invoiceDateInput = By.xpath("//label[normalize-space(text())='Invoice Date']/../following-sibling::td[1]//span[@class='dateInput dateOnlyInput']/input");
	public static final By invoiceDateInput2 = By.xpath("//input[contains(@onfocus,'Date')]");
	public static final By taxCodeSelect = By.xpath("//th[normalize-space(text())='Tax Code']/following-sibling::td[1]//select");
	public static final By invoiceFormatSelect = By.cssSelector("[id$='invoiceFormat']");	
	public static final By netAmountInput = By.xpath("//label[contains(text(), 'Net Amount (')]/../following-sibling::td[1]//input");
	public static final By invoicePaymentTermInput = By.xpath("//label[normalize-space(text())='Invoice Payment Term Days']/../following-sibling::td[1]//input");
	public static final By businessUnitSelect = By.xpath("//th[normalize-space(text())='Business Unit Trading Entity']/following-sibling::td[1]//select");
	public static final By notesInput = By.xpath("//label[normalize-space(text())='Notes']/../following-sibling::td[1]//textarea");
	public static final By invoiceTemplateSelect = By.xpath("//th[normalize-space(text())='Supplier Invoice Template']/following-sibling::td[1]//select");
	public static final By generateSupplierInvoice = By.xpath("//input[@value='Generate Supplier Invoice']");
	public static final By startButton = By.xpath("//input[@id='startButton']");
	public static final By backButton = By.xpath("//input[@value='Back']");
	
	public static final By includeItemsTable = By.id("selectCostItems");
	
	public static final String wizardCompleteWaitSelector = "//body[//div[@class='breadcrumb-item-link breadcrumb-heading'][normalize-space(text())=\"{{reference}}\"] | //input[normalize-space(@value)='New Supplier Invoice']]";
	
	// templated selectors
	
	// select requisition lines table
	public static final String requisitionLinesTable = "//div[contains(@id, 'TheMatchingRequisitionLines')]//table[@class='detailList']//table[@class='list']";
	public static final String itemDescriptionCell = requisitionLinesTable + "//td[4]/span[normalize-space(text())=\"{{description}}\"]";
	public static final String itemRow = itemDescriptionCell + "/../..";
	public static final String itemSelectCheckBox = itemRow + "/td[1]/input";
	public static final String invoiceApprovalButton = ("//input[@value='Send For Approval']");
	public static final String raiseCreditNotelButton = ("//input[@value='Raise Supplier Credit Note']");
	
	// select delivery element line
	public static final String deliveryElementLine = "//span[normalize-space(text())='{{description}}']/../..//input[@type='checkbox']";
	public static final String itemsToInvoice ="//a[@title='{{date}}']//i[@class='jstree-icon jstree-checkbox']";
	
	// selected lines table
	public static final String selectedLinesTable = "//table[contains(@id, 'TheSelectedSupplierRequisitionLines')]";
	public static final String selectedItemDescriptionCell = selectedLinesTable + "//td[1]/span[normalize-space(text())=\"{{description}}\"]";
	public static final String selectedItemRow = selectedItemDescriptionCell + "/../..";
	public static final String isInvoiceCompleteCheckbox = selectedItemRow + "/td[4]/input";
	public static final String lineEditTable = selectedItemRow + "/td/table";
	public static final String itemAllocationAmountInput = lineEditTable + "//td[3]/input";
	public static final String itemActionCheckBox = lineEditTable + "//td[4]/input";
	
	// Invoice pdf value selectors
	static final String invoicePdfSubTotals = "//table[@id='invoiceTotals']";
	static final By invoicePdfGrossTotalValue = By.xpath(invoicePdfSubTotals + "//td[normalize-space(text())='TOTAL']/following-sibling::td[1]");
	static final By invoicePdfTotal = By.xpath("//table[3]/tbody/tr[4]/td[3]");
	static final By invoicePdfTotalNoReq = By.xpath("//table[3]/tbody/tr[6]/td[3]");

	// templated invoice line value selectors
	static final String invoicePdfLinesTable = "//table[@id='supplierInvoiceLines']";
	static final String invoicePdfLine = invoicePdfLinesTable + "/tbody/tr[2+{{rowNo}}]";
	static final String invoicePdfLineNoReq = invoicePdfLinesTable + "/tbody/tr[2 +1]";
	static final String invoicePdfLineDescription = invoicePdfLine + "/td[2]";
	static final String invoicePdfLineNetAmount = invoicePdfLine + "/td[3]";
	static final String invoicePdfLineTaxAmount = invoicePdfLine + "/td[4]";
	static final String invoicePdfLineNetAmountNoReq = invoicePdfLineNoReq + "/td[3]";
	static final String invoicePdfLineTaxAmountNoReq = invoicePdfLineNoReq + "/td[4]";
	
	// resource batch generated
	static final String resourceSelector = "//span[text()='{{resourcename}}']//..//..//..//..//input[@type='checkbox']";//td[text()='T&M 1']/..//input[@class='row-selector-approval']
	static final String approveCheckbox = "//td[text()='{{deliveryelement}}']/..//input[@class='row-selector-approval']";
	static final String selectCheckbox = "//td[text()='{{deliveryelement}}']/..//input[@class='row-selector']";
	
	@FindBy(css = "input[name='go']")
	private WebElement goBtn;
	
	@FindBy(css = "span[id$='theSupplierCreditNoteReference']")
	private WebElement supplierCreditNoteReferenceField;
	
    public static String supplierInvoiceReference;
//	public static String supplierInvoiceReference = "SI_U100004";
	
	
	public static void startCreateWiz(SeleniumHelper sh, SupplierInvoice invoice) {
		if(sh.isLightning()){
			sh.clickLink(KBy.title("New"), 15);
			sh.waitForLightningSpinnerToBeHidden();
			waitClickable(sh, purcahseOrderInput, 15);
		}
		else{
			executeSequenceWithRefreshRetry(sh, 3, () -> {
				clickAndWaitSequence(sh, 20,
				/* click new								*/ newButton,
				/* wait for purchase order input			*/ purcahseOrderInput
				);
			});
		}
	}
	
	public static void startCreateWizNoReq(SeleniumHelper sh, SupplierInvoice invoice) {
		if (invoice.supplierRequisitioned == false){		
		
				executeSequenceWithRefreshRetry(sh, 3, () -> {
				clickAndWaitSequence(sh, 20, newButton, suppReqCheckBox);
				clickAndWaitSequence(sh, 20, nextButton,resourceDropDown);
				dropdownSelect(sh,resourceDropDown, invoice.supplierResource);
				clickAndWaitSequence(sh, 15, nextButton,cancelButton);
				}); } 
		else {
			
			executeSequenceWithRefreshRetry(sh, 3, () -> {
				clickAndWaitSequence(sh, 20, newButton, suppReqCheckBox);
				clickAndWaitSequence(sh, 20, suppReqCheckBox,nextButton);
				clickAndWaitSequence(sh, 20, nextButton, invoiceDateInput);
				/* input date								*/ clearAndInputText(sh, invoiceDateInput, invoice.invoiceDate);
				/* input requesting resource				*/ clearAndInputText(sh, purcahseOrderInput, invoice.purchaseOrder);
				/* input supplier account					*/ clearAndInputText(sh, supplierInput, invoice.supplier);
				/* input requisition date					*/ checkboxSelect(sh, selfBillCheckBox, invoice.selfBill);
				clickAndWaitSequence(sh, 20,
						/* click next 								*/ nextButton,
						/* wait for purchase order input			*/ cancelButton
						);
				}); 
			
			
		} 
			
		}
			

	
	public static void runCreateWizStep1(SeleniumHelper sh, SupplierInvoice invoice) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			/* input date								*/ clearAndInputText(sh, invoiceDateInput, invoice.invoiceDate);
			/* input requesting resource				*/ clearAndInputText(sh, purcahseOrderInput, invoice.purchaseOrder);
			/* input supplier account					*/ clearAndInputText(sh, supplierInput, invoice.supplier);
			/* input requisition date					*/ checkboxSelect(sh, selfBillCheckBox, invoice.selfBill);
			clickAndWaitSequence(sh, 20,
			/* click next 								*/ nextButton,
			/* wait for purchase order input			*/ referenceInput
			);
		});
	}
	
	
	public static void runCreateWizStep1NoReq(SeleniumHelper sh, SupplierInvoice invoice) {
		
		if (invoice.supplierRequisitioned == true){	
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			/* input date								*/ clearAndInputText(sh, invoiceDateInput, invoice.invoiceDate);
			/* input requesting resource				*/ clearAndInputText(sh, purcahseOrderInput, invoice.purchaseOrder);
			/* input supplier account					*/ clearAndInputText(sh, supplierInput, invoice.supplier);
			/* input requisition date					*/ checkboxSelect(sh, selfBillCheckBox, invoice.selfBill);
			clickAndWaitSequence(sh, 20,
			/* click next 								*/ nextButton,
			/* wait for purchase order input			*/ referenceInput
			);
		});
		}
				
	}

	public static void runCreateWizStep2(SeleniumHelper sh, SupplierInvoice invoice) {
		if(invoice.itemDescriptions == null)
			throw new RuntimeException("Supplier invoice items are missing");
		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			/* input reference							*/ clearAndInputText(sh, referenceInput, invoice.reference);
			//* input tax code							*/ dropdownSelect(sh, taxCodeSelect, invoice.taxCode);
			/* input tax code							*/ dropdownSelect(sh, invoiceFormatSelect, invoice.invoiceFormat);
			/* input date								*/ clearAndInputText(sh, netAmountInput, invoice.netAmount);
			for(String desc : invoice.itemDescriptions)
			/* select item								*/ checkboxSelectMany(sh, By.xpath(itemSelectCheckBox.replace("{{description}}", desc)), true);
			clickAndWaitSequence(sh, 20,
			/* click next 								*/ nextButton,
			/* wait for invoice complete checkbox		*/ includeItemsTable //By.xpath(itemActionCheckBox.replace("{{description}}", invoice.itemDescriptions.get(0)))
			);
		});
	}
	
	public static void runCreateWizStep2NoReq(SeleniumHelper sh, SupplierInvoice invoice) {
		if(invoice.itemDescriptions == null)
			throw new RuntimeException("Supplier invoice items are missing");
		
		
		if(invoice.supplierRequisitioned == true){
			executeSequenceWithRefreshRetry(sh, 3, () -> {
				/* select invoice format					*/ dropdownSelect(sh, invoiceFormatSelect, invoice.invoiceFormat);
				
				for(String desc : invoice.itemDescriptions)
				/* select item								*/ checkboxSelectMany(sh, By.xpath(deliveryElementLine.replace("{{description}}", desc)), true);
				clickAndWaitSequence(sh, 20,
				/* click next 								*/ nextButton,
				/* wait for invoice complete checkbox		*/ includeItemsTable //By.xpath(itemActionCheckBox.replace("{{description}}", invoice.itemDescriptions.get(0)))
				);
			});	
			
		} else {
			executeSequenceWithRefreshRetry(sh, 3, () -> {
				/* select business unit						*/ dropdownSelect(sh, businessUnitSelect, invoice.businessUnit);
				/* input tax code							*/ dropdownSelect(sh, taxCodeSelect, invoice.taxCode);
				/* select invoice format					*/ dropdownSelect(sh, invoiceFormatSelect, invoice.invoiceFormat);
				/* input invoice date						*/ clearAndInputText(sh, invoiceDateInput2, invoice.invoiceDate);
				
				for(String desc : invoice.itemDescriptions)
				/* select item								*/ checkboxSelectMany(sh, By.xpath(deliveryElementLine.replace("{{description}}", desc)), true);
				clickAndWaitSequence(sh, 20,
				/* click next 								*/ nextButton,
				/* wait for invoice complete checkbox		*/ includeItemsTable //By.xpath(itemActionCheckBox.replace("{{description}}", invoice.itemDescriptions.get(0)))
				);
			});	
		}

	}

	public static void runCreateWizStep3(SeleniumHelper sh, SupplierInvoice invoice) {
		By
		itemsToInvoice = By.linkText(invoice.itemDescriptions.get(0));	
		
		if(invoice.itemDescriptions == null)
			throw new RuntimeException("Supplier invoice items are missing");
		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
	//		for(String desc : invoice.itemDescriptions)
			/* select item								*/ checkboxSelect(sh, By.xpath("//i[@class='jstree-icon jstree-checkbox']"), true);

			clickAndWaitSequence(sh, 20,
			/* click next 								*/ nextButton,
			/* wait for invoice payment terms input		*/ invoicePaymentTermInput
			);
		});
		
		
	}
	
	public static void runCreateWizStep3NoReq(SeleniumHelper sh, SupplierInvoice invoice) {
		sh.waitForElementToBeClickable(By.cssSelector(INS_CLASS$_JSTREE_CHECKBOX));
		while(exists(sh, jsTreeClosedCaret, 5))
			click(sh, jsTreeClosedCaret);
		
	
		for(InvoiceLine invLines : invoice.lines){
			checkboxSelect(sh, By.xpath(itemsToInvoice.replace("{{date}}", invLines.name)), true);
		}
		
		
		clickAndWaitSequence(sh, 20,
				/* click next 								*/ nextButton,
				/* wait for invoice payment terms input		*/ invoicePaymentTermInput
				);
			
		}
		
	

	public static void runCreateWizStep4(SeleniumHelper sh, SupplierInvoice invoice) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			/* input payment term						*/ clearAndInputText(sh, invoicePaymentTermInput, invoice.paymentTerm);
			/* select business unit						*/ dropdownSelect(sh, businessUnitSelect, invoice.businessUnit);
			/* select template							*/ dropdownSelect(sh, invoiceTemplateSelect, invoice.invoiceTemplate);
			/* input tax code							*/ dropdownSelect(sh, taxCodeSelect, invoice.taxCode);
			clickAndWaitSequence(sh, 30,
			/* click finish 							*/ finishButton,
			/* wait for the wizard to complete			*/ By.xpath(wizardCompleteWaitSelector.replace("{{reference}}", invoice.reference))
			);
		});
	}
	
	public static void runCreateWizStep4NoReq(SeleniumHelper sh, SupplierInvoice invoice) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			   dropdownSelect(sh, invoiceTemplateSelect, invoice.invoiceTemplate);
			   sh.clickLink(finishButton); 
			   sh.waitMilliseconds(10000);
			   
		});
		
			sh.waitForPageToContainTextWithRetry("supplierinvoicepreview", 60);
			   String invoicepreview = sh.getCurrentUrl();
			   if (invoicepreview.contains("supplierinvoicepreview")){
				   System.out.println("invoice preview page has loaded");
			   }
			   else
			   {
			   throw new RuntimeException("Supplier invoice preview not loaded");
			   }			   

	}

	public static void approveSupplierInvoice(SeleniumHelper sh) {
		
		sh.waitForLightningSpinnerToBeHidden();
		sh.waitForElementToBeClickableWithRetry(By.xpath(invoiceApprovalButton), 20);
		sh.clickAndWait(By.xpath(invoiceApprovalButton), By.xpath(raiseCreditNotelButton), 20);
		sh.waitForLightningSpinnerToBeHidden();
		
	}
	
	public static void selectResourceAndGenerateInvoice(SeleniumHelper sh, String name) {

		WebElement resourceName = sh.getWebElement(By.xpath(resourceSelector.replace("{{resourcename}}", name)));
		resourceName.click();
		sh.clickLink(generateSupplierInvoice);
		sh.waitForElementToBeVisible(startButton, 20);
	
	}
	
	public static void selectAccountAndGenerateInvoice(SeleniumHelper sh, String name) {
		
		//can use the same selector used for resource batch generating 
		WebElement accountName = sh.getWebElement(By.xpath(resourceSelector.replace("{{resourcename}}", name)));
		accountName.click();
		sh.clickLink(generateSupplierInvoice);
		sh.waitForElementToBeVisible(startButton, 20);
	
	}
	
	public static void selectAssignmentToInvoice(SeleniumHelper sh, SupplierInvoice invoice) {

	//select 'select' and 'approve' checkbox based on the Delivery Element name
		WebElement selectCheck = sh.getWebElement(By.xpath(selectCheckbox.replace("{{deliveryelement}}", invoice.deliveryElement)));
		WebElement approveCheck = sh.getWebElement(By.xpath(approveCheckbox.replace("{{deliveryelement}}", invoice.deliveryElement)));
		
		selectCheck.click();
		approveCheck.click();
		sh.waitForElementToBeClickable(startButton);
		sh.clickAndWait(startButton, backButton, 60);
		//gets the first invoice reference from the list
		WebElement invoiceRef2 = sh.getWebElement(By.xpath("//tr[contains(@class,'first')]//td[3]//span"));
		String invoiceRefText2 = invoiceRef2.getText();
		supplierInvoiceReference = invoiceRefText2;				
	
	}
	public static void validateInvoicePdfValues(SeleniumHelper sh, SupplierInvoice expected) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.waitForElementToBePresent(invoicePdfTotal);
		if(expected.grossTotal != null)
			sh.assertEquals(sh.getWebElementTextOrNullWithRetry(invoicePdfTotal), expected.grossTotal, "The supplier invoice gross total is incorrect in the draft pdf");
		
		if(expected.lines == null)
			return;
		for(int i = 0; i < expected.lines.size(); i++) {
			InvoiceLine line = expected.lines.get(i);
			if(line.amount != null)
				sh.assertEquals(sh.getWebElementNumberOrNullWithRetry(By.xpath(invoicePdfLineNetAmount.replace("{{rowNo}}", "" + (i + 1)))), line.amount, "The supplier invoice line no." + i + " amount is incorrect in the draft pdf");
			if(line.tax != null)
				sh.assertEquals(sh.getWebElementNumberOrNullWithRetry(By.xpath(invoicePdfLineTaxAmount.replace("{{rowNo}}", "" + (i + 1)))), line.tax, "The supplier invoice line no." + i + " amount is incorrect in the draft pdf");
		}
	}


	public static void validateInvoicePdfValuesNoReq(SeleniumHelper sh, SupplierInvoice expected, String invoiceHome) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.waitForElementToBePresent(invoicePdfTotalNoReq);
		if(expected.grossTotal != null)
			sh.assertEquals(sh.getWebElementTextOrNullWithRetry(invoicePdfTotalNoReq), expected.grossTotal, "The supplier invoice gross total is incorrect in the draft pdf");

		WebElement net = sh.getWebElement(By.xpath(invoicePdfLineNetAmountNoReq));
		String netInv = net.getText();
		String netTrim = netInv.trim();		
		
			if(expected.netAmount != null)	
				sh.assertEquals(netTrim, expected.netAmount,"The supplier invoice net amount is incorrect in the draft pdf" );
				
			if(expected.taxAmount != null)
				sh.assertEquals(sh.getWebElementTextOrNullWithRetry(By.xpath(invoicePdfLineTaxAmountNoReq)), expected.taxAmount, "The supplier invoice tax amount is incorrect in the draft pdf");
		
		sh.goToUrl(invoiceHome);
	}
	
	public static void findSupplierInvoiceReference(SeleniumHelper sh) {
		
		WebElement invoiceRef = sh.getWebElement(By.xpath("//th[text()='Reference']//..//td[1]//span"));
		String invoiceRefText = invoiceRef.getText();
		
		supplierInvoiceReference = invoiceRefText;
		
	}

	
	public void OpenSupplierInvoice(SeleniumHelper sh, SupplierInvoice invoiceDetail) {
		// first try and click the link in the Recent Items list (in normal operation of the test
		// it's likely that the item will have been recently created
		try {
			sh.OpenExisting(supplierInvoiceReference);
			
		} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		} catch (Exception e1) {
			// not there so navigate to the main list then try in the list recent items
			NavigateToList(sh);
			try {
				sh.OpenExisting(supplierInvoiceReference);
			} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
				// if the browser has died (can happen with glitches in chromedriver)
				// then we should exit the test as retry will never succeed
				throw ubEx;
			} catch (Exception e) {
				// not in recent items list so continue onto the list itself
				goBtn.click();
				sh.OpenExisting(supplierInvoiceReference);
			}
		}
	}
	
	public void NavigateToList(SeleniumHelper sh) {
		sh.NavigateToList(SUPPLIERINVOICES);
	}
	
	public void GetSupplierCreditNoteReference(SeleniumHelper sh, CreditNote creditNoteDetail) {
		
		if(creditNoteDetail.isPartial != true){
		WebElement creditNoteRef = sh.getWebElement(By.xpath("//span[contains(@id,'theSupplierCreditNoteReference')]"));
		String invoiceRefText = creditNoteRef.getText();
		creditNoteDetail.reference = invoiceRefText;
		}
	}
}