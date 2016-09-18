package features;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nl.marcenschede.invoice.*;
import nl.marcenschede.invoice.calculators.VatCalculator;
import nl.marcenschede.invoice.tariffs.VatPercentage;
import nl.marcenschede.invoice.tariffs.VatRepository;
import nl.marcenschede.invoice.tariffs.VatTariff;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class Glue {

    private final List<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();
    private Optional<String> productOrigin = Optional.empty();
    private Optional<String> productDestination = Optional.empty();
    private Optional<ProductCategory> productCategory = Optional.empty();
    private Optional<Boolean> vatShifted = Optional.empty();

    private Company company;
    private Customer customer;
    private Invoice invoice;

    @Given("^A company in \"([^\"]*)\" with vat calculation policy is \"([^\"]*)\"$")
    public void a_company_with_VAT_id_in_and_vat_calculation_policy_is(final String primaryCountry,
                                                                       final String vatPolicy) throws Throwable {
        company = new Company() {
            private Map<String, String> vatRegistrations = new HashMap<>();

            @Override
            public VatCalculationPolicy getVatCalculationPolicy() {
                return VatCalculationPolicy.valueOf(vatPolicy);
            }

            @Override
            public String getPrimaryCountryIso() {
                return primaryCountry;
            }

            @Override
            public Map<String, String> getVatRegistrations() {
                return vatRegistrations;
            }

            @Override
            public boolean hasVatRegistrationFor(String isoOfcountryOfDestination) {
                return vatRegistrations.containsKey(isoOfcountryOfDestination);
            }

            @Override
            public Optional<String> getVatRegistrationInCountry(String country) {
                return Optional.ofNullable(vatRegistrations.get(country));
            }
        };

    }

    @Given("^the company has VAT id \"([^\"]*)\" in \"([^\"]*)\"$")
    public void the_company_has_VAT_id_in(String vatId, String companyCountry) throws Throwable {

        company.getVatRegistrations().put(companyCountry, vatId);
    }

    @Given("^An invoiceline worth \"([^\"]*)\" euro \"([^\"]*)\" VAT with \"([^\"]*)\" vat level and referencedate is \"([^\"]*)\"$")
    public void an_invoiceline_worth_euro_VAT_with_vat_level_and_referencedate_is(final String lineAmount,
                                                                                  final String inclExclVat,
                                                                                  final String vatTariff,
                                                                                  final String referenceDate) throws Throwable {

        InvoiceLine invoiceLine = new InvoiceLine() {
            private final VatRepository vatRepository = new VatRepository();
            private Invoice invoice;

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

            @Override
            public void setInvoice(Invoice invoice) {
                this.invoice = invoice;
            }

            @Override
            public VatAmountSummary getAmountSummary() {
                return new VatAmountSummary(getVatPercentage(), getVatAmount(), getLineAmountExclVat(), getLineAmountInclVat());
            }

            private BigDecimal getLineAmountInclVat() {
                return getInvoiceLineVatType() == InvoiceLineVatType.INCLUDING_VAT ?
                        new BigDecimal(lineAmount) : new BigDecimal("0.00");
            }

            private BigDecimal getLineAmountExclVat() {
                return getInvoiceLineVatType() == InvoiceLineVatType.EXCLUDING_VAT ?
                        new BigDecimal(lineAmount) : calculateLineAmountExclVat();
            }

            private BigDecimal getVatAmount() {
                return getInvoiceLineVatType() == InvoiceLineVatType.EXCLUDING_VAT?
                        new BigDecimal("0.00") : calculateVatFromLineAmountInclVat();

            }

            private BigDecimal calculateVatFromLineAmountInclVat() {

                BigDecimal percentage = getVatPercentage().getPercentage();
                return VatCalculator.calculateVatFromLineAmountInclVat(new BigDecimal(lineAmount), percentage);
            }

            private BigDecimal calculateLineAmountExclVat() {

                BigDecimal percentage = getVatPercentage().getPercentage();
                return VatCalculator.reduceVatFromLineAmount(new BigDecimal(lineAmount), percentage);
            }

            private VatPercentage getVatPercentage() {

                return vatRepository.findByOriginCountryTariffAndDate(
                                    invoice, getVatTariff(), LocalDate.parse(referenceDate));
            }

        };

        invoiceLines.add(invoiceLine);
    }

    @Given("^A customer without a validated VAT id and default country is \"([^\"]*)\"$")
    public void a_customer_without_a_validated_VAT_id(String defaultCountry) throws Throwable {

        customer = new Customer() {
            @Override
            public Optional<String> getDefaultCountry() {
                return Optional.ofNullable(StringUtils.isEmpty(defaultCountry) ? null : defaultCountry);
            }

            @Override
            public Optional<String> getEuTaxId() {
                return Optional.empty();
            }
        };

    }

    @Given("^Country of origin is \"([^\"]*)\"$")
    public void country_of_origin_is(String countryCode) throws Throwable {

        productOrigin = Optional.of(countryCode);
    }

    @Given("^Country of destination is \"([^\"]*)\"$")
    public void country_of_destination_is(String countryCode) throws Throwable {

        productDestination = Optional.of(countryCode);
    }

    @When("^A \"([^\"]*)\" invoice is created at \"([^\"]*)\"$")
    public void a_invoice_is_created_at(String invoiceTypeVal, String invoiceDate) throws Throwable {
        InvoiceType invoiceType = InvoiceType.valueOf(invoiceTypeVal.toUpperCase());

        Invoice invoice = new InvoiceImpl();
        invoice.setCompany(company);
        invoice.setCustomer(customer);
        invoice.setInvoiceType(invoiceType);
        invoice.setCountryOfOrigin(productOrigin);
        invoice.setProductDestinationCountry(productDestination);
        invoice.setProductCategory(productCategory);
        invoice.setVatShifted(vatShifted.orElse(false));
        invoice.setInvoiceLines(invoiceLines);

        invoiceLines.forEach(invoiceLine -> invoiceLine.setInvoice(invoice));

        this.invoice = invoice;
    }

    @Then("^The total amount including VAT is \"([^\"]*)\"$")
    public void the_total_amount_including_VAT_is(String expectedTotalAmountIncludingVat) throws Throwable {
        assert invoice != null;

        BigDecimal totalInvoiceAmountInclVat = invoice.getTotalInvoiceAmountInclVat();

        assertThat(totalInvoiceAmountInclVat, is(new BigDecimal(expectedTotalAmountIncludingVat)));
    }


    @Then("^The total amount excluding VAT is \"([^\"]*)\"$")
    public void the_total_amount_excluding_VAT_is(String expectedTotalAmountExclVat) throws Throwable {
        assert invoice != null;

        assertThat(invoice.getTotalInvoiceAmountExclVat(), is(new BigDecimal(expectedTotalAmountExclVat)));
    }

    @Then("^The total amount VAT is \"([^\"]*)\"$")
    public void the_total_amount_VAT_is(String expectedTotalAmountVat) throws Throwable {
        assert invoice != null;

        BigDecimal invoiceTotalVat = invoice.getInvoiceTotalVat();

        assertThat(invoiceTotalVat, is(new BigDecimal(expectedTotalAmountVat)));
    }

    @Then("^The VAT amount for percentage \"([^\"]*)\" is \"([^\"]*)\"$")
    public void the_VAT_amount_for_percentage_is(String percentage, String expectedAmount) throws Throwable {
        assert invoice != null;

        Optional<BigDecimal> actualAmount =
                invoice.getVatPerVatTariff().entrySet().stream()
                        .filter(entry -> entry.getKey().getPercentage().equals(new BigDecimal(percentage)))
                        .map(entry -> entry.getValue().getAmountVat())
                        .findFirst();

        assertThat(actualAmount.isPresent(), is(true));
        assertThat(actualAmount.get(), is(new BigDecimal(expectedAmount)));
    }

    @Then("^The total amount including VAT request throws an no origin country set exception$")
    public void the_total_amount_including_VAT_request_throws_an_no_origin_country_set_exception() throws Throwable {
        try {
            invoice.getTotalInvoiceAmountInclVat();
        } catch (VatRepository.NoOriginCountrySetException nocs) {
            return;
        }

        fail();
    }

    @Then("^The total amount excluding VAT request throws an no origin country set exception$")
    public void the_total_amount_excluding_VAT_request_throws_an_no_origin_country_set_exception() throws Throwable {
        try {
            invoice.getTotalInvoiceAmountExclVat();
        } catch (VatRepository.NoOriginCountrySetException nocs) {
            return;
        }

        fail();
    }

    @Then("^The total amount VAT request throws an no origin country set exception$")
    public void the_total_amount_VAT_request_throws_an_no_origin_country_set_exception() throws Throwable {
        try {
            invoice.getInvoiceTotalVat();
        } catch (VatRepository.NoOriginCountrySetException nocs) {
            return;
        }

        fail();
    }

    @Then("^The total amount including VAT request throws an vat percentage not found exception$")
    public void the_total_amount_including_VAT_request_throws_an_vat_percentage_not_found_exception() throws Throwable {
        try {
            invoice.getTotalInvoiceAmountInclVat();
        } catch (VatRepository.VatPercentageNotFoundException nocs) {
            return;
        }

        fail();
    }

    @Then("^The total amount excluding VAT request throws an vat percentage not found exception$")
    public void the_total_amount_excluding_VAT_request_throws_an_vat_percentage_not_found_exception() throws Throwable {
        try {
            invoice.getTotalInvoiceAmountExclVat();
        } catch (VatRepository.VatPercentageNotFoundException nocs) {
            return;
        }

        fail();
    }

    @Then("^The total amount VAT request throws an vat percentage not found exception$")
    public void the_total_amount_VAT_request_throws_an_vat_percentage_not_found_exception() throws Throwable {
        try {
            invoice.getInvoiceTotalVat();
        } catch (VatRepository.VatPercentageNotFoundException nocs) {
            return;
        }

        fail();
    }
}
