/*
 * VanStkTransOpConstraints.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */
package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


public class VanStkTransOpConstraints implements KvmSerializable, Serializable {
    
    public String EBELN = "";
    public String EBELP = "";
    public String BEDAT = "";
    public String LIFNR = "";
    public String SPRAS = "";
    public String ZTERM = "";
    public String EKORG = "";
    public String WAERS = "";
    public String INCO1 = "";
    public String INCO2 = "";
    public String RESWK = "";
    public String RESLO = "";
    public String MATNR = "";
    public String TXZ01 = "";
    public String BUKRS = "";
    public String WERKS = "";
    public String LGORT = "";
    public String BEDNR = "";
    public String MATKL = "";
    public String PSTYP = "";
    public String MENGE = "";
    public String MEINS = "";
    public String NETPR = "";
    public String NETWR = "";
    public String WEMNG = "";
    public String WAMNG = "";
    
    public VanStkTransOpConstraints (){}
    

    public VanStkTransOpConstraints (String[] values) {
        EBELN = values[0];
        EBELP = values[1];
        BEDAT = values[2];
        LIFNR = values[3];
        SPRAS = values[4];
        ZTERM = values[5];
        EKORG = values[6];
        WAERS = values[7];
        INCO1 = values[8];
        INCO2 = values[9];
        RESWK = values[10];
        RESLO = values[11];
        MATNR = values[12];
        TXZ01 = values[13];
        BUKRS = values[14];
        WERKS = values[15];
        LGORT = values[16];
        BEDNR = values[17];
        MATKL = values[18];
        PSTYP = values[19];
        MENGE = values[20];
        MEINS = values[21];
        NETPR = values[22];
        NETWR = values[23];
        WEMNG = values[24];
        WAMNG = values[25];
    }


    public Object getProperty(int arg0) {        
        switch(arg0) {
            case 0:
                return EBELN;
            case 1:
                return EBELP;
            case 2:
                return BEDAT;
            case 3:
                return LIFNR; 
            case 4:
                return SPRAS;
            case 5:
                return ZTERM;
            case 6:
                return EKORG;
            case 7:
                return WAERS;
            case 8:
                return INCO1;
            case 9:
                return INCO2;
            case 10:
                return RESWK;
            case 11:
                return RESLO;
            case 12:
                return MATNR;
            case 13:
                return TXZ01;
            case 14:
                return BUKRS;
            case 15:
                return WERKS;
            case 16:
                return LGORT;
            case 17:
                return BEDNR;
            case 18:
                return MATKL;
            case 19:
                return PSTYP;
            case 20:
                return MENGE;
            case 21:
                return MEINS;
            case 22:
                return NETPR;
            case 23:
                return NETWR;
            case 24:
                return WEMNG;
            case 25:
                return WAMNG;
        }
        
        return null;
    }

