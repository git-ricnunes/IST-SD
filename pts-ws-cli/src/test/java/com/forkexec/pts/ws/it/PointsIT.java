package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Test;

import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.*;

public class PointsIT extends BaseIT {
		
	@After
    public void tearDown() {
		
//		client.ctrlClear();
        
    }
	
	@Test
	public void okQCTest() throws InvalidEmailFault_Exception    {
		
		String validemail = "ricanune@ist.utl.pt";
		int pointsToAdd= 10;
		int pointsToSpend= 80;
		
		client.setVerbose(false);

		client.addPoints(validemail, pointsToAdd);

		client.addPoints(validemail, pointsToAdd);
		
		int pointsTest = client.pointsBalance(validemail);
		
		client.addPoints(validemail, pointsToAdd);
	
		client.spendPoints(validemail, pointsToSpend);
		
		pointsTest = client.pointsBalance(validemail);

		assertEquals(pointsTest, 50);
		

	}

	@Test
	public void failureQCTest() throws InvalidEmailFault_Exception    {
		
		String validemail = "ricanune@ist.utl.pt";
		int pointsToAdd= 10;
		int pointsToSpend= 80;
		
		client.setVerbose(false);

		client.addPoints(validemail, pointsToAdd);
		
		try {
			System.out.println("###Start Sleeping: Sleeping for 30 seconds Start server 3####");
			TimeUnit.SECONDS.sleep(15);
		} catch (InterruptedException e) {
			//ignore
		}
		
		System.out.println("###End Sleeping####");

		client.addPoints(validemail, pointsToAdd);
		
		int pointsTest = client.pointsBalance(validemail);
		
		client.addPoints(validemail, pointsToAdd);
		
		try {
			System.out.println("###Start Sleeping:Sleeping for 5 seconds Stop server 2####");
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			//ignore
		}
		
		System.out.println("###End Sleeping####");
		
		try {
			System.out.println("###Start Sleeping:Sleeping for 30 seconds Start server 2####");
			TimeUnit.SECONDS.sleep(15);
		} catch (InterruptedException e) {
			//ignore
		}
		
		System.out.println("###End Sleeping####");

		client.spendPoints(validemail, pointsToSpend);
		
		pointsTest = client.pointsBalance(validemail);

		assertEquals(pointsTest, 50);
		

	}

}


