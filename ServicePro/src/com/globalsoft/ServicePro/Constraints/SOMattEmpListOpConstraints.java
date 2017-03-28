package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class SOMattEmpListOpConstraints  implements KvmSerializable, Serializable {
    
    public String BP_UNAME = "";
    public String MATNR = "";
    public String MAKTX_INSYLANGU = "";
    public String VRKME = "";
    public String MEINS = "";
    
    public SOMattEmpListOpConstraints (){}
    

    public SOMattEmpListOpConstraints (String[] values) {
        BP_UNAME = values[0];
        MATNR = values[1];
        MAKTX_INSYLANGU = values[2];
        VRKME = values[3];
        MEINS = values[4];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return BP_UNAME;
            case 1:
                return MATNR;
            case 2:
                return MAKTX_INSYLANGU;
            case 3:
                return VRKME; 
            case 4:
                return MEINS;
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
                info.name = "BP_UNAME";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MATNR";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MAKTX_INSYLANGU";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "VRKME";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MEINS";
                break;
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                BP_UNAME = value.toString();
                break;
            case 1:
                MATNR = value.toString();
                break;
            case 2:
                MAKTX_INSYLANGU = value.toString();
                break;
            case 3:
                VRKME = value.toString();
                break;
            case 4:
                MEINS = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getBPName(){
        return this.BP_UNAME;
    }//fn getBPName
    
    public String getMaterial(){
        return this.MATNR;
    }//fn getMaterial
    
    public String getMaterialDesc(){
        return this.MAKTX_INSYLANGU;
    }//fn getMaterialDesc
    
    public String getMaterialName(){
        return this.VRKME;
    }//fn getMaterialPrice
    
    public String getMaterialUnit(){
        return this.MEINS;
    }//fn getPriceUnits
    
}//End of class SOMattEmpListOpConstraints 


