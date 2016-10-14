package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.Customer;
import nl.marcenschede.invoice.core.InvoiceTotals;
import nl.marcenschede.invoice.core.LineSummary;
import nl.marcenschede.invoice.core.VatAmountSummary;
import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;
import nl.marcenschede.invoice.core.functional.InvoiceData;
import nl.marcenschede.invoice.core.functional.InvoiceDataImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class SampleEventCreator {
    InvoiceCreationEvent invoke() {
        InvoiceTotals invoiceTotals = new InvoiceTotalsCreator().invoke();
        InvoiceData invoiceData = new InvoiceDataCreator().invoke();

        return new InvoiceCreationEvent(invoiceData, 12345L, invoiceTotals);
    }

    private class InvoiceDataCreator {
        InvoiceData invoke() {
            InvoiceDataImpl invoiceData = new InvoiceDataImpl();
            invoiceData.setCustomer(new CustomerDataCreator().invoke());
            return invoiceData;
        }
    }

    private class CustomerDataCreator {
        Customer invoke() {
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
        InvoiceTotals invoke() {
            List<LineSummary> lineSummaries = new ArrayList<>();

            VatAmountSummary vatAmountSummary = new SampleVatAmountSummaryCreator().invoke();
            lineSummaries.add(new LineSummary(vatAmountSummary, null));

            return new InvoiceTotals(
                        new BigDecimal("100.00"), new BigDecimal("21.00"), new BigDecimal("121.00"), null, lineSummaries);
        }

        private class SampleVatAmountSummaryCreator {
            VatAmountSummary invoke() {
                return new VatAmountSummary(null,
                                new BigDecimal("21.00"), new BigDecimal("100.00"), new BigDecimal("121.00") );
            }
        }
    }
}
