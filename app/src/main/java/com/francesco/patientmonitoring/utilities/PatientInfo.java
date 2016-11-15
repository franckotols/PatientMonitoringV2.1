package com.francesco.patientmonitoring.utilities;

import java.util.ArrayList;

/**
 * Created by Fra on 12/11/2016.
 */
public class PatientInfo {

    //TODO create property for patient info
    private static String patient_name;
    private static String patient_city;
    private static String patient_birthdate;
    private static String patient_id;
    private static boolean[] diseases;
    private static ArrayList<String> list;

    public static ArrayList<String> getList() {
        return list;
    }

    public static void setList(ArrayList<String> list) {
        PatientInfo.list = list;
    }

    public static String getPatient_name() {
        return patient_name;
    }

    public static void setPatient_name(String patient_name) {
        PatientInfo.patient_name = patient_name;
    }

    public static String getPatient_city() {
        return patient_city;
    }

    public static void setPatient_city(String patient_city) {
        PatientInfo.patient_city = patient_city;
    }

    public static String getPatient_birthdate() {
        return patient_birthdate;
    }

    public static void setPatient_birthdate(String patient_birthdate) {
        PatientInfo.patient_birthdate = patient_birthdate;
    }

    public static String getPatient_id() {
        return patient_id;
    }

    public static void setPatient_id(String patient_id) {
        PatientInfo.patient_id = patient_id;
    }

    public static boolean[] getDiseases(){
        if(diseases==null)
            diseases=new boolean[]{false, false, false, false, false, false, false, false};
        return diseases;
    }

    public static void setDiseases(boolean[] value){
        diseases=value;
    }

    public enum Disease{
        peritoneale,    //0
        emodialisi,
        insufficienza_renale,
        trapianto,
        tipo1,
        tipo2,
        sangue,
        urine
    }
}
