package com.automaticfactus;

import com.automaticfactus.model.*;
import com.automaticfactus.render.QuotationRenderer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuotationRendererTest {

    @Test
    void rendersQuotationHtmlWithAllSections() {
        Company company = new Company(
                "Dotaciones Zambrano",
                "NIT 1001331130-4",
                "3136673447",
                "Calle 70 sur #91-40, Bogotá");

        Customer customer = new Customer(
                "Andina Textil S.A.S.",
                "NIT",
                "900123456-1",
                "3157894213",
                "compras@andinatextil.co",
                "Cra 15 # 40-20, Bogotá");

        List<QuotationItem> items = List.of(
                new QuotationItem("Camisa oxford", "M", 10, new BigDecimal("35000")),
                new QuotationItem("Pantalón dril", null, 5, new BigDecimal("55000")));

        LocalDate today = LocalDate.of(2026, 8, 15);
        Quotation q = new Quotation(company, customer, "3721",
                today, today.plusDays(15), items, "Entrega en dos partes.");

        String html = new QuotationRenderer().render(q);

        assertTrue(html.contains("Dotaciones Zambrano"), "empresa emisora");
        assertTrue(html.contains("Andina Textil"),      "cliente");
        assertTrue(html.contains("3721"),                "número de contrato");
        assertTrue(html.contains("Camisa oxford"),       "producto 1");
        assertTrue(html.contains("Pantalón dril"),       "producto 2");
        assertTrue(html.contains("NA"),                  "talla vacía cae en 'NA'");
        assertTrue(html.contains("Entrega en dos partes."), "observaciones");
        assertEquals(0, q.getTotal().compareTo(new BigDecimal("625000")),
                "total = 10*35000 + 5*55000");
    }
}
