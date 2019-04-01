package com.forkexec.hub.domain;

import java.util.Comparator;

import com.forkexec.hub.ws.Food;


public class FoodPriceComparator implements Comparator<Food> {


	@Override
	public int compare(Food f1, Food f2) {
		
		return f1.getPrice() - f2.getPrice();
	}

}
