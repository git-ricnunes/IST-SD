package com.forkexec.cc.ws.it;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.forkexec.cc.ws.it.BaseIT;

public class PingIT extends BaseIT {

	// tests
	// assertEquals(expected, actual);

	// public String ping(String x)

	@Test
	public void pingEmptyTest() {
		assertNotNull(client.ping("test"));
	}

}
