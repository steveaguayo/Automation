package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.Iterator;
import java.util.Set;

import kimble.automation.KimbleOneTest;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;





public class ResourceSupplierDashboardPageC {
	
		static final By addSupplierProduct = By.cssSelector("a[title='Add Supplier Product']");
		static final By searchAccount = By.cssSelector("img[class='lookupIcon']");
		static final By searchGO = By.cssSelector("input[title='Go!']");
		
		static final By accountName = By.xpath("//div[@id='SupplierProductAddPopup']//input[@type='text']");
		static final By saveSupplierProduct = By.xpath("//div[@id='SupplierProductAddPopup']//input[@value='Save']");
		static final String accountNameLink = "//a[text() = '{{activityId}}']";
	
	
		
		
	
	public void addSupplierProduct(SeleniumHelper sh, String account){
		sh.clickAndWait(addSupplierProduct, saveSupplierProduct, 10);
	    clearAndInputText(sh, accountName, account);
	    By accountSearchLink = By.xpath(accountNameLink.replace("{{activityId}}", account));
	 	    
	    // click the search account button
	    sh.clickLink(searchAccount);
	    sh.waitMilliseconds(10000);
	   
	    //returns both windows in a string
	    Set <String> allWindows = sh.returnWindows(sh);    
   
	    //sets the main window in a string
	    Iterator<String> i1= allWindows.iterator();	
	    
	    String mainWin = i1.next();
	    String popUp = i1.next(); 
	    
	    sh.switchToWindow(popUp);
	    
	    sh.clickLink(accountSearchLink);
	    sh.switchToWindow(mainWin);
	    sh.clickAndWait(saveSupplierProduct, addSupplierProduct, 10);
	    
	}

	    
}   

