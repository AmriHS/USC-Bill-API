package com.usc.bill.jobSchedular;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.usc.bill.model.ClassStatus;
import com.usc.bill.model.StudentProfile;
import com.usc.bill.service.BillService;
import com.usc.bill.service.StudentManagementService;

/**
 * To handle collecting charges by the end of semester in tasks that are schedule on rough dates
 * @author WU, Alamri, STROUD
 * @version 1.0 11/19/2017
*/ 
@Service
@EnableScheduling
public class EndTermJobSchedular{
	
	/**
	 * instance of StudentManagementService in order to assist in Student-related bussiness data operations
	 */
	@Autowired
	private StudentManagementService studentManagementService;
	
	/**
	 * instance of BillService in order to assist in charge and payment-related business operations
	 */	
	@Autowired
	private BillService billService;
	
	/*
	 * get all students who are active and not graduated
	 * calculated charges on each student profile
	 */
    public void runJob(){
        List<StudentProfile> studentProfileList = studentManagementService.getAllExceptClassStatus(ClassStatus.GRADUATED);
        for (StudentProfile s : studentProfileList)
            billService.generateEndTermCharge(s);
    }
    
    /*
     * scheduled task runs on every 31 Dec. every year to calculate end of FALL semester charges
     */
    @Scheduled(cron = "0 0 0 31 12 ?")
    public void fallTermTask(){
    	runJob();
    }
    
    /*
     * scheduled task runs on every 10 May every year to calculate end of SPRING semester charges
     */
    @Scheduled(cron = "0 0 0 10 5 ?")
    public void springTermTask(){
    	runJob();
    }
    
    /*
     * scheduled task runs on every 15 Aug. every year to calculate end of SUMMER semester charges
     */
    @Scheduled(cron = "0 0 0 15 8 ?")
    public void summerTermTask(){
    	runJob();
    }
}