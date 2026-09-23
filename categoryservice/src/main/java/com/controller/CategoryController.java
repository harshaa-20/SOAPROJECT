package com.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Category;
import com.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    CategoryService CS;

    public CategoryController(CategoryService CS) {
        this.CS = CS;
    }


    @PostMapping
    public Object addCategory(
            @RequestBody Category category) {

        return CS.addCategory(category);
    }


    @GetMapping
    public Object getAllCategories() {

        return CS.getAllCategories();
    }


    @GetMapping("/{categoryId}")
    public Object getCategory(
            @PathVariable Long categoryId) {

        return CS.getCategory(categoryId);
    }


    @PutMapping("/{categoryId}")
    public Object updateCategory(
            @PathVariable Long categoryId,
            @RequestBody Category category) {

        return CS.updateCategory(
                categoryId,
                category
        );
    }


    @DeleteMapping("/{categoryId}")
    public Object deleteCategory(
            @PathVariable Long categoryId) {

        return CS.deleteCategory(categoryId);
    }
}