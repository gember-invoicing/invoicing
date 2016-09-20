package nl.marcenschede.invoice;

import nl.marcenschede.invoice.calcs.VatAmountSummaryFactory;
import nl.marcenschede.invoice.calculators.InvoiceCalculationsDelegate;
import nl.marcenschede.invoice.calculators.InvoiceCalculatorHelperFactory;
import nl.marcenschede.invoice.tariffs.VatPercentage;
import nl.marcenschede.invoice.tariffs.VatRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InvoiceImpl implements Invoice {
    private Company company;
    private Customer customer;
    private InvoiceType invoiceType;
    private Optional<String> countryOfOrigin;
    private Optional<String> countryOfDestination;
    private List<InvoiceLine> invoiceLines = new ArrayList<>();

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
    public void setProductCategory(Optional<ProductCategory> productCategory) {

    }

    @Override
    public void setVatShifted(Boolean aBoolean) {

    }

    @Override
    public BigDecimal getTotalInvoiceAmountInclVat() {
        InvoiceCalculationsDelegate calculationsHelper = InvoiceCalculatorHelperFactory.newInstance(this);

        Function<? super InvoiceLine, VatAmountSummary> vatCalculator =
                VatAmountSummaryFactory.create(calculationsHelper.getVatDeclarationCountry());

        return calculationsHelper.getTotalInvoiceAmountInclVat(vatCalculator);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountExclVat() {
        InvoiceCalculationsDelegate calculationsHelper = InvoiceCalculatorHelperFactory.newInstance(this);

        Function<? super InvoiceLine, VatAmountSummary> vatCalculator =
                VatAmountSummaryFactory.create(calculationsHelper.getVatDeclarationCountry());

        return calculationsHelper.getTotalInvoiceAmountExclVat(vatCalculator);
    }

    @Override
    public BigDecimal getInvoiceTotalVat() {
        InvoiceCalculationsDelegate calculationsHelper = InvoiceCalculatorHelperFactory.newInstance(this);

        Function<? super InvoiceLine, VatAmountSummary> vatCalculator =
                VatAmountSummaryFactory.create(calculationsHelper.getVatDeclarationCountry());

        return calculationsHelper.getInvoiceTotalVat(vatCalculator);
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

        final InvoiceCalculationsDelegate calculationsHelper = InvoiceCalculatorHelperFactory.newInstance(this);

        return this.getInvoiceLines().stream()
                .collect(Collectors.groupingBy(
                        invoiceLine -> vatRepository.findByCountryTariffAndDate(
                                calculationsHelper.getVatDeclarationCountry(),
                                invoiceLine.getVatTariff(),
                                invoiceLine.getVatReferenceDate())));
    }

    private VatAmountSummary calculateVatAmountForVatTariff(VatPercentage percentage, List<InvoiceLine> invoiceLines) {

        final BigDecimal ZERO = new BigDecimal("0.00");
        final InvoiceCalculationsDelegate calculationsHelper = InvoiceCalculatorHelperFactory.newInstance(this);

        Function<? super InvoiceLine, VatAmountSummary> vatCalculator =
                VatAmountSummaryFactory.create(calculationsHelper.getVatDeclarationCountry());

        return invoiceLines.stream()
                .map(vatCalculator)
                .reduce(new VatAmountSummary(percentage, ZERO, ZERO, ZERO), VatAmountSummary::add);
    }

    public static class NoRegistrationInOriginCountryException extends RuntimeException {
    }
}
