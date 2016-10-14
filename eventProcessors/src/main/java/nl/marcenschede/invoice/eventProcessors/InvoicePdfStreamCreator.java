package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

class InvoicePdfStreamCreator implements Consumer<InvoiceCreationEvent> {

    private static Logger logger = Logger.getLogger(DebtorBookEventCreator.class.getName());
    private final OutputStream stream;

    static Consumer<InvoiceCreationEvent> create(OutputStream stream) {
        return new InvoicePdfStreamCreator(stream);
    }

    private InvoicePdfStreamCreator(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    public void accept(InvoiceCreationEvent invoiceCreationEvent) {
        final Consumer<PDDocument> documentSaver = createDocumentSaver(stream);
        final Function<PDDocument, PDDocument> documentCreator =
                InvoicePdfCreatorHelper.createDocumentCreator(invoiceCreationEvent);

        try {
            createPdfDocument(documentCreator, documentSaver);
        } catch (IOException e) {
            throw new InvoicePdfCreatorHelper.InvoicePdfCreatorException(e);
        }

        log("Created invoice " + invoiceCreationEvent.getInvoiceNumber());
    }

    private static Consumer<PDDocument> createDocumentSaver(OutputStream stream) {
        return pdDocument -> {
            try {
                pdDocument.save(stream);
            } catch (IOException | COSVisitorException e) {
                throw new InvoicePdfCreatorHelper.InvoicePdfCreatorException(e);
            }
        };
    }

    private void createPdfDocument(Function<PDDocument, PDDocument> creator, Consumer<PDDocument> saver) throws IOException {
        PDDocument document = new PDDocument();
        saver.accept(creator.apply(document));
        document.close();
    }

    private void log(String invoiceFilename) {
        logger.log(Level.WARNING, String.format("Created invoice: %s", invoiceFilename));
    }

}
