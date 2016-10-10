package nl.marcenschede.invoice.eventProcessors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DebtorBookEventCreator implements Consumer<InvoiceCreationEvent> {

    private static Logger logger = Logger.getLogger(DebtorBookEventCreator.class.getName());

    interface DebtorBookEvent {
        BigDecimal getTotalInvoiceAmountInclVat();
        Long getDebtorId();
        Long getInvoiceNumber();
    }

    @Override
    public void accept(InvoiceCreationEvent invoiceCreationEvent) {
        DebtorBookEvent debtorBookEvent = createDebtorBookEvent(invoiceCreationEvent);
        logEventCreation(debtorBookEvent);
    }

    private DebtorBookEvent createDebtorBookEvent(InvoiceCreationEvent invoiceCreationEvent) {
        return new DebtorBookEvent() {
            @Override
            public BigDecimal getTotalInvoiceAmountInclVat() {
                return invoiceCreationEvent.getInvoiceTotals().totalInvoiceAmountInclVat;
            }

            @Override
            public Long getDebtorId() {
                return invoiceCreationEvent.getCustomer().getCustomerDebtorId();
            }

            @Override
            public Long getInvoiceNumber() {
                return invoiceCreationEvent.getInvoiceNumber();
            }
        };
    }

    private void logEventCreation(DebtorBookEvent debtorBookEvent) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            logger.log(
                    Level.WARNING,
                    String.format("Debtor book event created:  %s", objectMapper.writeValueAsString(debtorBookEvent)));
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
