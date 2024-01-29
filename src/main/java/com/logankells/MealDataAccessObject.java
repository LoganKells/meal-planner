package com.logankells;

import java.util.List;

public interface MealDataAccessObject {
    List<Meal> findAllByCategory(String searchCategory, boolean orderByMealName);
    Meal findById(int id);
    void add(Meal meal);
    void update(Meal meal);
    void deleteById(int id);
}
