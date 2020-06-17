package ch.m1m.infra.billing;

interface QrBillLanguage {
    public String getVorDerZahlungAbtrennen();
    public String getZahlteil();
    public String getEmpfangsschein();
    public String getKontoZahlbarAn();
    public String getReferenz();
}
