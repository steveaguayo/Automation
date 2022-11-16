package kimble.automation.helpers;

import static kimble.automation.helpers.SequenceActions.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import kimble.automation.kimbleobjects.classic.ListGridItemC;
import kimble.automation.kimbleobjects.classic.LoginPageC;
import kimble.automation.kimbleobjects.classic.PagesC;
import kimble.automation.Config;
import kimble.automation.KimbleOneTest;
import kimble.automation.TestInterceptor;
import kimble.automation.helpers.SequenceActions.AlertItem;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.TestException;

import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDeviceActionShortcuts;
import io.appium.java_client.android.AndroidKeyCode;

public class SeleniumHelper {

	public static Config config;
	public By
	
    alertOkButton = By.xpath("//form[contains(@class, 'answers')]/input[@value='OK']");
	
	static final By
	classicIFrame = By.cssSelector("div.active.oneContent iframe"),
	classicIFrameContext = By.xpath("//div[contains(@id,'vfFrame')]"),
	
	DEFAULT_EDIT_LINK = By.xpath("//span[.='Edit']/parent::a"),
	DEFAULT_DELETE_LINK = By.xpath("//span[.='Del']/parent::a"),
	DEFAULT_OPEN_LINK = By.xpath("//span[.='Edit']/parent::a/parent::div/parent::td/following-sibling::td/div/a"),
	
	listViewAllSelected = By.xpath("//span[text()='All'][@class='listItemSelected']"),
	listViewAllUnselected = By.xpath("//span[text()='All'][@class='listItemPad']"),
	
	LightningPopUp = By.xpath("//a[@title='Close']");
	
	
	private static final String 
	A_HREF_HOME_SHOW_ALL_TABS_JSP = "a[href$='/home/showAllTabs.jsp']",

	KIMBLEONE_NAMESPACE = "KimbleOne__",

	POPUP_CALENDAR = "div[id='ui-datepicker-div']",
	CLOSE_CALENDAR_JS = "$(\"" + POPUP_CALENDAR + "\")[0].style.display='none'",
	SF_PAGE_ERROR_CONTAINER = "span[id$='theError']",
	GO = "go",
	LKSRCH = "lksrch",
	FCF = "fcf",
	ROWSPERPAGE2 = "&rowsperpage=",
	LINKTEXTTOKEN = "<LINKTEXT>",
	ONCLICKTOKEN = "<ONCLICK>",
	LINKBYLINKANDONCLICKXPATH = "//a[text()='"+LINKTEXTTOKEN+"' and contains(@onclick,'"+ONCLICKTOKEN+"')]",
	LINKBYONCLICKXPATH = "//a[contains(@onclick,'"+ONCLICKTOKEN+"')]",

	HREF_ATTR = "href",
	WHOOPS = "Whoops",
	WHOOPS_ERROR_LINK = "here",
	ON_SCREEN_ERROR = "On screen Error: ",
	ERROR = "Error:",
	ERRORS = "Errors",
	DIV_CLASS_MESSAGE_TEXT = "div[class='messageText']",
	
	THREE_LEVEL_CHECKBOX_XPATH_TEMPLATE = "//*[@title=\"LEVEL_ONE_LABEL\"]/following-sibling::ul/li/a[@title=\"LEVEL_TWO_LABEL\"]/following-sibling::ul/li/a[@title=\"LEVEL_THREE_LABEL\"]/ins[1]",	
	
	TIME_ENTRY_THREE_LEVEL_CHECKBOX_XPATH_TEMPLATE = "//*[@id=\"TI\"]/a/following-sibling::ul/li/a[@title=\"LEVEL_TWO_LABEL\"]/following-sibling::ul/li/a[@title=\"LEVEL_THREE_LABEL\"]/ins[1]";	

	public final String 
	DAYMONTHYEARFORMAT = "dMMMMyyyy",
	MONTHYEARFORMAT = "MMMMyyyy",
	DAYNUMBERFORMAT = "EEEd",
	DAYNATURALNUMBERFORMAT = "d",
	YEARMONTHDAYPARAMFORMAT = "yyyy-MM-dd";

	public String validationFailure = "";
	public Boolean lightningOverride = null;
	public Boolean isInClassicIFrame = null;
	
    private static boolean debugRun = false, packagedOrg = false, isLightning = false;
    private KimbleOneTest currentTest = null;
    
	private WebDriver theWD;
	
	// this is populated after initialiseBrowser()
	public String defaultWindowHandle = null;
	
	public SeleniumHelper(WebDriver driver){
		setWD(driver);
	}
	
	public boolean isInClassicFrame() {
//		return state.isInClassicFrame;
		return isInClassicIFrame;
	}
	
	public JavascriptExecutor getJsExecutor() {
		return (JavascriptExecutor)theWD;
	}
	
//	/** This will initialise the urls from the JobState object. See {@link #initialiseUrls(String lightningUrl, String classicUrl) initialiseUrls}. */
//	public void initialiseUrls() {
//		initialiseUrls(state.lightningUrl, state.classicUrl);
//	}
//
//	/** 
//	 * Due to the complications brought by lightning there are now two urls that need to be handled. The browser 'lightning' url and the iframe classic
//	 * url. Obviously when running in classic, only the classic url is handled.
//	 */
//	public void initialiseUrls(String lightningUrl, String classicUrl) {
//		if(isLightning() && lightningUrl != null) {
//			switchOutOfClassicIframe();
//			if(!lightningUrl.equals(getCurrentUrl()))
//				goToUrl(lightningUrl);
//		}
//		if(classicUrl != null) {
//			try {
//				switchToClassicIframe();
//				if(!classicUrl.equals(getCurrentUrl()))
//					goToUrl(classicUrl);
//			} catch(Exception e) {
//				LogErrorMessageLine("Failed to find the classic iframe - this can be due to lightnings unpredictable navigation behaviour");
//			}
//		}
//	}
	
	/**
	 * Depending on whether the execution is in lightning or classic or in a classic iframe navigation to a url needs to be done differently.
	 */
	public void goToUrl(String url){
		for(int tries = 0; tries < 3 && !url.equals(getCurrentUrl()); tries++) {
			if(isLightning()){
				String urlL = url.replaceAll("InvoicePrintT1", "KimbleOne__InvoicePrintT1");
				getJsExecutor().executeScript("if(!window.sforce || !window.sforce.one) window.location.href = arguments[0]; else window.sforce.one.navigateToURL(arguments[0], false);", urlL);
			}
			else{
				getWD().navigate().to(url);
			}
			waitForPageLoadComplete(20);
		}
	}
	
	public static Set<String> returnWindows(SeleniumHelper aSh) {
		return aSh.getWD().getWindowHandles();
	}
	
	public static String returnWindow(SeleniumHelper aSh) {
		return aSh.getWD().getWindowHandle();
	}
		
	public boolean 
	isInClassicFrame = true;
	
	/** this method should only be used by stages for context switching */
	public void switchToClassicIframe() {
		if(!isLightning() || isInClassicFrame())
			return;
		waitClickable(this, classicIFrame, 40);
		theWD.switchTo().frame(getWebElement(classicIFrame));
//		state.isInClassicFrame = true;
		isInClassicIFrame = true;
	}
	
	public void switchToAccountEditIframe() {
		WebElement accountEditIframe = getWebElement(By.xpath("//iframe[@title='AccountEdit']"));
	//	waitClickable(this, accountEditIframe, 40);
		
		theWD.switchTo().frame(accountEditIframe);
//		state.isInClassicFrame = true;
		isInClassicIFrame = true;
	}
	
	public void switchOutOfAccountEditIframe() {
		theWD.switchTo().window(defaultWindowHandle);
//		state.isInClassicFrame = false;
		isInClassicIFrame = false;
	}
	
	public void switchToClassicIframeContext() {
		if(isLightning()) {	
			theWD.switchTo().parentFrame();
//			System.out.println("frames after switching to parent: " +theWD.findElements(By.tagName("iframe")).size());	
			waitClickable(this, classicIFrame, 40);
			theWD.switchTo().frame(getWebElement(classicIFrame));
//			state.isInClassicFrame = true;
		}
	}
	
	/** this method should only be used by stages for context switching */
	public void switchOutOfClassicIframe() {
		if(!isLightning() || !isInClassicFrame())
			return;
		theWD.switchTo().window(defaultWindowHandle);
//		state.isInClassicFrame = false;
		isInClassicIFrame = false;
	}
	
	public static boolean IsDebugRun() {
		return debugRun;
	}
	
	public static void SetDebugRun(boolean isDebugRun) {
		debugRun = isDebugRun;
	}
	
	public static void SetPackagedOrg(boolean isPackagedOrg) {
		setPackagedOrg(isPackagedOrg);
	}
	
	public static void SetLightningEnabled(boolean isLightning) {
		setLightningEnabled(isLightning);
	}
	
	public void SetCurrentTest(KimbleOneTest currentTest) {
		this.currentTest = currentTest;
	}
	
	public String getCurrentUrl(){
		return getWD().getCurrentUrl();
	}
	
	public void returnToPreviousURL(){
		getWD().navigate().back();
	}
	
	public void recordValidationFailure(String failureMessage) {
		validationFailure = failureMessage;
	}
	
	public void resetValidationFailure() {
		validationFailure = "";
	}

	public boolean hasValidationFailure() {
		return validationFailure.equals("");
	}
	
	public boolean isLightning() {
//		if(lightningOverride == null)
//			return Config.lightning ? true : false; 
//		return lightningOverride;
		return SeleniumHelper.isLightning;
	}

