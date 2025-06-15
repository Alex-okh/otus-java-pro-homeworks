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
    private static final Logger log = LoggerFactory.getLogger(DBServiceCachedClient.class);

    private final HwCache<String, Client> cache;
    private final DBServiceClient simpleDBServiceClient;

    public DBServiceCachedClient(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate, HwCache<String, Client> cache) {
        simpleDBServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplate);
        this.cache = cache;
    }


    @Override
    public Client saveClient(Client client) {
        Client savedClient = simpleDBServiceClient.saveClient(client);
        cache.put(savedClient.getId()
                             .toString(), savedClient);
        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(String.valueOf(id));
        if (cachedClient != null) {
            return Optional.of(cachedClient);
        } else {
            Optional<Client> loadedClient = simpleDBServiceClient.getClient(id);
            loadedClient.ifPresent(c -> cache.put(String.valueOf(id), c));
            return loadedClient;
        }

    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = simpleDBServiceClient.findAll();
        for (Client client : clients) {
            cache.put(client.getId()
                            .toString(), client);
        }
        return clients;
    }

    public void subscribe(HwListener<String, Client> listener) {
        cache.addListener(listener);
    }

    public void unSubscribe(HwListener<String, Client> listener) {
        cache.removeListener(listener);
    }
}
