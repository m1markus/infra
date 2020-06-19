package ch.m1m.infra.billing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QrBillQrCode  {

    private QrCodeGenerator externalQrCodeGenerator = new QrCodeGenerator();

    // code extracted from:
    // private void QrCodeGenerator#generateSwissQrCode(String payload)
    //
    public BufferedImage generateQrGraphic(String payload) throws IOException {

        // generate the qr code from the payload.
        BufferedImage qrCodeImage = externalQrCodeGenerator.generateQrCodeImage(payload);

        // overlay the qr code with a Swiss Cross
        return externalQrCodeGenerator.overlayWithSwissCross(qrCodeImage);
    }
}
