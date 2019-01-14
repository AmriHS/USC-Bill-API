package com.usc.bill.utility;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.usc.bill.dto.Course;
import com.usc.bill.dto.Transaction;
import com.usc.bill.dto.TransactionDate;
import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.College;
import com.usc.bill.model.InternationalStatus;
import com.usc.bill.model.Role;
import com.usc.bill.model.Scholarship;
import com.usc.bill.model.Semester;
import com.usc.bill.model.StudentCourse;
import com.usc.bill.model.StudentProfile;
import com.usc.bill.model.StudyAbroad;
import com.usc.bill.model.TransactionHistory;
import com.usc.bill.model.Type;
import com.usc.bill.model.User;
import com.usc.bill.utility.DataMapper;
import com.usc.bill.model.StudentCourse.CourseId;

/**
 * DataMapperTest.java Purpose: DataMapperTest is a class designed to
 * exercise the methods of the DataMapper class using JUnit.
 * 
 * @author Wu, Alamri, Stroud
 * @version 1.0 11/29/2017
 */
public class DataMapperTest {
	
	// Initialize class variables to use as input for various tests.
	static StudentProfile studentProfile;
	static List<Course> courses;	
	static List<Transaction> transactions;

	// Use @Before to establish a studentProfile for use across this class. We'll call her "Becky", with id of 1234.
	@Before
	public void setUp() throws Exception {
		studentProfile = new StudentProfile();
		User user = new User();
		user.setCollege(College.ENGINEERING_AND_COMPUTING);
		user.setFirstName("Becky");
		user.setLastName("Beckworth");
		user.setId("1234");
		user.setRole(Role.STUDENT);
		studentProfile.setUser(user);
		studentProfile.setId(1234);
		studentProfile.setPhone("888-888-8888");
		studentProfile.setEmailAddress("becky@email.sc.edu");
		studentProfile.setAddressStreet("888 Cabbot Cove");
		studentProfile.setAddressCity("Chapin");
		studentProfile.setAddressState("SC");
		studentProfile.setAddressPostalCode("29036");
		studentProfile.setActiveDuty(false);
		studentProfile.setGradAssistant(false);
		studentProfile.setInternational(true);
		studentProfile.setInternationalStatus(InternationalStatus.SHORT_TERM);
		studentProfile.setResident(true);
		studentProfile.setClassStatus(ClassStatus.JUNIOR);
		studentProfile.setVeteran(false);
		studentProfile.setFreeTuition(true);
		studentProfile.setScholarship(Scholarship.NONE);
		studentProfile.setSemesterBegin(Semester.SUMMER);
		studentProfile.setYearBegin(2016);
		studentProfile.setStudyAbroad(StudyAbroad.NONE);
		studentProfile.setNationalStudentExchange(true);
		studentProfile.setOutsideInsurance(false);
		studentProfile.setSemesterCapstoneEnrolled(Semester.FALL);
		studentProfile.setYearCapstoneEnrolled(0);
		Set<StudentCourse> courses = new java.util.HashSet<>();
		StudentCourse course = new StudentCourse();	
		CourseId id = new CourseId();
		id.setId("STAT 215");
		id.setOnline(false);
		course.setCourseId(id);
		course.setName("Statistics");
		course.setNumCredits(4);
		courses.add(course);
		studentProfile.setStudentCourses(courses);
	}
		
	/**
	 * Similarly, tearDown removes the entities established in setUp to prepare the class
	 * to run the test cases on any subsequent executions.
	 */
	@After
	public void tearDown() throws Exception {
		//Set the studentProfile to null
		studentProfile = null;
	}
		
	/**
	 * testGetStudentRecord, using our student Profile for our Becky verify if a transaction history
	 * exists and a new charge is made that the Datamapper computes the correct sum.
	 */
	@Test
	public void testGetStudentRecord() {
		List<TransactionHistory> histories = new ArrayList<>();
			
		// Create a Transaction HIstory for a charge of $100, remember charges are negative when applied to a bill.
		TransactionHistory history = new TransactionHistory();
		history.setAmount(new BigDecimal(100));
		history.setDate(new Date());
		history.setId(123);
		history.setNote("This is a note");
		history.setType(Type.CHARGE);
		history.setUser(new User());
		histories.add(history);
			
		Map<String, BigDecimal> currentCharges = new HashMap<String, BigDecimal>();
		currentCharges.put("aCharge", new BigDecimal(101.00));
		
		assertEquals("1234", DataMapper.getStudentRecord(studentProfile, histories, currentCharges, new BigDecimal(99.00)).getId());
	}
		
	/**
	 * testGetDate is a small tests that verifies if a valid transaction date is created (3/4/2017)
	 * that the DataMapper when provided this date correctly returns it.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testGetDate() {
		TransactionDate tDate = new TransactionDate();
		tDate.setDay(3);
		tDate.setMonth(4);
		tDate.setYear(2017);
		assertEquals(tDate.getDay(), DataMapper.getDate(tDate).getDate());
	}
		
	/**
	 * testGetBill, using our student Profile for our Becky verify if request is made to the datamapper
	 * to get the id of the user for which the current bill is associated, that it correctly returns
	 * the student id of Becky, 1234.
	 */
	@Test
	public void testGetBill() {
		Map<String, BigDecimal> currentCharges = new HashMap<String, BigDecimal>();
		currentCharges.put("aCharge", new BigDecimal(101.00));
			
		assertEquals("1234", DataMapper.getBill(studentProfile, currentCharges).getId());
	}
		
	/**
	 * testGetStudentTransactions, a small test that verifies if one charge is added to the current 
	 * charges, that if the current transactions are queried only 1 charge is returned.
	 */
	@Test
	public void testGetTransactions() {
		Map<String, BigDecimal> currentCharges = new HashMap<String, BigDecimal>();
		currentCharges.put("aCharge", new BigDecimal(101.00));
		
		assertEquals(1, DataMapper.getTransactions(currentCharges).size());
	}
}
