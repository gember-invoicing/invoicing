package features;

import nl.marcenschede.invoice.Company;
import nl.marcenschede.invoice.VatCalculationPolicy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CompanyGenerator {
    private final String primaryCountry;
    private final String vatPolicy;

    public CompanyGenerator(String primaryCountry, String vatPolicy) {
        this.primaryCountry = primaryCountry;
        this.vatPolicy = vatPolicy;
    }

    public Company invoke() {
        return new Company() {
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
}
