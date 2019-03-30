package com.forkexec.hub.ws.it;

import static org.junit.Assert.*;

import org.junit.Test;

import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

/**
 * Class that tests Ping operation
 */
public class ActivateAccountsIT extends BaseIT {
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void activateNullIdUserTest() throws InvalidUserIdFault_Exception {
	
		  String email = null;
		  client.activateAccount(email);
		
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void activateEmptyIdUserTest() throws InvalidUserIdFault_Exception {
		
		String email = "";
		client.activateAccount(email);	
		
	}
	
	@Test
	public void activateValidUserTest() throws InvalidUserIdFault_Exception {
		
		  String email = "new@ist.pt";
		  client.activateAccount(email);
		
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void activateDuplicateddUserTest() throws InvalidUserIdFault_Exception {
		
		  String email = "new@ist.pt";
		  client.activateAccount(email);
				
	}
	
	@Test
	public void creditsValidUserTest() throws InvalidUserIdFault_Exception {
		  String email = "new@ist.pt";
		  assertEquals(client.accountBalance(email), 100);
			
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void creditsNullUserTest() throws InvalidUserIdFault_Exception {
		  String email = null;
		  assertEquals(client.accountBalance(email), 100);
			
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void creditsInvalidUserTest() throws InvalidUserIdFault_Exception {
		  String email = "";
		  assertEquals(client.accountBalance(email), 100);
			
	}

}
