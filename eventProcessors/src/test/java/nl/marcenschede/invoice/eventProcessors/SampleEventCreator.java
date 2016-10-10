package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.Customer;
import nl.marcenschede.invoice.core.InvoiceTotals;
import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;
import nl.marcenschede.invoice.core.functional.InvoiceData;

import java.math.BigDecimal;
import java.util.Optional;

public class SampleEventCreator {
    public InvoiceCreationEvent invoke() {
        InvoiceTotals invoiceTotals = new InvoiceTotalsCreator().invoke();
        InvoiceData invoiceData = new InvoiceDataCreator().invoke();

        return new InvoiceCreationEvent(invoiceData, 12345L, invoiceTotals);
    }

    private class InvoiceDataCreator {
        public InvoiceData invoke() {
            InvoiceData invoiceData = new InvoiceData();
            invoiceData.setCustomer(new CustomerDataCreator().invoke());
            return invoiceData;
        }
    }

    private class CustomerDataCreator {
        public Customer invoke() {
            return new Customer() {
                @Override
                public Optional<String> getDefaultCountry() {
                    return Optional.of("NL");
                }

                @Override
                public Optional<String> getEuTaxId() {
                    return Optional.empty();
                }

                @Override
                public Long getCustomerDebtorId() {
                    return 2345L;
                }
            };
        }
    }

    private class InvoiceTotalsCreator {
        public InvoiceTotals invoke() {
            return new InvoiceTotals(
                        new BigDecimal("100.00"), new BigDecimal("21.00"), new BigDecimal("121.00"), null);
        }
    }
}
