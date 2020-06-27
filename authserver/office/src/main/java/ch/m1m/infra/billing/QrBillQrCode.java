package ch.m1m.infra.billing;

import java.awt.image.BufferedImage;
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

/*

SVG

https://dzone.com/articles/how-to-create-a-qr-code-svg-using-zxing-and-jfrees

http://www.jfree.org/jfreesvg/

<dependency>
    <groupId>org.jfree</groupId>
    <artifactId>jfreesvg</artifactId>
    <version>3.4</version>
</dependency>


public static String getQRCodeSvg(String targetUrl, int width,
    int height, boolean withViewBox){
    SVGGraphics2D g2 = new SVGGraphics2D(width, height);
    BufferedImage qrCodeImage = getQRCode(targetUrl, width, height);
    g2.drawImage(qrCodeImage, 0,0, width, height, null);
    ViewBox viewBox = null;
    if ( withViewBox ){
        viewBox = new ViewBox(0,0,width,height);
    }
    return g2.getSVGElement(null, true, viewBox, null, null);
}

*/
