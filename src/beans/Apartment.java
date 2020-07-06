package beans;

import java.util.ArrayList;
import java.util.List;

public class Apartment {

	private long id;
	private String type;                //APARTMENT,ROOM
	private int roomsNumber;
	private int guestsNumber;
	private Location location;
	private List<String> rentDates;
	private List<String> availableDates;
	private String host_username;               //domacin
	private List<String> images;
	private double price;
	private String check_in_time;
	private String check_out_time;
	private String status;               //ACTIVE,INACTIVE
	private List<Long> amenties;
	private List<Long> reservations;
	private boolean active;
	private String city;     //za search
	private String country;
	
	
	
	public Apartment() {
		this.roomsNumber = 0;
		this.guestsNumber = 0;
		this.rentDates = new ArrayList<String>();
		this.availableDates = new ArrayList<String>();
		this.images = new ArrayList<String>();
		this.price = 0;
		this.amenties = new ArrayList<Long>();
		this.reservations = new ArrayList<Long>();
		this.check_in_time = "14:00";
		this.check_out_time = "10:00";
		this.active = true;
	}

	public Apartment(String t, int rn, int gn, Location l, ArrayList<String>dates, String host, ArrayList<String>imgs, 
			         double p,ArrayList<Long>amenties, String in, String out) {
		this.type = t;
		this.roomsNumber = rn;
		this.guestsNumber = gn;
		this.location = l;
		this.rentDates = dates;
		this.host_username = host;
		this.availableDates = dates;
		this.images = imgs;
		this.price = p;
		this.amenties = amenties;
		this.reservations = new ArrayList<Long>();
		this.check_in_time = in;
		this.check_out_time = out;
		this.active = true;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int getRoomsNumber() {
		return roomsNumber;
	}


	public void setRoomsNumber(int roomsNumber) {
		this.roomsNumber = roomsNumber;
	}


	public int getGuestsNumber() {
		return guestsNumber;
	}


	public void setGuestsNumber(int guestsNumber) {
		this.guestsNumber = guestsNumber;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public List<String> getRentDates() {
		return rentDates;
	}


	public void setRentDates(List<String> rentDates) {
		this.rentDates = rentDates;
	}


	public List<String> getAvailableDates() {
		return availableDates;
	}


	public void setAvailableDates(List<String> availableDates) {
		this.availableDates = availableDates;
	}

	public String getHost_username() {
		return host_username;
	}


	public void setHost_username(String host_username) {
		this.host_username = host_username;
	}


	public List<String> getImages() {
		return images;
	}


	public void setImages(List<String> images) {
		this.images = images;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getCheck_in_time() {
		return check_in_time;
	}


	public void setCheck_in_time(String check_in_time) {
		this.check_in_time = check_in_time;
	}


	public String getCheck_out_time() {
		return check_out_time;
	}


	public void setCheck_out_time(String check_out_time) {
		this.check_out_time = check_out_time;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public List<Long> getAmenties() {
		return amenties;
	}


	public void setAmenties(List<Long> amenties) {
		this.amenties = amenties;
	}


	public List<Long> getReservations() {
		return reservations;
	}


	public void setReservations(List<Long> reservations) {
		this.reservations = reservations;
	}

	public boolean isActive() {
		return active;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
	
	
	
}
