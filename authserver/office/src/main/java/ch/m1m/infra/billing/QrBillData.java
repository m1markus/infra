package ch.m1m.infra.billing;

public class QrBillData {

    private QrBillPaymentReceiver receiver = new QrBillPaymentReceiver();

    // reserved for future use
    private QrBillAddress finalReceiver = new QrBillAddress();

    private QrBillPaymentInformation paymentInformation = new QrBillPaymentInformation();

    private QrBillAddress payer = new QrBillAddress();

    private QrBillPaymentReference reference = new QrBillPaymentReference();

    private QrBillAdditionalInformation additionalInformation = new QrBillAdditionalInformation();

    private QrBillPaymentAV av = new QrBillPaymentAV();

    public QrBillPaymentReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(QrBillPaymentReceiver receiver) {
        this.receiver = receiver;
    }

    public QrBillAddress getFinalReceiver() {
        return finalReceiver;
    }

    public void setFinalReceiver(QrBillAddress finalReceiver) {
        this.finalReceiver = finalReceiver;
    }

    public QrBillPaymentInformation getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(QrBillPaymentInformation paymentInformation) {
        this.paymentInformation = paymentInformation;
    }

    public QrBillAddress getPayer() {
        return payer;
    }

    public void setPayer(QrBillAddress payer) {
        this.payer = payer;
    }

    public QrBillPaymentReference getReference() {
        return reference;
    }

    public void setReference(QrBillPaymentReference reference) {
        this.reference = reference;
    }

    public QrBillAdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(QrBillAdditionalInformation additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public QrBillPaymentAV getAV() {
        return av;
    }

    public void setAV(QrBillPaymentAV av) {
        this.av = av;
    }
}
