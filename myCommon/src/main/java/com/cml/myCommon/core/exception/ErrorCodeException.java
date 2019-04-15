package com.cml.myCommon.core.exception;

public class ErrorCodeException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4143519479094905222L;
	
	private final int errorCode;
	

	public int getErrorCode() {
		return errorCode;
	}

	public ErrorCodeException(int errorCode){
		this.errorCode = errorCode;
	}
	
}
