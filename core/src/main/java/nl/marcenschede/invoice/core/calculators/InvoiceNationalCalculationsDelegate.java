package nl.marcenschede.invoice.core.calculators;

import nl.marcenschede.invoice.core.Invoice;
import nl.marcenschede.invoice.core.InvoiceLine;
import nl.marcenschede.invoice.core.LineSummary;
import nl.marcenschede.invoice.core.VatAmountSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class InvoiceNationalCalculationsDelegate extends InvoiceCalculationsDelegate {

    public InvoiceNationalCalculationsDelegate(Invoice invoice) {
        super(invoice);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountInclVat(List<LineSummary> lineSummaries,
                                                   Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return lineSummaries.stream()
                .map(LineSummary::getAmountInclVat)
                .reduce(ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountExclVat(List<LineSummary> lineSummaries,
                                                   Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return lineSummaries.stream()
                .map(LineSummary::getAmountExclVat)
                .reduce(ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getInvoiceTotalVat(List<LineSummary> lineSummaries,
                                         Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return lineSummaries.stream()
                .map(LineSummary::getAmountVat)
                .reduce(ZERO, BigDecimal::add);
    }

    @Override
    public String getVatDeclarationCountry() {
        return CountryOfOriginHelper.getOriginCountry(invoice);
    }

}
