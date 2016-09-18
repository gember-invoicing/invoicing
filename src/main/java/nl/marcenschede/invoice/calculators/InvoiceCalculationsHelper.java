package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Invoice;
import nl.marcenschede.invoice.InvoiceImpl;
import nl.marcenschede.invoice.InvoiceLine;
import nl.marcenschede.invoice.VatAmountSummary;

import java.math.BigDecimal;

public class InvoiceCalculationsHelper {

    private final Invoice invoice;

    public InvoiceCalculationsHelper(Invoice invoice) {
        this.invoice = invoice;
    }

    public BigDecimal getTotalInvoiceAmountInclVat() {
        validateValidity();

        return invoice.getInvoiceLines().stream()
                .map(InvoiceLine::getAmountSummary)
                .map(VatAmountSummary::getAmountInclVat)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    public BigDecimal getTotalInvoiceAmountExclVat() {
        validateValidity();

        return invoice.getInvoiceLines().stream()
                .map(InvoiceLine::getAmountSummary)
                .map(VatAmountSummary::getAmountExclVat)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    public BigDecimal getInvoiceTotalVat() {
        validateValidity();

        return invoice.getInvoiceLines().stream()
                .map(InvoiceLine::getAmountSummary)
                .map(VatAmountSummary::getAmountVat)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    private void validateValidity() {
        validateRegistrationInOriginCountry();
    }

    private void validateRegistrationInOriginCountry() {
        String originCountry = CountryOfOriginHelper.getOriginCountry(invoice);
        if(!invoice.getCompany().hasVatRegistrationFor(originCountry))
            throw new InvoiceImpl.NoRegistrationInOriginCountryException();
    }
}
