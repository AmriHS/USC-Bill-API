package com.usc.bill.utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.usc.bill.dto.Bill;
import com.usc.bill.dto.Course;
import com.usc.bill.dto.StudentRecord;
import com.usc.bill.dto.Term;
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
import com.usc.bill.model.StudentCourse.CourseId;

/**
 * To handle data mapping and transformation between different type of objects, for example,
 * to translate a string value to an enumerated value for the type Semester: [SPRING, SUMMER, FALL]
 * @author WU, Alamri, Stroud
 * @version 1.0 11/25/2017
 */ 
public class DataMapper {
	/**
	 * To load static resource file to map to date/s and vice-verse
	 */
	private static Resource resource = new ClassPathResource("/semester.properties");
	
	/**
	 * semesterProps is a file of semester and year associated with start date and end dates  
	 */
	private static Properties semesterProps;
	
	/**
	 * To initialize semesterProps only once when the class is loaded by JVM
	 * @throws IOException in case the file couldn't uploaded by the system 
	 */
	static {
		try {
			semesterProps = PropertiesLoaderUtils.loadProperties(resource);
		} 
		catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Map the required variables from a student profile into a bill object. This would be the values
	 * of the student at the time the end of semester bill is generated.
	 * @param studentProfile the profile object of the student whose bill will be returned
	 * @param allTransactionHistory list of transaction history to be mapped
	 * @param currentCharges a map of current charges to be mapped
	 * @return the Bill object
	 * @see com.usc.bill.model.StudentProfile
	 * @see com.usc.bill.model.TransactionHistory
	 */
	public static Bill getBill(StudentProfile studentProfile, List<TransactionHistory> allTransactionHistory, Map<String, BigDecimal> currentCharges) {
		// map each to its counterpart in Bill object
		Bill bill = new Bill();
		List<Course> courses = getCourses(studentProfile.getStudentCourses());
		List<Transaction> transactions = getTransactions(currentCharges, allTransactionHistory);
		bill.setId(studentProfile.getUser().getId());
		bill.setFirstName(studentProfile.getUser().getFirstName());
		bill.setLastName(studentProfile.getUser().getLastName());
		bill.setPhone(studentProfile.getPhone());
		bill.setEmailAddress(studentProfile.getEmailAddress());
		bill.setAddressCity(studentProfile.getAddressCity());
		bill.setAddressPostalCode(studentProfile.getAddressPostalCode());
		bill.setAddressState(studentProfile.getAddressState());
		bill.setAddressStreet(studentProfile.getAddressStreet());
		bill.setClassStatus(studentProfile.getClassStatus().toString());
		bill.setCollege(studentProfile.getUser().getCollege().toString());
		bill.setCourses(courses);
		bill.setTransactions(transactions);
		return bill;
	}

	/**
	 * Map the required variables into a bill object.
	 * @param studentProfile the profile object of the student whose bill will be returned
	 * @param allTransactionHistory list of transaction history to be mapped
	 * @return the Bill object
	 * @see com.usc.bill.model.StudentProfile
	 * @see com.usc.bill.model.TransactionHistory
	 */
	public static Bill getBill(StudentProfile studentProfile, List<TransactionHistory> allTransactionHistory) {
		// map each to its counterpart in Bill object
		Bill bill = new Bill();
		List<Course> courses = getCourses(studentProfile.getStudentCourses());
		List<Transaction> transactions = getTransactions(allTransactionHistory);
		
		bill.setId(studentProfile.getUser().getId());
		bill.setFirstName(studentProfile.getUser().getFirstName());
		bill.setLastName(studentProfile.getUser().getLastName());
		bill.setPhone(studentProfile.getPhone());
		bill.setEmailAddress(studentProfile.getEmailAddress());
		bill.setAddressCity(studentProfile.getAddressCity());
		bill.setAddressPostalCode(studentProfile.getAddressPostalCode());
		bill.setAddressState(studentProfile.getAddressState());
		bill.setAddressStreet(studentProfile.getAddressStreet());
		bill.setClassStatus(studentProfile.getClassStatus().toString());
		bill.setCollege(studentProfile.getUser().getCollege().toString());// not found in studentprofile
		bill.setCourses(courses);
		bill.setTransactions(transactions);
		return bill;
	}

	/**
	 * Map the required variables into a bill object.
	 * @param studentProfile the profile object of the student whose bill will be returned
	 * @param currentCharges  a map of current charges to be mapped
	 * @return the Bill object
	 * @see com.usc.bill.model.StudentProfile
	 */
	public static Bill getBill(StudentProfile studentProfile, Map<String, BigDecimal> currentCharges) {
		// map each to its counterpart in Bill object
		Bill bill = new Bill();
		List<Course> courses = getCourses(studentProfile.getStudentCourses());
		List<Transaction> transactions = getTransactions(currentCharges);
		bill.setId(studentProfile.getUser().getId());
		bill.setFirstName(studentProfile.getUser().getFirstName());
		bill.setLastName(studentProfile.getUser().getLastName());
		bill.setPhone(studentProfile.getPhone());
		bill.setEmailAddress(studentProfile.getEmailAddress());
		bill.setAddressCity(studentProfile.getAddressCity());
		bill.setAddressPostalCode(studentProfile.getAddressPostalCode());
		bill.setAddressState(studentProfile.getAddressState());
		bill.setAddressStreet(studentProfile.getAddressStreet());
		bill.setClassStatus(studentProfile.getClassStatus().toString());
		bill.setCollege(studentProfile.getUser().getCollege().toString());
		bill.setCourses(courses);
		bill.setTransactions(transactions);
		return bill;
	}	

	/**
	 * Map the required variables into a studentRecord object.
	 * @param studentProfile the profile object of the student whose bill will be returned
	 * @param allTansactionHistory list of transaction history to be mapped
	 * @param currentCharges a map of current charges to be mapped
	 * @param balance a final balance of all past and current transactions
	 * @return mapped StudentRecord object
	 * @see com.usc.bill.model.StudentProfile
	 * @see com.usc.bill.model.TransactionHistory
	 */
	public static StudentRecord getStudentRecord(StudentProfile studentProfile, List<TransactionHistory> allTansactionHistory,
					Map<String, BigDecimal> currentCharges, BigDecimal balance) {
		// map each to its counterpart in StudentRecord object
		StudentRecord studentRecord = new StudentRecord();
		List<Transaction> transactions = getTransactions(currentCharges, allTansactionHistory);
		List<Course> courses = getCourses(studentProfile.getStudentCourses());

		studentRecord.setBalance(balance);	
		studentRecord.setId(studentProfile.getUser().getId());
		studentRecord.setFirstName(studentProfile.getUser().getFirstName());
		studentRecord.setLastName(studentProfile.getUser().getLastName());
		studentRecord.setPhone(studentProfile.getPhone());
		studentRecord.setEmailAddress(studentProfile.getEmailAddress());
		studentRecord.setAddressCity(studentProfile.getAddressCity());
		studentRecord.setAddressPostalCode(studentProfile.getAddressPostalCode());
		studentRecord.setAddressState(studentProfile.getAddressState());
		studentRecord.setAddressStreet(studentProfile.getAddressStreet());
		studentRecord.setActiveDuty(studentProfile.getActiveDuty());
		
		// Capstone is optional to be null
		if (studentProfile.getSemesterCapstoneEnrolled() != null && studentProfile.getYearCapstoneEnrolled() == 0){ 
			Term capstoneTerm = new Term();
			capstoneTerm.setSemester(studentProfile.getSemesterCapstoneEnrolled().toString());
			capstoneTerm.setYear(studentProfile.getYearCapstoneEnrolled());
			studentRecord.setCapstoneEnrolled(capstoneTerm);
		}
		else
			studentRecord.setCapstoneEnrolled(null);

		studentRecord.setClassStatus(studentProfile.getClassStatus().toString());
		studentRecord.setCollege(studentProfile.getUser().getCollege().toString());
		studentRecord.setCourses(courses);
		studentRecord.setFreeTuition(studentProfile.getFreeTuition());
		studentRecord.setInternational(studentProfile.getInternational());
		studentRecord.setInternationalStatus(studentProfile.getInternationalStatus().toString());
		studentRecord.setNationalStudentExchange(studentProfile.getNationalStudentExchange());
		studentRecord.setOutsideInsurance(studentProfile.getOutsideInsurance());
		studentRecord.setResident(studentProfile.getResident());
		studentRecord.setGradAssistant(studentProfile.getGradAssistant());
		studentRecord.setScholarship(studentProfile.getScholarship().toString());
		studentRecord.setStudyAbroad(studentProfile.getStudyAbroad().toString());
		studentRecord.setVeteran(studentProfile.getVeteran());

		Term termBegin = new Term();
		termBegin.setSemester(studentProfile.getSemesterBegin().toString());
		termBegin.setYear(studentProfile.getYearBegin());
		
		studentRecord.setTermBegan(termBegin);
		studentRecord.setTransactions(transactions);
		return studentRecord;
	}

	/**
	 * Map the required variables into a studentRecord object.
	 * @param studentProfile the profile object of the student whose bill will be returned
	 * @param allTansactionHistory list of transaction history to be mapped
	 * @param balance a final balance of all past and current transactions
	 * @return mapped StudentRecord
	 * @see com.usc.bill.model.StudentProfile
	 * @see com.usc.bill.model.TransactionHistory
	 */
	public static StudentRecord getStudentRecord(StudentProfile studentProfile, List<TransactionHistory> allTansactionHistory,
		 BigDecimal balance) {
		StudentRecord studentRecord = new StudentRecord();
		List<Transaction> transactions = getTransactions(allTansactionHistory);
		List<Course> courses = getCourses(studentProfile.getStudentCourses());

		studentRecord.setBalance(balance);
		studentRecord.setId(studentProfile.getUser().getId());
		studentRecord.setFirstName(studentProfile.getUser().getFirstName());
		studentRecord.setLastName(studentProfile.getUser().getLastName());
		studentRecord.setPhone(studentProfile.getPhone());
		studentRecord.setEmailAddress(studentProfile.getEmailAddress());
		studentRecord.setAddressCity(studentProfile.getAddressCity());
		studentRecord.setAddressPostalCode(studentProfile.getAddressPostalCode());
		studentRecord.setAddressState(studentProfile.getAddressState());
		studentRecord.setAddressStreet(studentProfile.getAddressStreet());
		studentRecord.setActiveDuty(studentProfile.getActiveDuty());
		
		//capstone is optional to be null
		if (studentProfile.getSemesterCapstoneEnrolled() != null && studentProfile.getYearCapstoneEnrolled() == 0){ 
			Term capstoneTerm = new Term();
			capstoneTerm.setSemester(studentProfile.getSemesterCapstoneEnrolled().toString());
			capstoneTerm.setYear(studentProfile.getYearCapstoneEnrolled());
			studentRecord.setCapstoneEnrolled(capstoneTerm);
		}
		else
			studentRecord.setCapstoneEnrolled(null);
		
		studentRecord.setClassStatus(studentProfile.getClassStatus().toString());
		studentRecord.setCollege(studentProfile.getUser().getCollege().toString());
		studentRecord.setCourses(courses);
		studentRecord.setFreeTuition(studentProfile.getFreeTuition());
		studentRecord.setInternational(studentProfile.getInternational());
		studentRecord.setInternationalStatus(studentProfile.getInternationalStatus().toString());
		studentRecord.setNationalStudentExchange(studentProfile.getNationalStudentExchange());
		studentRecord.setOutsideInsurance(studentProfile.getOutsideInsurance());
		studentRecord.setResident(studentProfile.getResident());
		studentRecord.setGradAssistant(studentProfile.getGradAssistant());
		studentRecord.setScholarship(studentProfile.getScholarship().toString());
		studentRecord.setStudyAbroad(studentProfile.getStudyAbroad().toString());
		studentRecord.setVeteran(studentProfile.getVeteran());
		
		Term termBegin = new Term();
		termBegin.setSemester(studentProfile.getSemesterBegin().toString());
		termBegin.setYear(studentProfile.getYearBegin());
		
		studentRecord.setTermBegan(termBegin);
		studentRecord.setTransactions(transactions);
		return studentRecord;
	}
	
	/**
	 * Map the required variable into a StudentProfile object.
	 * @param studentRecord object of the StudentRecord
	 * @return mapped StudentProfile
	 * @see com.usc.bill.model.StudentProfile
	 * @see com.usc.bill.dto.StudentRecord
	 */
	public static StudentProfile getStudentProfile(StudentRecord studentRecord) {
		StudentProfile studentProfile = new StudentProfile();
		User user = new User();
		Set<StudentCourse> studentCourses = getStudentCourses(studentRecord.getCourses());		
		
		user.setId(studentRecord.getId());
		user.setCollege(getCollege(studentRecord.getCollege()));
		user.setFirstName(studentRecord.getFirstName());
		user.setLastName(studentRecord.getLastName());
		user.setRole(Role.STUDENT);
		studentProfile.setUser(user);
		
		studentProfile.setPhone(studentRecord.getPhone());
		studentProfile.setEmailAddress(studentRecord.getEmailAddress());
		studentProfile.setAddressCity(studentRecord.getAddressCity());
		studentProfile.setAddressPostalCode(studentRecord.getAddressPostalCode());
		studentProfile.setAddressState(studentRecord.getAddressState());
		studentProfile.setAddressStreet(studentRecord.getAddressStreet());
		studentProfile.setActiveDuty(studentRecord.getActiveDuty());
		
		
		if (studentRecord.getCapstoneEnrolled() != null){
			studentProfile.setSemesterCapstoneEnrolled(getSemester(studentRecord.getCapstoneEnrolled().getSemester()));
			studentProfile.setYearCapstoneEnrolled(studentRecord.getCapstoneEnrolled().getYear());
		}
		else{
			studentProfile.setSemesterCapstoneEnrolled(null);
			studentProfile.setYearCapstoneEnrolled(0);
		}

		studentProfile.setClassStatus(getClassStatus(studentRecord.getClassStatus()));
		
		studentProfile.setStudentCourses(studentCourses);
		studentProfile.setFreeTuition(studentProfile.getFreeTuition());
		studentProfile.setGradAssistant(studentRecord.getGradAssistant());
		studentProfile.setInternational(studentRecord.getInternational());
		studentProfile.setInternationalStatus(
				getInternationalStatus(studentRecord.getInternationalStatus()));
		studentProfile.setNationalStudentExchange(studentRecord.getNationalStudentExchange());
		studentProfile.setOutsideInsurance(studentRecord.getOutsideInsurance());
		studentProfile.setResident(studentRecord.getResident());
		
		studentProfile.setScholarship(getScholarship(studentRecord.getScholarship()));
		studentProfile.setStudyAbroad(getStudyAbroad(studentRecord.getStudyAbroad()));
		
		Term termBegin = studentRecord.getTermBegan();
		studentProfile.setSemesterBegin(getSemester(termBegin.getSemester()));
		studentProfile.setYearBegin(termBegin.getYear());
		
		studentProfile.setVeteran(studentRecord.getVeteran());
		return studentProfile;
	}

	/**
	 * Map the required variables into a Transaction object
	 * @param currentCharges a map of current charges
	 * @param allTransactionHistory list of transaction history to be mapped
	 * @return list of Transaction objects
	 */
	public static List<Transaction> getTransactions(Map<String, BigDecimal> currentCharges, List<TransactionHistory> allTransactionHistory) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		Date currentDate = new Date();
		// Current charges block
		currentCharges.forEach((key, charge) -> {
			Transaction t = new Transaction();
			t.setType(Type.CHARGE.toString());
			t.setTransactionDate(getTransactionDate(currentDate));
			t.setNote(key);
			t.setAmount(charge);
			transactions.add(t);
		});

		transactions.addAll(getTransactions(allTransactionHistory));
		return transactions;
	}

