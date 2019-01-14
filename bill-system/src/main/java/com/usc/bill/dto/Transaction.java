package com.usc.bill.dto;

import java.math.BigDecimal;

/**
 * A transaction is a class that used to inidcate if a charge has been incurred towards a student's bill
 * or if a payment has been made towards a student's bill. As such, a transaction has an attribute of a type [PAYMENT, CHARGE]
 * as well as an amount and a date when the transaction occurred.
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */ 

public class Transaction{
	/**
	 * A type of either [PAYMENT, CHARGE] to indicate if the transaction amount should increase the amount owed (charge)
	 * or decrease the amount owed (payment) by the student.
	 */
	
	public String type;

	/**
	 * The dollar amount of the charge or payment.
	 */
	private BigDecimal amount;

	/**
	 * The day, month, and year of the transaction.
	 */
	private TransactionDate transactionDate;

	/**
	 * Any miscellaneous comment to be displayed with the transaction.
	 */
	private String note;

	/**
	 * @return the type 
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @return the transactionDate
	 */
	public TransactionDate getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(TransactionDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
}

