package com.usc.bill.dto;

/**
 * A term is an object with two parts, a semester and a year, e.g. Spring-2017, which is used to indicate
 * a point in time for items such as a bill.
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */ 
public class Term{
	/**
	 * The year of a Term is the Gregorian calendar year for the term, e.g. 2017.
	 */
	private int year;

	/**
	 * The semester of a Term is a portion of a school year, i.e. [SPRING, SUMMER, FALL]
	 */
	private String semester;


	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the semester
	 */
	public String getSemester() {
		return semester;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @param semester the semester to set
	 */
	public void setSemester(String semester) {
		this.semester = semester;
	}
	
}

