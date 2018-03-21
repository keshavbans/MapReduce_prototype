package mainRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;

import mapReduce.*;
import objectives.Task1;
import objectives.Task2;
import objectives.Task3;
import objectives.Task4;
import operations.*;

public class Runner {
//----------------------------------------- Creating a Map Reduce Framework ----------------------------------------------
	public static final int MAPPER_THREADS = Runtime.getRuntime().availableProcessors();
    public static final int REDUCER_THREADS = MAPPER_THREADS;
    private static List<KeyValue> airportList, flightList, errList;
    
    public static void main(String[] args) {

   //--------------------------------------------- Inputing Files------------------------------------------------------------
        
    	ReadFile fRead = new ReadFile();
        String[] passData = new String[0];
        String[] apData = new String[0];

        try {
            passData = fRead.read("inputFiles/AComp_Passenger_data.csv");
            apData = fRead.read("inputFiles/Top30_airports_LatLong.csv");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
  //------------------------------------- MapReduce framework ----------------------------------------
        
        errList = MapReduceErrorDetection(passData);
        flightList = MapReduceFlights(passData);
        airportList = MapAirports(apData);
        
 //--------------------------------- Output of the Tasks -------------------------------------------
      
        //textFilesOutput();
        new File("outputFiles").mkdirs();
        Task1 airportFlightCounter = new Task1(airportList);
        Task2 flightInventory = new Task2(flightList);
        Task3 flightPassengerCounter = new Task3(flightList);
        Task4 mileageCounter = new Task4(flightList, airportList);
        
        try {
        	airportFlightCounter.TextFileOut();
            flightInventory.TextFileOut();
            flightPassengerCounter.TextFileOut();
            mileageCounter.TextFileOut();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //consoleOutput();
        airportFlightCounter.ConsoleOut();
        flightInventory.ConsoleOut();
        flightPassengerCounter.ConsoleOut();
        mileageCounter.ConsoleOut();
        
    }
    
 //------------------------------- MapReduce on Error Detection ---------------------------------------    
    
    private static List<KeyValue> MapReduceErrorDetection(String[] passData) {
        Mapper mapErr = new MapErrors();
        List<KeyValue> mappedErrors = mapErr.execute(passData);
        Shuffler shuffleErr = new Shuffler();
        List<KeyValue<String, List>> shuffledErrors = shuffleErr.execute(mappedErrors);
        Reducer reduceErr = new ReduceErrors();
        return reduceErr.execute(shuffledErrors);
    }
 
 //------------------- Maps each attribute overrides the execute method of Mapper ---------------------------
 
    private static class MapErrors extends Mapper {
        @Override
        public List<KeyValue> execute(String[] input) {
            for (String line : input) {
                String[] attributes = line.split(",");

                for (String attribute : attributes) {
                    Future future = executorService.submit(() -> map(attribute));
                    futures.add(future);
                }
            }
            results(output);
            return output;
        }
        public KeyValue map(String attribute) {
            return new KeyValue<>(attribute, 1);
        }
    }
    
 //--------------------- Error correction on attributes with only one occurrence -------------------------
    
    private static class ReduceErrors extends Reducer {
        public KeyValue reduce(KeyValue<String, List> input, List<KeyValue<String, List>> shuffled) {
            List occurrences = input.getValue();    
            if (occurrences.size() == 1) {    // if the occurrence of the word is once then it's an error
                return correctError(input.getKey(), shuffled);
            }
            return null;
        }      
        
 //------------------- Corrects an incorrect attribute by the help of the lowest Levenshtein distance --------------

        private KeyValue correctError(String attribute, List<KeyValue<String, List>> shuffled) {
            int lowestLD = Integer.MAX_VALUE; // lowestLD = lowest Levenshtein Distance
            String corrected = "";
            for (KeyValue<String, List> keyValue : shuffled) { // Loop for lowest Levenshein distance
                String candidate = keyValue.getKey();
                // Find lD = levenshtein Distanceget the Levenshtein using Apache StringUtils
                int lD = StringUtils.getLevenshteinDistance(attribute, keyValue.getKey());
             // UPPERCASE attributes as replacement candidates
                if ((lD < lowestLD) && lD > 0 && candidate.toUpperCase().equals(candidate)) { 
                    lowestLD = lD; //lowercase attributes are all incorrect
                    corrected = candidate;
                }
            } 
            return new KeyValue<>(attribute, corrected);
        }
    }
    
  //------------------------------- MapReduce on Flights -----------------------------------------------  
   
    private static List<KeyValue> MapReduceFlights(String[] passData) {
        Mapper flightMapper = new FlightMapper();
        List<KeyValue> mappedFlights = flightMapper.execute(passData);
        Shuffler flightShuffler = new Shuffler();
        List<KeyValue<String, List>> shuffledFlights = flightShuffler.execute(mappedFlights);
        Reducer flightReducer = new FlightReducer();
        return flightReducer.execute(shuffledFlights);
    }

  //-------------------------- Create a flight object by mapping flight code ----------------------------
    
    private static class FlightMapper extends Mapper {
        public KeyValue map(String input) {
            String[] attr = input.split(",");
            for (KeyValue keyValue : errList) { // Replace incorrect values
                for (int i = 0; i < attr.length; i++) {
                    String oldStr = (String) keyValue.getKey();
                    String newStr = (String) keyValue.getValue();
                    if (attr[i].equals(oldStr)) {
                        attr[i] = newStr;
                        break;
                    }
                }
            }
            String passenger = attr[0], flightCode = attr[1], sourceAirport = attr[2]; 
            String destinationAirport = attr[3], departureTime = attr[4], flightDuration = attr[5];
            if (passenger.equals("") || flightCode.equals("") || sourceAirport.equals("") || destinationAirport.equals("") || departureTime.equals("0") || flightDuration.equals("0")) {
                return null;
            }
            return new KeyValue<>(flightCode, new ExtractFlight(flightCode, sourceAirport, destinationAirport, departureTime, flightDuration, passenger));
        }
    }

//----------------------- Reduce the flights by passenger list ------------------------------------
    
    private static class FlightReducer extends Reducer {
        public KeyValue reduce(KeyValue<String, List> input, List<KeyValue<String, List>> shuffled) {
            List flightData = input.getValue();
            List<String> passengers = new ArrayList<>();
            for (Object object : flightData) {
                ExtractFlight flight = (ExtractFlight) object;
                if (!passengers.contains(flight.getPassenger())) {  // Delete duplicate passengers
                    passengers.add(flight.getPassenger());
                }
            }
            ExtractFlight aFlight = (ExtractFlight) flightData.get(0);
            ExtractFlight consolidatedFlight = new ExtractFlight(aFlight.getFlightId(), aFlight.getSourceAirportCode(), aFlight.getDestinationAirportCode(), aFlight.getDepartureTime(), aFlight.getArrivalTime(), aFlight.getDuration(), passengers);
            return new KeyValue<>(input.getKey(), consolidatedFlight);
        }
    }
    
    //------------------------------ Mapping the Airports -----------------------------------------
    
    private static List<KeyValue> MapAirports(String[] apData) {
        Mapper mapAirport = new MapAirport();
        return mapAirport.execute(apData);
    }

//---------------------- Create airport object by by mapping airport codes -------------------------------
    
    private static class MapAirport extends Mapper {
        public KeyValue map(String input) {
            String[] attr = input.split(",");
            if (attr.length > 1) {
                String apName = attr[0], apCode = attr[1], apLat = attr[2], apLong = attr[3];
                int fCount = 0;
                for (KeyValue keyValue : flightList) { //count the flight from this airport
                    ExtractFlight flight = (ExtractFlight) keyValue.getValue();
                    if (flight.getSourceAirportCode().equals(apCode)) {
                        fCount++;
                    }
                }
                return new KeyValue<>(apCode, new ExtractAirport(apName, apCode, apLat, apLong, fCount));
            }
            return null;
        }
    }
    
   //---------------------------------------------------------------------------------------------------------

}