package nl.marcenschede.invoice.core.calculators;

import nl.marcenschede.invoice.core.*;
import nl.marcenschede.invoice.core.calcs.VatAmountSummaryFactory;
import nl.marcenschede.invoice.core.tariffs.CountryTariffPeriodPercentageTuple;
import nl.marcenschede.invoice.core.tariffs.VatRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static nl.marcenschede.invoice.core.BigDecimalHelper.ZERO;

public abstract class InvoiceCalculationsDelegate {
    protected final Invoice invoice;
    protected final VatRepository vatRepository;

    public InvoiceCalculationsDelegate(Invoice invoice, VatRepository vatRepository) {
        this.invoice = invoice;
        this.vatRepository = vatRepository;
    }

    public abstract BigDecimal getTotalInvoiceAmountInclVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator);

    public abstract BigDecimal getTotalInvoiceAmountExclVatOnTotals(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> vatCalculator);

    public abstract BigDecimal getTotalInvoiceAmountExclVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator);

    public abstract BigDecimal getInvoiceTotalVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator);

    public abstract String getVatDeclarationCountry();

    private void checkInvoiceValidity() {
        validateRegistrationInOriginCountry();
    }

    private void validateRegistrationInOriginCountry() {
        String originCountry = CountryOfOriginHelper.getOriginCountry(invoice);
        if (!invoice.getCompany().hasVatRegistrationFor(originCountry))
            throw new InvoiceImpl.NoRegistrationInOriginCountryException();
    }

    public abstract CountryTariffPeriodPercentageTuple createCountryTariffPeriodPercentageTupleForCalcationStratery(CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple);

    public InvoiceTotals calculateInvoice() {
        checkInvoiceValidity();

        List<LineSummary> lineSummaries = calculateLineSummaries();

        Map<CountryTariffPeriodPercentageTuple, VatAmountSummary> vatAmountSummaryPerPercentage =
                calculateSubTotals(lineSummaries);

        return calculateInvoieTotals(lineSummaries, vatAmountSummaryPerPercentage);
    }

    private List<LineSummary> calculateLineSummaries() {
        Function<? super InvoiceLine, VatAmountSummary> vatCalculator = getVatAmountSummaryFunction();

        return invoice.getInvoiceLines().stream()
                .map(invoiceLine -> new LineSummary(vatCalculator.apply(invoiceLine), invoiceLine))
                .collect(Collectors.toList());
    }

    protected Function<? super InvoiceLine, VatAmountSummary> getVatAmountSummaryFunction() {
        return VatAmountSummaryFactory.createVatCalculatorWith()
                .apply(vatRepository)
                .apply(getVatDeclarationCountry());
    }

    private Map<CountryTariffPeriodPercentageTuple, VatAmountSummary> calculateSubTotals(List<LineSummary> lineSummaries) {
        Map<CountryTariffPeriodPercentageTuple, VatAmountSummary> vatAmountSummaryPerPercentage =
                invoice.getCompany().getVatCalculationPolicy() == VatCalculationPolicy.VAT_CALCULATION_PER_LINE ?
                        getVatAmountSummaryPerPercentagePerLine(lineSummaries) :
                        getVatAmountSummaryPerPercentageOnSubTotals(lineSummaries);

        return vatAmountSummaryPerPercentage;
    }

    private Map<CountryTariffPeriodPercentageTuple, VatAmountSummary> getVatAmountSummaryPerPercentagePerLine(List<LineSummary> lineSummaries) {

        Map<CountryTariffPeriodPercentageTuple, List<LineSummary>> mapOfInvoiceLinesPerVatPercentage = getMapOfCountryTariffPeriodPercentageTuples(lineSummaries);

        return mapOfInvoiceLinesPerVatPercentage.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        optionalListEntry -> sumAmountsToVatAmountSummary(
                                optionalListEntry.getKey(),
                                optionalListEntry.getValue())));
    }

    private Map<CountryTariffPeriodPercentageTuple, VatAmountSummary> getVatAmountSummaryPerPercentageOnSubTotals(List<LineSummary> lineSummaries) {

        Map<CountryTariffPeriodPercentageTuple, List<LineSummary>> mapOfInvoiceLinesPerVatPercentage = getMapOfCountryTariffPeriodPercentageTuples(lineSummaries);

        return mapOfInvoiceLinesPerVatPercentage.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        optionalListEntry -> {
                            BigDecimal totalInclVat = optionalListEntry.getValue().stream()
                                    .map(x -> x.getAmountInclVat())
                                    .reduce(ZERO, BigDecimal::add);

                            BigDecimal factor = BigDecimalHelper.ZERO;

                            switch (optionalListEntry.getKey().getVatTariff()) {
                                case HIGH:
                                    factor = new BigDecimal("21.00").divide(new BigDecimal("121.00"), 10, BigDecimal.ROUND_HALF_UP);
                                    break;
                                case LOW1:
                                    factor = new BigDecimal("6.00").divide(new BigDecimal("106.00"), 10, BigDecimal.ROUND_HALF_UP);
                                    break;
                                case ZERO:
                                    factor = ZERO;
                                    break;
                            }

                            BigDecimal totalVat = totalInclVat.multiply(factor).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal totalExVat = totalInclVat.subtract(totalVat);

                            return new VatAmountSummary(
                                    optionalListEntry.getKey(),
                                    totalVat, totalExVat, totalInclVat);
                        }));
    }

    private Map<CountryTariffPeriodPercentageTuple, List<LineSummary>> getMapOfCountryTariffPeriodPercentageTuples(List<LineSummary> lineSummaries) {
        return lineSummaries.stream()
                .collect(Collectors.groupingBy(VatAmountSummary::getCountryTariffPeriodPercentageTuple));
    }

    private VatAmountSummary sumAmountsToVatAmountSummary(CountryTariffPeriodPercentageTuple percentage, List<LineSummary> lineSummaries) {
        return lineSummaries.stream()
                .map(lineSummary -> (VatAmountSummary) lineSummary)
                .reduce(new VatAmountSummary(percentage, ZERO, ZERO, ZERO), VatAmountSummary::add);
    }

    private InvoiceTotals calculateInvoieTotals(List<LineSummary> lineSummaries, Map<CountryTariffPeriodPercentageTuple, VatAmountSummary> vatAmountSummaryPerPercentage) {

        return new InvoiceTotals(
                vatAmountSummaryPerPercentage.values().stream().map(x -> x.getAmountExclVat()).reduce(ZERO, BigDecimal::add),
                vatAmountSummaryPerPercentage.values().stream().map(x -> x.getAmountVat()).reduce(ZERO, BigDecimal::add),
                vatAmountSummaryPerPercentage.values().stream().map(x -> x.getAmountInclVat()).reduce(ZERO, BigDecimal::add),
                vatAmountSummaryPerPercentage,
                lineSummaries);
    }
}
