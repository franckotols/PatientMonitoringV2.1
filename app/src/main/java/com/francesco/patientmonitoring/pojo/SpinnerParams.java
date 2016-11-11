package com.francesco.patientmonitoring.pojo;

/**
 * Created by Fra on 20/10/2016.
 */
public class SpinnerParams {

    private String id;
    private String name;

    public SpinnerParams(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
