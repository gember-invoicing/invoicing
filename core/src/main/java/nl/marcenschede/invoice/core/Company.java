package nl.marcenschede.invoice.core;

import java.util.Map;
import java.util.Optional;

public interface Company {
    public VatCalculationPolicy getVatCalculationPolicy();

    public String getPrimaryCountryIso();

    public Map<String, String> getVatRegistrations();

    boolean hasVatRegistrationFor(String isoOfCountryOfDestination);

    Optional<String> getVatRegistrationInCountry(String country);
}
