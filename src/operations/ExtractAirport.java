package operations;

public class ExtractAirport {
//------------------------------------ Variable -----------------------------------------------------
    private final String apName, apCode; // Airport Name & Airport Code
    private final double apLat, apLong; // longitude and latitude of the Airport	
    private final int fCount; // Flight Count

    public ExtractAirport(String apName, String apCode, String apLat, String apLong, int fCount) {
        this.apName = apName;
        this.apCode = apCode;
        this.apLat = Double.parseDouble(apLat);
        this.apLong = Double.parseDouble(apLong);
        this.fCount = fCount;
    }
//---------------------------------------- Method --------------------------------------------------------
    public String getAPName() {
        return apName;						//Airport Name
    }

    public String getAPCode() {
        return apCode;						// Airport Code
    }

    public double getAPLat() {
        return apLat;						//Airport Latitude
    }

    public double getAPLong() {
        return apLong;						//Airport Longitude
    }

    public int getFCount() {
        return fCount;						//Flight Count
    }
 //----------------------------------------------------------------------------------------------------------
}