package protocole;

public class Data {
	
	private String latitude ;
	private String longitude ;
	private int kmMax ;
	private String dateMax ;
	
	public Data() {
		// TODO Auto-generated constructor stub
	}
	
	public String getDateMax() {
		return dateMax;
	}
	
	public int getKmMax() {
		return kmMax;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setDateMax(String dateMax) {
		this.dateMax = dateMax;
	}
	
	public void setKmMax(int kmMax) {
		this.kmMax = kmMax;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
