package com.github.xabgesagtx.fatlining.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * Exception that signals that someone manipulated a form
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ManipulatedFormException extends RuntimeException {
	
	private static final long serialVersionUID = -374332220239965397L;

	public ManipulatedFormException(String message) {
		super(message);
	}

}
