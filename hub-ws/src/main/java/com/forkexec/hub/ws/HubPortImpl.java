package com.forkexec.hub.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.jws.WebService;

import com.forkexec.hub.domain.CartItem;
import com.forkexec.hub.domain.FoodHub;
import com.forkexec.hub.domain.FoodPriceComparator;
import com.forkexec.hub.domain.FoodTimeComparator;
import com.forkexec.hub.domain.Hub;
import com.forkexec.pts.domain.exception.RestaurantNotFoundException;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidPointsFault_Exception;
import com.forkexec.pts.ws.NotEnoughBalanceFault_Exception;
import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.cli.RestaurantClient;

import pt.ulisboa.tecnico.sdis.ws.CreditCard;
import pt.ulisboa.tecnico.sdis.ws.CreditCardImplService;
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
			throwInvalidUserIdFault("Invalid user ID: "+e.getMessage());
		}
	}

	@Override
	public void loadAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		
		CreditCardImplService ccService = new CreditCardImplService();
		
		CreditCard  cc =ccService.getCreditCardImplPort();
		
		if(creditCardNumber == null || creditCardNumber.equals("") || !cc.validateNumber(creditCardNumber)){
			throwInvalidUserCreditCard("Invalid credit card Number!"); 
		}
		
		if(userId == null || userId.equals(""))
			throwInvalidUserIdFault("Invalid user ID.");
		
		if(moneyToAdd <=0)
			throwInvalidUserIdFault("Invalid value to add the credits.");
		
		try {
			Hub.getInstance().AddCredits(userId, moneyToAdd);
		} catch (InvalidPointsFault_Exception e) {
			throwInvalidMoney("Invalid value to add the credits.");
		}
		
	}
	
	
	@Override
	public List<Food> searchDeal(String description) throws InvalidTextFault_Exception {
		List<Food> retListFood = new ArrayList<Food>();
		List<FoodHub> menus = new ArrayList<FoodHub>();;
		try {
			menus = Hub.getInstance().getMenusByDescription(description);
		
		} catch (BadTextFault_Exception e) {
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

		} catch (BadTextFault_Exception e) {
		}

		for(FoodHub f : menus)
			retListFood.add(buildFood(f));
		
		Collections.sort(retListFood,new FoodTimeComparator());
		
		return retListFood;	
		
	}
	
	@Override
	public void addFoodToCart(String userId, FoodId foodId, int foodQuantity)
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
				
		Hub.getInstance().addToCart(userId,foodId.getMenuId(),foodId.getRestaurantId());
		
	}

	@Override
	public void clearCart(String userId) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().ClearCart(userId);
		} catch (InvalidEmailFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPointsFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnoughBalanceFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadTextFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public FoodOrder orderCart(String userId)
			throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception {
		// TODO 
		return null;
	}

	@Override
	public int accountBalance(String userId) throws InvalidUserIdFault_Exception {
		int retPoints = 0;
		
		try {
			retPoints = Hub.getInstance().getCredit(userId);
		} catch (InvalidUserIdFault_Exception e) {
			throwInvalidUserIdFault(userId);
		}
		return retPoints;		
	}

	@Override
	public Food getFood(FoodId foodId) throws InvalidFoodIdFault_Exception {
		
		
		foodId.getMenuId();
		foodId.getRestaurantId();
		// TODO
		return null;
	}

	@Override
	public List<FoodOrderItem> cartContents(String userId) throws InvalidUserIdFault_Exception {

		List<FoodOrderItem> foodOrderList = new ArrayList<FoodOrderItem>();
		
		for(CartItem cartItem : Hub.getInstance().getCart(userId))
			foodOrderList.add(buildFoodOrder(cartItem));

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
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void ctrlInitUserPoints(int startPoints) throws InvalidInitFault_Exception {
		// TODO Auto-generated method stub
		
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
	 
	 private FoodOrderItem buildFoodOrder(CartItem cartItem) {
		 FoodOrderItem foodOrder = new FoodOrderItem();
		 FoodId foodID = new FoodId();
		 
		 foodID.setMenuId(cartItem.getMenuId());
		 foodID.setRestaurantId(cartItem.getRestaurantId());
		 
		 foodOrder.setFoodId(foodID);
		 
		 return foodOrder;
	 }

	
	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
//	private void throwBadInit(final String message) throws BadInitFault_Exception {
//		BadInitFault faultInfo = new BadInitFault();
//		faultInfo.message = message;
//		throw new BadInitFault_Exception(message, faultInfo);
//	}

	private void throwInvalidUserIdFault(final String message) throws InvalidUserIdFault_Exception {
		InvalidUserIdFault faultInfo = new InvalidUserIdFault();
		faultInfo.setMessage(message);
		throw new InvalidUserIdFault_Exception(message, faultInfo);
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
}
