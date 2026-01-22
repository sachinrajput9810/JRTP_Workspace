package com.jrtp.runner;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.jrtp.entity.CitizenPlan;
import com.jrtp.repository.CitizenPlanRepo;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private CitizenPlanRepo repo ;
 
    @Override
    public void run(ApplicationArguments args) throws Exception {
        repo.deleteAll();
        CitizenPlan p1 = new CitizenPlan("Raj", "raj@gmail.com", 1234567890L, 1234567890L, "Male", "Health", "Approved", LocalDate.now(), LocalDate.now().plusMonths(6)); 
        CitizenPlan p2 = new CitizenPlan("Ravi", "ravi9313@gmail.com", 987654567L, 98765445678L, "Male", "Cash", "Denied" , null , null );
        CitizenPlan p3 = new CitizenPlan("Cathy", "cathy@gmail.com", 877382923L, 9323445678L, "Female", "Food", "Approved" , LocalDate.now() , LocalDate.now().plusMonths(8) );
        CitizenPlan p4 = new CitizenPlan("Raj Lakshmi", "rajlakshmi@gmail.com", 224567890L, 3322445678L, "Female", "Food", "Denied", null , null); 
        CitizenPlan p5 = new CitizenPlan("Robert", "robert12@gmail.com", 32444422L, 567890L, "Male", "Cash", "Active", LocalDate.now(), LocalDate.now().plusMonths(6));
        CitizenPlan p6 = new CitizenPlan("Emilie", "emilie@gmail.com", 33232444422L,12567890L, "Female", "Health", "Active", LocalDate.now(), LocalDate.now().plusMonths(6)); 
        repo.saveAll(List.of(p1,p2,p3,p4,p5,p6));
    }
    
}
