package kimble.automation.kimbleobjects.classic;

import kimble.automation.domain.CreditNote;
import kimble.automation.domain.CreditNoteLine;
import kimble.automation.domain.Invoice;
import kimble.automation.domain.InvoiceLine;
import kimble.automation.domain.SupplierInvoice;
import kimble.automation.helpers.SeleniumHelper;

import static kimble.automation.helpers.SequenceActions.checkboxSelect;
import static kimble.automation.helpers.SequenceActions.click;
import static kimble.automation.helpers.SequenceActions.clickAndWaitSequence;
import static kimble.automation.helpers.SequenceActions.exists;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.thoughtworks.selenium.webdriven.commands.WaitForCondition;



public class CreditNotePageC extends BasePageC {
	
		
	private static final String INS_CLASS$_JSTREE_CHECKBOX = "i[class$='jstree-checkbox']";

	private static final String CREDIT_NOTE_REFERENCE_FIELD = "span[id$='credit-note-reference']";
	
	private static final String INPUT_ID$_THE_DISPATCHED_CREDIT_NOTE_FILENAME = "a[id$='theDispatchedCreditNoteFilename']";

	private static final String INPUT_ID$_DISPATCH_CREDIT_NOTE_BTN = "input[id$='dispatchCreditNoteBtn']";

	private static final String INPUT_ID$_SEND_CREDIT_NOTE_FOR_APPROVAL_BTN = "input[id$='sendCreditNoteForApprovalBtn']";

	private static final String INPUT_ID$_REASON_CODE_SELECT = "select[id$='reasonCode']";
	
	private static final String INPUT_ID$_REASON_CODE_SELECT_SUPPLIER = "select[id$='82:reasonCode']";
	
	private static final String INPUT_ID$_REASON_CODE_SELECT_SUPPLIER_PARTIAL = "select[id$='107:reasonCode']";

	private static final String INPUT_ID$_RAISE_CREDIT_NOTE_BTN = "input[id$='CreditNoteBtn']";
	
	static final By creditNotePdfTotal = By.xpath("//table[3]/tbody/tr[6]/td[3]");
	static final By jsTreeClosedCaret = By.xpath("//li[@class='jstree-node  jstree-closed jstree-last']/i[@class='jstree-icon jstree-ocl']");
	static final By nextButton = By.xpath("//input[normalize-space(@value)='Next']");
	static final By finishButton = By.xpath("//input[normalize-space(@value)='Finish']");
	
	// templated invoice line value selectors
	static final String creditNotePdfLinesTable = "//table[@id='supplierCreditNoteLines']";
	static final String creditNotesPdfLineNo = creditNotePdfLinesTable + "/tbody/tr[2 +1]";
	static final String creditNotePdfLineNetAmount = creditNotesPdfLineNo + "/td[3]";
	static final String creditNotePdfLineTaxAmount = creditNotesPdfLineNo + "/td[4]";
	static final String itemsToInvoice ="//a[@title='{{date}}']//i[@class='jstree-icon jstree-checkbox']";
	
	@FindBy(css = INPUT_ID$_RAISE_CREDIT_NOTE_BTN)
	private WebElement raiseCreditNoteBtn;
	
	@FindBy(css = "input[id$='creditNoteDate']")
	private WebElement creditNoteDateField;
	
	@FindBy(css = "input[class*='kimbleDatePicker']")
	private WebElement creditNoteDateFieldPartial;
	
	@FindBy(css = INPUT_ID$_REASON_CODE_SELECT)
	private WebElement reasonCodeSelect;
	
	@FindBy(css = INPUT_ID$_REASON_CODE_SELECT_SUPPLIER)
	private WebElement reasonCodeSelectSupplier;
	
	@FindBy(css = INPUT_ID$_REASON_CODE_SELECT_SUPPLIER_PARTIAL)
	private WebElement reasonCodeSelectSupplierPartial;
	
	@FindBy(css = "textarea[id$='reasonDescription']")
	private WebElement reasonDescription;
	
	@FindBy(css = "textarea[id$='id111']")
	private WebElement reasonDescriptionPartial;
	
	@FindBy(css = "input[value='Save']")
	private WebElement saveCreditNoteBtn;
	
	@FindBy(css = INPUT_ID$_SEND_CREDIT_NOTE_FOR_APPROVAL_BTN)
	private WebElement sendCreditNoteForApprovalBtn;
	
	@FindBy(css = INPUT_ID$_DISPATCH_CREDIT_NOTE_BTN)
	private WebElement dispatchCreditNoteBtn;
	
	@FindBy(css = CREDIT_NOTE_REFERENCE_FIELD)
	private WebElement creditNoteReferenceField;
	
