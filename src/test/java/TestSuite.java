
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.kotteletfisk.todo_exer.ListCategory;
import org.kotteletfisk.todo_exer.Task;
import org.kotteletfisk.todo_exer.TaskManager;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class TestSuite {

    private final String TESTDB_URL = "jdbc:sqlite:src/test/java/test.db";
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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

        try (var conn = DriverManager.getConnection(TESTDB_URL); var stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            fail("Failed to get SQLite object: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

//  APPLICATION TESTING ******************************************************************************
    @ParameterizedTest
    @DisplayName("Convert String to Category enum test")
    @CsvSource({
        "1, LOW",
        "2, MID",
        "3, HIGH",
        "0, DEFAULT",
        "f, DEFAULT"
    })
    void testEnumConversion(String input, ListCategory expected) {
        // Numbers 1-3 should return enums LOW, MID, HIGH.
        // All else returns DEFAULT
        assertEquals(expected, ListCategory.parseFromStr(input));
    }

    @Test
    @DisplayName("Task parse test")
    void testTaskInput() {
        // Task should be succesfully created from correct parsed string inputs.
        TaskManager tm = new TaskManager();

        // Simulated user input
        String name = "navn";
        String deadlineStr = "12-12-2025";
        String categoryStr = "1";

        Task t = Task.createTaskFromStrings(name, deadlineStr, categoryStr, scanner, tm.dateTimeFormatter);

        assertNotNull(t);
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
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(TESTDB_URL);
            tm.addTask(t, conn);
        } catch (SQLException e) {
            fail("Failed insert into SQLite table: " + e.getClass().getName() + ": " + e.getMessage());
        }

        var sql = "SELECT * FROM tasks WHERE name = " + name;

        try (var conn2 = DriverManager.getConnection(TESTDB_URL);

            var stmt = conn2.createStatement();
            var rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    String fetchedName = rs.getString("name");
                    String fetchedDeadline = rs.getString("deadline");
                    String fetchedCategory = rs.getString("category");
                    int fetchedCompleted = rs.getInt("isCompleted");
                    
                    // Task fetched_t = new Task(fetchedName, fetchedDeadline, fetchedCategory, fetchedCompleted);
                }
            } catch (SQLException e) {
            fail("Failed to get SQLite object: " + e.getClass().getName() + ": " + e.getMessage());
        }
        }
    }
