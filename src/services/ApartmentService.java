package services;

import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Apartment;
import dao.ApartmentDAO;
import dto.AdDTO;
import dto.ApartmentDTO;

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
		if(servletContext.getAttribute("apartments") == null) {
			String p = servletContext.getRealPath("");                
			servletContext.setAttribute("apartments", new ApartmentDAO(p));
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(ApartmentDTO apartment) {
		System.out.println("*****ADD APARTMENT*****");
		
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		apartments.addApartment(apartment);
		//apartments.loadApartmnets();
		return  Response.status(200).build();
		
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AdDTO> getActiveApartment() {
		System.out.println("*****GET APARTMENT*****");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.findActiveAparment();
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AdDTO> searchApartment(@QueryParam("start")String startDate, @QueryParam("end")String endDate,
			                       @QueryParam("cO")String cO, @QueryParam("cD")String cD,
			                       @QueryParam("sO")String sO, @QueryParam("sD")String sD,
			                       @QueryParam("o")String o) {
		System.out.println("*****SEARCH APARTMENT*****");
		System.out.println(startDate);
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.getSearchApartment(startDate, endDate, cO, cD, sO, sD, o);
	}
	
	
	@GET
	@Path("/sort")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Apartment> getSortApartment(@QueryParam("type")String type) {
		System.out.println("*****GET APARTMENT*****");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return apartments.getSort(type);
	}
	

}
