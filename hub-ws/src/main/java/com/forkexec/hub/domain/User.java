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
	
		if(newPoints.get() > 0) {
			 points.set(newPoints.get()); 
		 } else {
			 throw new InsufficientCreditsException();
		 }
	}

	
	public synchronized void incrementBalance(int amount){
		 balance.getAndAdd(amount);
	}
	
	public String getEmail() {
		return email;
	}
	
	public boolean getHasBina() {
		return hasBina.get();
	}
	

	public int getCredit() {
		return balance.get();
	}

	public synchronized void validateCanRentBina() throws InsufficientCreditsException{
		if(getCredit() <= 0) {
			throw new InsufficientCreditsException();
		}
		
	}

	public synchronized void effectiveRent() throws InsufficientCreditsException {
		decrementBalance();
		hasBina.set(true);
	}

	public synchronized void effectiveReturn(int prize) throws UserHasNoBinaException {
		if( ! getHasBina()) {
			throw new UserHasNoBinaException();
		}
		hasBina.set(false);
		incrementBalance(prize);
	}


	
}
