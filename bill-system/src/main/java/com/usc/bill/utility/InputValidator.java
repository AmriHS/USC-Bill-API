package com.usc.bill.utility;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import com.usc.bill.dto.Course;
import com.usc.bill.dto.StudentRecord;
import com.usc.bill.dto.Transaction;
import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.College;
import com.usc.bill.model.InternationalStatus;
import com.usc.bill.model.Scholarship;
import com.usc.bill.model.Semester;
import com.usc.bill.model.StudyAbroad;
import com.usc.bill.model.Type;
import com.usc.bill.model.User;

/**
* To handle the validation of different types of input 
* @author WU, Alamri, STROUD
* @version 1.0 11/19/2017
*/
public class InputValidator {
	
	/**
	 * regex to validate email address format.
	 */
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	
	/**
	 * regex to validate phone format.
	 */
	private static final String PHONE_REGEX = "\\d{3}[-]\\d{3}[-]\\d{4}";
	
	/**
	 * regex to validate year format.
	 */
	private static final String YEAR_REGEX = "\\d{4}";

	/**
	 * to validate that the userId is not either null or empty
	 * @param userId the user ID to be validated
	 * @throws IllegalArgumentException in case is not validated
	 */
	public static void validateUserId(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User id is null");
		}
		if (userId.isEmpty()) {
			throw new IllegalArgumentException("User id is empty");
		} 
	}

	/**
	 * to validate that user attributes
	 * @param user the user object to be validated
	 * @throws IllegalArgumentException in case is not validated
	 */
	public static void validateUser(User user) {
		if (user != null){		
			if (user.getId() == null || user.getId().isEmpty() )
				throw new IllegalArgumentException("id attribute is null or empty");
			else if ( user.getFirstName() == null || user.getFirstName().isEmpty() )
				throw new IllegalArgumentException("first name attribute is null or empty");
			else if (user.getLastName() == null || user.getLastName().isEmpty()) 
				throw new IllegalArgumentException("last name attribute is null or empty");
			else if (user.getCollege() == null || user.getCollege().toString().isEmpty())
				throw new IllegalArgumentException("college attribute is null or unknown");
			else if (user.getRole() == null || user.getRole().toString().isEmpty())
				throw new IllegalArgumentException("college attribute is null or unknown");
		}
		else 
			throw new IllegalArgumentException("null");		
	}
	
   /**
	* to validate student record attributes
	* @param studentRecord studentRecord to be validated
	* @throws IllegalArgumentException in case is not validated
	*/
	public static void validateStudentRecord(StudentRecord studentRecord) {
		Boolean valid;	

		if (studentRecord == null) 
			throw new IllegalArgumentException("Student record is null");
		
		if (studentRecord.getCourses()!=null){
			for (Course course : studentRecord.getCourses()) {
				validateCourse(course);
			}
		}
		else 
			throw new IllegalArgumentException("Course attribute is null");

		if (studentRecord.getTransactions() != null){
			for (Transaction transaction : studentRecord.getTransactions())
				validateTransaction(transaction);
		}
		
		if (studentRecord.getId() == null || studentRecord.getId().isEmpty() )
			throw new IllegalArgumentException("id attribute is null or empty");
		else if ( studentRecord.getFirstName() == null || studentRecord.getFirstName().isEmpty() )
			throw new IllegalArgumentException("first name attribute is null or empty");
		else if (studentRecord.getLastName() == null || studentRecord.getLastName().isEmpty()) 
			throw new IllegalArgumentException("last name attribute is null or empty");
		else if (studentRecord.getPhone() == null || studentRecord.getPhone().isEmpty())  
			throw new IllegalArgumentException("phone attribute is null");
		else if (studentRecord.getEmailAddress() == null || studentRecord.getEmailAddress().isEmpty())
			throw new IllegalArgumentException("email address attribute is null or empty");
		else if (studentRecord.getAddressCity() == null || studentRecord.getAddressCity().isEmpty())
			throw new IllegalArgumentException("address city attribute is null or empty");
		else if (studentRecord.getAddressPostalCode() == null || studentRecord.getAddressPostalCode().isEmpty())
			throw new IllegalArgumentException("address postal code attribute is null or empty");
		else if (studentRecord.getAddressState() == null || studentRecord.getAddressState().isEmpty())
			throw new IllegalArgumentException("addreess state attribute is null or empty");
		else if (studentRecord.getAddressStreet() == null || studentRecord.getAddressStreet().isEmpty())
			throw new IllegalArgumentException("address street is null or empty");
		else if (studentRecord.getCollege() == null || studentRecord.getCollege().isEmpty())
			throw new IllegalArgumentException("college attribute is null or empty");
		else if (studentRecord.getClassStatus() == null || studentRecord.getClassStatus().isEmpty())
			throw new IllegalArgumentException("class status attribute is null or empty");
		else if (studentRecord.getInternationalStatus() == null || studentRecord.getInternationalStatus().isEmpty())
			throw new IllegalArgumentException("international status attribute is null or empty");
		else if (studentRecord.getScholarship() == null || studentRecord.getScholarship().isEmpty() )
			throw new IllegalArgumentException("scholarship status attribute is null or empty");
		else if (studentRecord.getStudyAbroad() == null || studentRecord.getStudyAbroad().isEmpty())
			throw new IllegalArgumentException("study abroad attribute is null or empty");
		else if (studentRecord.getTermBegan() == null)
			throw new IllegalArgumentException("term began attribute is null or empty");
		else if (studentRecord.getTermBegan().getSemester() == null || studentRecord.getTermBegan().getSemester().isEmpty())
			throw new IllegalArgumentException("term began semester attribute is null or empty");
		else if (studentRecord.getCapstoneEnrolled() != null){
			 if (studentRecord.getCapstoneEnrolled().getSemester() == null || studentRecord.getCapstoneEnrolled().getSemester().isEmpty())
				 	throw new IllegalArgumentException("Capstone semester is null or empty");
		}
		
		if (studentRecord.getAddressPostalCode().length() != 5) 
			throw new IllegalArgumentException("postal code attribute has improper length error");
		else if (!VALID_EMAIL_ADDRESS_REGEX.matcher(studentRecord.getEmailAddress()).find())
			throw new IllegalArgumentException("email attribute has impoper format");
		else if (!studentRecord.getPhone().matches(PHONE_REGEX)) {
			throw new IllegalArgumentException("Phone format error");
		}
		
		valid = false;
		for (ClassStatus status : ClassStatus.values()) {
			if (studentRecord.getClassStatus().equals(status.toString())) {
				valid = true;
				break;
			}
		}
		if (!valid) {
			throw new IllegalArgumentException("class status value is unrecognizable in the system");
		}
		valid = false;
		for (Scholarship scholarship : Scholarship.values()) {
			if (studentRecord.getScholarship().equals(scholarship.toString())) {
				valid = true;
				break;
			}
		}
		if (!valid) {
			throw new IllegalArgumentException("scholarship value is unrecognizable in the system");
		}
		valid = false;
		for (College college : College.values()) {
			if (studentRecord.getCollege().equals(college.toString())) {
				valid = true;
				break;
			}
		}
		if (!valid) {
			throw new IllegalArgumentException("college value is unrecognizable in the system");
		}
		valid = false;
		for (InternationalStatus internationalStatus : InternationalStatus.values()) {
			if (studentRecord.getInternationalStatus().equals(internationalStatus.toString())) {
				valid = true;
				break;
			}
		}
		if (!valid) {
			throw new IllegalArgumentException("international status value is unrecognizable in the system");
		}
		
		valid = false;
		for (StudyAbroad studyAbroad : StudyAbroad.values()) {
			if (studentRecord.getStudyAbroad().equals(studyAbroad.toString())) {
				valid = true;
				break;
			}
		}
		if (!valid) {
			throw new IllegalArgumentException("study abroad value is unrecognizable in the system");
		}

		valid = false;
		for (Semester semester : Semester.values()) {
			if (studentRecord.getTermBegan().getSemester().equals(semester.toString())) {
				valid = true;
				break;
			}
		}
		
		if (!valid) {
			throw new IllegalArgumentException("Term began semester value is unrecognizable in the system");
		}
		
		if (studentRecord.getCapstoneEnrolled() != null){
			valid = false;
			for (Semester semester : Semester.values()) {
				if (studentRecord.getCapstoneEnrolled().getSemester().equals(semester.toString())) {
					valid = true;
					break;
				}
			}
			if (!valid) {
				throw new IllegalArgumentException("capstone semester value is unrecognizable in the system");
			}
		}
		
		if (!String.valueOf(studentRecord.getTermBegan().getYear()).matches(YEAR_REGEX)) {
			throw new IllegalArgumentException("Year Began is inproper format");
		}		
		if (studentRecord.getInternational() && studentRecord.getResident()) {
			throw new IllegalArgumentException("International student can't be resident at the same time");
		}
		if (studentRecord.getInternational() && studentRecord.getInternationalStatus().equals(InternationalStatus.NONE.toString())) {
			throw new IllegalArgumentException("International student should be associated with either short-term or sponsored status");
		}
		if (!studentRecord.getScholarship().equals(Scholarship.NONE.toString()) && studentRecord.getResident()) {
			throw new IllegalArgumentException("Student with scholarship shouldn't be resident & vise-verse");
		}
		if (!studentRecord.getStudyAbroad().equals(StudyAbroad.NONE.toString()) && !studentRecord.getScholarship().equals(Scholarship.NONE.toString())) {
			throw new IllegalArgumentException("Student studying abroad shouldn't have scholarship & vise-verse");
		}
		if (studentRecord.getInternational() && (studentRecord.getFreeTuition() || studentRecord.getActiveDuty() || studentRecord.getVeteran())) {
			throw new IllegalArgumentException("International Student are not eligible for free tuition or on acive duty or member of vetern family and vise-verse");
		}
		if (!studentRecord.getStudyAbroad().equals(StudyAbroad.NONE.toString()) && studentRecord.getActiveDuty()) {
			throw new IllegalArgumentException("Student studying abroad shouldn't be on active duty and vise-verse");
		}
		if (studentRecord.getGradAssistant() && !(studentRecord.getClassStatus().equals(ClassStatus.MASTERS.toString())
				|| studentRecord.getClassStatus().equals(ClassStatus.PHD.toString()) || studentRecord.getClassStatus().equals(ClassStatus.GRADUATED.toString()))) {		
			throw new IllegalArgumentException("Undergraduate student shouldn't be Graduate assistant");
		}
		if (studentRecord.getFreeTuition() && (studentRecord.getClassStatus().equals(ClassStatus.MASTERS.toString())
				|| studentRecord.getClassStatus().equals(ClassStatus.PHD.toString()))) {
			throw new IllegalArgumentException("Graduate student is not eligible for undergraduate free tuition");
		}
		if (studentRecord.getFreeTuition() && !studentRecord.getResident()) {
			throw new IllegalArgumentException("Free tuition is not eligible for non-resident");
		}
	}

	/**
	* to validate course attributes
	* @param course course to be validated
	* @throws IllegalArgumentException in case is not validated
	*/
	public static void validateCourse(Course course){
		if (course.getId()==null || course.getId().isEmpty()) 
			throw new IllegalArgumentException("Course id is null or empty");
		if (course.getName() == null || course.getName().isEmpty())
			throw new IllegalArgumentException("Course name is null or empty");
		if (course.getNumCredits() <= 0)
			throw new IllegalArgumentException("credit hours shouldn't have a value of 0 or less");
	}

	/**
	 * to validate Transaction attributes
	 * @param transaction Transaction object to be validated
	 * @throws IllegalArgumentException in case is not validated
	 */
	public static void validateTransaction(Transaction transaction){
		if (transaction == null)
			throw new IllegalArgumentException("Transaction is null");
		else if (!transaction.getType().equals(Type.CHARGE.toString()) && !transaction.getType().equals(Type.PAYMENT.toString()))
			throw new IllegalArgumentException("Transaction type is not known");
		else if (transaction.getAmount().compareTo(new BigDecimal(0)) != 1)
			throw new IllegalArgumentException("Transaction amount is less than 0");
		else if (transaction.getNote() == null) 
			throw new IllegalArgumentException("transaction note is null");
		else 
			validateDate(transaction.getTransactionDate().month, transaction.getTransactionDate().day, transaction.getTransactionDate().year);
	}
	
	
	/**
	 * to validate month, date, and year as a valid attributes
	 * @param month month in Gregorian calendar to be validated
	 * @param day day in Gregorian calendar to be validated
	 * @param year year in Gregorian calendar to be validated
	 * @throws IllegalArgumentException in case is not validated
	 */
	public static void validateDate(int month, int day, int year){
		if (month > 12 || month < 1)
			throw new IllegalArgumentException("Invalid month");
		else if (day > 31 || day < 1)
			throw new IllegalArgumentException("Invalid day");
		else if (!String.valueOf(year).matches(YEAR_REGEX))
			throw new IllegalArgumentException("Invalid year");
		else {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddyyyy");
				String date = String.valueOf(month) + String.valueOf(day) + String.valueOf(year);
				simpleDateFormat.parse(date);
			} catch (ParseException e) {
					throw new IllegalArgumentException ("Invalid date");
			}
		}
	}

	/**
	 * Validate startDate is prior/less than endDate
	 * @param startDate the start date to be validated
	 * @param endDate the end date to be validated
	 */
	public static void validateDateRange(Date startDate, Date endDate){
		if (startDate.after(endDate))
			throw new IllegalArgumentException("Date range error");
	}
}
