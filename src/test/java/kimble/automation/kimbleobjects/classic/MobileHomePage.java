package kimble.automation.kimbleobjects.classic;

import kimble.automation.KimbleOneTest;
import kimble.automation.domain.ExpenseClaim;
import kimble.automation.domain.ExpenseDetail;
import kimble.automation.domain.ExpenseEntry;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.TimeEntry;
import kimble.automation.domain.Timesheet;
import kimble.automation.domain.UserCredentials;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.kimbleobjects.classic.MobileSelectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidKeyCode;

import org.testng.Assert;

import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

public class MobileHomePage extends BasePageC {
	
	public MobileHomePage(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
 	
 	static final By
 	mondayBox = By.xpath("//XCUIElementTypeStaticText[@name='Monday']"),
 	tuesdayBox = By.xpath("//XCUIElementTypeStaticText[@name='Tuesday']"),
 	wednesdayBox = By.xpath("//XCUIElementTypeStaticText[@name='Wednesday']"),
 	thursdayBox = By.xpath("//XCUIElementTypeStaticText[@name='Thursday']"),
 	rejectedTimesheet = By.xpath("//XCUIElementTypeCell[@name='time_cell_server_too']"),
 	menuIcon = By.xpath("//android.widget.ImageButton[@content-desc='open side menu']");
 
	 
 	public void testClickMenu(SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
	        sh.appiumClick(selectors.menuBtn, driver);
		} 
		if(OS.equals("iOS")){
	        sh.appiumClick(selectors.menuBtn, driver);
		}
 	}
 	
 	public void scrollToTimePeriod(SeleniumHelper sh, Timesheet timesheetDetails, AppiumDriver driver, MobileSelectors selectors) throws ParseException{

        sh.waitForElementToBeVisible(selectors.dateWidget, 60, driver);
        
        int counter = 0;
        int screenVerticalMiddle = driver.manage().window().getSize().height /2;
        int screenHorizontalLeft = driver.manage().window().getSize().width /10;
        int screenHorizontalRight = screenHorizontalLeft *9;
        
 		while(counter < 100){
	        String mobileCurrentDate = driver.findElement(selectors.dateWidget).getAttribute(selectors.timePeriodAttribute);
	        String timeEntryDate = timesheetDetails.mobilePeriodDate;
	        
	        if(!mobileCurrentDate.contains(timeEntryDate)) {
	            sh.waitForElementToBeClickable(selectors.dateWidget, 20, driver);
		 		new TouchAction(driver).press(screenHorizontalLeft, screenVerticalMiddle).waitAction(400).moveTo(screenHorizontalRight, screenVerticalMiddle).release().perform();	
	            sh.waitForElementToBeClickable(selectors.dateWidget, 20, driver);
	        } else {
	            counter = 100;
	            break;
	        }
		    counter++;
 		}
	}
 	
