package beans;


public class Amenties {
	
	private long id;
	private String name;
	private boolean active;  //fleg za logicko brisanje  TRUE-ACTIVAN, FALSE-OBRISAN
	
	
	public Amenties() {
		super();
		this.active = true;
	}
	
	public Amenties(long id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.active = true;
	}
	
	public Amenties(long id, String name, boolean active) {
		super();
		this.id = id;
		this.name = name;
		this.active = active;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
   
	

	

}
