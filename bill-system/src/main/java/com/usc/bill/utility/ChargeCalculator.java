package com.usc.bill.utility;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.usc.bill.dto.Term;
import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.StudentCourse;
import com.usc.bill.model.InternationalStatus;
import com.usc.bill.model.Scholarship;
import com.usc.bill.model.Semester;
import com.usc.bill.model.StudentProfile;
import com.usc.bill.model.StudyAbroad;
import com.usc.bill.model.TransactionHistory;
import com.usc.bill.model.Type;


/**
 * To handle the calculation of tuition, fee and balance
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
*/
public class ChargeCalculator{
	/**
	 * UNDERGRADUATE - RESIDENT - VETERAN - TUITION TUITION
	 */
	private final static double undergradResOrVetTuition = 494.25;
	
	/**
	 * UNDERGRADUATE - NONRESIDENT - TUITION RATE
	 */
	private final static double undergradNonResTuition = 1331.75;
	
	/**
	 * UNDERGRADUATE - NONRESIDENT SCHOLARSHIP - GENERAL UNIVERSITY TUITION RATE
	 */
	private final static double undergradGenSchTuition = 494.25;

	/**
	 * UNDERGRADUATE - NONRESIDENT - SCHOLARSHIP - WOODROW, DEPARTMENTAL OR ATHLETIC TUITION RATE
	 */
	private final static double undergradDepOrAthlOrWodrSchTuition = 733.50;

	/**
	 * UNDERGRADUATE - NONRESIDENT - SCHOLARSHIP - SIMS TUITION RATE
	 */
	private final  static double  undergradSimsSchTuition = 946.00;

	/**
	 * ACTIVE DUTY MILITARY UNDERGRADUATE - TUITION
	 */
	private final static double undergradActDutyTuition = 289.50;

	/**
	 * GRADUATE - RESIDENT - TUITION RATE
	 */
	private final static double gradResidentTuition = 533;

	/**
	 * GRADUATE - NONRESIDENT - TUITION RATE
	 */
	private final static double gradNonResidentTuition = 1142;

	/**
	 * UNDERGRADUATE - RESIDENT, NONRESIDENT SCHOLARSHIP, ACTIVE DUTY MILITARY - 17 HOURS AND ABOVE FEE RATE
	 */
	private final static double undergradResActDutyOrSchSeventeenOrAbvCreditFee = 80;

	/**
	 * UNDERGRADUATE - NONRESIDENT - 17 HOURS AND ABOVE FEE RATE
	 */
	private final static double undergradNonSeventeenOrAbvCreditFee = 208;

	/**
	 * GRADUATE - RESIDENT - 17 HOURS AND ABOVE FEE RATE
	 */
	private final static double gradResSeventeenOrAbvCreditFee = 80;

	/**
	 * GRADUATE - NONRESIDENT - 17 HOURS AND ABOVE FEE RATE
	 */
	private final static double gradNonResAndSeventeenOrAbvCreditFee = 170;

	/**
	 * HEALTH INSURANCE - (STUDENTS WITHOUT COVERAGE) - CONTRACT W/THIRD PARTY RATE
	 */
	private final static double outsideInsurance = 2020;
	
	/**
	 * MANDATORY STUDY ABROAD INSURANCE RATE
	 */
	private final static double outsideStAbroadInsurance = 360;
	
	/**
	 * UNDERGRADUATE STUDENTS - (6 TO 11 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER
	 */
	private final static double undergradHealthSixToElevenCreditFee = 119;
	
	/**
	 * GRADUATE ASSISTANTS - LESS THAN 12 HOURS - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER
	 */	
	private final static double gradAssHealthLessThanTwelveCreditFee = 178;
	
	/**
	 * GRADUATE STUDENTS - (9 TO 11 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER
	 */
	private final static double gradHealthNineToElevenCreditORFee = 178;
	
	/**
	 * GRADUATE STUDENTS - (6 TO 8 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER
	 */
	private final static double gradHealthSixToEightCreditORFee = 119;
	
	/**
	 * TECHNOLOGY FEE RATE
	 */
	private final static double technologyFeePerHour = 17;
	
	/**
	 * ENROLLMENT FEE RATE
	 */
	private final static double enrollmentFee = 750;
	
