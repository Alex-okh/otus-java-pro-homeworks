package tests;

import annotations.AfterEach;
import annotations.BeforeEach;
import annotations.Test;

public class SampleTest {
    @BeforeEach
    public void setup() {
        System.out.println("before test");
    }

    @BeforeEach
    public void setup2() {
        System.out.println("before test, method 2");
    }

    @Test
    public void firstTestMethod() {
        System.out.println("first test processed");
    }

    @Test
    public void secondTestMethod() {
        System.out.println("second test throwing exception");
        throw new RuntimeException("Something went wrong in test #2");
    }

    @Test
    public void thirdTestMethod() {
        System.out.println("third test processed");
    }

    @AfterEach
    public void close() {
        System.out.println("after test");
    }

    @AfterEach
    public void close2() {
        System.out.println("after test, method2");


    }
}
