package com.forkexec.hub.domain;

import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.pts.domain.exception.InsufficientCreditsException;

/**
 * 
 * Domain class that represents the User and deals with their creation
 * 
 *
 */
public class User {

	private String email;
	private AtomicInteger points;
	private boolean active;
	
	public User(String email, int initialPoints) {
		this.email = email;
		this.points = new AtomicInteger(initialPoints);
		this.active= true;
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

	public boolean isStatus() {
		return active;
	}

	public void setStatus(boolean active) {
		this.active = active;
	}

	
}
