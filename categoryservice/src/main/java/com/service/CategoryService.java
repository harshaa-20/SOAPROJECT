package com.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.model.Category;
import com.repo.CategoryRepo;

@Service
public class CategoryService {

    CategoryRepo CR;

    public CategoryService(CategoryRepo CR) {
        this.CR = CR;
    }

    // Add Category
    public Object addCategory(Category category) {

        Map<String, Object> response = new HashMap<>();

        if (category.getName() == null ||
            category.getName().isBlank()) {

            response.put("code", 400);
            response.put("message", "Category Name Missing");

            return response;
        }

        if (CR.existsByName(category.getName())) {

            response.put("code", 409);
            response.put("message", "Category Already Exists");

            return response;
        }

        if (category.getStatus() == null ||
            category.getStatus().isBlank()) {

            category.setStatus("ACTIVE");
        }

        Category savedCategory = CR.save(category);

        response.put("code", 200);
        response.put("status", "Category Added Successfully");
        response.put("categoryId", savedCategory.getCategoryId());
        response.put("name", savedCategory.getName());

        return response;
    }


    // Get All Categories
    public Object getAllCategories() {

        Map<String, Object> response = new HashMap<>();

        List<Category> categories = CR.findAll();

        response.put("code", 200);
        response.put("status", "Success");
        response.put("count", categories.size());
        response.put("categories", categories);

        return response;
    }


    // Get Category by ID
    public Object getCategory(Long categoryId) {

        Map<String, Object> response = new HashMap<>();

        if (!CR.existsById(categoryId)) {

            response.put("code", 404);
            response.put("message", "Category Not Found");

            return response;
        }

        Category category = CR.findById(categoryId).get();

        response.put("code", 200);
        response.put("status", "Success");
        response.put("category", category);

        return response;
    }


    // Update Category
    public Object updateCategory(
            Long categoryId,
            Category category) {

        Map<String, Object> response = new HashMap<>();

        if (!CR.existsById(categoryId)) {

            response.put("code", 404);
            response.put("message", "Category Not Found");

            return response;
        }

        Category existingCategory =
                CR.findById(categoryId).get();

        if (category.getName() != null &&
            !category.getName().isBlank()) {

            existingCategory.setName(category.getName());
        }

        if (category.getDescription() != null) {

            existingCategory.setDescription(
                    category.getDescription()
            );
        }

        if (category.getStatus() != null) {

            existingCategory.setStatus(
                    category.getStatus()
            );
        }

        Category updatedCategory =
                CR.save(existingCategory);

        response.put("code", 200);
        response.put("status", "Category Updated Successfully");
        response.put("category", updatedCategory);

        return response;
    }


    // Delete Category
    public Object deleteCategory(Long categoryId) {

        Map<String, Object> response = new HashMap<>();

        if (!CR.existsById(categoryId)) {

            response.put("code", 404);
            response.put("message", "Category Not Found");

            return response;
        }

        CR.deleteById(categoryId);

        response.put("code", 200);
        response.put("status", "Category Deleted Successfully");

        return response;
    }
}