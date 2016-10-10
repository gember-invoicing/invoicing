package nl.marcenschede.invoice.data;

import nl.marcenschede.invoice.tariffs.CountryTariffPeriodPercentageTuple;
import nl.marcenschede.invoice.tariffs.VatRepository;
import nl.marcenschede.invoice.tariffs.VatTariff;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class VatRepositoryImpl implements VatRepository {

    private List<CountryTariffPeriodPercentageTuple> allPercentages;

    public VatRepositoryImpl() {
        String nl = "NL";
        String de = "DE";
        String be = "BE";

        allPercentages = new ArrayList<>();

        allPercentages.add(new CountryTariffPeriodPercentageTuple(be, VatTariff.ZERO, LocalDate.of(2016, 1, 1), null, new BigDecimal("0.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(be, VatTariff.LOW1, LocalDate.of(2016, 1, 1), null, new BigDecimal("6.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(be, VatTariff.LOW2, LocalDate.of(2016, 1, 1), null, new BigDecimal("12.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(be, VatTariff.HIGH, LocalDate.of(2016, 1, 1), null, new BigDecimal("19.00")));

        allPercentages.add(new CountryTariffPeriodPercentageTuple(de, VatTariff.ZERO, LocalDate.of(1983, 7, 1), null, new BigDecimal("0.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(de, VatTariff.LOW1, LocalDate.of(1983, 7, 1), null, new BigDecimal("7.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(de, VatTariff.HIGH, LocalDate.of(1983, 7, 1), LocalDate.of(1992, 12, 31), new BigDecimal("14.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(de, VatTariff.HIGH, LocalDate.of(1993, 1, 1), LocalDate.of(1998, 3, 31), new BigDecimal("15.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(de, VatTariff.HIGH, LocalDate.of(1998, 4, 1), LocalDate.of(2006, 12, 31), new BigDecimal("16.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(de, VatTariff.HIGH, LocalDate.of(2007, 1, 1), null, new BigDecimal("19.00")));

        allPercentages.add(new CountryTariffPeriodPercentageTuple(nl, VatTariff.ZERO, LocalDate.of(1986, 10, 1), null, new BigDecimal("0.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(nl, VatTariff.LOW1, LocalDate.of(1986, 10, 1), null, new BigDecimal("6.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(nl, VatTariff.HIGH, LocalDate.of(1986, 10, 1), LocalDate.of(1988, 12, 31), new BigDecimal("20.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(nl, VatTariff.HIGH, LocalDate.of(1989, 1, 1), LocalDate.of(1992, 9, 30), new BigDecimal("18.50")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(nl, VatTariff.HIGH, LocalDate.of(1992, 10, 1), LocalDate.of(2000, 12, 31), new BigDecimal("17.50")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(nl, VatTariff.HIGH, LocalDate.of(2001, 1, 1), LocalDate.of(2012, 9, 30), new BigDecimal("19.00")));
        allPercentages.add(new CountryTariffPeriodPercentageTuple(nl, VatTariff.HIGH, LocalDate.of(2012, 10, 1), null, new BigDecimal("21.00")));
    }

    @Override
    public CountryTariffPeriodPercentageTuple findByCountryTariffAndDate(String country, VatTariff vatTariff, LocalDate referenceDate) {
        Optional<CountryTariffPeriodPercentageTuple> vatPercentage = getValidVatPercentage(country, vatTariff, referenceDate);
        return vatPercentage.orElseThrow(VatPercentageNotFoundException::new);
    }

    private Optional<CountryTariffPeriodPercentageTuple> getValidVatPercentage(String country, VatTariff vatTariff, LocalDate referenceDate) {
        return allPercentages.stream()
                    .filter(isValidVatPercentatePredicate(country, vatTariff, referenceDate))
                    .findFirst();
    }

    private Predicate<CountryTariffPeriodPercentageTuple> isValidVatPercentatePredicate(String country, VatTariff vatTariff, LocalDate referenceDate) {
        return isSelectedCountryPredicate(country)
                .and(isSelectedTariffPredicate(vatTariff))
                .and(isStartingOnOrBeforeReferenceDatePredicate(referenceDate))
                .and(isEndingOnOrAfterReferenceDateOrHasNoEndDatePredicate(referenceDate));
    }

    private Predicate<CountryTariffPeriodPercentageTuple> isEndingOnOrAfterReferenceDateOrHasNoEndDatePredicate(LocalDate referenceDate) {
        return vatPercentage1 -> (vatPercentage1.getEndDate() == null || vatPercentage1.getEndDate().compareTo(referenceDate) >= 0);
    }

    private Predicate<CountryTariffPeriodPercentageTuple> isStartingOnOrBeforeReferenceDatePredicate(LocalDate referenceDate) {
        return vatPercentage -> vatPercentage.getStartDate().compareTo(referenceDate) <= 0;
    }

    private Predicate<CountryTariffPeriodPercentageTuple> isSelectedTariffPredicate(VatTariff vatTariff) {
        return vatPercentage -> vatPercentage.getVatTariff() == vatTariff;
    }

    private Predicate<CountryTariffPeriodPercentageTuple> isSelectedCountryPredicate(String country) {
        return vatPercentage -> vatPercentage.getIsoCountryCode().equals(country);
    }

    public class VatPercentageNotFoundException extends RuntimeException {
    }

}
