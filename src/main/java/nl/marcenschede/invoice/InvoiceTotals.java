package nl.marcenschede.invoice;

import nl.marcenschede.invoice.tariffs.VatPercentage;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by marc on 06/10/2016.
 */
public class InvoiceTotals {
    public BigDecimal totalInvoiceAmountExclVat;
    public BigDecimal invoiceTotalVat;
    public BigDecimal totalInvoiceAmountInclVat;
    public Map<VatPercentage, VatAmountSummary> vatAmountSummaryPerPercentage;

    public InvoiceTotals(BigDecimal totalInvoiceAmountExclVat,
                         BigDecimal invoiceTotalVat,
                         BigDecimal totalInvoiceAmountInclVat,
                         Map<VatPercentage, VatAmountSummary> vatAmountSummaryPerPercentage) {

        this.totalInvoiceAmountExclVat = totalInvoiceAmountExclVat;
        this.invoiceTotalVat = invoiceTotalVat;
        this.totalInvoiceAmountInclVat = totalInvoiceAmountInclVat;
        this.vatAmountSummaryPerPercentage = vatAmountSummaryPerPercentage;
    }
}
