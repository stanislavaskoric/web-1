package dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Apartment;
import beans.Guest;
import beans.Reservation;

@SuppressWarnings("unchecked")
public class ReservationDAO {
	
	private HashMap<Long,Reservation> reservations;
	private String path;
	
	public ReservationDAO(String path,HashMap<String,Guest>guests , HashMap<Long,Apartment>apartments) {
		this.reservations = new HashMap<Long, Reservation>();
		this.path = path;
		this.loadReservations();
	}
	
	
	public Collection<Reservation> getAllReservation() {
		return reservations.values();
	}
	
	
	public Collection<Reservation> getAllGuestReservation(String gu) {
		List<Reservation>ret = new ArrayList<Reservation>();
		for(Reservation r: reservations.values()) {
			if(gu.equals(r.getGuest())) {
				ret.add(r);
			}
		}
		return ret;
	}
	
	
	public boolean isAvailable(LocalDate ld, int n, List<LocalDate>ad) {
		for(int i=0; i<n; i++) {
			LocalDate temp = ld.plusDays(i+1);
			if(!ad.contains(temp)) {
				return false;
			}
		}
		return true;
	}
	
	
	
	public boolean addReservation(Reservation reservation, double pricePerNight, List<LocalDate> holidayDays,List<LocalDate> ad) {
		System.out.println("---add reservation---");
		//proveriti da li je apartman raspoloziv
		int nightsNumber = reservation.getNightsNumber();
		String date = reservation.getStartDate();
		System.out.println("SD"+date);
		String []dateA = date.split("/");
		LocalDate ld = LocalDate.of(Integer.parseInt(dateA[2]),Integer.parseInt(dateA[0]),Integer.parseInt(dateA[1]));
		System.out.println("SD"+ld);
		if(!isAvailable(ld,nightsNumber,ad)) {
			return false;
		}
		
		reservation.setId(reservations.size()+1);     
		reservation.setStatus("CREATE");
		
		double price = nightsNumber * pricePerNight;
		
		//variranje cene rezervacije
		DayOfWeek dayOfWeek = ld.getDayOfWeek();
		int numberOfDay = dayOfWeek.getValue();
		if(numberOfDay == 5 || numberOfDay == 6 || numberOfDay == 7) {
			price*= 0.9;
		}
		for(LocalDate hd: holidayDays) {
			if(hd.equals(ld)) {
				price*= 1.05;
			}
		}
		reservation.setFinalPrice(price);
		reservations.put(reservation.getId(),reservation);
		saveReservations();
		return true;
	}
	
	
	@SuppressWarnings("resource")
	public void saveReservations() {
		System.out.println("---save reservation---");
		JSONArray list = new JSONArray();
		for(Long id_res : reservations.keySet()) {
			Reservation r = reservations.get(id_res);
			JSONObject json_res = new JSONObject();
			json_res.put("id", r.getId());
			json_res.put("apartment", r.getApartment());
			json_res.put("startDate", r.getStartDate());
			json_res.put("nightsNumber", r.getNightsNumber());
			json_res.put("finalPrice", r.getFinalPrice());
			json_res.put("message", r.getMessage());
			json_res.put("guest", r.getGuest());
			json_res.put("status", r.getStatus());
			list.add(json_res);
		} try{
			System.out.println(path+"/reservations.json");		
			FileWriter fw = new FileWriter(path+"/reservations.json"); 
			fw.write(list.toJSONString());
			fw.flush();
		}catch(Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void loadReservations() {
		System.out.println("---load reservations---");
		JSONParser parser = new JSONParser(); 
		try {
			Object obj = parser.parse(new FileReader(path+"/reservations.json"));
			JSONArray jsonArray = (JSONArray) obj;
			System.out.println(jsonArray);
			
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject jsonObject = iterator.next();
				Reservation reservation = new Reservation();
				
				reservation.setId((Long)jsonObject.get("id"));
				reservation.setStartDate((String)jsonObject.get("startDate"));
				reservation.setNightsNumber(((Long)jsonObject.get("nightsNumber")).intValue());
				reservation.setFinalPrice((Double)jsonObject.get("finalPrice"));
				reservation.setMessage((String)jsonObject.get("message"));
				
				String username = (String)jsonObject.get("guest");
				reservation.setGuest(username);
				
				Long id_apartment = (Long)jsonObject.get("apartment");
				reservation.setApartment(id_apartment);
				
				reservation.setStatus((String)jsonObject.get("status"));
				reservations.put(reservation.getId(), reservation);			
			}			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	

}
