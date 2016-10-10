package nl.marcenschede.invoice.tariffs;

import java.time.LocalDate;

/**
 * Created by marc on 10/10/2016.
 */
public interface VatRepository {
    CountryTariffPeriodPercentageTuple findByCountryTariffAndDate(String country, VatTariff vatTariff, LocalDate referenceDate);
}
