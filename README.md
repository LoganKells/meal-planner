# Meal Planner

## About

"Every day, people face a lot of difficult choices: for example, what to prepare for breakfast, lunch, and dinner? Are
the necessary ingredients in stock? With the Meal Planner, this can be quick and painless! You can make a database of
categorized meals and set the menu for the week. This app will also help create and store shopping lists based on the
meals so that no ingredient is missing." [1]

## Install and Run

- Install postgresql and setup database
  - Download and install: https://www.postgresql.org/download/
  - Create a new database with DB_URL = `jdbc:postgresql:meals_db`
  - Create a new user with USER = "`postgres`" and PASS = `1111`;
- Install Maven
  - Download and install: https://maven.apache.org/install.html
- `mvn exec:java` will start the program.

## User Interaction
The system input is used with the following commands to interact with the program:

- "add"
  - Program saves a meal with its properties — category, name, necessary ingredients.
  - The program stores all the meals in the Postgresql database and allows you to access them even after you close and reopen the app.
- "show"
  - The program has the ability to display the meals by the chosen category.
- "plan"
  - With the new "plan" command, the program helps the user plan their daily meals based on the saved options.
- "save"
  - The program generates a shopping list containing all the required ingredients for the planned meals and save this list to the file together with the weekly plan.
- "exit"
  - The program exits.

##  References

1. [Meal Planner (Java) Hyperskill project](https://hyperskill.org/projects/318)