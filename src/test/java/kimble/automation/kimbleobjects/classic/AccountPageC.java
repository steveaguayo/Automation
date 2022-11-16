package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.Account;
import kimble.automation.domain.SupplierProduct;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;

public class AccountPageC {
	
	static final By 
	newButton = By.name("new"),
	editButton = By.name("edit"),
	saveButton = By.name("save"),
	
	nameLabel = KBy.label("Account Name"),
	typeLabel = KBy.label("Type"),
	isSupplierLabel = KBy.label("Is Supplier"),
	isCustomer = By.cssSelector("input[id$=accountIsCustomer]"),
	isInternal = By.cssSelector("input[id$=accountIsInternal]"),
	isSupplier = By.cssSelector("input[id$=accountIsSupplier]"),
	buTrading = By.cssSelector("input[id$=tradingEntityInput]"),
	buOperating = By.cssSelector("input[id$=businessUnitInput]"),
	accountInvoicingModel = By.cssSelector("input[value='New Account Invoicing Model']"),
	
	isInternalLabel = KBy.label("Is Internal"),
	iframeContentId = By.xpath("//div[@id='content-pane']"),
	accountCurrency = KBy.label("Account Currency"),
	buOperatingLabel = KBy.label("Business Unit (Operating)"),
	buTradingLabel = KBy.label("Business Unit (Trading)"),
	industryLabel = KBy.label("Industry"),
	billingContactLabel = KBy.label("Billing Contact"),
	billingStreetLabel = KBy.label("Billing Street"),
	billingCityLabel = KBy.label("Billing City"),
	billingStateLabel = KBy.label("Billing State/Province"),
	billingZipLabel = KBy.label("Billing Zip/Postal Code"),
	billingCountryLabel = KBy.label("Billing Country"),
	shippingStreetLabel = KBy.label("Shipping Street"),
	shippingCityLabel = KBy.label("Shipping City"),
	shippingStateLabel = KBy.label("Shipping State/Province"),
	shippingZipLabel = KBy.label("Shipping Zip/Postal Code"),
	shippingCountryLabel = KBy.label("Shipping Country"),
	invoicingCurrencyLabel = KBy.label("Invoicing Currency"),
	poRuleLabel = KBy.label("Purchase Order Rule"),
	taxCodeLabel = KBy.label("Tax Code"),
	invoiceFormatLabel = KBy.label("Invoice Format"),
    invoiceTemplateLabel = KBy.label("Invoice Template"),
	localeLabel = KBy.label("Locale"),
	allowPartItemInvoicingLabel = KBy.label("Allow Part Item Invoicing"),
	travelCategory = KBy.label("Travel Category"),
	isSupplierRequisitionedLabel = KBy.label("Is Supplier Requisitioned"),
	
	newAccountInvoicingModel = By.xpath("//div[@id='contentWrapper']//input[@value='New Account Invoicing Model']"),
	
	coreSave = By.xpath("//td[contains(@class,'pbButtonb')]//input[@value='Save']"),
	coreCancel = By.xpath("//td[@class='pbButtonb ']//input[@value='Cancel']"),
	coreEdit = By.xpath("//td[@class='pbButtonb ']//input[@value='Edit']");
	
	
		
	public static void startNewAccountCreationIfDoesntExist(SeleniumHelper sh, String accountName) {
		executeSequenceWithRetry(sh, 3, () -> {
//			if(exists(sh, By.linkText(accountName), 5))
//				return;
			clickAndWaitSequence(sh, 20,
			/* click new			*/	newButton,
			/* wait for name input	*/	nameLabel
			);
		});
	}
	
