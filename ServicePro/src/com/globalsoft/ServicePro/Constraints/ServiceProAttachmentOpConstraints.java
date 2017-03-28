package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class ServiceProAttachmentOpConstraints  implements KvmSerializable, Serializable {
    
	public String OBJECT_ID = "";
    public String OBJECT_TYPE = "";
    public String NUMBER_EXT = "";
    public String ATTCHMNT_ID = "";
    public String ATTCHMNT_CNTNT = "";
    
    public ServiceProAttachmentOpConstraints (){}    

    public ServiceProAttachmentOpConstraints (String[] values) {
        OBJECT_ID = values[0];
        OBJECT_TYPE = values[1];
        NUMBER_EXT = values[2];
        ATTCHMNT_ID = values[3];
        ATTCHMNT_CNTNT = values[4];
    }

    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return OBJECT_ID;
            case 1:
                return OBJECT_TYPE;
            case 2:
                return NUMBER_EXT;
            case 3:
                return ATTCHMNT_ID; 
            case 4:
                return ATTCHMNT_CNTNT;
        }
        
        return null;
    }

    public int getPropertyCount() {
        return 5;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "OBJECT_ID";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "OBJECT_TYPE";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "NUMBER_EXT";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ATTCHMNT_ID";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ATTCHMNT_CNTNT";
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
                OBJECT_TYPE = value.toString();
                break;
            case 2:
                NUMBER_EXT = value.toString();
                break;
            case 3:
                ATTCHMNT_ID = value.toString();
                break;
            case 4:
                ATTCHMNT_CNTNT = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getObjectId(){
        return this.OBJECT_ID;
    }//fn getObjectId
    
    public String getObjectType(){
        return this.OBJECT_TYPE;
    }//fn getObjectType
    
    public String getNumberExt(){
        return this.NUMBER_EXT;
    }//fn getNumberExt
    
    public String getAttachmentId(){
        return this.ATTCHMNT_ID;
    }//fn getAttachmentId
    
    public String getAttachmentContent(){
        return this.ATTCHMNT_CNTNT;
    }//fn getAttachmentContent
                
}//End of class ServiceProAttachmentOpConstraints