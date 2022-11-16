package kimble.automation.scenarios;



import java.util.List;

import kimble.automation.domain.Account;
import kimble.automation.domain.DeliveryEngagement;
import kimble.automation.domain.KimbleData;
import kimble.automation.domain.SalesOpportunity;
import kimble.automation.kimbleobjects.classic.PagesC;
import kimble.automation.KimbleOneTest;

import org.testng.annotations.Test;

import static kimble.automation.scenarios.Stages.*;

public class Scenario118Test extends KimbleOneTest {
	
	@Test(groups = {"ScenarioTests"}, dataProvider = "yamlDataProvider", testName = "Scenario118")
	public void Scenario118(KimbleData loginCredentials, KimbleData kimbleTestData) throws Exception {
		executeScenario(loginCredentials, kimbleTestData);		
	}
	
	public void executeTest() {
		Account account = data().accounts.get(0);
		SalesOpportunity salesOpp = account.salesOpportunities.get(0);
		List<DeliveryEngagement> engagements = salesOpp.deliveryEngagements;
		
		execute(
			createAccountsAndSalesOpportunities(null, "create accounts and sales opps", getSH(), data().accounts),
			
			// Create, edit, delete and validate risks before winning the opportunity
			navigateMenu(null, "navigate to risk sales", getSH(), PagesC.RISKSSALES),
			createRisks(null, "create 'note only' technical risk", getSH(), salesOpp.risks),
			validateRisks(null, "validate : create 'note only' technical risk", getSH(), data()),
			navigateMenu(null, "navigate to proposal risk dashboard", getSH(), PagesC.PROPOSALRISKDASHBOARD),
			validateRiskDashboard(null, "validate dashboard : create 'note only' technical risk", getSH(), data()),
			createRisksInDashboard(null, "create 'low' technical risk", getSH(), salesOpp.risks),
			validateRiskDashboard(null, "validate : create 'low' technical risk", getSH(), data()),
			createRisksInDashboard(null, "create 'high' commercial risk", getSH(), salesOpp.risks),
			validateRiskDashboard(null, "validate : create 'high' commercial risk", getSH(), data()),
			createRisksInDashboard(null, "create 'critical' management risk", getSH(), salesOpp.risks),
			validateRiskDashboard(null, "validate : create 'critical' management risk", getSH(), data()),
			navigateMenu(null, "navigate to risk sales 2", getSH(), PagesC.RISKSSALES),
			deleteRisks(null, "delete 'critical' management risk", getSH(), salesOpp.risks),
			validateRisks(null, "validate : delete 'critical' management risk", getSH(), data()),
			
			// Create, edit, delete and validate the engagement and it's elements in the OCS screens before winning the opportunity
			navigateMenu(null, "navigate to proposal items", getSH(), PagesC.PROPOSALITEMS),
			addAllOptionalDeliveryElementsInOcs(null, "add all optional delivery elements", getSH(), engagements),
			addDeliveryElementsInOcs(null, "add custom delivery elements", getSH(), engagements),
			validateSalesOpportunitySummaryFieldsOcs(null, "validate sales opportunity parts before configuration", getSH(), data().expectedResults),
			configureDeliveryEngagementsInOcs(null, "config engagements elements", getSH(), engagements),
			validateSalesOpportunityInOcs(null, "validate sales opportunity after configuration", getSH(), data().expectedResults),
			deleteDeliveryElementsInOcs(null, "delete delivery elements", getSH(), engagements),
			// Here we are doing the pre-win performance analysis validation
			validateForecasts(null, "validate sales opportunity forecast before winning", getSH(), data()),
			
			// Win the opportunity
			navigateFromAnywhereToSalesOpp(null, "navigate back to proposal", getSH(), salesOpp.name),
			navigateAndWinOpportunity(null, "win opportunity", getSH(), salesOpp),
			
			// Confirm that the risks that were created before winning the opportunity have been copied over to the delivery group
			navigateFromProposalToRisksDelivery(null, "navigate to risks delivery", getSH(), engagements.get(0).name),
			validateRisksExist(null, "validate risks exist", getSH(), salesOpp.risks, 
					"create 'note only' technical risk",
					"create 'low' technical risk",
					"create 'high' commercial risk",
					"create 'critical' management risk"),
					
			// Edit, delete and validate risks in the delivery group
			editRisks(null, "update commercial risk to 'critical' management risk", getSH(), salesOpp.risks),
			validateRisks(null, "validate : update commercial risk to 'critical' management risk", getSH(), data()),
			navigateMenu(null, "navigate to delivery group risk dashboard", getSH(), PagesC.DELIVERYGROUPRISKDASHBOARD),
			validateRiskDashboard(null, "validate dashboard : update commercial risk to 'critical' management risk", getSH(), data()),
			navigateMenu(null, "navigate to risks delivery 2", getSH(), PagesC.RISKSDELIVERY),
			deleteRisks(null, "delete 'critical' management risk again", getSH(), salesOpp.risks),
			validateRisks(null, "validate : delete 'critical' management risk again", getSH(), data()),
			
			// Final performance analysis validation to complete the scenario
			validateForecasts(null, "validate sales opportunity forecast after winning and editing risks", getSH(), data())
		);
	}
	
}
