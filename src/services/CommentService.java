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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Apartment;
import beans.Comment;
import beans.Guest;
import dao.ApartmentDAO;
import dao.CommentDAO;
import dao.UserDAO;

@Path("comment")
public class CommentService {

	@Context
	ServletContext servletContext;
	
	@Context
	HttpServletRequest request;
	
	public CommentService() {
		
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
		if(servletContext.getAttribute("comments") == null) {
			String p = servletContext.getRealPath("")+"/data";  
			UserDAO users = (UserDAO) servletContext.getAttribute("users");
			ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
			servletContext.setAttribute("comments",new CommentDAO(p,users.getGuests(),apartments.getApartments())); 
		}
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addComment(Comment comment, @QueryParam("username") String username, @QueryParam("id") Long id) {
		System.out.println("*****ADD COMMENT*****");
		
		CommentDAO comments = (CommentDAO) servletContext.getAttribute("comments");
		UserDAO users = (UserDAO) servletContext.getAttribute("users");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
	
		Guest g = users.findGuest(username);
		Apartment a = apartments.findApartmentById(id);
		comments.addComment(comment,g,a);
		
		return Response.status(200).build();
	}
	
}
