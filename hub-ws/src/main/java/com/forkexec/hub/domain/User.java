package com.forkexec.hub.domain;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.hub.domain.exception.InsufficientCreditsException;;

/**
 * 
 * Domain class that represents the User and deals with their creation
 * 
 *
 */
public class User {

	private String email;
	private String password;
	private AtomicInteger points;
	
	public User(String email,String password, int initialPoints) {
		this.email = email;
		this.password=password;
		this.points = new AtomicInteger(initialPoints);
	}
	
	public synchronized void decrementPoints(int pointsToTake) throws InsufficientCreditsException{
		AtomicInteger newPoints= points;
		newPoints.set(newPoints.get()-pointsToTake); 
	
		if(newPoints.get() >= 0) {
			 points.set(newPoints.get()); 
		 } else {
			 throw new InsufficientCreditsException();
		 }
	}

	
	public synchronized void incrementPoints(int pointsToGive){
		 points.getAndAdd(pointsToGive);
	}
	

	public int getCredit() {
		return points.get();
	}


		
	public synchronized void validateCanOrder() throws InsufficientCreditsException{
		if(getCredit() <= 0) {
			throw new InsufficientCreditsException();
		}
		
	}

	public synchronized void effectiveRent(int pointsToTake) throws InsufficientCreditsException {
		decrementPoints(pointsToTake);
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AtomicInteger getPoints() {
		return points;
	}

	public void setPoints(AtomicInteger points) {
		this.points = points;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
