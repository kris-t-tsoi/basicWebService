package nz.ac.auckland.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.ac.auckland.dto.ItemDTO;
import nz.ac.auckland.purchaseItems.Category;
import nz.ac.auckland.purchaseItems.Item;

@Path("/item")
public class ItemResource {
	//Set up Logger
	private static Logger logger = LoggerFactory.getLogger(UserResource.class);
		
	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("shoppingPU");
	
	
	@POST
	@Consumes("{application/xml}")
	public Response createItem(ItemDTO dto) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		logger.debug("Read item: " + dto);
		
		Item it = ItemMapper.toDomainModel(dto);
		logger.debug("create item: " + it);
		em.persist(it);
		em.getTransaction().commit();
		em.close();

		return Response.created(URI.create("/item/" + it.getId()))
				.build();
	}
	
	/*
	 * 		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();	
		
		logger.debug("Read user: " + user);
		
		User ur = UserMapper.toDomainModel(user);	
		
		logger.debug("Created user: " + ur);
		
		logger.debug("Path: " + ur);
		
		em.persist(ur);		
		em.getTransaction().commit();
		em.close();
	 */
	

	@GET
	@Path("/{id}")
	@Produces("application/xml")
	public ItemDTO getItem(@PathParam("id") int id) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		
		Item it = em.find(Item.class, id);
		
		if(it == null){
			// Return a HTTP 404 response if the specified Parolee isn't found.
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		
		ItemDTO dto = ItemMapper.toDTO(it);		
		em.getTransaction().commit();
		em.close();
		
		return dto;		
	}
	
	
//	@GET
//	@Path("/{id}")
//	@Produces("application/xml")
//	public List<Item> getAllItem() {
//		EntityManager em = entityManagerFactory.createEntityManager();
//		em.getTransaction().begin();
//		
//		List<Item> items  = em.createQuery("select i from Item i").getResultList( );
//			
//		em.getTransaction().commit();
//		em.close();
//
//		if(items.isEmpty()|| items == null){
//			return null;
//		}
//		return items;
//	}
	
	
	
	//get items within price range of
	
	
	
	
	//get items with name
	
	
	//get items in category
	@GET
	@Path("/{id}")
	@Produces("application/xml")
	public List<ItemDTO> getItemsInCategory(@PathParam("id") long id) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		
		List<ItemDTO> itemInCategory = new ArrayList<ItemDTO>();
		
		List<Item> items  = em.createQuery("select i from Item i").getResultList( );
		for(Item i : items){
			Set<Category> cat = i.getCategories();
			for(Category c :cat){
				if(c.getId()==id){
					ItemDTO dtoItem = ItemMapper.toDTO(i);
					itemInCategory.add(dtoItem);
				}
			}
		}
				
		em.getTransaction().commit();
		em.close();
		if(items.isEmpty()|| items == null){
			return null;
		}
		return itemInCategory;
	}
	
	
	
	
	//put item in a purchase
	
	
	
	

		
}
