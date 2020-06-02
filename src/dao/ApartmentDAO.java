package dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Apartment;



public class ApartmentDAO {
	
	private HashMap<Long, Apartment> apartments;       //mapa svih apartmana
	private String path;                               //putanja do contexta
	
	public ApartmentDAO(String path) {
		this.apartments = new HashMap<Long, Apartment>();
		this.path=path;
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
		System.out.println("dodavanje apartmana");
	    apartment.setId(apartments.size()+1);
	    apartments.put(apartment.getId(),apartment);
	    saveApartments();
	}
	
	public void saveApartments() {
		System.out.println("cuvanje apartmana");
		JSONArray list = new JSONArray();                  //lista u koju sve apartmane ubacujem
		for (Long apartments_key : apartments.keySet()) {
		        Apartment a = apartments.get(apartments_key);
		    	JSONObject obj = new JSONObject();
		    	JSONObject loc = new JSONObject();
		    	JSONArray rDates = new JSONArray();
		    	JSONArray aDates = new JSONArray();
		    	obj.put("id", a.getId());
		    	obj.put("type",a.getType());
		    	obj.put("roomsNumber", a.getRoomsNumber());
		    	obj.put("guestsNumber", a.getGuestsNumber());
		    	//location
		    	loc.put("latitude",a.getLocation().getLatitude());
		    	loc.put("longitude",a.getLocation().getLatitude());
		    	loc.put("address",a.getLocation().getAddress());
		    	//add location json object into apartment json object
		    	obj.put("location",loc);
		    	/*for(Date d: a.getRentDates()) {
		    		rDates.add(d);
		    	}
		    	for(Date d: a.getAvailableDates()) {
		    		aDates.add(d);
		    	}
		    	obj.put("rDates",rDates);
		    	obj.put("aDates",aDates);*/
		    	System.out.println(a.getGuestsNumber());
		    	list.add(obj);
		 } try {
				System.out.println(path+"/data/apartments.json");		
				FileWriter fw = new FileWriter(path+"/data/apartments.json"); 
				fw.write(list.toJSONString());
				fw.flush();
		}catch(Exception e) {
				e.printStackTrace();
	    }   
		
	}
	
	public void loadApartmnets() {
		System.out.println("ucitavanje apartmana");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(path+"/data/apartments.json"));
			//parsing the JSON string inside the file that we created earlier.
			
			JSONArray jsonArray = (JSONArray) obj;
			System.out.println(jsonArray);
			//Json string has been converted into JSONArray
			
			Iterator<JSONObject> iterator = jsonArray.iterator();
			//Iterator is used to access the each element in the list 
			//loop will continue as long as there are elements in the array.
			while (iterator.hasNext()) {
				JSONObject jsonObject = iterator.next();
				Apartment a = new Apartment();
				a.setId((long)jsonObject.get("id"));
				a.setRoomsNumber(((Long)jsonObject.get("roomsNumber")).intValue());
				a.setGuestsNumber(((Long)jsonObject.get("guestsNumber")).intValue());
				System.out.println(a.getRoomsNumber());
				JSONObject loc_json = (JSONObject) jsonObject.get("location");
				System.out.println(loc_json.toJSONString());
				
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
