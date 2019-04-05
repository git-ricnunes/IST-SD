package com.forkexec.rst.ws;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import com.forkexec.rst.domain.Restaurant;
import com.forkexec.rst.domain.Food;
/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.rst.ws.RestaurantPortType",
            wsdlLocation = "RestaurantService.wsdl",
            name ="RestaurantWebService",
            portName = "RestaurantPort",
            targetNamespace="http://ws.rst.forkexec.com/",
            serviceName = "RestaurantService"
)
public class RestaurantPortImpl implements RestaurantPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private RestaurantEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public RestaurantPortImpl(RestaurantEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}
	
	// Main operations -------------------------------------------------------
	
	@Override
	public Menu getMenu(MenuId menuId) throws BadMenuIdFault_Exception {
		
		Food  restMenu = null;

		try{
			 restMenu = Restaurant.getInstance().getMenu(menuId);
		}catch(BadMenuIdFault_Exception e){
			throwBadMenuIdFault("Bad MenuID!");
		}
		

		return buildMenu(restMenu);
	}
	
	@Override
	public List<Menu> searchMenus(String descriptionText) throws BadTextFault_Exception {
		
		
		if(descriptionText==null||descriptionText.equals(""))
			throwBadTextFault("Invalid description to search");

		List<Menu> retMenus = new ArrayList<Menu>();
		Collection<Food> restMenus = Restaurant.getInstance().getMenus();
		synchronized (restMenus) {
			
			for(Food menu : restMenus){
				if(	menu.getEntrada().contains(descriptionText) ||
					menu.getPrincipal().contains(descriptionText)||
					menu.getSobremesa().contains(descriptionText) )
					retMenus.add(buildMenu(menu));		
				System.out.println(menu);
				}
			}
		return retMenus;
	}

	@Override
	public MenuOrder orderMenu(MenuId arg0, int arg1) throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
		
		Food restMenu = null;
		MenuOrder retMenuOrder = new MenuOrder();
		Integer quantidade =null ;

			if(arg0.getId().isEmpty() || "".equals(arg0.getId()))
				throwBadMenuIdFault("Invalid MenuID !");
			
			restMenu = Restaurant.getInstance().getMenu(arg0);
			quantidade = restMenu.getQuantidade();

			if (arg1<1)
				throwBadQuantityFault("Quantity must be greater than zero!");
			else if (quantidade == null || quantidade<arg1)
				throwInsufficientQuantityFault("Not enough menus to serve!");
			else {	
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				
				//generates unique order ID with F(ork)E(xec)_<restid>_<order_timestamp>
				
				String actualOderId="FE_"+Restaurant.getInstance().getId()+"_"+dateFormat.format(date).toString();
				
				restMenu.setQuantidade(restMenu.getQuantidade()-arg1);
				retMenuOrder= builOrder(arg0,actualOderId,arg1);
				
			}
			
			return retMenuOrder;
		}

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the park does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Restaurant";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		Restaurant.getInstance().reset();
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInit(List<MenuInit> initialMenus) throws BadInitFault_Exception {
		try {
						
			Collection<Food> menus = new ArrayList<Food>();

			for (MenuInit initialMenu :initialMenus) {
				
				Food menu=new Food();
				menu.setId(initialMenu.getMenu().getId().getId());
				menu.setEntrada(initialMenu.getMenu().getEntree());
				menu.setPrincipal(initialMenu.getMenu().getPlate());
				menu.setSobremesa(initialMenu.getMenu().getDessert());
				menu.setPreco(initialMenu.getMenu().getPrice());
				menu.setTempoDeConfecao(initialMenu.getMenu().getPreparationTime());
				menu.setQuantidade(initialMenu.getQuantity());
				menus.add(menu);
			}

			Restaurant.getInstance().init(menus,this.endpointManager.getWsName());
		
		} catch (BadInitFault_Exception e) {
			throwBadInit("Invalid initialization values!");
		}
	}

	// View helpers ----------------------------------------------------------

	 /** Helper to convert a domain object to a view. */
	 private Menu buildMenu (Food menu) {
		 
		 Menu retMenu = new Menu();
		 MenuId menuid = new MenuId();
		 menuid.setId(menu.getId());
		 retMenu.setId(menuid);
		 retMenu.setEntree(menu.getEntrada());
		 retMenu.setPlate(menu.getPrincipal());
		 retMenu.setDessert(menu.getSobremesa());
		 retMenu.setPreparationTime(menu.getTempoDeConfecao());
		 retMenu.setPrice(menu.getPreco());
		 return retMenu;
	 }

	 private MenuOrder builOrder (MenuId menuid,String id, int quantidade) {
		 MenuOrder retMenu = new MenuOrder();
		 MenuOrderId retOrderId = new MenuOrderId();
		 retOrderId.setId(id);
		 retMenu.setMenuId(menuid);
		 retMenu.setId(retOrderId);
		 retMenu.setMenuQuantity(quantidade);
		 return retMenu;
	 }
	 
	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
	private void throwBadInit(final String message) throws BadInitFault_Exception {
		BadInitFault faultInfo = new BadInitFault();
		faultInfo.message = message;
		throw new BadInitFault_Exception(message, faultInfo);
	}
	
	private void throwBadTextFault(final String message) throws BadTextFault_Exception {
		BadTextFault faultInfo = new BadTextFault();
		faultInfo.message = message;
		throw new BadTextFault_Exception(message, faultInfo);
	}
	
	private void throwBadMenuIdFault(final String message) throws BadMenuIdFault_Exception {
		BadMenuIdFault faultInfo = new BadMenuIdFault();
		faultInfo.message = message;
		throw new BadMenuIdFault_Exception(message, faultInfo);
	}
	
	private void throwBadQuantityFault(final String message) throws BadQuantityFault_Exception {
		BadQuantityFault faultInfo = new BadQuantityFault();
		faultInfo.message = message;
		throw new BadQuantityFault_Exception(message, faultInfo);
	}
	
	private void throwInsufficientQuantityFault(final String message) throws InsufficientQuantityFault_Exception {
		InsufficientQuantityFault faultInfo = new InsufficientQuantityFault();
		faultInfo.message = message;
		throw new InsufficientQuantityFault_Exception(message, faultInfo);
	}



}
