package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class SOServiceActListOpConstraints  implements KvmSerializable, Serializable {
    
    public String PRODUCT_ID = "";
    public String SHORT_TEXT = ""; 
    
    public SOServiceActListOpConstraints (){}
    

    public SOServiceActListOpConstraints (String[] values) {
        PRODUCT_ID = values[0];
        SHORT_TEXT = values[1];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return PRODUCT_ID;
            case 1:
                return SHORT_TEXT;
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
                info.name = "PRODUCT_ID";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SHORT_TEXT";
                break;
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                PRODUCT_ID = value.toString();
                break;
            case 1:
                SHORT_TEXT = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getProductId(){
        return this.PRODUCT_ID;
    }//fn getProductId
    
    public String getProductDesc(){
        return this.SHORT_TEXT;
    }//fn getProductDesc
    
}//End of class SOServiceActListOpConstraints 
