package com.linkage.lib.exception;

import java.io.IOException;

public class NetworkNotAvaiableException extends IOException {
	
	private static final long serialVersionUID = 1L;
	private String mRequestUrl;
	
	public NetworkNotAvaiableException(String msg, String url) {
		super(msg);
		this.mRequestUrl = url;
	}
	
	@Override
	public String toString() {
		return getMessage() + ", request url is " + this.mRequestUrl;
	}
}
