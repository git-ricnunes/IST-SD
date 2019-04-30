package com.forkexec.cc.ws.it;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * Class that tests Ping operation
 */
public class CreditCartIT extends BaseIT {

	@Test
	public void validateNumberCCTest()   {
		
	
		String validMastercard="5291329339651663";
		
		boolean restTest = client.validateNumber(validMastercard);
		
		assertTrue(restTest);

	}
	
	@Test
	public void validateNumberCCTestFalse()   {
		
		String validMastercard="1";
		
		boolean restTest = client.validateNumber(validMastercard);
		
		assertFalse(restTest);

	}

}
