package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import kimble.automation.domain.CreditNote;
import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.Invoice;
import kimble.automation.domain.InvoiceLine;
import kimble.automation.domain.InvoiceLineItem;
import kimble.automation.domain.JsTreeItemList;
import kimble.automation.helpers.General;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.FindBy;

import com.thoughtworks.selenium.webdriven.commands.Click;

public class InvoicePageC extends BasePageC {
	
	private static final String 
	INVOICE_NEW_BUTTON = "input[value$='New']",
	IFRAME_SRC_APEX_INVOICE = "iframe[src^='/apex/Invoice']",
	INS_CLASS$_JSTREE_CHECKBOX = "i[class$='jstree-checkbox']",
	SELECT_ALL_CHECKBOX = "input[onclick='selectAllRootNodes(this)']",
	INPUT_VALUE$_NEXT = "input[value$='Next']",
	INVOICE_FORMAT_SELECT = "select[id$='invoiceFormatSelect']",
	INVOICES = "Invoices",
	SELECT_ID$_BUSINESS_UNIT_TRADING_ENTITY_SELECT_SELECT = "select[id$='businessUnitTradingEntitySelect_SELECT']",
	SELECT_ID$_RECEIVABLE_BANK_ACCOUNT_SELECT_SELECT = "select[id$='bankAccountSelect']",
	INPUT_ID$_GO_NEXT_BTN = "input[id$='goNextBtn']",
	INVOICE_REFERENCE_FIELD = "span[id$='invoice-reference']",
	INPUT_ID$_SEND_INVOICE_FOR_APPROVAL_BTN = "input[id$='sendInvoiceForApprovalBtn']",
	INPUT_ID$_DOWNLOAD_BTN = "input[id$='downloadBtn']",
	INPUT_ID$_RAISE_CREDIT_NOTE_BTN = "input[id$='raiseCreditNoteBtn']",
	INPUT_ID$_DISPATCH_INVOICE_BTN = "input[id$='dispatchInvoiceBtn']",

	anyCreatedInvoiceLineSelector = "//li[@class='invoiceLine'][@id!='new']",
	anyInvoiceLineSelector	=	"//div[@class='invoiceSection'][1]//li[@class='invoiceLine'][2]",
	
	// templated invoice line and line item selectors
//	static final String anyInvoiceLineSelector = "//div[@class='narrative'][text()=\"{{narrative}}\"]",

	invoiceLineSelector = "//div[@class='narrative'][text()=\"{{narrative}}\"]/parent::div/parent::div/parent::li[@class='invoiceLine']",
	invoiceLineSelectorByAmount = "//div[@class='lineAmount'][contains(text(),{{narrative}})]/parent::div/parent::li[@class='invoiceLine']",
	newinvoiceLineSelector = "//div[@class='narrative-container']/div[@class='narrative'][text()='{{narrative}}']",
	
	invoiceLineHeaderSelector = invoiceLineSelectorByAmount + "/div[@class='invoiceLineHeader ui-sortable-handle']",
	invoiceLineNarrativeSelector = invoiceLineHeaderSelector + "/div[@class='narrative-container']",
	invoiceLineNarrativeEdit = anyInvoiceLineSelector + "//i[@title='Edit']",
	invoiceLineNarrativeInput = anyInvoiceLineSelector + "//input",
	invoiceLineNarrativeOkButton = anyInvoiceLineSelector + "//i[@title='OK']",
	anynvoiceLineItemSelector = "//div[2]//li/"
			+ "div[@class='name'][text()=\"{{name}}\"]/following-sibling::"
			+ "div[@class='date'][text()=\"{{date}}\"]/following-sibling::"
			+ "div[@class='amount'][text()=\"{{amount}}\"]/parent::"
			+ "li",
	invoiceLineItemSelector = invoiceLineSelector  + anynvoiceLineItemSelector,
	
	// templated invoice line value selectors
	invoiceHomeLine = "//div[text()='LineNumber']/ancestor::table[1]/tbody/tr[{{rowNo}}]",
	invoiceHomeLineNetAmount = invoiceHomeLine + "/td[3]/span",
	invoiceHomeLineTaxAmount = invoiceHomeLine + "/td[4]/span",
		
