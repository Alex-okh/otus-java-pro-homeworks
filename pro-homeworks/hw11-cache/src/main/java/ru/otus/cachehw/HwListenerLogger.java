package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HwListenerLogger<K,V> implements HwListener<K,V> {
    private static final Logger log = LoggerFactory.getLogger(HwListenerLogger.class);

    @Override
    public void notify(K key, V value, String action) {
        log.info("---Listener information below---");
        log.info("{} : {} : {}", action, key, value);
    }
}
