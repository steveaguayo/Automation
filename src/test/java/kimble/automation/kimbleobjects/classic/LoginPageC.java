package kimble.automation.kimbleobjects.classic;

import java.net.URLEncoder;

import kimble.automation.domain.UserCredentials;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPageC extends BasePageC {
	private static final String PASSWORD_ID = "password";

	private static final String LOGIN_URL = "https://login.salesforce.com/";
		
	@FindBy(id="username")
	private WebElement usernameField;
	
	@FindBy(id=PASSWORD_ID)
	private WebElement passwordField;
	
	@FindBy(id="Login")
	private WebElement loginButton;
	
	public LoginPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void Login(UserCredentials userCredentialsDetail){
		fastLogin(userCredentialsDetail);

		// wait for any redirects etc to complete before continuing
		theSH.sleep(5000);
	}
	
	public void fastLogin(UserCredentials userCredentialsDetail){
		theSH.waitForElementToBeClickable(By.id(PASSWORD_ID));
		usernameField.sendKeys(userCredentialsDetail.username);
		passwordField.sendKeys(userCredentialsDetail.password);
		
		loginButton.click();
	}
	
	public void NavigateToLoginPage(String loginUrl){
		theSH.goToUrl(loginUrl);
	}
	
	public void NavigateToLoginPage(){
		theSH.goToUrl(LOGIN_URL);
	}
	
	public void NavigateToLoginPageAndDirectToAllTabs(){
		NavigateToLoginPageAndDirect(theSH, null, "home/showAllTabs.jsp");
	}
	
	public void NavigateToLoginPageAndDirectToAllTabs(String loginUrl){
		NavigateToLoginPageAndDirect(theSH, loginUrl, "home/showAllTabs.jsp");
	}
	
	public static void NavigateToLoginPageAndDirect(SeleniumHelper sh, String loginUrl, String startUrl) {
		try {
			sh.goToUrl((loginUrl != null && loginUrl != "" ? loginUrl : LOGIN_URL) + 
					(startUrl != null ? "?startURL=" + URLEncoder.encode(startUrl, "UTF-8") : ""));
		} catch(Exception e) {
			throw new RuntimeException("Failed to navigate to login with redirect to home/showAllTabs.jsp" , e);
		}
	}
}
