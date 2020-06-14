package ch.m1m.infra.billing;

// https://www.paymentstandards.ch/dam/downloads/ig-qr-bill-de.pdf
// https://www.paymentstandards.ch/dam/downloads/qrcodegenerator.java
// https://www.paymentstandards.ch/de/home/software-partner/faq.html

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QrBillCodeGenerator {

    public String generateStringV2(QrBillData data) {
        if (data == null) {
            throw new IllegalArgumentException("input data is null)");
        }

        List<String> list = new ArrayList<>();
        addFixedHeaderV2(list);
        addReceiver(data.getReceiver(), list);
        addFinalReceiver(data.getFinalReceiver(), list);
        addPaymentInformation(data.getPaymentInformation(), list);
        addPayer(data.getPayer(), list);
        addReference(data.getReference(), list);
        addAdditionalInformation(data.getAdditionalInformation(), list);
        addPaymentAV(data.getAV(), list);

        // convert to one string with newline delimiter
        Stream<String> outStream = list.stream();
        String result = outStream.collect(Collectors.joining("\n"));
        return result;
    }

    private void addFixedHeaderV2(List<String> list) {
        // Swiss Payment Code -> SPC
        list.add("SPC");
        // Version
        list.add("0200");
        // UTF-8
        list.add("1");
    }

    private void addReceiver(QrBillPaymentReceiver receiver, List<String> list) {
        String iban = "";
        if (receiver.getIban() != null) {
            iban = receiver.getIban();
        }
        list.add(iban);

        QrBillAddress address = receiver.getAddress();
        addAddress(address, list);
    }

    private void addFinalReceiver(QrBillAddress address, List<String> list) {
        addAddress(address, list);
    }

    private void addPaymentInformation(QrBillPaymentInformation paymentInformation, List<String> list) {
        list.add(paymentInformation.getAmount().toString());
        list.add(paymentInformation.getCurrency());
    }

    private void addPayer(QrBillAddress address, List<String> list) {
        addAddress(address, list);
    }

    private void addReference(QrBillPaymentReference reference, List<String> list) {
        list.add(reference.getType());
        list.add(reference.getReference());
    }

    private void addAdditionalInformation(QrBillAdditionalInformation additionalInformation, List<String> list) {
        list.add(additionalInformation.getNote());
        // «EPD» (End Payment Data)
        list.add("EPD");
        list.add(additionalInformation.getPaymentInformation());
    }

    private void addPaymentAV(QrBillPaymentAV av, List<String> list) {
        list.add(av.getAV1());
        list.add(av.getAV2());
    }

    private void addAddress(QrBillAddress address, List<String> list) {
        String addressType = address.getType();
        switch(addressType) {
            case "":  // same as addAddressAsTypeS()
            case "S": addAddressAsTypeS(address, list); break;
            case "K": addAddressAsTypeK(address, list); break;
            default: throw new IllegalArgumentException("unknown address type");
        }
    }

    private void addAddressAsTypeS(QrBillAddress address, List<String> list) {
        // set the Type
        list.add(address.getType());
        list.add(address.getName());
        list.add(address.getStreet());
        list.add(address.getHouseNumber());
        list.add(address.getZipCode());
        list.add(address.getCityName());
        list.add(address.getCountry());
    }

    private void addAddressAsTypeK(QrBillAddress address, List<String> list) {
        list.add(address.getType());
        list.add(address.getName());
        list.add(address.getStreet());
        list.add(address.getCityName());
        list.add("");       // filler 1
        list.add("");       // filler 2
        list.add(address.getCountry());
    }
}
