package com.forkexec.pts.domain.exception;

public class RestaurantNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public RestaurantNotFoundException() {
	}

	public RestaurantNotFoundException(String message) {
		super(message);
	}
}
