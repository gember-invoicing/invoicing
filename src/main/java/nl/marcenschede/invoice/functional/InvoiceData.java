package nl.marcenschede.invoice.functional;

import nl.marcenschede.invoice.Customer;
import nl.marcenschede.invoice.InvoiceLine;
import nl.marcenschede.invoice.InvoiceType;
import nl.marcenschede.invoice.ProductCategory;

import java.util.List;
import java.util.Optional;

public class InvoiceData {
    private Optional<String> countryOfOrigin;
    private Optional<String> countryOfDestination;
    private Customer customer;
    private InvoiceType invoiceType;
    private Optional<ProductCategory> productCategory;
    private Boolean vatShifted;
    private List<InvoiceLine> invoiceLines;

    public Optional<String> getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(Optional<String> countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public void setCountryOfDestination(Optional<String> countryOfDestination) {
        this.countryOfDestination = countryOfDestination;
    }

    public Optional<String> getCountryOfDestination() {
        return countryOfDestination;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setProductCategory(Optional<ProductCategory> productCategory) {
        this.productCategory = productCategory;
    }

    public Optional<ProductCategory> getProductCategory() {
        return productCategory;
    }

    public void setVatShifted(Boolean vatShifted) {
        this.vatShifted = vatShifted;
    }

    public Boolean getVatShifted() {
        return vatShifted;
    }

    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }
}
