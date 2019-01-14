package com.usc.bill.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.usc.bill.dto.StudentRecord;
import com.usc.bill.exception.NoBillExistedException;
import com.usc.bill.exception.NoStudentProfileFoundException;
import com.usc.bill.exception.UserNotFoundException;
import com.usc.bill.exception.UserPermissionException;
import com.usc.bill.model.Type;
import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.InternationalStatus;
import com.usc.bill.model.Scholarship;
import com.usc.bill.model.Semester;
import com.usc.bill.model.StudentCourse;
import com.usc.bill.model.StudentProfile;
import com.usc.bill.model.StudyAbroad;
import com.usc.bill.model.TransactionHistory;
import com.usc.bill.model.StudentCourse.CourseId;
import com.usc.bill.repository.StudentCourseRepository;
import com.usc.bill.repository.StudentProfileRepository;
import com.usc.bill.repository.TransactionHistoryRepository;
import com.usc.bill.repository.UserRepository;
import com.usc.bill.service.BillService;
import com.usc.bill.service.StudentManagementService;
import com.usc.bill.service.UserManagmentService;

/**
 * ServiceTest.java Purpose: ServiceTest is a class designed
 * to exercise the methods of the Service class using JUnit.
 * 
 * Note-Because of the interaction between the BillService, StudentMangementSErvice
 * and UserManagementService, a SINGLE test class for all 3 of the Services
 * has been abstracted.
 * 
 * @author Wu, Alamri, Stroud
 * @version 1.0 11/29/2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ServiceTest {	
	
	/**
	 * instance of StudentManagementService in order to assist in Student-related bussiness data operations
	 */
	@Autowired
	private StudentManagementService studentManagementService;

	/**
	 * instance of UserManagmentService in order to assist in user-related bussiness data operations
	 */
	@Autowired
	private UserManagmentService userManagmentService;
	
	/**
	 * instance of BillService in order to assist in charge and payment-related business operations
	 */
	@Autowired
	private BillService billService;
	
	/**
	 * instance of UserRepository in order to assist in user-related data access operations
	 */
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * instance of StudentProfileRepository in order to assist in student profile-related data access operations
	 */
	@Autowired
	private StudentProfileRepository stPrRepository;
	
	/**
	 * instance of StudentCourseRepository in order to assist in student course-related data access operations
	 */
	@Autowired
	private StudentCourseRepository stCoRepository;
	
	/**
	 * instance of TransactionHistoryRepository in order to assist in Transaction-related data access operations
	 */
	@Autowired
	private TransactionHistoryRepository tranHisRepository;

	private static StudentProfile studentProfile1;

	/**
	 * Instantiate our three service objects and load a set of test users and student profile.
	 * Create our standard test user profile, Becky, to use across tests too.
	 */
	@Before
	public  void setUp() throws Exception {
		
		userManagmentService.loadUsers("users_02");
		studentManagementService.loadStudentProfiles("students_02");

		studentProfile1 = new StudentProfile();
		studentProfile1.setId(1234);
		studentProfile1.setPhone("888-888-8888");
		studentProfile1.setEmailAddress("becky@email.sc.edu");
		studentProfile1.setAddressStreet("888 Cabbot Cove");
		studentProfile1.setAddressCity("Chapin");
		studentProfile1.setAddressState("SC");
		studentProfile1.setAddressPostalCode("29036");
		studentProfile1.setActiveDuty(false);
		studentProfile1.setGradAssistant(false);
		studentProfile1.setInternational(true);
		studentProfile1.setInternationalStatus(InternationalStatus.SHORT_TERM);
		studentProfile1.setResident(true);
		studentProfile1.setClassStatus(ClassStatus.JUNIOR);
		studentProfile1.setVeteran(false);
		studentProfile1.setFreeTuition(true);
		studentProfile1.setScholarship(Scholarship.NONE);
		studentProfile1.setSemesterBegin(Semester.SUMMER);
		studentProfile1.setYearBegin(2016);
		studentProfile1.setStudyAbroad(StudyAbroad.NONE);
		studentProfile1.setNationalStudentExchange(true);
		studentProfile1.setOutsideInsurance(false);
		Set<StudentCourse> courses = new java.util.HashSet<>();
		
		// Assign one class to Becky, with 4 credit hours.
		StudentCourse course = new StudentCourse();
		CourseId id = new CourseId();
		id.setId("STAT 215");
		id.setOnline(false);
		course.setCourseId(id);
		course.setName("Statistics");
		course.setNumCredits(4);
		courses.add(course);
		studentProfile1.setStudentCourses(courses);
	}
	
	/**
	 * Similarly, tearDown removes the entities established in setUp to prepare the clss
	 * to run the test cases on any subsequent executions.
	 */
	@After
	public void tearDown() throws Exception {
		//Clean up the tables in-memory database
		tranHisRepository.deleteAllInBatch();
		stPrRepository.deleteAllInBatch();
		stCoRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
		studentProfile1 = null;
	}

	/**
	 * Based on the data loaded from student_02, get all of the students who are not
	 * Freshman. A total of 11 students were loaded, 4 were Freshman, so assert a
	 * return value of 7. 
	 */
	@Test
	public void testGetAllExceptClassStatus() {
		// Ask the student management service how many of the students loaded has a classStatus
		// NOT Equal to Freshman. The answer should be 7 as 4 were Freshman (11-4=7)
		int size = studentManagementService.getAllExceptClassStatus(ClassStatus.FRESHMAN).size();
		assertEquals(7, size);
	}

	/**
	 * Based on the data loaded from student_02, get all of the students who are
	 * Juniors. A total of 11 students were loaded, 4 were Juniors.	 
	 */
	@Test
	public void testGetGetByClassStatus() {
		
		// Verify our studentManagementService returns 4 students who are Juniors.
		List<ClassStatus> classStatus = new ArrayList<>();
		classStatus.add(ClassStatus.JUNIOR);
		int size = studentManagementService.getByClassStatus(classStatus).size();
		assertEquals(4, size);
	}

	/**
	 * As no edits have been done to a student profile, the value of tempStudentProfile
	 * is NULL. Assert this to be true.
	 */
	@Test
	public void testGetTempStudentProfile() {
		// Because we have done on edits yet, we still have no TempStudentProfile.
		// Verify this attribute's get returns Null.
		assertNull(studentManagementService.getTempStudentProfile());
	}

	/**
	 * The file user_bad_01 is a Json format but the outer most array brackes of [ ]
	 * have been removed. Assert that if this file is attempted to load as StudentProfiles
	 * that the exception ParseException is thrown.
	 */
	@Test(expected = ParseException.class)
	public void testLoadStudentProfilesBadFormat()
			throws NullPointerException, URISyntaxException, ParseException, UserNotFoundException {
		// Input the same file as user_01 without the Json array bracket
		// To see if it throws proper exception
		studentManagementService.loadStudentProfiles("users_bad_01");
	}
	
	/**
	 * The file user_bad_01 is a Json format but the outer most array brackes of [ ]
	 * have been removed. Assert that if this file is attempted to load as StudentProfiles
	 * that the exception ParseException is thrown. Note, this excercises the billService
	 * while the test above exercises the studentManagmentService.
	 */
	@Test(expected = ParseException.class)
	public void testLoadTransactionBadFormat()
			throws NullPointerException, URISyntaxException, ParseException, UserNotFoundException, NoStudentProfileFoundException {
		// Input the same file as user_01 without the Json array bracket
		// To see if it throws proper exception
		billService.loadTransactions("users_bad_01");
	}
	
	/**
	 * Ther is no transaction with id of 1234. Verify if the BillService tries to access this
	 * transaction that the NullPointerException is thrown.
	 */
	@Test(expected = NullPointerException.class)
	public void testLoadTransactionNullFile()
			throws NullPointerException, URISyntaxException, ParseException, UserNotFoundException, NoStudentProfileFoundException {
		// Input the non-exist file 1234
		// To see if it throws proper exception
		billService.loadTransactions("1234");
	}

	/**
	 * The file students_orphaned_noUser is properly formatted for students, however
	 * it contains students who have not matching entry in a a loaded users file.
	 * Verify this throws the UserNotFound exception.
	 */
	@Test(expected = UserNotFoundException.class)
	public void testLoadStudentProfilesNoUser()
			throws NullPointerException, URISyntaxException, ParseException, UserNotFoundException {
		// Input the students file with no user associated
		// To see if it throws proper exception
		studentManagementService.loadStudentProfiles("students_orphaned_noUser");
	}

	/**
	 * Make certain no user is logged in by calling Logout. Then try to get the student record
	 * 1234 which is our student Becky. The exception thrown should be UserNotFound.
	 */
	@Test(expected = UserNotFoundException.class)
	public void testGetStudentRecordTempProfile()
			throws NoStudentProfileFoundException, UserPermissionException, UserNotFoundException {
		// Make certain no user is logged in
		userManagmentService.logout();
		// try to get a student record with no one logged in should throw UserNotFoundException
		studentManagementService.getStudentRecord("1234");
	}
	
	/**
	 * The file users_bad_01 has no outer array bracket surrounding the user records.
	 * Verify attempting to load this file results in a Parse Exception.
	 */
	@Test(expected = ParseException.class)
	public void testUserManagementServiceParseBadJsonFile() throws IOException, ParseException, URISyntaxException {
		// Input the same file as user_01 without the Json array bracket
		// To see if it throws proper exception
		userManagmentService.loadUsers("users_bad_01");
	}
	
	/**
	 * Verify the BillService class is able to return the endTerm charges for our student Becky.
	 */
	@Test
	public void testGenerateEndTermCharge() {
		// GenerateEndTermCharge is going to summarize the provided profile's charges into end term charge for future calculations.
		// As a method that throws no exceptions and returns void, this test just ensures the call can be made.
		billService.generateEndTermCharge(studentProfile1);
	}
	
	/**
	 * Verify the method getBalance when supplied with an array containing a transaction hitory
	 * functions properly, that is to say no exception is thrown.
	 */
	@Test
	public void testGetBalance() {
		// Create a dummy list of TransactionHistory to test if getBalance can be called
		List<TransactionHistory> histories = new ArrayList<>();
		TransactionHistory history = new TransactionHistory();
		history.setAmount(new BigDecimal(100.00));
		history.setDate(new Date());
		history.setId(0);
		history.setNote("This is a note");
		history.setType(Type.PAYMENT);
		history.setUser(studentProfile1.getUser());
		billService.getBalance(histories);
	}
	
	/**
	 * The user rrbob is an admin but does not have onBehalf of authority over mhunt student.
	 * Verify if rbob requests the bill for mhunt that the exception UserPermission is raised.
	 */
	@Test(expected = UserPermissionException.class)
	public void testGetCurrentBillNoPermission() throws UserNotFoundException, NoStudentProfileFoundException, UserPermissionException, NoBillExistedException {
		// Login as the user rbob who is in the college of ENGINEERING_AND_COMPUTING
		userManagmentService.login("rbob");
		
		// User rbob should not be allowed to get the current bill for student mhunt because
		// she is in the college of ARTS_AND_SCIENCES. Therefore expect a userPermissionException
		billService.getCurrentBill("mhunt");
	}
	
	/**
	 * Make certain no user is logged in and attempt to get the student record for ggay from the
	 * studentManagementService. Verify this raises the UserNotFound exception.
	 */
	@Test(expected = UserNotFoundException.class)
	public void testGetStudentRecordNoUserLogin() throws NoStudentProfileFoundException, UserPermissionException, UserNotFoundException {
		// Make certain no user is logged in at all.
		userManagmentService.logout();
		
		// Now try to get a StudentRecord, should through UserNotFound exception.
		studentManagementService.getStudentRecord("ggay");
	}
	
	/**
	 * The user rrbob is an admin but does not have onBehalf of authority over mhunt student.
	 * Verify if rbob requests the student record for mhunt that the exception UserPermission is raised.
	 */	
	@Test(expected = UserPermissionException.class)
	public void testGetStudentRecordNoPermission() throws UserNotFoundException, NoStudentProfileFoundException, UserPermissionException, NoBillExistedException {
		// Login as the user rbob who is in the college of ENGINEERING_AND_COMPUTING
		userManagmentService.login("rbob");
		
		// User rbob should not be allowed to get the student record for student mhunt because
		// she is in the college of ARTS_AND_SCIENCES. Therefore expect a userPermissionException
		studentManagementService.getStudentRecord("mhunt");
	}
	
	/**
	 * The user rrbob is an admin but does not have onBehalf of authority over mhunt student.
	 * Verify if rbob requests the to update the student profile for mhunt that the exception
	 * UserPermission is raised.
	 */	
	@Test(expected = UserPermissionException.class)
	public void testUpdateStudentProfileNoPermission() throws UserNotFoundException, NoStudentProfileFoundException, UserPermissionException, NoBillExistedException {
		// Login as the user rbob who is in the college of ENGINEERING_AND_COMPUTING
		userManagmentService.login("rbob");
		
		// User rbob should not be allowed to update the student profile for student mhunt because
		// she is in the college of ARTS_AND_SCIENCES. Therefore expect a userPermissionException
		studentManagementService.updateStudentProfle("mhunt", new StudentRecord(), true);
	}
	
	/**
	 * The user rrbob is an admin. Verify if rbob requests the to update the student profile for
	 * a student 123 which does not exist, that the exception NoStudentProfileFound is raised.
	 */	
	@Test(expected = NoStudentProfileFoundException.class)
	public void testUpdateStudentProfileNoProfile() throws UserNotFoundException, NoStudentProfileFoundException, UserPermissionException, NoBillExistedException {
		// Login as the user rbob who is in the college of ENGINEERING_AND_COMPUTING
		userManagmentService.login("rbob");
	
		// User rbob should not be allowed to get the student record for student 123 because
		// user 123 is not existed in the file. Therefore expect a NoStudentProfileFoundException
		studentManagementService.updateStudentProfle("123", new StudentRecord(), true);
	}

}
