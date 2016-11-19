package com.francesco.patientmonitoring.utilities;

/**
 * Created by Fra on 19/11/2016.
 */
public class LastPointValues {

    private static String date;
    private static String time_of_day;
    private static String syst;
    private static String diast;
    private static String spo2;
    private static String hr;
    private static String glic;

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        LastPointValues.date = date;
    }

    public static String getTime_of_day() {
        return time_of_day;
    }

    public static void setTime_of_day(String time_of_day) {
        LastPointValues.time_of_day = time_of_day;
    }

    public static String getSyst() {
        return syst;
    }

    public static void setSyst(String syst) {
        LastPointValues.syst = syst;
    }

    public static String getDiast() {
        return diast;
    }

    public static void setDiast(String diast) {
        LastPointValues.diast = diast;
    }

    public static String getSpo2() {
        return spo2;
    }

    public static void setSpo2(String spo2) {
        LastPointValues.spo2 = spo2;
    }

    public static String getHr() {
        return hr;
    }

    public static void setHr(String hr) {
        LastPointValues.hr = hr;
    }

    public static String getGlic() {
        return glic;
    }

    public static void setGlic(String glic) {
        LastPointValues.glic = glic;
    }
}
