package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;
import org.junit.Test;

public class DebtorBookEventCreatorTest {

    @Test
    public void shouldCreateEvent() {
        DebtorBookEventCreator debtorBookEventCreator = new DebtorBookEventCreator();

        InvoiceCreationEvent invoiceCreationEvent = new SampleEventCreator().invoke();

        debtorBookEventCreator.accept(invoiceCreationEvent);
    }
}