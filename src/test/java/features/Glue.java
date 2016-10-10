package features;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nl.marcenschede.invoice.*;
import nl.marcenschede.invoice.calculators.CountryOfDestinationHelper;
import nl.marcenschede.invoice.calculators.CountryOfOriginHelper;
import nl.marcenschede.invoice.data.VatRepositoryImpl;
import nl.marcenschede.invoice.functional.InvoiceCalculatorFactory;
import nl.marcenschede.invoice.functional.InvoiceCreationEvent;
import nl.marcenschede.invoice.functional.InvoiceCreationFactory;
import nl.marcenschede.invoice.functional.InvoiceData;
import org.apache.commons.lang3.StringUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class Glue {

    private final List<InvoiceLine> invoiceLines = new ArrayList<>();
    private Optional<String> productOrigin = Optional.empty();
    private Optional<String> productDestination = Optional.empty();
    private Optional<ProductCategory> productCategory = Optional.empty();
    private Optional<Boolean> vatShifted = Optional.empty();

    private Company company;
    private Customer customer;
    private Function<InvoiceData, InvoiceTotals> invoiceCalculatorFactory;
    private InvoiceData invoiceData;

    @Given("^A company in \"([^\"]*)\" with vat calculation policy is \"([^\"]*)\"$")
    public void a_company_with_VAT_id_in_and_vat_calculation_policy_is(final String primaryCountry,
                                                                       final String vatPolicy) throws Throwable {

        company = new CompanyGenerator(primaryCountry, vatPolicy).invoke();
    }

    @Given("^the company has VAT id \"([^\"]*)\" in \"([^\"]*)\"$")
    public void the_company_has_VAT_id_in(String vatId, String companyCountry) throws Throwable {
        assert company != null;

        company.getVatRegistrations().put(companyCountry, vatId);
    }

    @Given("^An invoiceline worth \"([^\"]*)\" euro \"([^\"]*)\" VAT with \"([^\"]*)\" vat level and referencedate is \"([^\"]*)\"$")
    public void an_invoiceline_worth_euro_VAT_with_vat_level_and_referencedate_is(final String lineAmount,
                                                                                  final String inclExclVat,
                                                                                  final String vatTariff,
                                                                                  final String referenceDate) throws Throwable {

        InvoiceLine invoiceLine = new InvoiceLineCreator(lineAmount, inclExclVat, vatTariff, referenceDate).invoke();
        invoiceLines.add(invoiceLine);
    }

    @Given("^A customer without a validated VAT id and default country is \"([^\"]*)\"$")
    public void a_customer_without_a_validated_VAT_id(String defaultCountry) throws Throwable {

        customer = new CustomerCreator(null, defaultCountry).invoke();
    }

    @Given("^A customer with VAT id \"([^\"]*)\" and default country is \"([^\"]*)\"$")
    public void a_customer_with_VAT_id_and_default_country_is(String vatId, String defaultCountry) throws Throwable {

        customer = new CustomerCreator(vatId, defaultCountry).invoke();
    }

    @Given("^Country of origin is \"([^\"]*)\"$")
    public void country_of_origin_is(String countryCode) throws Throwable {

        productOrigin = Optional.of(countryCode);
    }

    @Given("^Country of destination is \"([^\"]*)\"$")
    public void country_of_destination_is(String countryCode) throws Throwable {

        productDestination = Optional.of(countryCode);
    }

    @Given("^The product category is \"([^\"]*)\"$")
    public void the_product_category_is(String productCategory) throws Throwable {

        this.productCategory = StringUtils.isEmpty(productCategory) ?
                Optional.empty() : Optional.of(ProductCategory.valueOf(productCategory));
    }

    @Given("^Vat is shifted$")
    public void vat_is_shifted() throws Throwable {

        vatShifted = Optional.of(true);
    }

    @When("^A \"([^\"]*)\" invoice is created at \"([^\"]*)\"$")
    public void a_invoice_is_created_at(String invoiceTypeVal, String invoiceDate) throws Throwable {

        invoiceCalculatorFactory = InvoiceCalculatorFactory.getInvoiceCalculatorFactory(company, new VatRepositoryImpl());

        invoiceData = new InvoiceData();
        invoiceData.setCustomer(customer);
        invoiceData.setInvoiceType(InvoiceType.valueOf(invoiceTypeVal.toUpperCase()));
        invoiceData.setCountryOfOrigin(productOrigin);
        invoiceData.setCountryOfDestination(productDestination);
        invoiceData.setProductCategory(productCategory);
        invoiceData.setVatShifted(vatShifted.orElse(false));
        invoiceData.setInvoiceLines(invoiceLines);
    }

    @Then("^The total amount including VAT is \"([^\"]*)\"$")
    public void the_total_amount_including_VAT_is(String expectedTotalAmountIncludingVat) throws Throwable {
        assert invoiceCalculatorFactory != null;
        assert invoiceData != null;

        final InvoiceTotals totals = invoiceCalculatorFactory.apply(invoiceData);

        assertThat(totals.totalInvoiceAmountInclVat, is(new BigDecimal(expectedTotalAmountIncludingVat)));
    }


    @Then("^The total amount excluding VAT is \"([^\"]*)\"$")
    public void the_total_amount_excluding_VAT_is(String expectedTotalAmountExclVat) throws Throwable {
        assert invoiceCalculatorFactory != null;
        assert invoiceData != null;

        final InvoiceTotals totals = invoiceCalculatorFactory.apply(invoiceData);

        assertThat(totals.totalInvoiceAmountExclVat, is(new BigDecimal(expectedTotalAmountExclVat)));
    }

    @Then("^The total amount VAT is \"([^\"]*)\"$")
    public void the_total_amount_VAT_is(String expectedTotalAmountVat) throws Throwable {
        assert invoiceCalculatorFactory != null;
        assert invoiceData != null;

        final InvoiceTotals totals = invoiceCalculatorFactory.apply(invoiceData);

        assertThat(totals.invoiceTotalVat, is(new BigDecimal(expectedTotalAmountVat)));
    }

    @Then("^The VAT amount for percentage \"([^\"]*)\" is \"([^\"]*)\"$")
    public void the_VAT_amount_for_percentage_is(String percentage, String expectedAmount) throws Throwable {
        assert invoiceCalculatorFactory != null;
        assert invoiceData != null;

        final InvoiceTotals totals = invoiceCalculatorFactory.apply(invoiceData);

        Optional<BigDecimal> actualAmount =
                totals.vatAmountSummaryPerPercentage.entrySet().stream()
                        .filter(entry -> entry.getKey().getPercentage().equals(new BigDecimal(percentage)))
                        .map(entry -> entry.getValue().getAmountVat())
                        .findFirst();

        assertThat(actualAmount.isPresent(), is(true));
        assertThat(actualAmount.get(), is(new BigDecimal(expectedAmount)));
    }

    @Then("^Invoice is attributed as VAT Shifted$")
    public void invoice_is_attributed_as_VAT_Shifted() throws Throwable {
        assert invoiceData != null;

        assertThat(invoiceData.getVatShifted(), is(true));
    }

    @Then("^The invoice calculation request throws an no origin country set exception$")
    public void the_invoice_calculation_VAT_request_throws_an_no_origin_country_set_exception() throws Throwable {
        assert invoiceCalculatorFactory != null;
        assert invoiceData != null;

        try {
            invoiceCalculatorFactory.apply(invoiceData);
        } catch (CountryOfOriginHelper.NoOriginCountrySetException nocs) {
            return;
        }

        fail();
    }

    @Then("^The invoice calculation request throws an vat percentage not found exception$")
    public void the_invoice_calculation_request_throws_an_vat_percentage_not_found_exception() throws Throwable {
        assert invoiceCalculatorFactory != null;
        assert invoiceData != null;

        try {
            invoiceCalculatorFactory.apply(invoiceData);
        } catch (VatRepositoryImpl.VatPercentageNotFoundException nocs) {
            return;
        }

        fail();
    }

    @Then("^The invoice calculation request throws an no registration in origin country exception$")
    public void the_invoice_calculation_request_throws_an_no_registration_in_origin_country_exception() throws Throwable {
        assert invoiceCalculatorFactory != null;
        assert invoiceData != null;

        try {
            invoiceCalculatorFactory.apply(invoiceData);
        } catch (InvoiceImpl.NoRegistrationInOriginCountryException e) {
            return;
        }

        fail();
    }

    @Then("^The invoice calculation request throws an no destination country set exception$")
    public void the_invoice_calculation_request_throws_an_no_destination_country_set_exception() throws Throwable {
        assert invoiceCalculatorFactory != null;
        assert invoiceData != null;

        try {
            invoiceCalculatorFactory.apply(invoiceData);
        } catch (CountryOfDestinationHelper.NoDestinationCountrySetException e) {
            return;
        }

        fail();
    }

    @Then("^An invoice creation event is created$")
    public void an_invoice_creation_event_is_created() throws Throwable {

        final Function<Consumer<InvoiceCreationEvent>, Consumer<InvoiceData>> consumerConsumerFunction =
                InvoiceCreationFactory.invoiceCreationFactory(getInvoiceCounter(), company, new VatRepositoryImpl());

        final Consumer consumer = mock(Consumer.class);

        final Consumer<InvoiceData> eventConsumer = consumerConsumerFunction.apply(consumer);
        eventConsumer.accept(invoiceData);

        verify(consumer, times(1)).accept(Mockito.any());
    }

    @When("^An invoice creation event is created with total invoice amount is \"([^\"]*)\"$")
    public void an_invoice_creation_event_is_created_with_total_invoice_amount_is(String expectedTotalAmountInclVat) throws Throwable {

        final Function<Consumer<InvoiceCreationEvent>, Consumer<InvoiceData>> consumerConsumerFunction =
                InvoiceCreationFactory.invoiceCreationFactory(getInvoiceCounter(), company, new VatRepositoryImpl());

        final Consumer consumer = mock(Consumer.class);
        ArgumentCaptor<InvoiceCreationEvent> eventArgumentCaptor = ArgumentCaptor.forClass(InvoiceCreationEvent.class);
        doNothing().when(consumer).accept(eventArgumentCaptor.capture());

        final Consumer<InvoiceData> eventConsumer = consumerConsumerFunction.apply(consumer);
        eventConsumer.accept(invoiceData);

        assertThat(
                eventArgumentCaptor.getValue().getInvoiceTotals().totalInvoiceAmountInclVat,
                is(new BigDecimal(expectedTotalAmountInclVat)));

    }

    @When("^An invoice creation event is created with invoice number is \"([^\"]*)\"$")
    public void an_invoice_creation_event_is_created_with_invoice_number_is(String expectedInvoiceNumber) throws Throwable {
        final Function<Consumer<InvoiceCreationEvent>, Consumer<InvoiceData>> consumerConsumerFunction =
                InvoiceCreationFactory.invoiceCreationFactory(getInvoiceCounter(), company, new VatRepositoryImpl());

        final Consumer consumer = mock(Consumer.class);
        ArgumentCaptor<InvoiceCreationEvent> eventArgumentCaptor = ArgumentCaptor.forClass(InvoiceCreationEvent.class);
        doNothing().when(consumer).accept(eventArgumentCaptor.capture());

        final Consumer<InvoiceData> eventConsumer = consumerConsumerFunction.apply(consumer);
        eventConsumer.accept(invoiceData);

        assertThat(
                eventArgumentCaptor.getValue().getInvoiceNumber(),
                is(new Long(expectedInvoiceNumber)));
    }

    public Supplier<Long> getInvoiceCounter() {
        return new InvoiceNumerGeneratorCreator().invoke();
    }

}
