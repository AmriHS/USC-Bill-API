package com.usc.bill.dto;

import com.usc.bill.dto.Course;

import java.math.BigDecimal;
import java.util.List;

/**
 * A studentRecord is a a complex structure containing information about and associated with a student.
 * This includes simple demographic information, such as name and address, but also a series of values and flags used
 * to compute a students bill, such as what college the student is attending, whether they have a scholarship, or
 * are active duty military. Additionally the student profile contains a list of courses being taken by the student
 * and a list of transactions, i.e. payments and charges, associated with the student's record.  
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */ 

public class StudentRecord{
	/**
	 * The unique id used to identify the student.
	 */	
	private String id;

	/**
	 * The first name of the student. E.g. if the student's name is "John Doe" the first name is "John".
	 */
	private String firstName;

	/**
	 * The last name of the student. E.g. if the student's name is "John Doe" the last name is "Doe".
	 */
	private String lastName;

	/**
	 * The email address associated to the student.
	 */
	private String emailAddress;

	/**
	 * The phone number associated with the student.
	 */
	private String phone;

	/**
	 * The street portion of the address associated with the student, e.g. "123 Maple St."
	 */
	private String addressStreet;

	/**
	 * The state portion of the address associated with the student, e.g. "SC", "GA", "NY", etc.
	 */
	private String addressState;

	/**
	 * The city portion of the address associated with the student, e.g. "Columbia", "Atlanta", "New York City", etc.
	 */
	private String addressCity;

	/**
	 * The ZIP or postal code associated with the student, e.g. 29170, 10026, 60619.
	 */
	private String addressPostalCode;

	/**
	 * The college is the school which the student is attending. The value of college will be translated to a value
	 * of the enumerated type College and be restricted as such. 
	 */
	private String college;

	/**
	 * The termBegan is the time when the student started college. The value of termBegan is of type Term, e.g. Spring-2017,
	 * and will be restricted as such.
	 */
	private Term termBegan;

	/**
	 * The classStatus is the year or class of the student, e.g. Freshman, Sophomore, etc. The value will be translated to 
	 * the enumerated type ClassStatus and will be restricted as such.
	 */
	private String classStatus;

	/**
	 * gradAssistant is a Boolean flag to indicate if the student is a graduate assistant for the school
	 */
	private boolean gradAssistant;

	/**
	 * international is a Boolean flag to inidcate if the student is an International or Domestic student.
	 */
	private boolean international;

	/**
	 * internationalStatus is an attribute used to indicate if the student is an interantional student, what type of
	 * international student they are. Possible values are NONE (not an international student), SHORT_TERM, or SPONSORED.
	 */
	private String internationalStatus;

	/**
	 * resident is a Boolean flag used to indicate if the student resides in South Carolina or not. Resident students
	 * and non-resident students pay different rates for tuition and therefore this is a key attribute for bill computation.
	 */
	private boolean resident;

	/**
	 * capstoneEnrolled is the term, e.g. Spring-2017, when the student first entered the Capstone Program.
	 */
	private Term capstoneEnrolled;

	/**
	 * scholarship is the indicator of which, if any, scholarship the student is entitled to when computing the students
	 * bill. The possible valus of scholarship are limited to a known discrete set, e.g. [Athletic, Sims, Woodrow, Departmental, General, None]
	 */
	private String scholarship;

	/**
	 * freeTution is a Boolean flag indicating if all tuition charges are waived for this student.
	 */
	private boolean freeTuition;

	/**
	 * veteran is Boolean flag inidicating if the student is a member of a family of a veteran and therefore entitled to
	 * certain reductions in tution.
	 */
	private boolean veteran;

	/**
	 * studyAbroad is an indicator to whether the student is studying overseas and if so in what capacity. Values for this 
	 * field are limited to [REGULAR, COHORT, NONE].
	 */
	private String studyAbroad;

	/**
	 * outsideInsurance is a Boolean flag indicating if the student is covered by separate 3rd party health insurance, and if not
	 * that they are required to purchase health insurance through the university.
	 */
	private boolean outsideInsurance;

	/**
	 * activeDuty is a Boolean flag indicating if the student is currently serving on active duty in the US Military. Being
	 * activeDuty entitles the student to certain reductions in tuition but limit options with reguard to studying abroad.
	 */
	private boolean activeDuty;

	/**
	 * nationalStudentExchange is a Boolean flag indicating if the student is a foreign exchange student.
	 */
	private boolean nationalStudentExchange;

	/**
	 * transactions is the list of Transaction objects, i.e. payments and amounts, charges and amounts, associated with the student.
	 */
	public List<Transaction> transactions;

	/**
	 * courses is the list of Course objects, i.e. academic classes, associated with the student.
	 */
	private List<Course> courses;

	/**
	 * balance is an amount of how much student is owed to the school and vice-verse
	 */
	private BigDecimal balance;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return the addressStreet
	 */
	public String getAddressStreet() {
		return addressStreet;
	}

	/**
	 * @return the addressState
	 */
	public String getAddressState() {
		return addressState;
	}

