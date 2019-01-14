package com.usc.bill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usc.bill.model.StudentCourse;
import com.usc.bill.model.StudentCourse.CourseId;

/**
 * To handle CRUD operations on course-related data
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */    
@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, CourseId>{
	/**
	 * to persist student course
	 * @param studentCourse an object of StudentCourse
	 * @return student course that has already persisted
	 * @see com.usc.bill.model.StudentCourse
	 */
	public StudentCourse save(StudentCourse studentCourse);
}

