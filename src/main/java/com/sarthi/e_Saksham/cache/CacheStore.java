package com.sarthi.e_Saksham.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CacheStore<K, V> {
    private static final Logger log = LoggerFactory.getLogger(CacheStore.class);

    private final Cache<K, V> cache;

    public CacheStore(int expiryDuration, TimeUnit timeUnit) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiryDuration, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public V get(@NotNull K key) {
        log.info("Inside CacheStore, Retrieving from Cache with key {}", key.toString());
        return cache.getIfPresent(key);
    }

    public void put(@NotNull K key, @NotNull V value) {
        log.info("Inside CacheStore, Storing record in Cache for key {}", key.toString());
        cache.put(key, value);
    }

    public void evict(@NotNull K key) {
        log.info("Inside CacheStore, Removing from Cache with key {}", key.toString());
        cache.invalidate(key);
    }
}
