package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import kimble.automation.domain.TravelRequisition;
import kimble.automation.domain.TravelRequisitionBooking;
import kimble.automation.domain.TravelRequisitionItinerary;
import kimble.automation.helpers.KBy;
import kimble.automation.helpers.SeleniumHelper;
import org.openqa.selenium.By;

public class TravelRequisitionPageC {
	
	static final By 
	newButton = By.name("new"),
	editButton = By.name("edit"),
	bookButton = By.xpath("//input[@value='Book']"),
	saveButton = By.xpath("//input[@value='Save']"),
	cancelButton = By.xpath("//input[@value='Cancel']"),
	submitForApprovalButton = By.xpath("//input[@value='Submit For Approval']"),
	amendBookingButton = By.xpath("//input[@value='Amend Booking']"),
	requestItineraryButton = By.xpath("//input[@value='Request Itinerary']"),
	updateItineraryOptionsButton = By.xpath("//input[@value='Update Itinerary Options']"),
	submitForItineraryApprovalButton = By.xpath("//input[@value='Submit For Itinerary Approval']"), 
	requestItinerarylButton = By.xpath("//input[@value='Request Itinerary']"), 

	//resourceInput = By.name("j_id0:j_id1:TheForm:j_id88:j_id92:j_id93:j_id96"),
	resourceInput = By.xpath("//label[contains(text(),'Resource')]/parent::th/following-sibling::td//span//input"),
	nameInput = By.xpath("//label[contains(text(),'Name')]/parent::th/following-sibling::td//div//input"),
	fromDateInput = By.xpath("//label[contains(text(),'From Date')]/parent::th/following-sibling::td[1]//span//input"),
	toDateInput = By.xpath("//label[contains(text(),'To Date')]/parent::th/following-sibling::td//span//input"),
	activityDropdown = By.xpath("//label[contains(text(),'Activity')]/parent::th/following-sibling::td//select"),
	
	addFlight = By.xpath("//h2[text()='Add Flight']"),
	addHotel = By.xpath("//h2[text()='Add Hotel']"),
	addCarHire = By.xpath("//h2[text()='Add Car Hire']"),
	remove = By.linkText("Remove"),
	
// flight
	departureAirport = By.xpath("//td[normalize-space(text())='Departure Airport']/following-sibling::td/select"),
	destinationAirport = By.xpath("//td[normalize-space(text())='Destination Airport']/following-sibling::td/select"),
	departureTime = By.xpath("//td[normalize-space(text())='Departure Time']/following-sibling::td/select"),
	returnFromAirport = By.xpath("//td[normalize-space(text())='Return From Airport']/following-sibling::td/select"),
	returnTime = By.xpath("//td[normalize-space(text())='Return Time']/following-sibling::td/select"),
	departureDate = By.xpath("//td[normalize-space(text())='Departure Date']/following-sibling::td//input"),
	returnDate = By.xpath("//td[normalize-space(text())='Return Date']/following-sibling::td//input"),
// hotel
	checkIn = By.xpath("//div[contains(@class, 'hotel')]/following-sibling::div[1]//tr/td[2]/span[1][contains(@class, 'dateInput')]/input"),
	checkOut = By.xpath("//div[contains(@class, 'hotel')]/following-sibling::div[1]//tr/td[3]/span[1][contains(@class, 'dateInput')]/input"),
	preference = By.xpath("//div[contains(@class, 'hotel')]/following-sibling::div[1]//tr/td[4]/input"),
// car
	pickUp = By.xpath("//td[normalize-space(text())='Pick-up']/following-sibling::td/input"),
	pickUpTime = By.xpath("//td[normalize-space(text())='Pick-up Time']/following-sibling::td/input"),
	dropOff = By.xpath("//td[normalize-space(text())='Drop-off']/following-sibling::td/input"),
	dropOffTime = By.xpath("//td[normalize-space(text())='Drop-off Time']/following-sibling::td/input"),
	pickUpDate = By.xpath("//td[normalize-space(text())='Pick-up Date']/following-sibling::td//input"),
	dropOffDate = By.xpath("//td[normalize-space(text())='Drop-off Date']/following-sibling::td//input"),
	
	newBreadcrumb = By.cssSelector("div.breadcrumb-item-link.breadcrumb-heading");
		
