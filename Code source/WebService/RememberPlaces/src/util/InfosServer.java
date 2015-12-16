package util;

public class InfosServer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String ipServerJava = "192.168.0.52";
	private final static int portServerJava = 15000 ;
	private final static int portServerWeb = 15000 ;
	private final static int distanceKmMaxPoints = 15 ;
	private final static double longitude = -71.983636 ;
	private final static double latitude = 45.370139 ;
	private final static int nbEssaiEnvoiMax = 0 ;
	private final static String dateMax = "22/06/2015" ;
	
	public static String getIpserverjava() {
		return ipServerJava;
	}
	
	public static int getPortserverjava() {
		return portServerJava;
	}
	
	public static int getPortserverweb() {
		return portServerWeb;
	}
	
	public static int getDistancekmmaxpoints() {
		return distanceKmMaxPoints;
	}
	
	public static double getLatitude() {
		return latitude;
	}
	
	public static double getLongitude() {
		return longitude;
	}
	
	public static int getNbessaienvoimax() {
		return nbEssaiEnvoiMax;
	}
	
	public static String getDatemax() {
		return dateMax;
	}

}
