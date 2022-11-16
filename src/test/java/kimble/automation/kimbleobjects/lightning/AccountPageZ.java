package kimble.automation.kimbleobjects.lightning;

import static kimble.automation.helpers.SequenceActions.*;
import kimble.automation.domain.Account;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class AccountPageZ {
	
	static final By 
	newButton = KBy.title("New"),
	editButton = KBy.title("Edit"),
	saveButton = KBy.title("Save"),
	
	nameInput = KBy.inputByLightningLabel("Account Name"),
	isSupplierCheckbox = KBy.inputByLightningLabel("Is Supplier"),
	
	typeSelect = KBy.selectPopupTriggerByLightningLabel("Type"),
	industrySelect = KBy.selectPopupTriggerByLightningLabel("Industry"),
	
	billingStreetInput = KBy.inputByLightningLabel("Billing Street"),
	billingCityInput = KBy.inputByLightningLabel("Billing City"),
	billingStateInput = KBy.inputByLightningLabel("Billing State/Province"),
	billingZipInput = KBy.inputByLightningLabel("Billing Zip/Postal Code"),
	billingCountryInput = KBy.inputByLightningLabel("Billing Country"),
	shippingStreetInput = KBy.inputByLightningLabel("Shipping Street"),
	shippingCityInput = KBy.inputByLightningLabel("Shipping City"),
	shippingStateInput = KBy.inputByLightningLabel("Shipping State/Province"),
	shippingZipInput = KBy.inputByLightningLabel("Shipping Zip/Postal Code"),
	shippingCountryInput = KBy.inputByLightningLabel("Shipping Country"),
	invoicingCurrencyInput = KBy.inputByLightningLabel("Invoicing Currency"),
	allowPartItemInvoicingCheckbox = KBy.inputByLightningLabel("Allow Part Item Invoicing"),
	taxCode = KBy.inputByLightningLabel("Tax Code");
	
		
	public static void createNew(SeleniumHelper sh, Account account) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 30,
			/* click new			*/	newButton,
			/* wait for name input	*/	nameInput
			);
			
			clearAndInputText(sh, nameInput, account.name);
			lightningDropdownSelect(sh, "Type", account.type);
			checkboxSelect(sh, isSupplierCheckbox, account.isSupplier);
			lightningLookupSelect(sh, "Business Unit (Operating)", account.operatingBusinessUnit);
			lightningLookupSelect(sh, "Business Unit (Trading)", account.tradingBusinessUnit);
			lightningDropdownSelect(sh, "Industry", account.industry);
			
			lightningLookupSelect(sh, "Billing Contact", account.billingContact);
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
			clearAndInputText(sh, invoicingCurrencyInput, account.invoicingCurrency);
			lightningLookupSelect(sh, "Purchase Order Rule", account.purchaseOrderRule);

//			wait needed here 
			sh.waitMilliseconds(1000);
			click(sh, taxCode);
			lightningLookupSelect(sh, "Tax Code", account.taxCode);
			lightningLookupSelect(sh, "Invoice Format", account.invoiceFormat);
			lightningLookupSelect(sh, "Locale", account.locale);

			checkboxSelect(sh, allowPartItemInvoicingCheckbox, account.allowPartItemInvoicing);

			clickAndWaitSequence(sh, 20,
			/* click save			*/	saveButton,
			/* wait for edit button	*/	editButton
			);
		});
	}
}
