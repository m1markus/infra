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

public class QrBillPdf {

    private static final PDFont FONT_NORMAL = PDType1Font.HELVETICA;
    private static final PDFont FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    private static final int FONT_SIZE_TITLE = 11;

    private static final QrBillLanguageDE langDE = new QrBillLanguageDE();

    private QrBillCodeGenerator qrDataStringGenerator = new QrBillCodeGenerator();
    private QrBillQrCode qrCodeGenerator = new QrBillQrCode();

    // this is to fine tune the starting points depending on the
    //
    private float offsetX = 0f;
    private float offsetY = 0f;

    private boolean printHelperLines = true;

    public static void main(String... args) throws IOException {

        String fileName = "/tmp/qr_bill.pdf";
        QrBillTestDataGenerator testData = new QrBillTestDataGenerator();

        QrBillPdf bill = new QrBillPdf();

        // offset test
        //
        //bill.setOffsetX(100f); bill.setOffsetY(100f);

        String language = "DE";
        QrBillData data = testData.generateBillData_case_1();
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

        float leftX = lowerLeftX + QrBillDeviceUnit.MARGIN_NON_PRINTABLE;
        float startY = lowerLeftY + frameBillHeight - QrBillDeviceUnit.MARGIN_NON_PRINTABLE;

        stream.beginText();

        String title = langText.getEmpfangsschein();
        float currentY = startY - QrBillDeviceUnit.millimetersToDeviceUnit(6);
        drawText(title, leftX, currentY, FONT_BOLD, FONT_SIZE_TITLE, stream);

        stream.endText();
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

        float leftX = lowerLeftX + QrBillDeviceUnit.RECEIPT_WIDTH + QrBillDeviceUnit.MARGIN_NON_PRINTABLE;
        float startY = lowerLeftY + frameBillHeight - QrBillDeviceUnit.MARGIN_NON_PRINTABLE;

        String qrCodeDataString = qrDataStringGenerator.generateStringV2(data);
        BufferedImage qrCodeImage = qrCodeGenerator.generateQrGraphic(qrCodeDataString);
        PDImageXObject imageXObj = convertBufferedImageToPDImageXObject(qrCodeImage, document);

        float qrImageWidth = QrBillDeviceUnit.QR_CODE_IMAGE_SIZE;
        float qrImageHeight = qrImageWidth;
        stream.drawImage(imageXObj, leftX + 7, 100, qrImageWidth, qrImageHeight);

        stream.beginText();

        String title = langText.getZahlteil();
        float currentY = startY - QrBillDeviceUnit.millimetersToDeviceUnit(6);
        drawText(title, leftX, currentY, FONT_BOLD, FONT_SIZE_TITLE, stream);

        stream.endText();
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