	public static void createNewTravelRequisition(SeleniumHelper sh, TravelRequisition travelReq) {

		executeSequenceWithRetry(sh, 3, () -> {
			sh.NavigateToList("Travel Requisitions");
			sh.clickAndWait(newButton, saveButton, 10);
			
			clearAndInputText(sh, resourceInput, "");
			clearAndInputText(sh, fromDateInput, travelReq.fromDate);
			clearAndInputText(sh, nameInput, travelReq.name);
			clearAndInputText(sh, toDateInput, travelReq.toDate);
			
			sh.clickLink(newBreadcrumb);
	
			clearAndInputText(sh, resourceInput, travelReq.resource);
			sh.clickLink(newBreadcrumb);
			sh.sleep(3000);
			dropdownSelect(sh, activityDropdown, travelReq.activity);
		});
		
		if(travelReq.flight==true){
			sh.waitForElementToBeClickable(addFlight);
			sh.clickAndWait(addFlight, remove, 20);
			dropdownSelect(sh, departureAirport, travelReq.departureAirport);	
			dropdownSelect(sh, destinationAirport, travelReq.destinationAirport);	
			dropdownSelect(sh, departureTime, travelReq.departureTime);	
			dropdownSelect(sh, returnFromAirport, travelReq.returnFromAirport);	
			dropdownSelect(sh, returnTime, travelReq.returnTime);	
			clearAndInputText(sh, departureDate, travelReq.departureDate);
			clearAndInputText(sh, returnDate, travelReq.returnDate);
		}
		
		if(travelReq.hotel==true){
			sh.clickAndWait(addHotel, checkIn, 10);
			clearAndInputText(sh, checkIn, travelReq.checkIn);
			clearAndInputText(sh, checkOut, travelReq.checkOut);
			clearAndInputText(sh, preference, travelReq.preference);
		}
		
		if(travelReq.car==true){
			sh.clickAndWait(addCarHire, pickUp, 10);
			clearAndInputText(sh, pickUp, travelReq.pickUp);	
			clearAndInputText(sh, pickUpTime, travelReq.pickUpTime);	
			clearAndInputText(sh, dropOff, travelReq.dropOff);	
			clearAndInputText(sh, dropOffTime, travelReq.dropOffTime);	
			clearAndInputText(sh, pickUpDate, travelReq.pickUpDate);
			clearAndInputText(sh, dropOffDate, travelReq.dropOffDate);
			
		}
		sh.clickAndWait(saveButton, amendBookingButton, 30);
	}
	
	public static void requestItinerary(SeleniumHelper sh){
		sh.clickAndWait(requestItineraryButton, updateItineraryOptionsButton, 30);
	}
	
	public static void submitRequestForApproval(SeleniumHelper sh){
		sh.clickAndWait(submitForApprovalButton, cancelButton, 30);
	}
	
	static final By 
	//flight						
	flight_2_addOption =  By.xpath("//td[@id='j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id122']//a[.='Add Option']"),
	flight_1_ItineraryOptions =  By.name("j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id125:0:j_id135"),
	flight_2_ItineraryOptions =  By.name("j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id125:1:j_id135"),
	flight_1_currency =  By.xpath("//td[@id='j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id122']/table/tbody/tr[1]/td[3]/select"),
	flight_2_currency =  By.xpath("//td[@id='j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id122']/table/tbody/tr[5]/td[3]/select"),
	flight_1_amount =  By.id("j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id125:0:j_id140"),
	flight_2_amount =  By.id("j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id125:1:j_id140"),
	flight_1_DepartureAirline =  By.xpath("//select[@id='j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id125:0:j_id142']"),
	flight_2_DepartureAirline =  By.xpath("//select[@id='j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id125:1:j_id142']"),
	flight_1_ReturnAirline =  By.xpath("//select[@id='j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id125:0:j_id144']"),
	flight_2_ReturnAirline =  By.xpath("//select[@id='j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id125:1:j_id144']"),
	flight_1_RequiresApproval =  By.id("j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id125:0:j_id146"),
	flight_2_RequiresApproval =  By.id("j_id0:j_id1:TheForm:j_id91:j_id113:0:j_id125:1:j_id146"),
	
	//hotel
	hotel_2_addOption =  By.xpath("//td[@id='j_id0:j_id1:TheForm:j_id91:j_id158:0:j_id167']//a[.='Add Option']"),
	hotel_1_ItineraryOptions =  By.name("j_id0:j_id1:TheForm:j_id91:j_id158:0:j_id170:0:j_id180"),
	hotel_2_ItineraryOptions =  By.name("j_id0:j_id1:TheForm:j_id91:j_id158:0:j_id170:1:j_id180"),
	hotel_1_currency =  By.xpath("//td[@id='j_id0:j_id1:TheForm:j_id91:j_id158:0:j_id167']/table/tbody/tr[1]/td[3]/select"),
	hotel_2_currency =  By.xpath("//td[@id='j_id0:j_id1:TheForm:j_id91:j_id158:0:j_id167']/table/tbody/tr[3]/td[3]/select"),
	hotel_1_amount =  By.id("j_id0:j_id1:TheForm:j_id91:j_id158:0:j_id170:0:j_id185"),
	hotel_2_amount =  By.id("j_id0:j_id1:TheForm:j_id91:j_id158:0:j_id170:1:j_id185"),
	hotel_1_RequiresApproval =  By.id("j_id0:j_id1:TheForm:j_id91:j_id158:0:j_id170:0:j_id187"),
	hotel_2_RequiresApproval =  By.id("j_id0:j_id1:TheForm:j_id91:j_id158:0:j_id170:1:j_id187"),
			
