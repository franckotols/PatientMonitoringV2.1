package com.francesco.patientmonitoring.pojo;

/**
 * Created by Fra on 10/11/2016.
 */
public class UrinMeasure {

    private String date;
    private String manufacturer;

    private String bil, pro, uro, blo, leu, ph, ket, sg, glu, nit;

    public UrinMeasure(String date, String manufacturer, String bil, String pro, String blo, String uro, String leu, String ph, String sg, String ket, String glu, String nit) {
        this.date = date;
        this.manufacturer = manufacturer;
        this.bil = bil;
        this.pro = pro;
        this.blo = blo;
        this.uro = uro;
        this.leu = leu;
        this.ph = ph;
        this.sg = sg;
        this.ket = ket;
        this.glu = glu;
        this.nit = nit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBil() {
        return bil;
    }

    public void setBil(String bil) {
        this.bil = bil;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getUro() {
        return uro;
    }

    public void setUro(String uro) {
        this.uro = uro;
    }

    public String getBlo() {
        return blo;
    }

    public void setBlo(String blo) {
        this.blo = blo;
    }

    public String getLeu() {
        return leu;
    }

    public void setLeu(String leu) {
        this.leu = leu;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public String getSg() {
        return sg;
    }

    public void setSg(String sg) {
        this.sg = sg;
    }

    public String getGlu() {
        return glu;
    }

    public void setGlu(String glu) {
        this.glu = glu;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }
}