	// templated invoice line value selectors
	invoicePdfLinesTable = "//table[@id='invoiceLineDetailsDefault'][2]",
	invoicePdfLine = invoicePdfLinesTable + "/tbody/tr[2+{{rowNo}}]",
	invoicePdfLineRate = invoicePdfLine + "/td[3]",
	invoicePdfLineUnits = invoicePdfLine + "/td[4]",
	invoicePdfLineNetAmount = invoicePdfLine + "/td[5]",
	
	// delivery element selectors
	elementNameSpan = "//td/span[normalize-space(text())=\"{{element}}\"]",
	elementRow = elementNameSpan + "/ancestor::tr[1]",
	elementSelectCheckbox = elementRow + "/td[2]/input",
	
	// Invoice pdf value selectors
	invoicePdfSubTotals = "//table[@id='invoiceTotals']";
	
	static final By 

	// invoice specific tax code selectors
	invoicePdfTax0 = By.xpath(invoicePdfSubTotals + "//td[normalize-space(text())='0%']/following-sibling::td[1]"),
	invoicePdfTax5 = By.xpath(invoicePdfSubTotals + "//td[normalize-space(text())='5%']/following-sibling::td[1]"),
	invoicePdfTax20 = By.xpath(invoicePdfSubTotals + "//td[normalize-space(text())='20%']/following-sibling::td[1]"),
	invoicePdfTaxGST = By.xpath(invoicePdfSubTotals + "//td[normalize-space(text())='GST 5%']/following-sibling::td[1]"),
	invoicePdfTaxPST = By.xpath(invoicePdfSubTotals + "//td[normalize-space(text())='PST 7%']/following-sibling::td[1]"),
	
	
	newLineButton = By.xpath("//a[@onclick='newSectionLine();']"),
	anyInvoiceLine = By.xpath(anyCreatedInvoiceLineSelector),
	invoiceLinesSaveButton = By.xpath("//label[@class='hover-mutton save-btn']"),
	
	// Invoice pdf value selectors
	invoicePdfGrossTotalValue = By.xpath(invoicePdfSubTotals + "//td[normalize-space(text())='Total GBP']/following-sibling::td[1]"),
	invoicePdfNetTotalValue = By.xpath(invoicePdfSubTotals + "//td[normalize-space(text())='Sub-total']/following-sibling::td[1]"),
	invoicePdfTaxValue = By.xpath(invoicePdfSubTotals + "//td[normalize-space(text())='Sub-total']/parent::tr/following-sibling::tr[1]/td[@class='amount']"),
	
	//	Multiple tax code selectors
	multiTax1 = By.xpath("//div[2]/span/table[7]/tbody/tr[2]/td[3]"),
	multiTax2 = By.xpath("//div[2]/span/table[7]/tbody/tr[3]/td[3]"),
	multiTax3 = By.xpath("//div[2]/span/table[7]/tbody/tr[4]/td[3]"),
	// 
	multiTax4 = By.xpath("//div[2]/span/table[3]/tbody/tr[2]/td[3]"),
	multiTax5 = By.xpath("//div[2]/span/table[3]/tbody/tr[3]/td[3]"),
	
	// Invoice home value selectors
	invoiceHomeGrossTotalValue = By.cssSelector("span[id$='invoiceGrossAmount']"),
	invoiceHomeNetTotalValue = By.xpath("//th[text()='Net Amount']/following-sibling::td[1]/span"),
	invoiceHomeTaxValue = By.xpath("//label[normalize-space(text())='Tax Amount']/parent::th/following-sibling::td[1]/span"),
	
	jsTreeClosedCaret = By.cssSelector("li.jstree-closed > ins.jstree-icon"),
	jsTreeOpenCaret = By.cssSelector("li.jstree-open > ins.jstree-icon"),
	
	//	Edit Period Receivables Selectors
	annuityEdit = By.xpath("//td[normalize-space(text())='Annuity']/parent::tr//a[text()='Edit']"), 
	fixedPriceEdit = By.xpath("//td[normalize-space(text())='Fixed Price']/parent::tr//a[text()='Edit']"),
	tmEdit = By.xpath("//td[normalize-space(text())='T&M']/parent::tr//a[text()='Edit']"),
	periodReceivablesSave =  By.xpath("//input[@value='Save']"),
	periodReceivablesEditTaxCode = By.xpath("//input[contains(@class, 'GetTaxCodeList')]/following-sibling::select");
	//#j_id0\3a j_id1\3a j_id150\3a j_id179\3a j_id183 > div > table > tbody > tr:nth-child(2) > td:nth-child(4) > select
	
