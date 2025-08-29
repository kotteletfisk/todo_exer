/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.kotteletfisk.todo_exer;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author kotteletfisk
 */
public class Todo_exer {

    public static void main(String[] args) {
        //TaskManager tm = new TaskManager();
        //tm.runTaskManager();

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            TaskManager tm = new TaskManager();

            tm.initDatabase(c);
            tm.insertTestTask(c);
            var tasks = tm.fetchAllTasks(c);
            tm.printTasks(tasks);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
