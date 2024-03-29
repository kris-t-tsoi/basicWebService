package nz.ac.auckland.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.Metamodel.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.hibernate.type.EntityType;
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
	@Consumes("application/xml")
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


	@GET
	@Path("/{id}")
	@Produces("application/xml")
	public ItemDTO getItem(@PathParam("id") int id) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		
		Item it = em.find(Item.class,(long) id);
		
		if(it == null){
			// Return a HTTP 404 response if the specified Parolee isn't found.
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		
		ItemDTO dto = ItemMapper.toDTO(it);		
		em.getTransaction().commit();
		em.close();
		
		return dto;		
	}
	
	
	@GET
	public Response getPriceRangeItem(@DefaultValue("0")@QueryParam("from") int from,@DefaultValue("1000")@QueryParam("to")int to) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Item> q = cb.createQuery(Item.class);;
		Root<Item> c = q.from(Item.class);
		
		if(from>to){
			int temp=from;
			from = to;
			to= temp;
		}
		
		q.select(c).where(cb.between(c.get("price").as(Integer.class), from, to));
		
		TypedQuery<Item> query = em.createQuery(q);
		List<Item> items = query.getResultList();
		List<ItemDTO> dto = new ArrayList<ItemDTO>();
		for(Item i:items){
			dto.add(ItemMapper.toDTO(i));
		}
		
		GenericEntity<List<ItemDTO>> enty = new GenericEntity<List<ItemDTO>>(dto){};
		em.getTransaction().commit();
		em.close();

		if(items.isEmpty()|| items == null){
			return null;
		}
		return Response.ok(enty).build();
	}
	
	
	
	//get items within price range of
	
	
	
	
	//get items with name
	
	
	//get items in category
//	@GET
//	@Path("/{id}")
//	@Produces("application/xml")
//	public List<ItemDTO> getItemsInCategory(@PathParam("id") long id) {
//		EntityManager em = entityManagerFactory.createEntityManager();
//		em.getTransaction().begin();
//		
//		List<ItemDTO> itemInCategory = new ArrayList<ItemDTO>();
//		
//		List<Item> items  = em.createQuery("select i from Item i").getResultList( );
////		for(Item i : items){
////			Set<Category> cat = i.getCategories();
////			for(Category c :cat){
////				if(c.getId()==id){
////					ItemDTO dtoItem = ItemMapper.toDTO(i);
////					itemInCategory.add(dtoItem);
////				}
////			}
////		}
//				
//		em.getTransaction().commit();
//		em.close();
//		if(items.isEmpty()|| items == null){
//			return null;
//		}
//		return itemInCategory;
//	}
	
	
	
	
	//put item in a purchase
	
	
	
	

		
}
