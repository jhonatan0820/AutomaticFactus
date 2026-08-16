package com.automaticfactus.service;

import com.automaticfactus.model.*;
import com.automaticfactus.render.QuotationRenderer;
import com.automaticfactus.repositories.OrderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio central de cotizaciones/facturas.
 */
@Service
public class QuotationService {

    private static final Company DEFAULT_COMPANY = new Company("Dotaciones Zambrano","NIT 1001331130-4","3136673447","Calle 70 sur #91-40, Bogotá");
    private static final int DEFAULT_VALIDITY_DAYS = 15;

    private final QuotationRenderer renderer;
    private final OrderRepository orders;
    private final DatabaseService db;

    public QuotationService(QuotationRenderer renderer, OrderRepository orders, DatabaseService db) {
        this.renderer = renderer;
        this.orders = orders;
        this.db = db;
    }

    /** Próximo número de cotización. Devuelve null si la BD no está lista. */
    public Integer nextNumber() {
        if (!db.isReady()) return null;
        return orders.findMaxIdOrder() + 1;
    }

    public String renderFromPayload(JsonNode payload) {
        Quotation q = buildFromPayload(payload);
        String docType = text(payload, "documentType");
        return renderer.render(q, docType);
    }

    // ---------------- helpers ----------------

    private Quotation buildFromPayload(JsonNode root) {
        Customer customer = buildCustomer(root.path("cliente"));
        Company company   = buildCompanyWithAdvisor(root.path("asesor"));

        String number  = text(root, "numero");
        LocalDate date = parseDateOrToday(text(root, "fecha"));
        LocalDate until = date.plusDays(DEFAULT_VALIDITY_DAYS);
        String notes   = text(root, "observaciones");

        List<QuotationItem> items = buildItems(root.path("items"));
        return new Quotation(company, customer, number, date, until, items, notes);
    }

    private Customer buildCustomer(JsonNode c) {
        return new Customer(
                text(c, "nombre"),
                text(c, "tipoIdentificacion"),
                text(c, "nit"),
                text(c, "telefono"),
                text(c, "correo"),
                text(c, "direccion"));
    }

    private Company buildCompanyWithAdvisor(JsonNode advisor) {
        String phone = text(advisor, "telefono");
        if (phone == null || phone.isBlank()) return DEFAULT_COMPANY;
        return new Company(
                DEFAULT_COMPANY.getName(),
                DEFAULT_COMPANY.getNit(),
                phone,
                DEFAULT_COMPANY.getCity());
    }

    private LocalDate parseDateOrToday(String raw) {
        if (raw == null || raw.isBlank()) return LocalDate.now();
        return LocalDate.parse(raw);
    }

    private List<QuotationItem> buildItems(JsonNode arr) {
        List<QuotationItem> items = new ArrayList<>();
        for (JsonNode it : arr) {
            String desc = text(it, "descripcion");
            if (desc == null || desc.isBlank()) continue;
            int qty = it.path("cantidad").asInt(0);
            BigDecimal price = new BigDecimal(it.path("precioUnitario").asText("0"));
            String size = text(it, "talla");
            items.add(new QuotationItem(desc.trim(), size, qty, price));
        }
        return items;
    }

    private static String text(JsonNode node, String field) {
        JsonNode v = node.path(field);
        return v.isMissingNode() || v.isNull() ? null : v.asText();
    }
}
