package dto;

import java.util.List;

public class ApartmentDTO {    //pomocna klasa za dodavanje apartmana - u njoj elementi sa forme na frontu
	
	private String tip_apartmana;
	private String broj_soba;
	private String broj_gostiju;
	private String ulica;
	private String grad;
	private String broj_stana;
	private String zip;
	private double geo_sirina;
	private double geo_duzina;
	private String datumi;
	private String domacin;
	private List<String> slike;
	private String price;
	private String prijava;
	private String odjava;
	private List<String> sadrzaj;
	
	
	public ApartmentDTO() {
		
	}
	
	public ApartmentDTO(String tip_apartmana) {
		this.tip_apartmana = tip_apartmana;
	}
	
	
	public ApartmentDTO(String tip_apartmana, String broj_soba, String broj_gostiju, String ulica, String grad,
			String broj_stana, String zip, double geo_sirina, double geo_duzina, String datumi, String domacin,
			List<String> slike,String price, String prijava, String odjava, List<String> sadrzaj) {
		super();
		this.tip_apartmana = tip_apartmana;
		this.broj_soba = broj_soba;
		this.broj_gostiju = broj_gostiju;
		this.ulica = ulica;
		this.grad = grad;
		this.broj_stana = broj_stana;
		this.zip = zip;
		this.geo_sirina = geo_sirina;
		this.geo_duzina = geo_duzina;
		this.datumi = datumi;
		this.domacin = domacin;
		this.slike = slike;
		this.price = price;
		this.prijava = prijava;
		this.odjava = odjava;
		this.sadrzaj = sadrzaj;
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



	public String getUlica() {
		return ulica;
	}



	public String getGrad() {
		return grad;
	}



	public String getBroj_stana() {
		return broj_stana;
	}



	public String getZip() {
		return zip;
	}



	public double getGeo_sirina() {
		return geo_sirina;
	}



	public double getGeo_duzina() {
		return geo_duzina;
	}



	public String getDatumi() {
		return datumi;
	}



	public String getDomacin() {
		return domacin;
	}



	public List<String> getSlike() {
		return slike;
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

	public String getPrice() {
		return price;
	}

	
	
	
	
}
