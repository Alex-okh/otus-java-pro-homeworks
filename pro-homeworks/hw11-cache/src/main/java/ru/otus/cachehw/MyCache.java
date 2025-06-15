package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> cache;
    private final List<HwListener<K, V>> listeners;


    public MyCache() {
        cache = new WeakHashMap<>();
        listeners = new ArrayList<>();
    }

    // Надо реализовать эти методы

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "Put");


    }

    @Override
    public void remove(K key) {
        var removed = cache.remove(key);
       notifyListeners(key, removed, "Removed");

    }

    @Override
    public V get(K key) {
        var result = cache.get(key);
        String notifyMessage;
        if (result != null) {
            notifyMessage = "Cache Hit";
        } else {
            notifyMessage = "Cache Miss";
        }
        notifyListeners(key, result, notifyMessage);

        return result;

    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String notifyMessage) {
        for (var listener : listeners) {
        try {
            listener.notify(key, value, notifyMessage);
        } catch (Exception e) {
            log.error("Listener exception: {}", e.getMessage());
        }
    }}
}
