package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

class InvoiceFilePdfCreator implements Consumer<InvoiceCreationEvent> {

    private static Logger logger = Logger.getLogger(InvoiceFilePdfCreator.class.getName());
    private final String filename;

    static Consumer<InvoiceCreationEvent> getInvoicePdfCreator(String filename) {
        return new InvoiceFilePdfCreator(filename);
    }

    private InvoiceFilePdfCreator(String filename) {
        this.filename = filename;
    }

    @Override
    public void accept(InvoiceCreationEvent invoiceCreationEvent) {
        final String invoiceFilename = String.format(filename, invoiceCreationEvent.getInvoiceNumber());

        final Consumer<PDDocument> documentSaver = createDocumentSaver(invoiceFilename);
        final Function<PDDocument, PDDocument> documentCreator =
                InvoicePdfCreatorHelper.createDocumentCreator(invoiceCreationEvent);

        try {
            createPdfDocument(documentCreator, documentSaver);
        } catch (IOException e) {
            throw new InvoicePdfCreatorHelper.InvoicePdfCreatorException(e);
        }

        log(invoiceFilename);
    }

    private Consumer<PDDocument> createDocumentSaver(String filename) {
        return pdDocument -> {
            try {
                pdDocument.save(filename);
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
