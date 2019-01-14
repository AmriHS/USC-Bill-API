package com.usc.bill.dto;

/**
 * A course is an academic class associated with a Student, the number of courses and attributes 
 * such as the number of credit hours will be used to calculate the students bill.
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */ 
public class Course{
	
	/**
	 * The id of a course is the unique identifier of the course, e.g. "CSCE 740".
	 */ 
	private String id;

	/**
	 * The name is the short string description of a course, e.g. "Sofware Processes".
	 */ 
	private String name;

	/**
	 * The numCredits is the number of credit hours of the course, e.g. 3. A student's bill is calculated in part
	 * by the number of credit hours enrolled by the student.
	 */
	private int numCredits;

	/**
	 * online is a Boolean flag used to indicate if the course is taken physically on campus, or remotely via the web.
	 * Additional charges are typically incurred for online courses.
	 */ 
	private boolean online;


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param numCredits the numCredits to set
	 */
	public void setNumCredits(int numCredits) {
		this.numCredits = numCredits;
	}

	/**
	 * @param online the online to set
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the numCredits
	 */
	public int getNumCredits() {
		return numCredits;
	}

	/**
	 * @return the online
	 */
	public boolean getOnline() {
		return online;
	}
}

