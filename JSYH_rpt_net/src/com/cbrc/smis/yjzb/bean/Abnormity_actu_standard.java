package com.cbrc.smis.yjzb.bean;
import java.io.Serializable;
/**
 * 预警指标主表实体类
 * @author jhb
 *
 */
public class Abnormity_actu_standard  implements Serializable{
	/**
	 * 实际字报表ID
	 */
	private Integer repInId;
	/**
	 * 预警指标ID
	 */
	private Integer as_id;
	/**
	 * 异常项PDF
	 */
	private String as_pdf_item;
	/**
	 * 异常项EXCEL
	 */
	private String as_excel_item;
	/**
	 * 异常变化标准
	 */
	private String as_standard;
	/**
	 * 计算公式ID
	 */
	private Integer exp_id;
	/**
	 * 栏目ID
	 */
	private Integer title_id;
	/**
	 * 上报频度
	 */
	private Integer rep_freq_id;
	
	/**
	 * 子报表ID
	 */
	private String  child_rep_id;
	/**
	 * 版本号ID
	 */
	private String  version_id;
    /**
     * 构造函数
     */
	public Abnormity_actu_standard (){}
	

	public String getChild_rep_id() {
		return child_rep_id;
	}

	public void setChild_rep_id(String child_rep_id) {
		this.child_rep_id = child_rep_id;
	}

	public String getVersion_id() {
		return version_id;
	}

	public void setVersion_id(String version_id) {
		this.version_id = version_id;
	}


	public String getAs_excel_item() {
		return as_excel_item;
	}


	public void setAs_excel_item(String as_excel_item) {
		this.as_excel_item = as_excel_item;
	}


	public Integer getAs_id() {
		return as_id;
	}


	public void setAs_id(Integer as_id) {
		this.as_id = as_id;
	}


	public String getAs_pdf_item() {
		return as_pdf_item;
	}


	public void setAs_pdf_item(String as_pdf_item) {
		this.as_pdf_item = as_pdf_item;
	}


	public String getAs_standard() {
		return as_standard;
	}


	public void setAs_standard(String as_standard) {
		this.as_standard = as_standard;
	}


	public Integer getExp_id() {
		return exp_id;
	}


	public void setExp_id(Integer exp_id) {
		this.exp_id = exp_id;
	}


	public Integer getRep_freq_id() {
		return rep_freq_id;
	}


	public void setRep_freq_id(Integer rep_freq_id) {
		this.rep_freq_id = rep_freq_id;
	}


	public Integer getTitle_id() {
		return title_id;
	}


	public void setTitle_id(Integer title_id) {
		this.title_id = title_id;
	}


	public Integer getRepInId() {
		return repInId;
	}


	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}
	
}
