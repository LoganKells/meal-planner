package com.logankells;

public class Plan {
    int id;
    int mealId;
    String day;
    String category;

    public Plan(int mealId, String day, String category) {
//        this.mealByCategory = mealByCategory;
        this.mealId = mealId;
        this.day = day;
        this.category = category;
    }
}
