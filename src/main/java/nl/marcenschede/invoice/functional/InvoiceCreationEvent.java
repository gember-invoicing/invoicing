package nl.marcenschede.invoice.functional;

import nl.marcenschede.invoice.InvoiceTotals;

public class InvoiceCreationEvent extends InvoiceData {

    private InvoiceTotals invoiceTotals;

    public InvoiceCreationEvent(InvoiceData invoiceData, InvoiceTotals invoiceTotals) {
        super(invoiceData);
        this.invoiceTotals = invoiceTotals;
    }

    public InvoiceTotals getInvoiceTotals() {
        return invoiceTotals;
    }
}
