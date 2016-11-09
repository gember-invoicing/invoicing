package nl.marcenschede.invoice.core.calculators;

import nl.marcenschede.invoice.core.*;
import nl.marcenschede.invoice.core.tariffs.CountryTariffPeriodPercentageTuple;
import nl.marcenschede.invoice.core.tariffs.VatRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class InvoiceEuEServicesCalculationsDelegate extends InvoiceCalculationsDelegate {
    public InvoiceEuEServicesCalculationsDelegate(Invoice invoice, VatRepository vatRepository) {
        super(invoice, vatRepository);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountInclVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return lineSummaries.stream()
                .map(VatAmountSummary::getAmountInclVat)
                .reduce(BigDecimalHelper.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountExclVatOnTotals(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> vatCalculator) {
        return BigDecimalHelper.ZERO;
    }

    @Override
    public BigDecimal getTotalInvoiceAmountExclVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return lineSummaries.stream()
                .map(VatAmountSummary::getAmountExclVat)
                .reduce(BigDecimalHelper.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getInvoiceTotalVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return lineSummaries.stream()
                .map(VatAmountSummary::getAmountVat)
                .reduce(BigDecimalHelper.ZERO, BigDecimal::add);
    }

    @Override
    public String getVatDeclarationCountry() {
        return CountryOfDestinationHelper.getDestinationCountry(invoice);
    }

    @Override
    public CountryTariffPeriodPercentageTuple createCountryTariffPeriodPercentageTupleForCalcationStratery(
            CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {

        return countryTariffPeriodPercentageTuple;
    }
}
