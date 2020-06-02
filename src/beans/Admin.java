package beans;

public class Admin extends User {

	public Admin() {
		super();
		this.setRole("ADMIN");
	}
	
	public Admin(User u) {
		this.setUsername(u.getUsername());
		this.setFirstName(u.getFirstName());
		this.setLastName(u.getLastName());
		this.setPassword(u.getPassword());
		this.setGender(u.getGender());
		this.setRole("ADMIN");
	}

	 
}
