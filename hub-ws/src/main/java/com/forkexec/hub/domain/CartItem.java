package com.forkexec.hub.domain;

public class CartItem {
	
	String restaurantId;
	String menuId;
	
	public CartItem(String restaurantId, String menuId) {
		this.restaurantId = restaurantId;
		this.menuId = menuId;
	}
	
	public String getRestaurantId() {
		return restaurantId;
	}
	
	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}
	
	public String getMenuId() {
		return menuId;
	}
	
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	

}