    public int getPropertyCount() {
        return 26;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "EBELN";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "EBELP";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BEDAT";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LIFNR";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SPRAS";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ZTERM";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "EKORG";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "WAERS";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "INCO1";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "INCO2";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "RESWK";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "RESLO";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MATNR";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "TXZ01";
                break;
            case 14:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BUKRS";
                break;
            case 15:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "WERKS";
                break;
            case 16:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LGORT";
                break;
            case 17:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BEDNR";
                break;
            case 18:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MATKL";
                break;
            case 19:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PSTYP";
                break;
            case 20:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MENGE";
                break;
            case 21:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MEINS";
                break;
            case 22:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "NETPR";
                break;
            case 23:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "NETWR";
                break;
            case 24:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "WEMNG";
                break;   
            case 25:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "WAMNG";
                break;   
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index){
            case 0:
                EBELN = value.toString();
                break;
            case 1:
                EBELP = value.toString();
                break;
            case 2:
                BEDAT = value.toString();
                break;
            case 3:
                LIFNR = value.toString();
                break;
            case 4:
                SPRAS = value.toString();
                break;
            case 5:
                ZTERM = value.toString();
                break;
            case 6:
                EKORG = value.toString();
                break;
            case 7:
                WAERS = value.toString();
                break;
            case 8:
                INCO1 = value.toString();
                break;
            case 9:
                INCO2 = value.toString();
                break;
            case 10:
                RESWK = value.toString();
                break;
            case 11:
                RESLO = value.toString();
                break;
            case 12:
                MATNR = value.toString();
                break;
            case 13:
                TXZ01 = value.toString();
                break;
            case 14:
                BUKRS = value.toString();
                break;
            case 15:
                WERKS = value.toString();
                break;
            case 16:
                LGORT = value.toString();
                break;
            case 17:
                BEDNR = value.toString();
                break;
            case 18:
                MATKL = value.toString();
                break;
            case 19:
                PSTYP = value.toString();
                break;
            case 20:
                MENGE = value.toString();
                break;
            case 21:
                MEINS = value.toString();
                break;
            case 22:
                NETPR = value.toString();
                break;
            case 23:
                NETWR = value.toString();
                break;
            case 24:
                WEMNG = value.toString();
                break;
            case 25:
                WAMNG = value.toString();
                break;
            default:
                break;
        }
    }
    
    public String getSTO(){
        return this.EBELN;
    }//fn getSTO
    
    public String getItem(){
        return this.EBELP;
    }//fn getItem
    
    public String getSTODate(){
        return this.BEDAT;
    }//fn getSTODate
    
    public String getVendor(){
        return this.LIFNR;
    }//fn getVendor
    
    public String getLanguage(){
        return this.SPRAS;
    }//fn getLanguage
    
    public String getPymtTermsCode(){
        return this.ZTERM;
    }//fn getPymtTermsCode
    
    public String getPurchasingOrg(){
        return this.EKORG;
    }//fn getPurchasingOrg
    
    public String getCurrency(){
        return this.WAERS;
    }//fn getCurrency
    
    public String getTermPart1(){
        return this.INCO1;
    }//fn getTermPart1
    
    public String getTermPart2(){
        return this.INCO2;
    }//fn getTermPart2
    
    public String getSupplyPlant(){
        return this.RESWK;
    }//fn getSupplyPlant
    
    public String getSupplyStrLoc(){
        return this.RESLO;
    }//fn getSupplyStrLoc
    
    public String getMaterial(){
        return this.MATNR;
    }//fn getMaterial
       
    public String getSTOItemDesc(){
        return this.TXZ01;
    }//fn getSTOItemDesc
    
    public String getCOCode(){
        return this.BUKRS;
    }//fn getCOCode    
         
    public String getRecvPlant(){
        return this.WERKS;
    }//fn getRecvPlant    
     
    public String getRecvStrLoc(){
        return this.LGORT;
    }//fn getRecvStrLoc    
     
    public String getSTOTracking(){
        return this.BEDNR;
    }//fn getSTOTracking
         
    public String getMattGroup(){
        return this.MATKL;
    }//fn getMattGroup
         
    public String getPOItemCat(){
        return this.PSTYP;
    }//fn getPOItemCat
         
    public String getPurchaseQty(){
        return this.MENGE;
    }//fn getPurchaseQty    
     
    public String getPurchaseQtyUnit(){
        return this.MEINS;
    }//fn getPurchaseQtyUnit    
     
    public String getPurchasePrice(){
        return this.NETPR;
    }//fn getPurchasePrice
     
    public String getPurchaseValue(){
        return this.NETWR;
    }//fn getPurchaseValue
    
    public String getQtyGoodsRecd(){
        return this.WEMNG;
    }//fn getQtyGoodsRecd
    
    public String getQtyGoodsIssd(){
        return this.WAMNG;
    }//fn getQtyGoodsIssd
                        
}//End of class VanStkTransOpConstraints 

