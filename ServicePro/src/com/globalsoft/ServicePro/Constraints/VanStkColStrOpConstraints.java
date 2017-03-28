/*
 * VanStkColStrOpConstraints.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


public class VanStkColStrOpConstraints implements KvmSerializable, Serializable {
    
    public String BP_UNAME = "";
    public String WERKS = "";
    public String LGORT = "";
    public String LGOBE = "";
    
    public VanStkColStrOpConstraints (){}
    

    public VanStkColStrOpConstraints (String[] values) {
        BP_UNAME = values[0];
        WERKS = values[1];
        LGORT = values[2];
        LGOBE = values[3];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return BP_UNAME;
            case 1:
                return WERKS;
            case 2:
                return LGORT;
            case 3:
                return LGOBE;
        }
        
        return null;
    }

    public int getPropertyCount() {
        return 4;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BP_UNAME";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "WERKS";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LGORT";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LGOBE";
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
                WERKS = value.toString();
                break;
            case 2:
                LGORT = value.toString();
                break;
            case 3:
                LGOBE = value.toString();
                break;
        }
    }
    
    public String getEmpUsername(){
        return this.BP_UNAME;
    }//fn getEmpUsername
    
    public String getEmpPlant(){
        return this.WERKS;
    }//fn getEmpPlant
    
    public String getEmpStorageLoc(){
        return this.LGORT;
    }//fn getEmpStorageLoc
    
    public String getStorageLocDesc(){
        return this.LGOBE;
    }//fn getStorageLocDesc
    
}//End of class VanStkColStrOpConstraints 

