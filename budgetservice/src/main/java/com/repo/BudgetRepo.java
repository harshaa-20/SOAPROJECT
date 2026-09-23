package com.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Budget;

@Repository
public interface BudgetRepo extends JpaRepository<Budget, Long> {

    List<Budget> findByUserId(Long userId);

    List<Budget> findByCategoryId(Long categoryId);

    List<Budget> findByUserIdAndCategoryId(
            Long userId,
            Long categoryId
    );

    List<Budget> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate date1,
            LocalDate date2
    );
}