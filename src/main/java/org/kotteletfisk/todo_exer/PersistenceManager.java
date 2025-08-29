/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kotteletfisk.todo_exer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kotteletfisk
 */
public class PersistenceManager {

    public void createTask(Task t, Connection c, DateTimeFormatter dtf) throws SQLException {

        String sql = "INSERT INTO tasks VALUES(?, ?, ?, ?)";

        try (var pstmt = c.prepareStatement(sql)) {

            pstmt.setString(1, t.name);
            pstmt.setInt(2, t.isCompleted ? 1 : 0);
            pstmt.setString(3, t.deadline.format(dtf));
            pstmt.setString(4, t.category.toString());
            pstmt.executeUpdate();
        }
    }

    public void initDB(Connection c, String db_url) throws SQLException {
        String createSql = "CREATE TABLE IF NOT EXISTS tasks ("
                + "	name TEXT PRIMARY KEY,"
                + "	isCompleted INTEGER,"
                + "	deadline TEXT,"
                + "	category TEXT"
                + ");";
        try (var stmt = c.createStatement()) {
            // create a new table
            stmt.execute(createSql);
        }
    }

    public List<Task> fetchAllTasks(Connection c) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT name, isCompleted, deadline, category FROM tasks";

        try (var stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Query executed successfully!");

        } catch (Exception e) {
            System.out.println("Error fetching tasks: " + e.getMessage());
        }

        return tasks;
    }

    public void deleteTask(Connection c, String name) throws SQLException {

        String sql = "DELETE FROM tasks WHERE name = ?";

        try (var pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }
}
