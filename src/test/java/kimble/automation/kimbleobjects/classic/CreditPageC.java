package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import kimble.automation.domain.Account;
import kimble.automation.domain.AccountCredits;
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
import org.openqa.selenium.support.FindBy;

import com.thoughtworks.selenium.webdriven.commands.Click;

public class CreditPageC extends BasePageC {
	
	
	public static final String CREDITS = "DeliveryElementRevenueRedirect";

	static final By
	
	creditNameLabel = KBy.label("Name"),
	creditDate = KBy.label("Credit Date");
	
	private static final String CREDIT_VALUE = "input[name$='inputCreditValue']";
	private static final String CREDIT_REVENUE = "input[name$='id168']";

	@FindBy(css = "input[value$='Save']")
	private WebElement creditSave;	
	
	@FindBy(css = "input[value$='Edit']")
	private WebElement creditEdit;	
	
	public CreditPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	
	public static void NavigateToCredits(SeleniumHelper sh) {
		sh.waitForLightningSpinnerToBeHidden();
		sh.clickMenuItem(PagesC.CREDITS, false);
	}

	
	public void enterCreditDetails(SeleniumHelper sh, AccountCredits ac){	
	
		sh.waitForPageLoadComplete(10);
		By
			creditNameInput = sh.getSelectorBasedOnLabel(creditNameLabel),
			creditDateInput = sh.getSelectorBasedOnLabel(creditDate);
			
		clearAndInputText(sh, creditNameInput, ac.creditName);
		clearAndInputText(sh, creditDateInput , ac.creditDate);
	
		//getSelectorBasedOnLabel does not work for Credit Revenue/Value as there is no for attribute
		sh.sendKeysIfVisibleAndEnabled(By.cssSelector(CREDIT_REVENUE), ac.creditRevenue);
//		sh.sendKeysIfVisibleAndEnabled(By.cssSelector(CREDIT_VALUE), ac.creditValue);
		sh.clearAndSendKeysIfVisibleAndEnabled(By.cssSelector(CREDIT_VALUE), ac.creditValue);
	
		creditSave.click();
		
		sh.waitForElementToBeVisible(creditEdit, 20);
		
	}
		
}

			
			
			