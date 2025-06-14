package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private Map<K, V> cache;
    private List<HwListener<K, V>> listeners;


    MyCache() {
        cache = new WeakHashMap<>();
        listeners = new ArrayList<>();
    }

    // Надо реализовать эти методы

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, value, "Put");
        }

    }

    @Override
    public void remove(K key) {
        var removed = cache.remove(key);
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, removed, "Removed");
        }
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
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, result, notifyMessage);
        }
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
}
