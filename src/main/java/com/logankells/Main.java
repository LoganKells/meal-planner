package com.logankells;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

  static final List<String> DAYS_OF_WEEK = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
//  static final List<String> DAYS_OF_WEEK = List.of("Monday", "Tuesday");

  static final List<String> MEAL_CATEGORIES = List.of("breakfast", "lunch", "dinner");
  static DbMealDataAccessObject mealDao = new DbMealDataAccessObject();
  static DbPlanDataAccessObject planDao = new DbPlanDataAccessObject();

  private static Meal createMeal(int mealId) {
    String category = InputUtil.inputMealCategory(true);
    String name = InputUtil.inputMealName();
    ArrayList<String> ingredients = InputUtil.inputMealIngredients();
    return new Meal(category, name, ingredients, mealId);
  }

  private static void printMeals(List<Meal> meals, String category) {
    if (meals.isEmpty()) {
      System.out.println("No meals found.");
    } else {
      System.out.println("Category: " + category);
      for (Meal meal : meals) {
        System.out.println(meal);
      }
    }
  }

  /**
   * Create the plan for each day of the week. Each day has
   * breakfast, lunch, and dinner categories in the plan.
   * */
  private static void getPlan() {
    for (String day : DAYS_OF_WEEK) {
      System.out.println(day);
      for (String category : MEAL_CATEGORIES) {
        List<Meal> mealsFromDb = mealDao.findAllByCategory(category, true);
        printMealNames(mealsFromDb);
        String mealName = InputUtil.chooseMeal(mealsFromDb, category, day);
        Meal mealSelected = mealDao.findByMealNameAndCategory(mealName, category);
        Plan plannedMeal = new Plan(mealSelected.id, day, category);
        planDao.add(plannedMeal);
      }
      System.out.println("Yeah! We planned the meals for " + day + ".");
    }
  }

  private static void printPlan() {
      for (String day : DAYS_OF_WEEK) {
        System.out.println(day);
        for (String category : MEAL_CATEGORIES) {
          Plan plan = planDao.findPlanByDayAndCategory(day, category);
          if (plan == null) {
            System.out.println(category + ":");
          } else {
            Meal meal = mealDao.findById(plan.mealId);
            System.out.println(category + ": " + meal.name);
          }
        }
        System.out.println();
      }
  }

  private static void printMealNames(List<Meal> meals) {
    for (Meal meal : meals) {
      System.out.println(meal.name);
    }
  }

  private static void savePlannedIngredientToFile(Map<String, Integer> ingredientCounts) {
    String filename = InputUtil.inputFileName();
    File dataFile = new File("./" + filename);
    try (PrintWriter printWriter = new PrintWriter(dataFile)) {
      for (Map.Entry<String, Integer> entry : ingredientCounts.entrySet()) {
        String ingredient = entry.getKey();
        int count = entry.getValue();
        if (count > 1) {
          String countFormatted = " x" + count;
          printWriter.println(ingredient + countFormatted);
        } else {
          printWriter.println(ingredient);
        }
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    System.out.println("Saved!");
  }

  public static void main(String[] args) {
    int mealId = 1;  // incrementing ID for primary key
    while (true) {
      String userSelection = InputUtil.inputWhatToDo();
      if ("add".equals(userSelection)) {
        Meal newMeal = createMeal(mealId);
        mealDao.add(newMeal);
        System.out.println("The meal has been added!");
        mealId++;
      } else if ("show".equals(userSelection)) {
        String category = InputUtil.inputMealCategory(false);
        List<Meal> mealsFromDb = mealDao.findAllByCategory(category, false);
        printMeals(mealsFromDb, category);
      } else if ("plan".equals(userSelection)) {
        getPlan();
        printPlan();
      } else if ("save".equals(userSelection)) {
          int planCount = planDao.getPlanCount();
          if (planCount <= 0) {
            System.out.println("Unable to save. Plan your meals first.");
          } else {
            Map<String, Integer> ingredientCounts = planDao.getPlannedIngredientCounts();
            savePlannedIngredientToFile(ingredientCounts);
          }
      } else if ("exit".equals(userSelection)) {
        System.out.println("Bye!");
        System.exit(0);
      }
    }

  }
}