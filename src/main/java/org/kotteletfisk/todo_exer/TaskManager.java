/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kotteletfisk.todo_exer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author kotteletfisk
 */
public class TaskManager {

    public DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public PersistenceManager pm = new PersistenceManager(dateTimeFormatter);
    private final String DB_URL = "jdbc:sqlite:tasks.db";

    public void runTaskManager() {
        Scanner scanner = new Scanner(System.in);
        String input;
        List<Task> fetchedTasks;

        try (Connection c = DriverManager.getConnection(DB_URL)) {
            pm.initDB(c, DB_URL);
        } catch (SQLException e) {
            System.err.println("Failed to init db: " + e.getMessage());
            return;
        }

        while (true) {

            try (Connection c = DriverManager.getConnection(DB_URL)) {
                fetchedTasks = pm.fetchAllTasks(c);
            } catch (SQLException e) {
                System.err.println("Failed to fetch tasks from db: " + e.getMessage());
                return;
            }

            // Print all tasks currently in DB
            printTasks(fetchedTasks);

            System.out.println(
                    """
Welcome to TaskMaster5000!

1. Add Task

3. Delete Task
            
""");
            input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    String name = printAndInput("Enter name: ", scanner);
                    String deadlineInput = printAndInput("Enter deadline date (dd-MM-yyyy): ", scanner);

                    String categoryInput = printAndInput(
                            """
Input Task category:
0. DEFAULT
1. LOW
2. MID
3. HIGH
""", scanner);
                    Task t = TaskFactory.createTaskFromStrings(name, deadlineInput, categoryInput, dateTimeFormatter);
                    try (Connection c = DriverManager.getConnection(DB_URL)) {
                        pm.createTask(t, c, dateTimeFormatter);
                    } catch (SQLException ex) {
                        System.err.println("Error creating task: " + ex.getMessage());
                    }
                }

                default ->
                    throw new AssertionError();
            }
        }
    }

    public String printAndInput(String print, Scanner scanner) {
        System.out.println(print);
        return scanner.nextLine();
    }

    public void initDatabase(Connection c) {
        try (var stmt = c.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS tasks ("
                    + "name TEXT PRIMARY KEY, "
                    + "isCompleted INTEGER, "
                    + "deadline TEXT, "
                    + "category TEXT)");
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public void insertTestTask(Connection c) {
        try (var stmt = c.createStatement()) {
            stmt.execute("DELETE FROM tasks");
            stmt.execute("INSERT INTO tasks (name, isCompleted, deadline, category) "
                    + "VALUES ('learn-fetch', 0, '2025-12-31', 'LOW')");
        } catch (Exception e) {
            System.out.println("Error inserting test task: " + e.getMessage());
        }
    }

    public void printTasks(List<Task> tasks) {
        System.out.println("=== Tasks fetched from DB ===");
        for (Task t : tasks) {
            System.out.println("Name: " + t.name);
            System.out.println("Deadline: " + t.deadline);
            System.out.println("Category: " + t.category);
            System.out.println("Completed: " + t.isCompleted);
            System.out.println("--------------------------");
        }
    }
}