	/**
	 * SHORT TERM INTERNATIONAL STUDENT FEE RATE
	 */
	private final static double internationalShortTermFee = 200;
	
	/**
	 * SPONSORED INTERNATIONAL STUDENT FEE
	 */
	private final static double internationalSponseredFee = 250;
	
	/**
	 * NATIONAL STUDENT EXCHANGE PLACEMENT AND ADMINISTRATIVE FEE
	 */
	private final static double nationalStudentExchangeFee = 250;
	
	/**
	 * STUDY ABROAD EXCHANGE PROGRAM DEPOSIT - NONREFUNDABLE
	 */
	private final static double stAbroadExProgFee = 500;

	/**
	 * REGULAR STUDY ABROAD
	 */
	private final static double stAbroadRegFee = 150;

	/**
	 * COHORT STUDY ABROAD
	 */	
	private final static double stAbroadCohFee = 300;

	/**
	 * MATRICULATION FEE RATE
	 */
	private final static double matriculationFee = 80;
	
	/**
	 * CAPSTONE SCHOLAR FEE - PER SEMESTER FEE RATE
	 */	
	private final static double capstoneFee = 150;



	/**
	 * to calculate the final charges
	 * @param studentProfile where the charges will be calculated based on 
	 * @return the final charges
	 */	
	public static BigDecimal calculateFinalCharges(StudentProfile studentProfile) {
		BigDecimal finalCharges = BigDecimal.valueOf(0); // declare initially a 0 of final charge
		Map<String, BigDecimal> currentCharges = calculateCharges(studentProfile); // calculate the current charges 
		// Iterator over the map of current charges		
		for (Map.Entry<String, BigDecimal> entry : currentCharges.entrySet())
			finalCharges = finalCharges.add(entry.getValue()); // get the amount associated with a charge and add it subsequently 
		// return the final charges of current semester
		return finalCharges;
	}

	/**
	 * to calculate the current charges
	 * @param studentProfile where the charges will be calculated based on 
	 * @return the final charges
	 */	
	public static Map<String, BigDecimal> calculateCharges(StudentProfile studentProfile) {
		Map<String, BigDecimal> charges = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> tuitions = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> fees = new HashMap<String, BigDecimal>();
		Date [] startEndBeganDates = DataMapper.getStartDateAndEndDate(studentProfile.getSemesterBegin().toString(), studentProfile.getYearBegin());
		
		if (studentProfile.getClassStatus().equals(ClassStatus.GRADUATED) 
				|| startEndBeganDates==null || (startEndBeganDates!=null && startEndBeganDates[0].after(new Date()))) // 
			return charges;
		else{
			tuitions = calculateTuition(studentProfile);
			fees = calculateFee(studentProfile);
		}
		
		charges.putAll(tuitions);
		charges.putAll(fees);
		return charges;
	}

