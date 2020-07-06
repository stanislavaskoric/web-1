package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Amenties;
import dao.AmentiesDAO;



@Path("amenties")
public class AmentiesService {


	@Context
	ServletContext servletContext;
	
	@Context
	HttpServletRequest request;
	
	public AmentiesService() {
		
	}

	@PostConstruct
	public void init() {
		if(servletContext.getAttribute("amenties") == null) {
			String p = servletContext.getRealPath("")+"/data";                
			servletContext.setAttribute("amenties", new AmentiesDAO(p));
		}		
	}
	
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addAmenties(@QueryParam("name")String name) {
	   System.out.println("****ADD INTO AMENTIES*****");
	  // System.out.println(name);
	   AmentiesDAO amenties = (AmentiesDAO) servletContext.getAttribute("amenties");
	   boolean isAdd = amenties.addAmentie(name);
	   if(isAdd) {
		   return  Response.status(200).entity("Uspesno dodato!").build();
	   }else {
		   return Response.status(400).entity("Element vec postoji u listi sadrzaja!").build();
	 }	   
	}
	
	
	@PUT
	@Path("/")
	public Response updateAmenties(@QueryParam("oldName")String oldName, @QueryParam("newName")String newName) {
		  System.out.println("****UPDATE AMENTIE*****");	
		  System.out.println(oldName+" "+newName);
		  AmentiesDAO amenties = (AmentiesDAO) servletContext.getAttribute("amenties");
		  int fleg = amenties.updateAmentie(oldName, newName);
		  System.out.println("FLEG"+fleg);
		  if(fleg == 1) {
			   return  Response.status(200).entity("Uspesno izmenjen naziv stavke!").build();
		  }else if(fleg == 2){
			   return Response.status(400).entity("Element ne postoji postoji u listi sadrzaja!").build();
		  }else{
			  return Response.status(400).entity("Novi naziv stavke je zauzet.Pokusajte opet!").build();
		  }
	}
	
	
	
	@DELETE
	@Path("/")
	public Response deleteAmenties(@QueryParam("name")String name) {
		 System.out.println("****DELETE AMENTIE*****");	            //sada taj sadrzaj treba izbrisati i iz svih apartmana
		 System.out.println(name);
		 AmentiesDAO amenties = (AmentiesDAO) servletContext.getAttribute("amenties");
		 boolean isDeleted = amenties.deleteAmentie(name);
		  if(isDeleted) {
			   return  Response.status(200).entity("Uspesno obrisana stvka sadrzaja apartmana!").build();
		   }else {
			   return Response.status(400).entity("Element ne postoji postoji u listi sadrzaja!").build();
		   }		 
	}
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Amenties> getAmenties(){
		System.out.println("****GET AMENTIES*****");
	    AmentiesDAO amenties = (AmentiesDAO) servletContext.getAttribute("amenties");
	    return amenties.findAllActiveAmenties();
	}
	
}
