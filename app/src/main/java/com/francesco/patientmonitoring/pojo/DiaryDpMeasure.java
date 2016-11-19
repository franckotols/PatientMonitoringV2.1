package com.francesco.patientmonitoring.pojo;

import java.util.ArrayList;

/**
 * Created by Fra on 15/11/2016.
 */
public class DiaryDpMeasure {

    private String date, startTime, endTime;
    private boolean abdPain,diarrhea,dischProbs,nausea,sameConc, turbidDisch,vomit;
    private int ant_bags, cyclNumb,dischTime,ex_bags,glu150bags,glu250bags,glu350bags,hepBags,insBags, totDischnCycTIDAL;
    private int lastLoadVol,loadStopTime,loadVol,loadVolTIDAL,lostTime,skipCyc,stopTimeTIDAL,totVol,totUF,volum1Disch, volumUFTidal;
    private String alarmsMachine;
    private String manualExchanges; //è una stringa che deve essere trasformata in json;

    public DiaryDpMeasure(String date, String startTime, String endTime, boolean abdPain, boolean diarrhea, boolean dischProbs, boolean nausea, boolean sameConc, boolean turbidDisch, boolean vomit, int ant_bags, int cyclNumb, int dischTime, int ex_bags, int glu150bags, int glu250bags, int hepBags, int glu350bags, int insBags, int lastLoadVol, int loadStopTime, int loadVol, int loadVolTIDAL, int lostTime, int skipCyc, int stopTimeTIDAL, int totVol, int totUF, int volum1Disch, int volumUFTidal, String alarmsMachine, String manualExchanges, int totDischnCycTIDAL) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.abdPain = abdPain;
        this.diarrhea = diarrhea;
        this.dischProbs = dischProbs;
        this.nausea = nausea;
        this.sameConc = sameConc;
        this.turbidDisch = turbidDisch;
        this.vomit = vomit;
        this.ant_bags = ant_bags;
        this.cyclNumb = cyclNumb;
        this.dischTime = dischTime;
        this.ex_bags = ex_bags;
        this.glu150bags = glu150bags;
        this.glu250bags = glu250bags;
        this.hepBags = hepBags;
        this.glu350bags = glu350bags;
        this.insBags = insBags;
        this.lastLoadVol = lastLoadVol;
        this.loadStopTime = loadStopTime;
        this.loadVol = loadVol;
        this.loadVolTIDAL = loadVolTIDAL;
        this.lostTime = lostTime;
        this.skipCyc = skipCyc;
        this.stopTimeTIDAL = stopTimeTIDAL;
        this.totVol = totVol;
        this.totUF = totUF;
        this.volum1Disch = volum1Disch;
        this.volumUFTidal = volumUFTidal;
        this.alarmsMachine = alarmsMachine;
        this.manualExchanges = manualExchanges;
        this.totDischnCycTIDAL= totDischnCycTIDAL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isAbdPain() {
        return abdPain;
    }

    public void setAbdPain(boolean abdPain) {
        this.abdPain = abdPain;
    }

    public boolean isDiarrhea() {
        return diarrhea;
    }

    public void setDiarrhea(boolean diarrhea) {
        this.diarrhea = diarrhea;
    }

    public boolean isDischProbs() {
        return dischProbs;
    }

    public void setDischProbs(boolean dischProbs) {
        this.dischProbs = dischProbs;
    }

    public boolean isNausea() {
        return nausea;
    }

    public void setNausea(boolean nausea) {
        this.nausea = nausea;
    }

    public boolean isSameConc() {
        return sameConc;
    }

    public void setSameConc(boolean sameConc) {
        this.sameConc = sameConc;
    }

    public boolean isTurbidDisch() {
        return turbidDisch;
    }

    public void setTurbidDisch(boolean turbidDisch) {
        this.turbidDisch = turbidDisch;
    }

    public boolean isVomit() {
        return vomit;
    }

    public void setVomit(boolean vomit) {
        this.vomit = vomit;
    }

