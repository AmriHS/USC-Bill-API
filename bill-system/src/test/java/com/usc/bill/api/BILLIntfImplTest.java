package com.usc.bill.api;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.usc.bill.dto.Bill;
import com.usc.bill.dto.StudentRecord;
import com.usc.bill.dto.Transaction;
import com.usc.bill.exception.NoBillExistedException;
import com.usc.bill.exception.NoStudentProfileFoundException;
import com.usc.bill.exception.UserNotFoundException;
import com.usc.bill.exception.UserPermissionException;
import com.usc.bill.model.StudyAbroad;
import com.usc.bill.repository.StudentCourseRepository;
import com.usc.bill.repository.StudentProfileRepository;
import com.usc.bill.repository.TransactionHistoryRepository;
import com.usc.bill.repository.UserRepository;

/**
 * BillIntfImplTest is the test class for BillIntfImpl.
 * Various test cases are defined using JUnit with assertions to verify
 * the correctness of BillIntfImpl methods as well as forced cases to
 * trigger errors and to affirm that the proper Exception has been raised.
 * 
 * @author Wu, Alamri, Stroud
 * @version 1.0 11/30/2017
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false) 
@Transactional
public class BILLIntfImplTest{
	
	/**
	 * An instance of the BillIntf is required to execute the test cases.
	 */
	private static BILLIntf billIntf;

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
	/**
	 * The user and student records defined in test/resources/data are a precondition for
	 * each method to run and are therefore loaded in the setUp method of the test class.
	 */
	@Before
	public void setUp() throws Exception {
		billIntf = new BILLIntfImpl();
	    billIntf.loadUsers("users_02");
		billIntf.loadRecords("students_02");
	}

	/**
	 * Similarly, tearDown removes the entities established in setUp to prepare the class
	 * and resets the login and temporary student profile to run the test cases on any subsequent executions.
	 */
	@After
	public void tearDown() throws Exception {
		billIntf.logOut();

		//Clean up the tables in-memory database
		tranHisRepository.deleteAllInBatch();
		stPrRepository.deleteAllInBatch();
		stCoRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();	
	}
	
	/**
	 * The loading of users_01 should encounter errors in the date and throw
	 * the IllegalArument exception. Verify this happens.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFailedLoadUsers() throws Exception {
		billIntf.loadUsers("users_01");
	}
	
	/**
	 * The loading of fakefilename for Users, which as the name implies is a
	 * non-existent file, should throw the NullPointerException. Verify this.
	 */
	@Test(expected=NullPointerException.class)
	public void testNonExistedUserFile() throws Exception {
		billIntf.loadUsers("fakefilename");
	}

	/**
	 * The loading of fakefilename for Students, which as the name implies is a
	 * non-existent file, should throw the NullPointerException. Verify this.
	 */
	@Test(expected=NullPointerException.class)
	public void testNonExistedStudentFile() throws Exception {
		billIntf.loadRecords("fakefilename");
	}
	
	/**
	 * The loading of students_01  for Students will encounter users already
	 * loaded in the setUp method and throw the IllegalArgumentException. Verify this.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testDuplicatLoadRecords() throws Exception {
		billIntf.loadRecords("students_01");
	}

	/**
	 * The student "fakeId" does not exist in the file student_02 which was loaded
	 * in setUp. First login as a valid user from user_02, rbob, then try to 
	 * retrieve student fakeId. This should throw the NoStudenProfileFound exeption.
	 * Verify this.
	 */
	@Test(expected=NoStudentProfileFoundException.class)
	public void testNonExistedUserRecords() throws Exception {
		billIntf.logIn("rbob");
		billIntf.getRecord("fakeId");
	}
	
	/**
	 * The user mhunt is a valiid user loaded in setUp. Confirm that she is able to
	 * log in. This is confirmed by asserting the user set in billIntf is in fact mhunt.
	 */
	@Test
	public void testSuccessfulLogin() throws Exception {
		billIntf.logIn("mhunt");
		assertEquals(billIntf.getUser(), "mhunt");
	}

	/**
	 * Attempt to login as fakeId. This is not a user who has been loaded in user_02
	 * and therefore should throw the UserNotFound excetion. Verify this.
	 */
	@Test(expected=UserNotFoundException.class)
	public void testFailedLogIn() throws Exception {
		billIntf.logIn("fakeId");
	}
	
	/**
	 * As the setUp method loaded users but did NOT log in any user, a call to the
	 * billIntf method getUser should return NULL. Assert that null is returned.
	 */
	@Test
	public void testNoUserLoggedIn() throws Exception {
		billIntf.logOut();
	}

	/**
	 * The user bbradley exists in users_02 and was loaded in setUp. However the role
	 * of this user is not Admin and therefore a call to the getStudentIDs method, which
	 * is designed for Admin users, should throw the UserPermissionException. Verify this.
	 * @throws Exception in case user is no exist or the logged-in user has no administration privilege
	 */
	@Test(expected=UserPermissionException.class)
	public void testFailedGetStudentIDs() throws Exception{
		billIntf.logIn("bbradley");
		billIntf.getStudentIDs();
	}

	/**
	 * The user jsmith exists in users_02 and was loaded in setUp and has the role of Admin.
	 * Admin users are permitted to access a list of other students who they have administrative
	 * rights "onBehalfOf", see Requirements document, Appendix, for these detailed rules.
	 * As per the students in students_02 loaded by setUP, jsmith should have privilege
	 * over the three users mhunt, aabbott, and bbradley. Assert this to be true.
	 */
	@Test
	public void testGetStudentIDs() throws Exception{
		billIntf.logIn("jsmith");
		List<String> userIds = billIntf.getStudentIDs();
		assertArrayEquals(userIds.toArray(new String[userIds.size()]), new String []{"mhunt","aabbott", "bbradley"});
	}
	
	/**
	 * The user mhunt exists in users_02 and was loaded in setUp and, once logged in,
	 * has permission to access her student record which was loaded from student_02 during
	 * setUp. Assert she may access her own Student record in a 2 step process. That she
	 * may call getRecord to fetch her record AND that the record returned is in fact hers.
	 */
	@Test
	public void testGetRecord() throws Exception {
		billIntf.logIn("mhunt");
		StudentRecord studentRecord = billIntf.getRecord("mhunt");
		assertNotNull(studentRecord);
		assertEquals(studentRecord.getId(), "mhunt");
	}

	/**
	 * The user jsmith exists in users_02 and was loaded in setUp and as per the records
	 * loaded from studnts_02 has administrative rights over the user bbradley, which
	 * includes editing bbradley's student record. However the firstName field should
	 * deny a NULL value and throw the IllegalArgument exception. Verify this.
	 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFailedEditRecord() throws Exception {
		billIntf.logIn("jsmith");
		StudentRecord studentRecord = billIntf.getRecord("bbradley");
		studentRecord.setFirstName(null);
		billIntf.editRecord(studentRecord.getId(), studentRecord, true);
	}

	/**
	 * The user mhunt exists in users_02 and was loaded in setUp. She has permissio to
	 * generate her own bill and based on the data loaded in students_02 the resulting
	 * bill should have 3 transactions. Assert this to be true.
	 */
	@Test
	public void testGenerateBill() throws Exception {
		billIntf.logIn("mhunt");
		assertEquals(billIntf.generateBill("mhunt").getTransactions().size(), 3);
	}

	/**
	 * The user mmathews exists in users_02 and was loaded in setUp and has permission
	 * to edit the student mhunt, who was also loaded from the students_02 file, also 
	 * in setUp. Verify that mmatthews, once logged in, can modify mhunt's student record
	 * with a valid value of setOutsideInsurance=true AND can further generate mhunt's
	 * bill AND that the total charges for mhunt is $2,351.
	 */
	@Test
	public void testGenerateBillAfterUpdateRecord() throws Exception {
		billIntf.logIn("mmatthews");
		StudentRecord studentRecord = billIntf.getRecord("mhunt");
		studentRecord.setOutsideInsurance(true);
		billIntf.editRecord("mhunt", studentRecord, true);
		Bill bill = billIntf.generateBill("mhunt");
		BigDecimal totalCharges = BigDecimal.valueOf(0);
		for (Transaction t: bill.getTransactions()){
		 	totalCharges = totalCharges.add(t.getAmount());
		 	System.out.println(t.getNote());
		}
		assertEquals(totalCharges, BigDecimal.valueOf(2351.0));
	}
	
	/**
	 * The user ggay exists in users_02 and was loaded in setUp and was present in
	 * student_02 and loaded as a student. Verify ggay may generate his own bill
	 * and that the total of his charges, note he is taking a heavy class load with
	 * 17 credit hours as per students_02, and his charges are $15,838.50.
	 * 
	 * Note-Important to the calculation of these charges, student ggay is a
	 * capstone scholar, the only capstone scholar in students_02.
	 */
	@Test
	public void testGenerateBillWithCapstoneScholar() throws Exception {
		billIntf.logIn("ggay");
		Bill bill = billIntf.generateBill("ggay");
		BigDecimal totalCharges = BigDecimal.valueOf(0);
		for (Transaction t: bill.getTransactions())
		 	totalCharges = totalCharges.add(t.getAmount());
		assertEquals(totalCharges, BigDecimal.valueOf(15838.5));
	}
	
	/**
	 * The user cconnorR (R for Regular) exists in users_02 and was loaded in setUp
	 * and was present in student_02 and loaded as a student. Verify cconnorr may 
	 * generate his own bill and that the total of his charges, note he is 14 credit
	 * hours as per students_02, and his charges are $13,426.00
	 * 
	 * Note-Important to the calculation of these charges, student cconnorr is a
	 * studyAbroad student, one of only 2 in students_02 and the only of type REGULAR.
	 */	
	@Test
	public void testGenerateBillWithStudyAbroad() throws Exception {
		billIntf.logIn("cconnorr");
		Bill bill = billIntf.generateBill("cconnorr");
		BigDecimal totalCharges = BigDecimal.valueOf(0);
		for (Transaction t: bill.getTransactions())
		 	totalCharges = totalCharges.add(t.getAmount());
		assertEquals(totalCharges, BigDecimal.valueOf(13423.0));
	}
	
	/**
	 * The user cconnorC (C for Cohort) exists in users_02 and was loaded in setUp
	 * and was present in student_02 and loaded as a student. Verify cconnorC may 
	 * generate his own bill. The test in this case is not that a particular amount
	 * is returned, we have other tests for amount accuracy, but that student
	 * cconnorC is unique in that he has already graduated (classStatus=Graduated).
	 * 
	 */	
	@Test(expected=NoBillExistedException.class)
	public void testViewChargesForGraduated() throws Exception {
		billIntf.logIn("cconnorC");
		billIntf.generateBill("cconnorC");
	}
	
	/**
	 * The user mhunt exists in users_02 and was loaded in setUp and was present in
	 * student_02 and loaded as a student. At the time of loading her City and State
	 * had values of Pittsburgh, PA. Verify she may modify her values for City and State
	 * and assert after the update call (editRecord) the new value are returned by
	 * getRecord. 
	 * 
	 */	
	@Test
	public void testSucessfulRecordUpdate() throws Exception {
		billIntf.logIn("mhunt");
		StudentRecord studentRecord = billIntf.getRecord("mhunt");
		studentRecord.setAddressCity("Charlotte");
		studentRecord.setAddressState("NC");
		billIntf.editRecord("mhunt", studentRecord, true);
		assertEquals(billIntf.getRecord("mhunt").getAddressState(), "NC");
		assertEquals(billIntf.getRecord("mhunt").getAddressCity(), "Charlotte");
	}
	
	/**
	 * The user ggay exists in users_02 and was loaded in setUp
	 * and was present in student_02 and loaded as a student. While ggay
	 * should be allowed to modify his own student record a business rule
	 * exists (see Requirements document, appendix) stating a student may  
	 * not be allowed to study abroad if they are on schoarship. Verify
	 * student gggay, who is defined in students_02 as on WOODROW scholarship
	 * may not modify his studyAbroad to COHORT because of this restriction.
	 * The exception IllegalArgument should be thrown.
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void testRestrictedRecordUpdate() throws Exception {
		billIntf.logIn("ggay");
		StudentRecord studentRecord = billIntf.getRecord("ggay");
		studentRecord.setStudyAbroad(StudyAbroad.COHORT.toString());
		billIntf.editRecord("ggay", studentRecord, false);
	}
	
	/**
	 * The user mmatthews is a graduate school administrator and should be
	 * defined permission to view the charges on bbradly, a non graduate
	 * student. Verify the exception UserPermission is thrown when user
	 * mmatthews attempts this.
	 */	
	@Test(expected=UserPermissionException.class)
	public void testNonPermittedViewCharges() throws Exception {
		billIntf.logIn("mmatthews");
		billIntf.viewCharges("bbradley", 2, 2, 2013, 2, 2, 2016);
	}
	
	/**
	 * The user rbob exists in users_02 and was loaded in setUp
	 * and has permission over the student ggaya, who was loaded in
	 * setUp from students_02. However user ggaya started school in
	 * 2017. Verify is rbob requests charges for ggaya from 2013 thht
	 * the exception NoBillExisted is thrown.
	 */	
	@Test(expected=NoBillExistedException.class)
	public void testNoBillViewCharges() throws Exception {
		billIntf.logIn("rbob");
		billIntf.viewCharges("ggaya", 2, 2, 2013, 2, 4, 2013);
	}

	/**
	 * The user rbob exists in users_02 and was loaded in setUp
	 * and has permission over the student bbradley, who was loaded in
	 * setUp from students_02. Verify is rbob requests to view the charges
	 * for bbradley where the START date is after the END date, that
	 * an exception for IllegalArgument is thrown.
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDateViewCharges() throws Exception {
		billIntf.logIn("rbob");
		billIntf.viewCharges("bbradley", 2, 2, 2018, 1, 12, 2017);
	}
	
	/**
	 * The user rbob exists in users_02 and was loaded in setUp
	 * and has permission over the student bbradley, who was loaded in
	 * setUp from students_02. Verify is rbob requests to view the charges
	 * for bbradley with a START date of February 34th, that this illegal
	 * date is rejected by throwing an exception for IllegalArgument.
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDayViewCharges() throws Exception {
		billIntf.logIn("rbob");
		billIntf.viewCharges("bbradley", 2, 34, 2017, 1, 12, 2017);
	}
	
	/**
	 * The user bbradley exists in users_02 and was loaded in setUp
	 * and was also loaded by setUp from students_02 as a student. Verify
	 * when bbradly request his charges with a valid date range for which he
	 * has charges that an amount is returned. The amount is asserted to be
	 * a toal of $5,372.50
	 */	
	@Test
	public void testViewCharges() throws Exception {
		billIntf.logIn("bbradley");
		Bill bill = billIntf.viewCharges("bbradley", 2, 2, 2013, 1, 12, 2018);
		BigDecimal totalCharges = BigDecimal.valueOf(0);
		for (Transaction t: bill.getTransactions())
			totalCharges = totalCharges.add(t.getAmount());
		assertEquals(totalCharges, BigDecimal.valueOf(5372.5));
	}
	
	/**
	 * The user rbob exists in users_02 and was loaded in setUp
	 * but does not have a student record and therefore has no bill
	 * or charges. Verify if rbob requests his own charages the 
	 * exception of NoStudentProfile is thrown.
	 */	
	@Test(expected=NoStudentProfileFoundException.class)
	public void testViewChargesOnAdmin() throws Exception {
		billIntf.logIn("rbob");
		billIntf.viewCharges("rbob", 2, 2, 2013, 1, 12, 2018);
	}
	
	/**
	 * The user mhunt exists in users_02 and was loaded in setUp
	 * and as a student also in setup from student_02. Her student
	 * profile was loaded with 3 payment records. Verify she may 
	 * make a payment to her own account and afterwards her bill
	 * has a count of 4 payments.
	 */	
	@Test
	public void testApplyPayment() throws Exception {
		billIntf.logIn("mhunt");
		billIntf.applyPayment("mhunt", BigDecimal.valueOf(1), "FakePayment");
		assertEquals(billIntf.generateBill("mhunt").getTransactions().size(), 4);
	}
	
	/**
	 * The user cconnorF exists in users_02 and was loaded in setUp
	 * and as a student also in setup from student_02. Attempt to 
	 * make a payment by and for CconnorF but use a negative number
	 * which should throw the exception IllegalArgument. Verify this.
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void testNegativePaymentWithNote() throws Exception {
		billIntf.logIn("cconnorf");
		billIntf.applyPayment("cconnorf", new BigDecimal(-100), "this is a note");
		billIntf.logOut();
	}

	/**
	 * The user cconnorF exists in users_02 and was loaded in setUp
	 * and as a student also in setup from student_02. Attempt to 
	 * make a payment by and for CconnorF, use a valid payment amount
	 * but do not include a note with the payment. As per the requirements
	 * a note value is required and therefore this should throw the 
	 * exception IllegalArgument. Verify this.
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void testPositivePaymentWithoutNote() throws Exception {
		billIntf.logIn("cconnorf");
		billIntf.applyPayment("cconnorf", new BigDecimal(100), null);
		billIntf.logOut();
	}
	
	/**
	 * The user mhunt exists in users_02 and was loaded in setUp
	 * and as a student also in setup from student_02. Generate the
	 * bill for mhunt and then assert that all of the demographic,
	 * class and college information can be fecthed correctly from the 
	 * bill as it was provided originally in students_02. Note, a
	 * prior test modified the City and State to Charlotte, NC. However
	 * because setUp is defined as @Before instead of @BeforeClass, 
	 * the original values of Pittsburgh, PA should be in the profile.
	 * Assert the fields are as defined in students_02.
	 */	
	@Test
	public void testBillAttr() throws Exception {
		billIntf.logIn("mhunt");
		Bill bill = billIntf.generateBill("mhunt");
		assertEquals(bill.getId(), "mhunt");
		assertEquals(bill.getFirstName(), "Michelle");
		assertEquals(bill.getLastName(), "Hunt");
		assertEquals(bill.getPhone(), "999-999-9999");
		assertEquals(bill.getEmailAddress(), "mhunt@mailbox.sc.edu");
		assertEquals(bill.getAddressStreet(), "221B Baker St.");
		assertEquals(billIntf.generateBill("mhunt").getAddressCity(), "Pittsburgh");
		assertEquals(billIntf.generateBill("mhunt").getAddressState(), "PA");
		assertEquals(bill.getAddressPostalCode(), "26505");
		assertEquals(bill.getClassStatus(), "PHD");
		assertEquals(bill.getCollege(), "ARTS_AND_SCIENCES");
		assertEquals(bill.getCourses().size(), 3);
		billIntf.logOut();
	}
}