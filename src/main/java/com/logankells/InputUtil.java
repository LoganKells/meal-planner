package com.logankells;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputUtil {
    static Scanner scanner = new Scanner(System.in);

    public static String inputWhatToDo() {
        while (true) {
            System.out.println("What would you like to do (add, show, plan, save, exit)?");
            String userSelection = scanner.nextLine();
            if ("add".equals(userSelection) || "show".equals(userSelection)
            || "plan".equals(userSelection) || "save".equals(userSelection)) {
                return userSelection;
            } else if ("exit".equals(userSelection)) {
                System.out.println("Bye!");
                System.exit(0);
            }
        }
    }

    public static String chooseMeal(List<Meal> meals, String category, String day) {
        List<String> mealNames = meals.stream().map(e -> e.name).toList();
        System.out.println("Choose the " + category + " for " + day + " from the list above:");
        while (true) {
            String userSelection = scanner.nextLine();
            if (!mealNames.contains(userSelection)) {
                System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            } else {
                return userSelection;
            }
        }

    }

    public static String inputMealCategory(boolean addMeal) {
        if (addMeal) {
            System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        } else {
            System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        }
        while (true) {
            String category = scanner.nextLine();
            if (!("breakfast".equals(category) || "lunch".equals(category) || "dinner".equals(category))) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            } else {
                return category;
            }
        }
    }

    public static String inputFileName() {
        System.out.println("Input a filename:");
        return scanner.nextLine();
    }

    private static boolean validateMealInput(String s) {
        if (s.isEmpty()) {
            return false;
        } else if (s.replaceAll("\\s", "").isEmpty()) {
            return false;
        }
        String sCleaned = s.replaceAll("[^a-zA-Z\\s]", "");
        return s.equals(sCleaned);
    }

    public static String inputMealName() {
        System.out.println("Input the meal's name:");
        while (true) {
            String name = scanner.nextLine();
            boolean validName = validateMealInput(name);
            if (!validName) {
                System.out.println("Wrong format. Use letters only!");
            } else {
                return name;
            }
        }
    }

    public static int countCommasReplace(String text) {
        int originalLength = text.length();
        int lengthWithoutCommas = text.replace(",", "").length();
        return originalLength - lengthWithoutCommas;
    }

    private static boolean validateIngredients(ArrayList<String> ingredients) {
        for (String ingredient : ingredients) {
            boolean validIngredient = validateMealInput(ingredient);
            if (!validIngredient) {
                System.out.println("Wrong format. Use letters only!");
                return false;
            }
        }
        return true;
    }

    public static ArrayList<String> inputMealIngredients() {
        System.out.println("Input the ingredients:");

        while (true) {
            boolean allIngredientsValid;
            String ingredientsInput = scanner.nextLine();

            // Evaluate how many ingredients the user input. If this number is not
            // maintained after String.split() is called, then we know trailing
            // spaces or commas are present.
            int inputIngredientCount = countCommasReplace(ingredientsInput) + 1;
            String[] ingredientsAfterSplit = ingredientsInput.split("\\s*,\\s*");
            ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(ingredientsAfterSplit));
            if (ingredientsAfterSplit.length != inputIngredientCount) {
                System.out.println("Wrong format. Use letters only!");
                allIngredientsValid = false;
            } else {
                allIngredientsValid = validateIngredients(ingredients);
            }
            if (allIngredientsValid) {
                return ingredients;
            }
        }
    }

    private InputUtil() {}
}
