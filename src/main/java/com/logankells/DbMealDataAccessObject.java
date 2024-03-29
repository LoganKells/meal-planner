package com.logankells;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbMealDataAccessObject implements MealDataAccessObject {
    private static final String DB_URL = "jdbc:postgresql:meals_db";
    private static final String USER = "postgres";
    private static final String PASS = "1111";
    private static final String CREATE_MEALS = """
                CREATE TABLE IF NOT EXISTS meals (
                    meal_id INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                    category VARCHAR,
                    meal VARCHAR
                );
                """;
    private static final String CREATE_INGREDIENTS = """
                CREATE TABLE IF NOT EXISTS ingredients (
                    ingredient_id INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                    meal_id INTEGER REFERENCES meals(meal_id),
                    ingredient VARCHAR
                );
                """;
    private final DbClient dbClient;

    public DbMealDataAccessObject() {
        // Connect to the database
        dbClient = new DbClient();
        dbClient.setUrl(DB_URL);
        dbClient.setUser(USER);
        dbClient.setPass(PASS);

        // Create the data structure.
        createTables();
    }

    public void createTables() {
        dbClient.executeUpdateRawSql(CREATE_MEALS);
        dbClient.executeUpdateRawSql(CREATE_INGREDIENTS);
    }


    /**
     * Query all meals and find the ingredients for each meal
     * and return a List<Meal>.
     * */
    @Override
    public List<Meal> findAllByCategory(String searchCategory, boolean orderByMealName) {
        List<Meal> meals = new ArrayList<>();
        String queryMeals;
        if (orderByMealName) {
            queryMeals = String.format("""
                    SELECT * FROM meals WHERE category = '%s' ORDER BY meal ASC
                    """, searchCategory);
        } else {
            queryMeals = String.format("""
                    SELECT * FROM meals WHERE category = '%s'
                    """, searchCategory);
        }


        try (
                Connection conn = dbClient.getConnection();
                Statement stmnt = conn.createStatement();
        ) {
            ResultSet resultSetFromMeals = stmnt.executeQuery(queryMeals);
            while (resultSetFromMeals.next()) {
                int mealId = resultSetFromMeals.getInt("meal_id");
                String mealName = resultSetFromMeals.getString("meal");
                String mealCategory = resultSetFromMeals.getString("category");

                Meal meal = hydrateMeal(mealId, mealName, mealCategory);
                meals.add(meal);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meals;
    }

    /**
    * Get ingredients for the current meal
    * */
    private Meal hydrateMeal(int mealId, String mealName, String category) {
        String queryIngredients = String.format("""
                        SELECT * FROM ingredients WHERE meal_id = %s;
                        """, mealId);
        List<String> ingredients = new ArrayList<>();
        try (
                Connection conn = dbClient.getConnection();
                Statement stmnt = conn.createStatement();
        ) {
            ResultSet resultSetFromIngredients = stmnt.executeQuery(queryIngredients);
            while (resultSetFromIngredients.next()) {
                String ingredient = resultSetFromIngredients.getString("ingredient");
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Meal(category, mealName, ingredients, mealId);
    }


    @Override
    public Meal findById(int id) {
        String queryMeals = "SELECT * FROM meals WHERE meal_id = ?;";
        try (
                Connection connection = dbClient.getConnection();
        ) {
            PreparedStatement statement = connection.prepareStatement(queryMeals);
            statement.setInt(1, id);

            // Use the first row in the query response as the return Meal.
            ResultSet resultSetFromMeals = statement.executeQuery();
            resultSetFromMeals.next();
            int mealId = resultSetFromMeals.getInt("meal_id");
            String mealName = resultSetFromMeals.getString("meal");
            String mealCategory = resultSetFromMeals.getString("category");
            return hydrateMeal(mealId, mealName, mealCategory);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Meal findByMealNameAndCategory(String name, String category) {
        String queryMeals = "SELECT * FROM meals WHERE category = ? AND meal = ?;";
        try (
                Connection connection = dbClient.getConnection();
        ) {
            PreparedStatement statement = connection.prepareStatement(queryMeals);
            statement.setString(1, category);
            statement.setString(2, name);

            // Use the first row in the query response as the return Meal.
            ResultSet resultSetFromMeals = statement.executeQuery();
            resultSetFromMeals.next();
            int mealId = resultSetFromMeals.getInt("meal_id");
            String mealName = resultSetFromMeals.getString("meal");
            String mealCategory = resultSetFromMeals.getString("category");
            return hydrateMeal(mealId, mealName, mealCategory);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Meal meal) {
        String insertMeal = "INSERT INTO meals (category, meal) VALUES (?, ?);";
        String insertIngredient = "INSERT INTO ingredients (meal_id, ingredient) VALUES (?, ?);";
        try (
                Connection connection = dbClient.getConnection();
        ) {
            PreparedStatement mealStatement = connection.prepareStatement(insertMeal, Statement.RETURN_GENERATED_KEYS);
            mealStatement.setString(1, meal.category);
            mealStatement.setString(2, meal.name);
            int rowsUpdated = mealStatement.executeUpdate();

            if (rowsUpdated > 0) {
                ResultSet primaryKeys = mealStatement.getGeneratedKeys();
                if (primaryKeys.next()) {
                    for (String ingredient : meal.ingredients) {
                        PreparedStatement ingredientStatement = connection.prepareStatement(insertIngredient);
                        ingredientStatement.setInt(1, primaryKeys.getInt(1));
                        ingredientStatement.setString(2, ingredient);
                        ingredientStatement.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Meal meal) {

    }

    @Override
    public void deleteById(int id) {

    }
}
