package com.forkexec.hub.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidTextFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import com.forkexec.hub.ws.NotEnoughPointsFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.BadQuantityFault_Exception;
import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.InsufficientQuantityFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.cli.RestaurantClient;
import com.forkexec.rst.ws.cli.RestaurantClientException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

/**
 * Hub
 *
 * A restaurants hub server.
 *
 */

public class Hub {
	
	private String uddiURL = null;
	private Map<String,User> users = new HashMap<String,User>();

	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Hub() {
		// Initialization of default values
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Hub INSTANCE = new Hub();
	}

	public static synchronized Hub getInstance() {
		return SingletonHolder.INSTANCE;
	}
		
	public  synchronized void createUser(String email) throws  InvalidUserIdFault_Exception {
	
	String uddiUrl = Hub.getInstance().getUddiURL();
	PointsClient pc = null;
	
		try {
			pc = new PointsClient(uddiUrl, "A65_Points1");
		} catch (PointsClientException e) {
		}
		try {
			pc.activateUser(email);
			User user = new User(email);
			users.put(email,user);
		} catch (EmailAlreadyExistsFault_Exception | InvalidEmailFault_Exception e) {
			
			throw new InvalidUserIdFault_Exception("UserID invalid!", null);
		}
		
	}
	
	public synchronized void AddToCart(String email,FoodId foodId, int foodQtd) throws InvalidFoodIdFault_Exception  {
		
		String uddiUrl = Hub.getInstance().getUddiURL();
		RestaurantClient rc = null;
			
		try {
			rc = new RestaurantClient(uddiUrl, foodId.getRestaurantId());
		} catch (RestaurantClientException e) {
			
		}
		
		MenuId menuid = new MenuId();
		menuid.setId(foodId.getMenuId());
		
		Menu menu = null;
		try {
			menu = rc.getMenu(menuid);
		} catch (BadMenuIdFault_Exception e) {
			throw new InvalidFoodIdFault_Exception("Invalid Menu ID",null);
		}
	
		FoodHub foodHubItem = new FoodHub(
				foodId.getRestaurantId(),
				foodId.getMenuId(), 
				menu.getEntree(), 
				menu.getPlate(), 
				menu.getDessert(), 
				menu.getPrice(),
				menu.getPreparationTime(),
				foodQtd);
		
		users.get(email).getCart().add(foodHubItem);

	}
	
	public synchronized void ClearCart(String email) throws InvalidUserIdFault_Exception  {
		Hub.getInstance().getCart(email).clear();
	}


	public int getCredit(String email) throws  InvalidUserIdFault_Exception, InvalidUserIdFault_Exception {
		
		String uddiUrl = Hub.getInstance().getUddiURL();
		PointsClient pc = null;
		int creditReturn=0;
			try {
				pc = new PointsClient(uddiUrl, "A65_Points1");
			} catch (PointsClientException e) {
				
			}
			
			try {
				creditReturn =  pc.pointsBalance(email);
			} catch (InvalidEmailFault_Exception e) {
				throw new InvalidUserIdFault_Exception("Invali userId !",null);
			}
		
			return creditReturn;
			}
	
	public List<FoodHub> getCart(String email) throws  InvalidUserIdFault_Exception {
		
		User user = users.get(email);
		
		synchronized (user){
			return user.getCart();
			}
		}
	
	public void  orderCart(String email,List<FoodHub> cartitem) throws InvalidUserIdFault_Exception,NotEnoughPointsFault_Exception,InvalidFoodQuantityFault_Exception {
		
		String uddiUrl = Hub.getInstance().getUddiURL();
		PointsClient pc = null;
		RestaurantClient rc = null;
		int points=0;
		
		try {
			pc = new PointsClient(uddiUrl, "A65_Points1");
		} catch (PointsClientException e) {
		}
		
		for(FoodHub fh: cartitem)
			points += + (fh.getPreco()*fh.getQuantidade());
		
					try {
						pc.spendPoints(email,points);
					} catch (InvalidEmailFault_Exception e1) {
						throw new InvalidUserIdFault_Exception("Invalid UserId!", null);
					}
//					} catch (InvalidPointsFault_Exception e1) {
//							//todo
//					} catch (NotEnoughBalanceFault_Exception e1) {
//						throw new NotEnoughPointsFault_Exception("Not enough points to order!", null);
//					}
			
		for(FoodHub fh: cartitem){		
	
			MenuId menuid = new MenuId();
			menuid.setId(fh.getMenuid());
			
			try {
				rc = new RestaurantClient(uddiUrl, fh.getRestauranteid());
			} catch (RestaurantClientException e) {
				continue;
			}
			
					try {
						rc.orderMenu(menuid, fh.getQuantidade());
					} catch (BadMenuIdFault_Exception e) {
						//TODO
					} catch (BadQuantityFault_Exception e) {
						//TODO
					} catch (InsufficientQuantityFault_Exception e) {
						throw new InvalidFoodQuantityFault_Exception("Not enough quantity to satisfy your order!", null);
					}
			}
			
	}
	
