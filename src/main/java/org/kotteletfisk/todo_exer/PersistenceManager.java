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

    DateTimeFormatter dtf;

    public PersistenceManager(DateTimeFormatter dtf) {
        this.dtf = dtf;
    }

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

    public Task fetchTask(String name ,Connection c) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE name = ?";

        try (var pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String deadlineStr = rs.getString("deadline");
                    String categoryStr = rs.getString("category");
                    int isCompletedInt = rs.getInt("isCompleted");

                    return TaskFactory.createTaskFromStrings(name, deadlineStr, categoryStr, isCompletedInt, dtf);
                }
            }
        }
        return null;
    }

    public List<Task> fetchAllTasks(Connection c) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT name, isCompleted, deadline, category FROM tasks";

        try (var stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String deadlineStr = rs.getString("deadline");
                String categoryStr = rs.getString("category");

                Task t = TaskFactory.createTaskFromStrings(name, deadlineStr, categoryStr, rs.getInt("isCompleted"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                tasks.add(t);
            }

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