	/**
	 * to calculate the tuition
	 * @param studentProfile where the tuition will be calculated based on 
	 * @return the map of tuitions
	 */	
	public static Map<String, BigDecimal> calculateTuition(StudentProfile studentProfile) {
		Map<String, BigDecimal> tuitions = new HashMap<String, BigDecimal>(); // declare initial map of tuitions
		int totalNonOnlineCredit = getNonOnlineCreditHours(studentProfile.getStudentCourses()); // get the total non-online credit hours
		int totalOnlineCredit = getOnlineCreditHours(studentProfile.getStudentCourses()); // get the total non-online credit hours

		if (studentProfile.getClassStatus().equals(ClassStatus.MASTERS) ||
				studentProfile.getClassStatus().equals(ClassStatus.PHD)){ // if student is a graduate 			
			if (studentProfile.getResident()){ // if student is resident
				if (studentProfile.getGradAssistant()) // will be calculated as a zero tuition if the resident grad is assistant
					tuitions.put("RESIDENT GRADUATE ASSISTANT TUITION", BigDecimal.valueOf(0));			
				else // otherwise, resident tuition rate will be incurred 
					tuitions.put("GRADUATE - RESIDENT - TUITION", BigDecimal.valueOf((totalNonOnlineCredit+totalOnlineCredit)*gradResidentTuition));			
			}
			else { // if student is non-resident
				if (studentProfile.getGradAssistant()){ // if student is grade assistant which will be calculated as non-resident graduate tuition minus resident graduate tuition per hour
					tuitions.put("NON-RESIDENT GRADUATE ASSISTANT TUITION", BigDecimal.valueOf((totalNonOnlineCredit+totalOnlineCredit)
							*(gradNonResidentTuition-gradResidentTuition)));	
				}
				else if (!studentProfile.getStudyAbroad().equals(StudyAbroad.NONE)){ // if student is studying abroad which will be calculated as non-resident graduate tuition minus resident graduate tuition per hour
					tuitions.put("GRADUATE - STUDY ABROAD - TUITION", 
							BigDecimal.valueOf((totalNonOnlineCredit+totalOnlineCredit)*(gradNonResidentTuition-gradResidentTuition)));	
				}
				else{ // otherwise, non-resident tuition rate will be incurred			
					tuitions.put("GRADUATE - NONRESIDENT - TUITION", 
							BigDecimal.valueOf((totalNonOnlineCredit*gradNonResidentTuition)+(totalOnlineCredit*gradResidentTuition)));	
				}
			}
		} 
		else{ // else, student is a undergraduate 
			if (studentProfile.getFreeTuition() && studentProfile.getResident()) // if student is eligible for free tuition of amount 0, which is exclusive for undergraduate student
				tuitions.put("FREE TUITION", BigDecimal.valueOf(0));
			else{ // not free tuition
				if (studentProfile.getActiveDuty()) // if undergraduate student is on active military duty
					tuitions.put("ACTIVE DUTY MILITARY UNDERGRADUATE - TUITION", BigDecimal.valueOf(totalNonOnlineCredit*undergradActDutyTuition));
				else if (studentProfile.getResident() || studentProfile.getVeteran()) // if undergraduate student either member of veteran family or resident
					tuitions.put("UNDERGRADUATE - RESIDENT - VETERAN - TUITION", BigDecimal.valueOf(totalNonOnlineCredit*undergradResOrVetTuition));
				else if (!studentProfile.getResident() && !studentProfile.getStudyAbroad().equals(StudyAbroad.NONE)) // if student is studying abroad which will be calculated as non-resident undergraduate tuition minus resident undergraduate tuition per hour 
					tuitions.put("STUDY ABROAD Tuition", BigDecimal.valueOf(totalNonOnlineCredit*(undergradNonResTuition-undergradResOrVetTuition)));
				else if (!studentProfile.getResident() && studentProfile.getScholarship().equals(Scholarship.GENERAL)) // if undergraduate non-resident student has a general scholarship
					tuitions.put("UNDERGRADUATE - NONRESIDENT SCHOLARSHIP - GENERAL UNIVERSITY TUITION", BigDecimal.valueOf(totalNonOnlineCredit*undergradGenSchTuition));
				else if (!studentProfile.getResident() && (studentProfile.getScholarship().equals(Scholarship.DEPARTMENTAL)|| 
						studentProfile.getScholarship().equals(Scholarship.WOODROW)|| 
						studentProfile.getScholarship().equals(Scholarship.ATHLETIC))){ // if undergraduate non-resident student has a athletic, departmental or woodrow scholarship
						tuitions.put("UNDERGRADUATE - NONRESIDENT - SCHOLARSHIP - WOODROW & DEPARTMENTAL & ATHLETIC TUITION", BigDecimal.valueOf(totalNonOnlineCredit*undergradDepOrAthlOrWodrSchTuition));
				}
				else if (!studentProfile.getResident() && (studentProfile.getScholarship().equals(Scholarship.SIMS))) // if undergraduate non-resident student has a sims scholarship
						tuitions.put("UNDERGRADUATE - NONRESIDENT - SCHOLARSHIP - SIMS TUITION", BigDecimal.valueOf(totalNonOnlineCredit*undergradSimsSchTuition));
				else if (!studentProfile.getResident() && (studentProfile.getScholarship().equals(Scholarship.NONE))) // if undergraduate non-resident student has a no scholarship, then non-resident tuition rate will be incurred
						tuitions.put("UNDERGRADUATE - NONRESIDENT - TUITION", BigDecimal.valueOf(totalNonOnlineCredit*undergradNonResTuition));
			}	
		}
		return tuitions;
	}

