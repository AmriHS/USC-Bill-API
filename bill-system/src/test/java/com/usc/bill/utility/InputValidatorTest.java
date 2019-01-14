package com.usc.bill.utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.usc.bill.dto.Course;
import com.usc.bill.dto.StudentRecord;
import com.usc.bill.dto.Term;
import com.usc.bill.dto.Transaction;
import com.usc.bill.dto.TransactionDate;
import com.usc.bill.model.Type;
import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.College;
import com.usc.bill.model.InternationalStatus;
import com.usc.bill.model.Scholarship;
import com.usc.bill.model.Semester;
import com.usc.bill.model.StudyAbroad;
import com.usc.bill.model.User;
import com.usc.bill.utility.InputValidator;

/**
 * InputValidatorTest.java Purpose: InputValidatorTest is a class designed to
 * exercise the methods of the InputValidator class using JUnit.
 * 
 * @author Wu, Alamri, Stroud
 * @version 1.0 11/29/2017
 */
public class InputValidatorTest {

	// Initialize class variables to use as input for various tests.
	private StudentRecord studentRecord;
	private List<Course> courses;
	private List<Transaction> transactions;

	// Define a SetUp method to initialize class variables for each tests method
	@Before
	public void setUp() throws Exception {

		/**
		 * Create a standard Student Record, "Becky", to use throughout the tests.
		 */
		// Create our standard user Becky, id of 123. She is a Junior at College of E&C,
		// initially.
		studentRecord = new StudentRecord();
		studentRecord.setId("123");
		studentRecord.setFirstName("Becky");
		studentRecord.setLastName("Beckworth");
		studentRecord.setPhone("888-888-8888");
		studentRecord.setEmailAddress("becky@email.sc.edu");
		studentRecord.setAddressStreet("888 Cabbot Cove");
		studentRecord.setAddressCity("Chapin");
		studentRecord.setAddressState("SC");
		studentRecord.setAddressPostalCode("29036");
		studentRecord.setActiveDuty(false);
		studentRecord.setGradAssistant(false);
		studentRecord.setInternational(true);
		studentRecord.setInternationalStatus(InternationalStatus.SHORT_TERM.toString());
		studentRecord.setResident(true);
		studentRecord.setClassStatus(ClassStatus.JUNIOR.toString());
		studentRecord.setVeteran(false);
		studentRecord.setCollege(College.ENGINEERING_AND_COMPUTING.toString());
		studentRecord.setFreeTuition(true);
		studentRecord.setScholarship(Scholarship.NONE.toString());
		studentRecord.setStudyAbroad(StudyAbroad.NONE.toString());
		studentRecord.setNationalStudentExchange(true);
		studentRecord.setOutsideInsurance(false);
		Term term = new Term();
		term.setSemester(Semester.FALL.toString());
		term.setYear(2017);
		studentRecord.setCapstoneEnrolled(term);
		studentRecord.setTermBegan(term);
		List<Transaction> transactionsForRecord = new ArrayList<>();
		Transaction transactionForRecord = new Transaction();

		// Add a payment to Becky's transactions of $100 on 2/2/2017
		transactionForRecord.setAmount(new BigDecimal(100.00));
		transactionForRecord.setNote("This is a note");
		TransactionDate dateForRecord = new TransactionDate();
		dateForRecord.setDay(2);
		dateForRecord.setMonth(2);
		dateForRecord.setYear(2017);
		transactionForRecord.setTransactionDate(dateForRecord);
		transactionForRecord.setType(Type.PAYMENT.toString());
		transactionsForRecord.add(transactionForRecord);

		// Assign one course of 3 credit hours, not online. This course is VALID.
		List<Course> coursesForRecord = new ArrayList<>();
		Course courseForRecord = new Course();
		courseForRecord.setId("id");
		courseForRecord.setName("Name");
		courseForRecord.setNumCredits(3);
		courseForRecord.setOnline(false);
		coursesForRecord.add(courseForRecord);
		studentRecord.setCourses(coursesForRecord);
		studentRecord.setTransactions(transactionsForRecord);

		// Now create an array of some bad (invalid) courses, we won't add these to
		// Becky
		// They are just to test validation.
		courses = new ArrayList<>();

		// Course with a NULL id
		Course courseIdNull = new Course();
		courseIdNull.setId(null);
		courseIdNull.setName("Name");
		courseIdNull.setNumCredits(3);
		courseIdNull.setOnline(false);

		// Course with a Null name
		Course courseNameNull = new Course();
		courseNameNull.setId("id");
		courseNameNull.setName(null);
		courseNameNull.setNumCredits(3);
		courseNameNull.setOnline(false);

		// Course with a negative value for Credit Hours
		Course courseCreditLessThanZero = new Course();
		courseCreditLessThanZero.setId("id");
		courseCreditLessThanZero.setName("Name");
		courseCreditLessThanZero.setNumCredits(-1);
		courseCreditLessThanZero.setOnline(false);

		// Add these invalid courses a NullId @ 0, NullName @ 1, LessThanZeroCreditHours
		// @ 2
		courses.add(courseIdNull);
		courses.add(courseNameNull);
		courses.add(courseCreditLessThanZero);

		// Similar to courses, build a dummy arrray of transactions and add invalid
		// records
		transactions = new ArrayList<>();

		// Build a TRansaction Date of 2/2/2017 we'll reuse a few times.
		TransactionDate dateForTestingTrans = new TransactionDate();
		dateForTestingTrans.setDay(2);
		dateForTestingTrans.setMonth(2);
		dateForTestingTrans.setYear(2017);

		// Create a transaction that is simply null
		Transaction transactionNull = null;

		// Create a transactio with an invalid type, instead of Charge or Payment, make
		// it Refund.
		Transaction transactionBadType = new Transaction();
		transactionBadType.setAmount(new BigDecimal(98.00));
		transactionBadType.setNote("This is a note");
		transactionBadType.setType("Refund");
		transactionBadType.setTransactionDate(dateForTestingTrans);

		// Create a Transaction with a negative amount
		Transaction transactionNegativeAmount = new Transaction();
		transactionNegativeAmount.setAmount(new BigDecimal(-99.00));
		transactionNegativeAmount.setNote("This is a note");
		transactionNegativeAmount.setType(Type.PAYMENT.toString());
		transactionNegativeAmount.setTransactionDate(dateForTestingTrans);

		// Create a Transaction with a null note. As per the design document in section
		// 4.3.6.3, lack of a note makes
		// the trnasaction fail validation.
		Transaction transactionNoteNull = new Transaction();
		transactionNoteNull.setAmount(new BigDecimal(99.00));
		transactionNoteNull.setNote(null);
		transactionNoteNull.setType(Type.PAYMENT.toString());
		transactionNoteNull.setTransactionDate(dateForTestingTrans);

		// Add the 4 invalid transactions to our list.
		transactions.add(transactionNull);
		transactions.add(transactionBadType);
		transactions.add(transactionNegativeAmount);
		transactions.add(transactionNoteNull);
	}

