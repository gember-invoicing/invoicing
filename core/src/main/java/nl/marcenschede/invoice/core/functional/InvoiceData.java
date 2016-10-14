package nl.marcenschede.invoice.core.functional;

import nl.marcenschede.invoice.core.Customer;
import nl.marcenschede.invoice.core.InvoiceLine;
import nl.marcenschede.invoice.core.InvoiceType;
import nl.marcenschede.invoice.core.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface InvoiceData {
    Optional<String> getCountryOfOrigin();

    Optional<String> getCountryOfDestination();

    Customer getCustomer();

    InvoiceType getInvoiceType();

    Optional<ProductCategory> getProductCategory();

    Boolean getVatShifted();

    List<InvoiceLine> getInvoiceLines();
}
