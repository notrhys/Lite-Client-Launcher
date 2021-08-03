package me.mat1337.easy.gui.util.manager;

import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapManager<K, V> {

    @Getter
    private final Map<K, V> map = new HashMap<>();

    public void push(K key, V value) {
        map.put(key, value);
    }

    public void pop(K key) {
        map.remove(key);
    }

    public V at(K key) {
        return map.get(key);
    }

    public boolean hasKey(K key) {
        return map.containsKey(key);
    }

    public boolean hasValue(V value) {
        return map.containsValue(value);
    }

    public Set<K> keys() {
        return map.keySet();
    }

    public Collection<V> values() {
        return map.values();
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

}