package com.cbrc.smis.yjzb.bean;
import java.io.Serializable;
public class AbnormityResult  implements Serializable{
/**
 * 本期值
 */
	private  String current_value;
	/**
	 * 上期值
	 */
	private  String previou_value;
	/**
	 * 实际字报表ID
	 */
	private  Integer repInId;
	/**
	 * 结果值
	 */
	private  String result_value;
	/*
	 * 预警指标ID
	 */
	private  Integer as_id;
	/**
	 * 计算公式ID
	 */
	
	private  Integer Exp_id;
	/**
	 * 相对的结果值
	 */
	private  String face_value;
	/**
	 * 状态
	 */
	private Integer flag;
	
	
	 public AbnormityResult(){}
	
	
	public Integer getAs_id() {
		return as_id;
	}
	public void setAs_id(Integer as_id) {
		this.as_id = as_id;
	}
	public String getCurrent_value() {
		return current_value;
	}
	public void setCurrent_value(String current_value) {
		this.current_value = current_value;
	}
	public Integer getExp_id() {
		return Exp_id;
	}
	public void setExp_id(Integer exp_id) {
		Exp_id = exp_id;
	}
	public String getPreviou_value() {
		return previou_value;
	}
	public void setPreviou_value(String previou_value) {
		this.previou_value = previou_value;
	}
	public Integer getRepInId() {
		return repInId;
	}
	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}
	public String getResult_value() {
		return result_value;
	}
	public void setResult_value(String result_value) {
		this.result_value = result_value;
	}


	public Integer getFlag() {
		return flag;
	}


	public void setFlag(Integer flag) {
		this.flag = flag;
	}


	public String getFace_value() {
		return face_value;
	}


	public void setFace_value(String face_value) {
		this.face_value = face_value;
	}

}
