package kimble.automation.kimbleobjects.classic;

import kimble.automation.domain.KimbleData;
import kimble.automation.domain.ExpenseDetail;
import kimble.automation.domain.ExpenseEntry;
import kimble.automation.domain.UserCredentials;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.kimbleobjects.classic.MobileSelectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidKeyCode;

import org.testng.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class MobileExpenseEntry extends BasePageC {

	public MobileExpenseEntry(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	  
	public void createExpenseClaim(SeleniumHelper sh, ExpenseEntry expense, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) throws ParseException{

		if(OS.equals("Android")){
			sh.waitForElementToBeClickable(selectors.expenseActivityField, 20, driver);
		}
		if(OS.equals("iOS")){
			sh.appiumClick(By.xpath("//XCUIElementTypeButton[@name='goto_time_btn']"), driver);
		}
        enterClaimName(sh, expense, driver, selectors, OS);
		enterClaimStartAndEndDate(sh, expense, driver, selectors, OS);
        enterClaimActivity(sh, expense, driver, selectors, OS);
        
        AddExpenseItem(sh, expense, detail, driver, selectors, OS);
        
        enterClaimCategory(sh, detail, driver, selectors);
        enterClaimIncurredAmount(sh, detail, driver, selectors, OS);
        enterClaimReceipt(sh, detail, driver, selectors, OS); 
        enterClaimStartAndEndLocation(sh, detail, driver, selectors, OS);
        enterClaimMileageUnits(sh, detail, driver, selectors, OS);
        enterClaimAttendees(sh, detail, driver, selectors, OS); 
        enterClaimNotes(sh, detail, driver, selectors, OS);
        clickDone(sh, driver, selectors, OS);
	}
	
	public void AddExpenseItem(SeleniumHelper sh, ExpenseEntry expense, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(OS.equals("Android")){
			sh.appiumClick(By.id("kimble.timeandexpenses2:id/add_btn"), driver);
		}
		if(OS.equals("iOS")){
			sh.appiumClick(By.xpath("//XCUIElementTypeButton[@name='Add']"), driver);
		}
	}
	
	public void addExpenseToExistingClaim(SeleniumHelper sh, ExpenseEntry expense, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) throws ParseException{
		enterClaimStartAndEndDate(sh, expense, driver, selectors, OS);
        enterClaimCategory(sh, detail, driver, selectors);
        enterClaimIncurredAmount(sh, detail, driver, selectors, OS);
        enterClaimReceipt(sh, detail, driver, selectors, OS); 
        enterClaimStartAndEndLocation(sh, detail, driver, selectors, OS);
        enterClaimMileageUnits(sh, detail, driver, selectors, OS);
        enterClaimAttendees(sh, detail, driver, selectors, OS); 
        enterClaimNotes(sh, detail, driver, selectors, OS);
        clickDone(sh, driver, selectors, OS);
	}
	
	public void editClaimDetails(SeleniumHelper sh, ExpenseEntry expense, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) throws ParseException{

		sh.scrollMobile(driver);
        enterClaimIncurredAmount(sh, detail, driver, selectors, OS);
        enterClaimMileageUnits(sh, detail, driver, selectors, OS);
        clickDone(sh, driver, selectors, OS);
	}
	
	public static By ANDROID_EXPENSE_SAVE = By.xpath("//android.widget.Button[@text='Save']");
	
	private void clickDone(SeleniumHelper sh, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(OS.equals("Android")){
			try{
				sh.clickAndWait(ANDROID_EXPENSE_SAVE, selectors.submitAll, 10, driver);	
			} catch(Exception e){
				sh.clickAndWait(ANDROID_EXPENSE_SAVE, selectors.submitAll, 10, driver);	
			}
		}
		if(OS.equals("iOS")){
		
			try{
				sh.clickAndWait(selectors.expenseDoneBtn, selectors.submitAll, 20, driver);	
			} catch(Exception e){
				sh.scrollMobile(driver);
				sh.clickAndWait(selectors.expenseDoneBtn, selectors.submitAll, 20, driver);	
			}
		}
	}

	private void enterClaimName(SeleniumHelper sh, ExpenseEntry expenseEntry, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(!(expenseEntry.name==null)){
			if(OS.equals("Android")){
		        sh.appiumClick(selectors.expenseNameField, driver);
		        sh.sleep(2000);
		        driver.getKeyboard().sendKeys(expenseEntry.name);
			}
			if(OS.equals("iOS")){
				try{
					sh.appiumClick(selectors.expenseNameField, driver);
				} catch(Exception e){
					sh.scrollMobile(driver);
					sh.appiumClick(selectors.expenseNameField, driver);
				}
		        driver.getKeyboard().sendKeys(expenseEntry.name);
				sh.appiumClick(By.xpath("//XCUIElementTypeButton[@name='Done']"), driver);
			}
		}
	}

	private void enterClaimStartAndEndDate(SeleniumHelper sh, ExpenseEntry expenseEntry, AppiumDriver driver, MobileSelectors selectors, String OS) throws ParseException {
		// define the start and end dates
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        Calendar calStart = Calendar.getInstance();
        Date expenseEntryStartDate = sdf.parse(expenseEntry.incurredDate);
        calStart.setTime(expenseEntryStartDate);        
        int startDay = calStart.get(Calendar.DAY_OF_MONTH);
		
		if(OS.equals("Android")){
			sh.appiumClick(selectors.expenseEndDateField, driver);
			sh.appiumClick(By.xpath("//android.view.View[@text='" + expenseEntry.incurredDay + "']"), driver);
			sh.appiumClick(By.xpath("//android.widget.Button[@text='OK']"), driver);
		}
		if(OS.equals("iOS")){
			sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='Incurred Date']"), driver);	
			driver.findElement(selectors.scrollWheel1).sendKeys(expenseEntry.monthBeginning.replace(" 2013", "").replace("01 ", ""));
			driver.findElement(selectors.scrollWheel2).sendKeys(Integer.toString(startDay));
		}
	}

	private void enterClaimActivity(SeleniumHelper sh, ExpenseEntry expenseEntry, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(OS.equals("Android")){
			sh.appiumClick(selectors.expenseActivityField, driver);
			sh.appiumClickVisibleElement(By.xpath(selectors.expenseActivitySelect.replace(selectors.activity, expenseEntry.activityName)), driver);
		}
//		catch(Exception e){
//			sh.appiumClickVisibleElement(By.xpath(selectors.expenseActivitySelectFirst), driver);
//		}

		if(OS.equals("iOS")){
			sh.appiumClick(selectors.expenseActivityField, driver);
	        sh.waitForElementToBeVisible(By.xpath("//XCUIElementTypeCell[@name='dropdown']"), 30, driver); 
			sh.appiumClick(By.xpath("//XCUIElementTypeCell[@name='dropdown']"), driver);
		}
	}

	private void enterClaimNotes(SeleniumHelper sh, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(!(detail.notes==null)){
			if(OS.equals("Android")){
				sh.scrollMobile(driver);
				sh.appiumClick(selectors.expenseNotesField, driver);
				driver.getKeyboard().sendKeys(detail.notes);
				sh.appiumClick(By.xpath("//android.widget.ImageButton"), driver);						
			}
			if(OS.equals("iOS")){
				try{
					sh.appiumClick(selectors.expenseNotesField, driver);
					driver.getKeyboard().sendKeys(detail.notes);
					sh.appiumClick(selectors.expenseReturnTo, driver);
				}catch(Exception e){
					sh.appiumClick(selectors.expenseReturnTo, driver);
					sh.appiumClick(selectors.expenseNotesField, driver);
					driver.getKeyboard().sendKeys(detail.notes);
					sh.appiumClick(selectors.expenseReturnTo, driver);
				}
			}
		}
	}

	private void enterClaimCategory(SeleniumHelper sh, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors) {
		sh.appiumClick(selectors.expenseCategoryField, driver);
		try{
			sh.appiumClick(By.xpath(selectors.expenseCategorySelect.replace(MobileSelectors.expenseCategory, detail.category)), driver);
		}
		catch(Exception e){
			sh.scrollMobile(driver);
			sh.appiumClick(By.xpath(selectors.expenseCategorySelect.replace(MobileSelectors.expenseCategory, detail.category)), driver);
		}
	}

	private void enterClaimIncurredAmount(SeleniumHelper sh, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(!(detail.amount==null)){
			if(OS.equals("Android")){
				if(!(detail.currency==null)){
					sh.appiumClick(selectors.expenseCurrencyBtn, driver);
					sh.appiumClick(By.xpath(selectors.expenseCurrencySelect.replace(selectors.expenseCurrency, detail.currency)), driver);
					// todo - put this in a catch to scroll up/down if it can't locate the currency
				}
				sh.appiumClick(selectors.expenseIncurredAmountField, driver);
				sh.enterAndroidBackspace(5, driver);
				sh.enterStringIntoMobileField(detail.amount, driver);
				
			}
			if(OS.equals("iOS")){ 
				if(!(detail.currency==null)){
					sh.appiumClick(selectors.expenseCurrencyBtn, driver);
					sh.scrollMobile(driver);
					sh.appiumClick(By.xpath(selectors.expenseCurrencySelect.replace(selectors.expenseCurrency, detail.currency)), driver);
		        	sh.clickAndWait(selectors.expenseIncurredAmountField, selectors.keyboardButton, 5, driver);
				}
				else{
					try{
						sh.appiumClick(selectors.expenseIncurredAmountField, driver);
			        	sh.clickAndWait(selectors.expenseIncurredAmountField, selectors.keyboardButton, 5, driver);
					}
					catch(Exception e){
			        	sh.clickAndWait(selectors.expenseIncurredAmountField, selectors.keyboardButton, 5, driver);
					}
				}
				sh.enterIOSBackspace(5, driver);
				driver.getKeyboard().sendKeys(detail.amount);
				driver.getKeyboard().pressKey(Keys.ENTER);
			}
		}
	}

	private void enterClaimStartAndEndLocation(SeleniumHelper sh, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(!(detail.startLocation==null)){
			if(OS.equals("Android")){
				sh.appiumClick(selectors.expenseStartLocationField, driver);
				driver.getKeyboard().sendKeys(detail.startLocation);
				sh.appiumClick(selectors.expenseEndLocationField, driver);
				driver.getKeyboard().sendKeys(detail.endLocation);
			}
			if(OS.equals("iOS")){
				driver.findElement(selectors.expenseStartLocationField).click();
				driver.getKeyboard().sendKeys(detail.startLocation);
				driver.getKeyboard().pressKey(Keys.ENTER);
				driver.findElement(selectors.expenseEndLocationField).click();
				driver.getKeyboard().sendKeys(detail.endLocation);
				driver.getKeyboard().pressKey(Keys.ENTER);
			}
		}
	}

	private void enterClaimMileageUnits(SeleniumHelper sh, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(!(detail.miles==null)){
			if(OS.equals("Android")){
		        sh.appiumClick(selectors.expenseMileageUnitsField, driver);
				sh.enterAndroidBackspace(5, driver);
		        driver.getKeyboard().sendKeys(detail.miles);
			}
			if(OS.equals("iOS")){
				try{
					sh.clickAndWait(selectors.expenseMileageUnitsField, selectors.keyboardButton, 5, driver);
				}
				catch(Exception e){
//		        	sh.clickAndWait(selectors.expenseMileageUnitsField, selectors.keyboardButton, 5, driver);
				}
				sh.enterIOSBackspace(5, driver);
				driver.getKeyboard().sendKeys(detail.miles);
				driver.getKeyboard().pressKey(Keys.ENTER);
//				sh.appiumClick(selectors.expenseDoneBtn, driver);
			}
		}
	}

	private void enterClaimAttendees(SeleniumHelper sh, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(!(detail.attendees==null)){
			if(OS.equals("Android")){
				sh.appiumClick(selectors.expenseAttendeesField, driver);
				sh.appiumClick(selectors.expenseAddAttendeeBtn, driver);
				driver.getKeyboard().sendKeys(detail.attendees);
				sh.androidType(driver, AndroidKeyCode.KEYCODE_ENTER);
				sh.appiumClick(By.xpath("//android.widget.ImageButton"), driver);
			}
			if(OS.equals("iOS")){
				sh.appiumClick(selectors.expenseAttendeesField, driver);
				sh.appiumClick(By.xpath("//XCUIElementTypeStaticText[@name='Enter Name']"), driver);
				driver.getKeyboard().sendKeys(detail.attendees);
				driver.getKeyboard().sendKeys(Keys.ENTER);
				sh.appiumClick(By.xpath("//XCUIElementTypeButton[@name='Expense Item']"), driver);
			}
		}
	}
	
	static final By recieptAdd = By.xpath("//android.widget.TextView[@text='Receipt']");
	static final By recieptAddImage = By.xpath("android.widget.ImageButton");
	static final By recieptGalleryBtn = By.xpath("android.widget.Button[@text='Gallery']");
	static final By recieptAllowAccess = By.xpath("android.widget.Button[@text='Allow']");

	static final By recieptCompleteFromGallery = By.xpath("android.widget.TextView[@text='Gallery']");
	static final By recieptAlwaysUse = By.xpath("android.widget.Button[@text='Always']");

	static final By recieptShutterButton = By.xpath("//android.widget.ImageView[@content-desc='Shutter button']");
	static final By recieptConfirmImage = By.id("com.android.camera:id/btn_done");

	static final By recieptImageOK = By.xpath("android.widget.Button[@text='OK']");
	

	static final By recieptBackToExpense = By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']");
	
	
	private void enterClaimReceipt(SeleniumHelper sh, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(!(detail.reciept==null)){
			if(OS.equals("Android")){
				sh.scrollMobile(driver);
				sh.appiumClick(recieptAdd, driver);
				sh.appiumClick(recieptAddImage, driver);
				
			}
			if(OS.equals("iOS")){
				
			}
		}
	}
	
	public void validateTaxCode(SeleniumHelper sh, ExpenseDetail detail, AppiumDriver driver, MobileSelectors selectors, String OS) {
		if(!(detail==null)){
			if(OS.equals("Android")){
				WebElement taxCode = (WebElement) driver.findElement(By.xpath(selectors.expenseTaxCode));
				Assert.assertEquals(taxCode.getText(), detail.taxCode);
				sh.appiumClick(By.xpath("//android.widget.Button[@text='Save']"), driver);
				sh.appiumClick(By.xpath("//android.widget.Button[@text='Save']"), driver);
				sh.waitForElementToBeVisible(selectors.submitAll, 30, driver);
			}
			if(OS.equals("iOS")){
				WebElement taxCode = (WebElement) driver.findElement(By.xpath(selectors.expenseTaxCode.replace(selectors.taxCode, detail.taxCode)));
				Assert.assertEquals(taxCode.getText(), detail.taxCode);
		        clickDone(sh, driver, selectors, OS);
			}
		}
	}
}





