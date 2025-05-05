package classes;

import annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Ioc {

    private static TestLogging loggingProxy = null;

    private Ioc() {
    }

    public static TestLogging getLoggingProxy() {
        if (Objects.isNull(loggingProxy)) {
            TestLogging originalObject = new TestLoggingImpl();
            InvocationHandler handler = new LogInvocationHandler(originalObject);
            loggingProxy = (TestLogging) Proxy.newProxyInstance(originalObject.getClass().getClassLoader(), originalObject.getClass().getInterfaces(), handler);
        }
        return loggingProxy;
    }

    private static class LogInvocationHandler implements InvocationHandler {
        private final Object target;
        private final Set<String> annotatedMethods;

        private LogInvocationHandler(Object target) {
            this.target = target;
            this.annotatedMethods = Arrays.stream(target.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Log.class))
                .map(method -> method.getName() + Arrays.toString(method.getParameterTypes()))
                .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (annotatedMethods.contains(method.getName() + Arrays.toString(method.getParameterTypes()))) {
                System.out.println("PROXY LOGGING: Executing method " + method.getName());
                System.out.print("PROXY LOGGING: parameters: ");
                System.out.println(Arrays.toString(args));

            }
            return method.invoke(target, args);
        }
    }
}
