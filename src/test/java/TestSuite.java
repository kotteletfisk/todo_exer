import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.kotteletfisk.todo_exer.Other_class;

class TestSuite {

       @Test
       public void test1() {
        Other_class oc = new Other_class();
        int actual = oc.add(2, 3);
        int expected = 5;
        assertEquals(actual, expected);
        System.out.println("Test executed");
    }
}