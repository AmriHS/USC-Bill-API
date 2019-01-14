package com.usc.bill.service;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usc.bill.dto.Bill;
import com.usc.bill.dto.Term;
import com.usc.bill.exception.NoBillExistedException;
import com.usc.bill.exception.NoStudentProfileFoundException;
import com.usc.bill.exception.UserNotFoundException;
import com.usc.bill.exception.UserPermissionException;
import com.usc.bill.model.StudentProfile;
import com.usc.bill.model.TransactionHistory;
import com.usc.bill.model.Type;
import com.usc.bill.model.User;
import com.usc.bill.repository.TransactionHistoryRepository;
import com.usc.bill.utility.ChargeCalculator;
import com.usc.bill.utility.DataMapper;


/**
 * A common interface to handle business operations that concerns generating charges and applying payment for student within the scope of the current requirement
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
*/ 
@Service
public class BillServiceImpl implements BillService{
	/**
	 * instance of UserManagmentService in order to assist in user-related bussiness data operations
	 */
	@Autowired
	public UserManagmentService userManagementService;

	/**
	 * instance of StudentManagementService in order to assist in Student-related bussiness data operations
	 */
	@Autowired
	public StudentManagementService studentManagementService;

	/**
	 * instance of TransactionHistoryRepository in order to assist in Transaction-related data access operations
	 */
	@Autowired
	public TransactionHistoryRepository transactionHistoryRepository;
	
	/**
	 * to load transactions contained in student record file that is formated as a JSON into the system datasource 
	 * @param filename name of file where the record of transaction existed
	 * @throws NullPointerException if the file associated with a given filename doesn't exist
	 * @throws URISyntaxException if the given filename has improper format  
	 * @throws ParseException if the file associated with a given filename couldn't be parsed into JSONArray
	 * @throws NoStudentProfileFoundException if student with user id associated with transaction doesn't exist
	 */
	@Transactional
	public void loadTransactions(String filename) throws URISyntaxException, NullPointerException, ParseException, NoStudentProfileFoundException {
		try{
        	JSONParser parser = new JSONParser(); // declate an object of JSON parser
        	File file = new File(this.getClass().getResource("/"+filename+".txt").toURI()); // convert the given filename to class path URL
        	Object obj = parser.parse(new FileReader(file)); // read the data
        	JSONArray jsonArray = (JSONArray) obj; // declare an object of JSON array, assuming the data is always in Array format
        	List<TransactionHistory> allTransactionHistory= DataMapper.getAllTransactionHistory(jsonArray); // get list of transaction history by extracting them from json array through DataMapper       	
			// iterate over list of transaction history
        	for(TransactionHistory t: allTransactionHistory){
	        	User user = studentManagementService.getStudentProfile(t.getUser().getId()).getUser(); //  // get the student associated with user id of transaction history through service  
        		t.setUser(user);
	        	transactionHistoryRepository.save(t); //transaction history to be each of them persisted in the system
        	}
		}
		catch(NullPointerException ex){ // if the file doesn't exist 
        	throw new NullPointerException("File "+filename+" doesn't exist in the system resources"); 
		}
        catch(ParseException ex){// if the file couldn't be parsed into JSONArray
        	throw new ParseException(0, "File "+filename+" can't be parsed");
        }
		catch (IOException e){ // otherwise handle any unexpected I/O exceptional occurance
			e.printStackTrace();
		}
	}

	/**
	 * to generate charges by the end of semester and calculate final amount in order to be preserved in the system for future record
	 * @param studentProfile a profile where the charge will be calculated
	 */
	@Transactional
	public void generateEndTermCharge(StudentProfile studentProfile){
		BigDecimal finalCharge = ChargeCalculator.calculateFinalCharges(studentProfile);
		if (finalCharge.doubleValue()<=0) return;
		
		Term currentTerm = DataMapper.getTerm(new Date()); 
		TransactionHistory transactionHistory = new TransactionHistory(); 
		transactionHistory.setUser(studentProfile.getUser()); 
		transactionHistory.setType(Type.CHARGE);
		transactionHistory.setAmount(finalCharge);
		transactionHistory.setDate(new Date());
		transactionHistory.setNote("Final total "+currentTerm.getSemester()+" "+currentTerm.getYear());			
		transactionHistoryRepository.save(transactionHistory);
	}
	
