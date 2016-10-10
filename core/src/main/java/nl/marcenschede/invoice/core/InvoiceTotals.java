package nl.marcenschede.invoice.core;

import nl.marcenschede.invoice.core.tariffs.CountryTariffPeriodPercentageTuple;

import java.math.BigDecimal;
import java.util.Map;

public class InvoiceTotals {
    public BigDecimal totalInvoiceAmountExclVat;
    public BigDecimal invoiceTotalVat;
    public BigDecimal totalInvoiceAmountInclVat;
    public Map<CountryTariffPeriodPercentageTuple, VatAmountSummary> vatAmountSummaryPerPercentage;

    public InvoiceTotals(BigDecimal totalInvoiceAmountExclVat,
                         BigDecimal invoiceTotalVat,
                         BigDecimal totalInvoiceAmountInclVat,
                         Map<CountryTariffPeriodPercentageTuple, VatAmountSummary> vatAmountSummaryPerPercentage) {

        this.totalInvoiceAmountExclVat = totalInvoiceAmountExclVat;
        this.invoiceTotalVat = invoiceTotalVat;
        this.totalInvoiceAmountInclVat = totalInvoiceAmountInclVat;
        this.vatAmountSummaryPerPercentage = vatAmountSummaryPerPercentage;
    }
}