	public CreditNotePageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void CreateNew(Invoice invoiceDetail, CreditNote creditNoteDetail){
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_RAISE_CREDIT_NOTE_BTN),20);	
		raiseCreditNoteBtn.click();
				
		// popup takes sometime to render so wait for the first selector field to appear (as these take the most time to render)
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_REASON_CODE_SELECT), 20);
		// tab away from the credit note date field as it's popup is obscuring the reason code dropdown
		creditNoteDateField.sendKeys(Keys.TAB);
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_REASON_CODE_SELECT), 20);
	
		theSH.selectByVisibleText(reasonCodeSelect, creditNoteDetail.reasonCode);
		reasonDescription.sendKeys(creditNoteDetail.reasonDescription);				
		saveCreditNoteBtn.click();
		
		// the screen takes a while to close after clicking save so wait for it to have closed
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeHidden(By.id("newCreditNotePopup"));
	}
	
	public void CreateNewSupplierCn(SeleniumHelper sh, SupplierInvoice invoiceDetail, CreditNote creditNoteDetail){
		
		if(creditNoteDetail.isPartial == true){
			theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_RAISE_CREDIT_NOTE_BTN),20);	
			raiseCreditNoteBtn.click();
			sh.waitForElementToBeClickable(By.cssSelector(INS_CLASS$_JSTREE_CHECKBOX));
			
			while(exists(sh, jsTreeClosedCaret, 5))
				click(sh, jsTreeClosedCaret);
			
		
			for(CreditNoteLine cnLines : creditNoteDetail.lines){
				checkboxSelect(sh, By.xpath(itemsToInvoice.replace("{{date}}", cnLines.name)), true);
			}
			
			
			clickAndWaitSequence(sh, 20,
					/* click next 								*/ nextButton,
					/* wait for invoice payment terms input		*/ finishButton
					);		
			
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_REASON_CODE_SELECT_SUPPLIER_PARTIAL), 20);
			// tab away from the credit note date field as it's popup is obscuring the reason code dropdown
			
			creditNoteDateFieldPartial.clear();
			creditNoteDateFieldPartial.sendKeys(creditNoteDetail.creditNoteDate);
			
			creditNoteDateFieldPartial.sendKeys(Keys.TAB);
			theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_REASON_CODE_SELECT_SUPPLIER_PARTIAL), 20);
		
			theSH.selectByVisibleText(reasonCodeSelectSupplierPartial, creditNoteDetail.reasonCode);
			reasonDescriptionPartial.sendKeys(creditNoteDetail.reasonDescription);	
			
			sh.clickLink(finishButton);
				
		
			
			
			sh.waitForPageToContainTextWithRetry("creditnotepreview", 60);
			
			String creditNotePreview = sh.getCurrentUrl();
			
			   if (creditNotePreview.contains("creditnotepreview")){
				   System.out.println("credit note preview page has loaded");
			   }
			   else
			   {
			   throw new RuntimeException("credit note preview page not loaded");
			   }
			//navigate to supplier credit note home
			String id = sh.getIDFromCurrentURL();
			String supplierCreditNoteHome = sh.getApexBaseFromCurrentUrl() + "/SupplierCreditNoteHome?id=" + id;
			sh.goToUrl(supplierCreditNoteHome);
			
		} else {
		
		
		
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_RAISE_CREDIT_NOTE_BTN),20);	
		raiseCreditNoteBtn.click();
				
		// popup takes sometime to render so wait for the first selector field to appear (as these take the most time to render)
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_REASON_CODE_SELECT_SUPPLIER), 20);
		// tab away from the credit note date field as it's popup is obscuring the reason code dropdown
		
		creditNoteDateField.clear();
		creditNoteDateField.sendKeys(creditNoteDetail.creditNoteDate);
		
		creditNoteDateField.sendKeys(Keys.TAB);
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_REASON_CODE_SELECT_SUPPLIER), 20);
	
		theSH.selectByVisibleText(reasonCodeSelectSupplier, creditNoteDetail.reasonCode);
		reasonDescription.sendKeys(creditNoteDetail.reasonDescription);				
		saveCreditNoteBtn.click();
		
		// the screen takes a while to close after clicking save so wait for it to have closed
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeHidden(By.id("newSupplierCreditNotePopup"));
		}
	}

	public void SendCreditNoteForApproval(CreditNote creditNoteDetails) {	
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_SEND_CREDIT_NOTE_FOR_APPROVAL_BTN), 20);
		sendCreditNoteForApprovalBtn.click();
		theSH.waitForLightningSpinnerToBeHidden();
		try{
			theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_DISPATCH_CREDIT_NOTE_BTN));
		}catch(Exception e){
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_DISPATCH_CREDIT_NOTE_BTN));
		}
		//invoice will now have a different reference, update this in the testinput object
		theSH.clickMenuItem(PagesC.CREDITNOTEHOME);
		
		//credit note will now have a different reference, update this in the testinput object
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeVisible(By.cssSelector(CREDIT_NOTE_REFERENCE_FIELD), 20);
		creditNoteDetails.reference = creditNoteReferenceField.getText();
	}

	public void DispatchCreditNote(CreditNote creditNoteDetails) {
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_DISPATCH_CREDIT_NOTE_BTN), 20);
		dispatchCreditNoteBtn.click();
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_THE_DISPATCHED_CREDIT_NOTE_FILENAME), 20);
	}
	
	public void OpenCreditNote(CreditNote creditNoteDetails) {
		if(creditNoteDetails.isPartial != true){
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.clickLink(By.linkText(creditNoteDetails.reference));
		}
	}


	public static void validateSupplierCreditNotePdfValues(SeleniumHelper sh, CreditNote expected, String creditNoteUrl) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.waitForElementToBePresent(creditNotePdfTotal);
		if(expected.grossTotal != null)
			sh.assertEquals(sh.getWebElementTextOrNullWithRetry(creditNotePdfTotal), expected.grossTotal, "The supplier invoice gross total is incorrect in the draft pdf");
	
		WebElement net = sh.getWebElement(By.xpath(creditNotePdfLineNetAmount));
		String netInv = net.getText();
		String netTrim = netInv.trim();		
		
			if(expected.netAmount != null)	
				sh.assertEquals(netTrim, expected.netAmount,"The supplier credit note net amount is incorrect in the draft pdf" );
				
			if(expected.taxAmount != null)
				sh.assertEquals(sh.getWebElementTextOrNullWithRetry(By.xpath(creditNotePdfLineTaxAmount)), expected.taxAmount, "The supplier credit note tax amount is incorrect in the draft pdf");
			
		sh.goToUrl(creditNoteUrl);
	}

}