	/**
	 * to get the charges and payment made in the current semester presented in Bill object
	 * @param userId an id of user corresponding to student profile that 
	 * @return bill consisted of student of profile along wiht charges, payment of current semester
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @throws NoBillExistedException if there is no bill existed for the given user id
	 * @throws UserPermissionException if the logged-in user has no permission accessing given id of user
	 * @throws UserNotFoundException if no user is logged-in
     * @see com.usc.bill.dto.Bill
	 */
	@Transactional
	public Bill getCurrentBill(String userId) throws NoStudentProfileFoundException, UserPermissionException, UserNotFoundException, NoBillExistedException {
		// if the user associated with student profile doesn't exist
		if(userManagementService.getLoggedInUserId()==null) 
			throw new UserNotFoundException("No user currently is logged in");
		
		StudentProfile studentProfile = studentManagementService.getStudentProfile(userId);	
		// check whether the logged-in user has an access to the student profile
		if (!userManagementService.checkAccess(studentProfile))
			throw new UserPermissionException("Logged in user has no permission to access user ID "+userId);
		
		// retrieve the current term by given the current data through DataMapper
		Term currentTerm = DataMapper.getTerm(new Date()); 
		Date [] startEndCurrentDates = DataMapper.getStartDateAndEndDate(currentTerm.getSemester(), currentTerm.getYear()); // retrieve the start and end date of current semester by given the current term through DataMapper
		Bill bill = null; 
		List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.
		findByUserIdAndDateBetween(userId, startEndCurrentDates[0], startEndCurrentDates[1]); 
		Map<String, BigDecimal> currentCharges= ChargeCalculator.calculateCharges(studentProfile); 
		bill = DataMapper.getBill(studentProfile, transactionHistoryList, currentCharges); 
		
		// if the bill has one or more transaction
		if (bill.getTransactions().size()>0) 
			return bill;
		else
			throw new NoBillExistedException("No bill existed for the user ID "+userId);
	}

	/**
	 * to get the charges and payment made within the range of dates given presented in Bill object
	 * @param userId the id of user that is associated with transaction
	 * @param startDate a start date where transactions' dates fall after it
	 * @param endDate an end date where transactions' dates fall before it
	 * @return bill consisted of student of profile along wiht charges, payment within the range of dates given
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @throws UserPermissionException if the logged-in user has no permission accessing given id of user
	 * @throws UserNotFoundException in case no user is logged in
	 * @throws NoBillExistedException if there is no bill existed for the given user id
  	 * @see com.usc.bill.dto.Bill
	 */
	@Transactional
	public Bill getAllCharges(String userId, Date startDate, Date endDate) throws NoStudentProfileFoundException, UserPermissionException, UserNotFoundException, NoBillExistedException {
		// check whether there is a user logged in
		if(userManagementService.getLoggedInUserId()==null)
			throw new UserNotFoundException("No user currently is logged in");
		
		StudentProfile studentProfile = studentManagementService.getStudentProfile(userId);
		// check whether the logged-in user has an access to the student profile
		if (!userManagementService.checkAccess(studentProfile))
			throw new UserPermissionException("Logged in user has no permission to access user ID "+userId);
			
		List<TransactionHistory> allTransactionHistory = getAllTransactionHistory(userId, startDate, endDate);
		if (DataMapper.isCurrentTermInBetween(startDate, endDate)){
			Map<String, BigDecimal> currentCharges = ChargeCalculator.calculateCharges(studentProfile); 	
			Bill bill = DataMapper.getBill(studentProfile, allTransactionHistory, currentCharges); 
			if (bill.getTransactions().size() > 0)
				return bill;
			else 
				throw new NoBillExistedException("No bill existed for the user ID "+userId+" within given start date and end date");
		}
		else{ //otherwise, retrieve the past transactions 
			Bill bill = DataMapper.getBill(studentProfile, allTransactionHistory);
			if (bill.getTransactions().size() > 0) 
				return bill;
			else 
				throw new NoBillExistedException("No bill existed for the user ID "+userId+" within given start date and end date");
		}
	}

