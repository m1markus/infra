package ch.m1m.infra.billing;

public class QrBillLanguageDE implements QrBillLanguage {

    public String getVorDerZahlungAbtrennen() { return "Vor der Einzahlung abtrennen"; }

    public String getZahlteil() {
        return "Zahlteil";
    }
    public String getEmpfangsschein() {
        return "Empfangsschein";
    }

    public String getKontoZahlbarAn() {
        return "Konto / Zahlbar an";
    }
    public String getReferenz() {
        return "Referenz";
    }
}
