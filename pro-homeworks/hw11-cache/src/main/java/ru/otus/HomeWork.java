package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.HwListenerLogger;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;
import ru.otus.cachehw.DBServiceCachedClient;

import javax.sql.DataSource;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) throws NoSuchMethodException {
        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient,
                                                        entityClassMetaDataClient); // реализация DataTemplate, универсальная

        // Работа с клиентом без кэша
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        Client finalClientSecond1 = clientSecond;
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                                                  .orElseThrow(() -> new RuntimeException(
                                                          "Client not found, id:" + finalClientSecond1.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        // Работа с клиентом с кэшированием
        HwListenerLogger<Long,Client> listener = new HwListenerLogger<>();
        var dbServiceCacheClient = new DBServiceCachedClient(transactionRunner, dataTemplateClient);
        dbServiceCacheClient.subscribe(listener);
        dbServiceCacheClient.saveClient(new Client("dbServiceFirst"));

        clientSecond = dbServiceCacheClient.saveClient(new Client("dbServiceSecond"));
        Client finalClientSecond = clientSecond;
        clientSecondSelected = dbServiceCacheClient.getClient(clientSecond.getId())
                                              .orElseThrow(() -> new RuntimeException(
                                                      "Client not found, id:" + finalClientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                           .dataSource(dataSource)
                           .locations("classpath:/db/migration")
                           .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
