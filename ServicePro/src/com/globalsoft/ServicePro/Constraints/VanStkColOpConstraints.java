/*
 * VanStkColOpConstraints.java
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


public class VanStkColOpConstraints implements KvmSerializable, Serializable {
    
    public String PARTNER = "";
    public String MC_NAME1 = "";
    public String MC_NAME2 = "";
    public String TEL_NO = "";
    public String TEL_NO2 = "";
    public String UNAME = "";
    public String PLANT = "";
    public String STORAGE_LOC = "";
    
    public VanStkColOpConstraints (){}
    

    public VanStkColOpConstraints (String[] values) {
        PARTNER = values[0];
        MC_NAME1 = values[1];
        MC_NAME2 = values[2];
        TEL_NO = values[3];
        TEL_NO2 = values[4];
        UNAME = values[5];
        PLANT = values[6];
        STORAGE_LOC = values[7];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return PARTNER;
            case 1:
                return MC_NAME1;
            case 2:
                return MC_NAME2;
            case 3:
                return TEL_NO;
            case 4:
                return TEL_NO2;
            case 5:
                return UNAME;
            case 6:
                return PLANT;
            case 7:
                return STORAGE_LOC;
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
                info.name = "PARTNER";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MC_NAME1";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MC_NAME2";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "TEL_NO";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "TEL_NO2";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "UNAME";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PLANT";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "STORAGE_LOC";
                break;
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                PARTNER = value.toString();
                break;
            case 1:
                MC_NAME1 = value.toString();
                break;
            case 2:
                MC_NAME2 = value.toString();
                break;
            case 3:
                TEL_NO = value.toString();
                break;
            case 4:
                TEL_NO2 = value.toString();
                break;
            case 5:
            	UNAME = value.toString();
                break;
            case 6:
                PLANT = value.toString();
                break;
            case 7:
                STORAGE_LOC = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getPartnerId(){
        return this.PARTNER;
    }//fn getPartnerId
    
    public String getPartnerName1(){
        return this.MC_NAME1;
    }//fn getPartnerName1
    
    public String getPartnerName2(){
        return this.MC_NAME2;
    }//fn getPartnerName2
    
    public String getTelephoneNo1(){
        return this.TEL_NO;
    }//fn getTelephoneNo1
    
    public String getTelephoneNo2(){
        return this.TEL_NO2;
    }//fn getTelephoneNo2
    
    public String getUName(){
        return this.UNAME;
    }//fn getUName
    
    public String getPlant(){
        return this.PLANT;
    }//fn getPlant
    
    public String getStorageLoc(){
        return this.STORAGE_LOC;
    }//fn getStorageLoc
    
}//End of class VanStkColOpConstraints 

