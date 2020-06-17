package dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import beans.Date;
import beans.Location;
import beans.Reservation;


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
	
	public void addApartment(Apartment apartment) {
		System.out.println("--dodavanje apartmana--");
	    apartment.setId(apartments.size()+1);
	    apartment.setStatus("INACTIVE");
	    apartments.put(apartment.getId(),apartment);
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
		    	
		    	
		    /*	JSONArray rDates = new JSONArray();
		    	if(!a.getRentDates().isEmpty()) {
			    	for(Date d: a.getRentDates()) {
			    		 rDates.add(d.getDate());
			    	}
		    	}
		    	obj_apartment.put("rDates",rDates);

		    	JSONArray aDates = new JSONArray();
		    	if(!a.getAvailableDates().isEmpty()) {
			    	for(Date d: a.getAvailableDates()) {
			    		aDates.add(d.getDate());
			    	}
		    	}
		    	obj_apartment.put("aDates",aDates);
		    	
		        if(a.getHost()!=null) {
		        	obj_apartment.put("host",a.getHost().getUsername());
		        }
		        
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
		    		for(Amenties amentie : a.getAmenties()) {
		    			JSONObject amentieJson = new JSONObject();
		    			amentieJson.put("id", amentie.getId());
		    			amentieJson.put("name", amentie.getName());
		    			amentieJson.put("description", amentie.getDescription());
		    			amenties.add(amentieJson);
		    		}
		    	}
		    	obj_apartment.put("amenties",amenties);
		    	
		    	JSONArray reservations = new JSONArray();
		    	if(!a.getReservations().isEmpty()) {
		    		for(Reservation r: a.getReservations()) {
		    			reservations.add(r.getId());
		    		}
		    	}
		    	obj_apartment.put("reservations", reservations);*/
		    	
		    	list_apartments.add(obj_apartment);
		 } try {
				System.out.println(path+"/apartments.json");		
				FileWriter fw = new FileWriter(path+"/apartments.json"); 
				fw.write(list_apartments.toJSONString());
				fw.flush();
		}catch(Exception e) {
				e.printStackTrace();
	    }   
		
	}
	
	public void loadApartments() {
		System.out.println("---ucitavanje apartmana---");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(path+"/apartments.json"));
					
			JSONArray jsonArray = (JSONArray) obj;
			System.out.println(jsonArray);
						
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject jsonObject = iterator.next(); //jedan json apartman
				Apartment a = new Apartment();
				a.setId((long)jsonObject.get("id"));
				a.setRoomsNumber(((Long)jsonObject.get("roomsNumber")).intValue());
				a.setGuestsNumber(((Long)jsonObject.get("guestsNumber")).intValue());
				
				/*JSONObject loc_json = (JSONObject) jsonObject.get("location");
				System.out.println(loc_json.toJSONString());
				Location location = new Location();
				location.setLatitude((double)loc_json.get("latitude"));
				location.setLongitude((double)loc_json.get("longitude"));
				location.setAddress((String)loc_json.get("address"));
				a.setLocation(location);
				
				/*JSONArray rDates = (JSONArray) jsonObject.get("rDates");
				List<Date> rentDates = new ArrayList<Date>();
				Iterator<String> iter = rDates.iterator();
				while(iter.hasNext()) {
					String d = (String) iter.next();	
					Date rdate = new Date(d);
					rentDates.add(rdate);
				}
				a.setRentDates(rentDates);
				
				JSONArray aDates = (JSONArray) jsonObject.get("aDates");
				List<Date> availableDates = new ArrayList<Date>();
				Iterator<String> iter2 = aDates.iterator();
				while(iter2.hasNext()) {
					String d = (String) iter2.next();	
					Date adate = new Date(d);
					availableDates.add(adate);
				}
				a.setRentDates(availableDates);
				
				//user
				
				//komentari
				
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
				
				JSONArray amentiesJSON = (JSONArray) jsonObject.get("amenties");
				List<Amenties> amentiesList = new ArrayList<Amenties>();
				Iterator<JSONObject> iter5 = amentiesJSON.iterator();
				while(iter5.hasNext()) {
					JSONObject amentie = iter5.next();
					Amenties amenties =new Amenties();
					amenties.setId((Long)amentie.get("id"));
					amenties.setName((String)amentie.get("name"));
					amenties.setDescription((String)amentie.get("description"));
					amentiesList.add(amenties);
				}
				a.setAmenties(amentiesList);*/
				
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
	
	
	public Apartment findApartmentById(Long id) {
		return apartments.get(id);
	}

	
}
