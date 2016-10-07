package nl.marcenschede.invoice;

import nl.marcenschede.invoice.tariffs.CountryTariffPeriodPercentageTuple;

import java.math.BigDecimal;
import java.util.Map;

public class InvoiceTotals {
    public BigDecimal totalInvoiceAmountExclVat;
    public BigDecimal invoiceTotalVat;
    public BigDecimal totalInvoiceAmountInclVat;
    public Map<CountryTariffPeriodPercentageTuple, VatAmountSummary> vatAmountSummaryPerPercentage;

    InvoiceTotals(BigDecimal totalInvoiceAmountExclVat,
                         BigDecimal invoiceTotalVat,
                         BigDecimal totalInvoiceAmountInclVat,
                         Map<CountryTariffPeriodPercentageTuple, VatAmountSummary> vatAmountSummaryPerPercentage) {

        this.totalInvoiceAmountExclVat = totalInvoiceAmountExclVat;
        this.invoiceTotalVat = invoiceTotalVat;
        this.totalInvoiceAmountInclVat = totalInvoiceAmountInclVat;
        this.vatAmountSummaryPerPercentage = vatAmountSummaryPerPercentage;
    }
}
