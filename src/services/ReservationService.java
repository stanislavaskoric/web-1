package services;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import beans.Apartment;
import beans.Guest;
import beans.Host;
import beans.Reservation;
import beans.User;
import dao.AmentiesDAO;
import dao.ApartmentDAO;
import dao.CodeBookDAO;
import dao.ReservationDAO;
import dao.UserDAO;



@Path("/reservation")
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
		if(servletContext.getAttribute("amenties") == null) {
			String p = servletContext.getRealPath("")+"/data";                
			servletContext.setAttribute("amenties", new AmentiesDAO(p));
		}
		if(servletContext.getAttribute("apartments") == null) {
			String p = servletContext.getRealPath("");                
			AmentiesDAO amenties = (AmentiesDAO) servletContext.getAttribute("amenties");
			servletContext.setAttribute("apartments", new ApartmentDAO(p,amenties.getAmenties()));
		}
		if(servletContext.getAttribute("codebooks") == null) {
			String p = servletContext.getRealPath("")+"/data";                
			servletContext.setAttribute("codebooks", new CodeBookDAO(p));
		}
		if(servletContext.getAttribute("reservations") == null) {
			String p = servletContext.getRealPath("")+"/data";  
			UserDAO users = (UserDAO) servletContext.getAttribute("users");
			ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
			servletContext.setAttribute("reservations",new ReservationDAO(p,users.getSystemUsers(),apartments.getApartments())); 
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(Reservation reservation) {
		System.out.println("*****ADD RESERVATION*****");
		System.out.println(reservation);
		
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		CodeBookDAO codebooks = (CodeBookDAO) servletContext.getAttribute("codebooks");
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		
		
		double price = apartments.getPricePerNight(reservation.getApartment());
		List<LocalDate> holidays = codebooks.getHolidayDayes();
		List<LocalDate> available = apartments.getAvailableDays(reservation.getApartment());
		boolean fleg = reservations.addReservation(reservation,price,holidays,available);
		
		if(fleg) {
			apartments.changeAvailableDates(reservation.getStartDate(),reservation.getNightsNumber(), reservation.getApartment());
			Apartment a = apartments.getApartments().get(reservation.getApartment());
			Long newID = (long) reservations.getAllReservation().size();
			List<Long>appRes = a.getReservations();
			appRes.add(newID);
			a.setReservations(appRes);
			apartments.saveApartments();
			return Response.status(200).build();
		}else {
			return Response.status(400).entity("Rezervaciju nije moguce izvrsiti za navedene datume!").build();
		}
		
	}
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getAllReservations(){
		System.out.println("***Get Reservations-admin***");
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		return reservations.getAllReservation();
	}
	
	
	@GET
	@Path("/host")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getAllHostReservations(){
		System.out.println("***Get Reservations-host***");
		User user = (User) request.getSession(false).getAttribute("user");
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		return reservations.getAllHostReservation(user.getUsername());
	}
	
	
	@PUT
	@Path("/accept")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response acceptReservation(@QueryParam("id")Long id) {
		System.out.println("***Acept reservation+ "+id);
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		if(reservations.acceptReservation(id)) {
			return  Response.status(200).build();
		}else {
			return Response.status(400).entity("Ovu rezervaciju ne mozete prihvatiti!").build();
		}
	}
	
	@PUT
	@Path("/denied")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deniedReservation(@QueryParam("id")Long id) {
		System.out.println("***Denied reservation+ "+id);
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		if(reservations.deniedReservation(id)) {
			return  Response.status(200).build();
		}else {
			return Response.status(400).entity("Ovu rezervaciju ne mozete odbiti!").build();
		}
	}
	
	@PUT
	@Path("/complete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response completeReservation(@QueryParam("id")Long id) {
		System.out.println("***Denied reservation+ "+id);
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		if(reservations.completeReservation(id)) {
			return  Response.status(200).build();
		}else {
			return Response.status(400).entity("Ovu rezervaciju ne mozete odbiti!").build();
		}
	}
	
	
	@GET
	@Path("/guest")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getAllGuestReservations(){
		System.out.println("***Get Reservations-guest***");
		User user = (User) request.getSession(false).getAttribute("user");
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		return reservations.getAllGuestReservation(user.getUsername());
	}
	
	
	@PUT
	@Path("/cancel")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelReservation(@QueryParam("id")Long id) {
		System.out.println("***Cancel reservation+ "+id);
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		if(reservations.cancelReservation(id)) {
			return  Response.status(200).build();
		}else {
			return Response.status(400).entity("Od ove rezervacije ne mozete odustati!").build();
		}
	}
	
	
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getAllHostUsers(){
		System.out.println("***Get users by host***");
		User user = (User) request.getSession(false).getAttribute("user");
		System.out.println(user.getUsername());
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		return reservations.getAllUserByHost(user.getUsername());
	}
	
	@GET
	@Path("/searchUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> searchUsers(@QueryParam("username") String username,
			                            @QueryParam("gender") String gender,
			                            @QueryParam("role") String role){
		System.out.println("***SEARCH USERS***");
		System.out.println(username+" "+gender+" "+role);
		User user = (User) request.getSession(false).getAttribute("user");
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		Collection<User> ret = reservations.searchUserByHost(user.getUsername(),username, gender, role);
		
		return ret;
	}
	
	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getSearchReservations(@QueryParam("guest") String guest, List<Reservation>res){
		System.out.println("***Search Reservations***"+res.size());
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		return reservations.searchReservation(guest, res);
	}
	
	@POST
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getFilterReservations(@QueryParam("status") String status, List<Reservation>res){
		System.out.println("***Filter Reservations***"+res.size());
		System.out.println(status);
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		return reservations.filterReservation(status, res);
	}
	
	@POST
	@Path("/sort")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getSortReservations(@QueryParam("type") String type, List<Reservation>res){
		System.out.println("***Sort Reservations***");
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		return reservations.sortReservation(type, res);
	}
	
}
