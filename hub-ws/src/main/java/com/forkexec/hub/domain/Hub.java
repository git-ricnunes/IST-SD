package com.forkexec.hub.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.BadQuantityFault_Exception;
import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.InsufficientQuantityFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.cli.*;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import com.forkexec.pts.domain.exception.RestaurantNotFoundException;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidPointsFault_Exception;
import com.forkexec.pts.ws.NotEnoughBalanceFault_Exception;
import com.forkexec.pts.ws.cli.*;

/**
 * Hub
 *
 * A restaurants hub server.
 *
 */

public class Hub {
	
	private String uddiURL = null;
	private String restaurantTemplateName = null;
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
		
	public void createUser(String email) throws  InvalidUserIdFault_Exception {
	
	String uddiUrl = Hub.getInstance().getUddiURL();
	PointsClient pc = null;
	
		try {
			pc = new PointsClient(uddiUrl, "A65_pts1");
		} catch (PointsClientException e) {
		}
		try {
			pc.activateUser(email);
			User user = new User(email);
			users.put(email,user);
		} catch (EmailAlreadyExistsFault_Exception | InvalidEmailFault_Exception e) {
			
			throw new InvalidUserIdFault_Exception("UserID invalid!" +e.getMessage(), null);
		}
		
	}
	
	public void addToCart(String email,String menuId,String restID) throws  InvalidUserIdFault_Exception {
		CartItem cartItem = new CartItem(restID,menuId);
		users.get(email).getCart().add(cartItem);

	}
	
	public void ClearCart(String email) throws  InvalidUserIdFault_Exception, InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception, BadTextFault_Exception {
		Hub.getInstance().getCart(email).clear();
	}


	public int getCredit(String email) throws  InvalidUserIdFault_Exception {
		
		String uddiUrl = Hub.getInstance().getUddiURL();
		PointsClient pc = null;
		int creditReturn=0;
			try {
				pc = new PointsClient(uddiUrl, "A65_pts1");
			} catch (PointsClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				creditReturn =  pc.pointsBalance(email);
			} catch (InvalidEmailFault_Exception e) {
				
				throw new InvalidUserIdFault_Exception("UserID invalid!" +e.getMessage(), null);
			}
			return creditReturn;
			}
	
	public List<CartItem> getCart(String email) throws  InvalidUserIdFault_Exception {
		
			User user = users.get(email);
			
			return user.getCart();
		
			}
	
	public void AddCredits(String email,int money) throws InvalidUserIdFault_Exception, InvalidPointsFault_Exception{
		
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
			pc = new PointsClient(uddiUrl, "A65_pts1");
		} catch (PointsClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			pc.addPoints(email, creditsToAdd);
		} catch (InvalidEmailFault_Exception e) {
			throw new InvalidUserIdFault_Exception("UserID invalid!", null);
		} catch (InvalidPointsFault_Exception e) {
			throw new InvalidPointsFault_Exception("Invalid value of credits!", null);
		}		
		
	}
	
	public List<FoodHub> getMenusByDescription(String description) throws  BadTextFault_Exception {

		Collection<String> restaurants = this.getRestaurants();
		List<Menu> menusRest = new ArrayList<Menu>();
		List<FoodHub> menusHub = new ArrayList<FoodHub>();
		String uddiUrl = Hub.getInstance().getUddiURL();
		
		for (String s : restaurants) {
			try {
				RestaurantClient sc = new RestaurantClient(uddiUrl, s);
				menusRest = sc.searchMenus(description);

				
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
			records = uddi.listRecords("A65_rts" + "%");
			for (UDDIRecord u : records)
				rests.add(u.getOrgName());
		} catch (UDDINamingException e) {
		}
		return rests;
	}
	


	public void initUddiURL(String uddiURL) {
		setUddiURL(uddiURL);
	}

	public void initStationTemplateName(String stationTemplateName) {
		setStationTemplateName(stationTemplateName);
	}

	public String getUddiURL() {
		return uddiURL;
	}

	private void setUddiURL(String url) {
		uddiURL = url;
	}

	private void setStationTemplateName(String sn) {
		restaurantTemplateName = sn;
	}

	public String getStationTemplateName() {
		return restaurantTemplateName;
	}
	
}
