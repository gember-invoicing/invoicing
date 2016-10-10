package nl.marcenschede.invoice.eventProcessors;

import org.junit.Test;

public class LedgerEventCreatorTest {

    @Test
    public void accept() throws Exception {
        LedgerEventCreator ledgerEventCreator = new LedgerEventCreator();

        ledgerEventCreator.accept(new SampleEventCreator().invoke());
    }

}