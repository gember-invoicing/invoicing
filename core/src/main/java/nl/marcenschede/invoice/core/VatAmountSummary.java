package nl.marcenschede.invoice.core;

import nl.marcenschede.invoice.core.tariffs.CountryTariffPeriodPercentageTuple;

import java.math.BigDecimal;

public class VatAmountSummary {
    private CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple;
    private BigDecimal amountVat;
    private BigDecimal amountExclVat;
    private BigDecimal amountInclVat;

    public VatAmountSummary(CountryTariffPeriodPercentageTuple countryTariffPeriodPercentageTuple, BigDecimal amountVat, BigDecimal amountExclVat, BigDecimal amountInclVat) {
        this.countryTariffPeriodPercentageTuple = countryTariffPeriodPercentageTuple;
        this.amountVat = amountVat;
        this.amountExclVat = amountExclVat;
        this.amountInclVat = amountInclVat;
    }

    public VatAmountSummary(VatAmountSummary vatAmountSummary) {
        this(vatAmountSummary.getCountryTariffPeriodPercentageTuple(),
                vatAmountSummary.getAmountVat(),
                vatAmountSummary.getAmountExclVat(),
                vatAmountSummary.getAmountInclVat());
    }

    public BigDecimal getAmountVat() {
        return amountVat;
    }

    public CountryTariffPeriodPercentageTuple getCountryTariffPeriodPercentageTuple() {
        return countryTariffPeriodPercentageTuple;
    }

    public BigDecimal getAmountExclVat() {
        return amountExclVat;
    }

    public BigDecimal getAmountInclVat() {
        return amountInclVat;
    }

    public VatAmountSummary add(VatAmountSummary operand) {
        BigDecimal amountInclVat = this.getAmountInclVat().add(operand.amountInclVat);
        BigDecimal amountExclVat = this.getAmountExclVat().add(operand.amountExclVat);
        BigDecimal amountVat = this.getAmountVat().add(operand.getAmountVat());

        return new VatAmountSummary(this.getCountryTariffPeriodPercentageTuple(), amountVat, amountExclVat, amountInclVat);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VatAmountSummary that = (VatAmountSummary) o;

        if (countryTariffPeriodPercentageTuple != null ? !countryTariffPeriodPercentageTuple.equals(that.countryTariffPeriodPercentageTuple) : that.countryTariffPeriodPercentageTuple != null)
            return false;
        if (amountVat != null ? !amountVat.equals(that.amountVat) : that.amountVat != null) return false;
        if (amountExclVat != null ? !amountExclVat.equals(that.amountExclVat) : that.amountExclVat != null)
            return false;
        return amountInclVat != null ? amountInclVat.equals(that.amountInclVat) : that.amountInclVat == null;

    }

    @Override
    public int hashCode() {
        int result = countryTariffPeriodPercentageTuple != null ? countryTariffPeriodPercentageTuple.hashCode() : 0;
        result = 31 * result + (amountVat != null ? amountVat.hashCode() : 0);
        result = 31 * result + (amountExclVat != null ? amountExclVat.hashCode() : 0);
        result = 31 * result + (amountInclVat != null ? amountInclVat.hashCode() : 0);
        return result;
    }
}
