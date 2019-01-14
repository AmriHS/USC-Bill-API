package com.usc.bill.model;

/**
 * A type is an enumerated class containing values of [CHARGE, PAYMENT] and is used to indicate if
 * a transaction should decrease the amount owed by a student towards a bill (payment) or increase the amount
 * owed (charge)
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */ 
public enum Type {
	CHARGE, PAYMENT;
}
