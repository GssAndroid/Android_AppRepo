/*
 * VanStkOpConstraints.java
 *
 * © <GSS>, <2001>
 * Confidential and proprietary. 
 * Developer : G.M.Ibrahim 
 */

package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


public class VanStkOpConstraints implements KvmSerializable, Serializable {
    
    public String BP_UNAME = "";
    public String MATNR = "";
    public String WERKS = "";
    public String LGORT = "";
    public String CHARG = "";
    public String MAKTX_INSYLANGU = "";
    public String MEINS = "";
    public String LABST = "";
    public String TRAME = "";
    public String ZZSERNR_EXIST = "";
    
    public VanStkOpConstraints (){}
    

    public VanStkOpConstraints (String[] values) {
        BP_UNAME = values[0];
        MATNR = values[1];
        WERKS = values[2];
        LGORT = values[3];
        CHARG = values[4];
        MAKTX_INSYLANGU = values[5];
        MEINS = values[6];
        LABST = values[7];
        TRAME = values[8];
        ZZSERNR_EXIST = values[9];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return BP_UNAME;
            case 1:
                return MATNR;
            case 2:
                return WERKS;
            case 3:
                return LGORT; 
            case 4:
                return CHARG;
            case 5:
                return MAKTX_INSYLANGU;
            case 6:
                return MEINS;
            case 7:
                return LABST;
            case 8:
                return TRAME;
            case 9:
                return ZZSERNR_EXIST;
        }
        
        return null;
    }

    public int getPropertyCount() {
        return 10;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BP_UNAME";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MATNR";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "WERKS";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LGORT";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CHARG";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MAKTX_INSYLANGU";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MEINS";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LABST";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "TRAME";
                break;  
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ZZSERNR_EXIST ";
                break;
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                BP_UNAME = value.toString();
                break;
            case 1:
                MATNR = value.toString();
                break;
            case 2:
                WERKS = value.toString();
                break;
            case 3:
                LGORT = value.toString();
                break;
            case 4:
                CHARG = value.toString();
                break;
            case 5:
                MAKTX_INSYLANGU = value.toString();
                break;
            case 6:
                MEINS = value.toString();
                break;
            case 7:
                LABST = value.toString();
                break;
            case 8:
                TRAME = value.toString();
                break;
            case 9:
                ZZSERNR_EXIST = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getBusinessPrtName(){
        return this.BP_UNAME;
    }//fn getBusinessPrtName
    
    public String getMaterial(){
        return this.MATNR;
    }//fn getMaterial
    
    public String getWerks(){
        return this.WERKS;
    }//fn getWerks
    
    public String getStorageLoc(){
        return this.LGORT;
    }//fn getStorageLoc
    
    public String getBatchNo(){
        return this.CHARG;
    }//fn getBatchNo
    
    public String getMattDesc(){
        return this.MAKTX_INSYLANGU;
    }//fn getMattDesc
    
    public String getMeasureUnits(){
        return this.MEINS;
    }//fn getMeasureUnits
    
    public String getStockQty(){
        return this.LABST;
    }//fn getStockQty
    
    public String getMatQtyTransit(){
        return this.TRAME;
    }//fn getMatQtyTransit
    
    public String getSerAvailable(){
        return this.ZZSERNR_EXIST;
    }//fn getSerAvailable
    
}//End of class VanStkOpConstraints 

