package objectives;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import mapReduce.KeyValue;
import operations.ExtractAirport;

public class Task1 {
	private final List<KeyValue> airports;

    public Task1(List<KeyValue> airports) {
        this.airports = airports;
    }
  //--------------------------Output to Text File-------------------------------------------
    public void TextFileOut() throws FileNotFoundException {

    	String fName = "outputFiles\\Task1.txt";
    	//logger.info("Writing airport flight count to file: " + fName);
        PrintWriter out = new PrintWriter(fName);
		
        List<String> unusedAirports = new ArrayList<>();
        
        out.println("\nTask 1");
        out.println("-------------------------");
        out.println("List of Airport  and number of Flights");
        out.println("-------------------------");
        out.println();
        
        for (KeyValue keyValue : airports) {
            ExtractAirport airport = (ExtractAirport) keyValue.getValue();

            if (airport.getFCount() == 0) {
                unusedAirports.add(airport.getAPName());
            } else {
            	out.println(airport.getAPName() + ": " + airport.getFCount());
            	
            }
        }

        out.println("-----------------------------");
        out.println("List of Airports not used");
        out.println("------------------------------");
        
        
        for (String airport : unusedAirports) {
            out.println(airport);
        
        }
        out.println("\n------------- End of Task1 ---------------");
        out.close();    
    }
    //-------------------------Output to the Console---------------------------------------------
    public void ConsoleOut() {
        List<String> unusedAirports = new ArrayList<>();

        System.out.println("\nTask 1");
        System.out.println("-------------------------");
        System.out.println("List of Airport and number of Flights");
        System.out.println("-------------------------\n");
        System.out.println("AiportName: Frequecy");
        
        for (KeyValue keyValue : airports) {
            ExtractAirport airport = (ExtractAirport) keyValue.getValue();

            if (airport.getFCount() == 0) {
                unusedAirports.add(airport.getAPName());
            } else {
                System.out.println(airport.getAPName() + ": " + airport.getFCount());
            }
        }
        System.out.println("\n--------------------------");
        System.out.println("Listof Airports not used");
        System.out.println("---------------------------");

        for (String airport : unusedAirports) {
            System.out.println(airport);
        }
        System.out.println("\n-----------End of Task 1-----------");
    }
    
 //-------------------------------------------------------------------------------------
}
