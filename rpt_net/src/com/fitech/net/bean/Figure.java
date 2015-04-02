package com.fitech.net.bean;

public class Figure 
{
	   private String targetDefineName = null;
       private String currentValue=null;
       private String allWarnMessage=null;
       private String warnType=null;
	   public String getTargetDefineName() 
	   {
	        return this.targetDefineName;
	   }
	   public void setTargetDefineName(String targetDefineName) 
	   {
	        this.targetDefineName = targetDefineName;
	   }
	   public String getCurrentValue() 
	   {
	        return this.currentValue;
	   }
	   public void setCurrentValue(String currentValue) 
	   {
	        this.currentValue = currentValue;
	   }
	   public String getAllWarnMessage() 
	   {
	        return this.allWarnMessage;
	   }
	   public void setAllWarnMessage(String allWarnMessage) 
	   {
	        this.allWarnMessage = allWarnMessage;
	   }
	   public String getWarnType() 
	   {
	        return this.warnType;
	   }
	   public void setWarnType(String warnType) 
	   {
	        this.warnType = warnType;
	   }
}
