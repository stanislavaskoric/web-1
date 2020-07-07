package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Amenties;
import beans.Apartment;
import beans.Comment;
import beans.Location;
import beans.Reservation;
import dto.AdDTO;
import dto.ApartmentDTO;
import dto.ChangeDTO;


@SuppressWarnings("unchecked")
public class ApartmentDAO {
	
	private HashMap<Long, Apartment> apartments;       //mapa svih apartmana
	private List<Amenties> amenties;                   //sifarnik sadrzaja
	private String path;                               //putanja do contexta
	
	public ApartmentDAO(String path, List<Amenties>amenties) {
		this.apartments = new HashMap<Long, Apartment>();
		this.amenties = amenties;
		this.path=path;
		this.loadApartments();
		
	}

	public HashMap<Long, Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(HashMap<Long, Apartment> apartments) {
		this.apartments = apartments;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	public boolean deleteApartment(Long id) {
		Apartment a = apartments.get(id);
		boolean fleg = false;
		if(a.isActive()) {
			a.setActive(false);
			fleg = true;
			saveApartments();
		}
		return fleg;
	}
	
	
	
	public List<String> getAmentiesFromApartment(Long id_apartment){
		List<String> ret = new ArrayList<String>();
		Apartment a = apartments.get(id_apartment);
		List<Long> apartmentAmenties = a.getAmenties();
		for(Amenties am : amenties) {
		   if(apartmentAmenties.contains(am.getId()) && am.isActive()) {
			   ret.add(am.getName());
		   }
		}
		return ret;
	}
	
	
	public void changeAvailableDates(String ss, int n, Long id) {
		Apartment a = findApartmentById(id);
		List<String>availableDates = a.getAvailableDates();
		int firstIndexForDeleted = 0;
		String []dateA = ss.split("/");
		String startDate = dateA[1]+"/"+dateA[0]+"/"+dateA[2];
		for(String s : availableDates) {
			if(s.equals(startDate)) {
				break;
			}
			firstIndexForDeleted ++;
		}
		for(int i=0; i<n; i++) {
			availableDates.remove(firstIndexForDeleted);
			firstIndexForDeleted++;
		}
		
		a.setAvailableDates(availableDates);
		saveApartments();
	}
	
	
	
	public Apartment findApartmentById(Long id) {
		Apartment a = apartments.get(id);
		if(a != null && a.isActive()) {
			return a;
		}else {
			return null;
		}
		//return apartments.get(id);
	}
	
	
	public Collection<String> getDateForReservation(Long apartment_id){   //radi samo sa oblikom 2-1-2020
		Apartment a = findApartmentById(apartment_id);
		List<String>dates = a.getAvailableDates();
		List<String>ret = new ArrayList<String>();
		for(String s : dates) {
			String []sa = s.split("/"); // 25/06/2020
			String d= sa[0].replaceFirst("^0+(?!$)", "");
			String m= sa[1].replaceFirst("^0+(?!$)", "");
			String t = d+"-"+m+"-"+sa[2];
			System.out.println(t);
			ret.add(t);
      	}
		return ret;
	}
	
	
	public List<LocalDate> getAvailableDays(Long id){
		Apartment a = findApartmentById(id);
		List<String> ad = a.getAvailableDates();
		List<LocalDate> ret = new ArrayList<LocalDate>();
		for(String s : ad) {
			String []dateA = s.split("/");
			System.out.println(s);
			LocalDate ld = LocalDate.of(Integer.parseInt(dateA[2]),Integer.parseInt(dateA[1]),Integer.parseInt(dateA[0]));
			ret.add(ld);
		}
		return ret;
	}
		

	
	public double getPricePerNight(Long id) {
		Apartment a = apartments.get(id);
		return a.getPrice();
	}
	
	
	
	public List<AdDTO> findActiveAparment(){  
		List<AdDTO> ret = new ArrayList<AdDTO>();
		for(Apartment a: apartments.values()) {
			  if(a.getStatus().equals("ACTIVE") && a.isActive()) {
				  AdDTO ad = new AdDTO();
			       ad.setId(a.getId());
			       ad.setAddress(a.getLocation().getAddress());
			     //  System.out.println("JELENA"+a.getImages().size());
			       ad.setImg("./images/"+a.getImages().get(0));
			       ad.setDescription("maksimalan kapacitet: "+a.getGuestsNumber()+"<br />"+
			                         "cena od: "+a.getPrice()+" RSD");
			       ret.add(ad);  
			  }
		}
		return ret;
	}
	
	public List<Apartment> findSearchApartment(){
		List<Apartment> ret = new ArrayList<Apartment>();
		for(Apartment a: apartments.values()) {
			  if(a.getStatus().equals("ACTIVE") && a.isActive()) {
			       ret.add(a);  
			  }
		}
		return ret;
	}
	
	
	public List<AdDTO> findAllAparment(){  
		List<AdDTO> ret = new ArrayList<AdDTO>();
		for(Apartment a: apartments.values()) {
			 if(a.isActive()) {
				   AdDTO ad = new AdDTO();
			       ad.setId(a.getId());
			       ad.setAddress(a.getLocation().getAddress());
			       ad.setImg("./images/"+a.getImages().get(0));
			       ad.setDescription("maksimalan kapacitet: "+a.getGuestsNumber()+"<br />"+
			                         "cena od: "+a.getPrice()+" RSD");
			       ret.add(ad);   
			 }
		}
		return ret;
	}
	
	
	public List<Apartment> findNotDeletedApartment(){
		List<Apartment> ret = new ArrayList<Apartment>();
		for(Apartment a: apartments.values()) {
			if(a.isActive()) {
				ret.add(a);
			}
		}
		return ret;
	}
	
	
	public List<AdDTO> findAllHostActiveApartment(String host_username){  
		List<AdDTO> ret = new ArrayList<AdDTO>();
		for(Apartment a: findNotDeletedApartment()) {
			 if(a.getStatus().equals("ACTIVE") && a.getHost_username().equals(host_username)) {
				   AdDTO ad = new AdDTO();
			       ad.setId(a.getId());
			       ad.setAddress(a.getLocation().getAddress());
			       ad.setImg("./images/"+a.getImages().get(0));
			       ad.setDescription("maksimalan kapacitet: "+a.getGuestsNumber()+"<br />"+
			                         "cena od: "+a.getPrice()+" RSD");
			       ret.add(ad);   
			 }
		}
		return ret;
	}
	
	
	public List<AdDTO> findAllHostInactiveApartment(String host_username){  
		List<AdDTO> ret = new ArrayList<AdDTO>();
		for(Apartment a: findNotDeletedApartment()) {
			 if(a.getStatus().equals("INACTIVE") && a.getHost_username().equals(host_username)) {
				   AdDTO ad = new AdDTO();
			       ad.setId(a.getId());
			       ad.setAddress(a.getLocation().getAddress());
			       ad.setImg("./images/"+a.getImages().get(0));
			       ad.setDescription("maksimalan kapacitet: "+a.getGuestsNumber()+"<br />"+
			                         "cena od: "+a.getPrice()+" RSD");
			       ret.add(ad);   
			 }
		}
		return ret;
	}
	
	
	public List<Apartment> findAllHostApartment(String host_username){  
		List<Apartment> ret = new ArrayList<Apartment>();
		for(Apartment a: findNotDeletedApartment()) {
			 if(a.getStatus().equals("ACTIVE") && a.getHost_username().equals(host_username)) {
			       ret.add(a);   
			 }
		}
		return ret;
	}
	
	public List<Long> findHostsApartment(String host_username){  
		List<Long> ret = new ArrayList<Long>();
		for(Apartment a: findNotDeletedApartment()) {
			 if(a.getHost_username().equals(host_username)) {
			       ret.add(a.getId());   
			 }
		}
		return ret;
	}
	
	
	
	/////SORTIRANI APARTMANI HOST
	public Collection<AdDTO> getSortHost(String type, String host_username){
		List<Apartment> temp = findAllHostApartment(host_username);
		List<AdDTO> ret = new ArrayList<AdDTO>();
	    Comparator<Apartment> compareByCost = (Apartment o1, Apartment o2) -> Double.compare(o1.getPrice(), o2.getPrice());
	    if(type.equals("asceding")) {
	    	Collections.sort(temp, compareByCost);
	    }else {
	    	Collections.sort(temp, compareByCost.reversed());
	    }
	    for(Apartment a: temp) {
	    	   AdDTO ad = new AdDTO();
		       ad.setId(a.getId());
		       ad.setAddress(a.getLocation().getAddress());
		       ad.setImg("./images/"+a.getImages().get(0));
		       ad.setDescription("maksimalan kapacitet: "+a.getGuestsNumber()+"<br />"+
		                         "cena od: "+a.getPrice()+" RSD");
		       ret.add(ad);   
	    }
	    return ret;
	}
	
	////FILTRIRANI HOST
	public Collection<AdDTO> getFilterHost(String type, String status, List<String>sadrzaj, String host_username){
		List<Apartment> temp = findAllHostApartment(host_username);
		List<AdDTO> ret = new ArrayList<AdDTO>();
		boolean fleg = true;
		for(Apartment a: temp) {
			if(a.getType().toLowerCase().contains(type.toLowerCase())) {
				if(a.getStatus().toLowerCase().contains(status.toLowerCase())) {
					if(!sadrzaj.isEmpty()) {
						fleg = checkAmentiesIntoApartment(sadrzaj,a.getId());
					}
					if(fleg) {
						System.out.println("usla u dod");
						 AdDTO ad = new AdDTO();
					       ad.setId(a.getId());
					       ad.setAddress(a.getLocation().getAddress());
					       ad.setImg("./images/"+a.getImages().get(0));
					       ad.setDescription("maksimalan kapacitet: "+a.getGuestsNumber()+"<br />"+
					                         "cena od: "+a.getPrice()+" RSD");
					       ret.add(ad); 
					}
				}
			}
		}
		return ret;
	}
	
	
	public boolean checkAmentiesIntoApartment(List<String>filter, Long id) {
		System.out.println("usla u am");
		List<String> apartment_amenties = getAmentiesFromApartment(id);
		for(String s: filter) {
			System.out.println("provera: "+s);
			if(!apartment_amenties.contains(s)) {
				return false;
			}
		}
		return true;
	}
	
	
	
	public Collection<Apartment> getSort(String type, List<Apartment>ret){
		//List<Apartment> ret = findNotDeletedApartment();
	    Comparator<Apartment> compareByCost = (Apartment o1, Apartment o2) -> Double.compare(o1.getPrice(), o2.getPrice());
	    if(type.equals("asceding")) {
	    	Collections.sort(ret, compareByCost);
	    }else {
	    	Collections.sort(ret, compareByCost.reversed());
	    }
	    return ret;
	}
	
	
	
	
	public Collection<AdDTO> getSearchApartment(List<Apartment>aps, String dO, String dD, String cO, String cD, String sO, String sD, String o,
			                                    String c,String dr, String tip_sort){
		List<Apartment>retTemp = new ArrayList<Apartment>();
		List<AdDTO>ret = new ArrayList<AdDTO>();
		List<String> datesOpseg = getDaysBetweenDates(dO,dD);
		for(Apartment a: aps){
			if(opsegDate(datesOpseg,a.getRentDates())) {  //promeni ovde available dates
				if(opsegCena(a.getPrice(),cO,cD)) {
					if(opsegSoba(a.getRoomsNumber(),sO,sD)) {
						if(c.equals("") || a.getCity().equals(c)) {
							if(dr.equals("") || a.getCountry().equals(dr)) {
								if(o.length()>0) {
									int brojOsoba = Integer.parseInt(o);	
									if(brojOsoba == a.getGuestsNumber()) {  
										// AdDTO temp = new AdDTO(a);
										 retTemp.add(a);
									}
								}else {
									 //AdDTO temp = new AdDTO(a);
									 retTemp.add(a);
								}
							}else { //nije to ta drzava
								
							}
						}else { //nije to taj grad
							
						}
					}else { //broj soba nije u opsegu
					
					}
				}else {  //cena nije u opsegu
					
				}
			}else {  //datum nije u opsegu
				
			}
		}
		//ako imam neke pretrazene->sortiraj i pretvori u AdDTO
		if(!retTemp.isEmpty()) {
			getSort(tip_sort, retTemp);
			for(Apartment a: retTemp) {
				AdDTO temp = new AdDTO(a);
				ret.add(temp);
			}
		}
		
		return ret;
	}
	
	
	
	
	
	public boolean opsegDate(List<String> myOpsegDate, List<String>availableDates) {
		if(myOpsegDate == null) {
			return true;
		}
		// da li se sve iz opsega nalazi u listi slobodnih dana
		for(String s : myOpsegDate) {
			if(!availableDates.contains(s)) {
				return false;
			}
		}		
		return true;
	}
	
	
	
	
	public List<String> getDaysBetweenDates(String min, String max) //racunam prvi, poslednji ne
	{
	    List<String> dates = new ArrayList<String>();
	    if(min.length() == 0) {
	    	return dates;
	    }
	    Date minn = null;
		Date maxx = null;
		try {
		    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		    minn = sdf.parse(formated(min));
		    maxx = sdf.parse(formated(max));
		} catch (java.text.ParseException e) {
		    e.printStackTrace();
		}
	    
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(minn);
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	    while (calendar.getTime().before(maxx))
	    {
	        Date result = calendar.getTime();
	        String todayAsString = sdf.format(result);
	        dates.add(todayAsString);
	        calendar.add(Calendar.DATE, 1);
	    }
	    return dates;
	}
	
	
	
	public String formated(String s) {
		System.out.println(s);
		String []a = s.split("-");
		return a[2]+"/"+a[1]+"/"+a[0];
	}
	
	
	
	public boolean opsegCena(double prava, String min, String max) {
		if(min.length() == 0) {
			return true;
		}
		boolean flag = false;
		double minn, maxx;
		if(!min.equals("")) {
			minn = Double.parseDouble(min);
		}
		else {
			minn = 0;
		}
		if(!max.equals("")) {
			maxx = Double.parseDouble(max);
		}
		else {
			maxx = Double.MAX_VALUE;
		}
		
		if(prava >= minn && prava <= maxx) {
			flag = true;
		}
		
		return flag;
	}
	
	
	
	public boolean opsegSoba(int prava, String min, String max) {
		if(min.length() == 0) {
			return true;
		}
		boolean flag = false;
		int minn, maxx;
		if(!min.equals("")) {
			minn = Integer.parseInt(min);
		}
		else {
			minn = 0;
		}
		if(!max.equals("")) {
			maxx = Integer.parseInt(max);
		}
		else {
			maxx = Integer.MAX_VALUE;
		}
		
		if(prava >= minn && prava <= maxx) {
			flag = true;
		}
		
		return flag;
	}
	
	public void changeApartment(ChangeDTO apartment){
		System.out.println("--izmena apartmana--");
		
		String type = apartment.getTip_apartmana();
		int roomsNumber = Integer.parseInt(apartment.getBroj_soba());
		int guestsNumber = Integer.parseInt(apartment.getBroj_gostiju());
		
		//System.out.println(type+" "+roomsNumber+" "+guestsNumber);
		
		ArrayList<String> slike = new ArrayList<String>();
		slike.add("app1.jpg");
		slike.add("app2.jpg");
		slike.add("app3.jpg");
		double price = Double.parseDouble(apartment.getPrice());	
		String v_prijave = apartment.getPrijava();
		String v_odjave = apartment.getOdjava();
		
		List<String>sadrzaj = apartment.getSadrzaj();
		ArrayList<Long>amenties =  new ArrayList<Long>();
		for(String s : sadrzaj) {
			long l = Integer.parseInt(s);
			amenties.add(l);
		}
		
		ArrayList<String> datumi = new ArrayList<String>();
		String datum = apartment.getDatumi();
		String[]dates = datum.split(",");
		for(int i=0; i<dates.length; i++) {
			datumi.add(dates[i]);
		}
		
	    
		Apartment a = findApartmentById(apartment.getId_apartmana());
		a.setType(type);
		a.setRoomsNumber(roomsNumber);
		a.setGuestsNumber(guestsNumber);
		a.setImages(slike);
		a.setPrice(price);
		a.setCheck_in_time(v_prijave);
		a.setCheck_out_time(v_odjave);
		a.setAvailableDates(datumi);
		a.setAmenties(amenties);
	    saveApartments();
	}
	
	
	
	
	public void addApartment(ApartmentDTO apartment, String host) {
		System.out.println("--dodavanje apartmana--");
		
		String type = apartment.getTip_apartmana();
		int roomsNumber = Integer.parseInt(apartment.getBroj_soba());
		int guestsNumber = Integer.parseInt(apartment.getBroj_gostiju());
		
		//System.out.println(type+" "+roomsNumber+" "+guestsNumber);
		//System.out.println(apartment.getUlica()+" "+apartment.getGrad()+ "" +apartment.getBroj_stana()+" "+apartment.getZip());
		   String street = apartment.getUlica();
		   int homeNumber = Integer.parseInt(apartment.getBroj_stana());
		   String city = apartment.getGrad();
		   String zips = (apartment.getZip()).replaceAll("\\s","");
		   int zip = Integer.parseInt(zips);
		   double latitude = apartment.getGeo_sirina();
		   double longitude = apartment.getGeo_duzina();
		//System.out.println(latitude + " "+longitude+" "+zip);
		Location location = new Location(latitude, longitude, street, homeNumber,city,zip);
		
		String host_username = "";		
		ArrayList<String> slike = (ArrayList<String>) apartment.getSlike();
		double price = Double.parseDouble(apartment.getPrice());	
		String v_prijave = apartment.getPrijava();
		String v_odjave = apartment.getOdjava();
		
		List<String>sadrzaj = apartment.getSadrzaj();
		ArrayList<Long>amenties =  new ArrayList<Long>();
		for(String s : sadrzaj) {
			long l = Integer.parseInt(s);
			amenties.add(l);
		}
		
		ArrayList<String> datumi = new ArrayList<String>();
		String datum = apartment.getDatumi();
		String[]dates = datum.split(",");
		for(int i=0; i<dates.length; i++) {
			datumi.add(dates[i]);
		}
		
		Apartment a = new Apartment(type,roomsNumber,guestsNumber,location,datumi,host_username,slike,price,amenties,v_prijave,v_odjave);
		a.setId(apartments.size()+1);
	    a.setStatus("ACTIVE");
	    a.setCity(city);
	    a.setCountry("Srbija");
	    a.setHost_username(host);
	    apartments.put(a.getId(),a);
	    saveApartments();
	}
	
	
	
	
	
	@SuppressWarnings("resource")
	public void saveApartments() {
		System.out.println("--cuvanje apartmana--");
		JSONArray list_apartments = new JSONArray(); 
	
		for (Long apartments_key : apartments.keySet()) {
		        Apartment a = apartments.get(apartments_key);
		    	JSONObject obj_apartment = new JSONObject();
		    	
		    	obj_apartment.put("id", a.getId());
		    	obj_apartment.put("type",a.getType());
		    	obj_apartment.put("roomsNumber", a.getRoomsNumber());
		    	obj_apartment.put("guestsNumber", a.getGuestsNumber());
		    	
		    	JSONObject location = new JSONObject();
		    	if(a.getLocation()!=null) {
		    		location.put("latitude",a.getLocation().getLatitude());
			    	location.put("longitude",a.getLocation().getLatitude());
			    	location.put("address",a.getLocation().getAddress());
			    	obj_apartment.put("location",location);
		    	}
		    	
		    	
		    	JSONArray rDates = new JSONArray();
		    	if(!a.getRentDates().isEmpty()) {
			    	for(String d: a.getRentDates()) {
			    		 rDates.add(d);
			    	}
		    	}
		    	obj_apartment.put("rDates",rDates);

		    	JSONArray aDates = new JSONArray();
		    	if(!a.getAvailableDates().isEmpty()) {
			    	for(String d: a.getAvailableDates()) {
			    		aDates.add(d);
			    	}
		    	}
		    	obj_apartment.put("aDates",aDates);
		    	
		        
		        obj_apartment.put("host",a.getHost_username());
		        
		        
		      /*  JSONArray comments = new JSONArray();
		        if(!a.getComments().isEmpty()) {
		        	for(Comment c: a.getComments()) {
		        		comments.add(c.getId());
		        	}
		        }
		        obj_apartment.put("comments", comments);*/
		    	
		    	JSONArray images = new JSONArray();
		    	if(!a.getImages().isEmpty()) {
		    		for(String i : a.getImages()) {
		    			images.add(i);
		    		}
		    	}
		    	obj_apartment.put("images", images);
		    	
		    	obj_apartment.put("price", a.getPrice());
		    	obj_apartment.put("check_in",a.getCheck_in_time());
		    	obj_apartment.put("check_out", a.getCheck_out_time());
		    	obj_apartment.put("status", a.getStatus());
		    	
		    	JSONArray amenties = new JSONArray();
		    	if(!a.getAmenties().isEmpty()) {
		    		for(long l: a.getAmenties()) {
		    			amenties.add(l);
		    		}
		    	}
		    	obj_apartment.put("amenties",amenties);
		    	
		    	JSONArray reservations = new JSONArray();
		    	if(!a.getReservations().isEmpty()) {
		    		for(Long r: a.getReservations()) {
		    			reservations.add(r);
		    		}
		    	}
		    	obj_apartment.put("reservations", reservations);
		    	
		    	obj_apartment.put("active", a.isActive());
		    	obj_apartment.put("city", a.getCity());
		    	obj_apartment.put("country", a.getCountry());
		    	
		    	
		    	list_apartments.add(obj_apartment);
		 } try {
				System.out.println(path+"/data/apartments.json");		
				FileWriter fw = new FileWriter(path+"/data/apartments.json"); 
				fw.write(list_apartments.toJSONString());
				fw.flush();
		}catch(Exception e) {
				e.printStackTrace();
	    }   
		
	}
	
	
	
	public void loadApartments() {
		System.out.println("---ucitavanje apartmana---");
		System.out.println(path);
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(path+"/data/apartments.json"));
					
			JSONArray jsonArray = (JSONArray) obj;
			System.out.println(jsonArray);
						
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject jsonObject = iterator.next(); //jedan json apartman
				Apartment a = new Apartment();
				a.setId((long)jsonObject.get("id"));
				a.setRoomsNumber(((Long)jsonObject.get("roomsNumber")).intValue());
				a.setGuestsNumber(((Long)jsonObject.get("guestsNumber")).intValue());
				
				JSONObject loc_json = (JSONObject) jsonObject.get("location");
				Location location = new Location();
				location.setLatitude((double)loc_json.get("latitude"));
				location.setLongitude((double)loc_json.get("longitude"));
				location.setAddress((String)loc_json.get("address"));
				a.setLocation(location);
				
				JSONArray rDates = (JSONArray) jsonObject.get("rDates");
				List<String> rentDates = new ArrayList<String>();
				Iterator<String> iter = rDates.iterator();
				while(iter.hasNext()) {
					String d = (String) iter.next();	
					rentDates.add(d);
				}
				a.setRentDates(rentDates);
				
				
				JSONArray aDates = (JSONArray) jsonObject.get("aDates");
				System.out.println("HEREEEE"+aDates);
				List<String> availableDates = new ArrayList<String>();
				Iterator<String> iter2 = aDates.iterator();
				while(iter2.hasNext()) {
					String d = (String) iter2.next();	
					availableDates.add(d);
				}
				a.setAvailableDates(availableDates);
				
				a.setHost_username((String)jsonObject.get("host"));
				a.setType((String)jsonObject.get("type"));
				
				/*JSONArray aComments = (JSONArray) jsonObject.get("comments");
				List<Comment> comments = new ArrayList<Comment>();
				Iterator<String> iter3 = aComments.iterator();
				while(iter3.hasNext()) {
					String d = (String) iter3.next();	
					comments.add(d);
				}
				a.setComments(comments);*/
				
				
				JSONArray imagesJSON = (JSONArray) jsonObject.get("images");
				List<String> images = new ArrayList<String>();
				Iterator<String> iter4 = imagesJSON.iterator();
				while(iter4.hasNext()) {
					String img = (String) iter4.next();
					images.add(img);
				}
				a.setImages(images);
				System.out.println("UCITAVANJE"+images.size());
				
				a.setPrice((double)jsonObject.get("price"));
				a.setCheck_in_time((String)jsonObject.get("check_in"));
				a.setCheck_out_time((String)jsonObject.get("check_out"));
				a.setStatus((String)jsonObject.get("status"));
				
				JSONArray amenties = (JSONArray) jsonObject.get("amenties");
				List<Long> ament = new ArrayList<Long>();
				Iterator<Long> iter5 = amenties.iterator();
				while(iter5.hasNext()) {
					long d = (long)iter5.next();
					ament.add(d);
				}
				a.setAmenties(ament);
				
				
				JSONArray reservations = (JSONArray) jsonObject.get("reservations");
				List<Long> res = new ArrayList<Long>();
				Iterator<Long> iter6 = reservations.iterator();
				while(iter6.hasNext()) {
					long d = (long)iter6.next();
					res.add(d);
				}
				a.setReservations(res);
				
				
				a.setActive((boolean) jsonObject.get("active"));
				a.setCity((String)jsonObject.get("city"));
				a.setCountry((String)jsonObject.get("country"));
				
				apartments.put(a.getId(),a);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	

	

}
