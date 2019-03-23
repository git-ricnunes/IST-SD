package com.forkexec.rst.ws;

import java.util.Collection;
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
		
		Collection<Food> restMenus = Restaurant.getInstance().getMenus();
		Food retMenu = null;
		
		synchronized (restMenus) {
			
			for(Food menu : restMenus)
				if(menuId.getId().equals(menu.getId()))
					retMenu=menu;
		}
		return buildMenu(retMenu);
	}
	
	@Override
	public List<Menu> searchMenus(String descriptionText) throws BadTextFault_Exception {
		
		List<Menu> retMenus = null;
		Collection<Food> restMenus = Restaurant.getInstance().getMenus();

		synchronized (restMenus) {
		
			for(Food menu : restMenus)
				if(menu.getEntrada().contains(descriptionText))
					retMenus.add(buildMenu(menu));
		
		}
		return retMenus;
	}

	@Override
	public MenuOrder orderMenu(MenuId arg0, int arg1)
			throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
		// TODO Auto-generated method stub
		return null;
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
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInit(List<MenuInit> initialMenus) throws BadInitFault_Exception {
		// TODO Auto-generated method stub
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

	
	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
	private void throwBadInit(final String message) throws BadInitFault_Exception {
		BadInitFault faultInfo = new BadInitFault();
		faultInfo.message = message;
		throw new BadInitFault_Exception(message, faultInfo);
	}



}
