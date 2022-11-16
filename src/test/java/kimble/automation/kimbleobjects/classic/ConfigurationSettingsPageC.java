package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;


public class ConfigurationSettingsPageC extends BasePageC {

	
	static final String ConfigSettingsURL = "/ConfigurationSettings";
	
	static final By filterBox = By.xpath("//input[@id='filter-input']");
	static final By overrideDateBox = By.xpath("//div[@setting-name='CurrentDate']/input");
	
	static final By 
	TRAVEL_REQ_SLIDER = By.xpath("//div[@setting-name='TravelRequisitionWithItinerary']/i[1]"),	
	SELF_BILL_SUPPLIER_WITHOUT_REQ = By.xpath("//div[@setting-name='EnableAdhocSelfBillSupplierInvoices']/i[1]"),
	GENERATE_SUPPLIER_INVOICE_REF = By.xpath("//div[@setting-name='GenerateSelfBillRefForManualSuppInv']/i[1]"),
	PARTIAL_SUPPLIER_CREDIT_NOTE = By.xpath("//div[@setting-name='EnablePartialSupplierCreditNotes']/i[1]");
	
	
	public ConfigurationSettingsPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public static void overrideDateService(SeleniumHelper sh, String date) {
//		String configSettingsURL = sh.getApexBaseFromCurrentUrl() + ConfigSettingsURL;
		sh.navigateToVFPage("ConfigurationSettings");
//		sh.goToUrl(configSettingsURL);
		sh.waitForElementToBeClickable(filterBox);
		
		clearAndInputText(sh, filterBox, "Override Current Date");

		sh.waitForElementToBeClickable(overrideDateBox);
		clearAndInputText(sh, overrideDateBox, date);
		sh.waitForElementToBeClickable(overrideDateBox);
		
	}
	
	public static void enableTravelReqConfigSetting(SeleniumHelper sh, String configSetting){
		sh.navigateToVFPage("ConfigurationSettings");
		sh.waitForElementToBeClickable(filterBox);
		clearAndInputText(sh, filterBox, configSetting);
		
		String switchValue = sh.getWebElement(TRAVEL_REQ_SLIDER).getAttribute("title");
		
		if(switchValue.contains("off")){
			sh.clickLink(TRAVEL_REQ_SLIDER);
			sh.sleep(2000);
			sh.waitForElementToBeClickable(TRAVEL_REQ_SLIDER);
			
			sh.refreshBrowser();
			sh.waitForPageLoadComplete(15);
			clearAndInputText(sh, filterBox, configSetting);
			sh.assertEquals(sh.getWebElement(TRAVEL_REQ_SLIDER).getAttribute("title"), "on", "Config Setting is set to Off ");
		}

	}
	
	public static void disableTravelReqConfigSetting(SeleniumHelper sh, String configSetting){
		sh.navigateToVFPage("ConfigurationSettings");
		sh.waitForElementToBeClickable(filterBox);
		clearAndInputText(sh, filterBox, configSetting);
		
		String switchValue = sh.getWebElement(TRAVEL_REQ_SLIDER).getAttribute("title");
		
		if(switchValue.contains("on")){
			sh.clickLink(TRAVEL_REQ_SLIDER);
			sh.sleep(2000);
			sh.waitForElementToBeClickable(TRAVEL_REQ_SLIDER);
			
			sh.refreshBrowser();
			sh.waitForPageLoadComplete(15);
			clearAndInputText(sh, filterBox, configSetting);
			sh.assertEquals(sh.getWebElement(TRAVEL_REQ_SLIDER).getAttribute("title"), "off", "Config Setting is set to On ");
		}
	}
	
	public static void enableSuppInvoiceWithoutReqConfigSetting(SeleniumHelper sh, String configSetting){
		sh.navigateToVFPage("ConfigurationSettings");
		sh.waitForElementToBeClickable(filterBox);
		clearAndInputText(sh, filterBox, configSetting);
		
		String switchValue = sh.getWebElement(SELF_BILL_SUPPLIER_WITHOUT_REQ).getAttribute("title");
		
		if(switchValue.contains("off")){
			sh.clickLink(SELF_BILL_SUPPLIER_WITHOUT_REQ);
			sh.sleep(2000);
			sh.waitForElementToBeClickable(SELF_BILL_SUPPLIER_WITHOUT_REQ);
			
			sh.refreshBrowser();
			sh.waitForPageLoadComplete(15);
			clearAndInputText(sh, filterBox, configSetting);
			sh.assertEquals(sh.getWebElement(SELF_BILL_SUPPLIER_WITHOUT_REQ).getAttribute("title"), "on", "Config Setting is set to Off ");
		}

	}
	
	
	
