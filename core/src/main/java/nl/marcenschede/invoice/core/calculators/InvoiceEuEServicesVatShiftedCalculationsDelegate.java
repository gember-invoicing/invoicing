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

class InvoiceEuEServicesVatShiftedCalculationsDelegate extends InvoiceCalculationsForVatShiftedDelegate {
    InvoiceEuEServicesVatShiftedCalculationsDelegate(Invoice invoice, VatRepository vatRepository) {
        super(invoice, vatRepository);
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
    public BigDecimal getTotalInvoiceAmountExclVatOnTotals(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> vatCalculator) {
        return ZERO;
    }

    @Override
    public BigDecimal getInvoiceTotalVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return ZERO;
    }

    @Override
    public String getVatDeclarationCountry() {
        return CountryOfDestinationHelper.getDestinationCountry(invoice);
    }

    @Override
    public CountryTariffPeriodPercentageTuple createCountryTariffPeriodPercentageTupleForCalcationStratery(
            CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return new CountryTariffPeriodPercentageTuple(
                true,
                countryTariffPeriodPercentageTuple.getIsoCountryCode(),
                countryTariffPeriodPercentageTuple.getVatTariff(),
                countryTariffPeriodPercentageTuple.getStartDate(),
                countryTariffPeriodPercentageTuple.getEndDate(),
                countryTariffPeriodPercentageTuple.getPercentage()
        );
    }
}
