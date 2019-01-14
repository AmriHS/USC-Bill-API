package com.usc.bill.service;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.usc.bill.dto.StudentRecord;
import com.usc.bill.exception.NoStudentProfileFoundException;
import com.usc.bill.exception.UserNotFoundException;
import com.usc.bill.exception.UserPermissionException;
import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.College;
import com.usc.bill.model.Role;
import com.usc.bill.model.StudentCourse;
import com.usc.bill.model.StudentProfile;
import com.usc.bill.model.TransactionHistory;
import com.usc.bill.model.User;
import com.usc.bill.repository.StudentCourseRepository;
import com.usc.bill.repository.StudentProfileRepository;
import com.usc.bill.utility.DataMapper;
import com.usc.bill.utility.InputValidator;


/**
 * StudentManagementServiceImpl.java
 * Purpose: an implementation that handles business operations that concerns student profile and courses-related data within the scope of the current requirement
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
*/
@Service
public class StudentManagementServiceImpl implements StudentManagementService{
	/**
	 * instance of UserManagmentService in order to assist in user-related bussiness data operations
	 */
	@Autowired
	public UserManagmentService userManagementService;

	/**
	 * instance of BillService in order to assist in charge and payment-related business operations
	 */	
	@Autowired
	public BillService billService;
	
	/**
	 * instance of StudentProfileRepository in order to assist in student profile-related data access operations
	 */
	@Autowired
	public StudentProfileRepository studentProfileRepository;

	/**
	 * instance of StudentCourseRepository in order to assist in student course-related data access operations
	 */
	@Autowired
	public StudentCourseRepository studentCourseRepository;

	/**
	 * an instance of student profile that is in case the update of student profile requested to be not persisted/**
	 * instance of BillService in order to assist in charge and payment-related business operations
	 */	
	private StudentProfile tempStudentProfile;

