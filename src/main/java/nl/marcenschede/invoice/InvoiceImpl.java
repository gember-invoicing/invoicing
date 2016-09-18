package nl.marcenschede.invoice;

import nl.marcenschede.invoice.tariffs.VatPercentage;
import nl.marcenschede.invoice.tariffs.VatRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InvoiceImpl implements Invoice {
    private Company company;
    private Customer customer;
    private InvoiceType invoiceType;
    private Optional<String> countryOfOrigin;
    private Optional<String> productDestination;
    private List<InvoiceLine> invoiceLines = new ArrayList<>();

    @Override
    public void setCompany(Company company) {

        this.company = company;
    }

    @Override
    public void setCustomer(Customer customer) {

        this.customer = customer;
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
    public void setProductDestinationCountry(Optional<String> productDestination) {

        this.productDestination = productDestination;
    }

    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    @Override
    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {

        this.invoiceLines = invoiceLines;
    }

    @Override
    public void setProductCategory(Optional<ProductCategory> productCategory) {

    }

    @Override
    public void setVatShifted(Boolean aBoolean) {

    }

    @Override
    public BigDecimal getTotalInvoiceAmountInclVat() {
        return invoiceLines.stream()
                .map(InvoiceLine::getAmountSummary)
                .map(VatAmountSummary::getAmountInclVat)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountExclVat() {
        return invoiceLines.stream()
                .map(InvoiceLine::getAmountSummary)
                .map(VatAmountSummary::getAmountExclVat)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    @Override
    public BigDecimal getInvoiceTotalVat() {

        return invoiceLines.stream()
                .map(InvoiceLine::getAmountSummary)
                .map(VatAmountSummary::getAmountVat)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    @Override
    public Map<VatPercentage, VatAmountSummary> getVatPerVatTariff() {

        VatRepository vatRepository = new VatRepository();

        Map<VatPercentage, List<InvoiceLine>> mapOfInvoiceLinesPerVatPercentage = getMapOfPercentages(vatRepository);
        return getVatAmountSummaryPerPercentage(mapOfInvoiceLinesPerVatPercentage);
    }

    private Map<VatPercentage, VatAmountSummary> getVatAmountSummaryPerPercentage(Map<VatPercentage, List<InvoiceLine>> mapOfInvoiceLinesPerVatPercentage) {
        return mapOfInvoiceLinesPerVatPercentage.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        optionalListEntry -> calculateVatAmountForVatTariff(
                                optionalListEntry.getKey(),
                                optionalListEntry.getValue())));
    }

    private Map<VatPercentage, List<InvoiceLine>> getMapOfPercentages(VatRepository vatRepository) {
        return this.getInvoiceLines().stream()
                .collect(Collectors.groupingBy(
                        invoiceLine -> vatRepository.findByTariffAndDate(
                                this.countryOfOrigin.get(),
                                invoiceLine.getVatTariff(),
                                invoiceLine.getVatReferenceDate())));
    }

    private VatAmountSummary calculateVatAmountForVatTariff(VatPercentage percentage, List<InvoiceLine> invoiceLines) {

        final BigDecimal ZERO = new BigDecimal("0.00");

        return invoiceLines.stream()
                .map(InvoiceLine::getAmountSummary)
                .reduce(new VatAmountSummary(percentage, ZERO, ZERO, ZERO), VatAmountSummary::add);
    }
}
