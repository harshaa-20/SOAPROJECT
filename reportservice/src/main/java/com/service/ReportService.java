package com.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ReportService {

    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;

    public ReportService(
            DiscoveryClient discoveryClient,
            RestClient.Builder restClientBuilder) {

        this.discoveryClient = discoveryClient;
        this.restClient = restClientBuilder.build();
    }

    // =========================================================
    // GET EXPENSE SERVICE URL USING EUREKA
    // =========================================================

    private String getExpenseServiceUrl() {

        var instances =
                discoveryClient.getInstances("expenseservice");

        if (instances == null || instances.isEmpty()) {

            throw new RuntimeException(
                    "Expense Service is not available");
        }

        var instance = instances.get(0);

        return instance.getUri().toString();
    }

    // =========================================================
    // GET USER EXPENSES
    // =========================================================

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getUserExpenses(Long userId) {

        String url =
                getExpenseServiceUrl()
                + "/expenses/user/"
                + userId;

        System.out.println(
                "Calling Expense Service: " + url);

        try {

            Map<String, Object> response =
                    restClient.get()
                            .uri(url)
                            .retrieve()
                            .body(
                                    new ParameterizedTypeReference<
                                            Map<String, Object>>() {
                                    }
                            );

            if (response == null) {
                return new ArrayList<>();
            }

            Object expensesObject =
                    response.get("expenses");

            if (expensesObject == null) {
                return new ArrayList<>();
            }

            return (List<Map<String, Object>>) expensesObject;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to get expenses from Expense Service: "
                    + e.getMessage());
        }
    }

    // =========================================================
    // MONTHLY REPORT
    // =========================================================

    public Map<String, Object> getMonthlyReport(Long userId) {

        List<Map<String, Object>> expenses =
                getUserExpenses(userId);

        LocalDate today = LocalDate.now();

        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        double totalAmount = 0;

        int transactionCount = 0;

        Map<Long, Double> categoryTotals =
                new LinkedHashMap<>();

        for (Map<String, Object> expense : expenses) {

            String dateString =
                    String.valueOf(
                            expense.get("expenseDate"));

            LocalDate expenseDate =
                    LocalDate.parse(dateString);

            if (expenseDate.getMonthValue() == currentMonth
                    && expenseDate.getYear() == currentYear) {

                double amount =
                        Double.parseDouble(
                                String.valueOf(
                                        expense.get("amount")));

                totalAmount += amount;

                transactionCount++;

                Long categoryId =
                        Long.parseLong(
                                String.valueOf(
                                        expense.get("categoryId")));

                categoryTotals.put(
                        categoryId,
                        categoryTotals.getOrDefault(
                                categoryId,
                                0.0) + amount);
            }
        }

        Map<String, Object> response =
                new HashMap<>();

        response.put("code", 200);

        response.put("status", "Success");

        response.put("userId", userId);

        response.put(
                "month",
                today.getMonth().toString());

        response.put(
                "year",
                currentYear);

        response.put(
                "totalAmount",
                totalAmount);

        response.put(
                "transactionCount",
                transactionCount);

        response.put(
                "categoryWiseAmount",
                categoryTotals);

        return response;
    }

    // =========================================================
    // YEARLY REPORT
    // =========================================================

    public Map<String, Object> getYearlyReport(Long userId) {

        List<Map<String, Object>> expenses =
                getUserExpenses(userId);

        LocalDate today = LocalDate.now();

        int currentYear =
                today.getYear();

        double totalAmount = 0;

        int transactionCount = 0;

        Map<String, Double> monthlyTotals =
                new LinkedHashMap<>();

        for (Map<String, Object> expense : expenses) {

            String dateString =
                    String.valueOf(
                            expense.get("expenseDate"));

            LocalDate expenseDate =
                    LocalDate.parse(dateString);

            if (expenseDate.getYear() == currentYear) {

                double amount =
                        Double.parseDouble(
                                String.valueOf(
                                        expense.get("amount")));

                totalAmount += amount;

                transactionCount++;

                String month =
                        expenseDate.getMonth().toString();

                monthlyTotals.put(
                        month,
                        monthlyTotals.getOrDefault(
                                month,
                                0.0) + amount);
            }
        }

        Map<String, Object> response =
                new HashMap<>();

        response.put("code", 200);

        response.put("status", "Success");

        response.put("userId", userId);

        response.put(
                "year",
                currentYear);

        response.put(
                "totalAmount",
                totalAmount);

        response.put(
                "transactionCount",
                transactionCount);

        response.put(
                "monthlyAmount",
                monthlyTotals);

        return response;
    }

    // =========================================================
    // EXPENSE SUMMARY
    // =========================================================

    public Map<String, Object> getSummary(Long userId) {

        List<Map<String, Object>> expenses =
                getUserExpenses(userId);

        double totalAmount = 0;

        double highestAmount = 0;

        int transactionCount = expenses.size();

        for (Map<String, Object> expense : expenses) {

            double amount =
                    Double.parseDouble(
                            String.valueOf(
                                    expense.get("amount")));

            totalAmount += amount;

            if (amount > highestAmount) {
                highestAmount = amount;
            }
        }

        double averageAmount = 0;

        if (transactionCount > 0) {

            averageAmount =
                    totalAmount / transactionCount;
        }

        Map<String, Object> response =
                new HashMap<>();

        response.put("code", 200);

        response.put("status", "Success");

        response.put("userId", userId);

        response.put(
                "totalAmount",
                totalAmount);

        response.put(
                "transactionCount",
                transactionCount);

        response.put(
                "averageAmount",
                averageAmount);

        response.put(
                "highestExpense",
                highestAmount);

        return response;
    }
}