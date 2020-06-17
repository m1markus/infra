package ch.m1m.infra.billing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class QrBillCodeGeneratorTest {

    QrBillTestDataGenerator testData = new QrBillTestDataGenerator();

    @Test
    public void my_test() {
        // GIVEN
        String referenceData = getBillRefData3();

        // WHEN
        QrBillCodeGenerator qrCode = new QrBillCodeGenerator();
        String generatedData = qrCode.generateStringV2(generateBillData());

        // THEN
        assertEquals(referenceData, generatedData);
    }

    private QrBillData generateBillData() {
        return testData.generateBillData_case_1();
    }

    private String getBillRefData3() {
        //
        // https://www.paymentstandards.ch/dam/downloads/ig-qr-bill-de.pdf
        // page 43
        //
        String ref = ""
                + "SPC\n"
                + "0200\n"
                + "1\n"
                + "CH5800791123000889012\n"
                + "S\n"
                + "Robert Schneider AG\n"
                + "Rue du Lac\n"
                + "1268\n"
                + "2501\n"
                + "Biel\n"
                + "CH\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "CH\n"
                + "199.95\n"
                + "CHF\n"
                + "K\n"
                + "Pia-Maria Rutschmann-Schnyder\n"
                + "Grosse Marktgasse 28\n"
                + "9400 Rorschach\n"
                + "\n"
                + "\n"
                + "CH\n"
                + "SCOR\n"
                + "RF18539007547034\n"
                + "\n"
                + "EPD\n"
                + "\n"
                + "\n"
                + "";
        return ref;
    }
}