	//car hire	
	carHire_2_addOption =  By.xpath("//td[@id='j_id0:j_id1:TheForm:j_id91:j_id275:0:j_id284']//a[.='Add Option']"),
	carHire_1_ItineraryOptions =  By.name("j_id0:j_id1:TheForm:j_id91:j_id275:0:j_id287:0:j_id297"),
	carHire_2_ItineraryOptions =  By.name("j_id0:j_id1:TheForm:j_id91:j_id275:0:j_id287:1:j_id297"),
	carHire_1_currency =  By.xpath("//td[@id='j_id0:j_id1:TheForm:j_id91:j_id275:0:j_id284']/table/tbody/tr[1]/td[3]/select"),
	carHire_2_currency =  By.xpath("//td[@id='j_id0:j_id1:TheForm:j_id91:j_id275:0:j_id284']/table/tbody/tr[3]/td[3]/select"),
	carHire_1_amount =  By.id("j_id0:j_id1:TheForm:j_id91:j_id275:0:j_id287:0:j_id302"),
	carHire_2_amount =  By.id("j_id0:j_id1:TheForm:j_id91:j_id275:0:j_id287:1:j_id302"),
	carHire_1_RequiresApproval =  By.id("j_id0:j_id1:TheForm:j_id91:j_id275:0:j_id287:0:j_id304"),
	carHire_2_RequiresApproval =  By.id("j_id0:j_id1:TheForm:j_id91:j_id275:0:j_id287:1:j_id304");


	public static void updateItineraryOptions(SeleniumHelper sh, String travelReqName, TravelRequisitionItinerary itin){
		
		sh.waitForElementToBeClickable(saveButton);

		if(itin.itineraryOptionsAndCost.contains("flight1")){
			clearAndInputText(sh, flight_1_ItineraryOptions, itin.itineraryOptionsAndCost);
			clearAndInputText(sh, flight_1_currency, itin.currency);
			clearAndInputText(sh, flight_1_amount, itin.amount);
			dropdownSelect(sh, flight_1_DepartureAirline, itin.departureAirline);	
			dropdownSelect(sh, flight_1_ReturnAirline, itin.returnAirline);	
			checkboxSelect(sh, flight_1_RequiresApproval, itin.requiresApproval);
		}
		if(itin.itineraryOptionsAndCost.contains("flight2")){
			sh.clickAndWait(flight_2_addOption, flight_2_DepartureAirline, 10);
			clearAndInputText(sh, flight_2_ItineraryOptions, itin.itineraryOptionsAndCost);
			clearAndInputText(sh, flight_2_currency, itin.currency);
			clearAndInputText(sh, flight_2_amount, itin.amount);
			dropdownSelect(sh, flight_2_DepartureAirline, itin.departureAirline);	
			dropdownSelect(sh, flight_2_ReturnAirline, itin.returnAirline);	
			checkboxSelect(sh, flight_2_RequiresApproval, itin.requiresApproval);
		}
		
		if(itin.itineraryOptionsAndCost.contains("hotel1")){
			clearAndInputText(sh, hotel_1_ItineraryOptions, itin.itineraryOptionsAndCost);
			clearAndInputText(sh, hotel_1_currency, itin.currency);
			clearAndInputText(sh, hotel_1_amount, itin.amount);
			checkboxSelect(sh, hotel_1_RequiresApproval, itin.requiresApproval);
		}
		if(itin.itineraryOptionsAndCost.contains("hotel2")){
			sh.clickAndWait(hotel_2_addOption, hotel_2_amount, 10);
			clearAndInputText(sh, hotel_2_ItineraryOptions, itin.itineraryOptionsAndCost);
			clearAndInputText(sh, hotel_2_currency, itin.currency);
			clearAndInputText(sh, hotel_2_amount, itin.amount);
			checkboxSelect(sh, hotel_2_RequiresApproval, itin.requiresApproval);
		}

		if(itin.itineraryOptionsAndCost.contains("carHire1")){
			clearAndInputText(sh, carHire_1_ItineraryOptions, itin.itineraryOptionsAndCost);
			clearAndInputText(sh, carHire_1_currency, itin.currency);
			clearAndInputText(sh, carHire_1_amount, itin.amount);
			checkboxSelect(sh, carHire_1_RequiresApproval, itin.requiresApproval);
		}
		if(itin.itineraryOptionsAndCost.contains("carHire2")){
			sh.clickAndWait(carHire_2_addOption, carHire_2_amount, 10);
			clearAndInputText(sh, carHire_2_ItineraryOptions, itin.itineraryOptionsAndCost);
			clearAndInputText(sh, carHire_2_currency, itin.currency);
			clearAndInputText(sh, carHire_2_amount, itin.amount);
			checkboxSelect(sh, carHire_2_RequiresApproval, itin.requiresApproval);
		}
	}

