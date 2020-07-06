package beans;

import java.util.ArrayList;
import java.util.List;

public class Host extends User{

	private List<Apartment> rentApartments;

	
	public Host() {
		super();
		this.setRole("HOST");
		this.rentApartments = new ArrayList<Apartment>();
	}
	
	public Host(User u) {
		this.setUsername(u.getUsername());
		this.setFirstName(u.getFirstName());
		this.setLastName(u.getLastName());
		this.setGender(u.getGender());
		this.setPassword(u.getPassword());
		this.setBlocked(u.isBlocked());
		this.setRole("HOST");
		this.rentApartments = new ArrayList<Apartment>();
	}
	
	public List<Apartment> getRentApartments() {
		return rentApartments;
	}

	public void setRentApartments(List<Apartment> apartmentsForRent) {
		this.rentApartments = apartmentsForRent;
	}
	 
	 
	 
	 
}
