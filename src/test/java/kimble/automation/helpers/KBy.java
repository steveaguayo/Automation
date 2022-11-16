package kimble.automation.helpers;

import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * This Class holds generalised selectors for elements in a page. This is where you would keep throw away code. Sometimes you can have complicated ways
 * of finding an element in a page. In that case you would create a selector in this class and then reference it in your automation code. This will
 * keep your automation code clean and when the page changes you won't have to go and change your automation code, but you can come into this class
 * and rewrite a selector. Lightning especially has some complications due to what look like bugs, so this is a good place to tackle those problems.
 */
public class KBy {
	
	public static String 
	selectByLabel = "//div[contains(@class, 'uiInput')]/label[contains(@class, 'uiLabel')]/span[normalize-space(text())=\"{{label}}\"]/../..//a[@class='select']",
	selectPopupTrigger = selectByLabel + "/../../..",
	selectPopupByLabeledId = "//div[@aria-labelledby='{{id}}']",
	selectItemByLabelledIdAndItemName = selectPopupByLabeledId + "//a[text()='{{item}}']",
//	lightningRibbonButtonByTitle = "ul.oneActionsRibbon.forceActionsContainer a[title=\"{{title}}\"]";
	lightningLookupMenuItem = "div.lookup__menu div[title*=\"{{item}}\"]",
	lightningRibbonButtonByTitle = "//li[not(@class)]/a[@title='{{title}}']/div[@title='{{title}}']";
	

	public static By label(String labelText) {
		return By.xpath("//label[contains(text(),'" + labelText + "')]");
	}
	
	public static By title(String titleText) {
		return By.xpath("//*[@title='" + titleText + "']");
	}
	
	public static By lightningRibbonButtonByTitle(String title) {
		// There's a bug in lightning where, every time a new sales opp, for instance, is created and you navigate back to the sales opp list
		// a new set of buttons is added and the previous set doesn't get removed. Hence get the last element.
		return last(By.xpath(lightningRibbonButtonByTitle.replace("{{title}}", title)));
	}
	
	public static By lightningLookupMenuItem(String item) {
		return By.cssSelector(lightningLookupMenuItem.replace("{{item}}", item));
	}

	public static By inputByLightningLabel(String labelText) {
		return By.xpath("//div[contains(@class, 'uiInput')]/label[contains(@class, 'uiLabel')]/span[normalize-space(text())=\"" + labelText + "\"]/../..//input");
	}
	
	public static By selectByLightningLabel(String labelText) {
		return By.xpath(selectByLabel.replace("{{label}}", labelText));
	}
	
	public static By selectPopupTriggerByLightningLabel(String labelText) {
		return By.xpath(selectPopupTrigger.replace("{{label}}", labelText));
	}
	
	public static By selectPopupByLightningLabeledId(String id) {
		return By.xpath(selectPopupByLabeledId.replace("{{label}}", id));
	}
	
	public static By lightningLabel(String labelText) {
		return By.xpath("//label/span[contains(text(),'" + labelText + "')]");
	}
	
	public static By selectItemByLabelAndItemName(String label, String item) {
		return new LightningSelectItemByLabelAndItemName(label, item);
	}
	
	public static By lookupItemByLabelAndItemName(String label, String item) {
		return new LightningLookupItemByLabelAndItemName(label, item);
	}
	
	public static By last(By selector) {
		return new By() {
			public List<WebElement> findElements(SearchContext context) {
				List<WebElement> els = context.findElements(selector);
				if(els != null && els.size() > 0)
					return Collections.singletonList(els.get(els.size()-1));
				return Collections.emptyList();
			}
			public String toString() {
				return "last: " + selector.toString();
			}
		};
	}
	
	public static class LightningSelectItemByLabelAndItemName extends By {
		
		String label, item;
		public LightningSelectItemByLabelAndItemName(String aLabel, String aItem) { label = aLabel; item = aItem; }

		public List<WebElement> findElements(SearchContext c) {
			WebElement trigger = c.findElement(selectPopupTriggerByLightningLabel(label));
			trigger.click();
			String id = trigger.getAttribute("id");
			By itemSelector = By.xpath(selectItemByLabelledIdAndItemName.replace("{{id}}", id).replace("{{item}}", item));
			KWaitUntil.clickable(c, itemSelector, 2000);
			return c.findElements(itemSelector);
		}
		
	}
	
	public static class LightningLookupItemByLabelAndItemName extends By {
		
		String label, item;
		public LightningLookupItemByLabelAndItemName(String aLabel, String aItem) { label = aLabel; item = aItem; }

		public List<WebElement> findElements(SearchContext c) {
			WebElement searchBox = c.findElement(inputByLightningLabel(label));
			searchBox.clear();
			searchBox.sendKeys(item);
			By itemSelector = lightningLookupMenuItem(item);
			KWaitUntil.clickable(c, itemSelector, 2000);
			return c.findElements(itemSelector);
		}
		
	}
}
