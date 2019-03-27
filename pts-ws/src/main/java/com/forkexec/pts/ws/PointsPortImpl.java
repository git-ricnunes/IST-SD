package com.forkexec.pts.ws;

import javax.jws.WebService;

import com.forkexec.hub.domain.exception.EmailAlreadyExistsException;
import com.forkexec.hub.domain.exception.InsufficientCreditsException;
import com.forkexec.hub.domain.exception.InvalidEmailException;
import com.forkexec.hub.domain.exception.UserNotFoundException;
import com.forkexec.pts.domain.UsersManager;

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

    /** Constructor receives a reference to the endpoint manager. */
    public PointsPortImpl(final PointsEndpointManager endpointManager) {
	this.endpointManager = endpointManager;
    }

    // Main operations -------------------------------------------------------

    @Override
	public void activateUser(final String userEmail) throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
    	try {
    		
			UsersManager.getInstance().RegisterNewUser(userEmail);
			
    	} catch (EmailAlreadyExistsException e) {
			throwEmailExists("Email already exists: " + userEmail);
		} catch (InvalidEmailException e) {
			throwInvalidEmail("Invalid email: " + userEmail);
		} 
    }

    @Override
    public int pointsBalance(final String userEmail) throws InvalidEmailFault_Exception {
    	int userCredit = 0;
		try {
			userCredit = UsersManager.getInstance().getUser(userEmail).getCredit();
		} catch (UserNotFoundException e) {
			throwInvalidEmail("Email not found: " + userEmail);
		}
		
      return userCredit;
    }

    @Override
    public int addPoints(final String userEmail, final int pointsToAdd)
	    throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
    	
    	int userCredit =0;
    	
    	if(pointsToAdd <= 0 )
    		throwInvalidPoints("Number of points are invalid: " + pointsToAdd);
    	
    	try {
			UsersManager.getInstance().getUser(userEmail).incrementPoints(pointsToAdd);
			userCredit = UsersManager.getInstance().getUser(userEmail).getCredit();

		} catch (UserNotFoundException e) {
			throwInvalidEmail("Email not found: " + userEmail);
		}
    	
        return userCredit;
    }

    @Override
    public int spendPoints(final String userEmail, final int pointsToSpend)
	    throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
    	int userCredit=0;
    	
    		try {
				UsersManager.getInstance().getUser(userEmail).decrementPoints(pointsToSpend);
				userCredit = UsersManager.getInstance().getUser(userEmail).getCredit();
			} catch (InsufficientCreditsException e) {
				throwNotEnoughBalance("Not enough points to spend.");
				e.printStackTrace();
			} catch (UserNotFoundException e) {
				throwInvalidEmail("Email not found: " + userEmail);
			}
			
        return userCredit;
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
		// TODO Auto-generated method stub
		
	}
	
    /** Return all variables to default values. */
    @Override
    public void ctrlClear() {
        //TODO
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
