package com.forkexec.pts.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;

import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.CreditView;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.PointsPortType;
import com.forkexec.pts.ws.PointsService;
import com.forkexec.pts.ws.ReadResponse;
import com.forkexec.pts.ws.WriteResponse;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

/**
 * Client port wrapper.
 *
 * Adds easier end point address configuration to the Port generated by
 * wsimport.
 */
public class PointsClient {

	/** WS service */
	PointsService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	PointsPortType port = null;

	/** UDDI server URL */
	private String uddiURL = null;

	/** WS name */
	private String wsName = null;

	/** WS end point address */
	private String wsURL = null; // default value is defined inside WSDL
	
	/** Servers available for the QC implemetation */
	private int N;
	
	/** Quorom for the QC implementation */
	private int Q;
	
	/*Class containing the max value/tag for the QC*/
	private CreditView cv;
	
	public String getWsURL() {
		return wsURL;
	}
	
	public String getuddiURL() {
		return uddiURL;
	}

	/** output option **/
	private boolean verbose = true;

	

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/** constructor with provided web service URL */
	public PointsClient(String wsURL) throws PointsClientException {
		this.wsURL = wsURL;
		createStub();
	}

	/** constructor with provided UDDI location and name */
	public PointsClient(String uddiURL, String wsName) throws PointsClientException {
		
		this.uddiURL = uddiURL;
		this.wsName = "A65_Points";
		this.N=3;
		this.Q=(int) Math.floor(N/2)+1;
		CreditView cv=new CreditView();
		this.cv = cv;
				
		cv.setTag(0);
		cv.setValue(0);
		
	}

	/** UDDI lookup */
	private void uddiLookup() throws PointsClientException {
		try {
			if (verbose)
				System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);

			if (verbose)
				System.out.printf("Looking for '%s'%n", wsName);
			wsURL = uddiNaming.lookup(wsName);

		} catch (Exception e) {
			String msg = String.format("Client failed lookup on UDDI at %s!", uddiURL);
			throw new PointsClientException(msg, e);
		}

