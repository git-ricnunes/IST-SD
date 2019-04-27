package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import com.forkexec.pts.ws.CreditView;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidPointsFault_Exception;
import com.forkexec.pts.ws.NotEnoughBalanceFault_Exception;


public class PointsIT extends BaseIT {
/*	
	@After
    public void tearDown() {
        
    }
	
	@Test
	public void activateUserTest() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception    {
		
		
		String validemail = "mrasquin1@ist.utl.pt";
		
		client.activateUser(validemail);

	}
	
	@Test(expected = EmailAlreadyExistsFault_Exception.class)
	public void activateExistingUserTest() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception    {
		
		
		String validemail = "mrasquin2@ist.utl.pt";
		
		client.activateUser(validemail);
		client.activateUser(validemail);

	}
	
	@Test(expected = InvalidEmailFault_Exception.class)
	public void activateInvalidUserTest() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception    {
		
		
		String invalidemail = "";
		
		client.activateUser(invalidemail);

	}
	

	@Test
	public void pointsBalanceTest() throws InvalidEmailFault_Exception    {
		
		String validemail = "ricanune1@ist.utl.pt";
		
		
		try {
			client.activateUser(validemail);
		} catch (EmailAlreadyExistsFault_Exception e) {
		
		}
	
				
		CreditView pointsTest = client.pointsBalance(validemail);
		
		assertEquals(pointsTest, 100);


	}
	
	@Test(expected = InvalidEmailFault_Exception.class)
	public void pointsBalanceInvalidEmailFaultTest() throws InvalidEmailFault_Exception    {
		
		String validemail = "";

		CreditView pointsTest = client.pointsBalance(validemail);
		
		assertEquals(pointsTest, 100);


	}
	
	@Test(expected = InvalidEmailFault_Exception.class)
	public void pointsBalanceInvalidEmailFaultTest2() throws InvalidEmailFault_Exception    {
		
		String validemail = null;

		CreditView pointsTest = client.pointsBalance(validemail);
		
		assertEquals(pointsTest, 100);
		
	}
	
	
	@Test
	public void addPointsTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception    {
		
		String validemail = "ricanune2@ist.utl.pt";
		int pointsToAdd= 10;
		
		
		try {
			client.activateUser(validemail);
		} catch (EmailAlreadyExistsFault_Exception e) {
		
		}
	
		int pointsTest = client.addPoints(validemail, pointsToAdd);
		
		assertEquals(pointsTest, 110);


	}
	
	@Test(expected = InvalidEmailFault_Exception.class)
	public void addPointsInvalidEmailTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception    {
		
		String validemail = "";
		int pointsToAdd= 10;
		
		

		int pointsTest = client.addPoints(validemail, pointsToAdd);
		
		assertEquals(pointsTest, 110);

	}
	
	@Test(expected = InvalidPointsFault_Exception.class)
	public void addPointsInvalidPointsTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception    {
		
		String validemail = "ricanune3@ist.utl.pt";
		int pointsToAdd= -1;
		
		
	
		int pointsTest = client.addPoints(validemail, pointsToAdd);
		
		assertEquals(pointsTest, 110);

	}
	
	@Test
	public void spendPointsTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception   {
		
		String validemail = "ricanune4@ist.utl.pt";
		int pointsToSpend = 20;
		
		try {
			client.activateUser(validemail);
		} catch (EmailAlreadyExistsFault_Exception e) {
		
		}
		

		CreditView pointsTest = client.spendPoints(validemail, pointsToSpend);
		
		assertEquals(pointsTest, 80);


 	}
	
	@Test(expected = InvalidEmailFault_Exception.class)
	public void spendPointsInvalidEmailFaultTest() throws InvalidEmailFault_Exception, 
	InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception   {
		
		String validemail = "";
		int pointsToSpend = 10;

		CreditView pointsTest = client.spendPoints(validemail, pointsToSpend);
		
		assertEquals(pointsTest, 90);


	}
	
	@Test(expected = InvalidEmailFault_Exception.class)
	public void spendPointsInvalidEmailFaultTest2() throws InvalidEmailFault_Exception, 
	InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception   {
		
		String validemail = null;
		int pointsToSpend = 10;

		CreditView pointsTest = client.spendPoints(validemail, pointsToSpend);
		
		assertEquals(pointsTest, 90);


	}
	
	
	@Test(expected = InvalidPointsFault_Exception.class)
	public void spendPointsInvalidPointsFaultTest() throws InvalidEmailFault_Exception, 
	InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception   {
		
		String validemail = "ricanune5@ist.utl.pt";
		int pointsToSpend = -1;
	
		
		CreditView pointsTest = client.spendPoints(validemail, pointsToSpend);
		
		assertEquals(pointsTest, 90);

	}
	
	@Test(expected = NotEnoughBalanceFault_Exception.class)
	public void spendPointsNotEnoughBalanceFaultTest() throws InvalidEmailFault_Exception, 
	InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception   {
		
		String validemail = "ricanune6@ist.utl.pt";
		int pointsToSpend = 200;
		
		
		try {
			client.activateUser(validemail);
		} catch (EmailAlreadyExistsFault_Exception e) {
		
		}
		
		
		CreditView pointsTest = client.spendPoints(validemail, pointsToSpend);
		
		assertEquals(pointsTest, 90);


		}
*/
}


