package features;

import nl.marcenschede.invoice.core.InvoiceLine;
import nl.marcenschede.invoice.core.InvoiceLineVatType;
import nl.marcenschede.invoice.core.tariffs.VatTariff;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InvoiceLineCreator {
    private final String lineAmount;
    private final String inclExclVat;
    private final String vatTariff;
    private final String referenceDate;

    public InvoiceLineCreator(String lineAmount, String inclExclVat, String vatTariff, String referenceDate) {
        this.lineAmount = lineAmount;
        this.inclExclVat = inclExclVat;
        this.vatTariff = vatTariff;
        this.referenceDate = referenceDate;
    }

    public InvoiceLine invoke() {
        return new InvoiceLine() {
            @Override
            public BigDecimal getLineAmount() {
                return new BigDecimal(lineAmount);
            }

            @Override
            public InvoiceLineVatType getInvoiceLineVatType() {

                switch (inclExclVat.toUpperCase()) {
                    case "INCL":
                        return InvoiceLineVatType.INCLUDING_VAT;
                    case "EXCL":
                        return InvoiceLineVatType.EXCLUDING_VAT;
                }

                return null;
            }

            @Override
            public LocalDate getVatReferenceDate() {
                return LocalDate.parse(referenceDate, DateTimeFormatter.ISO_DATE);
            }

            @Override
            public VatTariff getVatTariff() {
                return VatTariff.valueOf(vatTariff.toUpperCase());
            }

        };
    }
}
