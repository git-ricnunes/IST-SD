package com.forkexec.hub.domain.exception;

public class InvalidPasswordException extends Exception {

	private static final long serialVersionUID = 3366375831095548860L;

	public InvalidPasswordException() {
	}

	public InvalidPasswordException(String message) {
		super(message);
	}
}
