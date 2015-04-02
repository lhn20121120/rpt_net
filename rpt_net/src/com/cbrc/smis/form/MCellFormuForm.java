
package com.cbrc.smis.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * Form for MCellFormu.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MCellFormuForm"
 */
public class MCellFormuForm extends ActionForm {
   private java.lang.Integer _cellFormuId = null;
   private java.lang.String _cellFormu = null;
   private java.lang.Integer _formuType = null;
   /**
    * 关系表达式定义文件
    */
   private FormFile expressionFile=null;
   /**
    * 报表名称
    */
   private String reportName=null;
   
   /**
    * 子报ID
    */
   private String childRepId=null;
   private String expression=null;
   /**
    * 版本号
    */
   private String versionId=null;
   /**
    * 单元格ID
    */
   private Integer cellId=null;
   /**
    * 表间关系表达式关联的报表集
    */
   private List relationTables=null;
   /**
    * 表达式相似的单元格列表
    */
   private List sameCells=null;
   /**
    * 报表类型
    */
   private Integer reportStyle=null;
   /**
    * 单元格公式的显示公式
    */
   private String cellFormuView=null;
   
   private String reportFlg=null;
   
   /**
    * Standard constructor.
    */
   public MCellFormuForm() {
   }
   	
   /**
    * 设置报表类型
    * 
    * @param Integer reportStyle
    * @return void
    */
   public void setReportStyle(Integer reportStyle){
	   this.reportStyle=reportStyle;
   }   
   
   public void setReportStyle(String reportStyle){
	   try{
		   this.reportStyle=Integer.valueOf(reportStyle);
	   }catch(Exception e){
		   this.reportStyle=null;
	   }
   }
   
   /**
    * 获取报表类型
    * 
    * @return Integer
    */
   public Integer getReportStyle(){
	   return this.reportStyle==null || this.reportStyle==0?1:this.reportStyle;
   }
   
   public void reset(ActionMapping mapping, HttpServletRequest request) {
	   _cellFormuId = null;
	   _cellFormu = null;
	   _formuType = null;
	   expressionFile=null;
	   reportName=null;
	   childRepId=null;
	   versionId=null;
	   cellId=null;
	   cellFormuView=null;
   }
   
   /**
    * Validate the properties that have been set from this HTTP request,
    * and return an <code>ActionErrors</code> object that encapsulates any
    * validation errors that have been found.  If no errors are found, return
    * <code>null</code> or an <code>ActionErrors</code> object with no
    * recorded error messages.
    *
    * @param mapping The mapping used to select this instance
    * @param request The servlet request we are processing
    */
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
      ActionErrors errors = new ActionErrors();
      
      return errors;
   }

	public java.lang.String getCellFormu() {
		return _cellFormu;
	}
	
	public void setCellFormu(java.lang.String formu) {
		_cellFormu = formu;
	}
	
	public java.lang.Integer getCellFormuId() {
		return _cellFormuId;
	}
	
	public void setCellFormuId(java.lang.Integer formuId) {
		_cellFormuId = formuId;
	}
	
	public java.lang.Integer getFormuType() {
		return _formuType;
	}
	
	public void setFormuType(java.lang.Integer type) {
		_formuType = type;
	}
	
	public Integer getCellId() {
		return cellId;
	}
	
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}
	
	public String getChildRepId() {
		return childRepId;
	}
	
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	
	public FormFile getExpressionFile() {
		return expressionFile;
	}
	
	public void setExpressionFile(FormFile expressionFile) {
		this.expressionFile = expressionFile;
	}
	
	public String getReportName() {
		return reportName;
	}
	
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	public String getVersionId() {
		return versionId;
	}
	
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
	public void setRelationTables(List relationTables){
		this.relationTables=relationTables;
	}
	public List getRelationTables(){
		return this.relationTables;
	}
	
	public void setSameCells(List sameCells){
		this.sameCells=sameCells;
	}
	public List getSameCells(){
		return this.sameCells;
	}
	
	public String getCellFormuView() {
		return cellFormuView;
	}

	public void setCellFormuView(String cellFormuView) {
		this.cellFormuView = cellFormuView;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getReportFlg() {
		return reportFlg;
	}

	public void setReportFlg(String reportFlg) {
		this.reportFlg = reportFlg;
	}
}
