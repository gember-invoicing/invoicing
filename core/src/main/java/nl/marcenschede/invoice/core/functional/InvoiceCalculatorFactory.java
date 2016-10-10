package nl.marcenschede.invoice.core.functional;

import nl.marcenschede.invoice.core.Company;
import nl.marcenschede.invoice.core.Invoice;
import nl.marcenschede.invoice.core.InvoiceImpl;
import nl.marcenschede.invoice.core.InvoiceTotals;
import nl.marcenschede.invoice.core.tariffs.VatRepository;

import java.util.function.Function;

public class InvoiceCalculatorFactory {

    public static Function<VatRepository, Function<Company, Function<InvoiceData, InvoiceTotals>>> getInvoiceCalculatorFactory() {
        return vatRepository -> getInvoiceCalculatorFactory(vatRepository);
    }

    public static Function<Company, Function<InvoiceData, InvoiceTotals>> getInvoiceCalculatorFactory(VatRepository vatRepository) {
        return company -> getInvoiceCalculatorFactory(company, vatRepository);
    }

    public static Function<InvoiceData, InvoiceTotals> getInvoiceCalculatorFactory(Company company, VatRepository vatRepository) {
        return invoiceData -> getInvoiceTotals(invoiceData, company, vatRepository);
    }

    public static InvoiceTotals getInvoiceTotals(InvoiceData invoiceData, Company company, VatRepository vatRepository) {
        Invoice invoice = new InvoiceImpl(vatRepository);

        invoice.setCountryOfOrigin(invoiceData.getCountryOfOrigin());
        invoice.setCountryOfDestination(invoiceData.getCountryOfDestination());
        invoice.setCustomer(invoiceData.getCustomer());
        invoice.setInvoiceType(invoiceData.getInvoiceType());
        invoice.setProductCategory(invoiceData.getProductCategory());
        invoice.setVatShifted(invoiceData.getVatShifted());
        invoice.setInvoiceLines(invoiceData.getInvoiceLines());

        invoice.setCompany(company);

        return invoice.getInvoiceTotals();
    }
}
