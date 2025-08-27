
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.kotteletfisk.todo_exer.Other_class;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class TestSuite {

    @Test
    @DisplayName("Custom test name")
    public void test1() {
        Other_class oc = new Other_class();
        int actual = oc.add(2, 3);
        int expected = 5;
        assertEquals(actual, expected);
        System.out.println("Test executed");
    }

    @Test
    public void lol() { 

    }

}
