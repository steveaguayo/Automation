package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.util.Collection;
import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.ActivityAssignment;
import kimble.automation.domain.ActivityAssignmentsMany;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.Invoice;
import kimble.automation.domain.SupplierInvoice;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.Proposal;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.domain.SupplierRequisition;
import kimble.automation.domain.SupplierRequisitionResult;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.helpers.SeleniumHelper;
import kimble.automation.KimbleOneTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.webdriven.commands.GetAllWindowNames;

public class Scenario147Test extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario147")
	public void Scenario147(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	@Override
	public void executeTest() {
		Account supplierAccount = data().accounts.get(0);
		Account account = data().accounts.get(1);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		DeliveryEngagement engagement = salesOpp.deliveryEngagements.get(0);
		List<SupplierRequisition> supplierReq = data().supplierRequisitions;
		List<SupplierRequisitionResult> expectedRes = data().expectedResults.get(0).supplierRequisitions;
			
		String configSetting1 = "Allow Self-Bill Supplier Invoices to be generated without an allocated Supplier Requisition";
		String configSetting2 = "Generate Supplier Invoice Reference for manually created self-billed Supplier Invoices";
		String configSetting3 = "Enable Partial Supplier Credit Notes";
		
		execute(
		   enableSuppInvoiceWithoutReq(null, "enable the setting " + configSetting1, getSH(), configSetting1),
		   enableGenerateSupplierInvoiceRef(null, "enable the setting " + configSetting2, getSH(), configSetting2),
		   disableEnablePartialSuppCreditNotes(null,"disable the setting " + configSetting3, getSH(), configSetting3),
	   	   createAccount(null, "create supplier", getSH(), supplierAccount),
		   createAccount(null, "create account", getSH(), account),
	       createSalesOpportunitiyEngagementsAndElements(null, "create sales opp, engagements and elements", getSH(), account, salesOpp),
		   navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
		   activateEngagements(null, "activate engagements", getSH(), salesOpp),
	       runAllJobs(null, "run jobs", getSH()),
		   navigateToSupplierResourceHomeAndSetSupplierAccount(null, "associate supplier resource with account", getSH(), "US Supplier 1", supplierAccount.name),
		   createSupplierRequisitions(null, "create supplier req", getSH(), salesOpp.name, data().supplierRequisitions),
		   validateSupplierRequisitionResults(null, "postSupplierRequisitionCreation", getSH(), salesOpp.name, supplierReq, expectedRes),
		   enterTime(null, "book time", getSH(),data().timesheets),
		   runAllJobs(null, "run jobs", getSH()),
     	   // supplier invoice 1 - without requisition, fully credited
		   createSupplierInvoice(null, "supplier invoice without requisition", getSH(), data().supplierInvoices.get(0)),
		   createSupplierCreditNotes(null, "create supplier credit note number 1", getSH(), data().supplierInvoices.get(0)),
		  
		   // supplier invoice 2 - without requisition, batch generated from resource list view, fully credited
		   createSupplierInvoice(null, "resource batch generated supplier invoice without requisition", getSH(), data().supplierInvoices.get(1)),
		   createSupplierCreditNotes(null, "create supplier credit note number 2", getSH(), data().supplierInvoices.get(1)),
		 
		   // enable partial supplier credit notes
		   enablePartialSuppCreditNotes(null, "enable the setting " + configSetting3, getSH(), configSetting3),
		   
		   // supplier invoice 3 - with requisition, partially credited
		   createSupplierInvoice(null, "supplier invoice with requisition", getSH(), data().supplierInvoices.get(2)),
		   createSupplierCreditNotes(null, "create partial supplier credit note number 3", getSH(), data().supplierInvoices.get(2)),
		  
		   // supplier invoice 4 - with requisition, batch generated from account list view, partially credited
		   createSupplierInvoice(null, "account batch generated supplier invoice with requisition", getSH(), data().supplierInvoices.get(3)),
		   createSupplierCreditNotes(null, "create partial supplier credit note number 4", getSH(), data().supplierInvoices.get(3)),
		 
		   // disable the 3 configuration settings
		   disableSuppInvoiceWithoutReq(null,"disable the setting " + configSetting1, getSH(), configSetting1),
		   disableGenerateSupplierInvoiceRef(null,"disable the setting " + configSetting2, getSH(), configSetting2),
		   disableEnablePartialSuppCreditNotes(null,"disable the setting " + configSetting3, getSH(), configSetting3)
		);
	}
	
}

