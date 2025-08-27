/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.kotteletfisk.todo_exer;
import java.util.Scanner; 

/**
 *
 * @author kotteletfisk
 */
public class TaskManager {

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
                    // Task t = createTaskFromInput();
                    addTask();
                }
                default -> throw new AssertionError();
            }
        }
    }

    void addTask() {
        
    }

    public Task createTaskFromInput(String name, String deadline, String category) {
        return new Task();
    }

    public String printAndInput(String print) {
        Scanner s = new Scanner(System.in);
        System.out.println(print);
        return s.nextLine();
    }
}
