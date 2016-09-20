package nl.marcenschede.invoice.calculators;

import nl.marcenschede.invoice.Invoice;

public class InvoiceCalculatorHelperFactory {

    public static InvoiceCalculationsHelper newInstance(Invoice invoice) {
        if(isNational(invoice))
            return new InvoiceNationalCalculationsHelper(invoice);

        return invoice.getCustomer().getEuTaxId().isPresent() ?
                new InvoiceEuGoodsDelegate(invoice): new InvoiceEuB2CCalculationsHelper(invoice);
    }

    private static boolean isNational(Invoice invoice) {
        return CountryOfOriginHelper.getOriginCountry(invoice).equals(CountryOfOriginHelper.getDestinationCountry(invoice));
    }
}
