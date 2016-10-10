package nl.marcenschede.invoice.core;

import java.util.Optional;

public interface Customer {
    Optional<String> getDefaultCountry();

    Optional<String> getEuTaxId();

    Long getCustomerDebtorId();
}
