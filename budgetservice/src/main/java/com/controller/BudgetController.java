package com.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Budget;
import com.service.BudgetService;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    BudgetService BS;

    public BudgetController(BudgetService BS) {
        this.BS = BS;
    }


    @PostMapping
    public Object addBudget(
            @RequestBody Budget budget) {

        return BS.addBudget(budget);
    }


    @GetMapping
    public Object getAllBudgets() {

        return BS.getAllBudgets();
    }


    @GetMapping("/{budgetId}")
    public Object getBudget(
            @PathVariable Long budgetId) {

        return BS.getBudget(budgetId);
    }


    @GetMapping("/user/{userId}")
    public Object getUserBudgets(
            @PathVariable Long userId) {

        return BS.getUserBudgets(userId);
    }


    @GetMapping("/category/{categoryId}")
    public Object getCategoryBudgets(
            @PathVariable Long categoryId) {

        return BS.getCategoryBudgets(categoryId);
    }


    @PutMapping("/{budgetId}")
    public Object updateBudget(
            @PathVariable Long budgetId,
            @RequestBody Budget budget) {

        return BS.updateBudget(
                budgetId,
                budget
        );
    }


    @DeleteMapping("/{budgetId}")
    public Object deleteBudget(
            @PathVariable Long budgetId) {

        return BS.deleteBudget(budgetId);
    }
}