package nl.marcenschede.invoice.core.calculators;

import nl.marcenschede.invoice.core.Invoice;
import nl.marcenschede.invoice.core.InvoiceLine;
import nl.marcenschede.invoice.core.LineSummary;
import nl.marcenschede.invoice.core.VatAmountSummary;
import nl.marcenschede.invoice.core.tariffs.CountryTariffPeriodPercentageTuple;
import nl.marcenschede.invoice.core.tariffs.VatRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static nl.marcenschede.invoice.core.BigDecimalHelper.ZERO;

public class InvoiceNationalCalculationsDelegate extends InvoiceCalculationsDelegate {

    public InvoiceNationalCalculationsDelegate(Invoice invoice, VatRepository vatRepository) {
        super(invoice, vatRepository);
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

    @Override
    public CountryTariffPeriodPercentageTuple createCountryTariffPeriodPercentageTupleForCalcationStratery(
            CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {

        return countryTariffPeriodPercentageTuple;
    }

    @Override
    public BigDecimal getTotalInvoiceAmountExclVatOnTotals(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> vatCalculator) {
        BigDecimal totalInVat = getTotalInvoiceAmountInclVatOnTotals(lineSummaries, vatCalculator);
        BigDecimal totalVat = getInvoiceTotalVatOnTotals(lineSummaries, vatCalculator);

        return totalInVat.min(totalVat);
    }

    private BigDecimal getInvoiceTotalVatOnTotals(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> vatCalculator) {
        return ZERO;
    }

    private BigDecimal getTotalInvoiceAmountInclVatOnTotals(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> vatCalculator) {
        return ZERO;
    }
}
