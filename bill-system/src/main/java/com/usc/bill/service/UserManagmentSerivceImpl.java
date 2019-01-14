package com.usc.bill.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usc.bill.exception.UserNotFoundException;
import com.usc.bill.exception.UserPermissionException;
import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.College;
import com.usc.bill.model.Role;
import com.usc.bill.model.StudentProfile;
import com.usc.bill.model.User;
import com.usc.bill.repository.UserRepository;
import com.usc.bill.utility.DataMapper;
import com.usc.bill.utility.InputValidator;


/**
 * To handle business operations that concerns generating charges and applying payment for student
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
*/ 
@Service
public class UserManagmentSerivceImpl implements UserManagmentService{
	
	/**
	 * the an instance of User who is logged-in to be saved in memory as a session
 	 */	
	private User loggedInUser = null;

	/**
	 * instance of UserRepository in order to assist in user-related data access operations
	 */
	@Autowired
	public UserRepository userRepository;

	/**
	 * instance of StudentManagementService in order to assist in Student-related bussiness data operations
	 */
	@Autowired
	public StudentManagementService studentManagementService;
	
	/**
	 * to load users contained in file that is formated as a JSON into the system datasource
	 * @param filename name of file where the records of user existed
	 * @throws URISyntaxException if the given filename has improper format  
	 * @throws NullPointerException if the file associated with a given filename doesn't exist
	 * @throws ParseException if the file associated with a given filename couldn't be parsed into JSONArray
	 */ 
	public void loadUsers(String filename) throws NullPointerException, ParseException, URISyntaxException{
		try{
         	JSONParser parser = new JSONParser(); // declate an object of JSON parser
        	File file = new File(this.getClass().getResource("/"+filename+".txt").toURI()); // convert the given filename to class path URL
        	Object obj = parser.parse(new FileReader(file)); // read the data
        	JSONArray jsonArray = (JSONArray) obj; // declare an object of JSON array, assuming the data is always in Array format
        	List<User> users = DataMapper.getAllUsers(jsonArray); // get list of users by extracting them from json array through DataMapper       	
			// iterate over list of users
        	for(User u: users){
        		InputValidator.validateUser(u); // validate user as a valid input through InputValidator
	    		if (userRepository.findOneById(u.getId()) != null) // if the user already exist in the system
	    			throw new IllegalArgumentException("User with ID "+u.getId()+" already exist in the system");
        	}
        	// iterate over list of users
        	for(User u: users){
        		userRepository.save(u); // persist users through repository
        	}
        }	
		catch(NullPointerException ex){ // if the file doesn't exist 
        	throw new NullPointerException("File "+filename+" doesn't exist in the system resources"); 
		}
        catch(ParseException ex){ // if the file couldn't be parsed into JSONArray
        	throw new ParseException(0, "File "+filename+" can't be parsed");
        }
        catch (IOException e){ // otherwise handle any unexpected I/O exceptional occurance
			e.printStackTrace();
        }
	}

	/**
	 * user to login with a given an id of user
	 * @param userId an id of user
	 * @throws UserNotFoundException if the user associated with a given id is not existed in the system
	 */
	public void login(String userId) throws UserNotFoundException{
		User user = userRepository.findOneById(userId);	// retrieve the user through repository	
		if (user== null)
			throw new UserNotFoundException("User "+userId+" doesn't exist in the system");
		loggedInUser = user;
	}

	/**
	 * current logged-in user is logged-out
	 */
	public void logout() {
		loggedInUser = null;
	}

	/**
	 * to get the logged-in user's id
	 * @return logged-in user's id
	 * @throws UserNotFoundException if there is no user is logged-in with a the time of requesting
	 */
	public String getLoggedInUserId() throws UserNotFoundException{
		if (loggedInUser==null)
			throw new UserNotFoundException("No user currently is logged in");
		return loggedInUser.getId();
	}

	/**
	 * to get all users' id fall under the privilege of the current logged-in user
	 * @return list of users fall under the privilege of the current logged-in user 
     * @throws UserPermissionException in case the logged in user has no administration privilege to access other users' id
	 * @throws UserNotFoundException in case there is no user is logged in.
	 */
	public List<String> getUserIdsUnderAdminPrivilidge() throws UserPermissionException, UserNotFoundException{
		// check whether there is a user logged in
		if (loggedInUser==null)
			throw new UserNotFoundException("No user currently is logged in");

		if (!loggedInUser.getRole().equals(Role.ADMIN))
			throw new UserPermissionException("Current logged in user has no administration role");
		List<String> userIds = new ArrayList<>(); 
		if (loggedInUser.getCollege().equals(College.GRADUATE_SCHOOL)){ 
			ClassStatus [] classStatusArray = {ClassStatus.MASTERS, ClassStatus.PHD};
			List<StudentProfile> studentProfiles=studentManagementService.getByClassStatus(Arrays.asList(classStatusArray));
			for (StudentProfile s : studentProfiles)
				userIds.add(s.getUser().getId());
		}
		else{
			List<StudentProfile> studentProfiles = studentManagementService.getByCollege(loggedInUser.getCollege()); 
			for (StudentProfile s : studentProfiles) 
				userIds.add(s.getUser().getId());
		}
		return userIds;
	}

	/**
	 * check whether the current logged-in user has a privilege to access student profile
	 * @param studentProfile where to check whether logged-in user has the access to  
	 * @return true if is accessible by logged-in user, otherwise, false
	 */
	public boolean checkAccess(StudentProfile studentProfile) {
		// check whether there is a user logged in
		if (loggedInUser==null)
			return false;
		if (!loggedInUser.getId().equals(studentProfile.getUser().getId())) 
				return false;
		if (loggedInUser.getRole().equals(Role.ADMIN)){ 
			if (loggedInUser.getCollege().equals(College.GRADUATE_SCHOOL) 
					&& (studentProfile.getClassStatus().equals(ClassStatus.MASTERS) 
					|| studentProfile.getClassStatus().equals(ClassStatus.PHD)))
				return true;
			else if (loggedInUser.getCollege().equals(studentProfile.getUser().getCollege())) 
				return true;
			else  
				return false;
		}
		return true;
	}
	
	/**
	 * get user by the given userId
	 * @param userId an id of user 
	 * @return user associated with a given user id
	 */
	public User getByUserId(String userId){
		return userRepository.findOneById(userId); // retrieve the user through repository
	}
	
}