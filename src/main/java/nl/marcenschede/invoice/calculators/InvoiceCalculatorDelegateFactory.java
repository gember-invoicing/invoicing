package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Invoice;
import nl.marcenschede.invoice.ProductCategory;

public class InvoiceCalculatorDelegateFactory {

    public static InvoiceCalculationsDelegate newInstance(Invoice invoice) {
        if(isNational(invoice))
            return new InvoiceNationalCalculationsDelegate(invoice);

        if(invoice.getCustomer().getEuTaxId().isPresent() && invoice.getProductCategory().get() == ProductCategory.GOODS)
            return new InvoiceEuGoodsDelegate(invoice);

        if(invoice.getCustomer().getEuTaxId().isPresent() && invoice.getProductCategory().get() == ProductCategory.SERVICES)
            return new InvoiceEuServicesCalculationsDelegate(invoice);

        return new InvoiceEuB2CCalculationsDelegate(invoice);
    }

    private static boolean isNational(Invoice invoice) {
        return CountryOfOriginHelper.getOriginCountry(invoice).equals(CountryOfOriginHelper.getDestinationCountry(invoice));
    }
}
