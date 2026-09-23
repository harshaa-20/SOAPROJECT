package com.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.model.Expense;
import com.repo.ExpenseRepo;

@Service
public class ExpenseService {

    ExpenseRepo ER;
    DiscoveryClient discoveryClient;

    public ExpenseService(ExpenseRepo ER, DiscoveryClient discoveryClient) {
        this.ER = ER;
        this.discoveryClient = discoveryClient;
    }

    // Add Expense
    public Object addExpense(Expense expense) {

        Map<String, Object> response = new HashMap<>();

        // Basic validation
        if (expense.getUserId() == null ||
            expense.getCategoryId() == null ||
            expense.getAmount() <= 0 ||
            expense.getExpenseDate() == null) {

            response.put("code", 400);
            response.put("message", "Required Data Missing");
            return response;
        }

 
        try {

            List<org.springframework.cloud.client.ServiceInstance> instances =
                    discoveryClient.getInstances("categoryservice");

            if (instances == null || instances.isEmpty()) {

                response.put("code", 503);
                response.put("message", "Category Service is not available");
                return response;
            }

            // Get first available Category Service instance
            String categoryUrl =
                    instances.get(0).getUri().toString()
                    + "/categories/"
                    + expense.getCategoryId();

            RestClient client = RestClient.create();

            String categoryResponse = client.get()
                    .uri(categoryUrl)
                    .retrieve()
                    .body(String.class);

            System.out.println(
                    "Category Service Response: " + categoryResponse
            );

        } catch (Exception e) {

            System.out.println(
                    "Category Service communication failed: "
                    + e.getMessage()
            );

            response.put("code", 503);
            response.put("message", "Unable to verify category");
            return response;
        }

        // Default status
        if (expense.getStatus() == null ||
            expense.getStatus().isBlank()) {

            expense.setStatus("RECORDED");
        }

        // Save expense
        Expense savedExpense = ER.save(expense);

        response.put("code", 200);
        response.put("status", "Expense Added Successfully");
        response.put("expenseId", savedExpense.getExpenseId());
        response.put("userId", savedExpense.getUserId());
        response.put("categoryId", savedExpense.getCategoryId());
        response.put("amount", savedExpense.getAmount());

        return response;
    }


    // Get Expense by ID
    public Object getExpense(Long expenseId) {

        Map<String, Object> response = new HashMap<>();

        if (!ER.existsById(expenseId)) {

            response.put("code", 404);
            response.put("message", "Expense Not Found");

            return response;
        }

        Expense expense = ER.findById(expenseId).get();

        response.put("code", 200);
        response.put("status", "Success");
        response.put("expense", expense);

        return response;
    }


    // Get All Expenses
    public Object getAllExpenses() {

        Map<String, Object> response = new HashMap<>();

        List<Expense> expenses = ER.findAll();

        response.put("code", 200);
        response.put("status", "Success");
        response.put("count", expenses.size());
        response.put("expenses", expenses);

        return response;
    }


    // Get Expenses by User
    public Object getUserExpenses(Long userId) {

        Map<String, Object> response = new HashMap<>();

        List<Expense> expenses = ER.findByUserId(userId);

        response.put("code", 200);
        response.put("status", "Success");
        response.put("userId", userId);
        response.put("count", expenses.size());
        response.put("expenses", expenses);

        return response;
    }


    // Get Expenses by Category
    public Object getCategoryExpenses(Long categoryId) {

        Map<String, Object> response = new HashMap<>();

        List<Expense> expenses = ER.findByCategoryId(categoryId);

        response.put("code", 200);
        response.put("status", "Success");
        response.put("categoryId", categoryId);
        response.put("count", expenses.size());
        response.put("expenses", expenses);

        return response;
    }


    // Delete Expense
    public Object deleteExpense(Long expenseId) {

        Map<String, Object> response = new HashMap<>();

        if (!ER.existsById(expenseId)) {

            response.put("code", 404);
            response.put("message", "Expense Not Found");

            return response;
        }

        ER.deleteById(expenseId);

        response.put("code", 200);
        response.put("status", "Expense Deleted Successfully");

        return response;
    }
}