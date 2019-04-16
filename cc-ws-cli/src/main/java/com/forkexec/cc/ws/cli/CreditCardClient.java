package com.forkexec.cc.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.*;

/**
 * Client port wrapper.
 *
 * Adds easier end point address configuration to the Port generated by
 * wsimport.
 */
public class CreditCardClient implements CreditCard {

	/** WS service */
	CreditCardImplService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	CreditCard port = null;

	/** WS name */
	private String wsName = null;

	/** WS end point address */
	private String wsURL = null; // default value is defined inside WSDL

	public String getWsURL() {
		return wsURL;
	}

	/** constructor with provided UDDI location and name */
	public CreditCardClient() throws  CreditCardClientException{
		this.service = new CreditCardImplService();
		this.port=this.service.getCreditCardImplPort();
	}

	@Override
	public String ping(String name) {
		return port.ping(name);
	}

	@Override
	public boolean validateNumber(String numberAsString) {
		return port.validateNumber(numberAsString);
	}


}