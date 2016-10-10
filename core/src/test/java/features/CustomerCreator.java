package features;

import nl.marcenschede.invoice.core.Customer;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class CustomerCreator {
    private final String vatId;
    private final String defaultCountry;

    public CustomerCreator(String vatId, String defaultCountry) {
        this.vatId = vatId;
        this.defaultCountry = defaultCountry;
    }

    public Customer invoke() {
        return new Customer() {
            @Override
            public Optional<String> getDefaultCountry() {
                return Optional.ofNullable(StringUtils.isBlank(defaultCountry) ? null : defaultCountry);
            }

            @Override
            public Optional<String> getEuTaxId() {
                return Optional.ofNullable(vatId);
            }

            @Override
            public Long getCustomerDebtorId() {
                return 2345L;
            }
        };
    }
}