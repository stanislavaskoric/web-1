package dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import beans.Admin;
import beans.Apartment;
import beans.Guest;
import beans.Host;
import beans.Reservation;
import beans.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



@SuppressWarnings("unchecked")
public class UserDAO {
	
	private HashMap<String, Admin> admins;
	private HashMap<String, Host> hosts;
	private HashMap<String, Guest> guests;
	private String path;                       
	
	
	public UserDAO(String path) {
		this.admins = new HashMap<String, Admin>();
		this.hosts = new HashMap<String, Host>();
		this.guests = new HashMap<String, Guest>();
		this.setPath(path);
		System.out.println(path);
		loadUsers(path+"/admins.json");
		loadUsers(path+"/hosts.json");
		loadUsers(path+"/guests.json");
	}
	
	public HashMap<String, Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(HashMap<String, Admin> admins) {
		this.admins = admins;
	}

	public HashMap<String, Host> getHosts() {
		return hosts;
	}

	public void setHosts(HashMap<String, Host> hosts) {
		this.hosts = hosts;
	}

	public HashMap<String, Guest> getGuests() {
		return guests;
	}

	public void setGuests(HashMap<String, Guest> guests) {
		this.guests = guests;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	public User addUser(User user) {
		System.out.println("add user");
		String username = user.getUsername();
		if((admins.get(username)==null) && (hosts.get(username)==null) && (guests.get(username)==null)) {
			if(user.getRole().equals("HOST")) {
				addHost(user);
			}else {
				addGuest(user);
			}			
		}else {
			user = null;  //ovakav korisnik vec postoji u sistemu
		}
		return user;
	}
	
	public void addHost(User user) {
		Host h = new Host(user);
		System.out.println("HOST" + h.getUsername());
		hosts.put(h.getUsername(),h);
		try {
		    saveHosts();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addGuest(User user) {
		Guest g = new Guest(user);
		System.out.println("GUEST" + g.getUsername());
		guests.put(g.getUsername(),g);
		try {
		    saveGuests();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@SuppressWarnings("resource")
	public void saveHosts() {
		 System.out.println("save hosts");
		 JSONArray list = new JSONArray();
		 for (String host_key : hosts.keySet()) {
		    	Host host = hosts.get(host_key);
		    	JSONObject obj = new JSONObject();
		    	JSONArray apartments = new JSONArray();
		    	System.out.println("HERE" + host.getUsername());
		        obj.put("username",host.getUsername());
		    	obj.put("password",host.getPassword());
		    	obj.put("firstName",host.getFirstName());
		    	obj.put("lastName",host.getLastName());
		    	obj.put("gender", host.getGender());
		    	obj.put("role",host.getRole());
		    	if(!host.getRentApartments().isEmpty()){
		    		for(Apartment a: host.getRentApartments()) {
		    			apartments.add(a.getId());
		    		}
		    	}
		        obj.put("rentApartments",apartments);	    	
		    	list.add(obj);
		   }
		   try{
				System.out.println(path+"/hosts.json");		
				FileWriter fw = new FileWriter(path+"/hosts.json"); 
				fw.write(list.toJSONString());
				fw.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}   
	}
	
	
	@SuppressWarnings("resource")
	public void saveGuests() {
		 System.out.println("save guests");
		 JSONArray list = new JSONArray();
		 for (String guest_key : guests.keySet()) {
		    	Guest guest= guests.get(guest_key);
		    	JSONObject obj = new JSONObject();
		    	JSONArray apartments = new JSONArray();
		    	JSONArray reservations = new JSONArray();
		    	System.out.println("HERE" + guest.getUsername());
		        obj.put("username",guest.getUsername());
		    	obj.put("password",guest.getPassword());
		    	obj.put("firstName",guest.getFirstName());
		    	obj.put("lastName",guest.getLastName());
		    	obj.put("gender", guest.getGender());
		    	obj.put("role",guest.getRole());
		    	if(!guest.getRentedApartments().isEmpty()){
		    		for(Apartment a: guest.getRentedApartments()) {
		    			apartments.add(a.getId());
		    		}
		    	}
		    	if(!guest.getReservations().isEmpty()) {
		    		for(Reservation r: guest.getReservations()) {
		    			reservations.add(r.getId());
		    		}
		    	}
		        obj.put("rentedApartments",apartments);	
		        obj.put("reservations", reservations);
		    	list.add(obj);
		   }
		   try{
				System.out.println(path+"/guests.json");		
				FileWriter fw = new FileWriter(path+"/guests.json"); 
				fw.write(list.toJSONString());
				fw.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}   
	}
	
	
	public void loadUsers(String loadPath) {
		System.out.println("load users");
		JSONParser parser = new JSONParser(); 
		//JsonParser to convert JSON string into Json Object
		try {
			Object obj = parser.parse(new FileReader(loadPath));
			//parsing the JSON string inside the file that we created earlier.
			
			JSONArray jsonArray = (JSONArray) obj; //tu mi je sada lista json objekata
			System.out.println(jsonArray);
			//Json string has been converted into JSONArray
			
			Iterator<JSONObject> iterator = jsonArray.iterator();
			//Iterator is used to access the each element in the list 
			//loop will continue as long as there are elements in the array.
			while (iterator.hasNext()) {
				JSONObject jsonObject = iterator.next();
				User user = new User();
				user.setUsername((String) jsonObject.get("username"));
				user.setPassword((String) jsonObject.get("password"));
				user.setFirstName((String) jsonObject.get("firstName"));
				user.setLastName((String) jsonObject.get("lastName"));
				user.setGender((String) jsonObject.get("gender"));
				user.setRole((String)jsonObject.get("role"));
				
				if(user.getRole().equals("HOST")) {
					Host h = new Host(user);
					JSONArray apartments = (JSONArray)jsonObject.get("rentApartments");
					if(!apartments.isEmpty()) {
						for(int i=0; i<apartments.size(); i++) {
							//nadjem apartman sa tim idijem i dodam ga u hosta
						}
					}
					hosts.put(h.getUsername(), h);
				}else if(user.getRole().equals("GUEST")) {
					Guest g = new Guest(user);
					JSONArray rentedApartments = (JSONArray)jsonObject.get("rentedApartments");
					if(!rentedApartments.isEmpty()) {
						
					}
					JSONArray reservations = (JSONArray)jsonObject.get("reservations");
					if(!reservations.isEmpty()) {
						
					}
					guests.put(g.getUsername(), g);
				}else {
					Admin a = new Admin(user);
					admins.put(a.getUsername(), a);
				}								
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 }

	public User find(String username) {
        User user = new User();
        Admin admin = admins.get(username);
        if(admin == null) {
        	Host host = hosts.get(username);
        	if( host== null) {
        		Guest guest = guests.get(username);
        		if( guest == null) {
        			user = null;
        		}else {
        			user = (User)guest;
        		}
        	}else {
        		user = (User)host;
        	}
        }else {
        	user = (User)admin;
        }
        if(user!=null)
        	System.out.println(user.getUsername() + user.getRole());
        return user;
	}
	
	
	
	
}
