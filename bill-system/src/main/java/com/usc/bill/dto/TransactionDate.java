package com.usc.bill.dto;

/**
 * A transactionDate is the month, day, and year and is associated with a transaction. The combined date
 * must constitute a valid value of the Gregorian calendar, this is to say dates like 11/31/2017 and 02/30/2000
 * are disallowed.
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */ 
public class TransactionDate{
	/**
	 * Month is the month portion of the Transaction date. E.g. if the Transaction date is 10-31-2031 the month is 10.
	 */
	public int month;

	/**
	 * Day is the day portion of the Transaction date. E.g. if the Transaction date is 10-31-2031 the day is 31. 
	 */
	public int day;

	/**
	 * Year is the year portion of the Transaction date. E.g. if the Transaction date is 10-31-2031 the year is 2017
	 */
	public int year;

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
}