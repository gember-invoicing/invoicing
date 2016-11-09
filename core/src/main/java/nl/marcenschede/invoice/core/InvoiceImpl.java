package nl.marcenschede.invoice.core;

import nl.marcenschede.invoice.core.calculators.InvoiceCalculationsDelegate;
import nl.marcenschede.invoice.core.calculators.InvoiceCalculationsDelegateFactory;
import nl.marcenschede.invoice.core.tariffs.VatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceImpl implements Invoice {
    private final VatRepository vatRepository;

    private Company company;
    private Customer customer;
    private InvoiceType invoiceType;
    private Optional<String> countryOfOrigin;
    private Optional<String> countryOfDestination;
    private List<InvoiceLine> invoiceLines = new ArrayList<>();
    private Optional<ProductCategory> productCategory;
    private Boolean vatShifted;

    public InvoiceImpl(VatRepository vatRepository) {
        this.vatRepository = vatRepository;
    }

    @Override
    public Company getCompany() {
        return company;
    }

    @Override
    public void setCompany(Company company) {

        this.company = company;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(Customer customer) {

        this.customer = customer;
    }

    @Override
    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    @Override
    public void setInvoiceType(InvoiceType invoiceType) {

        this.invoiceType = invoiceType;
    }

    @Override
    public Optional<String> getCountryOfOrigin() {

        return countryOfOrigin;
    }

    @Override
    public void setCountryOfOrigin(Optional<String> countryOfOrigin) {

        this.countryOfOrigin = countryOfOrigin;
    }

    @Override
    public Optional<String> getCountryOfDestination() {
        return countryOfDestination;
    }

    @Override
    public void setCountryOfDestination(Optional<String> countryOfDestination) {
        this.countryOfDestination = countryOfDestination;
    }

    @Override
    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    @Override
    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    @Override
    public Optional<ProductCategory> getProductCategory() {
        return productCategory;
    }

    @Override
    public void setProductCategory(Optional<ProductCategory> productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public boolean getVatShifted() {
        return vatShifted;
    }

    @Override
    public void setVatShifted(Boolean vatShifted) {
        this.vatShifted = vatShifted;
    }

    @Override
    public InvoiceTotals getInvoiceTotals() {
        InvoiceCalculationsDelegate calculationsHelper =
                InvoiceCalculationsDelegateFactory.newInstance(this, vatRepository);

        return calculationsHelper.calculateInvoice();
    }

    public static class NoRegistrationInOriginCountryException extends RuntimeException {
    }
}
