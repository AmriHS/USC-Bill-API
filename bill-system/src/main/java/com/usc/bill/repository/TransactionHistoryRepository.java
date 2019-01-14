package com.usc.bill.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.usc.bill.model.TransactionHistory;


/**
 * To handle CRUD operations on transaction-related data
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */  
@Repository
public  interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long>{
	
	/**
	 * to load all transactions that match given user id 
	 * @param id the id of user that is associated with transaction
	 * @return list of transaction history that match given user id
	 * @see com.usc.bill.model TransactionHistory
	 */
	public List<TransactionHistory> findByUser_Id(String id);

	/**
	 * to load all transactions that match given user id and within given the range of dates 
	 * @param userId the id of user that is associated with transaction
	 * @param startDate a start date where transactions' dates fall after it
	 * @param endDate an end date where transactions' dates fall before it
	 * @return list of transaction history that match given user id and within given the range of dates 
	 * @see com.usc.bill.model TransactionHistory
	 */
	@Query("SELECT t FROM TransactionHistory t inner join t.user u WHERE u.id =:userId AND t.date >=:startDate AND t.date <=:endDate")
	public List<TransactionHistory> findByUserIdAndDateBetween(@Param("userId") String userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	/**
	 * to persist transaction
	 * @param transactionHistory an object of TransactionHistory
	 * @return transaction history that has already persisted
	 * @see com.usc.bill.model.TransactionHistory
	 */
	public TransactionHistory save(TransactionHistory transactionHistory);


}

