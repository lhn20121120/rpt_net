package com.fitech.net.obtain.text;
import java.io.Serializable;

public class obtaintext implements Serializable {
	/** ����*/
	private String id=null;
	/**�ӱ���id*/
	private String childReportId=null;
	/** �汾��*/
	private String versionId=null;
	/**ģ������*/
	private String reportname=null;
	/** text*/
	private String dataSourceEname=null;
	/****/
	private String dataSourceCname=null;
	/** ���к�**/
	private String rowColumn=null;
	/** �ָ���**/
	private String 	splitChar=null;
	/** ��ʽ**/
	private String formula=null;
	/** ״̬**/
	private String flag=null;
	/** ����*/
	private String des=null;
	/**������**/
	private String orgId=null;
	
	
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public obtaintext(String reportname,String id,String chidReportId,String versionId,String dataSourcecEname,String dataSourceCname,String rowColumn,String splitChar,String formula,String flag,String des)
	{
		this.reportname =reportname;
		this.id =id;
		this.childReportId=chidReportId;
		this.versionId =versionId;
		this.dataSourceEname=dataSourcecEname;
		this.dataSourceCname=dataSourceCname;
		this.rowColumn=rowColumn;
		this.splitChar=splitChar;
		this.formula=formula;
		this.flag=flag;
		this.des=des;
		
	}
	
	public obtaintext()
	{
		
	}
	
	public String getDataSourceCname() {
		return dataSourceCname;
	}
	public void setDataSourceCname(String dataSourceCname) {
		this.dataSourceCname = dataSourceCname;
	}
	public String getDataSourceEname() {
		return dataSourceEname;
	}
	public void setDataSourceEname(String dataSourceEname) {
		this.dataSourceEname = dataSourceEname;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRowColumn() {
		return rowColumn;
	}
	public void setRowColumn(String rowColumn) {
		this.rowColumn = rowColumn;
	}
	public String getSplitChar() {
		return splitChar;
	}
	public void setSplitChar(String splitChar) {
		this.splitChar = splitChar;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getChildReportId() {
		return childReportId;
	}

	public void setChildReportId(String childReportId) {
		this.childReportId = childReportId;
	}

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	

	
	
	

}
