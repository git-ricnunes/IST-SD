package com.forkexec.hub.ws;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import com.forkexec.cc.ws.cli.CreditCardClient;
import com.forkexec.cc.ws.cli.CreditCardClientException;
import com.forkexec.hub.domain.FoodHub;
import com.forkexec.hub.domain.FoodPriceComparator;
import com.forkexec.hub.domain.FoodTimeComparator;
import com.forkexec.hub.domain.Hub;
import com.forkexec.hub.domain.User;
import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuInit;
import com.forkexec.rst.ws.cli.RestaurantClient;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.hub.ws.HubPortType",
            wsdlLocation = "HubService.wsdl",
            name ="HubWebService",
            portName = "HubPort",
            targetNamespace="http://ws.hub.forkexec.com/",
            serviceName = "HubService"
)
public class HubPortImpl implements HubPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private HubEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public HubPortImpl(HubEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}
	
	// Main operations -------------------------------------------------------
	
	@Override
	public void activateAccount(String userId) throws InvalidUserIdFault_Exception {

		try {
			Hub.getInstance().createUser(userId);
		} catch (InvalidUserIdFault_Exception e) {
			throwInvalidUserIdFault("Invalid user ID.");
		}
	}

	@Override
	public void loadAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		
		CreditCardClient ccClient = null;
		
		try {
			ccClient = new CreditCardClient();
		} catch (CreditCardClientException e1) {
		
		}
		
		ccClient.validateNumber(creditCardNumber);
		
		if(creditCardNumber == null || creditCardNumber.equals("") || !ccClient.validateNumber(creditCardNumber)){
			throwInvalidUserCreditCard("Invalid credit card number!"); 
		}
		
		try {
			Hub.getInstance().AddCredits(userId, moneyToAdd);
		} catch (InvalidUserIdFault_Exception e) {
			throwInvalidUserIdFault("Invalid UserId!");
		} catch (InvalidMoneyFault_Exception e) {
			throwInvalidMoney("Invalid Credits!");

		}
		
	}
	
	@Override
	public List<Food> searchDeal(String description) throws InvalidTextFault_Exception {
		List<Food> retListFood = new ArrayList<Food>();
		List<FoodHub> menus = new ArrayList<FoodHub>();;
		try {
			menus = Hub.getInstance().getMenusByDescription(description);
		} catch (InvalidTextFault_Exception e) {
			throwInvalidTextFault("Invalid  menu item description!");
		}
		
		for(FoodHub f : menus)
			retListFood.add(buildFood(f));
		
		Collections.sort(retListFood,new FoodPriceComparator());
		
		return retListFood;
	}
	
	@Override
	public List<Food> searchHungry(String description) throws InvalidTextFault_Exception {
		List<Food> retListFood = new ArrayList<Food>();
		List<FoodHub> menus = null;
		try {
			menus = Hub.getInstance().getMenusByDescription(description);
			System.out.println("searchHungry:"+menus);

		} catch (InvalidTextFault_Exception e) {
			throwInvalidTextFault("Invalid menu item description!");
		}

		for(FoodHub f : menus)
			retListFood.add(buildFood(f));
		
		Collections.sort(retListFood,new FoodTimeComparator());
		
		return retListFood;	
		
	}
	
	@Override
	public void addFoodToCart(String userId, FoodId foodId, int foodQuantity)
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		
		Map<String, User> users = Hub.getInstance().getUsers();
		
		if(userId == null ||userId.equals("")||users.get(userId)==null)
			throw new InvalidUserIdFault_Exception("UserID invalid!", null);
				
		if(foodQuantity<=0)
			throwInvalidFoodQuantityFault("FoodId invalid!");

		try {
			Hub.getInstance().AddToCart(userId,foodId,foodQuantity);
		} catch (InvalidFoodIdFault_Exception e) {
			throwInvalidFoodId("FoodId invalid!");
		}
		
	} 

	@Override
	public void clearCart(String userId) throws InvalidUserIdFault_Exception {

		Map<String, User> users = Hub.getInstance().getUsers();
		
		if(userId == null ||userId.equals("")||users.get(userId)==null)
			throwInvalidUserIdFault("UserID invalid!");
		
		Hub.getInstance().ClearCart(userId);
		
	}

	@Override
	public FoodOrder orderCart(String userId)
			throws  EmptyCartFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception,
			NotEnoughPointsFault_Exception {
		
		
		List<FoodHub> cart =  Hub.getInstance().getCart(userId);
		Map<String, User> users = Hub.getInstance().getUsers();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		 
		//generates unique order ID with F(ork)E(xec)_<userid>_<order_timestamp>
		String oId="FE_"+userId+"_"+dateFormat.format(date).toString();

		//Arguments validation 
		if(userId == null ||userId.equals("")||users.get(userId)==null)
			throwInvalidUserIdFault("UserID invalid!");
		 
		if(cart.isEmpty())
		 	throwEmptyCartFault("User cart is empty!");
	
			try {
				Hub.getInstance().orderCart(userId, cart);
			} catch (NotEnoughPointsFault_Exception e) {
				throwNotEnoughPointsFault("Not enough points to make your order!");
			}
				
		FoodOrder returnFoodOrder =  buildFoodOrder(oId,cart);
		 	
		return returnFoodOrder;
	}

	@Override
	public int accountBalance(String userId) throws InvalidUserIdFault_Exception {
		int retPoints = 0;
	
			try {
				retPoints = Hub.getInstance().getCredit(userId);
			} catch (InvalidUserIdFault_Exception e) {
				throwInvalidUserIdFault("Invalid UserId.");
			}
	
		return retPoints;		
	}

	@Override
	public Food getFood(FoodId foodId) throws InvalidFoodIdFault_Exception {
		
		Hub.getInstance().getFood(foodId);
		
		return buildFood(Hub.getInstance().getFood(foodId));
	}

	@Override
	public List<FoodOrderItem> cartContents(String userId) throws InvalidUserIdFault_Exception {

		List<FoodOrderItem> foodOrderList = new ArrayList<FoodOrderItem>();
		Map<String, User> users = Hub.getInstance().getUsers();

		if(userId == null ||userId.equals("")||users.get(userId)==null)
			throwInvalidUserIdFault("UserID invalid!");
		
		for(FoodHub cartItem : Hub.getInstance().getCart(userId))
			foodOrderList.add(buildFoodOrderItem(cartItem));

		return foodOrderList;
	}

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the service does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Hub";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		
		
		Collection<String> restsUrls = null;
		try {
			UDDINaming uddiNaming = endpointManager.getUddiNaming();
			restsUrls = uddiNaming.list("A65_Restaurant"+ "%");
			builder.append("Found ");
			builder.append(restsUrls.size());
			builder.append(" restaurants on UDDI.\n");
		} catch(UDDINamingException e) {
			builder.append("Failed to contact the UDDI server:");
			builder.append(e.getMessage());
			builder.append(" (");
			builder.append(e.getClass().getName());
			builder.append(")");
			return builder.toString();
		}

		for(String restUrl : restsUrls) {
			builder.append("Ping result for restaurants at ");
			builder.append(restUrl);
			builder.append(":");
			try {
				RestaurantClient client = new RestaurantClient(restUrl);
				String supplierPingResult = client.ctrlPing(endpointManager.getWsName());
				builder.append(supplierPingResult);
			} catch(Exception e) {
				builder.append(e.getMessage());
				builder.append(" (");
				builder.append(e.getClass().getName());
				builder.append(")\n");
			}
			builder.append("\n");
		}
		
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		
		Collection<String> restsUrls = null;
		UDDINaming uddiNaming = endpointManager.getUddiNaming();

		PointsClient clientPts = null;
		try {

			restsUrls = uddiNaming.list("A65_Points"+ "%");
			for(String restUrl : restsUrls) {
				clientPts = new PointsClient(restUrl);
				clientPts.ctrlClear();
			}
		} catch (PointsClientException | UDDINamingException e1) {
		
		}
		
		try {
			restsUrls = uddiNaming.list("A65_Restaurant"+ "%");
			
		} catch(UDDINamingException e) {
		}

		for(String restUrl : restsUrls) {
		
			try {
				RestaurantClient client = new RestaurantClient(restUrl);
				 client.ctrlClear();
			} catch(Exception e) {
			
			}
			
		}
		
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		Collection<String> restsUrls = null;
		List<MenuInit> menuInits=new ArrayList<MenuInit>();
		
		try {
			UDDINaming uddiNaming = endpointManager.getUddiNaming();
			restsUrls = uddiNaming.list("A65_Restaurant"+ "%");
		
		} catch(UDDINamingException e) {
		
		}

		for(String restUrl : restsUrls) {
		
			try {
				RestaurantClient client = new RestaurantClient(restUrl);
				
				for(FoodInit fi :initialFoods){
					if(fi.getFood().getId().getMenuId().equals(restUrl))
						menuInits.add(buildMenuInit(fi));
						
				}
				
				client.ctrlInit(menuInits);

			
			} catch(Exception e) {
			
			}
			
			
		}
	}
	
	@Override
	public void ctrlInitUserPoints(int startPoints) {

			UDDINaming uddiNaming = endpointManager.getUddiNaming();
			Collection<String> pointsUrls = null;
			try {
				pointsUrls = uddiNaming.list("A65_Points1");
			} catch (UDDINamingException e) {
				
			}
			PointsClient client = null;
			
			

			for(String ptsurl: pointsUrls) {
				try {
					client = new PointsClient(ptsurl);
				} catch (PointsClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

				try {
					client.ctrlInit(startPoints);
				} catch (BadInitFault_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		

		
	}



	// View helpers ----------------------------------------------------------

	// /** Helper to convert a domain object to a view. */
	 private Food buildFood(FoodHub foodhub) {
		 Food food = new Food();
		 food.setEntree(foodhub.getEntrada());
		 food.setPlate(foodhub.getPrincipal());
		 food.setDessert(foodhub.getSobremesa());
		 food.setPreparationTime(foodhub.getTempoDeConfecao());
		 food.setPrice(foodhub.getPreco());
		 return food;
	 }
	 
	 private MenuInit buildMenuInit(FoodInit foodinit) {
		MenuInit food = new MenuInit();
		Menu menu = new Menu(); 
		
		menu.setEntree(foodinit.getFood().getDessert());
		menu.setPlate(foodinit.getFood().getPlate());
		menu.setDessert(foodinit.getFood().getDessert());
		menu.setPreparationTime(foodinit.getFood().getPreparationTime());
		menu.setPrice(foodinit.getFood().getPrice());
		
		food.setMenu(menu);
		food.setQuantity(foodinit.getQuantity());
		return food;
	 }
	 
	 
	 
	 private FoodOrderItem buildFoodOrderItem(FoodHub cartItem) {
		 FoodOrderItem foodOrderItem = new FoodOrderItem();
		 FoodId foodID = new FoodId();
		 
		 foodID.setMenuId(cartItem.getMenuid());
		 foodID.setRestaurantId(cartItem.getRestauranteid());
		 
		 foodOrderItem.setFoodId(foodID);
		 
		 return foodOrderItem;
	 }
	 
	 private FoodOrder buildFoodOrder(String foodOrderId,List<FoodHub> cart) {
		 
		 FoodOrder retFoodOrder = new FoodOrder();
		 FoodOrderId retFoodOrderId = new FoodOrderId();
		 
		 retFoodOrderId.setId(foodOrderId);
		 
		 for(FoodHub fh :cart)
		 {
			 FoodOrderItem foi = new FoodOrderItem();
			 FoodId fi = new FoodId();
			 
			 fi.setMenuId(fh.getMenuid());
			 fi.setRestaurantId(fh.getRestauranteid());
			 
			 foi.setFoodId(fi);
			 foi.setFoodQuantity(fh.getQuantidade());
			 
			 retFoodOrder.getItems().add(foi);
		 }
		 
		 retFoodOrder.setFoodOrderId(retFoodOrderId);
		
		return retFoodOrder;
	
	 }
	 
	 

	
	// Exception helpers -----------------------------------------------------

	private void throwInvalidUserIdFault(final String message) throws InvalidUserIdFault_Exception {
		InvalidUserIdFault faultInfo = new InvalidUserIdFault();
		faultInfo.setMessage(message);
		throw new InvalidUserIdFault_Exception(message, faultInfo);
	}
	
	private void throwInvalidTextFault(final String message) throws InvalidTextFault_Exception{
		InvalidTextFault faultInfo = new InvalidTextFault();
		faultInfo.setMessage(message);
		throw new InvalidTextFault_Exception(message, faultInfo);
	}
	
	private void throwInvalidUserCreditCard(final String message) throws InvalidCreditCardFault_Exception {
		InvalidCreditCardFault faultInfo = new InvalidCreditCardFault();
		faultInfo.setMessage(message);
		throw new InvalidCreditCardFault_Exception(message, faultInfo);
	}
	
	private void throwInvalidMoney(final String message) throws InvalidMoneyFault_Exception {
		InvalidMoneyFault faultInfo = new InvalidMoneyFault();
		faultInfo.setMessage(message);
		throw new InvalidMoneyFault_Exception(message, faultInfo);
	}
	
	private void throwInvalidFoodId(final String message) throws InvalidFoodIdFault_Exception{
		InvalidFoodIdFault faultInfo = new InvalidFoodIdFault();
		faultInfo.setMessage(message);
		throw new InvalidFoodIdFault_Exception(message, faultInfo);
	}
	
	private void throwInvalidFoodQuantityFault(final String message) throws InvalidFoodQuantityFault_Exception{
		InvalidFoodQuantityFault faultInfo = new InvalidFoodQuantityFault();
		faultInfo.setMessage(message);
		throw new InvalidFoodQuantityFault_Exception(message, faultInfo);
	}

	private void throwEmptyCartFault(final String message) throws EmptyCartFault_Exception{
		EmptyCartFault faultInfo = new EmptyCartFault();
		faultInfo.setMessage(message);
		throw new EmptyCartFault_Exception(message, faultInfo);
	}
	
	private void throwNotEnoughPointsFault(final String message) throws NotEnoughPointsFault_Exception  {
		NotEnoughPointsFault faultInfo = new NotEnoughPointsFault();
		faultInfo.setMessage(message);
		throw new NotEnoughPointsFault_Exception(message, faultInfo);
	}
	
	
}
