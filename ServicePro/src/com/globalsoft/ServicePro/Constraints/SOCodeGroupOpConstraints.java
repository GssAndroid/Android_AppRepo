package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class SOCodeGroupOpConstraints  implements KvmSerializable, Serializable {
    
    public String CODEGRUPPE = "";
    public String KURZTEXT = ""; 
    
    public SOCodeGroupOpConstraints (){}
    
    public SOCodeGroupOpConstraints (String[] values) {
        CODEGRUPPE = values[0];
        KURZTEXT = values[1];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return CODEGRUPPE;
            case 1:
                return KURZTEXT;
        }
        return null;
    }

    public int getPropertyCount() {
        return 2;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CODEGRUPPE";
                break;
            case 1:
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
                KURZTEXT = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getCodeGroup(){
        return this.CODEGRUPPE;
    }//fn getCodeGroup
    
    public String getCodeGroupDesc(){
        return this.KURZTEXT;
    }//fn getCodeGroupDesc
    
}//End of class SOCodeGroupOpConstraints

 

