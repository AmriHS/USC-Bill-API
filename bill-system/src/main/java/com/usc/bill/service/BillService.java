package com.usc.bill.service;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.usc.bill.dto.Bill;
import com.usc.bill.exception.NoBillExistedException;
import com.usc.bill.exception.NoStudentProfileFoundException;
import com.usc.bill.exception.UserNotFoundException;
import com.usc.bill.exception.UserPermissionException;
import com.usc.bill.model.StudentProfile;
import com.usc.bill.model.TransactionHistory;


/**
 * A common interface to handle a general business operations that concerns generating charges and applying payment for student
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
*/ 
@Service
public  interface BillService {
	/**
	 * to load transactions contained in student record file that is formated as a JSON into the system datasource
	 * @param filename name of file where the record of transaction existed
	 * @throws URISyntaxException if the given filename has improper format  
	 * @throws NullPointerException if the file associated with a given filename doesn't exist
	 * @throws ParseException if the file associated with a given filename couldn't be parsed into JSONArray
	 * @throws NoStudentProfileFoundException if student with user id associated with transaction doesn't exist
	 */
	public void loadTransactions(String filename) throws URISyntaxException, ParseException, NullPointerException, NoStudentProfileFoundException;
	
	/**
	 * to generate charges by the end of semester and calculate final amount in order to be preserved in the system for future record
	 * @param studentProfile a profile where the charge will be calculated
     * @see com.usc.bill.model.StudentProfile
	 */
	public void generateEndTermCharge(StudentProfile studentProfile);	

	/**
	 * to get the charges and payment made in the current semester presented in Bill object
	 * @param userId an id of user corresponding to student profile that 
	 * @return bill consisted of student of profile along wiht charges, payment of current semester
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @throws NoBillExistedException if there is no bill existed for the given user id
	 * @throws UserPermissionException if the logged-in user has no permission accessing given id of user
	 * @throws UserNotFoundException if no user is logged-in
     * @see com.usc.bill.dto.Bill
	 */
	public Bill getCurrentBill(String userId) throws NoStudentProfileFoundException, UserPermissionException, UserNotFoundException, NoBillExistedException;
	
	/**
	 * to get the charges and payment made within the range of dates given presented in Bill object
	 * @param userId the id of user that is associated with transaction
	 * @param startDate a start date where transactions' dates fall after it
	 * @param endDate an end date where transactions' dates fall before it
	 * @return bill consisted of student of profile along wiht charges, payment within the range of dates given
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @throws UserPermissionException if the logged-in user has no permission accessing given id of user
	 * @throws UserNotFoundException in case no user is logged in
	 * @throws NoBillExistedException if there is no bill existed for the given user id
  	 * @see com.usc.bill.dto.Bill
	 */
	public Bill getAllCharges(String userId, Date startDate, Date endDate) throws NoStudentProfileFoundException, UserPermissionException, UserNotFoundException, NoBillExistedException;

	/**
	 * to get current charges with a given userId
	 * @param userId an id of user where the charges will be incurred
	 * @return map of current charges
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 */
	public Map<String, BigDecimal> getCurrentCharges(String userId) throws NoStudentProfileFoundException;
	
	/**
	 * to get all charges and payment associated with a given userId
	 * @param userId an id of user where the transaction made
	 * @return all transaction made that are associated with a given userId
	 * @see com.usc.bill.model.TransactionHistory
	 */
	public List<TransactionHistory> getAllTransactionHistory(String userId);

	/**
	 * to get all charges and payment associated with a given userId and within the range of dates
	 * @param userId the id of user that is associated with transaction
	 * @param startDate a start date where transactions' dates fall after it
	 * @param endDate an end date where transactions' dates fall before it
	 * @return all transaction made that are associated with a given userId and within the range of dates
	 * @see com.usc.bill.model.TransactionHistory
	 */
	public List<TransactionHistory> getAllTransactionHistory(String userId, Date startDate, Date endDate) ;

	/**
	 * to get the total balance from all transaction history by calculating charge against payment
	 * @param allTransactionHistory consists of all payment and charges
	 * @return the balance in respective to the payments made and past charges
	 * @see com.usc.bill.model.TransactionHistory
	 */
	public BigDecimal getBalance(List<TransactionHistory> allTransactionHistory);

	/**
	 * to get the total balance from current charges and all transaction history by calculating charge against payment
	 * @param currentCharges charges of current semester
	 * @param allTransactionHistory consists of all payment and charges
	 * @return the balance in respective to the payments made and past charges
	 * @see com.usc.bill.model.TransactionHistory
	*/
	public BigDecimal getBalance(Map<String, BigDecimal> currentCharges, List<TransactionHistory> allTransactionHistory) ;

	/**
	 * to add a payment against the current student's bill 
	 * @param userId the id of student where payment will be applied
	 * @param amount a mount in dollar currency
	 * @param note a reason for the payment
	 * @throws UserNotFoundException if the user associated with a given id is not existed in the system
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @throws UserPermissionException if the logged-in user has no permission accessing given id of user
	 */	
	public void addPayment(String userId, BigDecimal amount, String note) throws UserNotFoundException, NoStudentProfileFoundException, UserPermissionException;
}

