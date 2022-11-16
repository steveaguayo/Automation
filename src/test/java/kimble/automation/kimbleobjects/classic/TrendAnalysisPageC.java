package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.List;

import kimble.automation.domain.ExpectedValue;
import kimble.automation.domain.Fact;
import kimble.automation.domain.Opportunity;
import kimble.automation.domain.Timesheet;
import kimble.automation.domain.TrendAnalysis;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;

public class TrendAnalysisPageC {


	public static void navigateToTrendAnalysis(SeleniumHelper sh, String businessUnit){
		sh.goToAllTabsPage();
		sh.clickLink(By.linkText("Business Units"));
		sh.clickGoInListView();
		sh.clickLink(By.linkText("UK Co 6 Scenario 41 (Operating/Trading GBP)"));
		sh.waitForElementToBeClickable(By.xpath("//span[@tip-title='Business Unit Diagnostics']/span[2]"), 20);
		sh.clickLink(By.xpath("//span[@tip-title='Business Unit Diagnostics']/span[2]"));
	}

	static final By datePickerButton = By.cssSelector(".ui-datepicker-trigger");
	static final By marginAmountCard = By.xpath("//div[@fact-enum='MarginAmount']");
	static final By costCard = By.xpath("//div[@fact-enum='Cost']");
	static final By revenueCard = By.xpath("//div[@fact-enum='Revenue']");
	static final By deliveryUtilisationPctCard = By.xpath("//div[@fact-enum='DeliveryUtilisationPct']");
	
	public static void navigateToDay(SeleniumHelper sh, String dateStr) {
		sh.waitForLightningSpinnerToBeHidden();
		JQueryDatePickerC.navigateToDay(sh, datePickerButton, dateStr);
	}
	
	public static void configureAnalysisPage(SeleniumHelper sh, TrendAnalysis trend){
		navigateToDay(sh, trend.date); 
		// set prob/dimension/fact
		sh.clickLink(marginAmountCard);
		sh.clickLink(costCard);
		sh.clickLink(revenueCard);
		sh.clickLink(deliveryUtilisationPctCard);
	}
	
	static final String replaceMe = "";
	static final By factCardLink = By.xpath("//div[normalize-space(text())='"+ replaceMe +"']");
	
	public static void validateTrendAnalysis(SeleniumHelper sh, List<TrendAnalysis> ta){
		for (TrendAnalysis trend : ta) {
			navigateToTrendAnalysis(sh, trend.businessUnitName);
			configureAnalysisPage(sh, trend);
			
			for(Fact f : trend.facts){
				String factBasis = "//div[normalize-space(text())='"+ f.factCard +"']";
				
				if(f.thisValue!= null){
					By factLinkThis = By.xpath(factBasis + "/following-sibling::div[@class='fact-this']/span");
					validateStringValue(sh, factLinkThis, f.thisValue, "assert This: " + f.thisValue);
				}
				if(f.lastValue!= null){
					By factLinkLast = By.xpath(factBasis + "/following-sibling::div[@class='fact-last']/span");
					validateStringValue(sh, factLinkLast, f.lastValue, "assert Last: " + f.lastValue);
				}
				if(f.changeValue!= null){
					By factLinkChange = By.xpath(factBasis + "/following-sibling::div[@class='fact-this']/div/span");
					validateStringValue(sh, factLinkChange, f.changeValue, "assert Change: " + f.changeValue);		
				}	
				if(f.info!= null){
					By factLinkInfo = By.xpath(factBasis + "/following-sibling::div[@class='fact-narrative']");
					validateStringValue(sh, factLinkInfo, f.info, "assert Info: " + f.info);
				}
				if(f.impact!= null){					
					By factLinkImpact = By.xpath(factBasis + "/following-sibling::div[@class='fact-impact-pct']");					
					validateStringValue(sh, factLinkImpact, f.impact, "assert Impact: " + f.impact);
				}
				
			}
		}
	}
	
}
