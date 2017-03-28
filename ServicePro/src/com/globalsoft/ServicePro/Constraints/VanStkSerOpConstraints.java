/*
 * VanStkSerOpConstraints.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


public class VanStkSerOpConstraints implements KvmSerializable, Serializable {
    
    public String BP_UNAME = "";
    public String MATNR = "";
    public String WERKS = "";
    public String LGORT = "";
    public String MAKTX_INSYLANGU = "";
    public String EQUNR = "";
    public String SERNR = "";
    
    public VanStkSerOpConstraints (){}
    

    public VanStkSerOpConstraints (String[] values) {
        BP_UNAME = values[0];
        MATNR = values[1];
        WERKS = values[2];
        LGORT = values[3];
        MAKTX_INSYLANGU = values[4];
        EQUNR = values[5];
        SERNR = values[6];
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
                return MAKTX_INSYLANGU;
            case 5:
                return EQUNR;
            case 6:
                return SERNR;
        }
        
        return null;
    }

    public int getPropertyCount() {
        return 7;
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
                info.name = "MAKTX_INSYLANGU";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "EQUNR";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SERNR";
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
                MAKTX_INSYLANGU = value.toString();
                break;
            case 5:
                EQUNR = value.toString();
                break;
            case 6:
                SERNR = value.toString();
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
    
    public String getMattDesc(){
        return this.MAKTX_INSYLANGU;
    }//fn getMattDesc
    
    public String getEquipment(){
        return this.EQUNR;
    }//fn getEquipment
    
    public String getSerialNo(){
        return this.SERNR;
    }//fn getSerialNo
    
}//End of class VanStkSerOpConstraints 

