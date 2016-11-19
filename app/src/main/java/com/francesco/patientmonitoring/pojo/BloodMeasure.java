package com.francesco.patientmonitoring.pojo;

/**
 * Created by Fra on 17/11/2016.
 */
public class BloodMeasure {

    private String date;
    private String keys;
    private String values;

    public BloodMeasure(String date, String keys, String values) {
        this.date = date;
        this.keys = keys;
        this.values = values;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
