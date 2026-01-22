package com.jrtp.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jrtp.binding.SearchCriteria;
import com.jrtp.entity.CitizenPlan;
import com.jrtp.repository.CitizenPlanRepo;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CitizenPlanServiceImpl implements CitizenPlanService {

    private final CitizenPlanRepo repo;
    private final JavaMailSender mailSender;

    public CitizenPlanServiceImpl(CitizenPlanRepo repo, JavaMailSender mailSender) {
        this.repo = repo;
        this.mailSender = mailSender;
    }

    @Override
    public List<String> getPlanNames() {
        return repo.findDistinctPlanNames();
    }

    @Override
    public List<String> getPlanStatus() {
        return repo.findDistinctPlanStatuses();
    }

    @Override
    public List<CitizenPlan> searchCitizen(SearchCriteria criteria) {
        List<CitizenPlan> citizens = repo.findAll();

        return citizens.stream()
                .filter(c -> criteria.getPlanName() == null || criteria.getPlanName().isEmpty()
                        || criteria.getPlanName().equals(c.getPlanName()))
                .filter(c -> criteria.getPlanStatus() == null || criteria.getPlanStatus().isEmpty()
                        || criteria.getPlanStatus().equals(c.getPlanStatus()))
                .filter(c -> criteria.getGender() == null || criteria.getGender().isEmpty()
                        || criteria.getGender().equals(c.getGender()))
                .filter(c -> criteria.getPlanStartDate() == null
                        || (c.getPlanStarDate() != null && !c.getPlanStarDate().isBefore(criteria.getPlanStartDate())))
                .filter(c -> criteria.getPlanEndDate() == null
                        || (c.getPlanEndDate() != null && !c.getPlanEndDate().isAfter(criteria.getPlanEndDate())))
                .toList();
    }

    @Override
    public void generateExcelReport(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=citizen-plans.xls");

        byte[] excelBytes = generateExcelBytes();
        response.getOutputStream().write(excelBytes);
    }

    @Override
    public void generatePdfReport(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=citizen-plans.pdf");

        byte[] pdfBytes = generatePdfBytes();
        response.getOutputStream().write(pdfBytes);
    }

    @Override
    public void sendReportEmail(String email, boolean sendPdf, boolean sendExcel) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Citizen Plan Report");
        helper.setText("Please find the attached Citizen Plan Report(s).");

        if (sendPdf) {
            byte[] pdfBytes = generatePdfBytes();
            helper.addAttachment("citizen-plans.pdf", new ByteArrayResource(pdfBytes));
        }

        if (sendExcel) {
            byte[] excelBytes = generateExcelBytes();
            helper.addAttachment("citizen-plans.xls", new ByteArrayResource(excelBytes));
        }

        mailSender.send(message);
    }

    private byte[] generateExcelBytes() throws Exception {
        List<CitizenPlan> plans = repo.findAll();

        try (Workbook workbook = new HSSFWorkbook();
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Citizen Plans");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font excelHeaderFont = workbook.createFont();
            excelHeaderFont.setBold(true);
            headerStyle.setFont(excelHeaderFont);

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "ID", "Name", "Email", "Phone", "Gender", "SSN",
                    "Plan Name", "Plan Status", "Start Date", "End Date"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowIdx = 1;
            for (CitizenPlan plan : plans) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(plan.getCitizenId() != null ? plan.getCitizenId() : 0);
                row.createCell(1).setCellValue(plan.getName() != null ? plan.getName() : "");
                row.createCell(2).setCellValue(plan.getEmail() != null ? plan.getEmail() : "");
                row.createCell(3).setCellValue(plan.getPhno() != null ? plan.getPhno() : 0);
                row.createCell(4).setCellValue(plan.getGender() != null ? plan.getGender() : "");
                row.createCell(5).setCellValue(plan.getSsn() != null ? plan.getSsn() : 0);
                row.createCell(6).setCellValue(plan.getPlanName() != null ? plan.getPlanName() : "");
                row.createCell(7).setCellValue(plan.getPlanStatus() != null ? plan.getPlanStatus() : "");
                row.createCell(8).setCellValue(plan.getPlanStarDate() != null ? plan.getPlanStarDate().toString() : "");
                row.createCell(9).setCellValue(plan.getPlanEndDate() != null ? plan.getPlanEndDate().toString() : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();
        }
    }

    private byte[] generatePdfBytes() throws Exception {
        List<CitizenPlan> plans = repo.findAll();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLUE);
        Paragraph title = new Paragraph("Citizen Plans Report", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Table with 10 columns
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Header cells
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        String[] headers = {
                "ID", "Name", "Email", "Phone", "Gender", "SSN",
                "Plan Name", "Plan Status", "Start Date", "End Date"
        };

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(Color.DARK_GRAY);
            cell.setPadding(5);
            table.addCell(cell);
        }

        // Data rows
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        for (CitizenPlan plan : plans) {
            table.addCell(new Phrase(String.valueOf(plan.getCitizenId()), dataFont));
            table.addCell(new Phrase(plan.getName() != null ? plan.getName() : "", dataFont));
            table.addCell(new Phrase(plan.getEmail() != null ? plan.getEmail() : "", dataFont));
            table.addCell(new Phrase(plan.getPhno() != null ? String.valueOf(plan.getPhno()) : "", dataFont));
            table.addCell(new Phrase(plan.getGender() != null ? plan.getGender() : "", dataFont));
            table.addCell(new Phrase(plan.getSsn() != null ? String.valueOf(plan.getSsn()) : "", dataFont));
            table.addCell(new Phrase(plan.getPlanName() != null ? plan.getPlanName() : "", dataFont));
            table.addCell(new Phrase(plan.getPlanStatus() != null ? plan.getPlanStatus() : "", dataFont));
            table.addCell(
                    new Phrase(plan.getPlanStarDate() != null ? plan.getPlanStarDate().toString() : "", dataFont));
            table.addCell(new Phrase(plan.getPlanEndDate() != null ? plan.getPlanEndDate().toString() : "", dataFont));
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}
