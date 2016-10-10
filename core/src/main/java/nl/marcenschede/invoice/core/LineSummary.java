package nl.marcenschede.invoice.core;

public class LineSummary extends VatAmountSummary {
    private InvoiceLine invoiceLine;

    public LineSummary(VatAmountSummary vatAmountSummary, InvoiceLine invoiceLine) {
        super(vatAmountSummary);
        this.invoiceLine = invoiceLine;
    }

    public InvoiceLine getInvoiceLine() {
        return invoiceLine;
    }
}
