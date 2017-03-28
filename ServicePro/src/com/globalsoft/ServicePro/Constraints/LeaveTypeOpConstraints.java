package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class LeaveTypeOpConstraints implements KvmSerializable, Serializable {
    
    public String LEAVE_TYPE = "";
    public String SPRSL = "";
    public String SHORT_TEXT = "";
    
    public LeaveTypeOpConstraints (){}
    

    public LeaveTypeOpConstraints (String[] values) {
        LEAVE_TYPE = values[0];
        SPRSL = values[1];
        SHORT_TEXT = values[2];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return LEAVE_TYPE;
            case 1:
                return SPRSL;
            case 2:
                return SHORT_TEXT;
        }
        
        return null;
    }

    public int getPropertyCount() {
        return 3;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LEAVE_TYPE";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SPRSL";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SHORT_TEXT";
                break;  
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                LEAVE_TYPE = value.toString();
                break;
            case 1:
                SPRSL = value.toString();
                break;
            case 2:
            	SHORT_TEXT = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getLeaveType(){
        return this.LEAVE_TYPE;
    }//fn getLeaveType
    
    public String getLeaveCode(){
        return this.SPRSL;
    }//fn getLeaveCode
    
    public String getLeaveShortText(){
        return this.SHORT_TEXT;
    }//fn getLeaveShortText
    
}//End of class LeaveTypeOpConstraints 


