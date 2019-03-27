package com.forkexec.hub.domain;

import java.util.Collection;

import com.forkexec.rst.ws.BadInitFault_Exception;
import com.forkexec.rst.ws.cli.RestaurantClient;


/**
 * Hub
 *
 * A restaurants hub server.
 *
 */

public class Hub {
	

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
	
}
