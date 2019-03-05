package com.vzurauskas.accountstransfers.misc;

import java.util.function.Supplier;

public final class Cached<T> implements Supplier<T> {

    private final Supplier<T> origin;
    private T cached;

    public Cached(Supplier<T> origin) {
        this.origin = origin;
    }

    @Override
    public T get() {
        if (cached == null) {
            cached = origin.get();
        }
        return cached;
    }
}
