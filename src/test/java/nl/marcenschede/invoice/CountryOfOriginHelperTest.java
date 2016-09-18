package nl.marcenschede.invoice;

import nl.marcenschede.invoice.tariffs.VatPercentage;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CountryOfOriginHelperTest {

    @Test
    public void shouldSelectOriginCountryFromInvoice() {

        Invoice invoice = new Invoice() {
            @Override
            public Company getCompany() {
                return null;
            }

            @Override
            public void setCompany(Company company) {

            }

            @Override
            public Customer getCustomer() {
                return null;
            }

            @Override
            public void setCustomer(Customer customer) {

            }

            @Override
            public void setInvoiceType(InvoiceType invoiceType) {

            }

            @Override
            public Optional<String> getCountryOfOrigin() {
                return Optional.of("XX");
            }

            @Override
            public void setCountryOfOrigin(Optional<String> productOrigin) {

            }

            @Override
            public void setProductDestinationCountry(Optional<String> productDestination) {

            }

            @Override
            public void setInvoiceLines(List<InvoiceLine> invoiceLines) {

            }

            @Override
            public void setProductCategory(Optional<ProductCategory> productCategory) {

            }

            @Override
            public void setVatShifted(Boolean aBoolean) {

            }

            @Override
            public BigDecimal getTotalInvoiceAmountInclVat() {
                return null;
            }

            @Override
            public BigDecimal getTotalInvoiceAmountExclVat() {
                return null;
            }

            @Override
            public BigDecimal getInvoiceTotalVat() {
                return null;
            }

            @Override
            public Map<VatPercentage, VatAmountSummary> getVatPerVatTariff() {
                return null;
            }
        };

        String originCountry = CountryOfOriginHelper.getOriginCountry(invoice);

        assertThat(originCountry, is("XX"));
    }

    @Test
    public void shouldSelectOriginCountryFromCompany() {

        Invoice invoice = new Invoice() {
            @Override
            public Company getCompany() {
                return new Company() {
                    @Override
                    public VatCalculationPolicy getVatCalculationPolicy() {
                        return null;
                    }

                    @Override
                    public String getPrimaryCountryIso() {
                        return "YY";
                    }

                    @Override
                    public Map<String, String> getVatRegistrations() {
                        return null;
                    }

                    @Override
                    public boolean hasVatRegistrationFor(String isoOfCountryOfDestination) {
                        return false;
                    }

                    @Override
                    public Optional<String> getVatRegistrationInCountry(String country) {
                        return null;
                    }
                };
            }

            @Override
            public void setCompany(Company company) {

            }

            @Override
            public Customer getCustomer() {
                return null;
            }

            @Override
            public void setCustomer(Customer customer) {

            }

            @Override
            public void setInvoiceType(InvoiceType invoiceType) {

            }

            @Override
            public Optional<String> getCountryOfOrigin() {
                return Optional.empty();
            }

            @Override
            public void setCountryOfOrigin(Optional<String> productOrigin) {

            }

            @Override
            public void setProductDestinationCountry(Optional<String> productDestination) {

            }

            @Override
            public void setInvoiceLines(List<InvoiceLine> invoiceLines) {

            }

            @Override
            public void setProductCategory(Optional<ProductCategory> productCategory) {

            }

            @Override
            public void setVatShifted(Boolean aBoolean) {

            }

            @Override
            public BigDecimal getTotalInvoiceAmountInclVat() {
                return null;
            }

            @Override
            public BigDecimal getTotalInvoiceAmountExclVat() {
                return null;
            }

            @Override
            public BigDecimal getInvoiceTotalVat() {
                return null;
            }

            @Override
            public Map<VatPercentage, VatAmountSummary> getVatPerVatTariff() {
                return null;
            }
        };

        String originCountry = CountryOfOriginHelper.getOriginCountry(invoice);

        assertThat(originCountry, is("YY"));
    }

    @Test(expected = CountryOfOriginHelper.NoOriginCountrySetException.class)
    public void shouldThrowExceptionSelectWhenOriginCountryFromCompanyNotSet() {

        Invoice invoice = new Invoice() {
            @Override
            public Company getCompany() {
                return new Company() {
                    @Override
                    public VatCalculationPolicy getVatCalculationPolicy() {
                        return null;
                    }

                    @Override
                    public String getPrimaryCountryIso() {
                        return null;
                    }

                    @Override
                    public Map<String, String> getVatRegistrations() {
                        return null;
                    }

                    @Override
                    public boolean hasVatRegistrationFor(String isoOfCountryOfDestination) {
                        return false;
                    }

                    @Override
                    public Optional<String> getVatRegistrationInCountry(String country) {
                        return null;
                    }
                };
            }

            @Override
            public void setCompany(Company company) {

            }

            @Override
            public Customer getCustomer() {
                return null;
            }

            @Override
            public void setCustomer(Customer customer) {

            }

            @Override
            public void setInvoiceType(InvoiceType invoiceType) {

            }

            @Override
            public Optional<String> getCountryOfOrigin() {
                return Optional.empty();
            }

            @Override
            public void setCountryOfOrigin(Optional<String> productOrigin) {

            }

            @Override
            public void setProductDestinationCountry(Optional<String> productDestination) {

            }

            @Override
            public void setInvoiceLines(List<InvoiceLine> invoiceLines) {

            }

            @Override
            public void setProductCategory(Optional<ProductCategory> productCategory) {

            }

            @Override
            public void setVatShifted(Boolean aBoolean) {

            }

            @Override
            public BigDecimal getTotalInvoiceAmountInclVat() {
                return null;
            }

            @Override
            public BigDecimal getTotalInvoiceAmountExclVat() {
                return null;
            }

            @Override
            public BigDecimal getInvoiceTotalVat() {
                return null;
            }

            @Override
            public Map<VatPercentage, VatAmountSummary> getVatPerVatTariff() {
                return null;
            }
        };

        String originCountry = CountryOfOriginHelper.getOriginCountry(invoice);
    }

}