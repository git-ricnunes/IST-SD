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
	private Map<String,Credit> userPoints = Collections.synchronizedMap(new HashMap<String, Credit>());
	private int version;
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

		Credit credit = new Credit();
		
		credit.setValue(DEFAULT_INITIAL_BALANCE);
		
		userPoints.put(email, credit);
	}
	
	public synchronized Credit addCredit(String email,int points) throws  InvalidEmailFault_Exception, InvalidPointsFault_Exception  {
		
		AtomicInteger newBalance = new AtomicInteger();
		
		if(email==null||email.equals("")||userPoints.get(email)==null)
			throw new InvalidEmailFault_Exception("Invalid email!",null);

		if( points <=0)
			throw new InvalidPointsFault_Exception("Points added must be higher than 0.",null);
				
		Credit credit = userPoints.get(email);
			
		newBalance.addAndGet(points);
		
		credit.setValue(newBalance.get());
		credit.setTag();
		
		userPoints.put(email,credit);
		
		return credit;

	}

	public synchronized Credit subtractCredit(String email,int points) throws  InvalidEmailFault_Exception, InvalidPointsFault_Exception,
	NotEnoughBalanceFault_Exception  {
		
		if(email==null||email.equals("")||userPoints.get(email)==null)
			throw new InvalidEmailFault_Exception("Invalid email!",null);
		
		if( points <=0)
			throw new InvalidPointsFault_Exception("Points subtract must be higher than 0",null);
		
		
		Credit credit = userPoints.get(email);
		
		Credit backupCredit = credit;
		
		AtomicInteger newBalance = new AtomicInteger();
		newBalance.addAndGet(credit.getValue()+(points * -1));
		
		credit.setValue(newBalance.get());
		

		if (userPoints.get(email).getValue() < 0){
			
			userPoints.put(email,backupCredit);
			throw  new NotEnoughBalanceFault_Exception("Not enough credit!",null);
			
		}else{
			
			credit.setTag();
			userPoints.put(email,credit);
		
		}
		
		return credit ;
		
	}

	public synchronized Credit getCredit(String email) throws  InvalidEmailFault_Exception {
				
		if(email==null||email.equals("")||userPoints.get(email)==null)
			throw new InvalidEmailFault_Exception("Invalid email!",null);
				
			Credit credit = userPoints.get(email);
			credit.setTag();

			return  credit;
			
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
    
    
}
