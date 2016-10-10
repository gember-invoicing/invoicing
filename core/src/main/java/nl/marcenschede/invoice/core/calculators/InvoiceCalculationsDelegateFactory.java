package nl.marcenschede.invoice.core.calculators;

import nl.marcenschede.invoice.core.Invoice;
import nl.marcenschede.invoice.core.ProductCategory;

public class InvoiceCalculationsDelegateFactory {

    public static InvoiceCalculationsDelegate newInstance(Invoice invoice) {
        if (isNational(invoice) && !isVatShifted(invoice))
            return new InvoiceNationalCalculationsDelegate(invoice);

        if (isNational(invoice) && isVatShifted(invoice))
            return new InvoiceNationalVatShiftedCalculationsDelegate(invoice);

        if (isEuTransaction(invoice))
            return getInvoiceCalculatorForEuTransaction(invoice);

        return getInvoiceCalculatorForExportTransaction(invoice);
    }

    private static boolean isVatShifted(Invoice invoice) {
        return invoice.getVatShifted();
    }

    private static boolean isNational(Invoice invoice) {
        String originCountry = CountryOfOriginHelper.getOriginCountry(invoice);
        String destinationCountry = CountryOfDestinationHelper.getDestinationCountry(invoice);

        return originCountry.equals(destinationCountry);
    }

    private static boolean isEuTransaction(Invoice invoice) {
        String destinationCountry = CountryOfDestinationHelper.getDestinationCountry(invoice);

        return EuCountry.isEuCountry(destinationCountry);
    }

    private static InvoiceCalculationsDelegate getInvoiceCalculatorForEuTransaction(Invoice invoice) {
        if (isBusinessCustomer(invoice) && invoice.getProductCategory().orElse(null) == ProductCategory.GOODS)
            return new InvoiceEuGoodsCalculationsDelegate(invoice);

        if (isBusinessCustomer(invoice) && invoice.getProductCategory().orElse(null) == ProductCategory.SERVICES)
            if (!isVatShifted(invoice))
                return new InvoiceEuServicesCalculationsDelegate(invoice);
            else
                return new InvoiceEuServicesVatShiftedCalculationsDelegate(invoice);

        if (isBusinessCustomer(invoice) && invoice.getProductCategory().orElse(null) == ProductCategory.E_SERVICES)
            if (!isVatShifted(invoice))
                return new InvoiceEuEServicesCalculationsDelegate(invoice);
            else
                return new InvoiceEuEServicesVatShiftedCalculationsDelegate(invoice);

        return new InvoiceEuB2CCalculationsDelegate(invoice);
    }

    private static InvoiceCalculationsDelegate getInvoiceCalculatorForExportTransaction(Invoice invoice) {
        if (!isVatShifted(invoice))
            return new InvoiceExportCalculationsDelegate(invoice);
        else
            return new InvoiceExportVatShiftedCalculationsDelegate(invoice);
    }

    private static Boolean isBusinessCustomer(Invoice invoice) {
        return invoice.getCustomer().getEuTaxId().isPresent();
    }
}
