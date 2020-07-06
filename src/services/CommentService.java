package services;

import java.util.Collection;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Apartment;
import beans.Comment;
import beans.Guest;
import beans.User;
import dao.AmentiesDAO;
import dao.ApartmentDAO;
import dao.CommentDAO;
import dao.ReservationDAO;
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
		if(servletContext.getAttribute("amenties") == null) {
			String p = servletContext.getRealPath("")+"/data";                
			servletContext.setAttribute("amenties", new AmentiesDAO(p));
		}
		if(servletContext.getAttribute("apartments") == null) {
			String p = servletContext.getRealPath("");                
			AmentiesDAO amenties = (AmentiesDAO) servletContext.getAttribute("amenties");
			servletContext.setAttribute("apartments", new ApartmentDAO(p,amenties.getAmenties()));
		}
		if(servletContext.getAttribute("reservations") == null) {
			String p = servletContext.getRealPath("")+"/data";  
			UserDAO users = (UserDAO) servletContext.getAttribute("users");
			ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
			servletContext.setAttribute("reservations",new ReservationDAO(p,users.getSystemUsers(),apartments.getApartments())); 
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
	public Response addComment(Comment comment) {
		System.out.println("*****ADD COMMENT*****");
		
		User user = (User) request.getSession(false).getAttribute("user");
		CommentDAO comments = (CommentDAO) servletContext.getAttribute("comments");
		ReservationDAO reservations = (ReservationDAO) servletContext.getAttribute("reservations");
		
		if(reservations.canCreateComment(comment.getApartment(), user.getUsername())) {  
			System.out.println("kreiram komentar");
			comment.setGuest(user.getUsername()); 
			comments.addComment(comment);
			return Response.status(200).build();
		}else {
			return Response.status(400).entity("Nije vam dozvoljeno ostavljanje komentara!").build();
		}			
	}
	
		
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getAllByAdmin(){
		System.out.println("*****GET ALL COMMENT*****");
		CommentDAO comments = (CommentDAO) servletContext.getAttribute("comments");
		return comments.getAllAdmin();
	}
	
	
	@GET
	@Path("/guest")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getAllByGuest(@QueryParam("id")Long id){
		System.out.println("*****GET ALL COMMENT BY GUESTS*****");
		CommentDAO comments = (CommentDAO) servletContext.getAttribute("comments");
		return comments.getActiveComment(id);
	}
	
	@GET
	@Path("/host")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getAllByHost(){
		System.out.println("*****GET ALL COMMENT BY HOST*****");
		User user = (User) request.getSession(false).getAttribute("user");
		CommentDAO comments = (CommentDAO) servletContext.getAttribute("comments");
		ApartmentDAO apartments = (ApartmentDAO) servletContext.getAttribute("apartments");
		return comments.getHostsComment(apartments.findHostsApartment(user.getUsername()));
	}
	
	
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response availableComment(@QueryParam("id")Long id) {
		System.out.println("*****AVAILABLE COMMENT*****");
		CommentDAO comments = (CommentDAO) servletContext.getAttribute("comments");
		comments.chooseComment(id);
		return Response.status(200).build();
	}
	
}
