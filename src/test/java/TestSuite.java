
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.kotteletfisk.todo_exer.ListCategory;
import org.kotteletfisk.todo_exer.PersistenceManager;
import org.kotteletfisk.todo_exer.Task;
import org.kotteletfisk.todo_exer.TaskFactory;
import org.kotteletfisk.todo_exer.TaskManager;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class TestSuite {

    private final String TESTDB_URL = "jdbc:sqlite:src/test/java/test.db";
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final PersistenceManager pm = new PersistenceManager(dtf);

//  INTEGRATION TESTING *****************************************************************************
    @Test
    @BeforeEach
    @DisplayName("Test Connection to SQLite test Database")
    void testDatabaseConnection() {

        String dropSql = "DROP TABLE IF EXISTS tasks;";
        String createSql = "CREATE TABLE tasks ("
                + "	name TEXT PRIMARY KEY,"
                + "	isCompleted INTEGER,"
                + "	deadline TEXT,"
                + "	category TEXT"
                + ");";

        try (var conn = DriverManager.getConnection(TESTDB_URL); var stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(dropSql);
            stmt.execute(createSql);
        } catch (SQLException e) {
            fail("Failed to get SQLite object: " + e.getClass().getName() + ": " + e.getMessage());
        }
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
            pm.createTask(t, conn, tm.dateTimeFormatter);
        } catch (SQLException e) {
            fail("Failed insert into SQLite table: " + e.getClass().getName() + ": " + e.getMessage());
        }

        var sql = "SELECT * FROM tasks WHERE name = '" + name + "';";
        Task fetched_t = null;

        try (var conn2 = DriverManager.getConnection(TESTDB_URL); var stmt = conn2.createStatement(); var rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String fetchedName = rs.getString("name");
                String fetchedDeadline = rs.getString("deadline");
                String fetchedCategory = rs.getString("category");
                int fetchedCompleted = rs.getInt("isCompleted");

                fetched_t = TaskFactory.createTaskFromStrings(fetchedName, fetchedDeadline, fetchedCategory, fetchedCompleted, tm.dateTimeFormatter);
            }

            assertNotNull(fetched_t);
            assertEquals(false, fetched_t.isCompleted);

        } catch (SQLException e) {
            fail("Failed to get SQLite object: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    //Test Fetch All Tasks
    @Test
    @DisplayName("Test Fetch All task from SQLlite")
    void testFetchAllTasks() throws Exception {
        // Arrange
        try (Connection c = DriverManager.getConnection(TESTDB_URL); var stmt = c.createStatement()) {

            // clean table so test is predictable
            stmt.execute("DELETE FROM tasks");
            // insert data
            stmt.execute("INSERT INTO tasks(name, isCompleted, deadline, category) VALUES('task1', 0, '2025-08-28', 'LOW')");
            stmt.execute("INSERT INTO tasks(name, isCompleted, deadline, category) VALUES('task2', '1', '2026-08-28', 'HIGH')");

            TaskManager tm = new TaskManager();

            // Act
            //var tasks = tm.fetchAllTask(c)
        }
    }

    @Test
    @DisplayName("Fetch a single task")
    void testFetchTask() {

        String testName = "test";
        try (Connection c = DriverManager.getConnection(TESTDB_URL); var stmt = c.createStatement()) {

            // clean table so test is predictable
            stmt.execute("DELETE FROM tasks");
            // insert data
            stmt.execute("INSERT INTO tasks(name, isCompleted, deadline, category) VALUES('" + testName + "', 0, '28-08-2025', 'LOW')");

        } catch (SQLException e) {
            fail("Could not insert test task into database");
        }
        try (Connection c = DriverManager.getConnection(TESTDB_URL)) {
            Task t = pm.fetchTask(testName, c);
            assertNotNull(t);
            assertEquals(ListCategory.LOW, t.category);
        } catch (SQLException e) {
            fail("Failed to fetch test task: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Delete A task")
    void testDeleteTask() {

        
    }

//  APPLICATION TESTING ******************************************************************************
    @ParameterizedTest
    @DisplayName("Convert String to Category enum test")
    @CsvSource({
        "1, LOW",
        "2, MID",
        "3, HIGH",
        "0, DEFAULT",
        "f, DEFAULT",
        "LOW, LOW",
        "MID, MID",
        "HIGH, HIGH",})
    void testEnumConversion(String input, ListCategory expected) {
        // Numbers 1-3 should return enums LOW, MID, HIGH.
        // All else returns DEFAULT
        assertEquals(expected, ListCategory.parseFromStr(input));
    }

    @Test
    @DisplayName("Task parse happy path test")
    void testTaskInput() {
        // Task should be succesfully created from correct parsed string inputs.
        TaskManager tm = new TaskManager();

        // Simulated user input
        String name = "navn";
        String deadlineStr = "12-12-2025";
        String categoryStr = "1";

        Task t = TaskFactory.createTaskFromStrings(name, deadlineStr, categoryStr, tm.dateTimeFormatter);

        assertNotNull(t);
        assertEquals("navn", t.name);
        assertEquals(ListCategory.LOW, t.category);
    }

    @Test
    @DisplayName("Parse Exception on wrong datetime format")
    void testTaskInputWrongDateFormat() {
        TaskManager tm = new TaskManager();

        // Simulated user input
        String name = "navn";
        String deadlineStr = "12/12/2025";
        String categoryStr = "1";

        assertThrows(DateTimeParseException.class, () -> {
            TaskFactory.createTaskFromStrings(name, deadlineStr, categoryStr, tm.dateTimeFormatter);
        });

    }

    @Test
    @DisplayName("Parse Exception on wrong boolean format")
    void testTaskInputWrongBooleanFormat() {
        TaskManager tm = new TaskManager();

        // Simulated user input
        String name = "navn";
        String deadlineStr = "12-12-2025";
        String categoryStr = "1";
        int isCompletedInt = 2;

        assertThrows(IllegalArgumentException.class, () -> {
            TaskFactory.createTaskFromStrings(name, deadlineStr, categoryStr, isCompletedInt, tm.dateTimeFormatter);
        });
    }
}
