/*
 * Created on 2005-5-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.smis.adapter.StrutsCalendarDelegate;
import com.cbrc.smis.adapter.StrutsEXChangeRateDelegate;
import com.cbrc.smis.adapter.StrutsListingColsDelegate;
import com.cbrc.smis.adapter.StrutsLogTypeDelegate;
import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.adapter.StrutsMRepFreqDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.adapter.StrutsMRepTypeDelegate;
import com.cbrc.smis.adapter.StrutsOATDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.Calendar;
import com.cbrc.smis.hibernate.LogType;
import com.cbrc.smis.hibernate.VCurrency;
import com.fitech.gznx.form.CodeLibForm;
import com.fitech.gznx.service.StrutsCodeLibDelegate;
import com.fitech.gznx.service.XmlTreeUtil;
import com.fitech.net.adapter.StrutsAnalysisTemplateDelegate;
import com.fitech.net.form.AnalysisTPTYPEForm;
import com.fitech.net.form.ETLReportForm;
/**
 * @author rds
 *
 * 把常用的下拉列表的值放入此Form
 */
public class UtilForm implements Serializable{
 /**
  * 日志类型列表
  */
  private List logTypes = null; 

  /**
   * 报表类型信息列表
   */
  private List repTypes = null;
  
  private List repRHTypes= null;
  
  private List repQTTypes= null;
  
  /**
   * 字典类型值列表
   * 
   */
  private List typeName = null;
  /**
   * 报表类型信息列表
   */
  private List FRrepList = null;
  /**
   * 报表模板信息列表
   */
  private List repList = null;
  /**
   * 报表模板信息列表
   */
  private List reportList = null;
  
  /**
   * 日历类型列表
   */
  private List calendarTypes =null;
 /**
  * 上报频度列表
  */
  private List repFreqs =null;
  
  /**
   * 上报频度列表
   */
   private List repFreqsaf =null;
   /**
    * 上报人行频度列表
    */
    private List repFreqsrh =null;
   
  
  /**
   * 数据范围类别列表
   */
  private List dataRgTypes = null;
  
  /**
   * 可输入单元格列表
   */
  private List inputCells=null;
  /**
   * 清单式报表的列信息列表
   */
  private List inputCols=null;
  /**
   * 货币单位信息列表
   */
  private List curUnits=null;
  /**
   * 币种信息
   */
  private List curIds=null;
  /**
   * 审核标志下拉标志
   */
  private List checkFlag=null;
  /**
   * 频度类别信息列表
   */
  private List orgActuTypes=null;
  /**
   * 币种
   * @return
   */
  private List vcurrency=null;
  
  /**
   * 分析报表类型
   * @return
   */
  private List analysisTPType=null;
  
  private String type;
  
  private String orgId =null;
  
  // 币种列表信息
  private String treeCurrContent;
  
  
  /**
   * 数据仓库报表模板信息列表
   */
  private List excelReportList = null;
  
  
  /**
   * 报表报送信息列表
   */
  private List templateTypes = null;
  
  public List getTemplateTypes() {
	  
		 ArrayList lists = new ArrayList();
		 lists.add(new LabelValueBean(com.fitech.gznx.common.Config.TEMPLATE_ANALYSIS_NAME,
				 com.fitech.gznx.common.Config.TEMPLATE_ANALYSIS));
		 lists.add(new LabelValueBean(com.fitech.gznx.common.Config.TEMPLATE_REPORT_NAME,
				 com.fitech.gznx.common.Config.TEMPLATE_REPORT));
		 lists.add(new LabelValueBean(com.fitech.gznx.common.Config.TEMPLATE_VIEW_NAME,
				 com.fitech.gznx.common.Config.TEMPLATE_VIEW));
		        
		  return lists;
}

public void setTemplateTypes(List templateTypes) {
	this.templateTypes = templateTypes;
}

public List getVcurrency() {
	 ArrayList lists=new ArrayList();
	 List list=StrutsEXChangeRateDelegate.findAllCurrency();
	        if(list!=null){
	        	 for(int i=0;i<list.size();i++){
	        		 VCurrency vc=(VCurrency)list.get(i);
	        	 	lists.add(new LabelValueBean(vc.getVccnname(),vc.getVcid()));       	 	
	        	 }
	        }
	        
	  return lists;
	        
  }
  
