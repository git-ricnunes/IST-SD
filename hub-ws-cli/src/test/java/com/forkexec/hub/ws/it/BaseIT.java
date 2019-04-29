package com.forkexec.hub.ws.it;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;
import com.forkexec.hub.ws.cli.*;
/**
 * Base class for testing a Park Load properties from test.properties
 */
public class BaseIT {

	private static final String TEST_PROP_FILE = "/test.properties";
	protected static Properties testProps;

	protected static HubClient client;

	@BeforeClass
	public static void oneTimeSetup() throws Exception {
		testProps = new Properties();
		try {
			testProps.load(BaseIT.class.getResourceAsStream(TEST_PROP_FILE));
			System.out.println("Loaded test properties:");
			System.out.println(testProps);
		} catch (IOException e) {
			final String msg = String.format("Could not load properties file {}", TEST_PROP_FILE);
			System.out.println(msg);
			throw e;
		}

		final String uddiEnabled = testProps.getProperty("uddi.enabled");
		final String verboseEnabled = testProps.getProperty("verbose.enabled");

		final String uddiURL = testProps.getProperty("uddi.url");
		final String wsName = testProps.getProperty("ws.name");
		final String wsURL = testProps.getProperty("ws.url");

		if ("true".equalsIgnoreCase(uddiEnabled)) {
			client = new HubClient(uddiURL, wsName);
		} else {
			client = new HubClient(wsURL);
		}
		client.setVerbose("true".equalsIgnoreCase(verboseEnabled));
		
		//Initialize yummi menus
		FoodInit foodInit = new FoodInit();
		FoodId foodid = new FoodId();
		foodid.setMenuId("Menu diaria");
		foodid.setRestaurantId("A65_Restaurant1");
		
		Food food = new Food();
		
		food.setId(foodid);
		food.setEntree("Manteiga Mimosa");
		food.setPlate("Salsicha da lata com batata frita");
		food.setDessert("Mousse de Chocolate");
		food.setPreparationTime(30);
		food.setPrice(5);
		
		foodInit.setFood(food);
		foodInit.setQuantity(100);
		
		//
		FoodInit foodInit2 = new FoodInit();
		FoodId foodid2 = new FoodId();
		foodid2.setMenuId("Menu gourmet");
		foodid2.setRestaurantId("A65_Restaurant2");
		
		Food food2 = new Food();
		
		food2.setId(foodid2);
		food2.setEntree("Mini rissois de leitao");
		food2.setPlate("Tournedos Rossini");
		food2.setDessert("Crumble de maca com doce de leite e frutas secas");
		food2.setPreparationTime(35);
		food2.setPrice(200);
		
		foodInit2.setFood(food2);
		foodInit2.setQuantity(20);
		
		//
		FoodInit foodInit3 = new FoodInit();
		FoodId foodid3 = new FoodId();
		foodid3.setMenuId("Menu +- gourmet");
		foodid3.setRestaurantId("A65_Restaurant1");
		
		Food food3 = new Food();
		
		food3.setId(foodid3);
		food3.setEntree("Mini rissois de leitao");
		food3.setPlate("Tournedos Rossini");
		food3.setDessert("Crumble de maca com doce de leite e frutas secas");
		food3.setPreparationTime(40);
		food3.setPrice(10);
		
		foodInit3.setFood(food3);
		foodInit3.setQuantity(20);

		//
		
		FoodInit foodInit4 = new FoodInit();
		FoodId foodid4 = new FoodId();
		foodid4.setMenuId("Menu italia");
		foodid4.setRestaurantId("A65_Restaurant2");
		
		Food food4 = new Food();
		
		food4.setId(foodid3);
		food4.setEntree("Bruschetta");
		food4.setPlate("Pizza calzone com chourico picante da Toscania");
		food4.setDessert("Panna cotta de frutos vermelhos");
		food4.setPreparationTime(15);
		food4.setPrice(40);
				
		foodInit3.setFood(food4);
		foodInit3.setQuantity(20);
	
		
		List<FoodInit> initialMenus = new ArrayList<FoodInit>();
		
		initialMenus.add(foodInit);
		initialMenus.add(foodInit2);
		initialMenus.add(foodInit3);
		initialMenus.add(foodInit4);
		
	 client.ctrlInitFood(initialMenus);
	 
	 client.ctrlInitUserPoints(100);
	 
	  
	}

	@AfterClass
	public static void cleanup() throws Exception {
		
		testProps = new Properties();
		try {
			testProps.load(BaseIT.class.getResourceAsStream(TEST_PROP_FILE));
			System.out.println("Loaded test properties:");
			System.out.println(testProps);
		} catch (IOException e) {
			final String msg = String.format("Could not load properties file {}", TEST_PROP_FILE);
			System.out.println(msg);
			throw e;
		}

		final String uddiEnabled = testProps.getProperty("uddi.enabled");
		final String verboseEnabled = testProps.getProperty("verbose.enabled");

		final String uddiURL = testProps.getProperty("uddi.url");
		final String wsName = testProps.getProperty("ws.name");
		final String wsURL = testProps.getProperty("ws.url");

		if ("true".equalsIgnoreCase(uddiEnabled)) {
			client = new HubClient(uddiURL, wsName);
		} else {
			client = new HubClient(wsURL);
		}
		client.setVerbose("true".equalsIgnoreCase(verboseEnabled));
		
		client.ctrlClear();
		 
	}

}
