package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Invoice;
import nl.marcenschede.invoice.ProductCategory;

public class InvoiceCalculationsDelegateFactory {

    public static InvoiceCalculationsDelegate newInstance(Invoice invoice) {
        if(isNational(invoice))
            return new InvoiceNationalCalculationsDelegate(invoice);

        return getInvoiceCalculatorForEuTransaction(invoice);
    }

    private static boolean isNational(Invoice invoice) {
        String originCountry = CountryOfOriginHelper.getOriginCountry(invoice);
        String destinationCountry = CountryOfDestinationHelper.getDestinationCountry(invoice);

        return originCountry.equals(destinationCountry);
    }

    private static InvoiceCalculationsDelegate getInvoiceCalculatorForEuTransaction(Invoice invoice) {
        if(isBusinessCustomer(invoice) && invoice.getProductCategory().get() == ProductCategory.GOODS)
            return new InvoiceEuGoodsCalculationsDelegate(invoice);

        if(isBusinessCustomer(invoice) && invoice.getProductCategory().get() == ProductCategory.SERVICES)
            return new InvoiceEuServicesCalculationsDelegate(invoice);

        if(isBusinessCustomer(invoice) && invoice.getProductCategory().get() == ProductCategory.E_SERVICES)
            return new InvoiceEuEServicesCalculationsDelegate(invoice);

        return new InvoiceEuB2CCalculationsDelegate(invoice);
    }

    private static Boolean isBusinessCustomer(Invoice invoice) {
        return invoice.getCustomer().getEuTaxId().isPresent();
    }
}
