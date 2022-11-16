package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryElement;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.ExpectedResult;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.domain.SupplierInvoice;
import kimble.automation.domain.SupplierRequisition;
import kimble.automation.domain.SupplierRequisitionResult;
import kimble.automation.domain.Timesheet;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.kimbleobjects.classic.DeliveryElementSupplierRequisitionsPageC;
import kimble.automation.KimbleOneTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class Scenario130Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario130")
	public void Scenario130(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		SeleniumHelper sh = getSH();
    	WebDriver wd = sh.getWD();     	
		WebDriverWait wait = new WebDriverWait(wd, 25);
		
		Account account_1 = data().accounts.get(0);
		SalesOpportunity salesOpp_1 = account_1.salesOpportunities.get(0);
		DeliveryEngagement engagement_1 = salesOpp_1.deliveryEngagements.get(0);
		Account account_2 = data().accounts.get(1);
		SalesOpportunity salesOpp_2 = account_2.salesOpportunities.get(0);
		DeliveryEngagement engagement_2 = salesOpp_2.deliveryEngagements.get(0);
		
		execute(		
			createAccountsSalesOppsEngagementsAndElements(null, "create account, sales opp, engagement, element and assignment", getSH(), data().accounts),
//  Test 1									
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp_1),
			activateEngagements(null, "activate engagements", getSH(), salesOpp_1),															
								
//	Test 2				
			editInvoiceTaxCode(null, "edit tax code for annuities", getSH(), engagement_2, engagement_2.deliveryElements.get(2), 1),
			editInvoiceTaxCode(null, "edit tax code for fixed price", getSH(), engagement_2, engagement_2.deliveryElements.get(1), 2),
			editInvoiceTaxCode(null, "edit tax code for t&m", getSH(), engagement_2, engagement_2.deliveryElements.get(0), 4),
			
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp_2),
			activateEngagements(null, "activate engagements", getSH(), salesOpp_2),	
			completeMilestonesStage(null, "complete milestones", getSH(), salesOpp_2),
			completeAnnuityPeriods(null, "navigate to engagement to complete annuities and create revenue adjustments", getSH(), salesOpp_2.name, engagement_2),

// time and invoices for both Tests
			runAllJobs(null, "run jobs before creating timesheets", getSH()),
			enterTime(null, "firstTimeBooking", getSH(), data().timesheets),
			validateForecasts(null, "postTimeEntry", getSH(), data()),			
			setPurchaseOrderRules(null, "set the purchase order rules", getSH(), salesOpp_2.name, engagement_2),	
			setPurchaseOrderRules(null, "set the purchase order rules", getSH(), salesOpp_1.name, engagement_1),	
			generateInvoices(null, "create invoice for Test 2", getSH(), data().invoices, true)	
		);
	}
}