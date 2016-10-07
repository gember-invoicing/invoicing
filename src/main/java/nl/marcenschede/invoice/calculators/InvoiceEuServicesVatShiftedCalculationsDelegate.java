package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Invoice;
import nl.marcenschede.invoice.InvoiceLine;
import nl.marcenschede.invoice.LineSummary;
import nl.marcenschede.invoice.VatAmountSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

class InvoiceEuServicesVatShiftedCalculationsDelegate extends InvoiceCalculationsDelegate {
    InvoiceEuServicesVatShiftedCalculationsDelegate(Invoice invoice) {
        super(invoice);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountInclVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return getTotalInvoiceAmountExclVat(lineSummaries, amountSummaryCalculator);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountExclVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return lineSummaries.stream()
                .map(VatAmountSummary::getAmountExclVat)
                .reduce(ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getInvoiceTotalVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return ZERO;
    }

    @Override
    public String getVatDeclarationCountry() {
        return CountryOfOriginHelper.getOriginCountry(invoice);
    }
}
