
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author  唐磊
 */
public class MCurUnitForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _curUnit = null;
   private java.lang.String _curUnitName = null;
   private java.lang.String _addCurUnitName = null;
   private java.lang.String _editCurUnitName = null;

   /**
    * 构造函数
    */
   public MCurUnitForm() {
   }

   /**
    * @return curUnit 保存货币单位的编号
    */
   public java.lang.Integer getCurUnit() {
      return _curUnit;
   }

   /**
    * curUnit 的set方法
    */
   public void setCurUnit(java.lang.Integer curUnit) {
      _curUnit = curUnit;
   }
   /**
    * 返回 curUnitName
    */
   public java.lang.String getCurUnitName() {
	   
	   return _curUnitName; 
   }

   /**
    * curUnitName的set方法
    */
   public void setCurUnitName(java.lang.String curUnitName) {
      _curUnitName = curUnitName;    
      //--------------------------
      //GONGMING 2008-07-25
      if(null != this._curUnitName)
          this._curUnitName = this._curUnitName.trim();
   }


   /**
    * validate方法
    * @param mapping 一个ActionMapping
    * @param request  一个HttpServletRequest
    */
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
      ActionErrors errors = new ActionErrors();
    
      return errors;
   }

public java.lang.String getAddCurUnitName() {
	return _addCurUnitName;
}

public void setAddCurUnitName(java.lang.String curUnitName) {
	_addCurUnitName = curUnitName;
}

public java.lang.String getEditCurUnitName() {
	return _editCurUnitName;
}

public void setEditCurUnitName(java.lang.String curUnitName) {
	_editCurUnitName = curUnitName;
}
}
