package com.logankells;

public interface PlanDataAccessObject {
    Plan findPlanByDayAndCategory(String day, String category);

    void add(Plan plan);
    void update(Plan plan);
    void deleteById(int id);

}
