package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import kimble.automation.domain.TestState;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;

public class InterfaceRunPageC extends BasePageC {

	static final By interfaceRunNew = By.xpath("//div[1]/div[2]/table/tbody/tr/td[2]/div[3]/div[1]/div/div[1]/form/table/tbody/tr/td[2]/input");
	static final By typeDropdown = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[2]/div/div[2]/div/div[2]/table/tbody/tr/td/select");
	static final By chooseFileButton = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[2]/div/div[2]/div/div[2]/table/tbody/tr[2]/td/input");
	static final By uploadFileSave = By.xpath("//td[@id='j_id0:j_id1:TheForm:j_id63:j_id64:bottom']/input[1]");
	
	public InterfaceRunPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
	
	public void importTimeEntry(SeleniumHelper sh, String file) {
		waitClickable(sh, interfaceRunNew, 20);
		click(sh, interfaceRunNew);
		waitClickable(sh, typeDropdown, 20);
		dropdownSelect(sh, typeDropdown, "TimeEntryImport");
		waitClickable(sh, chooseFileButton, 20);
		click(sh, chooseFileButton);
		inputText(sh, chooseFileButton, file);
		
//	    driver.setFileDetector(new LocalFileDetector());
//	    WebElement upload = driver.findElement(By.id("myfile"));
//	    upload.sendKeys("/Users/sso/the/local/path/to/darkbulb.jpg");
	}
	
	
	static final By interfaceRunGoBtn = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[1]/div/div[3]/table/tbody/tr/td[2]/input[1]");
	static final By interfaceTypeEntryBox = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/form/div[1]/div/div[2]/div/div/table/tbody/tr[1]/td/input") ;
	static final By interfaceDataEntryBox = By.xpath("//table[@class='detailList']/tbody/tr[2]/td/textarea");
	static final String interfaceRunURL = "/InboundInterfaceRunTest";
	
	public static void enterInterfaceRun(SeleniumHelper sh, String interfaceType, String rawJson ){
		
		String id = "";
		
		if(interfaceType.equals("UpsertActivityAssignment")){
			id = getUsageBehaviourRuleId(sh);
			System.out.println(id);
			System.out.println(rawJson.replace("UBR_HERE", id));
		}
		
		sh.navigateToVFPage("InboundInterfaceRunTest");
		sh.waitForElementToBeClickable(interfaceRunGoBtn);
		
		clearAndInputText(sh, interfaceTypeEntryBox, interfaceType);
		clearAndInputText(sh, interfaceDataEntryBox, rawJson.replace("UBR_HERE", id));
		
		sh.clickAndWait(interfaceRunGoBtn, interfaceRunGoBtn, 20);
	}
	
	public static String getUsageBehaviourRuleId(SeleniumHelper sh){
		String id = null;
		sh.goToAllTabsPage();
		sh.clickLastLink("Usage Behaviour Rules");
		sh.closeLightningPopUp();
		sh.clickGoInListView();
		sh.clickLink(By.linkText("Defined Effort between two Dates"));
		
		id = sh.getCurrentUrl().split("id=")[1];
		id = id.split("&")[0];
		System.out.println(id);	
		return id;
	}
	
}
