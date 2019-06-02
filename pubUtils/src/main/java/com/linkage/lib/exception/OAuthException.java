package com.linkage.lib.exception;

public class OAuthException extends Exception {
	
	private static final long serialVersionUID = -584950457043541113L;
	
	private String errorInfo;
	
	public OAuthException(String info) {
		super("oauth exception");
		this.errorInfo = info;
	}
	
	public String getErrorInfo() {
		return errorInfo;
	}
}
