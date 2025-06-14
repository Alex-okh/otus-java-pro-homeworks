package ru.otus.jdbc.mapper;

import ru.otus.crm.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;


public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Field idField;
    private final String name;
    private final Class<?> clazz;
    private final Constructor<T> constructor;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) throws NoSuchMethodException {
        this.name = clazz.getSimpleName()
                         .toLowerCase();
        this.clazz = clazz;
        this.constructor = clazz.getConstructor();
        this.idField = Arrays.stream(clazz.getDeclaredFields())
                             .filter(field -> field.isAnnotationPresent(Id.class))
                             .findFirst()
                             .orElseThrow();
        this.fieldsWithoutId = Arrays.stream(clazz.getDeclaredFields())
                                     .filter(field -> !field.isAnnotationPresent(Id.class))
                                     .toList();
        this.allFields = List.of(clazz.getDeclaredFields());

    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
