package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Invoice;
import nl.marcenschede.invoice.InvoiceLine;
import nl.marcenschede.invoice.VatAmountSummary;

import java.math.BigDecimal;
import java.util.function.Function;

public class InvoiceEuGoodsCalculationsDelegate extends InvoiceCalculationsDelegate {
    public InvoiceEuGoodsCalculationsDelegate(Invoice invoice) {
        super(invoice);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountInclVat(Function<? super InvoiceLine, VatAmountSummary> amountSummaryCalculator) {
        return getTotalInvoiceAmountExclVat(amountSummaryCalculator);
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
        return BigDecimal.valueOf(0, 2);
    }

    @Override
    public String getVatDeclarationCountry() {
        return CountryOfOriginHelper.getOriginCountry(invoice);
    }
}
