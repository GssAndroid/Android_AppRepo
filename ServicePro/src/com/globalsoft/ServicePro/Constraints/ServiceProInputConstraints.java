package com.globalsoft.ServicePro.Constraints;

import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class ServiceProInputConstraints implements KvmSerializable {
    
    public String Cdata;
    
    public ServiceProInputConstraints (){}
    

    public ServiceProInputConstraints (String value) {
        Cdata = value;
    }

    public Object getProperty(int arg0) {        
        switch(arg0){
        case 0:
            return Cdata;            
        }
        return null;
    }//fn getProperty
    

    public int getPropertyCount() {
        return 1;
    }//fn getPropertyCount
    

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Cdata";
                break;
            default:break;
        }
    }//fn getPropertyInfo
    

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                Cdata = value.toString();
                break;
            default:
                break;
        }
    }//fn setProperty
    
}//End of class ServiceProInputConstraints 
