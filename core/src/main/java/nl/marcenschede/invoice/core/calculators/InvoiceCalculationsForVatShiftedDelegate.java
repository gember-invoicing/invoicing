package nl.marcenschede.invoice.core.calculators;

import nl.marcenschede.invoice.core.Invoice;
import nl.marcenschede.invoice.core.InvoiceLine;
import nl.marcenschede.invoice.core.VatAmountSummary;
import nl.marcenschede.invoice.core.calcs.VatAmountSummaryFactory;
import nl.marcenschede.invoice.core.tariffs.VatRepository;

import java.util.function.Function;

public abstract class InvoiceCalculationsForVatShiftedDelegate extends InvoiceCalculationsDelegate {
    public InvoiceCalculationsForVatShiftedDelegate(Invoice invoice, VatRepository vatRepository) {
        super(invoice, vatRepository);
    }

    @Override
    protected Function<? super InvoiceLine, VatAmountSummary> getVatAmountSummaryFunction() {
        return VatAmountSummaryFactory.createVatShiftedVatCalculatorWith()
                .apply(vatRepository)
                .apply(getVatDeclarationCountry());
    }

}
