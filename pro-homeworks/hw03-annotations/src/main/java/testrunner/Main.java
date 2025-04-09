package testrunner;

import tests.SampleTest;

public class Main {
    public static void main(String[] args) {
       TestRunner.processTests(SampleTest.class);
    }
}
