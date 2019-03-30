package com.forkexec.hub.domain;

import java.util.Collection;
import com.forkexec.rst.ws.cli.*;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
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
	
	//TODO
	
	public void createUser(String email) throws  InvalidUserIdFault_Exception {
	
	String uddiUrl = Hub.getInstance().getUddiURL();
	PointsClient pc = null;
	
		try {
			pc = new PointsClient(uddiUrl, "A65_pts1");
		} catch (PointsClientException e) {
		}
		try {
			pc.activateUser(email);
		} catch (EmailAlreadyExistsFault_Exception | InvalidEmailFault_Exception e) {
			
			throw new InvalidUserIdFault_Exception("UserID invalid!" +e.getMessage(), null);
		}

			
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
