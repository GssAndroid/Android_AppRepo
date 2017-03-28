package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


public class ServiceProOutputConstraints  implements KvmSerializable, Serializable{
    
    public String OBJECT_ID = "";
    public String PROCESS_TYPE = "";
    public String ZZKEYDATE = "";
    public String PARTNER = "";
    public String NAME_ORG1 = "";
    public String NAME_ORG2 = "";    
    public String NICKNAME = "";
    public String STRAS = "";
    public String ORT01 = "";
    public String REGIO = "";
    public String PSTLZ = "";    
    public String LAND1 = "";
    public String STATUS = "";
    public String STATUS_TXT30 = "";
    public String STATUS_REASON = "";
    public String CP1_PARTNER = "";
    public String CP1_NAME1_TEXT = "";
    public String CP1_TEL_NO = "";
    public String CP1_TEL_NO2 = "";
    public String CP2_PARTNER = "";
    public String CP2_NAME1_TEXT = "";
    public String CP2_TEL_NO = "";
    public String CP2_TEL_NO2 = "";
    public String DESCRIPTION = "";
    public String PRIORITY = "";
    public String IB_IBASE = "";
    public String IB_INSTANCE = "";
    public String SERIAL_NUMBER = "";
    public String REFOBJ_PRODUCT_ID = "";
    public String IB_DESCR = "";
    public String IB_INST_DESCR = "";
    public String REFOBJ_PRODUCT_DESCR = "";
    public String TIMEZONE_FROM = "";
    public String ZZETADATE = "";
    public String ZZETATIME = "";
    public String PROCESS_TYPE_DESCR = "";
    public String NOTEZZFIELDNOTE = "";
    public String ZZFIRSTSERVICEPRODUCT = "";
    public String ZZFIRSTSERVICEPRODUCTDESCR = "";
    public String ZZFIRSTSERVICEITEM = "";   
    
    
    public ServiceProOutputConstraints (){}
    

