package nl.marcenschede.invoice.functional;

import nl.marcenschede.invoice.Company;
import nl.marcenschede.invoice.InvoiceTotals;
import nl.marcenschede.invoice.tariffs.VatRepository;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class InvoiceCreationFactory {

    public static Function<Consumer<InvoiceCreationEvent>, Consumer<InvoiceData>> invoiceCreationFactory(
            Supplier<Long> invoiceNumberGenerator,
            Company company,
            VatRepository vatRepository) {
        return publisher -> invoiceCreationFactory(publisher, invoiceNumberGenerator, company, vatRepository);
    }

    public static Consumer<InvoiceData> invoiceCreationFactory(
            Consumer<InvoiceCreationEvent> publisher,
            Supplier<Long> invoiceNumberGenerator,
            Company company,
            VatRepository vatRepository) {
        return invoiceData -> createInvoice(invoiceData, publisher, invoiceNumberGenerator, company, vatRepository);
    }

    public static void createInvoice(
            InvoiceData invoiceData,
            Consumer<InvoiceCreationEvent> publisher,
            Supplier<Long> invoiceNumberGenerator,
            Company company, VatRepository vatRepository) {

        final InvoiceTotals totals = InvoiceCalculatorFactory.getInvoiceTotals(invoiceData, company, vatRepository);

        publisher.accept(new InvoiceCreationEvent(invoiceData, invoiceNumberGenerator.get(), totals));
    }
}
