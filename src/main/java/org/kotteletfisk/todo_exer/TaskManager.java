/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.kotteletfisk.todo_exer;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.Scanner; 

/**
 *
 * @author kotteletfisk
 */
public class TaskManager {

    public DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void runTaskManager() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
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
                    Task t = Task.createTaskFromStrings(name, deadlineInput, categoryInput, scanner, dateTimeFormatter);
                    // addTask(t);
                }
                default -> throw new AssertionError();
            }
        }
    }

    public void addTask(Task t, Connection c) {
        
    }

    public String printAndInput(String print, Scanner scanner) {
        System.out.println(print);
        return scanner.nextLine();
    }
}
