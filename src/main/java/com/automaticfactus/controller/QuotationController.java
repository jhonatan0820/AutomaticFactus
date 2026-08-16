package com.automaticfactus.controller;

import com.automaticfactus.service.QuotationService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class QuotationController {

    private final QuotationService service;

    public QuotationController(QuotationService service) {
        this.service = service;
    }

    /** POST /api/cotizacion — devuelve el HTML del PDF a partir del JSON del form. */
    @PostMapping(value = "/api/cotizacion", produces = "text/html;charset=UTF-8")
    public ResponseEntity<String> render(@RequestBody JsonNode payload) {
        try {
            String html = service.renderFromPayload(payload);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
                    .body(html);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.parseMediaType("text/plain;charset=UTF-8"))
                    .body("Error: " + e.getMessage());
        }
    }

    /** GET /api/cotizacion/proximo-numero */
    @GetMapping("/api/cotizacion/proximo-numero")
    public Map<String, Object> nextNumber() {
        Map<String, Object> body = new HashMap<>();
        try {
            body.put("numero", service.nextNumber());
        } catch (Exception e) {
            System.err.println("[/api/cotizacion/proximo-numero] "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
            body.put("numero", null);
        }
        return body;
    }
}
