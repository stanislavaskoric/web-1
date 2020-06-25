package dto;

import beans.Apartment;

public class AdDTO {

	private long id;
	private String img;
	private String address;
	private String description;
	
	public AdDTO() {
		
	}
	
	public AdDTO(Apartment a) {
		   this.setId(a.getId());
	       this.setAddress(a.getLocation().getAddress());
	       this.setImg("./images/"+a.getImages().get(0));
	       this.setDescription("maksimalan kapacitet: "+a.getGuestsNumber()+"<br />"+
	                         "cena od: "+a.getPrice()+" RSD");
	}
		
	public long getId() {
		return id;
	}
	
	public String getImg() {
		return img;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
