package ch.m1m.infra.billing;

import java.math.BigDecimal;

public class QrBillTestDataGenerator {

    public QrBillData generateBillData_case_1() {
        QrBillData billData = new QrBillData();

        billData.getReceiver().setIban("CH5800791123000889012");
        QrBillAddress receiverAddress = billData.getReceiver().getAddress();
        receiverAddress.setType("S");
        receiverAddress.setName("Robert Schneider AG");
        receiverAddress.setStreet("Rue du Lac");
        receiverAddress.setHouseNumber("1268");
        receiverAddress.setZipCode("2501");
        receiverAddress.setCityName("Biel");

        billData.getPaymentInformation().setAmount(new BigDecimal("1888777199.95"));
        billData.getPaymentInformation().setCurrency("CHF");

        billData.getPayer().setType("K");
        billData.getPayer().setName("Pia-Maria Rutschmann-Schnyder");
        billData.getPayer().setStreet("Grosse Marktgasse 28");
        billData.getPayer().setCityName("9400 Rorschach");

        billData.getReference().setType(QrBillPaymentReference.TYPE_SCOR);
        billData.getReference().setReference("RF18539007547034");

        return billData;
    }

    public QrBillData generateBillData_priv() {
        QrBillData billData = new QrBillData();

        billData.getReceiver().setIban("CH850023323376935840K");
        QrBillAddress receiverAddress = billData.getReceiver().getAddress();
        receiverAddress.setType("S");
        receiverAddress.setName("Sandra Mueller");
        receiverAddress.setStreet("Blumenstrasse");
        receiverAddress.setHouseNumber("15");
        receiverAddress.setZipCode("4102");
        receiverAddress.setCityName("Binningen");

        // finalReceiver for free

        billData.getPaymentInformation().setAmount(new BigDecimal("1.00"));
        billData.getPaymentInformation().setCurrency("CHF");

        billData.getPayer().setType("K");
        billData.getPayer().setName("Markus Mueller");
        billData.getPayer().setStreet("Blumenstrasse 15");
        billData.getPayer().setCityName("4102 Binningen");

        // billData.getReference().setType(QrBillPaymentReference.TYPE_SCOR);
        // billData.getReference().setReference("RF18539007547034");

        return billData;
    }
}
