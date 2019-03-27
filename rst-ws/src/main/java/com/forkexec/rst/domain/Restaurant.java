package com.forkexec.rst.domain;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.rst.ws.BadInitFault_Exception;
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
	private Collection<Food> Menus = null;
	private String orderid;
	
	
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
	
	public synchronized void init(Collection<Food> menus) throws BadInitFault_Exception {
 		if(menus.isEmpty())
 			throw new BadInitFault_Exception("Failed to initiate restaurant", null);
		this.id=id;
 		this.Menus = menus;
 		this.orderid="0";
 	}
	
	public synchronized void reset() {
		this.Menus.clear();
		this.orderid="0";
	}
	
	public synchronized Food getMenu(MenuId menuid) {
		
		Food retMenu = null;
		
		for(Food menu : Menus)
			if(menuid.getId().equals(menu.getId()))
				retMenu=menu;
		
		return retMenu;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
	public String getOrderId() {
		return orderid;
	}


	public void setOrderId(String orderid) {
		this.orderid = orderid;
	}


	public Collection<Food> getMenus() {
		return Menus;
	}

	
	public void setMenus(Collection<Food> menus) {
		Menus = menus;
	}
	
	
	
}
