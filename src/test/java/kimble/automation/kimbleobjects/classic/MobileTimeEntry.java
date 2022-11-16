package kimble.automation.kimbleobjects.classic;

import kimble.automation.domain.KimbleData;
import kimble.automation.domain.ResourcedActivity;
import kimble.automation.domain.TimeEntry;
import kimble.automation.domain.Timesheet;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.kimbleobjects.classic.MobileSelectors;
import static kimble.automation.helpers.SequenceActions.*;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidKeyCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MobileTimeEntry extends BasePageC {

	public MobileTimeEntry(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	 
	public void createTimeEntry(SeleniumHelper sh, TimeEntry timeEntry, AppiumDriver driver, MobileSelectors selectors, String OS) throws ParseException{
        sh.waitForElementToBeClickable(selectors.timeEntryActivityField, 10, driver);
        enterActivity(sh, timeEntry, driver, selectors, OS);
		enterStartAndEndDate(sh, timeEntry, driver, selectors, OS);
		enterTimeType(sh, timeEntry, driver, selectors, OS);
		enterTask(sh, timeEntry, driver, selectors, OS);
		enterDraftRemaining(sh, timeEntry, driver, selectors, OS);
		enterEntryUnits(sh, timeEntry, driver, selectors, OS);
		enterCategories(sh, timeEntry, driver, selectors, OS);
		clickDone(sh, timeEntry, driver, selectors);
	}
	
	public void editTimesheetDetails(SeleniumHelper sh, Timesheet ts, TimeEntry te, AppiumDriver driver, MobileSelectors selectors, String OS){
        sh.waitForElementToBeVisible(selectors.timeEntryActivityField, 10, driver);
		enterEntryUnits(sh, te, driver, selectors, OS);
		clickDone(sh, te, driver, selectors);
	}
	
	public void enterActivity(SeleniumHelper sh, TimeEntry timeEntry, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
			sh.appiumClick(selectors.timeEntryActivityField, driver);
	        sh.waitForElementToBeVisible(By.xpath("//android.widget.CheckedTextView[contains(@resource-id,'desc_txt')]"), 30, driver); 
			sh.appiumClick(By.xpath("//android.widget.CheckedTextView[contains(@text,'"+timeEntry.plannedActivity+"')]"), driver);
		}
		if(OS.equals("iOS")){
			sh.appiumClick(selectors.timeEntryActivityField, driver);
	        sh.waitForElementToBeVisible(By.xpath("//XCUIElementTypeCell[@name='dropdown']"), 30, driver); 
			sh.appiumClick(By.xpath("//XCUIElementTypeCell[@name='dropdown']"), driver);
		}
	}
	
	public void enterStartAndEndDate(SeleniumHelper sh, TimeEntry timeEntry, AppiumDriver driver, MobileSelectors selectors, String OS) throws ParseException{
		// define the start and end dates
  /*      SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy");
        Calendar calStart = Calendar.getInstance();
        Date timeEntryStartDate = sdf.parse(timeEntry.startDate);
        calStart.setTime(timeEntryStartDate);        
        int startDay = calStart.get(Calendar.DAY_OF_MONTH);
        
        Date timeEntryEndDate = sdf.parse(timeEntry.endDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(timeEntryEndDate);        
        int endDay = calEnd.get(Calendar.DAY_OF_MONTH);
		
		if(OS.equals("Android")){
			sh.appiumClick(selectors.timeEntryStartDateField, driver);	
	        sh.appiumClick(By.xpath("//android.view.View[@text='"+startDay+"']"), driver);
	        sh.appiumClick(selectors.timeEntryCalendarOK, driver);
	        
	        sh.waitForElementToBeClickable(selectors.timeEntryActivityField, 10, driver);
			sh.appiumClick(selectors.timeEntryEndDateField, driver);
	        sh.appiumClick(By.xpath("//android.view.View[@text='"+endDay+"']"), driver);
	        sh.appiumClick(selectors.timeEntryCalendarOK, driver);
		}*/
		
		
		//XCUIElementTypeCell[3]/XCUIElementTypePicker/XCUIElementTypePickerWheel

		if(OS.equals("iOS")){
			try{
				sh.appiumClick(selectors.timeEntryStartDateField, driver);	
			} catch(Exception e){
				sh.scrollMobile(driver);
				sh.appiumClick(selectors.timeEntryStartDateField, driver);
			}
			
			
			
			driver.findElement(By.xpath("//XCUIElementTypePickerWheel")).sendKeys(timeEntry.startDate);	
			
			
			
			executeSequenceWithRetry(sh, 3, () -> {
				sh.appiumClick(By.xpath("//XCUIElementTypeSwitch[@name='Repeat Until']"), driver);
				sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='End Date']"), driver);
				driver.findElement(By.xpath("//XCUIElementTypePickerWheel")).sendKeys(timeEntry.endDate);
			});
		
			sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='End Date']"), driver);	
		}
	}
	
	public void enterTimeType(SeleniumHelper sh, TimeEntry timeEntry, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(!(timeEntry.timeType==null)){
			if(OS.equals("Android")){
		        sh.clickAndWait(selectors.timeEntryTimeTypeField, By.xpath("//android.widget.CheckedTextView[@text='"+timeEntry.timeType+"']"), 10, driver);
		        sh.appiumClick(By.xpath("//android.widget.CheckedTextView[@text='"+timeEntry.timeType+"']"), driver);
			}
			if(OS.equals("iOS")){
		        sh.clickAndWait(selectors.timeEntryTimeTypeField, By.xpath("//XCUIElementTypeStaticText[@name='"+timeEntry.timeType+"']"), 10, driver);
		        sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='"+timeEntry.timeType+"']"), driver);
			}
		}
	}
	
	public void enterEntryUnits(SeleniumHelper sh, TimeEntry timeEntry, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(OS.equals("Android")){
	        try{
	        	sh.clickAndWait(selectors.timeEntryUnitsHourField, selectors.timeEntryUnitsHourField, 10, driver);
	        }
	        catch(Exception e){
	        	sh.clickAndWait(selectors.timeEntryUnitsDayField, selectors.timeEntryUnitsDayField, 10, driver);
	        }
	       
	        sh.enterAndroidBackspace(4, driver);
	//        sh.enterStringIntoMobileField(timeEntry.entryUnits, driver);	
	        
	        // use this whilst resolve backwards number entry (then use comment above)
	        try{
		        driver.findElement(selectors.timeEntryUnitsHourField).sendKeys(timeEntry.entryUnits);
	        } catch(Exception e){
		        driver.findElement(selectors.timeEntryUnitsDayField).sendKeys(timeEntry.entryUnits);
	        }
		}
		if(OS.equals("iOS")){
			try{
				sh.appiumClick(selectors.timeEntryUnitsHourField, driver);
				sh.appiumClick(selectors.timeEntryUnitsHourField, driver);
	        	sh.clickAndWait(selectors.timeEntryUnitsHourField, By.xpath("//XCUIElementTypeKey[@name='1']"), 5, driver);
			}
			catch(Exception e){
				sh.appiumClick(selectors.timeEntryUnitsDayField, driver);
				sh.appiumClick(selectors.timeEntryUnitsDayField, driver);
	        	sh.clickAndWait(selectors.timeEntryUnitsDayField, By.xpath("//XCUIElementTypeKey[@name='1']"), 5, driver);
			}
			sh.enterIOSBackspace(5, driver);
			driver.getKeyboard().sendKeys(timeEntry.entryUnits);
	//		driver.getKeyboard().pressKey(Keys.ENTER);
			driver.getKeyboard().sendKeys(Keys.ENTER);
			sh.waitMilliseconds(4000);
	//      sh.appiumClick(selectors.timeEntryDoneBtn, driver);
		}
	}
	
	public void clickDone(SeleniumHelper sh, TimeEntry timeEntry, AppiumDriver driver, MobileSelectors selectors){
	        sh.appiumClick(selectors.timeEntryDoneBtn, driver);
	}
	
	public void enterCategories(SeleniumHelper sh, TimeEntry timeEntry, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(!(timeEntry.timeCategory1==null)){
			if(OS.equals("Android")){
				sh.scrollMobile(driver);
				sh.appiumClick(By.xpath("//android.widget.TextView[@text='"+timeEntry.timeCategory1+"']"), driver);
				sh.appiumClick(By.xpath("//android.widget.CheckedTextView[@text='"+timeEntry.timeCategory1Selection+"']"), driver);
				sh.appiumClick(By.xpath("//android.widget.TextView[@text='"+timeEntry.timeCategory2+"']"), driver);
				sh.appiumClick(By.xpath("//android.widget.CheckedTextView[@text='"+timeEntry.timeCategory2Selection+"']"), driver);
			}
			if(OS.equals("iOS")){
				//todo
			}			
		}
		
		
	}
	
	public void enterTask(SeleniumHelper sh, TimeEntry timeEntry, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(!(timeEntry.task==null)){
	        int screenVerticalUpper = driver.manage().window().getSize().height /5 ;
	        int screenVerticalLower = driver.manage().window().getSize().height *4/5 ;
	        int screenHorizontalmiddle = driver.manage().window().getSize().width /2;
//			driver.swipe(screenHorizontalmiddle, screenVerticalLower, screenHorizontalmiddle, screenVerticalUpper, 200);
	 		new TouchAction(driver).press(screenHorizontalmiddle, screenVerticalLower).waitAction(400).moveTo(screenHorizontalmiddle, screenVerticalUpper).release().perform();
			
			if(OS.equals("Android")){
		        sh.clickAndWait(selectors.timeEntryTaskField, By.xpath("//android.widget.CheckedTextView[@text='"+timeEntry.task+"']"), 10, driver);
		        sh.appiumClick(By.xpath("//android.widget.CheckedTextView[@text='"+timeEntry.task+"']"), driver);
			}
			if(OS.equals("iOS")){
				sh.appiumClick(selectors.timeEntryTaskField, driver);
		        sh.waitForElementToBeVisible(By.xpath("//XCUIElementTypeCell[@name='dropdown']"), 30, driver); // TODO need this to use the tsk name
				sh.appiumClick(By.xpath("//XCUIElementTypeCell[@name='dropdown']"), driver);
			}
		}
	}
	
	public void enterDraftRemaining(SeleniumHelper sh, TimeEntry timeEntry, AppiumDriver driver, MobileSelectors selectors, String OS){
		if(!(timeEntry.draftRemaining==null)){
			
			if(OS.equals("Android")){
		        sh.appiumClick(selectors.timeEntryDraftRemainingField, driver);
		        driver.getKeyboard().sendKeys(timeEntry.draftRemaining);
			}
			
			if(OS.equals("iOS")){			
				try{
					sh.appiumClick(selectors.timeEntryDraftRemainingField, driver);
		        	sh.clickAndWait(selectors.timeEntryDraftRemainingField, By.xpath("//XCUIElementTypeKey[@name='1']"), 5, driver);
				}
				catch(Exception e){
		        	sh.clickAndWait(selectors.timeEntryDraftRemainingField, By.xpath("//XCUIElementTypeKey[@name='1']"), 5, driver);
				}
	
				driver.getKeyboard().sendKeys(timeEntry.draftRemaining);
				driver.getKeyboard().pressKey(Keys.ENTER);
			}
		}
	}
	
	public void verifyTime(SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, Timesheet ts, String OS){
		if(OS.equals("Android")){
			for(TimeEntry te : ts.timeEntries){
				WebElement hour = (WebElement) driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'"+te.startDate+"')]/../following-sibling::android.widget.LinearLayout//android.widget.TextView[@index=1]"));
				Assert.assertEquals(hour.getText(), te.entryUnits);
			}
		} 
		
		if(OS.equals("iOS")){		
			for(TimeEntry te : ts.timeEntries){	
				String hoursToHour = te.entryUnits.replace("hours", " hours");
				WebElement hour = (WebElement) driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='"+te.startDate+"']/../..//XCUIElementTypeStaticText[@name='"+hoursToHour+"']"));
				Assert.assertEquals(hour.getText(), hoursToHour);
			}
		}
	}	
}




