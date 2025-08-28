/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kotteletfisk.todo_exer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author kotteletfisk
 */
public class TaskFactory {

    public static Task createTaskFromStrings(String name, String deadlineStr, String categoryStr, DateTimeFormatter dtf)
            throws DateTimeParseException,
            IllegalArgumentException {

        LocalDate deadline = LocalDate.parse(deadlineStr, dtf);
        ListCategory lc = ListCategory.parseFromStr(categoryStr);

        return new Task(name, deadline, lc);
    }

    public static Task createTaskFromStrings(String name, String deadlineStr, String categoryStr, int completedInt, DateTimeFormatter dtf)
            throws DateTimeParseException,
            IllegalArgumentException {

        LocalDate deadline = LocalDate.parse(deadlineStr, dtf);
        ListCategory lc = ListCategory.parseFromStr(categoryStr);

        if (completedInt > 1 || completedInt < 0) {
            throw new IllegalArgumentException("int cannot be parsed to boolean: " + completedInt);
        }

        return new Task(name, deadline, lc, completedInt == 1);
    }

}