	/**
	 * @return the addressCity
	 */
	public String getAddressCity() {
		return addressCity;
	}

	/**
	 * @return the addressPostalCode
	 */
	public String getAddressPostalCode() {
		return addressPostalCode;
	}

	/**
	 * @return the college
	 */
	public String getCollege() {
		return college;
	}

	/**
	 * @return the termBegan
	 */
	public Term getTermBegan() {
		return termBegan;
	}

	/**
	 * @return the classStatus
	 */
	public String getClassStatus() {
		return classStatus;
	}

	/**
	 * @return the gradAssistant
	 */
	public boolean getGradAssistant() {
		return gradAssistant;
	}

	/**
	 * @return the international
	 */
	public boolean getInternational() {
		return international;
	}

	/**
	 * @return the internationalStatus
	 */
	public String getInternationalStatus() {
		return internationalStatus;
	}

	/**
	 * @return the resident
	 */
	public boolean getResident() {
		return resident;
	}

	/**
	 * @return the capstoneEnrolled
	 */
	public Term getCapstoneEnrolled() {
		return capstoneEnrolled;
	}

	/**
	 * @return the scholarship
	 */
	public String getScholarship() {
		return scholarship;
	}

	/**
	 * @return the freeTuition
	 */
	public boolean getFreeTuition() {
		return freeTuition;
	}

	/**
	 * @return the veteran
	 */
	public boolean getVeteran() {
		return veteran;
	}

	/**
	 * @return the studyAbroad
	 */
	public String getStudyAbroad() {
		return studyAbroad;
	}

	/**
	 * @return the outsideInsurance
	 */
	public boolean getOutsideInsurance() {
		return outsideInsurance;
	}

	/**
	 * @return the activeDuty
	 */
	public boolean getActiveDuty() {
		return activeDuty;
	}

	/**
	 * @return the nationalStudentExchange
	 */
	public boolean getNationalStudentExchange() {
		return nationalStudentExchange;
	}

	/**
	 * @return the transaction
	 */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @return the courses
	 */
	public List<Course> getCourses() {
		return courses;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @param addressStreet the addressStreet to set
	 */
	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	/**
	 * @param addressState the addressState to set
	 */
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	/**
	 * @param addressCity the addressCity to set
	 */
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	/**
	 * @param addressPostalCode the addressPostalCode to set
	 */
	public void setAddressPostalCode(String addressPostalCode) {
		this.addressPostalCode = addressPostalCode;
	}

	/**
	 * @param college the college to set
	 */
	public void setCollege(String college) {
		this.college = college;
	}

	/**
	 * @param termBegan the termBegan to set
	 */
	public void setTermBegan(Term termBegan) {
		this.termBegan = termBegan;
	}

	/**
	 * @param classStatus the classStatus to set
	 */
	public void setClassStatus(String classStatus) {
		this.classStatus = classStatus;
	}

	/**
	 * @param gradAssistant the gradAssistant to set
	 */
	public void setGradAssistant(boolean gradAssistant) {
		this.gradAssistant = gradAssistant;
	}

	/**
	 * @param international the international to set
	 */
	public void setInternational(boolean international) {
		this.international = international;
	}

	/**
	 * @param internationalStatus the internationalStatus to set
	 */
	public void setInternationalStatus(String internationalStatus) {
		this.internationalStatus = internationalStatus;
	}

	/**
	 * @param resident the resident to set
	 */
	public void setResident(boolean resident) {
		this.resident = resident;
	}

	/**
	 * @param capstoneEnrolled the capstoneEnrolled to set
	 */
	public void setCapstoneEnrolled(Term capstoneEnrolled) {
		this.capstoneEnrolled = capstoneEnrolled;
	}

	/**
	 * @param scholarship the scholarship to set
	 */
	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	/**
	 * @param freeTuition the freeTuition to set
	 */
	public void setFreeTuition(boolean freeTuition) {
		this.freeTuition = freeTuition;
	}

	/**
	 * @param veteran the veteran to set
	 */
	public void setVeteran(boolean veteran) {
		this.veteran = veteran;
	}

	/**
	 * @param studyAbroad the studyAbroad to set
	 */
	public void setStudyAbroad(String studyAbroad) {
		this.studyAbroad = studyAbroad;
	}

	/**
	 * @param outsideInsurance the outsideInsurance to set
	 */
	public void setOutsideInsurance(boolean outsideInsurance) {
		this.outsideInsurance = outsideInsurance;
	}

	/**
	 * @param activeDuty the activeDuty to set
	 */
	public void setActiveDuty(boolean activeDuty) {
		this.activeDuty = activeDuty;
	}

	/**
	 * @param nationalStudentExchange the nationalStudentExchange to set
	 */
	public void setNationalStudentExchange(boolean nationalStudentExchange) {
		this.nationalStudentExchange = nationalStudentExchange;
	}

	/**
	 * @param transactions the transaction to set
	 */
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	/**
	 * @param courses the courses to set
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}