package nl.marcenschede.invoice.core;

import nl.marcenschede.invoice.core.tariffs.VatTariff;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InvoiceLine {
    BigDecimal getLineAmount();

    InvoiceLineVatType getInvoiceLineVatType();

    LocalDate getVatReferenceDate();

    VatTariff getVatTariff();
}
