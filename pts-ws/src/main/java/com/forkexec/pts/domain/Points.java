package com.forkexec.pts.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidPointsFault_Exception;
import com.forkexec.pts.ws.NotEnoughBalanceFault_Exception;

/**
 * Points
 * <p>
 * A points server.
 */
public class Points {
	

    /**
     * Constant representing the default initial balance for every new client
     */
    private static final int DEFAULT_INITIAL_BALANCE = 100;
	private Map<String,AtomicInteger> userPoints = Collections.synchronizedMap(new HashMap<String, AtomicInteger>());;

    /**
     * Global with the current value for the initial balance of every new client
     */
    private final AtomicInteger initialBalance = new AtomicInteger(DEFAULT_INITIAL_BALANCE);

    // Singleton -------------------------------------------------------------

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private Points() { }


	/**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        private static final Points INSTANCE = new Points();
    }

    public static synchronized Points getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
	public void createUser(String email) throws  InvalidEmailFault_Exception,EmailAlreadyExistsFault_Exception {
		
		if(email==null||email.equals(""))
			throw new InvalidEmailFault_Exception("Invalid email!",null);
				
		if( userPoints.containsKey(email) )
			throw new EmailAlreadyExistsFault_Exception("Email already exists!",null);

			userPoints.put(email, initialBalance);
	}
	
	public int addCredit(String email,int points) throws  InvalidEmailFault_Exception, InvalidPointsFault_Exception  {
		
		if(email==null||email.equals(""))
			throw new InvalidEmailFault_Exception("Invalid email!",null);
		
		if( points <=0)
			throw new InvalidPointsFault_Exception("Points added must be higher than 0",null);
		
			userPoints.get(email).getAndAdd(points);
	
		return userPoints.get(email).get();

	}

	public int subtractCredit(String email,int points) throws  InvalidEmailFault_Exception, InvalidPointsFault_Exception,
	NotEnoughBalanceFault_Exception  {
		
		if(email==null||email.equals(""))
			throw new InvalidEmailFault_Exception("Invalid email!",null);
		
		if( points <=0)
			throw new InvalidPointsFault_Exception("Points subtract must be higher than 0",null);
		
		userPoints.get(email).getAndAdd(points * -1);

		if (userPoints.get(email).get() < 0){
			userPoints.get(email).getAndAdd(points);
			throw  new NotEnoughBalanceFault_Exception("Not enough credit!",null);
		}
		
		return userPoints.get(email).get() ;
		
	}

	public int getCredit(String email) throws  InvalidEmailFault_Exception {
		
		int returnPoints=0;
		
		if(email==null||email.equals(""))
			throw new InvalidEmailFault_Exception("Invalid email!",null);
				
		System.out.println(email+userPoints.get(email));
			returnPoints = userPoints.get(email).get();
			return  returnPoints;
			
	}
    
    
}
