package nl.marcenschede.invoice.core.calcs;

import nl.marcenschede.invoice.core.InvoiceLine;
import nl.marcenschede.invoice.core.InvoiceLineVatType;
import nl.marcenschede.invoice.core.VatAmountSummary;
import nl.marcenschede.invoice.core.calculators.VatCalculator;
import nl.marcenschede.invoice.core.tariffs.CountryTariffPeriodPercentageTuple;
import nl.marcenschede.invoice.core.tariffs.VatRepository;

import java.math.BigDecimal;
import java.util.function.Function;

public class VatAmountSummaryFactory {

    public static Function<VatRepository, Function<String, Function<? super InvoiceLine, VatAmountSummary>>> createVatCalculatorWith2() {

        return vatRepository -> ( originCountry -> (invoiceLine -> {
            CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple =
                    vatRepository.findByCountryTariffAndDate(originCountry, invoiceLine.getVatTariff(), invoiceLine.getVatReferenceDate());

            return new VatAmountSummary(
                    countryTariffPeriodPercentageTuple,
                    getVatAmount(invoiceLine, countryTariffPeriodPercentageTuple),
                    getLineAmountExclVat(invoiceLine, countryTariffPeriodPercentageTuple),
                    getLineAmountInclVat(invoiceLine, countryTariffPeriodPercentageTuple));
        }));
    }

    private static BigDecimal getLineAmountInclVat(InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return invoiceLine.getInvoiceLineVatType() == InvoiceLineVatType.INCLUDING_VAT ?
                invoiceLine.getLineAmount() : calculateLineAmountInclVat(invoiceLine, countryTariffPeriodPercentageTuple);
    }

    private static BigDecimal getLineAmountExclVat(InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return invoiceLine.getInvoiceLineVatType() == InvoiceLineVatType.EXCLUDING_VAT ?
                invoiceLine.getLineAmount() : calculateLineAmountExclVat(invoiceLine, countryTariffPeriodPercentageTuple);
    }

    private static BigDecimal getVatAmount(InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return invoiceLine.getInvoiceLineVatType() == InvoiceLineVatType.EXCLUDING_VAT ?
                calculateVatFromLineAmountExclVat(invoiceLine, countryTariffPeriodPercentageTuple) :
                calculateVatFromLineAmountInclVat(invoiceLine, countryTariffPeriodPercentageTuple);
    }

    private static BigDecimal calculateVatFromLineAmountExclVat(InvoiceLine invoiceLine, CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple) {
        return VatCalculator.calculateVatFromLineAmountExclVat(invoiceLine.getLineAmount(), countryTariffPeriodPercentageTuple.getPercentage());
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

