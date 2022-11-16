package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.ExpectedResult;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.domain.SupplierInvoice;
import kimble.automation.domain.SupplierRequisition;
import kimble.automation.domain.SupplierRequisitionResult;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.kimbleobjects.classic.DeliveryElementSupplierRequisitionsPageC;
import kimble.automation.KimbleOneTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class Scenario126Test extends KimbleOneTest {

	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario126")
	public void Scenario126(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() throws Exception, ParseException {
		SeleniumHelper sh = getSH();
    	WebDriver wd = sh.getWD();     	
		WebDriverWait wait = new WebDriverWait(wd, 25);
		
		Account supplierAccount = data().accounts.get(0);
		Account normalAccount = data().accounts.get(1);
		SalesOpportunity salesOpp = normalAccount.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		List<SupplierRequisition> supplierReq = data().supplierRequisitions;
		
		List<SupplierRequisitionResult> expectedRes = data().expectedResults.get(0).supplierRequisitions;
		List<SupplierRequisitionResult> expectedResFinal = data().expectedResults.get(1).supplierRequisitions;
		
		List<SupplierInvoice> supplierInvoiceResult = data().expectedResults.get(1).supplierInvoices;
		
		String configSetting1 = "Allow Self-Bill Supplier Invoices to be generated without an allocated Supplier Requisition";
		String configSetting2 = "Generate Supplier Invoice Reference for manually created self-billed Supplier Invoices";
		
		execute(
			// added to check that the settings are disabled before starting the scenario - the below settings are enabled as part of scenario 147
			disableSuppInvoiceWithoutReq(null,"disable the setting " + configSetting1, getSH(), configSetting1),
		    disableGenerateSupplierInvoiceRef(null,"disable the setting " + configSetting2, getSH(), configSetting2),
				
			createAccount(null, "create supplier account", getSH(), supplierAccount),
			createAccount(null, "create normal account", getSH(), normalAccount),
			createSalesOpportunitiyEngagementsAndElements(null, "create sales opp, engagements and elements", getSH(), normalAccount, salesOpp),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			activateEngagements(null, "activate engagements", getSH(), salesOpp),				
			createSupplierRequisitions(null, "raise requisition planned cost", getSH(), salesOpp.name, data().supplierRequisitions),	
														
			validateSupplierRequisitionResults(null, "postSupplierRequisitionCreation", getSH(), salesOpp.name, supplierReq, expectedRes),
						
			enterTime(null, "book time", getSH(), data().timesheets),
			completeMilestonesStage(null, "complete the milestone", getSH(), salesOpp),
			completeAnnuityPeriods(null, "complete annuity periods", getSH(), salesOpp.name, engagement),			
			runAllJobs(null, "run jobs before creating supplier invoices", getSH()),						
			createSupplierInvoices(null, "create supplier invoices", getSH(), data().supplierInvoices),
				
			validateSupplierRequisitionResults(null, "postSupplierRequisitionInvoiceCreation", getSH(), salesOpp.name, supplierReq, expectedResFinal),			
			validateSupplierInvoices(null, "postSupplierRequisitionInvoiceCreation", getSH(), supplierInvoiceResult)
		
		);
	}
}