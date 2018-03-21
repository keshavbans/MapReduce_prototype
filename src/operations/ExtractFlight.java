package operations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ExtractFlight {

	private final String flightId, duration, sourceAirportCode, destinationAirportCode, departureTime, arrivalTime;
	private String passenger;
    private List<String> passengerList;
    
    public ExtractFlight(String flightId, String sourceAirportCode, String destinationAirportCode, String departureTime, String duration, String passenger) {
       //--------------------------------------  This is required during the mapping --------------------------------------- 
    	this.flightId = flightId;
        this.sourceAirportCode = sourceAirportCode;
        this.destinationAirportCode = destinationAirportCode;
        this.passenger = passenger;
      //------------------------------------- Calculate arrival time --------------------------------------------------
        long departureTimeNumerical = Long.parseLong(departureTime);
        long durationNumerical = Long.parseLong(duration) * 60; // Convert minutes to seconds
        long arrivalTimeNumerical = departureTimeNumerical + durationNumerical;

        //------------------------------------ Time format to HH:mm:ss -----------------------------------------------------
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.departureTime = sdf.format(new Date(departureTimeNumerical*1000L))  + " (GMT)";
        this.arrivalTime = sdf.format(new Date(arrivalTimeNumerical*1000L)) + " (GMT)";
        this.duration =  String.format("%02d:%02d:%02d", durationNumerical / 3600, (durationNumerical % 3600) / 60, (durationNumerical % 60));
    }
    //------------------------------- This is required during the reducing ----------------------------------------------------
    public ExtractFlight(String flightId, String sourceAirportCode, String destinationAirportCode, String departureTime, String arrivalTime, String duration, List<String> passengerList) {
        this.flightId = flightId;
        this.sourceAirportCode = sourceAirportCode;
        this.destinationAirportCode = destinationAirportCode;
        this.passengerList = passengerList;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
    }
    //---------------------------------------------------- Methods -----------------------------------------------------
    public String getFlightId() {
        return flightId;
    }

    public String getSourceAirportCode() {
        return sourceAirportCode;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getPassenger() {
        return passenger;
    }

    public List<String> getPassengers() {
        return passengerList;
    }
    
 //---------------------------------------------------------------------------------------------------------------------------   
}
