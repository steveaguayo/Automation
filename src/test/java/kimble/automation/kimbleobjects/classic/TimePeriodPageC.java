
package kimble.automation.kimbleobjects.classic;

import java.util.List;

import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TimePeriodPageC extends BasePageC {
		
	private static final String RESET_TIME_PERIODS_VIEWNAME = "ResetTimePeriods";

	private static final String STATUS_LABEL = "Status";
	
	@FindBy(css = "input[name$='save']")
	private WebElement saveButton;
		
	@FindBy(css = "select[title^='View']")
	private WebElement viewSelector;
	
	public TimePeriodPageC(SeleniumHelper seleniumHelperInstance){
		super(seleniumHelperInstance);
	}
		
	public void NavigateToList(){
		theSH.NavigateToList("Time Periods");
	}
		
	public void resetTimePeriods(){
		NavigateToList();
		// in the org there needs to be a view called ResetTimePeriods which is filtered to contain the TimePeriods that
		// need to be reopened upon repeat of the automation run
		// Currently Status of "Closed" and Business Unit Group Name of "Group 2 (Asia)"
		viewSelector.sendKeys(RESET_TIME_PERIODS_VIEWNAME);
		
		List<ListGridItemC> timePeriodLinks = theSH.getEditDelAndOpenLinks();
		
		for (ListGridItemC listGridItem : timePeriodLinks) {
			theSH.goToUrl(listGridItem.editUrl);
			theSH.getElementBasedOnLabel(STATUS_LABEL).clear();
			theSH.getElementBasedOnLabel(STATUS_LABEL).sendKeys("Open");
			saveButton.click();
			theSH.sleep(1000);
		}
	}
}
