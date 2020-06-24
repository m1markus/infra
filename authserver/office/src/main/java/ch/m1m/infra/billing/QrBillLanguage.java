package ch.m1m.infra.billing;

interface QrBillLanguage {
    public String getVorDerZahlungAbtrennen();
    public String getZahlteil();
    public String getEmpfangsschein();
    public String getKontoZahlbarAn();
    public String getReferenz();
    public String getZahlbarDurch();
    public String getWaehrung();
    public String getBetrag();
    public String getAnnahmestelle();
}
