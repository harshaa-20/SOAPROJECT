package com.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.service.ReportService;

@RestController
public class ReportController {

    private final ReportService reportService;

    public ReportController(
            ReportService reportService) {

        this.reportService = reportService;
    }

    // =========================================================
    // MONTHLY REPORT
    // =========================================================

    @GetMapping("/reports/monthly/{userId}")
    public Map<String, Object> monthlyReport(
            @PathVariable Long userId) {

        return reportService.getMonthlyReport(userId);
    }

    // =========================================================
    // YEARLY REPORT
    // =========================================================

    @GetMapping("/reports/yearly/{userId}")
    public Map<String, Object> yearlyReport(
            @PathVariable Long userId) {

        return reportService.getYearlyReport(userId);
    }

    // =========================================================
    // SUMMARY REPORT
    // =========================================================

    @GetMapping("/reports/summary/{userId}")
    public Map<String, Object> summaryReport(
            @PathVariable Long userId) {

        return reportService.getSummary(userId);
    }
}