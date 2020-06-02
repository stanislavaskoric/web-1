package beans;

public class Location {
	
	private double latitude;
	private double longitude;
	private String address;

	public Location() {
		super();
	}
	
	public Location(double latitude, double longitude, String address) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}

	public Location(double latitude, double longitude, String street, int num, String place, int ptt) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = street+" "+num+","+place+" "+ptt;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	

}
