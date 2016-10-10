package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class InvoiceCreatedEventProcessor implements Consumer<InvoiceCreationEvent> {

    List<Consumer<InvoiceCreationEvent>> consumerList = new ArrayList<>();

    public InvoiceCreatedEventProcessor(Consumer<InvoiceCreationEvent>... consumers) {
        consumerList.addAll(Arrays.asList(consumers));
    }

    public void addConsumer(Consumer<InvoiceCreationEvent> consumer) {
        consumerList.add(consumer);
    }

    @Override
    public void accept(InvoiceCreationEvent invoiceCreationEvent) {
        consumerList.stream().forEach(
                invoiceCreationEventConsumer -> invoiceCreationEventConsumer.accept(invoiceCreationEvent));
    }
}
