package objectives;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import mapReduce.KeyValue;
import operations.ExtractFlight;

public class Task2 {
	
	private final List<KeyValue> flights;

    public Task2(List<KeyValue> flights) {
        this.flights = flights;
    }
  //--------------------------------Output to Text File---------------------------------------------
    public void TextFileOut() throws FileNotFoundException {

        String fileName = "outputFiles\\Task2.txt";
        PrintWriter out = new PrintWriter(fileName);

        out.println("\nTask 2");
        out.println("-------------------------");
        out.println("Flights List");
        out.println("-------------------------\n");

        for (KeyValue keyValue : flights) {
            String flightId = (String) keyValue.getKey();
            ExtractFlight flight = (ExtractFlight) keyValue.getValue();
//----------------------------- Extract all the flight information --------------------------------
            out.println("\n---------------------------------------------");
            out.println("Flight ID: " + flightId);
            out.println("---------------------------------------------");
            out.println("Departure time: " + flight.getDepartureTime());
            out.println("---------------------------------------------");
            out.println("Arrival time: " + flight.getArrivalTime());
            out.println("---------------------------------------------");
            out.println("Source airport IATA/FAA code: " + flight.getSourceAirportCode());
            out.println("---------------------------------------------");
            out.println("Destination airport IATA/FAA code: " + flight.getDestinationAirportCode());
            out.println("---------------------------------------------");
            out.println("Flight Duration: " + flight.getDuration());
            out.println("---------------------------------------------");

            out.println("\nPassenger list:");

//-------------------------------- Print each passenger on the flight --------------------------------
            for (String passenger : flight.getPassengers()) {
                out.println("  " + passenger);
            }

            out.println("");
        }
        out.println("------------- End of Task2 ---------------");
        out.close();
    }
//--------------------------------- Console Output --------------------------------------------
    public void ConsoleOut() {
    	System.out.println("\nTask 2");
        System.out.println("-------------------------");
        System.out.println("List of Flights");
        System.out.println("-------------------------");

        for (KeyValue keyValue : flights) {
            String flightId = (String) keyValue.getKey();
            ExtractFlight flight = (ExtractFlight) keyValue.getValue();
            System.out.println("\n---------------------------------------------");
            System.out.println("Flight ID: " + flightId);
            System.out.println("---------------------------------------------");
            System.out.println("Departure time: " + flight.getDepartureTime());
            System.out.println("---------------------------------------------");
            System.out.println("Arrival time: " + flight.getArrivalTime());
            System.out.println("---------------------------------------------");;
            System.out.println("Source airport IATA/FAA code: " + flight.getSourceAirportCode());
            System.out.println("---------------------------------------------");
            System.out.println("Destination airport IATA/FAA code: " + flight.getDestinationAirportCode());
            System.out.println("---------------------------------------------");
            System.out.println("Flight Duration: " + flight.getDuration());
            System.out.println("---------------------------------------------");
            System.out.println("\nPassenger list:");

//-------------------------------- Print each passenger on the flight --------------------------------
            for (String passenger : flight.getPassengers()) {
                System.out.println("  " + passenger);
            }

            
        }
        System.out.print("\n-----------End of Task2-------------");
    }
//-----------------------------------------------------------------------------------------------
}
