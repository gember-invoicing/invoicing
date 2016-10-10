package nl.marcenschede.invoice.eventProcessors;

import org.junit.Test;

public class InvoicePdfCreatorTest {

    @Test
    public void accept() throws Exception {
        InvoicePdfCreator invoicePdfCreator = new InvoicePdfCreator();

        invoicePdfCreator.accept(new SampleEventCreator().invoke());

//        Assert.assertThat(file exists);
    }

}