	/**
	 * to load student profiles from file that is formated as a JSON into the system datasource
	 * @param filename name of file where the records of student existed
	 * @throws URISyntaxException if the given filename has improper format  
	 * @throws NullPointerException if the file associated with a given filename doesn't exist
	 * @throws ParseException if the file associated with a given filename couldn't be parsed into JSONArray
	 * @throws UserNotFoundException if the id associated with student record is existed in the system for admin user
	 */
	@Transactional
	public void loadStudentProfiles(String filename) throws URISyntaxException, NullPointerException, ParseException, UserNotFoundException{
		try{
        	JSONParser parser = new JSONParser(); // declate an object of JSON parser
        	File file = new File(this.getClass().getResource("/"+filename+".txt").toURI()); // convert the given filename to class path URL
        	Object obj = parser.parse(new FileReader(file)); // read the data
        	JSONArray jsonArray = (JSONArray) obj; // declare an object of JSON array, assuming the data is always in Array format
            List<StudentRecord> StudentRecords = DataMapper.getAllStudentRecords(jsonArray);  // get list of student records by extracting them from json array through DataMapper    
        	List<StudentProfile> studentProfiles = new ArrayList<>(); // declare a list of student profile to be mapped from list of student records
			// iterate over list of student records
			for (StudentRecord s:StudentRecords){
        		InputValidator.validateStudentRecord(s); // validate student record as a valid input through InputValidator
        		StudentProfile studentProfile = DataMapper.getStudentProfile(s); // map student record to student profile through DataMapper
        		User user = userManagementService.getByUserId(studentProfile.getUser().getId());   // get the user associated with user id of student profile through repository    		
        		if (user == null)
        			throw new UserNotFoundException("Student with user ID "+s.getId()+" has no user record exist in the system");
        		
        		// if user exist
	        	if (studentProfileRepository.findOneByUser_Id(user.getId()) != null)
	            	throw new IllegalArgumentException ("Student with user ID "+s.getId()+" is already exist in the system");
	        	else if ( user.getRole().equals(Role.ADMIN))
	        		throw new UserNotFoundException("user ID "+s.getId()+" has an Admin role exist in the system");
	        	else{
	        		studentProfile.setUser(user); // set the managed user eneity to the student profile
		        	studentProfiles.add(studentProfile); // add mapped student profile to the list
	        	} 			
        	}
        	// iterate over list of student profiles   	
        	for(StudentProfile s: studentProfiles){
	        		Set<StudentCourse> studentCourses = new HashSet<>(); // declate a set of student courses
	        		for (StudentCourse c: s.getStudentCourses()){ // iterate over a list of courses associated with a student profile
	        			StudentCourse tmpCourse = studentCourseRepository.findOne(c.getCourseId()); // retreive student course if exist 
	        			if (tmpCourse!=null){ // If the entity already exist, we need to get an instance/attached of it to be only merge not persisted by the JPA        			
	        				studentCourses.add(tmpCourse); 
	        			}
	        			else  // Otherwise, added new entity already mapped by DataMapper, therefore, it will be deemed as a new entity and subsequently persisted
	        				studentCourses.add(c);	
	        		}
	    			studentCourseRepository.save(studentCourses); // Persist and/or merge if there is an entity exist
	    			
	    			s.setStudentCourses(studentCourses); // set student profile with a set of student courses
	        		studentProfileRepository.save(s); // Persist student with a set of classes that are already managed by its own entity
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
	 * to get student profile assoiated with a given user id from system datasource 
	 * @param userId id of user where student profile exist
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @see com.usc.bill.model.StudentProfile
	 */
	public StudentProfile getStudentProfile(String userId) throws NoStudentProfileFoundException{
		if(tempStudentProfile != null && tempStudentProfile.getUser().getId().equals(userId)){ // if there is already student profile that is updated temporarily and has the user id. 
			return tempStudentProfile; // means, the existed login session has updated the student profile not permanentaly, and this temporare instance should be dealt with as a current status of student profile
		}
		else{ // otherwise, retrieve the student profile through repository
			StudentProfile studentProfile = studentProfileRepository.findOneByUser_Id(userId); 
			if (studentProfile !=  null) // if exist in the system datasource
				return studentProfile;
			else // otherwise, it doesn't exist
				throw new NoStudentProfileFoundException("No student record with user ID "+userId+" found");	
		}
	}
	
	/**
	 * to get student record from student profile that is existed in system datasource 
	 * @param userId id of user where student profile exist
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @throws UserPermissionException if the logged-in user has no permission accessing given id of user
	 * @throws UserNotFoundException if no user is logged-in
	 * @see com.usc.bill.dto.StudentRecord
	 */
	@Transactional
	public StudentRecord getStudentRecord(String userId) throws NoStudentProfileFoundException, UserPermissionException, UserNotFoundException{
		// check whether there is a user logged in
		if(userManagementService.getLoggedInUserId()==null)
			throw new UserNotFoundException("No user currently logged in");

		StudentProfile studentProfile = null; 
		if(tempStudentProfile != null && tempStudentProfile.getUser().getId().equals(userId)) // if there is already student profile that is updated temporarily and has the user id
			studentProfile = tempStudentProfile;
		else
			studentProfile = studentProfileRepository.findOneByUser_Id(userId);
			
		if (studentProfile==null)
			throw new NoStudentProfileFoundException("No student record with user ID "+userId+" found");

		if (!userManagementService.checkAccess(studentProfile))
			throw new UserPermissionException("Logged in user has no permission to access user ID "+userId);

		BigDecimal balance = BigDecimal.valueOf(0); // declate an initial of 0 balance
		List<TransactionHistory> allTransactionHistory = billService.getAllTransactionHistory(userId); //retreive all transactions
		Map<String,BigDecimal> currentCharges = billService.getCurrentCharges(userId); // retreive the current charges through ChargeCalculator
		balance = billService.getBalance(currentCharges,allTransactionHistory);	// Calculate the balance through ChargeCalculator
		return DataMapper.getStudentRecord(studentProfile, allTransactionHistory, balance); // return the mapped StudentRecord from DataMapper
	}
	
	/**
	 * to updated existed student profile with a given record of student
	 * @param userId id of user where student profile exist
	 * @param studentRecord will be updated to the existing student record 
	 * @param permanent to determine whether the changes should be permanent or temporary
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @throws UserPermissionException if the logged-in user has no permission accessing given id of user
	 * @throws UserNotFoundException if no user is logged-in
	 * @see com.usc.bill.model.StudentProfile	 
	 * @see com.usc.bill.dto.StudentRecord
	 */
	@Transactional
	public void updateStudentProfle(String userId, StudentRecord studentRecord, boolean permanent) 
			throws NoStudentProfileFoundException, UserPermissionException, UserNotFoundException{
		// check whether there is a user logged in
		if(userManagementService.getLoggedInUserId()==null)
			throw new UserNotFoundException("No user currently logged in");

		StudentProfile studentProfile= studentProfileRepository.findOneByUser_Id(userId);
		if (studentProfile==null)
			throw new NoStudentProfileFoundException("No student record with user ID "+userId+" found");

		if (!userManagementService.checkAccess(studentProfile))
			throw new UserPermissionException("Logged in user has no permission to access user ID "+userId);

		if (studentProfile.getUser().getId() != studentRecord.getId())
			new IllegalArgumentException("The given user id doesn't match with student record id ");

		StudentProfile updatedStudentProf= DataMapper.getStudentProfile(studentRecord);
		if (permanent){ 
			updatedStudentProf.setId(studentProfile.getId()); 
			studentProfileRepository.save(updatedStudentProf); 
		}
		else 
			tempStudentProfile = updatedStudentProf;
	}	
	
	/**
	 * to get all the students that except ones that match given class status from system datasource
	 * @param classStatus class status of student
	 * @return students except ones that match given class status
	 * @see com.usc.bill.model.StudentProfile
	 */
	@Transactional
	public List<StudentProfile> getAllExceptClassStatus(ClassStatus classStatus){
		return studentProfileRepository.findAllExceptClassStatus(classStatus);
	}
	
	/**
	 * to get all the students that match given list of class status 
	 * @param classStatusList a list of class status
	 * @return students with given list of class stauts
	 * @see com.usc.bill.model.ClassStatus
	 * @see com.usc.bill.model.StudentProfile
	 */
	@Transactional
	public List<StudentProfile> getByClassStatus(List<ClassStatus> classStatusList){
		return studentProfileRepository.findByClassStatus(classStatusList);
	}
	
	/**
	 * to get all students that match given college 
	 * @param college college of where student belong to
	 * @return students with given college
	 * @see com.usc.bill.model.College
	 * @see com.usc.bill.model.StudentProfile
	 */
	@Transactional
	public List<StudentProfile> getByCollege(College college){
		return studentProfileRepository.findByCollege(college);
	}
		
	/**
	 * @return tempStudentProfile
	 * @see com.usc.bill.model.StudentProfile
	 */
	public StudentProfile getTempStudentProfile() {
		return tempStudentProfile;
	}
	
	/**
	 * @param studentProfile tempStudentProfile to set
	 */
	public void setTempStudentPofile(StudentProfile studentProfile) {
		tempStudentProfile = studentProfile;
	}	
}