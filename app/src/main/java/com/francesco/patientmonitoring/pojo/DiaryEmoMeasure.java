package com.francesco.patientmonitoring.pojo;

/**
 * Created by Fra on 21/11/2016.
 */
public class DiaryEmoMeasure {

    private String date;

    private String emoFeverTemp, emoOtherAnnotation, emoIdealWeight, emoDeltaWeight, emoProgrammingWeight;
    private String emoDialysisHour, emoFilter, emoDuration, emoInitialWeight, emoFinalWeight, emoDialysisTherapyProgress, emoDialysisTherapyEnd;
    private String emoStartHour, emoPreClosingHour, emoHourCramp, emoEndHour;
    private String emoPressureStartMin, emoPressureStartMax, emoPressurePreMin, emoPressurePreMax, emoPressureEndMin, emoPressureEndMax;
    private String emoHRStartHour, emoHRPreHour, emoHREndHour, emoBloodStrStartHour, emoBloodStrEndHour, emoVenPressStartHour, emoVenPressEndHour;
    private boolean emoCramp, emoFever;
    private String emoListDisMonitor, emoListDisOsm, emoListOthAct;

    public DiaryEmoMeasure(String date, String emoFeverTemp, String emoOtherAnnotation, String emoIdealWeight, String emoDeltaWeight, String emoProgrammingWeight, String emoDialysisHour, String emoFilter, String emoDuration, String emoInitialWeight, String emoFinalWeight, String emoDialysisTherapyProgress, String emoDialysisTherapyEnd, String emoStartHour, String emoPreClosingHour, String emoEndHour, String emoHourCramp, String emoPressureStartMin, String emoPressureStartMax, String emoPressurePreMin, String emoPressurePreMax, String emoPressureEndMin, String emoPressureEndMax, String emoHRStartHour, String emoHRPreHour, String emoHREndHour, String emoBloodStrStartHour, String emoBloodStrEndHour, String emoVenPressStartHour, String emoVenPressEndHour, boolean emoCramp, boolean emoFever, String emoListDisMonitor, String emoListDisOsm, String emoListOthAct) {
        this.date = date;
        this.emoFeverTemp = emoFeverTemp;
        this.emoOtherAnnotation = emoOtherAnnotation;
        this.emoIdealWeight = emoIdealWeight;
        this.emoDeltaWeight = emoDeltaWeight;
        this.emoProgrammingWeight = emoProgrammingWeight;
        this.emoDialysisHour = emoDialysisHour;
        this.emoFilter = emoFilter;
        this.emoDuration = emoDuration;
        this.emoInitialWeight = emoInitialWeight;
        this.emoFinalWeight = emoFinalWeight;
        this.emoDialysisTherapyProgress = emoDialysisTherapyProgress;
        this.emoDialysisTherapyEnd = emoDialysisTherapyEnd;
        this.emoStartHour = emoStartHour;
        this.emoPreClosingHour = emoPreClosingHour;
        this.emoEndHour = emoEndHour;
        this.emoHourCramp = emoHourCramp;
        this.emoPressureStartMin = emoPressureStartMin;
        this.emoPressureStartMax = emoPressureStartMax;
        this.emoPressurePreMin = emoPressurePreMin;
        this.emoPressurePreMax = emoPressurePreMax;
        this.emoPressureEndMin = emoPressureEndMin;
        this.emoPressureEndMax = emoPressureEndMax;
        this.emoHRStartHour = emoHRStartHour;
        this.emoHRPreHour = emoHRPreHour;
        this.emoHREndHour = emoHREndHour;
        this.emoBloodStrStartHour = emoBloodStrStartHour;
        this.emoBloodStrEndHour = emoBloodStrEndHour;
        this.emoVenPressStartHour = emoVenPressStartHour;
        this.emoVenPressEndHour = emoVenPressEndHour;
        this.emoCramp = emoCramp;
        this.emoFever = emoFever;
        this.emoListDisMonitor = emoListDisMonitor;
        this.emoListDisOsm = emoListDisOsm;
        this.emoListOthAct = emoListOthAct;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmoFeverTemp() {
        return emoFeverTemp;
    }

    public void setEmoFeverTemp(String emoFeverTemp) {
        this.emoFeverTemp = emoFeverTemp;
    }

    public String getEmoOtherAnnotation() {
        return emoOtherAnnotation;
    }

    public void setEmoOtherAnnotation(String emoOtherAnnotation) {
        this.emoOtherAnnotation = emoOtherAnnotation;
    }

    public String getEmoIdealWeight() {
        return emoIdealWeight;
    }

    public void setEmoIdealWeight(String emoIdealWeight) {
        this.emoIdealWeight = emoIdealWeight;
    }

    public String getEmoDeltaWeight() {
        return emoDeltaWeight;
    }

    public void setEmoDeltaWeight(String emoDeltaWeight) {
        this.emoDeltaWeight = emoDeltaWeight;
    }

    public String getEmoProgrammingWeight() {
        return emoProgrammingWeight;
    }

    public void setEmoProgrammingWeight(String emoProgrammingWeight) {
        this.emoProgrammingWeight = emoProgrammingWeight;
    }

    public String getEmoDialysisHour() {
        return emoDialysisHour;
    }

    public void setEmoDialysisHour(String emoDialysisHour) {
        this.emoDialysisHour = emoDialysisHour;
    }

    public String getEmoFilter() {
        return emoFilter;
    }

    public void setEmoFilter(String emoFilter) {
        this.emoFilter = emoFilter;
    }

    public String getEmoDuration() {
        return emoDuration;
    }

    public void setEmoDuration(String emoDuration) {
        this.emoDuration = emoDuration;
    }

    public String getEmoInitialWeight() {
        return emoInitialWeight;
    }

    public void setEmoInitialWeight(String emoInitialWeight) {
        this.emoInitialWeight = emoInitialWeight;
    }

    public String getEmoFinalWeight() {
        return emoFinalWeight;
    }

    public void setEmoFinalWeight(String emoFinalWeight) {
        this.emoFinalWeight = emoFinalWeight;
    }

    public String getEmoDialysisTherapyProgress() {
        return emoDialysisTherapyProgress;
    }

    public void setEmoDialysisTherapyProgress(String emoDialysisTherapyProgress) {
        this.emoDialysisTherapyProgress = emoDialysisTherapyProgress;
    }

    public String getEmoDialysisTherapyEnd() {
        return emoDialysisTherapyEnd;
    }

    public void setEmoDialysisTherapyEnd(String emoDialysisTherapyEnd) {
        this.emoDialysisTherapyEnd = emoDialysisTherapyEnd;
    }

    public String getEmoStartHour() {
        return emoStartHour;
    }

    public void setEmoStartHour(String emoStartHour) {
        this.emoStartHour = emoStartHour;
    }

    public String getEmoPreClosingHour() {
        return emoPreClosingHour;
    }

    public void setEmoPreClosingHour(String emoPreClosingHour) {
        this.emoPreClosingHour = emoPreClosingHour;
    }

    public String getEmoHourCramp() {
        return emoHourCramp;
    }

    public void setEmoHourCramp(String emoHourCramp) {
        this.emoHourCramp = emoHourCramp;
    }

    public String getEmoEndHour() {
        return emoEndHour;
    }

    public void setEmoEndHour(String emoEndHour) {
        this.emoEndHour = emoEndHour;
    }

    public String getEmoPressureStartMin() {
        return emoPressureStartMin;
    }

    public void setEmoPressureStartMin(String emoPressureStartMin) {
        this.emoPressureStartMin = emoPressureStartMin;
    }

    public String getEmoPressureStartMax() {
        return emoPressureStartMax;
    }

    public void setEmoPressureStartMax(String emoPressureStartMax) {
        this.emoPressureStartMax = emoPressureStartMax;
    }

    public String getEmoPressurePreMin() {
        return emoPressurePreMin;
    }

    public void setEmoPressurePreMin(String emoPressurePreMin) {
        this.emoPressurePreMin = emoPressurePreMin;
    }

    public String getEmoPressurePreMax() {
        return emoPressurePreMax;
    }

    public void setEmoPressurePreMax(String emoPressurePreMax) {
        this.emoPressurePreMax = emoPressurePreMax;
    }

    public String getEmoPressureEndMax() {
        return emoPressureEndMax;
    }

    public void setEmoPressureEndMax(String emoPressureEndMax) {
        this.emoPressureEndMax = emoPressureEndMax;
    }

    public String getEmoPressureEndMin() {
        return emoPressureEndMin;
    }

    public void setEmoPressureEndMin(String emoPressureEndMin) {
        this.emoPressureEndMin = emoPressureEndMin;
    }

    public String getEmoHRStartHour() {
        return emoHRStartHour;
    }

    public void setEmoHRStartHour(String emoHRStartHour) {
        this.emoHRStartHour = emoHRStartHour;
    }

    public String getEmoHRPreHour() {
        return emoHRPreHour;
    }

    public void setEmoHRPreHour(String emoHRPreHour) {
        this.emoHRPreHour = emoHRPreHour;
    }

    public String getEmoHREndHour() {
        return emoHREndHour;
    }

    public void setEmoHREndHour(String emoHREndHour) {
        this.emoHREndHour = emoHREndHour;
    }

    public String getEmoBloodStrStartHour() {
        return emoBloodStrStartHour;
    }

    public void setEmoBloodStrStartHour(String emoBloodStrStartHour) {
        this.emoBloodStrStartHour = emoBloodStrStartHour;
    }

    public String getEmoBloodStrEndHour() {
        return emoBloodStrEndHour;
    }

    public void setEmoBloodStrEndHour(String emoBloodStrEndHour) {
        this.emoBloodStrEndHour = emoBloodStrEndHour;
    }

    public String getEmoVenPressStartHour() {
        return emoVenPressStartHour;
    }

    public void setEmoVenPressStartHour(String emoVenPressStartHour) {
        this.emoVenPressStartHour = emoVenPressStartHour;
    }

    public String getEmoVenPressEndHour() {
        return emoVenPressEndHour;
    }

    public void setEmoVenPressEndHour(String emoVenPressEndHour) {
        this.emoVenPressEndHour = emoVenPressEndHour;
    }

    public boolean isEmoCramp() {
        return emoCramp;
    }

    public void setEmoCramp(boolean emoCramp) {
        this.emoCramp = emoCramp;
    }

    public boolean isEmoFever() {
        return emoFever;
    }

    public void setEmoFever(boolean emoFever) {
        this.emoFever = emoFever;
    }

    public String getEmoListDisMonitor() {
        return emoListDisMonitor;
    }

    public void setEmoListDisMonitor(String emoListDisMonitor) {
        this.emoListDisMonitor = emoListDisMonitor;
    }

    public String getEmoListDisOsm() {
        return emoListDisOsm;
    }

    public void setEmoListDisOsm(String emoListDisOsm) {
        this.emoListDisOsm = emoListDisOsm;
    }

    public String getEmoListOthAct() {
        return emoListOthAct;
    }

    public void setEmoListOthAct(String emoListOthAct) {
        this.emoListOthAct = emoListOthAct;
    }
}
