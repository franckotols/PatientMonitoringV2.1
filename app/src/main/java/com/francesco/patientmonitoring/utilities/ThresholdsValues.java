package com.francesco.patientmonitoring.utilities;

/**
 * Created by Fra on 24/11/2016.
 */
public class ThresholdsValues {

    private static double sysMin, sysMax;
    private static double diasMin, diasMax;
    private static double hrMin, hrMax;
    private static double spo2Min, spo2Max;
    private static double glicMin, glicMax;

    public static double getSysMin() {
        return sysMin;
    }

    public static void setSysMin(double sysMin) {
        ThresholdsValues.sysMin = sysMin;
    }

    public static double getSysMax() {
        return sysMax;
    }

    public static void setSysMax(double sysMax) {
        ThresholdsValues.sysMax = sysMax;
    }

    public static double getDiasMin() {
        return diasMin;
    }

    public static void setDiasMin(double diasMin) {
        ThresholdsValues.diasMin = diasMin;
    }

    public static double getDiasMax() {
        return diasMax;
    }

    public static void setDiasMax(double diasMax) {
        ThresholdsValues.diasMax = diasMax;
    }

    public static double getHrMin() {
        return hrMin;
    }

    public static void setHrMin(double hrMin) {
        ThresholdsValues.hrMin = hrMin;
    }

    public static double getHrMax() {
        return hrMax;
    }

    public static void setHrMax(double hrMax) {
        ThresholdsValues.hrMax = hrMax;
    }

    public static double getSpo2Min() {
        return spo2Min;
    }

    public static void setSpo2Min(double spo2Min) {
        ThresholdsValues.spo2Min = spo2Min;
    }

    public static double getSpo2Max() {
        return spo2Max;
    }

    public static void setSpo2Max(double spo2Max) {
        ThresholdsValues.spo2Max = spo2Max;
    }

    public static double getGlicMin() {
        return glicMin;
    }

    public static void setGlicMin(double glicMin) {
        ThresholdsValues.glicMin = glicMin;
    }

    public static double getGlicMax() {
        return glicMax;
    }

    public static void setGlicMax(double glicMax) {
        ThresholdsValues.glicMax = glicMax;
    }
}
