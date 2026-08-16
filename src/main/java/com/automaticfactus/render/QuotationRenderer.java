package com.automaticfactus.render;

import com.automaticfactus.model.Quotation;
import com.automaticfactus.model.QuotationItem;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

@Component
public class QuotationRenderer {

    private final TemplateEngine engine;
    private final NumberFormat currency;
    private final DateTimeFormatter dateFmt =
            DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "CO"));
    private final String logoDataUri;

    public QuotationRenderer() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        this.engine = new TemplateEngine();
        this.engine.setTemplateResolver(resolver);

        Locale co = new Locale("es", "CO");
        this.currency = NumberFormat.getCurrencyInstance(co);
        this.currency.setMinimumFractionDigits(0);
        this.currency.setMaximumFractionDigits(0);

        this.logoDataUri = loadLogo();
    }

    private String loadLogo() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("static/img/logo.jpg")) {
            if (in == null) return null;
            byte[] bytes = in.readAllBytes();
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    public String render(Quotation q) {
        return render(q, "cotizacion");
    }

    public String render(Quotation q, String docType) {
        boolean isFactura = "factura".equalsIgnoreCase(docType);
        String titleCap = isFactura ? "Factura" : "Cotización";
        String titleLow = isFactura ? "factura" : "cotización";

        Context ctx = new Context(new Locale("es", "CO"));
        ctx.setVariable("empresa", q.getCompany());
        ctx.setVariable("cliente", q.getCustomer());
        ctx.setVariable("numero_cotizacion", q.getNumber());
        ctx.setVariable("fecha", q.getDate() != null ? q.getDate().format(dateFmt) : "");
        ctx.setVariable("vigencia", q.getValidUntil() != null ? q.getValidUntil().format(dateFmt) : "");
        ctx.setVariable("productos", q.getItems().stream().map(this::asRow).toList());
        ctx.setVariable("subtotal", money(q.getSubtotal()));
        ctx.setVariable("total", money(q.getTotal()));
        ctx.setVariable("observaciones", q.getNotes());
        ctx.setVariable("logoDataUri", logoDataUri);
        ctx.setVariable("docTitle", titleCap);
        ctx.setVariable("docTitleLower", titleLow);
        return engine.process("quotation/quotation", ctx);
    }

    private ItemRow asRow(QuotationItem it) {
        String size = it.getSize();
        if (size == null || size.isBlank()) size = "NA";
        return new ItemRow(it.getDescription(), size, it.getQuantity(),
                money(it.getUnitPrice()), money(it.getTotal()));
    }

    private String money(BigDecimal v) {
        return currency.format(v == null ? BigDecimal.ZERO : v);
    }

    public record ItemRow(String descripcion, String talla, int cantidad,
                          String precioUnitario, String precioTotal) {}
}

