package ch.m1m.infra.billing;

public class QrBillPaymentReference {

    public static String TYPE_NONE = "NON";
    public static String TYPE_SCOR = "SCOR";

    private String type = TYPE_NONE;

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