	/**
	 * to calculate the fee
	 * @param studentProfile where the fee will be calculated based on 
	 * @return the map of fees
	 */
	public static Map<String, BigDecimal>  calculateFee(StudentProfile studentProfile) {
		Map<String, BigDecimal> fees = new HashMap<String, BigDecimal>(); // declare initial map of fees
		boolean isGradStatus = studentProfile.getClassStatus().equals(ClassStatus.MASTERS) || studentProfile.getClassStatus().equals(ClassStatus.PHD); // whether student is graduate or undergraduate
		int totalCredit = getTotalCreditHours(studentProfile.getStudentCourses()); // total credit hours
		Date [] capstoneDates = null;
		int diff=0; // year gap between the capstone year enrolled and the ?????????????
		if (studentProfile.getYearCapstoneEnrolled() != 0 || studentProfile.getSemesterCapstoneEnrolled() != null){
			capstoneDates = DataMapper.getStartDateAndEndDate(studentProfile.getSemesterCapstoneEnrolled().toString(), studentProfile.getYearCapstoneEnrolled());
			if (capstoneDates!=null)
				diff = getYearDifferencesInCapstone(capstoneDates[0]);
			else 
				diff = 3;
		}
		
		Term currentTerm = DataMapper.getTerm(new Date());
		
		// Calculate technology fee per credit hour instantaneously
		fees.put("TECHNOLOGY FEE", BigDecimal.valueOf(totalCredit*technologyFeePerHour));
		
		// Calculate Enrollment and Matriculation fee if the current semester is the term where student begins
		if (studentProfile.getSemesterBegin().toString().equals(currentTerm.getSemester()) && studentProfile.getYearBegin() == currentTerm.getYear()){
			fees.put("ENROLLMENT FEE", BigDecimal.valueOf(enrollmentFee));
			fees.put("MATRICULATION FEE", BigDecimal.valueOf(matriculationFee));
		}
		
		// Calculate the capstone scholar fee if still within the range of 2 academic years 
		if (diff<=2 && capstoneDates!=null){
			if (diff == 1 || studentProfile.getSemesterCapstoneEnrolled().toString().equals(currentTerm.getSemester())
					|| (diff == 0 && studentProfile.getSemesterCapstoneEnrolled().equals(Semester.SUMMER) && !currentTerm.getSemester().equals(Semester.FALL.toString()))
					|| (diff == 2 && studentProfile.getSemesterCapstoneEnrolled().equals(Semester.SPRING) && currentTerm.getSemester().equals(Semester.FALL.toString()))
					|| (diff == 2 && studentProfile.getSemesterCapstoneEnrolled().equals(Semester.SUMMER) && currentTerm.getSemester().equals(Semester.FALL.toString()))
					|| (diff == 2 && studentProfile.getSemesterCapstoneEnrolled().equals(Semester.SPRING) && currentTerm.getSemester().equals(Semester.SUMMER.toString()))){
				fees.put("CAPSTONE SCHOLAR FEE - PER SEMESTER FEE", BigDecimal.valueOf(capstoneFee));
			}
		}
		
		// Calculate the 17 credit hours or above fee
		if (totalCredit>=17){
			if (isGradStatus){
				if (studentProfile.getResident()) // if graduate resident student
					fees.put("GRADUATE - RESIDENT - 17 HOURS AND ABOVE FEE", 
							BigDecimal.valueOf(gradResSeventeenOrAbvCreditFee));
				else // otherwise, graduate non-resident student
					fees.put("GRADUATE - NONRESIDENT - 17 HOURS AND ABOVE FEE", 
							BigDecimal.valueOf(gradNonResAndSeventeenOrAbvCreditFee));
			}
			else{ // otherwise, undergraduate 
				if (studentProfile.getResident() || studentProfile.getActiveDuty() 
						|| (!studentProfile.getScholarship().equals(Scholarship.NONE) && !studentProfile.getResident())) // if either undergraduate resident or on active militaryd dutry or non-resident has a scholarship
						fees.put("UNDERGRADUATE - RESIDENT, NONRESIDENT SCHOLARSHIP, ACTIVE DUTY MILITARY - 17 HOURS AND ABOVE FEE", 
								BigDecimal.valueOf(undergradResActDutyOrSchSeventeenOrAbvCreditFee));
				else // otherwise, undergradate non-resident regular fee will be incurred
					fees.put("UNDERGRADUATE - NONRESIDENT - 17 HOURS AND ABOVE FEE", 
							BigDecimal.valueOf(undergradNonSeventeenOrAbvCreditFee));
			}
		}
		
		//Calculating the health fee for only non-study abroad/on-campus students
		if (studentProfile.getStudyAbroad().equals(StudyAbroad.NONE)){ // if student is not studying abroad
			if (totalCredit < 12 && isGradStatus && studentProfile.getGradAssistant()){ // graduate assistnat student with less than 12 credit hours
				fees.put("GRADUATE ASSISTANTS - LESS THAN 12 HOURS - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER", 
						BigDecimal.valueOf(gradAssHealthLessThanTwelveCreditFee));
			}
			else if ((totalCredit >= 9 && totalCredit <= 11) && isGradStatus){ // graduate student with credit hours with in 9 & 11
				fees.put("GRADUATE STUDENTS - (9 TO 11 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER", 
						BigDecimal.valueOf(gradHealthNineToElevenCreditORFee));
			}
			else if ((totalCredit >= 6 && totalCredit <= 8) && isGradStatus){ // graduate student with credit hours with in 6 & 8
				fees.put(" GRADUATE STUDENTS - (6 TO 8 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER", 
						BigDecimal.valueOf(gradHealthSixToEightCreditORFee));
			}
			else if ((totalCredit >= 6 && totalCredit <= 11) && !isGradStatus){ // undergraduate student with credit hours with in 6 & 11
				fees.put("UNDERGRADUATE STUDENTS - (6 TO 11 HOURS) - REQUIRED STUDENT HEALTH CENTER FEE - PER SEMESTER", 
						BigDecimal.valueOf(undergradHealthSixToElevenCreditFee));
			}
		}
		
		//Calculating the internatinal student fees
		if (studentProfile.getInternational() && !studentProfile.getInternationalStatus().equals(InternationalStatus.NONE)){ // if international and has a status
			if (studentProfile.getInternationalStatus().equals(InternationalStatus.SHORT_TERM) && ((isGradStatus && totalCredit>=9) ||
					(!isGradStatus && totalCredit>=12))) // if short-term and enrolled for a full-time (12 credit hours for undergraduate and 9 credit hours for graduate)
				fees.put(" INTERNATIONAL STUDENT FEE", BigDecimal.valueOf(internationalShortTermFee));
			if (studentProfile.getInternationalStatus().equals(InternationalStatus.SPONSORED) && ((isGradStatus && totalCredit>=9) ||
					(!isGradStatus && totalCredit>=12))) // if sponsored and enrolled for a full-time (12 credit hours for undergraduate and 9 credit hours for graduate)
				fees.put("SPONSORED INTERNATIONAL STUDENT FEE", BigDecimal.valueOf(internationalSponseredFee));
		}
		
		// Calculating study abroad fee if the student either Regular or Cohort 
		if (!studentProfile.getStudyAbroad().equals(StudyAbroad.NONE)){
			fees.put("STUDY ABROAD EXCHANGE PROGRAM DEPOSIT - NONREFUNDABLE", BigDecimal.valueOf(stAbroadExProgFee));
			if (studentProfile.getStudyAbroad().equals(StudyAbroad.REGULAR))
				fees.put("REGULAR STUDY ABROAD FEE", BigDecimal.valueOf(stAbroadRegFee));
			else
				fees.put("COHORT STUDY ABROAD FEE", BigDecimal.valueOf(stAbroadCohFee));
			
			if (studentProfile.getNationalStudentExchange())
				fees.put("NATIONAL STUDENT EXCHANGE PLACEMENT & ADMINISTRATIVE FEE", BigDecimal.valueOf(nationalStudentExchangeFee));
		}	
		
		// Calculating the outside insurance coverage for either on-campus or study abroad student
		if (studentProfile.getOutsideInsurance()){
			if (studentProfile.getStudyAbroad().equals(StudyAbroad.NONE)) // if student is on campus
				fees.put("HEALTH INSURANCE - (STUDENTS WITHOUT COVERAGE) - CONTRACT W/THIRD PARTY", BigDecimal.valueOf(outsideInsurance));
			else // otherwise, insurance of studying abroad will be incurred
				fees.put("MANDATORY STUDY ABROAD INSURANCE", BigDecimal.valueOf(outsideStAbroadInsurance));
		}
		return fees;
	}
	
