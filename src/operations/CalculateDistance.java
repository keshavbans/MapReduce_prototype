package operations;

public class CalculateDistance {
// ------------------------ Calculate the distance by the help of Long Lat values -------------------	
	public static double calculate(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(degToRad(lat1)) * Math.sin(degToRad(lat2)) + Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) * Math.cos(degToRad(theta));
        dist = Math.acos(dist);
        dist = radToDeg(dist);
        dist = dist * 60 * 1.1515 * 0.8684;

        return (dist);
    }
//-------------------------------- Methods for Conversions ---------------------------------------
    private static double degToRad(double deg) { //  Convert Degrees to Radians
        return (deg * Math.PI / 180.0);
    }

    private static double radToDeg(double rad) { // Convert Radians to Degrees
        return (rad * 180 / Math.PI);
    }
}
