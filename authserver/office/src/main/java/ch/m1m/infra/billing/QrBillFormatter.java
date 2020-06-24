package ch.m1m.infra.billing;

import nl.garvelink.iban.IBAN;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class QrBillFormatter {

    String iban(String inIban) {
        IBAN iban = IBAN.valueOf(inIban);
        return iban.toString();
    }

    String amount(BigDecimal inAmount) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        String formattedAmount = formatter.format(inAmount);
        return formattedAmount;
    }
}
