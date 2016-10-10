package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.InvoiceTotals;
import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;
import nl.marcenschede.invoice.core.functional.InvoiceData;
import org.junit.Test;

import java.math.BigDecimal;

public class LedgerEventCreatorTest {

    @Test
    public void accept() throws Exception {
        LedgerEventCreator ledgerEventCreator = new LedgerEventCreator();

        ledgerEventCreator.accept(getInvoiceCreatedEvent());
    }

    public InvoiceCreationEvent getInvoiceCreatedEvent() {
        InvoiceTotals invoiceTotals = new InvoiceTotals(
                new BigDecimal("100.00"), new BigDecimal("21.00"), new BigDecimal("100.00"), null);

        InvoiceData invoiceData = new InvoiceData();

        return new InvoiceCreationEvent(invoiceData, 12345L, invoiceTotals);
    }
}