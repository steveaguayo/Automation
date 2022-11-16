package kimble.automation.helpers;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.ui.FluentWait;

/**
 * This is a class for implementing custom waits. For some reason the waits, provided by the Selenium framework, operate
 * on a WebDriver instead of the more general SearchContext. When implementing custom selectors in KBy, one can only work on a SearchContext, 
 * so the framework default waits don't work unless you're sure that you'll be working on a WebDriver instead of a WebElement, in which
 * case you can do a type cast and use the default waits.
 */
public class KWaitUntil {

	public static void clickable(SearchContext context, By selector, int timeoutMillis) {
		FluentWait<SearchContext> wait = new FluentWait(context).withTimeout(timeoutMillis, TimeUnit.MILLISECONDS).withMessage("Waited " + 
				timeoutMillis + " milliseconds for element to be clickable by selector: " + selector);
		wait.until( KExpectedCondition.clickable(selector) );
	}

}
