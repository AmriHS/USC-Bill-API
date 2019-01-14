package com.usc.bill.exception;

/**
 * to handle anomalous or exceptional conditions related to existence of bill when requesting
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */
public class NoBillExistedException extends Exception{

	/**
	 * constructor
	 * @param message states why this occurrence is exceptional 
	 */
	public NoBillExistedException(String message){
		super(message); // passing requested message to the parent constructor 
	}
}

