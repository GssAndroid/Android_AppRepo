package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


public class ReportOpConstraints implements KvmSerializable, Serializable {
    
    public String OBJECT_ID = "";
    public String PROCESS_TYPE = "";
    public String PROCESS_TYPE_TXT = "";
    public String SOLD_TO_PARTY_LIST = "";
    public String SOLD_TO_PARTY = "";
    public String CONTACT_PERSON_LIST = "";
    public String NET_VALUE = "";
    public String CURRENCY = "";
    public String PRIORITY_TXT = "";
    public String DESCRIPTION = "";
    public String PO_DATE_SOLD = "";
    public String CREATED_BY = "";
    public String CONCATSTATUSER = "";
    public String POSTING_DATE = "";
    public String WRK_START_DATE = "";
    public String WRK_END_DATE = "";
    public String HRS_LABOR = "";
    public String HRS_TRAVEL = "";
    public String HRS_TOTAL = "";
    public String EQUIP_NO = "";
    public String REQ_START_DT = "";
    
    public ReportOpConstraints (){}
    

    public ReportOpConstraints (String[] values) {
        OBJECT_ID = values[0];
        PROCESS_TYPE = values[1];
        PROCESS_TYPE_TXT = values[2];
        SOLD_TO_PARTY_LIST = values[3];
        SOLD_TO_PARTY = values[4];
        CONTACT_PERSON_LIST = values[5];
        NET_VALUE = values[6];
        CURRENCY = values[7];
        PRIORITY_TXT = values[8];
        DESCRIPTION = values[9];
        PO_DATE_SOLD = values[10];
        CREATED_BY = values[11];
        CONCATSTATUSER = values[12];
        POSTING_DATE = values[13];
        WRK_START_DATE = values[14];
        WRK_END_DATE = values[15];
        HRS_LABOR = values[16];
        HRS_TRAVEL = values[17];
        HRS_TOTAL = values[18];
        EQUIP_NO = values[19];
        REQ_START_DT = values[20];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return OBJECT_ID;
            case 1:
                return PROCESS_TYPE;
            case 2:
                return PROCESS_TYPE_TXT;
            case 3:
                return SOLD_TO_PARTY_LIST; 
            case 4:
                return SOLD_TO_PARTY;
            case 5:
                return CONTACT_PERSON_LIST;
            case 6:
                return NET_VALUE;
            case 7:
                return CURRENCY;
            case 8:
                return PRIORITY_TXT;
            case 9:
                return DESCRIPTION;
            case 10:
                return PO_DATE_SOLD;
            case 11:
                return CREATED_BY;
            case 12:
                return CONCATSTATUSER;
            case 13:
                return POSTING_DATE;
            case 14:
                return WRK_START_DATE;
            case 15:
                return WRK_END_DATE;
            case 16:
                return HRS_LABOR;
            case 17:
                return HRS_TRAVEL;
            case 18:
                return HRS_TOTAL;
            case 19:
                return EQUIP_NO;
            case 20:
                return REQ_START_DT;
        }
        
        return null;
    }

    public int getPropertyCount() {
        return 21;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "OBJECT_ID";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PROCESS_TYPE";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PROCESS_TYPE_TXT";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SOLD_TO_PARTY_LIST";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SOLD_TO_PARTY";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CONTACT_PERSON_LIST";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "NET_VALUE";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CURRENCY";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PRIORITY_TXT";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DESCRIPTION";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PO_DATE_SOLD";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CREATED_BY";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CONCATSTATUSER";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "POSTING_DATE";
                break;
            case 14:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "WRK_START_DATE";
                break;
            case 15:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "WRK_END_DATE";
                break;
            case 16:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "HRS_LABOR";
                break;
            case 17:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "HRS_TRAVEL";
                break;
            case 18:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "HRS_TOTAL";
                break;
            case 19:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "EQUIP_NO";
                break;
            case 20:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "REQ_START_DT";
                break;
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                OBJECT_ID = value.toString();
                break;
            case 1:
                PROCESS_TYPE = value.toString();
                break;
            case 2:
                PROCESS_TYPE_TXT = value.toString();
                break;
            case 3:
                SOLD_TO_PARTY_LIST = value.toString();
                break;
            case 4:
                SOLD_TO_PARTY = value.toString();
                break;
            case 5:
                CONTACT_PERSON_LIST = value.toString();
                break;
            case 6:
                NET_VALUE = value.toString();
                break;
            case 7:
                CURRENCY = value.toString();
                break;
            case 8:
                PRIORITY_TXT = value.toString();
                break;
            case 9:
                DESCRIPTION = value.toString();
                break;
            case 10:
                PO_DATE_SOLD = value.toString();
                break;
            case 11:
                CREATED_BY = value.toString();
                break;
            case 12:
                CONCATSTATUSER = value.toString();
                break;
            case 13:
                POSTING_DATE = value.toString();
                break;
            case 14:
                WRK_START_DATE = value.toString();
                break;
            case 15:
                WRK_END_DATE = value.toString();
                break;
            case 16:
                HRS_LABOR = value.toString();
                break;
            case 17:
                HRS_TRAVEL = value.toString();
                break;
            case 18:
                HRS_TOTAL = value.toString();
                break;
            case 19:
                EQUIP_NO = value.toString();
                break;
            case 20:
                REQ_START_DT = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getObjectId(){
        return this.OBJECT_ID;
    }//fn getObjectId
    
    public String getProcessType(){
        return this.PROCESS_TYPE;
    }//fn getProcessType
    
    public String getProcessTypeText(){
        return this.PROCESS_TYPE_TXT;
    }//fn getProcessTypeText
    
    public String getSoldToPartyList(){
        return this.SOLD_TO_PARTY_LIST;
    }//fn getSoldToPartyList
    
    public String getSoldToParty(){
        return this.SOLD_TO_PARTY;
    }//fn getSoldToParty
    
    public String getContactPersonList(){
        return this.CONTACT_PERSON_LIST;
    }//fn getContactPersonList
    
    public String getNetValue(){
        return this.NET_VALUE;
    }//fn getNetValue
    
    public String getCurrency(){
        return this.CURRENCY;
    }//fn getCurrency
    
    public String getPriorityText(){
        return this.PRIORITY_TXT;
    }//fn getPriorityText
    
    public String getDescription(){
        return this.DESCRIPTION;
    }//fn getDescription
    
    public String getPODateSold(){
        return this.PO_DATE_SOLD;
    }//fn getPODateSold
    
    public String getCreatedBy(){
        return this.CREATED_BY;
    }//fn getCreatedBy
    
    public String getConcatStatUser(){
        return this.CONCATSTATUSER;
    }//fn getConcatStatUser
       
    public String getPostingDate(){
        return this.POSTING_DATE;
    }//fn getPostingDate
    
    public String getWorkStartDate(){
        return this.WRK_START_DATE;
    }//fn getWorkStartDate    
         
    public String getWorkEndDate(){
        return this.WRK_END_DATE;
    }//fn getWorkEndDate    
     
    public String getHoursLabor(){
        return this.HRS_LABOR;
    }//fn getHoursLabor    
     
    public String getHoursTravel(){
        return this.HRS_TRAVEL;
    }//fn getHoursTravel
         
    public String getHoursTotal(){
        return this.HRS_TOTAL;
    }//fn getHoursTotal
         
    public String getEquipNo(){
        return this.EQUIP_NO;
    }//fn getEquipNo
         
    public String getReqStartDate(){
        return this.REQ_START_DT;
    }//fn getReqStartDate    
    
}//End of class ReportOpConstraints 
