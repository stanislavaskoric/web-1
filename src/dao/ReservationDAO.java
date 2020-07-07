package dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Apartment;
import beans.Guest;
import beans.Host;
import beans.Reservation;
import beans.User;

@SuppressWarnings("unchecked")
public class ReservationDAO {
	
	private HashMap<Long,Reservation> reservations;
	private HashMap<Long,Apartment> apartments;
	private HashMap<String, User> systemUsers;
	private String path;
	
	public ReservationDAO(String path,HashMap<String,User>systemUsers , HashMap<Long,Apartment>apartments) {
		this.reservations = new HashMap<Long, Reservation>();
		this.apartments = apartments;
		this.systemUsers = systemUsers;
		this.path = path;
		this.loadReservations();
	}
	
	public Collection<Reservation> searchReservation(String guest, List<Reservation>res){
		List<Reservation> ret = new ArrayList<Reservation>();
		for(Reservation r: res) {
			//System.out.println("rezervacijaaaaaaaaaaa"+r.getGuest());
			if(r.getGuest().toLowerCase().equals(guest.toLowerCase())) {
				//System.out.println("dodajem"+r.getGuest());
				ret.add(r);
			}
		}
		return ret;
	}
	
	public Collection<Reservation> filterReservation(String status, List<Reservation>res){
		List<Reservation> ret = new ArrayList<Reservation>();
		for(Reservation r: res) {
			if(r.getStatus().toLowerCase().contains(status.toLowerCase())) {
				ret.add(r);
			}
		}
		return ret;
	}
	
	public Collection<Reservation> sortReservation(String type, List<Reservation>ret){
		Comparator<Reservation> compareByCost = (Reservation o1, Reservation o2) -> Double.compare(o1.getFinalPrice(), o2.getFinalPrice());
	    if(type.equals("asceding")) {
	    	Collections.sort(ret, compareByCost);
	    }else {
	    	Collections.sort(ret, compareByCost.reversed());
	    }
	    return ret;
	}
	
	
	public Collection<User> getAllUserByHost(String u) {
		List<User> ret = new ArrayList<User>();
		for(Reservation r : reservations.values()) {
			Apartment a = apartments.get(r.getApartment());
			if(a.getHost_username().equals(u)) {
				User us = systemUsers.get(r.getGuest_username());
				if(!ret.contains(us)) {
					 ret.add(us);	
				}
			}
		}
		return ret;
	}
	
	
	public Collection<User> searchUserByHost(String host_username, String username, String gender, String role){
		System.out.println("---search user by host---");
		List<User> returnList = new ArrayList<User>();
		if(username ==null && gender==null && role==null) {
			return returnList;
		}
        
		List<User> usersForSearch = (List<User>) getAllUserByHost(host_username);
		
		for(User u: usersForSearch) {
			if(username.equals(u.getUsername()) || username.equals("")) {
				if(gender.equals(u.getGender()) || gender.equals("")) {
					if(role.toUpperCase().equals(u.getRole().toUpperCase()) || role.equals("")) {
						returnList.add(u);
					}
				}
			}
		}
		
		
		return returnList;
	}
	
	
	
	public Collection<Reservation> getAllReservation() {
		return reservations.values();
	}
	
	
	public List<Reservation> getApartmentReservation(Long id){
		List<Reservation> ret = new ArrayList<Reservation>();
		for(Reservation r: reservations.values()) {
			if(r.getApartment() == id) {
				ret.add(r);
			}
		}
		return ret;
	}
	
	
	public boolean canCreateComment(Long id_apartment, String guest) {
		List<Reservation> guestReservation = getApartmentReservation(id_apartment);
		boolean fleg = false;
		for(Reservation r: guestReservation) {
			if(r.getGuest_username().equals(guest)) {
				if(r.getStatus().equals("DENEID") || r.getStatus().equals("COMPLETED")) {
					fleg=true;
				}
						
			}
		}
		return fleg;
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
	
	
	public Collection<Reservation> getAllHostReservation(String u) {
		List<Reservation> ret = new ArrayList<Reservation>();
		for(Reservation r : reservations.values()) {
			Apartment a = apartments.get(r.getApartment());
			if(a.getHost_username().equals(u)) {
				if(!r.isFinish()) {
					finishReservation(r.getId());	
				}
				ret.add(r);
			}
		}
		return ret;
	}
	
	
	public boolean acceptReservation(Long id) {
		Reservation r = reservations.get(id);
		if(r.getStatus().equals("CREATE")) {
			r.setStatus("ACCEPT");
			return true;
		}
		return false;
	}
	
	
	public boolean deniedReservation(Long id) {
		Reservation r = reservations.get(id);
		if(r.getStatus().equals("CREATE") || r.getStatus().equals("ACCEPT") ) {
			r.setStatus("DENIED");
			return true;
		}
		return false;
	}
	
	public boolean completeReservation(Long id) {
		Reservation r = reservations.get(id);
		if(r.getStatus().equals("ACCEPT") && r.isFinish()) {
			r.setStatus("COMPLETE");
			return true;
		}
		return false;
	}
	
	public boolean cancelReservation(Long id) {
		Reservation r = reservations.get(id);
		if(r.getStatus().equals("CREATE") || r.getStatus().equals("ACCEPT") ) {
			r.setStatus("CANCEL");
			return true;
		}
		return false;
	}
	
	
	public void finishReservation(Long id) {
		Reservation r = reservations.get(id);
			String date = r.getStartDate();
			String []dateA = date.split("/");
			LocalDate ld = LocalDate.of(Integer.parseInt(dateA[2]),Integer.parseInt(dateA[0]),Integer.parseInt(dateA[1]));
			LocalDate finish_date = ld.plusDays(r.getNightsNumber());
			LocalDate today_day = LocalDate.now();
			if(today_day.isAfter(finish_date)) {
				r.setFinish(true);
			}
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
			json_res.put("finish", r.isFinish());
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
				
				boolean f = (boolean)jsonObject.get("finish");
				reservation.setFinish(f);
				
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