	@FindBy(partialLinkText = INVOICES)
	private WebElement deliveryEngagementInvoicesLink;

	private By newInvoiceButton = By.cssSelector(INVOICE_NEW_BUTTON);
	
	@FindBy(css = INPUT_ID$_GO_NEXT_BTN)
	private WebElement invoiceNextButton;
	
	@FindBy(css = SELECT_ALL_CHECKBOX)
	private WebElement selectAll;
	
	@FindBy(css = INPUT_ID$_GO_NEXT_BTN)
	private WebElement invoiceFinishButton;

	@FindBy(css = INS_CLASS$_JSTREE_CHECKBOX)
	private WebElement invoiceCheckbox;
	
	@FindBy(css = INVOICE_REFERENCE_FIELD)
	private WebElement invoiceReferenceField;
	
//	@FindBy(css = invoiceHomeGrossTotalValue)
//	private WebElement invoiceGrossAmountLabel;
	
	@FindBy(css = INPUT_ID$_SEND_INVOICE_FOR_APPROVAL_BTN)
	private WebElement sendInvoiceForApprovalBtn;
	
	@FindBy(css = INPUT_ID$_DISPATCH_INVOICE_BTN)
	private WebElement dispatchInvoiceBtn;
	
	@FindBy(css = "span[id$='theCreditNoteReference']")
	private WebElement creditNoteReferenceField;
	
	@FindBy(css = "input[name='go']")
	private WebElement goBtn;
	
	public InvoicePageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
		
	public String CreateNew(Invoice invoiceDetail) {
		
		if(invoiceDetail.isinternal != true){
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.clickMenuItem(PagesC.DELIVERYGROUPINVOICES);
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.clickLink(newInvoiceButton);
			theSH.waitForLightningSpinnerToBeHidden();
					
		}
			else
		
		theSH.waitForElementToBeClickable(By.cssSelector(INVOICE_FORMAT_SELECT), 30);
		theSH.waitForElementToBeClickable(By.cssSelector(INPUT_VALUE$_NEXT));
		
		if(invoiceDetail.deliveryElements != null)
			selectElements(invoiceDetail.deliveryElements);
		
		invoiceNextButton.click();
		
		theSH.sleep(5000);
		
		// by default invoice just the top level item
		// or optioanlly invoice the groups as configured in the input data
		if(invoiceDetail.invoiceGroups != null)
		{
			selectItemsToInvoice(invoiceDetail.invoiceGroups, false);
		}
		else
		{
			if(invoiceDetail.selectall != false)	
			{
				theSH.waitForElementToBeClickable(By.cssSelector(SELECT_ALL_CHECKBOX));
				selectAll.click();
				invoiceNextButton.click();
			}
			else
			{
			theSH.waitForElementToBeClickable(By.cssSelector(INS_CLASS$_JSTREE_CHECKBOX));
			invoiceCheckbox.click();
			invoiceNextButton.click();
			}
			
		}
		
		theSH.sleep(2000);
		
		finishInvoicingWizard();
		theSH.waitForLightningSpinnerToBeHidden();
		
		if(invoiceDetail.lines != null) {
			theSH.clickMenuItem(PagesC.INVOICELINESEDIT);
//			WebElement fromLine = theSH.getWebElement(anyInvoiceLine);
			separateInvoiceIntoLines(invoiceDetail.lines);
			theSH.clickLink(invoiceLinesSaveButton);
		}
		else
			theSH.clickMenuItem(PagesC.INVOICEHOME);

		theSH.waitForLightningSpinnerToBeHidden();
		try{
			invoiceDetail.invoiceReference = getInvoiceReference();
		}
		catch(Exception e){
			theSH.waitMilliseconds(2000);	// wait needed for Lightning
			theSH.waitForLightningSpinnerToBeHidden();
			invoiceDetail.invoiceReference = getInvoiceReference();
		}
		return invoiceDetail.invoiceReference;
	}
	
	public String getInvoiceReference() { return invoiceReferenceField.getText(); }
	
	void separateInvoiceIntoLines(Collection<InvoiceLine> lines) {
		separateInvoiceIntoLines(lines.toArray(new InvoiceLine[lines.size()]));
	}
	
	void separateInvoiceIntoLines(InvoiceLine... lines) {
		for(InvoiceLine line : lines) {
			if(line.name == null || line.items == null || line.items.length == 0)
				continue;
			createInvoiceLine(line.name);
			dragLineItems(line.name, line.items);
		}
	}
	
