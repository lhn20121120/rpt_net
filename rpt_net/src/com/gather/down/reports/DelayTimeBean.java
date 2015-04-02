package com.gather.down.reports;

public class DelayTimeBean {

   private String formModelID="";   //子报表id
   private String formModelVersion="";  //版本
   private String rangeID="";    //数据范围id
   private String frequencyID="";   //频率id
   private String commonDay="";      //正常时间
   private String uncommonDay="";    //延迟时间
/**
 * @return Returns the commonDay.
 */
public String getCommonDay() {
	return commonDay;
}
/**
 * @param commonDay The commonDay to set.
 */
public void setCommonDay(String commonDay) {
	this.commonDay = commonDay;
}
/**
 * @return Returns the formModelID.
 */
public String getFormModelID() {
	return formModelID;
}
/**
 * @param formModelID The formModelID to set.
 */
public void setFormModelID(String formModelID) {
	this.formModelID = formModelID;
}
/**
 * @return Returns the formModelVersion.
 */
public String getFormModelVersion() {
	return formModelVersion;
}
/**
 * @param formModelVersion The formModelVersion to set.
 */
public void setFormModelVersion(String formModelVersion) {
	this.formModelVersion = formModelVersion;
}
/**
 * @return Returns the frequencyID.
 */
public String getFrequencyID() {
	return frequencyID;
}
/**
 * @param frequencyID The frequencyID to set.
 */
public void setFrequencyID(String frequencyID) {
	this.frequencyID = frequencyID;
}
/**
 * @return Returns the rangeID.
 */
public String getRangeID() {
	return rangeID;
}
/**
 * @param rangeID The rangeID to set.
 */
public void setRangeID(String rangeID) {
	this.rangeID = rangeID;
}
/**
 * @return Returns the uncommonDay.
 */
public String getUncommonDay() {
	return uncommonDay;
}
/**
 * @param uncommonDay The uncommonDay to set.
 */
public void setUncommonDay(String uncommonDay) {
	this.uncommonDay = uncommonDay;
}
   

}
