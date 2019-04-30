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
    
    public void init(int initPoints){
    	this.initialBalance.set(initPoints);
    }
    
    public void clear(){
    	this.userPoints.clear();
    }
    
	public synchronized void createUser(String email) throws  InvalidEmailFault_Exception,EmailAlreadyExistsFault_Exception {
		
		if(email==null||email.equals(""))
			throw new InvalidEmailFault_Exception("Invalid email!",null);
				
		if( userPoints.containsKey(email) )
			throw new EmailAlreadyExistsFault_Exception("Email already exists!",null);

			userPoints.put(email, initialBalance);
	}
	
	public synchronized int addCredit(String email,int points) throws  InvalidEmailFault_Exception, InvalidPointsFault_Exception  {
		

		if(email==null||email.equals("")||userPoints.get(email)==null)
			throw new InvalidEmailFault_Exception("Invalid email!",null);

		System.out.println("#####addCredit######"+userPoints+"######addCredit#####");

		if( points <=0)
			throw new InvalidPointsFault_Exception("Points added must be higher than 0.",null);
				
			AtomicInteger newBalance = new AtomicInteger();
			newBalance.addAndGet(userPoints.get(email).get()+points);
			
			userPoints.put(email,newBalance);
			System.out.println("#####addCredit2######"+userPoints+"######addCredit2#####");

		return userPoints.get(email).get();

	}

	public synchronized int subtractCredit(String email,int points) throws  InvalidEmailFault_Exception, InvalidPointsFault_Exception,
	NotEnoughBalanceFault_Exception  {
		
		if(email==null||email.equals("")||userPoints.get(email)==null)
			throw new InvalidEmailFault_Exception("Invalid email!",null);
		
		if( points <=0)
			throw new InvalidPointsFault_Exception("Points subtract must be higher than 0",null);
		
		System.out.println("#####subtractCredit######"+userPoints+"######subtractCredit#####");
		
		
		AtomicInteger newBalance = new AtomicInteger();
		newBalance.addAndGet(userPoints.get(email).get()+(points * -1));
		
		userPoints.put(email,newBalance);

		if (userPoints.get(email).get() < 0){
			
			AtomicInteger oldBalance = new AtomicInteger();
			oldBalance.addAndGet(userPoints.get(email).get()+points);
			
			userPoints.put(email,oldBalance);
			throw  new NotEnoughBalanceFault_Exception("Not enough credit!",null);
		}
		System.out.println("#####subtractCredit2######"+userPoints+"######subtractCredit2#####");

		return userPoints.get(email).get() ;
		
	}

	public synchronized int getCredit(String email) throws  InvalidEmailFault_Exception {
		
		int returnPoints=0;
		
		if(email==null||email.equals("")||userPoints.get(email)==null)
			throw new InvalidEmailFault_Exception("Invalid email!",null);
				
		System.out.println("#####getCredit######"+userPoints+"######getCredit#####");
			returnPoints = userPoints.get(email).get();
		System.out.println("#####getCredit2######"+userPoints+"######getCredit2#####");

			return  returnPoints;
			
	}
    
    
}
