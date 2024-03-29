package nz.ac.auckland.services;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.persistence.*;

import org.jboss.logging.Param;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import nz.ac.auckland.dto.UserDTO;
import nz.ac.auckland.userDetail.Address;
import nz.ac.auckland.userDetail.User;

/**
 * Class to implement a simple REST Web service for managing accounts.
 * 
 * UserResource implements a WEB service with the following interface:
 * - GET    <base-uri>/user/{id}
 *          Retrieves a user based on their unique id. The format of the 
 *          returned data is XML.
 *          
 * - GET    <base-uri>/user?start&size
 *          Retrieves a collection of user, where the "start" query 
 *          parameter identifies an index position, and "size" represents the 
 *          max number of successive user to return. The format of the 
 *          returned data is XML.
 *          
 * - POST   <base-uri>/user
 *          Creates a new user. The HTTP post message contains an XML 
 *          representation of the user to be created.
 *          
 * - PUT    <base-uri>/user/{id}
 *          Updates a user, identified by their id.The HTTP PUT message
 *          contains an XML document describing the new state of the user.
 *          
 * - DELETE <base-uri>/user/{id}
 *          Deletes a user, identified by their unique id.
 * 
 * @author Ian Warren
 *
 */

@Path("/user")
public class UserResource {
	
	//Set up Logger
	private static Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("shoppingPU");
	
	
	

	@GET
	@Path("/{id}")
	@Produces("application/xml")
	public UserDTO getUser(@PathParam("id") int id) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		logger.debug("Read user with id: " + id);
		
		User user = em.find(User.class, (long)id);
		
		if(user == null){
			// Return a HTTP 404 response if the specified Parolee isn't found.
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		
		UserDTO dto = UserMapper.toDTO(user);		
		em.getTransaction().commit();
		em.close();
		
		return dto;		
	}

	
	@POST
	@Consumes("application/xml")
	public Response createUser(UserDTO user) {		
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();	
		
		logger.debug("Read user: " + user);
		
		User ur = UserMapper.toDomainModel(user);	
		
		logger.debug("Created user: " + ur);
		
		logger.debug("Path: " + ur);
		
		em.persist(ur);		
		em.getTransaction().commit();
		em.close();

		return Response.created(URI.create("/user/" + ur.getId()))
				.build();
	}

	
	@GET
	@Path("/{id}/cookie")
	@Produces("application/xml")
	public Response createCookie(@PathParam("id")int id) {
//		logger.info("Retrieving account with id: " + value);
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		
		User user = em.find(User.class,(long)id);	
		
		if(user == null){
			// Return a HTTP 404 response if the specified Parolee isn't found.
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}			

		NewCookie cookie = new NewCookie("id", Long.toString(user.getId()));	
		
		em.getTransaction().commit();
		em.close();
		
		return Response.ok().cookie(cookie).build();	
	}
	
	@GET
	@Path("/login")
	@Produces("application/xml")
	public UserDTO getUserFromCookie(@CookieParam("id") Cookie cookie) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		System.err.println("in");
		System.err.println("value is "+cookie.getValue());
    	User user = em.find(User.class,Long.parseLong(cookie.getValue()));
    	System.err.println("id of user: "+user.getId());
        return UserMapper.toDTO(user);
	  
	}
	
	
	
	
	@PUT
	@Path("/{id}/update")
	@Consumes("application/xml")
	public Response updateUser(@PathParam("id") int id, User user) {	
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		
		User current = em.find(User.class, id);
		
		if(user == null){
			// Return a HTTP 404 response if the specified Parolee isn't found.
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}			

		// Update the details of the Parolee to be updated.
		current.setFirstName(user.getFirstName());
		current.setLastName(user.getLastName());
		current.setUserName(user.getUserName());
		current.changeBillingAddress(user.getBillingAddress());
		current.changeShippingAddress(user.getShippingAddress());
		current.setPurchaseHistory(user.getPurchaseHistory());

		em.persist(current);		
		em.getTransaction().commit();
		em.close();
		
		return Response.ok().build();

	}

	
}
