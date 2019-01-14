package com.usc.bill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;


/**
 * To model user-related information.
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */
@Entity
public class User{
	
	/**
	 * the unique id of user
	 */
	@Id 
	@Column(nullable = false) 
	private String id;

	/**
	 * role of user in the system  
	 * @see com.usc.bill.model.Role
	 */
	@Enumerated(EnumType.STRING) 
	@Column(nullable = false) 
	private Role role;

	/**
	 * college to which the user is associated
	 * @see com.usc.bill.model.College
	 */
	@Enumerated(EnumType.STRING) 
	@Column(nullable = false) 
	private College college;

	/**
	 * first name of user
	 */
	@Column(nullable = false) 
	private String firstName;

	/**
	 * last name of user
	 */
	@Column(nullable = false) 
	private String lastName;

	/**
	 * @return the id
	 */
	public String getId(){
		return id;
	}

	/**
	 * @return the role
	 */
	public Role getRole(){
		return role;
	}

	/**
	 * @return the college
	 */
	public College getCollege(){
		return college;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName(){
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName(){
		return lastName;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(Role role){
		this.role = role;
	}

	/**
	 * @param college the college to set
	 */
	public void setCollege(College college){
		this.college = college;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
}

