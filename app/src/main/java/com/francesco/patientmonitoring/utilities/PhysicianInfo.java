package com.francesco.patientmonitoring.utilities;

/**
 * Created by Fra on 12/11/2016.
 */
public class PhysicianInfo {

    public static String getPhysician_id() {
        return physician_id;
    }

    public static void setPhysician_id(String physician_id) {
        PhysicianInfo.physician_id = physician_id;
    }

    private static String physician_id;
    //private static String physician_name;
}
