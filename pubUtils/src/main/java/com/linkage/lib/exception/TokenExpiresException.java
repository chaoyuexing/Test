package com.linkage.lib.exception;

public class TokenExpiresException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TokenExpiresException() {
		super("access token is expires");
	}
}
