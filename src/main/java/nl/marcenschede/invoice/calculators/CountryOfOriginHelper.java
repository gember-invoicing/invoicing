package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Company;
import nl.marcenschede.invoice.Customer;
import nl.marcenschede.invoice.Invoice;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class CountryOfOriginHelper {

    public static String getOriginCountry(Invoice invoice) {
        final Optional<String> invoiceOriginCountry = invoice.getCountryOfOrigin();
        return invoiceOriginCountry.orElseGet(() -> getCompanyPrimaryCountryAndCheckIfNotBlank(invoice));
    }

    public static String getDestinationCountry(Invoice invoice) {
        Optional<String> invoiceDestinationCountry = invoice.getCountryOfDestination();
        return invoiceDestinationCountry.orElseGet(() -> getCustomerDefaultCountryAndCheckIfNotBlank(invoice));
    }

    private static String getCompanyPrimaryCountryAndCheckIfNotBlank(Invoice invoice) {
        String companyPrimaryCountry = getCompanyDefaultCountry(invoice);

        if(StringUtils.isBlank(companyPrimaryCountry))
            throw new CountryOfOriginHelper.NoOriginCountrySetException();

        return companyPrimaryCountry;
    }

    private static String getCustomerDefaultCountryAndCheckIfNotBlank(Invoice invoice) {
        final Customer customer = invoice.getCustomer();
        String destinationCountry =
                customer.getDefaultCountry().orElseThrow(() -> new NoDestinationCountrySetException());

        return destinationCountry;
    }

    private static String getCompanyDefaultCountry(Invoice invoice) {
        final Company company = invoice.getCompany();
        final String primaryCountryIso = company.getPrimaryCountryIso();

        return primaryCountryIso;
    }

    public static class NoOriginCountrySetException extends RuntimeException {
    }

    public static class NoDestinationCountrySetException extends RuntimeException {
    }
}
