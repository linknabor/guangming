package com.yumu.hexie.service.exception;

public class InteractionException extends RuntimeException {

	private static final long serialVersionUID = -881925016625984044L;

	private String message;
	
	public InteractionException() {

	}

	public InteractionException(String message) {

		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	
}
