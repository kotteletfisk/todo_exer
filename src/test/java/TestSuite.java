
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import org.kotteletfisk.todo_exer.Task;
import org.kotteletfisk.todo_exer.TaskManager;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class TestSuite {

    @Test
    @DisplayName("Test Connection to SQLite Database")
    void testDatabaseConnection() {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/test/java/test.db");
        } catch (Exception e) {
            fail("Failed to get SQLite object: " + e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Opened database successfully");
    }

    @Test
    @DisplayName("Task input test")
    void testTaskInput() {
        TaskManager tm = new TaskManager();
        String name = "navn";
        String deadline = "12/12/2025";
        String category = "1";

        Task t = tm.createTaskFromInput(name, deadline, category);

        assertEquals("navn", t.name);
    }
}
