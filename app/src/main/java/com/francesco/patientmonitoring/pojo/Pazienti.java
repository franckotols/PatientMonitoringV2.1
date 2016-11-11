package com.francesco.patientmonitoring.pojo;

/**
 * Created by Fra on 20/09/2016.
 */
public class Pazienti {

    private String full_name, città, birthdate, pat_id;

    public Pazienti(String full_name, String città, String birthdate, String pat_id) {
        this.full_name = full_name;
        this.città = città;
        this.birthdate = birthdate;
        this.pat_id = pat_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCittà() {
        return città;
    }

    public void setCittà(String città) {
        this.città = città;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPat_id() {
        return pat_id;
    }

    public void setPat_id(String pat_id) {
        this.pat_id = pat_id;
    }
}
