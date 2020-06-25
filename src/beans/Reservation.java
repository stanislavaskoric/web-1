package beans;



public class Reservation {
	
	private long id;
	private long apartment_id;
	private String startDate;
	private int nightsNumber;
	private double finalPrice;
	private String message;
	private String guest_username;
	private String status; //CREATE,DENIED,CANCELED,ACCEPT,COMPLETED

	
	public Reservation() {
		super();
		this.nightsNumber = 1;
		this.finalPrice = 0;
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getApartment() {
		return apartment_id;
	}

	public void setApartment(Long apartment_id) {
		this.apartment_id = apartment_id;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public int getNightsNumber() {
		return nightsNumber;
	}

	public void setNightsNumber(int nightsNumber) {
		this.nightsNumber = nightsNumber;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGuest() {
		return guest_username;
	}

	public void setGuest(String guest_username) {
		this.guest_username = guest_username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
