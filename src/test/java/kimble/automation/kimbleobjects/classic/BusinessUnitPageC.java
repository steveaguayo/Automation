package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.ActivityAssignment;
import kimble.automation.domain.ActivityAssignmentsMany;
import kimble.automation.domain.BusinessUnit;
import kimble.automation.domain.Invoice;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.kimbleobjects.lightning.GeneralOperationsZ;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.remote.server.handler.ClickElement;
import org.openqa.selenium.remote.server.handler.SendKeys;

import com.thoughtworks.selenium.webdriven.commands.Click;

public class BusinessUnitPageC extends BasePageC {
	
	private static final String GENERATE_INVOICE = "//a[@title='Generate Invoice']";
	private static final String NEXT_BUTTON = "//input[@value='Next']";
	private static final String ACCOUNT_ENTER = "//div[@class='pbSubsection']//span[@class='lookupInput']/input[@type='text']";
	private static final String EDIT_BU = "//input[@value='Edit']";
	private static final String SAVE_BU = "//input[@value='Save']";

	static final String 
	LINK_HREF_SPAN = "<linkspantext>",
	LINK_HREF_TEMPLATE = "//span[text()='" + LINK_HREF_SPAN + "']/..",
	BU_NAME_REF = "<linkspantext>",
	BU_LINK_TEMPLATE = "//a[@title='" + BU_NAME_REF + "']";
	
	static final By 
	internalAccountNameLabel = KBy.label("Internal Account");
	
	public BusinessUnitPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	

	public static void LoadExistingByName(SeleniumHelper sh, String buName){	
				String businessunitLinkXPath = LINK_HREF_TEMPLATE.replace(LINK_HREF_SPAN, buName);
				sh.waitForElementToBeClickable(By.xpath(businessunitLinkXPath));
				sh.getWebElement(By.xpath(businessunitLinkXPath)).click();
			}

	public static void setInternalAccount(SeleniumHelper sh, String accountName){	
		
		By edit = By.xpath(EDIT_BU);
		sh.clickLink(edit);
		
		By savebtn = By.xpath(SAVE_BU);
		sh.waitForElementToBeClickable(savebtn);
		
		By internalaccount = sh.getSelectorBasedOnLabel(internalAccountNameLabel);
		clearAndInputText(sh, internalaccount, accountName);
		
		sh.clickLink(savebtn);
		
		sh.waitForElementToBeVisible(edit, 5);

	}
	
	public static void selectGenerateInvoice(SeleniumHelper sh, String buName, String invoicingBU, String invoicingAccount){	
		//selecting the hyperlink of the BU that needs to be Invoiced			
		String businessunitLinkXPath = BU_LINK_TEMPLATE.replace(BU_NAME_REF, invoicingBU);
				sh.waitForElementToBeClickable(By.xpath(businessunitLinkXPath));
				sh.getWebElement(By.xpath(businessunitLinkXPath)).click();
				
				sh.waitForElementToBeClickable(By.xpath(GENERATE_INVOICE));
				sh.clickLink(By.xpath(GENERATE_INVOICE));	
				
//				By accountenter2 = By.xpath(ACCOUNT_ENTER);
//				sh.waitForElementToBeClickable(accountenter2, 10000);
//				sh.checkElementEnabled(accountenter2);
//				sh.sendKeysIfVisibleAndEnabled(accountenter2, invoicingAccount);
//		
//				sh.clickLink(By.xpath(NEXT_BUTTON));		
			
	}
		
}

			
			
			