package ch.m1m.infra.billing;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class QrBillDeviceUnit {

    private static final float DEVICE_UNITS_PER_MILLIMETER = PDRectangle.A4.getUpperRightX() / 210f;

    public static final float FORM_WIDTH = millimetersToDeviceUnit(210);
    public static final float FORM_HEIGHT = millimetersToDeviceUnit(105);

    public static final float MARGIN_NON_PRINTABLE = millimetersToDeviceUnit(5);
    
    public static final float RECEIPT_WIDTH = millimetersToDeviceUnit(62);

    public static final float PAYMENT_COL_2_START = RECEIPT_WIDTH + MARGIN_NON_PRINTABLE + millimetersToDeviceUnit(51);

    public static final float TITLE_HEIGHT = millimetersToDeviceUnit(7);

    public static final float QR_CODE_IMAGE_SIZE = millimetersToDeviceUnit(46);

    public static float millimetersToDeviceUnit(float millimeters) {
        return millimeters * DEVICE_UNITS_PER_MILLIMETER;
    }
}
