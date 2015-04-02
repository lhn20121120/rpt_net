package com.gather.bean;
import java.io.Serializable;
import java.util.List;
public class ShowOrgName implements Serializable{
     
	private List nameList=null;
	
	public void setNameList(List nameList){
		this.nameList=nameList;
	}
	
	public List getNameList(){
		return this.nameList;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
