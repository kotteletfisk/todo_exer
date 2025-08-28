/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kotteletfisk.todo_exer;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

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
}
