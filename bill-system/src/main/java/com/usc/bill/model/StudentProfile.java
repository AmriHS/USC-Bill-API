package com.usc.bill.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 * To model Student Profile-related information.
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */
@Entity
public class StudentProfile{
	
	/**
	 * The unique and incremental id of student profile perserved in the system for Datbase relation purpose
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	/**
	 * user for which the student profile belong to.
	 * OneToOne Annotation is required as every student has only one user-related information. 
	 * EAGER value will fetch the user along the way with student profile
	 * it is not updatable or persisted, since it is already handled by the owner of User entity
	 * @see com.usc.bill.model.User
	 */
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;

	/**
	 * a set of courses student is enrolled for
	 * ManyToMany annotation is required as every student has multiple courses and vice-verse. 
	 * EAGER value will fetch the courses along the way with student profile
	 * it is updatable and persisted in only a join table, and the ownership of student course entity handled by StudentCourse entity
	 * @see com.usc.bill.model.StudentCourse
	 */
 	@ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "student_course_association", joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"), 
    			inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id")
    			, @JoinColumn(name = "course_online", referencedColumnName = "online")})
 	private Set<StudentCourse> studentCourses;

	/**
	 * email address of student
	 */
	@Column(nullable = false) 
	private String emailAddress;

	/**
	 * phone number of student 
	 */
	@Column(nullable = false) 
	private String phone;

	/**
	 * street address of where student is residing
	 */
	@Column(nullable = false) 
	private String addressStreet;

	/**
	 * state address of where student is residing
	 */
	@Column(nullable = false) 
	private String addressState;

	/**
	 * city address of where student is residing
	 */
	@Column(nullable = false) 
	private String addressCity;

	/**
	 * postal code address of where student is residing
	 */
	@Column(nullable = false) 
	private String addressPostalCode;
	
	/**
	 * which semester student has enrolled
	 */
	@Column(nullable = false)
	private Semester semesterBegin;

	/**
	 * which year student has enrolled
	 */
	@Column(nullable = false) 
	private int yearBegin;

	/**
	 * class categorization for the student, e.g. Freshman, Sophomore, etc.
	 * @see com.usc.bill.model.ClassStatus
	 */
	@Enumerated(EnumType.STRING) 
	@Column(nullable = false)
	private ClassStatus classStatus;

	/**
	 * flag to indicate if graduate student is assistant
	 */
	@Column(nullable = false) 
	private boolean gradAssistant;
    
	/**
	 * flag to indicate if the student is an international student
	 */
	@Column(nullable = false) 
	private boolean international;
    
	/**
	 * value to indicate if the student is an international student, what type of international student they are
	 * @see com.usc.bill.model.InternationalStatus
	 */
	@Enumerated(EnumType.STRING) 
	@Column(nullable = false) 
	private InternationalStatus internationalStatus;

	/**
	 * flag to indicate if the student is a resident of South Carolina
	 */ 
	@Column
	private boolean resident;

	/**
	 * semester when the student enrolled in the capstone program
	 * @see com.usc.bill.model.Semester
	 */
	@Enumerated(EnumType.STRING) 
	@Column(nullable = true) 
	private Semester semesterCapstoneEnrolled;
	
	/**
	 * year when the student enrolled in the capstone program
	 */
	@Column
	private int yearCapstoneEnrolled;

	/**
	 * value of the scholarship, if any, the student is entitled to
	 * @see com.usc.bill.model.Scholarship
	 */
	@Enumerated(EnumType.STRING) 
	@Column(nullable = false) 
	private Scholarship scholarship;

	/**
	 * flag to indicate if entire tuition charges should be waived for the student
	 */
	@Column(nullable = false) 
	private boolean freeTuition;

	/**
	 * flag to indicate if the student is a family member of a veteran
	 */
	@Column(nullable = false) 
	private boolean veteran;

	/**
	 * value to indicate if the student is studying abroad and if so what type of abroad 
	 * @see com.usc.bill.model.StudyAbroad
	 */ 
	@Enumerated(EnumType.STRING) 
	@Column(nullable = false) 
	private StudyAbroad studyAbroad;

	/**
	 * flag to indicate if the student has health insurance through an outside 3rd party
	 */
	@Column(nullable = false) 
	private boolean outsideInsurance;

	/**
	 * flag to indicate if the student is active duty military
	 */
	@Column(nullable = false) 
	private boolean activeDuty;

	/**
	 * flag to indicate if the student is an exchange student
	 */
	@Column(nullable = false) 
	private boolean nationalStudentExchange;

	/**
	 * @return the id
	 */
	public long getId(){
		return id;
	}

	/**
	 * @return the user
	 */
	public User getUser(){
		return user;
	}

	/**
	 * @return the studentCourses
	 */
	public Set<StudentCourse> getStudentCourses(){
		return studentCourses;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress(){
		return emailAddress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone(){
		return phone;
	}

	/**
	 * @return the addressStreet
	 */
	public String getAddressStreet(){
		return addressStreet;
	}

	/**
	 * @return the addressState
	 */
	public String getAddressState(){
		return addressState;
	}

	/**
	 * @return the addressCity
	 */
	public String getAddressCity(){
		return addressCity;
	}

	/**
	 * @return the addressPostalCode
	 */
	public String getAddressPostalCode(){
		return addressPostalCode;
	}


	/**
	 * @return the semesterBegin
	 */
	public Semester getSemesterBegin(){
		return semesterBegin;
	}

	/**
	 * @return the yearBegin
	 */
	public int getYearBegin() {
		return yearBegin;
	}


	/**
	 * @return the classStatus
	 */
	public ClassStatus getClassStatus(){
		return classStatus;
	}

	/**
	 * @return the gradAssistant
	 */
	public boolean getGradAssistant(){
		return gradAssistant;
	}

	/**
	 * @return the international
	 */
	public boolean getInternational(){
		return international;
	}

	/**
	 * @return the internationalStatus
	 */
	public InternationalStatus getInternationalStatus(){
		return internationalStatus;
	}

	/**
	 * @return the resident
	 */
	public boolean getResident(){
		return resident;
	}

	

	/**
	 * @return the semesterCapstoneEnrolled
	 */
	public Semester getSemesterCapstoneEnrolled(){
		return semesterCapstoneEnrolled;
	}

	/**
	 * @return the yearCapstoneEnrolled
	 */
	public int getYearCapstoneEnrolled(){
		return yearCapstoneEnrolled;
	}

	/**
	 * @return the scholarship
	 */
	public Scholarship getScholarship(){
		return scholarship;
	}

	/**
	 * @return the freeTuition
	 */
	public boolean getFreeTuition(){
		return freeTuition;
	}

	/**
	 * @return the veteran
	 */
	public boolean getVeteran(){
		return veteran;
	}

	/**
	 * @return the studyAbroad
	 */
	public StudyAbroad getStudyAbroad(){
		return studyAbroad;
	}

	/**
	 * @return the outsideInsurance
	 */
	public boolean getOutsideInsurance(){
		return outsideInsurance;
	}

	/**
	 * @return the activeDuty
	 */
	public boolean getActiveDuty(){
		return activeDuty;
	}

	/**
	 * @return the nationalStudentExchange
	 */
	public boolean getNationalStudentExchange(){
		return nationalStudentExchange;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user){
		this.user = user;
	}

	/**
	 * @param studentCourses the studentCourses to set
	 */
	public void setStudentCourses(Set<StudentCourse> studentCourses){
		this.studentCourses = studentCourses;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress){
		this.emailAddress = emailAddress;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone){
		this.phone = phone;
	}

	/**
	 * @param addressStreet the addressStreet to set
	 */
	public void setAddressStreet(String addressStreet){
		this.addressStreet = addressStreet;
	}

	/**
	 * @param addressState the addressState to set
	 */
	public void setAddressState(String addressState){
		this.addressState = addressState;
	}

	/**
	 * @param addressCity the addressCity to set
	 */
	public void setAddressCity(String addressCity){
		this.addressCity = addressCity;
	}

	/**
	 * @param addressPostalCode the addressPostalCode to set
	 */
	public void setAddressPostalCode(String addressPostalCode){
		this.addressPostalCode = addressPostalCode;
	}

	/**
	 * @param semesterBegin the semesterBegin to set
	 */
	public void setSemesterBegin(Semester semesterBegin){
		this.semesterBegin = semesterBegin;
	}

	/**
	 * @param yearBegin the yearBegin to set
	 */
	public void setYearBegin(int yearBegin){
		this.yearBegin = yearBegin;
	}

	/**
	 * @param classStatus the classStatus to set
	 */
	public void setClassStatus(ClassStatus classStatus){
		this.classStatus = classStatus;
	}

	/**
	 * @param gradAssistant the gradAssistant to set
	 */
	public void setGradAssistant(boolean gradAssistant){
		this.gradAssistant = gradAssistant;
	}

	/**
	 * @param international the international to set
	 */
	public void setInternational(boolean international){
		this.international = international;
	}

	/**
	 * @param internationalStatus the internationalStatus to set
	 */
	public void setInternationalStatus(InternationalStatus internationalStatus){
		this.internationalStatus = internationalStatus;
	}

	/**
	 * @param resident the resident to set
	 */
	public void setResident(boolean resident){
		this.resident = resident;
	}

	/**
	 * @param semesterCapstoneEnrolled the semesterCapstoneEnrolled to set
	 */
	public void setSemesterCapstoneEnrolled(Semester semesterCapstoneEnrolled){
		this.semesterCapstoneEnrolled = semesterCapstoneEnrolled;
	}

	/**
	 * @param yearCapstoneEnrolled the yearCapstoneEnrolled to set
	 */
	public void setYearCapstoneEnrolled(int yearCapstoneEnrolled){
		this.yearCapstoneEnrolled = yearCapstoneEnrolled;
	}

	/**
	 * @param scholarship the scholarship to set
	 */
	public void setScholarship(Scholarship scholarship){
		this.scholarship = scholarship;
	}

	/**
	 * @param freeTuition the freeTuition to set
	 */
	public void setFreeTuition(boolean freeTuition){
		this.freeTuition = freeTuition;
	}

	/**
	 * @param veteran the veteran to set
	 */
	public void setVeteran(boolean veteran){
		this.veteran = veteran;
	}

	/**
	 * @param studyAbroad the studyAbroad to set
	 */
	public void setStudyAbroad(StudyAbroad studyAbroad) {
		this.studyAbroad = studyAbroad;
	}

	/**
	 * @param outsideInsurance the outsideInsurance to set
	 */
	public void setOutsideInsurance(boolean outsideInsurance){
		this.outsideInsurance = outsideInsurance;
	}

	/**
	 * @param activeDuty the activeDuty to set
	 */
	public void setActiveDuty(boolean activeDuty){
		this.activeDuty = activeDuty;
	}

	/**
	 * @param nationalStudentExchange the nationalStudentExchange to set
	 */
	public void setNationalStudentExchange(boolean nationalStudentExchange){
		this.nationalStudentExchange = nationalStudentExchange;
	}
}

