package nl.marcenschede.invoice.functional;

import nl.marcenschede.invoice.Company;
import nl.marcenschede.invoice.InvoiceTotals;
import nl.marcenschede.invoice.tariffs.VatRepository;

import java.util.function.Consumer;
import java.util.function.Function;

public class InvoiceCreationFactory {

    public static Function<Consumer<InvoiceCreationEvent>, Consumer<InvoiceData>> invoiceCreationFactory(Company company, VatRepository vatRepository) {
        return publisher -> invoiceCreationFactory(publisher, company, vatRepository);
    }

    public static Consumer<InvoiceData> invoiceCreationFactory(Consumer<InvoiceCreationEvent> publisher, Company company, VatRepository vatRepository) {
        return invoiceData -> createInvoice(invoiceData, publisher, company, vatRepository);
    }

    public static void createInvoice(InvoiceData invoiceData, Consumer<InvoiceCreationEvent> publisher, Company company, VatRepository vatRepository) {
        final InvoiceTotals totals = InvoiceCalculatorFactory.getInvoiceTotals(invoiceData, company, vatRepository);

        publisher.accept(new InvoiceCreationEvent(invoiceData, totals));
    }
}
