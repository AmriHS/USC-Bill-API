package com.usc.bill.exception;

/**
 * To handle anomalous or exceptional conditions related to the existence of user records 
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */
public class UserNotFoundException extends Exception{
	
	/**
	 * constructor
	 * @param message states why this occurrence is exceptional 
	 */
	public UserNotFoundException(String message){
		super(message); // passing requested message to the parent constructor 
	}
}

