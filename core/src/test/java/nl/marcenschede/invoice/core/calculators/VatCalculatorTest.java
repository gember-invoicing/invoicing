package nl.marcenschede.invoice.core.calculators;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertThat;

public class VatCalculatorTest {

    @Test
    public void shouldReduceVatFromBaseAmount() throws Exception {

        BigDecimal reducedAmount = VatCalculator.reduceVatFromLineAmount(new BigDecimal("121.00"), new BigDecimal("21.00"));
        assertThat(reducedAmount, Matchers.is(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReduceVatFromBaseAmount_largeAmount() throws Exception {

        BigDecimal reducedAmount = VatCalculator.reduceVatFromLineAmount(new BigDecimal("121000.00"), new BigDecimal("21.00"));
        assertThat(reducedAmount, Matchers.is(new BigDecimal("100000.00")));
    }

    @Test
    public void shouldReduceAndRoundVatFromBaseAmount() throws Exception {

        BigDecimal reducedAmount = VatCalculator.reduceVatFromLineAmount(new BigDecimal("1.00"), new BigDecimal("21.00"));
        assertThat(reducedAmount, Matchers.is(new BigDecimal("0.83")));
    }

    @Test
    public void shouldIncreateWithVat() {

        BigDecimal actualValue = VatCalculator.increateWithVat(new BigDecimal("100.00"), new BigDecimal("21.00"));
        assertThat(actualValue, Matchers.is(new BigDecimal("121.00")));
    }

    @Test
    public void shouldIncreateWithVatWithRounding() {

        BigDecimal actualValue = VatCalculator.increateWithVat(new BigDecimal("0.08"), new BigDecimal("21.00"));
        assertThat(actualValue, Matchers.is(new BigDecimal("0.10")));
    }

    @Test
    public void shouldCalculateVatFromLineAmountInclVat() {

        BigDecimal actualValue =
                VatCalculator.calculateVatFromLineAmountInclVat(new BigDecimal("121.00"), new BigDecimal("21.00"));
        assertThat(actualValue, Matchers.is(new BigDecimal("21.00")));
    }

    @Test
    public void shouldCalculateFromAmountExclVat() {
        BigDecimal actualValue =
                VatCalculator.calculateVatFromLineAmountExclVat(new BigDecimal("100.00"), new BigDecimal("21.00"));
        assertThat(actualValue, Matchers.is(new BigDecimal("21.00")));
    }
}