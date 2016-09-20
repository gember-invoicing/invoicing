package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Company;
import nl.marcenschede.invoice.Invoice;
import nl.marcenschede.invoice.InvoiceLine;
import nl.marcenschede.invoice.VatAmountSummary;

import java.math.BigDecimal;
import java.util.function.Function;

public class InvoiceEuB2CCalculationsDelegate extends InvoiceCalculationsDelegate {

    public final BigDecimal ZERO = new BigDecimal("0.00");

    public InvoiceEuB2CCalculationsDelegate(Invoice invoice) {
        super(invoice);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountInclVat(Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        validateValidity();

        return invoice.getInvoiceLines().stream()
                .map(amountSummaryCalculator)
                .map(VatAmountSummary::getAmountInclVat)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);

    }

    @Override
    public BigDecimal getTotalInvoiceAmountExclVat(Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        validateValidity();

        return invoice.getInvoiceLines().stream()
                .map(amountSummaryCalculator)
                .map(VatAmountSummary::getAmountExclVat)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    @Override
    public BigDecimal getInvoiceTotalVat(Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        validateValidity();

        return invoice.getInvoiceLines().stream()
                .map(amountSummaryCalculator)
                .map(VatAmountSummary::getAmountVat)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    @Override
    public String getVatDeclarationCountry() {

        String destinationCountry = CountryOfOriginHelper.getDestinationCountry(invoice);

        Company company = invoice.getCompany();
        Boolean companyHasRegistrationInDestinationCountry =
                company.hasVatRegistrationFor(destinationCountry);

        return companyHasRegistrationInDestinationCountry ?
                destinationCountry : CountryOfOriginHelper.getOriginCountry(invoice);
    }

}
