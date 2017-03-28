package com.globalsoft.ServicePro.Item;

import java.io.Serializable;

public final class SOSparesClass implements Serializable{
    
    public String materialStr = "";
    public String matDescStr = "";
    public String quantityStr = "";
    public String unitStr = "";
    public String serialNoStr = "";
    public String numberExtStr = "";
    
    public SOSparesClass (){}
    
    public SOSparesClass (String s1, String s2, String s3, String s4, String s5, String s6) {
        materialStr = s1;
        matDescStr = s2;
        quantityStr = s3;
        unitStr = s4;
        serialNoStr = s5;
        numberExtStr = s6;
    }
    
    public SOSparesClass (String[] values) {
        materialStr = values[0];
        matDescStr = values[1];
        quantityStr = values[2];
        unitStr = values[3];
        serialNoStr = values[4];
        numberExtStr = values[5];
    }
    
    //Get Functions
    public String getMaterialStr(){
        return this.materialStr;
    }//fn getMaterialStr
    
    public String getMaterialDescStr(){
        return this.matDescStr;
    }//fn getMaterialDescStr
    
    public String getQuantityStr(){
        return this.quantityStr;
    }//fn getQuantityStr
    
    public String getUnitsStr(){
        return this.unitStr;
    }//fn getUnitsStr
    
    public String getSerialNoStr(){
        return this.serialNoStr;
    }//fn getSerialNoStr
    
    public String getNumberExtStr(){
        return this.numberExtStr;
    }//fn getNumberExtStr
    
    // Set Functions
    public void setMaterialStr(String str){
        this.materialStr = str;        
    }//fn setMaterialStr
    
    public void setMaterialDescStr(String str){
        this.matDescStr = str;        
    }//fn setMaterialDescStr
    
    public void setQuantityStr(String str){
        this.quantityStr = str;        
    }//fn setQuantityStr
    
    public void setUnitsStr(String str){
        this.unitStr = str;        
    }//fn setUnitsStr
    
    public void setSerialNoStr(String str){
        this.serialNoStr = str;        
    }//fn setSerialNoStr
    
    public void setNumberExtStr(String str){
        this.numberExtStr = str;    
    }//fn setNumberExtStr
    
}//End of class SOSparesClass 


