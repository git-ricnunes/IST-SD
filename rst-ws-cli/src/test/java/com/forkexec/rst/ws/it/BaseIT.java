package com.forkexec.rst.ws.it;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;
import com.forkexec.rst.ws.cli.RestaurantClient;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Base class for testing a Park Load properties from test.properties
 */
public class BaseIT {

	private static final String TEST_PROP_FILE = "/test.properties";
	protected static Properties testProps;

	protected static RestaurantClient client;

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
			client = new RestaurantClient(uddiURL, wsName);
		} else {
			client = new RestaurantClient(wsURL);
		}
		client.setVerbose("true".equalsIgnoreCase(verboseEnabled));
		
		
		//Initialize yummi menus
		MenuId menuId1 = new MenuId();
		menuId1.setId("Menu diaria");

		Menu menu1 = new Menu();
		menu1.setId(menuId1);
		menu1.setEntree("Manteiga Mimosa");
		menu1.setPlate("Salsicha da lata com batata frita");
		menu1.setDessert("Mousse de Chocolate");
		menu1.setPreparationTime(30);
		menu1.setPrice(5);
		
		MenuId menuId2 = new MenuId();
		menuId2.setId("Menu gourmet");

		Menu menu2 = new Menu();
		menu2.setId(menuId2);
		menu2.setEntree("Mini rissois de leitao");
		menu2.setPlate("Tournedos Rossini");
		menu2.setDessert("Crumble de maca com doce de leite e frutas secas");
		menu2.setPreparationTime(35);
		menu2.setPrice(200);

		MenuId menuId3 = new MenuId();
		menuId3.setId("Menu italia");

		Menu menu3 = new Menu();
		menu3.setId(menuId3);
		menu3.setEntree("Bruschetta");
		menu3.setPlate("Pizza calzone com chourico picante da Toscania");
		menu3.setDessert("Panna cotta de frutos vermelhos");
		menu3.setPreparationTime(15);
		menu3.setPrice(40);
				
		MenuInit menuinit1 = new MenuInit();
		menuinit1.setMenu(menu1);
		menuinit1.setQuantity(100);
		
		MenuInit menuinit2 = new MenuInit();
		menuinit2.setMenu(menu2);
		menuinit2.setQuantity(5);
		
		MenuInit menuinit3 = new MenuInit();
		menuinit3.setMenu(menu3);
		menuinit3.setQuantity(50);
		
		List<MenuInit> initialMenus = new ArrayList<MenuInit>();
		
		initialMenus.add(menuinit1);
		initialMenus.add(menuinit2);
		initialMenus.add(menuinit3);
		
		client.ctrlInit(initialMenus);
	}

	@AfterClass
	public static void cleanup()  throws Exception  {
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
			client = new RestaurantClient(uddiURL, wsName);
		} else {
			client = new RestaurantClient(wsURL);
		}
		client.setVerbose("true".equalsIgnoreCase(verboseEnabled));
		
		
		client.ctrlClear();
	}

}
