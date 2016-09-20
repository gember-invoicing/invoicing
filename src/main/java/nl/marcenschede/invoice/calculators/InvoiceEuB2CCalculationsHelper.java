package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Invoice;
import nl.marcenschede.invoice.InvoiceLine;
import nl.marcenschede.invoice.VatAmountSummary;

import java.math.BigDecimal;
import java.util.function.Function;

public class InvoiceEuB2CCalculationsHelper extends InvoiceCalculationsHelper {

    public final BigDecimal ZERO = new BigDecimal("0.00");

    public InvoiceEuB2CCalculationsHelper(Invoice invoice) {
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

//        return invoice.getInvoiceLines().stream()
//                .map(InvoiceLine::getAmountSummary)
//                .map(VatAmountSummary::getAmountExclVat)
//                .reduce(new BigDecimal("0.00"), BigDecimal::add);

        return ZERO;
    }

    @Override
    public BigDecimal getInvoiceTotalVat(Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        validateValidity();

//        return invoice.getInvoiceLines().stream()
//                .map(InvoiceLine::getAmountSummary)
//                .map(VatAmountSummary::getAmountVat)
//                .reduce(new BigDecimal("0.00"), BigDecimal::add);

        return ZERO;
    }

}
