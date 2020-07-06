package beans;

import java.util.ArrayList;
import java.util.List;

public class Guest extends User {
	
	private List<Apartment> rentedApartments;
	private List<Reservation> reservations;
	
	
	public Guest() {
		super();
        this.setRole("GUEST");
        this.rentedApartments = new ArrayList<Apartment>();
        this.reservations = new ArrayList<Reservation>();
	}
	
	public Guest(User u) {
		this.setFirstName(u.getFirstName());
		this.setLastName(u.getLastName());
		this.setGender(u.getGender());
		this.setUsername(u.getUsername());
		this.setPassword(u.getPassword());
		this.setBlocked(u.isBlocked());
		this.setRole("GUEST");
        this.rentedApartments = new ArrayList<Apartment>();
        this.reservations = new ArrayList<Reservation>();
	}


	public List<Apartment> getRentedApartments() {
		return rentedApartments;
	}


	public void setRentedApartments(List<Apartment> rentedApartments) {
		this.rentedApartments = rentedApartments;
	}


	public List<Reservation> getReservations() {
		return reservations;
	}


	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	
	
	

}
