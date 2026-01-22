package com.jrtp.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SearchCriteria {
    private String planName ;
    private String planStatus ;
    private String gender ;
    private LocalDate planStartDate ;
    private LocalDate planEndDate ;
}