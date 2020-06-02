package beans;

public class Date {
    
	private String date;
	
	public Date() {
		
	}
	
	public Date(String d, String m, String y) {
		this.date = d+"/"+m+"/"+y;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
}
