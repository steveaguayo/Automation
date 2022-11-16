package kimble.automation.kimbleobjects.classic;

import kimble.automation.Config;
import kimble.automation.KimbleOneTest;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.UserCredentials;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidKeyCode;

import org.testng.Assert;
import java.util.Set;

public class MobileLoginPage extends BasePageC {
	
	private static final String PASSWORD_ID = "password";
	private static final String LOGIN_URL = "https://login.salesforce.com/";

	static final By
	usernameField_iOS = By.xpath("//XCUIElementTypeApplication[@name=\"Kimble 2.0\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeWebView/XCUIElementTypeOther"),
	doneBtn_iOS = By.xpath("//*[@label=\"Done\"]"),
	allowBtn_iOS = By.xpath("//input[@title='Allow'] "),
	loginBtn_iOS = By.name("Login"),	
	menuBtn_iOS = By.xpath("//XCUIElementTypeButton[contains(@name,'menu_btn')] "),	
	allowNotificationBtn_iOS = By.xpath("//XCUIElementTypeButton[@name='Allow']");	
	
	static final By
	usernameField_Android = By.xpath("//android.widget.EditText[@content-desc='Username']"),
	passwordField_Android = By.xpath("//android.webkit.WebView[@content-desc='Login | Salesforce']/android.view.View[2]/android.widget.EditText[2]"),
	allowBtn_Android = By.xpath("//android.widget.Button[@content-desc='Allow']"),
	loginBtn_Android = By.xpath("//android.widget.Button[@content-desc='Log In']"),
	menuBtn_Android = By.xpath("//android.widget.ImageButton[@content-desc='open side menu']");
	
	
	
	public MobileLoginPage(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	 
	public void MobileLogin(UserCredentials userCredentialsDetail, AppiumDriver driver, String testName){
		for(int i=0; i<Config.androidTests.length; i++){
			if(Config.androidTests[i].toString().contains(testName)){
		        theSH.appiumClick(usernameField_Android, driver);
				driver.getKeyboard().sendKeys(userCredentialsDetail.androidUsername);
				theSH.androidType(driver, AndroidKeyCode.KEYCODE_TAB);
		        theSH.appiumClick(passwordField_Android, driver);
				driver.getKeyboard().sendKeys(userCredentialsDetail.password);
				theSH.androidType(driver, AndroidKeyCode.KEYCODE_TAB);
		        theSH.appiumClick(loginBtn_Android, driver);
		        theSH.appiumClick(allowBtn_Android, driver);
		        theSH.waitForElementToBeClickable(menuBtn_Android, 60, driver);
			}
			if(Config.iOSTests[i].toString().contains(testName)){
				theSH.clickAndWait(allowNotificationBtn_iOS, loginBtn_iOS, 30, driver);
				theSH.waitForElementToBeClickable(loginBtn_iOS, 30, driver);
				theSH.clickAndWait(loginBtn_iOS, usernameField_iOS, 30, driver);
				theSH.appiumClick(usernameField_iOS, driver);
//				theSH.clickAndEnterText(usernameField_iOS, userCredentialsDetail.iPhoneUsername, driver);
//				theSH.appiumClick(doneBtn_iOS, driver);
//				theSH.clickAndEnterText(usernameField_iOS, userCredentialsDetail.password, driver);	//	enters pw
//		        driver.getKeyboard().pressKey(Keys.ENTER);
		        
				theSH.appiumClick(By.xpath("//XCUIElementTypeButton[@name='Previous']"), driver);
				driver.getKeyboard().sendKeys(userCredentialsDetail.iPhoneUsername);
				theSH.appiumClick(By.xpath("//XCUIElementTypeButton[@name='Next']"), driver);
				driver.getKeyboard().sendKeys(userCredentialsDetail.password);
		        driver.getKeyboard().pressKey(Keys.ENTER);
		        
		        try{
		        	Set<String> contextNames = driver.getContextHandles();
		        	driver.context((String) contextNames.toArray()[1]);
		        	theSH.waitForElementToBeClickable(allowBtn_iOS, 20, driver);
			        theSH.appiumClick(allowBtn_iOS, driver);
		        } 
		        catch(Exception e){
		        	try{
		        		Set<String> contextNames = driver.getContextHandles();
			        	driver.context((String) contextNames.toArray()[1]);
			        	theSH.waitForElementToBeClickable(allowBtn_iOS, 20, driver);
				        theSH.appiumClick(allowBtn_iOS, driver);
		        	}
		        	catch(Exception e1){
		        		Set<String> contextNames = driver.getContextHandles();
			        	driver.context((String) contextNames.toArray()[0]);
			        	driver.context((String) contextNames.toArray()[1]);
			        	theSH.waitForElementToBeClickable(allowBtn_iOS, 20, driver);
				        theSH.appiumClick(allowBtn_iOS, driver);
		        	}
		        }
		        Set<String> contextNames = driver.getContextHandles();
		        driver.context((String) contextNames.toArray()[0]);
		        theSH.waitMilliseconds(10000); // wait needed here to allow sync and load
	
		        theSH.waitForElementToBeClickable(menuBtn_iOS, 60, driver);
	
		    	driver.context("NATIVE_APP");
			}
		}
	}
}





