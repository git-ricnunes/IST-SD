package com.forkexec.hub.domain;

import java.util.Comparator;

import com.forkexec.hub.ws.Food;


public class FoodTimeComparator implements Comparator<Food> {


	@Override
	public int compare(Food f1, Food f2) {
		f1.getPreparationTime();
		
		return f1.getPreparationTime() - f2.getPreparationTime();
	}

}
