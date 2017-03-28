package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class ServiceFollDocOpConstraints  implements KvmSerializable, Serializable {
    
    public String OBJECT_ID = "";
    public String PROCESS_TYPE = "";    
    public String NUMBER_EXT = "";   
    public String ERDAT = "";  
    public String STATUS = "";   
    public String TXT30 = "";  
    public String SRCDOC_OBJECT_ID = "";
    public String SRCDOC_PROCESS_TYPE = "";
    public String SRCDOC_NUMBER_EXT = "";
    
    public ServiceFollDocOpConstraints (){}
    

    public ServiceFollDocOpConstraints (String[] values) {
        OBJECT_ID = values[0];
        PROCESS_TYPE = values[1];
        NUMBER_EXT = values[2];
        ERDAT = values[3];
        STATUS = values[4];
        TXT30 = values[5];
        SRCDOC_OBJECT_ID = values[6];
        SRCDOC_PROCESS_TYPE = values[7];
        SRCDOC_NUMBER_EXT = values[8];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return OBJECT_ID;
            case 1:
                return PROCESS_TYPE;
            case 2:
                return NUMBER_EXT;
            case 3:
                return ERDAT; 
            case 4:
                return STATUS;
            case 5:
                return TXT30;
            case 6:
                return SRCDOC_OBJECT_ID;
            case 7:
                return SRCDOC_PROCESS_TYPE;
            case 8:
                return SRCDOC_NUMBER_EXT;
        }
        
        return null;
    }

    public int getPropertyCount() {
        return 9;
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
                info.name = "NUMBER_EXT";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ERDAT";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "STATUS";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "TXT30";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SRCDOC_OBJECT_ID";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SRCDOC_PROCESS_TYPE";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SRCDOC_NUMBER_EXT";
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
                NUMBER_EXT = value.toString();
                break;
            case 3:
                ERDAT = value.toString();
                break;
            case 4:
                STATUS = value.toString();
                break;
            case 5:
                TXT30 = value.toString();
                break;
            case 6:
                SRCDOC_OBJECT_ID = value.toString();
                break;
            case 7:
                SRCDOC_PROCESS_TYPE = value.toString();
                break;
            case 8:
                SRCDOC_NUMBER_EXT = value.toString();
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
    
    public String getNumberExtn(){
        return this.NUMBER_EXT;
    }//fn getNumberExtn
    
    public String getERDate(){
        return this.ERDAT;
    }//fn getERDate
    
    public String getStatus(){
        return this.STATUS;
    }//fn getStatus
    
    public String getTXT30(){
        return this.TXT30;
    }//fn getTXT30
    
    public String getSRCDocObjId(){
        return this.SRCDOC_OBJECT_ID;
    }//fn getSRCDocObjId
    
    public String getSRCDocProcessType(){
        return this.SRCDOC_PROCESS_TYPE;
    }//fn getSRCDocProcessType
    
    public String getSRCDocNumberExt(){
        return this.SRCDOC_NUMBER_EXT;
    }//fn getSRCDocNumberExt
    
}//End of class ServiceFollDocOpConstraints 
