package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        try {
            Object configInstance = configClass.getDeclaredConstructor()
                                               .newInstance();
            List<Method> annotatedMethods = Stream.of(configClass.getMethods())
                                                  .filter(m -> m.isAnnotationPresent(AppComponent.class))
                                                  .sorted(Comparator.comparingInt(
                                                          m -> m.getAnnotation(AppComponent.class)
                                                                .order()))
                                                  .toList();
            createComponents(annotatedMethods, configInstance);

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Could not create config class instance", e);
        }

    }

    private void createComponents(List<Method> componentMethods, Object configInstance) {


        for (Method method : componentMethods) {
            try {
                var methodArgTypes = List.of(method.getParameterTypes());
                List<Object> args = new ArrayList<>();
                for (var arg : methodArgTypes) {
                    args.add(getAppComponent(arg));
                }

                Object component = method.invoke(configInstance, args.toArray());
                addComponent(component, method.getAnnotation(AppComponent.class)
                                              .name());
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException("Could not create component %s".formatted(method.getName()), e);
            }
        }
    }

    private void addComponent(Object component, String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            throw new RuntimeException("Component " + componentName + " already exists. No duplicates supported.");
        }
        appComponents.add(component);
        appComponentsByName.put(componentName, component);
    }


    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> foundComponents = appComponents.stream()
                                                    .filter(c -> componentClass.isAssignableFrom(c.getClass()))
                                                    .toList();
        if (foundComponents.isEmpty()) {
            throw new IllegalArgumentException("No app component found for class " + componentClass.getName());
        }
        if (foundComponents.size() > 1) {
            throw new IllegalArgumentException(
                    "More than one app component found for class " + componentClass.getName());
        }
        return (C) foundComponents.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        } else {
            throw new IllegalArgumentException("No app component found for name " + componentName);
        }
    }
}