 	public void switchToTimeView(SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
	        sh.waitForElementToBeClickable(selectors.menuBtn, 10, driver);
	        sh.clickAndWait(selectors.menuBtn, selectors.timeEntryBtn, 10, driver); 
	        sh.clickAndWait(selectors.timeEntryBtn, selectors.menuBtn, 10, driver); 
		}
		if(OS.equals("iOS")){	
			try{
		        int screenVerticalMiddle = driver.manage().window().getSize().height /2 ;
		        int screenHorizontalRight = driver.manage().window().getSize().width *9/10;
		 		new TouchAction(driver).press(0, screenVerticalMiddle).waitAction(400).moveTo(screenHorizontalRight, screenVerticalMiddle).release().perform();	
		        sh.clickAndWait(selectors.timeEntryBtn, selectors.dateWidget, 60, driver);
		       
		        
			} catch (Exception e){
				sh.waitMilliseconds(10000);		        
		        int screenVerticalMiddle = driver.manage().window().getSize().height /2 ;
		        int screenHorizontalRight = driver.manage().window().getSize().width *9/10;
		 		new TouchAction(driver).press(0, screenVerticalMiddle).waitAction(400).moveTo(screenHorizontalRight, screenVerticalMiddle).release().perform();	
		 		sh.clickAndWait(selectors.timeEntryBtn, selectors.dateWidget, 60, driver); 
		        
			}		
		}
	}    	
			
 	public void switchToExpensesView(SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS){
 		
 		
 		if(OS.equals("iOS")){
 			sh.waitForElementToBeClickable(selectors.menuBtn, 30, driver);
	        int screenVerticalMiddle = driver.manage().window().getSize().height /2 ;
	        int screenHorizontalRight = driver.manage().window().getSize().width *9/10;
	 		new TouchAction(driver).press(0, screenVerticalMiddle).waitAction(400).moveTo(screenHorizontalRight, screenVerticalMiddle).release().perform();	
	 		sh.appiumClickVisibleElement(selectors.expenseEntryBtn, driver);
 			sh.waitForElementToBeVisible(selectors.expenseEntryAdd, 15, driver);
 			
 		} else{
 			
	 		try{
	 			sh.appiumClickVisibleElement(selectors.menuBtn, driver);
	 	 		sh.appiumClickVisibleElement(selectors.expenseEntryBtn, driver);
	 			sh.waitForElementToBeVisible(selectors.expenseEntryAdd, 15, driver);
	 	 	} catch(Exception e){
	 	 		sh.appiumClickVisibleElement(selectors.menuBtn, driver);
	 	 		sh.appiumClickVisibleElement(selectors.expenseEntryBtn, driver);
	 			sh.waitForElementToBeVisible(selectors.expenseEntryAdd, 15, driver);
	 		}
 		}
 	}
	
 	
 	public void addExpense(SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
			sh.clickAndWait(selectors.expenseEntryAdd, By.id("kimble.timeandexpenses2:id/add_claim_btn"), 20, driver);
			sh.clickAndWait(By.id("kimble.timeandexpenses2:id/add_claim_btn"), selectors.expenseNameField, 15, driver);
		}
		if(OS.equals("iOS")){
			sh.clickAndWait(selectors.expenseEntryAdd, By.xpath("//XCUIElementTypeButton[@name='goto_time_btn']"), 20, driver);
		}
		
	}  	
 	
 	public void addTimeEntry(SeleniumHelper sh, AppiumDriver driver, Timesheet timesheetDetails, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
			sh.clickAndWait(By.xpath("//android.widget.TextView[contains(@resource-id,'left_subtitle_lbl') and @text='"+timesheetDetails.mobileTrackingPeriodStartDate+"']"), selectors.timeEntryAdd, 20, driver);
			sh.clickAndWait(selectors.timeEntryAdd, selectors.timeEntryActivityField, 20, driver);
			}
		if(OS.equals("iOS")){
	        sh.waitForElementToBeClickable(By.xpath("//XCUIElementTypeStaticText[@name='"+timesheetDetails.mobileTrackingPeriodStartDate+"']"), 60, driver);
        	sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='"+timesheetDetails.mobileTrackingPeriodStartDate+"']"), driver);
        	
        	// clicking the add time button doesn't work - need to click the centre of the image
        	try{
        		sh.getElementLocationAndTapCentreAndWait(selectors.timeEntryAdd, selectors.timeEntryActivityField, driver);        		
        	}catch(Exception e){ 
            	sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='"+timesheetDetails.mobileTrackingPeriodStartDate+"']"), driver);
        		sh.getElementLocationAndTapCentreAndWait(selectors.timeEntryAdd, selectors.timeEntryActivityField, driver);
        	}

		}
	}    	
 	
 	public void mobileSync(SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS){
 		if(OS.equals("Android")){
 			try{
 				sh.clickAndWait(menuIcon, selectors.syncBtn, 15, driver); 
 			//	sh.clickAndWait(selectors.menuBtn, selectors.syncBtn, 15, driver); 
 			} catch(Exception e){
 				sh.scrollMobile(driver);
		        sh.clickAndWait(selectors.menuBtn, selectors.syncBtn, 15, driver); 
 			}

	        
	        try{
		        sh.appiumClick(selectors.syncBtn, driver);
	        } catch(Exception e){
	        	sh.scrollMobile(driver);
		        sh.appiumClick(selectors.syncBtn, driver);
	        }
	        
	        sh.waitMilliseconds(10000);
	        
	        sh.waitForElementToBeVisible(selectors.syncSuccessful, 60, driver);
	        
	        sh.clickAndWait(selectors.syncSuccessful, selectors.menuBtn, 20, driver);
		}

		if(OS.equals("iOS")){
			//sh.waitForElementToBeClickable(selectors.menuBtn, 30, driver);
	        int screenVerticalMiddle = driver.manage().window().getSize().height /2 ;
	        int screenHorizontalRight = driver.manage().window().getSize().width *9/10;
	 		new TouchAction(driver).press(0, screenVerticalMiddle).waitAction(400).moveTo(screenHorizontalRight, screenVerticalMiddle).release().perform();			
			
			try{
				sh.clickAndDissmissPopupAndWait(selectors.syncBtn, selectors.syncBtn, driver);
			} catch(Exception e){
		 		new TouchAction(driver).press(screenHorizontalRight, screenVerticalMiddle).waitAction(400).moveTo(0, screenVerticalMiddle).release().perform();
		        sh.waitForElementToBeClickable(selectors.menuBtn, 30, driver);
		 		new TouchAction(driver).press(0, screenVerticalMiddle).waitAction(400).moveTo(screenHorizontalRight, screenVerticalMiddle).release().perform();
		        sh.waitForElementToBeClickable(selectors.syncBtn, 30, driver);
				sh.clickAndDissmissPopupAndWait(selectors.syncBtn, selectors.syncBtn, driver);
			}

	 		new TouchAction(driver).press(screenHorizontalRight, screenVerticalMiddle).waitAction(400).moveTo(0, screenVerticalMiddle).release().perform();

			try{
		        sh.waitForElementToBeClickable(selectors.menuBtn, 10, driver);
			}catch(Exception e){
				sh.appiumClick(By.xpath("//XCUIElementTypeNavigationBar[@name='Kimble.KATimesheetVC']/XCUIElementTypeButton[1]"), driver);
		        sh.waitForElementToBeClickable(selectors.menuBtn, 20, driver);
			}
		}
 	}
 	
 	public void submitAll(SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
			try{
		        sh.waitForElementToBeClickable(selectors.submitAll, 10, driver);				
			}catch(Exception e){
				sh.scrollMobile(driver);
		        sh.waitForElementToBeClickable(selectors.submitAll, 10, driver);				
			}
			
			sh.sleep(10000); // adding wait for elements to sync 
	        sh.appiumClick(selectors.submitAll, driver);
	        
	        sh.appiumClick(By.id("android:id/button2"), driver);
	        
	        sh.waitForElementToBeVisible(By.xpath("//android.widget.TextView[@text='Submitted Successfully!']"), 25, driver);
	        driver.findElement(By.xpath("//android.widget.TextView[@text='Submitted Successfully!']")).click();
	        sh.waitForElementToBeClickable(selectors.menuBtn, 25, driver);
	        try{
		        sh.appiumClick(By.xpath("//android.widget.ImageView[contains(@resource-id,'action_mode_close_button')]"), driver);
	        } catch(Exception e){
	        	// do nothing
	        }
	        
	        sh.sleep(5000);
		}
		if(OS.equals("iOS")){ 
			sh.scrollMobileUp(driver);
			sh.appiumClick(selectors.submitAll, driver);
			sh.appiumClick(By.xpath("//XCUIElementTypeButton[@name='Yes']"), driver);
			
	        sh.waitForElementToBeVisible(By.xpath("//XCUIElementTypeStaticText[@name='Submitted Successfully!']"), 25, driver);
	        try{
	        	sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='Submitted Successfully!']"), driver);
	        }catch(Exception e){
	        	sh.tapCentre(driver);
	        }
	      
		}
 	} 
 	
 	public void deleteSelectedTimeEntries(SeleniumHelper sh, Timesheet ts, AppiumDriver driver, MobileSelectors selectors, String OS){
 		// todo
		if(OS.equals("Android")){
	        sh.clickAndWait(selectors.optionsBtn, selectors.deleteBtn, 10, driver); 
			sh.appiumClick(selectors.deleteBtn, driver);
			for(int i = 0; i < ts.timeEntries.size(); i++){
				sh.appiumClick(By.xpath("//android.widget.TextView[@text='"+ ts.timeEntries.get(i).startDate +"']/../following-sibling::android.widget.CheckBox"), driver);
			}
			
	        sh.appiumClick(selectors.submitSelected, driver);
	        sh.waitForElementToBeVisible(By.xpath("//android.widget.TextView[@text='Submitted Successfully!']"), 25, driver);
	        driver.findElement(By.xpath("//android.widget.TextView[@text='Submitted Successfully!']")).click();
	        sh.appiumClick(By.xpath("//android.widget.ImageView[contains(@resource-id,'action_mode_close_button')]"), driver);
		}
        
		if(OS.equals("iOS")){ 
			// todo
	        sh.clickAndWait(selectors.optionsBtn, selectors.deleteBtn, 10, driver); 
			sh.appiumClick(selectors.deleteBtn, driver);
			
			for(int i = 0; i < ts.timeEntries.size(); i++){
				sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='"+ ts.timeEntries.get(i).startDate +"']"), driver);
			}
			sh.appiumClick(selectors.submitSelected, driver);
	        sh.waitForElementToBeVisible(By.xpath("//XCUIElementTypeStaticText[@name='Submitted Successfully!']"), 25, driver);
	        sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='Submitted Successfully!']"), driver);
	        sh.waitForElementToBeClickable(selectors.submitSelected, 25, driver);
		}		
 	}   	
 	
 	public void submitSelectedExpenseClaim(SeleniumHelper sh, List<ExpenseEntry> expenses, ExpenseEntry entryToSubmit, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
			sh.appiumClick(selectors.optionsBtn, driver);
			sh.appiumClick(selectors.selectBtn, driver);
			
			for(int i = 0; i < expenses.size(); i++){
				driver.findElement(By.xpath("//android.widget.TextView[@text='"+expenses.get(i).name+"']/../following-sibling::android.widget.CheckBox")).click();
			}
			
			driver.findElement(By.xpath("//android.widget.TextView[@text='"+entryToSubmit.name+"']/../following-sibling::android.widget.CheckBox")).click();
	        sh.appiumClick(selectors.submitSelected, driver);
	        sh.waitForElementToBeVisible(By.xpath("//android.widget.TextView[@text='Submitted Successfully!']"), 10, driver);
	        driver.findElement(By.xpath("//android.widget.TextView[@text='Submitted Successfully!']")).click();
	        sh.appiumClick(By.xpath("//android.widget.ImageView[contains(@resource-id,'action_mode_close_button')]"), driver);
		}
        
		if(OS.equals("iOS")){ 
			// todo
			sh.appiumClick(selectors.selectBtn, driver);
			
			for(int i = 0; i < expenses.size(); i++){		
				sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='"+expenses.get(i).name+"']"), driver);
			}
			sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='"+entryToSubmit.name+"']"), driver);
			
			sh.appiumClick(selectors.submitSelected, driver);
	        sh.waitForElementToBeVisible(By.xpath("//XCUIElementTypeStaticText[@name='Submitted Successfully!']"), 10, driver);
	        sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='Submitted Successfully!']"), driver);
	        sh.waitForElementToBeClickable(selectors.submitSelected, 10, driver);