	public static void enablePartialSuppCreditNoteConfigSetting(SeleniumHelper sh, String configSetting){
		sh.navigateToVFPage("ConfigurationSettings");
		sh.waitForElementToBeClickable(filterBox);
		clearAndInputText(sh, filterBox, configSetting);
		
		String switchValue = sh.getWebElement(PARTIAL_SUPPLIER_CREDIT_NOTE).getAttribute("title");
		
		if(switchValue.contains("off")){
			sh.clickLink(PARTIAL_SUPPLIER_CREDIT_NOTE);
			sh.sleep(2000);
			sh.waitForElementToBeClickable(PARTIAL_SUPPLIER_CREDIT_NOTE);
			
			sh.refreshBrowser();
			sh.waitForPageLoadComplete(15);
			clearAndInputText(sh, filterBox, configSetting);
			sh.assertEquals(sh.getWebElement(PARTIAL_SUPPLIER_CREDIT_NOTE).getAttribute("title"), "on", "Config Setting is set to Off ");
		}

	}
	
	public static void enableGenerateSupplierInvoiceRefConfigSetting(SeleniumHelper sh, String configSetting){
		sh.navigateToVFPage("ConfigurationSettings");
		sh.waitForElementToBeClickable(filterBox);
		clearAndInputText(sh, filterBox, configSetting);
		
		String switchValue = sh.getWebElement(GENERATE_SUPPLIER_INVOICE_REF).getAttribute("title");
		
		if(switchValue.contains("off")){
			sh.clickLink(GENERATE_SUPPLIER_INVOICE_REF);
			sh.sleep(2000);
			sh.waitForElementToBeClickable(GENERATE_SUPPLIER_INVOICE_REF);
			
			sh.refreshBrowser();
			sh.waitForPageLoadComplete(15);
			clearAndInputText(sh, filterBox, configSetting);
			sh.assertEquals(sh.getWebElement(GENERATE_SUPPLIER_INVOICE_REF).getAttribute("title"), "on", "Config Setting is set to Off ");
		}

	}
	public static void disableSuppInvoiceWithoutReqConfigSetting(SeleniumHelper sh, String configSetting){
		sh.navigateToVFPage("ConfigurationSettings");
		sh.waitForElementToBeClickable(filterBox);
		clearAndInputText(sh, filterBox, configSetting);
		
		String switchValue = sh.getWebElement(SELF_BILL_SUPPLIER_WITHOUT_REQ).getAttribute("title");
		
		if(switchValue.contains("on")){
			sh.clickLink(SELF_BILL_SUPPLIER_WITHOUT_REQ);
			sh.sleep(2000);
			sh.waitForElementToBeClickable(SELF_BILL_SUPPLIER_WITHOUT_REQ);
			
			sh.refreshBrowser();
			sh.waitForPageLoadComplete(15);
			clearAndInputText(sh, filterBox, configSetting);
			sh.assertEquals(sh.getWebElement(SELF_BILL_SUPPLIER_WITHOUT_REQ).getAttribute("title"), "off", "Config Setting is set to On ");
		}
	}
	
	public static void disableGenerateSupplierInvoiceRefConfigSetting(SeleniumHelper sh, String configSetting){
		sh.navigateToVFPage("ConfigurationSettings");
		sh.waitForElementToBeClickable(filterBox);
		clearAndInputText(sh, filterBox, configSetting);
		
		String switchValue = sh.getWebElement(GENERATE_SUPPLIER_INVOICE_REF).getAttribute("title");
		
		if(switchValue.contains("on")){
			sh.clickLink(GENERATE_SUPPLIER_INVOICE_REF);
			sh.sleep(2000);
			sh.waitForElementToBeClickable(GENERATE_SUPPLIER_INVOICE_REF);
			
			sh.refreshBrowser();
			sh.waitForPageLoadComplete(15);
			clearAndInputText(sh, filterBox, configSetting);
			sh.assertEquals(sh.getWebElement(GENERATE_SUPPLIER_INVOICE_REF).getAttribute("title"), "off", "Config Setting is set to On ");
		}
	}
	
	public static void disablePartialSuppCreditNoteConfigSetting(SeleniumHelper sh, String configSetting){
		sh.navigateToVFPage("ConfigurationSettings");
		sh.waitForElementToBeClickable(filterBox);
		clearAndInputText(sh, filterBox, configSetting);
		
		String switchValue = sh.getWebElement(PARTIAL_SUPPLIER_CREDIT_NOTE).getAttribute("title");
		
		if(switchValue.contains("on")){
			sh.clickLink(PARTIAL_SUPPLIER_CREDIT_NOTE);
			sh.sleep(2000);
			sh.waitForElementToBeClickable(PARTIAL_SUPPLIER_CREDIT_NOTE);
			
			sh.refreshBrowser();
			sh.waitForPageLoadComplete(15);
			clearAndInputText(sh, filterBox, configSetting);
			sh.assertEquals(sh.getWebElement(PARTIAL_SUPPLIER_CREDIT_NOTE).getAttribute("title"), "off", "Config Setting is set to On ");
		}
	}
}
