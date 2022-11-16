package kimble.automation.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.base.Predicate;

/**
 * This is a class for implementing custom expected conditions. For some reason the conditions, provided by the Selenium framework, operate
 * on a WebDriver instead of the more general SearchContext. When implementing custom selectors in KBy, one can only work on a SearchContext, 
 * so the framework default conditions don't work unless you're sure that you'll be working on a WebDriver instead of a WebElement, in which
 * case you can do a type cast and use the default conditions.
 */
public class KExpectedCondition {
	
	public static Predicate<SearchContext> clickable(By selector) {
		return c -> {
			try {
				WebElement e = c.findElement(selector);
				return e != null && e.isDisplayed();
			} catch(NoSuchElementException e) {
				return false;
			}
		};
	}

}
