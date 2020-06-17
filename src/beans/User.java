package beans;

public class User {
    
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String gender;
	private String role;      //admin, host, guest
	
	public User() {
		
	}
	
	public User(String u,String p, String f, String l, String g) {
		this.username = u;
		this.password = p;
		this.firstName = f;
		this.lastName = l;
		this.gender = g;
	}
	  
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	  
	  
	  
	
	  
	  
}
