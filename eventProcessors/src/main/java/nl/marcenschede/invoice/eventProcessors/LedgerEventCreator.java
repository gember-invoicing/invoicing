package nl.marcenschede.invoice.eventProcessors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LedgerEventCreator implements Consumer<InvoiceCreationEvent> {

    private static Logger logger = Logger.getLogger(LedgerEventCreator.class.getName());

    interface LedgerEvent {
        BigDecimal getTotalInvoiceAmountExclVat();
        BigDecimal getInvoiceTotalVat();
        BigDecimal getTotalInvoiceAmountInclVat();
        String getDescription();
    }

    @Override
    public void accept(InvoiceCreationEvent invoiceCreationEvent) {
        LedgerEvent ledgerEvent = createLedgerEventFrom(invoiceCreationEvent);
        logEventCreation(ledgerEvent);
    }

    private LedgerEvent createLedgerEventFrom(final InvoiceCreationEvent invoiceCreationEvent) {
        return new LedgerEvent() {

                @Override
                public BigDecimal getTotalInvoiceAmountExclVat() {
                    return invoiceCreationEvent.getInvoiceTotals().totalInvoiceAmountExclVat;
                }

                @Override
                public BigDecimal getInvoiceTotalVat() {
                    return invoiceCreationEvent.getInvoiceTotals().invoiceTotalVat;
                }

                @Override
                public BigDecimal getTotalInvoiceAmountInclVat() {
                    return invoiceCreationEvent.getInvoiceTotals().totalInvoiceAmountInclVat;
                }

                @Override
                public String getDescription() {
                    return String.format("Invoicenumber: %d", invoiceCreationEvent.getInvoiceNumber());
                }
            };
    }

    private void logEventCreation(LedgerEvent ledgerEvent) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            logger.log(
                    Level.FINE,
                    String.format("Ledger event created:  %s", objectMapper.writeValueAsString(ledgerEvent)));
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    private class JsonException extends RuntimeException {
        public JsonException(JsonProcessingException e) {
            super(e);
        }
    }
}
