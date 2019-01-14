package com.usc.bill.service;

import java.net.URISyntaxException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.usc.bill.exception.UserNotFoundException;
import com.usc.bill.exception.UserPermissionException;
import com.usc.bill.model.StudentProfile;
import com.usc.bill.model.User;

/**
 * A common interface to handle business operations that concerns user-related data 
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
*/ 
@Service
public  interface UserManagmentService 
{
	/**
	 * to load users contained in file that is formated as a JSON into the system datasource
	 * @param filename name of file where the records of user existed
	 * @throws URISyntaxException if the given filename has improper format  
	 * @throws NullPointerException if the file associated with a given filename doesn't exist
	 * @throws ParseException if the file associated with a given filename couldn't be parsed into JSONArray
	 */ 
	public void loadUsers(String filename) throws NullPointerException, ParseException, URISyntaxException;

	/**
	 * user to login with a given an id of user
	 * @param userId an id of user
	 * @throws UserNotFoundException if the user associated with a given id is not existed in the system
	 */
	public void login(String userId) throws UserNotFoundException;

	/**
	 * current logged-in user is logged-out
	 */
	public void logout();
	
	/**
	 * to get the logged-in user's id
	 * @return logged-in user's id
	 * @throws UserNotFoundException if there is no user is logged-in with a the time of requesting
	 */
	public String getLoggedInUserId() throws UserNotFoundException;

	/**
	 * to get all users' id fall under the privilege of the current logged-in user
	 * @return list of users fall under the privilege of the current logged-in user 
     * @throws UserPermissionException in case the logged in user has no administration privilege to access other users' id
	 * @throws UserNotFoundException in case there is no user is logged in.
	 */
	public List<String> getUserIdsUnderAdminPrivilidge() throws UserNotFoundException, UserPermissionException;

	/**
	 * check whether the current logged-in user has a privilege to access student profile
	 * @param studentProfile where to check whether logged-in user has the access to  
	 * @return true if is accessible by logged-in user, otherwise, false
	 */
	public boolean checkAccess(StudentProfile studentProfile);
	
	/**
	 * get user by the given userId
	 * @param userId an id of user 
	 * @return user associated with a given user id
	 */
	public User getByUserId(String userId);
}

