package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Apartment;
import beans.User;
import dao.AmentiesDAO;
import dao.ApartmentDAO;
import dao.UserDAO;
import dto.AdDTO;
import dto.ApartmentDTO;
import dto.ChangeDTO;

@Path("apartment")
public class ApartmentService {
	
	@Context
	ServletContext servletContext;
	
	@Context
	HttpServletRequest request;
	
	public ApartmentService() {
		
	}
	
	@PostConstruct
	public void init() {
		if(servletContext.getAttribute("amenties") == null) {
			String p = servletContext.getRealPath("")+"/data";                
			servletContext.setAttribute("amenties", new AmentiesDAO(p));
		}
		if(servletContext.getAttribute("apartments") == null) {
			String p = servletContext.getRealPath("");     
			AmentiesDAO amenties = (AmentiesDAO) servletContext.getAttribute("amenties");
			servletContext.setAttribute("apartments", new ApartmentDAO(p,amenties.getAmenties()));
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(ApartmentDTO apartment) {
		System.out.println("*****ADD APARTMENT*****");
		
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		User user = (User) request.getSession(false).getAttribute("user");
		apartments.addApartment(apartment, user.getUsername());
		//apartments.loadApartmnets();
		return  Response.status(200).build();
		
	}
	
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AdDTO> getActiveApartment() {
		System.out.println("*****GET ACTIVE APARTMENT*****");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.findActiveAparment();
	}
	
	
	@GET
	@Path("/admin")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AdDTO> getAllApartment() {
		System.out.println("*****GET ALL APARTMENT BY ADMIN*****");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.findAllAparment();
	}
	
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AdDTO> searchApartment(@QueryParam("start")String startDate, @QueryParam("end")String endDate,
			                       @QueryParam("cO")String cO, @QueryParam("cD")String cD,
			                       @QueryParam("sO")String sO, @QueryParam("sD")String sD,
			                       @QueryParam("o")String o, @QueryParam("c")String c,
			                       @QueryParam("dr")String dr,@QueryParam("sort")String sort) {
		System.out.println("*****SEARCH APARTMENT*****");
		System.out.println(c);
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		List<Apartment> aps = apartments.findSearchApartment(); ///U ZAVISNOSTI PO CEMU PRETRAZUJES
		return apartments.getSearchApartment(aps,startDate, endDate, cO, cD, sO, sD, o, c, dr, sort);
	}
	
	
	@GET
	@Path("/sort")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Apartment> getSortApartment(@QueryParam("type")String type) {
		System.out.println("*****GET APARTMENT*****");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.getSort(type, apartments.findNotDeletedApartment());
	}
	
	
	@GET
	@Path("/adates")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<String> getAvailableDatesForReservations(@QueryParam("id")Long id) {
		System.out.println("*****GET AVAILABLE DATES*****");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.getDateForReservation(id);
	}
	
	
	@GET
	@Path("/details")
	@Produces(MediaType.APPLICATION_JSON)
	public Apartment getApartmentById(@QueryParam("id")Long id) {
		System.out.println("*****GET APARTMENT DETAILS*****");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.findApartmentById(id);
	}
	
	
	@GET
	@Path("/amenties")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<String> getAmentiesFromApartment(@QueryParam("id")Long id) {
		System.out.println("*****GET AMENTIES FROM APARTMENT*****");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.getAmentiesFromApartment(id);
	}

	
	@DELETE
	@Path("/")
	public Response deleteApartment(@QueryParam("id")Long id) {
		 System.out.println("****DELETE APARTMENT*****");	     
		 ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		 boolean isDeleted = apartments.deleteApartment(id);
		  if(isDeleted) {
			   return  Response.status(200).entity("Uspesno obrisan apartman!").build();
		   }else {
			   return Response.status(400).entity("Ne mozete obrisati trazeni apartman!").build();
		   }		 
	}
	
	@POST
	@Path("/change")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response change(ChangeDTO apartment) {
		System.out.println("*****CHANGE APARTMENT*****");
		
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		apartments.changeApartment(apartment);
		return  Response.status(200).build();
		
	}
	
	
	
	@GET
	@Path("/a")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AdDTO> getAllActiveHostApartment() {
		System.out.println("*****GET ALL ACTIVE APARTMENT BY HOST*****");
		User user = (User) request.getSession(false).getAttribute("user");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.findAllHostActiveApartment(user.getUsername());
	}
	
	
	@GET
	@Path("/i")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AdDTO> getAllInactiveHostApartment() {
		System.out.println("*****GET ALL INACTIVE APARTMENT BY HOST*****");
		User user = (User) request.getSession(false).getAttribute("user");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.findAllHostInactiveApartment(user.getUsername());
	}
	
	
	@GET
	@Path("/hsort")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AdDTO> getSortHostApartment(@QueryParam("type")String type) {
		System.out.println("*****SORT APARTMENT BY HOST*****");
		User user = (User) request.getSession(false).getAttribute("user");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.getSortHost(type, user.getUsername());
	}
	
	
	@POST
	@Path("/hfilter")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AdDTO> getFilterHostApartment(@QueryParam("t")String t, @QueryParam("s")String s, List<String>a) {
		System.out.println("*****FILTER APARTMENT BY HOST*****");
		System.out.println(t+" "+s+" parametriiiii");
		User user = (User) request.getSession(false).getAttribute("user");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.getFilterHost(t, s, a,user.getUsername());
	}
	
}