	/**
	 * Similarly, tearDown removes the entities established in setUp to prepare the class
	 * to run the test cases on any subsequent executions.
	 */
	@After
	public void tearDown() throws Exception {
		//Set the dummy instances to null
		studentRecord = null;
		transactions = null;
		courses = null;
	}

	/**
	 * TestCourseIdNull verifies a course with a Null Id throws Illegal Argument
	 * exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCourseIdNull() {

		// course(0) is the course with a null id, this should fail validation & throw
		// IllegalArgument exception.
		InputValidator.validateCourse(courses.get(0));
	}

	/**
	 * TestCourseNameNull verifies a course with a Null Name throws Illegal Argument
	 * exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCourseNameNull() {
		// course(1) is the course with a null name, this should fail validation & throw
		// IllegalArgument exception.
		InputValidator.validateCourse(courses.get(1));
	}

	/**
	 * TestCourseCreditLessThanZero verifies a course with a negative number of
	 * credit hours throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCourseCreditLessThanZero() {
		// course(2) is the course with a negative credit hour value, this should fail
		// validation & throw IllegalArgument exception.
		InputValidator.validateCourse(courses.get(2));
	}

	/**
	 * testTransactionNull verifies a transaction with a Null Id throws Illegal
	 * Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTransactionNull() {
		// transaction(0) is the a null transcation, this should fail validation & throw
		// IllegalArgument exception.
		InputValidator.validateTransaction(transactions.get(0));
	}

	/**
	 * testTransactionNull verifies a transaction with a type not in the proper
	 * enumerated range throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTransactionBadType() {
		// transaction(1) is the trans with a type=Refund, this should fail validation &
		// throw IllegalArgument exception.
		InputValidator.validateTransaction(transactions.get(1));
	}

	/**
	 * testTransactionNegativeAmount verifies a transaction with an amoutn less than
	 * 0 throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTransactionNegativeAmount() {
		// transaction(2) is the trans with a negative value for credit hours, this
		// should fail validation & throw IllegalArgument exception.
		InputValidator.validateTransaction(transactions.get(2));
	}

	/**
	 * testTransactionNoteNull verifies a transaction with a note attrbute set to
	 * Null throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTransactionNoteNull() {
		// transaction(3) is the trans with a null notea type=Refund, this should fail
		// validation & throw IllegalArgument exception.
		// lack of a note seems an odd reason to fail validation, but this matches the
		// requirement spec in section 4.3.6.3 so if there is
		// an issue it is with the requirement spec as the test correctly matches it.
		InputValidator.validateTransaction(transactions.get(3));
	}

	/**
	 * testDateInvalidDay verifies a date for a nonexistant day, like Feb 35th,
	 * throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDateInvalidDay() {
		// Simple failure test of Feb 35th 2016, should fail with Illegal Argument
		InputValidator.validateDate(2, 35, 2016);
	}

	/**
	 * testDateInvalidMonth verifies a date for a nonexistant month, like the 15th
	 * month throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDateInvalidMonth() {
		// Simple failure test of a date for the 15th month, should fail with Illegal
		// Argument
		InputValidator.validateDate(15, 15, 2016);
	}

	/**
	 * testDateInvalidYear verifies a date for a bogus year, like -16 throws Illegal
	 * Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDateInvalidYear() {
		// Simple failure test of a date with a negative year, should fail with Illegal
		// Argument
		InputValidator.validateDate(1, 15, -16);
	}

	/**
	 * testStudentRecordCourseNull verifies a student record which has no courses
	 * (NULL) will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentRecordCourseNull() {
		// A student Record with a NULL list of courses should not validate.
		studentRecord.setCourses(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordIdNull verifies a student record with a Null value for the
	 * Id will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentRecordIdNull() {
		// A student record with a null id should not validate
		studentRecord.setId(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordFirstnameNull verifies a student record with a Null value
	 * for the first name will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentRecordFirstNameNull() {
		// A student record with a null first name should not validate
		studentRecord.setFirstName(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordLastNameNull verifies a student record with a Null value for
	 * the last name will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentRecordLastNameNull() {
		// A student record with a null last name should not validate
		studentRecord.setLastName(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordPhoneNull verifies a student record with a Null value for
	 * the phone number will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentRecordPhoneNull() {
		// A student record with a null phone should not validate
		studentRecord.setPhone(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordEmailAddressNull verifies a student record with a Null value
	 * for the email address will not validate and throws Illegal Argument
	 * exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentRecordEmailNull() {
		// A student record with a null email address should not validate
		studentRecord.setEmailAddress(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordAddressCityNull verifies a student record with a Null value
	 * for the city will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentAddressCityNull() {
		// A student record with a null city should not validate
		studentRecord.setAddressCity(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordPostalCodeNull verifies a student record with a Null value
	 * for the postal code will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentAddressPostalCodeNull() {
		// A student record with a null zip/postal should not validate
		// Note, a null failure is different than a fomrat error
		studentRecord.setAddressPostalCode(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordAddressStateNull verifies a student record with a Null value
	 * for address state will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentAddressStateNull() {
		// A student record with a null city should not validate
		studentRecord.setAddressState(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordAddressStreetNull verifies a student record with a Null
	 * value for address city will not validate and throws Illegal Argument
	 * exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentAddressStreetNull() {
		// Verify student records missing a value for street fail to validate
		studentRecord.setAddressStreet(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordCollegeNull verifies a student record with a Null value for
	 * college will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentCollegeNull() {
		// A student record with a null College should not validate
		studentRecord.setCollege(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordClassStatusNull verifies a student record with a Null value
	 * for class status will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentClassStatusNull() {
		// A student record with a null Class status should not validate
		studentRecord.setClassStatus(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordInternationalStatusNull verifies a student record with a
	 * Null value for international status will not validate and throws Illegal
	 * Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentInternationalStatusNull() {
		// A student record with a null International status should not validate
		studentRecord.setInternationalStatus(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordScholarshipNull verifies a student record with a Null value
	 * for scholarship will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentScholarshipStatusNull() {
		// A student record with a null Scholarship should not validate
		studentRecord.setScholarship(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordStudyAbroadNull verifies a student record with a Null value
	 * for study abroad will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentStudyAbroadStatusNull() {
		// A student record with a null Study abroad should not validate
		studentRecord.setStudyAbroad(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordtermBeganNull verifies a student record with a Null value
	 * for termBegan will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentTermBeganNull() {
		// A student record with a null Term began should not validate
		studentRecord.setTermBegan(null);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordTermBeganSemesterNull verifies a student record with a Null
	 * value for termBegan, the attribute semester will not validate and throws
	 * Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentTermBeganSemesterNull() {
		// A student record with a null TermBegan semester should not validate
		Term term = new Term();
		term.setSemester(null);
		term.setYear(2017);
		studentRecord.setTermBegan(term);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordCapstoneEnrolledNull verifies a student record with a Null
	 * value for capstone enrolled, the semesester attribute, will not validate and
	 * throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentCapstoneEnrolledNull() {
		// A student record with a null Capstone Enrolled should not validate
		Term term = new Term();
		term.setSemester(null);
		term.setYear(2017);
		studentRecord.setCapstoneEnrolled(term);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordLongPostalCode verifies a student record with a 6 digit
	 * postal code will not validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentRecordLongPostalCode() {
		// A student record with a null Address Postal Code should not validate
		studentRecord.setAddressPostalCode("123456");
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordPhoneBadFormat verifies a student record with a phone number
	 * not in the expected format of 3 digit, 3 digit, 4 digit, will not validate
	 * and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentRecordPhoneBadFormat() {
		// A student record with bad format of phone should not validate
		studentRecord.setPhone("8888888-88-8");
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentRecordBadEmailFormat verifies a student record with a email
	 * address not in the expected format with a domain after the @ sign will not
	 * validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentRecordEmailBadFormat() {
		// A student record with a bad format of email address should not validate
		studentRecord.setEmailAddress("sdfnlsd_sdl....@");
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentClassStatusInvalid verifies a student record with a class status
	 * not in the expected enumerated values will not validate and throws Illegal
	 * Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentClassStatusInvalid() {
		// A student record with a invalid class status should not validate
		studentRecord.setClassStatus("Barbering and Cosmetology");
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentScholarshipInvalid verifies a student record with a scholarship
	 * not in the expected enumerated values will not validate and throws Illegal
	 * Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentScholarshipInvalid() {
		// A student record with a invalid scholarship should not validate
		studentRecord.setScholarship("Barbering and Cosmetology");
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentCollegeInvalid verifies a student record with a college not in the
	 * expected enumerated values will not validate and throws Illegal Argument
	 * exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentCollegeInvalid() {
		// A student record with a invalid college should not validate
		studentRecord.setCollege("Barbering and Cosmetology");
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentInternationalInvalid verifies a student record with an
	 * intenrational status not in the expected enumerated values will not validate
	 * and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentInternationalInvalid() {
		// A student record with a invalid International status should not validate
		studentRecord.setInternationalStatus("Brazil");
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentStudyAbroadInvalid verifies a student record with a study abroad
	 * attr not in the expected enumerated values will not validate and throws
	 * Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentStudyAbroadInvalid() {
		// A student record with a invalid study abroad status should not validate
		studentRecord.setStudyAbroad("Brazil");
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentTermBeganSemeseterInvalid verifies a student record with a
	 * termBegan with an invalid value in the semester attribute will not validate
	 * and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentTermBeganSemesterInvalid() {
		// A student record with a invalid semester for Term began should not validate
		Term term = new Term();
		term.setSemester("Autumn");
		term.setYear(2017);
		studentRecord.setTermBegan(term);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentCapstoneInvalid verifies a student record with a capstone with an
	 * invalid value in the semester attribute will not validate and throws Illegal
	 * Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentCapstoneInvalid() {
		// A student record with a invalid semester for Capstone term should not
		// validate
		Term term = new Term();
		term.setSemester("Autumn");
		term.setYear(2017);
		studentRecord.setCapstoneEnrolled(term);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentTermBeganYearInvalid verifies a student record with a term began
	 * with an invalid value in the year attribute will not validate and throws
	 * Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentTermBeganYearInvalid() {
		// A student record with a invalid year for Term should not validate
		Term term = new Term();
		term.setSemester(Semester.FALL.toString());
		term.setYear(12345);
		studentRecord.setTermBegan(term);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentCapstoneYearInvalid verifies a student record with a term began
	 * with an invalid value in the year attribute will not validate and throws
	 * Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentCapstoneYearInvalid() {
		// A student record with a invalid year for Capstone Term should not validate
		Term term = new Term();
		term.setSemester(Semester.FALL.toString());
		term.setYear(12345);
		studentRecord.setCapstoneEnrolled(term);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentInternationalCondition verifies a student record that violates the
	 * requirements setforth in Appendix D of the requirements document where a
	 * studnet is both International=true AND international status = ONE will not
	 * validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentInternationalCondition() {
		// A student record with conflict of international=True yet
		// IntenationalStatusNone.
		// As per the requirements document, Appendix D, this is an invalid combination.
		studentRecord.setResident(false);
		studentRecord.setInternational(true);
		studentRecord.setInternationalStatus(InternationalStatus.NONE.toString());
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentScholarshipCondition verifies a student record that violates the
	 * requirements setforth in Appendix D of the requirements document where a
	 * studnet is both resident=true AND scholarship other than NONE will not
	 * validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentScholarshipCondition() {
		// A student record with conflict of resident = true and a scholarship value
		// that is NOT equla to None.
		// As per the requirements document, Appendix D, this is an invalid combination.
		studentRecord.setInternational(false);
		studentRecord.setInternationalStatus(InternationalStatus.NONE.toString());
		studentRecord.setResident(true);
		studentRecord.setScholarship(Scholarship.WOODROW.toString());
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testStudentFreeTutionAndInternationalCondition verifies a student record that
	 * violates the requirements setforth in Appendix D of the requirements document
	 * where a studnet is both international=true AND freeTuition=true will not
	 * validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStudentFreeTuitionAndInternationalCondition() {
		// A student record with conflict of international = true and a freeTuition =
		// true.
		// As per the requirements document, Appendix D, this is an invalid combination.
		studentRecord.setResident(false);
		studentRecord.setInternational(true);
		studentRecord.setFreeTuition(true);
		InputValidator.validateStudentRecord(studentRecord);
	}

	/**
	 * testValidateNullStudentRecord verifies a student record that is null will not
	 * validate and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateNullStudentRecord() {
		InputValidator.validateStudentRecord(null);
	}

	/**
	 * testValidateNullUserId verifies a user id that is null will not validate and
	 * throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateUserNullId() {
		InputValidator.validateUserId(null);
	}

	/**
	 * testValidate verifies an empty string will not validate as a user and throws
	 * Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateEmptyId() {
		InputValidator.validateUserId("");
	}

	/**
	 * testValidateFirstname verifies an empty string will not validate as a user's
	 * first name and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUserNullFirstName() {
		User user = new User();
		user.setFirstName("");// blank in purpose
		user.setLastName("Wu");
		user.setId("123");
		user.setRole(com.usc.bill.model.Role.STUDENT);
		user.setCollege(College.ENGINEERING_AND_COMPUTING);
		InputValidator.validateUser(user);
	}

	/**
	 * testValidateLaststname verifies an empty string will not validate as a user's
	 * last name and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUserNullLastName() {
		User user = new User();
		user.setFirstName("Heiru");
		user.setLastName("");// blank in purpose
		user.setId("123");
		user.setRole(com.usc.bill.model.Role.STUDENT);
		user.setCollege(College.ENGINEERING_AND_COMPUTING);
		InputValidator.validateUser(user);
	}

	/**
	 * testUserId verifies an empty string will not validate as a user's id and
	 * throws Illegal Argument exception.
	 * 
	 * Note this is a different case of passing a valid user with an empty id to
	 * validateUser, than calling the validateUserId directly.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUserNullId() {
		User user = new User();
		user.setFirstName("Heiru");
		user.setLastName("Wu");
		user.setId("");// blank in purpose
		user.setRole(com.usc.bill.model.Role.STUDENT);
		user.setCollege(College.ENGINEERING_AND_COMPUTING);
		InputValidator.validateUser(user);
	}

	/**
	 * testUserRole verifies an empty string will not validate as a user's role and
	 * throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUserNullRole() {
		User user = new User();
		user.setFirstName("Heiru");
		user.setLastName("Wu");
		user.setId("123");
		user.setRole(null);// blank in purpose
		user.setCollege(College.ENGINEERING_AND_COMPUTING);
		InputValidator.validateUser(user);
	}

	/**
	 * testUserCollege verifies an empty string will not validate as a user's
	 * college and throws Illegal Argument exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUserNullCollege() {
		User user = new User();
		user.setFirstName("Heiru");
		user.setLastName("Wu");
		user.setId("123");
		user.setRole(com.usc.bill.model.Role.STUDENT);
		user.setCollege(null);// blank in purpose
		InputValidator.validateUser(user);
	}

	/**
	 * testNullUser verifies a null passed to validateUser throws Illegal Argument
	 * exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullUser() {
		User user = null;
		InputValidator.validateUser(user);
	}
}
