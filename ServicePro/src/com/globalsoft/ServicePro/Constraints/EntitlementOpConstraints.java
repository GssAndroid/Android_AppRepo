package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class EntitlementOpConstraints implements KvmSerializable, Serializable {
    
    public String ORDER_ID = "";
    public String ITEM_ID = "";
    public String ITEM_GUID = "";
    public String PROCESS_TYPE = "";
    public String PROFILE = "";
    public String PROF_DESC = "";
    public String ENT_TYPE = "";
    public String ENT_DESC = "";
    public String DISC_LABOUR = "";
    public String DISC_PARTS = "";
    public String DISC_TRAVEL = "";
    public String FREQUENCY = "";
    public String TIMEPERIOD = "";
    
    public EntitlementOpConstraints (){}
    

    public EntitlementOpConstraints (String[] values) {
        ORDER_ID = values[0];
        ITEM_ID = values[1];
        ITEM_GUID = values[2];
        PROCESS_TYPE = values[3];
        PROFILE = values[4];
        PROF_DESC = values[5];
        ENT_TYPE = values[6];
        ENT_DESC = values[7];
        DISC_LABOUR = values[8];
        DISC_PARTS = values[9];
        DISC_TRAVEL = values[10];
        FREQUENCY = values[11];
        TIMEPERIOD = values[12];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return ORDER_ID;
            case 1:
                return ITEM_ID;
            case 2:
                return ITEM_GUID;
            case 3:
                return PROCESS_TYPE;
            case 4:
                return PROFILE; 
            case 5:
                return PROF_DESC;
            case 6:
                return ENT_TYPE;
            case 7:
                return ENT_DESC;
            case 8:
                return DISC_LABOUR;
            case 9:
                return DISC_PARTS;
            case 10:
                return DISC_TRAVEL;
            case 11:
                return FREQUENCY;
            case 12:
                return TIMEPERIOD;
        }
        
        return null;
    }

    public int getPropertyCount() {
        return 13;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ORDER_ID";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ITEM_ID";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ITEM_GUID";
                break;    
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PROCESS_TYPE";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PROFILE";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PROF_DESC";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ENT_TYPE";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ENT_DESC";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DISC_LABOUR";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DISC_PARTS";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DISC_TRAVEL";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "FREQUENCY";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "TIMEPERIOD";
                break;
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                ORDER_ID = value.toString();
                break;
            case 1:
                ITEM_ID = value.toString();
                break;
            case 2:
            	ITEM_GUID = value.toString();
                break;   
            case 3:
                PROCESS_TYPE = value.toString();
                break;
            case 4:
                PROFILE = value.toString();
                break;
            case 5:
                PROF_DESC = value.toString();
                break;
            case 6:
                ENT_TYPE = value.toString();
                break;
            case 7:
                ENT_DESC = value.toString();
                break;
            case 8:
                DISC_LABOUR = value.toString();
                break;
            case 9:
                DISC_PARTS = value.toString();
                break;
            case 10:
                DISC_TRAVEL = value.toString();
                break;
            case 11:
                FREQUENCY = value.toString();
                break;
            case 12:
                TIMEPERIOD = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getOrderId(){
        return this.ORDER_ID;
    }//fn getOrderId
    
    public String getItemId(){
        return this.ITEM_ID;
    }//fn getItemId
    
    public String getItemGuid(){
        return this.ITEM_GUID;
    }//fn getItemGuid
    
    public String getProcessType(){
        return this.PROCESS_TYPE;
    }//fn getProcessType
    
    public String getProfile(){
        return this.PROFILE;
    }//fn getProfile
    
    public String getProfileDesc(){
        return this.PROF_DESC;
    }//fn getProfileDesc
    
    public String getEntType(){
        return this.ENT_TYPE;
    }//fn getEntType
    
    public String getEntDesc(){
        return this.ENT_DESC;
    }//fn getEntDesc
    
    public String getDiscLabour(){
        return this.DISC_LABOUR;
    }//fn getDiscLabour
    
    public String getDiscParts(){
        return this.DISC_PARTS;
    }//fn getDiscParts
    
    public String getDiscTravel(){
        return this.DISC_TRAVEL;
    }//fn getDiscTravel
    
    public String getFrequency(){
        return this.FREQUENCY;
    }//fn getFrequency
    
    public String getTimePeriod(){
        return this.TIMEPERIOD;
    }//fn getTimePeriod
    
}//End of class EntitlementOpConstraints 

