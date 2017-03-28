package com.globalsoft.ServicePro.Constraints;

import java.io.Serializable;
import java.util.Vector;

import com.globalsoft.ServicePro.Item.SOActivityClass;

public class ServiceProVectSerializable implements  Serializable {
	
	private Vector serVect;
	private int index;
	private SOActivityClass actvObj;
	
	public ServiceProVectSerializable(){		
	}
	
	public ServiceProVectSerializable(Vector vect){
		this.serVect = vect;
	}
	
	/*public ServiceProVectSerializable(int index_value){
		this.index = index_value;
	}
	
	public ServiceProVectSerializable(SOActivityClass actvObj_arg){
		this.actvObj = actvObj_arg;
	}*/
	
	public void setVector(Vector vect){
		this.serVect = vect;
	}//fn setVector
	
	public Vector getVector(){
		return this.serVect;
	}//fn getVector
	
	/*public void setIndex(int index_value){
		this.index = index_value;
	}//fn setIndex	
	
	public int getIndex(){
		return this.index;
	}//fn setIndex
	
	public SOActivityClass getActvObj(){
		return this.actvObj;
	}//fn getActvObj
	
	public void setActvObj(SOActivityClass actvObj_arg){
		this.actvObj = actvObj_arg;
	}//fn setActvObj*/
	
}//End of class ServiceProVectSerializable
