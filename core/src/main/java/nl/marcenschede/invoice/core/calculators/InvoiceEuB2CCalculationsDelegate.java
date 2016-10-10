package nl.marcenschede.invoice.core.calculators;

import nl.marcenschede.invoice.core.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class InvoiceEuB2CCalculationsDelegate extends InvoiceCalculationsDelegate {

    public InvoiceEuB2CCalculationsDelegate(Invoice invoice) {
        super(invoice);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountInclVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return lineSummaries.stream()
                .map(VatAmountSummary::getAmountInclVat)
                .reduce(ZERO, BigDecimal::add);

    }

    @Override
    public BigDecimal getTotalInvoiceAmountExclVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return lineSummaries.stream()
                .map(VatAmountSummary::getAmountExclVat)
                .reduce(ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getInvoiceTotalVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return lineSummaries.stream()
                .map(VatAmountSummary::getAmountVat)
                .reduce(ZERO, BigDecimal::add);
    }

    @Override
    public String getVatDeclarationCountry() {

        String destinationCountry = CountryOfDestinationHelper.getDestinationCountry(invoice);

        Company company = invoice.getCompany();
        Boolean companyHasRegistrationInDestinationCountry =
                company.hasVatRegistrationFor(destinationCountry);

        return companyHasRegistrationInDestinationCountry ?
                destinationCountry : CountryOfOriginHelper.getOriginCountry(invoice);
    }

}
