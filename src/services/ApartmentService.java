package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Apartment;
import dao.ApartmentDAO;

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
			String p = servletContext.getRealPath("")+"/data";                
			servletContext.setAttribute("apartments", new ApartmentDAO(p));
		}
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean add(Apartment apartment) {
		System.out.println("*****ADD APARTMENT*****");
		
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		apartments.addApartment(apartment);
		//apartments.loadApartmnets();
		return true;
		
	}

}