	void dragLineItems(String toLineName, InvoiceLineItem... items) {
		if(items != null)
			for(InvoiceLineItem item : items)
				dragLineItem(toLineName, item);
	}
	
	void dragLineItem(String toLineName, InvoiceLineItem item) {
		WebElement itemInOldLocation = theSH.getWebElement(By.xpath(anynvoiceLineItemSelector.
				replace("{{name}}", item.name).
				replace("{{date}}", item.date).
				replace("{{amount}}", item.amount)));
		By targetLine = By.xpath(invoiceLineSelector.replace("{{narrative}}", toLineName));
		dragTo(itemInOldLocation, targetLine);
		
		By lineItemInNewLocation = By.xpath(invoiceLineItemSelector.
				replace("{{narrative}}", toLineName).
				replace("{{name}}", item.name).
				replace("{{date}}", item.date).
				replace("{{amount}}", item.amount));
		theSH.waitForElementToBeClickable(lineItemInNewLocation);
	}
	
	void dragTo(WebElement item, By to) {
		WebElement toWe = theSH.getWebElement(to);
		Actions builder = new Actions(theSH.getWD());
		Action dragAndDrop = builder.clickAndHold(item)
//				   .moveToElement(otherElement)
				   .release(toWe)
				   .build();	
		dragAndDrop.perform();
	}
	
	void createInvoiceLine(String name) {
		theSH.clickAndWaitSequence(5, newLineButton, By.xpath(invoiceLineNarrativeSelector.replace("{{narrative}}", "' 0.00'")), By.xpath(invoiceLineNarrativeEdit));
		theSH.getWebElement(By.xpath(invoiceLineNarrativeEdit)).click();
		theSH.getWebElement(By.xpath(invoiceLineNarrativeInput)).sendKeys(name);
		theSH.clickAndWait(By.xpath(invoiceLineNarrativeOkButton), By.xpath(newinvoiceLineSelector.replace("{{narrative}}", name)), 20);
	}
	
	void selectElements(List<String> elements) {
		for(String e : elements)
			selectElement(e);
	}
	
	void selectElement(String element) {
		By checkBoxSelector = By.xpath(elementSelectCheckbox.replace("{{element}}", element));
		if(!theSH.getWebElement(checkBoxSelector).isSelected())
			theSH.clickLink(checkBoxSelector);
	}

