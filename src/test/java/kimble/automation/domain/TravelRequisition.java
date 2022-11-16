package kimble.automation.domain;

import java.util.List;

public class TravelRequisition {
	public String resource;
	public String fromDate;
	public String activity;
	public String name;
	public String toDate;
	
	public boolean flight = false;
	public String departureAirport;
	public String destinationAirport;
	public String departureDate;
	public String departureTime;
	public String returnFromAirport;
	public String returnDate;
	public String returnTime;
	
	public boolean hotel = false;
	public String checkIn;
	public String checkOut;
	public String preference;
	
	public boolean car = false;
	public String pickUp;
	public String pickUpDate;
	public String pickUpTime;
	public String dropOff;
	public String dropOffDate;
	public String dropOffTime;
	public List<TravelRequisitionItinerary> travelRequisitionItineraryEdit;
	public List<TravelRequisitionBooking> travelRequisitionBooking;

}