	/** 
	 * to get the balance of all payments against past and current charges 
	 * @param currentCharges current semester charges
	 * @param allTransactionHistory a list of transactions which consists of payments and charges
	 * @return the balance of payments against charges 
	 * @see com.usc.bill.model.TransactionHistory 
	 */
	public static BigDecimal calculateBalance(Map<String, BigDecimal> currentCharges, List<TransactionHistory> allTransactionHistory) {
		BigDecimal balance = BigDecimal.valueOf(0);		
		// Iterator over the PaymentHistory List
		for (TransactionHistory t: allTransactionHistory){
			if (t.getType().equals(Type.PAYMENT)) // if payment, add it from the balance
				balance = balance.add(t.getAmount());
			else
				balance = balance.subtract(t.getAmount()); // otherwise, subtract it from the balance
		}		
		// Iterator over the map of current charges		
		for (Map.Entry<String, BigDecimal> entry : currentCharges.entrySet())
	        balance = balance.subtract(entry.getValue()); // subtract it from the balance since it is always charges
		
		return balance;	
	}

	/** 
	 * to get the balance of all payments against past charges 
	 * @param allTransactionHistory a list of transactions which consists of payments and charges
	 * @return the balance of payments against charges 
	 * @see com.usc.bill.model.TransactionHistory 
	 */
	public static BigDecimal calculateBalance(List<TransactionHistory> allTransactionHistory){
		BigDecimal balance = BigDecimal.valueOf(0);
		
		// Iterator over the PaymentHistory List
		for (TransactionHistory t: allTransactionHistory){
			if (t.getType().equals(Type.PAYMENT)) // if payment, add it from the balance
				balance = balance.add(t.getAmount());
			else
				balance = balance.subtract(t.getAmount()); // otherwise, subtract it from the balance
		}
		
		return balance;
	}

