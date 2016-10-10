package nl.marcenschede.invoice.core.calculators;

import nl.marcenschede.invoice.core.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class InvoiceCalculationsDelegate {
    public final BigDecimal ZERO = new BigDecimal("0.00");

    protected final Invoice invoice;

    public InvoiceCalculationsDelegate(Invoice invoice) {
        this.invoice = invoice;
    }

    public abstract BigDecimal getTotalInvoiceAmountInclVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator);

    public abstract BigDecimal getTotalInvoiceAmountExclVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator);

    public abstract BigDecimal getInvoiceTotalVat(List<LineSummary> lineSummaries, Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator);

    public abstract String getVatDeclarationCountry();

    public void checkInvoiceValidity() {
        validateRegistrationInOriginCountry();
    }

    private void validateRegistrationInOriginCountry() {
        String originCountry = CountryOfOriginHelper.getOriginCountry(invoice);
        if(!invoice.getCompany().hasVatRegistrationFor(originCountry))
            throw new InvoiceImpl.NoRegistrationInOriginCountryException();
    }

    public List<LineSummary> getLineSummaries(Function<? super InvoiceLine, VatAmountSummary> vatCalculator) {
        return invoice.getInvoiceLines().stream()
                .map(invoiceLine -> createLineSummary(invoiceLine, vatCalculator))
                .collect(Collectors.toList());
    }

    private LineSummary createLineSummary(InvoiceLine invoiceLine, Function<? super InvoiceLine, VatAmountSummary> vatCalculator) {
        return new LineSummary(vatCalculator.apply(invoiceLine), invoiceLine);
    }
}
