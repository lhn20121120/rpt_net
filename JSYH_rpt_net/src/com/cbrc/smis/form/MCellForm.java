
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MCell.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MCellForm"
 */
public class MCellForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _cellId = null;
   private java.lang.String _cellName = null;
   private java.lang.Integer _dataType = null;
   private java.lang.Integer _rowId = null;
   private java.lang.String _colId = null;
   private java.lang.String _childRepId = null;
   private java.lang.String _versionId = null;
   private Integer _collectType=null;   //Ìí¼Ózyl
   public Integer getCollectType() {
	return _collectType;
}

public void setCollectType(Integer type) {
	_collectType = type;
}

/**
    * Standard constructor.
    */
   public MCellForm() {
   }

   /**
    * Returns the cellId
    *
    * @return the cellId
    */
   public java.lang.Integer getCellId() {
      return _cellId;
   }

   /**
    * Sets the cellId
    *
    * @param cellId the new cellId value
    */
   public void setCellId(java.lang.Integer cellId) {
      _cellId = cellId;
   }
   /**
    * Returns the cellName
    *
    * @return the cellName
    */
   public java.lang.String getCellName() {
      return _cellName;
   }

   /**
    * Sets the cellName
    *
    * @param cellName the new cellName value
    */
   public void setCellName(java.lang.String cellName) {
      _cellName = cellName;
   }
   /**
    * Returns the dataType
    *
    * @return the dataType
    */
   public java.lang.Integer getDataType() {
      return _dataType;
   }

   /**
    * Sets the dataType
    *
    * @param dataType the new dataType value
    */
   public void setDataType(java.lang.Integer dataType) {
      _dataType = dataType;
   }
   /**
    * Returns the rowId
    *
    * @return the rowId
    */
   public java.lang.Integer getRowId() {
      return _rowId;
   }

   /**
    * Sets the rowId
    *
    * @param rowId the new rowId value
    */
   public void setRowId(java.lang.Integer rowId) {
      _rowId = rowId;
   }
   /**
    * Returns the colId
    *
    * @return the colId
    */
   public java.lang.String getColId() {
      return _colId;
   }

   /**
    * Sets the colId
    *
    * @param colId the new colId value
    */
   public void setColId(java.lang.String colId) {
      _colId = colId;
   }
   /**
    * Returns the childRepId
    *
    * @return the childRepId
    */
   public java.lang.String getChildRepId() {
      return _childRepId;
   }

   /**
    * Sets the childRepId
    *
    * @param childRepId the new childRepId value
    */
   public void setChildRepId(java.lang.String childRepId) {
      _childRepId = childRepId;
   }
   /**
    * Returns the versionId
    *
    * @return the versionId
    */
   public java.lang.String getVersionId() {
      return _versionId;
   }

   /**
    * Sets the versionId
    *
    * @param versionId the new versionId value
    */
   public void setVersionId(java.lang.String versionId) {
      _versionId = versionId;
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
  
}