	/**
	 * Map the required variables into a list of Transaction.
	 * @param currentCharges a map of current charges
	 * @return list of Transaction objects
	 * @see com.usc.bill.dto.Transaction
	 */
	public static List<Transaction> getTransactions(Map<String, BigDecimal> currentCharges) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		Date currentDate = new Date();
		
		// iterate over map of charges and add them into the list
		currentCharges.forEach((key, charge) -> {
			Transaction t = new Transaction();
			t.setType(Type.CHARGE.toString());
			t.setTransactionDate(getTransactionDate(currentDate));
			t.setNote(key);
			t.setAmount(charge);
			transactions.add(t);
		});
		return transactions;
	}
	
	/**
	 * Map the required variables into a list of Transaction.
	 * @param allTransactionHistory history of transactions
	 * @return the Transaction object
	 * @see com.usc.bill.model.TransactionHistory
	 * @see com.usc.bill.dto.Transaction
	 */
	public static List<Transaction> getTransactions(List<TransactionHistory> allTransactionHistory){
		List<Transaction> transactions = new ArrayList<Transaction>();

		// iterate over list of transactionHistory and add it to list of transaction
		allTransactionHistory.forEach((transactionHistory) -> {
			Transaction t = new Transaction();
			t.setType(transactionHistory.getType().toString());	
			
			TransactionDate transactionDate = getTransactionDate(transactionHistory.getDate());
			t.setTransactionDate(transactionDate);
			
			t.setAmount(transactionHistory.getAmount());
			t.setNote(transactionHistory.getNote());
			transactions.add(t);
		});
	
		return transactions;
	}

	/**
	 * to map Date object to TreansactionDate object
	 * @param date the data object to be map
	 * @return the Date object
	 * @see com.usc.bill.dto.TransactionDate
	 */
	public static TransactionDate getTransactionDate(Date date) {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    
		TransactionDate tDate = new TransactionDate();
		tDate.setMonth(cal.get(Calendar.MONTH)+1); // Month start at 0 not 1, so to get the right month value adding 1 will set the intended value
		tDate.setDay(cal.get(Calendar.DAY_OF_MONTH)); 
		tDate.setYear(cal.get(Calendar.YEAR));
		return tDate;
	}

	/**
	 * to map TreansactionDate object to Date object
	 * @param transactionDate the TransactionDate object to extract desired variables
	 * @return the Date object
	 * @see com.usc.bill.dto.TransactionDate
	 */
	public static Date getDate(TransactionDate transactionDate) {
		Calendar c = Calendar.getInstance();
		// Month start at 0 not 1, so to get the right month value adding 1 will set the intended value
		c.set(transactionDate.getYear(), transactionDate.getMonth()-1, transactionDate.getDay(), 0, 0);
		return c.getTime();
	}

	/**
	 * to determine whether the current semester is within the range of given startDate and endDate
	 * @param startDate input data's start date
	 * @param endDate input data's end date
	 * @return a list of term objects
	 */
	public static boolean isCurrentTermInBetween(Date startDate, Date endDate) {		
		try {
			for(String key : semesterProps.stringPropertyNames()) {
				  String value = semesterProps.getProperty(key);
				  String []dates = value.split("-");
				  SimpleDateFormat formate = new SimpleDateFormat("MMddyyyy");
				  Date currentDate = new Date();
				  
				  if ((currentDate.after(formate.parse(dates[0])) && currentDate.before(formate.parse(dates[1]))) || 
						  currentDate.equals(formate.parse(dates[0])) || currentDate.equals(formate.parse(dates[1]))){
					  	  
					  if((formate.parse(dates[0]).after(startDate) && formate.parse(dates[1]).before(endDate)) || 
							(formate.parse(dates[0]).after(startDate) && formate.parse(dates[0]).before(endDate)) || 
					  		formate.parse(dates[0]).equals(startDate) || formate.parse(dates[1]).equals(startDate) || 
					  		formate.parse(dates[0]).equals(endDate) || formate.parse(dates[1]).equals(endDate))
								return true;  
				  }		  				  
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * to map each JSON user object in user object, and eventually return a list of them 
	 * @param usersJson the users in JSON array to be extracted
	 * @return a list of StudentRecords objects
	 * @see com.usc.bill.model.User
	 */
	public static List<User> getAllUsers(JSONArray usersJson) {
		List<User> users = new ArrayList<User>();
		for (Object o : usersJson) {
			if (o instanceof JSONObject) {
				JSONObject userObj = (JSONObject)o;
				User user = new User();
				user.setId(userObj.get("id").toString());
				user.setFirstName(userObj.get("firstName").toString());
				user.setLastName(userObj.get("lastName").toString());
				
				if (userObj.get("role").toString().equals(Role.STUDENT.toString())) {
					user.setRole(Role.STUDENT);
				} else if (userObj.get("role").toString()
						.equals(Role.ADMIN.toString())) {
					user.setRole(Role.ADMIN);	
				}				
				user.setCollege(getCollege(userObj.get("college").toString()));
		
				users.add(user);
			}
		}
		return users;
	}

	/**
	 * to map each JSON student object in StudentRecords object, and eventually return a list of them 
	 * @param studentsJson student record in JSON array to be extracted
	 * @return a list of StudentRecord objects
	 * @see com.usc.bill.dto.StudentRecord
	 */
	public static List<StudentRecord> getAllStudentRecords(JSONArray studentsJson) {
		List<StudentRecord> studentRecordList = new ArrayList<StudentRecord>();
		for (Object obj : studentsJson) {
			if (obj instanceof JSONObject) {
				JSONObject StudRecordObj = (JSONObject) obj;
				StudentRecord studentRecord = new StudentRecord();
				JSONObject studObj = (JSONObject) StudRecordObj.get("student");
				studentRecord.setId(studObj.get("id").toString());
				studentRecord.setFirstName(studObj.get("firstName").toString());
				studentRecord.setLastName(studObj.get("lastName").toString());
				studentRecord.setCollege(StudRecordObj.get("college").toString());
				studentRecord.setPhone(studObj.get("phone").toString());
				studentRecord.setEmailAddress(studObj.get("emailAddress").toString());
				studentRecord.setAddressCity(studObj.get("addressCity").toString());
				studentRecord.setAddressState(studObj.get("addressState").toString());
				studentRecord.setAddressStreet(studObj.get("addressStreet").toString());
				studentRecord.setAddressPostalCode(studObj.get("addressPostalCode").toString());

				// termBegan
				Term termBegan = new Term();
				termBegan.setSemester(((JSONObject)StudRecordObj.get("termBegan")).get("semester").toString());
				termBegan.setYear(Integer.parseInt(((JSONObject)StudRecordObj.get("termBegan")).get("year").toString()));
				studentRecord.setTermBegan(termBegan);				

				// Capstone enrolled term
				if (StudRecordObj.get("capstoneEnrolled") != null){
					Term termCapstoneEnrolled = new Term();
					termCapstoneEnrolled.setSemester(((JSONObject)StudRecordObj.get("capstoneEnrolled")).get("semester").toString());				
					termCapstoneEnrolled.setYear(Integer.parseInt(((JSONObject)StudRecordObj.get("capstoneEnrolled")).get("year").toString()));
					studentRecord.setCapstoneEnrolled(termCapstoneEnrolled);
				}
				else{
					studentRecord.setCapstoneEnrolled(null);
				}
				
				studentRecord.setActiveDuty(Boolean.valueOf(StudRecordObj.get("activeDuty").toString()));
				studentRecord.setGradAssistant(Boolean.valueOf(StudRecordObj.get("gradAssistant").toString()));
				studentRecord.setInternational(Boolean.valueOf(StudRecordObj.get("international").toString()));
				studentRecord.setResident(Boolean.valueOf(StudRecordObj.get("resident").toString()));
				studentRecord.setVeteran(Boolean.valueOf(StudRecordObj.get("veteran").toString()));
				studentRecord.setFreeTuition(Boolean.valueOf(StudRecordObj.get("freeTuition").toString()));
				studentRecord.setNationalStudentExchange(
						Boolean.valueOf(StudRecordObj.get("nationalStudentExchange").toString()));
				studentRecord.setOutsideInsurance(Boolean.valueOf(StudRecordObj.get("outsideInsurance").toString()));
				studentRecord.setClassStatus(StudRecordObj.get("classStatus").toString());
				studentRecord.setInternationalStatus(StudRecordObj.get("internationalStatus").toString());				
				studentRecord.setScholarship(StudRecordObj.get("scholarship").toString());
				studentRecord.setStudyAbroad(StudRecordObj.get("studyAbroad").toString());
				
				JSONArray coursesArray = (JSONArray) StudRecordObj.get("courses");
				List<Course> courseList = new ArrayList<Course>();
				
				// extract and map courses
				if (coursesArray != null){
					for (Object nestedObj : coursesArray) {
						if (nestedObj instanceof JSONObject) {
							JSONObject courseObj = (JSONObject) nestedObj;
							Course course = new Course();
							course.setId(((JSONObject) courseObj).get("id").toString());
							course.setOnline(Boolean.valueOf(((JSONObject) courseObj).get("name").toString()));
							course.setName(((JSONObject) courseObj).get("name").toString());
							course.setNumCredits(Integer.parseInt(((JSONObject) courseObj).get("numCredits").toString()));
							courseList.add(course);
						}
					}
				}
				studentRecord.setCourses(courseList);
				
				//extract and map transactions				
				List<Transaction> transactionList = new ArrayList<Transaction>();
				JSONArray transactionsArray = (JSONArray) ((JSONObject) StudRecordObj).get("transactions");
				if (transactionsArray != null){
					for (Object tranArrObj : transactionsArray){
						if (tranArrObj instanceof JSONObject){
							JSONObject transObj = (JSONObject) tranArrObj;
							Transaction transaction = new Transaction();	
							Type trType = transObj.get("type").toString().equals(Type.PAYMENT.toString())? Type.PAYMENT : Type.CHARGE;
							transaction.setType(trType.toString());
								
							JSONObject transDateObj = (JSONObject) transObj.get("transactionDate");
							int day = Integer.parseInt(transDateObj.get("day").toString());
							int month = Integer.parseInt(transDateObj.get("month").toString());
							int year = Integer.parseInt(transDateObj.get("year").toString());							
							TransactionDate transactionDate = new TransactionDate();
							transactionDate.setDay(day);
							transactionDate.setMonth(month);
							transactionDate.setYear(year);
								
							transaction.setTransactionDate(transactionDate);
							transaction.setAmount(new BigDecimal(Double.parseDouble(transObj.get("amount").toString())));
							String note = (transObj.get("note") ==null)?"":transObj.get("note").toString();
							transaction.setNote(note);
								
							transactionList.add(transaction);
						}
					}
				}
				studentRecord.setTransactions(transactionList);			
				
				studentRecordList.add(studentRecord);
			}
		}
		return studentRecordList;
	}

	/**
	 * to map each JSON transaction object in TransactionHistory object, and eventually return a list of them 
	 * @param studentsJson the student's transactions in JSON array to be extracted
	 * @return a list of ChargeHistory objects
	 * @see com.usc.bill.model.TransactionHistory
	 * @see com.usc.bill.dto.Transaction
	 */
	public static List<TransactionHistory> getAllTransactionHistory(JSONArray studentsJson) {
		List<TransactionHistory> transactionHistoryList = new ArrayList<>();
		for (Object studentObj : studentsJson) {
			if(studentObj instanceof JSONObject){
				JSONArray transactionsArray = (JSONArray) ((JSONObject) studentObj).get("transactions");
				JSONObject jsonObject =(JSONObject) ((JSONObject) studentObj).get("student");
				String userId = jsonObject.get("id").toString();
				if (transactionsArray != null){
					for (Object obj : transactionsArray){
						if (obj instanceof JSONObject){
							JSONObject transaction = (JSONObject) obj;
							TransactionHistory transactionHistory = new TransactionHistory();	
							Type trType = transaction.get("type").toString().equals(Type.PAYMENT.toString())? Type.PAYMENT : Type.CHARGE;
							transactionHistory.setType(trType);
							
							JSONObject transDateObj = (JSONObject) transaction.get("transactionDate");
							int day = Integer.parseInt(transDateObj.get("day").toString());
							int month = Integer.parseInt(transDateObj.get("month").toString());
							int year = Integer.parseInt(transDateObj.get("year").toString());							
							Date date = getDate(month, day, year);
								
							transactionHistory.setDate(date);
							transactionHistory.setAmount(new BigDecimal(Double.parseDouble(transaction.get("amount").toString())));
							String note = (transaction.get("note") ==null)?"":transaction.get("note").toString();
							transactionHistory.setNote(note);
							
							User user = new User();
							user.setId(userId);
							transactionHistory.setUser(user);
							transactionHistoryList.add(transactionHistory);
						}
					}
				}
			}
		}
		return transactionHistoryList;
	}

	/**
	 * to map a list of StudentCourse into a set of Course
	 * @param studentCourses a list of studentCourse
	 * @return a list of Course
	 * @see com.usc.bill.model.StudentCourse
	 * @see com.usc.bill.dto.Course
	 */
	public static List<Course> getCourses(Set<StudentCourse> studentCourses){
		List<Course> courses = new ArrayList<Course>();
		for (StudentCourse s : studentCourses) {
				Course course = new Course();
				course.setId(s.getCourseId().getId());
				course.setOnline(s.getCourseId().getOnline());
				course.setName(s.getName());
				course.setNumCredits(s.getNumCredits());
				courses.add(course);
		}
		return courses;
	}
	
	/**
	 * to map a list of Course into a set of studentCourse
	 * @param Courses a list of Course
	 * @return a list of StudentCourse
	 * @see com.usc.bill.model.StudentCourse
	 * @see com.usc.bill.dto.Course
	 */
	public static Set<StudentCourse> getStudentCourses(List<Course> Courses){
		Set<StudentCourse> studentCourses = new HashSet<StudentCourse>();
		for (Course c : Courses) {
				StudentCourse studentCourse = new StudentCourse();
				CourseId courseId = new CourseId();
				courseId.setId(c.getId());
				courseId.setOnline(c.getOnline());
				
				studentCourse.setCourseId(courseId);
				studentCourse.setName(c.getName());
				studentCourse.setNumCredits(c.getNumCredits());
				studentCourses.add(studentCourse);
		}
		return studentCourses;
	}
	
	/**
	 * to get Term from provided date object
	 * @param date input date object
	 * @return term object
	 * @see com.usc.bill.dto.Term
	 */
	public static Term getTerm(Date date) {
		try {
			for(String key : semesterProps.stringPropertyNames()) {
				  String value = semesterProps.getProperty(key);
				  String []dates = value.split("-");
				  SimpleDateFormat formate = new SimpleDateFormat("MMddyyyy");
				  Date startDate = formate.parse(dates[0]);
				  Date endDate = formate.parse(dates[1]);

				  if ((date.after(startDate) && date.before(endDate)) || date.equals(startDate) 
					|| date.equals(endDate)){
						Term term = new Term();
						String [] semesterYear = key.split("_");
						term.setYear(Integer.parseInt(semesterYear[1]));
						term.setSemester(semesterYear[0]);
						return term;
				  }
			}
		
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * to get start and end date of given semester and year
	 * @param semester input semester
	 * @param year input year
	 * @return a start date and end date
	 */
	public static Date[] getStartDateAndEndDate(String semester, int year) {
		try {
			for(String key : semesterProps.stringPropertyNames()) {
				  String [] semesterYear = key.split("_");
				  if (semesterYear[0].equals(semester) && Integer.parseInt(semesterYear[1]) == year){
					  String value = semesterProps.getProperty(key);
					  String []dates = value.split("-");
					  SimpleDateFormat formate = new SimpleDateFormat("MMddyyyy");
					  Date startDate = formate.parse(dates[0]);
					  Date endDate = formate.parse(dates[1]);
					  return new Date [] {startDate, endDate};  
				  }
			}		
		}
		catch (ParseException e) {
			e.printStackTrace();
		}		
		return null;
	}

	/**
	 * to map month, day, year into a Date object
	 * @param month month in Gregorian calendar
	 * @param day day in Gregorian calendar
	 * @param year year in Gregorian calendar
	 * @return a Date object
	 */
	public static Date getDate(int month, int day, int year){
		Calendar c = Calendar.getInstance();
		c.set(year, month-1, day, 0, 0); // Calendar start month with 0
		return c.getTime();
	}
	
	/**
	 * to determine the college object from input string
	 * @param college input college string 
	 * @return a college object
	 * @see com.usc.bill.model.College
	 */
	public static College getCollege(String college){
		if (college.equals(College.ARTS_AND_SCIENCES.toString())) {
			return College.ARTS_AND_SCIENCES;
		} else if (college.toString()
				.equals(College.ENGINEERING_AND_COMPUTING.toString())) {
			return College.ENGINEERING_AND_COMPUTING;
		} else if (college.equals(College.GRADUATE_SCHOOL.toString())) {
			return College.GRADUATE_SCHOOL;
		}
		else 
			return null;
	}
	
	/**
	 * to determine the internationalStatus object from input string
	 * @param internationalStatus input internationalStatus string 
	 * @return a internationalStatus object
	 * @see com.usc.bill.model.InternationalStatus
	 */
	public static InternationalStatus getInternationalStatus(String internationalStatus){
		if (internationalStatus.equals(InternationalStatus.SHORT_TERM.toString()))
			return InternationalStatus.SHORT_TERM;
		else if (internationalStatus.equals(InternationalStatus.SPONSORED.toString()))
			return InternationalStatus.SPONSORED;
		else if (internationalStatus.equals(InternationalStatus.NONE.toString()))
			return InternationalStatus.NONE;
		else
			return null;
	}
	
	/**
	 * to determine the semester object from input string
	 * @param semester input semester string 
	 * @return a semester object
	 * @see com.usc.bill.model.Semester
	 */
	public static Semester getSemester(String semester){
		if (semester.equals(Semester.SPRING.toString()))
			return Semester.SPRING;
		else if (semester.equals(Semester.SUMMER.toString()))
			return Semester.SUMMER;
		else if (semester.equals(Semester.FALL.toString()))
			return Semester.FALL;
		else 
			return null;
	}
	
	/**
	 * to determine the classStatus object from input string
	 * @param classStatus input classStatus string 
	 * @return a classStatus object
	 * @see com.usc.bill.model.ClassStatus
	 */
	public static ClassStatus getClassStatus(String classStatus){
		if (classStatus.equals(ClassStatus.FRESHMAN.toString()))
			return ClassStatus.FRESHMAN;
		else if (classStatus.equals(ClassStatus.SOPHOMORE.toString()))
			return ClassStatus.SOPHOMORE;
		else if (classStatus.equals(ClassStatus.JUNIOR.toString()))
			return ClassStatus.JUNIOR;
		else if (classStatus.equals(ClassStatus.SENIOR.toString()))
			return ClassStatus.SENIOR;
		else if (classStatus.equals(ClassStatus.MASTERS.toString())) 
			return ClassStatus.MASTERS;
		else if (classStatus.equals(ClassStatus.PHD.toString())) 
			return ClassStatus.PHD;
		else if (classStatus.equals(ClassStatus.GRADUATED.toString()))
			return ClassStatus.GRADUATED;
		else 
			return null;
	}
	
	/**
	 * to determine the scholarship object from input string
	 * @param scholarship input scholarship string 
	 * @return a scholarship object
	 * @see com.usc.bill.model.Scholarship
	 */
	public static Scholarship getScholarship(String scholarship){
		if (scholarship.equals(Scholarship.ATHLETIC.toString()))
			return Scholarship.ATHLETIC;
		else if (scholarship.equals(Scholarship.DEPARTMENTAL.toString()))
			return Scholarship.DEPARTMENTAL;
		else if (scholarship.equals(Scholarship.GENERAL.toString()))
			return Scholarship.GENERAL;
		else if (scholarship.equals(Scholarship.NONE.toString()))
			return Scholarship.NONE;
		else if (scholarship.equals(Scholarship.SIMS.toString()))
			return Scholarship.SIMS;
		else if (scholarship.equals(Scholarship.WOODROW.toString()))
			return Scholarship.WOODROW;
		else 
			return null;
	}
	
	/**
	 * to determine the studyAbroad object from input string
	 * @param studyAbroad input studyAbroad string 
	 * @return a studyAbroad object
	 * @see StudyAbroad
	 */
	public static StudyAbroad getStudyAbroad(String studyAbroad){
		if (studyAbroad.equals(StudyAbroad.COHORT.toString()))
			return StudyAbroad.COHORT; 
		else if (studyAbroad.equals(StudyAbroad.NONE.toString()))
			return StudyAbroad.NONE;
		else if (studyAbroad.equals(StudyAbroad.REGULAR.toString()))
			return StudyAbroad.REGULAR;
		else 
			return null;
	}
}