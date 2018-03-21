package objectives;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.Future;

import mapReduce.*;
import operations.*;

public class Task4 {
	private final List<KeyValue> airports, flightMileage, passengerMileage;
	DecimalFormat df = new DecimalFormat("###,###.##"); // Trim decimals after 2 digits of decimal
	
    public Task4(List<KeyValue> flights, List<KeyValue> airports) {
        this.airports = airports;

        MapMileage mileage = new MapMileage();
        flightMileage = mileage.executeFlightMapper(flights);

        mileage = new MapMileage();
        List<KeyValue> mappedPassengerMileage = mileage.executePassengerMapper(flights);

        Shuffler shufflePassMileage = new Shuffler();
        List<KeyValue<String, List>> shuffledPassengerMileage = shufflePassMileage.execute(mappedPassengerMileage);

        Reducer passMileage = new ReducePassMileage();
        passengerMileage = passMileage.execute(shuffledPassengerMileage);
    }
  //------------------------------------------- Output to Text File --------------------------------------------------  
    public void TextFileOut() throws FileNotFoundException {

        String fileName = "outputFiles\\Task4.txt";
        PrintWriter out = new PrintWriter(fileName);

        out.println("\nTask 4");
        out.println("-------------------------");
        out.println("\nNautical miles covered by each flight");
        out.println("-------------------------");
        out.println("");

        for (KeyValue keyValue : flightMileage) {
            String flightCode = (String) keyValue.getKey();
            Double miles = (Double) keyValue.getValue();
            out.println(flightCode + ": " + df.format(miles));
        }

        out.println("");
        out.println("Part 2");
        out.println("-------------------------");
        out.println("\nNautical miles covered by each Passenger");
        out.println("-------------------------");
        out.println("");

        for (KeyValue keyValue : passengerMileage) {
            String passengerCode = (String) keyValue.getKey();
            Double miles = (Double) keyValue.getValue();
            out.println(passengerCode + ": " + df.format(miles));
        }
        out.println("\n------------- End of Task 4 ---------------");
        out.close();
        
    }
    
//----------------------------------------------- Output to Console -----------------------------------------------------
    public void ConsoleOut() {
    	System.out.println("\nTask 4");
    	System.out.println("-------------------------");
    	System.out.println("\nNautical miles of each flight");
        System.out.println("-----------------------------");
        System.out.println("");
        
        for (KeyValue keyValue : flightMileage) {
            String flightCode = (String) keyValue.getKey();
            Double miles = (Double) keyValue.getValue();

            System.out.println(flightCode + ": " +df.format(miles));
        }
        System.out.println("\nPart 2");
        System.out.println("-------------------------");
        System.out.println("\nNautical miles covered by each Passenger");
        System.out.println("-------------------------");
        System.out.println("");

        for (KeyValue keyValue : passengerMileage) {
            String passengerCode = (String) keyValue.getKey();
            Double miles = (Double) keyValue.getValue();
            System.out.println(passengerCode + ": " + df.format(miles));
        }
        System.out.println("\n------------- End of Task 4 ---------------");
    }

//-------------------------------------------------------- Methods ----------------------------------------------------
    private class MapMileage extends Mapper {
        private List<KeyValue> executeFlightMapper(List<KeyValue> input) {// Map he mileage covered by each flight
            for (KeyValue keyValue : input) {
                Future future = executorService.submit(() -> map(keyValue));
                futures.add(future);
            }
            results(output);
            return output;
        }
        
        private List<KeyValue> executePassengerMapper(List<KeyValue> input) {//Maps the mileage covered by each passenger
            for (KeyValue keyValue : input) {
                ExtractFlight flight = (ExtractFlight) keyValue.getValue();
                for (String passenger : flight.getPassengers()) {
                    Future future = executorService.submit(() -> map(new KeyValue<>(passenger, flight)));
                    futures.add(future);
                }
            }
            results(output);
            return output;
        }

        private KeyValue map(KeyValue keyValue) { // Key Value pair
            ExtractFlight flight = (ExtractFlight) keyValue.getValue();
            ExtractAirport sourceAirport = null;
            ExtractAirport destinationAirport = null;
            for (KeyValue a : airports) { //Match airports by airport code
                ExtractAirport airport = (ExtractAirport) a.getValue();
                if (flight.getSourceAirportCode().equals(airport.getAPCode())) {
                    sourceAirport = airport;
                }
                if (flight.getDestinationAirportCode().equals(airport.getAPCode())) {
                    destinationAirport = airport;
                }
            }

            // Calculate the distance with the help of Long Lat
            double distance = CalculateDistance.calculate(sourceAirport.getAPLat(), sourceAirport.getAPLong(), destinationAirport.getAPLat(), destinationAirport.getAPLong());
            return new KeyValue<>(keyValue.getKey(), distance);
        }
        public KeyValue map(String input) {
            return null;
        }
    }

//----------------------------------- Reduce passenger Miles from the total miles ------------------------------------------
    public class ReducePassMileage extends Reducer {

        public KeyValue reduce(KeyValue<String, List> input, List<KeyValue<String, List>> shuffled) {

            List distances = input.getValue();
            double totalDistance = 0;

            for (Object object : distances) { // Total Distance 
                Double distance = (Double) object;
                totalDistance = totalDistance + distance;
            }
            return new KeyValue<>(input.getKey(), totalDistance);
        }
    }
   // --------------------------------------------------------------------------------------------------------------- 
}
