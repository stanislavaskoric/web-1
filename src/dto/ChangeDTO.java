package dto;

import java.util.List;

public class ChangeDTO {
    
	private Long id_apartmana;
	private String tip_apartmana;
	private String broj_soba;
	private String broj_gostiju;
	private String datumi;
	private List<String> slike;
	private String price;
	private String prijava;
	private String odjava;
	private List<String> sadrzaj;
	
	public ChangeDTO() {
		super();
	}

	public ChangeDTO(Long id_apartmana, String tip_apartmana, String broj_soba, String broj_gostiju, String datumi,
			List<String> slike, String price, String prijava, String odjava, List<String> sadrzaj) {
		super();
		this.id_apartmana = id_apartmana;
		this.tip_apartmana = tip_apartmana;
		this.broj_soba = broj_soba;
		this.broj_gostiju = broj_gostiju;
		this.datumi = datumi;
		this.slike = slike;
		this.price = price;
		this.prijava = prijava;
		this.odjava = odjava;
		this.sadrzaj = sadrzaj;
	}
	
	public Long getId_apartmana() {
		return id_apartmana;
	}
	public String getTip_apartmana() {
		return tip_apartmana;
	}
	public String getBroj_soba() {
		return broj_soba;
	}
	public String getBroj_gostiju() {
		return broj_gostiju;
	}
	public String getDatumi() {
		return datumi;
	}
	public List<String> getSlike() {
		return slike;
	}
	public String getPrice() {
		return price;
	}
	public String getPrijava() {
		return prijava;
	}
	public String getOdjava() {
		return odjava;
	}
	public List<String> getSadrzaj() {
		return sadrzaj;
	}
	public void setId_apartmana(Long id_apartmana) {
		this.id_apartmana = id_apartmana;
	}
	public void setTip_apartmana(String tip_apartmana) {
		this.tip_apartmana = tip_apartmana;
	}
	public void setBroj_soba(String broj_soba) {
		this.broj_soba = broj_soba;
	}
	public void setBroj_gostiju(String broj_gostiju) {
		this.broj_gostiju = broj_gostiju;
	}
	public void setDatumi(String datumi) {
		this.datumi = datumi;
	}
	public void setSlike(List<String> slike) {
		this.slike = slike;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setPrijava(String prijava) {
		this.prijava = prijava;
	}
	public void setOdjava(String odjava) {
		this.odjava = odjava;
	}
	public void setSadrzaj(List<String> sadrzaj) {
		this.sadrzaj = sadrzaj;
	}
	
	
}
