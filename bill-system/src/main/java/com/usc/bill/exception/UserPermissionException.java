package com.usc.bill.exception;

/**
 * To handle anomalous or exceptional conditions related to accessibility or privilege when requesting on behalf of user
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */
public class UserPermissionException extends Exception{

	/**
	 * constructor
	 * @param message states why this occurrence is exceptional 
	 */ 
	public UserPermissionException(String message){
		super(message); // passing requested message to the parent constructor 
	}

}

