package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class SOCodeListOpConstraints  implements KvmSerializable, Serializable {
    
    public String CODEGRUPPE = "";
    public String CODE = ""; 
    public String KURZTEXT = ""; 
    
    public SOCodeListOpConstraints (){}
    
    public SOCodeListOpConstraints (String[] values) {
        CODEGRUPPE = values[0];
        CODE = values[1];
        KURZTEXT = values[2];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return CODEGRUPPE;
            case 1:
                return CODE;
            case 2:
                return KURZTEXT;
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
                info.name = "CODEGRUPPE";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CODE";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "KURZTEXT";
                break;
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                CODEGRUPPE = value.toString();
                break;
            case 1:
                CODE = value.toString();
                break;
            case 2:
                KURZTEXT = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getCodeGroup(){
        return this.CODEGRUPPE;
    }//fn getCodeGroup
    
    public String getCode(){
        return this.CODE;
    }//fn getCode
    
    public String getCodeGroupDesc(){
        return this.KURZTEXT;
    }//fn getCodeGroupDesc
    
}//End of class SOCodeListOpConstraints 


