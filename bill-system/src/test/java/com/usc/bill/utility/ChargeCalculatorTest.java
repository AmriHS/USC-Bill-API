package com.usc.bill.utility;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.usc.bill.model.Type;
import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.College;
import com.usc.bill.model.InternationalStatus;
import com.usc.bill.model.Role;
import com.usc.bill.model.Scholarship;
import com.usc.bill.model.Semester;
import com.usc.bill.model.StudentCourse;
import com.usc.bill.model.StudentCourse.CourseId;
import com.usc.bill.model.StudentProfile;
import com.usc.bill.model.StudyAbroad;
import com.usc.bill.model.TransactionHistory;
import com.usc.bill.model.User;
import com.usc.bill.utility.ChargeCalculator;

/**
 * ChargeCalculatorTest.java Purpose: ChargeCalculatorTest is a class designed
 * to exercise the methods of the ChargeCalculator class using JUnit.
 * 
 * A variety of configurations of a Student Profile will be created and passed
 * to the Charge Calculator to verify tuition rates, fees, and final charges are
 * correct.
 * 
 * @author Wu, Alamri, Stroud
 * @version 1.0 11/29/2017
 */
public class ChargeCalculatorTest {

	static StudentProfile studentProfile1;

	// Use @Before to establish a studentProfile for use across this class. We'll
	// call her "Becky", with id of 1234.
	@Before
	public void setUp() throws Exception {
		studentProfile1 = new StudentProfile();
		User user = new User();
		user.setCollege(College.ENGINEERING_AND_COMPUTING);
		user.setFirstName("Becky");
		user.setLastName("Beckworth");
		user.setId("1234");
		user.setRole(Role.STUDENT);
		studentProfile1.setUser(user);
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
	 * Similarly, tearDown removes the entities established in setUp to prepare the
	 * class to run the test cases on any subsequent executions.
	 */
	@After
	public void tearDown() throws Exception {
		// Set the studentProfile to null
		studentProfile1 = null;
	}

	/**
	 * This test creates a transaction history for a payment of $100 and applies it
	 * to the charge calculator, since the balance is initially 0 the balance should
	 * then be -100, assert this. Then make a 2nd payment of $50 and asser the
	 * balance is now $50.
	 */
	@Test
	public void testCalculateBalance() throws Exception {
		// Create a dummy List of transaction histories.
		List<TransactionHistory> histories = new ArrayList<>();

		// Create a Transaction HIstory for a charge of $100, remember charges are
		// negative when applied to a bill.
		TransactionHistory history = new TransactionHistory();
		history.setAmount(new BigDecimal(100));
		history.setDate(new Date());
		history.setId(123);
		history.setNote("This is a note");
		history.setType(Type.CHARGE);
		history.setUser(new User());
		histories.add(history);

		// The calculated balance based on this one transaction history should be -100.
		assertEquals(new BigDecimal(-100), ChargeCalculator.calculateBalance(histories));

		// Now add a payment of $50 to the transaction history, remember payments are
		// positive when applied to a bill.
		TransactionHistory history2 = new TransactionHistory();
		history2.setAmount(new BigDecimal(50));
		history2.setDate(new Date());
		history2.setId(123);
		history2.setNote("This is a note");
		history2.setType(Type.PAYMENT);
		history2.setUser(new User());
		histories.add(history2);

		// Now the calculated balance should be (-100 + 50) = -50.
		assertEquals(new BigDecimal(-50), ChargeCalculator.calculateBalance(histories));
	}

	/**
	 * Assert that if provided with a student profile where free tuiton flag is set
	 * that calculateTution returns 0.
	 */
	@Test
	public void testCalculateTuitionWhenFree() {
		// Our dummy studentProfile has freeTuition = true, so if we calulcate Tuition
		// it should return 0.
		assertEquals(new BigDecimal(0), ChargeCalculator.calculateTuition(studentProfile1).get("FREE TUITION"));
	}

	/**
	 * Assert that if we cacluate the Technology fee for our student Becky, becasue
	 * she has 4 credit hours her fee is 4x17=$68.
	 */
	@Test
	public void testCalculateFee() {
		// Technology Fee is for our undergraduate student profile is $17 per credit
		// hour.
		// Student profile has 4 credit hours so assert this fee is 4x17=68.
		assertEquals("68.0", (ChargeCalculator.calculateFee(studentProfile1).get("TECHNOLOGY FEE").toString()));
	}

	/**
	 * Add a course to our student Becky to guarantee she is at or over 17 credit
	 * hours, and set her to be a graduate student and resident. Then exercise the
	 * charge calculator to verify she has been assigned the Graduate Resident fee
	 * for 17+ credit hour students. The amount should be $80.
	 */
	@Test
	public void testGraduateStudentResidentTotalCreditGreaterThanSEVENTEEN() {
		// Add another course to this student profile to forct it over 17 credit hours.
		Set<StudentCourse> courses = studentProfile1.getStudentCourses();
		StudentCourse courseWithHugeCreditHour = new StudentCourse();
		CourseId courseId = new CourseId();
		courseId.setId("id");
		courseId.setOnline(false);
		courseWithHugeCreditHour.setCourseId(courseId);
		courseWithHugeCreditHour.setName("name");
		courseWithHugeCreditHour.setNumCredits(17);
		courses.add(courseWithHugeCreditHour);

		// Now change the student profile to be a Graduate Student (i.e. PHD or Masters)
		// and set other attributes
		// necessary to be eligible for the special Gradaute Resident 17+ & Up fee.
		studentProfile1.setStudentCourses(courses);
		studentProfile1.setResident(true);
		studentProfile1.setInternational(false);
		studentProfile1.setClassStatus(ClassStatus.PHD);

		// Assert this fee has been applied and it's cost was $80.
		assertEquals(BigDecimal.valueOf(80.0),
				ChargeCalculator.calculateFee(studentProfile1).get("GRADUATE - RESIDENT - 17 HOURS AND ABOVE FEE"));
	}

	/**
	 * Use our student profile for Becky and increase her courses to meet or exceed
	 * 17 credit hours. Set her profile to be a gradaute but NON resident then
	 * confirm the charge calculator assigned her the graduate non resident 17+ fee.
	 * The amount should be $170.
	 */
	@Test
	public void testGraduateStudentNoneResidentTotalCreditGreaterThanSEVENTEEN() {
		// Run same test as above, meaning add a huge credit hour course to up hours
		// over 17.
		Set<StudentCourse> courses = studentProfile1.getStudentCourses();
		StudentCourse courseWithHugeCreditHour = new StudentCourse();
		CourseId courseId = new CourseId();
		courseId.setId("id");
		courseId.setOnline(false);
		courseWithHugeCreditHour.setCourseId(courseId);
		courseWithHugeCreditHour.setName("name");
		courseWithHugeCreditHour.setNumCredits(17);
		courses.add(courseWithHugeCreditHour);

		// this time though set the student profile to be a NON resident. This will
		// charge the Grad NON Resident 17+ Fee
		studentProfile1.setStudentCourses(courses);
		studentProfile1.setResident(false);
		studentProfile1.setInternational(false);
		studentProfile1.setClassStatus(ClassStatus.PHD);

		// Assert the fee was applied and the cost was $170.
		assertEquals(BigDecimal.valueOf(170.0),
				ChargeCalculator.calculateFee(studentProfile1).get("GRADUATE - NONRESIDENT - 17 HOURS AND ABOVE FEE"));
	}

	/**
	 * Using our student profile for Becky, increase her courses to be at or over 17
	 * credit hours. Set her to be an undergraduate and non resident. Verify the
	 * charge calculator has assigned her the undergrad non resident 17+ fee. The
	 * amount should be $208.
	 */
	@Test
	public void testUnderGradStudentNoneResidentTotalCreditGreaterThanSEVENTEEN() {
		// Modify dummy student to have huge hours, but be non resident & undergrad.
		Set<StudentCourse> courses = studentProfile1.getStudentCourses();
		StudentCourse courseWithHugeCreditHour = new StudentCourse();
		CourseId courseId = new CourseId();
		courseId.setId("id");
		courseId.setOnline(false);
		courseWithHugeCreditHour.setCourseId(courseId);
		courseWithHugeCreditHour.setName("name");
		courseWithHugeCreditHour.setNumCredits(17);
		courses.add(courseWithHugeCreditHour);
		studentProfile1.setStudentCourses(courses);
		studentProfile1.setResident(false);
		studentProfile1.setInternational(false);
		studentProfile1.setClassStatus(ClassStatus.JUNIOR);

		// Verify the Undergrad Non Resident 17+ fee of $208 was charged.
		assertEquals(BigDecimal.valueOf(208.0), ChargeCalculator.calculateFee(studentProfile1)
				.get("UNDERGRADUATE - NONRESIDENT - 17 HOURS AND ABOVE FEE"));
	}

	/**
	 * Using our student profile for Becky, set her credit hours to be between 6-11.
	 * Note she already has 4 credit hours from setUp so add a course with 6 more.
	 * Verify the charge calculator assigned her the Student Health Center fee for
	 * 9-11 hours and the amount should be $178.
	 */
	@Test
	public void testGradStudentNoneStudyAbroadNineToEleven() {
		Set<StudentCourse> courses = studentProfile1.getStudentCourses();
		StudentCourse courseWithHugeCreditHour = new StudentCourse();
		CourseId courseId = new CourseId();
		courseId.setId("id");
		courseId.setOnline(false);
		courseWithHugeCreditHour.setCourseId(courseId);
		courseWithHugeCreditHour.setName("name");
		courseWithHugeCreditHour.setNumCredits(6);
		courses.add(courseWithHugeCreditHour);
		studentProfile1.setStudentCourses(courses);
		studentProfile1.setResident(false);
		studentProfile1.setInternational(false);
		studentProfile1.setStudyAbroad(StudyAbroad.NONE);
		studentProfile1.setClassStatus(ClassStatus.PHD);
		assertEquals(BigDecimal.valueOf(178.0), ChargeCalculator.calculateFee(studentProfile1)
				.get("GRADUATE STUDENTS - (9 TO 11 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER"));
	}

	/**
	 * Using our student profile for Becky, assign courses to set her credit hours
	 * to 7, she already had 4 credit hours from SetUp so add a course with 3 more.
	 * Then verify the charge calculator assigned her the student health center
	 * charge for 6-8 credit hours with an amount of $119.
	 */
	@Test
	public void testGradStudentNoneStudyAbroadSixToEight() {
		// Set student record to have 7 hours (original 4 + 3 more), and make Grad
		// student.
		Set<StudentCourse> courses = studentProfile1.getStudentCourses();
		StudentCourse courseWithHugeCreditHour = new StudentCourse();
		CourseId courseId = new CourseId();
		courseId.setId("id");
		courseId.setOnline(false);
		courseWithHugeCreditHour.setCourseId(courseId);
		courseWithHugeCreditHour.setName("name");
		courseWithHugeCreditHour.setNumCredits(3);
		courses.add(courseWithHugeCreditHour);
		studentProfile1.setStudentCourses(courses);
		studentProfile1.setResident(false);
		studentProfile1.setInternational(false);
		studentProfile1.setStudyAbroad(StudyAbroad.NONE);
		studentProfile1.setClassStatus(ClassStatus.PHD);

		// Verify the rad 6-8 Hour Health Center fee of $119 was applied, study abroad
		// must be false.
		assertEquals(BigDecimal.valueOf(119.0), ChargeCalculator.calculateFee(studentProfile1)
				.get(" GRADUATE STUDENTS - (6 TO 8 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER"));
	}

	/**
	 * Using our student profile for Becky from Setup, modify her student profile to
	 * be an international student who is sponsored. Then verify the charge
	 * calculator assigns her the sponsored international fee with an amount of
	 * $250.
	 */
	@Test
	public void testGradStudentIntSponsoredCreditGreaterThanTwelve() {
		// More special fee tests. Make this student a Sponosored inteernational student
		// with more than 12 hours.
		Set<StudentCourse> courses = studentProfile1.getStudentCourses();
		StudentCourse courseWithHugeCreditHour = new StudentCourse();
		CourseId courseId = new CourseId();
		courseId.setId("id");
		courseId.setOnline(false);
		courseWithHugeCreditHour.setCourseId(courseId);
		courseWithHugeCreditHour.setName("name");
		courseWithHugeCreditHour.setNumCredits(9);
		courses.add(courseWithHugeCreditHour);
		studentProfile1.setStudentCourses(courses);
		studentProfile1.setResident(false);
		studentProfile1.setInternational(true);
		studentProfile1.setInternationalStatus(InternationalStatus.SPONSORED);

		// Verify the Sponsored Int Student fee of $250 was charged.
		assertEquals(BigDecimal.valueOf(250.0),
				ChargeCalculator.calculateFee(studentProfile1).get("SPONSORED INTERNATIONAL STUDENT FEE"));
	}

	/**
	 * Using our student profile for Becky from SetUp, modify her class status to be
	 * a graduate student of PhD and a resident, and not a Graduate Assistant.
	 * Verify her Tuition is calculated then at a rate of $533 per credit hour for 4
	 * credit hours, which equals $2,132.
	 */
	@Test
	public void testGradStudentResidentTuition() {
		// Set student to be a Grad & resident.
		studentProfile1.setClassStatus(ClassStatus.PHD);
		studentProfile1.setInternational(false);
		studentProfile1.setInternationalStatus(InternationalStatus.NONE);
		studentProfile1.setResident(true);
		studentProfile1.setGradAssistant(false);

		// Verfiy the Grad Resident Tuition fee was charge and was $2132 since it is
		// $533 per hour & he has 4 hours.
		assertEquals(BigDecimal.valueOf(2132.0),
				ChargeCalculator.calculateTuition(studentProfile1).get("GRADUATE - RESIDENT - TUITION"));
	}

	/**
	 * Using our student profile for Becky from Setup, modify her to bea
	 * non-resident and a graduate assistant. Verify when her tuition is calculated
	 * it is at a rage of the non-resident rate of $1,142, less the resident rate of
	 * $533 since she is a graduate assistant, times her 4 credit hours for a final
	 * charge of $2,436.
	 */
	@Test
	public void testGradStudentNotResidentGradAssistantTuition() {
		// configure student to be a Grad & non-resident, plus a Graduate Assistant.
		studentProfile1.setClassStatus(ClassStatus.PHD);
		studentProfile1.setResident(false);
		studentProfile1.setGradAssistant(true);

		// The tuition for a Non Resident Grad Student who is also a Grad Assistant,
		// should be calculated
		// as the Non-Resident Grad rate, less the Resident Grad rate due to the
		// Assistant postion.
		// Figure 4 hours x (1142-533) and assert.
		assertEquals(BigDecimal.valueOf(2436.0),
				ChargeCalculator.calculateTuition(studentProfile1).get("NON-RESIDENT GRADUATE ASSISTANT TUITION"));
	}

	/**
	 * Using our student profile of Becky from Setup, ensure her profile is set to a
	 * graduate student, who is non resident, who is not a graduate teaching
	 * assistant, but is studying abroad as "Regular". Then calcualte her tution and
	 * for 4 credit hours it should be $2,436.
	 */
	@Test
	public void testGradStudentNotResidentStudyAbroadTuition() {
		// Make student a Graduate & Non Resident, this time not a graduate assistant
		// but they are studying abroad.
		studentProfile1.setClassStatus(ClassStatus.PHD);
		studentProfile1.setResident(false);
		studentProfile1.setGradAssistant(false);
		studentProfile1.setStudyAbroad(StudyAbroad.REGULAR);

		// Tuition is a again the difference between Grad Non Resident & Resident Rates,
		// times credit hours. $2,346.
		assertEquals(BigDecimal.valueOf(2436.0),
				ChargeCalculator.calculateTuition(studentProfile1).get("GRADUATE - STUDY ABROAD - TUITION"));
	}

	/**
	 * Using our student profile for Becky from setUp, ensure she is a graduate
	 * student who is not a resident, not a grad assistnat, and not studying abroad.
	 * Assert the charge calculator returns the graduate non resident tution charge
	 * with an amount of $4,568 for her 4 credit hours.
	 */
	@Test
	public void testGradStudentNotResidentTuition() {
		// Make student a Graduate Student, non Resident, and no special category
		// otherwise.
		studentProfile1.setClassStatus(ClassStatus.PHD);
		studentProfile1.setResident(false);
		studentProfile1.setGradAssistant(false);
		studentProfile1.setStudyAbroad(StudyAbroad.NONE);

		// Assert straight non resident graduate rate of $1142 x 4 credit hours = $4,568
		assertEquals(BigDecimal.valueOf(4568.0),
				ChargeCalculator.calculateTuition(studentProfile1).get("GRADUATE - NONRESIDENT - TUITION"));
	}

	/**
	 * Create a course that is Online. Verify the chargeCalculator recognizes this
	 * course as Online and that the number of credit hours (3) for this course is
	 * returned.
	 */
	@Test
	public void testGetOnlineCreditHour() {
		// Because all prevous tests have only used non-online courses, add one that is
		// online=true.
		Set<StudentCourse> courses = new java.util.HashSet<>();
		StudentCourse onlineCourse = new StudentCourse();
		CourseId courseId = new CourseId();
		courseId.setId("onlineCourseId");
		courseId.setOnline(true);
		onlineCourse.setCourseId(courseId);
		onlineCourse.setName("name");
		onlineCourse.setNumCredits(3);
		courses.add(onlineCourse);

		// Assert a simple case for now that the chargeCalculator at least recognized
		// this new course to be online, and it is 3 credit hours.
		assertEquals(3, ChargeCalculator.getOnlineCreditHours(courses));
	}

	/**
	 * Using our student profil for Becky from SetUp, ensure she is a non resident
	 * graudate student who is not studying abroad and not a graduate assistant.
	 * Verify her final charges are calculated as her tuiton of $4,568 plus her
	 * technology fee of $68. Note she has 4 credit hours for these calculations.
	 */
	@Test
	public void testGetFinalCharges() {
		// Configure our dummy student to be a Graduate Student, non Res, and nothing
		// else special with regard to study abroad or assistant.
		studentProfile1.setClassStatus(ClassStatus.PHD);
		studentProfile1.setResident(false);
		studentProfile1.setGradAssistant(false);
		studentProfile1.setStudyAbroad(StudyAbroad.NONE);

		// the sum of all charges for this student should only be the tuition calculated
		// in testGradStudentNonResident of $1142x4=$4568
		// plus the technology fee we calculated in testCalculateFee, which was $17x4
		// credit hours = $68.
		// Total final charges are then $4,636.
		assertEquals(BigDecimal.valueOf(4636.0), ChargeCalculator.calculateFinalCharges(studentProfile1));
	}

	/**
	 * Attempt to calculate the tuition for a Null student profile. Verify the
	 * exception of NullPointer is returned.
	 */
	@Test(expected = NullPointerException.class)
	public void testEmptyStudentProfileCalculateTuition() {
		ChargeCalculator.calculateTuition(new StudentProfile());
	}

	/**
	 * Using our student profile for Becky from Setup, add a course with a negative
	 * number of credit hours. Verify calculate fee returns a Null value because of
	 * this.
	 */
	@Test
	public void testMinusCreditHourCalculateTuition() {
		// More special fee tests. Make this student with minus credit hours
		Set<StudentCourse> courses = studentProfile1.getStudentCourses();
		StudentCourse courseWithMinusCreditHour = new StudentCourse();
		CourseId courseId = new CourseId();
		courseId.setId("id");
		courseId.setOnline(false);
		courseWithMinusCreditHour.setCourseId(courseId);
		courseWithMinusCreditHour.setName("name");
		courseWithMinusCreditHour.setNumCredits(-9);
		courses.add(courseWithMinusCreditHour);
		studentProfile1.setStudentCourses(courses);
		studentProfile1.setResident(false);
		studentProfile1.setInternational(true);
		studentProfile1.setInternationalStatus(InternationalStatus.SPONSORED);

		// Verify the Technology fee, should be null but returned minus value
		assertNull(ChargeCalculator.calculateFee(studentProfile1).get("TECHNOLOGY FEE"));
	}

}
