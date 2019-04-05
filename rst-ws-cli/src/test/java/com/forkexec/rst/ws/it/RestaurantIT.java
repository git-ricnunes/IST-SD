package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.BadQuantityFault_Exception;
import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.InsufficientQuantityFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuOrder;

/**
 * Class that tests Ping operation
 */
public class RestaurantIT extends BaseIT {
	
	@Test
	public void getMenuTest() throws BadMenuIdFault_Exception  {
		
		MenuId menuId = new MenuId();
		menuId.setId("Menu diaria");
		
		String restTest = client.getMenu(menuId).getDessert();
		
		assertEquals(restTest, "Mousse de Chocolate");

	}
	
	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuTBadTextFaultest() throws BadMenuIdFault_Exception  {
		
		MenuId menuId = new MenuId();
		menuId.setId(null);
	
		String restTest = client.getMenu(menuId).getDessert();
			
		assertEquals(restTest, "Mousse de Chocolate");

	}
	
	@Test
	public void searchMenusTest() throws BadTextFault_Exception  {
		
		String testSearch="leitao";
	
		List<Menu> restTest = client.searchMenus(testSearch);
						
		assertEquals(restTest.get(0).getId().getId(), "Menu gourmet");

		
	}
	
	@Test(expected = BadTextFault_Exception.class)
	public void searchMenusBadTextFaultTest() throws BadTextFault_Exception  {
		
		String testSearch="";
	
		List<Menu> restTest = client.searchMenus(testSearch);
						
		assertEquals(restTest.get(0).getId().getId(), "Menu gourmet");
		
	}	
	
	@Test
	public void orderMenuTest() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception  {
		
		MenuOrder destTest = null;
		MenuId menuid = new MenuId();
		menuid.setId("Menu diaria");
		
		int quantity = 100;
		
		destTest = client.orderMenu(menuid, quantity);
		assertEquals(destTest.getMenuId().getId(),menuid.getId() );

	}
	
	@Test(expected = BadMenuIdFault_Exception.class)
	public void orderMenuBadMenuIdFaultTest() throws BadMenuIdFault_Exception, BadQuantityFault_Exception,
	InsufficientQuantityFault_Exception  {
		
		MenuOrder destTest = null;
		MenuId menuid = new MenuId();
		menuid.setId("");
		
		int quantity = 1;
		
		destTest = client.orderMenu(menuid, quantity);
		
		assertEquals(destTest.getMenuId().getId(),menuid.getId() );

	}
	
	@Test(expected = BadQuantityFault_Exception.class)
	public void orderMenuBadQuantityFaultTest() throws BadMenuIdFault_Exception, BadQuantityFault_Exception,
	InsufficientQuantityFault_Exception  {
		
		MenuOrder destTest = null;
		MenuId menuid = new MenuId();
		menuid.setId("Menu gourmet");
		
		int quantity = -1;
		
		destTest = client.orderMenu(menuid, quantity);
		
		assertEquals(destTest.getMenuId().getId(),menuid.getId() );

	}
	
	@Test(expected = InsufficientQuantityFault_Exception.class)
	public void orderMenuInsufficientQuantityFaultTest() throws BadMenuIdFault_Exception, BadQuantityFault_Exception,
	InsufficientQuantityFault_Exception  {
		
		MenuOrder destTest = null;
		MenuId menuid = new MenuId();
		menuid.setId("Menu gourmet");
		
		int quantity = 25;
		
		destTest = client.orderMenu(menuid, quantity);
		
		assertEquals(destTest.getMenuId().getId(),menuid.getId() );

	}
	

}
