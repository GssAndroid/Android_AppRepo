package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class LeavesOpConstraints implements KvmSerializable, Serializable {
    
    public String USER_NAME = "";
    public String MANAGER = "";
    public String LEAVE_REQ_NUM = "";
    public String LEAVE_TYPE = "";
    public String BEGIN_DATE = "";
    public String BEGIN_TIME = "";
    public String END_DATE = "";
    public String END_TIME = "";
    public String APPLIED_DATE = "";
    public String APPLIED_TIME = "";
    public String APPROVAL_STATUS = "";
    public String APPROVAL_DATE = "";
    public String APPROVAL_TIME = "";
    
    public LeavesOpConstraints (){}
    

    public LeavesOpConstraints (String[] values) {
        USER_NAME = values[0];
        MANAGER = values[1];
        LEAVE_REQ_NUM = values[2];
        LEAVE_TYPE = values[3];
        BEGIN_DATE = values[4];
        BEGIN_TIME = values[5];
        END_DATE = values[6];
        END_TIME = values[7];
        APPLIED_DATE = values[8];
        APPLIED_TIME = values[9];
        APPROVAL_STATUS = values[10];
        APPROVAL_DATE = values[11];
        APPROVAL_TIME = values[12];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return USER_NAME;
            case 1:
                return MANAGER;
            case 2:
                return LEAVE_REQ_NUM;
            case 3:
                return LEAVE_TYPE;
            case 4:
                return BEGIN_DATE; 
            case 5:
                return BEGIN_TIME;
            case 6:
                return END_DATE;
            case 7:
                return END_TIME;
            case 8:
                return APPLIED_DATE;
            case 9:
                return APPLIED_TIME;
            case 10:
                return APPROVAL_STATUS;
            case 11:
                return APPROVAL_DATE;
            case 12:
                return APPROVAL_TIME;
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
                info.name = "USER_NAME";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MANAGER";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LEAVE_REQ_NUM";
                break;    
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LEAVE_TYPE";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BEGIN_DATE";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BEGIN_TIME";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "END_DATE";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "END_TIME";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "APPLIED_DATE";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "APPLIED_TIME";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "APPROVAL_STATUS";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "APPROVAL_DATE";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "APPROVAL_TIME";
                break;
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                USER_NAME = value.toString();
                break;
            case 1:
                MANAGER = value.toString();
                break;
            case 2:
            	LEAVE_REQ_NUM = value.toString();
                break;   
            case 3:
                LEAVE_TYPE = value.toString();
                break;
            case 4:
                BEGIN_DATE = value.toString();
                break;
            case 5:
                BEGIN_TIME = value.toString();
                break;
            case 6:
                END_DATE = value.toString();
                break;
            case 7:
                END_TIME = value.toString();
                break;
            case 8:
                APPLIED_DATE = value.toString();
                break;
            case 9:
                APPLIED_TIME = value.toString();
                break;
            case 10:
                APPROVAL_STATUS = value.toString();
                break;
            case 11:
                APPROVAL_DATE = value.toString();
                break;
            case 12:
                APPROVAL_TIME = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getUserName(){
        return this.USER_NAME;
    }//fn getUserName
    
    public String getManager(){
        return this.MANAGER;
    }//fn getManager
    
    public String getLeaveRequestNo(){
        return this.LEAVE_REQ_NUM;
    }//fn getLeaveRequestNo
    
    public String getLeaveType(){
        return this.LEAVE_TYPE;
    }//fn getLeaveType
    
    public String getBeginDate(){
        return this.BEGIN_DATE;
    }//fn getBeginDate
    
    public String getBeginTime(){
        return this.BEGIN_TIME;
    }//fn getBeginTime
    
    public String getEndDate(){
        return this.END_DATE;
    }//fn getEndDate
    
    public String getEndTime(){
        return this.END_TIME;
    }//fn getEndTime
    
    public String getAppliedDate(){
        return this.APPLIED_DATE;
    }//fn getAppliedDate
    
    public String getAppliedTime(){
        return this.APPLIED_TIME;
    }//fn getAppliedTime
    
    public String getApprovalStatus(){
        return this.APPROVAL_STATUS;
    }//fn getApprovalStatus
    
    public String getApprovalDate(){
        return this.APPROVAL_DATE;
    }//fn getApprovalDate
    
    public String getApprovalTime(){
        return this.APPROVAL_TIME;
    }//fn getApprovalTime
    
}//End of class LeavesOpConstraints 