	/**
	 * to get current charges with a given userId
	 * @param userId an id of user where the charges will be incurred
	 * @return map of current charges
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 */
	public Map<String, BigDecimal> getCurrentCharges(String userId) throws NoStudentProfileFoundException {
		StudentProfile studentProfile = studentManagementService.getStudentProfile(userId); // retrieve student profile
		return ChargeCalculator.calculateCharges(studentProfile); // calculate the current 
	}
	
	/**
	 * to get all charges and payment associated with a given userId
	 * @param userId an id of user where the transaction made
	 * @return all transaction made that are associated with a given userId
	 * @see com.usc.bill.model.TransactionHistory
	 */
	@Transactional
	public List<TransactionHistory> getAllTransactionHistory(String userId) {
		return transactionHistoryRepository.findByUser_Id(userId);
	}	
	
	/**
	 * to get all charges and payment associated with a given userId
	 * @param userId the id of user that is associated with transaction
	 * @param startDate a start date where transactions' dates fall after it
	 * @param endDate an end date where transactions' dates fall before it
	 * @return all transaction made that are associated with a given userId
	 * @see com.usc.bill.model.TransactionHistory
	 */
	@Transactional
	public List<TransactionHistory> getAllTransactionHistory(String userId, Date startDate, Date endDate) {
		return transactionHistoryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
	}
	
	/**
	 * to get the total balance from all transaction history by calculating charge against payment
	 * @param allTransactionHistory consists of all payment and charges
	 * @return the balance in respective to the payments made and past charges
	 * @see com.usc.bill.model.TransactionHistory
	 */
	@Transactional
	public BigDecimal getBalance(List<TransactionHistory> allTransactionHistory){	
		return ChargeCalculator.calculateBalance(allTransactionHistory);
	}

	/**
	 * to get the total balance from current charges and all transaction history by calculating charge against payment
	 * @param allTransactionHistory consists of all payment and charges
	 * @return the balance in respective to the payments made and past charges
	 * @see com.usc.bill.model.TransactionHistory
	*/
	@Transactional
	public BigDecimal getBalance(Map<String, BigDecimal> currentCharges, List<TransactionHistory> allTransactionHistory){
		return ChargeCalculator.calculateBalance(currentCharges, allTransactionHistory);
	}
	
	/**
	 * to add a payment against the current student's bill 
	 * @throws UserNotFoundException if the user associated with a given id is not existed in the system
	 * @throws NoStudentProfileFoundException if there is no student profile associated with user id existed in the system
	 * @throws UserPermissionException if the logged-in user has no permission accessing given id of user
	 */
	@Transactional
	public void addPayment(String userId, BigDecimal amount, String note) throws UserNotFoundException, NoStudentProfileFoundException, UserPermissionException{	
		// check whether there is a user logged in
		if(userManagementService.getLoggedInUserId()==null)
			throw new UserNotFoundException("No user currently is logged in");
		
		StudentProfile studentProfile = studentManagementService.getStudentProfile(userId);
		
		// check whether the logged-in user has an access to the student profile			
		if (!userManagementService.checkAccess(studentProfile)) 
			throw new UserPermissionException("Logged in user has no permission to access user ID "+userId); // in case the logged-in user has no access to the student profile requested

		// create a user and transaction history objects to persist through repository
		User user = studentProfile.getUser();
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.setUser(user);
		transactionHistory.setType(Type.PAYMENT);
		transactionHistory.setAmount(amount);
		transactionHistory.setNote(note);
		transactionHistory.setDate(new Date());
		transactionHistoryRepository.save(transactionHistory);
	}
}