	/** 
	 * to get the total number of credit hours for the courses  
	 * @param courses set of courses
	 * @return total number of credit hours for courses 
	 * @see com.usc.bill.model.StudentCourse 
	 */
	public static int getTotalCreditHours(Set<StudentCourse> courses){
		int totalCredit = 0;
		for(StudentCourse c: courses)
				totalCredit+=c.getNumCredits();
		return totalCredit;
	}
	
	/** 
	 * to get the total number of credit hours for non-online courses   
	 * @param studentCourses set of StudentCourses
	 * @return total number of credit hours for non-online courses 
	 * @see com.usc.bill.model.StudentCourse 
	 */
	public static int getNonOnlineCreditHours(Set<StudentCourse> studentCourses){
		int totalCredit = 0;
		for(StudentCourse c: studentCourses){
			if (!c.getCourseId().getOnline())
				totalCredit+=c.getNumCredits();
		}
		return totalCredit;
	}
	
	/** 
	 * to get the total number of credit hours for online courses  
	 * @param courses set of courses
	 * @return total number of credit hours for online courses 
	 * @see com.usc.bill.model.StudentCourse 
	 */
	public static int getOnlineCreditHours(Set<StudentCourse> courses){
		int totalCredit = 0;
		for(StudentCourse c: courses){
			if (c.getCourseId().getOnline())
				totalCredit+=c.getNumCredits();
		}
		return totalCredit;
	}
	
	/** 
	 * to get the difference of two year after capstone enrollment and current year  
	 * @param capstoneDate date enrollment on capstone
	 * @return difference in year
	 */
	private static int getYearDifferencesInCapstone(Date capstoneDate){
		int yearDiff = 0;
		Calendar currentCal = Calendar.getInstance();
		currentCal.setTime(new Date());
		
		Calendar twoYearsAfterCal = Calendar.getInstance();
		twoYearsAfterCal.setTime(capstoneDate);
		twoYearsAfterCal.add(Calendar.YEAR, 2); // to get calendar of date of two years after.	
		
		yearDiff = twoYearsAfterCal.get(Calendar.YEAR) - currentCal.get(Calendar.YEAR); //to get the difference between two dates' years
		
		return yearDiff;
	}
}