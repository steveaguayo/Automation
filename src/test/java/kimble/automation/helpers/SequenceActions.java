package kimble.automation.helpers;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SequenceActions {
	
	public static class AlertItem extends By {
		public List<WebElement> findElements(SearchContext arg0) { return null; }
	}
	
	public static final AlertItem alertOk = new AlertItem();
	public static final AlertItem alertCancel = new AlertItem();
	public static final AlertItem alertText = new AlertItem();
	
	public static void executeSequence(SeleniumHelper sh, Runnable... executables) {
		for(Runnable r : executables)
			r.run();
	} 
	
	public static void executeSequenceWithRefreshRetry(SeleniumHelper sh, int retryCount, Runnable... executables) {
		boolean success = false;
		for(int i = 0; i < retryCount-1; i++) {
			try { 
				executeSequence(sh, executables); 
				success = true;
				break;
			} catch(Exception e) { 
				sh.refreshBrowser();sh.waitForPageLoadComplete(20); 
			}
		}
		if(!success)
			executeSequence(sh, executables); 
	}
	
	public static void executeSequenceWithRefreshRetry(SeleniumHelper sh, int retryCount, List<? extends Runnable> executables) {
		executeSequenceWithRefreshRetry(sh, retryCount, executables.toArray(new Runnable[executables.size()]));
	}
	
	public static void executeSequenceWithRetry(SeleniumHelper sh, int retryCount, Runnable... executables) {
		boolean success = false;
		for(int i = 0; i < retryCount-1; i++) {
			try { 
				executeSequence(sh, executables); 
				success = true;
				break;
			} catch(Exception e) {}
		}
		if(!success)
			executeSequence(sh, executables); 
	}
	
	public static void executeSequenceWithRetry(SeleniumHelper sh, int retryCount, List<? extends Runnable> executables) {
		executeSequenceWithRetry(sh, retryCount, executables.toArray(new Runnable[executables.size()]));
	}
	
	public static abstract class SequenceAction implements Runnable {
		SeleniumHelper sh;
		boolean isRun = true;
		public SequenceAction(SeleniumHelper aSh) { sh = aSh; }
	}
	
	public static void inputText(SeleniumHelper aSh, By aTarget, String aText) {
		if(aText != null) 
			aSh.getWebElement(aTarget).sendKeys(aText);
	}
	
	/** Use this with especially with date inputs. It's a good choice for other text inputs also, but will cause navigation through page history if used with
	 * 	something other than text inputs. 
	 */
	public static void clearAndInputText(SeleniumHelper aSh, By aTarget, String aText) {
		if(aText == null)
			return;
		String content = aSh.getWebElementTextOrNullWithRetry(aTarget);
		StringBuilder sb = new StringBuilder();
		if(content != null)
			for(int i = 0; i < content.length(); i++)
				sb.append("\u0008");
		sb.append(aText);
		sb.append(Keys.TAB);
		if(!aSh.isLightning() || !(aSh.getCurrentUrl().contains("Account") && aSh.getCurrentUrl().contains("lightning")))
			sb.append(Keys.ESCAPE);
		else{
			sb.append(Keys.TAB);
			sb.append(Keys.TAB);
			sb.append(Keys.TAB);
		}

		aSh.getWebElement(aTarget).sendKeys(sb.toString());
		try {
			waitHidden(aSh, By.cssSelector("div#ui-datepicker-div"), 5);
		} catch(Exception e) {
			// if a dynamic update happened before the last escape key was sent the date selector might still be visible so try escaping again
			aSh.getWebElement(aTarget).sendKeys(Keys.ESCAPE);
			waitHidden(aSh, By.cssSelector("div#ui-datepicker-div"), 5);
		}
	}

	public static void clearAndInputText(SeleniumHelper aSh, By aTarget, Number aNumber) {
		if(aNumber == null)
			return;
		clearAndInputText(aSh, aTarget, aNumber.toString());
	}
	
	public static void clearAndInputTextIfVisibleAndEnabled(SeleniumHelper aSh, By aTarget, String aText) {
		if(aText == null || !aSh.checkElementVisible(aTarget) || !aSh.checkElementEnabled(aTarget))
			return;
		clearAndInputText(aSh, aTarget, aText);
	}
	
	public static void clearAndInputTextIfVisibleAndEnabled(SeleniumHelper aSh, By aTarget, Number aNumber) {
		if(aNumber == null)
			return;
		clearAndInputTextIfVisibleAndEnabled(aSh, aTarget, aNumber.toString());
	}
	
	public static void dropdownSelect(SeleniumHelper aSh, By aTarget, String aText) { 
		if(aText != null) {
			aSh.selectByVisibleText(aTarget, aText);
			try {
				aSh.getWebElement(aTarget).sendKeys("" + Keys.TAB + Keys.ESCAPE);
			} catch(StaleElementReferenceException e) {
				try {
					aSh.getWebElement(aTarget).sendKeys("" + Keys.TAB + Keys.ESCAPE);
				} catch(StaleElementReferenceException ee) {
					aSh.getWebElement(aTarget).sendKeys("" + Keys.TAB + Keys.ESCAPE);
				}
			}
		}
	}

	public static void dropdownSelectWithRetry(SeleniumHelper aSh, By aTarget, String aText, int aTries) {
		for(int i = 0; i < aTries; i++) {
			try {
				dropdownSelect(aSh, aTarget, aText);
				return;
			} catch(Exception e) {
				if(i+1 == aTries)
					throw new RuntimeException("failed dropdown select after: " + aTries + " tries", e);
			}
		}
	}
	
	public static void checkboxSelect(SeleniumHelper aSh, By aTarget, Boolean aSelect) { 
		if(aSelect == null) 
			return;
		WebElement el = aSh.getWebElement(aTarget);
		if ( el.isSelected() != aSelect )
			el.click();
	}
	
	public static void checkboxSelectIfVisibleAndEnabled(SeleniumHelper aSh, By aTarget, Boolean aSelect) {
		if(aSelect == null || !aSh.checkElementVisible(aTarget) || !aSh.checkElementEnabled(aTarget))
			return;
		checkboxSelect(aSh, aTarget, aSelect);
	}
	
	public static void checkboxSelectMany(SeleniumHelper aSh, By aTarget, Boolean aSelect) {
		if(aSelect == null) 
			return;
		aSh.waitForElementToBeClickable(aTarget);
		List<WebElement> els = aSh.getWebElements(aTarget);
		for(WebElement el : els)
			if ( el.isSelected() != aSelect )
				el.click();
	}
	
	public static void radioButtonSelect(SeleniumHelper aSh, By aTarget, Boolean aSelect) { 
		checkboxSelect(aSh, aTarget, aSelect); 
	}
	
	public static void click(SeleniumHelper aSh, By aTarget) {
		if(aTarget instanceof AlertItem) {
			Alert alert = aSh.getWD().switchTo().alert();
			if(aTarget.equals(alertOk))
				alert.accept();
			else if(aTarget.equals(alertCancel))
				alert.dismiss();
		}
		else
			aSh.getWebElement(aTarget).click(); 
	}
	
	public static void clickIfExists(SeleniumHelper aSh, By aTarget) {
		if(exists(aSh, aTarget, 5))
			click(aSh, aTarget);
	}
		
	public static void clickAll(SeleniumHelper aSh, By aTarget) {
		if(aTarget instanceof AlertItem)
			click(aSh, aTarget);
		else
			for(WebElement we : aSh.getWebElements(aTarget))
				we.click(); 
	}
	
	public static void hoverOn(SeleniumHelper aSh, By aTarget) {
		if(aTarget instanceof AlertItem)
			throw new RuntimeException("Can't hover over alert items");
		else
			aSh.hoverOnElement(aTarget); 
	}
	
	public static void waitClickable(SeleniumHelper aSh, By aTarget, int aTime) { 
		if(aTarget instanceof AlertItem) {
			WebDriverWait wait = new WebDriverWait(aSh.getWD(), aTime);
			wait.until(ExpectedConditions.alertIsPresent());
			aSh.getWD().switchTo().alert();
		}
		else
			aSh.waitForElementToBeClickable(aTarget, aTime);
	}
	
	public static void waitHidden(SeleniumHelper aSh, By aTarget, int aTime) { 
		aSh.waitForElementToBeHidden(aTarget, aTime); 
	}
	
	public static void clickAndWaitSequence(SeleniumHelper sh, int time, By... selectors) {
		for(int i = 0; i < selectors.length - 1; i++) {
			waitClickable(sh, selectors[i], time);
			click(sh, selectors[i]);
		}
		waitClickable(sh, selectors[selectors.length - 1], time);
	}
	
	public static void clickAndWaitSequenceWithRetry(SeleniumHelper sh, int time, int tries, By... selectors) {
		for(int i = 0; i < tries; i++) {
			try {
				clickAndWaitSequence(sh, time, selectors);
				return;
			} catch(Exception e) {
				if(i+1 == tries)
					throw new RuntimeException("failed click and wait sequence after: " + tries + " tries", e);
			}
		}
	}
	
	// Validations
	
	public static void validateStringValue(SeleniumHelper aSh, By aTarget, String aVal, String aDescription) {
		if(aVal != null)
			aSh.assertEquals(aSh.getWebElementTextOrNull(aTarget), aVal, aDescription); 
	}
	
	public static void validateNumberValue(SeleniumHelper aSh, By aTarget, Number aVal, String aDescription) {
		if(aVal != null)
			aSh.assertEquals(aSh.getWebElementNumberOrNull(aTarget), aVal, aDescription); 
	}
	
	public static boolean exists(SeleniumHelper aSh, By aTarget, int aTime) {
		try {
			waitClickable(aSh, aTarget, aTime);
			return true;
		} catch(Exception e) { return false; }
	}
	
	// lightning
	
	public static void lightningDropdownSelect(SeleniumHelper aSh, String selectLabel, String aText) { 
		if(aText != null)
			click(aSh, KBy.selectItemByLabelAndItemName(selectLabel, aText));
	}
	
	public static void lightningLookupSelect(SeleniumHelper aSh, String selectLabel, String aText) { 
		if(aText != null)
			click(aSh, KBy.lookupItemByLabelAndItemName(selectLabel, aText));
	}
}
