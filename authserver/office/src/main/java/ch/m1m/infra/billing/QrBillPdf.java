package ch.m1m.infra.billing;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

// https://www.paymentstandards.ch/dam/downloads/style-guide-de.pdf
//
// https://www.paymentstandards.ch/dam/downloads/ig-qr-bill-de.pdf
//

public class QrBillPdf {

    private static final PDFont FONT_NORMAL = PDType1Font.HELVETICA;
    private static final PDFont FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    private static final int FONT_SIZE_TITLE = 11;

    private static final QrBillLanguageDE langDE = new QrBillLanguageDE();

    private QrBillCodeGenerator qrDataStringGenerator = new QrBillCodeGenerator();
    private QrBillQrCode qrCodeGenerator = new QrBillQrCode();
    private QrBillFormatter format = new QrBillFormatter();

    // this is to fine tune the starting points depending on the
    //
    private float offsetX = 0f;
    private float offsetY = 0f;

    private boolean printHelperLines = false;

    public static void main(String... args) throws IOException {

        String fileName = "/tmp/qr_bill.pdf";
        QrBillTestDataGenerator testData = new QrBillTestDataGenerator();

        QrBillPdf bill = new QrBillPdf();

        // offset test
        //
        //bill.setOffsetX(100f); bill.setOffsetY(100f);

        String language = "DE";

        //QrBillData data = testData.generateBillData_case_1();
        //QrBillData data = testData.generateBillData_priv();
        QrBillData data = testData.generateBillData_mieterReinach();

        PDDocument document = bill.generateDocument(data, language);

        document.save(fileName);
        document.close();

        System.out.println("created pdf:     " + fileName);

        // open the platform default viewer
        String osCommand = String.format("open %s", fileName);
        Runtime.getRuntime().exec(osCommand);
    }

    public PDDocument generateDocument(QrBillData data, String language) throws IOException {
        PDDocument document = new PDDocument();
        PDPage qrPage = generatePage(data, language, document);
        document.addPage(qrPage);
        return document;
    }

    public PDPage generatePage(QrBillData data, String language, PDDocument document) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        QrBillLanguage langText = getLanguage(language);

        float lowerLeftX = 0f + getOffsetX();
        float lowerLeftY = 0f + getOffsetY();

        final float frameBillWidth = QrBillDeviceUnit.FORM_WIDTH;
        final float frameBillHeight = QrBillDeviceUnit.FORM_HEIGHT;

        PDPageContentStream stream = new PDPageContentStream(document, page);

        drawCutLines(frameBillWidth, frameBillHeight, stream, lowerLeftX, lowerLeftY, langText);
        drawReceipt(frameBillHeight, data, stream, lowerLeftX, lowerLeftY, langText);
        drawPayment(frameBillHeight, data, stream, lowerLeftX, lowerLeftY, langText, document);

        stream.close();