		if (wsURL == null) {
			String msg = String.format("Service with name %s not found on UDDI at %s", wsName, uddiURL);
			throw new PointsClientException(msg);
		}
	}

	/** Stub creation and configuration */
	private void createStub() {
		if (verbose)
			System.out.println("Creating stub ...");
		service = new PointsService();
		port = service.getPointsPort();

		if (wsURL != null) {
			if (verbose)
				System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, wsURL);
		}
	}

	// remote invocation methods ----------------------------------------------

	public void activateUser(String userEmail) throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		port.activateUser(userEmail);
	}
	
	/* * writeClient - Auxilary function for the implementation of the QC
	 * 
	 *   input : email - the user hash key to get points 
	 *   		 value - the credit value to add/subtract the user 
	 * 
	 *   description :  -This function helps the client class to implement the write function in QC fault tolerance system.
	 *   				-Get the value with highest tag availabe, increments that tag and assign the new value to that tag.
	 *   			    -It calls N (server and replication servers) times the available servers and replace the previous value with the new value with the async function.
	 *    				-It waits for each async responses of Q servers (QC majority), during this step it removes responses that were already processed.
	 * */
	
	public synchronized void writeClient(String email, int value){
		cv = readClient(email);
		int newTag = cv.getTag() + 1;

		int received = 0;
		List<Response<WriteResponse>> responses = new ArrayList<Response<WriteResponse>>();
		Response<WriteResponse> responsedelete = null;

		for(int i = 1; i <= N /*3*/; i++) {	
				
			this.wsName= this.wsName.substring(0,10)+i;
				
					try {
						
						uddiLookup();
						createStub();
						
						try {
									port.activateUser(email);
								} catch (InvalidEmailFault_Exception e) {
									//IGNORE
								} catch (EmailAlreadyExistsFault_Exception e) {
									//IGNORE user already registred
							}
						
						port.writeAsync(email,newTag,cv.getValue()+value, new AsyncHandler<WriteResponse>() {
							@Override
							public void handleResponse(Response<WriteResponse> response) {
								responses.add(response);  
								}
							});
					} catch (PointsClientException e) {
						//IGNORE servers that are down, to avoid error in the activateUser skip to the next server
						continue; 
					}
				}
		
		while(received <  this.Q /*2*/ ) {
			for(Response<WriteResponse> response : responses) {
				if(response.isDone()) {
					received++;
					responsedelete = response;	
					break;
				}
			}
			responses.remove(responsedelete);	
		}
	}
	
	/* * readClient - Auxilary function for the implementation of the QC
	 * 
	 *   input : email - the user hash key to get points 
	 * 	 
	 *   output : CreditView class with the value with the maximun tag available in the servers and replication servers. 
	 *     
	 *   description :  -This function helps the client class to implement the QC fault tolerance system.
	 *   			    -It calls N times the available servers and get all the pair <value,tag> available in each of the servers.
	 *    				-It waits for the response of Q servers ( QC majority) and with each response checks if there is a need to refresh the value to return.
	 * */
	

	public synchronized CreditView readClient(String userEmail){
	
		int received = 0;	
		String uddiUrl = this.getuddiURL();
		List<Response<ReadResponse>> responses = new ArrayList<Response<ReadResponse>>();
		Response<ReadResponse> responsedelete = null;
			
		for(int i = 1; i <= N /*3*/ ; i++) {
				
			this.wsName= this.wsName.substring(0,10)+i;
			
				try {
					uddiLookup();
					createStub();
					try {
						port.activateUser(userEmail);
					} catch (EmailAlreadyExistsFault_Exception e) {
						//IGNORE user already registred
					} catch (InvalidEmailFault_Exception e) {
					
					}
				
					port.readAsync(userEmail, new AsyncHandler<ReadResponse>() {
						@Override
						public void handleResponse(Response<ReadResponse> response) {
							responses.add(response);  
							}
						});
	
				} catch (PointsClientException e) {
					//IGNORE servers that are down, to avoid error in the activateUser skip to the next server
					continue;
				}
			}	
	
	 		//Check if there is consensus in the quorum
		
		while (received < this.Q /*2*/ ) {
	 			System.out.println("Responses:"+responses.size());
					for(Response<ReadResponse> response : responses) {
						if(response.isDone()){
							received++;						
						try {
							if(response.get().getReturn().getTag() > cv.getTag()){
								cv.setTag(response.get().getReturn().getTag());
								cv.setValue(response.get().getReturn().getValue());
								}
							} catch (InterruptedException|ExecutionException e2) {
								// IGNORE
							}
						responsedelete = response;	
						break;
						}
					}
					responses.remove(responsedelete);
					if(responses.isEmpty())
						break;
					}	

 		return cv;
	} 
	
	public int pointsBalance(String userEmail) throws InvalidEmailFault_Exception {
		return readClient(userEmail).getValue();

	}
	
	public void addPoints(String userEmail, int pointsToAdd)
			throws InvalidEmailFault_Exception{
			writeClient(userEmail,pointsToAdd);
	}

	public void spendPoints(String userEmail, int pointsToSpend)
			throws InvalidEmailFault_Exception{
			int pointsConversion = pointsToSpend * -1;
			writeClient(userEmail,pointsConversion);
	}
		
	// control operations -----------------------------------------------------

	public String ctrlPing(String inputMessage) {
		return port.ctrlPing(inputMessage);
	}

	public void ctrlClear() {
		
		for(int i = 1; i <= N /*3*/ ; i++) {
			
			this.wsName= this.wsName.substring(0,10)+i;
				try {
					uddiLookup();
					createStub();
					
					port.ctrlClear();
	
				} catch (PointsClientException e) {
					continue;
				}
			}	
	}

	public void ctrlInit(int startPoints) throws BadInitFault_Exception {
		for(int i = 1; i <= N /*3*/ ; i++) {
			
			this.wsName= this.wsName.substring(0,10)+i;
				try {
					uddiLookup();
					createStub();
					
					port.ctrlInit(startPoints);
	
				} catch (PointsClientException e) {
					continue;
				}
			}	
	}
}
