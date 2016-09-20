package nl.marcenschede.invoice.calcs;

import nl.marcenschede.invoice.InvoiceLine;
import nl.marcenschede.invoice.InvoiceLineVatType;
import nl.marcenschede.invoice.VatAmountSummary;
import nl.marcenschede.invoice.calculators.VatCalculator;
import nl.marcenschede.invoice.tariffs.VatPercentage;
import nl.marcenschede.invoice.tariffs.VatRepository;

import java.math.BigDecimal;
import java.util.function.Function;

public class VatAmountSummaryFactory {

    public static Function<? super InvoiceLine, VatAmountSummary> create(String originCountry) {

        return new Function<InvoiceLine, VatAmountSummary>() {
            @Override
            public VatAmountSummary apply(InvoiceLine invoiceLine) {

                VatRepository vatRepository = new VatRepository();

                VatPercentage vatPercentage =
                        vatRepository.findByCountryTariffAndDate(originCountry, invoiceLine.getVatTariff(), invoiceLine.getVatReferenceDate());

                return new VatAmountSummary(
                        vatPercentage,
                        getVatAmount(invoiceLine, vatPercentage),
                        getLineAmountExclVat(invoiceLine, vatPercentage),
                        getLineAmountInclVat(invoiceLine, vatPercentage));
            }
        };
    }

    private static BigDecimal getLineAmountInclVat(InvoiceLine invoiceLine, VatPercentage vatPercentage) {
        return invoiceLine.getInvoiceLineVatType() == InvoiceLineVatType.INCLUDING_VAT ?
                invoiceLine.getLineAmount() : calculateLineAmountInclVat(invoiceLine, vatPercentage);
    }

    private static BigDecimal getLineAmountExclVat(InvoiceLine invoiceLine, VatPercentage vatPercentage) {
        return invoiceLine.getInvoiceLineVatType() == InvoiceLineVatType.EXCLUDING_VAT ?
                invoiceLine.getLineAmount() : calculateLineAmountExclVat(invoiceLine, vatPercentage);
    }

    private static BigDecimal getVatAmount(InvoiceLine invoiceLine, VatPercentage vatPercentage) {
        return invoiceLine.getInvoiceLineVatType() == InvoiceLineVatType.EXCLUDING_VAT ?
                calculateVatFromLineAmountExclVat(invoiceLine, vatPercentage) :
                calculateVatFromLineAmountInclVat(invoiceLine, vatPercentage);
    }

    private static BigDecimal calculateVatFromLineAmountExclVat(InvoiceLine invoiceLine, VatPercentage vatPercentage) {
        return VatCalculator.calculateVatFromLineAmountExclVat(invoiceLine.getLineAmount(), vatPercentage.getPercentage());
    }

    private static BigDecimal calculateVatFromLineAmountInclVat(InvoiceLine invoiceLine, VatPercentage vatPercentage) {
        return VatCalculator.calculateVatFromLineAmountInclVat(invoiceLine.getLineAmount(), vatPercentage.getPercentage());
    }

    private static BigDecimal calculateLineAmountInclVat(InvoiceLine invoiceLine, VatPercentage vatPercentage) {
        return VatCalculator.addVatToAmount(invoiceLine.getLineAmount(), vatPercentage.getPercentage());
    }

    private static BigDecimal calculateLineAmountExclVat(InvoiceLine invoiceLine, VatPercentage vatPercentage) {
        return VatCalculator.reduceVatFromLineAmount(invoiceLine.getLineAmount(), vatPercentage.getPercentage());
    }
}

