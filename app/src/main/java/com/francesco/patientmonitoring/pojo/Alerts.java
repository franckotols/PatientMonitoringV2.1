package com.francesco.patientmonitoring.pojo;

/**
 * Created by Fra on 29/09/2016.
 */
public class Alerts {

    private String patient_name, date, type, message, read_status,id;


    public Alerts(String patient_name, String date, String type, String message, String read_status, String id) {
        this.patient_name = patient_name;
        this.date = date;
        this.type = type;
        this.message = message;
        this.read_status = read_status;
        this.id = id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
