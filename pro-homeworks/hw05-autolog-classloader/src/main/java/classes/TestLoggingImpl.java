package classes;

import annotations.Log;

public class TestLoggingImpl implements TestLogging {
    public TestLoggingImpl() {}

    @Override
    @Log
    public void calculation(int param1) {
        System.out.println("METHOD OUTPUT: calculation with 1 parameter. Result: " + param1);
    }

    @Override
    @Log
    public void calculation(int param1, int param2) {
        System.out.println("METHOD OUTPUT: calculation with 2 parameters. Result: " + (param1+param2));
    }

    @Override
    @Log
    public void calculation(int param1, int param2, int param3) {
        System.out.println("METHOD OUTPUT: calculation with 3 parameters. Result: " + (param1+param2+param3));

    }
}
