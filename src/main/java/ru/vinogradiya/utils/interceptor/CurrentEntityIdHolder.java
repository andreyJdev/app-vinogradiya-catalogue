package ru.vinogradiya.utils.interceptor;

import java.util.Optional;
import java.util.UUID;

public final class CurrentEntityIdHolder {

    private static final ThreadLocal<UUID> CURRENT_ID = new ThreadLocal<>();

    private CurrentEntityIdHolder() {
    }

    public static void set(UUID id) {
        CURRENT_ID.set(id);
    }

    public static Optional<UUID> get() {
        return Optional.ofNullable(CURRENT_ID.get());
    }

    public static void clear() {
        CURRENT_ID.remove();
    }
}