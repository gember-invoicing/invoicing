package nl.marcenschede.invoice;

import nl.marcenschede.invoice.tariffs.VatTariff;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InvoiceLine {
    BigDecimal getLineAmount();

    InvoiceLineVatType getInvoiceLineVatType();

    LocalDate getVatReferenceDate();

    VatTariff getVatTariff();
}
