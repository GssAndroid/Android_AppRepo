package com.globalsoft.ServicePro.Item;

import java.io.Serializable;

public final class SOFaultClass implements Serializable{
    
    public String activityStr = "";
    public String sympGrpStr = "";
    public String sympCodeStr = "";
    public String sympDescStr = "";
    public String probGrpStr = "";
    public String probCodeStr = "";
    public String probDescStr = "";
    public String causeGrpStr = "";
    public String causeCodeStr = "";
    public String causeDescStr = "";
    
    public SOFaultClass (){}
    
    public SOFaultClass (String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, String s10) {
        activityStr = s1;
        sympGrpStr = s2;
        sympCodeStr = s3;
        sympDescStr = s4;
        probGrpStr = s5;
        probCodeStr = s6;
        probDescStr = s7;
        causeGrpStr = s8;
        causeCodeStr = s9;
        causeDescStr = s10;
    }
    
    public SOFaultClass (String[] values) {
        activityStr = values[0];
        sympGrpStr = values[1];
        sympCodeStr = values[2];
        sympDescStr = values[3];
        probGrpStr = values[4];
        probCodeStr = values[5];
        probDescStr = values[6];
        causeGrpStr = values[7];
        causeCodeStr = values[8];
        causeDescStr = values[9];
    }
    
    //Get Functions
    public String getActivityStr(){
        return this.activityStr;
    }//fn getActivityStr
    
    public String getSymptomGroupStr(){
        return this.sympGrpStr;
    }//fn getSymptomGroupStr
    
    public String getSymptomCodeStr(){
        return this.sympCodeStr;
    }//fn getSymptomCodeStr
    
    public String getSymptomDescStr(){
        return this.sympDescStr;
    }//fn getSymptomDescStr
    
    public String getProblemGroupStr(){
        return this.probGrpStr;
    }//fn getProblemGroupStr
    
    public String getProblemCodeStr(){
        return this.probCodeStr;
    }//fn getProblemCodeStr
    
    public String getProblemDescStr(){
        return this.probDescStr;
    }//fn getProblemDescStr
    
    public String getCauseGroupStr(){
        return this.causeGrpStr;
    }//fn getCauseGroupStr
    
    public String getCauseCodeStr(){
        return this.causeCodeStr;
    }//fn getCauseCodeStr
    
    public String getCauseDescStr(){
        return this.causeDescStr;
    }//fn getCauseDescStr
    
    // Set Functions
    public void setActivityStr(String str){
        this.activityStr = str;        
    }//fn setActivityStr
    
    public void setProblemGroupStr(String str){
        this.probGrpStr = str;        
    }//fn getProblemGroupStr
    
    public void setProblemCodeStr(String str){
        this.probCodeStr = str;        
    }//fn getProblemCodeStr
    
    public void setProblemDescStr(String str){
        this.probDescStr = str;        
    }//fn setProblemDescStr
    
    public void setSymptomGroupStr(String str){
        this.sympGrpStr = str;        
    }//fn setSymptomGroupStr
    
    public void setSymptomCodeStr(String str){
        this.sympCodeStr = str;        
    }//fn setSymptomCodeStr
    
    public void setSymptomDescStr(String str){
        this.sympDescStr = str;        
    }//fn setSymptomDescStr
    
    public void setCauseGroupStr(String str){
        this.causeGrpStr = str;        
    }//fn setCauseGroupStr
    
    public void setCauseCodeStr(String str){
        this.causeCodeStr = str;        
    }//fn setCauseCodeStr
    
    public void setCauseDescStr(String str){
        this.causeDescStr = str;        
    }//fn setCauseDescStr
    
}//End of class SOFaultClass 


