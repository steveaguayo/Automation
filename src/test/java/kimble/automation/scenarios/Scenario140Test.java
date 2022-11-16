package kimble.automation.scenarios;


import static kimble.automation.scenarios.Stages.*;

import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.domain.TravelRequisition;
import kimble.automation.domain.TravelRequisitionBooking;
import kimble.automation.domain.TravelRequisitionItinerary;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

public class Scenario140Test extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario140")
	public void Scenario140(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
		
		Account supplierAccount = data().accounts.get(0);
		Account account = data().accounts.get(1);
		
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		TravelRequisition travelReq1 = data().travelRequisitions.get(0);
		TravelRequisition travelReq2 = data().travelRequisitions.get(1);
		List<TravelRequisitionItinerary> itin = travelReq1.travelRequisitionItineraryEdit;
		TravelRequisitionBooking booking = travelReq1.travelRequisitionBooking.get(0);

		String configSetting = "Enable Itinerary workflow for Travel Requisition process";
		
		execute(
			enableTravelReqConfigSetting(null, "enable config setting: " + configSetting, getSH(), configSetting),
	  		createAccount(null, "create supplier account", getSH(), supplierAccount),
	  		createAccount(null, "create normal account", getSH(), account),
	  		createSalesOpportunitiyEngagementsAndElements(null, "create sales opp, engagements and elements", getSH(), account, salesOpp),
	  		navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
	  		activateEngagements(null, "activate engagements", getSH(), salesOpp),
			addSupplierProductsToAccount(null, "create supplier account products", getSH(), supplierAccount.supplierProducts),
 			validateForecasts(null, "postSetup", getSH(), data()),	
 			createTravelRequisitions(null, "create travel req", getSH(), travelReq1),
 			requestItineraryForRequisition(null, "request itinerary", getSH()),
 			updateTravelRequestItinerary(null, "update itinerary", getSH(), travelReq1, itin),
 			bookTravelRequisition(null, "book travel req", getSH(), travelReq1, booking),
 			validateForecasts(null, "postFirstRequisition", getSH(), data()),
			disableTravelReqConfigSetting(null, "enable config setting: " + configSetting, getSH(), configSetting),
			createTravelRequisitions(null, "create travel req", getSH(), travelReq2),
			submitTravelRequisitionRequestForApproval(null, "submit travel request for approval", getSH()),
			enableTravelReqConfigSetting(null, "enable config setting: " + configSetting, getSH(), configSetting)	
		);
	}
}