        return page;
    }

    private void drawReceipt(float frameBillHeight, QrBillData data, PDPageContentStream stream,
                             float lowerLeftX, float lowerLeftY, QrBillLanguage langText) throws IOException {

        final float lineSpacing = 9;
        float leftX = lowerLeftX + QrBillDeviceUnit.MARGIN_NON_PRINTABLE;
        float startY = lowerLeftY + frameBillHeight - QrBillDeviceUnit.MARGIN_NON_PRINTABLE;

        // title: Empangsschein
        //
        String title = langText.getEmpfangsschein();
        float currentHeight = 11;
        float workY = startY - currentHeight;
        drawText2(title, leftX, workY, FONT_BOLD, FONT_SIZE_TITLE, stream);
        // blank line
        workY -= lineSpacing;

        // title: Konto / Zahlbar an
        //
        title = langText.getKontoZahlbarAn();
        currentHeight = 6;
        workY -= currentHeight;
        drawText2(title, leftX, workY, FONT_BOLD, 6, stream);

        // data IBAN
        //
        String dataIBAN = data.getReceiver().getIban();
        dataIBAN = format.iban(dataIBAN);
        workY -= lineSpacing;
        drawText2(dataIBAN, leftX, workY, FONT_NORMAL, 8, stream);
        //
        // data Name
        //
        List<String> dataAddressList = format.addressToList(data.getReceiver().getAddress());
        String dataLine1 = dataAddressList.get(0);
        workY -= lineSpacing;
        drawText2(dataLine1, leftX, workY, FONT_NORMAL, 8, stream);
        //
        // data Strasse
        //
        String dataLine2 = dataAddressList.get(1);
        workY -= lineSpacing;
        drawText2(dataLine2, leftX, workY, FONT_NORMAL, 8, stream);
        //
        // data Ort
        //
        String dataLine3 = dataAddressList.get(2);
        workY -= lineSpacing;
        drawText2(dataLine3, leftX, workY, FONT_NORMAL, 8, stream);
        // blank line
        workY -= lineSpacing;

        // title: Referenz (optional)
        //
        QrBillPaymentReference ref = data.getReference();
        if (!QrBillPaymentReference.TYPE_NONE.equals(ref.getType())) {
            title = langText.getReferenz();
            workY -= 6;
            drawText2(title, leftX, workY, FONT_BOLD, 6, stream);
            workY -= lineSpacing;
            String dataRef = ref.getReference();
            drawText2(dataRef, leftX, workY, FONT_NORMAL, 8, stream);
            // blank line
            workY -= lineSpacing;
        }

        QrBillAddress payer = data.getPayer();
        if (payer != null) {
            dataAddressList = format.addressToList(payer);
            //
            // title: Zahlbar durch
            //
            title = langText.getZahlbarDurch();
            workY -= 6;
            drawText2(title, leftX, workY, FONT_BOLD, 6, stream);
            //
            // adresse line 1
            //
            dataLine1 = dataAddressList.get(0);
            workY -= lineSpacing;
            drawText2(dataLine1, leftX, workY, FONT_NORMAL, 8, stream);
            //
            // adresse line 2
            //
            dataLine2 = dataAddressList.get(1);
            workY -= lineSpacing;
            drawText2(dataLine2, leftX, workY, FONT_NORMAL, 8, stream);
            //
            // adresse line 3
            //
            dataLine3 = dataAddressList.get(2);
            workY -= lineSpacing;
            drawText2(dataLine3, leftX, workY, FONT_NORMAL, 8, stream);
        }

        // area Betrag
        //
        float leftAmountX = leftX + QrBillDeviceUnit.millimetersToDeviceUnit(15);
        float amountY = lowerLeftY + QrBillDeviceUnit.MARGIN_NON_PRINTABLE + QrBillDeviceUnit.millimetersToDeviceUnit(18 + 14);
        workY = amountY -= 6;
        title = langText.getWaehrung();
        drawText2(title, leftX, workY, FONT_BOLD, 6, stream);
        title = langText.getBetrag();
        drawText2(title, leftAmountX, workY, FONT_BOLD, 6, stream);
        workY -= 11;
        String dataCurrencyCode = data.getPaymentInformation().getCurrency();
        drawText2(dataCurrencyCode, leftX, workY, FONT_NORMAL, 8, stream);
        BigDecimal dataAmount = data.getPaymentInformation().getAmount();
        String dataAmountFormatted = format.amount(dataAmount);
        drawText2(dataAmountFormatted, leftAmountX, workY, FONT_NORMAL, 8, stream);

        // area Annahmestelle
        //
        float annahmestelleY = lowerLeftY + QrBillDeviceUnit.MARGIN_NON_PRINTABLE + QrBillDeviceUnit.millimetersToDeviceUnit(18);
        float annahmestelleX = leftX + QrBillDeviceUnit.millimetersToDeviceUnit(35);
        title = langText.getAnnahmestelle();
        drawText2(title, annahmestelleX, annahmestelleY, FONT_BOLD, 6, stream);
    }

    private PDImageXObject convertBufferedImageToPDImageXObject(BufferedImage image,
                                                                PDDocument document) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            PDImageXObject imageXObj = PDImageXObject.createFromByteArray(document, imageInByte, "qr_code");
            return imageXObj;
        }
    }

    private void drawPayment(float frameBillHeight, QrBillData data, PDPageContentStream stream,
                             float lowerLeftX, float lowerLeftY, QrBillLanguage langText,
                             PDDocument document) throws IOException {

        final float lineSpacing = 11;
        float leftX = lowerLeftX + QrBillDeviceUnit.RECEIPT_WIDTH + QrBillDeviceUnit.MARGIN_NON_PRINTABLE;
        float startY = lowerLeftY + frameBillHeight - QrBillDeviceUnit.MARGIN_NON_PRINTABLE;
        float leftCol1X = leftX;
        float leftCol2X = leftX + QrBillDeviceUnit.millimetersToDeviceUnit(51);

        // title: Zahlteil
        //
        String title = langText.getZahlteil();
        float currentHeight = lineSpacing;
        float workY = startY - currentHeight;
        drawText2(title, leftCol1X, workY, FONT_BOLD, FONT_SIZE_TITLE, stream);
        //
        // Konto / Zahlbar an
        //
        title = langText.getKontoZahlbarAn();
        workY += 3;
        drawText2(title, leftCol2X, workY, FONT_BOLD, 8, stream);
        //
        // IBAN
        //
        String dataIBAN = data.getReceiver().getIban();
        dataIBAN = format.iban(dataIBAN);
        workY -= lineSpacing;
        drawText2(dataIBAN, leftCol2X, workY, FONT_NORMAL, 10, stream);
        //
        // Receiver Address 1
        //
        List<String> dataAddressList = format.addressToList(data.getReceiver().getAddress());
        String dataLine1 = dataAddressList.get(0);
        workY -= lineSpacing;
        drawText2(dataLine1, leftCol2X, workY, FONT_NORMAL, 10, stream);
        //
        // Receiver Address 2
        //
        String dataLine2 = dataAddressList.get(1);
        workY -= lineSpacing;
        drawText2(dataLine2, leftCol2X, workY, FONT_NORMAL, 10, stream);
        //
        // Receiver Address 3
        //
        String dataLine3 = dataAddressList.get(2);
        workY -= lineSpacing;
        drawText2(dataLine3, leftCol2X, workY, FONT_NORMAL, 10, stream);
        // blank line
        workY -= lineSpacing;

        // title: Referenz (optional)
        //
        QrBillPaymentReference ref = data.getReference();
        if (!QrBillPaymentReference.TYPE_NONE.equals(ref.getType())) {
            title = langText.getReferenz();
            workY -= lineSpacing;
            drawText2(title, leftCol2X, workY, FONT_BOLD, 8, stream);
            workY -= lineSpacing;
            String dataRef = ref.getReference();
            drawText2(dataRef, leftCol2X, workY, FONT_NORMAL, 10, stream);
            // blank line
            workY -= lineSpacing;
        }

        // title: Zusätzliche Informationen (optional)
        //
        // ...

        // title: Zahlbar durch
        //
        QrBillAddress payer = data.getPayer();
        if (payer != null) {
            dataAddressList = format.addressToList(payer);
            //
            // title: Zahlbar durch
            //
            title = langText.getZahlbarDurch();
            workY -= lineSpacing;
            drawText2(title, leftCol2X, workY, FONT_BOLD, 8, stream);
            //
            // adresse line 1
            //
            dataLine1 = dataAddressList.get(0);
            workY -= lineSpacing;
            drawText2(dataLine1, leftCol2X, workY, FONT_NORMAL, 10, stream);
            //
            // adresse line 2
            //
            dataLine2 = dataAddressList.get(1);
            workY -= lineSpacing;
            drawText2(dataLine2, leftCol2X, workY, FONT_NORMAL, 10, stream);
            //
            // adresse line 3
            //
            dataLine3 = dataAddressList.get(2);
            workY -= lineSpacing;
            drawText2(dataLine3, leftCol2X, workY, FONT_NORMAL, 10, stream);
            // blank line
            workY -= lineSpacing;
        }

        //
        // QR Code
        //
        String qrCodeDataString = qrDataStringGenerator.generateStringV2(data);
        BufferedImage qrCodeImage = qrCodeGenerator.generateQrGraphic(qrCodeDataString);
        PDImageXObject imageXObj = convertBufferedImageToPDImageXObject(qrCodeImage, document);
        // draw QR code
        float qrImageWidth = QrBillDeviceUnit.QR_CODE_IMAGE_SIZE;
        float qrImageHeight = qrImageWidth;
        float qrY = QrBillDeviceUnit.millimetersToDeviceUnit(5 + 18 + 14 + 5);
        stream.drawImage(imageXObj, leftX, qrY, qrImageWidth, qrImageHeight);

        // area: Betrag
        //
        //float qrWidth = QrBillDeviceUnit.millimetersToDeviceUnit(46);
        //drawHorizontalLine(leftX, workY, qrWidth, stream);

        float leftAmountX = leftX + QrBillDeviceUnit.millimetersToDeviceUnit(15);
        float amountY = lowerLeftY + QrBillDeviceUnit.MARGIN_NON_PRINTABLE + QrBillDeviceUnit.millimetersToDeviceUnit(18 + 14);
        workY = amountY -= 8;
        title = langText.getWaehrung();
        drawText2(title, leftX, workY, FONT_BOLD, 8, stream);
        title = langText.getBetrag();
        drawText2(title, leftAmountX, workY, FONT_BOLD, 8, stream);
        workY -= lineSpacing;
        String dataCurrencyCode = data.getPaymentInformation().getCurrency();
        drawText2(dataCurrencyCode, leftX, workY, FONT_NORMAL, 10, stream);
        BigDecimal dataAmount = data.getPaymentInformation().getAmount();
        String dataAmountFormatted = format.amount(dataAmount);
        drawText2(dataAmountFormatted, leftAmountX, workY, FONT_NORMAL, 10, stream);

        // area: Weitere Informationen
        //
    }

    private void drawCutLines(float frameBillWidth, float frameBillHeight, PDPageContentStream stream,
                              float lowerLeftX, float lowerLeftY, QrBillLanguage langText) throws IOException {

        stream.saveGraphicsState();

        stream.setLineWidth(0.5f);
        stream.setStrokingColor(Color.BLACK);
        stream.setLineDashPattern(new float[]{1, 3}, 0);

        // horizontal upper line
        //
        float x = lowerLeftX;
        float top = frameBillHeight + lowerLeftY;
        stream.moveTo(x, top);
        x = frameBillWidth;
        stream.lineTo(x, top);
        stream.stroke();

        // vertical "Empfangsschein"
        //
        float receiptX = lowerLeftX + QrBillDeviceUnit.RECEIPT_WIDTH;
        drawVerticalLine(receiptX, lowerLeftY, QrBillDeviceUnit.FORM_HEIGHT, stream);

        stream.restoreGraphicsState();

        // draw some helper lines
        //
        if (printHelperLines) {
            stream.setStrokingColor(Color.BLUE);
            stream.setLineWidth(0.5f);

            x = lowerLeftX + QrBillDeviceUnit.MARGIN_NON_PRINTABLE;
            drawVerticalLine(x, lowerLeftY, QrBillDeviceUnit.FORM_HEIGHT, stream);

            x = lowerLeftX + QrBillDeviceUnit.RECEIPT_WIDTH + QrBillDeviceUnit.MARGIN_NON_PRINTABLE;
            drawVerticalLine(x, lowerLeftY, QrBillDeviceUnit.FORM_HEIGHT, stream);

            x = QrBillDeviceUnit.PAYMENT_COL_2_START;
            drawVerticalLine(x, lowerLeftY, QrBillDeviceUnit.FORM_HEIGHT, stream);

            stream.setStrokingColor(Color.BLACK);
        }

        // print notes to cut off before payment
        //
        String cutText = langText.getVorDerZahlungAbtrennen();
        float textX = receiptX + QrBillDeviceUnit.MARGIN_NON_PRINTABLE;
        float textY = top + 10;
        stream.beginText();
        drawText(cutText, textX, textY, FONT_NORMAL, 10, stream);
        stream.endText();

        stream.stroke();
    }

    private void drawHorizontalLine(float x, float y, float width, PDPageContentStream stream) throws IOException {
        stream.moveTo(x, y);
        float rightX = x + width;
        stream.lineTo(rightX, y);
        stream.stroke();
    }

    private void drawVerticalLine(float x, float y, float height, PDPageContentStream stream) throws IOException {
        stream.moveTo(x, y);
        float topY = y + height;
        stream.lineTo(x, topY);
        stream.stroke();
    }

    private void drawText(String text, float x, float y,
                          PDFont font, float size,
                          PDPageContentStream stream) throws IOException {
        stream.setFont(font, size);
        stream.newLineAtOffset(x, y);
        stream.showText(text);
    }

    private void drawText2(String text, float x, float y,
                           PDFont font, float size,
                           PDPageContentStream stream) throws IOException {
        stream.beginText();
        stream.setFont(font, size);
        stream.newLineAtOffset(x, y);
        stream.showText(text);
        stream.endText();
    }


    private QrBillLanguage getLanguage(String language) {
        switch (language) {
            case "EN":
            case "FR":
            case "IT":
            case "DE":
                return langDE;
            default:
                return langDE;
        }
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
}
