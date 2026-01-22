package com.jrtp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jrtp.binding.EmailRequest;
import com.jrtp.binding.SearchCriteria;
import com.jrtp.service.CitizenPlanService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CitizenPlanController {

    private final CitizenPlanService service;

    public CitizenPlanController(CitizenPlanService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("criteria", new SearchCriteria());
        model.addAttribute("planNames", service.getPlanNames());
        model.addAttribute("planStatuses", service.getPlanStatus());
        return "index";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("criteria") SearchCriteria criteria, Model model) {
        model.addAttribute("criteria", criteria);
        model.addAttribute("planNames", service.getPlanNames());
        model.addAttribute("planStatuses", service.getPlanStatus());
        model.addAttribute("citizens", service.searchCitizen(criteria));
        return "index";
    }

    @GetMapping("/excel")
    public void generateExcel(HttpServletResponse response) throws Exception {
        service.generateExcelReport(response);
    }

    @GetMapping("/pdf")
    public void generatePdf(HttpServletResponse response) throws Exception {
        service.generatePdfReport(response);
    }

    @PostMapping("/send-email")
    public String sendEmail(@ModelAttribute EmailRequest emailRequest, RedirectAttributes redirectAttributes) {
        try {
            if (!emailRequest.isSendPdf() && !emailRequest.isSendExcel()) {
                redirectAttributes.addFlashAttribute("error",
                        "Please select at least one report format (PDF or Excel)");
                return "redirect:/";
            }

            service.sendReportEmail(emailRequest.getEmail(), emailRequest.isSendPdf(), emailRequest.isSendExcel());
            redirectAttributes.addFlashAttribute("success", "Report sent successfully to " + emailRequest.getEmail());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to send email: " + e.getMessage());
        }
        return "redirect:/";
    }
}
