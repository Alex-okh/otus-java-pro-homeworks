package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.sessionmanager.DataBaseOperationException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;

    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    T result = entityClassMetaData.getConstructor()
                                                  .newInstance();
                    setFields(rs, result, entityClassMetaData.getAllFields());
                    return result;
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                throw new DataBaseOperationException("Database error", ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    @Override
    public List<T> findAll(Connection connection) {
        List<T> found = new ArrayList<>();
        dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), rs -> {
            try {
                while (rs.next()) {
                    T result = entityClassMetaData.getConstructor()
                                                  .newInstance();
                    setFields(rs, result, entityClassMetaData.getAllFields());
                    found.add(result);

                }
            } catch (SQLException ex) {
                throw new DataBaseOperationException("Database error", ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return found;
        });
        return found;
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                                           getInsertValues(entityClassMetaData.getFieldsWithoutId(), client));
    }

    @Override
    public void update(Connection connection, T client) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                                    getUpdateValues(entityClassMetaData.getFieldsWithoutId(),
                                                    entityClassMetaData.getIdField(), client));
    }

    private void setFields(ResultSet rs, T result, List<Field> fields) throws SQLException {
        for (Field field : fields) {
            setFieldValue(result, field, rs.getObject(field.getName()));
        }
    }

    private void setFieldValue(T result, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(result, value);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<Object> getInsertValues(List<Field> fields, T obj) {
        List<Object> values = new ArrayList<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                values.add(field.get(obj));
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return values;

    }

    private List<Object> getUpdateValues(List<Field> fields, Field idField, T obj) {
        List<Object> values = new ArrayList<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                values.add(field.get(obj));
            }
            values.add(idField.get(obj));
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return values;

    }
}
