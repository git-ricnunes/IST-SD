package com.forkexec.pts.ws;

import javax.jws.WebService;

import com.forkexec.pts.domain.Credit;
import com.forkexec.pts.domain.Points;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.pts.ws.PointsPortType", wsdlLocation = "PointsService.wsdl", name = "PointsWebService", portName = "PointsPort", targetNamespace = "http://ws.pts.forkexec.com/", serviceName = "PointsService")
public class PointsPortImpl {

    /**
     * The Endpoint manager controls the Web Service instance during its whole
     * lifecycle.
     */
	
    private final PointsEndpointManager endpointManager;

    /** Constructor receives a reference to the endpoint manager. */
    public PointsPortImpl(final PointsEndpointManager endpointManager) {
	this.endpointManager = endpointManager;
    }

    // Main operations -------------------------------------------------------

	public void activateUser(final String userEmail) throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception   {
    	
    	try{
    	Points.getInstance().createUser(userEmail); 
    	
    	} catch (InvalidEmailFault_Exception e) {
    		throwInvalidEmail("Email not found: " + userEmail);
    	} catch (EmailAlreadyExistsFault_Exception e) {
    		throwEmailExists("Email already exists: " + userEmail);
    	}
    }

    public int pointsBalance(final String userEmail) throws InvalidEmailFault_Exception {
    	int userCredit = 0;
		
      return userCredit;
    }
    
	public CreditView read(String userEmail) {
		Credit userCredit = null;
		try {
			userCredit = Points.getInstance().getCredit(userEmail);
		} catch (InvalidEmailFault_Exception e) {
			//ignore
		}
		return buildCreditView(userCredit);
	}

	public CreditView write(String userEmail, int tag, int points) {
		
		int val = 0;
		try {
		val =	Points.getInstance().addCredit(userEmail,tag,points);
		} catch (InvalidEmailFault_Exception e) {
		
		}
	
		return null;
	}
	
	/** Helper to convert a domain object to a view. */
	 private CreditView buildCreditView (Credit credit) {

		 CreditView creditView = new CreditView();
			
		 creditView.setTag(credit.getTag());
		 creditView.setValue(credit.getVal());

		 return creditView;
	 }

	

    // Control operations ----------------------------------------------------
    /** Diagnostic operation to check if service is running. */

    public String ctrlPing(String inputMessage) {
	// If no input is received, return a default name.
	if (inputMessage == null || inputMessage.trim().length() == 0)
	    inputMessage = "friend";

	// If the park does not have a name, return a default.
	String wsName = endpointManager.getWsName();
	if (wsName == null || wsName.trim().length() == 0)
	    wsName = "Park";

	// Build a string with a message to return.
	final StringBuilder builder = new StringBuilder();
	builder.append("Hello ").append(inputMessage);
	builder.append(" from ").append(wsName);
	return builder.toString();
    }

	
	public void ctrlInit(int startPoints) throws BadInitFault_Exception {

		if(startPoints<=0)
			throwBadInit("Invalid points to init");
		
		Points.getInstance().init(startPoints);		
	}
	
    /** Return all variables to default values. */
    
    public void ctrlClear() {
        Points.getInstance().clear();    
    }

    // Exception helpers -----------------------------------------------------

    /** Helper to throw a new BadInit exception. */
    private void throwBadInit(final String message) throws BadInitFault_Exception {
        final BadInitFault faultInfo = new BadInitFault();
        faultInfo.message = message;
        throw new BadInitFault_Exception(message, faultInfo);
    }

	private void throwInvalidEmail(final String message) throws InvalidEmailFault_Exception {
		InvalidEmailFault faultInfo = new InvalidEmailFault();
		faultInfo.setMessage(message);
		throw new InvalidEmailFault_Exception(message, faultInfo);
	}
	
	private void throwEmailExists(final String message) throws EmailAlreadyExistsFault_Exception {
		EmailAlreadyExistsFault faultInfo = new EmailAlreadyExistsFault();
		faultInfo.setMessage(message);
		throw new EmailAlreadyExistsFault_Exception(message, faultInfo);
	}

}
