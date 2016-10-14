package nl.marcenschede.invoice.core.functional;

import nl.marcenschede.invoice.core.Customer;
import nl.marcenschede.invoice.core.InvoiceLine;
import nl.marcenschede.invoice.core.InvoiceType;
import nl.marcenschede.invoice.core.ProductCategory;

import java.util.List;
import java.util.Optional;

public class InvoiceDataImpl implements InvoiceData {
    private Optional<String> countryOfOrigin;
    private Optional<String> countryOfDestination;
    private Customer customer;
    private InvoiceType invoiceType;
    private Optional<ProductCategory> productCategory;
    private Boolean vatShifted;
    private List<InvoiceLine> invoiceLines;

    public InvoiceDataImpl() {
    }

    InvoiceDataImpl(InvoiceData invoiceData) {
        this.countryOfDestination = invoiceData.getCountryOfDestination();
        this.countryOfOrigin = invoiceData.getCountryOfOrigin();
        this.customer = invoiceData.getCustomer();
        this.invoiceType = invoiceData.getInvoiceType();
        this.productCategory = invoiceData.getProductCategory();
        this.vatShifted = invoiceData.getVatShifted();
        this.invoiceLines = invoiceData.getInvoiceLines();
    }

    @Override
    public Optional<String> getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(Optional<String> countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public void setCountryOfDestination(Optional<String> countryOfDestination) {
        this.countryOfDestination = countryOfDestination;
    }

    @Override
    public Optional<String> getCountryOfDestination() {
        return countryOfDestination;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    @Override
    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setProductCategory(Optional<ProductCategory> productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public Optional<ProductCategory> getProductCategory() {
        return productCategory;
    }

    public void setVatShifted(Boolean vatShifted) {
        this.vatShifted = vatShifted;
    }

    @Override
    public Boolean getVatShifted() {
        return vatShifted;
    }

    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    @Override
    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }
}
