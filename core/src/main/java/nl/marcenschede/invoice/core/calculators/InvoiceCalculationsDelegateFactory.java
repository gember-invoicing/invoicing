package nl.marcenschede.invoice.core.calculators;

import nl.marcenschede.invoice.core.Invoice;
import nl.marcenschede.invoice.core.ProductCategory;
import nl.marcenschede.invoice.core.tariffs.VatRepository;

public class InvoiceCalculationsDelegateFactory {

    public static InvoiceCalculationsDelegate newInstance(Invoice invoice, VatRepository vatRepository) {
        if (isNational(invoice) && !isVatShifted(invoice))
            return new InvoiceNationalCalculationsDelegate(invoice, vatRepository);

        if (isNational(invoice) && isVatShifted(invoice))
            return new InvoiceNationalVatShiftedCalculationsDelegate(invoice, vatRepository);

        if (isEuTransaction(invoice))
            return getInvoiceCalculatorForEuTransaction(invoice, vatRepository);

        return getInvoiceCalculatorForExportTransaction(invoice, vatRepository);
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

    private static InvoiceCalculationsDelegate getInvoiceCalculatorForEuTransaction(Invoice invoice, VatRepository vatRepository) {
        if (isBusinessCustomer(invoice) && invoice.getProductCategory().orElse(null) == ProductCategory.GOODS)
            return new InvoiceEuGoodsCalculationsDelegate(invoice, vatRepository);

        if (isBusinessCustomer(invoice) && invoice.getProductCategory().orElse(null) == ProductCategory.SERVICES)
            if (!isVatShifted(invoice))
                return new InvoiceEuServicesCalculationsDelegate(invoice, vatRepository);
            else
                return new InvoiceEuServicesVatShiftedCalculationsDelegate(invoice, vatRepository);

        if (isBusinessCustomer(invoice) && invoice.getProductCategory().orElse(null) == ProductCategory.E_SERVICES)
            if (!isVatShifted(invoice))
                return new InvoiceEuEServicesCalculationsDelegate(invoice, vatRepository);
            else
                return new InvoiceEuEServicesVatShiftedCalculationsDelegate(invoice, vatRepository);

        return new InvoiceEuB2CCalculationsDelegate(invoice, vatRepository);
    }

    private static InvoiceCalculationsDelegate getInvoiceCalculatorForExportTransaction(Invoice invoice, VatRepository vatRepository) {
        if (!isVatShifted(invoice))
            return new InvoiceExportCalculationsDelegate(invoice, vatRepository);
        else
            return new InvoiceExportVatShiftedCalculationsDelegate(invoice, vatRepository);
    }

    private static Boolean isBusinessCustomer(Invoice invoice) {
        return invoice.getCustomer().getEuTaxId().isPresent();
    }
}
