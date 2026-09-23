package com.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Expense;
import com.service.ExpenseService;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    ExpenseService ES;

    public ExpenseController(ExpenseService ES) {
        this.ES = ES;
    }

    @PostMapping
    public Object addExpense(@RequestBody Expense expense) {
        return ES.addExpense(expense);
    }

    @GetMapping
    public Object getAllExpenses() {
        return ES.getAllExpenses();
    }

    @GetMapping("/{expenseId}")
    public Object getExpense(
            @PathVariable Long expenseId) {

        return ES.getExpense(expenseId);
    }

    @GetMapping("/user/{userId}")
    public Object getUserExpenses(
            @PathVariable Long userId) {

        return ES.getUserExpenses(userId);
    }

    @GetMapping("/category/{categoryId}")
    public Object getCategoryExpenses(
            @PathVariable Long categoryId) {

        return ES.getCategoryExpenses(categoryId);
    }

    @DeleteMapping("/{expenseId}")
    public Object deleteExpense(
            @PathVariable Long expenseId) {

        return ES.deleteExpense(expenseId);
    }
}