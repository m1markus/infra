package ch.m1m.infra.billing;

public class QrBillPaymentReference {

    private String type = "NON";

    private String reference = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
