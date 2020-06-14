package ch.m1m.infra.billing;

public class QrBillPaymentReceiver {

    private String iban = "";

    private QrBillAddress address = new QrBillAddress();

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public QrBillAddress getAddress() {
        return address;
    }

    public void setAddress(QrBillAddress address) {
        this.address = address;
    }
}