//	        sh.clickAndDissmissPopupAndWait(selectors.submitSelected, selectors.submitSelected, driver);
		}
 	}   
 	
 	public void addExpenseToExistingEntry(SeleniumHelper sh, ExpenseEntry entry, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){ 		
			sh.appiumClickVisibleElement(By.xpath("//android.widget.TextView[@text='"+entry.name+"']"), driver);
	 		sh.appiumClickVisibleElement(By.xpath("//android.widget.TextView[@text='"+entry.name+"']/../../following-sibling::android.widget.LinearLayout"), driver);
		}
        
		if(OS.equals("iOS")){ 
			sh.appiumClickVisibleElement(By.xpath("//XCUIElementTypeStaticText[@name='"+entry.name+"']"), driver);
			sh.appiumClickVisibleElement(By.xpath("//XCUIElementTypeCell[@name='additem_cell']"), driver);
		}
 	}
 	
 	public void selectExpense(SeleniumHelper sh, ExpenseEntry entry, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){ 		
			sh.appiumClickVisibleElement(By.xpath("//android.widget.TextView[@text='"+entry.name+"']"), driver);
//	 		sh.appiumClickVisibleElement(By.xpath("//android.widget.TextView[@text='"+entry.name+"']/../../following-sibling::android.widget.FrameLayout"), driver);
			sh.appiumClick(By.id("kimble.timeandexpenses2:id/expenses_view_listview_subitem_rootlayout"), driver);
		}
        
		if(OS.equals("iOS")){ 
			sh.appiumClickVisibleElement(By.xpath("//XCUIElementTypeStaticText[@name='"+entry.name+"']"), driver);
			sh.appiumClickVisibleElement(By.xpath("//XCUIElementTypeCell[@name='expense_cell']"), driver);
		}
 	}
 	
 	public void editExpenseClaim(SeleniumHelper sh, ExpenseEntry entry, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){ 		
			sh.appiumClickVisibleElement(By.xpath("//android.widget.TextView[@text='"+entry.name+"']"), driver);
			sh.appiumClickVisibleElement(By.xpath("//android.widget.TextView[@text='"+detail.category+"']"), driver);
			
		}
		if(OS.equals("iOS")){
			sh.appiumClickVisibleElement(By.xpath("//XCUIElementTypeStaticText[@name='"+entry.name+"']"), driver);
			sh.appiumClickVisibleElement(By.xpath("//XCUIElementTypeStaticText[@name='"+detail.category+"']"), driver);
		}
 	}
 	
 	public void editTimeEntry(SeleniumHelper sh, Timesheet ts, TimeEntry te, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){ 		
			sh.appiumClickVisibleElement(By.xpath("//android.widget.TextView[@text='"+te.startDate.replace("2013", "13")+"']"), driver);
			sh.appiumClickVisibleElement(By.xpath("//android.widget.TextView[@text='"+te.plannedActivity+" ']"), driver);
			
		}
		if(OS.equals("iOS")){
			sh.appiumClickVisibleElement(By.xpath("//XCUIElementTypeStaticText[@name='"+te.startDate.replace("2013", "13")+"']"), driver);
			sh.appiumClickVisibleElement(By.xpath("//XCUIElementTypeStaticText[@name='"+te.plannedActivity+" ']"), driver);
		}
 	}
	
 	public void uncheckFridayAndSubmitTime(SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
			By friday = By.xpath("//android.widget.TextView[@text='Friday']/../following-sibling::android.widget.CheckBox");
//			sh.appiumClick(selectors.optionsBtn, driver);
//			sh.appiumClick(selectors.selectBtn, driver);
//			sh.waitForElementToBeClickable(selectors.submitAll, 15, driver);
//			sh.scrollMobile(driver);
//			sh.waitForElementToBeClickable(friday, 15, driver);
//			
//			if(driver.findElement(friday).getAttribute("checked").equals("true"));
//				driver.findElement(friday).click();
//				
//			sh.scrollMobileUp(driver);
//			sh.scrollMobileUp(driver);
//			submitAll(sh, driver, selectors, OS);
			sh.waitMilliseconds(5000);
			sh.appiumClick(selectors.submitSelected, driver);
			
			try{
				sh.appiumClick(By.xpath("//android.widget.TextView[@text='Monday']/../following-sibling::android.widget.CheckBox"), driver);
			} catch(Exception e){
				sh.waitMilliseconds(5000);
				sh.appiumClick(selectors.submitSelected, driver);
				sh.appiumClick(By.xpath("//android.widget.TextView[@text='Monday']/../following-sibling::android.widget.CheckBox"), driver);
			}
			sh.waitMilliseconds(5000);
			sh.appiumClick(selectors.submitAll, driver);
			
			
	        sh.waitForElementToBeVisible(By.xpath("//android.widget.TextView[@text='Submitted Successfully!']"), 25, driver);
	        driver.findElement(By.xpath("//android.widget.TextView[@text='Submitted Successfully!']")).click();
	        
			sh.appiumClick(By.xpath("//android.widget.Button[@text='Cancel']"), driver);
			
	        sh.waitForElementToBeClickable(selectors.menuBtn, 25, driver);
		}
        
		if(OS.equals("iOS")){ 
			sh.appiumClick(By.xpath("//XCUIElementTypeButton[@name='Select Items']"), driver);
//			sh.waitForElementToBeClickable(By.xpath("//XCUIElementTypeStaticText[@name='Monday']"), 15, driver);
			
			// key change here - when clicking Submit Selected, the days are ALL default NOT Selected. Select Monday & submit?
			sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='Monday']"), driver);
	        sh.appiumClick(selectors.submitSelected, driver);
			
	        sh.waitForElementToBeVisible(By.xpath("//XCUIElementTypeStaticText[@name='Submitted Successfully!']"), 25, driver);
	        try{
	        	sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='Submitted Successfully!']"), driver);
	        }catch(Exception e){
	        	sh.tapCentre(driver);
	        }
			sh.appiumClick(By.xpath("//XCUIElementTypeButton[@name='Cancel']"), driver);
	        sh.waitForElementToBeClickable(selectors.menuBtn, 30, driver);
		}
 	}  
	
 	public void deleteTimeEntries(SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
			By Monday = By.xpath("//android.widget.TextView[@text='Monday']/../following-sibling::android.widget.CheckBox");
//			By deleteTimeEntries = By.xpath("//android.widget.TextView[contains(@content-desc,'Delete')]");
			sh.appiumClick(selectors.submitSelected, driver);
//			sh.appiumClick(selectors.deleteBtn, driver);
			sh.appiumClick(Monday, driver);
			sh.appiumClick(selectors.deleteBtn, driver);
			sh.appiumClick(By.xpath("//android.widget.Button[@text='Cancel']"), driver);


//			for(int i = 0; i < 6; i++){
//				By dayCheckbox = By.xpath("//android.widget.FrameLayout[@index='" + i + "']//android.widget.CheckBox");
//				By activityCheckbox = By.xpath("//android.widget.LinearLayout[@index='" + i + "']//android.widget.CheckBox");
//				
//				try{
//					if(driver.findElement(activityCheckbox).getAttribute("checked").equals("false")){
//						driver.findElement(activityCheckbox).click();
//					}					
//				} catch(Exception e){}				
//				try{
//					if(driver.findElement(dayCheckbox).getAttribute("checked").equals("false")){
//						driver.findElement(dayCheckbox).click();
//					}					
//				} catch(Exception e){}
//			}
//			
//			sh.scrollMobile(driver);
//			
//			for(int i = 0; i < 6; i++){
//				By dayCheckbox = By.xpath("//android.widget.FrameLayout[@index='" + i + "']//android.widget.CheckBox");
//				By activityCheckbox = By.xpath("//android.widget.LinearLayout[@index='" + i + "']//android.widget.CheckBox");
//				
//				try{
//					if(driver.findElement(activityCheckbox).getAttribute("checked").equals("false")){
//						driver.findElement(activityCheckbox).click();
//					}					
//				} catch(Exception e){}				
//				try{
//					if(driver.findElement(dayCheckbox).getAttribute("checked").equals("false")){
//						driver.findElement(dayCheckbox).click();
//					}					
//				} catch(Exception e){}
//			}
//			
//	        sh.appiumClick(deleteTimeEntries, driver);
//	        sh.appiumClick(By.xpath("//android.widget.ImageView[contains(@content-desc,'Done')]"), driver);
		}
        
		if(OS.equals("iOS")){ 
    		sh.getElementLocationAndTapCentreAndWait(mondayBox, By.xpath("//XCUIElementTypeStaticText[@name='Rejected']"), driver);  
			sh.swipeMobileLeft(rejectedTimesheet, driver);	
			
			try{
				sh.appiumClick(By.xpath("//XCUIElementTypeButton[@name='Delete']"), driver);
		        sh.waitForElementToBeHidden(rejectedTimesheet, 15, driver);
			} catch(Exception e){
				sh.waitForElementToBeHidden(rejectedTimesheet, 15, driver);
			}
			
		}
 	}     	
	
 	public void submitSelectedTimeEntry(SeleniumHelper sh, List<TimeEntry> te, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
			By day = By.xpath("//android.widget.TextView[@text='" + te + "']/../following-sibling::android.widget.CheckBox");
			sh.appiumClick(selectors.optionsBtn, driver);
			sh.appiumClick(selectors.selectBtn, driver);
			
			sh.uncheckIfVisibleAndEnabled(day, driver);
			
			sh.scrollMobileUp(driver);
			sh.scrollMobileUp(driver);
			
			for(int i = 0; i < te.size(); i++){
				System.out.println(By.xpath("//android.widget.TextView[@text='"+ te.get(i).startDate.replace("201", "1") +"']/../following-sibling::android.widget.CheckBox"));
				sh.checkIfVisibleAndDisabled(By.xpath("//android.widget.TextView[@text='"+ te.get(i).startDate.replace("201", "1") +"']/../following-sibling::android.widget.CheckBox"), driver);
			}
			
	        sh.appiumClick(selectors.submitSelected, driver);
		}
        
		if(OS.equals("iOS")){ 
			// todo
		}
 	}   
}