    public int getAnt_bags() {
        return ant_bags;
    }

    public void setAnt_bags(int ant_bags) {
        this.ant_bags = ant_bags;
    }

    public int getCyclNumb() {
        return cyclNumb;
    }

    public void setCyclNumb(int cyclNumb) {
        this.cyclNumb = cyclNumb;
    }

    public int getDischTime() {
        return dischTime;
    }

    public void setDischTime(int dischTime) {
        this.dischTime = dischTime;
    }

    public int getEx_bags() {
        return ex_bags;
    }

    public void setEx_bags(int ex_bags) {
        this.ex_bags = ex_bags;
    }

    public int getGlu150bags() {
        return glu150bags;
    }

    public void setGlu150bags(int glu150bags) {
        this.glu150bags = glu150bags;
    }

    public int getGlu250bags() {
        return glu250bags;
    }

    public void setGlu250bags(int glu250bags) {
        this.glu250bags = glu250bags;
    }

    public int getGlu350bags() {
        return glu350bags;
    }

    public void setGlu350bags(int glu350bags) {
        this.glu350bags = glu350bags;
    }

    public int getHepBags() {
        return hepBags;
    }

    public void setHepBags(int hepBags) {
        this.hepBags = hepBags;
    }

    public int getInsBags() {
        return insBags;
    }

    public void setInsBags(int insBags) {
        this.insBags = insBags;
    }

    public int getLastLoadVol() {
        return lastLoadVol;
    }

    public void setLastLoadVol(int lastLoadVol) {
        this.lastLoadVol = lastLoadVol;
    }

    public int getLoadStopTime() {
        return loadStopTime;
    }

    public void setLoadStopTime(int loadStopTime) {
        this.loadStopTime = loadStopTime;
    }

    public int getLoadVol() {
        return loadVol;
    }

    public void setLoadVol(int loadVol) {
        this.loadVol = loadVol;
    }

    public int getLoadVolTIDAL() {
        return loadVolTIDAL;
    }

    public void setLoadVolTIDAL(int loadVolTIDAL) {
        this.loadVolTIDAL = loadVolTIDAL;
    }

    public int getLostTime() {
        return lostTime;
    }

    public void setLostTime(int lostTime) {
        this.lostTime = lostTime;
    }

    public int getSkipCyc() {
        return skipCyc;
    }

    public void setSkipCyc(int skipCyc) {
        this.skipCyc = skipCyc;
    }

    public int getStopTimeTIDAL() {
        return stopTimeTIDAL;
    }

    public void setStopTimeTIDAL(int stopTimeTIDAL) {
        this.stopTimeTIDAL = stopTimeTIDAL;
    }

    public int getTotVol() {
        return totVol;
    }

    public void setTotVol(int totVol) {
        this.totVol = totVol;
    }

    public int getTotUF() {
        return totUF;
    }

    public void setTotUF(int totUF) {
        this.totUF = totUF;
    }

    public int getVolum1Disch() {
        return volum1Disch;
    }

    public void setVolum1Disch(int volum1Disch) {
        this.volum1Disch = volum1Disch;
    }

    public int getVolumUFTidal() {
        return volumUFTidal;
    }

    public void setVolumUFTidal(int volumUFTidal) {
        this.volumUFTidal = volumUFTidal;
    }

    public String getAlarmsMachine() {
        return alarmsMachine;
    }

    public void setAlarmsMachine(String alarmsMachine) {
        this.alarmsMachine = alarmsMachine;
    }

    public String getManualExchanges() {
        return manualExchanges;
    }

    public void setManualExchanges(String manualExchanges) {
        this.manualExchanges = manualExchanges;
    }

    public int getTotDischnCycTIDAL() {
        return totDischnCycTIDAL;
    }

    public void setTotDischnCycTIDAL(int totDischnCycTIDAL) {
        this.totDischnCycTIDAL = totDischnCycTIDAL;
    }
}
