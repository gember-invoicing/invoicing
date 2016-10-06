package nl.marcenschede.invoice;

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

    void setVatShifted(Boolean aBoolean);

    InvoiceTotals getInvoiceTotals();
}
