package com.logankells;

import java.util.List;

public class Meal {
    String category;
    String name;
    List<String> ingredients;
    int id;

    public Meal(String category, String name, List<String> ingredients, int id) {
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder ingredientsForString = new StringBuilder("");
        for (String ingredient : ingredients) {
            ingredientsForString.append(ingredient).append("\n");
        }

        return "Name: " + name  + "\n" +
                "Ingredients:\n" + ingredientsForString;
    }

}
