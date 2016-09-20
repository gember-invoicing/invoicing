package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Invoice;
import nl.marcenschede.invoice.InvoiceImpl;
import nl.marcenschede.invoice.InvoiceLine;
import nl.marcenschede.invoice.VatAmountSummary;

import java.math.BigDecimal;
import java.util.function.Function;

public abstract class InvoiceCalculationsDelegate {
    protected final Invoice invoice;

    public InvoiceCalculationsDelegate(Invoice invoice) {
        this.invoice = invoice;
    }

    public abstract BigDecimal getTotalInvoiceAmountInclVat(Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator);

    public abstract BigDecimal getTotalInvoiceAmountExclVat(Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator);

    public abstract BigDecimal getInvoiceTotalVat(Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator);

    public abstract String getVatDeclarationCountry();

    protected void validateValidity() {
        validateRegistrationInOriginCountry();
    }

    private void validateRegistrationInOriginCountry() {
        String originCountry = CountryOfOriginHelper.getOriginCountry(invoice);
        if(!invoice.getCompany().hasVatRegistrationFor(originCountry))
            throw new InvoiceImpl.NoRegistrationInOriginCountryException();
    }
}
