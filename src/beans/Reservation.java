package beans;



public class Reservation {
	
	private long id;
	private Apartment apartment;
	private Date startDate;
	private int nightsNumber;
	private double finalPrice;
	private String message;
	private Guest guest;
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

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
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

	public Guest getGuest() {
		return guest;
	}

	public void setGuest(Guest guest) {
		this.guest = guest;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
