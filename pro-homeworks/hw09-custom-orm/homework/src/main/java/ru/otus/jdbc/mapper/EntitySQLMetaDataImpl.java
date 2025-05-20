package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
  private final String selectAllSql;
  private final String insertSql;
  private final String updateSql;
  private final String selectByIdSql;

  public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
  this.selectAllSql = "SELECT * FROM %s;".formatted(entityClassMetaData.getName());
  this.selectByIdSql = "SELECT * FROM %s WHERE %s = ?;".formatted(entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
  this.insertSql = buildInsert (entityClassMetaData);
  this.updateSql = buildUpdate (entityClassMetaData);

  }


  @Override
  public String getSelectAllSql() {
    return selectAllSql;
  }

  @Override
  public String getSelectByIdSql() {
    return selectByIdSql;
  }

  @Override
  public String getInsertSql() {
    return insertSql;
  }

  @Override
  public String getUpdateSql() {
    return updateSql;
  }


  private String buildInsert (EntityClassMetaData<T> entityClassMetaData) {
    StringBuilder insertSqlBuild = new StringBuilder();
    insertSqlBuild.append("INSERT INTO %s (".formatted(entityClassMetaData.getName()));
    for (Field f : entityClassMetaData.getFieldsWithoutId()) {
      insertSqlBuild.append(f.getName().toLowerCase());
      insertSqlBuild.append(", ");
    }
    insertSqlBuild.delete(insertSqlBuild.length() - 2, insertSqlBuild.length());
    insertSqlBuild.append(") VALUES (");
    insertSqlBuild.append("?, ".repeat(entityClassMetaData.getFieldsWithoutId().size()));
    insertSqlBuild.delete(insertSqlBuild.length() - 2, insertSqlBuild.length());
    insertSqlBuild.append(");");
    return insertSqlBuild.toString();
  }

  private String buildUpdate (EntityClassMetaData<T> entityClassMetaData) {
    StringBuilder updateSqlBuild = new StringBuilder();
    updateSqlBuild.append("UPDATE %s SET ".formatted(entityClassMetaData.getName()));
    for (Field f : entityClassMetaData.getFieldsWithoutId()) {
      updateSqlBuild.append(f.getName().toLowerCase());
      updateSqlBuild.append(" = ?, ");
    }
    updateSqlBuild.delete(updateSqlBuild.length() - 2, updateSqlBuild.length());
    updateSqlBuild.append(" WHERE %s = ?;".formatted(entityClassMetaData.getIdField().getName()));
    return updateSqlBuild.toString();
  }
}
