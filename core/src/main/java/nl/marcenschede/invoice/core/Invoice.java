package nl.marcenschede.invoice.core;

import java.util.List;
import java.util.Optional;

public interface Invoice {
    Company getCompany();

    void setCompany(Company company);

    Customer getCustomer();

    void setCustomer(Customer customer);

    void setInvoiceType(InvoiceType invoiceType);

    Optional<String> getCountryOfOrigin();

    void setCountryOfOrigin(Optional<String> productOrigin);

    Optional<String> getCountryOfDestination();

    void setCountryOfDestination(Optional<String> countryOfDestination);

    List<InvoiceLine> getInvoiceLines();

    void setInvoiceLines(List<InvoiceLine> invoiceLines);

    Optional<ProductCategory> getProductCategory();

    void setProductCategory(Optional<ProductCategory> productCategory);

    boolean getVatShifted();

    void setVatShifted(Boolean aBoolean);

    InvoiceTotals getInvoiceTotals();
}
