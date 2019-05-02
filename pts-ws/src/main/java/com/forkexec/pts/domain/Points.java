package com.forkexec.pts.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;

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
	private static Credit credit = new Credit();

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
    	Points.credit=new Credit();
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
	

	public synchronized Credit getCredit(String email) throws  InvalidEmailFault_Exception {


		if(email==null||email.equals("")||userPoints.get(email)==null)
			throw new InvalidEmailFault_Exception("Invalid email!",null);
				
			credit.setVal(userPoints.get(email).get());
			System.out.println("#####Read Tag:"+credit.getTag());
			System.out.println("#####Read Value:"+credit.getVal());

			return  credit;
			
	}
	
	public synchronized int addCredit(String email,int tag,int points) throws  InvalidEmailFault_Exception{
		

		if(email==null||email.equals("")||userPoints.get(email)==null)
			throw new InvalidEmailFault_Exception("Invalid email!",null);
				
			AtomicInteger newBalance = new AtomicInteger();
			newBalance.addAndGet(points);
			
			userPoints.put(email,newBalance);
			credit.setTag(tag);
			credit.setVal(userPoints.get(email).get());

			System.out.println("#####Write Tag:"+credit.getTag());
			System.out.println("#####Write Value:"+credit.getVal());

		return userPoints.get(email).get();

	}
    
    
}
