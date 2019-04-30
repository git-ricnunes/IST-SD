package com.forkexec.cc.ws.cli;

/** 
 * Client application. 
 * 
 * Looks for Hub using UDDI and arguments provided
 */
public class CreditCardApp {

	public static void main(String[] args) throws Exception {
		// Check arguments.
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + CreditCardApp.class.getName() + " wsURL OR uddiURL wsName");
			return;
		}
		
		String wsURL = null;
		if (args.length == 1) {
			wsURL = args[0];
		} 

		// Create client.
		CreditCardClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new CreditCardClient();
		} 

		// The following remote invocation is just a basic example.
		// The actual tests are made using JUnit.

	
	}

}
