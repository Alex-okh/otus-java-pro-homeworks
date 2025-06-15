package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.DBServiceCachedClient;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListenerLogger;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final int RECORDS_QUANTITY = 500; //4 DB objects created for each

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


        for (int i = 0; i < RECORDS_QUANTITY; i++) {
            dbServiceClient.saveClient(new Client("dbServiceFirst" + (i + 10000)));
            var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond" + (i + 10000)));
            var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                                                      .orElseThrow(() -> new RuntimeException(
                                                              "Client not found, id:" + clientSecond.getId()));
            log.info("clientSecondSelected:{}", clientSecondSelected);
        }


        // Работа с клиентом с кэшированием
        HwListenerLogger<String, Client> listener = new HwListenerLogger<>();
        HwCache<String, Client> clientCache = new MyCache<>();
        var dbServiceCacheClient = new DBServiceCachedClient(transactionRunner, dataTemplateClient, clientCache);
        dbServiceCacheClient.subscribe(listener);
        for (int i = 0; i < RECORDS_QUANTITY; i++) {
            dbServiceCacheClient.saveClient(new Client("dbServiceFirst" + i));

            var clientSecond = dbServiceCacheClient.saveClient(new Client("dbServiceSecond" + i));
            var clientSecondSelected = dbServiceCacheClient.getClient(clientSecond.getId())
                                                           .orElseThrow(() -> new RuntimeException(
                                                                   "Client not found, id:" + clientSecond.getId()));
            log.info("clientSecondSelected:{}", clientSecondSelected);
        }

        // Чтение с клиентом без кэша
        long timer = System.currentTimeMillis();

        for (int i = 0; i < RECORDS_QUANTITY; i++) {
            int id = (int) (Math.random() * RECORDS_QUANTITY * 6);
            var clientSecondSelected = dbServiceClient.getClient(id)
                                                      .orElse(null);
            log.info("clientSecondSelected:{}", clientSecondSelected);
        }
        long timeUncached = System.currentTimeMillis() - timer;

        // Чтение с кэшированным клиентом
        timer = System.currentTimeMillis();
        log.info("Reading {} objects cached: ", RECORDS_QUANTITY);
        for (int i = 0; i < RECORDS_QUANTITY; i++) {
            int id = (int) (Math.random() * RECORDS_QUANTITY * 6);
            var clientSecondSelected = dbServiceCacheClient.getClient(id)
                                                           .orElse(null);
            log.info("clientSecondSelected:{}", clientSecondSelected);

        }
        long timeCached = System.currentTimeMillis() - timer;

        log.info("---------------STATISTICS-------------------------");
        log.info("Not existing id probability: 33%");
        log.info("Reading {} objects uncached: time taken {}ms", RECORDS_QUANTITY, timeUncached);
        log.info("Reading {} objects cached: time taken {}ms", RECORDS_QUANTITY, timeCached);
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
