package com.jrtp.userMgmt.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Data
@Entity
public class UserAccount {
    @Id
    @GeneratedValue
    private Integer userId;
    private String fullName;
    private String email;
    private Long phoneNumber;
    private String gender;
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private Long ssn;
    private String activeSw = "Y";
    @CreationTimestamp
    private LocalDate createdDate;
    @UpdateTimestamp
    private LocalDate updatedDate;

}
