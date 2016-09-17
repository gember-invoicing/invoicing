package nl.marcenschede.invoice.calculators;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class VatCalculator {
    private final static BigDecimal HUNDRED = new BigDecimal("100.00");

    public static BigDecimal reduceVatFromLineAmount(BigDecimal base, BigDecimal vatPercentage) {

        BigDecimal devisionFactor = calculateConversionFactor(vatPercentage);
        return base.divide(devisionFactor, 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal increateWithVat(BigDecimal base, BigDecimal vatPercentage) {

        BigDecimal multiplicationFactor = calculateConversionFactor(vatPercentage);
        return base.multiply(multiplicationFactor).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateVatFromLineAmountInclVat(BigDecimal base, BigDecimal percentage) {

        BigDecimal factorA = percentage.divide(HUNDRED);
        BigDecimal factorB = calculateConversionFactor(percentage);

        return base.multiply(factorA).divide(factorB, 2, BigDecimal.ROUND_HALF_UP);
    }

    private static BigDecimal calculateConversionFactor(BigDecimal vatPercentage) {
        return BigDecimal.ONE.add(vatPercentage.divide(HUNDRED));
    }
}
