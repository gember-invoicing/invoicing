package nl.marcenschede.invoice.core.calcs;

import nl.marcenschede.invoice.core.BigDecimalHelper;
import nl.marcenschede.invoice.core.InvoiceLine;
import nl.marcenschede.invoice.core.InvoiceLineVatType;
import nl.marcenschede.invoice.core.VatAmountSummary;
import nl.marcenschede.invoice.core.calculators.VatCalculator;
import nl.marcenschede.invoice.core.tariffs.CountryTariffPeriodPercentageTuple;
import nl.marcenschede.invoice.core.tariffs.VatRepository;
import nl.marcenschede.invoice.core.tariffs.VatTariff;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Function;

public class VatAmountSummaryFactory {

    public static Function<VatRepository,
            Function<String,
                    Function<? super InvoiceLine, VatAmountSummary>>> createVatCalculatorWith() {

        return vatRepository -> originCountry -> invoiceLine -> {
            CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple =
                    vatRepository.findByCountryTariffAndDate(originCountry, invoiceLine.getVatTariff(), invoiceLine.getVatReferenceDate());

            return new VatAmountSummary(
                    countryTariffPeriodPercentageTuple,
                    getLineAmountVat(invoiceLine, countryTariffPeriodPercentageTuple),
                    getLineAmountExclVat(invoiceLine, countryTariffPeriodPercentageTuple),
                    getLineAmountInclVat(invoiceLine, countryTariffPeriodPercentageTuple));
        };
    }

    public static Function<VatRepository,
            Function<String,
                    Function<? super InvoiceLine, VatAmountSummary>>> createVatShiftedVatCalculatorWith() {

        return vatRepository -> originCountry -> invoiceLine -> {
            CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple =
                    vatRepository.findByCountryTariffAndDate(originCountry, invoiceLine.getVatTariff(), invoiceLine.getVatReferenceDate());

            return new VatAmountSummary(
                    new CountryTariffPeriodPercentageTuple(
                            true,
                            countryTariffPeriodPercentageTuple.getIsoCountryCode(),
                            VatTariff.ZERO,
                            LocalDate.of(1992, 1, 1),
                            null,
                            BigDecimalHelper.ZERO),
                    BigDecimalHelper.ZERO,
                    getLineAmountExclVat(invoiceLine, countryTariffPeriodPercentageTuple),
                    getLineAmountExclVat(invoiceLine, countryTariffPeriodPercentageTuple));
        };
    }

    private static BigDecimal getLineAmountInclVat(
            InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return countryTariffPeriodPercentageTuple.getVatShifted() == false ?
                (invoiceLine.getInvoiceLineVatType() == InvoiceLineVatType.INCLUDING_VAT ?
                        invoiceLine.getLineAmount() : calculateLineAmountInclVat(invoiceLine, countryTariffPeriodPercentageTuple)) :
                getLineAmountExclVat(invoiceLine, countryTariffPeriodPercentageTuple);
    }

    private static BigDecimal getLineAmountExclVat(InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return invoiceLine.getInvoiceLineVatType() == InvoiceLineVatType.EXCLUDING_VAT ?
                invoiceLine.getLineAmount() : calculateLineAmountExclVat(invoiceLine, countryTariffPeriodPercentageTuple);
    }

    private static BigDecimal getLineAmountVat(InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return countryTariffPeriodPercentageTuple.getVatShifted() == false ?
                (invoiceLine.getInvoiceLineVatType() == InvoiceLineVatType.EXCLUDING_VAT ?
                        calculateVatFromLineAmountExclVat(invoiceLine, countryTariffPeriodPercentageTuple) :
                        calculateVatFromLineAmountInclVat(invoiceLine, countryTariffPeriodPercentageTuple)) :
                BigDecimalHelper.ZERO;
    }

    private static BigDecimal calculateVatFromLineAmountExclVat(InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return VatCalculator.calculateVatFromLineAmountExclVat(invoiceLine.getLineAmount(),
                countryTariffPeriodPercentageTuple.getPercentage());
    }

    private static BigDecimal calculateVatFromLineAmountInclVat(InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return VatCalculator.calculateVatFromLineAmountInclVat(invoiceLine.getLineAmount(), countryTariffPeriodPercentageTuple.getPercentage());
    }

    private static BigDecimal calculateLineAmountInclVat(InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return VatCalculator.addVatToAmount(invoiceLine.getLineAmount(), countryTariffPeriodPercentageTuple.getPercentage());
    }

    private static BigDecimal calculateLineAmountExclVat(InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return VatCalculator.reduceVatFromLineAmount(invoiceLine.getLineAmount(), countryTariffPeriodPercentageTuple.getPercentage());
    }
}

