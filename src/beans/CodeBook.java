package beans;

import java.time.LocalDate;

public class CodeBook {

	private LocalDate date;        
	private String description;
	
	public CodeBook() {
		super();
	}

	public CodeBook(LocalDate date, String description) {
		super();
		this.date = date;
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
