package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Invoice;
import nl.marcenschede.invoice.InvoiceLine;
import nl.marcenschede.invoice.LineSummary;
import nl.marcenschede.invoice.VatAmountSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

class InvoiceNationalVatShiftedCalculationsDelegate extends InvoiceCalculationsDelegate {

    InvoiceNationalVatShiftedCalculationsDelegate(Invoice invoice) {
        super(invoice);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountInclVat(List<LineSummary> lineSummaries,
                                                   Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {

        return getTotalInvoiceAmountExclVat(lineSummaries, amountSummaryCalculator);
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
        return ZERO;
    }

    @Override
    public String getVatDeclarationCountry() {
        return CountryOfOriginHelper.getOriginCountry(invoice);
    }

}
