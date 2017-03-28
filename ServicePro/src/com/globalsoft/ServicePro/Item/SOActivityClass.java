package com.globalsoft.ServicePro.Item;

import java.io.Serializable;

import org.ksoap2.serialization.KvmSerializable;

public final class SOActivityClass implements Serializable{
    
    public String activityStr = "";
    public String durationStr = "";
    public long srtTimeStr = System.currentTimeMillis();
    public long endTimeStr = System.currentTimeMillis();
    public String notesStr = "";
    public String numberExtStr = "";
    public String timeZoneStr = "";
    public String serviceDesc = "";
    
    public SOActivityClass (){}
    
    public SOActivityClass (String s1, String s2, long s3, long s4, String s5, String s6, String s7, String s8) {
        activityStr = s1;
        durationStr = s2;
        srtTimeStr = s3;
        endTimeStr = s4;
        notesStr = s5;
        numberExtStr = s6;
        timeZoneStr = s7;
        serviceDesc = s8;
    }
    /*
    public SOActivityClass (String[] values) {
        activityStr = values[0];
        durationStr = values[1];
        srtTimeStr = values[2];
        endTimeStr = values[3];
        notesStr = values[4];
    }
    */
    
    //Get Functions
    public String getActivityStr(){
        return this.activityStr;
    }//fn getActivityStr
    
    public String getDurationStr(){
        return this.durationStr;
    }//fn getDurationStr
    
    public long getStartTimeStr(){
        return this.srtTimeStr;
    }//fn getStartTimeStr
    
    public long getEndTimeStr(){
        return this.endTimeStr;
    }//fn getEndTimeStr
    
    public String getNotesStr(){
        return this.notesStr;
    }//fn getNotesStr
    
    public String getServiceDescStr(){
        return this.serviceDesc;
    }//fn getServiceDescStr
    
    public String getNumberExtnStr(){
        return this.numberExtStr;
    }//fn getNumberExtnStr
    
    public String getTimeZoneStr(){
        return this.timeZoneStr;
    }//fn getTimeZoneStr
    
    // Set Functions
    public void setActivityStr(String str){
        this.activityStr = str;        
    }//fn setActivityStr
    
    public void setDurationStr(String str){
        this.durationStr = str;        
    }//fn setDurationStr
    
    public void setStartTimeStr(long str){
        this.srtTimeStr = str;        
    }//fn setStartTimeStr
    
    public void setEndTimeStr(long str){
        this.endTimeStr = str;        
    }//fn setEndTimeStr
    
    public void setNotesStr(String str){
        this.notesStr = str;        
    }//fn setNotesStr
    
    public void setNumberExtnStr(String str){
        this.numberExtStr = str;        
    }//fn setNumberExtnStr
    
    public void setTimeZoneStr(String str){
        this.timeZoneStr = str;        
    }//fn setTimeZoneStr
    
    public void setServiceDescStr(String str){
        this.serviceDesc = str;
    }//fn setServiceDescStr
    
}//End of class SOActivityClass 


