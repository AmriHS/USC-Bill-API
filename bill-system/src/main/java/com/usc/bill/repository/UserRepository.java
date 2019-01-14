package com.usc.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usc.bill.model.Role;
import com.usc.bill.model.User;


/**
 * To handle CRUD operations on user-related data
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */  
@Repository
public interface UserRepository extends JpaRepository<User, String>{
	
	/**
	 * to load user that matches a given id
	 * @param id user id
	 * @return students except ones that match given class status 
	 * @see com.usc.bill.model.User
	 */
	public User findOneById(String id);

	/**
	 * to load user that matches a given role
	 * @param role role of where user belong to
	 * @return users that matches a given role
	 * @see com.usc.bill.model.User
	 * @see com.usc.bill.model.Role
	 */
	public List<User> findByRole(Role role);

	/**
	 * to persist user
	 * @param user an object of User
	 * @return user that has already persisted
	 * @see com.usc.bill.model.User
	 */
	public User save(User user);
}