package com.cbrc.smis.yjzb.bean;
import java.io.Serializable;
public class AbnormityResult  implements Serializable{
/**
 * ����ֵ
 */
	private  String current_value;
	/**
	 * ����ֵ
	 */
	private  String previou_value;
	/**
	 * ʵ���ֱ���ID
	 */
	private  Integer repInId;
	/**
	 * ���ֵ
	 */
	private  String result_value;
	/*
	 * Ԥ��ָ��ID
	 */
	private  Integer as_id;
	/**
	 * ���㹫ʽID
	 */
	
	private  Integer Exp_id;
	/**
	 * ��ԵĽ��ֵ
	 */
	private  String face_value;
	/**
	 * ״̬
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
