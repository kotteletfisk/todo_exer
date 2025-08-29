
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.kotteletfisk.todo_exer.ListCategory;
import org.kotteletfisk.todo_exer.Task;
import org.kotteletfisk.todo_exer.TaskManager;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class TestSuite {

    private final String TESTDB_URL = "jdbc:sqlite:src/test/java/test.db";

//  INTEGRATION TESTING *****************************************************************************
    @Test
    @DisplayName("Test Connection to SQLite test Database")
    void testDatabaseConnection() {

        var sql = "CREATE TABLE IF NOT EXISTS tasks ("
        + "	name TEXT PRIMARY KEY,"
        + "	isCompleted INTEGER"
        + "	deadline TEXT"
        + "	category TEXT"
        + ");";

        try (var conn = DriverManager.getConnection(TESTDB_URL);
            var stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            fail("Failed to get SQLite object: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

//  APPLICATION TESTING ******************************************************************************
    @Test
    @DisplayName("Convert String to Category enum test")
    void testEnumConversion() {
        // Numbers 1-3 should return enums LOW, MID, HIGH.
        // All else returns DEFAULT

        String input1 = "1";
        String input2 = "2";
        String input3 = "3";
        String input4 = "0";
        String input5 = "f";

        assertEquals(ListCategory.LOW, ListCategory.parseFromStr(input1));
        assertEquals(ListCategory.MID, ListCategory.parseFromStr(input2));
        assertEquals(ListCategory.HIGH, ListCategory.parseFromStr(input3));
        assertEquals(ListCategory.DEFAULT, ListCategory.parseFromStr(input4));
        assertEquals(ListCategory.DEFAULT, ListCategory.parseFromStr(input5));
    }

    @Test
    @DisplayName("Task parse test")
    void testTaskInput() {
        // Task should be succesfully created from parsed string inputs.
        TaskManager tm = new TaskManager();
        String name = "navn";
        LocalDate deadline = LocalDate.parse("12-12-2025", tm.dateTimeFormatter);
        ListCategory category = ListCategory.parseFromStr("1");

        Task t = new Task(name, deadline, category);

        assertEquals("navn", t.name);
        assertEquals(LocalDate.parse("12-12-2025", tm.dateTimeFormatter), t.deadline);
        assertEquals(ListCategory.LOW, t.category);
    }

    @Test
    @DisplayName("Test task persistance to SQLite")
    void testTaskPersistence() {
        // A succesfully created task should be retrievable from database
        TaskManager tm = new TaskManager();
        String name = "testTaskPersistence";
        LocalDate deadline = LocalDate.parse("12-12-2025", tm.dateTimeFormatter);
        ListCategory category = ListCategory.parseFromStr("1");

        Task t = new Task(name, deadline, category);

        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/test/java/test.db");
        } catch (Exception e) {
            fail("Failed to get SQLite object: " + e.getClass().getName() + ": " + e.getMessage());
        }

        tm.addTask(t, c);

    }

    //Test Fetch All Tasks
    @Test
    @DisplayName("Test Fetch All task from SQLlite")
    void testFetchAllTasks() throws Exception {
        // Arrange
        try (Connection c = DriverManager.getConnection(TESTDB_URL);
            var stmt = c.createStatement()) {

            // clean table so test is predictable
            stmt.execute("DELETE FROM tasks");
            // insert data
            stmt.execute("INSERT INTO tasks(name, isCompleted, deadline, category) VALUES('task1', 0, '2025-08-28', 'LOW')");
            stmt.execute("INSERT INTO tasks(name, isCompleted, deadline, category) VALUES('task2, '1', '2026-08-28', 'HIGH')");

            TaskManager tm = new TaskManager();

            // Act
            //var tasks = tm.fetchAllTask(c)

        }
    }
    


}
