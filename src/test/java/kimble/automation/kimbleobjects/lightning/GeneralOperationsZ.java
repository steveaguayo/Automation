package kimble.automation.kimbleobjects.lightning;

import static kimble.automation.helpers.SequenceActions.*;

import org.openqa.selenium.By;

import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;

public class GeneralOperationsZ {
	
	static final String setupMenu = "";

	static final By 
	lightningSpinner = By.name("lightning-spinner"),
	showAllTabs = By.className("slds-icon-waffle"),
	appLauncherHeader = By.xpath("//h2[text()='App Launcher']"),
	setupCog = By.cssSelector("span.icon-settings-component.icon-settings-bolt"),
	setupMenuItem = By.xpath("//li[@role='presentation']/a[text()='Setup Home']"),
	usersDropDown = KBy.last(By.xpath("//a[text()='Users'][@role='treeitem'][@href='javascript:void(0)']")),
	usersMenuItem = KBy.last(By.xpath("//a[text()='Users'][@role='treeitem'][not(@href='javascript:void(0)')]"));
	
	public static void navigateFromAnywhereToTab(SeleniumHelper sh, String tabName) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			goToAllTabsPage(sh);
			navigateToTab(sh, tabName);
		});
	}	
	
	public static void pickListViewItemZ(String stageName, SeleniumHelper sh, String itemName) {
		By listViewPicker = By.cssSelector("span.selectedListView");
//		By listViewPickerOptionAll = By.xpath("//span[normalize-space(@class)='virtualAutocompleteOptionText'][text()='All']");
		By listViewPickerOptionAll = By.xpath("//span[text()='All']");
		By item = By.linkText(itemName);
		try {
			waitClickable(sh, item, 20);
			click(sh, item);
		} catch(Exception e) {
			clickAndWaitSequence(sh, 20, 
					listViewPicker, 
					listViewPickerOptionAll,
					item);
			click(sh, item);
		}
		sh.waitForElementToBeHidden(lightningSpinner);
	}
	
	public static void goToAllTabsPage(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			waitClickable(sh, showAllTabs, 20);
			click(sh, showAllTabs);
			// wait until the screen has loaded - signified the app launcher dialog appearing
			waitClickable(sh, appLauncherHeader, 10);
		});
	}
	
	public static void navigateToTab(SeleniumHelper sh, String tabName) {
		By tab = KBy.last(By.linkText(tabName));
		waitClickable(sh, tab, 20);
		click(sh, tab); 
	}
	
	public static void navigateToSetup(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			waitClickable(sh, setupCog, 20);
			click(sh, setupCog);
			waitClickable(sh, setupMenuItem, 20);
			click(sh, setupMenuItem);
		});
	}
	
	public static void navigateToUsers(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			waitClickable(sh, usersDropDown, 20);
			click(sh, usersDropDown);
			waitClickable(sh, usersMenuItem, 20);
			click(sh, usersMenuItem);
		});
	}

	public static void waitForLoggedInAsSpan(SeleniumHelper sh, String userName) {
		By loggedInAsSpan = By.xpath("//span[contains(text(), \"Logged in as " + userName + "\")]");
		waitClickable(sh, loggedInAsSpan, 40);
	}
	
}
