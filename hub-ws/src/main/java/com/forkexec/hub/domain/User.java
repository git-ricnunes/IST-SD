package com.forkexec.hub.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Domain class that represents the User and deals with their creation
 * 
 *
 */
public class User {

	private String email;
	private List<FoodHub> cart;
	
	public User(String email) {
		this.email = email;
		this.cart = new ArrayList<FoodHub>();
	}
	
	public User getUserByEmail(String email){
		
		
		
		return this;
	}
	
	
	public List<FoodHub> getCart() {
		return cart;
	}

	public void setCart(List<FoodHub> cart) {
		this.cart = cart;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	
}
