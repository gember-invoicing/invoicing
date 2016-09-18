package nl.marcenschede.invoice.tariffs;

import nl.marcenschede.invoice.Company;
import nl.marcenschede.invoice.CountryOfOriginHelper;
import nl.marcenschede.invoice.Invoice;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class VatRepository {

    private List<VatPercentage> allPercentages;

    public VatRepository() {
        String nl = new String("NL");
        String de = new String("DE");
        String be = new String("BE");

        allPercentages = new ArrayList<>();

        allPercentages.add(new VatPercentage(be, VatTariff.ZERO, LocalDate.of(2016, 1, 1), null, new BigDecimal("0.00")));
        allPercentages.add(new VatPercentage(be, VatTariff.LOW1, LocalDate.of(2016, 1, 1), null, new BigDecimal("6.00")));
        allPercentages.add(new VatPercentage(be, VatTariff.LOW2, LocalDate.of(2016, 1, 1), null, new BigDecimal("12.00")));
        allPercentages.add(new VatPercentage(be, VatTariff.HIGH, LocalDate.of(2016, 1, 1), null, new BigDecimal("19.00")));

        allPercentages.add(new VatPercentage(de, VatTariff.ZERO, LocalDate.of(1983, 7, 1), null, new BigDecimal("0.00")));
        allPercentages.add(new VatPercentage(de, VatTariff.LOW1, LocalDate.of(1983, 7, 1), null, new BigDecimal("7.00")));
        allPercentages.add(new VatPercentage(de, VatTariff.HIGH, LocalDate.of(1983, 7, 1), LocalDate.of(1992, 12, 31), new BigDecimal("14.00")));
        allPercentages.add(new VatPercentage(de, VatTariff.HIGH, LocalDate.of(1993, 1, 1), LocalDate.of(1998, 3, 31), new BigDecimal("15.00")));
        allPercentages.add(new VatPercentage(de, VatTariff.HIGH, LocalDate.of(1998, 4, 1), LocalDate.of(2006, 12, 31), new BigDecimal("16.00")));
        allPercentages.add(new VatPercentage(de, VatTariff.HIGH, LocalDate.of(2007, 1, 1), null, new BigDecimal("19.00")));

        allPercentages.add(new VatPercentage(nl, VatTariff.ZERO, LocalDate.of(1986, 10, 1), null, new BigDecimal("0.00")));
        allPercentages.add(new VatPercentage(nl, VatTariff.LOW1, LocalDate.of(1986, 10, 1), null, new BigDecimal("6.00")));
        allPercentages.add(new VatPercentage(nl, VatTariff.HIGH, LocalDate.of(1986, 10, 1), LocalDate.of(1988, 12, 31), new BigDecimal("20.00")));
        allPercentages.add(new VatPercentage(nl, VatTariff.HIGH, LocalDate.of(1989, 1, 1), LocalDate.of(1992, 9, 30), new BigDecimal("18.50")));
        allPercentages.add(new VatPercentage(nl, VatTariff.HIGH, LocalDate.of(1992, 10, 1), LocalDate.of(2000, 12, 31), new BigDecimal("17.50")));
        allPercentages.add(new VatPercentage(nl, VatTariff.HIGH, LocalDate.of(2001, 1, 1), LocalDate.of(2012, 9, 30), new BigDecimal("19.00")));
        allPercentages.add(new VatPercentage(nl, VatTariff.HIGH, LocalDate.of(2012, 10, 1), null, new BigDecimal("21.00")));
    }

    public VatPercentage findByOriginCountryTariffAndDate(Invoice invoice, VatTariff vatTariff, LocalDate referenceDate) {
        String originCountry = CountryOfOriginHelper.getOriginCountry(invoice);
        return findByCountryTariffAndDate(originCountry, vatTariff, referenceDate);
    }

    private VatPercentage findByCountryTariffAndDate(String country, VatTariff vatTariff, LocalDate referenceDate) {
        Optional<VatPercentage> vatPercentage = getValidVatPercentage(country, vatTariff, referenceDate);
        return vatPercentage.orElseThrow(VatPercentageNotFoundException::new);
    }

    private Optional<VatPercentage> getValidVatPercentage(String country, VatTariff vatTariff, LocalDate referenceDate) {
        return allPercentages.stream()
                    .filter(isValidVatPercentatePredicate(country, vatTariff, referenceDate))
                    .findFirst();
    }

    private Predicate<VatPercentage> isValidVatPercentatePredicate(String country, VatTariff vatTariff, LocalDate referenceDate) {
        return isSelectedCountryPredicate(country)
                .and(isSelectedTariffPredicate(vatTariff))
                .and(isStartingOnOrBeforeReferenceDatePredicate(referenceDate))
                .and(isEndingOnOrAfterReferenceDateOrHasNoEndDatePredicate(referenceDate));
    }

    private Predicate<VatPercentage> isEndingOnOrAfterReferenceDateOrHasNoEndDatePredicate(LocalDate referenceDate) {
        return vatPercentage1 -> (vatPercentage1.endDate == null || vatPercentage1.endDate.compareTo(referenceDate) >= 0);
    }

    private Predicate<VatPercentage> isStartingOnOrBeforeReferenceDatePredicate(LocalDate referenceDate) {
        return vatPercentage -> vatPercentage.startDate.compareTo(referenceDate) <= 0;
    }

    private Predicate<VatPercentage> isSelectedTariffPredicate(VatTariff vatTariff) {
        return vatPercentage -> vatPercentage.vatTariff == vatTariff;
    }

    private Predicate<VatPercentage> isSelectedCountryPredicate(String country) {
        return vatPercentage -> vatPercentage.isoCountryCode.equals(country);
    }

    public class VatPercentageNotFoundException extends RuntimeException {
    }

}
