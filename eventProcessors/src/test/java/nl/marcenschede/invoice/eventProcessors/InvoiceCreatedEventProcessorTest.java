package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;
import nl.marcenschede.invoice.core.functional.InvoiceData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceCreatedEventProcessorTest {



    @Mock
    public Consumer<InvoiceCreationEvent> consumer1;

    @Mock
    public Consumer<InvoiceCreationEvent> consumer2;

    @Test
    public void accept() throws Exception {

        InvoiceCreatedEventProcessor invoiceCreatedEventProcessor =
                new InvoiceCreatedEventProcessor(consumer1);
        invoiceCreatedEventProcessor.addConsumer(consumer2);

        InvoiceCreationEvent event = new InvoiceCreationEvent(new InvoiceData(), 12345L, null);

        invoiceCreatedEventProcessor.accept(event);

        Mockito.verify(consumer1).accept(Mockito.any(InvoiceCreationEvent.class));
        Mockito.verify(consumer2).accept(Mockito.any(InvoiceCreationEvent.class));
    }

}