package com.jrtp.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer citizenId;
    private String name;
    private String email;
    private Long phno;
    private Long ssn;
    private String gender;
    private String planName;
    private String planStatus;
    private LocalDate planStarDate;
    private LocalDate planEndDate;

    public CitizenPlan(String name, String email, Long phno, Long ssn, String gender, String planName,
            String planStatus,
            LocalDate planStarDate, LocalDate planEndDate) {
        this.name = name;
        this.email = email;
        this.phno = phno;
        this.ssn = ssn;
        this.gender = gender;
        this.planName = planName;
        this.planStatus = planStatus;
        this.planStarDate = planStarDate;
        this.planEndDate = planEndDate;
    }
}
