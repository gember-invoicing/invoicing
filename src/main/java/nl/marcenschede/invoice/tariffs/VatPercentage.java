package nl.marcenschede.invoice.tariffs;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VatPercentage {

    String isoCountryCode;
    VatTariff vatTariff;
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal percentage;

    public VatPercentage(String isoCountryCode, VatTariff vatTariff, LocalDate startDate, LocalDate endDate, BigDecimal percentage) {
        this.isoCountryCode = isoCountryCode;
        this.vatTariff = vatTariff;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VatPercentage that = (VatPercentage) o;

        if (vatTariff != that.vatTariff) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        return percentage != null ? percentage.equals(that.percentage) : that.percentage == null;

    }

    @Override
    public int hashCode() {
        int result = vatTariff != null ? vatTariff.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (percentage != null ? percentage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VatPercentage{" +
                "isoCountryCode=" + isoCountryCode +
                ", vatTariff=" + vatTariff +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", percentage=" + percentage +
                '}';
    }
}
















