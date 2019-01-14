package com.usc.bill.service;


import java.net.URISyntaxException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.usc.bill.dto.StudentRecord;
import com.usc.bill.exception.NoStudentProfileFoundException;
import com.usc.bill.exception.UserNotFoundException;
import com.usc.bill.exception.UserPermissionException;
import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.College;
import com.usc.bill.model.StudentProfile;

/**
 * A common interface that handle business operations that concerns student profile and courses-related data within the scope of the current requirement
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */
@Service
public interface StudentManagementService{
	
	/**
	 * to load student profiles from file that is formated as a JSON into the system datasource
	 * @param filename name of file where the records of student existed
	 * @throws URISyntaxException if the given filename has improper format  
	 * @throws NullPointerException if the file associated with a given filename doesn't exist
	 * @throws ParseException if the file associated with a given filename couldn't be parsed into JSONArray
	 * @throws UserNotFoundException if the id associated with student record is existed in the system for admin user
	 */
	public void loadStudentProfiles(String filename) throws URISyntaxException, NullPointerException, ParseException, UserNotFoundException;	
	
	/**
	 * to get student profile assoiated with a given user id from system datasource 
	 * @param userId id of user where student profile exist
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @return student profile associated with the given userId
	 * @see com.usc.bill.model.StudentProfile
	 */
	public StudentProfile getStudentProfile(String userId) throws NoStudentProfileFoundException;
	
	/**
	 * to get student record from student profile that is existed in system datasource 
	 * @param userId id of user where student profile exist
	 * @return student record asscoiated with given userId
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @throws UserPermissionException if the logged-in user has no permission accessing given id of user
	 * @throws UserNotFoundException if no user is logged-in
	 * @see com.usc.bill.dto.StudentRecord
	 */
	public StudentRecord getStudentRecord(String userId) throws NoStudentProfileFoundException, UserPermissionException, UserNotFoundException;
		
	/**
	 * to updated existed student profile with a given record of student
	 * @param userId id of user where student profile exist
	 * @param studentRecord will be updated to the existing student record 
	 * @param permanent to determine whether the changes should be permanent or temporary
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @throws UserPermissionException if the logged-in user has no permission accessing given id of user
	 * @throws UserNotFoundException if no user is logged-in
	 * @see com.usc.bill.dto.StudentRecord
	 */
	public void updateStudentProfle(String userId, StudentRecord studentRecord, boolean permanent)
			throws NoStudentProfileFoundException, UserPermissionException, UserNotFoundException;
	/**
	 * to get all the students that except ones that match given class status from system datasource
	 * @param classStatus class status of student
	 * @return students except ones that match given class status
	 * @see com.usc.bill.model.StudentProfile
	 */
	public List<StudentProfile> getAllExceptClassStatus(ClassStatus classStatus);
	
	/**
	 * to get all the students that match given list of class status 
	 * @param classStatusList a list of class status
	 * @return students with given list of class stauts
	 * @see com.usc.bill.model.ClassStatus
	 * @see com.usc.bill.model.StudentProfile
	 */
	public List<StudentProfile> getByClassStatus(List<ClassStatus> classStatusList);
	
	/**
	 * to get all students that match given college 
	 * @param college college of where student belong to
	 * @return students with given college
	 * @see com.usc.bill.model.College
	 * @see com.usc.bill.model.StudentProfile
	 */
	public List<StudentProfile> getByCollege(College college);
	
	/**
	 * @return tempStudentProfile
	 * @see com.usc.bill.model.StudentProfile
	 */
	public StudentProfile getTempStudentProfile();
	
	/**
	 * @param studentProfile tempStudentProfile to set
	 * @see com.usc.bill.model.StudentProfile
	 */
	public void setTempStudentPofile(StudentProfile studentProfile);
}