package com.automaticfactus.controller;

import com.automaticfactus.service.DatabaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final DatabaseService db;

    public HealthController(DatabaseService db) {
        this.db = db;
    }

    
    //valida que el back este arriba
    @GetMapping(value = "/health", produces = "text/plain;charset=UTF-8")
    public String health() {
        return "ok";
    }

    //valida el estado de la bd
    @GetMapping("/api/db-status")
    public Map<String, Boolean> dbStatus() {
        return Map.of("ready", db.isReady());
    }
}