	public void AddCredits(String email,int money) throws InvalidUserIdFault_Exception, InvalidMoneyFault_Exception
	{
		
		String uddiUrl = Hub.getInstance().getUddiURL();
		PointsClient pc = null;
		int creditsToAdd = 0;
		
		switch(money){
		case 10: creditsToAdd = 1000;break;
		case 20: creditsToAdd = 2100;break;
		case 30: creditsToAdd = 3300;break;
		case 50: creditsToAdd = 5500;break;
		}
		
		try {
			pc = new PointsClient(uddiUrl, "A65_Points1");
		} catch (PointsClientException e) {
			
		}
		
		try {
			pc.addPoints(email, creditsToAdd);
		} catch (InvalidEmailFault_Exception e) {
			throw new InvalidUserIdFault_Exception("Invalid user email!", null);
		}
//		} catch (InvalidPointsFault_Exception e) {
//			throw new InvalidMoneyFault_Exception("Invalid credits!", null);
//
//		}		
		
	}
	
	public synchronized List<FoodHub> getMenusByDescription(String description) throws InvalidTextFault_Exception  {

		Collection<String> restaurants = this.getRestaurants();
		List<Menu> menusRest = new ArrayList<Menu>();
		List<FoodHub> menusHub = new ArrayList<FoodHub>();
		String uddiUrl = Hub.getInstance().getUddiURL();
		System.out.println("rests:"+restaurants+description);

		for (String s : restaurants) {
			try {
				RestaurantClient sc = new RestaurantClient(uddiUrl, s);
				try {
					menusRest = sc.searchMenus(description);
					System.out.println("rests:"+menusRest+description);

				} catch (BadTextFault_Exception e) {
					throw new InvalidTextFault_Exception("Invalid search criteria.", null);
				}

			} catch (RestaurantClientException e) {
				continue;
			}
		}
		
		for(Menu menu :menusRest){
			FoodHub food = new FoodHub();
			food.setEntrada(menu.getEntree());
			food.setPrincipal(menu.getPlate());
			food.setSobremesa(menu.getDessert());
			food.setTempoDeConfecao(menu.getPreparationTime());
			food.setPreco(menu.getPrice());
			menusHub.add(food);
		}
		return menusHub;
		
	}
	
	public Collection<String> getRestaurants() {
		Collection<UDDIRecord> records = null;
		Collection<String> rests = new ArrayList<String>();
		try {
			UDDINaming uddi = new UDDINaming(uddiURL);
			records = uddi.listRecords("A65_Restaurant" + "%");
			for (UDDIRecord u : records){
				rests.add(u.getOrgName());
			}
				
				
		} catch (UDDINamingException e) {
		}
		System.out.println(rests);
		return rests;
	}

	
	public FoodHub getFood(FoodId foodid) throws InvalidFoodIdFault_Exception{
		
		
		String restaurantId = foodid.getRestaurantId();
 		RestaurantClient rc = null;
		MenuId menuid = new MenuId();
		FoodHub retfoodHub = new FoodHub();
		Menu menu = null;
		
		menuid.setId(foodid.getMenuId());
		
		try {
			rc = new RestaurantClient(restaurantId);
		} catch (RestaurantClientException e) {
			
		}

		try {
			menu =  rc.getMenu(menuid);	
		} catch (BadMenuIdFault_Exception e) {
			throw new InvalidFoodIdFault_Exception("Invalid Menu Id!", null);
		}
		
		retfoodHub.setEntrada(menu.getEntree());
		retfoodHub.setPrincipal(menu.getPlate());
		retfoodHub.setSobremesa(menu.getDessert());
		retfoodHub.setTempoDeConfecao(menu.getPreparationTime());
		retfoodHub.setPreco(menu.getPrice());
		
		return retfoodHub;
		
	}
	
	public void initUddiURL(String uddiURL) {
		setUddiURL(uddiURL);
	}

	public String getUddiURL() {
		return uddiURL;
	}

	private void setUddiURL(String url) {
		uddiURL = url;
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	
}