	public void waitForMenuItemToBeClickable(String PageName)
	{
		forceOpenCollapsedMenus();
		waitForElementToBeClickable(By.cssSelector("a[href*='" + PageName + "?']"));
	}
	
	public void clickMenuItem(String PageName)
	{
		clickMenuItem(PageName, false);
	}
	
	public void clickMenuItem(String PageName, boolean withOutFilterId)
	{
		forceOpenCollapsedMenus();
		clickLinkByPageName(PageName, withOutFilterId);
	}

	public void clickMenuItemWithRetry(String PageName)
	{
		try {
			forceOpenCollapsedMenus();
			clickLinkByPageName(PageName, 5);
		} catch(Exception e) {
			try {
				forceOpenCollapsedMenus();
				clickLinkByPageName(PageName, 5);
			} catch(Exception ee) {
				forceOpenCollapsedMenus();
				clickLinkByPageName(PageName, 5);
			}
		}
	}

	public void clickMiniCharmByPageName(String PageName)
	{
		// odd syntax here is the only way without xpath 2.0 to do a case insensitive match on the link href
		clickLink(By.xpath("//span[contains(translate(@charm-detail-page, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'" + PageName.toLowerCase() + "')]/span[@class='miniCardTile']"));
	}

	public void clickLinkByPageName(String PageName)
	{
		clickLinkByPageName(PageName, false);
	}

	public void clickLinkByPageName(String PageName, boolean withOutFilterId)
	{
		clickLinkByPageName(PageName, 20, withOutFilterId);
	}
	
	public void clickLinkByPageName(String PageName, int waitSecs)
	{
		clickLinkByPageName(PageName, waitSecs, false);
	}
	
	public void clickLinkByPageName(String PageName, int waitSecs, boolean withOutFilterId)
	{
		By link = getLinkByPageName(PageName, withOutFilterId);
		waitForElementToBeClickable(link, waitSecs);
		clickLink(link);
	}
	
	public void hoverOverLinkByPageName(String PageName)
	{
		hoverOverLinkByPageName(PageName, 20);
	}
	
	public void hoverOverLinkByPageName(String PageName, int waitSecs) {
		hoverOverLinkByPageName(PageName, waitSecs, false);
	}
	
	public void hoverOverLinkByPageNameClickandHold(String PageName, int waitSecs) {
		hoverOverLinkByPageNameClickandHold(PageName, waitSecs, false);
	}
	
	public void hoverOverLinkByPageName(String PageName, int waitSecs, boolean withOutFilterId)
	{
		By link = getLinkByPageName(PageName, withOutFilterId);
		waitForElementToBeClickable(link, waitSecs);
		hoverOnElement(link);
	}
	
	public void hoverOverLinkByPageNameClickandHold(String PageName, int waitSecs, boolean withOutFilterId)
	{
		By link = getLinkByPageName(PageName, withOutFilterId);
		waitForElementToBeClickable(link, waitSecs);
		hoverOnElementClickandHold(link);
	}
	
	public By getLinkByPageName(String pageName) {
		return getLinkByPageName(pageName, false);
	}
	
	public By getLinkByPageName(String pageName, boolean withOutFilterId) {
		return By.cssSelector("a[href*='" + pageName + "?" + (withOutFilterId ? "id=" : "") + "']");
	}
	
	// navigate to a page relative to the domain - first split the current url and then append the new url
	public void navigateToVFPage(String PageName, String params){		
		goToUrl(getFullApexUrl(PageName) + "?" + params);
	}
	
	public void navigateToVFPage(String PageName){		
		goToUrl(getFullApexUrl(PageName));
	}
	
	public void navigateToSFPage(String url, String params){		
		goToUrl(getFullUrl(url) + "?" + params);
	}
	
	public void navigateToSFPage(String url){		
		goToUrl(getFullUrl(url));
	}
	
	private String getFullApexUrl(String PageName)
	{
		return getRootUrl() + PagesC.GetRelativeUrl(KimbleOneify(PageName));		
	}
	
	private String getFullUrl(String url)
	{
		return getRootUrl() + url;		
	}
	
	private String getRootUrl()
	{
		String[] urlElements = getCurrentUrl().split("/");
		return urlElements[0] + "//" + urlElements[2] + "/";
	}
	

	public String KimbleOneify(String originalValue)
	{
		return isPackagedOrg() ? KIMBLEONE_NAMESPACE + originalValue : originalValue;
	}
	
	public void appendPageParam(String pageParam) {
		goToUrl(getCurrentUrl() + pageParam);
	}
	
	public void goToAllTabsPage() {
		executeSequenceWithRefreshRetry(this, 3, () -> {
			closeLightningPopUp();
			waitForElementToBeClickable(By.cssSelector(A_HREF_HOME_SHOW_ALL_TABS_JSP));
			getWebElement(By.cssSelector(A_HREF_HOME_SHOW_ALL_TABS_JSP)).click();
			// wait until the screen has loaded - signified by one of our tab links being clickable
			waitForElementToBeClickableWithRetry(By.linkText("Activity Roles"), 10);
		});
	}
	
	public void goToObjectLinksPage(){
		navigateToVFPage(PagesC.OBJECTLINKS);	
	}	
	
	public void sleep(long sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	 
	}
	
	public void sleep(long sleepTime, String reason) {
		LogMessageLine("!TODO!: sleeping for " + sleepTime + "ms. Reason: " + reason);
		this.sleep(sleepTime);
	}
	
	// click the last link found by link text (avoids problem with multiple links on the same page)
	public void clickLastLink(String linkText){
		getLastLink(linkText).click();
	}
	
	public WebElement getLastLink(String linkText) {
		List<WebElement> matchingLinks = getWebElements(By.linkText(linkText));
		
		if(matchingLinks.size()==0)
		{
			throw new TestException("Link [" + linkText + "] not found on page");
		}
		
		return matchingLinks.get(matchingLinks.size() - 1);
	}

	public void clickLinkByTitle(String linkTitle){
		clickLink(By.cssSelector("a[title='" + linkTitle + "']"));
	}
	
	public void clickLink(By locator, int waitSecs)
	{
		executeSequenceWithRetry(this, 3, () -> {
			waitClickable(this, locator, waitSecs);
			click(this, locator);
		});
	}
	
	public void clickLink(By locator)
	{
		switchToClassicIframeContext();
		clickLink(locator, 20);
	}
	
	public void clickAndWait(By clickElement, By waitForElement, int seconds) {
		clickLink(clickElement);
		if(isLightning()){
			waitForLightningSpinnerToBeHidden();
			waitForElementToBeClickable(waitForElement, seconds);
		}
		else
			waitForElementToBeClickable(waitForElement, seconds);
	}
		
	public void clickAndWaitSequence(int seconds, By... selectors) {
		if(selectors.length < 2)
			throw new RuntimeException("You must pass at least two selectors");
		for(int i = 0; i < selectors.length - 1; i++) {
			By clickElement = selectors[i];
			By waitForElement = selectors[i+1];
			clickAndWait(clickElement, waitForElement, seconds);
		}
	}
	
	public void clickAndWaitSequenceWithRefreshRetry(int seconds, By... selectors) {
		try {
			clickAndWaitSequence(seconds, selectors);
		} catch(Exception e) {
			try {
				refreshBrowser();
				waitForPageLoadComplete(20);
				clickAndWaitSequence(seconds, selectors);
			} catch(Exception ee) {
				refreshBrowser();
				waitForPageLoadComplete(20);
				clickAndWaitSequence(seconds, selectors);
			}
		}
	}
	
	public void clickAndWaitSequenceWithRetry(int seconds, By... selectors) {
		try {
			clickAndWaitSequence(seconds, selectors);
		} catch(Exception e) {
			try {
				clickAndWaitSequence(seconds, selectors);
			} catch(Exception ee) {
				clickAndWaitSequence(seconds, selectors);
			}
		}
	}
	
	public void clickAndRepeat(int seconds, By selector, int repeats) {
		for(int i = 0; i < repeats; i++)
			clickAndWait(selector, selector, seconds);
	}
	
	public void clickLinkIfVisible(By locator)
	{
		waitForElementToBeClickable(locator, 5);
		if(checkElementVisible(locator))
			clickLink(locator);
	}
	
	public void sendKeysIfVisibleAndEnabled(By locator, CharSequence... paramVarArgs)
	{
		if(!checkElementVisible(locator) || !checkElementEnabled(locator))
			return;
		getWebElement(locator).sendKeys(paramVarArgs);
		getWebElement(locator).sendKeys(Keys.ENTER);
	}
	
	public void clearAndSendKeysIfVisibleAndEnabled(By locator, CharSequence... paramVarArgs)
	{
		if(!checkElementVisible(locator) || !checkElementEnabled(locator))
			return;
		WebElement el = getWebElement(locator);
		el.clear();
		el.sendKeys(paramVarArgs);
		theWD.switchTo().window(theWD.getWindowHandle());
	}
	
	public void clearField(By locator){
		WebElement el = getWebElement(locator);
		el.clear();
	}
	
	public void setSelectedIfVisibleAndEnabled(By locator, boolean isSelected)
	{
		if(!checkElementVisible(locator) || !checkElementEnabled(locator))
			return;
		WebElement el = getWebElement(locator);
		if(el.isSelected() != isSelected)
			el.click();
	}
	
	public void clickLinkByPartialLinkText(String linkText){
		clickLink(By.partialLinkText(linkText));
	}

