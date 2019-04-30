package com.forkexec.hub.ws.it;

import static org.junit.Assert.*;

import org.junit.Test;

import com.forkexec.hub.ws.InvalidCreditCardFault_Exception;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidTextFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

/**
 * Class that tests Ping operation
 */
public class HubIT extends BaseIT {
	
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
	
	@Test
	public void ccDevTest() throws InvalidUserIdFault_Exception,InvalidCreditCardFault_Exception,InvalidMoneyFault_Exception {
		  String email = "new@ist.pt";
			
		  client.loadAccount(email, 10, "4024007102923926");
		  assertEquals(client.accountBalance(email), 1100);
	}
	
	@Test
	public void searchDealTest() throws  InvalidTextFault_Exception {
	
		  String description = "leitao";
		  
		  System.out.println(client.searchDeal(description));
		  System.out.println(client.searchHungry(description));
		  assertTrue(client.searchDeal(description).get(0).getPrice() < client.searchDeal(description).get(1).getPrice());
		  
	}
	
	//searchHungry
	//addFoodToCart
	//clearCart
	//orderCart
	//accountBalance
	//getFood
	//cartContents

}
