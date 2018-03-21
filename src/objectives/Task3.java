package objectives;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import mapReduce.KeyValue;
import operations.ExtractFlight;

public class Task3 {
	private final List<KeyValue> flights;

    public Task3(List<KeyValue> flights) {
        this.flights = flights;
    }
 //------------------------------------ Output to Test Files ----------------------------------------------
    public void TextFileOut() throws FileNotFoundException {

        String fileName = "outputFiles\\Task3.txt";

        PrintWriter out = new PrintWriter(fileName);

        out.println("\nTask 3");
        out.println("-------------------------");
        out.println("\nPassengers on each flight");
        out.println("-------------------------");
        out.println();

        for (KeyValue flightKV : flights) {
            ExtractFlight flight = (ExtractFlight) flightKV.getValue();
            List<String> passengers = flight.getPassengers();

            out.println(flightKV.getKey() + ": " + passengers.size());
        }
        out.println("------------- End of Task3 ---------------");
        out.close();
    }
    
  //---------------------------------------- Output to Console --------------------------------------
    public void ConsoleOut() {
        System.out.println("\nTask 3");
        System.out.println("-------------------------");
        System.out.println("Passengers on each flight");
        System.out.println("-------------------------");
        System.out.println();
        
        for (KeyValue flightKV : flights) {
            ExtractFlight flight = (ExtractFlight) flightKV.getValue();
            List<String> passengers = flight.getPassengers();

            System.out.println(flightKV.getKey() + ": " + passengers.size());
        }
        System.out.println("\n----------- End of Task 3 ------------\n");
    }
}
