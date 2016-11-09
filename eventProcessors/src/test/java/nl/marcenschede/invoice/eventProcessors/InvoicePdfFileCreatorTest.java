package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.function.Consumer;

public class InvoicePdfFileCreatorTest {

    @Test
    public void accept() throws Exception {
        String templatePathname = "target/invoice-%d.pdf";
        String expectedPathname = "target/invoice-12345.pdf";

        final Consumer<InvoiceCreationEvent> pdfCreator =
                InvoiceFilePdfCreator.getInvoicePdfCreator(templatePathname);

        pdfCreator.accept(new SampleEventCreator().invoke());

        File f = new File(expectedPathname);
        Assert.assertTrue(f.exists() && !f.isDirectory());
        f.delete();
    }

}