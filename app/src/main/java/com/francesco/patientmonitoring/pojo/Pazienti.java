package com.francesco.patientmonitoring.pojo;

import java.util.ArrayList;

/**
 * Created by Fra on 20/09/2016.
 */
public class Pazienti {

    private String full_name, città, birthdate, pat_id;
    private ArrayList<String> list_disease;

    public ArrayList<String> getList_disease() {
        return list_disease;
    }

    public void setList_disease(ArrayList<String> list_disease) {
        this.list_disease = list_disease;

    }

    public Pazienti(String full_name, String città, String birthdate, String pat_id, ArrayList<String> list_disease) {
        this.full_name = full_name;
        this.città = città;
        this.birthdate = birthdate;
        this.pat_id = pat_id;
        this.list_disease = list_disease;
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