	public boolean checkUrlContainsText(String urlText, boolean ignorecase){
		String currentUrl = getWD().getCurrentUrl();
		
		if(ignorecase) {
			currentUrl = currentUrl.toLowerCase();
			urlText = urlText.toLowerCase();
		}
		
		return currentUrl.contains(urlText);
	}
	
	public boolean checkUrlDoesNotContainText(String urlText, boolean ignorecase){
		return !checkUrlContainsText(urlText, ignorecase);
	}
	
	public void closeBrowser(){
		// using close does not clear up resources so quit instead
		getWD().quit();
	}
	
	public boolean checkElementVisibleAndEnabled(By criteria) { 
		return checkElementVisible(criteria) &&
				checkElementEnabled(criteria);
	}
	
	public boolean checkElementVisible(By criteria) { 
		try { 
			getWD().findElement(criteria); 
			return true; 
			} 
		catch (NoSuchElementException e) { 
			return false; 
		} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		} 
	}
	
	public boolean checkElementEnabled(By criteria) {
		try { 
			return getWebElement(criteria).isEnabled(); 
		} 
		catch (NoSuchElementException e) { 
			return false; 
		} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		} 
	}
	
	public void waitForElementToBeClickableWithRefreshRetry(By criteria, int waitSecs) {
		try {
			waitForElementToBeClickable(criteria, waitSecs);
		} catch(Exception e) {
			try {
				refreshBrowser();
				waitForElementToBeClickable(criteria, waitSecs);
			} catch(Exception ee) {
				refreshBrowser();
				waitForElementToBeClickable(criteria, waitSecs);
			}
		}
	}
	
	public void waitForElementToBeClickableWithRetry(By criteria, int maxRetries) {
		int currentAttempt = 1;
		boolean itemFound = false;
		
		while (currentAttempt <= maxRetries) {

			try	{
				if(isLightning()){
					try{
						waitForElementToBeClickable(criteria);
					} 
					catch(Exception e){
						waitForLightningSpinnerToBeHidden();
					}
				}
				waitForElementToBeClickable(criteria);
				itemFound = true;
				break;
			}
			catch(NoSuchWindowException|UnreachableBrowserException ubEx)
			{
				// if the browser has died (can happen with glitches in chromedriver)
				// then we should exit the test as retry will never succeed
				throw ubEx;
			}
			catch(Exception ex){
				// if not found the webdriver methods will have thrown an Exception
				// we don't need to know this unless it is an exception indicating we've lost contact with the browser
				processWaitException(ex);
			}
			
			// if there are still iterations of the search then pause until the next search
			// each subsequent search will wait 1 second longer than the last
			if(!itemFound){
				if(currentAttempt < maxRetries) {
					// check for any on-screen errors and fail the test if any are found
					String pageError = handleOnScreenMessage();
					if(pageError != "")
						throw new TestException("<< Abandoning test following on-screen error message >> [" + pageError + "]"); 
					
					if(currentAttempt == 1) {
						LogMessageLine("waiting for [" + criteria + "] to be clickable, retrying.");
					} else {
						LogProgressDot();
					}
				} else {
					if(recordUnavailable())
					{
						LogErrorMessageLine("Record Currently Unavailable - ERROR");
					}
					else
					{
						LogErrorMessageLine("[" + criteria + "] not clickable - ERROR.");
					}
				}
			}
			
			currentAttempt++;
		}
		
		assert(itemFound);
	}
	
	public boolean recordUnavailable()
	{
		// check if the page contains the text Record Currently Unavailable as if so the previous
		// functional step hit a record locking issue; track this specifically in the test console output
		return verifyPageContainsText("Record Currently Unavailable");
	}
	
	public void processWaitException(Exception ex) {
		// exceptions that happen during waits can for the most part be ignored as they
		// are indicating that an element isn't present which is expected until the element does appear
		// there are situations however where we want to test for specfic exception text and end the test
		// such as the "chrome not reachable" exception seen if the browser has died
		// (un)helpfully these are thrown by the Selenium webdriver as top level UnreachableBrowserException types meaning
		// that they cannot be differentiated from other valid subtyped UnreachableBrowserExceptions such as staleelement or elementnotvisible
		// the only option we have is to parse the message text for known patterns
		String exceptionMessage = ex.getMessage();
		
		// this is a valid exception meaning we've lost contact with the browser and should end this test
		if(exceptionMessage.contains("reachable"))
		{
			throw new UnreachableBrowserException("Lost contact with the browser", ex);
		}
	}

	public void waitForLabelToBe(WebElement targetElement, String expectedValue, int maxRetries) {
		int currentAttempt = 1;
		int sleepSeconds = 0;
		boolean itemFound = false;
		
		while (currentAttempt <= maxRetries) {
			sleepSeconds=sleepSeconds+1000;

			try	{
				if (targetElement.getText().equals(expectedValue)) {
					itemFound = true;
					break;
				} else {
					// if there are still iterations of the search then pause until the next search
					// each subsequent search will wait 1 second longer than the last
					if(currentAttempt < maxRetries) {
						String pageError = handleOnScreenMessage();
						if(pageError != "")
							throw new TestException("<< Abandoning test following on-screen error message >> [" + pageError + "]"); 

						if(currentAttempt == 1) {
							LogMessageLine("Waiting for [" + targetElement.getText() + "] to be [" + expectedValue + "].");
						} else {
							LogProgressDot();
						}
						sleep(sleepSeconds);
					} else {
						LogErrorMessageLine("[" + targetElement.getText() + "] is not [" + expectedValue + "] - ERROR");
					}
				}
			}
			catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
				// if the browser has died (can happen with glitches in chromedriver)
				// then we should exit the test as retry will never succeed
				throw ubEx;
			} catch(Exception ex){
				// if not found the webdriver methods will have thrown an Exception
				// we don't need to know this unless it is an exception indicating we've lost contact with the browser
				processWaitException(ex);
			}
			
			currentAttempt++;
		}
		
		assert(itemFound);
	}
		
	public void waitForPageToContainTextWithRetry(String criteria, int maxRetries) {
		int currentAttempt = 1;
		int sleepSeconds = 0;
		boolean itemFound = false;
		
		while (currentAttempt <= maxRetries) {
			sleepSeconds=sleepSeconds+1000;
			
			try	{
				itemFound = verifyPageContainsText(criteria);
			}
			catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
				// if the browser has died (can happen with glitches in chromedriver)
				// then we should exit the test as retry will never succeed
				throw ubEx;
			} catch(Exception ex){
				// if not found the webdriver methods will have thrown an Exception
				// we don't need to know this unless it is an exception indicating we've lost contact with the browser
				processWaitException(ex);
			}
			
			// if there are still iterations of the search then pause until the next search
			// each subsequent search will wait 1 second longer than the last
			if(!itemFound){
				if(currentAttempt < maxRetries) {
					String pageError = handleOnScreenMessage();
					if(pageError != "")
						throw new TestException("<< Abandoning test following on-screen error message >> [" + pageError + "]"); 

					if(currentAttempt == 1) {
						LogMessageLine("Waiting for page to contain [" + criteria + "].");
					} else {
						LogProgressDot();
					}
					sleep(sleepSeconds);
				} else {
					LogErrorMessageLine("Page does not contain [" + criteria + "] - ERROR.");
				}
			} else {
				// the item was found so we can jump out of the loop
				//LogMessageLine(".  Found");
				break;
			}
			
			currentAttempt++;
		}
		
		assert(itemFound);
	}
	
	public void waitForElementToBeVisible(WebElement targetElement, int timeout){
		WebDriverWait wait = new WebDriverWait(getWD(), timeout);
		wait.until(ExpectedConditions.visibilityOf(targetElement));
	}
	
	public void waitForElementToBeVisible(WebElement targetElement){
		waitForElementToBeVisible(targetElement,10);
	}

	public void waitForElementToBeVisible(By criteria){
		waitForElementToBeClickable(criteria, 20);
	}
	
	public void waitForElementToBeVisible(By criteria, int timeoutSeconds){
		WebDriverWait wait = new WebDriverWait(getWD(), timeoutSeconds);
		wait.until(ExpectedConditions.visibilityOfElementLocated(criteria));
	}
	
	public void waitForElementToBeClickable(By criteria){
		waitForElementToBeClickable(criteria, 30);
	}

	public void waitForElementToBePresent(By criteria, int timeoutSeconds){
		WebDriverWait wait = new WebDriverWait(getWD(), timeoutSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(criteria));
	}
	
	public void waitForElementToBePresent(By criteria){
		waitForElementToBePresent(criteria, 20);
	}
	
	public void waitForElementToBeClickable(By criteria, int timeoutSeconds){
		WebDriverWait wait = new WebDriverWait(getWD(), timeoutSeconds);
		wait.until(ExpectedConditions.elementToBeClickable(criteria));
	}
	
	public void waitForElementToBeEditable(By criteria){
		waitForElementToBeEditable(criteria, 20);
	}
	
	public void waitForElementToBeEditable(final By criteria, int timeoutSeconds){
		WebDriverWait wait = new WebDriverWait(getWD(), timeoutSeconds);
		wait.until(new ExpectedCondition<Boolean>() { 
			public Boolean apply(WebDriver d) {
				// isEnabled and clickable methods will always return true if the element is focusable
				// but it still may not be editable (such as the case of the monthly usage numbers view)
				// determine if the element has the attribute "readonly"
				// Note this will likely only work for web elements which support the readonly HTML attribute
				return (d.findElement(criteria).getAttribute("readonly") == null);
				} 
			});
	}
	
	public void waitForLightningSpinnerToBeHidden() {
		if(isLightning()) {
			waitMilliseconds(3000); // short wait needed to ensure spinner loads
			waitForElementToBeHidden(By.className("slds-spinner_container"), 60);
			switchToClassicIframeContext();
		}
	}
	
	public void waitForBoxyToBeHidden() {
		waitForElementToBeHidden(By.cssSelector("div.boxy-modal-blackout"), 60);
	}
	
	public void waitForElementToBeHidden(By criteria){
		waitForElementToBeHidden(criteria, 30);
	}
	
	public void waitForElementToBeHidden(By criteria, int timeoutSeconds){
		WebDriverWait wait = new WebDriverWait(getWD(), timeoutSeconds);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(criteria));
	}
		
	public void waitForElementToBeHiddenCss(String cssSelector, int timeoutSeconds){
		WebDriverWait wait = new WebDriverWait(getWD(), timeoutSeconds);
		wait.until(ExpectedConditions.or(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(cssSelector)), 
				ExpectedConditions.jsReturnsValue("var el = $(\"" + cssSelector + "\").get(0); if(el.offsetWidth === 0 || el.offsetHeight === 0) return true;")));
	}
	
	/**
	 * Using this method will break compatibility with Internet Explorer if we decide to go there
	 * 
	 * @param xpathSelector
	 * @param timeoutSeconds
	 */
	public void waitForElementToBeHiddenXpath(String xpathSelector, int timeoutSeconds){
		WebDriverWait wait = new WebDriverWait(getWD(), timeoutSeconds);
		wait.until(ExpectedConditions.or(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(xpathSelector)), 
				ExpectedConditions.jsReturnsValue("var el = document.evaluate(\"" + xpathSelector + "\", document, null, XPathResult.ANY_TYPE, null).iterateNext(); if(el.offsetWidth === 0 || el.offsetHeight === 0) return true;")));
	}
		
	// date fields are bound with a picker which popups up when trying to type
	// first we clear the field and then enter the value, then explicitly tab away to dismiss the picker
	public void setDatePickerField(By targetLocator, String dateValue){
		WebElement targetElement = getVisibleWebElement(targetLocator);
		targetElement.clear();
		targetElement.sendKeys(dateValue);
		targetElement.sendKeys(Keys.TAB);
		
		// lots of problems with the date popup not disappearing in time and covering up the save button
		// bolt and braces approach is to force the popup to close (we don't use it anyway)
		sleep(100);
		forceCalendarToClose();
	}
	
	public void setSfDatePickerField(WebElement targetElement, String dateValue){
		targetElement.clear();
		targetElement.sendKeys(dateValue);
		targetElement.sendKeys(Keys.TAB);
	}

	public void forceCalendarToClose() {
		// if any calendar popups are open, force them to close
		if(getWebElements(By.cssSelector(POPUP_CALENDAR)).size() > 0)
		{
			executeJavaScript(CLOSE_CALENDAR_JS);
		}
	}

	public void hideAllElementsByClassName(String className) {
		executeJavaScript("$('." + className + "').hide()");
	}

	public void showAllElementsByClassName(String className) {
		executeJavaScript("$('." + className + "').show()");
	}
	
	public void showAllElementsByCssSelector(String selector) {
		executeJavaScript("$(\"" + selector + "\").show()");
	}
	
	// different to the above show method as this is where the div has display: none rather than just hidden
	public void displayElementsByClassName(String className) {
		executeJavaScript("$('." + className + "').css('display', 'block')");
	}
	
	public void toggleElementClassName(String className) {
		executeJavaScript("$('." + className + "').toggle()");
	}
	
	// working around a Chrome bug that seems to mean occasionally the TD containing
	// this link receives the focus instead.
	// based on comment #27 here http://code.google.com/p/selenium/issues/detail?id=2766
	//public void scrollElementIntoView(WebElement targetElement) {
	//	executeJavaScript("window.scrollTo(0,"+targetElement.getLocation().y+")");
	//}
	
	public Object executeJavaScript(String scriptToExecute) {
		JavascriptExecutor js = (JavascriptExecutor) getWD();
		return js.executeScript(scriptToExecute);
	}
	
	public boolean checkLinkExistsWithRetry(By locator) {
		boolean exists = checkLinkExists(locator);
		if(!exists)
			exists = checkLinkExists(locator);
		if(!exists)
			exists = checkLinkExists(locator);
		return exists;
	}
	
	public boolean checkLinkExists(By locator){
		boolean elementExists = true;
		
		try {
			WebElement targetElement = getWD().findElement(locator);
			assert targetElement.isDisplayed();
			
		} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		} catch (Exception e) {
			// an exception means the check failed: return false
			elementExists = false;
		}
		return elementExists;
	}
		
	// where text is displayed in error messages or dynamically added we
	// won't have a specific ID or css to find it, instead use this last resort
	// to scan the complete page source for the searchTerm
	public boolean verifyPageContainsText(String searchTerm)
	{
		return getWD().getPageSource().contains(searchTerm);
	}

	public void waitForPageLoadComplete(int timeoutSeconds) {
		
	     ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
	        public Boolean apply(WebDriver driver) {
	          return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
	        }
	      };

	     Wait<WebDriver> wait = new WebDriverWait(getWD(),timeoutSeconds);
	      try {
	              wait.until(expectation);
	      } catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
				// if the browser has died (can happen with glitches in chromedriver)
				// then we should exit the test as retry will never succeed
				throw ubEx;
			} catch(Throwable error) {
	              throw new TimeoutException("Timeout waiting for Page Load to complete.");
	      }
	 } 
	
	public List<WebElement> getWebElements(By criteria){
		try {
			waitForElementToBeClickable(criteria, 5);
			return (List) getWD().findElements(criteria);
		} catch(Exception e) { return (List) Collections.emptyList(); }
	}

	public WebElement getLastWebElement(By critera){
		// for scenarios where there are multiple matches on screen, return the last instance
		// for example on the expense entry screen where each new row results in a matching duplicate
		// set of webelements
		List<WebElement> matchingElements = getWebElements(critera);
		return matchingElements.get(matchingElements.size() - 1);
	}
	
	public String getWebElementTextOrNullWithRefreshRetry(By criteria){
		String text = getWebElementTextOrNull(criteria);
		if(text == null) {
			refreshBrowser();
			waitForPageLoadComplete(20);
			text = getWebElementTextOrNull(criteria);
		}
		if(text == null) {
			refreshBrowser();
			waitForPageLoadComplete(20);
			text = getWebElementTextOrNull(criteria);
		}
		return text;
	}
	
	public String getWebElementTextOrNullWithRetry(By criteria){
		String text = getWebElementTextOrNull(criteria);
		if(text == null)
			text = getWebElementTextOrNull(criteria);
		if(text == null)
			text = getWebElementTextOrNull(criteria);
		return text;
	}
	
	public String getWebElementTextOrNull(By criteria){
		String result = null;
		try { // try three times in case of refresh flicker
			result = getTextOrNull(getElement(criteria));
		} catch(Exception e) {
			try {
				result = getTextOrNull(getElement(criteria));
			} catch(Exception ee) {
				try {
					result = getTextOrNull(getElement(criteria));
				} catch(Exception eee) {}
			}
		}
		if(result != null)
			result = result.trim().replaceAll("\\s", " ");
		return result;
	}
	
	private WebElement getElement(By criteria) {
		//waitForElementToBeClickable(criteria, 1);
		return getWebElement(criteria);
	}
	
	private String getTextOrNull(WebElement el) {
		if(el == null)
			return null;
		new Actions(theWD).moveToElement(el).perform();
		if(el.getTagName().equals("input"))
			return el.getAttribute("value");
		if(el.getTagName().equals("select"))
			return new Select(el).getFirstSelectedOption().getText();
		return el.getText();
	}
	
	public Double getWebElementNumberOrNullWithRefreshRetry(By criteria){
		Double number = getWebElementNumberOrNull(criteria);
		if(number == null) {
			refreshBrowser();
			waitForPageLoadComplete(20);
			number = getWebElementNumberOrNull(criteria);
		}
		if(number == null) {
			refreshBrowser();
			waitForPageLoadComplete(20);
			number = getWebElementNumberOrNull(criteria);
		}
		return number;
	}
	
	public Double getWebElementNumberOrNullWithRetry(By criteria){
		Double number = getWebElementNumberOrNull(criteria);
		if(number == null)
			number = getWebElementNumberOrNull(criteria);
		if(number == null)
			number = getWebElementNumberOrNull(criteria);
		return number;
	}
	
	public Double getWebElementNumberOrNull(By criteria) {
		String txt = getWebElementTextOrNull(criteria);
		if(txt == null)
			return null;
		try {
			return Double.parseDouble(txt.replaceAll("[^0-9.]", ""));
		} catch(Exception e) {
			return null;
		}
	}

	public WebElement getWebElement(By critera){
		return getWD().findElement(critera);
	}

	public void setWD(WebDriver webDriverInstance) {
		this.theWD = webDriverInstance;
	}

	public WebDriver getWD() {
		return theWD;
	}
	
	public WebElement getLevelThreeCheckbox(String levelOneLabel, String levelTwoLabel, String levelThreeLabel) {
		String xPathToTarget = THREE_LEVEL_CHECKBOX_XPATH_TEMPLATE;
		
		xPathToTarget = xPathToTarget.replace("LEVEL_ONE_LABEL", levelOneLabel);
		xPathToTarget = xPathToTarget.replace("LEVEL_TWO_LABEL", levelTwoLabel);
		xPathToTarget = xPathToTarget.replace("LEVEL_THREE_LABEL", levelThreeLabel);
		
		WebElement targetCheckbox = theWD.findElement(By.xpath(xPathToTarget));
		
		return targetCheckbox;
	}
	
	public WebElement getTimeEntryLevelThreeCheckbox(String levelOneLabel, String levelTwoLabel, String levelThreeLabel) {
		String xPathToTarget = TIME_ENTRY_THREE_LEVEL_CHECKBOX_XPATH_TEMPLATE;
		
		// Level One not used as instead we assume ID of "TI" instead of using Title
		//xPathToTarget = xPathToTarget.replace("LEVEL_ONE_LABEL", levelOneLabel);
		xPathToTarget = xPathToTarget.replace("LEVEL_TWO_LABEL", levelTwoLabel);
		xPathToTarget = xPathToTarget.replace("LEVEL_THREE_LABEL", levelThreeLabel);
		
		WebElement targetCheckbox = theWD.findElement(By.xpath(xPathToTarget));
		
		return targetCheckbox;
	}
	
	public void searchAndSelectViaLookup(String associatedInputId, String searchDomain, String searchTerm){
		theWD.findElement(By.id(associatedInputId + "_lkwgt")).click();
		// short sleep to allow the popup to render
		sleep(1000);
		// switch focus to the search screen
		String parentWindowHandle = switchToPopup();
		// then switch to the search frame in the search window
		switchToFrameById("searchFrame");
		
		// now enter the search criteria
		theWD.findElement(By.id(FCF)).sendKeys(searchDomain);
		theWD.findElement(By.id(LKSRCH)).sendKeys(searchTerm);
		theWD.findElement(By.name(GO)).click();
		
		// short sleep for the search
		sleep(500);
		
		switchToFrameById("resultsFrame");
		clickLink(By.linkText(searchTerm));			
		
		// switch the driver back to the parent window
		switchToWindowHandle(parentWindowHandle);
	}
	
	public void searchAndSelectViaLookup(WebElement associatedInputField, String searchTerm){
		if(searchTerm != null && !searchTerm.equals("")){

			theWD.findElement(By.id(associatedInputField.getAttribute("id") + "_lkwgt")).click();
			// short sleep to allow the popup to render
			sleep(1000);
			// switch focus to the search screen
			String parentWindowHandle = switchToPopup();

			try {
				// first try to see if the result is already in the list of
				// previous results (if this is a search following other similar searches
				// it is likely to already be there
				switchToFrameById("resultsFrame");
				clickLink(By.linkText(searchTerm));
			} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
				// if the browser has died (can happen with glitches in chromedriver)
				// then we should exit the test as retry will never succeed
				throw ubEx;
			} catch (Exception e) {
				// no matter, try and normal search instead
				// then switch to the search frame in the search window
				switchToFrameById("searchFrame");
				
				// now enter the search criteria
				theWD.findElement(By.id(LKSRCH)).sendKeys(searchTerm);
				theWD.findElement(By.name(GO)).click();
				
				// short sleep for the search
				sleep(500);
				
				switchToFrameById("resultsFrame");
				clickLink(By.linkText(searchTerm));	
			}
				
			// switch the driver back to the parent window
			switchToWindowHandle(parentWindowHandle);			
		}
	}
	
	// switch to the popup and return the parent so the consumer can switch back if required
	private String switchToPopup(){
		String currentHandle = theWD.getWindowHandle();

		Set<?> windowHandles = theWD.getWindowHandles();
		Iterator<?> windowIterator = windowHandles.iterator();
		
		while(windowIterator.hasNext()){
			String popupHandle = windowIterator.next().toString();
			if(!popupHandle.contains(currentHandle)){
				switchToWindowHandle(popupHandle);
			}
		}
	
		return currentHandle;
	}
	
	private void switchToWindowHandle(String targetWindowHandle){
		theWD.switchTo().window(targetWindowHandle);
	}

	public void switchToFrameByTitle(String frameTitle){
		theWD.switchTo().defaultContent();
		WebElement targetFrame = theWD.findElement(By.cssSelector("[title='" + frameTitle + "']"));
		theWD.switchTo().frame(targetFrame);		
	}
	
	public void switchToWindow(String windowName){
		theWD.switchTo().window(windowName);		
	}

	public void switchToFrameById(String frameId){
		// you can only switch between frames that are siblings by first going to the parent
		// described in http://darrellgrainger.blogspot.co.uk/2012/04/frames-and-webdriver.html
		// so first go to defaultContent() which is the root of the screen
		theWD.switchTo().defaultContent();
		WebElement targetFrame = theWD.findElement(By.id(frameId));
		theWD.switchTo().frame(targetFrame);		
	}
	
	public String getValueBasedOnLabel(String labelText) {
		return getElementBasedOnLabel(labelText).getAttribute("value");
	}

	public void waitForElementToBeClickableBasedOnLabel(String labelText) {
		waitForElementToBeClickable(By.xpath("//label[contains(text(),'" + labelText + "')]"));
	}
	
	public WebElement getElementBasedOnLabel(String labelText) {
		return getWebElement(getSelectorBasedOnLabel(KBy.label(labelText)));
	}
	
	public By getSelectorBasedOnLabel(By label) {
		WebElement targetLabel = getWebElement(label);
		return By.id(targetLabel.getAttribute("for"));
	}
	
	public List<WebElement> getElementsBasedOnLabel(String labelText) {
		ArrayList<WebElement> targetElements = new ArrayList();
		List<WebElement> targetLabels = getWebElements(By.xpath("//label[contains(text(),'" + labelText + "')]"));
		
		for(WebElement labelElement : targetLabels)
		{
			targetElements.add(getWebElement(By.id(labelElement.getAttribute("for"))));
		}
		
		return targetElements;
	}
	
	public WebElement getElementBasedOnLabel(WebElement associatedLabelWebElement) {
		return getWebElement(By.id(associatedLabelWebElement.getAttribute("for")));
	}
	
	public WebElement getLinkByLinkTextAndOnClick(String linkText, String linkOnClick) {
		String linkTargetXPath = LINKBYLINKANDONCLICKXPATH.replace(LINKTEXTTOKEN, linkText).replace(ONCLICKTOKEN,linkOnClick);
		return theWD.findElement(By.xpath(linkTargetXPath));	
	}
	
	public WebElement getLinkByOnClick(String linkOnClick) {
		String linkTargetXPath = LINKBYONCLICKXPATH.replace(ONCLICKTOKEN,linkOnClick);
		return theWD.findElement(By.xpath(linkTargetXPath));	
	}
	
	public void selectByVisibleText(By criteria, String textValue)
	{
		waitForElementToBeClickable(criteria, 20);
		WebElement theTargetElement = getWebElement(criteria);
		new Select(theTargetElement).selectByVisibleText(textValue);
	}
	
	public void selectByVisibleTextWithRetry(By criteria, String textValue)
	{
		try {
			selectByVisibleText(criteria, textValue);
		} catch(Exception e) {
			try {
				selectByVisibleText(criteria, textValue);
			} catch(Exception ee) {
				selectByVisibleText(criteria, textValue);
			}
		}
	}
	
	public void sendKeysWithRetry(By criteria, String textValue) {
		try {
			waitForElementToBeVisible(criteria);
			getWebElement(criteria).sendKeys(textValue);
		} catch(Exception e) {
			try {
				waitForElementToBeVisible(criteria);
				getWebElement(criteria).sendKeys(textValue);
			} catch(Exception ee) {
				waitForElementToBeVisible(criteria);
				getWebElement(criteria).sendKeys(textValue);
			}
		}
	}
	
	public void selectByVisibleText(WebElement theTargetElement, String textValue)
	{
		new Select(theTargetElement).selectByVisibleText(textValue);
	}
	
	public void selectByPosition(WebElement theTargetElement, Integer targetPosition)
	{
		new Select(theTargetElement).selectByIndex(targetPosition);
	}
	
	public String dateAdd(String originalCloseDate, int field, int amount) throws ParseException {	
		Date newDate;
		String newDateAsString;
		// format strings here: http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
		Date inputDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(originalCloseDate);	
		Calendar c = Calendar.getInstance(); 
		c.setTime(inputDate); 
		c.add(field, amount);
		
		newDate = c.getTime();
		// Screen label is the form "Tue 1"
		// SimpleDateFormat reference here:
		// http://oak.cs.ucla.edu/cs144/projects/java/simpledateformat.html
		String newDateDay = new SimpleDateFormat("dd").format(newDate);
		String newDateMonth = new SimpleDateFormat("MM").format(newDate);
		String newDateYear = new SimpleDateFormat("yyyy").format(newDate);

		newDateAsString = newDateDay + "/" + newDateMonth + "/" + newDateYear;
		
		return newDateAsString;
	}
	
	public List<String> getUrlsFromWebElements(List<WebElement> linkElements) {
		List<String> linkUrls = new ArrayList();
		
		for (WebElement webElement : linkElements) {
			String newUrl = webElement.getAttribute("href");
			linkUrls.add(newUrl);
		}
		
		return linkUrls;
	}
	
	public List<ListGridItemC> getEditDelAndOpenLinks(){
		return getEditDelAndOpenLinks(DEFAULT_EDIT_LINK, DEFAULT_DELETE_LINK, DEFAULT_OPEN_LINK, DEFAULT_OPEN_LINK);
	}

	public List<ListGridItemC> getEditDelAndOpenLinks(By editLinkXPath, By deleteLinkXPath, By openLinkXPath, By infoXPath){
		
		List<WebElement> allEditElements = getWebElements(editLinkXPath);
		List<WebElement> allDeleteElements = getWebElements(deleteLinkXPath);
		List<WebElement> allOpenElements = getWebElements(openLinkXPath);
		List<WebElement> allInfoElements = getWebElements(infoXPath);
		
		List<ListGridItemC> labelLinks = constructListNavigator(allEditElements, allDeleteElements, allOpenElements, allInfoElements);		
		
		return labelLinks;
	}
	
	public List<ListGridItemC> getDelLinks(String deleteLinkXPath){
		
		List<WebElement> allDeleteElements = getWebElements(By.xpath(deleteLinkXPath));
		
		List<ListGridItemC> labelLinks = constructListNavigatorDeleteOnly(allDeleteElements);		
		
		return labelLinks;
	}

	private List<ListGridItemC> constructListNavigator(List<WebElement> editLinkElements, List<WebElement> deleteLinkElements, List<WebElement> openLinkElements, List<WebElement> infoElements) {
		List<String> editLinks = getUrlsFromWebElements(editLinkElements);
		List<String> deleteLinks = getUrlsFromWebElements(deleteLinkElements);
		List<String> openLinks = getUrlsFromWebElements(openLinkElements);
		// use the openLink to derive the name
		List<String> names = getNamesFromWebElements(openLinkElements);
		List<String> infoDetails = getValueFromWebElements(infoElements);
		
		List<ListGridItemC> listGridItems = new ArrayList();
		
		// assume each of the lists is the same length
		for(int i=0; i<editLinks.size(); i++) {
			ListGridItemC newItem = new ListGridItemC();
			newItem.name = names.get(i);
			newItem.info = infoDetails.get(i);
			
			newItem.editUrl = editLinks.get(i);
			newItem.deleteUrl = deleteLinks.get(i);
			newItem.openUrl = openLinks.get(i);

			newItem.editLink = editLinkElements.get(i);
			newItem.deleteLink = deleteLinkElements.get(i);
			newItem.openLink = openLinkElements.get(i);
			
			listGridItems.add(newItem);
		}
		
		return listGridItems;
	}

	private List<ListGridItemC> constructListNavigatorDeleteOnly(List<WebElement> deleteLinkElements) {
		List<String> deleteLinks = getUrlsFromWebElements(deleteLinkElements);		
		List<ListGridItemC> listGridItems = new ArrayList();
		
		for(int i=0; i<deleteLinks.size(); i++) {
			ListGridItemC newItem = new ListGridItemC();
			newItem.deleteUrl = deleteLinks.get(i);
			newItem.deleteLink = deleteLinkElements.get(i);
			
			listGridItems.add(newItem);
		}
		
		return listGridItems;
	}
	
	private List<String> getNamesFromWebElements(List<WebElement> allNames) {
		List<String> linkUrls = new ArrayList();
		
		for (WebElement webElement : allNames) {
			String newUrl = webElement.getAttribute("text");
			linkUrls.add(newUrl);
		}
		
		return linkUrls;
	}
	
	private List<String> getValueFromWebElements(List<WebElement> allNames) {
		List<String> linkUrls = new ArrayList();
		
		for (WebElement webElement : allNames) {
			String newUrl = webElement.getText();
			linkUrls.add(newUrl);
		}
		
		return linkUrls;
	}

	public void fixRowsPerPage() {
		goToUrl(getCurrentUrl() + ROWSPERPAGE2 + "2000" );
	}
	
	public void fixRowsPerPage(int rowsPerPage) {
		goToUrl(getCurrentUrl() + ROWSPERPAGE2 + rowsPerPage );
	}
	
	public String getIDFromCurrentURL() {
		String url = getCurrentUrl();
        URL aURL = null;
		
        try {
			aURL = new URL(url);
		} catch(NoSuchWindowException|UnreachableBrowserException ubEx) {
			// if the browser has died (can happen with glitches in chromedriver)
			// then we should exit the test as retry will never succeed
			throw ubEx;
		} catch (MalformedURLException e) { e.printStackTrace(); }
		
        url = aURL.getQuery();
        
        if(isLightning()){
    		String urlLightning = getCurrentUrl();
        	urlLightning.replace("/view?nooverride=true", "");
        	String[] urlLightning2 = urlLightning.split("__c/");
        	String[] urlLightning3 = urlLightning2[1].split("/view");
            return urlLightning3[0];  
        }
        else{
        	String[] id = url.split("=");
            return id[1];  
        }
	}
	
	public String getHostFromCurrentUrl() {
		return getCurrentUrl().replace("https://", "").replace("http://", "").split("/")[0];
	}
	
	public String getBaseFromCurrentUrl() {
		return "https://" + getHostFromCurrentUrl();
	}
	
	public String getApexBaseFromCurrentUrl() {
		return getBaseFromCurrentUrl() + "/apex";
	}
	
	public void OpenExisting(String linkText) {
		clickLink(By.linkText(linkText));
	}

	public void OpenExisting(String linkText, int waitSecs) {
		clickLink(By.linkText(linkText), waitSecs);
	}

	public String formatLogOutput(String output)
	{
		return "[" + currentTest.getTestNameLogFormat() + "] " + output;
	}
	
	public void LogProgressDot() {
		if(IsDebugRun()) {
			System.out.print(".");
		}
	}
	
	public void LogErrorMessageLine(String output) {
		System.out.println(formatLogOutput(output));
	}
	
	public void LogInfoMessageLine(String output) {
		System.out.println(formatLogOutput(output));
	}
	
	public void LogErrorMessage(String output) {
		System.out.println(formatLogOutput(output));
	}
	
	public void LogMessageLine(String output) {
		if(IsDebugRun()) {		
			System.out.println(formatLogOutput(output));
		}
	}
	
	public void LogMessage(String output) {
		if(IsDebugRun()) {
			System.out.print(formatLogOutput(output));
		}
	}

	// in some instances there are screens which have multiple components with the same id
	// but only one is visible, this method finds and return the visible element based on the By criteria passed in
	public WebElement getVisibleWebElement(By criteria) {
		List<WebElement> matchedWebElements = getWebElements(criteria);
		
		return getVisibleElement(matchedWebElements);
	}

	private WebElement getVisibleElement(List<WebElement> matchedWebElements) {
		for(WebElement matchedWebElement : matchedWebElements)
		{
			if(matchedWebElement.isDisplayed())
			{
				return matchedWebElement;
			}
		}
		
		return null;
	}

	public boolean hasVisibleWebElement(By criteria) {
		try {
			return getWD().findElement(criteria) != null;
		} catch(Exception e) { return false; }
	}
	
	// in some instances there are screens which have multiple components with the same label
	// but only one is visible, this method finds and return the visible element based on the By criteria passed in
	public WebElement getVisibleWebElementBasedOnLabel(String labelName) {
		List<WebElement> matchedWebElements = getElementsBasedOnLabel(labelName);
		
		return getVisibleElement(matchedWebElements);
	}
	
	public String getDateLabelForDate(String timeEntryDate, String targetFormat) throws ParseException {
		return getDateLabelForDate(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(timeEntryDate), targetFormat);
	}
	
	public String getDateLabelForDate(Date inputDate, String targetFormat) throws ParseException {
		String dateLabel = "";
		
		// SimpleDateFormat reference here:
		// http://oak.cs.ucla.edu/cs144/projects/java/simpledateformat.html
		String inputDateDayName = new SimpleDateFormat("EEE").format(inputDate);	//Tue
		String inputDateDayNum = new SimpleDateFormat("d").format(inputDate);		//3
		String inputDateDayFullNum = new SimpleDateFormat("dd").format(inputDate);	//03
		String inputDateMonthName = new SimpleDateFormat("MMMM").format(inputDate);	//December
		String inputDateMonthFullNum = new SimpleDateFormat("MM").format(inputDate);//07
		String inputDateYearNum = new SimpleDateFormat("yyyy").format(inputDate);	//2003

		if(targetFormat.equals(DAYNUMBERFORMAT)) {
			// Screen label is the form "Tue 1"
			dateLabel = inputDateDayName + " " + inputDateDayNum;
		} else if(targetFormat.equals(MONTHYEARFORMAT)) {
			// Screen label is the form "January 2013"
			dateLabel = inputDateMonthName + " " + inputDateYearNum;
		} else if(targetFormat.equals(DAYMONTHYEARFORMAT)) {
			// Screen label is the form "01 January 2013"
			dateLabel = inputDateDayFullNum + " " + inputDateMonthName + " " + inputDateYearNum;
		} else if(targetFormat.equals(DAYNATURALNUMBERFORMAT)) {
			// "1"
			dateLabel = inputDateDayNum;
		} else if(targetFormat.equals(YEARMONTHDAYPARAMFORMAT)) {
			// "2013-01-28"
			dateLabel = inputDateYearNum + "-" + inputDateMonthFullNum + "-" + inputDateDayFullNum;
		}
		
		return dateLabel;
	}
	
	public void ClickOKButtonOnOpenAlert() {
		Alert javascriptAlert = getWD().switchTo().alert();
		javascriptAlert.accept();
	}

	public void refreshBrowser() {
		theWD.navigate().refresh();
	}
	
	public void NavigateToList(String listLinkName) {
		goToAllTabsPage();
		clickLastLink(listLinkName);
		closeLightningPopUp();
		try {
			waitForElementToBeClickable(By.xpath("//input[@class='btn'][@name='new']"), 2);
		} catch(Exception e) {
			waitForElementToBeClickable(By.name(GO));
			clickAndWaitSequenceWithRetry(5, By.name(GO), By.xpath("//input[@class='btn'][@name='new']"));
		}
	}
	
    public void closeLightningPopUp (){
        
          
        try {
            waitForElementToBeVisible(LightningPopUp,4);
            WebElement lightningClose =  getWebElement(LightningPopUp);
            lightningClose.click();
        }
        catch(Exception e){
            
            //Lightning pop up doesn't exist
        }
    }
	
    public void alertClickOk (){
        
        waitForElementToBeVisible(alertOkButton,4);
        WebElement alertOk =  getWebElement(alertOkButton);
        alertOk.click();
    }
	
	public void clickGoInListView() {
		if(!isLightning()){
			executeSequenceWithRefreshRetry(this, 3, () -> {
				click(this, By.name(GO));
				if(!exists(this, listViewAllSelected, 10))
					click(this, listViewAllUnselected);
				waitClickable(this, listViewAllSelected, 20);
			});
		}
	}

	public void forceOpenCollapsedMenus() {
		forceOpenCollapsedMenus(false);
	}
	
	public void forceOpenCollapsedMenus(boolean alwaysForceOpen) {
		waitForLightningSpinnerToBeHidden();
		// because menus are now collapsible this causes problems since the menu item
		// may not be visible - expand all menus
		// only need to do this if any are collapsed which we can tell by the existance
		// of the > icon on one of the elements	or if the caller has explicitly stated the menu should be
		// expanded: this is done when the are menu filters that we'd like to expand (no expand icon is shown in that circumstance)
		
		By toggler = By.cssSelector("span#menu-collapse-toggler");
		waitForElementToBeClickable(toggler, 40);
		By closedToggler = By.cssSelector("span#menu-collapse-toggler.fa-angle-right");
		By openToggler = By.cssSelector("span#menu-collapse-toggler.fa-angle-down");
		if((alwaysForceOpen && hasVisibleWebElement(closedToggler)) || hasVisibleWebElement(By.cssSelector(".sub-menu-indicator.fa-angle-right")))
			clickAndWaitSequenceWithRetry(5, closedToggler, openToggler);
	}

	public WebElement getCurrentWebElement() {
		WebElement currentElement = theWD.switchTo().activeElement();
		return currentElement;
	}

	public void selectBurgerMenuOption(By cssSelector, String menuOptionText, By waitForElement) {
		clickAndWaitSequenceWithRefreshRetry(10, cssSelector, By.linkText(menuOptionText), waitForElement);
	}

	public void hoverOnElement(By elementLocator) {
		WebDriver driver = getWD();
	    Actions action = new Actions(driver);
	    WebElement elem = driver.findElement(elementLocator);
	    waitForElementToBeVisible(elem);
	    
	    action.moveToElement(elem);
	    
	    action.perform();
	    	
	}
	
	public void hoverOnElementClickandHold(By elementLocator) {
		WebDriver driver = getWD();
	    Actions action = new Actions(driver);
	    WebElement elem = driver.findElement(elementLocator);
	    waitForElementToBeVisible(elem);
	    	    
	    action.clickAndHold(elem);
	    action.perform();
	
	}
	
	public void centerBoxy() {
		getJsExecutor().executeScript("stackedBoxy[0].moveToX(0);");
	}
	
	public void moveBoxyToTheLeft() {
		getJsExecutor().executeScript("stackedBoxy[0].moveToX(0);");
	}
	
	public String handleOnScreenMessage() {
		
		List<WebElement> errorMessages = new ArrayList();
		String firstErrorMessageText = "";
		
		try
		{
			// get any errors from our onscreen error message region
			errorMessages = getWebElements(By.cssSelector(DIV_CLASS_MESSAGE_TEXT));
			// also gather any whole page errors (the whole visualforce page failed and instead we have a salesforce error displayed)
			if(errorMessages.size() == 0) errorMessages = getWebElements(By.cssSelector(SF_PAGE_ERROR_CONTAINER));
			    
			for (WebElement theErrorMessage : errorMessages) {
				String errorMessageText = theErrorMessage.getText();
				
				// only log messages if they aren't blank lines or the headings of the error
				// message box itself
				if(errorMessageText != null && errorMessageText != "" && errorMessageText != ERRORS && errorMessageText != ERROR)
				{
					if(firstErrorMessageText == "") firstErrorMessageText = errorMessageText;
					
					LogErrorMessage(ON_SCREEN_ERROR + errorMessageText);
					
					// attempt to get the rest of the detail for Whoops errors
					if(errorMessageText.contains(WHOOPS))
					{
						WebElement whoopsErrorElement = getWebElement(By.linkText(WHOOPS_ERROR_LINK));
						LogErrorMessage("Whoops Error Link: " + whoopsErrorElement.getAttribute(HREF_ATTR));
					}
				}
			}
		}
		catch(Exception ex)
		{
			// we aren't bothered about errors that happen here as the test has already thrown
			// an exception and if it was a technical exception the selenium driver
			// has already failed
		}
		
		// return the first of the errors or a blank string
		return firstErrorMessageText;
	}

	public static void setPackagedOrg(boolean packagedOrg) {
		SeleniumHelper.packagedOrg = packagedOrg;
	}

	public static void setLightningEnabled(boolean isLightning) {
		SeleniumHelper.isLightning = isLightning;
	}

	public static boolean isPackagedOrg() {
		return packagedOrg;
	}

	public void invalidExpectedResultsConfiguration(String factName, String measure) throws Exception {
		throw new Exception("Invalid Expected Results Fact configuration.  Fact [" + factName + "], Measure [" + measure + "]");
	}

	public void logValidationResult(String testStage, String factName, String measure, String expectedValue, String actualValue, String result) {
		String output = "   " + result + ": E[" + expectedValue + "] A[" + actualValue + "] (S[" + testStage + "], F[" + factName + "], M[" + measure + "])";
		
		if(result.equals("FAILED"))
			this.LogErrorMessageLine(output);		
		else
			this.LogMessageLine(output);
	}

	public void logIgnoreFact(String testStage, String factName, String ignoreReason, String measure) {
		this.LogMessageLine("   IGNORED, Reason [" + ignoreReason + "] (S[" + testStage + "], F[" + factName + "], M[" + measure + "])");
	}
	
    public void assertTrue(boolean actual, String narrative) {
    	try { Assert.assertTrue(actual, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(Object actual, Object expected, String narrative) {
    	try { Assert.assertEquals(actual, expected, narrative);	} catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(String actual, String expected, String narrative) {
    	try { Assert.assertEquals(actual, expected, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(Integer actual, Integer expected, String narrative) {
    	try { Assert.assertEquals(actual, expected, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(Long actual, Long expected, String narrative) {
    	try { Assert.assertEquals(actual, expected, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(Double actual, Double expected, String narrative) {
    	try { Assert.assertEquals(actual, expected, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(Float actual, Float expected, String narrative) {
    	try { Assert.assertEquals(actual, expected, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(Object actual, Object expected) {
    	try { Assert.assertEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(String actual, String expected) {
    	try { Assert.assertEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(Integer actual, Integer expected) {
    	try { Assert.assertEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(Long actual, Long expected) {
    	try { Assert.assertEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(Double actual, Double expected) {
    	try { Assert.assertEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void assertEquals(Float actual, Float expected) {
    	try { Assert.assertEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}
    

    public void assertNotEquals(Object actual, Object expected, String narrative) {
    	try { Assert.assertNotEquals(actual, expected, narrative);	} catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(String actual, String expected, String narrative) {
    	try { Assert.assertNotEquals(actual, expected, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(Integer actual, Integer expected, String narrative) {
    	try { Assert.assertNotEquals(actual, expected, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(Long actual, Long expected, String narrative) {
    	try { Assert.assertNotEquals(actual, expected, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(Double actual, Double expected, String narrative) {
    	try { Assert.assertNotEquals(actual, expected, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(Float actual, Float expected, String narrative) {
    	try { Assert.assertNotEquals(actual, expected, narrative); } catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(Object actual, Object expected) {
    	try { Assert.assertNotEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(String actual, String expected) {
    	try { Assert.assertNotEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(Integer actual, Integer expected) {
    	try { Assert.assertNotEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(Long actual, Long expected) {
    	try { Assert.assertNotEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(Double actual, Double expected) {
    	try { Assert.assertNotEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void assertNotEquals(Float actual, Float expected) {
    	try { Assert.assertNotEquals(actual, expected); } catch(Error e) { handleAssertError(e); }
	}

    public void handleAssertError(Error err) {
    	if(config.abortOnFirstValidationFailure)
    		throw err;
    	LogErrorMessage(err.getMessage());
    	recordValidationFailure(err.getMessage());
     }
    
	public void waitForElementVisible(By criteria, int timeoutSeconds){
		WebDriverWait wait = new WebDriverWait(getWD(), timeoutSeconds);
		wait.until(ExpectedConditions.visibilityOfElementLocated(criteria));
	}
    
    // Appium Helpers
	public void waitForElementToBeVisible(By criteria, int timeoutSeconds, AppiumDriver driver){
		WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
		wait.until(ExpectedConditions.visibilityOfElementLocated(criteria));
	}
	
	public void waitForElementToBeClickable(By criteria, int timeoutSeconds, AppiumDriver driver){
		WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
		wait.until(ExpectedConditions.elementToBeClickable(criteria));
	}
	
	public void appiumClick(By aTarget, AppiumDriver driver) {
		waitForElementToBeClickable(aTarget, 30, driver);
		driver.findElement(aTarget).click();
	}
	
	public void appiumClickVisibleElement(By aTarget, AppiumDriver driver) {
		waitForElementToBeVisible(aTarget, 25, driver);
		driver.findElement(aTarget).click();
	}
	
	public void clickAndWait(By clickElement, By waitForElement, int seconds, AppiumDriver driver) {
		appiumClick(clickElement, driver);
		waitForElementToBeClickable(waitForElement, seconds, driver);
	}
	
	public void clickAndEnterText(By aTarget, String textToEnter, AppiumDriver driver) {
		driver.findElement(aTarget).click();
		driver.getKeyboard().sendKeys(textToEnter);
	}
	
 	public void waitMilliseconds(int waitTime){
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
 	}
	
 	public void androidType(AppiumDriver driver, int ...androidKeyCodes ){
 		for (int code:androidKeyCodes)
 		{
 			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
 			((AndroidDeviceActionShortcuts) driver).pressKeyCode( code);
        }
 	}
 	
	public void clickAndWaitWithRetry(By clickElement, By waitForElement, int seconds, AppiumDriver driver){
		try {
			clickAndWait(clickElement, waitForElement, 20, driver);
		} catch(Exception e) {
			try {
				clickAndWait(clickElement, waitForElement, 20, driver);
			} catch(Exception ee) {
				clickAndWait(clickElement, waitForElement, 20, driver);
			}
		}
	}
	
	public void waitForElementToBeHidden(By criteria, int timeoutSeconds, AppiumDriver driver){
		WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(criteria));
	}
	
	public void getElementLocationAndTapCentreAndWait(By criteria, By waitForElement, AppiumDriver driver){
		
		int leftX = driver.findElement(criteria).getLocation().getX();
		int widthX = driver.findElement(criteria).getSize().getWidth();
		int middleX = leftX + (widthX/2);
		int upperY = driver.findElement(criteria).getLocation().getY();
		int heightY = driver.findElement(criteria).getSize().getHeight();
		int middleY = upperY + (heightY/2);
    	driver.tap(1, middleX, middleY, 100);
    	
    	try{
    		waitForElementToBeVisible(waitForElement, 10, driver);
    	}
    	catch(Exception e){
    		driver.tap(1, middleX, middleY, 100);
    	}
    	finally{
    		waitForElementToBeVisible(waitForElement, 10, driver);      
    	}
	}
	
	public void getElementLocationAndTapBelow(By criteria, AppiumDriver driver){
		// method taps the screen in the centre (x) and half the height of the element underneath it (y)
		int leftX = driver.findElement(criteria).getLocation().getX();
		int widthX = driver.findElement(criteria).getSize().getWidth();
		int middleX = leftX + (widthX/2);
		int upperY = driver.findElement(criteria).getLocation().getY();
		int heightY = driver.findElement(criteria).getSize().getHeight();
		int belowY = upperY + (heightY*3/2);
    	driver.tap(1, middleX, belowY, 100);
	}	
	
	public void tapCentre(AppiumDriver driver){
        int screenVerticalCentre = driver.manage().window().getSize().height *1/2 ;
        int screenHorizontalCentre = driver.manage().window().getSize().width *1/2 ;
    	driver.tap(1, screenHorizontalCentre, screenVerticalCentre, 100);
	}
	
	public void clickAndDissmissPopupAndWait(By clickElement, By waitForElement, AppiumDriver driver){
		
        int screenVerticalMiddle = driver.manage().window().getSize().height /2 ;
        int screenHorizontalRight = driver.manage().window().getSize().width *9/10;	    
        
        try {
        	appiumClick(clickElement, driver);
	        waitForElementToBeClickable(waitForElement, 20, driver);
		} catch(Exception e) {

			try {
		        driver.tap(1, screenHorizontalRight, screenVerticalMiddle, 100);
		        waitForElementToBeClickable(waitForElement, 20, driver);
			} catch(Exception ee) {
		        driver.tap(1, screenHorizontalRight, screenVerticalMiddle, 100);
		        waitForElementToBeClickable(waitForElement, 20, driver);
			}
		}
		
	}
	
	public void enterStringIntoMobileField(String str, AppiumDriver driver){
    	for(int i = 0, n = str.length(); i < n; i++){
    		int intOfStr = Character.getNumericValue(str.charAt(i));
    		
    		if(intOfStr==0){androidType(driver, AndroidKeyCode.KEYCODE_0);}
    		if(intOfStr==1){androidType(driver, AndroidKeyCode.KEYCODE_1);}
    		if(intOfStr==2){androidType(driver, AndroidKeyCode.KEYCODE_2);}
    		if(intOfStr==3){androidType(driver, AndroidKeyCode.KEYCODE_3);}
    		if(intOfStr==4){androidType(driver, AndroidKeyCode.KEYCODE_4);}
    		if(intOfStr==5){androidType(driver, AndroidKeyCode.KEYCODE_5);}
    		if(intOfStr==6){androidType(driver, AndroidKeyCode.KEYCODE_6);}
    		if(intOfStr==7){androidType(driver, AndroidKeyCode.KEYCODE_7);}
    		if(intOfStr==8){androidType(driver, AndroidKeyCode.KEYCODE_8);}
    		if(intOfStr==9){androidType(driver, AndroidKeyCode.KEYCODE_9);}
    	}        
	}
	
	public void enterAndroidBackspace(int taps, AppiumDriver driver){
    	for(int i = 0, n = taps-1; i < n; i++){		
    		androidType(driver, AndroidKeyCode.BACKSPACE);    		
    	}
	}
	
	public void enterIOSBackspace(int taps, AppiumDriver driver){
    	for(int i = 0, n = taps-1; i < n; i++){		
			driver.getKeyboard().pressKey(Keys.BACK_SPACE);		
    	}
	}
	
	public void scrollMobile(AppiumDriver driver){
        int screenVerticalUpper = driver.manage().window().getSize().height /5 ;
        int screenVerticalLower = driver.manage().window().getSize().height *4/5 ;
        int screenHorizontalmiddle = driver.manage().window().getSize().width /2;
 		new TouchAction(driver).press(screenHorizontalmiddle, screenVerticalLower).waitAction(400).moveTo(screenHorizontalmiddle, screenVerticalUpper).release().perform();
	}
	
	public void scrollMobileHalf(AppiumDriver driver){
		//	simulates scrolling down on mobile using 1/10 of the screen
        int screenVerticalUpper = driver.manage().window().getSize().height *2/10;
        int screenVerticalLower = driver.manage().window().getSize().height *3/10;
        int screenHorizontalmiddle = driver.manage().window().getSize().width /2;
 		new TouchAction(driver).press(screenHorizontalmiddle, screenVerticalLower).waitAction(400).moveTo(screenHorizontalmiddle, screenVerticalUpper).release().perform();
	}
	
	public void scrollToElement(AppiumDriver driver, By element){
        
		scrollMobile(driver);
		
		try{
			waitForElementToBeClickable(element, 5, driver);
		}catch(Exception e){
			scrollMobileHalf(driver);
			waitForElementToBeClickable(element, 5, driver);
		}
	}
	
	public void scrollMobileUp(AppiumDriver driver){
		//	simulates scrolling up on mobile using 2/5 of the screen
        int screenVerticalUpper = driver.manage().window().getSize().height *2/5 ;
        int screenVerticalLower = driver.manage().window().getSize().height *4/5 ;
        int screenHorizontalmiddle = driver.manage().window().getSize().width /2;
 		new TouchAction(driver).press(screenHorizontalmiddle, screenVerticalUpper).waitAction(200).moveTo(screenHorizontalmiddle, screenVerticalLower).release().perform();
	}
	
	public void swipeMobileLeft(By criteria, AppiumDriver driver){
		
		waitForElementToBeClickable(criteria, 10, driver);
		
		int leftX = driver.findElement(criteria).getLocation().getX();
		int widthX = driver.findElement(criteria).getSize().getWidth();
		int rightX = leftX + (widthX*9/10);
		int upperY = driver.findElement(criteria).getLocation().getY();
		int heightY = driver.findElement(criteria).getSize().getHeight();
		int middleY = upperY + (heightY/2);

 		new TouchAction(driver).press(rightX, middleY).waitAction(400).moveTo(leftX, middleY).release().perform();
	}
	
	public void uncheckIfVisibleAndEnabled(By target, AppiumDriver driver){
		try{
			waitForElementToBeClickable(target, 4, driver);
			System.out.println("Found: " + target);
			if(driver.findElement(target).getAttribute("checked").equals("true"));
				driver.findElement(target).click();
		}catch(Exception e){
			System.out.println("Skipping, couldn't find: " + target);
		}
	}
	
	public void checkIfVisibleAndDisabled(By target, AppiumDriver driver){
		try{
			waitMilliseconds(1000);
			waitForElementToBeClickable(target, 4, driver);
			System.out.println("Found: " + target);
			if(driver.findElement(target).getAttribute("checked").equals("false"));
				driver.findElement(target).click();
		}catch(Exception e){
			System.out.println("Skipping, couldn't find: " + target);
		}
	}
}
