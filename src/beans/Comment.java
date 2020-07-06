package beans;

public class Comment {
	
	private long id;
	private String guest_username;
	private Long apartment_id;
	private String text;
	private int evaluation;
	private boolean odabran;

	
	public Comment() {
		super();
		this.odabran = false;
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGuest() {
		return guest_username;
	}

	public void setGuest(String guest_username) {
		this.guest_username = guest_username;
	}

	public Long getApartment() {
		return apartment_id;
	}

	public void setApartment(Long apartment_id) {
		this.apartment_id = apartment_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}


	public boolean isOdabran() {
		return odabran;
	}


	public void setOdabran(boolean odabran) {
		this.odabran = odabran;
	}
	
	
	

}
