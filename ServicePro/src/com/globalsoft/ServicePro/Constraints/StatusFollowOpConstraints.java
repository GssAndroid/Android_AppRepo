package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class StatusFollowOpConstraints implements KvmSerializable, Serializable {
    
    public String PROCESS_TYPE = "";
    public String STATUS = "";
    public String STONR_NEXT = "";
    public String STATUS_NEXT = "";
    
    public StatusFollowOpConstraints (){}
    
    public StatusFollowOpConstraints (String[] values) {
        PROCESS_TYPE = values[0];
        STATUS = values[1];
        STONR_NEXT = values[2];
        STATUS_NEXT = values[3];
    }

    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return PROCESS_TYPE;
            case 1:
                return STATUS;
            case 2:
                return STONR_NEXT;
            case 3:
                return STATUS_NEXT;
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
                info.name = "PROCESS_TYPE";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "STATUS";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "STONR_NEXT";
                break;    
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "STATUS_NEXT";
                break;
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                PROCESS_TYPE = value.toString();
                break;
            case 1:
                STATUS = value.toString();
                break;
            case 2:
            	STONR_NEXT = value.toString();
                break;   
            case 3:
                STATUS_NEXT = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getProcessType(){
        return this.PROCESS_TYPE;
    }//fn getProcessType
    
    public String getStatus(){
        return this.STATUS;
    }//fn getStatus
    
    public String getStonr_next(){
        return this.STONR_NEXT;
    }//fn getStonr_next
    
    public String getStatus_next(){
        return this.STATUS_NEXT;
    }//fn getStatus_next
        
}//End of class StatusFollowOpConstraints 