package nl.marcenschede.invoice.eventProcessors;

import nl.marcenschede.invoice.core.functional.InvoiceCreationEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.function.Function;

class InvoicePdfCreatorHelper {

    static Function<PDDocument, PDDocument> createDocumentCreator(InvoiceCreationEvent invoiceCreationEvent) {
        return document -> {
            try {
                PDPage page = new PDPage();
                document.addPage( page );

                PDFont font = PDType1Font.HELVETICA_BOLD;

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont( font, 12 );
                contentStream.moveTextPositionByAmount( 100, 700 );
                contentStream.drawString( String.format("Factuur voor debiteur: %s", invoiceCreationEvent.getCustomer().getCustomerDebtorId()) );
                contentStream.moveTextPositionByAmount( 0, -15 );

                invoiceCreationEvent.getInvoiceTotals().lineSummaries
                        .forEach(lineSummary -> {
                            try {
                                contentStream.moveTextPositionByAmount( 0, -15 );
                                contentStream.drawString( String.format("Invoice line, ex VAT %s, VAT %s, in VAT %s", lineSummary.getAmountExclVat().toString(), lineSummary.getAmountVat().toString(), lineSummary.getAmountInclVat().toString()) );
                            } catch (IOException e) {
                                throw new InvoicePdfCreatorHelper.InvoicePdfCreatorException(e);
                            }
                        });

                contentStream.moveTextPositionByAmount( 0, -30 );
                contentStream.drawString( String.format("Totaal ex BTW is %s", invoiceCreationEvent.getInvoiceTotals().totalInvoiceAmountExclVat.toString()) );

                contentStream.moveTextPositionByAmount( 0, -15 );
                contentStream.drawString( String.format("Totaal BTW is %s", invoiceCreationEvent.getInvoiceTotals().invoiceTotalVat.toString()) );

                contentStream.moveTextPositionByAmount( 0, -15 );
                contentStream.drawString( String.format("Totaal in BTW is %s", invoiceCreationEvent.getInvoiceTotals().totalInvoiceAmountInclVat.toString()) );

                contentStream.endText();

                contentStream.close();

                return document;
            } catch (IOException e) {
                throw new InvoicePdfCreatorHelper.InvoicePdfCreatorException(e);
            }
        };
    }

    static class InvoicePdfCreatorException extends RuntimeException {
        InvoicePdfCreatorException(Exception e) {
            super(e);
        }
    }


}
