package nl.marcenschede.invoice.core.functional;

import nl.marcenschede.invoice.core.InvoiceTotals;

public class InvoiceCreationEvent extends InvoiceDataImpl {

    private InvoiceTotals invoiceTotals;
    private Long invoiceNumber;

    public InvoiceCreationEvent(InvoiceData invoiceData, Long invoiceNumber, InvoiceTotals invoiceTotals) {
        super(invoiceData);
        this.invoiceTotals = invoiceTotals;
        this.invoiceNumber = invoiceNumber;
    }

    public InvoiceTotals getInvoiceTotals() {
        return invoiceTotals;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }
}
