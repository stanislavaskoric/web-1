package beans;

import java.util.ArrayList;
import java.util.List;

public class Apartment {

	private long id;
	private String type;                //APARTMENT,ROOM
	private int roomsNumber;
	private int guestsNumber;
	private Location location;
	private List<Date> rentDates;
	private List<Date> availableDates;
	private Host host;
	private List<Comment> comments;
	private List<String> images;
	private double price;
	private String check_in_time;
	private String check_out_time;
	private String status;               //ACTIVE,INACTIVE
	private List<Amenties> amenties;
	private List<Reservation> reservations;
	
	
	
	public Apartment() {
		this.roomsNumber = 0;
		this.guestsNumber = 0;
		this.rentDates = new ArrayList<Date>();
		this.availableDates = new ArrayList<Date>();
		this.comments = new ArrayList<Comment>();
		this.images = new ArrayList<String>();
		this.price = 0;
		this.amenties = new ArrayList<Amenties>();
		this.reservations = new ArrayList<Reservation>();
		this.check_in_time = "2 PM";
		this.check_out_time = "10 AM";
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


	public List<Date> getRentDates() {
		return rentDates;
	}


	public void setRentDates(List<Date> rentDates) {
		this.rentDates = rentDates;
	}


	public List<Date> getAvailableDates() {
		return availableDates;
	}


	public void setAvailableDates(List<Date> availableDates) {
		this.availableDates = availableDates;
	}


	public Host getHost() {
		return host;
	}


	public void setHost(Host host) {
		this.host = host;
	}


	public List<Comment> getComments() {
		return comments;
	}


	public void setComments(List<Comment> comments) {
		this.comments = comments;
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


	public List<Amenties> getAmenties() {
		return amenties;
	}


	public void setAmenties(List<Amenties> amenties) {
		this.amenties = amenties;
	}


	public List<Reservation> getReservations() {
		return reservations;
	}


	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	
	
	
	
	
}
