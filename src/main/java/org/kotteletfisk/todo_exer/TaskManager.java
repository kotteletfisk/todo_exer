/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.kotteletfisk.todo_exer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner; 

/**
 *
 * @author kotteletfisk
 */
public class TaskManager {

    public DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public PersistenceManager pm = new PersistenceManager();
    private final String DB_URL = "jdbc:sqlite:tasks.db";

    public void runTaskManager() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {

            try (Connection c = DriverManager.getConnection(DB_URL)) {
                pm.initDB(c, DB_URL);
            } catch (SQLException e) {
                System.err.println("Failed to init db: " + e.getMessage());
                break;
            }
            System.out.println(
"""
Welcome to TaskMaster5000!

1. Add Task
            
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

                default -> throw new AssertionError();
            }
        }
    }

    public String printAndInput(String print, Scanner scanner) {
        System.out.println(print);
        return scanner.nextLine();
    }
}
