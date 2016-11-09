package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class InvoicePdfStreamCreatorTest {

    @Test
    public void accept() throws Exception {

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final Consumer<InvoiceCreationEvent> pdfCreator =
                InvoicePdfStreamCreator.create(os);

        pdfCreator.accept(new SampleEventCreator().invoke());

        final byte[] actualOutputArray = os.toByteArray();
        assertTrue(actualOutputArray.length > 3);
        assertThat(new String(actualOutputArray).substring(0, 4), CoreMatchers.is("%PDF"));
    }

}