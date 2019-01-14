package com.usc.bill.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * A course is an academic class associated with a student.
 * the number of courses and attributes such as the number of credit hours will be used to calculate the student’s bill.
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
 */ 
@Entity
public class StudentCourse{	
	
	/**
	 * a composite ID of StudentCourse
	 */ 
	@EmbeddedId
	@AttributeOverrides({
	    @AttributeOverride(name="id", column=@Column(name="id")),
	    @AttributeOverride(name="online", column=@Column(name="online"))
	})
	private CourseId courseId;
	
	/**
	 * the name, or title, of the class
	 */ 
	@Column(nullable = false) 
	private String name;

	/**
	 * the number of credit hours for the class
	 */
	@Column 
	private int numCredits;
	
	/**
	 * @return the courseId
	 */
	public CourseId getCourseId() {
		return courseId;
	}

	/**
	 * @return the name
	 */
	public String getName(){
		return name;
	}

	/**
	 * @return the numCredits
	 */
	public int getNumCredits(){
		return numCredits;
	}
	
	
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(CourseId courseId) {
		this.courseId = courseId;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * @param numCredits the numCredits to set
	 */
	public void setNumCredits(int numCredits){
		this.numCredits = numCredits;
	}	

	 /**
	 * StudentCourse.java
	 * Purpose: : to form a composite primary key which consists of course id and whether its online or not. 
	 * Online course is distinguishable from non-online course
	 * @author WU, Alamri, STROUD
	 * @version 1.0 11/19/2017
	 */
	@Embeddable
	public static class CourseId implements Serializable{
		
		/**
		 * Composite ID is serialiable and should consist of version id to facilitate the version of data during the serilization and de-serialization 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * the unique id of the Course
		 */ 
		@Column(nullable=false)
		private String id;
		
		/**
		 * flag to indicate if the course is taught online
		 */
		@Column(nullable=false)
		private boolean online;
		
		/**
		 * @return the id
		 */
		public String getId(){
			return id;
		}
		
		/**
		 * @return the online
		 */
		public boolean getOnline(){
			return online;
		}
		
		/**
		 * @param id the id to set
		 */
		public void setId(String id){
			this.id = id;
		}
		
		/**
		 * @param online the online to set
		 */
		public void setOnline(boolean online){
			this.online = online;
		}
	}
}