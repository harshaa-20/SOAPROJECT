package com.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.model.Budget;
import com.repo.BudgetRepo;

@Service
public class BudgetService {

    BudgetRepo BR;

    public BudgetService(BudgetRepo BR) {
        this.BR = BR;
    }


    // Add Budget
    public Object addBudget(Budget budget) {

        Map<String, Object> response = new HashMap<>();

        if (budget.getUserId() == null ||
            budget.getCategoryId() == null ||
            budget.getBudgetAmount() <= 0 ||
            budget.getStartDate() == null ||
            budget.getEndDate() == null) {

            response.put("code", 400);
            response.put("message", "Required Data Missing");

            return response;
        }


        if (budget.getEndDate()
                .isBefore(budget.getStartDate())) {

            response.put("code", 400);
            response.put(
                    "message",
                    "End Date Cannot Be Before Start Date"
            );

            return response;
        }


        if (budget.getSpentAmount() < 0) {

            budget.setSpentAmount(0);
        }


        if (budget.getSpentAmount() >
                budget.getBudgetAmount()) {

            response.put("code", 400);
            response.put(
                    "message",
                    "Spent Amount Cannot Exceed Budget Amount"
            );

            return response;
        }


        if (budget.getStatus() == null ||
            budget.getStatus().isBlank()) {

            budget.setStatus("ACTIVE");
        }


        Budget savedBudget = BR.save(budget);

        response.put("code", 200);
        response.put(
                "status",
                "Budget Added Successfully"
        );
        response.put(
                "budgetId",
                savedBudget.getBudgetId()
        );

        response.put(
                "userId",
                savedBudget.getUserId()
        );

        response.put(
                "categoryId",
                savedBudget.getCategoryId()
        );

        response.put(
                "budgetAmount",
                savedBudget.getBudgetAmount()
        );

        return response;
    }


    // Get All Budgets
    public Object getAllBudgets() {

        Map<String, Object> response = new HashMap<>();

        List<Budget> budgets = BR.findAll();

        response.put("code", 200);
        response.put("status", "Success");
        response.put("count", budgets.size());
        response.put("budgets", budgets);

        return response;
    }


    // Get Budget by ID
    public Object getBudget(Long budgetId) {

        Map<String, Object> response = new HashMap<>();

        if (!BR.existsById(budgetId)) {

            response.put("code", 404);
            response.put("message", "Budget Not Found");

            return response;
        }


        Budget budget = BR.findById(budgetId).get();

        response.put("code", 200);
        response.put("status", "Success");
        response.put("budget", budget);

        return response;
    }


    // Get Budgets by User
    public Object getUserBudgets(Long userId) {

        Map<String, Object> response = new HashMap<>();

        List<Budget> budgets =
                BR.findByUserId(userId);

        response.put("code", 200);
        response.put("status", "Success");
        response.put("userId", userId);
        response.put("count", budgets.size());
        response.put("budgets", budgets);

        return response;
    }


    // Get Budgets by Category
    public Object getCategoryBudgets(Long categoryId) {

        Map<String, Object> response = new HashMap<>();

        List<Budget> budgets =
                BR.findByCategoryId(categoryId);

        response.put("code", 200);
        response.put("status", "Success");
        response.put("categoryId", categoryId);
        response.put("count", budgets.size());
        response.put("budgets", budgets);

        return response;
    }


    // Update Budget
    public Object updateBudget(
            Long budgetId,
            Budget budget) {

        Map<String, Object> response = new HashMap<>();

        if (!BR.existsById(budgetId)) {

            response.put("code", 404);
            response.put("message", "Budget Not Found");

            return response;
        }


        Budget existingBudget =
                BR.findById(budgetId).get();


        if (budget.getBudgetAmount() > 0) {

            existingBudget.setBudgetAmount(
                    budget.getBudgetAmount()
            );
        }


        if (budget.getSpentAmount() >= 0) {

            if (budget.getSpentAmount() >
                    existingBudget.getBudgetAmount()) {

                response.put("code", 400);
                response.put(
                        "message",
                        "Spent Amount Cannot Exceed Budget Amount"
                );

                return response;
            }

            existingBudget.setSpentAmount(
                    budget.getSpentAmount()
            );
        }


        if (budget.getStartDate() != null) {

            existingBudget.setStartDate(
                    budget.getStartDate()
            );
        }


        if (budget.getEndDate() != null) {

            existingBudget.setEndDate(
                    budget.getEndDate()
            );
        }


        if (budget.getStatus() != null &&
            !budget.getStatus().isBlank()) {

            existingBudget.setStatus(
                    budget.getStatus()
            );
        }


        Budget updatedBudget =
                BR.save(existingBudget);

        response.put("code", 200);
        response.put(
                "status",
                "Budget Updated Successfully"
        );
        response.put(
                "budget",
                updatedBudget
        );

        return response;
    }


    // Delete Budget
    public Object deleteBudget(Long budgetId) {

        Map<String, Object> response = new HashMap<>();

        if (!BR.existsById(budgetId)) {

            response.put("code", 404);
            response.put("message", "Budget Not Found");

            return response;
        }


        BR.deleteById(budgetId);

        response.put("code", 200);
        response.put(
                "status",
                "Budget Deleted Successfully"
        );

        return response;
    }
}