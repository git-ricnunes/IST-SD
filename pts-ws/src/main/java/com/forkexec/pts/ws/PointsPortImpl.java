package com.forkexec.pts.ws;

import javax.jws.WebService;

import com.forkexec.pts.domain.Credit;
import com.forkexec.pts.domain.Points;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.pts.ws.PointsPortType", wsdlLocation = "PointsService.wsdl", name = "PointsWebService", portName = "PointsPort", targetNamespace = "http://ws.pts.forkexec.com/", serviceName = "PointsService")
public class PointsPortImpl implements PointsPortType {

    /**
     * The Endpoint manager controls the Web Service instance during its whole
     * lifecycle.
     */
    private final PointsEndpointManager endpointManager;

    int version = 0;
    /** Constructor receives a reference to the endpoint manager. */
    public PointsPortImpl(final PointsEndpointManager endpointManager) {
	this.endpointManager = endpointManager;
    }

    // Main operations -------------------------------------------------------

    @Override
	public void activateUser(final String userEmail) throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception   {
    	
    	try{
    	Points.getInstance().createUser(userEmail); 
    	
    	} catch (InvalidEmailFault_Exception e) {
    		throwInvalidEmail("Email not found: " + userEmail);
    	} catch (EmailAlreadyExistsFault_Exception e) {
    		throwEmailExists("Email already exists: " + userEmail);
    	}
    }

    @Override
    public CreditView pointsBalance(final String userEmail) throws InvalidEmailFault_Exception {
    	Credit userCredit = null;
    	
    	System.out.println("POINTSBALANCE from: "+endpointManager.getWsName());
		try {
			userCredit = Points.getInstance().getCredit(userEmail);
		} catch (InvalidEmailFault_Exception e) {
			throwInvalidEmail("Email not found: " + userEmail);
		}
		
      return buildCreditView(userCredit);
    }

    //TODO
    @Override
    public int addPoints(final String userEmail, final int pointsToAdd)
	    throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
    	
    	Credit userCredit=null;
    	
    	if(pointsToAdd <= 0 )
    		throwInvalidPoints("Number of points are invalid: " + pointsToAdd);
    	
    	try {
    		userCredit=Points.getInstance().addCredit(userEmail,pointsToAdd);
		} catch (InvalidEmailFault_Exception e) {
			throwInvalidEmail("Email not found: " + userEmail);
		}
    	
        return 1;
    }

    @Override
    public CreditView spendPoints(final String userEmail, final int pointsToSpend)
	    throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
    	
    	Credit userCredit=null;
    	
    	if(pointsToSpend <= 0 )
    		throwInvalidPoints("Number of points are invalid: " + pointsToSpend);
    	
    	try {
    		userCredit = Points.getInstance().subtractCredit(userEmail,pointsToSpend);
    		
		} catch (InvalidEmailFault_Exception e) {
			throwInvalidEmail("Email not found: " + userEmail);
		} catch (NotEnoughBalanceFault_Exception e){
			throwNotEnoughBalance("Not enough credits: " + userEmail);
		}
    	
        return buildCreditView(userCredit);
			
    }
    
 // View helpers ----------------------------------------------------------

 	 /** Helper to convert a domain object to a view. */
 	 private CreditView buildCreditView (Credit credit) {
 		 
 		 CreditView creditView = new CreditView();
 		 
 		 creditView.setTag(credit.getTag());
 		 creditView.setValue(credit.getValue());
 		 
 		 return creditView;
 	 }

    // Control operations ----------------------------------------------------
    // TODO
    /** Diagnostic operation to check if service is running. */
    @Override
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

	@Override
	public void ctrlInit(int startPoints) throws BadInitFault_Exception {

		if(startPoints<=0)
			throwBadInit("Invalid points to init");
		
		Points.getInstance().init(startPoints);		
	}
	
    /** Return all variables to default values. */
    @Override
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
	
	private void throwInvalidPoints(final String message) throws InvalidPointsFault_Exception {
		InvalidPointsFault faultInfo = new InvalidPointsFault();
		faultInfo.setMessage(message);
		throw new InvalidPointsFault_Exception(message, faultInfo);
	}
	
	
	private void throwNotEnoughBalance(final String message) throws NotEnoughBalanceFault_Exception {
		NotEnoughBalanceFault faultInfo = new NotEnoughBalanceFault();
		faultInfo.setMessage(message);
		throw new NotEnoughBalanceFault_Exception(message, faultInfo);
	}

}