    public ServiceProOutputConstraints (String[] values) {
        OBJECT_ID = values[0];
        PROCESS_TYPE = values[1];
        ZZKEYDATE = values[2];
        PARTNER = values[3];
        NAME_ORG1 = values[4];
        NAME_ORG2 = values[5];
        NICKNAME = values[6];
        STRAS = values[7];
        ORT01 = values[8];
        REGIO = values[9];
        PSTLZ = values[10];        
        LAND1 = values[11];
        STATUS = values[12];
        STATUS_TXT30 = values[13];
        STATUS_REASON = values[14];
        CP1_PARTNER = values[15];
        CP1_NAME1_TEXT = values[16];
        CP1_TEL_NO = values[17];
        CP1_TEL_NO2 = values[18];
        CP2_PARTNER = values[19];
        CP2_NAME1_TEXT = values[20];
        CP2_TEL_NO = values[21];
        CP2_TEL_NO2 = values[22];
        DESCRIPTION = values[23];
        PRIORITY = values[24];
        IB_IBASE = values[25];
        IB_INSTANCE = values[26];
        SERIAL_NUMBER = values[27];
        REFOBJ_PRODUCT_ID = values[28];
        IB_DESCR = values[29];
        IB_INST_DESCR  = values[30];
        REFOBJ_PRODUCT_DESCR  = values[31];
        TIMEZONE_FROM  = values[32];
        ZZETADATE = values[33];
        ZZETATIME = values[34];
        PROCESS_TYPE_DESCR = values[35];
        NOTEZZFIELDNOTE = values[36];
        ZZFIRSTSERVICEPRODUCT = values[37];    
        ZZFIRSTSERVICEPRODUCTDESCR = values[38];
        ZZFIRSTSERVICEITEM = values[39];
        
    }

    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return OBJECT_ID;
            case 1:
                return PROCESS_TYPE;
            case 2:
                return ZZKEYDATE;
            case 3:
                return PARTNER; 
            case 4:
                return NAME_ORG1;
            case 5:
                return NAME_ORG2;
            case 6:
                return NICKNAME;
            case 7:
                return STRAS;
            case 8:
                return ORT01;
            case 9:
                return REGIO;
            case 10:
                return PSTLZ;
            case 11:
                return LAND1;
            case 12:
                return STATUS;
            case 13:
                return STATUS_TXT30;
            case 14:
                return STATUS_REASON;
            case 15:
                return CP1_PARTNER;
            case 16:
                return CP1_NAME1_TEXT;
            case 17:
                return CP1_TEL_NO;
            case 18:
                return CP1_TEL_NO2;
            case 19:
                return CP2_PARTNER;
            case 20:
                return CP2_NAME1_TEXT;
            case 21:
                return CP2_TEL_NO;
            case 22:
                return CP2_TEL_NO2;
            case 23:
                return DESCRIPTION;
            case 24:
                return PRIORITY;
            case 25:
                return IB_IBASE;
            case 26:
                return IB_INSTANCE;
            case 27:
                return SERIAL_NUMBER;
            case 28:
                return REFOBJ_PRODUCT_ID;
            case 29:
                return IB_DESCR;
            case 30:
                return IB_INST_DESCR;
            case 31:
                return REFOBJ_PRODUCT_DESCR;
            case 32:
                return TIMEZONE_FROM;
            case 33:
                return ZZETADATE;
            case 34:
                return ZZETATIME;
            case 35:
                return PROCESS_TYPE_DESCR;
            case 36:
                return NOTEZZFIELDNOTE;
            case 37:
                return ZZFIRSTSERVICEPRODUCT;
            case 38:
                return ZZFIRSTSERVICEPRODUCTDESCR;
            case 39:
                return ZZFIRSTSERVICEITEM;
            
        }        
        return null;
    }

    public int getPropertyCount() {
        return 40;
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
                info.name = "ZZKEYDATE";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PARTNER";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "NAME_ORG1";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "NAME_ORG2";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "NICKNAME";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "STRAS";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ORT01";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "REGIO";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PSTLZ";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LAND1";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "STATUS";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "STATUS_TXT30";
                break;
            case 14:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "STATUS_REASON";
                break;
            case 15:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CP1_PARTNER";
                break;
            case 16:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CP1_NAME1_TEXT";
                break;
            case 17:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CP1_TEL_NO";
                break;
            case 18:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CP1_TEL_NO2";
                break;
            case 19:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CP2_PARTNER";
                break;
            case 20:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CP2_NAME1_TEXT";
                break;
            case 21:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CP2_TEL_NO";
                break;
            case 22:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CP2_TEL_NO2";
                break;
            case 23:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DESCRIPTION";
                break;
            case 24:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PRIORITY";
                break;   
            case 25:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "IB_IBASE";
                break;   
            case 26:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "IB_INSTANCE";
                break;   
            case 27:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SERIAL_NUMBER";
                break;   
            case 28:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "REFOBJ_PRODUCT_ID";
                break;  
            case 29:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "IB_DESCR";
                break; 
            case 30:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "IB_INST_DESCR"; 
                break;
            case 31:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "REFOBJ_PRODUCT_DESCR"; 
                break;   
            case 32:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "TIMEZONE_FROM"; 
                break;       
            case 33:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ZZETADATE";
                break;
            case 34:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ZZETATIME";
                break;
            case 35:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PROCESS_TYPE_DESCR";
                break;
            case 36:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "NOTEZZFIELDNOTE";
                break;
            case 37:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ZZFIRSTSERVICEPRODUCT";
                break;
            case 38:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ZZFIRSTSERVICEPRODUCTDESCR";
                break;
            case 39:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ZZFIRSTSERVICEITEM";
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
                ZZKEYDATE = value.toString();
                break;
            case 3:
                PARTNER = value.toString();
                break;
            case 4:
                NAME_ORG1 = value.toString();
                break;
            case 5:
                NAME_ORG2 = value.toString();
                break;
            case 6:
            	NICKNAME = value.toString();
                break;
            case 7:
                STRAS = value.toString();
                break;
            case 8:
                ORT01 = value.toString();
                break;
            case 9:
                REGIO = value.toString();
                break;
            case 10:
                PSTLZ = value.toString();
                break;
            case 11:
                LAND1 = value.toString();
                break;
            case 12:
                STATUS = value.toString();
                break;
            case 13:
                STATUS_TXT30 = value.toString();
                break;
            case 14:
            	STATUS_REASON = value.toString();
                break;
            case 15:
                CP1_PARTNER = value.toString();
                break;
            case 16:
                CP1_NAME1_TEXT = value.toString();
                break;
            case 17:
                CP1_TEL_NO = value.toString();
                break;
            case 18:
                CP1_TEL_NO2 = value.toString();
                break;
            case 19:
                CP2_PARTNER = value.toString();
                break;
            case 20:
                CP2_NAME1_TEXT = value.toString();
                break;
            case 21:
                CP2_TEL_NO = value.toString();
                break;
            case 22:
                CP2_TEL_NO2 = value.toString();
                break;
            case 23:
                DESCRIPTION = value.toString();
                break;
            case 24:
                PRIORITY = value.toString();
                break;
            case 25:
                IB_IBASE = value.toString();
                break;
            case 26:
                IB_INSTANCE = value.toString();
                break;
            case 27:
                SERIAL_NUMBER = value.toString();
                break;
            case 28:
                REFOBJ_PRODUCT_ID = value.toString();
                break;
            case 29:
            	IB_DESCR = value.toString();
                break;
            case 30:
            	IB_INST_DESCR = value.toString();
                break; 
            case 31:
            	REFOBJ_PRODUCT_DESCR = value.toString();
                break;       
            case 32:
            	TIMEZONE_FROM = value.toString();
                break;       
            case 33:
            	ZZETADATE = value.toString();
                break;
            case 34:
            	ZZETATIME = value.toString();
                break;
            case 35:
            	PROCESS_TYPE_DESCR = value.toString();
                break;
            case 36:
            	NOTEZZFIELDNOTE = value.toString();
                break;
            case 37:
            	ZZFIRSTSERVICEPRODUCT = value.toString();
                break;
            case 38:
            	ZZFIRSTSERVICEPRODUCTDESCR = value.toString();
                break;
            case 39:
            	ZZFIRSTSERVICEITEM = value.toString();
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
    
    public String getZZKEeyDate(){
        return this.ZZKEYDATE;
    }//fn getStartDate
    
    public void setZZKEeyDate(String date){
        this.ZZKEYDATE = date;
    }//fn setZZKEeyDate
    
    public String getPartner(){
        return this.PARTNER;
    }//fn getPartner
    
    public String getNameOrg1(){
        return this.NAME_ORG1;
    }//fn getNameOrg1
    
    public String getNameOrg2(){
        return this.NAME_ORG2;
    }//fn getNameOrg2
    
    public String getNickName(){
        return this.NICKNAME;
    }//fn getNickName
    
    public String getCity(){
        return this.ORT01;
    }//fn getCity
    
    public String getPostalCode1(){
        return this.PSTLZ;
    }//fn getPostalCode1
    
    public String getStreet(){
        return this.STRAS;
    }//fn getStreet
    
    public String getCountryIso(){
        return this.LAND1;
    }//fn getCountryIso
    
    public String getRegion(){
        return this.REGIO;
    }//fn getRegion
       
    public String getStatus(){
        return this.STATUS;
    }//fn getStatus
    
    public void setStatus(String stus){
        this.STATUS = stus;
    }//fn getStatus
    
    public String getTxt30(){
        return this.STATUS_TXT30;
    }//fn getTxt30
    
    public String getStatusReason(){
        return this.STATUS_REASON;
    }//fn getStatusReason
    
    public String getCp1Partner(){
        return this.CP1_PARTNER;
    }//fn getCp1Partner    
     
    public String getCp1Name(){
        return this.CP1_NAME1_TEXT;
    }//fn getCp1Name    
     
    public String getCp1TelNo(){
        return this.CP1_TEL_NO;
    }//fn getCp1TelNo
         
    public String getCp1TelNo2(){
        return this.CP1_TEL_NO2;
    }//fn getCp1TelNo2
         
    public String getCp2Partner(){
        return this.CP2_PARTNER;
    }//fn getCp2Partner
         
    public String getCp2Name(){
        return this.CP2_NAME1_TEXT;
    }//fn getCp2Name    
     
    public String getCp2TelNo(){
        return this.CP2_TEL_NO;
    }//fn getCp2TelNo    
     
    public String getCp2TelNo2(){
        return this.CP2_TEL_NO2;
    }//fn getCp2TelNo2
     
    public String getDescription(){
        return this.DESCRIPTION;
    }//fn getDescription
    
    public String getPriority(){
        return this.PRIORITY;
    }//fn getPriority
    
    public String getIB_Base(){
        return this.IB_IBASE;
    }//fn getIB_Base
    
    public String getIB_Instance(){
        return this.IB_INSTANCE;
    }//fn getIB_Instance
    
    public String getSerialNumber(){
        return this.SERIAL_NUMBER;
    }//fn getSerialNumber
    
    public String getRefObjProductId(){
        return this.REFOBJ_PRODUCT_ID;
    }//fn getRefObjProductId
    
    public String getIB_Descr(){
        return this.IB_DESCR;
    }//fn getIB_Descr    
    
    public String getIB_Inst_Descr(){
        return this.IB_INST_DESCR;
    }//fn getIB_Inst_Descr 
        
    public String getRefObj_Product_Descr(){
        return this.REFOBJ_PRODUCT_DESCR;
    }//fn getRefObj_Product_Descr
    
    public String getTimeZone(){
        return this.TIMEZONE_FROM;
    }//fn getTimeZone 
    
    public String getZzetaDate(){
        return this.ZZETADATE;
    }//fn getZzetaDate
    
    public void setZzetaDate(String etadate){
        this.ZZETADATE = etadate;
    }//fn setZzetaDate
    
    public String getZzetaTime(){
        return this.ZZETATIME;
    }//fn getZzetaTime
    
    public void setZzetaTime(String etatime){
        this.ZZETATIME = etatime;
    }//fn setZzetaTime 
    
    public String getProcessTypeDescr(){
        return this.PROCESS_TYPE_DESCR;
    }//fn getProcessTypeDescr  
    
    public String getNote(){
        return this.NOTEZZFIELDNOTE;
    }//fn getNote 
    /*
    public String getFieldNote(){
        return this.ZZFIRSTSERVICEITEM;
    }//fn getNote 
*/    
    public String getZzFirstServiceProduct(){
        return this.ZZFIRSTSERVICEPRODUCT;
    }//fn getZzFirstServiceProduct 
    
    public String getZzFirstServiceProductDescr(){
        return this.ZZFIRSTSERVICEPRODUCTDESCR;
    }//fn getZzFirstServiceProductDescr
     
   
    public String getNumberExtn(){
        return this.ZZFIRSTSERVICEITEM;
    }//fn getNumberExtn 
    
}//End of class SapTasksOutputConstraints 
