package com.forkexec.rst.domain;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.rst.ws.BadInitFault_Exception;

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
	
	
	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Restaurant() {
		// Initialization of default values
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


	// TODO 
	
	public synchronized void init(String id ,Collection<Food> menus) throws BadInitFault_Exception {
 		if(id.equals(""))
 			throw new BadInitFault_Exception("Restaurant ID cannot be empty", null);
		this.Menus = menus;
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
