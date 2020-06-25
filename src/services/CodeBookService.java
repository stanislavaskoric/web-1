package services;


import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.CodeBookDAO;

@Path("codebook")
public class CodeBookService {
	
	@Context
	ServletContext servletContext;
	
	@Context
	HttpServletRequest request;
	
	public CodeBookService() {
		
	}
	
	
	@PostConstruct
	public void init() {
		if(servletContext.getAttribute("codebooks") == null) {
			String p = servletContext.getRealPath("")+"/data";                
			servletContext.setAttribute("codebooks", new CodeBookDAO(p));
		}		
	}
	
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCodeBook(@QueryParam("date")String date, @QueryParam("desc")String desc) {
	   System.out.println("****ADD INTO CODEBOOK*****");
	   CodeBookDAO codebooks = (CodeBookDAO) servletContext.getAttribute("codebooks");
	   System.out.println(date+" "+desc);
	   boolean isAdd = codebooks.addCodeBook(date, desc);
	   if(isAdd) {
		   return  Response.status(200).entity("Uspesno dodato!").build();
	   }else {
		   return Response.status(400).entity("Datum vec postoji u listi praznicnih datuma!").build();
	 }	   
	}

}
