package com.usc.bill.model;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * To handle transaction data made by/against the student’s bill 
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */ 
@Entity 
public class TransactionHistory {
	
	/**
	 * The unique identifier of the transaction
	 */	 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;

	/**
	 * the user associated to transaction whether payment or charge
	 * OneToOne Annotation is required as every transaction has only one user-related information. 
	 * EAGER value will fetch the user along the way with transaction history
	 * it is not updatable or persisted, since it is already handled by the owner of User entity
	 * @see com.usc.bill.model.User
	 */
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id", updatable=false)
	private User user;

	
	/**
	 * type of transaction. Either payment or charge
	 * @see com.usc.bill.model.Type
	 */	 
	@Enumerated(EnumType.STRING) 
	@Column(nullable = false) 
	private Type type;
	
	/**
	 * The amount of the transaction
	 */
	@Column(nullable = false) 
	private BigDecimal amount;

	/**
	 * Chronologically, when the transaction was made
	 */
	@Temporal(TemporalType.DATE) 
	@Column(nullable = false) 
	private Date date;

	/**
	 * Ancillary note comment about the transaction
	 */	 
	@Column(nullable = false) 
	private String note;

	/**
	 * @return the id
	 */
	public long getId(){
		return id;
	}

	/**
	 * @return the user
	 */
	public User getUser(){
		return user;
	}
	
	/**
	 * @return the type
	 */
	public Type getType(){
		return type;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount(){
		return amount;
	}

	/**
	 * @return the paymentDate
	 */
	public Date getDate(){
		return date;
	}

	/**
	 * @return the note
	 */
	public String getNote(){
		return note;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user){
		this.user = user;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type){
		this.type = type;
	}

	
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount){
		this.amount = amount;
	}

	/**
	 * @param date the paymentDate to set
	 */
	public void setDate(Date date){
		this.date = date;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note){
		this.note = note;
	}
	
}