	private void selectItemsToInvoice(Map<String, JsTreeItemList> groupMap, boolean expandAll) {
		theSH.waitForElementToBeClickable(By.cssSelector(INS_CLASS$_JSTREE_CHECKBOX));
		while(expandAll && exists(theSH, jsTreeClosedCaret, 5))
			click(theSH, jsTreeClosedCaret);
		String rootName = General.getRootItemName(theSH);
		while(rootName != null && groupMap.containsKey(rootName)) {
			String rootLinkSelectorByName = General.rootItem + General.itemLink.replace("{{item}}", rootName);
			General.selectJsTreeItems(groupMap.get(rootName).items, rootName, theSH);
			General.counterReset();
			invoiceNextButton.click();
			theSH.waitForElementToBeHidden(By.xpath(rootLinkSelectorByName));
			theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_GO_NEXT_BTN));
			rootName = theSH.getWebElementTextOrNull(By.xpath(General.rootItemLink));
		}
	}
	
	private void finishInvoicingWizard() {

		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(SELECT_ID$_RECEIVABLE_BANK_ACCOUNT_SELECT_SELECT), 4);
	//	theSH.waitForElementToBeClickableWithRetry(By.cssSelector(SELECT_ID$_BUSINESS_UNIT_TRADING_ENTITY_SELECT_SELECT), 4);

		invoiceFinishButton.click();
		theSH.sleep(2000);
		theSH.waitForLightningSpinnerToBeHidden();
		try {
			// try to move on from invoicing, if we aren't seeing the expected next stage then try hitting finish
			// again : having to do this due to an odd problem with the finish button and some javascript
			if(theSH.getWebElement(By.cssSelector(INPUT_ID$_GO_NEXT_BTN)).isEnabled()) {
				// first Finish click didn't advance wizard, try again
				theSH.LogMessageLine("First Finish click didn't advance wizard, retrying");
				theSH.getWebElement(By.cssSelector(INPUT_ID$_GO_NEXT_BTN)).click();
			}
		} catch (NoSuchElementException ex) {
			// if the isEnabled threw an exception then the button is gone which means we can carry on
			theSH.LogMessageLine("Finish Button no longer present (NoSuchElementException)");
		} catch (StaleElementReferenceException ex) {
			// if the isEnabled threw an exception then the button is gone which means we can carry on
			theSH.LogMessageLine("Finish Button no longer present (StaleElementReferenceException)");
		} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		}
		// now we should really have moved on to be showing the preview
		// first get the selector which will be different if in a packaged org
		// KimbleOneify if in a packaged org
		String invoiceSelector = IFRAME_SRC_APEX_INVOICE;
		if(SeleniumHelper.isPackagedOrg())
		{
			invoiceSelector = IFRAME_SRC_APEX_INVOICE.replace("apex/", "apex/KimbleOne__");
		}

		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(invoiceSelector), 20);
	}
	
	public void GenerateInvoiceForDeliveryEngagementWithNoPO(Invoice invoiceDetail) {	
		theSH.waitForLightningSpinnerToBeHidden();
		
//		deliveryEngagementInvoicesLink.click();
		theSH.forceOpenCollapsedMenus();
		theSH.clickMenuItem("Invoices");

		theSH.waitForLightningSpinnerToBeHidden();
		theSH.clickLink(newInvoiceButton);

		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickable(By.cssSelector(INPUT_VALUE$_NEXT), 30);
		
		invoiceNextButton.click();
		
		// at this point the screen should indicate an error that Services PO Cover is required
		// stop and verify this in the calling test
		// first wait for the screen to return (upon which the next button is enabled)
		theSH.waitForElementToBeClickable(By.cssSelector(INPUT_VALUE$_NEXT));
	}
	
	public String GetInvoiceValue(Invoice invoiceDetail) {
		String invoiceValue = "";
		
		OpenInvoice(invoiceDetail);
		
		theSH.waitForElementToBeVisible(invoiceHomeGrossTotalValue);
		invoiceValue = theSH.getWebElementTextOrNull(invoiceHomeGrossTotalValue);
		
		return invoiceValue;
	}
	
	public void clickKimbleLogo(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickLink(By.id("k-image"));
	}
	
	public void validateInvoiceHomeValues(Invoice expected) {
		theSH.waitForPageLoadComplete(20);
		if(expected.expectedInvoiceNetValue != null)
			theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(invoiceHomeNetTotalValue), expected.expectedInvoiceNetValue, "The invoice net total is incorrect in invoice home");
		if(expected.expectedInvoiceTaxValue != null)
			theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(invoiceHomeTaxValue), expected.expectedInvoiceTaxValue, "The invoice tax amount is incorrect in invoice home");
		if(expected.expectedInvoiceValue != null)
			theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(invoiceHomeGrossTotalValue), expected.expectedInvoiceValue, "The invoice gross total is incorrect in invoice home");
		
		if(expected.lines == null)
			return;
		for(int i = 0; i < expected.lines.size(); i++) {
			InvoiceLine line = expected.lines.get(i);
			if(line.netAmountString != null)
				theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(By.xpath(invoiceHomeLineNetAmount.replace("{{rowNo}}", "" + (i + 1)))), line.netAmountString, "The invoice line no." + i + " net amount is incorrect invoice home");
			if(line.taxAmountString != null)
				theSH.assertEquals(theSH.getWebElementTextOrNullWithRetry(By.xpath(invoiceHomeLineTaxAmount.replace("{{rowNo}}", "" + (i + 1)))), line.taxAmountString, "The invoice line no." + i + " tax amount is incorrect invoice home");
		}
	}

	public void validateInvoicePdfValues(Invoice expected) {
		theSH.waitForPageLoadComplete(20);
		if(expected.subTotal != null)
			theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(invoicePdfNetTotalValue), expected.subTotal, "The invoice net total is incorrect in the draft pdf");
		if(expected.tax != null)
			theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(invoicePdfTaxValue), expected.tax, "The invoice tax amount is incorrect in the draft pdf");
		if(expected.grossTotal != null)
			theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(invoicePdfGrossTotalValue), expected.grossTotal, "The invoice gross total is incorrect in the draft pdf");

		if(expected.taxMulti1 != null)
			theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(invoicePdfTax0), expected.taxMulti1, "The first tax line is incorrect in the draft pdf");
		if(expected.taxMulti2 != null)
			theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(invoicePdfTax5), expected.taxMulti2, "The second tax line is incorrect in the draft pdf");
		if(expected.taxMulti3 != null)
			theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(invoicePdfTax20), expected.taxMulti3, "The third tax line is incorrect in the draft pdf");
		if(expected.taxMulti4 != null)
			theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(invoicePdfTaxGST), expected.taxMulti4, "The multiple tax line is incorrect in the draft pdf");
		if(expected.taxMulti5 != null)
			theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(invoicePdfTaxPST), expected.taxMulti5, "The multiple tax line is incorrect in the draft pdf");
		
		if(expected.lines == null)
			return;
		for(int i = 0; i < expected.lines.size(); i++) {
			InvoiceLine line = expected.lines.get(i);
			if(line.rate != null)
				theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(By.xpath(invoicePdfLineRate.replace("{{rowNo}}", "" + (i + 1)))), line.rate, "The invoice line no." + i + " rate is incorrect in the draft pdf");
			if(line.units != null)
				theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(By.xpath(invoicePdfLineUnits.replace("{{rowNo}}", "" + (i + 1)))), line.units, "The invoice line no." + i + " units are incorrect in the draft pdf");
			if(line.amount != null)
				theSH.assertEquals(theSH.getWebElementNumberOrNullWithRetry(By.xpath(invoicePdfLineNetAmount.replace("{{rowNo}}", "" + (i + 1)))), line.amount, "The invoice line no." + i + " amount is incorrect in the draft pdf");
		}
	}

	public void OpenInvoice(Invoice invoiceDetail) {
		// first try and click the link in the Recent Items list (in normal operation of the test
		// it's likely that the item will have been recently created
		try {
			theSH.OpenExisting(invoiceDetail.invoiceReference);
		} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		} catch (Exception e1) {
			// not there so navigate to the main list then try in the list recent items
			NavigateToList();
			try {
				theSH.OpenExisting(invoiceDetail.invoiceReference);
			} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
				// if the browser has died (can happen with glitches in chromedriver)
				// then we should exit the test as retry will never succeed
				throw ubEx;
			} catch (Exception e) {
				// not in recent items list so continue onto the list itself
				goBtn.click();
				theSH.OpenExisting(invoiceDetail.invoiceReference);
			}
		}
	}
	
	public void NavigateToList() {
		theSH.NavigateToList(INVOICES);
	}
	
	public void SendInvoiceForApproval(Invoice invoiceDetail) {		
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeClickableWithRetry(By.cssSelector(INPUT_ID$_SEND_INVOICE_FOR_APPROVAL_BTN), 20);
		sendInvoiceForApprovalBtn.click();
		theSH.waitForLightningSpinnerToBeHidden();
		try{
			theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_DISPATCH_INVOICE_BTN), 40);
		}
		catch(Exception e){
			theSH.waitForLightningSpinnerToBeHidden();
			theSH.waitForElementToBeClickable(By.cssSelector(INPUT_ID$_DISPATCH_INVOICE_BTN), 40);
		}
		theSH.clickMenuItem(PagesC.INVOICEHOME);

		theSH.waitForLightningSpinnerToBeHidden();
		theSH.waitForElementToBeVisible(By.cssSelector(INVOICE_REFERENCE_FIELD));
		invoiceDetail.invoiceReference = invoiceReferenceField.getText();	
	}
	
	public void DispatchInvoice(Invoice invoiceDetail) {	
		theSH.clickAndWait(By.cssSelector(INPUT_ID$_DISPATCH_INVOICE_BTN), By.cssSelector(INPUT_ID$_DOWNLOAD_BTN), 60);
	}
	
	public void GetCreditNoteReference(CreditNote creditNoteDetail) {
		creditNoteDetail.reference = creditNoteReferenceField.getText();
	}

	public void EditTaxCode(DeliveryElement element, Integer row) {
		theSH.waitForLightningSpinnerToBeHidden();
		theSH.clickMenuItem(PagesC.DELIVERYGROUPINVOICES);
		
		executeSequenceWithRefreshRetry(theSH, 3, () -> {
			if(row == 1 )
				theSH.clickLink(annuityEdit);
			if(row == 2 )
				theSH.clickLink(fixedPriceEdit);
			if(row == 4 )
				theSH.clickLink(tmEdit);
			
			dropdownSelect(theSH, periodReceivablesEditTaxCode, element.taxCode);
			theSH.clickAndWait(periodReceivablesSave, annuityEdit, 60);
		});
	}	
}
