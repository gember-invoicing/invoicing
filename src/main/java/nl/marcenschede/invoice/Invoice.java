package nl.marcenschede.invoice;

import nl.marcenschede.invoice.tariffs.VatPercentage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Invoice {
    Company getCompany();

    void setCompany(Company company);

    Customer getCustomer();

    void setCustomer(Customer customer);

    void setInvoiceType(InvoiceType invoiceType);

    Optional<String> getCountryOfOrigin();

    void setCountryOfOrigin(Optional<String> productOrigin);

    void setProductDestinationCountry(Optional<String> productDestination);

    void setInvoiceLines(List<InvoiceLine> invoiceLines);

    void setProductCategory(Optional<ProductCategory> productCategory);

    void setVatShifted(Boolean aBoolean);

    BigDecimal getTotalInvoiceAmountInclVat();

    BigDecimal getTotalInvoiceAmountExclVat();

    BigDecimal getInvoiceTotalVat();

    public Map<VatPercentage, VatAmountSummary> getVatPerVatTariff();
}
