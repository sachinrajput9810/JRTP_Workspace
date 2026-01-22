package com.jrtp.service;

import java.util.List;

import com.jrtp.binding.SearchCriteria;
import com.jrtp.entity.CitizenPlan;

import jakarta.servlet.http.HttpServletResponse;

public interface CitizenPlanService {
    List<String> getPlanNames();

    List<String> getPlanStatus();

    List<CitizenPlan> searchCitizen(SearchCriteria criteria);

    void generateExcelReport(HttpServletResponse response) throws Exception;

    void generatePdfReport(HttpServletResponse response) throws Exception;

    void sendReportEmail(String email, boolean sendPdf, boolean sendExcel) throws Exception;
}
