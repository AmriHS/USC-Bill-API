/**
 * BILLIntfImpl
 * Implementation for the BILLIntf Interface.
 * Last Modified: ..........
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the University of Minnesota nor the names of its 
 *   contributors may be used to endorse or promote products derived from this 
 *   software without specific prior written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.usc.bill.api;


import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.usc.bill.dto.Bill;
import com.usc.bill.dto.StudentRecord;
import com.usc.bill.exception.UserNotFoundException;
import com.usc.bill.service.BillService;
import com.usc.bill.service.BillServiceImpl;
import com.usc.bill.service.StudentManagementService;
import com.usc.bill.service.StudentManagementServiceImpl;
import com.usc.bill.service.UserManagmentSerivceImpl;
import com.usc.bill.service.UserManagmentService;
import com.usc.bill.utility.InputValidator;

/**
 * This defines the interface to the BILL back-end 
 * processing system.
 * 
 * NOTE:  When you implement this interface, it is good practice to create
 * your own exceptions for the project.  We expect this to be done for this 
 * project.
 * 
 */


/**
 * Your implementation of this interface must be named BILL
 */
public class BILLIntfImpl implements BILLIntf{
	 private static ApplicationContext ctx = null;
	
	 @Autowired
	private UserManagmentService userManagmentService;
	
	 @Autowired
	private StudentManagementService studentManagementService;
	
	 @Autowired
	private BillService billService;
	
	public BILLIntfImpl(){
		if (ctx == null) {
	          ctx = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
		}
		userManagmentService = (UserManagmentService) ctx.getBean(UserManagmentSerivceImpl.class);
		studentManagementService = (StudentManagementService) ctx.getBean(StudentManagementServiceImpl.class);
		billService = (BillService) ctx.getBean(BillServiceImpl.class);
	}
	
    /**
     * Loads the list of system usernames and permissions.
     * @param usersFile the filename of the users file.
     * @throws Exception for I/O errors.  SEE NOTE IN CLASS HEADER.
     */
    public void loadUsers(String usersFile) throws Exception{
        userManagmentService.loadUsers(usersFile);
        
    }

    /**
     * Loads the list of system transcripts.
     * @param recordsFile the filename of the transcripts file.
     * @throws Exception for I/O errors.  SEE NOTE IN CLASS HEADER.
     */
    public void loadRecords(String recordsFile) throws Exception{
    	studentManagementService.loadStudentProfiles(recordsFile);
    	billService.loadTransactions(recordsFile);
    }

    /**
     * Sets the user id of the user currently using the system.
     * @param userId  the id of the user to log in.
     * @throws Exception  if the user id is invalid.  SEE NOTE IN CLASS HEADER.
     */
    public void logIn(String userId) throws Exception{
    	InputValidator.validateUserId(userId);
    	userManagmentService.login(userId);
    }

    /**
     * Closes the current session, logs the user out, and clears any session data.
     * @throws Exception  if the user id is invalid.  SEE NOTE IN CLASS HEADER.
     */
    public void logOut() throws Exception{
    	userManagmentService.logout();
    	studentManagementService.setTempStudentPofile(null);
    }
    
    /**
     * Gets the user id of the user currently using the system.
     * @return  the user id of the user currently using the system.
     */
    public String getUser(){
    	try{
    		return userManagmentService.getLoggedInUserId();
    	}
    	catch (UserNotFoundException ex){
    		return null;
    	}
    }
    
    /**
     * Gets a list of the userIds of the students that an admin can view.
     * @return a list containing the userId of for each student in the
     *      college belonging to the current user 
     * @throws Exception is the current user is not an admin.
     */
    public List<String> getStudentIDs() throws Exception{
    	return userManagmentService.getUserIdsUnderAdminPrivilidge();
    }
    
    /**
     * Gets the raw student record data for a given userId.
     * @param userId  the identifier of the student.
     * @return the student record data.
     * @throws Exception if the form data could not be retrieved. SEE NOTE IN 
     *      CLASS HEADER.
     */
    public StudentRecord getRecord(String userId) 
            throws Exception{
    	InputValidator.validateUserId(userId);
    	return studentManagementService.getStudentRecord(userId);
    }
    
    /**
     * Saves a new set of student data to the records data.  
     * @param userId the student ID to overwrite.
     * @param transcript  the new student record
     * @param permanent  a status flag indicating whether (if false) to make a 
     * temporary edit to the in-memory structure or (if true) a permanent edit.
     * @throws Exception if the transcript data could not be saved or failed
     * a validity check.  SEE NOTE IN CLASS HEADER.
     */
    public void editRecord(String userId, StudentRecord transcript, Boolean permanent) 
            throws Exception{
    	InputValidator.validateUserId(userId);
    	InputValidator.validateStudentRecord(transcript);
    	studentManagementService.updateStudentProfle(userId, transcript, permanent);
    }

    /**
     * Generates current bill.
     * @param userId the student to generate the bill for.
     * @return the student's bill in a data class matching the I/O file.
     * @throws Exception  if the bill could not be generated.  
     * SEE NOTE IN CLASS HEADER.
     */
    public Bill generateBill(String userId) 
            throws Exception{
    	InputValidator.validateUserId(userId);
    	return billService.getCurrentBill(userId);
    }

    /**
     * Generates a list of transactions for a chosen period.
     * @param userId the student to generate the list for.
     * @param startMonth the month of the start date.
     * @param startDay the day of the start date.
     * @param startYear the year of the start date.
     * @param endMonth the month of the end date.
     * @param endDay the day of the end date.
     * @param endYear the year of the end date.
     * @return the student's bill in a data class matching the I/O file.
     * @throws Exception  if the bill could not be generated.  
     * SEE NOTE IN CLASS HEADER.
     */
    public Bill viewCharges(String userId, int startMonth, int startDay, int startYear, 
                            int endMonth, int endDay, int endYear) throws Exception{
    	InputValidator.validateUserId(userId);
    	InputValidator.validateDate(startMonth, startDay, startYear);
    	InputValidator.validateDate(endMonth, endDay, endYear);
    	
    	Date startDate = new GregorianCalendar(startYear, startMonth-1, startDay).getTime();
    	Date endDate = new GregorianCalendar(endYear, endMonth-1, endDay).getTime();
    	
    	InputValidator.validateDateRange(startDate, endDate);
    	
    	return billService.getAllCharges(userId, startDate, endDate);
    }

    /**
     * Makes a payment for the student
     * @param userId  the student to make a payment for.
     * @param amount  amount to apply to the balance.
     * @param note  a string indicating the reason for the payment
     * @throws Exception if the payment fails a validity check 
     * or fails to save to file.  
     * SEE NOTE IN CLASS HEADER.
     */
    public void applyPayment(String userId, BigDecimal amount, String note) 
            throws Exception{
    	InputValidator.validateUserId(userId);
    	if (amount.doubleValue()>0 && note != null)
    		billService.addPayment(userId, amount, note);
    	else {
    		if (amount.doubleValue()<=0)
        		throw new IllegalArgumentException("amount is not a valid input");
    		else 
         		throw new IllegalArgumentException("note is not a valid input");
    	}
    }

}