	public static void inputValuesAndSave(SeleniumHelper sh, Account account) {
		By 
		nameInput = sh.getSelectorBasedOnLabel(nameLabel),
		typeDropdown = sh.getSelectorBasedOnLabel(nameLabel),
		currencyDropdown = sh.getSelectorBasedOnLabel(accountCurrency),

		industryDropdown = sh.getSelectorBasedOnLabel(industryLabel),
		
		billingContactInput = sh.getSelectorBasedOnLabel(billingContactLabel),
		billingStreetInput = sh.getSelectorBasedOnLabel(billingStreetLabel),
		billingCityInput = sh.getSelectorBasedOnLabel(billingCityLabel),
		billingStateInput = sh.getSelectorBasedOnLabel(billingStateLabel),
		billingZipInput = sh.getSelectorBasedOnLabel(billingZipLabel),
		billingCountryInput = sh.getSelectorBasedOnLabel(billingCountryLabel),
		shippingStreetInput = sh.getSelectorBasedOnLabel(shippingStreetLabel),
		shippingCityInput = sh.getSelectorBasedOnLabel(shippingCityLabel),
		shippingStateInput = sh.getSelectorBasedOnLabel(shippingStateLabel),
		shippingZipInput = sh.getSelectorBasedOnLabel(shippingZipLabel),
		shippingCountryInput = sh.getSelectorBasedOnLabel(shippingCountryLabel);

		
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clearAndInputText(sh, nameInput, account.name);
			dropdownSelect(sh, typeDropdown, account.type);

			dropdownSelect(sh, industryDropdown, account.industry);
			
			clearAndInputText(sh, billingContactInput, account.billingContact);
			if(account.billingAddress != null) {
				clearAndInputText(sh, billingStreetInput, account.billingAddress.street);
				clearAndInputText(sh, billingCityInput, account.billingAddress.city);
				clearAndInputText(sh, billingStateInput, account.billingAddress.stateProvince);
				clearAndInputText(sh, billingZipInput, account.billingAddress.zipPostalCode);
				clearAndInputText(sh, billingCountryInput, account.billingAddress.country);
			}
			if(account.shippingAddress != null) {
				clearAndInputText(sh, shippingStreetInput, account.shippingAddress.street);
				clearAndInputText(sh, shippingCityInput, account.shippingAddress.city);
				clearAndInputText(sh, shippingStateInput, account.shippingAddress.stateProvince);
				clearAndInputText(sh, shippingZipInput, account.shippingAddress.zipPostalCode);
				clearAndInputText(sh, shippingCountryInput, account.shippingAddress.country);
			}
				
			if(account.accountCurrency != null){	
				dropdownSelect(sh, currencyDropdown, account.accountCurrency);	
			}
					
			clickAndWaitSequence(sh, 20,
			/* click save			*/	saveButton,
			/* wait for edit button	*/	editButton
			);
		});
	}
	
	
	
	public static void inputCoreAccountDetails(SeleniumHelper sh, Account account){
		
		sh.waitForElementToBeVisible(newAccountInvoicingModel, 10);		
		sh.switchToAccountEditIframe();
		sh.clickAndWait(coreEdit, coreSave, 10);
		
		
		sh.waitMilliseconds(2000);
		sh.waitForElementToBeClickable(isInternal);
		checkboxSelect(sh, isInternal, account.isInternal);
		sh.waitMilliseconds(2000);
		sh.waitForElementToBeClickable(isSupplier);
		checkboxSelect(sh, isSupplier, account.isSupplier);
		sh.waitMilliseconds(2000);
		clearAndInputText(sh, buTrading, account.tradingBusinessUnit);
		sh.waitMilliseconds(2000);
		clearAndInputText(sh, buOperating, account.operatingBusinessUnit);

		sh.clickAndWait(coreSave, coreEdit, 20);

	}
	
	
	public static void inputInvoicingDetails(SeleniumHelper sh, Account account) {
	
		if(account.locale != null){			
			executeSequenceWithRetry(sh, 3, () -> {
				sh.checkElementVisible(newAccountInvoicingModel);
			    sh.waitForElementToBeClickable(accountInvoicingModel);
				clickAndWaitSequence(sh, 20,
				/* click account invoicing model*/	accountInvoicingModel,
				/* wait for save button     	*/	saveButton
				);
			});		
			
		if(account.invoiceTemplate !=null){
			By invoiceTemplate = sh.getSelectorBasedOnLabel(invoiceTemplateLabel);
			clearAndInputText(sh, invoiceTemplate, account.invoiceTemplate);			
		}
//			sh.checkElementVisible(newAccountInvoicingModel);
//		    sh.waitForElementToBeClickable(accountInvoicingModel);
//			sh.clickLink(accountInvoicingModel);
//			sh.waitForElementToBeClickable(saveButton);
				
			By
				invoicingCurrencyInput = sh.getSelectorBasedOnLabel(invoicingCurrencyLabel),
				poRuleInput = sh.getSelectorBasedOnLabel(poRuleLabel),
				taxCodeInput = sh.getSelectorBasedOnLabel(taxCodeLabel),
				invoiceFormatInput = sh.getSelectorBasedOnLabel(invoiceFormatLabel),
				localeInput = sh.getSelectorBasedOnLabel(localeLabel),
				allowPartItemInvoicingCheckbox = sh.getSelectorBasedOnLabel(allowPartItemInvoicingLabel);
		
							
			clearAndInputText(sh, invoicingCurrencyInput, account.invoicingCurrency);
			clearAndInputText(sh, poRuleInput, account.purchaseOrderRule);
			clearAndInputText(sh, taxCodeInput, account.taxCode);
			clearAndInputText(sh, invoiceFormatInput, account.invoiceFormat);
			clearAndInputText(sh, localeInput, account.locale);
			checkboxSelect(sh, allowPartItemInvoicingCheckbox, account.allowPartItemInvoicing);
				
			sh.clickAndWait(saveButton, editButton, 10);
		}
	
		sh.clickLinkByPartialLinkText(account.name);
	}
	
	
	public static void addSupplierProducts(SeleniumHelper sh, SupplierProduct supplierProduct){
		
		sh.NavigateToList("Accounts");
		sh.clickGoInListView();
		sh.clickLink(By.linkText(supplierProduct.supplierAccount));
		
		executeSequenceWithRetry(sh, 3, () -> {
			sh.waitForElementToBeClickable(By.xpath("//input[@value='New Supplier Product']"),10);
			sh.clickLink(By.xpath("//input[@value='New Supplier Product']"));
			sh.waitForElementToBeClickable(By.xpath("//input[@id='Name']"),10);
		});
		
		clearAndInputText(sh, By.xpath("//input[@id='Name']"), supplierProduct.supplierProductName);
		
		checkboxSelect(sh, sh.getSelectorBasedOnLabel(isSupplierRequisitionedLabel), supplierProduct.isSupplierRequisitioned);
		clearAndInputText(sh, sh.getSelectorBasedOnLabel(travelCategory), supplierProduct.travelCategory);
		
		clickAndWaitSequence(sh, 20,
		/* click save			*/	saveButton,
		/* wait for edit button	*/	editButton
		);
	}
	
	public static void deleteAllAccounts(SeleniumHelper sh){
		sh.executeJavaScript("result = sforce.connection.query('Select Id from Account'); records = result.getArray('records');  for (var i=0; i< records.length; i++) {    var record = records[i];    sforce.connection.deleteIds([record.Id]);  }");
	}
	
	public static void deleteAllAccountInvoicingModelRecords(SeleniumHelper sh){
		String namespace = sh.isPackagedOrg() ? "KimbleOne__": "";
		sh.executeJavaScript("result = sforce.connection.query('SELECT Id FROM "+namespace+"AccountInvoicingModel__c WHERE "+namespace+"Account__c != null'); records = result.getArray('records');  for (var i=0; i< records.length; i++) {    var record = records[i];    sforce.connection.deleteIds([record.Id]);  }");
	}
}
