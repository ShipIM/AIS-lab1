package org.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Pair<K, V> {
    private final K key;
    private final V value;

    @Override
    public String toString() {
        return "is a " + key;
    }
}
