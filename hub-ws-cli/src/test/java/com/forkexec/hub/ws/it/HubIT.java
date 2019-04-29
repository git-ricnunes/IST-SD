package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.forkexec.hub.ws.InvalidCreditCardFault_Exception;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

/**
 * Class that tests Ping operation
 */
public class HubIT extends BaseIT {
	
	
	
	@Test
	public void ReadTest() throws InvalidUserIdFault_Exception {
		

		  int credits = client.accountBalance("test@iST.pt");
		  assertEquals(100,credits);	


	}
	
	@Test
	public void WriteTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
		
		  client.loadAccount("tes@ist.pt", 10, "5291329339651663");
	
		  int credits = client.accountBalance("tes@ist.pt");
	
		  assertEquals(1100,credits);	


	}
	
	
}