  public void setVcurrency(List vcurrency) {	
	  this.vcurrency = vcurrency;	
  }

  public List getCheckFlag() {
	if (this.checkFlag==null){
		try{
			
			List list=null;
			list=new ArrayList();
			list.add(new LabelValueBean("○", String.valueOf(Config.CHECK_FLAG_UN)));
			list.add(new LabelValueBean("√",String.valueOf(Config.CHECK_FLAG_OK)));
			list.add(new LabelValueBean("×",String.valueOf(Config.CHECK_FLAG_NO)));
			
			return this.checkFlag;
		}catch(Exception e){
			e.printStackTrace();
			return null;
			}
	}else{
		return this.checkFlag;
	}
  }




	public void setCheckFlag(List checkFlag) {
		this.checkFlag = checkFlag;
	}
	
	public List getCurIds() {
		if(this.curIds==null){
			try{
				List list=com.cbrc.smis.adapter.StrutsMCurrDelegate .findAll();
				
				if(list!=null && list.size()>0){
					curIds=new ArrayList();
					for(int i=0;i<list.size();i++){
						MCurrForm mCurrForm=(MCurrForm)list.get(i);
						curIds.add(new LabelValueBean(mCurrForm.getCurName(),String.valueOf(mCurrForm.getCurId())));
					}
				}
				return this.curIds;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}else{
			return this.curIds;
		}
	}
	
	public void setCurIds(List curIds) {
		this.curIds = curIds;
	}

  /**
   * 设置日志类型列表
   * 
   * @param logTypes List
   * @return void
   */
   public void setLogTypes(List logTypes){
	  this.logTypes=logTypes;
  }
  
  /**
   * 获取日志类型列表
   * 
   * @return List
   */
  public List getlogTypes(){
	   	if (this.logTypes!=null) {
	   		return logTypes;
	   	}
	   	else {
	   		ArrayList lists =  new ArrayList();
	   		
	   		List results = null;
			try {
				results = StrutsLogTypeDelegate.findAll();
				if (results!=null) {
		   			for (int i=0; i<results.size(); i++) {
		   				LogType lt = (LogType)(results.get(i));
		   				lists.add(new LabelValueBean(lt.getLogType(), lt.getLogTypeId().toString()));
		   			}
		   		}
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
	   		return lists;
	   	} 
  }
  
 /**
   * 设置报表类型信息列表
   * 
   * @param repTypes 报表类型信息列表
   * @return void
   */
  public void setRepTypes(List repTypes){
	  this.repTypes=repTypes;
  }
  
  /**
   * 获取报表类型信息列表
   *
   * @return List
   */
  public List getRepTypes(){
	  if(this.repTypes==null){	
		  List rtList=new ArrayList();
		  try{
			  List list=StrutsMRepTypeDelegate.findAll();
			  
			  if(list!=null && list.size()>0){
				  for(int i=0;i<list.size();i++){
					  MRepTypeForm mRepTypeForm=(MRepTypeForm)list.get(i);
					  rtList.add(new LabelValueBean(mRepTypeForm.getRepTypeName(),String.valueOf(mRepTypeForm.getRepTypeId())));
				  }
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			  return rtList;
		  }
		  return rtList;
	  }else{
		return this.repTypes;
	  }
  }
  
  /**
   * 设置报表模板信息列表
   * 
   * @param repList List 
   * @return void
   */
  public void setRepList(List repList){
	  this.repList=repList;
  }
  /**
   * 设置报表模板信息列表
   * 
   * @param repList List 
   * @return void
   */
  public void setReportList(List reportList){
	  this.reportList=reportList;
  }
	 
  /**
   * 获取报表模板信息列表
   * 
   * @return List
   */
  public List getRepList(){
	  if(this.repList==null){
		  List rtList=new ArrayList();;
		  
		  try{
			  List list=StrutsMChildReportDelegate.findAll();
			 if(list!=null && list.size()>0){
				  MChildReportForm reportForm=null;
				  for(int i=0;i<list.size();i++){
					  reportForm=(MChildReportForm)list.get(i);
					  rtList.add(new LabelValueBean(reportForm.getReportName(),
							  String.valueOf(reportForm.getChildRepId())));
				  }
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  return rtList;
	  }else{
		  return this.repList;
	  }
	 
  }
  /**
   * 获取报表模板+version信息列表
   * 
   * @return List
   */
  public List getReportList(){
	  if(this.reportList==null){
		  List rtList=new ArrayList();;
		  
		  try{
			  List list=StrutsMChildReportDelegate.findAllReport();
			 if(list!=null && list.size()>0){
				  MChildReportForm reportForm=null;
				  for(int i=0;i<list.size();i++){
					  reportForm=(MChildReportForm)list.get(i);
					  rtList.add(new LabelValueBean(reportForm.getReportName(),
							  String.valueOf(reportForm.getChildRepId()+"_"+reportForm.getVersionId())));
				  }
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  return rtList;
	  }else{
		  return this.repList;
	  }
	 
  }
  /**
   * 获取日期类型列表
   * 
   * @return List
   */
  public List getCalendarTypes() {
	if (this.calendarTypes!=null) {
   		return calendarTypes;
   	}
   	else {
   		ArrayList lists =  new ArrayList();
   		
   		List results = null;
		try {
			results = StrutsCalendarDelegate.findAll();
			if (results!=null) {
	   			for (int i=0; i<results.size(); i++) {
	   				Calendar calendar = (Calendar)(results.get(i));
	   				lists.add(new LabelValueBean(calendar.getCalName(),calendar.getCalId().toString()));
	   			}
	   		}
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		//// System.out.println("CalendarTypes is "+ lists.size());
   		return lists;
   	} 
}
  /**
   * 设置日期类型列表
   * 
   * @param logTypes List
   * @return void
   */
	public void setCalendarTypes(List calendarTypes) {
		this.calendarTypes = calendarTypes;
	}
	/**
	 * 上报频度列表
	 * @return
	 */
	public List getRepFreqs() {
		if (this.repFreqs!=null) {
	   		return repFreqs;
	   	}
	   	else {
	   		ArrayList lists =  new ArrayList();
	   		
	   		List results = null;
			try {
				results = StrutsMRepFreqDelegate.findAll();
				if (results!=null) {
		   			for (int i=0; i<results.size(); i++) {
		   				MRepFreqForm mRepFreqForm = (MRepFreqForm)(results.get(i));
		   				lists.add(new LabelValueBean(mRepFreqForm.getRepFreqName(),mRepFreqForm.getRepFreqId().toString()));
		   			}
		   		}
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}

	   		return lists;
	   	} 
	}
	/**
	 * 设置上报频度列表
	 * @param repFreqs
	 */
	public void setRepFreqs(List repFreqs) {
		this.repFreqs = repFreqs;
	}
	/**
	 * 数据范围类别列表
	 * @return List
	 */ 
	public List getDataRgTypes() {
		if (this.dataRgTypes!=null) {
	   		return dataRgTypes;
	   	}
	   	else {
	   		ArrayList lists =  new ArrayList();
	   		
	   		List results = null;
			try {
				results = StrutsMDataRgTypeDelegate.findAll();
				if (results!=null) {
		   			for (int i=0; i<results.size(); i++) {
		   				MDataRgTypeForm mDataRgTypeForm = (MDataRgTypeForm)(results.get(i));
		   				lists.add(new LabelValueBean(mDataRgTypeForm.getDataRgDesc(),mDataRgTypeForm.getDataRangeId().toString()));
		   			}
		   		}
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
	   		return lists;
	   	} 
	}
	/**
	 * 设置数据范围类别
	 * @param dataRgTypes
	 */
	public void setDataRgTypes(List dataRgTypes) {
		this.dataRgTypes = dataRgTypes;
	}
	
	/**
	 * 设置可输入单元格列表
	 * 
	 * @author rds 
	 * @serialData 2005-12-11 1:26
	 * 
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @reutrn void
	 */
	public void setInputCells(String childRepId,String versionId){
		try{
			List list=null;
			//点对点式可输入单元格列表
			list=StrutsMCellDelegate.getCells(childRepId,versionId);
			
			if(list!=null && list.size()>0){
				inputCells=new ArrayList();
				MCellForm mCellForm=null;
				for(int i=0;i<list.size();i++){
					mCellForm=(MCellForm)list.get(i);
					inputCells.add(new LabelValueBean(mCellForm.getCellName(),
							String.valueOf(mCellForm.getCellId())));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			this.inputCells=null;
		}
	}
	
	/**
	 * 设置可输入单元格列表
	 * 
	 * @param inputCells List
	 * @reutrn void
	 */
	public void setInputCells(List inputCells){
		this.inputCells=inputCells;
	}
	
	/**
	 * 获取可输入单元格列表
	 * 
	 * @return List
	 */
	public List getInputCells(){
		return this.inputCells==null?new ArrayList():this.inputCells;
	}
	
	/**
	 * 获取可输入单元格下接列表
	 * 
	 * @author rds
	 * @serialData 2005-12-11 1:30
	 * 
	 * @return String
	 */
	public String getInputCellsSelect(){
		StringBuffer sbSelect=new StringBuffer("<select name='colName' onchange='_selChange(this)'><option value=''></option>");
		
		if(this.inputCells!=null){
			LabelValueBean item=null;
			for(int i=0;i<this.inputCells.size();i++){
				item=(LabelValueBean)this.inputCells.get(i);
				sbSelect.append("<option value='" + item.getValue() + "'>" + item.getLabel() + "</option>");
			}
		}
		
		sbSelect.append("</select>");
		
		return sbSelect.toString();
	}
	
	/**
	 * 设置清单式报表的信息列表
	 * 
	 * @author rds 
	 * @serialData 2005-12-13 2:05
	 * 
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @reutrn void
	 */
	public void setInputCols(String childRepId,String versionId){
		try{
			List list=null;
			//清单式报表的列信息列表
			list=StrutsListingColsDelegate.findAll(childRepId,versionId);
		
			if(list!=null && list.size()>0){
				inputCols=new ArrayList();
				ListingColsForm listingColsForm=null;
				for(int i=0;i<list.size();i++){
					listingColsForm=(ListingColsForm)list.get(i);
					inputCols.add(new LabelValueBean(listingColsForm.getPdfColName(),
							listingColsForm.getDbColName()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			this.inputCols=null;
		}
	}
	
	/**
	 * 设置清单式报表的列信息列表
	 */
	public void setInputCols(List inputCols){
		this.inputCols=inputCols;
	}
	
	/**
	 * 获取清单式报表的列信息列表
	 */
	public List getInputCols(){
		return this.inputCols;
	}
	
	/**
	 * 获取清单式报表的列信息下接列表
	 * 
	 * @author rds
	 * @serialData 2005-12-11 1:30
	 * 
	 * @return String
	 */
	public String getInputColsSelect(){
		StringBuffer sbSelect=new StringBuffer("<select name='colName' onchange='_selChange(this)'><option value=''></option>");
		
		if(this.inputCols!=null){
			LabelValueBean item=null;

			for(int i=0;i<this.inputCols.size();i++){
				item=(LabelValueBean)this.inputCols.get(i);
				sbSelect.append("<option value='" + item.getValue() + "'>" + item.getLabel() + "</option>");
			}
		}
		
		sbSelect.append("</select>");

		return sbSelect.toString();
	}
	
	/**
	 * 设置货币单位信息列表
	 * 
	 * @param curUnits List 
	 * @return void
	 */
	public void setCurUnits(List curUnits){
		this.curUnits=curUnits;
	}
	
	/**
	 * 获取货币单位信息列表
	 * 
	 * @return List
	 */
	public List getCurUnits(){
		if(this.curUnits==null){
			try{
				List list=com.cbrc.smis.adapter.StrutsMCurUnitDelegate.findAll();
				
				if(list!=null && list.size()>0){
					curUnits=new ArrayList();
					for(int i=0;i<list.size();i++){
						MCurUnitForm mCurUnitForm=(MCurUnitForm)list.get(i);
					
						curUnits.add(new LabelValueBean(mCurUnitForm.getCurUnitName(),String.valueOf(mCurUnitForm.getCurUnit())));
					}
				}
				return this.curUnits;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}else{
			return this.curUnits;
		}
	}
	
   /**
	* 设置频度类别信息列表
	* 
	* @param orgActuTypes 频度类别信息列表
	* @return void
	*/
	public void setOrgActuTypes(List orgActuTypes){
		this.orgActuTypes=orgActuTypes;
	}
	
   /**
	* 获得频度类别信息列表
	* 
	* @return List
	*/
	public List getOrgActuTypes(){
		if(this.orgActuTypes==null){
			try{
				List list=StrutsOATDelegate.findAll();
				
				if(list!=null && list.size()>0){
					this.orgActuTypes=new ArrayList();
					for(int i=0;i<list.size();i++){
						OrgActuTypeForm orgActuTypeForm=(OrgActuTypeForm)list.get(i);
					
						this.orgActuTypes.add(new LabelValueBean(orgActuTypeForm.getOATName(),String.valueOf(orgActuTypeForm.getOATId())));
					}
				}
				return this.orgActuTypes;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}else{
			return this.orgActuTypes;
		}
	}

public List getAnalysisTPType()
{
	  if(this.analysisTPType==null){	
		  List rtList=new ArrayList();
		  try{
			  List list=StrutsAnalysisTemplateDelegate.findAllAnalysisTPType();
			  
			  if("view".equals(this.type)){
				  rtList.add(new LabelValueBean("全部类型","0"));
			  }
			  if(list!=null && list.size()>0){
				  for(int i=0;i<list.size();i++){
					  AnalysisTPTYPEForm analysisTPTYPEForm=(AnalysisTPTYPEForm)list.get(i);
					  rtList.add(new LabelValueBean(analysisTPTYPEForm.getAnalyTypeName(),String.valueOf(analysisTPTYPEForm.getAnalyTypeID())));
				  }
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			  return rtList;
		  }
		  return rtList;
	  }else{
		return this.analysisTPType;
	  }
}

public void setAnalysisTPType(List analysisTPType)
{
	analysisTPType = analysisTPType;
}

public String getType()
{
	return type;
}

public void setType(String type)
{
	this.type = type;
}

public List getFRrepList()
{

	  if(this.FRrepList==null){
		  List rtList=new ArrayList();;
		  
		  try{
			  	java.util.Calendar calendar = java.util.Calendar.getInstance();		
				Integer year =new Integer(calendar.get(java.util.Calendar.YEAR));		   
				Integer term= new Integer(calendar.get(java.util.Calendar.MONTH)+1); 
				String versionId= StrutsMRepRangeDelegate.getFRVersionId(year.toString(),term.toString(),this.orgId);
		         
			  List list=StrutsMChildReportDelegate.findAllFR(versionId);
			 if(list!=null && list.size()>0){
				  MChildReportForm reportForm=null;
				  rtList.add(new LabelValueBean("请选择报表","-1"));
				  for(int i=0;i<list.size();i++){
					  reportForm=(MChildReportForm)list.get(i);
					  rtList.add(new LabelValueBean(reportForm.getReportName(),
							  String.valueOf(reportForm.getChildRepId())));
				  }
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  return rtList;
	  }else{
		  return this.FRrepList;
	  }
	 

}

public void setFRrepList(List rrepList)
{
	FRrepList = rrepList;
}

public String getOrgId()
{
	return orgId;
}

public void setOrgId(String orgId)
{
	this.orgId = orgId;
}

public List getExcelReportList()
{
	if(this.excelReportList==null){
		try{
			List list = StrutsMChildReportDelegate.findReportExcel();
			if(list!=null && list.size()>0){
				excelReportList=new ArrayList();
				for(int i=0;i<list.size();i++){
					ETLReportForm etlReportForm=(ETLReportForm)list.get(i);
					excelReportList.add(new LabelValueBean(etlReportForm.getExcelId()+"-"+etlReportForm.getReportName(),etlReportForm.getExcelId()));
				}
			}
			return this.excelReportList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}else{
		return this.excelReportList;
	}
 
}

public void setExcelReportList(List excelReportList)
{
	this.excelReportList = excelReportList;
}

public List getRepRHTypes() {
	  if(this.repRHTypes==null){	
		  List rtList=null;
		  try{
			  rtList=StrutsCodeLibDelegate.getRepTypes(Config.RHTEMPLATE_TYPE);
			  
			  
		  }catch(Exception e){
			  e.printStackTrace();
			  return rtList;
		  }
		  return rtList;
	  }else{
		return this.repRHTypes;
	  }
	
}

public void setRepRHTypes(List repRHTypes) {
	this.repRHTypes = repRHTypes;
}

public List getRepQTTypes() {

	  if(this.repQTTypes==null){	
		  List rtList=null;
		  try{
			  rtList=StrutsCodeLibDelegate.getRepTypes(Config.QTTEMPLATE_TYPE);
			  
			  
		  }catch(Exception e){
			  e.printStackTrace();
			  return rtList;
		  }
		  return rtList;
	  }else{
		return this.repQTTypes;
	  }

}

public void setRepQTTypes(List repQTTypes) {
	this.repQTTypes = repQTTypes;
}

public List getRepFreqsaf() {
	if (this.repFreqsaf!=null) {
   		return repFreqsaf;
   	}
   	else {  		
   		
   		List results = null;
		try {
			results = StrutsMRepFreqDelegate.findAllFeq();
			
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}

   		return results;
   	} 
}

public void setRepFreqsaf(List repFreqsaf) {
	this.repFreqsaf = repFreqsaf;
}

public String getTreeCurrContent() {
	if (this.treeCurrContent!=null) {
   		return treeCurrContent;
   	}
   	else {
   		String currTree = XmlTreeUtil.createCurrTree("TREE1_NODES",null,false);
   		return currTree;
   	}
}

public void setTreeCurrContent(String treeCurrContent) {
	this.treeCurrContent = treeCurrContent;
}

public List getTypeName() {
	
	if(this.typeName == null){
		 	List rtList=null;
			DBConn conn = null;
			Session session = null;
			
		  try{	
			  rtList = new ArrayList();
				// conn对象的实例化
				conn = new DBConn();
				// 打开连接开始会话
				session = conn.openSession();
				String hql = "select distinct c.typeName, c.id.codeType from AfCodelib c";
				
				List list = session.createQuery(hql).list();
				rtList.add(new LabelValueBean("",""));
				for(int i=0;i<list.size();i++){
					Object[] os = (Object[])list.get(i);
					rtList.add(new LabelValueBean((String.valueOf(os[0])),(String.valueOf(os[1]))));
				}
			
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  if(conn!=null)
				  conn.closeSession();
		  }
		  return rtList;				
	}else{
		return this.typeName;
	}
}

public void setTypeName(List typeName) {
	this.typeName = typeName;
}

public List getRepFreqsrh() {

	if (this.repFreqsrh!=null) {
   		return repFreqsrh;
   	}
   	else {  		
   		
   		List results = null;
		try {
			results = StrutsMRepFreqDelegate.findRHAllFeq();			
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}

   		return results;
   	} 

}

public void setRepFreqsrh(List repFreqsrh) {
	this.repFreqsrh = repFreqsrh;
}


}