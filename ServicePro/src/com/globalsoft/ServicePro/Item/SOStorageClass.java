package com.globalsoft.ServicePro.Item;

import java.io.Serializable;
import java.util.Vector;

public final class SOStorageClass implements Serializable{
    
    private Vector productListVect;
    private Vector causeCodeListVect;
    private Vector causeCodeGroupVect;
    private Vector probCodeListVect;
    private Vector probCodeGroupVect;
    private Vector symptmCodeListVect;
    private Vector symptmCodeGroupVect;
    private Vector mattEmpListVect;
    
    public SOStorageClass (){}
    
    public SOStorageClass (Vector s1, Vector s2, Vector s3, Vector s4, Vector s5, Vector s6, Vector s7, Vector s8) {
        productListVect = s1;
        causeCodeListVect = s2;
        causeCodeGroupVect = s3;
        probCodeListVect = s4;
        probCodeGroupVect = s5;
        symptmCodeListVect = s6;
        symptmCodeGroupVect = s7;
        mattEmpListVect = s8;
    }
    
    //Get Functions
    public Vector getProductListVect(){
        return this.productListVect;
    }//fn getProductListVect
    
    public Vector getCauseCodeListVect(){
        return this.causeCodeListVect;
    }//fn getCauseCodeListVect
    
    public Vector getCauseCodeGroupVect(){
        return this.causeCodeGroupVect;
    }//fn getCauseCodeGroupVect
    
    public Vector getProbCodeListVect(){
        return this.probCodeListVect;
    }//fn getProbCodeListVect
    
    public Vector getProbCodeGroupVect(){
        return this.probCodeGroupVect;
    }//fn getProbCodeGroupVect
    
    public Vector getSymptmCodeListVect(){
        return this.symptmCodeListVect;
    }//fn getSymptmCodeListVect
    
    public Vector getSymptmCodeGroupVect(){
        return this.symptmCodeGroupVect;
    }//fn getSymptmCodeGroupVect
    
    public Vector getMaterialEmpListVect(){
        return this.mattEmpListVect;
    }//fn getMaterialEmpListVect
    
    // Set Functions
    public void setProductListVect( Vector str){
        this.productListVect = str;        
    }//fn setProductListVect
    
    public void setCauseCodeListVect( Vector str){
       this.causeCodeListVect = str;        
    }//fn setCauseCodeListVect
    
    public void setCauseCodeGroupVect( Vector str){
       this.causeCodeGroupVect = str;        
    }//fn setCauseCodeGroupVect
    
    public void setProbCodeListVect( Vector str){
        this.probCodeListVect = str;        
    }//fn setProbCodeListVect
    
    public void setProbCodeGroupVect( Vector str){
        this.probCodeGroupVect = str;        
    }//fn setProbCodeGroupVect
    
    public void setSymptmCodeListVect( Vector str){
        this.symptmCodeListVect = str;        
    }//fn setSymptmCodeListVect
    
    public void setSymptmCodeGroupVect( Vector str){
        this.symptmCodeGroupVect = str;        
    }//fn setSymptmCodeGroupVect
    
    public void setMaterialEmpListVect( Vector str){
        this.mattEmpListVect = str;        
    }//fn setMaterialEmpListVect
    
}//End of class SOStorageClass 


