package com.linkage.lib.exception;

public class SchoolException extends Exception{
	
	private static final long serialVersionUID = -2868391357582745508L;
	private String mDesc;
	
	public SchoolException(String desc) {
		super(desc);
		mDesc = desc;
	}
	
	public String getDesc() {
		return mDesc;
	}
}
