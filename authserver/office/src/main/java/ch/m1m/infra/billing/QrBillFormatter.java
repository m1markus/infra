package ch.m1m.infra.billing;

import nl.garvelink.iban.IBAN;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QrBillFormatter {

    String iban(String inIban) {
        IBAN iban = IBAN.valueOf(inIban);
        return iban.toString();
    }

    // FIXME: amount 1.- not formatted correctly -> 1.00
    String amount(BigDecimal inAmount) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        String formattedAmount = formatter.format(inAmount);
        return formattedAmount;
    }

    List<String> addressToList(QrBillAddress address) {
        List<String> addressList = new ArrayList<>();
        switch (address.getType()) {
            case "S": {
                String line1 = address.getName();
                addressList.add(line1);
                String line2 = address.getStreet() + " " + address.getHouseNumber();
                addressList.add(line2);
                String line3 = address.getZipCode() + " " + address.getCityName();
                addressList.add(line3);
            }
            break;
            case "K": {
                String line1 = address.getName();
                addressList.add(line1);
                String line2 = address.getStreet();
                addressList.add(line2);
                String line3 = address.getCityName();
                addressList.add(line3);
            }
            break;
            default:
                throw new RuntimeException("only address type 'S' and 'K' are supported");
        }
        return addressList;
    }
}
