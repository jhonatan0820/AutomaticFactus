package com.automaticfactus.service;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Estado de conexión con Aiven MySQL con caché en memoria.
 * <p>
 * Un heartbeat en background hace {@code isValid()} cada 10s y guarda el
 * resultado. {@link #isReady()} devuelve la caché instantáneamente (sin
 * bloquear el request) y, si está caída, dispara un intento de "wake" en
 * un hilo aparte para que el siguiente poll del frontend la encuentre lista.
 */
@Service
public class DatabaseService {

    private static final int  PING_TIMEOUT_SECONDS = 3;
    private static final long HEARTBEAT_MS         = 10_000L;

    private final DataSource dataSource;
    private final AtomicBoolean ready  = new AtomicBoolean(false);
    private final AtomicBoolean waking = new AtomicBoolean(false);

    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** Wake inicial sin bloquear el arranque. */
    @PostConstruct
    void warmUpOnStartup() {
        triggerAsyncWake();
    }

    /** Heartbeat periódico para mantener la caché fresca. */
    @Scheduled(fixedDelay = HEARTBEAT_MS)
    void heartbeat() {
        ready.set(ping());
    }

    /**
     * Devuelve al instante el último estado conocido. Si está caído,
     * arranca un wake en background para que el próximo poll ya la vea arriba.
     */
    public boolean isReady() {
        boolean current = ready.get();
        if (!current) triggerAsyncWake();
        return current;
    }

    private void triggerAsyncWake() {
        if (!waking.compareAndSet(false, true)) return;
        Thread t = new Thread(() -> {
            try { ready.set(ping()); }
            finally { waking.set(false); }
        }, "db-wake");
        t.setDaemon(true);
        t.start();
    }

    private boolean ping() {
        try (Connection c = dataSource.getConnection()) {
            return c.isValid(PING_TIMEOUT_SECONDS);
        } catch (Exception e) {
            return false;
        }
    }
}
