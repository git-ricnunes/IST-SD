package com.forkexec.rst.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.rst.ws.BadInitFault_Exception;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;

/**
 * Restaurant
 *
 * A restaurant server.
 *
 */
public class Restaurant {
	
	/**
	 * Restaurant Client
	 *
	 * Station is defined by an ID, a set of Coordinates, capacity and retrieval bonus. 
	 *
	 */
	
	private String id;
	private Collection<Food> Menus = new ArrayList<Food>();
	
	
	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Restaurant() {
		reset();
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Restaurant INSTANCE = new Restaurant();
	}

	public static synchronized Restaurant getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public void init(Collection<Food> menus,String restID) throws BadInitFault_Exception {
 		if(menus.isEmpty())
 			throw new BadInitFault_Exception("Failed to initiate restaurant", null);

 		for(Food menu:menus){
 			this.Menus.add(menu);
 		}
 		
 		this.Menus = menus;
 		this.id=restID;
 	}
	
	public synchronized void reset() {
		this.Menus.clear();
	}
	
	public synchronized Food getMenu(MenuId menuid) throws BadMenuIdFault_Exception {
		
		Food retMenu = null;

		if(menuid==null || menuid.getId()== null || menuid.getId()=="" )
			throw new BadMenuIdFault_Exception("Invalid Menuid!",null);

		ArrayList<Food> listMenus = (ArrayList<Food>) Restaurant.getInstance().getMenus();
		
		for(Food menu : listMenus){
			if(menuid.getId().equals(menu.getId())){
				retMenu=menu;
				break;
			}
		}
		return retMenu;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Collection<Food> getMenus() {
		return Menus;
	}

	
	public void setMenus(Collection<Food> menus) {
		Menus = menus;
	}
	
	
	
}
