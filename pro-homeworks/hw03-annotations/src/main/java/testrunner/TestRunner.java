package testrunner;

import annotations.AfterEach;
import annotations.BeforeEach;
import annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {
    private final Map<String, TestResult> results = new HashMap<>();
    private final List<Method> beforeEachList = new ArrayList<>();
    private final List<Method> afterEachList = new ArrayList<>();

    public static <T> void processTests(Class<T> clazz) {
        TestRunner testRunner = new TestRunner();
        testRunner.process(clazz);
    }

    private <T> void process(Class<T> clazz) {
        try {
            initMethods(clazz);
            runTests(clazz);
            printResult();
        } catch (Exception ex) {
            System.out.println("Test failed: " + ex.getMessage());
        }
    }

    private void initMethods(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(BeforeEach.class)) {
                beforeEachList.add(method);
            }
            if (method.isAnnotationPresent(AfterEach.class)) {
                afterEachList.add(method);
            }
        }
    }

    private void runTests(Class<?> clazz) throws Exception {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                runSingleTest(method, clazz);
            }
        }
    }

    private void runSingleTest(Method method, Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = clazz.getConstructor();
        var testInstance = constructor.newInstance();
        for (Method m : beforeEachList) {
            m.invoke(testInstance);
        }
        try {
            method.invoke(testInstance);
            results.put(method.getName(), new TestResult(true));
        } catch (Exception ex) {
            results.put(method.getName(), new TestResult(false, ex));
        } finally {
            for (Method m : afterEachList) {
                m.invoke(testInstance);
            }
        }
    }

    private void printResult() {
        int passed = 0;
        int failed = 0;
        System.out.println("\n----------------Test results:-----------------\n");
        for (Map.Entry<String, TestResult> res : results.entrySet()) {
            if (res.getValue().isPassed()) {
                System.out.printf("Test <%s> has passed successfully%n", res.getKey());
                passed++;

            } else {
                System.out.printf("Test <%s> has failed%n", res.getKey());
                System.out.printf("Reason: %s %n", res.getValue().getExceptionTrowed());
                failed++;
            }
        }
        System.out.println("\n----------------Test statistics:---------------\n");
        System.out.printf("Total tests: %d. Passed: %d. Failed: %d%n", passed + failed, passed, failed);
    }


    private static class TestResult {
        private final boolean passed;
        private final Throwable exceptionTrowed;

        public TestResult(boolean passed, Throwable exceptionTrowed) {
            this.passed = passed;
            this.exceptionTrowed = exceptionTrowed;
        }

        public TestResult(boolean passed) {
            this(passed, null);
        }

        public boolean isPassed() {
            return passed;
        }

        public Throwable getExceptionTrowed() {
            return exceptionTrowed;
        }


    }


}
