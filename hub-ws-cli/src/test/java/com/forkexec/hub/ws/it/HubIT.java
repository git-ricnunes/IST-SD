package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

/**
 * Class that tests Ping operation
 */
public class HubIT extends BaseIT {
	
	
	
	@Test
	public void GetPointsTest() throws InvalidUserIdFault_Exception {
		

		  int i = client.accountBalance("test@i.pt");
		  assertEquals(100,i);	


	}
	
}
