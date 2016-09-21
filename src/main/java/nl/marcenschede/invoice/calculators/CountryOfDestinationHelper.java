package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Customer;
import nl.marcenschede.invoice.Invoice;

import java.util.Optional;

public class CountryOfDestinationHelper {

    public static String getDestinationCountry(Invoice invoice) {
        Optional<String> invoiceDestinationCountry = invoice.getCountryOfDestination();
        return invoiceDestinationCountry.orElseGet(() -> getCustomerDefaultCountryAndCheckIfNotBlank(invoice));
    }

    private static String getCustomerDefaultCountryAndCheckIfNotBlank(Invoice invoice) {
        final Customer customer = invoice.getCustomer();
        String destinationCountry =
                customer.getDefaultCountry().orElseThrow(() -> new NoDestinationCountrySetException());

        return destinationCountry;
    }

    public static class NoDestinationCountrySetException extends RuntimeException {
    }
}
