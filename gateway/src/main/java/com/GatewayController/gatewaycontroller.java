package com.GatewayController;

import org.springframework.web.bind.annotation.*;

import com.service.Gatewayservice;

@RestController
@RequestMapping("/gateway")
public class gatewaycontroller {

    private final Gatewayservice gatewayService;

    public gatewaycontroller(Gatewayservice gatewayService) {
        this.gatewayService = gatewayService;
    }

    // =================================================
    // AUTH SERVICE
    // =================================================

    // Register user
    @PostMapping("/auth/signup")
    public Object signup(@RequestBody String request) {

        return gatewayService.invokePostService(
                "authenticationservice",
                "auth/signup",
                request
        );
    }

    // Login
    @PostMapping("/auth/login")
    public Object login(@RequestBody String request) {

        return gatewayService.invokePostService(
                "authenticationservice",
                "auth/login",
                request
        );
    }

    // =================================================
    // ACCOUNT SERVICE
    // =================================================

    // Get account
    @GetMapping("/account/{userId}")
    public Object getAccount(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {

        return gatewayService.invokeServiceWithToken(
                "accountmodule",
                "account/" + userId,
                token
        );
    }

    // =================================================
    // CATEGORY SERVICE
    // =================================================

    // Get all categories
    @GetMapping("/categories")
    public Object getCategories(
            @RequestHeader("Authorization") String token) {

        return gatewayService.invokeServiceWithToken(
                "categoryservice",
                "categories",
                token
        );
    }

    // Add category
    @PostMapping("/categories")
    public Object addCategory(
            @RequestBody String request,
            @RequestHeader("Authorization") String token) {

        return gatewayService.invokePostServiceWithToken(
                "categoryservice",
                "categories",
                request,
                token
        );
    }

    // =================================================
    // EXPENSE SERVICE
    // =================================================

    // Add expense
    @PostMapping("/expenses")
    public Object addExpense(
            @RequestBody String request,
            @RequestHeader("Authorization") String token) {

        return gatewayService.invokePostServiceWithToken(
                "expenseservice",
                "expenses",
                request,
                token
        );
    }

    // Get user expenses
    @GetMapping("/expenses/{userId}")
    public Object getExpenses(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {

        return gatewayService.invokeServiceWithToken(
                "expenseservice",
                "expenses/user/" + userId,
                token
        );
    }

    // =================================================
    // BUDGET SERVICE
    // =================================================

    // Create budget
    @PostMapping("/budgets")
    public Object createBudget(
            @RequestBody String request,
            @RequestHeader("Authorization") String token) {

        return gatewayService.invokePostServiceWithToken(
                "budgetservice",
                "budgets",
                request,
                token
        );
    }

    // Get user budget
    @GetMapping("/budgets/{userId}")
    public Object getBudget(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {

        return gatewayService.invokeServiceWithToken(
                "budgetservice",
                "budgets/user/" + userId,
                token
        );
    }

    // =================================================
    // REPORT SERVICE
    // =================================================

    // Get monthly report
    @GetMapping("/reports/monthly/{userId}")
    public Object getMonthlyReport(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {

        return gatewayService.invokeServiceWithToken(
                "reportservice",
                "reports/monthly/" + userId,
                token
        );
    }

    // Get yearly report
    @GetMapping("/reports/yearly/{userId}")
    public Object getYearlyReport(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {

        return gatewayService.invokeServiceWithToken(
                "reportservice",
                "reports/yearly/" + userId,
                token
        );
    }
}