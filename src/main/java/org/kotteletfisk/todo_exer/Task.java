/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kotteletfisk.todo_exer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author kotteletfisk
 */
public class Task {

    public String name;
    public boolean isCompleted;
    public LocalDate deadline;
    public ListCategory category;

    public Task() {
    }

    public Task(String name, LocalDate deadline, ListCategory category) {
        this.name = name;
        this.isCompleted = false;
        this.deadline = deadline;
        this.category = category;
    }    
    
    public Task(String name, LocalDate deadline, ListCategory category, int isCompletedInt) {
        this.name = name;
        this.isCompleted = isCompletedInt == 1;
        this.deadline = deadline;
        this.category = category;
    }

    public static Task createTaskFromStrings(String name, String deadlineStr, String categoryStr, Scanner s, DateTimeFormatter dtf)
            throws DateTimeParseException,
            IllegalArgumentException {

        LocalDate deadline = LocalDate.parse(deadlineStr, dtf);
        ListCategory lc = ListCategory.parseFromStr(categoryStr);

        return new Task(name, deadline, lc);
    }
}
