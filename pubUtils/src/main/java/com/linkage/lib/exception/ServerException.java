package com.linkage.lib.exception;

public class ServerException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private int mErrorCode = 500;
	private String mErrorInfo;
	private int mResponseCode = 200;
	
	public ServerException(String errorInfo) {
		mResponseCode = 200;
		mErrorInfo = errorInfo;
	}
	
	public ServerException(int responseCode, String errorInfo) {
		this.mResponseCode = responseCode;
		this.mErrorInfo = errorInfo;
	}
	
	public int getErrorCode() {
		return mErrorCode;
	}
	
	public int getResponseCode() {
		return mResponseCode;
	}
	
	public String getErrorInfo() {
		return mErrorInfo;
	}
	
	@Override
	public String toString() {
		return "Response code is:" + mResponseCode + "\n" + mErrorInfo + "\n" + super.toString();
	}
}
