package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

import beans.User;
import dao.UserDAO;

@Path("")
public class UserService {
	
	@Context
	ServletContext servletContext;
	
	@Context
	HttpServletRequest request;
	
	public UserService() {
			
    }
	
	@PostConstruct
	public void init() {
		if(servletContext.getAttribute("users") == null) {
			String p = servletContext.getRealPath("")+"/data";   //preuzimam putanju
			servletContext.setAttribute("users",new UserDAO(p)); //prosledjujem je kako bih je koristila u okviru metoda usera
		}
	}
	
	
	@POST
	@Path("/registration")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registration(User user) {
		System.out.println("*****REGISTRATION*****");
		
		UserDAO users = (UserDAO) servletContext.getAttribute("users");	// listu izvucem iz konteksta
		User u = (User) users.addUser(user);		                    // dodajem jednog usera
		
		if(u == null) {
			return Response.status(400).entity("Uneti username vec postoji!.").build();
		}
		return  Response.status(200).build();
	}
	
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user) {
		System.out.println("*****LOGIN*****");
		
		UserDAO users = (UserDAO) servletContext.getAttribute("users");
		User u = users.find(user.getUsername());
		
		if(u != null) {
			if(!u.getPassword().equals(user.getPassword())) {
				return Response.status(400).entity("Korisnicko ime ili lozinka nisu ispravni").build();
			}else {
				//System.out.println(u.getRole()+"JEKOOOO");
				HttpSession session = request.getSession();
				session.setAttribute("user", u);
				return  Response.status(200).build();
			}
		}else {
			return Response.status(400).entity("Korisnicko ime ili lozinka nisu ispravni").build();
		}
		
	}
	
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout() {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			session.invalidate();
			return Response.status(200).build();
		}
		else {
			return Response.status(400).entity("Korisnik je vec izlogovan").build();
		}
	}
	
	
	@GET                  
	@Path("/getActive")
	@Produces(MediaType.APPLICATION_JSON)
	public User getActiveUser() {            //iz sesije ucitavam ulogovanog korisnika
		System.out.println("****GET ACTIVE****");
		User u;
		try {
			u = (User) request.getSession(false).getAttribute("user");
			return u;
		} catch (NullPointerException e) {
			return null;	
		}
	}
	
	
	
	@GET
	@Path("/searchUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> searchUsers(@QueryParam("username") String username,
			                            @QueryParam("gender") String gender,
			                            @QueryParam("role") String role){
		System.out.println("***SEARCH USERS***");
		UserDAO users = (UserDAO) servletContext.getAttribute("users");
		Collection<User> ret = users.searchUser(username, gender, role);
		
		return ret;
	}
	
	@PUT
	@Path("/")
	public Response updateUser(User u) {
		System.out.println("***UPDATE USER***");
		System.out.println(u.getUsername());
		UserDAO users = (UserDAO) servletContext.getAttribute("users");
		users.updateUser(u);
		HttpSession session = request.getSession();
		session.setAttribute("user", u);
		return Response.status(200).build();
	}
	
	
   
	

}
