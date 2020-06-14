package ch.m1m.infra.billing;

public class QrBillAdditionalInformation {

    private String note = "";

    private String paymentInformation = "";

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(String paymentInformation) {
        this.paymentInformation = paymentInformation;
    }
}