	public static void saveItineraryOptions(SeleniumHelper sh){
		sh.clickAndWait(saveButton, updateItineraryOptionsButton, 10);
		sh.clickAndWait(submitForItineraryApprovalButton, requestItinerarylButton, 10);
	}

	static final By
	flight_1_Select = By.xpath("//textarea[normalize-space(text())='flight1']/parent::td/parent::tr//input"),
	hotel_1_Select = By.xpath("//textarea[normalize-space(text())='hotel1']/parent::td/parent::tr//input"),
	carHire_1_Select = By.xpath("//textarea[normalize-space(text())='carHire1']/parent::td/parent::tr//input"),
	
	flight_1_BookNow = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/div[1]/form/div[2]/div/div/div[1]/span/div[2]/table/tbody/tr/td[5]/a"),
	hotel_1_BookNow = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/div[1]/form/div[2]/div/div/div[1]/span/div[4]/table/tbody/tr/td[5]/a"),
	carHire_1_BookNow = By.xpath("//div[1]/div[2]/table/tbody/tr/td/div[2]/div[1]/form/div[2]/div/div/div[1]/span/div[6]/table/tbody/tr/td[5]/a"),

	bookNowSupplier = By.xpath("//div[@id='supplieraccount-reqd']//select"),
	bookNowCurrency = By.xpath("//label[normalize-space(text())='Currency']/parent::th/following-sibling::td//select"),
	bookNowNetAmount = By.xpath("//table/tbody/tr/td/div[2]/form/div[2]/div/div[1]/div/div/table/tbody/tr[6]/td/input"),
	bookNowBookingReference = By.xpath("//table/tbody/tr/td/div[2]/form/div[2]/div/div[1]/div/div/table/tbody/tr[7]/td/div/input");
	
	public static void selectTravelOptions(SeleniumHelper sh, TravelRequisitionBooking booking){
		executeSequenceWithRetry(sh, 3, () -> {
			sh.waitForElementToBeClickable(amendBookingButton);
			radioButtonSelect(sh, flight_1_Select, true);
			sh.waitMilliseconds(2000);
			radioButtonSelect(sh, hotel_1_Select, true);
			sh.waitMilliseconds(2000);
			radioButtonSelect(sh, carHire_1_Select, true);
			sh.waitMilliseconds(2000);
		});
		
		sh.sleep(1000);
		sh.clickAndWait(submitForApprovalButton, By.linkText("Book Now"), 10);
		
		executeSequenceWithRetry(sh, 3, () -> {
			sh.clickAndWait(flight_1_BookNow, bookButton, 10);
			dropdownSelect(sh, bookNowSupplier, booking.flightSupplier);
			dropdownSelect(sh, bookNowCurrency, booking.flightCurrency);
			clearAndInputText(sh, bookNowNetAmount, booking.flightNetAmount);
			clearAndInputText(sh, bookNowBookingReference, booking.flightBookingReference);
			sh.clickLink(bookButton);
			sh.sleep(5000);
		});
		
		executeSequenceWithRetry(sh, 3, () -> {
			// book now hotel
			sh.refreshBrowser();
			sh.clickAndWait(hotel_1_BookNow, bookButton, 10);
			dropdownSelect(sh, bookNowSupplier, booking.hotelSupplier);
			clearAndInputText(sh, bookNowCurrency, booking.hotelCurrency);
			clearAndInputText(sh, bookNowNetAmount, booking.hotelNetAmount);
			clearAndInputText(sh, bookNowBookingReference, booking.hotelBookingReference);
			sh.clickLink(bookButton);
			sh.sleep(5000);
		});
		
		executeSequenceWithRetry(sh, 3, () -> {
			// book now carHire
			sh.refreshBrowser();
			sh.clickAndWait(carHire_1_BookNow, bookButton, 10);
			dropdownSelect(sh, bookNowSupplier, booking.carHireSupplier);
			dropdownSelect(sh, bookNowCurrency, booking.carHireCurrency);
			clearAndInputText(sh, bookNowNetAmount, booking.carHireNetAmount);
			clearAndInputText(sh, bookNowBookingReference, booking.carHireBookingReference);
			sh.clickLink(bookButton);
			sh.sleep(5000);
		});
		
	}








}
