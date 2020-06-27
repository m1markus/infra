package ch.m1m.infra.billing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class QrBillFormatterTest {

    private QrBillFormatter format = new QrBillFormatter();

    @Test
    public void givenBigDecimal_whenFormattingAmount_then1000SeparatorIsSpace() {
        // GIVEN
        BigDecimal amount1 = new BigDecimal("9357.95");

        // WHEN
        String result = format.amount(amount1);

        // THEN
        assertEquals("9 357.95", result);
    }

    @Test
    public void givenOneWithoutDecimal_whenFormattingAmount_thenWeGetTwoDecimalZeros() {
        // GIVEN
        BigDecimal amount1 = new BigDecimal("1");

        // WHEN
        String result = format.amount(amount1);

        // THEN
        assertEquals("1.00", result);
    }
}
