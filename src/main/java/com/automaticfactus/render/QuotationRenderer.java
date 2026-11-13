package com.automaticfactus.render;

import com.automaticfactus.model.Quotation;
import com.automaticfactus.model.QuotationItem;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

@Component
public class QuotationRenderer {

    private static final String PDF_STYLES = """
            @page { size: A4; margin: 0; }
            * { box-sizing: border-box; }
            html, body { margin: 0; padding: 0; background: #fff; color: #1a1f2b; font-family: Arial, sans-serif; font-size: 9pt; line-height: 1.3; }
            .page { width: 210mm; margin: 0; padding: 0; }
            .quotation { width: 210mm; padding: 8mm 10mm; border: 0; background: #fff; }
            .doc-header { display: block; padding-bottom: 4mm; border-bottom: 2px solid #0f2a4a; }
            .doc-header__brand { display: table; width: 100%; }
            .doc-header__logo-img { display: table-cell; width: 24mm; height: 24mm; padding-right: 4mm; vertical-align: middle; }
            .doc-header__logo { display: table-cell; width: 24mm; height: 24mm; vertical-align: middle; text-align: center; background: #0f2a4a; color: #fff; font-size: 18pt; font-weight: bold; }
            .doc-header__company { display: table-cell; vertical-align: middle; }
            .doc-header__name { margin: 0 0 1mm; color: #0f2a4a; font-size: 16pt; font-weight: bold; }
            .doc-header__meta { margin: 0; color: #4a5568; font-size: 8pt; }
            .dot { padding: 0 1mm; color: #7a8598; }
            .doc-title { display: table; width: 100%; margin-top: 5mm; }
            .doc-title > div { display: table-cell; vertical-align: bottom; }
            .doc-title__eyebrow { margin: 0 0 1mm; color: #7a8598; font-size: 7pt; text-transform: uppercase; letter-spacing: 1pt; }
            .doc-title__text { margin: 0; color: #0f2a4a; font-size: 19pt; font-weight: bold; }
            .doc-title__meta { text-align: right; }
            .doc-title__meta dl { margin: 0; }
            .doc-title__meta dt { clear: both; float: left; width: 28mm; color: #7a8598; font-size: 7pt; text-align: left; text-transform: uppercase; }
            .doc-title__meta dd { margin: 0 0 1mm; color: #1a1f2b; font-size: 8pt; font-weight: bold; text-align: right; }
            .parties { display: table; width: 100%; margin: 5mm 0 0; border-spacing: 3mm 0; }
            .party { display: table-cell; width: 50%; padding: 3mm; border: 1px solid #e4e8ef; background: #fafbfd; vertical-align: top; }
            .party__label { margin: 0 0 2mm; color: #7a8598; font-size: 7pt; letter-spacing: 1pt; text-transform: uppercase; }
            .party__name { margin: 0 0 1mm; color: #1a1f2b; font-size: 10pt; font-weight: bold; }
            .party__line { margin: 0.5mm 0; color: #4a5568; font-size: 8pt; }
            .items { margin-top: 5mm; }
            .items__table { width: 100%; border-collapse: collapse; font-size: 8pt; }
            .items__table thead { display: table-header-group; }
            .items__table th { padding: 2mm; background: #0f2a4a; color: #fff; font-size: 7pt; text-align: left; text-transform: uppercase; }
            .items__table td { padding: 2mm; border-bottom: 1px solid #e4e8ef; vertical-align: top; }
            .items__table tbody tr:nth-child(even) td { background: #fafbfd; }
            .col-idx, .col-size { text-align: center; }
            .col-qty, .col-price, .col-total { text-align: right; white-space: nowrap; }
            .totals { margin-top: 3mm; text-align: right; }
            .totals__box { display: inline-block; width: 75mm; }
            .totals__row { display: table; width: 100%; padding: 1.5mm 3mm; color: #4a5568; font-size: 8pt; }
            .totals__label, .totals__value { display: table-cell; }
            .totals__value { text-align: right; }
            .totals__row--grand { border-top: 2px solid #0f2a4a; background: #eaf0f7; color: #0f2a4a; font-size: 10pt; font-weight: bold; }
            .terms { margin-top: 5mm; padding-top: 3mm; border-top: 1px solid #e4e8ef; color: #4a5568; font-size: 8pt; }
            .terms__title { margin: 0 0 1.5mm; color: #7a8598; font-size: 7pt; text-transform: uppercase; letter-spacing: 1pt; }
            .terms__title--spaced { margin-top: 3mm; }
            .terms__list { margin: 0; padding-left: 4mm; }
            .terms__list li { margin: 0 0 0.5mm; }
            .terms__notes { margin-bottom: 2mm; }
            .terms__notes p { margin: 0; }
            .doc-footer { margin-top: 5mm; padding-top: 2mm; border-top: 1px solid #e4e8ef; color: #7a8598; font-size: 7pt; text-align: center; }
            .doc-footer__legal { margin: 1mm 0 0; font-size: 6.5pt; }
            """;

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

    public byte[] renderPdf(Quotation q, String docType) {
        String html = render(q, docType);
        int styleStart = html.indexOf("<style>");
        int styleEnd = html.indexOf("</style>", styleStart);
        if (styleStart >= 0 && styleEnd >= 0) {
            html = html.substring(0, styleStart) + "<style>" + PDF_STYLES + "</style>"
                    + html.substring(styleEnd + "</style>".length());
        }
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            new PdfRendererBuilder()
                    .useFastMode()
                    .useDefaultPageSize(210, 297, BaseRendererBuilder.PageSizeUnits.MM)
                    .withHtmlContent(html, null)
                    .toStream(output)
                    .run();
            return output.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar el PDF", e);
        }
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

