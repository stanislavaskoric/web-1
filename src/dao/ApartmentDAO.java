package dao;

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


@SuppressWarnings("unchecked")
public class ApartmentDAO {
	
	private HashMap<Long, Apartment> apartments;       //mapa svih apartmana
	private String path;                               //putanja do contexta
	
	public ApartmentDAO(String path) {
		this.apartments = new HashMap<Long, Apartment>();
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
	
	
	
	
	
	public Apartment findApartmentById(Long id) {
		return apartments.get(id);
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
	
	
	
	public List<AdDTO> findActiveAparment(){  //ubaciti da se samo aktivni vracaju
		List<AdDTO> ret = new ArrayList<AdDTO>();
		for(Apartment a: apartments.values()) {
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
	
	
	public Collection<Apartment> getSort(String type){
		List<Apartment> ret = new ArrayList<Apartment>(apartments.values());
	    Comparator<Apartment> compareByCost = (Apartment o1, Apartment o2) -> Double.compare(o1.getPrice(), o2.getPrice());
	    if(type.equals("asceding")) {
	    	Collections.sort(ret, compareByCost);
	    }else {
	    	Collections.sort(ret, compareByCost.reversed());
	    }
	    return ret;
	}
	
	
	
	
	public Collection<AdDTO> getSearchApartment(String dO, String dD, String cO, String cD, String sO, String sD, String o){
		ArrayList<AdDTO>ret = new ArrayList<AdDTO>();
		List<String> datesOpseg = getDaysBetweenDates(dO,dD);
		for(Apartment a: apartments.values()) {
			if(opsegDate(datesOpseg,a.getRentDates())) {  //promeni ovde available dates
				System.out.println("cene");
				if(opsegCena(a.getPrice(),cO,cD)) {
					System.out.println("sobe");
					if(opsegSoba(a.getRoomsNumber(),sO,sD)) {
						System.out.println("osobe");
						if(o.length()>0) {
							int brojOsoba = Integer.parseInt(o);	
							if(brojOsoba == a.getGuestsNumber()) {  
								 AdDTO temp = new AdDTO(a);
								 ret.add(temp);
							}
						}else {
							 AdDTO temp = new AdDTO(a);
							 ret.add(temp);
						}
					}else { //broj soba nije u opsegu
					
					}
				}else {  //cena nije u opsegu
					
				}
			}else {  //datum nije u opsegu
				
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
	
	
	
	public void addApartment(ApartmentDTO apartment) {
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
		
		String host_username = apartment.getDomacin();		
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
	    a.setStatus("INACTIVE");
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
		        
		        
		        JSONArray comments = new JSONArray();
		        if(!a.getComments().isEmpty()) {
		        	for(Comment c: a.getComments()) {
		        		comments.add(c.getId());
		        	}
		        }
		        obj_apartment.put("comments", comments);
		    	
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
		    		for(Reservation r: a.getReservations()) {
		    			reservations.add(r.getId());
		    		}
		    	}
		    	obj_apartment.put("reservations", reservations);
		    	
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
				
				JSONArray aComments = (JSONArray) jsonObject.get("comments");
				List<Comment> comments = new ArrayList<Comment>();
				Iterator<String> iter3 = aComments.iterator();
				while(iter3.hasNext()) {
					String d = (String) iter3.next();	
					//comments.add(d);
				}
				a.setComments(comments);
				
				
				JSONArray imagesJSON = (JSONArray) jsonObject.get("images");
				List<String> images = new ArrayList<String>();
				Iterator<String> iter4 = imagesJSON.iterator();
				while(iter4.hasNext()) {
					String img = (String) iter4.next();
					images.add(img);
				}
				a.setImages(images);
				
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
				
				//reservations
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
