package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.List;
import java.util.Optional;

public class DBServiceCachedClient implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final HwCache<Long, Client> cache;
    private final DBServiceClient simpleDBServiceClient;

    public DBServiceCachedClient(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        simpleDBServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplate);
        cache = new MyCache<>();
    }


    @Override
    public Client saveClient(Client client) {
        Client savedClient = simpleDBServiceClient.saveClient(client);
        cache.put(savedClient.getId(), savedClient);
        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(id);
        if (cachedClient != null) {
            return Optional.of(cachedClient);
        } else {
            Optional<Client> loadedClient = simpleDBServiceClient.getClient(id);
            if (loadedClient.isPresent()) {
                cache.put(loadedClient.get()
                                      .getId(), loadedClient.get());
                return loadedClient;
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = simpleDBServiceClient.findAll();
        for (Client client : clients) {
            cache.put(client.getId(), client);
        }
        return clients;
    }

    public void subscribe(HwListener<Long,Client> listener) {
        cache.addListener(listener);
    }

    public void unSubscribe(HwListener<Long,Client> listener) {
        cache.removeListener(listener);
    }
}
