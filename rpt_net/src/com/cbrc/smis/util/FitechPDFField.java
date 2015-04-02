package com.cbrc.smis.util;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 存放PDF文件中域信息类
 * 
 * @author rds
 * @date 2005-12-3
 */
class FitechPDFField implements Serializable{
	private final String OMIT_CHARACTER="[0]";
	
	/**
	 * 域的数据类型:<br>
	 * 	文本类型	1
	 */
	public static final Integer CELL_DATA_TYPE_CHAR=new Integer(1);
	/**
	 * 域的数据类型:<br>
	 * 	数字型	2
	 */
	public static final Integer CELL_DATA_TYPE_INTEGER=new Integer(2); 
	/**
	 * 域的数据类型:<br>
	 * 	日期型	3
	 */
	public static final Integer CElL_DATA_TYPE_DATE=new Integer(3);
	/**
	 * 文本域
	 */
	public static final String TYPE_TX="TX";
	/**
	 * 按钮域
	 */
	public static final String TYPE_BTN="BTN";
	
	/**
	 * 域的类型<br>
	 * 有如下类型:<br>
	 * 	<code>Tx</code>	表示文本域<br>
	 * 	<code>Btn</code> 表示按钮域<br>
	 */
	private String fieldType;
	
	/**
	 * 域在PDF文件中定义的名称
	 */
	private String fieldName;
	
	/**
	 * 域在PDF文件中定义的值
	 */
	private String fieldValue;
	
	/**
	 * 域的数据类型
	 */
	private Integer dataType;
	
	/**
	 * 是否是可写域
	 */
	private boolean readOnly;
	
	/**
	 * 域是否可见属性
	 */
	private boolean visibility;
	
	/**
	 * 构造函数
	 */
	public FitechPDFField(){}
	
	/**
	 * 构造函数
	 */
	public FitechPDFField(String fieldType,String fieldName,String fieldValue,boolean _readOnly){
		this.fieldType=fieldType;
		this.fieldName=fieldName;
		this.fieldValue=fieldValue;
		this.readOnly=_readOnly;
	}
	
	/**
	 * 获取域的名称
	 * 
	 * @return String
	 */
	public String getFieldName() {
		return format(this.fieldName);
	}

	/**
	 * 设置域的名称
	 * 
	 * @param fieldName String
	 * @return void
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 获取域的类型
	 * 
	 * @return String
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * 设置域的类型
	 * 
	 * @param fieldType String
	 * @return void
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * 获取域的内容值
	 * 
	 * @return String
	 */
	public String getFieldValue() {
		return fieldValue;
	}

	/**
	 * 设置域的内容值
	 * 
	 * @param fieldValue String
	 * @return void
	 */
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * 获取域是否可写
	 * 
	 * @return boolean
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * 设置域是否可写
	 * 
	 * @param readOnly boolean 
	 * @return void 
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/**
	 * 设置域的数据类型
	 * 
	 * @param dataType Integer
	 * @return void
	 */
	public void setDataType(Integer dataType){
		this.dataType=dataType;
	}
	
	/**
	 * 获取域的数据类型
	 * 
	 * @return Integer
	 */
	public Integer getDataType(){
		return this.dataType;
	}
	
	/**
	 * 设置域是否可见
	 * 
	 * @param visibility boolean
	 * @return void
	 */
	public void setVisibility(boolean visibility){
		this.visibility=visibility;
	}
	
	/**
	 * 判断域是否可见
	 * 
	 * @return boolean
	 */
	public boolean isVisibility(){
		return this.visibility;
	}
	
	/**
	 * 格式化域的名称<br>
	 * 当从PDF文件解析得到的域的名称，如:P1[0],而域在PDF文件中实际的名称是:P1
	 * 
	 * @param partialName String 解析得到的域的名称
	 * @return String 
	 */
	private String format(String partialName){
		if(partialName==null) return null;
		
		return partialName.indexOf(this.OMIT_CHARACTER)>=0?
				partialName.substring(0,partialName.indexOf(this.OMIT_CHARACTER)):"";
	}
	
	/**
	 * equals方法
	 * 
	 * @param object Object 另一需比较对象
	 * @return boolean 两对象相等，返回true；否则，返回false
	 */
	public boolean equals(Object object){
		if(!(object instanceof FitechPDFField)) return false;
		FitechPDFField other=(FitechPDFField)object;
		return new EqualsBuilder()
			.append(this.getFieldName(),other.getFieldName())
			.append(this.getFieldType(),other.getFieldType())
			.append(this.getFieldValue(),other.getFieldValue())
			.isEquals();
	}

	/**
	 * hashCode方法 
	 * 
	 * @return int
	 */
	public int hashCode(){
		return new HashCodeBuilder()
			.append(this.fieldName)
			.append(this.fieldType)
			.append(this.fieldValue)
			.hashCode();
	}
	
	/**
	 * toString方法
	 * 
	 * @return String
	 */
	public String toString(){
		return new ToStringBuilder(this)
			.append("fildName",this.fieldName)
			.append("fieldType",this.fieldType)
			.append("fieldValue",this.fieldValue)
			.toString();
	}
}