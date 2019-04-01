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
	private List<CartItem> cart;
	
	public User(String email) {
		this.email = email;
		this.cart = new ArrayList<CartItem>();
	}
	
	public User getUserByEmail(String email){
		
		
		
		return this;
	}
	
	
	public List<CartItem> getCart() {
		return cart;
	}

	public void setCart(List<CartItem> cart) {
		this.cart = cart;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	
}
