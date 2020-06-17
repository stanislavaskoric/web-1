package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import beans.Apartment;
import beans.Guest;
import beans.Reservation;
import dao.ApartmentDAO;
import dao.ReservationDAO;
import dao.UserDAO;



@Path("reservation")
public class ReservationService {
	
	@Context
	ServletContext servletContext;
	
	@Context
	HttpServletRequest request;
	
	
	public ReservationService() {
		
	}

	@PostConstruct
	public void init() {
		if(servletContext.getAttribute("users") == null) {
			String p = servletContext.getRealPath("")+"/data";   
			servletContext.setAttribute("users",new UserDAO(p));
		}
		if(servletContext.getAttribute("apartments") == null) {
			String p = servletContext.getRealPath("")+"/data";                
			servletContext.setAttribute("apartments", new ApartmentDAO(p));
		}
		if(servletContext.getAttribute("reservations") == null) {
			String p = servletContext.getRealPath("")+"/data";  
			UserDAO users = (UserDAO) servletContext.getAttribute("users");
			ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
			servletContext.setAttribute("reservations",new ReservationDAO(p,users.getGuests(),apartments.getApartments())); 
		}
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(Reservation reservation, @QueryParam("username") String username, @QueryParam("id") Long id) {
		System.out.println("*****ADD RESERVATION*****");
		
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		UserDAO users = (UserDAO) servletContext.getAttribute("users");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		
		System.out.println("parametri"+username +id);
		Guest g = users.findGuest(username);
		Apartment a = apartments.findApartmentById(id);
		reservations.addReservation(reservation,g,a);
		
		return Response.status(200).build();
		
	}
	
	
	
	
}
