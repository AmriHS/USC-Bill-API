package com.usc.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.College;
import com.usc.bill.model.StudentProfile;


/**
 * To handle CRUD operations on student profile-related data
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */  
@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long>{


	/**
	 * to load student that matches a given user id
	 * @param id user id of student
	 * @return students except ones that match given class status 
	 * @see com.usc.bill.model.User
	 * @see com.usc.bill.model.StudentProfile
	 */
	public StudentProfile findOneByUser_Id(String id);

	/**
	 * to load all students that match given list of class status 
	 * @param classStatusList a list of class status 
	 * @return students with given list of class status
	 * @see com.usc.bill.model.ClassStatus
	 * @see com.usc.bill.model.StudentProfile
	 */
	@Query("FROM StudentProfile WHERE classStatus IN :classStatusList")
	public List<StudentProfile> findByClassStatus(@Param("classStatusList") List<ClassStatus> classStatusList);

	/**
	 * to load all students that except ones that match given class status 
	 * @param classStatus class status of student
	 * @return students except ones that match given class status
	 * @see com.usc.bill.model.ClassStatus
	 * @see com.usc.bill.model.StudentProfile
	 */
	@Query("FROM StudentProfile WHERE classStatus!=:classStatus")
	public List<StudentProfile> findAllExceptClassStatus(@Param("classStatus") ClassStatus classStatus);
	
	/**
	 * to load all students that match given college 
	 * @param college college of where student belong to
	 * @return students with given college 
	 * @see com.usc.bill.model.College
	 * @see com.usc.bill.model.StudentProfile
	 */
	@Query("SELECT s FROM StudentProfile s inner join s.user u WHERE u.college=:college")
	public List<StudentProfile> findByCollege(@Param("college") College college);

	/**
	 * to persist student profile
	 * @param studentProfile an object of StudentProfile
	 * @return student that has already persisted
	 * @see com.usc.bill.model.StudentProfile
	 */
	public StudentProfile save(StudentProfile studentProfile);
}

